package com.example.wallpaperapp;

import android.app.Dialog;
import android.content.Context;

import androidx.annotation.NonNull;

public class Loader extends Dialog {
    public Loader(@NonNull Context context) {
        super(context);
        setContentView(R.layout.loader);
        setCancelable(false);
    }
}
