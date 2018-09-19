package app;

import app.Controllers.ValidateFileController;
import app.Controllers.ValidateSoftwareController;
import app.Interface.Logging;

import java.io.IOException;
import java.net.URLDecoder;

/**
 * @author Lam Kai Loon <lkloon123@hotmail.com>
 */
public class Main implements Logging {
    private static String OS = System.getProperty("os.name").toLowerCase();

    public static void main(String[] args) {
        logger.info("Booting...");
        try {
            if (isWindows() && args.length == 0) {
                String path = Main.class.getProtectionDomain().getCodeSource().getLocation().getPath().substring(1);
                String decodedPath = URLDecoder.decode(path, "UTF-8");
                Runtime.getRuntime().exec("cmd /c start \"NeoSonFMS Launcher\" java -jar \"" + decodedPath + "\" actual_run");
                System.exit(0);
            }

            new ValidateFileController();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private static boolean isWindows() {
        return (OS.contains("win"));
    }

    private static boolean isUnix() {
        return (OS.contains("nix") || OS.contains("nux") || OS.indexOf("aix") > 0);
    }
}
