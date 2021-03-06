package com.example.user.snapkart;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUp extends AppCompatActivity {
    private EditText mEmailField,mPasswordField,mConfirmPassword,mMobileField,mNameField,mAddressField;
    private Button mSignBtn;
    private FirebaseAuth mAuth;
    private ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        pd = new ProgressDialog(SignUp.this);
        pd.setMessage("Please Wait...");
        mEmailField = findViewById(R.id.emailField);
        mPasswordField = findViewById(R.id.passwordField);
        mConfirmPassword = findViewById(R.id.confirmPasswordField);
        mMobileField = findViewById(R.id.mobileField);
        mNameField = findViewById(R.id.nameField);
        mAddressField = findViewById(R.id.addressField);
        mSignBtn = findViewById(R.id.signupBtn);
        mAuth = FirebaseAuth.getInstance();
        mNameField.setText("Monis");
        mEmailField.setText("456@test.com");
        mPasswordField.setText("79918031");
        mConfirmPassword.setText("79918031");
        mMobileField.setText("79918031");
        mAddressField.setText("bsdubg jgeijsei grhhrtd hgggwuiu8e  hkuhrbf");
        mSignBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Email = mEmailField.getText().toString();
                String Password = mPasswordField.getText().toString();
                String Name = mNameField.getText().toString();
                String Mobile = mMobileField.getText().toString();
                String Address = mAddressField.getText().toString();
                pd.show();
                signup(Email, Password, Name, Mobile,Address);
            }
        });
    }

    private void signup(final String email, String password, final String name, final String mobile, final String address) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            if(pd.isShowing())
                                pd.dismiss();
                            Intent i = new Intent(SignUp.this,HomeActivity.class);
                            i.putExtra("email",email);
                            i.putExtra("mobile",mobile);
                            i.putExtra("name",name);
                            i.putExtra("address",address);
                            PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putBoolean("login", Boolean.FALSE).apply();
                            startActivity(i);
                            SignUp.this.finish();
                        }
                        else {
                            if(pd.isShowing())
                                pd.dismiss();
                            Toast.makeText(getApplicationContext(),"Email Already exists", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}
