package dev.aura.bungeechat.test.account;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.junit.Assume.assumeTrue;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;
import java.util.UUID;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import dev.aura.bungeechat.account.AccountSQLStorage;
import dev.aura.bungeechat.api.account.AccountInfo;
import dev.aura.bungeechat.api.account.BungeeChatAccount;
import dev.aura.bungeechat.api.account.BungeeChatAccountStorage;
import dev.aura.bungeechat.api.enums.ChannelType;

public class AccountSQLStorageTest {
    private static Connection connection;
    private static final String ip = "sql11.freemysqlhosting.net";
    private static final int port = 3306;
    private static final String database = "sql11190382";
    private static final String password = "wrUTlZJcYR";
    private static final String username = "sql11190382";
    private static final String tablePrefix = "bungeechat_";
    private static final String host = "jdbc:mysql://" + ip + ":" + port + "/" + database
            + "?connectTimeout=0&socketTimeout=0&autoReconnect=true";
    private static boolean runTests = true;

    @BeforeClass
    public static void setUpBeforeClass() {
        try {
            connection = DriverManager.getConnection(host, username, password);
        } catch (SQLException e) {
            System.err.println(e.getLocalizedMessage());

            runTests = false;
        }
    }

    @AfterClass
    public static void tearDownAfterClass() throws SQLException {
        if (runTests) {
            try {
                connection.createStatement().execute("DROP TABLE `bungeechat_Ignores`");
                connection.createStatement().execute("DROP TABLE `bungeechat_Accounts`");
            } catch (SQLSyntaxErrorException e) {
                // Ignore
            }
        }
    }

    @After
    public void tearDown() throws SQLException {
        if (runTests) {
            connection.createStatement().execute("SET FOREIGN_KEY_CHECKS = 0");

            try {
                connection.createStatement().execute("TRUNCATE TABLE `bungeechat_Ignores`");
                connection.createStatement().execute("TRUNCATE TABLE `bungeechat_Accounts`");
            } catch (SQLSyntaxErrorException e) {
                // Ignore
            } finally {
                connection.createStatement().execute("SET FOREIGN_KEY_CHECKS = 1");
            }
        }
    }

    @Test
    public void connectionTest() {
        assumeTrue(runTests);

        try {
            new AccountSQLStorage(ip, port, database, username, password, tablePrefix);
        } catch (SQLException e) {
            fail("No SQL exception expected: " + e.getLocalizedMessage());
        }
    }

    @Test(expected = SQLException.class)
    public void exceptionsTest() throws SQLException {
        new AccountSQLStorage("example.com", 1, "example.com", "example.com", "example.com", "",
                "connectTimeout=10&socketTimeout=10");
    }

    @Test
    public void loadAndSaveTest() {
        assumeTrue(runTests);

        try {
            BungeeChatAccountStorage accountStorage = new AccountSQLStorage(ip, port, database, username, password,
                    tablePrefix);
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
            assertEquals("Should be same channel type", account.getChannelType(), account2.getChannelType());
            assertTrue("Should ignore itself", account2.hasIgnored(testUUID));
        } catch (SQLException e) {
            fail("No SQL exception expected: " + e.getLocalizedMessage());
        }
    }
}
