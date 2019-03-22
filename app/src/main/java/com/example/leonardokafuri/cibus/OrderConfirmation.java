package com.example.leonardokafuri.cibus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class OrderConfirmation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirmation);
        TextView result = findViewById(R.id.result);
        Intent intent = getIntent(); // get the intent that was passed to it
        if(intent!=null)
        {
            String qty = intent.getStringExtra("data"); // get the value that was passed
            result.setText(qty); // setting the textview with the value
        }


        Button proceed = findViewById(R.id.goPay);
        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OrderConfirmation.this,Payment.class));
            }
        });
    }

    //3 dot menu
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
                startActivity(new Intent(OrderConfirmation.this,AccountInfo.class));
                break;
            case R.id.History:
                startActivity(new Intent(OrderConfirmation.this,History.class));
                break;
            case R.id.Promotion:
                startActivity(new Intent(OrderConfirmation.this,Promotion.class));
                break;
            case R.id.logout:
                startActivity(new Intent(OrderConfirmation.this,Login.class));
                break;
        }
        return true;
    }
}
