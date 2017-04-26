package space.bobcheng.myapplication.apiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import space.bobcheng.myapplication.jsonClass.AddRecInfo;
import space.bobcheng.myapplication.jsonClass.Record;

/**
 * Created by bob on 17-4-10.
 */

public interface IAddRecordAPIService {
    @FormUrlEncoded
    @POST("addrecord")
    Call<AddRecInfo> addRecord(@Field("email") String email, @Field("start_from") String start_from,
                               @Field("end_to") String end_to, @Field("date") String date,
                               @Field("ticket_type") String ticket_type, @Field("ticket_left") String ticket_left);
}

