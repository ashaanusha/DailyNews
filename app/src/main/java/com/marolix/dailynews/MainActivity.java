package com.marolix.dailynews;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.prabhat1707.verticalpager.VerticalViewPager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private VerticalViewPager viewPager;
    ArrayList<Map<String,String>> al=new ArrayList<>();
    private Context context;
    RequestQueue queue;
RelativeLayout relativeLayout;
 static String selectedcategory="all";
    String arr[];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        viewPager = findViewById(R.id.viewPager);
        queue = Volley.newRequestQueue(this);
        relativeLayout=findViewById(R.id.relative);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//       if(toolbar.getVisibility()==View.GONE)
//       {
//            toolbar.setVisibility(View.VISIBLE);
//       }
//       else
//       {
//           toolbar.setVisibility(View.GONE);
//       }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
//        relativeLayout.setOnTouchListener(new View.OnTouchListener() {
//                        @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        getActivity().getActionBar().show();
//                    }
//                }, 5000);
//      ActionBar actionBar=getSupportActionBar();
//                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
//
//                    if ( actionBar.isShowing()) {
//                        actionBar.hide();
//                    } else {
//                        actionBar.show();
//                    }
//                    return true;
//                } else return false;
//            }
//        });
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                "https://raw.githubusercontent.com/ashaanusha/Schoolkids/master/news", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONArray news_array=response.getJSONArray("News_list");
                    for(int i=0;i<news_array.length();i++)
                    {
                        Map<String,String> map=new HashMap<>() ;
                        JSONObject news_details=news_array.getJSONObject(i);
                        String category=news_details.getString("category");
                        String heading=news_details.getString("heading");
                        String description=news_details.getString("description");
                        String urltoimage=news_details.getString("urltoimage");
                        String urltovideo=news_details.getString("urltovideo");
                        String publishdate=news_details.getString("publishdate");
                        String type=news_details.getString("type");
                       JSONArray arrayofimages=news_details.getJSONArray("arrayofimages");

                       if(type.equals("multipleimages")) {

                           arr=new String[10];
                           for (int j = 0; j < arrayofimages.length(); j++) {

                               arr[j] = arrayofimages.getString(j);
                           }
                       }
                        map.put("category",category);
                        map.put("heading",heading);
                        map.put("description",description);
                        map.put("urltoimage",urltoimage);
                        map.put("urltovideo",urltovideo);
                        map.put("publishdate",publishdate);
                        map.put("type",type);
                        map.put("selectedCategory","All");
                        if(type.equals("multipleimages"))
                        {
                            for (int a = 0; a < arrayofimages.length(); a++) {
                                map.put("array" + a, arr[a]);
                            }
                            map.put("arraylength",String.valueOf(arrayofimages.length()));
                            Log.e("arraylength",String.valueOf(arrayofimages.length()));
                        }
                        al.add(map);
                    }
                    viewPager.setAdapter(new MyAdapter(getSupportFragmentManager(), context,al));
                }  catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.e("response", response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error){
                Log.e("error", error.getMessage());
            }
        }
        );
        queue.add(request);




    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        Intent intent=new Intent(MainActivity.this,Language.class);
        startActivity(intent);

        } else if (id == R.id.nav_gallery) {
            Map<String,String> map=new HashMap<>() ;
            map.put("selectedCategory","Business");
            al.add(map);
            viewPager.setAdapter(new MyAdapter(getSupportFragmentManager(), context,al));

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}


