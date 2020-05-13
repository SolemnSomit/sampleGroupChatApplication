package com.example.samplegroupchatapplication.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;

import com.example.samplegroupchatapplication.R;
import com.example.samplegroupchatapplication.utils.StringUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class Login extends AppCompatActivity {

    public static final String TAG = "Login Activity";
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private TextInputLayout mInputLayout;
    private TextInputEditText mEmailID;
    private ProgressBar mProgressBar;
    private Button mLoginButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //checkIfLoggedIn
        checkIfUserLoggedIn();
        //Setup Views
        mProgressBar = findViewById(R.id.loginProgressBar);
        mLoginButton = findViewById(R.id.loginButton);
        mInputLayout = findViewById(R.id.textInputLayout);
        mEmailID = findViewById(R.id.emailInput);
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser(Objects.requireNonNull(mEmailID.getText()).toString());
            }
        });
        hideSoftKeyboard();

    }


    //Firebase functions

    /**
     * Check if user is logged in, if it is move to next activity
     */
    private void checkIfUserLoggedIn()
    {
        if(FirebaseAuth.getInstance().getCurrentUser()!=null)
            startChatActivity();
    }

    /**
     * Login and move to chat activity, if can't login display errors.
     * Password for every user set to "123456"
     * @param emailID
     */
    private void loginUser(String emailID)
    {
        if (StringUtils.isEmpty(emailID)) {
            mEmailID.setError("Can't be Empty");
            return;
        }
        if(StringUtils.isValidEmailID(emailID))
        {
            mEmailID.setError("Not Valid Email ID");
        }
        showDialog();
        FirebaseAuth.getInstance().signInWithEmailAndPassword(emailID.trim(),"Password123456")
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        hideDialog();

                        startChatActivity();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        hideDialog();
                        Log.e(TAG,"Can't Sing in",e);
                        mEmailID.setError("Can't sign in using this ID");
                    }
                });
    }


    private void startChatActivity()
    {
        Intent intent = new Intent(this, ChatRoomListActivity.class);
        startActivity(intent);
        finish();
    }

    private void showDialog(){
        mProgressBar.setVisibility(View.VISIBLE);

    }

    private void hideDialog(){
        if(mProgressBar.getVisibility() == View.VISIBLE){
            mProgressBar.setVisibility(View.INVISIBLE);
        }
    }

    private void hideSoftKeyboard(){
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }




}
