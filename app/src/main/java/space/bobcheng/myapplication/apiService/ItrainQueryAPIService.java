package space.bobcheng.myapplication.apiService;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import space.bobcheng.myapplication.jsonClass.MyQuery;

/**
 * Created by bob on 17-4-5.
 */
//retrofit的查询接口
public interface ItrainQueryAPIService{
    @GET("12306")
    Call<MyQuery> getTicketInfo(@Query("train_date") String date,
                                @Query("from_station") String from,
                                @Query("to_station") String to,
                                @Query("purpose_codes") String type);
}
