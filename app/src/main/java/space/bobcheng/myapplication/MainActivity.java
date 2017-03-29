package space.bobcheng.myapplication;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements HistoryFragment.MyCallback{
    private Fragment checkfragment;
    private Fragment historyFragment;
    private Toolbar toolbar;
    private TextView title;
    private BottomNavigationView navigation;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_dashboard:
                    getFragmentManager().beginTransaction().replace(R.id.content, checkfragment).commit();
                    title.setText(getResources().getString(R.string.title_check));
                    return true;
                case R.id.navigation_notifications:
                    getFragmentManager().beginTransaction().replace(R.id.content, historyFragment).commit();
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
        checkfragment = new CheckFragment();
        historyFragment = new HistoryFragment();
        title = (TextView) findViewById(R.id.title);
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

    }

    @Override
    public void changeFragment() {
        navigation.setSelectedItemId(R.id.navigation_dashboard);
    }
}
