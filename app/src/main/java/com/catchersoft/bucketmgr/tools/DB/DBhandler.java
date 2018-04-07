package com.catchersoft.bucketmgr.tools.DB;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Клим on 07.04.2018.
 */

class DBHandler {
    private static DBHandler ourInstance;
    private static DBhelper dbh;
    private static Context context;
    static DBHandler getInstance(Context  contex) {
        if (ourInstance == null)
        {
            ourInstance= new DBHandler();
            dbh = DBhelper.getInstance(contex);
            context=contex;
        }
        return ourInstance;
    }

    private DBHandler() {

    }


    public Cursor InitRead(String query){
        if ((ourInstance != null) & (dbh != null)) {
            SQLiteDatabase sd = dbh.getWritableDatabase();
            Cursor c = sd.rawQuery(query,new String[]{});
            if (c != null) {
                return  c;
            }
        }
        return null;
    }
    public void InitWrite(String query){
        if ((ourInstance != null) & (dbh != null)) {
            SQLiteDatabase sd = dbh.getWritableDatabase();
            sd.execSQL(query);
        }


    }

}
