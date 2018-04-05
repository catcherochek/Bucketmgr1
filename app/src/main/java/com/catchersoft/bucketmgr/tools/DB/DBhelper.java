package com.catchersoft.bucketmgr.tools.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.catchersoft.bucketmgr.tools.DB.DBConstants;

/**
 * Created by Клим on 09.09.2017.
 * класс для обслуживания БД - тип Singleton
 * создается через getInstance
 */

public class DBhelper extends SQLiteOpenHelper {
    private  static Context context;
    private static DBhelper self;

    /**
     * заменяет конструктор
     * @param context - параметр оставленый для совместимости.(впадло переписывать во всем коде)
     * @return возвращает объект.
     */
    public static DBhelper getInstance(Context context){// аля - конструктор
        if (self == null){
            self = new DBhelper(context);

        }
        return self;
    }



    public void Close(){

    }
    private DBhelper(Context context) {
        // конструктор суперкласса
        super(context, DBConstants.DATABASE_DBNAME, null, DBConstants.DATABASE_VERSION);
        this.context=context;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(DBConstants.QUERY_TABLE_GOODS_CREATE);
        db.execSQL(DBConstants.QUERY_TABLE_INV_CREATE);
        db.execSQL(DBConstants.QUERY_TABLE_DEBITORS_CREATE);
        db.execSQL(DBConstants.QUERY_TABLE_DEBITORS_TRANSACTIONS_CREATE);
        db.execSQL(DBConstants.QUERY_TABLE_SKLAD_CREATE);
        db.execSQL(DBConstants.QUERY_TABLE_SUPPLIERS_CREATE);
        db.execSQL(DBConstants.QUERY_TABLE_TRANSACTIONS_CREATE);
        db.execSQL(DBConstants.QUERY_VIEW_SELECT_DEBITORS_JOURNAL_CREATE);
        db.execSQL(DBConstants.QUERY_VIEW_SELECT_TOTAL_REPORT_CREATE);
        db.execSQL(DBConstants.QUERY_VIEW_SELECT_TRANSACTIONS_CREATE);
        db.execSQL(DBConstants.QUERY_VIEW_SELECT_DEBITORS_REPORT_CREATE);

        FillWithData(db);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {


    }

    public void FillWithData(SQLiteDatabase db){

        for (String query:DBConstants.QUERIES_FILL_GOODS) {
          db.execSQL(query);
        }
        for (String query:DBConstants.QUERIES_FILL_SUPPLIERS) {
            db.execSQL(query);
        }
        for (String query:DBConstants.QUERIES_FILL_TRANSACTIONS) {
            db.execSQL(query);
        }
    }
}
