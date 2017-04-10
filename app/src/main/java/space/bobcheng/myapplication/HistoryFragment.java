package space.bobcheng.myapplication;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import space.bobcheng.myapplication.apiService.IRecordsGetAPIService;
import space.bobcheng.myapplication.jsonClass.Record;

import static space.bobcheng.myapplication.MyUtlis.UnsafeHttp.getUnsafeOkHttpClient;


/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryFragment extends Fragment {
    private FloatingActionButton addhistory;
    private MyCallback mainAct;
    private HashMap<String, String> placeMap = MainActivity.placeMap;
    private HashMap<String, String> reversePlaceMap = MainActivity.reversePlaceMap;
    private String myusername = MainActivity.myusername;
    private String myemail = MainActivity.myemail;
    private Retrofit retrofit;
    private static final String BASE_URL = SignUpActivity.BASE_URL;
    private List<Record> records;
    public HistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_history, container, false);
        return v;

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mainAct = (MyCallback) activity;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();
        addhistory = (FloatingActionButton) getView().findViewById(R.id.addhistory);
        addhistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainAct.onChangeFragment();
            }
        });
        initList();

    }

    private void initList(){
        IRecordsGetAPIService iRecordsGetAPIService = retrofit.create(IRecordsGetAPIService.class);
        Call<List<Record>> call = iRecordsGetAPIService.getRecords(myemail);
        call.enqueue(new Callback<List<Record>>() {
            @Override
            public void onResponse(Call<List<Record>> call, Response<List<Record>> response) {
                try{
                    records = response.body();
                    Log.i("records", records.size() + "");
                }catch (Exception e){
                    Log.e("get_records", "data error");
                }
            }

            @Override
            public void onFailure(Call<List<Record>> call, Throwable t) {
                Log.e("net_failure", t.toString());
            }
        });
    }

    interface MyCallback{
        void onChangeFragment();
    }
}
