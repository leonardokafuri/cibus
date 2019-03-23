package com.example.leonardokafuri.cibus;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class Payment extends AppCompatActivity {
    DatabaseHelper dbh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        dbh = new DatabaseHelper(this);
        final EditText number = findViewById(R.id.CardNum);
        final EditText name = findViewById(R.id.cardName);
        final EditText date = findViewById(R.id.Expire);
        final EditText type = findViewById(R.id.CardType);
        Button submit = findViewById(R.id.submitOrd);
        final CheckBox saveCC = findViewById(R.id.saveCC);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(saveCC.isChecked())
               {
                    try {
                        boolean saved = dbh.saveCC(number.getText().toString(), name.getText().toString(), date.getText().toString(), type.getText().toString());
                        if(!saved)
                            Toast.makeText(Payment.this, "Something went wrong, please check your info and try again!", Toast.LENGTH_LONG).show();
                        else
                        {
                            Toast.makeText(Payment.this, "Credit Card successfully saved", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(Payment.this,History.class));
                        }
                    }catch(Exception e)
                    {
                        Toast.makeText(Payment.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                else
                startActivity(new Intent(Payment.this,History.class));


            }
        });

    }

    // 3 dot menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id)
        {
            case R.id.home:
                startActivity(new Intent(Payment.this,AccountInfo.class));
                break;
            case R.id.History:
                startActivity(new Intent(Payment.this,History.class));
                break;
            case R.id.Promotion:
                startActivity(new Intent(Payment.this,Promotion.class));
                break;
            case R.id.logout:
                startActivity(new Intent(Payment.this,Login.class));
                break;
        }
        return true;
    }
}
