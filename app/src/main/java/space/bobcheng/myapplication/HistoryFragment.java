package space.bobcheng.myapplication;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.SwipeDismissBehavior;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
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
import space.bobcheng.myapplication.jsonClass.CertainRecord;
import space.bobcheng.myapplication.jsonClass.MyRecord;
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
    private TextView processingHint;
    protected static Retrofit retrofit;
    private static final String BASE_URL = SignUpActivity.BASE_URL;
    private RecyclerView mRecyclerView;
    private MyRecyclerAdapter myRecyclerAdapter = null;
    protected MainActivity myMainActivity;
    protected TextView no_historyHint;
    protected SwipeRefreshLayout refreshLayout;
    protected SwipeRefreshLayout.OnRefreshListener refreshListener= new
            SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    initList();
                }
            };
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
        myMainActivity = (MainActivity) getActivity();
        processingHint = (TextView) getView().findViewById(R.id.processingHint);
        addhistory = (FloatingActionButton) getView().findViewById(R.id.addhistory);
        addhistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainAct.onChangeFragment();
            }
        });
        mRecyclerView = (RecyclerView) getView().findViewById(R.id.recycler);
        no_historyHint = (TextView) getView().findViewById(R.id.no_history);
        refreshLayout = (SwipeRefreshLayout) getView().findViewById(R.id.refresh);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        processingHint.setVisibility(View.INVISIBLE);
        no_historyHint.setVisibility(View.INVISIBLE);
        initList();
        refreshLayout.setColorSchemeColors(Color.BLUE);
        refreshLayout.setOnRefreshListener(refreshListener);

    }

    private void initList(){
        refreshLayout.setRefreshing(true);
        IRecordsGetAPIService iRecordsGetAPIService = retrofit.create(IRecordsGetAPIService.class);
        Call<MyRecord> call = iRecordsGetAPIService.getRecords(myemail);
        call.enqueue(new Callback<MyRecord>() {
            @Override
            public void onResponse(Call<MyRecord> call, Response<MyRecord> response) {
                try{
                    List<Record> records = response.body().getRecords();
                    List<CertainRecord> certainRecords = response.body().getCertainRecords();
                    Log.i("records", records.size() + "");
                    Log.i("certain_records", certainRecords.size() + "");
                    if(records.size() == 0 && certainRecords.size() == 0){
                        no_historyHint.setVisibility(View.VISIBLE);
                    }else {
                        no_historyHint.setVisibility(View.INVISIBLE);
                    }
                    if(myRecyclerAdapter == null){
                        //创建adapter
                        myRecyclerAdapter = new MyRecyclerAdapter(getContext(), records, certainRecords, HistoryFragment.this);
                        myRecyclerAdapter.setOnItemClickListener(new MyRecyclerAdapter.OnRecyclerViewItemClickListener() {
                            @Override
                            public void onItemClick(View view) {
                                int pos = mRecyclerView.getChildAdapterPosition(view);
                                Record record = myRecyclerAdapter.records.get(pos);
                                String date = record.getDate();
                                String start = record.getStartFrom();
                                String end = record.getEndTo();
                                String type = record.getTicketType();
                                launchTicketsActivity(start, end, date, type);
                            }

                            @Override
                            public void onItemLongClick(View view) {
                                //长按删除
                                int position = mRecyclerView.getChildAdapterPosition(view);
                                myRecyclerAdapter.removeItem(position);
                            }
                        });
                        mRecyclerView.setAdapter(myRecyclerAdapter);
                    }else {
                        //如果不是第一次创建adapter 就只用更新record就好
                        myRecyclerAdapter.setRecords(records);
                        myRecyclerAdapter.setCertainRecords(certainRecords);
                        myRecyclerAdapter.notifyDataSetChanged();
                    }

                }catch (Exception e){
                    Log.e("get_records", "data error");
                }finally {
                    processingHint.setVisibility(View.INVISIBLE);
                    refreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(Call<MyRecord> call, Throwable t) {
                Log.e("net_failure", t.toString());
                processingHint.setVisibility(View.INVISIBLE);
                refreshLayout.setRefreshing(false);
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
    List<Record> records;
    List<CertainRecord> certainRecords;
    private OnRecyclerViewItemClickListener mOnItemClickListener = null;
    private Fragment myFragment;

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    //适配器初始化
    public MyRecyclerAdapter(Context context,List<Record> records,
                             List<CertainRecord> certainRecords, Fragment f) {
        mContext=context;
        this.records=records;
        this.certainRecords = certainRecords;
        this.myFragment = f;
    }


    protected void removeItem(final int pos){
        ((HistoryFragment)myFragment).myMainActivity.progressBar.setVisibility(View.VISIBLE);
        final int position = pos;
        Call<Map<String, Boolean>> call;
        if(position < records.size()){
            String id = records.get(position).getId()+"";
            IDropRecordAPIService iDropRecordAPIService =
                    HistoryFragment.retrofit.create(IDropRecordAPIService.class);
            call = iDropRecordAPIService.dropRecord(id, "whole");
        }else{
            String id = certainRecords.get(position - records.size()).getId()+"";
            IDropRecordAPIService iDropRecordAPIService =
                    HistoryFragment.retrofit.create(IDropRecordAPIService.class);
            call = iDropRecordAPIService.dropRecord(id, "certain");
        }
        call.enqueue(new Callback<Map<String, Boolean>>() {
            @Override
            public void onResponse(Call<Map<String, Boolean>> call, Response<Map<String, Boolean>> response) {
                ((HistoryFragment)myFragment).myMainActivity.progressBar.setVisibility(View.INVISIBLE);
                try{
                    Map<String, Boolean> result = response.body();
                    if(result.get("status")){
                        Toast.makeText(mContext, "删除成功", Toast.LENGTH_LONG).show();

                        if(position < records.size()){
                            records.remove(position);
                        }else {
                            certainRecords.remove(position - records.size());
                        }
                        if(records.size() == 0 && certainRecords.size() == 0){
                            ((HistoryFragment)myFragment).no_historyHint.setVisibility(View.VISIBLE);
                        }else {
                            ((HistoryFragment)myFragment).no_historyHint.setVisibility(View.INVISIBLE);
                        }
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
                ((HistoryFragment)myFragment).myMainActivity.progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(mContext, "请检查网络连接", Toast.LENGTH_LONG).show();
                Log.e("removeItem", t.toString());
            }
        });
    }

    protected void  setRecords(List<Record> records){
        this.records = records;
    }

    protected void  setCertainRecords(List<CertainRecord> certainRecords){
        this.certainRecords = certainRecords;
    }

    @Override
    public int getItemViewType(int position) {
        if(position<records.size())
            return 0;
        else
            return 1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if(viewType == 0){
            View view = LayoutInflater.from(mContext).inflate(R.layout.history_item, viewGroup, false);
            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
            return new MyViewHolder(view);
        }else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.ticket_item, viewGroup, false);
            view.setOnLongClickListener(this);
            return new MyCertainViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof MyViewHolder){
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
        }else if(holder instanceof MyCertainViewHolder){
            MyCertainViewHolder myCertainViewHolder = (MyCertainViewHolder) holder;
            CertainRecord certainRecord = certainRecords.get(position - records.size());
            myCertainViewHolder.start_time.setText(certainRecord.getStartTime());
            myCertainViewHolder.end_time.setText(certainRecord.getEndTime());
            myCertainViewHolder.start_place.setText(MainActivity.reversePlaceMap.get(certainRecord.getStartFrom()));
            myCertainViewHolder.end_place.setText(MainActivity.reversePlaceMap.get(certainRecord.getEndTo()));
            myCertainViewHolder.train_num.setText(certainRecord.getTrainId());
            myCertainViewHolder.lishi.setText(certainRecord.getLishi());
            if(certainRecord.getTicketLeft().equals("1")){
                myCertainViewHolder.status.setTextColor(mContext.getResources().getColor(android.R.color.holo_green_dark));
                myCertainViewHolder.status.setText("有票");
            }else {
                myCertainViewHolder.status.setTextColor(Color.RED);
                myCertainViewHolder.status.setText("无票");
            }
            myCertainViewHolder.seats_first_num.setText(certainRecord.getFirstSeats());
            myCertainViewHolder.seats_second_num.setText(certainRecord.getSecondSeats());
            myCertainViewHolder.seats_rw_num.setText(certainRecord.getRw());
            myCertainViewHolder.seats_yw_num.setText(certainRecord.getYw());
            myCertainViewHolder.seats_yz_num.setText(certainRecord.getYz());
            myCertainViewHolder.seats_wz_num.setText(certainRecord.getWz());
            myCertainViewHolder.ticket_date.setText(certainRecord.getDate());
            myCertainViewHolder.ticket_type.
                    setText(certainRecord.getTicketType().equals("ADULT")?"成人票":"学生票");
        }

    }

    @Override
    public int getItemCount() {
        return records.size()+certainRecords.size();
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

    private class MyCertainViewHolder extends RecyclerView.ViewHolder {
        protected TextView start_time;
        protected TextView end_time;
        protected TextView start_place;
        protected TextView end_place;
        protected TextView train_num;
        protected TextView lishi;
        protected TextView status;
        protected TextView seats_first_num;
        protected TextView seats_second_num;
        protected TextView seats_rw_num;
        protected TextView seats_yw_num;
        protected TextView seats_yz_num;
        protected TextView seats_wz_num;
        protected TextView ticket_date;
        protected TextView ticket_type;


        public MyCertainViewHolder(View view)
        {
            super(view);
            start_time         =(TextView) view.findViewById(R.id.start_time);
            end_time           =(TextView) view.findViewById(R.id.end_time);
            start_place        =(TextView) view.findViewById(R.id.start_place);
            end_place          =(TextView) view.findViewById(R.id.end_place);
            train_num          =(TextView) view.findViewById(R.id.train_num);
            lishi              =(TextView) view.findViewById(R.id.lishi);
            status             =(TextView) view.findViewById(R.id.status);
            seats_first_num    =(TextView) view.findViewById(R.id.seats_first_num);
            seats_second_num   =(TextView) view.findViewById(R.id.seats_second_num);
            seats_rw_num       =(TextView) view.findViewById(R.id.seats_rw_num);
            seats_yw_num       =(TextView) view.findViewById(R.id.seats_yw_num);
            seats_yz_num       =(TextView) view.findViewById(R.id.seats_yz_num);
            seats_wz_num       =(TextView) view.findViewById(R.id.seats_wz_num);
            ticket_date        =(TextView) view.findViewById(R.id.ticket_date);
            ticket_type        =(TextView) view.findViewById(R.id.ticket_type);
        }
    }
}

