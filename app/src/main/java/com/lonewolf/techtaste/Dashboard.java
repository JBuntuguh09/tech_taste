package com.lonewolf.techtaste;

import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.lonewolf.techtaste.Resources.Sections_Page_Adapter;

public class Dashboard extends AppCompatActivity {

    private Sections_Page_Adapter sectionsPageAdapter;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private Toolbar toolbar;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        viewPager = findViewById(R.id.vpDashBoardMain);
        tabLayout = findViewById(R.id.tabDashboard);
        toolbar = findViewById(R.id.toolbarDashBoard);

        auth = FirebaseAuth.getInstance();
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("My Dashboard");

        sectionsPageAdapter = new Sections_Page_Adapter(getSupportFragmentManager());

        viewPager.setAdapter(sectionsPageAdapter);

        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabTextColors(Color.WHITE, Color.WHITE);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_dashboard, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.menuAdminDash){

        }else if(item.getItemId()==R.id.menuSettings){

        }else if(item.getItemId()==R.id.menuLogOut){
            auth.signOut();

            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
