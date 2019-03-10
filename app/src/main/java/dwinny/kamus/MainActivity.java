package dwinny.kamus;

import android.database.SQLException;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;

import dwinny.kamus.DB.Kamus_Helper;
import dwinny.kamus.DB.Kata;
import dwinny.kamus.adapter.Kamus_Adapter;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FrameLayout frameLayout;
    private MaterialSearchView materialSearchView;
    private boolean bhs_inggris = true;
    private Kamus_Adapter kamus_adapter;
    private Kamus_Helper kamus_helper;
    private ArrayList<Kata> arrayList;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        kamus_helper = new Kamus_Helper(this);
        arrayList = new ArrayList<>();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        recyclerView = findViewById(R.id.RView);
        frameLayout = findViewById(R.id.toolbar_container);
        materialSearchView = findViewById(R.id.search_view);

        setup();
        load();

        materialSearchView.setEllipsize(true);
        materialSearchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.e("submit", query);
                load(query);
                // Toast.makeText(MainActivity.this, "submit :" + query, Toast.LENGTH_SHORT).show();
                return false;
                //on Query Submit
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Toast.makeText(MainActivity.this, "onchange :" + newText, Toast.LENGTH_SHORT).show();
                if (newText.isEmpty()){
                    Log.e("change", newText);
                    load();
                }
                return false;
                //on Query Change

            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void load(String search) {
        Log.e("query", search);
        arrayList.clear();
        try {
            kamus_helper.open();
            if (search.isEmpty()) {
                arrayList = kamus_helper.getAllData(bhs_inggris);
            } else {
                arrayList= kamus_helper.getDataByKata(search, bhs_inggris);
            }

            if (bhs_inggris) {
                getSupportActionBar().setSubtitle(getResources().getString(R.string.inggris_indo));
            } else {
                getSupportActionBar().setSubtitle(getResources().getString(R.string.indo_inggris));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            kamus_helper.close();
        }
        kamus_adapter.addItem(arrayList);
    }

    private void load() {
        load("");
    }

    private void setup() {
        kamus_adapter = new Kamus_Adapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(kamus_adapter);
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

        MenuItem item = menu.findItem(R.id.action_search);
        materialSearchView.setMenuItem(item);

        return true;

    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_inggris_indo) {
            bhs_inggris=true;
            load();

            Log.e("Nav :", String.valueOf(bhs_inggris));


        } else if (id == R.id.nav_indo_inggris) {

            bhs_inggris=false;
            Log.e("Nav :", String.valueOf(bhs_inggris));
            load();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
