package com.example.ethon.car_service_station;

import android.app.TabActivity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;

public class DashBoard extends TabActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        TabHost tabHost=(TabHost)findViewById(android.R.id.tabhost);

        TabHost.TabSpec jobSpec=tabHost.newTabSpec("tid1");
        TabHost.TabSpec profileSpec=tabHost.newTabSpec("tid1");

        jobSpec.setIndicator("JOBS").setContent(new Intent(this,JobListActivity.class));
        profileSpec.setIndicator("PROFILE").setContent(new Intent(this,ProfileActivity.class));

        tabHost.addTab(jobSpec);
        tabHost.addTab(profileSpec);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_dash_board, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
