package com.raghav.android.crimedb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.raghav.android.crimedb.database.CrimeBaseHelper;
import com.raghav.android.crimedb.database.CrimeCursorWrapper;
import com.raghav.android.crimedb.database.CrimeDbSchema;
import com.raghav.android.crimedb.database.CrimeDbSchema.CrimeTable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Vighnesh on 22-04-2018.
 */


///MODEL

    //contains array list

//a singleton class which holds only one object of crime
public class CrimeLab {
    private static CrimeLab sCrimeLab;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    private CrimeLab(Context context){
        mContext = context.getApplicationContext();
        mDatabase = new CrimeBaseHelper(context).getWritableDatabase();
    }

    public void updateCrime(Crime crime){
        String uuidString = crime.getId().toString();
        ContentValues values = getContentValues(crime);

        mDatabase.update(CrimeTable.NAME,values,CrimeTable.Cols.UUID + "=?",new String[] {uuidString} );
    }

    public void addCrime(Crime c){
        ContentValues values = getContentValues(c);
        mDatabase.insert(CrimeTable.NAME,null,values);
    }
    public List<Crime> getCrimes(){
        List<Crime> crimeList = new ArrayList<>();
        CrimeCursorWrapper cursorWrapper = queryCrimes(null,null);
        try{
            cursorWrapper.moveToFirst();
            while (! cursorWrapper.isAfterLast()){
                crimeList.add(cursorWrapper.getCrime());
                cursorWrapper.moveToNext();
            }
        }finally {
            cursorWrapper.close();
        }
        return crimeList;
    }
    public Crime getCrime(UUID id){
        CrimeCursorWrapper cursorWrapper = queryCrimes(CrimeTable.Cols.UUID+"=?",new String[]{id.toString()});
        try{
            if (cursorWrapper.getCount()==0){
                return null;
            }
            cursorWrapper.moveToFirst();
            return cursorWrapper.getCrime();
        }finally {
            cursorWrapper.close();
        }
    }

    public static CrimeLab get(Context context){
        if(sCrimeLab==null){
            sCrimeLab = new CrimeLab(context);
        }
        return sCrimeLab;
    }
    private static ContentValues getContentValues(Crime crime){
        ContentValues values = new ContentValues();
        values.put(CrimeTable.Cols.UUID,crime.getId().toString());
        values.put(CrimeTable.Cols.TITLE,crime.getTitle().toString());
        values.put(CrimeTable.Cols.DATE,crime.getDate().toString());
        values.put(CrimeTable.Cols.SOLVED,crime.isSolved() ? 1 : 0 );
        values.put(CrimeTable.Cols.SUSPECT, crime.getSuspect());

        return values;
    }
    private CrimeCursorWrapper queryCrimes(String WhereClause, String[] WhereArgs){
        Cursor cursor = mDatabase.query(CrimeTable.NAME,null,WhereClause,WhereArgs,null,null,null);
        return new CrimeCursorWrapper(cursor);
    }

    public File getPhotoFile(Crime crime){
        File fileDir = mContext.getFilesDir();
        String filename = crime.getPhotoFileName();
        //Log.d("CrimeDB", "PhotoFilePath"+filename);
        return new File(fileDir,filename);
    }
}
