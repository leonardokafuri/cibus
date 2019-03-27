package com.example.leonardokafuri.cibus;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.leonardokafuri.cibus.ui.Restaurants;
import com.example.leonardokafuri.cibus.utils.DatabaseHelper;

public class History extends AppCompatActivity {

    DatabaseHelper dbh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        dbh = new DatabaseHelper(this);
        Button back = findViewById(R.id.backhome);
        TextView userHistory = findViewById(R.id.orders);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        int id = sp.getInt("key1",0);//retriving which user is using the app by checking his id

        Cursor c = dbh.viewHistory(id);
        StringBuilder str = new StringBuilder();
            if (c.getCount() > 0) {
                while (c.moveToNext()) {
                    str.append("Order ID : " + c.getString(0));
                    str.append("            ");
                    str.append(" Restaurant Name : " + c.getString(2));
                    str.append("\n");
                    str.append("Time : " + c.getString(3));
                    str.append("\n");
                    str.append("Amount : " + c.getString(4));
                    str.append("\n");
                    str.append("-----------------------------------------------------------------------------------------");
                    str.append("\n");
                }
                userHistory.setText(str);
            } else {
                //display a toast message
                Toast.makeText(History.this, "No orders found", Toast.LENGTH_SHORT).show();
            }



        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(History.this, Restaurants.class));
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
