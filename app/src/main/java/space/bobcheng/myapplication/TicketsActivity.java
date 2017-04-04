package space.bobcheng.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ProgressBar;

import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;
import space.bobcheng.myapplication.jsonClass.MyQuery;

import static space.bobcheng.myapplication.retrofitUtli.UnsafeHttp.getUnsafeOkHttpClient;



public class TicketsActivity extends AppCompatActivity {
    private CheckBox [] boxs = new CheckBox[6];
    private int statusCode = -1;
    private MyQuery query;
    private static final String BASE_URL = "https://kyfw.12306.cn/otn/leftTicket/";
    private ProgressBar progressBar;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tickets);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        toolbar = (Toolbar) findViewById(R.id.tickets_toolbar);
        //http://www.jcodecraeer.com/a/anzhuokaifa/androidkaifa/2014/1118/2006.html
        //http://www.jianshu.com/p/ae0013a4f71a
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.add_to_history:
                        Log.i("menuinfo", "add_to_history");
                        break;
                }
                return false;
            }
        });

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
        inputMessage.add("2017-04-05");
        inputMessage.add("ADULT");

        String requestUrl = "https://kyfw.12306.cn/otn/leftTicket/queryx?leftTicketDTO.train_date="+inputMessage.get(2)+
                "&leftTicketDTO.from_station="+inputMessage.get(0) +
                "&leftTicketDTO.to_station="+inputMessage.get(1)+
                "&purpose_codes="+inputMessage.get(3);
        Log.i("message_get", requestUrl);


        //http://www.jianshu.com/p/5bc866b9cbb9
        //http://square.github.io/retrofit/
        //http://www.jcodecraeer.com/a/anzhuokaifa/androidkaifa/2015/1016/3588.html
        //为了解决ssl证书的问题，这里创建的client可以信任所有证书。
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
                if(statusCode == 200){
                    query = response.body();
                    String arrivetime = query.getData().get(0).getQueryLeftNewDTO().getArriveTime();
                    Log.i("queryStatus", arrivetime);
                }else {
                    Log.e("StatusError", "server response error");
                }
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<MyQuery> call, Throwable t) {
                Log.e("requestError", t.toString());
            }

        });

    }
    //https://shanksleo.gitbooks.io/cookbook/content/view/toolbar.html
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tickets_activity_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //初始化单选框的逻辑
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
                    }else{
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


//retrofit的查询接口
interface ItrainQueryAPIService{
    @GET("query")
    Call<MyQuery> getTicketInfo(@Query("leftTicketDTO.train_date") String date,
                                @Query("leftTicketDTO.from_station") String from,
                                @Query("leftTicketDTO.to_station") String to,
                                @Query("purpose_codes") String type);
}