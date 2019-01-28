package com.cordite.cordite.Home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.cordite.cordite.Api.APIClient;
import com.cordite.cordite.Api.RunService;
import com.cordite.cordite.Deserializers.RunDeserializer;
import com.cordite.cordite.Entities.Run;
import com.cordite.cordite.MainActivity;
import com.cordite.cordite.Map.MapsActivity;
import com.cordite.cordite.R;
import com.cordite.cordite.Run.RunAdapter;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.io.IOException;
import java.util.ArrayList;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;

    private RunService runService;

    private RecyclerView recyclerView;
    private RunAdapter runAdapter;
    private RecyclerView.LayoutManager layoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        this.runService = APIClient.getClient().create(RunService.class);

        setupToolbar();
        setupButtons();

        getUserRuns();
    }

    @Override
    protected void onResume() {
        super.onResume();

        getUserRuns();
    }

    private String getToken() {
        SharedPreferences preferences = getSharedPreferences(getString(R.string.shared_preferences_key),
                Context.MODE_PRIVATE);

        return preferences.getString("token", "");
    }

    private void populateJournal(ArrayList<Run> runs) {
        this.runAdapter = new RunAdapter(runs);
        this.layoutManager = new LinearLayoutManager(HomeActivity.this);
        this.recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(runAdapter);
    }

    private ArrayList<Run> convertJsonToRuns(JsonArray jsonArray) {
        ArrayList<Run> runs = new ArrayList<>();

        RunDeserializer deserializer = new RunDeserializer();

        for(JsonElement element : jsonArray) {
            Run run = deserializer.deserialize(element, Run.class, null);
            runs.add(run);
        }

        return runs;
    }

    private void getUserRuns() {
        Call<JsonArray> request = runService.getUserRuns(getToken());

        request.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                ArrayList<Run> runs = convertJsonToRuns(response.body().getAsJsonArray());

                populateJournal(runs);
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Toast.makeText(HomeActivity.this, "Server error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupButtons() {
//        FloatingActionButton trackFab = findViewById(R.id.trackFab);
//
//        trackFab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(HomeActivity.this, MapsActivity.class);
//
//                startActivity(intent);
//            }
//        });
    }

    private void logout() {
        SharedPreferences.Editor editor = getSharedPreferences(getString(R.string.shared_preferences_key),
                Context.MODE_PRIVATE).edit();

        editor.remove("token");

        editor.commit();

        Intent intent = new Intent(HomeActivity.this, MainActivity.class);

        startActivity(intent);

        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        return true;
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
//        actionBar.setIcon(R.drawable.ic_play);

        mDrawerLayout = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                menuItem.setChecked(true);

                mDrawerLayout.closeDrawers();

                switch(menuItem.getItemId()) {
                    case R.id.logout:
                        logout();
                        return true;
                    case R.id.play:
                        Intent intent = new Intent(HomeActivity.this, MapsActivity.class);

                        startActivity(intent);
                        return true;
                }

                return false;
            }
        });
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
