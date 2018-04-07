package com.catchersoft.bucketmgr.tools.DB;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Клим on 07.04.2018.
 */

/**
 * Класс для работы с базой данных -одиночка
 * для создания используем ГэтИнстанс()
 */
public class DBHandler {
    /**
     * собственно сам класс
     */
    private static DBHandler ourInstance;
    /**
     * класс отвечающий за открытие БД
     */
    private static DBhelper dbh;
    /**
     * контекст переопределяется каждый раз при вызове getInstance(Context  contex)
     */
    private static Context context;
    private static SQLiteDatabase tempsd;
    private static Cursor tempc;

    /**
     * Получение объекта класса, в случае если класс не созданый выхывается исключение,
     * для создания класса или для переопределения контекста используйте переопределенный метод getInstance(Context  contex)
     * @return объект класса
     * @throws RuntimeException
     */
    public static DBHandler getInstance() throws RuntimeException {
        if (context==null)
            throw new RuntimeException("Instance cannot be initialized without context use getInstance(Context  contex) method");
        return ourInstance;
    }

    /**
     * Получение объекта класса,
     * создание, переопределение с учетом нового контекста, ссылка на созданый класс
     * если класс не создан создаются все нужные конструкторы
     *
     * @param contex если указан, то программа проверяет и переопределяет контекст если надо
     * @return
     */
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

    /**
     * подключение к БД для выборки типа Select....
     * @param query сырой запрос,  см. DBConstants.QUERY_GET_.....
     * @return Cursor Курсор, в случае удачной выборки
     *
     */
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

    /**
     * подключение к БД в случае INSERT\UPDATE\DELETE
     * @param query сырой запрос,  см. DBConstants.QUERY_GET_.....
     */
    public void InitWrite(String query){
        if ((ourInstance != null) & (dbh != null)) {
            SQLiteDatabase sd = dbh.getWritableDatabase();
            if (tempsd != null)
                tempsd.close();
            tempsd = sd;
            sd.execSQL(query);
        }


    }

    /**
     * Закрытия подключений
     */
    public void close(){
        if (tempc != null)
            tempc.close();
        if (tempsd != null)
            tempsd.close();

    }


}
