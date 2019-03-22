package com.example.leonardokafuri.cibus;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.UUID;

public class DatabaseHelper extends SQLiteOpenHelper{
    final static String DATABASE_NAME = "Cibus.db";
    final static int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //creating User table
        String queryUser = "Create Table User(UserId INTEGER PRIMARY KEY," +
                            "FirstName TEXT, LastName TEXT, Email TEXT, Phone TEXT, PromotionCode INTEGER, AccountName TEXT UNIQUE, Password TEXT )";

        String queryPasswordReset = "Create Table PasswordReset(ResetId INTEGER PRIMARY KEY," +
                "UserId INTEGER, PasswordToken TEXT, CreatedTime INTEGER, FOREIGN KEY(UserId) REFERENCES User(UserId))";

        String queryAddress = "Create Table Address(AddressId INTEGER PRIMARY KEY," +
                "UserId INTEGER, Province TEXT, City TEXT, StreetName TEXT, StreetNumber INTEGER, UnitNumber INTEGER, PostCode TEXT, Phone TEXT, FOREIGN KEY(UserId) REFERENCES User(UserId))";

        String queryCreditCard = "Create Table CreditCard(CreditCardId INTEGER PRIMARY KEY," +
                "UserId INTEGER, CardNumber INTEGER, CardName TEXT, CardExpireDate INTEGER, CardType TEXT, FOREIGN KEY(UserId) REFERENCES User(UserId))";

        String queryRestaurant = "Create Table Restaurant(RestaurantId INTEGER PRIMARY KEY," +
                "Phone TEXT, Phone2 TEXT, Phone3 TEXT, Province TEXT, City TEXT, StreetName TEXT, StreetNumber INTEGER, PostCode TEXT)";

        String queryOrder = "Create Table OrderDetail(OrderId INTEGER PRIMARY KEY," +
                "UserId INTEGER, RestaurantId INTEGER, Time INTEGER, Amount REAL, AddressId INTEGER, FOREIGN KEY(UserId) REFERENCES User(UserId), FOREIGN KEY(RestaurantId) REFERENCES Restaurant(RestaurantId),  FOREIGN KEY(AddressId) REFERENCES Address(AddressId) )";

        try{
            db.execSQL(queryUser);
            db.execSQL(queryPasswordReset);
            db.execSQL(queryAddress);
            db.execSQL(queryCreditCard);
            db.execSQL(queryRestaurant);
            db.execSQL(queryOrder);
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("Drop table if exists User");
        db.execSQL("Drop table if exists PasswordReset");
        db.execSQL("Drop table if exists Address");
        db.execSQL("Drop table if exists CreditCard");
        db.execSQL("Drop table if exists Restaurant");
        db.execSQL("Drop table if exists OrderDetail");

        onCreate(db);
    }

    //method to add user record
    public boolean register(String fn,String ln,String email,String phone,Integer pc,String an,String pw) throws NoSuchAlgorithmException{
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("FirstName",fn);
        values.put("LastName",ln);
        values.put("Email",email);
        values.put("Phone",phone);
        values.put("PromotionCode",pc);
        values.put("AccountName",an);
        //Hashing the password
        String hashPassword;
        hashPassword = "";
        hashPassword = get_SHA_256_SecurePassword(pw);
        values.put("Password",hashPassword);
        long r = db.insert("User",null,values);
        if(r == -1){
            return  false;
        }
        else{
            return true;
        }
    }

    public Cursor login(String userName, String password){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        String hashPassword;
        hashPassword = "";
        hashPassword = get_SHA_256_SecurePassword(password);
        String query = "SELECT * FROM User WHERE AccountName = '" + userName + "' AND password = '" + hashPassword + "'";
        Cursor c = sqLiteDatabase.rawQuery(query,null);

        return c;

    }

    public Cursor getUserByEmail(String email){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        String query = "SELECT * FROM User WHERE Email = '" + email + "'";
        Cursor c = sqLiteDatabase.rawQuery(query,null);
        return c;
    }

    public String createResetToken(Integer UserId){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("UserId",UserId);
        //Generates random token
        String uuid = UUID.randomUUID().toString().replace("-","").substring(0,6);
        values.put("PasswordToken",uuid);
        long now = System.currentTimeMillis();
        values.put("CreatedTime",now);

        long r = db.insert("PasswordReset",null,values);
        if(r == -1){
            return  "";
        }
        else{
            return uuid;
        }
    }

    public void resetPassword(String email, String code, String password) throws Exception{
        Cursor cUser = getUserByEmail(email);
        cUser.moveToFirst();
        Integer userId = cUser.getInt(0);
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        String query = "SELECT * FROM PasswordReset WHERE UserId = " + userId.toString() + " Order by CreatedTime desc";
        Cursor c = sqLiteDatabase.rawQuery(query,null);
        if(c.getCount() == 0)
            throw new Exception("There is no reset token for this user! Please fill your email and hit send code!");
        c.moveToFirst();
        if(!code.equals(c.getString(2)))
            throw new Exception("This code is invalid, please check your emails for the latest sent code");

        SQLiteDatabase db2 = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        password = get_SHA_256_SecurePassword(password);

        cv.put("Password",password);
        db2.update("User",cv,"Email=?",new String[]{email});

    }

    private static String bytesToHex(byte[] hash) {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if(hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

    private static String get_SHA_256_SecurePassword(String passwordToHash)
    {
        String generatedPassword = null;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedhash = digest.digest(
                    passwordToHash.getBytes());
            generatedPassword = bytesToHex(encodedhash);
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        return generatedPassword;
    }

}


