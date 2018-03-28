package com.catchersoft.bucketmgr.activities;


import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.catchersoft.bucketmgr.MainActivity;
import com.catchersoft.bucketmgr.R;
import com.catchersoft.bucketmgr.tools.DBhelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static android.R.attr.bitmap;
import static android.content.ContentValues.TAG;

    /**
     * A simple {@link Fragment} subclass.
     */
    public class reportgoods extends Fragment {


        private String Statement="";

        public reportgoods() {

        }


        @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reportgoods, container, false);
    }
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Отчеты по поставщикам и товарам");

        ListView listView = (ListView)view.findViewById(R.id.fragment_reportgoods_dataview);
// используем адаптер данных
        ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
        HashMap<String, String> map;

        DBhelper dbh  = new DBhelper(this.getContext());
        SQLiteDatabase db = dbh.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from select_total_report "+Statement+ " order by supplier",new String[]{});
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            map = new HashMap<>();
            String supplier= cursor.getString(cursor.getColumnIndex("supplier"));
            //int _id = cursor.getInt(cursor.getColumnIndex("id"));
            String goods = cursor.getString(cursor.getColumnIndex("goods"));
            String count = cursor.getString(cursor.getColumnIndex("summ"));
            //String date = cursor.getString(cursor.getColumnIndex("date"));

            //map.put("id",String.valueOf(_id));
            map.put("supplier",supplier);
            map.put("goods",goods);
            map.put("summ",count);
            //map.put("date",date);
            arrayList.add(map);
            cursor.moveToNext();
        }
        SimpleAdapter adapter = new SimpleAdapter(this.getContext(), arrayList,
                R.layout.fragment_reportgoods_dataview,
                new String[]{"supplier", "goods","summ"},
                new int[]{R.id.fragment_reportgoods_dataview_suppliername,R.id.fragment_reportgoods_dataview_goodsarticul,R.id.fragment_reportgoods_dataview_goodscount})
        {
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                // Get the Item from ListView
                View view = super.getView(position, convertView, parent);

                // Initialize a TextView for ListView each Item
                TextView tv = (TextView) view.findViewById(R.id.fragment_reportgoods_dataview_goodscount);
                if(Integer.parseInt(tv.getText().toString())<0){
                    tv.setTextColor(Color.parseColor("#00FF00"));

                }
                if(Integer.parseInt(tv.getText().toString())>0){
                    tv.setTextColor(Color.parseColor("#FF0000"));

                }

                // Set the text color of TextView (ListView Item)


                // Generate ListView Item using TextView
                return view;}
        }
                ;
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
                     public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                        final String suppname =  ((TextView)view.findViewById(R.id.fragment_reportgoods_dataview_suppliername)).getText().toString();
                        final String itemname =  ((TextView)view.findViewById(R.id.fragment_reportgoods_dataview_goodsarticul)).getText().toString();
                        final String itemcount =  ((TextView)view.findViewById(R.id.fragment_reportgoods_dataview_goodscount)).getText().toString();


                        AlertDialog.Builder ad  = new AlertDialog.Builder(view.getContext());
                        ad.setTitle("варианты");  // заголовок
                        ad.setMessage("выберите действие");
                        ad.setPositiveButton("просмотр", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int arg1) {

                                Fragment fragment = new journalgoods();
                                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                                ft.replace(R.id.content_frame, fragment);

                                //ft.show();
                                //DrawerLayout drawer = (DrawerLayout) view.findViewById(R.id.drawer_layout);
                                //drawer.closeDrawer(GravityCompat.START);
                                ((journalgoods)fragment).Statement = " where goods='" + itemname + "' "+" and " + " supplier='" + suppname + "' ";
                                ((journalgoods)fragment).today = false;
                                ft.commit();
                                }
                            });
                        ad.setNegativeButton("viber sms", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int arg1) {
                                boolean found = false;
                                Dialog dd = (Dialog)dialog;
                                Intent share = new Intent(android.content.Intent.ACTION_SEND);
                                share.setType("text/plain");
                                List<ResolveInfo> resInfo = dd.getContext().getPackageManager().queryIntentActivities(share, 0);
                                if (!resInfo.isEmpty()) {
                                    for (ResolveInfo info : resInfo) {
                                        if (info.activityInfo.packageName.toLowerCase(
                                                Locale.getDefault()).contains("com.viber.voip")
                                                || info.activityInfo.name.toLowerCase(
                                                Locale.getDefault()).contains("com.viber.voip")) {
                                            String mydate = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());

                                            share.putExtra(Intent.EXTRA_TEXT, "\t долг на: \n  "+mydate+" \n\n \t поставщик:\n'" + suppname + "' \n\n наименование товара:\n '"+itemname+"' \n\n \t количество: "+itemcount);
                                            share.setPackage(info.activityInfo.packageName);
                                            found = true;
                                            dd.getContext().startActivity(Intent.createChooser(share, "Select"));
                                            break;
                                        }
                                    }
                                }
                            }



                        });
                        ad.setNeutralButton("добавить", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int arg1) {
                                Fragment fragment = new journalgoods();
                                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                                ft.replace(R.id.content_frame, fragment);
                                String item = "";
                                DBhelper dbh  = new DBhelper(view.getContext());
                                SQLiteDatabase db = dbh.getReadableDatabase();
                                Cursor cursor = db.rawQuery("select artname from goods where name='"+itemname+"'",new String[]{});
                                cursor.moveToFirst();
                                item = cursor.getString(cursor.getColumnIndex("artname"));
                                db.close();
                                ((journalgoods)fragment).SelectedGoodsSpinner = item;
                                ((journalgoods)fragment).SelectedSupplierSpinner = suppname;
                                ft.commit();
                                ((journalgoods)fragment).Clicked = true;


                                //ft.show();
                                //DrawerLayout drawer = (DrawerLayout) view.findViewById(R.id.drawer_layout);
                                //drawer.closeDrawer(GravityCompat.START);
                                //Spinner sp1 = (Spinner) ((journalgoods)fragment).dialog.findViewById(R.id.dialog_journalgoods_addbtn_supplierspinner);
                                //sp1.setSelection(5);



                            }
                        });
                                ad.setCancelable(true);
                                ad.show();



                        Toast.makeText(view.getContext(), " onEditClick", Toast.LENGTH_LONG).show();
                        // TODO reportgoods:Обработка нажатия списка отчета по товарам товаров

                    }
                });
            dbh.close();

    }

}
