package com.example.leonardokafuri.cibus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.example.leonardokafuri.cibus.ui.RestaurantMenu;


import com.example.leonardokafuri.cibus.ui.RestaurantMenu_Adapter;
import com.example.leonardokafuri.cibus.utils.DatabaseHelper;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

public class Payment extends AppCompatActivity {
    DatabaseHelper dbh;
    RestaurantMenu rm;
    Intent intent = getIntent();
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        dbh = new DatabaseHelper(this);
        final EditText number = findViewById(R.id.CardNum);
        final EditText name = findViewById(R.id.cardName);
        final EditText date = findViewById(R.id.Expire);
        final EditText type = findViewById(R.id.CardType);
        RadioButton pickup = findViewById(R.id.pickup);
        RadioButton delivery = findViewById(R.id.pickup);
        RadioGroup radioGroup = findViewById(R.id.radioGroup);
        final TextView result = findViewById(R.id.total2);
        Button submit = findViewById(R.id.submitOrd);
        final CheckBox saveCC = findViewById(R.id.saveCC);
        final Date currentTime = Calendar.getInstance().getTime();

        final double totalPrice = intent.getDoubleExtra("totalPrice",0);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(saveCC.isChecked())
               {
                    try {
                        boolean saved = dbh.saveCC(number.getText().toString(), name.getText().toString(), date.getText().toString(), type.getText().toString());
                        if(!saved)
                            Toast.makeText(Payment.this, "Something went wrong, please check your info and try again!", Toast.LENGTH_LONG).show();
                        else
                        {
                            Toast.makeText(Payment.this, "Credit Card successfully saved", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(Payment.this,History.class));

                            dbh.saveOrder("McDonalds",currentTime.toString(),totalPrice);
                        }
                    }catch(Exception e)
                    {
                        Toast.makeText(Payment.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                else
                startActivity(new Intent(Payment.this,History.class));
                dbh.saveOrder("McDonalds",currentTime.toString(),totalPrice);


            }
        });

    radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            if(checkedId == R.id.pickup)
            {
                result.setText("Your total is " + totalPrice);
            }
            if(checkedId == R.id.delivery)
            {
                result.setText("Your total is " + (totalPrice + 3.99) + " ($3.99 delivery fee)");
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
                startActivity(new Intent(Payment.this,AccountInfo.class));
                break;
            case R.id.History:
                startActivity(new Intent(Payment.this,History.class));
                break;
            case R.id.Promotion:
                startActivity(new Intent(Payment.this,Promotion.class));
                break;
            case R.id.logout:
                startActivity(new Intent(Payment.this,Login.class));
                break;
        }
        return true;
    }
}
