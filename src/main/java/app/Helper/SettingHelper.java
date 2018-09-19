package app.Helper;

import app.Interface.Logging;

import java.io.*;
import java.util.Properties;

/**
 * @author Lam Kai Loon <lkloon123@hotmail.com>
 */
public class SettingHelper implements Logging {
    private static final String fileName = "setting.properties";

    public static void writeVersion(File file, String version) {
        Properties props = new Properties();

        try (OutputStream output = new FileOutputStream(file)) {
            props.setProperty("version", version);
            props.store(output, null);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    public static String readVersion(File file) throws IOException {
        Properties props = new Properties();

        try (InputStream input = new FileInputStream(file)) {
            props.load(input);
            return props.getProperty("version");
        }
    }
}
