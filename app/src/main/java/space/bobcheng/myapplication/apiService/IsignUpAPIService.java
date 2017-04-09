package space.bobcheng.myapplication.apiService;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import space.bobcheng.myapplication.jsonClass.SignUpInfo;

/**
 * Created by bob on 17-4-8.
 */

public interface IsignUpAPIService {
    @FormUrlEncoded
    @POST("signup")
    Call<SignUpInfo> sign_up(@Field("username") String username, @Field("email") String email, @Field("password") String pwd);
}
