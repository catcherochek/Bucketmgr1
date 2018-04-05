package com.catchersoft.bucketmgr.activities;


import android.app.Dialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.catchersoft.bucketmgr.R;
import com.catchersoft.bucketmgr.tools.DB.DBhelper;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
interface AddBtnCallbackCklick{
    //public void HandleClickCB(String name,String desc, String count);
    String getname();
    String getdescr();
    String getcount();

}

public class journaldebts extends Fragment implements View.OnClickListener {


    boolean Waitquenue = false;
    public String Statement = "";
    public View staticview;
    private boolean today = true;
    AddBtnCallbackCklick CB=null;
    Method m;
    public journaldebts()  {

    }
    public void RegisterBtnCallback(AddBtnCallbackCklick cb){
        CB = cb;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_journaldebts, container, false);

        return v;
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Журнал должников");

        Button button = (Button) view.findViewById(R.id.fragment_journaldebts_btnrefresh);
        button.setOnClickListener(this);
        Button button1 = (Button) view.findViewById(R.id.fragment_journaldebts_btnAdd);
        button1.setOnClickListener(this);

        FillSpinners((Spinner) view.findViewById(R.id.fragment_journaldebts_spinnergoods), "select name as text from goods", "выберите товар");
        FillSpinners((Spinner) view.findViewById(R.id.fragment_journaldebts_spinnersupplier), "select name as text from debitors", "выберите должника");
        staticview = view;
        updateList();
        if (CB!=null) {
            HandleClickCB(CB.getname(),CB.getdescr(),CB.getcount());
        }





    }


    

    
    public void HandleClickCB(String name,String desc, String count){
        if ((View)this.getView() != null){
        this.onClick((View)this.getView().findViewById(R.id.fragment_journaldebts_btnAdd));}
        else{
            this.Waitquenue = true;
        }
    }
    public void FillSpinners(Spinner spinner, String query, String title) {
        // адаптер
        DBhelper dbh = DBhelper.getInstance(this.getContext());
        SQLiteDatabase db = dbh.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, new String[]{});
        cursor.moveToFirst();
        ArrayList<String> data = new ArrayList<String>();

        while (!cursor.isAfterLast()) {
            String name = cursor.getString(cursor.getColumnIndex("text"));
            data.add(name);
            cursor.moveToNext();
        }
        String[] data1 = data.toArray(new String[data.size()]);
        ;

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_spinner_item, data1);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        spinner.setAdapter(adapter);
        // заголовок
        spinner.setPrompt(title);
        // выделяем элемент
        spinner.setSelection(0);
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

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fragment_journaldebts_btnAdd) {
            final Dialog dialog = new Dialog(this.getContext());

            dialog.setTitle("Добавление поступления");
            dialog.setContentView(R.layout.dialog_journaldebts_addbtn);
            String q1 = "";
            String q2 = "";
            if (CB!=null){
                q1 = " WHERE debitors.name='"+CB.getname()+"'";
                q2 = " WHERE goods.name='"+CB.getdescr()+"'";
                ((EditText)dialog.findViewById(R.id.dialog_journaldebts_addbtn_Count)).setText(((Integer)(Integer.parseInt(CB.getcount())*-1)).toString());
                CB = null;
            }
            FillSpinners((Spinner)dialog.findViewById(R.id.dialog_journaldebts_addbtn_supplierspinner),"select name as text from debitors"+q1,"выбор должника");
            FillSpinners((Spinner)dialog.findViewById(R.id.dialog_journaldebts_addbtn_articlespinner),"select artname as text from goods"+q2,"выбор товара");


            Button btnCancel = (Button)dialog.findViewById(R.id.dialog_journaldebts_addbtnbtn_CANCEL);
            btnCancel.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ///Toast.makeText(v.getContext(), v.getId()+" onCancelClick", Toast.LENGTH_LONG).show();
                    dialog.hide();
                }
            });


            Button btnOK = (Button)dialog.findViewById(R.id.dialog_journaldebts_addbtnbtn_OK);

            btnOK.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Spinner artSP = (Spinner)dialog.findViewById(R.id.dialog_journaldebts_addbtn_articlespinner);
                    String art = artSP.getSelectedItem().toString();
                    Spinner suppSP = (Spinner)dialog.findViewById(R.id.dialog_journaldebts_addbtn_supplierspinner);
                    String supp =  suppSP.getSelectedItem().toString();

                    String Count = ((EditText)dialog.findViewById(R.id.dialog_journaldebts_addbtn_Count)).getText().toString();

                    if(!art.equals("") | !supp.equals("")){

                        DBhelper dbh = DBhelper.getInstance(v.getContext());
                        SQLiteDatabase db = dbh.getWritableDatabase();
                        String f1="";
                        String f2 = "";

                        if (!Count.equals("0")){
                            String query =  "insert into \"debitors_transactions\" (goods_id,debitor_id,count)\n" +
                                    "VALUES((select id from goods where artname = '"+art+"'),(select id from debitors where name  = '"+supp+"'),"+Count+")";
                            db.execSQL(query);
                        }

                        //String query = "INSERT INTO goods (name,artname) VALUES ('"+name+"','"+artname+"')";
                        //db.execSQL(query);
                        Toast.makeText(v.getContext(),"данные внесены успешно", Toast.LENGTH_LONG).show();
                        dbh.close();
                        //journalgoods.imgpath = "";
                        dialog.hide();

                    }
                    else {
                        Toast.makeText(v.getContext()," Не выбран  артикул или должник", Toast.LENGTH_LONG).show();
                    }


                }
            });
            dialog.show();

        }

            updateList();
    }



    public void updateList() {

        ListView listView = (ListView) staticview.findViewById(R.id.fragment_journaldebts_dataview);


        ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
        HashMap<String, String> map;

        DBhelper dbh  = DBhelper.getInstance(this.getContext());
        SQLiteDatabase db = dbh.getWritableDatabase();


        if (today){
            if (Statement.equals("")){
                Statement = "  where date > date('now')";
            }
            else{
                Statement = Statement+ " and date > date('now')";
            }
            today = false;
        }

        Cursor cursor = db.rawQuery("select * from select_debitors_journal "+Statement+ " order by date",new String[]{});
        cursor.moveToFirst();
        map = new HashMap<>();
        while (!cursor.isAfterLast()) {
            map = new HashMap<>();
            int _id = cursor.getInt(cursor.getColumnIndex("id"));
            String supplier= cursor.getString(cursor.getColumnIndex("name"));
            String supplierDesc = cursor.getString(cursor.getColumnIndex("descr"));
            String goods = cursor.getString(cursor.getColumnIndex("goods"));
            String goodsArticle = cursor.getString(cursor.getColumnIndex("art"));
            int count = cursor.getInt(cursor.getColumnIndex("count"));
            String date = cursor.getString(cursor.getColumnIndex("date"));
            String action = "взял(а)";
            if (count<0){
                count = -1*count;
                action = "отдал(а)";
            }

            map.put("id",String.valueOf(_id));
            map.put("supplier",supplier);
            map.put("supplierdesc",supplierDesc);
            map.put("goods",goods);
            map.put("goodsart",goodsArticle);

            map.put("count",String.valueOf(count));
            map.put("date",date);
            map.put("action",action);
            arrayList.add(map);
            cursor.moveToNext();

        }
        if (!map.isEmpty()) {
            SimpleAdapter adapter = new SimpleAdapter(this.getContext(), arrayList,
                    R.layout.journaldebts_dataview,
                    new String[]{"id", "supplier", "supplierdesc", "goods", "goodsart", "count", "date", "action"},
                    new int[]{R.id.journaldebts_dataview_id,
                            R.id.journaldebts_dataview_suppliername,
                            R.id.journaldebts_dataview_description,
                            R.id.journaldebts_dataview_goodsname,
                            R.id.journaldebts_dataview_goodsarticul,
                            R.id.journaldebts_dataview_goodscount,
                            R.id.journaldebts_dataview_date,
                            R.id.journaldebts_dataview_action});
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                    Toast.makeText(view.getContext(), " journaldebts:onItemClick", Toast.LENGTH_LONG).show();


                    // TODO journaldebts:Обработка нажатия списка журнала должников

                }
            });
        }
        dbh.close();
    }
}
