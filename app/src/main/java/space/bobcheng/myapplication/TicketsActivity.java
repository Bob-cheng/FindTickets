package space.bobcheng.myapplication;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import space.bobcheng.myapplication.apiService.ItrainQueryAPIService;
import space.bobcheng.myapplication.apiService.ItrainQueryAPIService_x;
import space.bobcheng.myapplication.jsonClass.Datum;
import space.bobcheng.myapplication.jsonClass.MyQuery;
import space.bobcheng.myapplication.jsonClass.QueryLeftNewDTO;

import static space.bobcheng.myapplication.retrofitUtli.UnsafeHttp.getUnsafeOkHttpClient;



public class TicketsActivity extends AppCompatActivity {
    private CheckBox [] boxs = new CheckBox[6];
    private MyQuery query = null;
    private static final String BASE_URL = "https://kyfw.12306.cn/otn/leftTicket/";
    private ConstraintLayout mprocess_layout;
    private Toolbar toolbar;
    private Retrofit retrofit;
    private ArrayList<String> inputMessage;
    private FrameLayout mframeLayout;
    private List<Datum> trainsData = null;
    private ListView mTicketsListView;
    private View.OnClickListener reconnect = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mprocess_layout.setVisibility(View.VISIBLE);
            initMyQuery();
        }
    };
    private View.OnClickListener setOptions = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            makeLists();
        }
    };
    private HashMap<String, String> trainHashMap = new HashMap<String, String>(){
        {
            put("动车", "d");put("高铁", "g");put("快速", "k");
            put("特快", "t");put("直达", "z");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tickets);
        mprocess_layout = (ConstraintLayout) findViewById(R.id.process_layout);
        mframeLayout  = (FrameLayout) findViewById(R.id.frame_layout);
        mTicketsListView = (ListView) findViewById(R.id.ticket_list);


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

        Bundle data = getIntent().getExtras();
        //inputMessage =(ArrayList<String>) data.getSerializable(CheckFragment.INPUTS);
        inputMessage = data.getStringArrayList(CheckFragment.INPUTS);

        /*inputMessage = new ArrayList<>();
        inputMessage.clear();
        inputMessage.add("CDW");
        inputMessage.add("RXW");
        inputMessage.add("2017-04-16");
        inputMessage.add("ADULT");*/

        String requestUrl = "https://kyfw.12306.cn/otn/leftTicket/query?leftTicketDTO.train_date="+inputMessage.get(2)+
                "&leftTicketDTO.from_station="+inputMessage.get(0) +
                "&leftTicketDTO.to_station="+inputMessage.get(1)+
                "&purpose_codes="+inputMessage.get(3);
        Log.i("message_get", requestUrl);
        Log.i("options", getOptions().toString());

        //http://www.jianshu.com/p/5bc866b9cbb9
        //http://square.github.io/retrofit/
        //http://www.jcodecraeer.com/a/anzhuokaifa/androidkaifa/2015/1016/3588.html
        //为了解决ssl证书的问题，这里创建的client可以信任所有证书。
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(getUnsafeOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        initMyQuery();


    }
    //https://shanksleo.gitbooks.io/cookbook/content/view/toolbar.html
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tickets_activity_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private ArrayList<String> getOptions(){
        ArrayList<String> options = new ArrayList<>();
        for(int i = 1; i < 6; i++){
            if(boxs[i].isChecked()){
                options.add(trainHashMap.get(boxs[i].getText().toString()));
            }
        }
        return options;
    }
    //获取12306数据，先调用query如果query不生效就调用queryx
    private void makeLists(){
        if(trainsData == null){
            Snackbar.make(mframeLayout, "数据异常，请重试", Snackbar.LENGTH_LONG)
                    .setAction("重试", reconnect).show();
            return;
        }

        Log.i("List", "start to make list");
        List<Map<String, Object>> listItems =
                new ArrayList<>();
        ArrayList<String> options = getOptions();
        for(int i = 0; i < trainsData.size(); i++){
            QueryLeftNewDTO queryLeftNewDTO = trainsData.get(i).getQueryLeftNewDTO();
            String train_type = ""+queryLeftNewDTO.getStationTrainCode().charAt(0);
            if(options.contains(train_type.toLowerCase())){
                String Youwu = "无票";
                Map<String, Object> item = new HashMap<>();
                item.put("start_time", queryLeftNewDTO.getStartTime());
                item.put("end_time", queryLeftNewDTO.getArriveTime());
                item.put("start_place", queryLeftNewDTO.getFromStationName());
                item.put("end_place", queryLeftNewDTO.getToStationName());
                item.put("train_num", queryLeftNewDTO.getStationTrainCode());
                String lishi = queryLeftNewDTO.getLishi().replace(':', '时')+"分";
                if(lishi.startsWith("00")){
                    lishi = lishi.substring(4);
                }else if(lishi.startsWith("0")){
                    lishi = lishi.substring(1);
                }
                item.put("lishi", lishi);

                if(checkYouWu(queryLeftNewDTO.getZyNum()))
                    Youwu = "有票";
                item.put("seats_first_num", queryLeftNewDTO.getZyNum());
                if(checkYouWu(queryLeftNewDTO.getZeNum()))
                    Youwu = "有票";
                item.put("seats_second_num", queryLeftNewDTO.getZeNum());
                if(checkYouWu(queryLeftNewDTO.getRwNum()))
                    Youwu = "有票";
                item.put("seats_rw_num", queryLeftNewDTO.getRwNum());
                if(checkYouWu(queryLeftNewDTO.getYwNum()))
                    Youwu = "有票";
                item.put("seats_yw_num", queryLeftNewDTO.getYwNum());
                if(checkYouWu(queryLeftNewDTO.getYzNum()))
                    Youwu = "有票";
                item.put("seats_yz_num", queryLeftNewDTO.getYzNum());
                if(checkYouWu(queryLeftNewDTO.getWzNum()))
                    Youwu = "有票";
                item.put("seats_wz_num", queryLeftNewDTO.getWzNum());
                item.put("status", Youwu);
                listItems.add(item);
            }

        }
        SimpleAdapter simpleAdapter = new SimpleAdapter(this, listItems,
                R.layout.ticket_item,
                new String[]{"start_time", "end_time", "start_place", "end_place", "train_num",
                                "lishi", "seats_first_num", "seats_second_num", "seats_rw_num",
                                "seats_yw_num",  "seats_yz_num", "seats_wz_num",
                                "status"},
                new int[]{R.id.start_time, R.id.end_time, R.id.start_place, R.id.end_place, R.id.train_num,
                        R.id.lishi, R.id.seats_first_num, R.id.seats_second_num, R.id.seats_rw_num,
                        R.id.seats_yw_num, R.id.seats_yz_num, R.id.seats_wz_num,
                        R.id.status}
                );
        mTicketsListView.setAdapter(simpleAdapter);
        Log.i("List", "finish make list view");
    }

    private boolean checkYouWu(String input){
        if(input == "--" || input == "无")
            return false;
        else
            return true;
    }
    private void initMyQuery(){
        ItrainQueryAPIService trainApiService = retrofit.create(ItrainQueryAPIService.class);
        Call<MyQuery> call = trainApiService.getTicketInfo(inputMessage.get(2), inputMessage.get(0), inputMessage.get(1),inputMessage.get(3));
        call.enqueue(new Callback<MyQuery>() {
            @Override
            public void onResponse(Call<MyQuery> call, Response<MyQuery> response) {
                Log.i("statusCode", response.code()+"");
                try{
                    query = response.body();
                    String arrivetime = query.getData().get(0).getQueryLeftNewDTO().getArriveTime();
                    Log.i("query_Status", arrivetime);
                    trainsData = query.getData();
                    makeLists();
                    mprocess_layout.setVisibility(View.INVISIBLE);
                }catch (Exception e){
                    Log.e("Status", "start to queryx");
                    initMyQuery_x();
                }
            }
            @Override
            public void onFailure(Call<MyQuery> call, Throwable t) {
                Log.e("requestError", t.toString());
                Snackbar.make(mframeLayout, "连接失败，请检查网络设置", Snackbar.LENGTH_LONG)
                        .setAction("重试", reconnect).show();
                mprocess_layout.setVisibility(View.INVISIBLE);
            }
        });
    }
    //有时候获取的api只有queryx才生效。。。
    private void initMyQuery_x(){
        ItrainQueryAPIService_x trainApiService = retrofit.create(ItrainQueryAPIService_x.class);
        Call<MyQuery> call = trainApiService.getTicketInfo(inputMessage.get(2), inputMessage.get(0), inputMessage.get(1),inputMessage.get(3));
        call.enqueue(new Callback<MyQuery>() {
            @Override
            public void onResponse(Call<MyQuery> call, Response<MyQuery> response) {
                Log.i("x_statusCode", response.code()+"");
                try{
                    query = response.body();
                    String arrivetime = query.getData().get(0).getQueryLeftNewDTO().getArriveTime();
                    Log.i("query_x_Status", arrivetime);
                    trainsData = query.getData();
                    makeLists();
                }catch (Exception e){
                    Log.e("queryx", "server response error."+e.toString());
                    Snackbar.make(mframeLayout, "服务器数据异常，请稍后再试", Snackbar.LENGTH_LONG)
                            .setAction("重试", reconnect).show();
                }finally {
                    mprocess_layout.setVisibility(View.INVISIBLE);
                }
            }
            @Override
            public void onFailure(Call<MyQuery> call, Throwable t) {
                Log.e("x_requestError", t.toString());
                mprocess_layout.setVisibility(View.INVISIBLE);
            }
        });
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
                makeLists();
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
            boxs[i].setOnClickListener(setOptions);
        }
    }
}


