
package space.bobcheng.myapplication.jsonClass;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MyRecord {

    @SerializedName("records")
    @Expose
    private List<Record> records = null;
    @SerializedName("certain_records")
    @Expose
    private List<CertainRecord> certainRecords = null;

    public List<Record> getRecords() {
        return records;
    }

    public void setRecords(List<Record> records) {
        this.records = records;
    }

    public List<CertainRecord> getCertainRecords() {
        return certainRecords;
    }

    public void setCertainRecords(List<CertainRecord> certainRecords) {
        this.certainRecords = certainRecords;
    }

}
