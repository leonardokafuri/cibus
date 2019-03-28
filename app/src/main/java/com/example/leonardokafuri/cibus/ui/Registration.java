package com.example.leonardokafuri.cibus.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.leonardokafuri.cibus.R;
import com.example.leonardokafuri.cibus.utils.DatabaseHelper;

public class Registration extends AppCompatActivity {

    DatabaseHelper dbh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        dbh = new DatabaseHelper(this);

        final EditText username = findViewById(R.id.RegUser);
        final EditText password = findViewById(R.id.RegPass);
        final EditText phone = findViewById(R.id.RegCell);
        final EditText fname = findViewById(R.id.RegFname);
        final EditText lname  = findViewById(R.id.RegLname);
        final Button btRegister = findViewById(R.id.registerBtn);

        btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    boolean registered = dbh.register(fname.getText().toString(), lname.getText().toString(), username.getText().toString(), phone.getText().toString(), 0, username.getText().toString(), password.getText().toString());
                    if(!registered)
                        Toast.makeText(Registration.this, "Something went wrong, please check your info and try again!", Toast.LENGTH_LONG).show();
                    else
                    {
                        Intent i = new Intent(Registration.this, Login.class);
                        i.putExtra("registered",username.getText().toString());
                        startActivity(i);
                    }
                }catch(Exception e)
                {
                    Toast.makeText(Registration.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
