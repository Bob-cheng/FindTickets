
package space.bobcheng.myapplication.jsonClass;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MyQuery {

    @SerializedName("validateMessagesShowId")
    @Expose
    private String validateMessagesShowId;
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("httpstatus")
    @Expose
    private Integer httpstatus;
    @SerializedName("data")
    @Expose
    private List<Datum> data = null;
    @SerializedName("messages")
    @Expose
    private List<Object> messages = null;
    @SerializedName("validateMessages")
    @Expose
    private ValidateMessages validateMessages;

    public String getValidateMessagesShowId() {
        return validateMessagesShowId;
    }

    public void setValidateMessagesShowId(String validateMessagesShowId) {
        this.validateMessagesShowId = validateMessagesShowId;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Integer getHttpstatus() {
        return httpstatus;
    }

    public void setHttpstatus(Integer httpstatus) {
        this.httpstatus = httpstatus;
    }

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

    public List<Object> getMessages() {
        return messages;
    }

    public void setMessages(List<Object> messages) {
        this.messages = messages;
    }

    public ValidateMessages getValidateMessages() {
        return validateMessages;
    }

    public void setValidateMessages(ValidateMessages validateMessages) {
        this.validateMessages = validateMessages;
    }

}
