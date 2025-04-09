package dev.aura.bungeechat.account;

import com.google.common.annotations.VisibleForTesting;
import dev.aura.bungeechat.api.account.AccountInfo;
import dev.aura.bungeechat.api.account.BungeeChatAccount;
import dev.aura.bungeechat.api.account.BungeeChatAccountStorage;
import dev.aura.bungeechat.api.enums.ChannelType;
import dev.aura.bungeechat.util.LoggerHelper;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;
import lombok.Cleanup;
import lombok.SneakyThrows;

public class AccountSQLStorage implements BungeeChatAccountStorage {
  private final String tablePrefix;
  private final String connectionString;
  private final String username;
  private final String password;
  @VisibleForTesting Connection connection;

  private final String tableAccounts;
  private final String tableAccountsColumnUUID;
  private final String tableAccountsColumnUserName;
  private final String tableAccountsColumnChannelType;
  private final String tableAccountsColumnVanished;
  private final String tableAccountsColumnMessenger;
  private final String tableAccountsColumnSocialSpy;
  private final String tableAccountsColumnLocalSpy;
  private final String tableAccountsColumnMutedUntil;
  private final String tableAccountsColumnStoredPrefix;
  private final String tableAccountsColumnStoredSuffix;
  private final String tableIgnores;
  private final String tableIgnoresColumnUser;
  private final String tableIgnoresColumnIgnores;

  private PreparedStatement saveAccount = null;
  private PreparedStatement loadAccount = null;
  private PreparedStatement deleteIgnores = null;
  private PreparedStatement addIgnore = null;
  private PreparedStatement getIgnores = null;

  private static byte[] getBytesFromUUID(UUID uuid) {
    ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
    bb.putLong(uuid.getMostSignificantBits());
    bb.putLong(uuid.getLeastSignificantBits());

    return bb.array();
  }

  private static UUID getUUIDFromBytes(byte[] bytes) {
    ByteBuffer bb = ByteBuffer.wrap(bytes);
    long mostSigBits = bb.getLong();
    long leastSigBits = bb.getLong();

    return new UUID(mostSigBits, leastSigBits);
  }

  @VisibleForTesting
  static String optionsMapToString(Map<String, String> options) {
    final Map<String, String> allOptions = new LinkedHashMap<>();
    allOptions.put("connectTimeout", "0");
    allOptions.put("socketTimeout", "0");
    allOptions.putAll(options);

    return allOptions.entrySet().stream()
        .map(entry -> urlEncode(entry.getKey()) + '=' + urlEncode(entry.getValue()))
        .collect(Collectors.joining("&"));
  }

  @SneakyThrows(UnsupportedEncodingException.class)
  private static String urlEncode(String message) {
    return URLEncoder.encode(message, StandardCharsets.UTF_8.name());
  }

  public AccountSQLStorage(
      String ip,
      int port,
      String database,
      String username,
      String password,
      String tablePrefix,
      Map<String, String> options)
      throws SQLException {
    this(ip, port, database, username, password, tablePrefix, optionsMapToString(options));
  }

  public AccountSQLStorage(
      String ip,
      int port,
      String database,
      String username,
      String password,
      String tablePrefix,
      String options)
      throws SQLException {
    this.tablePrefix = tablePrefix;

    tableAccounts = getTableName("Accounts");
    tableAccountsColumnUUID = "UUID";
    tableAccountsColumnUserName = "UserName";
    tableAccountsColumnChannelType = "ChannelType";
    tableAccountsColumnVanished = "Vanished";
    tableAccountsColumnMessenger = "Messenger";
    tableAccountsColumnSocialSpy = "SocialSpy";
    tableAccountsColumnLocalSpy = "LocalSpy";
    tableAccountsColumnMutedUntil = "MutedUntil";
    tableAccountsColumnStoredPrefix = "StoredPrefix";
    tableAccountsColumnStoredSuffix = "StoredSuffix";
    tableIgnores = getTableName("Ignores");
    tableIgnoresColumnUser = "User";
    tableIgnoresColumnIgnores = "Ignores";

    connectionString =
        "jdbc:mysql://"
            + ip
            + ":"
            + port
            + "/"
            + database
            + (options.isEmpty() ? "" : ('?' + options));
    this.username = username;
    this.password = password;

    connect();

    prepareTables();
    prepareStatements();
  }

  @Override
  public void save(BungeeChatAccount account) {
    try {
      ensureConnection();

      byte[] uuidBytes = getBytesFromUUID(account.getUniqueId());

      // deleteIgnores
      deleteIgnores.setBytes(1, uuidBytes);

      deleteIgnores.execute();
      deleteIgnores.clearParameters();

      // saveAccount
      saveAccount.setBytes(1, uuidBytes);
      saveAccount.setString(2, account.getName());
      saveAccount.setString(3, account.getChannelType().name());
      saveAccount.setBoolean(4, account.isVanished());
      saveAccount.setBoolean(5, account.hasMessengerEnabled());
      saveAccount.setBoolean(6, account.hasSocialSpyEnabled());
      saveAccount.setBoolean(7, account.hasLocalSpyEnabled());
      saveAccount.setTimestamp(8, account.getMutedUntil());
      saveAccount.setString(9, account.getStoredPrefix().orElse(null));
      saveAccount.setString(10, account.getStoredSuffix().orElse(null));

      saveAccount.executeUpdate();
      saveAccount.clearParameters();

      // addIgnore
      addIgnore.setBytes(1, uuidBytes);

      for (UUID uuid : account.getIgnored()) {
        addIgnore.setBytes(2, getBytesFromUUID(uuid));

        addIgnore.executeUpdate();
      }

      addIgnore.clearParameters();
    } catch (SQLException e) {
      LoggerHelper.error("Could not save user " + account.getUniqueId() + " to database!", e);
    }
  }

  @Override
  public AccountInfo load(UUID uuid) {
    try {
      ensureConnection();

      byte[] uuidBytes = getBytesFromUUID(uuid);

      // loadAccount
      loadAccount.setBytes(1, uuidBytes);

      try (ResultSet resultLoadAccount = loadAccount.executeQuery()) {
        loadAccount.clearParameters();

        if (!resultLoadAccount.next()) return new AccountInfo(new Account(uuid), true, true);

        // getIgnores
        getIgnores.setBytes(1, uuidBytes);

        try (ResultSet resultGetIgnores = getIgnores.executeQuery()) {
          getIgnores.clearParameters();

          BlockingQueue<UUID> ignores = new LinkedBlockingQueue<>();

          while (resultGetIgnores.next()) {
            ignores.add(getUUIDFromBytes(resultGetIgnores.getBytes(tableIgnoresColumnIgnores)));
          }

          return new AccountInfo(
              new Account(
                  uuid,
                  ChannelType.valueOf(resultLoadAccount.getString(tableAccountsColumnChannelType)),
                  resultLoadAccount.getBoolean(tableAccountsColumnVanished),
                  resultLoadAccount.getBoolean(tableAccountsColumnMessenger),
                  resultLoadAccount.getBoolean(tableAccountsColumnSocialSpy),
                  resultLoadAccount.getBoolean(tableAccountsColumnLocalSpy),
                  ignores,
                  resultLoadAccount.getTimestamp(tableAccountsColumnMutedUntil),
                  Optional.ofNullable(resultLoadAccount.getString(tableAccountsColumnStoredPrefix)),
                  Optional.ofNullable(
                      resultLoadAccount.getString(tableAccountsColumnStoredSuffix))),
              true,
              false);
        }
      }
    } catch (SQLException e) {
      LoggerHelper.error("Could not load user " + uuid + " from database!", e);

      return new AccountInfo(new Account(uuid), true, true);
    }
  }

  @Override
  public boolean requiresConsoleAccountSave() {
    return true;
  }

  private Statement getStatement() throws SQLException {
    ensureConnection();

    return connection.createStatement();
  }

  private PreparedStatement getPreparedStatement(final String statement) throws SQLException {
    // Intentionally not calling `ensureConnection()` to prevent endless loops

    return connection.prepareStatement(statement);
  }

  @SuppressWarnings("unused")
  private ResultSet executeQuery(final String query) throws SQLException {
    @Cleanup Statement statement = getStatement();

    return statement.executeQuery(query);
  }

  @SuppressWarnings("UnusedReturnValue")
  private boolean executeStatement(final String query) throws SQLException {
    @Cleanup Statement statement = getStatement();

    return statement.execute(query);
  }

  @SuppressWarnings("unused")
  private int executeUpdate(final String query) throws SQLException {
    @Cleanup Statement statement = getStatement();

    return statement.executeUpdate(query);
  }

  private String getTableName(String baseName) {
    String name = tablePrefix + baseName;
    name = '`' + name.replaceAll("`", "``") + '`';

    return name;
  }

  private synchronized void connect() throws SQLException {
    connection = DriverManager.getConnection(connectionString, username, password);
  }

  private synchronized void ensureConnection() throws SQLException {
    if ((connection != null) && !connection.isClosed() && connection.isValid(10)) return;

    LoggerHelper.info("Connection inactive – reinitializing connection and prepared statements.");

    closePreparedStatements();

    if (connection != null && !connection.isClosed()) {
      connection.close();
    }

    connect();
    prepareStatements();
  }

  @SuppressFBWarnings(
      value = "SQL_NONCONSTANT_STRING_PASSED_TO_EXECUTE",
      justification = "Names are variable.")
  private void prepareTables() {
    try {
      final String channelTypeEnum =
          Arrays.stream(ChannelType.values())
              .map(ChannelType::name)
              .collect(Collectors.joining("','", " ENUM('", "')"));

      final String createAccountsTable =
          "CREATE TABLE IF NOT EXISTS "
              + tableAccounts
              + " ("
              + tableAccountsColumnUUID
              + " BINARY(16) NOT NULL, "
              + tableAccountsColumnUserName
              + " VARCHAR(16) NOT NULL, "
              + tableAccountsColumnChannelType
              + channelTypeEnum
              + " NOT NULL, "
              + tableAccountsColumnVanished
              + " BOOLEAN NOT NULL, "
              + tableAccountsColumnMessenger
              + " BOOLEAN NOT NULL, "
              + tableAccountsColumnSocialSpy
              + " BOOLEAN NOT NULL, "
              + tableAccountsColumnLocalSpy
              + " BOOLEAN NOT NULL, "
              + tableAccountsColumnMutedUntil
              + " DATETIME NOT NULL, "
              + tableAccountsColumnStoredPrefix
              + " TEXT, "
              + tableAccountsColumnStoredSuffix
              + " TEXT, PRIMARY KEY ("
              + tableAccountsColumnUUID
              + ")) DEFAULT CHARSET=utf8";
      final String createIgnoresTable =
          "CREATE TABLE IF NOT EXISTS "
              + tableIgnores
              + " ("
              + tableIgnoresColumnUser
              + " BINARY(16) NOT NULL, "
              + tableIgnoresColumnIgnores
              + " BINARY(16) NOT NULL, PRIMARY KEY ("
              + tableIgnoresColumnUser
              + ", "
              + tableIgnoresColumnIgnores
              + "), KEY ("
              + tableIgnoresColumnUser
              + "), KEY ("
              + tableIgnoresColumnIgnores
              + "), CONSTRAINT FOREIGN KEY ("
              + tableIgnoresColumnUser
              + ") REFERENCES "
              + tableAccounts
              + " ("
              + tableAccountsColumnUUID
              + "), CONSTRAINT FOREIGN KEY ("
              + tableIgnoresColumnIgnores
              + ") REFERENCES "
              + tableAccounts
              + " ("
              + tableAccountsColumnUUID
              + ")) DEFAULT CHARSET=utf8";

      executeStatement(createAccountsTable);
      executeStatement(createIgnoresTable);
    } catch (SQLException e) {
      LoggerHelper.error("Could not create tables!", e);
    }
  }

  private PreparedStatement safeClose(PreparedStatement stmt) {
    if (stmt != null) {
      try {
        stmt.close();
      } catch (SQLException e) {
        // Ignore
      }
    }

    return null;
  }

  private void closePreparedStatements() {
    saveAccount = safeClose(saveAccount);
    loadAccount = safeClose(loadAccount);
    deleteIgnores = safeClose(deleteIgnores);
    addIgnore = safeClose(addIgnore);
    getIgnores = safeClose(getIgnores);
  }

  @SuppressFBWarnings(
      value = "SQL_PREPARED_STATEMENT_GENERATED_FROM_NONCONSTANT_STRING",
      justification = "Names are variable.")
  private void prepareStatements() {
    try {
      final String saveAccountStr =
          "INSERT INTO "
              + tableAccounts
              + " ("
              + tableAccountsColumnUUID
              + ", "
              + tableAccountsColumnUserName
              + ", "
              + tableAccountsColumnChannelType
              + ", "
              + tableAccountsColumnVanished
              + ", "
              + tableAccountsColumnMessenger
              + ", "
              + tableAccountsColumnSocialSpy
              + ", "
              + tableAccountsColumnLocalSpy
              + ", "
              + tableAccountsColumnMutedUntil
              + ", "
              + tableAccountsColumnStoredPrefix
              + ", "
              + tableAccountsColumnStoredSuffix
              + ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE "
              + tableAccountsColumnUserName
              + " = VALUES("
              + tableAccountsColumnUserName
              + "), "
              + tableAccountsColumnChannelType
              + " = VALUES("
              + tableAccountsColumnChannelType
              + "), "
              + tableAccountsColumnVanished
              + " = VALUES("
              + tableAccountsColumnVanished
              + "), "
              + tableAccountsColumnMessenger
              + " = VALUES("
              + tableAccountsColumnMessenger
              + "), "
              + tableAccountsColumnSocialSpy
              + " = VALUES("
              + tableAccountsColumnSocialSpy
              + "), "
              + tableAccountsColumnLocalSpy
              + " = VALUES("
              + tableAccountsColumnLocalSpy
              + "), "
              + tableAccountsColumnMutedUntil
              + " = VALUES("
              + tableAccountsColumnMutedUntil
              + "), "
              + tableAccountsColumnStoredPrefix
              + " = VALUES("
              + tableAccountsColumnStoredPrefix
              + "), "
              + tableAccountsColumnStoredSuffix
              + " = VALUES("
              + tableAccountsColumnStoredSuffix
              + ")";
      final String loadAccountStr =
          "SELECT "
              + tableAccountsColumnChannelType
              + ", "
              + tableAccountsColumnVanished
              + ", "
              + tableAccountsColumnMessenger
              + ", "
              + tableAccountsColumnSocialSpy
              + ", "
              + tableAccountsColumnLocalSpy
              + ", "
              + tableAccountsColumnMutedUntil
              + ", "
              + tableAccountsColumnStoredPrefix
              + ", "
              + tableAccountsColumnStoredSuffix
              + " FROM "
              + tableAccounts
              + " WHERE "
              + tableAccountsColumnUUID
              + " = ? LIMIT 1";
      final String deleteIgnoresStr =
          "DELETE FROM " + tableIgnores + " WHERE " + tableIgnoresColumnUser + " = ?";
      final String addIgnoreStr =
          "INSERT INTO "
              + tableIgnores
              + " ("
              + tableIgnoresColumnUser
              + ", "
              + tableIgnoresColumnIgnores
              + ") VALUES (?, ?)";
      final String getIgnoresStr =
          "SELECT "
              + tableIgnoresColumnIgnores
              + " FROM "
              + tableIgnores
              + " WHERE "
              + tableIgnoresColumnUser
              + " = ? ";

      saveAccount = getPreparedStatement(saveAccountStr);
      loadAccount = getPreparedStatement(loadAccountStr);
      deleteIgnores = getPreparedStatement(deleteIgnoresStr);
      addIgnore = getPreparedStatement(addIgnoreStr);
      getIgnores = getPreparedStatement(getIgnoresStr);
    } catch (SQLException e) {
      LoggerHelper.error("Could not prepare statements!", e);
    }
  }
}
