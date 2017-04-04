
package space.bobcheng.myapplication.jsonClass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {

    @SerializedName("queryLeftNewDTO")
    @Expose
    private QueryLeftNewDTO queryLeftNewDTO;
    @SerializedName("secretStr")
    @Expose
    private String secretStr;
    @SerializedName("buttonTextInfo")
    @Expose
    private String buttonTextInfo;

    public QueryLeftNewDTO getQueryLeftNewDTO() {
        return queryLeftNewDTO;
    }

    public void setQueryLeftNewDTO(QueryLeftNewDTO queryLeftNewDTO) {
        this.queryLeftNewDTO = queryLeftNewDTO;
    }

    public String getSecretStr() {
        return secretStr;
    }

    public void setSecretStr(String secretStr) {
        this.secretStr = secretStr;
    }

    public String getButtonTextInfo() {
        return buttonTextInfo;
    }

    public void setButtonTextInfo(String buttonTextInfo) {
        this.buttonTextInfo = buttonTextInfo;
    }

}
