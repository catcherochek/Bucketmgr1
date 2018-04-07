package com.catchersoft.bucketmgr.activities;

/**
 * Created by Клим on 05.09.2017.
 */
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
import com.catchersoft.bucketmgr.tools.DB.DBhelper;
import com.catchersoft.bucketmgr.tools.DB.DBHandler;


public class costumerclass extends Fragment implements View.OnClickListener {




        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            //returning our layout file
            //change R.layout.yourlayoutfilename for each of your fragments

                super.onCreate(savedInstanceState);





            return inflater.inflate(R.layout.fragment_costumer, container, false);



        }


        @Override
        public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            //you can set the title for your toolbar here for different fragments different titles
            getActivity().setTitle("Поставщики");
            Button button = (Button)view.findViewById(R.id.costumer_btn1);

            button.setOnClickListener(this);


            Cursor cursor =  DBHandler.getInstance(this.getContext()).InitRead(DBConstants.QUERY_GET_SELECT_DATA_FROM_SUPPLIES());
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                String name= cursor.getString(cursor.getColumnIndex("name"));
                int _id = cursor.getInt(cursor.getColumnIndex("id"));
                TextView tv = new TextView(this.getContext());
                tv.setText(name);
                tv.setId(DBConstants.LAYOUT_ID_COSTUMERS+_id);
                tv.setOnClickListener(new TextView.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Toast.makeText(v.getContext(), v.getId()+" onEditClick", Toast.LENGTH_LONG).show();
                        // TODO costumerclass:Обработка выбора ячейки с именем поставщика
                    }
                });
                tv.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                        TableLayout.LayoutParams.WRAP_CONTENT, 1f));
                tv.setHeight(150);
                tv.setTextSize(20);
                tv.setPadding(40, 15, 0, 0);
                tv.setTextColor(getResources().getColor(R.color.white));

                LinearLayout ln = (LinearLayout) view.findViewById(R.id.costumer_dataview);
                ln.addView(tv);
                cursor.moveToNext();
            }
            DBHandler.getInstance().close();


        }

    @Override
    public void onClick(View v) {
        final Dialog dialog = new Dialog(this.getContext());
        dialog.setTitle("Добавить поставщика");
        // Передайте ссылку на разметку
        dialog.setContentView(R.layout.dialog_costumer_add);
        Button btnCancel = (Button)dialog.findViewById(R.id.dialog_insert_costumer_btn_CANCEL);
        btnCancel.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                ///Toast.makeText(v.getContext(), v.getId()+" onCancelClick", Toast.LENGTH_LONG).show();
                dialog.hide();
            }
        });
        Button btnOK = (Button)dialog.findViewById(R.id.dialog_insert_costumer_btn_OK);
        btnOK.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = ((TextView)dialog.findViewById(R.id.dialog_costumer_insert_name)).getText().toString();
                if(!text.equals("")){
                    DBhelper dbh = DBhelper.getInstance(v.getContext());
                    SQLiteDatabase db = dbh.getWritableDatabase();
                    String query = "INSERT INTO suppliers (name) VALUES ('"+text+"')";
                    db.execSQL(query);
                    Toast.makeText(v.getContext()," Поставщик добавлен успешно", Toast.LENGTH_LONG).show();
                    dialog.hide();
                }
                else {
                    Toast.makeText(v.getContext()," Не введено имя поставщика", Toast.LENGTH_LONG).show();
                }


            }
        });
        dialog.show();


    }
}
