package app.Controllers;

import app.Helper.ApiClient;
import app.Helper.ApiWrapper;
import app.Helper.DownloadHelper;
import app.Interface.Logging;
import app.Models.Response.GetResponse;
import app.Models.Software;
import app.Models.Version;
import javaxt.io.Jar;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

import java.io.File;
import java.io.IOException;

/**
 * @author Lam Kai Loon <lkloon123@hotmail.com>
 */
public class ValidateFileController implements Logging {
    public ValidateFileController() {
        logger.info("Validating Software...");
        try {
            File mainFile = new File("neosonfms_miner.jar");
            fetchVersion(new Jar(mainFile).getVersion());
        } catch (NullPointerException e) {
            //download the jar file
            fetchVersion("0");
        }
    }

    private void fetchVersion(String localVersion) {
        new ApiWrapper<GetResponse<Version>>().execute(ApiClient.getInterface().getVersion(),
                (data, error) -> {
                    if (!data.data.remoteVersion.equalsIgnoreCase(localVersion)) {
                        downloadMinerControlRequest(data.data.remoteVersion);
                    }
                    new ValidateSoftwareController();
                });
    }

    private void downloadMinerControlRequest(String remoteVer) {
        final Call<ResponseBody> call = ApiClient.getInterface().downloadControl();
        try {
            Response<ResponseBody> response = call.execute();
            if (response.isSuccessful()) {
                try {
                    DownloadHelper.download(response.body(), new File(System.getProperty("user.dir") + File.separator + "neosonfms_miner.jar"), new Software("neosonfms_miner", remoteVer));
                } catch (IOException e) {
                    logger.error(e.getMessage());
                }
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
