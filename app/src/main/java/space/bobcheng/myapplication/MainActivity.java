package space.bobcheng.myapplication;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity implements HistoryFragment.MyCallback{
    private Fragment checkfragment;
    private Fragment historyFragment;
    private FragmentManager mFragmentManager;
    private Toolbar toolbar;
    private TextView title;
    public static HashMap<String, String> placeMap;
    public static HashMap<String, String> reversePlaceMap;
    private BottomNavigationView navigation;
    private ViewPager mViewpager;
    private ArrayList<Fragment> mViews = new ArrayList<>();
    public static String myusername = "czy@gmail.com";//debug 先写死
    public static String myemail = "123123@gmail.com"; //debug 先写死

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_dashboard:
                    mViewpager.setCurrentItem(0, true);
                    title.setText(getResources().getString(R.string.title_check));
                    return true;
                case R.id.navigation_notifications:
                    mViewpager.setCurrentItem(1, true);
                    title.setText(getResources().getString(R.string.title_history));
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getPlaceMap();
        getReversePlacemap();
        // sayHello(); //debug 暂时不用调用
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        mViewpager = (ViewPager) findViewById(R.id.content);
        title = (TextView) findViewById(R.id.title);
        checkfragment = new CheckFragment();
        historyFragment = new HistoryFragment();
        mFragmentManager = getSupportFragmentManager();

        mViews.clear();
        mViews.add(checkfragment);
        mViews.add(historyFragment);



        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        // 添加返回按钮
        if(getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_dashboard);
        title.setText(getResources().getString(R.string.title_check));
        setmViewpagerLogic();


    }

    private void getPlaceMap(){
        try{
            InputStream jsonis = getAssets().open("stations.json");
            InputStreamReader jsonisr = new InputStreamReader(jsonis,"UTF-8");
            //json解析成map,Gson还是强大。
            Gson gson = new Gson();
            placeMap = gson.fromJson(jsonisr, new TypeToken<HashMap<String, String>>(){}.getType());
        }catch (IOException e){
            Log.e("init_placeMap", e.toString());
        }
    }
    private void getReversePlacemap(){
        reversePlaceMap = new HashMap<>();
        Set<Map.Entry<String, String>> set = placeMap.entrySet();
        Iterator<Map.Entry<String, String>> iterator = set.iterator();
        while (iterator.hasNext()){
            Map.Entry<String, String> entry = iterator.next();
            reversePlaceMap.put(entry.getValue(), entry.getKey());
        }
    }

    private void sayHello(){
        Bundle data = getIntent().getExtras();
        myusername = data.getString("username");
        myemail = data.getString("email");
        if(data.getBoolean("signup")){
            Toast.makeText(getApplicationContext(), "注册成功,你好"+myusername, Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(getApplicationContext(), "登录成功,你好"+myusername, Toast.LENGTH_LONG).show();
        }
    }

    private void setmViewpagerLogic(){
        mViewpager.setAdapter(new FragmentPagerAdapter(mFragmentManager) {
            @Override
            public Fragment getItem(int position) {
                return  mViews.get(position);
            }

            @Override
            public int getCount() {
                return mViews.size();
            }
        });

        mViewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        navigation.setSelectedItemId(R.id.navigation_dashboard);
                        break;
                    case 1:
                        navigation.setSelectedItemId(R.id.navigation_notifications);
                        break;
                    default:
                        break;
                }
            }
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    @Override
    public void onChangeFragment() {
        navigation.setSelectedItemId(R.id.navigation_dashboard);
    }
}
