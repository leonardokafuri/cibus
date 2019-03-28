package com.example.leonardokafuri.cibus;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.leonardokafuri.cibus.ui.OrderConfirmation;
import com.example.leonardokafuri.cibus.utils.DatabaseHelper;

public class Login extends AppCompatActivity {

    private DatabaseHelper dbh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dbh = new DatabaseHelper(this);


        Button Register = findViewById(R.id.register);
        Button Forgot = findViewById(R.id.forgot);
        final Button Login = findViewById(R.id.login);
        final EditText userName = findViewById(R.id.username);
        final EditText password = findViewById(R.id.CardNum);
        final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this); // created a shared pref to pass the user id
        Intent i = getIntent();
        String value = i.getStringExtra("registered");
        if(value != null && !value.isEmpty())
        {
            Toast.makeText(Login.this, "User registration sucessfull! Please login to continue", Toast.LENGTH_LONG).show();
        }


        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this,Registration.class));
            }
        });

        Forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this,ForgotPassword.class));
            }
        });

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check with database
                Cursor c = dbh.login(userName.getText().toString(), password.getText().toString());
                if(c.getCount() == 0)
                {
                    Toast.makeText(Login.this, "Invalid Username or Password", Toast.LENGTH_SHORT).show();
                }
                else{
                    Intent i = new Intent(Login.this, ConfirmAddress.class);
                    c.moveToFirst();
                    dbh.setUserId(c.getInt(0));
                    i.putExtra("userid",c.getInt(0));

                    SharedPreferences.Editor editor = sharedPref.edit();
                    int id = c.getInt(0); // getting the user id that is currently using the app and saving its id on the shared pref file
                    editor.putInt("userId",id);

                    editor.commit(); // the user id will alwayss be updated to the current user logged in

                    startActivity(i);
                }
                //then start the confirm address activity

            }
        });

    }
}