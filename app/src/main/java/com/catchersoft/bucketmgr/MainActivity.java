package com.catchersoft.bucketmgr;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.catchersoft.bucketmgr.activities.costumerclass;
import com.catchersoft.bucketmgr.activities.debitors;
import com.catchersoft.bucketmgr.activities.goods;
import com.catchersoft.bucketmgr.activities.journaldebts;
import com.catchersoft.bucketmgr.activities.journalgoods;
import com.catchersoft.bucketmgr.activities.reportdebts;
import com.catchersoft.bucketmgr.activities.reportgoods;

import com.catchersoft.bucketmgr.tools.DB.DBhelper;
import com.catchersoft.bucketmgr.tools.DataBckupHelper;
import com.catchersoft.bucketmgr.tools.OCRHelper;
import com.catchersoft.bucketmgr.tools.ReportsaveHelper;

import java.io.IOException;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //DatabaseManager db = DatabaseManager.GetManager(this);
        DBhelper db = DBhelper.getInstance(this);
        SQLiteDatabase d = db.getReadableDatabase();
        db.close();
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        //Uri outputFileUri = Uri.fromFile(new File(OCRHelper.img_path));
       // final Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
       // takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);

        //if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
      //      startActivityForResult(takePictureIntent, 1);
      //  }









    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent data) {
        String str = OCRHelper.HandleResult(requestCode,resultCode);

        //((TextView)findViewById(R.id.textView11)).setText(str);

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_save) {


            DataBckupHelper ds = new DataBckupHelper();
            try {
                ds.save(this);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        //Toast.makeText(MainActivity.this,"onNavigationItemSelected", Toast.LENGTH_LONG).show();
        int id = item.getItemId();
        Fragment fragment = null;
        if (id == R.id.nav_costumers) {
            fragment = new costumerclass();


        } else if (id == R.id.nav_goods) {
            fragment = new goods();
        } else if (id == R.id.nav_debitors) {
            fragment = new debitors();
        } else if (id == R.id.nav_rep_debitors) {
            fragment = new journaldebts();
        } else if (id == R.id.nav_journal_goods) {
            fragment = new journalgoods();
        } else if (id == R.id.nav_report_goods){
            fragment = new reportgoods();
        }
        else if (id == R.id.nav_report_debts){
            fragment = new reportdebts();
        }
        else if (id == R.id.nav_print_report_goods){
            ReportsaveHelper csv  = new ReportsaveHelper(this);
            csv.createreportdir();
            csv.SaveCsvReportGoods("1.csv");

        }


        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
