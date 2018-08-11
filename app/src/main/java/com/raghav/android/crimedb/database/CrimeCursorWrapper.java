package com.raghav.android.crimedb.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.raghav.android.crimedb.Crime;
import com.raghav.android.crimedb.database.CrimeDbSchema.CrimeTable;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Vighnesh on 23-07-2018.
 */

public class CrimeCursorWrapper extends CursorWrapper {

    public  CrimeCursorWrapper(Cursor cursor){
        super(cursor);
    }
    public Crime getCrime(){
        String uuid = getString(getColumnIndex(CrimeTable.Cols.UUID));
        String title = getString(getColumnIndex(CrimeTable.Cols.TITLE));
        Long date = getLong(getColumnIndex(CrimeTable.Cols.DATE));
        int isSolved = getInt(getColumnIndex(CrimeTable.Cols.SOLVED));
        String suspect = getString(getColumnIndex(CrimeTable.Cols.SUSPECT));

        Crime crime = new Crime(UUID.fromString(uuid));
        crime.setTitle(title);
        crime.setSolved(isSolved != 0);
        crime.setDate(new Date(date));
        crime.setSuspect(suspect);

        return  crime;
    }
}
