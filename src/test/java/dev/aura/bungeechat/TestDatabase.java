// Copyright © 2017, Project-Creative Dev-Team, All Rights Reserved
package dev.aura.bungeechat;

import ch.vorburger.exec.ManagedProcessException;
import ch.vorburger.mariadb4j.DB;
import ch.vorburger.mariadb4j.DBConfiguration;
import ch.vorburger.mariadb4j.DBConfigurationBuilder;
import com.google.common.base.Preconditions;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.SystemUtils;

@UtilityClass
public class TestDatabase {
  private static final String baseDir = SystemUtils.JAVA_IO_TMPDIR + "/MariaDB4j/base/";
  private static final String localhost = "localhost";
  private static DB databaseInstance;
  @Getter private static String host;
  @Getter private static int port;

  @SneakyThrows(ManagedProcessException.class)
  @SuppressFBWarnings( // TODO: Remove when fixed in SpotBugs
      value = "RV_RETURN_VALUE_IGNORED",
      justification = "Return values can be safely ignored as they are for chaining only.")
  public static void startDatabase() {
    final int limit = 100;
    int count = 0;
    String actualBaseDir;
    String actualDataDir;

    do {
      actualBaseDir = baseDir + count;
    } while ((++count < limit) && (new File(actualBaseDir)).exists());

    Preconditions.checkElementIndex(count, limit, "count must be less than " + limit);

    actualDataDir = actualBaseDir + "/data";
    final DBConfiguration config =
        DBConfigurationBuilder.newBuilder()
            .setPort(0)
            .setSocket(localhost)
            .setBaseDir(actualBaseDir)
            .setDataDir(actualDataDir)
            .build();
    databaseInstance = DB.newEmbeddedDB(config);
    databaseInstance.start();

    port = databaseInstance.getConfiguration().getPort();
    host = localhost + ':' + port;

    databaseInstance.createDB("test");
  }

  @SneakyThrows({ManagedProcessException.class})
  public static void stopDatabase() {
    databaseInstance.stop();

    try {
      Thread.sleep(500);

      FileUtils.deleteDirectory(new File(databaseInstance.getConfiguration().getBaseDir()));
    } catch (IOException | InterruptedException e) {
      // Ignore
    }
  }

  @SuppressFBWarnings(value = "DMI_CONSTANT_DB_PASSWORD", justification = "Hardcoding for tests.")
  public static Connection getDatabaseInstance() throws SQLException {
    return DriverManager.getConnection("jdbc:mysql://" + host + "/test", "test", "test");
  }

  public static void closeDatabaseInstance(Connection database) throws SQLException {
    database.close();
  }
}
