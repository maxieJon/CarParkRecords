package com.example.carpark_records;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Database extends AppCompatActivity {

    private EditText txtvehicle, txtplate, txtdate,txttime,txtamount;
    private Button btnupdate, btnview, btndelete;

    DBhelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database);
        txtvehicle=(EditText) findViewById(R.id.txtvehicle);
        txtplate=(EditText) findViewById(R.id.txtplate);
        txtdate=(EditText) findViewById(R.id.txtdate);
        txttime=(EditText) findViewById(R.id.txttime);
        txtamount=(EditText) findViewById(R.id.txtamount);

        btndelete=(Button) findViewById(R.id.btndelete);
        btnview=(Button) findViewById(R.id.btnview);
        btnupdate=(Button) findViewById(R.id.btnupdate);
        db=new DBhelper(this);

        btnupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String vehicleTXT= txtvehicle.getText().toString();
                String plateTXT= txtplate.getText().toString();
                String dateTXT= txtdate.getText().toString();
                String timeTXT= txttime.getText().toString();
                String amountTXT= txtamount.getText().toString();

                Boolean insertdata= db.insertdata(vehicleTXT,plateTXT,dateTXT,timeTXT,amountTXT);
                if (insertdata==true)
                    Toast.makeText(Database.this, "Entry Made", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(Database.this, "Entry Not Made", Toast.LENGTH_SHORT).show();
            }
        });

        btndelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String vehicleTXT= txtvehicle.getText().toString();
                Boolean deletedata= db.deletedata(vehicleTXT);
                if (deletedata == true)
                    Toast.makeText(Database.this, "Entry Deleted", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(Database.this, "Delete Failed", Toast.LENGTH_SHORT).show();

            }
        });
        btnview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor res= db.getdata();
                if (res.getCount()==0){
                    Toast.makeText(Database.this, "No Entry Available", Toast.LENGTH_SHORT).show();
                    return;
                }
                StringBuffer buffer =new StringBuffer();
                while (res.moveToNext()){
                    buffer.append("vehicle :" +res.getString(0)+"\n");
                    buffer.append("plate :" +res.getString(1)+"\n");
                    buffer.append("date :" +res.getString(2)+"\n\n");
                    buffer.append("time :" +res.getString(3)+"\n\n\n");
                    buffer.append("amount :" +res.getString(4)+"\n\n\n\n");
                }
                AlertDialog.Builder builder= new AlertDialog.Builder(Database.this);
                builder.setCancelable(true);
                builder.setTitle("Parkrecords");
                builder.setMessage(buffer.toString());
                builder.show();
            }
        });
    }
}