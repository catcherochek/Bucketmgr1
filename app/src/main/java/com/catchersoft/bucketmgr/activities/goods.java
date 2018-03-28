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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.catchersoft.bucketmgr.R;
import com.catchersoft.bucketmgr.tools.DBConstants;
import com.catchersoft.bucketmgr.tools.DBhelper;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link goods.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link goods#newInstance} factory method to
 * create an instance of this fragment.
 */
public class goods extends Fragment implements View.OnClickListener {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        return inflater.inflate(R.layout.fragment_goods, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Товары");


        Button button = (Button)view.findViewById(R.id.goods_btn1);

        button.setOnClickListener(this);
        DBhelper dbh = new DBhelper(this.getContext());
        SQLiteDatabase db = dbh.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from goods",new String[]{});
        cursor.moveToFirst();
        int _id = 0;
        final goods tis = this;
        while (!cursor.isAfterLast()) {
            String name= cursor.getString(cursor.getColumnIndex("name"));
            _id = cursor.getInt(cursor.getColumnIndex("id"));
            String artname = cursor.getString(cursor.getColumnIndex("artname"));

            LinearLayout tr = new LinearLayout(this.getContext());
            tr.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT, 1f));
            tr.setId(DBConstants.LAYOUT_ID_GOODS+_id);
            tr.setOrientation(LinearLayout.HORIZONTAL);

            tr.setOnClickListener(new TextView.OnClickListener() {

                @Override
                public void onClick(View v) {
                    tis.onClick(v);
                }
            });
            LinearLayout ln = (LinearLayout) view.findViewById(R.id.goods_dataview);
            ln.addView(tr);

            TextView tv = new TextView(this.getContext());
            tv.setText(name);
            //tv.setId(DBConstants.LAYOUT_ID_GOODS+_id);
            tv.setId(DBConstants.LAYOUT_ID_GOODS+DBConstants.LAYOUT_ID_GOODS_OFFSET_FOR_NAME+_id);
            tv.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT, 1f));
            tv.setHeight(150);
            tv.setTextSize(20);
            tv.setPadding(40, 15, 0, 0);
            tv.setTextColor(getResources().getColor(R.color.white));
            tr.addView(tv);

            TextView tv2 = new TextView(this.getContext());
            tv2.setText(artname);
            //tv.setId(DBConstants.LAYOUT_ID_GOODS+_id);
            tv2.setId(DBConstants.LAYOUT_ID_GOODS+DBConstants.LAYOUT_ID_GOODS_OFFSET_FOR_ARTICUL+_id);
            tv2.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT, 1f));
            tv2.setHeight(150);
            tv2.setTextSize(20);
            tv2.setPadding(40, 15, 0, 0);
            tv2.setTextColor(getResources().getColor(R.color.white));
            tr.addView(tv2);

            cursor.moveToNext();
        }
        DBConstants.LAYOUT_ID_GOODS_LAST = DBConstants.LAYOUT_ID_GOODS+_id;
        db.close();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if ((DBConstants.LAYOUT_ID_GOODS_LAST > id) & (id > DBConstants.LAYOUT_ID_GOODS)){
            final int _id = id- DBConstants.LAYOUT_ID_GOODS;

            String nme = ((TextView)v.findViewById(id+DBConstants.LAYOUT_ID_GOODS_OFFSET_FOR_NAME)).getText().toString();
            String art = ((TextView)v.findViewById(id+DBConstants.LAYOUT_ID_GOODS_OFFSET_FOR_ARTICUL)).getText().toString();

            final Dialog dialog = new Dialog(this.getContext());
            dialog.setTitle("Обновить товар");
            // Передайте ссылку на разметку
            dialog.setContentView(R.layout.dialog_goods_add);
            Button btnCancel = (Button) dialog.findViewById(R.id.dialog_goods_insert_btn_CANCEL);
            btnCancel.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ///Toast.makeText(v.getContext(), v.getId()+" onCancelClick", Toast.LENGTH_LONG).show();
                    dialog.hide();
                }
            });
            ((TextView) dialog.findViewById(R.id.dialog_goods_insert_name)).setText(nme);
            ((TextView) dialog.findViewById(R.id.dialog_goods_insert_articul)).setText(art);
            Button btnOK = (Button) dialog.findViewById(R.id.dialog_goods_insert_btn_OK);
            btnOK.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String name = ((TextView) dialog.findViewById(R.id.dialog_goods_insert_name)).getText().toString();
                    String artname = ((TextView) dialog.findViewById(R.id.dialog_goods_insert_articul)).getText().toString();
                    if (!name.equals("")) {
                        DBhelper dbh = new DBhelper(v.getContext());
                        SQLiteDatabase db = dbh.getWritableDatabase();
                        String query = "UPDATE goods \n"+
                                "SET name='"+name+"',\n"+
                                "artname='"+artname+"'\n"+
                                "WHERE id="+_id;
                        db.execSQL(query);
                        Toast.makeText(v.getContext(), "товар обновлен успешно", Toast.LENGTH_LONG).show();
                        dialog.hide();
                    } else {
                        Toast.makeText(v.getContext(), " Не введено имя товара", Toast.LENGTH_LONG).show();
                    }


                }
            });
            dialog.show();


        }
        if (id == R.id.goods_btn1) {
            final Dialog dialog = new Dialog(this.getContext());
            dialog.setTitle("Добавить товар");
            // Передайте ссылку на разметку
            dialog.setContentView(R.layout.dialog_goods_add);
            Button btnCancel = (Button) dialog.findViewById(R.id.dialog_goods_insert_btn_CANCEL);
            btnCancel.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ///Toast.makeText(v.getContext(), v.getId()+" onCancelClick", Toast.LENGTH_LONG).show();
                    dialog.hide();
                }
            });
            Button btnOK = (Button) dialog.findViewById(R.id.dialog_goods_insert_btn_OK);
            btnOK.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String name = ((TextView) dialog.findViewById(R.id.dialog_goods_insert_name)).getText().toString();
                    String artname = ((TextView) dialog.findViewById(R.id.dialog_goods_insert_articul)).getText().toString();
                    if (!name.equals("")) {
                        DBhelper dbh = new DBhelper(v.getContext());
                        SQLiteDatabase db = dbh.getWritableDatabase();
                        String query = "INSERT INTO goods (name,artname) VALUES ('" + name + "','" + artname + "')";
                        db.execSQL(query);
                        Toast.makeText(v.getContext(), "товар добавлен успешно", Toast.LENGTH_LONG).show();
                        dialog.hide();
                    } else {
                        Toast.makeText(v.getContext(), " Не введено имя товара", Toast.LENGTH_LONG).show();
                    }


                }
            });
            dialog.show();
        }
    }
}
