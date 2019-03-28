package com.example.leonardokafuri.cibus.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.leonardokafuri.cibus.R;
import com.example.leonardokafuri.cibus.utils.DatabaseHelper;

public class Login extends AppCompatActivity {

    private DatabaseHelper dbh;
    private int numOfTimesBackButtonPressed;
    private Handler handlerBackPress;

    @Override
    public void onBackPressed() {

        numOfTimesBackButtonPressed += 1;

        //Han : the 1st time user click back button will pop up a msg,
        //if user ignore the msg and click 2nd time within 2second after
        //clicking of 1st on back button, they will be switch back to login screen
        if(numOfTimesBackButtonPressed <= 1)
        {
            Toast.makeText(
                    this,
                    "Tap one more time to exit app.",
                    Toast.LENGTH_SHORT).show();

            handlerBackPress = new Handler();

            final Runnable countDownTwoSecond = new Runnable() {
                @Override
                public void run() {
                    numOfTimesBackButtonPressed -= 1;
                }
            };
            handlerBackPress.postDelayed(countDownTwoSecond, 2000);
        }else{
            super.onBackPressed();

        }

    }

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
                startActivity(new Intent(Login.this, Registration.class));
            }
        });

        Forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, ForgotPassword.class));
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
                    finish();
                }
                //then start the confirm address activity

            }
        });


    }
}