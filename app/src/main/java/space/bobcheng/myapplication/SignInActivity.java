package space.bobcheng.myapplication;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import space.bobcheng.myapplication.apiService.IsignInAPIService;
import space.bobcheng.myapplication.apiService.IsignUpAPIService;
import space.bobcheng.myapplication.jsonClass.SignInInfo;

import static space.bobcheng.myapplication.MyUtlis.MD5.getMd5;


public class SignInActivity extends AppCompatActivity {
    private EditText myusername;
    private EditText mypassword;
    private ConstraintLayout activitymain;
    private Retrofit retrofit;
    private Button mButton;
    private static final String BASE_URL = "http://23.83.231.104:8080/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        myusername = (EditText)findViewById(R.id.username);
        mypassword = (EditText)findViewById(R.id.passwd);
        activitymain = (ConstraintLayout) findViewById(R.id.activity_main);
        mButton = (Button) findViewById(R.id.button);
        activitymain.setFocusable(true);
        activitymain.setFocusableInTouchMode(true);
        activitymain.requestFocus();
        myusername.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.circleshape));
        myusername.getBackground().setAlpha(0);
        mypassword.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.circleshape));
        mypassword.getBackground().setAlpha(0);
        View.OnFocusChangeListener focuslistener = new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    ObjectAnimator in_animator = ObjectAnimator.ofInt(v.getBackground(), "Alpha", 0, 100);
                    in_animator.setDuration(500);
                    in_animator.start();
                }else{
                    ObjectAnimator out_animator = ObjectAnimator.ofInt(v.getBackground(), "Alpha", 100, 0);
                    out_animator.setDuration(500);
                    out_animator.start();
                    InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        };

        myusername.setOnFocusChangeListener(focuslistener);
        mypassword.setOnFocusChangeListener(focuslistener);
        activitymain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.requestFocus();
            }
        });
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = myusername.getText().toString();
                String password = getMd5(mypassword.getText().toString());
                signIn(email, password);
            }
        });



    }

    private void signIn(String email, String password){

        IsignInAPIService isignInAPIService = retrofit.create(IsignInAPIService.class);
        Call<SignInInfo> call  = isignInAPIService.sign_in(email, password);
        call.enqueue(new Callback<SignInInfo>() {
            @Override
            public void onResponse(Call<SignInInfo> call, Response<SignInInfo> response) {
                Log.i("sign_in_status", response.code()+"");
                try {
                    SignInInfo signInInfo = response.body();
                    if(signInInfo.getUserExist()){
                        if(signInInfo.getPwdValid()){
                            launchMainActivity(signInInfo.getUsername(), signInInfo.getEmail());
                        }else {
                            Snackbar.make(activitymain, "密码错误请重新输入", Snackbar.LENGTH_LONG)
                                    .setAction("修改", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            mypassword.requestFocus();
                                        }
                                    }).show();
                        }
                    }else {
                        Snackbar.make(activitymain, "无此用户，请检查邮箱或注册", Snackbar.LENGTH_LONG)
                                .setAction("修改", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        myusername.requestFocus();
                                    }
                                }).show();
                    }
                }catch (Exception e){
                    Log.e("sign_in_error", e.toString());
                }
            }

            @Override
            public void onFailure(Call<SignInInfo> call, Throwable t) {
                Log.e("network_error", t.toString());
                Snackbar.make(activitymain, "网络连接失败，请重试", Snackbar.LENGTH_LONG)
                        .setAction("重试", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mButton.callOnClick();
                            }
                        }).show();
            }
        });

    }

    private void launchMainActivity(String username, String email){
        Intent intent = new Intent(SignInActivity.this, MainActivity.class);
        Bundle data = new Bundle();
        data.putString("username", username);
        data.putString("email", email);
        data.putBoolean("signup", false);
        intent.putExtras(data);
        startActivity(intent);
    }
    protected void gotoSignUp(View v){
        Intent toSignUp = new Intent(this, SignUpActivity.class);
        startActivity(toSignUp);
    }

}
