package com.matthiasbruns.logos.content.logos;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by mbruns on 06/12/2016.
 */

public class LogoDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "logos.db";

    private static final String TAG = LogoDBHelper.class.getName();

    private static final String ENABLE_FOREIGN_KEYS = "PRAGMA foreign_keys = ON;";

    private static final int DATABASE_VERSION = 1;

    private static final String LOGOS_CREATE = "CREATE TABLE "
            + LogoContract.Logo.TABLE_NAME + "("
            + LogoContract.Logo.Columns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + LogoContract.Logo.Columns.EXTERNAL_ID + " STRING NOT NULL, "
            + LogoContract.Logo.Columns.LOGO_URL + " STRING NOT NULL, "
            + LogoContract.Logo.Columns.NAME + " STRING NOT NULL, "
            + LogoContract.Logo.Columns.SHORT_NAME + " STRING, "
            + LogoContract.Logo.Columns.SOURCE + " STRING NOT NULL, "
            + LogoContract.Logo.Columns.URL + " STRING NOT NULL"
            + ");";

    public LogoDBHelper(final Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(final SQLiteDatabase db) {
        execSQL(db, LOGOS_CREATE);
    }

    @Override
    public void onOpen(final SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            execSQL(db, ENABLE_FOREIGN_KEYS);
        }
    }

    @Override
    public void onUpgrade(final SQLiteDatabase db, final int i, final int i1) {

    }

    private void execSQL(final SQLiteDatabase db, final String statement) {
        Log.v(TAG, statement);
        db.execSQL(statement);
    }
}
