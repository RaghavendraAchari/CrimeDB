package com.raghav.android.crimedb;


import android.support.v4.app.Fragment;

/**
 * Created by Vighnesh on 22-04-2018.
 */

//CONTROLLER

public class CrimeListActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment(){
        return new CrimeListFragment();
    }
}
