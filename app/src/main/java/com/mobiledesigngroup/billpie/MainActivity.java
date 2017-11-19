package com.mobiledesigngroup.billpie;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
//object
    Database database ;
    EditText amount;
    EditText title;
    Button btnSubmit;
    Button  btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        database = new Database(this);

        View cardView = findViewById(R.id.cardView2);
        View cardView1 = findViewById(R.id.cardView1);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.customdilog,null);
                title = (EditText) mView.findViewById(R.id.titleText);
                amount = (EditText)mView.findViewById(R.id.amountText);
                btnSubmit = (Button)mView.findViewById(R.id.btn_submit);
                btnCancel = (Button)mView.findViewById(R.id.btn_cancel);

                btnSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //when submit clicked call insert method from database
                       boolean isInserted =  database.insertData(title.getText().toString(),
                                amount.getText().toString());

                        if(isInserted==true){
                            Toast.makeText(MainActivity.this,"submitted",Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(MainActivity.this,"Not inserted",Toast.LENGTH_SHORT).show();
                        }


                        Intent intent = new Intent(MainActivity.this,MainActivity.class);
                        startActivity(intent);
                        finish();
                    }

                });

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });

                dialogBuilder.setView(mView);
                AlertDialog dialog = dialogBuilder.create();
                dialog.show();
            }

        });


        cardView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,ListBills.class);
                startActivity(intent);
            }
        });


    }
}