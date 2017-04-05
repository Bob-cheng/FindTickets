package space.bobcheng.myapplication.apiService;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import space.bobcheng.myapplication.jsonClass.MyQuery;

/**
 * Created by bob on 17-4-5.
 */
//使用queryx查询的接口
public interface ItrainQueryAPIService_x{
    @GET("queryx")
    Call<MyQuery> getTicketInfo(@Query("leftTicketDTO.train_date") String date,
                                @Query("leftTicketDTO.from_station") String from,
                                @Query("leftTicketDTO.to_station") String to,
                                @Query("purpose_codes") String type);
}
