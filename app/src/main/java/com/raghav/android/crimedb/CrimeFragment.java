package com.raghav.android.crimedb;


import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.FileProvider;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.zip.Inflater;

/**
 * Created by Vighnesh on 17-04-2018.
 */

//CONTROLLER

public class CrimeFragment extends android.support.v4.app.Fragment {
    private Crime mCrime;
    private EditText mTextField;
    private Button mDateButton ,mReportButton,mSuspectButton;
    private CheckBox mCheckBox;
    public   static  final String ARG_CRIME_ID="ARG_CRIME_ID";
    public   static  final String DIALOG_DATE = "dialogdate";
    private static final int REQ_CODE=0;
    private static final int REQ_CONTACT = 1;
    private  static final int REQ_PHOTO=2;
    private ImageView mImageView;
    private ImageButton mImageButton;
    private File mPhotoFile;
    public static final String ImageDialog = "ImageDialog";



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        UUID id =(UUID) getActivity().getIntent().getSerializableExtra(CrimeActivity.Extra);
        UUID crimeID = (UUID) getArguments().getSerializable(ARG_CRIME_ID);
        mCrime = CrimeLab.get(getActivity()).getCrime(crimeID);
        mPhotoFile = CrimeLab.get(getActivity()).getPhotoFile(mCrime);
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
        updateDate();
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getFragmentManager();//get the fragment
                DatePickerFragment mDateFragment = DatePickerFragment.newInstance(mCrime.getDate());//create dialog
                mDateFragment.setTargetFragment(CrimeFragment.this,REQ_CODE);//set crimefrgmt as target fragmt to get back the result
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
        mReportButton = (Button)v.findViewById(R.id.crime_report);
        mReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT,getCrimeReport());
                intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.crime_report_subject));
                //to make chooser is displayed every time, use static Intent.createChooser()
                intent = Intent.createChooser(intent,getString(R.string.send_report));//send_report is the title for the chooser
                startActivity(intent);
            }
        });
        final Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        //intent.addCategory(Intent.CATEGORY_HOME);
        mSuspectButton = (Button)v.findViewById(R.id.crime_suspect);
        mSuspectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(intent,REQ_CONTACT);
            }
        });
        if(mCrime.getSuspect() != null){
            mSuspectButton.setText(mCrime.getSuspect());
        }
        //check if any contact app installed
        //PackageManager has all details of activity that matches intent
        PackageManager packageManager = getActivity().getPackageManager();
        if(packageManager.resolveActivity(intent,PackageManager.MATCH_DEFAULT_ONLY)==null){
            mSuspectButton.setEnabled(false);
        }

        mImageView = (ImageView)v.findViewById(R.id.photo);
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mPhotoFile.exists()) {
                    FragmentManager fragmentManager = getFragmentManager();
                    ImageViewFragment imageViewFragment = ImageViewFragment.newInstance(mPhotoFile);
                    imageViewFragment.show(fragmentManager,ImageDialog);
                }
            }
        });
        mImageButton = (ImageButton)v.findViewById(R.id.camera_button);

        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        boolean canTakePhoto = mPhotoFile != null && captureImage.resolveActivity(packageManager)!=null;
        mImageButton.setEnabled(canTakePhoto);

        mImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = FileProvider.getUriForFile(getActivity(),"com.raghav.android.crimedb.fileprovider",mPhotoFile);
                captureImage.putExtra(MediaStore.EXTRA_OUTPUT,uri);

                List<ResolveInfo> cameraActivities =getActivity().getPackageManager().queryIntentActivities(captureImage,PackageManager.MATCH_DEFAULT_ONLY);
                for (ResolveInfo activity:cameraActivities){
                    getActivity().grantUriPermission(activity.activityInfo.packageName,uri,Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                }
                startActivityForResult(captureImage,REQ_PHOTO);
            }
        });
        //update imageView
        updatePhotoView();

        return v;
    }
    public static CrimeFragment newInstance(UUID uuid){
        Bundle args = new Bundle();
        args.putSerializable(ARG_CRIME_ID,uuid);
        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode!= Activity.RESULT_OK)
            return;
        if(requestCode==REQ_CODE){
            Date date = (Date)data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mCrime.setDate(date);
            updateDate();
        }
        if(requestCode == REQ_CONTACT && data!=null){
            Uri contactUri = data.getData();
            //which value or field to be fetched
            String [] queryField = new String[]{ContactsContract.Contacts.DISPLAY_NAME};
            //perform query using contentResolver
            Cursor c = getActivity().getContentResolver().query(contactUri,queryField,null,null,null);

            try{
                if(c.getCount()==0)
                    return;
                c.moveToFirst();
                String suspect = c.getString(0);//from 0th pos
                mCrime.setSuspect(suspect);
                mSuspectButton.setText(suspect);
            }finally {
                c.close();
            }


        }
        if(requestCode == REQ_PHOTO ){
            Uri uri = FileProvider.getUriForFile(getActivity(),"com.raghav.android.crimedb.fileprovider",mPhotoFile);

            //revoke the permission for apps
            getActivity().revokeUriPermission(uri,Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            updatePhotoView();
        }

    }

    private void updateDate() {
        mDateButton.setText(mCrime.getDate().toString());
    }

    @Override
    public void onPause() {
        super.onPause();
        CrimeLab.get(getActivity()).updateCrime(mCrime);
    }

    private String getCrimeReport(){
        String solvedString = null;
        if(mCrime.isSolved()){
            solvedString = getString(R.string.crime_report_solved);
        }else {
            solvedString = getString(R.string.crime_report_unsolved);
        }
        String dateFormat = "EEE, MMM dd";
        String dateString = DateFormat.format(dateFormat,mCrime.getDate()).toString();

        String suspect = mCrime.getSuspect();
        if(suspect == null){
            suspect = getString(R.string.crime_report_no_suspect);
        }else {
            suspect = getString(R.string.crime_report_suspect,suspect);
        }

        String report = getString(R.string.crime_report,mCrime.getTitle(), dateString,solvedString,suspect);
        return report;
    }

    private void updatePhotoView(){
        if(mPhotoFile == null || !mPhotoFile.exists()){
            mImageView.setImageDrawable(null);
        }else {
            //get bitmap;
            Bitmap bitmap = PictureUtils.getScaledBitmap(mPhotoFile.getPath(),getActivity());
            mImageView.setImageBitmap(bitmap);
        }
    }
}
