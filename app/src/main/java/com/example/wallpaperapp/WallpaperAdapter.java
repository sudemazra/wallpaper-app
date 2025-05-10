package com.example.wallpaperapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.squareup.picasso.Picasso;

import java.util.List;

public class WallpaperAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Context ctx;
    private final List<String> wallpaperList;
    private InterstitialAd mInterstitialAd;

    WallpaperAdapter (Context ctx, List<String> wallpaperList) {
        this.ctx = ctx;
        this.wallpaperList = wallpaperList;
        loadInsterstitialAd();
    }

    private void loadInsterstitialAd() {
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(ctx, ctx.getString(R.string.inters), adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                mInterstitialAd = null;
                loadInsterstitialAd();
            }

            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                super.onAdLoaded(interstitialAd);
                mInterstitialAd = interstitialAd;
            }
        });
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new WallpaperView(LayoutInflater.from(ctx).inflate(R.layout.wallpaper, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        WallpaperView wallpaperView = (WallpaperView) holder;
        Picasso.get().load(wallpaperList.get(position)).placeholder(R.mipmap.logo).fit().into(wallpaperView.image);


        wallpaperView.itemView.setOnClickListener(view -> {
           PreviewActivity.wallpaper = wallpaperList.get(position);
           if(mInterstitialAd != null) {
               mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                   @Override
                   public void onAdDismissedFullScreenContent() {
                       super.onAdDismissedFullScreenContent();
                       ctx.startActivity(new Intent(ctx, PreviewActivity.class));
                   }
                   @Override
                   public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                       super.onAdFailedToShowFullScreenContent(adError);
                   }
               });
               mInterstitialAd.show((Activity) ctx);
           } else {
               ctx.startActivity(new Intent(ctx, PreviewActivity.class));
           }
        });
    }

    @Override
    public int getItemCount() {
        return wallpaperList.size();
    }

    private static class WallpaperView extends RecyclerView.ViewHolder {
        private final ImageView image;
        public WallpaperView(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.img);
        }
    }
}
