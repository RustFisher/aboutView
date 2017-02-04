package com.rust.aboutview.service;

import android.service.dreams.DreamService;

import com.rust.aboutview.R;

public class MyDayDream extends DreamService {

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        // Exit dream upon user touch
        setInteractive(false);
        // Hide system UI
        setFullscreen(true);
        // Set the dream layout
        setContentView(R.layout.my_day_dream);
    }

}
