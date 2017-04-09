package space.bobcheng.myapplication.apiService;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import space.bobcheng.myapplication.jsonClass.SignInInfo;
import space.bobcheng.myapplication.jsonClass.SignUpInfo;

/**
 * Created by bob on 17-4-9.
 */

public interface IsignInAPIService {
    @FormUrlEncoded
    @POST("login")
    Call<SignInInfo> sign_in(@Field("email") String email, @Field("password") String pwd);
}
