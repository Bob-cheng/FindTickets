package space.bobcheng.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

public class TicketsActivity extends AppCompatActivity {
    private CheckBox [] boxs = new CheckBox[6];
    private Integer a;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tickets);

        initCheckBoxs();



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
