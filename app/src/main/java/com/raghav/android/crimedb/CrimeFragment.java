package com.raghav.android.crimedb;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;

import java.util.Date;
import java.util.UUID;
import java.util.zip.Inflater;

/**
 * Created by Vighnesh on 17-04-2018.
 */

//CONTROLLER

public class CrimeFragment extends android.support.v4.app.Fragment {
    private Crime mCrime;
    private EditText mTextField;
    private Button mDateButton;
    private CheckBox mCheckBox;
    public   static  final String ARG_CRIME_ID="ARG_CRIME_ID";
    public   static  final String DIALOG_DATE = "dialogdate";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        UUID id =(UUID) getActivity().getIntent().getSerializableExtra(CrimeActivity.Extra);
        UUID crimeID = (UUID) getArguments().getSerializable(ARG_CRIME_ID);
        mCrime = CrimeLab.get(getActivity()).getCrime(crimeID);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v;
        v = inflater.inflate(R.layout.fragment_crime ,container,false);
        mTextField=(EditText)v.findViewById(R.id.edit_field);
        mDateButton=(Button)v.findViewById(R.id.date_button) ;
        mCheckBox=(CheckBox)v.findViewById(R.id.crime_solved) ;


        mTextField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mCrime.setTitle(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        mTextField.setText(mCrime.getTitle());
        mDateButton.setText(mCrime.getDate().toString());
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getFragmentManager();//get the fragment
                DatePickerFragment mDateFragment = DatePickerFragment.newInstance(mCrime.getDate());//create dialog
                mDateFragment.show(fm,DIALOG_DATE);//send fragment and tag to show mwthod of
                //DialogFragment class
            }
        });
        mCheckBox.setChecked(mCrime.isSolved());
        mCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mCrime.setSolved(b);
            }
        });
        return v;
    }
    public static CrimeFragment newInstance(UUID uuid){
        Bundle args = new Bundle();
        args.putSerializable(ARG_CRIME_ID,uuid);
        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(args);
        return fragment;
    }

}
