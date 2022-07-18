package com.example.carpark_records;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class SignUp extends AppCompatActivity {
    private EditText txtotp;
    private Button btnsignup;
    private FirebaseAuth mauth;
    private String mVerificationId;
    String mobile;
    String txtno;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mauth= FirebaseAuth.getInstance();
        txtotp=(EditText) findViewById(R.id.txtotp);

        Intent intent=getIntent();
        txtno=intent.getStringExtra("mobile");
        sendVerificationCode(mobile);

        findViewById(R.id.btnsignup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               String code = txtotp.getText().toString().trim();
               if (code.isEmpty() || code.length()<6){
                   txtotp.setError("Enter A Valid Code");
                   txtotp.requestFocus();
                   return;
               }
               verifycode(code);
            }
        });
    }

    private void verifycode(String code)
    {
        PhoneAuthCredential credential= PhoneAuthProvider.getCredential(mVerificationId, code);

        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential)
    {
        mauth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Intent intent= new Intent(SignUp.this, Database.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            Toast.makeText(SignUp.this,"Phone: " +mobile, Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            String message = "Something Is Wrong We Will Get Back To You";

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException){
                                message= "Invalid Code Entered";
                            }
                            Snackbar snackbar= Snackbar.make(findViewById(R.id.txtno), message,Snackbar.LENGTH_LONG);
                            snackbar.setAction("Dismiss", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                }
                            });
                            snackbar.show();
                        }
                    }
                });
    }

    private void sendVerificationCode(String mobile)
    {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mauth)
                        .setPhoneNumber("+254" +txtno)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks= new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();

            if (code !=null){
                txtotp.setText(code);
                verifycode(code);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(SignUp.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            mVerificationId= s;
        }
    };
}