package com.healthlitmus.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.healthlitmus.Fragment.Search;

import com.healthlitmus.Fragment.TestResult;
import com.healthlitmus.R;

public class MainActivity extends ActionBarActivity {

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Search search;
    private TestResult testResult;
    private SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        testResult = new TestResult();
        search = new Search();
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.framelayout_home, search);
        fragmentTransaction.commit();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        // enabling toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        //getSupportActionBar().setDisplayShowTitleEnabled(false);

        sharedPreferences = getApplicationContext().getSharedPreferences("UserInfo", getApplicationContext().MODE_PRIVATE);

        //Initializing NavigationView
        navigationView = (NavigationView) findViewById(R.id.navigation_view);

        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(false);
                android.support.v4.app.FragmentTransaction fragmentTransaction;
                android.support.v4.app.Fragment currentFragment;
                Log.v("MyApp", getClass().toString() + " navigation touch listener " );

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {
                    case R.id.navigation_drawer_home:
                        search = new Search();
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.framelayout_home, search).addToBackStack("");
                        fragmentTransaction.commit();
                        break;

                    case R.id.navigation_drawer_appointment:
                        Toast.makeText(getApplicationContext(), "Yet To be Developed", Toast.LENGTH_LONG).show();
                        break;

                    case R.id.navigation_drawer_Invoices:
                        Toast.makeText(getApplicationContext(), "Yet To be Developed", Toast.LENGTH_LONG).show();
                        break;

                    case R.id.navigation_drawer_pastTest:
                        Toast.makeText(getApplicationContext(), "Yet To be Developed", Toast.LENGTH_LONG).show();
                        break;

                    case R.id.navigation_drawer_loyaltyBonus:
                        Toast.makeText(getApplicationContext(), "Yet To be Developed", Toast.LENGTH_LONG).show();
                        break;

                    case R.id.navigation_drawer_offers:
                        Toast.makeText(getApplicationContext(), "Yet To be Developed", Toast.LENGTH_LONG).show();
                        break;

                }
                //Closing drawer on item click
                drawerLayout.closeDrawers();
                return true;
            }
        });

        View headerview = navigationView.getHeaderView(0);
        //navigationView.addHeaderView(headerview);
        headerview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent=new Intent(MainActivity.this, StudentProfile.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent);
                Log.v("MyApp", getClass().toString() + " headerview touch");
            }
        });
        // Initializing Drawer Layout and ActionBarToggle
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);


        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.openDrawer,
                R.string.closeDrawer){

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(false);
        actionBarDrawerToggle.setHomeAsUpIndicator(R.drawable.hamburger);
        //getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_dehaze_black_24dp);
        //calling sync state is necessay or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
        actionBarDrawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });

    }//onCreate()

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_profile :
                if(sharedPreferences.getBoolean("login", false )) {
                    //code to load profile.class
                    Toast.makeText(getApplicationContext(),"Already Logged In. Loading Profile", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(),"Not Logged In. Loading Login Page", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, Login.class);
                    startActivity(intent);
                }
        }

        return super.onOptionsItemSelected(item);
    }
}
