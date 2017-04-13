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
import android.widget.TextView;

import java.util.Map;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import space.bobcheng.myapplication.apiService.IsignUpAPIService;
import space.bobcheng.myapplication.jsonClass.MyQuery;
import space.bobcheng.myapplication.jsonClass.SignUpInfo;

import static space.bobcheng.myapplication.MyUtlis.MD5.getMd5;
import static space.bobcheng.myapplication.MyUtlis.UnsafeHttp.getUnsafeOkHttpClient;

public class SignUpActivity extends AppCompatActivity {
    private EditText username;
    private EditText email;
    private EditText passwd;
    private EditText passwd_again;
    private TextView backToSignIn;
    private Button mSubmmit;
    private ConstraintLayout root_layout;
    private Retrofit retrofit;
    protected static final String BASE_URL = "http://23.83.231.104:8080/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        root_layout = (ConstraintLayout)findViewById(R.id.root_layout);
        root_layout.setFocusable(true);
        root_layout.setFocusableInTouchMode(true);
        root_layout.requestFocus();
        username = (EditText)findViewById(R.id.user_name);
        email = (EditText)findViewById(R.id.email);
        passwd = (EditText)findViewById(R.id.passwd);
        passwd_again = (EditText)findViewById(R.id.passwd_again);
        backToSignIn = (TextView)findViewById(R.id.sign_in);
        mSubmmit = (Button) findViewById(R.id.button);
        TextView to_sign_in = (TextView) findViewById(R.id.sign_in);

        to_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toSignIn = new Intent(SignUpActivity.this, SignInActivity.class);
                startActivity(toSignIn);
                SignUpActivity.this.finish();
            }
        });

        View.OnFocusChangeListener  focusChangeListener  = new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    v.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.circleshape));
                    ObjectAnimator in_animator = ObjectAnimator.ofInt(v.getBackground(), "Alpha", 0, 100).setDuration(500);
                    in_animator.start();
                    openSoftKeyBord(getApplicationContext(), v);
                }else{
                    ObjectAnimator out_animator = ObjectAnimator.ofInt(v.getBackground(), "Alpha", 100, 0).setDuration(500);
                    out_animator.start();
                    closeSoftKeyBord(getApplicationContext(), v);
                }

            }
        };
        username.setOnFocusChangeListener(focusChangeListener);
        email.setOnFocusChangeListener(focusChangeListener);
        passwd.setOnFocusChangeListener(focusChangeListener);
        passwd_again.setOnFocusChangeListener(focusChangeListener);
        root_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.requestFocus();
            }
        });

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mSubmmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username_value = username.getText().toString();
                String email_value = email.getText().toString();
                String password_value = passwd.getText().toString();
                String password_again_value = passwd_again.getText().toString();
                if(checkInfo(username_value, email_value, password_value, password_again_value)){
                    toSignUp(username_value, email_value, getMd5(password_value));
                }
            }
        });
    }

    private boolean checkInfo(String username_v, String email_v, String psw_v, String psw_again_v){
        Pattern username_p = Pattern.compile("^\\w{3,10}$"); //3到10位字母或数字
        Pattern email_p = Pattern.compile("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$");
        Pattern password_p = Pattern.compile("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,10}$"); //包含大小写字母和数字，长度6-10
        if(!username_p.matcher(username_v).matches()){
            Snackbar.make(root_layout, "用户名为3到10位字母或数字", Snackbar.LENGTH_LONG).setAction("修改", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    username.requestFocus();
                }
            }).show();
        }else if(!email_p.matcher(email_v).matches()){
            Snackbar.make(root_layout, "请输入正确邮箱地址", Snackbar.LENGTH_LONG).setAction("修改", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    email.requestFocus();
                }
            }).show();
        }else if(!password_p.matcher(psw_v).matches()){
            Snackbar.make(root_layout, "密码包含大小写字母和数字，长度6-10", Snackbar.LENGTH_LONG).setAction("修改", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    passwd.requestFocus();
                }
            }).show();
        }else if(!psw_v.equals(psw_again_v)){
            Snackbar.make(root_layout, "两次密码需要相同", Snackbar.LENGTH_LONG).setAction("修改", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    passwd_again.requestFocus();
                }
            }).show();
        }else {
            return true;
        }
        return false;

    }

    private void toSignUp(String username, String email, String pwd){
        IsignUpAPIService isignUpAPIService = retrofit.create(IsignUpAPIService.class);
        Call<SignUpInfo> call = isignUpAPIService.sign_up(username, email, pwd);
        // map的结构
        // {"email_valid": false, "creat_status": false, "username": "undefine", "email": "undefine"}
        call.enqueue(new Callback<SignUpInfo>() {
            @Override
            public void onResponse(Call<SignUpInfo> call, Response<SignUpInfo> response) {
                Log.i("statusCode", response.code()+"");
                try{
                    SignUpInfo signUpInfo = response.body();
                    if(!signUpInfo.getEmailValid()){
                        Snackbar.make(root_layout, "邮箱已被注册，请选择其他邮箱或登录", Snackbar.LENGTH_LONG)
                                .setAction("修改", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        SignUpActivity.this.email.requestFocus();
                                    }
                                }).show();
                    }else if(signUpInfo.getCreatStatus()){
                        launchMainActivity(signUpInfo.getUsername(), signUpInfo.getEmail());
                    }else {
                        throw new Exception("服务器错误，创建失败");
                    }
                }catch (Exception e){
                    Snackbar.make(root_layout, "服务器数据错误，请重试", Snackbar.LENGTH_LONG)
                            .setAction("重试", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mSubmmit.callOnClick();
                                }
                            }).show();
                }
            }
            @Override
            public void onFailure(Call<SignUpInfo> call, Throwable t) {
                Log.e("network_error", t.toString());
                Snackbar.make(root_layout, "网络连接失败，请重试", Snackbar.LENGTH_LONG)
                        .setAction("重试", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mSubmmit.callOnClick();
                            }
                        }).show();
            }
        });
    }

    private void launchMainActivity(String username, String email){
        Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
        Bundle data = new Bundle();
        data.putString("username", username);
        data.putString("email", email);
        data.putBoolean("signup", true);
        intent.putExtras(data);
        startActivity(intent);
        this.finish();
    }
    //打开输入法
    private void openSoftKeyBord(Context context, View v){
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(v, 0);
    }

    //收起输入法
    private void closeSoftKeyBord(Context context, View v){
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }
}
