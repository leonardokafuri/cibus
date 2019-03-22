package com.example.leonardokafuri.cibus;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Restaurants extends AppCompatActivity {

    String [] Restaurants = {"White Spot", "Mcdonalds","Pizza Hut","Subway","KFC","Dominos","Boston Pizza"};
    int [] images = new int[]{R.drawable.whitespot,R.drawable.mcdonalds,R.drawable.pizzahut,R.drawable.subway,R.drawable.kfc,R.drawable.dominos,R.drawable.boston};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurants);

        List<HashMap<String,String>> aList  = new ArrayList<HashMap<String, String>>();

        for(int i = 0; i<7;i++)
        {
            HashMap<String,String> hm = new HashMap<String,String>();
            hm.put("txt", Restaurants[i]);
            hm.put("image",Integer.toString(images[i]));
            aList.add(hm);
        }
        String [] from ={"image","txt"};
        int [] to = {R.id.pics,R.id.restaurant};
        SimpleAdapter adpater = new SimpleAdapter(getBaseContext(),aList,R.layout.layout,from,to);
        final ListView listview = findViewById(R.id.listview);
        listview.setAdapter(adpater);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position)
                {
                    case 0:
                        startActivity(new Intent(Restaurants.this,WhiteSpot.class));
                        break;
                    case 1:
                        startActivity(new Intent(Restaurants.this,Mcdonalds.class));
                        //other classes with menus need to be created
                        break;
                    case 2:
                        startActivity(new Intent(Restaurants.this,PizzaHut.class));
                        break;
                    case 3:
                        startActivity(new Intent(Restaurants.this,Subway.class));
                        break;
                    case 4:
                        startActivity(new Intent(Restaurants.this,Kfc.class));
                        break;
                    case 5:
                        startActivity(new Intent(Restaurants.this,Dominos.class));
                        break;
                    case 6:
                        startActivity(new Intent(Restaurants.this,Boston.class));
                        break;

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
                startActivity(new Intent(Restaurants.this,AccountInfo.class));
                break;
            case R.id.History:
                startActivity(new Intent(Restaurants.this,History.class));
                break;
            case R.id.Promotion:
                startActivity(new Intent(Restaurants.this,Promotion.class));
                break;
            case R.id.logout:
                startActivity(new Intent(Restaurants.this,Login.class));
                break;
        }
        return true;
    }
}
