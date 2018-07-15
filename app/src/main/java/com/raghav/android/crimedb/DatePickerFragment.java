package com.raghav.android.crimedb;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Vighnesh on 14-07-2018.
 */

public class DatePickerFragment extends DialogFragment {
    public static final String ARG_DATE = "date";
    private DatePicker mDatePicker;
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_date,null);
        Date date = (Date)getArguments().getSerializable(ARG_DATE);

        Calendar mCalender  = Calendar.getInstance();
        mCalender.setTime(date);
        int day = mCalender.get(Calendar.DAY_OF_MONTH);
        int month = mCalender.get(Calendar.MONTH);
        int year = mCalender.get(Calendar.YEAR);
        //create datePicker Manually
        mDatePicker = (DatePicker)v.findViewById(R.id.dialog_date_picker);
        //initialize datepicker
        mDatePicker.init(year,month,day,null);

        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(R.string.date_picker_title)
                .setPositiveButton(android.R.string.ok,null)
                .create();
    }
    public static DatePickerFragment newInstance(Date date){
        Bundle args = new Bundle();
        args.putSerializable(ARG_DATE,date);
        DatePickerFragment fragment = new DatePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
