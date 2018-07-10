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

public class CrimePagerActivity extends AppCompatActivity {
    private static final String EXTRA_ID = "CRIME_ID";
    private ViewPager mViewPager;
    private List<Crime> mCrimeList;

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

        mViewPager = (ViewPager)findViewById(R.id.crime_view_pager);
        mCrimeList =  CrimeLab.get(this).getCrimes();
        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                Crime crime = mCrimeList.get(position);
                return CrimeFragment.newInstance(crime.getId());
            }

            @Override
            public int getCount() {
                return mCrimeList.size();
            }
        });
    }
}
