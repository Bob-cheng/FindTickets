package space.bobcheng.myapplication.apiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import space.bobcheng.myapplication.jsonClass.MyRecord;
import space.bobcheng.myapplication.jsonClass.Record;
import space.bobcheng.myapplication.jsonClass.SignInInfo;

/**
 * Created by bob on 17-4-10.
 */

public interface IRecordsGetAPIService {
    @FormUrlEncoded
    @POST("records")
    Call<List<MyRecord>> getRecords(@Field("email") String email);
}
