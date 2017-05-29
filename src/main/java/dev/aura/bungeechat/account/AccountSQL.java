package dev.aura.bungeechat.account;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import dev.aura.bungeechat.config.Config;
import lombok.Getter;
import net.md_5.bungee.config.Configuration;

public class AccountSQL {
    @Getter
    private static Connection connection;

    public static void openConnection() throws SQLException {
        Configuration c = Config.get().getSection("AccountDataBase");

        String host = "jdbc:mysql://" + c.getString("ip") + ":" + c.getString("port") + "/" + c.getString("database")
                + "?connectTimeout=0&socketTimeout=0&autoReconnect=true";
        String username = c.getString("username");
        String password = c.getString("password");

        connection = DriverManager.getConnection(host, username, password);

        Statement statement = getConnection().createStatement();
        String sql = "CREATE TABLE BungeeChatAccounts " + "(id INTEGER not NULL, " + " uuid VARCHAR(255), "
                + " channeltype VARCHAR(255), " + " vanished BIT, " + " messenger BIT, " + " socialspy BIT, "
                + " ignored TEXT(40000), " + " PRIMARY KEY ( id ))";
        statement.executeUpdate(sql);
    }

}
