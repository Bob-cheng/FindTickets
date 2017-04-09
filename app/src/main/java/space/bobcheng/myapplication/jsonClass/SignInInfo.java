package space.bobcheng.myapplication.jsonClass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SignInInfo {

    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("pwdValid")
    @Expose
    private Boolean pwdValid;
    @SerializedName("userExist")
    @Expose
    private Boolean userExist;

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

    public Boolean getPwdValid() {
        return pwdValid;
    }

    public void setPwdValid(Boolean pwdValid) {
        this.pwdValid = pwdValid;
    }

    public Boolean getUserExist() {
        return userExist;
    }

    public void setUserExist(Boolean userExist) {
        this.userExist = userExist;
    }

}