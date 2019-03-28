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
import com.example.leonardokafuri.cibus.utils.DatabaseHelper;

public class AccountInfo extends AppCompatActivity {

    DatabaseHelper dbh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_info);
        dbh = new DatabaseHelper(this);

        final EditText firstname = findViewById(R.id.Userfname);
        final EditText lastname = findViewById(R.id.UserLname);
        final EditText phonenum = findViewById(R.id.UserCell);
        final EditText email = findViewById(R.id.UserMail);

        Button update = findViewById(R.id.editinfo);

        //referencing to editText fields that will be auto filled with user information and  referencing to the btn to update

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        final int id = sharedPreferences.getInt("userId",0); // getting the current user id and passing into an int

        Cursor c = dbh.ViewUserInfo(id); // calling the function ViewUserInfo with the id from current user as an parameter
       try{
           if (c.getCount()==1) {
               while (c.moveToNext()) {
                   //if theres is a match in the databse set the text for each of the fields with the column of that were set on the select statment on databasehelper
                   firstname.setText (c.getString(0));
                   lastname.setText(c.getString(1));
                   email.setText(c.getString(2));
                   phonenum.setText(c.getString(3));
               }
           }else
           {
               Toast.makeText(AccountInfo.this, "Database Error", Toast.LENGTH_SHORT).show(); // if something wrong fetching data show error message
           }

       }catch (Exception e)
       {
           e.printStackTrace();
       }

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fname = firstname.getText().toString();
                 String lname = lastname.getText().toString();
                 String phone = phonenum.getText().toString();
                 String Email = email.getText().toString();
                 int userid = id;
                Cursor c = dbh.UpdateInformation(fname,lname,Email,phone,userid); // passing all the changed information from fields as an parameter to be updated
                if(c.getCount()==0)
                {
                    Toast.makeText(AccountInfo.this,"Information updated ! ",Toast.LENGTH_SHORT).show(); // if updates sucessfull display a toast message

                }else {
                    Toast.makeText(AccountInfo.this, "Something went wrong, Please try again", Toast.LENGTH_SHORT).show();// if doesnt update, try again
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
                Toast.makeText(this,"You already are on the Account Settings Page",Toast.LENGTH_LONG).show();
                break;
            case R.id.History:
                startActivity(new Intent(AccountInfo.this, History.class));
                break;
            case R.id.Promotion:
                startActivity(new Intent(AccountInfo.this, Promotion.class));
                break;
            case R.id.logout:
                startActivity(new Intent(AccountInfo.this, Login.class));
                break;
        }
        return true;
    }

}
