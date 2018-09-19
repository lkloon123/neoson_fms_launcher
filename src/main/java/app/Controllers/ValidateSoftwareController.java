package app.Controllers;

import app.Config.Config;
import app.Helper.ApiClient;
import app.Helper.ApiWrapper;
import app.Helper.DownloadHelper;
import app.Helper.SettingHelper;
import app.Interface.Logging;
import app.Models.Response.GetAllResponse;
import app.Models.Software;
import okhttp3.ResponseBody;
import org.apache.commons.io.FileUtils;
import org.zeroturnaround.zip.ZipUtil;
import retrofit2.Call;
import retrofit2.Response;

import java.io.File;
import java.io.IOException;

/**
 * @author Lam Kai Loon <lkloon123@hotmail.com>
 */
public class ValidateSoftwareController implements Logging {

    public ValidateSoftwareController() {
        fetchSoftware();
    }

    private void fetchSoftware() {
        new ApiWrapper<GetAllResponse<Software>>().execute(ApiClient.getInterface().getAllSoftware(),
                (data, error) -> {
                    for (Software item : data.data) {
                        File f = new File(System.getProperty("user.dir") + Config.SOFTWARE_FOLDER + item.name);
                        if (f.exists()) {
                            //check for version
                            try {
                                String localVer = SettingHelper.readVersion(new File(f, "version.properties"));
                                if (!isFileLatest(localVer, item.version)) {
                                    logger.info(item.name + " need to be update");
                                    FileUtils.deleteDirectory(f);
                                    downloadFileRequest(item);
                                }
                            } catch (IOException e) {
                                logger.error(e.getMessage());
                            }
                        } else {
                            //not exist
                            downloadFileRequest(item);
                        }
                    }
                    logger.info("Software Validation Completed!!");
                    try {
                        Runtime.getRuntime().exec("cmd /c start \"NeoSonFMS Miner Control\" java -jar \"neosonfms_miner.jar\" actual_run");
                    } catch (IOException e) {
                        logger.error(e.getMessage());
                    } finally {
                        System.exit(0);
                    }
                });
    }

    private void downloadFileRequest(Software software) {
        final Call<ResponseBody> call = ApiClient.getInterface().downloadSoftware(software.id);
        try {
            Response<ResponseBody> response = call.execute();
            if (response.isSuccessful()) {
                String directoryName = System.getProperty("user.dir") + Config.SOFTWARE_FOLDER + software.name;
                new File(directoryName).mkdirs();
                File file = new File(directoryName, software.name + ".zip");

                try {
                    DownloadHelper.download(response.body(), file, software);
                    unZip(file);
                    SettingHelper.writeVersion(new File(directoryName, "version.properties"), software.version);
                } catch (IOException e) {
                    logger.error(e.getMessage());
                }
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private boolean isFileLatest(String localVer, String remoteVer) {
        return localVer.equalsIgnoreCase(remoteVer);
    }

    private void unZip(File zipFile) {
        System.out.println("Extracting " + zipFile.getName());
        ZipUtil.unpack(zipFile, new File(zipFile.getParent()));

        //delete the zip file after extract
        zipFile.delete();
    }
}
