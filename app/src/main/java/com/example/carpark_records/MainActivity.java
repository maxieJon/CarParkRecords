package com.example.carpark_records;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private EditText txtno;
    private Button btnotp;
    private FirebaseAuth mauth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mauth= FirebaseAuth.getInstance();
        txtno=(EditText) findViewById(R.id.txtno);

        findViewById(R.id.btnotp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mobile =txtno.getText().toString().trim();
                if (mobile.isEmpty() || mobile.length()<10){
                    txtno.setError("Enter A Valid Phone Number");
                    txtno.requestFocus();
                    return;
                }
                Intent intent= new Intent(MainActivity.this,SignUp.class);
                intent.putExtra("mobile", mobile);
                startActivity(intent);
            }
        });
    }
}