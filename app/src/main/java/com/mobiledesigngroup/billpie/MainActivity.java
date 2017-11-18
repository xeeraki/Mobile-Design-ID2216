package com.mobiledesigngroup.billpie;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View cardView = findViewById(R.id.cardView2);

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.customdilog,null);
                final EditText title = (EditText) mView.findViewById(R.id.title);
                final EditText amount = (EditText)mView.findViewById(R.id.amount);
                Button btnSubmit = (Button)mView.findViewById(R.id.btn_submit);
                Button btnCancel = (Button)mView.findViewById(R.id.btn_cancel);

                btnSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!title.getText().toString().isEmpty()&&!amount.getText().toString().isEmpty()){
                            Toast.makeText(MainActivity.this,"submitted",Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(MainActivity.this,"add title and amount",Toast.LENGTH_SHORT).show();
                        }
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

    }}