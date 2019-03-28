package com.example.leonardokafuri.cibus.ui;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.leonardokafuri.cibus.R;
import com.example.leonardokafuri.cibus.utils.DatabaseHelper;

public class ConfirmAddress extends AppCompatActivity {

    private static final String LOG_TAG = ConfirmAddress.class.getSimpleName();

    //used to get user address from DB.
    private int userId;

    private DatabaseHelper dbh;

    private EditText province,city,streeName,streetNum, zipCode,unitNum, phoneNum;

    private String[] storeOriginalInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_address);

        province = findViewById(R.id.confirm_addr_province);
        city = findViewById(R.id.confirm_addr_city);
        streeName = findViewById(R.id.confirm_addr_streetName);
        streetNum = findViewById(R.id.confirm_addr_streetNum);
        zipCode = findViewById(R.id.confirm_addr_zip);
        unitNum = findViewById(R.id.confirm_addr_unitnumber);
        phoneNum = findViewById(R.id.confirm_addr_phone);

        dbh = new DatabaseHelper(this);

        storeOriginalInfo = new String[7];


        Intent getFromLoginActivity = getIntent();

        if(getFromLoginActivity.hasExtra("userid")){

            userId = getFromLoginActivity.getIntExtra("userid", -100);


            Cursor c = dbh.getDefaultAddress(userId);

            if(c.getCount() == 0)
            {
                Toast.makeText(
                        ConfirmAddress.this,
                        "There is no default address, You can enter your address here",
                        Toast.LENGTH_LONG).show();
            }else if (c.getCount() == 1){
                c.moveToFirst();

                province.setText(c.getString(2));
                storeOriginalInfo[0] = c.getString(2);

                city.setText(c.getString(3));
                storeOriginalInfo[1] = c.getString(3);

                streeName.setText(c.getString(4));
                storeOriginalInfo[2] = c.getString(4);

                streetNum.setText(c.getString(5));
                storeOriginalInfo[3] = c.getString(5);

                unitNum.setText(c.getString(6));
                storeOriginalInfo[4] = c.getString(6);

                zipCode.setText(c.getString(7));
                storeOriginalInfo[5] = c.getString(7);

                phoneNum.setText(c.getString(8));
                storeOriginalInfo[6] = c.getString(8);

            }else if (c.getCount() > 1){
                Log.e(LOG_TAG, "duplicate default address, check database!");
            }


        }


        Button confirm = findViewById(R.id.btn_confirm_address);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(ifUserEnteredAllField() == true){

                    if(hasValuesChangedByUser()==true){
                        //save to database
                        String[] defaultAddr = saveNewValuesIntoString();

                        dbh.insertNewDefaultAddress(userId,defaultAddr);

                    }

                    enterMainScreenAndDestroyThisActivity();
                }


            }
        });

        Button skip = findViewById(R.id.btn_skip_address);

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterMainScreenAndDestroyThisActivity();
            }
        });

    }

    private boolean ifUserEnteredAllField(){

        boolean output = true;

        if(province.getText().toString().trim().length() == 0){

            output= false;
        }else if (city.getText().toString().trim().length() == 0){

            output=  false;
        }else if (streeName.getText().toString().trim().length() == 0){

            output=  false;
        }else if (streetNum.getText().toString().trim().length() == 0){

            output=  false;
        }else if (zipCode.getText().toString().trim().length() == 0){

            output=  false;
        }else if (phoneNum.getText().toString().trim().length() == 0){

            output=  false;
        }

        if(output == false)
            msgToUserNeedToFillALl();

        return output;
    }

    private void msgToUserNeedToFillALl(){
        Toast.makeText(
                ConfirmAddress.this,
                "Please enter all field except unit NO.!",
                Toast.LENGTH_LONG
                ).show();
    }

    private boolean hasValuesChangedByUser(){
        String s0,s1,s2,s3,s4,s5,s6;

        s0 = province.getText().toString();
        s1= city.getText().toString();
        s2=streeName.getText().toString();
        s3 = streetNum.getText().toString();
        s4 = unitNum.getText().toString();
        s5 =zipCode.getText().toString();
        s6 =phoneNum.getText().toString();

        if(s0.equals(storeOriginalInfo[0])&& s1.equals(storeOriginalInfo[1])&&
            s2.equals(storeOriginalInfo[2])&& s3.equals(storeOriginalInfo[3])&&
            s4.equals(storeOriginalInfo[4])&& s5.equals(storeOriginalInfo[5])&&
            s6.equals(storeOriginalInfo[6])
        ){ return false;  }

        return true;
    }

    private String[] saveNewValuesIntoString(){
        String[] output = new String[8];
        output[0]= province.getText().toString();
        output[1]= city.getText().toString();
        output[2]=streeName.getText().toString();
        output[3] = streetNum.getText().toString();
        output[4] = unitNum.getText().toString();
        output[5] =zipCode.getText().toString();
        output[6] =phoneNum.getText().toString();

        //this value means default address
        output[7] = "1";
        return output;
    }

    private void enterMainScreenAndDestroyThisActivity(){
        //Han :check the address and load the restaurants that are close by only
        startActivity(new Intent(ConfirmAddress.this, Restaurants.class));
        //Han :to make sure user cannot go back to this page,
        // if user click back, they will go back to login screen
        finish();
    }
}
