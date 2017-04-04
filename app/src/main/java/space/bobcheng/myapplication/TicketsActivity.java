package space.bobcheng.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;
import space.bobcheng.myapplication.jsonPackage.MyQuery;

import static space.bobcheng.myapplication.UnsafeHttp.getUnsafeOkHttpClient;

interface ItrainQueryAPIService{
    @GET("queryx")
    Call<MyQuery> getTicketInfo(@Query("leftTicketDTO.train_date") String date,
                                @Query("leftTicketDTO.from_station") String from,
                                @Query("leftTicketDTO.to_station") String to,
                                @Query("purpose_codes") String type);
}


public class TicketsActivity extends AppCompatActivity {
    private CheckBox [] boxs = new CheckBox[6];
    private String requestUrl;
    private int statusCode = -1;
    private MyQuery query;
    //public static final String BASE_URL = "http://kyfw.12306.cn/otn/leftTicket";
    public static final String BASE_URL = "https://kyfw.12306.cn/otn/leftTicket/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tickets);

        initCheckBoxs();
        if(getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /*Bundle data = getIntent().getExtras();
        //inputMessage =(ArrayList<String>) data.getSerializable(CheckFragment.INPUTS);
        ArrayList<String> inputMessage = data.getStringArrayList(CheckFragment.INPUTS);*/

        ArrayList<String> inputMessage = new ArrayList<>();
        inputMessage.clear();
        inputMessage.add("CDW");
        inputMessage.add("RXW");
        inputMessage.add("2017-04-04");
        inputMessage.add("ADULT");

        requestUrl = "https://kyfw.12306.cn/otn/leftTicket/queryx?leftTicketDTO.train_date="+inputMessage.get(2)+
                "&leftTicketDTO.from_station="+inputMessage.get(0) +
                "&leftTicketDTO.to_station="+inputMessage.get(1)+
                "&purpose_codes="+inputMessage.get(3);
        Log.i("message_get", requestUrl);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(getUnsafeOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ItrainQueryAPIService trainApiService = retrofit.create(ItrainQueryAPIService.class);
        Call<MyQuery> call = trainApiService.getTicketInfo(inputMessage.get(2), inputMessage.get(0), inputMessage.get(1),inputMessage.get(3));
        call.enqueue(new Callback<MyQuery>() {

            @Override
            public void onResponse(Call<MyQuery> call, Response<MyQuery> response) {
                statusCode = response.code();
                Log.i("statusCode", statusCode+"");
                query = response.body();
                Log.i("queryStatus", query.getStatus().toString());
            }

            @Override
            public void onFailure(Call<MyQuery> call, Throwable t) {
                Log.e("requestError", t.toString());
            }

        });

    }


    private void initCheckBoxs (){
        boxs[0] = (CheckBox) findViewById(R.id.trains_all);
        boxs[1] = (CheckBox) findViewById(R.id.trains_d);
        boxs[2] = (CheckBox) findViewById(R.id.trains_g);
        boxs[3] = (CheckBox) findViewById(R.id.trains_k);
        boxs[4] = (CheckBox) findViewById(R.id.trains_t);
        boxs[5] = (CheckBox) findViewById(R.id.trains_z);
        boxs[0].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    for(int i = 1; i < 6; i++){
                        boxs[i].setChecked(true);
                    }
                }
            }
        });

        boxs[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox ch = (CheckBox) v;
                if(!ch.isChecked()){
                    for(int i = 1; i < 6; i++){
                        boxs[i].setChecked(false);
                    }
                }
            }
        });

        for(int i = 1; i < 6; i++){
            boxs[i].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(!isChecked){
                        boxs[0].setChecked(false);
                    }else if(isChecked) {
                        boolean flag = true;
                        for(int j = 1; j < 6; j++){
                            if(!boxs[j].isChecked()){
                                flag = false;
                                break;
                            }
                        }
                        if(flag)
                            boxs[0].setChecked(true);
                    }
                }
            });
        }
    }
}

