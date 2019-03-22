package com.example.leonardokafuri.cibus;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class ConfirmAddress extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_address);

        Button confirm = findViewById(R.id.btnConfirm);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check the address and load the restaurants that are close by only
                startActivity(new Intent(ConfirmAddress.this,Restaurants.class));
            }
        });

    }
}
