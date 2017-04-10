package space.bobcheng.myapplication.jsonClass;

        import com.google.gson.annotations.Expose;
        import com.google.gson.annotations.SerializedName;

public class AddRecInfo {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("existed")
    @Expose
    private Boolean existed;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Boolean getExisted() {
        return existed;
    }

    public void setExisted(Boolean existed) {
        this.existed = existed;
    }

}