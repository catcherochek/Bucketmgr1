package com.catchersoft.bucketmgr.tools;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import com.catchersoft.bucketmgr.MainActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Клим on 17.09.2017.
 */

public class ReportsaveHelper {

    private File ReportDir;
    Context context;



    public  ReportsaveHelper(Context tis){
        context = tis;
    }
    public void createreportdir() {
        DateFormat df = new SimpleDateFormat("dd-MM-yy-HH-mm-ss");
        Date dateobj = new Date();
        String currentTime = df.format(dateobj);
        File reportdir = new File(Environment.getExternalStorageDirectory()+"/Bucketmgr/Reports/"+currentTime);
        reportdir.mkdirs();
        boolean res = true;
        if (!reportdir.exists()){
            res = reportdir.mkdirs();

        }
        if (res){
        ReportDir = reportdir;}
    }
    public void SaveCsvReportGoods(String Reportname){
        File f = new File(ReportDir.toString()+"/"+Reportname);
        //f.mkdir();
        try {
            f.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        DBhelper dbh = new DBhelper(context);
        SQLiteDatabase db = dbh.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from select_total_report",new String[]{});
        cursor.moveToFirst();
        FileOutputStream dest = null;
        try {
            dest = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        String firstcolumn = "Имя поставщика; Наименование; Долг \n";
        byte[] col = firstcolumn.getBytes();
        DateFormat df = new SimpleDateFormat("dd-MM-yy");
        Date dateobj = new Date();
        String currentTime = df.format(dateobj);
        try {
            dest.write((new String(";Отчет по поставщикам от "+currentTime+"\n")).getBytes());
            dest.write(col);
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (!cursor.isAfterLast()) {

            String supplier= cursor.getString(cursor.getColumnIndex("supplier"));
            //int _id = cursor.getInt(cursor.getColumnIndex("id"));
            String goods = cursor.getString(cursor.getColumnIndex("goods"));
            String count = cursor.getString(cursor.getColumnIndex("summ"));
            //String date = cursor.getString(cursor.getColumnIndex("date"));
            String temp = supplier+";"+goods+";"+count+"\n";
            try {
                dest.write(temp.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
            cursor.moveToNext();
        }
        try {
            dest.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    };

}


