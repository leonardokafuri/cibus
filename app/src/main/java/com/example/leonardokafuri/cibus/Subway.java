package com.example.leonardokafuri.cibus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class Subway extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subway);

        //calculate the prices on the checkboxes and add to a price pass the price to the next screen

        Button order = findViewById(R.id.order);
        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Subway.this,OrderConfirmation.class));
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
                startActivity(new Intent(Subway.this,AccountInfo.class));
                break;
            case R.id.History:
                startActivity(new Intent(Subway.this,History.class));
                break;
            case R.id.Promotion:
                startActivity(new Intent(Subway.this,Promotion.class));
                break;
            case R.id.logout:
                startActivity(new Intent(Subway.this,Login.class));
                break;
        }
        return true;
    }
}
