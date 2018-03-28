package com.catchersoft.bucketmgr.activities;


import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.catchersoft.bucketmgr.R;
import com.catchersoft.bucketmgr.tools.DBhelper;

import java.io.File;
import java.text.ParseException;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class journalgoods extends Fragment implements OnClickListener {
    public String SelectedGoodsSpinner="";
    public String SelectedSupplierSpinner="";
    Boolean Clicked = false;
    public String Statement = "";
    public View staticview;
    public static String imgpath = "";
    public boolean today = true;
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode!=-1)
        {
            journalgoods.imgpath = "";
        }

    }
    public journalgoods() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_journalgoods, container, false);


    }
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Журнал движения товаров");
        Button button = (Button)view.findViewById(R.id.fragment_journalgoods_btnrefresh);
        button.setOnClickListener(this);
        Button btnAdd = (Button)view.findViewById(R.id.fragment_journalgoods_btnAdd);
        btnAdd.setOnClickListener(this);

        staticview = view;
        // получаем экземпляр элемента ListView
        updateList();

        FillSpinners((Spinner)view.findViewById(R.id.fragment_journalgoods_spinnergoods),"select name as text from goods","выберите товар");
        FillSpinners((Spinner)view.findViewById(R.id.fragment_journalgoods_spinnersupplier),"select name as text from suppliers","выберите товар");
        String dte = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        ((EditText)view.findViewById(R.id.fragment_journalgoods_EditDatepicker)).setText(dte);
        if (Clicked){btnAdd.performClick();}

    }
    public void FillSpinners(Spinner spinner,String query,String title,String text){
        FillSpinners(spinner,query,title);
        ArrayAdapter<String> array_spinner=(ArrayAdapter<String>)spinner.getAdapter();
        int id  = array_spinner.getPosition(text);
        if (id>=0){
        spinner.setSelection(id);}

    }
    public void updateList(){
        ListView listView = (ListView)staticview.findViewById(R.id.fragment_journalgoods_dataview);





// используем адаптер данных
        ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
        HashMap<String, String> map;

        DBhelper dbh  = new DBhelper(this.getContext());
        SQLiteDatabase db = dbh.getWritableDatabase();
        //Date today = new Date();
        //today.setHours(0);
        //today.setMinutes(0);
        //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:MM");
           // String ddate = sdf.format(today);
            //SimpleDateFormat sdf  = new  SimpleDateFormat("yyyy-MM-dd HH:MM");
            //Date strdate = sdf.parse(ddate);
            //Toast.makeText(this.getContext(), date, Toast.LENGTH_LONG).show();

        if (today){
            if (Statement.equals("")){
                Statement = "  where date > date('now')";
            }
            else{
                Statement = Statement+ " and date > date('now')";
            }
            today = false;
        }
        Cursor cursor = db.rawQuery("select * from select_transactions "+Statement+ " order by date",new String[]{});
        cursor.moveToFirst();
        int i = 0;
        final HashMap<String,String> mapBackground = new HashMap<>();
        while (!cursor.isAfterLast()) {
            map = new HashMap<>();
            String supplier= cursor.getString(cursor.getColumnIndex("supplier"));
            int _id = cursor.getInt(cursor.getColumnIndex("id"));
            String goods = cursor.getString(cursor.getColumnIndex("goods"));
            String count = cursor.getString(cursor.getColumnIndex("count"));
            String date = cursor.getString(cursor.getColumnIndex("date"));
            String photo1 = cursor.getString(cursor.getColumnIndex("photo"));
            if (photo1 != null){
                mapBackground.put(String.valueOf(_id),"#F39F18");
            }else{mapBackground.put(String.valueOf(_id),"none");}


            //Date date2 = sdf.parse(date);
            //if(Integer.parseInt(count)>0){
            //    map1.put(_id,"#F5DC49");
            //}else if (Integer.parseInt(count)==0)
            //{map1.put(_id,"");}
            //else if (Integer.parseInt(count)<0)
           // {map1.put(_id,"#8F9779");}



            map.put("id",String.valueOf(_id));
            map.put("supplier",supplier);
            map.put("goods",goods);
            map.put("count",count);
            map.put("date",date);
            arrayList.add(map);
            cursor.moveToNext();



        }
        SimpleAdapter adapter = new SimpleAdapter(this.getContext(), arrayList,
                R.layout.fragment_journalgoods_dataview,
                new String[]{"id", "supplier", "goods","count","date"},
                new int[]{R.id.fragment_journalgoods_dataview_id, R.id.fragment_journalgoods_dataview_suppliername,
                        R.id.fragment_journalgoods_dataview_goodsarticul,R.id.fragment_journalgoods_dataview_goodscount,R.id.fragment_journalgoods_dataview_date})
        {
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                // Get the Item from ListView
                View view = super.getView(position, convertView, parent);

                // Initialize a TextView for ListView each Item
                TextView tv = (TextView) view.findViewById(R.id.fragment_journalgoods_dataview_goodscount);
                String id = ((TextView)view.findViewById(R.id.fragment_journalgoods_dataview_id)).getText().toString();
                String colorr = "";
                if (!mapBackground.get(id).equals("none")){
                    colorr = mapBackground.get(id);
                }else{colorr = "#000000";}
                view.setBackgroundColor(Color.parseColor(colorr));
                if(Integer.parseInt(tv.getText().toString())<0){
                    tv.setTextColor(Color.parseColor("#00FF00"));

                }



                if(Integer.parseInt(tv.getText().toString())>0){
                    tv.setTextColor(Color.parseColor("#FF0000"));

                }

                // Set the text color of TextView (ListView Item)


                // Generate ListView Item using TextView
                return view;}
        };
        listView.setAdapter(adapter);


       // listView.getItemAtPosition()
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Dialog dialog = new Dialog(view.getContext());
                dialog.setTitle("фотка");
                DBhelper dbh = new DBhelper(view.getContext());
                SQLiteDatabase db  = dbh.getReadableDatabase();
                String ID =  ((TextView)((LinearLayout)view).findViewById(R.id.fragment_journalgoods_dataview_id)).getText().toString();
                String query  = "select photo from \"transaction\" where id = "+ID;
                Cursor cur = db.rawQuery(query,new String[]{});
                cur.moveToFirst();
                String path = cur.getString(cur.getColumnIndex("photo"));
                dialog.setContentView(R.layout.dialog_journalgoods_dataview_imgview);
                if (path != null){
                    WebView img = (WebView) dialog.findViewById(R.id.dialog_journalgoods_dataview_imgview_img);
                    img.setBackgroundColor(android.R.color.black);
                    img.getSettings().setSupportZoom(true);
                    img.getSettings().setBuiltInZoomControls(true);
                    img.setScrollbarFadingEnabled(true);
                    img.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

                    img.loadUrl("file:///"+path);
                    dialog.show();
                    ///img.setImageURI(Uri.parse(path));
                   }

                Toast.makeText(view.getContext(), " journalgoods:onItemClick", Toast.LENGTH_LONG).show();

                dbh.close();
                // TODO journalgoods:Обработка нажатия списка журнала движения товаров

            }
        });
        dbh.close();;
    }
    public void FillSpinners(Spinner spinner,String query,String title) {
        // адаптер
        DBhelper dbh = new DBhelper(this.getContext());
        SQLiteDatabase db = dbh.getReadableDatabase();
        Cursor cursor = db.rawQuery(query,new String[]{});
        cursor.moveToFirst();
        ArrayList<String> data = new ArrayList<String>();

        while (!cursor.isAfterLast()) {
            String name = cursor.getString(cursor.getColumnIndex("text"));
            data.add(name);
            cursor.moveToNext();
        }
        String[] data1 = data.toArray(new String[data.size()]);;

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_spinner_item, data1);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        spinner.setAdapter(adapter);
        // заголовок
        spinner.setPrompt(title);
        // выделяем элемент
        {
        spinner.setSelection(2);}



        // устанавливаем обработчик нажатия
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // показываем позиция нажатого элемента


                Toast.makeText(view.getContext(), "Position = " + position, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        dbh.close();
    }
    public void PerformClick(){

    }
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fragment_journalgoods_btnrefresh) {

            Spinner sp1 = (Spinner) this.getActivity().findViewById(R.id.fragment_journalgoods_spinnergoods);
            String goods = sp1.getSelectedItem().toString();
            Spinner sp2 = (Spinner) this.getActivity().findViewById(R.id.fragment_journalgoods_spinnersupplier);
            String supplier = sp2.getSelectedItem().toString();
            String statement = "";
            String wherest = " where ";
            if (!goods.equals("")) {
                statement = wherest + " goods='" + goods + "' ";
            }
            if (!supplier.equals("")) {
                if (statement.equals("")) {
                    statement = wherest + " supplier='" + supplier + "' ";
                } else {
                    statement = statement + " and " + " supplier='" + supplier + "' ";
                }

            }
            Statement = statement;
            updateList();
        }
        if (v.getId() == R.id.fragment_journalgoods_btnrefresh){
            //String datestart = ((EditText)v.findViewById(R.id.fragment_journalgoods_EditDatepicker)).getText().toString();

        }



        if (v.getId() == R.id.fragment_journalgoods_btnAdd){

            final Dialog dialog = new Dialog(this.getContext());

            dialog.setTitle("Добавление поступления");
            dialog.setContentView(R.layout.dialog_journalgoods_addbtn);

            FillSpinners((Spinner)dialog.findViewById(R.id.dialog_journalgoods_addbtn_supplierspinner),"select name as text from suppliers","выбор конртагента",SelectedSupplierSpinner);
            FillSpinners((Spinner)dialog.findViewById(R.id.dialog_journalgoods_addbtn_articlespinner),"select artname as text from goods","выбор товара",SelectedGoodsSpinner);


            Button btnCancel = (Button)dialog.findViewById(R.id.dialog_journalgoods_addbtnbtn_CANCEL);
            btnCancel.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ///Toast.makeText(v.getContext(), v.getId()+" onCancelClick", Toast.LENGTH_LONG).show();
                    dialog.hide();
                }
            });
            ImageButton btnPhotol = (ImageButton) dialog.findViewById(R.id.dialog_journalgoods_addbtnbtn_PHOTO);
            btnPhotol.setOnClickListener(new Button.OnClickListener() {


                @Override
                public void onClick(View v) {
                    ///Toast.makeText(v.getContext(), v.getId()+" onCancelClick", Toast.LENGTH_LONG).show();
                   File dir = v.getContext().getExternalCacheDir();
                    if (!dir.exists()){
                        dir.mkdirs();}
                    String fotoname = dir.getPath() + "/" + "photo_" + System.currentTimeMillis() + ".jpg";
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File file = new File(fotoname);

                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                    startActivityForResult(intent, 1);

                    journalgoods.imgpath = fotoname;

                }
            });

            Button btnOK = (Button)dialog.findViewById(R.id.dialog_journalgoods_addbtnbtn_OK);

            btnOK.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Spinner artSP = (Spinner)dialog.findViewById(R.id.dialog_journalgoods_addbtn_articlespinner);
                    String art = artSP.getSelectedItem().toString();
                    Spinner suppSP = (Spinner)dialog.findViewById(R.id.dialog_journalgoods_addbtn_supplierspinner);
                    String supp =  suppSP.getSelectedItem().toString();

                    String In = ((TextView)dialog.findViewById(R.id.dialog_journalgoods_addbtn_textIn)).getText().toString();
                    String Out = ((TextView)dialog.findViewById(R.id.dialog_journalgoods_addbtn_textOut)).getText().toString();
                    if(!art.equals("") | !supp.equals("")){

                        DBhelper dbh = new DBhelper(v.getContext());
                        SQLiteDatabase db = dbh.getWritableDatabase();
                        String f1="";
                        String f2 = "";
                        if (!journalgoods.imgpath.equals("")){
                            f1 = ",photo";
                            f2 = ",'"+journalgoods.imgpath+"'";
                        }
                        if (!In.equals("0")){
                            String query =  "insert into \"transaction\" (goods_id,supplier_id,count"+f1+")\n" +
                                    "VALUES((select id from goods where artname = '"+art+"'),(select id from suppliers where name  = '"+supp+"'),"+In+""+f2+")";
                            db.execSQL(query);
                        }
                        if (!Out.equals("0")){
                            String query =  "insert into \"transaction\" (goods_id,supplier_id,count"+f1+")\n" +
                                    "VALUES((select id from goods where artname = '"+art+"'),(select id from suppliers where name  = '"+supp+"'),"+Out+"*-1"+f2+")";
                            db.execSQL(query);
                        }
                        //String query = "INSERT INTO goods (name,artname) VALUES ('"+name+"','"+artname+"')";
                        //db.execSQL(query);
                        Toast.makeText(v.getContext(),"данные внесены успешно", Toast.LENGTH_LONG).show();
                        dbh.close();
                        journalgoods.imgpath = "";
                        dialog.hide();

                    }
                    else {
                        Toast.makeText(v.getContext()," Не выбран  артикул или контрагент", Toast.LENGTH_LONG).show();
                    }


                }
            });
            dialog.show();

        }
    }
}
