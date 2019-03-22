package com.example.leonardokafuri.cibus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class Boston extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boston);

        //calculate the prices on the checkboxes and add to a price pass the price to the next screen

        Button order = findViewById(R.id.order);
        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Boston.this,OrderConfirmation.class));
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
                startActivity(new Intent(Boston.this,AccountInfo.class));
                break;
            case R.id.History:
                startActivity(new Intent(Boston.this,History.class));
                break;
            case R.id.Promotion:
                startActivity(new Intent(Boston.this,Promotion.class));
                break;
            case R.id.logout:
                startActivity(new Intent(Boston.this,Login.class));
                break;
        }
        return true;
    }
}
