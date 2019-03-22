package com.example.leonardokafuri.cibus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class Promotion extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promotion);
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
                startActivity(new Intent(Promotion.this,AccountInfo.class));
                break;
            case R.id.History:
                startActivity(new Intent(Promotion.this,History.class));
                break;
            case R.id.Promotion:
                Toast.makeText(this,"You already are on the Promotion Page",Toast.LENGTH_LONG).show();
                break;
            case R.id.logout:
                startActivity(new Intent(Promotion.this,Login.class));
                break;
        }
        return true;
    }
}
