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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {
    private static final String TAG = "EmailPassword";
    private EditText mEmailField, mPasswordField;
    private Button mLoginBtn, mSignUpBtn;
    private FirebaseAuth mAuth;
    private ProgressDialog pd;
    private DatabaseReference mCustomerReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_login);
        pd = new ProgressDialog(Login.this);
        pd.setMessage("Please Wait...");
        mEmailField = findViewById(R.id.email);
        mPasswordField = findViewById(R.id.password);
        mLoginBtn = findViewById(R.id.loginBtn);
        mSignUpBtn = findViewById(R.id.signupBtn);
        mAuth = FirebaseAuth.getInstance();
        mCustomerReference = FirebaseDatabase.getInstance().getReference().child("Customer");
        mEmailField.setText("456@test.com");
        mPasswordField.setText("79918031");
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Email = mEmailField.getText().toString();
                String Password = mPasswordField.getText().toString();
                pd.show();
                login(Email, Password);
            }
        });
        mSignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Login.this,SignUp.class);
                startActivity(i);
            }
        });
    }
    private void login(final String id, final String password) {
        mAuth.signInWithEmailAndPassword(id, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            if(pd.isShowing())
                                pd.dismiss();

                            mCustomerReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for (DataSnapshot fire : dataSnapshot.getChildren()) {
                                        Customer temp = fire.getValue(Customer.class);
                                        if (temp.getEmail().equals(id)) {
                                            PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("customerId",temp.getCustomerid()).apply();
                                            //Toast.makeText(MyProduct.this,mDataSet.size()+ "", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                            PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putBoolean("loggedIn", Boolean.TRUE).apply();
                            PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putBoolean("login", Boolean.TRUE).apply();
                            Intent i = new Intent(Login.this,HomeActivity.class);
                           startActivity(i);
                           Login.this.finish();
                        }
                        else {
                            if(pd.isShowing())
                                pd.dismiss();
                            Toast.makeText(Login.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}