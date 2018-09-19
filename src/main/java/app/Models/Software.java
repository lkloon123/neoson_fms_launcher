package app.Models;

import com.google.gson.annotations.SerializedName;

/**
 * @author Lam Kai Loon <lkloon123@hotmail.com>
 */
public class Software {
    @SerializedName("id")
    public String id;
    @SerializedName("name")
    public String name;
    @SerializedName("exe_name")
    public String exe_name;
    @SerializedName("version")
    public String version;
    @SerializedName("sha256_checksum")
    public String sha256Checksum;

    public Software(String name, String version) {
        this.name = name;
        this.version = version;
    }
}
