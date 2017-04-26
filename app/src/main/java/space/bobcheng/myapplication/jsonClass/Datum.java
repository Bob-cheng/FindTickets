
package space.bobcheng.myapplication.jsonClass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {

    @SerializedName("queryLeftNewDTO")
    @Expose
    private QueryLeftNewDTO queryLeftNewDTO;

    public QueryLeftNewDTO getQueryLeftNewDTO() {
        return queryLeftNewDTO;
    }

    public void setQueryLeftNewDTO(QueryLeftNewDTO queryLeftNewDTO) {
        this.queryLeftNewDTO = queryLeftNewDTO;
    }

}
