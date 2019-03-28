package com.example.leonardokafuri.cibus.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

public class DatabaseHelper extends SQLiteOpenHelper{

    private static final String LOG_TAG = DatabaseHelper.class.getSimpleName();
    final static String DATABASE_NAME = "Cibus.db";
    final static int DATABASE_VERSION = 1;

    int userId;


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

        //Han: add a field(DefaultAddress 0-> not default, 1->default) to determine default address
        String queryAddress = "Create Table Address(AddressId INTEGER PRIMARY KEY," +
                "UserId INTEGER, Province TEXT, City TEXT, StreetName TEXT, " +
                "StreetNumber INTEGER, UnitNumber INTEGER, PostCode TEXT, " +
                "Phone TEXT, DefaultAddress INTEGER, FOREIGN KEY(UserId) REFERENCES User(UserId))";

        String queryCreditCard = "Create Table CreditCard(CreditCardId INTEGER PRIMARY KEY," +
                "UserId INTEGER, CardNumber INTEGER, CardName TEXT, CardExpireDate INTEGER, CardType TEXT, FOREIGN KEY(UserId) REFERENCES User(UserId))";

        String queryRestaurant = "Create Table Restaurant(RestaurantId INTEGER PRIMARY KEY," +
                "Phone TEXT, Phone2 TEXT, Phone3 TEXT, Province TEXT, City TEXT, StreetName TEXT, StreetNumber INTEGER, PostCode TEXT)";

        String queryOrder = "Create Table OrderDetail(OrderId INTEGER PRIMARY KEY," +
                "UserId INTEGER, RestaurantName TEXT, Time TEXT, Amount DOUBLE, FOREIGN KEY(UserId) REFERENCES User(UserId) )";

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
    public boolean register(String fn,String ln,String email,String phone,Integer pc,String an,String pw)
            throws NoSuchAlgorithmException{
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

    public void registerFakeAddressForCurrentUser(int userID)
    {
        boolean[] output = new boolean[4];
        String[] fakeAddress1 = {"BC",  "Vancouver", "East St", "100", "", "V3A0B1","6044861111","1"};
        String[] fakeAddress2 = {"BC",  "Coquitlam", "Burke Rd", "521", "1001", "V5Z5B1","7788899998","0"};
        String[] fakeAddress3 = {"BC",  "Richmond", "No.1 Rd", "12121", "", "V4C7Y3","6048885541","0"};
        String[] fakeAddress4 = {"BC",  "Burnaby", "Commercial St", "888", "2101", "V6T7Z4","6046046664","0"};

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        output[0] = insertThisAddress(values, userID, fakeAddress1, db);
        output[1] = insertThisAddress(values, userID, fakeAddress2, db);
        output[2] = insertThisAddress(values, userID, fakeAddress3, db);
        output[3] = insertThisAddress(values, userID, fakeAddress4, db);

        if(output[0]  &&output[1] &&output[2]&&output[3]){
            Log.i(LOG_TAG,"insertion successful");

        }else{
            Log.e(LOG_TAG,"error creating fake addresses");

        }


    }

    private boolean insertThisAddress(ContentValues val,
                                      int userID,
                                      String[] inputAddress,
                                      SQLiteDatabase db){

        val.put("UserId", userID);
        val.put("Province", inputAddress[0]);
        val.put("City", inputAddress[1]);
        val.put("StreetName",inputAddress[2]);

        val.put("StreetNumber", Integer.valueOf(inputAddress[3].toString()));

        if(inputAddress[4] == "" ){
            val.put("UnitNumber",0);
        }else{
            val.put("UnitNumber",inputAddress[4]);
        }
        val.put("PostCode", inputAddress[5]);
        val.put("Phone",inputAddress[6]);
        val.put("DefaultAddress", inputAddress[7]);

        //Log.e("testttttttttttt",val.toString());
        if(!ifAddressExist(val) ){
            long r = db.insert("Address",null,val);
            if(r == -1){
                Log.e(LOG_TAG,"Insertion good");
                return  false;
            }
            else{
                Log.e(LOG_TAG,"Insertion bad");
                return true;
            }
        }

        return false;

    }

    private Boolean ifAddressExist(ContentValues values){

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        String query = "SELECT * FROM Address WHERE " +
                "UserId = "+ values.getAsInteger("UserId")+" AND "+
                "Province = '"+values.getAsString("Province")+"' AND " +
                "City = '"+values.getAsString("City")+"' AND " +
                "StreetName='"+values.getAsString("StreetName")+"' AND " +
                "StreetNumber = "+values.getAsInteger("StreetNumber")+" AND " +
                "UnitNumber = "+values.getAsInteger("UnitNumber")+" AND " +
                "PostCode = '"+values.getAsString("PostCode")+"' AND " +
                "Phone = '"+values.getAsString("Phone")+"'";

        Cursor c = sqLiteDatabase.rawQuery(query,null);

        if(c.getCount() == 0){
            return false;
        }else if (c.getCount() == 1){
            Log.i(LOG_TAG, "address is existed");
            return true;
        }else{
            Log.e(LOG_TAG, "duplicated address for user: "+userId);
            return true;
        }



    }

    public void insertNewDefaultAddress(int userID, String[] inputAddress){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        int result = deleteDefaultAddress(db,userID);

        Log.e(LOG_TAG,"delete result is "+ result);

        insertThisAddress(values,userID, inputAddress, db);


    }

    private int deleteDefaultAddress(SQLiteDatabase db, int userID){

        int result = db.delete(
                "Address",
                "UserId=? and DefaultAddress=?",
                new String[]{String.valueOf(userID), "1"}
                );
        return result;
    }

    public Cursor getDefaultAddress(int userID){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        String query = "SELECT * FROM Address WHERE UserId = '" + userID + "' AND DefaultAddress = 1";
        Cursor c = sqLiteDatabase.rawQuery(query,null);
        return c;
    }
    public Cursor getSavedCC(int userID){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        String query = "SELECT CardNumber,CardName,CardExpireDate,CardType FROM CreditCard WHERE UserId = "+ userID +";";
        Cursor c = sqLiteDatabase.rawQuery(query,null);
        return c;
    }

    public Cursor UpdateInformation(String fname, String lname, String email, String phone, int id)
    {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        String query ="UPDATE User SET FirstName ='"+fname+"',LastName='"+lname+"',Email='"+email+"',Phone ='"+phone+"' WHERE UserId ="+id+";";
        Cursor c = sqLiteDatabase.rawQuery(query,null);
        return c;
    }



    public void setUserId(int userId) {
        this.userId = userId;
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


    public boolean saveCC(String number,String name,String date,String type,int userId){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("UserId",userId);
        values.put("CardNumber",number);
        values.put("CardName",name);
        values.put("CardExpireDate",date);
        values.put("CardType",type);
        long r = db.insert("CreditCard",null,values);
        if(r == -1){
            return  false;
        }
        else{
            return true;
        }
    }

    public void saveOrder(int id,String restaurantName,String date,double price){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("UserId",id);
        values.put("RestaurantName",restaurantName);
        values.put("Time",date);
        values.put("Amount",price);
        //values.put("AddressId",1);
        db.insert("OrderDetail",null,values);
    }

    public Cursor getUserByEmail(String email){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        String query = "SELECT * FROM User WHERE Email = '" + email + "'";
        Cursor c = sqLiteDatabase.rawQuery(query,null);
        return c;
    }

    public Cursor viewHistory(int id)
    {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        String query ="SELECT * FROM OrderDetail WHERE UserId = " + id +";";
        Cursor c = sqLiteDatabase.rawQuery(query,null);
        return c;
    }
    public Cursor ViewUserInfo(int id)
    {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        String query ="SELECT FirstName,LastName,Email,Phone FROM User  WHERE UserId ="+ id + ";";
        Cursor c = sqLiteDatabase.rawQuery(query,null);
        return c;
    }

    public Cursor PromotionCode(int id){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        String query = "SELECT PromotionCode FROM User  WHERE UserId ="+ id + ";";
        Cursor c = sqLiteDatabase.rawQuery(query,null);
        return c;
    }

    public Cursor UpdatePromoCode(int id)
    {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        String query ="UPDATE User SET PromotionCode=1 WHERE UserId ="+ id + ";";
        Cursor c = sqLiteDatabase.rawQuery(query,null);
        return c;
    }
    public  Cursor DeletePromoCode(int id)
    {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        String query ="UPDATE User SET PromotionCode=0  WHERE UserId ="+ id + ";";
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


