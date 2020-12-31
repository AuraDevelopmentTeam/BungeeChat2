package dev.aura.bungeechat.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import lombok.Generated;
import lombok.experimental.UtilityClass;

/**
 * Taken from https://stackoverflow.com/a/3348150/1996022<br>
 * All credit goes to the original author. Only minimal modifications have been made.
 *
 * <p>Marked generated to ignore coverage as this code is confirmed to work
 */
@Generated
@UtilityClass
public class FileUtils {
  @Generated
  public static boolean copyFile(final File toCopy, final File destFile) {
    try (final InputStream inStream = new FileInputStream(toCopy);
        final OutputStream outStream = new FileOutputStream(destFile)) {
      return FileUtils.copyStream(inStream, outStream);
    } catch (final IOException e) {
      e.printStackTrace();
    }

    return false;
  }

  @Generated
  public static boolean copyFilesRecursively(final File toCopy, final File destDir) {
    return copyFilesRecursively(toCopy, destDir, true);
  }

  @Generated
  public static boolean copyFilesRecursively(
      final File toCopy, final File destDir, boolean skipFirstDir) {
    assert destDir.isDirectory();

    if (!toCopy.isDirectory())
      return FileUtils.copyFile(toCopy, new File(destDir, toCopy.getName()));
    else {
      final File newDestDir = skipFirstDir ? destDir : new File(destDir, toCopy.getName());
      File[] listedFiles = toCopy.listFiles();

      if ((!newDestDir.exists() && !newDestDir.mkdir()) || (listedFiles == null)) return false;

      for (final File child : listedFiles) {
        if (!FileUtils.copyFilesRecursively(child, newDestDir, false)) return false;
      }
    }

    return true;
  }

  @Generated
  public static boolean copyJarResourcesRecursively(
      final JarURLConnection jarConnection, final File destDir) throws IOException {
    final JarFile jarFile = jarConnection.getJarFile();

    for (final Enumeration<JarEntry> e = jarFile.entries(); e.hasMoreElements(); ) {
      final JarEntry entry = e.nextElement();

      if (entry.getName().startsWith(jarConnection.getEntryName())) {
        final String filename =
            FileUtils.removeStart(entry.getName(), jarConnection.getEntryName());
        final File f = new File(destDir, filename);

        if (!entry.isDirectory()) {
          final InputStream entryInputStream = jarFile.getInputStream(entry);

          if (!FileUtils.copyStream(entryInputStream, f)) return false;

          entryInputStream.close();
        } else {
          if (!FileUtils.ensureDirectoryExists(f))
            throw new IOException("Could not create directory: " + f.getAbsolutePath());
        }
      }
    }

    return true;
  }

  @Generated
  public static boolean copyResourcesRecursively(final URL originUrl, final File destination) {
    try {
      final URLConnection urlConnection = originUrl.openConnection();

      if (urlConnection instanceof JarURLConnection)
        return FileUtils.copyJarResourcesRecursively((JarURLConnection) urlConnection, destination);
      else return FileUtils.copyFilesRecursively(new File(originUrl.getPath()), destination);
    } catch (final IOException e) {
      e.printStackTrace();
    }

    return false;
  }

  @Generated
  private static boolean copyStream(final InputStream is, final File f) {
    try {
      return FileUtils.copyStream(is, new FileOutputStream(f));
    } catch (final FileNotFoundException e) {
      e.printStackTrace();
    }

    return false;
  }

  @Generated
  private static boolean copyStream(final InputStream is, final OutputStream os) {
    try {
      final byte[] buf = new byte[1024];
      int len;

      try {
        while ((len = is.read(buf)) > 0) {
          os.write(buf, 0, len);
        }
      } finally {
        is.close();
        os.close();
      }

      return true;
    } catch (final IOException e) {
      e.printStackTrace();
    }

    return false;
  }

  @Generated
  private static boolean ensureDirectoryExists(final File f) {
    return f.exists() || f.mkdirs();
  }

  @Generated
  private static String removeStart(String str, String remove) {
    if (isEmpty(str) || isEmpty(remove)) return str;

    if (str.startsWith(remove)) return str.substring(remove.length());

    return str;
  }

  @Generated
  private static boolean isEmpty(CharSequence cs) {
    return (cs == null) || (cs.length() == 0);
  }
}
