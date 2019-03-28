package com.example.leonardokafuri.cibus.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.leonardokafuri.cibus.R;
import com.example.leonardokafuri.cibus.connection.SendMailTLS;
import com.example.leonardokafuri.cibus.utils.DatabaseHelper;

public class Promotion extends AppCompatActivity {

    DatabaseHelper dbh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promotion);
        dbh = new DatabaseHelper(this);
        final EditText friendEmail = findViewById(R.id.input);
        Button invite = findViewById(R.id.invite);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        final int id = sp.getInt("userId",0);

        Cursor c = dbh.ViewUserInfo(id);
        final StringBuilder user = new StringBuilder();
        if (c.getCount()==1) {
            while (c.moveToNext()) {
                user.append(c.getString(0));
                user.append(" ");
                user.append(c.getString(1)); // getting user name so it can use it on the email
            }
        }else
        {
            Toast.makeText(Promotion.this, "Can't fetch user information, try again!", Toast.LENGTH_SHORT).show();
        }

        invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    Cursor c = dbh.getUserByEmail(friendEmail.getText().toString());
                    if(c.getCount()==0) {
                        //if the email is not registered
                        Cursor updt = dbh.UpdatePromoCode(id);// updating the promotion code of the user so it can gets 10% off
                        if(updt.getCount()==0) {
                            SendMailTLS.sendInviteEmail(friendEmail.getText().toString(),user); // send the email to friend
                            Toast.makeText(Promotion.this, "Email sent, Enjoy 10% off on your next purchase!", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(Promotion.this,"Something went wrong, please try again",Toast.LENGTH_SHORT).show();
                        }
                        }else {
                        Toast.makeText(Promotion.this,"This email is already registered",Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
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
                startActivity(new Intent(Promotion.this, AccountInfo.class));
                break;
            case R.id.History:
                startActivity(new Intent(Promotion.this, History.class));
                break;
            case R.id.Promotion:
                Toast.makeText(this,"You already are on the Promotion Page",Toast.LENGTH_LONG).show();
                break;
            case R.id.logout:
                startActivity(new Intent(Promotion.this, Login.class));
                break;
        }
        return true;
    }
}
