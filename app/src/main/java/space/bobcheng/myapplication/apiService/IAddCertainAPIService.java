package space.bobcheng.myapplication.apiService;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import space.bobcheng.myapplication.jsonClass.AddRecInfo;

/**
 * Created by bob on 17-4-20.
 */

public interface IAddCertainAPIService {
    @FormUrlEncoded
    @POST("addcertain")
    Call<AddRecInfo> addCertain(@Field("email") String email, @Field("start_from") String start_from,
                                @Field("end_to") String end_to, @Field("date") String date,
                                @Field("start_time") String start_time, @Field("end_time") String end_time,
                                @Field("lishi") String lishi, @Field("train_id") String train_id,
                                @Field("first_seats") String first_seats, @Field("second_seats") String second_seats,
                                @Field("rw") String rw, @Field("yw") String yw,
                                @Field("yz") String yz, @Field("wz") String wz,
                                @Field("ticket_type") String ticket_type, @Field("ticket_left") String ticket_left);
}