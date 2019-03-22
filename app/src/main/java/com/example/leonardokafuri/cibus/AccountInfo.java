package com.example.leonardokafuri.cibus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class AccountInfo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_info);
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
                Toast.makeText(this,"You already are on the Account Settings Page",Toast.LENGTH_LONG).show();
                break;
            case R.id.History:
                startActivity(new Intent(AccountInfo.this,History.class));
                break;
            case R.id.Promotion:
                startActivity(new Intent(AccountInfo.this,Promotion.class));
                break;
            case R.id.logout:
                startActivity(new Intent(AccountInfo.this,Login.class));
                break;
        }
        return true;
    }

}
