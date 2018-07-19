package com.raghav.android.crimedb;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.UUID;

/**
 * Created by Vighnesh on 22-04-2018.
 */

//CONTROLLER


public class CrimeListFragment extends Fragment{
    private RecyclerView mCrimeRecyclerView;
    private CrimeAdapter mCrimeAdapter;
    private static  final int REQ_CODE=1;
    private boolean mSubtitleVisible;
    private static  final String SAVED_SUBTITLE_VISIBLE = "subtitle_visible";

    int mLastPos;

            //contains adapter from RecyclerView.Adapter which creates viewHolder and binds it to
            //individual crime
            /*
            calls onCreateViewHolder
             */
            private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder>{
                private List<Crime> mCrimes;

                public CrimeAdapter(List<Crime> crimes){
                    mCrimes=crimes;
                }

                @Override
                public CrimeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                    LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
                    return new CrimeHolder(layoutInflater,parent);
                }

                @Override
                public void onBindViewHolder(CrimeHolder holder, int position) {
                    Crime crime = mCrimes.get(position);
                    holder.bind(crime,position);
                }

                @Override
                public int getItemCount() {
                    return mCrimes.size();
                }

            }

            private class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
                private TextView mTitleTextView, mDateTextView;
                private ImageView mImageView;
                private Crime mCrime;
                private int pos=0;

                public CrimeHolder(LayoutInflater inflater, ViewGroup parent){
                    super(inflater.inflate(R.layout.list_item_crime, parent,false));

                    mTitleTextView = (TextView)itemView.findViewById(R.id.crime_title);
                    mDateTextView = (TextView)itemView.findViewById(R.id.crime_date);
                    mImageView = (ImageView)itemView.findViewById(R.id.crime_solved);
                    itemView.setOnClickListener(this);
                }
                public void bind(Crime crime,int position){
                    //pos = position;
                    mCrime=crime;
                    mTitleTextView.setText(mCrime.getTitle());
                    mDateTextView.setText(mCrime.getDate().toString());
                    mImageView.setVisibility(mCrime.isSolved()?View.VISIBLE:View.GONE);
                }

                @Override
                public void onClick(View view) {
                    //Toast.makeText(getActivity(),mCrime.getTitle()+" clicked!",Toast.LENGTH_SHORT).show();
                    UUID id = mCrime.getId();
                    Intent intent = CrimePagerActivity.newIntent(getActivity(), id);
                     pos = mCrimeRecyclerView.getChildLayoutPosition(view);
                    intent.putExtra("position", pos);
                    mLastPos=pos;
                    startActivity(intent);
                }
            }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==REQ_CODE){

        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crime_list,container,false);
        mCrimeRecyclerView = (RecyclerView)view.findViewById(R.id.crime_recycler_view);
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        if(savedInstanceState != null){
            mSubtitleVisible = savedInstanceState.getBoolean(SAVED_SUBTITLE_VISIBLE);
        }

        updateUI();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVED_SUBTITLE_VISIBLE,mSubtitleVisible);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime_list,menu);
        MenuItem mSubtitleItem = menu.findItem(R.id.show_subtitle);
        if(mSubtitleVisible){
            mSubtitleItem.setTitle(R.string.hide_subtitle);
        }else {
            mSubtitleItem.setTitle(R.string.show_subtitle);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.new_crime :
                Crime crime = new Crime();
                CrimeLab.get(getActivity()).addCrime(crime);
                Intent intent = CrimePagerActivity.newIntent(getActivity(),crime.getId());
                startActivity(intent);
                return  true;
            case R.id.show_subtitle:
                mSubtitleVisible = !mSubtitleVisible;
                getActivity().invalidateOptionsMenu();
                updateSubtitle();
                return  true;
            default :
                super.onOptionsItemSelected(item);
        }
        return false;

    }
    private void updateSubtitle(){
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        int crimeCount = crimeLab.getCrimes().size();
        String subtitle = getString(R.string.subtitle_format,crimeCount);
        if(!mSubtitleVisible){
            subtitle = null;
        }
        AppCompatActivity appCompatActivity = (AppCompatActivity)getActivity();//should use appcompactlibrary's method
        //so cast your activity to appCompact
        appCompatActivity.getSupportActionBar().setSubtitle(subtitle);
    }

    private void updateUI(){
        CrimeLab crimeLab = CrimeLab.get(getActivity());//getActivity() returns Context object which stores info of activity
        List<Crime> crimes = crimeLab.getCrimes();
        if(mCrimeAdapter==null) {
            mCrimeAdapter = new CrimeAdapter(crimes);
            mCrimeRecyclerView.setAdapter(mCrimeAdapter);
        }
        else
            mCrimeAdapter.notifyItemChanged(mLastPos);

        updateSubtitle();
    }

}
