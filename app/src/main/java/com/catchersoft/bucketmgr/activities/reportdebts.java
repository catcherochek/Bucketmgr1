package com.catchersoft.bucketmgr.activities;


import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.catchersoft.bucketmgr.R;
import com.catchersoft.bucketmgr.tools.DBhelper;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */




public class reportdebts extends Fragment  {


    private String Statement="";

    public reportdebts() {
        // Required empty public constructor
    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reportdebts, container, false);
    }





    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Отчеты по должникам");

        ListView listView = (ListView)view.findViewById(R.id.fragment_reportdebts_dataview);
// используем адаптер данных
        ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
        HashMap<String, String> map;

        DBhelper dbh  = new DBhelper(this.getContext());
        SQLiteDatabase db = dbh.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from select_debitors_report "+Statement+ " order by name",new String[]{});
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            map = new HashMap<>();
            String supplier= cursor.getString(cursor.getColumnIndex("name"));
            String desc = cursor.getString(cursor.getColumnIndex("description"));
            //int _id = cursor.getInt(cursor.getColumnIndex("id"));
            String goods = cursor.getString(cursor.getColumnIndex("name:1"));
            String count = cursor.getString(cursor.getColumnIndex("summ"));
            //String date = cursor.getString(cursor.getColumnIndex("date"));

            //map.put("id",String.valueOf(_id));

            map.put("supplier",supplier);
            map.put("description",desc);
            map.put("goods",goods);
            map.put("summ",count);
            //map.put("date",date);
            arrayList.add(map);
            cursor.moveToNext();
        }
        SimpleAdapter adapter = new SimpleAdapter(this.getContext(), arrayList,
                R.layout.fragment_repordebts_dataview,
                new String[]{"supplier","description", "goods","summ"},
                new int[]{R.id.fragment_reportdebts_dataview_suppliername,R.id.fragment_reportdebts_dataview_description,R.id.fragment_reportdebts_dataview_goodsarticul,R.id.fragment_reportdebts_dataview_goodscount});
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                Toast.makeText(view.getContext(), view.getId()+" onEditClick", Toast.LENGTH_LONG).show();
                // TODO reportgoods:Обработка нажатия списка отчета по должникам
                AlertDialog.Builder ad  = new AlertDialog.Builder(view.getContext());
                ad.setTitle("варианты");  // заголовок
                ad.setMessage("выберите действие");
                ad.setPositiveButton("погасить долг", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int arg1) {
                        final String name = ((TextView)view.findViewById(R.id.fragment_reportdebts_dataview_suppliername)).getText().toString();
                        final String goodss = ((TextView)view.findViewById(R.id.fragment_reportdebts_dataview_goodsarticul)).getText().toString();
                        final String countt = ((TextView)view.findViewById(R.id.fragment_reportdebts_dataview_goodscount)).getText().toString();
                        DBhelper dbh  = new DBhelper(view.getContext());
                        SQLiteDatabase db = dbh.getWritableDatabase();
                        //Cursor cur = db.rawQuery("select * from select_debitors_report "+Statement+ " order by name",new String[]{});
                        Fragment fragment = new journaldebts();
                        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.content_frame, fragment);
                        //((journaldebts)fragment).Clickbtn();
                        //ft.show();
                        //DrawerLayout drawer = (DrawerLayout) view.findViewById(R.id.drawer_layout);
                        //drawer.closeDrawer(GravityCompat.START);

                        ft.commit();
                        AddBtnCallbackCklick cb = new AddBtnCallbackCklick() {
                            @Override
                            public String getname() {
                                return name;
                            }

                            @Override
                            public String getdescr() {
                                return goodss;
                            }

                            @Override
                            public String getcount() {
                                return countt;
                            }
                        };
                        journaldebts j = (journaldebts)fragment;
                        j.RegisterBtnCallback(cb);


                    }
                });
                ad.setCancelable(true);
                ad.show();

            }
        });
        dbh.close();

    }



}
