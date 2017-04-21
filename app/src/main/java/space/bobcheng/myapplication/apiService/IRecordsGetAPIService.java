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
//TODO 这个接口需要改到 MyRecord 上， 同时需要改变的是对这个接口的调用方法
public interface IRecordsGetAPIService {
    @FormUrlEncoded
    @POST("records")
    Call<MyRecord> getRecords(@Field("email") String email);
}
