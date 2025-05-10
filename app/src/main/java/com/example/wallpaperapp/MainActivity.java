package com.example.wallpaperapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import com.google.android.gms.ads.MobileAds;

public class MainActivity extends AppCompatActivity {

    public static final String[] category = new String[] {"Abstract", "Cars", "Movie", "Quotes", "Texture"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        DrawerLayout drawerLayout = findViewById(R.id.drawer);
        NavigationView navigation = findViewById(R.id.navigation);
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        ViewPager2 viewPager = findViewById(R.id.viewPager);

        MobileAds.initialize(this, initializationStatus -> {
            AdView adView = new AdView(this);
            adView.setAdUnitId(getString(R.string.Banner));
            adView.setAdSize(new AdSize(450, 50));
            LinearLayout adContainer = findViewById(R.id.adView);
            AdRequest adRequest = new AdRequest.Builder().build();
            adContainer.addView(adView);
            adView.loadAd(adRequest);
        });

        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigation.setNavigationItemSelectedListener(item -> {
            drawerLayout.closeDrawer(GravityCompat.START);
            if(item.getItemId() == R.id.policy) {
                startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse("https://google.com")));
            } else if(item.getItemId() == R.id.contact) {
                startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse("https://google.com")));
            } else if(item.getItemId() == R.id.share) {
                startActivity(new Intent(Intent.ACTION_SEND).setType("text/plain").putExtra(Intent.EXTRA_TEXT,
                        "Let me recommend this great app. Download now - https://play.google.com/store/apps/details?id=" + getPackageName()));
            } else if(item.getItemId() == R.id.aboutUs) {
                new AlertDialog.Builder(this).setMessage("This app has been developed as a final project for mobile programming.").setPositiveButton("Dismiss",
                        (dialogInterface, which) -> dialogInterface.dismiss()).create().show();
            } else if(item.getItemId() == R.id.bookmark) {
                startActivity(new Intent(this, BookmarksActivity.class));
            }
            return false;
        });
        viewPager.setAdapter(new ViewPagerAdapter(this ));
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> tab.setText(category[position])).attach();
    }
}