package com.example.leonardokafuri.cibus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class History extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        Button back = findViewById(R.id.backhome);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(History.this,Restaurants.class));
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
                startActivity(new Intent(History.this,AccountInfo.class));
                break;
            case R.id.History:
                Toast.makeText(this,"You already are on the Order History Page",Toast.LENGTH_LONG).show();
                break;
            case R.id.Promotion:
                startActivity(new Intent(History.this,Promotion.class));
                break;
            case R.id.logout:
                startActivity(new Intent(History.this,Login.class));
                break;
        }
        return true;
    }
}
