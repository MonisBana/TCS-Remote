package com.mab.merchantapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUp extends AppCompatActivity {
    private EditText mNameField,mEmailField,mPasswordField,mConfirmPasswordField,mMobileFIeld;
    private Button mSignupBtn;
    private ApiService mApiService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mNameField = findViewById(R.id.nameField);
        mEmailField = findViewById(R.id.emailField);
        mPasswordField = findViewById(R.id.passwordField);
        mConfirmPasswordField = findViewById(R.id.confirmPasswordField);
        mMobileFIeld = findViewById(R.id.mobileField);
        mSignupBtn = findViewById(R.id.signupBtn);
        mApiService = ApiUtils.getAPIService();
        mSignupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               // if(mPasswordField.getText().equals(mConfirmPasswordField.getText())){
                Toast.makeText(SignUp.this, mPasswordField.getText().equals(mConfirmPasswordField.getText())+"", Toast.LENGTH_SHORT).show();
                    String name = mNameField.getText().toString();
                    String email = mEmailField.getText().toString();
                    String password = mPasswordField.getText().toString();
                    String mobile = mMobileFIeld.getText().toString();
                    User user = new User();
                    user.setName(name);
                    user.setEmail(email);
                    user.setPassword(password);
                    user.setMobile(mobile);
                    signup(user);
                //}
               // else
                {
                    Toast.makeText(SignUp.this, "Enter Password Again", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void signup(User user) {
        mApiService.signup(user).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()){
                    if(response.body().getMessage().equals("User created")){
                        Toast.makeText(SignUp.this, "Successfully Signed Up", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(SignUp.this,MainActivity.class);
                        startActivity(i);
                    }
                }

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }
}
