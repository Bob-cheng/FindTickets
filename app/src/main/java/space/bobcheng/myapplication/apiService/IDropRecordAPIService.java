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
    @POST("droprecord/whole")
    Call<Map<String, Boolean>> dropRecord(@Field("id") String id);
}
//TODO 增加一个droprecord/certain的接口用来删除某个特定的列车记录