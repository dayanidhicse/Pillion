package com.example.dayanidhi.pillion.animations;

import android.view.Menu;
import android.view.MenuItem;

import com.github.paolorotolo.appintro.AppIntro;


public abstract class BaseAppIntro extends AppIntro {
    private int mScrollDurationFactor = 1;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        setScrollDurationFactor(mScrollDurationFactor);
        return super.onOptionsItemSelected(item);
    }
}
