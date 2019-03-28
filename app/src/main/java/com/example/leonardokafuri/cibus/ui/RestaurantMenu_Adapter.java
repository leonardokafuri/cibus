package com.example.leonardokafuri.cibus.ui;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.leonardokafuri.cibus.R;
import com.example.leonardokafuri.cibus.datamodel.Menu;

import java.text.DecimalFormat;
import java.util.List;

public class RestaurantMenu_Adapter
        extends RecyclerView.Adapter<RestaurantMenu_Adapter.RestaurantMenu_View_Holder>{

    private static final String LOG_TAG = RestaurantMenu_Adapter.class.getSimpleName();


    private List<Menu> menus;

    //Han: this is used for tracking user have enter order unit in which position
    private int[] selectionList;

    private DecimalFormat priceFormater;

    public double totalPrice;

    public RestaurantMenu_Adapter(List<Menu> menus) {

        this.menus = menus;

        priceFormater = new DecimalFormat(".##");

        updateSelectionList();
    }

    @NonNull
    @Override
    public RestaurantMenu_View_Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.restaurant_menu_list_item, viewGroup, false);

        RestaurantMenu_View_Holder output = new RestaurantMenu_View_Holder(view);

        return output;
    }

    @Override
    public void onBindViewHolder(@NonNull final RestaurantMenu_View_Holder holder, int position) {

        Menu current =menus.get(position);

        holder.foodName.setText(current.getFoodName() +" ($"+ priceFormater.format(current.getPricePerUnit())+") ");


    }


    @Override
    public int getItemCount() {
        return menus.size();
    }



    public void updateSelectionList(){
        selectionList = new int[menus.size()];

    }

    public double getTotalPrice(){

        double output = 0;

        for (int i = 0; i < menus.size(); i++) {
            if(selectionList[i] != 0){
                //Han: calculate each menu's subtotal prices (2 decimal) and sum them total prices
                int thisUnitNum = selectionList[i];
                double thisUnitPrice = menus.get(i).getPricePerUnit();
                output = output + Math.round(thisUnitPrice * thisUnitNum *100.0)/100.0;
            }
        }

        return output;
    }

    public double[] getMenuPrice(){

        double[] output = new double[menus.size()];

        for (int i = 0; i < output.length; i++) {
            double thisUnitPrice = menus.get(i).getPricePerUnit();
            output[i] = thisUnitPrice;
        }
        return output;
    }

    public  int[]  getOrderQuantity(){

        return  selectionList;
    }


    public class RestaurantMenu_View_Holder extends RecyclerView.ViewHolder
    {
        public EditText price;
        public TextView foodName;

        public RestaurantMenu_View_Holder(@NonNull View itemView) {
            super(itemView);

            foodName = itemView.findViewById(R.id.restaurant_menu_foodname);

            price = itemView.findViewById(R.id.restaurant_menu_price);

            price.addTextChangedListener(new TextWatcher() {

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    if(selectionList == null){

                        selectionList = new int[menus.size()];
                    }

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {


                    int positionChanged = getAdapterPosition();

                    //Han: whenever user changed the edittext(order quantity of a dish),
                    // udpate the order quantity list(selectionList)
                    //try catch handles null situation so if null the selecitonList will not be modified
                    try{

                        selectionList[positionChanged] =Integer.valueOf(price.getText().toString());

                    }catch (Exception e){
                        Log.e(LOG_TAG,"error parsing user input");
                    }


                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });


        }

    }
}
