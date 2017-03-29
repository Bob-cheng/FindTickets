package space.bobcheng.myapplication;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

public class SignUpActivity extends AppCompatActivity {
    private EditText username;
    private EditText email;
    private EditText passwd;
    private EditText passwd_again;
    private TextView backToSignIn;
    private ConstraintLayout root_layout;
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

        View.OnFocusChangeListener  focusChangeListener  = new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    v.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.circleshape));
                    ObjectAnimator in_animator = ObjectAnimator.ofInt(v.getBackground(), "Alpha", 0, 100).setDuration(500);
                    in_animator.start();
                }else{
                    ObjectAnimator out_animator = ObjectAnimator.ofInt(v.getBackground(), "Alpha", 100, 0).setDuration(500);
                    out_animator.start();
                    InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
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
    }

    protected void toSignIn(View v){
        Intent toSignIn = new Intent(this, SignInActivity.class);
        startActivity(toSignIn);
    }
}
