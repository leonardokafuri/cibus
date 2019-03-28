package com.example.leonardokafuri.cibus.ui;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.leonardokafuri.cibus.R;
import com.example.leonardokafuri.cibus.connection.SendMailTLS;
import com.example.leonardokafuri.cibus.utils.DatabaseHelper;

public class ForgotPassword extends AppCompatActivity {

    DatabaseHelper dbh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        final EditText email = findViewById(R.id.Resetmail);
        final EditText sendCode = findViewById(R.id.ResetCode);
        final EditText newPass = findViewById(R.id.newpass);
        final Button btSendCode = findViewById(R.id.SendCode);
        final Button btReset = findViewById(R.id.Reset);

        dbh = new DatabaseHelper(this);

        btSendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    Cursor c = dbh.getUserByEmail(email.getText().toString());
                    if(c.getCount() == 0)
                        Toast.makeText(ForgotPassword.this, "This email is not registered in our systems, Try a new email and hit send code again!", Toast.LENGTH_LONG).show();
                    else{
                        c.moveToFirst();
                        String token = dbh.createResetToken(c.getInt(0));
                        if(token.isEmpty())
                        {
                            Toast.makeText(ForgotPassword.this, "Something went wrong when trying to send the code, please check your info and try again", Toast.LENGTH_SHORT).show();
                        }
                        else{

                            SendMailTLS.sendEmail(email.getText().toString(),token);
                            Toast.makeText(ForgotPassword.this, "An email has been sent to your address, please check your token and fill it on the code field along with your new password", Toast.LENGTH_SHORT).show();
                        }
                    }

                }
                catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        });

        btReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sendCode.getText().toString().isEmpty() || newPass.getText().toString().isEmpty())
                {
                    Toast.makeText(ForgotPassword.this, "Please fill the code and the new password fields", Toast.LENGTH_SHORT).show();
                }
                else{
                    try{
                        dbh.resetPassword(email.getText().toString(),sendCode.getText().toString(),newPass.getText().toString());
                        Toast.makeText(ForgotPassword.this, "Password Changed Successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(ForgotPassword.this, Login.class));
                    }
                    catch(Exception e)
                    {
                        Toast.makeText(ForgotPassword.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }
        });


    }
}
