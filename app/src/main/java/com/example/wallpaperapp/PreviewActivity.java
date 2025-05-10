package com.example.wallpaperapp;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_MEDIA_IMAGES;
import static android.Manifest.permission.SET_WALLPAPER;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.app.WallpaperManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.IOException;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class PreviewActivity extends AppCompatActivity {

    public static String wallpaper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        MobileAds.initialize(this, initializationStatus -> {
            AdView adView = new AdView(this);
            adView.setAdUnitId(getString(R.string.Banner));
            adView.setAdSize(new AdSize(300, 50));
            LinearLayout adContainer = findViewById(R.id.adView);
            AdRequest adRequest = new AdRequest.Builder().build();
            adContainer.addView(adView);
            adView.loadAd(adRequest);
        });
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        toolbar.setNavigationOnClickListener(view -> finish());

        String permission;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permission = READ_MEDIA_IMAGES;
        } else {
            permission = READ_EXTERNAL_STORAGE;
        }

        ImageView imageView = findViewById(R.id.img);
        Picasso.get().load(wallpaper).placeholder(R.mipmap.logo).fit().into(imageView);


        findViewById(R.id.set).setOnClickListener(view -> {
            WallpaperManager wallpaperManager = WallpaperManager.getInstance(this);
            if(ContextCompat.checkSelfPermission(this, SET_WALLPAPER) == PackageManager.PERMISSION_GRANTED) {
                Picasso.get().load(wallpaper).into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        try {
                            wallpaperManager.setBitmap(bitmap);
                            Toast.makeText(PreviewActivity.this, "Wallpaper Set Successfully!", Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            Toast.makeText(PreviewActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                        Toast.makeText(PreviewActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                });
            } else {
                ActivityCompat.requestPermissions(this, new String[] {SET_WALLPAPER, WRITE_EXTERNAL_STORAGE, permission}, 222);
            }
        });
        findViewById(R.id.download).setOnClickListener(view -> {
            if(ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED) {
                Picasso.get().load(wallpaper).into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        String title = "wallpaper_" + new Random().nextInt(10000);
                        String des = "Download from " + getString(R.string.app_name);
                        MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, title, des);
                        Toast.makeText(PreviewActivity.this, "Downloaded!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                        Toast.makeText(PreviewActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                });
            } else {
                ActivityCompat.requestPermissions(this, new String[] {SET_WALLPAPER, WRITE_EXTERNAL_STORAGE, permission}, 222);
            }
        });
        findViewById(R.id.share).setOnClickListener(view -> {
            if(ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED) {
                Picasso.get().load(wallpaper).into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        String title = "wallpaper_" + new Random().nextInt(10000);
                        String des = "Download from " + getString(R.string.app_name);
                        String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, title, des);
                        Intent intent = new Intent(Intent.ACTION_SEND).setType("text/plain")
                                .putExtra(Intent.EXTRA_STREAM, Uri.parse(path))
                                .putExtra(Intent.EXTRA_TEXT, "Download the app - https://play.google.com/store/apps/details?id=" + getPackageName());
                        startActivity(Intent.createChooser(intent, "Share via"));
                    }

                    @Override
                    public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                        Toast.makeText(PreviewActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                });
            } else {
                ActivityCompat.requestPermissions(this, new String[] {SET_WALLPAPER, WRITE_EXTERNAL_STORAGE, permission}, 222);
            }
        });

        TextView bookmarkBtn = findViewById(R.id.bookmark);
        SharedPreferences prefs = getSharedPreferences("bookmarks", MODE_PRIVATE);
        Set<String> bookmarks = prefs.getStringSet("wallpapers", new HashSet<>());
        // Bookmark butonunun ilk durumunu ayarla
        if (bookmarks.contains(wallpaper)) {
            bookmarkBtn.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.remove_bookmark, 0, 0); // Dolu ikon
        } else {
            bookmarkBtn.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.add_bookmark, 0, 0); // Boş ikon
        }

        bookmarkBtn.setOnClickListener(view -> {
            Set<String> updatedBookmarks = new HashSet<>(prefs.getStringSet("wallpapers", new HashSet<>()));
            if (updatedBookmarks.contains(wallpaper)) {
                updatedBookmarks.remove(wallpaper);
                bookmarkBtn.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.add_bookmark, 0, 0);
                Toast.makeText(this, "Bookmark removed", Toast.LENGTH_SHORT).show();
            } else {
                updatedBookmarks.add(wallpaper);
                bookmarkBtn.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.remove_bookmark, 0, 0);
                Toast.makeText(this, "Bookmarked!", Toast.LENGTH_SHORT).show();
            }
            prefs.edit().putStringSet("wallpapers", updatedBookmarks).apply();
        });
    }
}