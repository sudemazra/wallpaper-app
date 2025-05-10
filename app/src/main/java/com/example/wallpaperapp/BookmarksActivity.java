package com.example.wallpaperapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wallpaperapp.WallpaperAdapter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class BookmarksActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmarks);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        SharedPreferences prefs = getSharedPreferences("bookmarks", MODE_PRIVATE);
        Set<String> bookmarks = prefs.getStringSet("wallpapers", new HashSet<>());

        if (bookmarks.isEmpty()) {
            Toast.makeText(this, "No bookmarks yet!", Toast.LENGTH_SHORT).show();
        }

        // Burada kendi adapterini kullanacaksın
        ArrayList<String> bookmarkList = new ArrayList<>(bookmarks);
        WallpaperAdapter adapter = new WallpaperAdapter(this, bookmarkList);
        recyclerView.setAdapter(adapter);
    }
}