package com.raghav.android.crimedb;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.UUID;


public class CrimeActivity extends SingleFragmentActivity {
    public final static String Extra="app/UUID";

    @Override
    protected Fragment createFragment(){
        UUID uuid=(UUID)getIntent().getSerializableExtra(Extra);
        return  CrimeFragment.newInstance(uuid);
    }

    public static Intent newIntent(Context context, UUID id){
        Intent intent = new Intent(context,CrimeActivity.class);
        intent.putExtra(Extra,id);
        return intent;
    }
}
