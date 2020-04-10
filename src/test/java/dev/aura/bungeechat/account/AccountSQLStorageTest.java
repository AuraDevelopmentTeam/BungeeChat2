package dev.aura.bungeechat.account;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.google.common.collect.ImmutableMap;
import dev.aura.bungeechat.TestDatabase;
import dev.aura.bungeechat.api.account.AccountInfo;
import dev.aura.bungeechat.api.account.BungeeChatAccount;
import dev.aura.bungeechat.api.account.BungeeChatAccountStorage;
import dev.aura.bungeechat.api.enums.ChannelType;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

@SuppressFBWarnings(value = "DMI_CONSTANT_DB_PASSWORD", justification = "Hardcoding for tests.")
public class AccountSQLStorageTest {
  private static final String database = "test";
  private static final String password = "test";
  private static final String username = "test";
  private static final String tablePrefix = "bungeechat_";
  private static final ImmutableMap<String, String> defaultOptions =
      ImmutableMap.<String, String>builder()
          .put("useUnicode", "true")
          .put("characterEncoding", "utf8")
          .build();

  private Connection connection;

  @BeforeClass
  public static void setUpBeforeClass() {
    TestDatabase.startDatabase();
  }

  @AfterClass
  public static void tearDownAfterClass() {
    TestDatabase.stopDatabase();
  }

  @Before
  public void setUp() throws SQLException {
    connection = TestDatabase.getDatabaseInstance();
  }

  @After
  public void tearDown() throws Exception {
    TestDatabase.closeDatabaseInstance(connection);
  }

  @Test
  public void optionsMapToStringTest() {
    final Map<String, String> modifiedOptions = new LinkedHashMap<>(defaultOptions);
    modifiedOptions.put("connectTimeout", "10");

    assertEquals(
        "connectTimeout=0&socketTimeout=0&autoReconnect=true&useUnicode=true&characterEncoding=utf8",
        AccountSQLStorage.optionsMapToString(defaultOptions));
    assertEquals(
        "connectTimeout=10&socketTimeout=0&autoReconnect=true&useUnicode=true&characterEncoding=utf8",
        AccountSQLStorage.optionsMapToString(modifiedOptions));
  }

  @Test
  public void connectionTest() {
    try {
      new AccountSQLStorage(
          "localhost",
          TestDatabase.getPort(),
          database,
          username,
          password,
          tablePrefix,
          defaultOptions);
    } catch (SQLException e) {
      fail("No SQL exception expected: " + e.getLocalizedMessage());
    }
  }

  @Test(expected = SQLException.class)
  public void exceptionsTest() throws SQLException {
    new AccountSQLStorage(
        "example.com",
        1,
        "example.com",
        "example.com",
        "example.com",
        "",
        "connectTimeout=10&socketTimeout=10");
  }

  @Test
  public void loadAndSaveTest() {
    try {
      BungeeChatAccountStorage accountStorage =
          new AccountSQLStorage(
              "localhost",
              TestDatabase.getPort(),
              database,
              username,
              password,
              tablePrefix,
              defaultOptions);
      UUID testUUID = UUID.randomUUID();

      AccountInfo accountInfo = accountStorage.load(testUUID);
      BungeeChatAccount account = accountInfo.getAccount();

      assertTrue("Should be new account", accountInfo.isNewAccount());

      account.setChannelType(ChannelType.HELP);
      account.addIgnore(account);

      accountStorage.save(account);

      AccountInfo accountInfo2 = accountStorage.load(testUUID);
      BungeeChatAccount account2 = accountInfo.getAccount();

      assertFalse("Should not be new account", accountInfo2.isNewAccount());
      assertEquals(
          "Should be same channel type", account.getChannelType(), account2.getChannelType());
      assertTrue("Should ignore itself", account2.hasIgnored(testUUID));
    } catch (SQLException e) {
      fail("No SQL exception expected: " + e.getLocalizedMessage());
    }
  }
}
