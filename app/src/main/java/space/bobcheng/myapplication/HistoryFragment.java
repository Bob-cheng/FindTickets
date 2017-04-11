package space.bobcheng.myapplication;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import space.bobcheng.myapplication.apiService.IDropRecordAPIService;
import space.bobcheng.myapplication.apiService.IRecordsGetAPIService;
import space.bobcheng.myapplication.jsonClass.Record;


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
    protected static Retrofit retrofit;
    private static final String BASE_URL = SignUpActivity.BASE_URL;
    protected List<Record> records;
    private RecyclerView mRecyclerView;
    private MyRecyclerAdapter myRecyclerAdapter = null;
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
        mRecyclerView = (RecyclerView) getView().findViewById(R.id.recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        initList();

    }

    protected void initList(){
        IRecordsGetAPIService iRecordsGetAPIService = retrofit.create(IRecordsGetAPIService.class);
        Call<List<Record>> call = iRecordsGetAPIService.getRecords(myemail);
        call.enqueue(new Callback<List<Record>>() {
            @Override
            public void onResponse(Call<List<Record>> call, Response<List<Record>> response) {
                try{
                    records = response.body();
                    Log.i("records", records.size() + "");
                    if(myRecyclerAdapter == null){
                        //创建adapter
                        myRecyclerAdapter = new MyRecyclerAdapter(getContext(), records);
                        myRecyclerAdapter.setOnItemClickListener(new MyRecyclerAdapter.OnRecyclerViewItemClickListener() {
                            @Override
                            public void onItemClick(View view) {
                                String date = ((TextView)view.findViewById(R.id.date)).getText().toString();
                                String start = ((TextView)view.findViewById(R.id.start_from)).getText().toString();
                                String end = ((TextView)view.findViewById(R.id.end_to)).getText().toString();
                                String type = ((TextView)view.findViewById(R.id.ticket_type)).getText().toString();
                                launchTicketsActivity(start, end, date, type);
                            }

                            @Override
                            public void onItemLongClick(View view) {
                                //长按删除
                                int position = mRecyclerView.getChildPosition(view);
                                myRecyclerAdapter.removeItem(position);
                            }
                        });
                        mRecyclerView.setAdapter(myRecyclerAdapter);
                    }else {
                        //如果不是第一次创建adapter 就只用更新record就好
                        myRecyclerAdapter.setRecords(records);
                        myRecyclerAdapter.notifyDataSetChanged();
                    }

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

    private void launchTicketsActivity(String start, String end, String date, String type){
        Intent intent = new Intent(getActivity(), TicketsActivity.class);
        Bundle data = new Bundle();
        ArrayList<String> inputMessage = new ArrayList<>();
        inputMessage.clear();
        inputMessage.add(start);
        inputMessage.add(end);
        inputMessage.add(date);
        inputMessage.add(type);
        data.putSerializable(CheckFragment.INPUTS, inputMessage);
        intent.putExtras(data);
        startActivity(intent);
    }

    interface MyCallback{
        void onChangeFragment();
    }
}

class MyRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener, View.OnLongClickListener{
    private HashMap<String, String> reversePlaceMap = MainActivity.reversePlaceMap;
    //自定义监听事件
    public static interface OnRecyclerViewItemClickListener {
        void onItemClick(View view);
        void onItemLongClick(View view);
    }

    private Context mContext;
    private List<Record> records;
    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    //适配器初始化
    public MyRecyclerAdapter(Context context,List<Record> records) {
        mContext=context;
        this.records=records;
    }


    protected void removeItem(int pos){
        final int position = pos;
        String id = records.get(position).getId()+"";
        IDropRecordAPIService iDropRecordAPIService =
                HistoryFragment.retrofit.create(IDropRecordAPIService.class);
        Call<Map<String, Boolean>> call = iDropRecordAPIService.dropRecord(id);
        call.enqueue(new Callback<Map<String, Boolean>>() {
            @Override
            public void onResponse(Call<Map<String, Boolean>> call, Response<Map<String, Boolean>> response) {
                try{
                    Map<String, Boolean> result = response.body();
                    if(result.get("status")){
                        Toast.makeText(mContext, "删除成功", Toast.LENGTH_LONG).show();
                        records.remove(position);
                        notifyItemRemoved(position);
                    }else {
                        Log.e("removeItem", "服务器数据错误");
                        Toast.makeText(mContext, "服务器出错", Toast.LENGTH_LONG).show();
                    }
                }catch (Exception e){
                    Toast.makeText(mContext, "服务器出错", Toast.LENGTH_LONG).show();
                    Log.e("removeItem", e.toString());
                }

            }

            @Override
            public void onFailure(Call<Map<String, Boolean>> call, Throwable t) {
                Toast.makeText(mContext, "请检查网络连接", Toast.LENGTH_LONG).show();
                Log.e("removeItem", t.toString());
            }
        });
    }

    protected void  setRecords(List<Record> records){
        this.records = records;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.history_item, viewGroup, false);
        view.setOnClickListener(this);
        view.setOnLongClickListener(this);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyViewHolder myViewHolder = (MyViewHolder) holder;
        Record record = records.get(position);
        myViewHolder.date.setText(record.getDate());
        myViewHolder.start_from.setText(reversePlaceMap.get(record.getStartFrom()));
        myViewHolder.end_to.setText(reversePlaceMap.get(record.getEndTo()));

        if(record.getTicketLeft()){
            myViewHolder.ticket_status.setTextColor(mContext.getResources().getColor(android.R.color.holo_green_dark));
            myViewHolder.ticket_status.setText("有票");
        }else {
            myViewHolder.ticket_status.setTextColor(Color.RED);
            myViewHolder.ticket_status.setText("无票");
        }
        myViewHolder.ticket_type.setText(record.getTicketType().equals("ADULT")?"成人票":"学生票");
    }

    @Override
    public int getItemCount() {
        return records.size();
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(v);
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if (mOnItemClickListener!= null) {
            mOnItemClickListener.onItemLongClick(v);
        }
        return false;
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {
        protected TextView date;
        protected TextView start_from;
        protected TextView end_to;
        protected TextView ticket_status;
        protected TextView ticket_type;

        public MyViewHolder(View view)
        {
            super(view);
            date = (TextView) view.findViewById(R.id.date);
            start_from = (TextView) view.findViewById(R.id.start_from);
            end_to =  (TextView) view.findViewById(R.id.end_to);
            ticket_status = (TextView) view.findViewById(R.id.ticket_status);
            ticket_type = (TextView) view.findViewById(R.id.ticket_type);
        }
    }
}

