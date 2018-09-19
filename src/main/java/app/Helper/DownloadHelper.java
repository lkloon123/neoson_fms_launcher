package app.Helper;

import app.Interface.Logging;
import app.Models.Software;
import okhttp3.ResponseBody;

import java.io.*;

/**
 * @author Lam Kai Loon <lkloon123@hotmail.com>
 */
public class DownloadHelper implements Logging {

    public static void download(ResponseBody body, File file, Software software) throws IOException {
        logger.info("Downloading " + software.name + " (Version " + software.version + ")");

        ProgressBar pb = new ProgressBar();
        byte[] fileReader = new byte[4096];

        long fileSize = body.contentLength();
        long fileSizeDownloaded = 0;

        pb.update(0, fileSize);

        try (InputStream inputStream = body.byteStream();
             OutputStream outputStream = new FileOutputStream(file)) {
            while (true) {
                int read = inputStream.read(fileReader);

                if (read == -1) {
                    break;
                }

                outputStream.write(fileReader, 0, read);

                fileSizeDownloaded += read;
                pb.update(fileSizeDownloaded, fileSize);
            }
        }
    }
}
