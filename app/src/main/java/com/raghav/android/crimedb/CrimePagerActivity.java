package com.raghav.android.crimedb;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import java.util.List;
import java.util.UUID;

/**
 * Created by Vighnesh on 07-07-2018.
 */

// Controller

public class CrimePagerActivity extends AppCompatActivity {
    private static final String EXTRA_ID = "CRIME_ID";
    private ViewPager mViewPager;
    private List<Crime> mCrimeList;
    private Adapter mAdapter ;

    private class Adapter extends FragmentStatePagerAdapter{
        Adapter(FragmentManager fm){
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Crime crime = mCrimeList.get(position);
            return CrimeFragment.newInstance(crime.getId());
        }

        @Override
        public int getCount() {
            return mCrimeList.size();
        }
    }

    public static Intent newIntent(Context context, UUID crimeId){
        Intent intent = new Intent(context,CrimePagerActivity.class);
        intent.putExtra(CrimePagerActivity.EXTRA_ID,crimeId);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime_pager);


        UUID id =(UUID) getIntent().getSerializableExtra(EXTRA_ID);
        //assign viewpager layout and crime list
        mViewPager = (ViewPager)findViewById(R.id.crime_view_pager);
        mCrimeList =  CrimeLab.get(this).getCrimes();

        //viewpager needs fragment manager to add and detach fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        //class for adapter "FragmentStatePagerAdapter"
        //like Recycler ViewPager also needs the adapter to get individual fragment and count
        //create a Adapter object for ViewPager to implement two methods
        //getCount() and getItem()
        mAdapter = new Adapter(fragmentManager);
        mViewPager.setAdapter(mAdapter);
        for (int i=0;i<mCrimeList.size();i++){
            if(mCrimeList.get(i).getId().equals(id)){
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }
}
