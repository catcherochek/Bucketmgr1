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
import com.catchersoft.bucketmgr.tools.DB.DBConstants;
import com.catchersoft.bucketmgr.tools.DB.DBHandler;
import com.catchersoft.bucketmgr.tools.DB.DBhelper;

/**
 * Created by Клим on 10.09.2017.
 */

public class debitors extends Fragment implements View.OnClickListener {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        return inflater.inflate(R.layout.fragment_debitors, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Контрагенты");
        Button button = (Button)view.findViewById(R.id.debitors_btn1);

        button.setOnClickListener(this);
        DBHandler db = DBHandler.getInstance();


        Cursor cursor = db.InitRead(DBConstants.QUERY_GET_SELECT_DATA_FROM_DEBITORS());
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String name= cursor.getString(cursor.getColumnIndex("name"));
            int _id = cursor.getInt(cursor.getColumnIndex("id"));
            String artname = cursor.getString(cursor.getColumnIndex("description"));
            int OFFSET_FORTEXTVIEW = 10000;
            LinearLayout tr = new LinearLayout(this.getContext());
            tr.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT, 1f));
            tr.setId(DBConstants.LAYOUT_ID_DEBITORS+_id);
            tr.setOrientation(LinearLayout.HORIZONTAL);
            tr.setOnClickListener(new TextView.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), v.getId()+" onEditClick", Toast.LENGTH_LONG).show();
                    // TODO debitors:Обработка выбора ячейки с именем поставщика
                }
            });
            LinearLayout ln = (LinearLayout) view.findViewById(R.id.debitors_dataview);
            ln.addView(tr);

            TextView tv = new TextView(this.getContext());
            tv.setText(name);
            //tv.setId(DBConstants.LAYOUT_ID_GOODS+_id);
            tv.setId(DBConstants.LAYOUT_ID_DEBITORS+OFFSET_FORTEXTVIEW+_id);
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
            tv2.setId(DBConstants.LAYOUT_ID_DEBITORS+OFFSET_FORTEXTVIEW*2+_id);
            tv2.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT, 1f));
            tv2.setHeight(150);
            tv2.setTextSize(20);
            tv2.setPadding(40, 15, 0, 0);
            tv2.setTextColor(getResources().getColor(R.color.white));
            tr.addView(tv2);

            cursor.moveToNext();
        }
        db.close();
    }

    @Override
    public void onClick(View v) {
        final Dialog dialog = new Dialog(this.getContext());
        dialog.setTitle("Добавить контрагента");
        // Передайте ссылку на разметку
        dialog.setContentView(R.layout.dialog_debitors_add);
        Button btnCancel = (Button)dialog.findViewById(R.id.dialog_debitors_insert_btn_CANCEL);
        btnCancel.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                ///Toast.makeText(v.getContext(), v.getId()+" onCancelClick", Toast.LENGTH_LONG).show();
                dialog.hide();
            }
        });
        Button btnOK = (Button)dialog.findViewById(R.id.dialog_debitors_insert_btn_OK);
        btnOK.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = ((TextView)dialog.findViewById(R.id.dialog_debitors_insert_name)).getText().toString();
                String artname = ((TextView)dialog.findViewById(R.id.dialog_debitors_insert_description)).getText().toString();
                if(!name.equals("")){
                    DBHandler.getInstance().InitWrite(DBConstants.QUERY_INSERT_DEBITORS_DATA(name, artname));
                    Toast.makeText(v.getContext(),"должник добавлен успешно", Toast.LENGTH_LONG).show();
                    dialog.hide();
                    DBHandler.getInstance().close();
                }
                else {
                    Toast.makeText(v.getContext()," Не введено имя должника", Toast.LENGTH_LONG).show();
                }


            }
        });
        dialog.show();
    }
}
