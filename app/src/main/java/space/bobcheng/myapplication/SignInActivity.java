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


public class SignInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        EditText username = (EditText)findViewById(R.id.username);
        EditText password = (EditText)findViewById(R.id.passwd);
        ConstraintLayout activitymain = (ConstraintLayout) findViewById(R.id.activity_main);
        activitymain.setFocusable(true);
        activitymain.setFocusableInTouchMode(true);
        activitymain.requestFocus();
        username.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.circleshape));
        username.getBackground().setAlpha(0);
        password.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.circleshape));
        password.getBackground().setAlpha(0);
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

        username.setOnFocusChangeListener(focuslistener);
        password.setOnFocusChangeListener(focuslistener);
        activitymain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.requestFocus();
            }
        });


    }

    protected void gotoSignUp(View v){
        Intent toSignUp = new Intent(this, SignUpActivity.class);
        startActivity(toSignUp);
    }

}
