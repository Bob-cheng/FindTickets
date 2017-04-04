package space.bobcheng.myapplication;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements HistoryFragment.MyCallback{
    private Fragment checkfragment;
    private Fragment historyFragment;
    private FragmentManager mFragmentManager;
    private Toolbar toolbar;
    private TextView title;
    private BottomNavigationView navigation;
    private ViewPager mViewpager;
    private ArrayList<Fragment> mViews = new ArrayList<>();

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
