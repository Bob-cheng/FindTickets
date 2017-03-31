package space.bobcheng.myapplication;


import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class CheckFragment extends Fragment {
    private EditText start_time;
    private DatePickerDialog mTimeDialog;
    private EditText start_place;
    private EditText end_place;
    private CoordinatorLayout rootLayout;
    private Button mSubmitBtn;
    private HashMap<String, String> placeMap;
    private HashMap<Integer, String> numberMap = NumberMap.map;
    private Calendar today = Calendar.getInstance();
    private Calendar chose = Calendar.getInstance();
    private ArrayList<String> inputMessage = new ArrayList<>();



    public CheckFragment() {
        // Required empty public constructor
    }

    private void getPlaceMap() throws IOException {
        InputStream jsonis = getActivity().getAssets().open("stations.json");
        InputStreamReader jsonisr = new InputStreamReader(jsonis,"UTF-8");
        //json解析成map,Gson还是强大。
        Gson gson = new Gson();
        placeMap = gson.fromJson(jsonisr, new TypeToken<HashMap<String, String>>(){}.getType());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            getPlaceMap();
            Log.i("jsontest", placeMap.get("成都"));
        }catch (IOException e){
            Log.e("error","readJson False");
        }
        //getNumberMap();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mTimeDialog = new DatePickerDialog(getActivity(),
                new DatePickerDialog.OnDateSetListener(){
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        chose.set(year, month, dayOfMonth);
                        setTimeText(year, month, dayOfMonth);
                    }
                }
                , today.get(Calendar.YEAR)
                , today.get(Calendar.MONTH)
                , today.get(Calendar.DAY_OF_MONTH));

        return inflater.inflate(R.layout.fragment_check, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        start_time = (EditText) getView().findViewById(R.id.start_date);
        start_place = (EditText) getView().findViewById(R.id.check_start);
        end_place = (EditText) getView().findViewById(R.id.check_end);
        rootLayout = (CoordinatorLayout) getView().findViewById(R.id.root_layout);
        mSubmitBtn = (Button) getView().findViewById(R.id.submit);

        setTimeText(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH));
        start_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTimeDialog.show();
            }
        });
        start_time.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    mTimeDialog.show();
                }
            }
        });

        View.OnFocusChangeListener loseFcloseKey = new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    closeSoftKeyBord(getActivity(), v);
                }else {
                    openSoftKeyBord(getActivity(), v);
                }
            }
        };

        start_place.setOnFocusChangeListener(loseFcloseKey);
        end_place.setOnFocusChangeListener(loseFcloseKey);

        mSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("click", "YES");
                String start = start_place.getText().toString();
                String end = end_place.getText().toString();
                String time = start_time.getText().toString();
                if(!placeMap.containsKey(start)){
                    Log.i("click", "出发地");
                    Snackbar.make(rootLayout, "出发地不支持", Snackbar.LENGTH_LONG)
                            .setAction("修改", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    start_place.requestFocus();
                                    openSoftKeyBord(getActivity(), start_place);
                                }
                            }).show();

                }else if(!placeMap.containsKey(end)){
                    Log.i("click", "目的地");
                    Snackbar.make(rootLayout, "目的地不支持", Snackbar.LENGTH_LONG)
                            .setAction("修改", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    end_place.requestFocus();
                                    openSoftKeyBord(getActivity(), end_place);
                                }
                            }).show();

                }else if(chose.compareTo(today) == -1){
                    Log.i("click", "时间");
                    Snackbar.make(rootLayout, "请选择正确时间", Snackbar.LENGTH_LONG)
                            .setAction("修改", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    start_time.requestFocus();
                                    start_time.callOnClick();
                                }
                            }).show();
                }else{
                    Log.i("click", "OK");
                    inputMessage.clear();
                    inputMessage.add(placeMap.get(start));
                    inputMessage.add(placeMap.get(end));
                    inputMessage.add(time);
                    Log.i("finalmessage", inputMessage.get(0)+" "+inputMessage.get(1)+" "+inputMessage.get(2));
                }
            }
        });
    }
    //打开输入法
    private void closeSoftKeyBord(Context context, View v){
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }
    //收起输入法
    private void openSoftKeyBord(Context context, View v){
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(v, 0);
    }

    private void setTimeText(int year, int month, int dayOfMonth){
        start_time.setText(year+"-"+numberMap.get(month+1)+"-"+numberMap.get(dayOfMonth));
    }
}
