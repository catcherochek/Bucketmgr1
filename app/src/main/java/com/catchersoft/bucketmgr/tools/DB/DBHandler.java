package com.catchersoft.bucketmgr.tools.DB;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Клим on 07.04.2018.
 */

public class DBHandler {
    private static DBHandler ourInstance;
    private static DBhelper dbh;
    private static Context context;
    private static SQLiteDatabase tempsd;
    private static Cursor tempc;

    public static DBHandler getInstance() throws RuntimeException {
        if (context==null)
            throw new RuntimeException("Instance cannot be initialized without context use getInstance(Context  contex) method");
        return ourInstance;
    }
    public static DBHandler getInstance(Context  contex) {
        if (ourInstance == null)
        {
            ourInstance= new DBHandler();

        }
        if (context != contex){
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
            if (tempsd != null)
                tempsd.close();
            tempsd = sd;
            Cursor c = sd.rawQuery(query,new String[]{});
            if (c != null) {
                if (tempc != null)
                    tempc.close();
                tempc = c;
                return  c;
            }
        }
        return null;
    }
    public void InitWrite(String query){
        if ((ourInstance != null) & (dbh != null)) {
            SQLiteDatabase sd = dbh.getWritableDatabase();
            if (tempsd != null)
                tempsd.close();
            tempsd = sd;
            sd.execSQL(query);
        }


    }
    public void close(){
        if (tempc != null)
            tempc.close();
        if (tempsd != null)
            tempsd.close();

    }


}
