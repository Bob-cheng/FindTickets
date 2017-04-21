package space.bobcheng.myapplication.apiService;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import space.bobcheng.myapplication.jsonClass.Record;

/**
 * Created by bob on 17-4-12.
 */

public interface IDropRecordAPIService {
    @FormUrlEncoded
    @POST("droprecord")
    Call<Map<String, Boolean>> dropRecord(@Field("id") String id, @Field("table") String table);
}