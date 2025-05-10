package com.example.wallpaperapp;

import static com.example.wallpaperapp.MainActivity.category;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class MainFragment extends Fragment {

    private int counter;
    private Context context;
    private RecyclerView recyclerView;
    private static final String BASE_URL = "https://wallpaper-app-1f045-default-rtdb.firebaseio.com/";
    private static final String ARG_COUNT = "param1";

    public MainFragment() {

    }

    public static MainFragment newInstance(int counter) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COUNT,counter);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            counter = getArguments().getInt(ARG_COUNT);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getContext();
        recyclerView = view.findViewById(R.id.recyclerView);
        loadWallpapers();
    }

    private void loadWallpapers() {
        Loader loader = new Loader(context);
        loader.show();
        List<String> wallpaperList = new ArrayList<>();
        String url = BASE_URL + category[counter] +  "/.json";
        Volley.newRequestQueue(context).add(new JsonArrayRequest(url, response -> {
            try {
                for(int i = 0 ; i < response.length() ; i++) {
                    wallpaperList.add(response.getString(i));
                }
                recyclerView.setAdapter(new WallpaperAdapter(context, wallpaperList));
                loader.dismiss();
            } catch (JSONException e) {
                loader.dismiss();
                String message = e.getMessage();
                if (message == null || message.trim().isEmpty()) {
                    message = "Bir hata oluştu!";
                }
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }

        }, error -> {
            loader.dismiss();
            String message = error.getMessage();
            if (message == null || message.trim().isEmpty()) {
                message = "Bir hata oluştu!";
            }
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }));
    }
}
