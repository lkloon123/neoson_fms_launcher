package app.Interface;

import app.Models.Response.GetAllResponse;
import app.Models.Response.GetResponse;
import app.Models.Software;
import app.Models.Version;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * @author Lam Kai Loon <lkloon123@hotmail.com>
 */
public interface ApiMethod {

    @GET("public/software")
    Call<GetAllResponse<Software>> getAllSoftware();

    @GET("public/software/{id}/download")
    Call<ResponseBody> downloadSoftware(@Path("id") String id);

    @GET("modules/control/latest")
    Call<GetResponse<Version>> getVersion();

    @GET("modules/control/download")
    Call<ResponseBody> downloadControl();
}
