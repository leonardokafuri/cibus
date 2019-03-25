package com.example.leonardokafuri.cibus.ui;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.leonardokafuri.cibus.R;
import com.example.leonardokafuri.cibus.datamodel.Restaurant;

import java.util.List;

public class Restaurants_Adapter extends
        RecyclerView.Adapter<Restaurants_Adapter.Restaurant_View_Holder> {

    private List<Restaurant> restaurantList;

    private CustomListItemClickListener myOnClickListener;

    public Restaurants_Adapter(List<Restaurant> restaurantList,
                               CustomListItemClickListener listener) {
        this.restaurantList = restaurantList;
        this.myOnClickListener = listener;
    }

    public interface CustomListItemClickListener {
        void customOnListItemClick(int clickedItemIndex);
    }

    @NonNull
    @Override
    public Restaurant_View_Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.restaurant_list_item, viewGroup, false);

        Restaurant_View_Holder output = new Restaurant_View_Holder(view);

        return output;
    }

    @Override
    public void onBindViewHolder(@NonNull Restaurant_View_Holder holder, int position) {
        Restaurant current =restaurantList.get(position);
        holder.imageView.setImageResource(current.getLogo());
        holder.textView.setText(current.getName());
    }

    @Override
    public int getItemCount() {
        return restaurantList.size();
    }

    public class Restaurant_View_Holder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        public ImageView imageView;
        public TextView textView;

        public Restaurant_View_Holder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.restaurant_list_logo);

            textView = itemView.findViewById(R.id.restaurant_list_name);

            itemView.setOnClickListener(this);

        }


        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            myOnClickListener.customOnListItemClick(position);
        }
    }
}
