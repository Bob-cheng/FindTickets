package space.bobcheng.myapplication.jsonClass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SignUpInfo {

    @SerializedName("email_valid")
    @Expose
    private Boolean emailValid;
    @SerializedName("creat_status")
    @Expose
    private Boolean creatStatus;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("email")
    @Expose
    private String email;

    public Boolean getEmailValid() {
        return emailValid;
    }

    public void setEmailValid(Boolean emailValid) {
        this.emailValid = emailValid;
    }

    public Boolean getCreatStatus() {
        return creatStatus;
    }

    public void setCreatStatus(Boolean creatStatus) {
        this.creatStatus = creatStatus;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}