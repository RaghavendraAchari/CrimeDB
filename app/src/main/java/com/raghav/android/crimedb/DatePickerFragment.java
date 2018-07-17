package com.raghav.android.crimedb;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Vighnesh on 14-07-2018.
 */

public class DatePickerFragment extends DialogFragment {
    public static final String ARG_DATE = "date";
    private DatePicker mDatePicker;
    public static final String EXTRA_DATE = "passed_date";
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
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int year = mDatePicker.getYear();
                        int month = mDatePicker.getMonth();
                        int day = mDatePicker.getDayOfMonth();
                        Date date= new GregorianCalendar(year,month,day).getTime();// get time returns the date
                        sendResult(Activity.RESULT_OK,date);
                    }
                })
                .create();
    }
    public static DatePickerFragment newInstance(Date date){
        Bundle args = new Bundle();
        args.putSerializable(ARG_DATE,date);
        DatePickerFragment fragment = new DatePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }
    private void sendResult(int resultCode, Date date){
        if(getTargetFragment()==null)
            return;
        //create new intent and add extra date
        Intent intent = new Intent();
        intent.putExtra(DatePickerFragment.EXTRA_DATE,date);
        getTargetFragment().onActivityResult(getTargetRequestCode(),resultCode,intent);
    }
}
