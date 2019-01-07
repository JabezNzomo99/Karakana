package com.jabezmagomere.karakanaics;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import es.dmoral.toasty.Toasty;
import spencerstudios.com.bungeelib.Bungee;

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private DrawerLayout drawerLayout;
    private SessionManager sessionManager;
    private NavigationView navigationView;
    public static final String EMAIL="email";
    public static final String FIRST_NAME="first_name";
    public static final String LAST_NAME="last_name";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo activeNetwork = conMgr.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnected()) {
            } else {
            // notify user you are not online
            Toasty.warning(MainActivity.this,"Check Network Status", Toast.LENGTH_LONG,true).show();
        }
        sessionManager=new SessionManager(MainActivity.this);
        sessionManager.checkLogin();
        viewPager=(ViewPager)findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout=(TabLayout)findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_sos);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_mechaniconmap);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_shop);
        toolbar=(android.support.v7.widget.Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Home");
        ActionBar actionBar=getSupportActionBar();
        toolbar.setTitleTextColor(getResources().getColor(R.color.app_bkg));
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
        drawerLayout=(DrawerLayout)findViewById(R.id.drawer_layout);
        navigationView=(NavigationView)findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent;
                int id=item.getItemId();
                switch (id){
                    case R.id.user_profile:
                        drawerLayout.closeDrawers();
                        intent=new Intent(MainActivity.this, UserProfile.class);
                        startActivity(intent);
                        Bungee.card(MainActivity.this);
                        return true;
                    case R.id.purchases:
                        drawerLayout.closeDrawers();
                        intent=new Intent(MainActivity.this, PurchaseActivity.class);
                        startActivity(intent);
                        Bungee.card(MainActivity.this);
                        return true;
                    case R.id.about:
                        drawerLayout.closeDrawers();
                        intent=new Intent(MainActivity.this, AboutActivity.class);
                        startActivity(intent);
                        Bungee.card(MainActivity.this);
                        return true;
                }
                item.setChecked(true);
                drawerLayout.closeDrawers();
                return true;
            }
        });
    }
    private void setupViewPager(ViewPager viewPager){
        ViewPagerAdapter adapter=new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new RecoveryFragment(),"SoS");
        adapter.addFragment(new MechanicFragment(),"Mechanic");
        adapter.addFragment(new ShopFragment(),"SHOP");
        viewPager.setAdapter(adapter);
    }
    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> fragmentList=new ArrayList<>();
        private final List<String> fragmentTitleList=new ArrayList<>();
        public ViewPagerAdapter(FragmentManager manager){
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }
        public void addFragment(Fragment fragment, String title){
            fragmentList.add(fragment);
            fragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitleList.get(position);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                HashMap<String,String> user=sessionManager.getUserDetails();
                String FirstName=user.get(FIRST_NAME);
                String LastName=user.get(LAST_NAME);
                String Email=user.get(EMAIL);
                TextView tvUserName=(TextView)navigationView.findViewById(R.id.tvUserName);
                tvUserName.setText(FirstName+" "+LastName);
                TextView tvMail=(TextView)navigationView.findViewById(R.id.tvEmail);
                tvMail.setText(Email);
                drawerLayout.openDrawer(GravityCompat.START);

                return true;
            case R.id.menu_logout:
                sessionManager.logoutUser();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
