package com.itayc14.uppsaletask;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.itayc14.uppsaletask.utils.GetDishAPI;
import com.itayc14.uppsaletask.utils.RetrofitBuilderHelper;
import java.util.HashMap;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private final static int NO_INTERNET = 224;
    private static final String TAG = "tag";
    private static final int SERVER_CON_FAILURE = 225;
    private View loadBar;
    private DishesAdapter mAdapter;
    private RecyclerView mRecycler;
    private HashMap<String, Object> dishesPressed;
    private DatabaseReference myDBRef;
    private List<Dish> mDishesList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadBar =findViewById(R.id.main_loadBar);
        if (!isConnectedToInternet())
            notifyUser(NO_INTERNET);
        mRecycler = (RecyclerView)findViewById(R.id.main_recycler_view);
        retreiveDishes();
        myDBRef = FirebaseDatabase.getInstance().getReference("dishesPresses");
        dishesPressed = new HashMap<>();
        myDBRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<HashMap<String,Object>> t =
                        new GenericTypeIndicator<HashMap<String,Object>>() {};
                dishesPressed = dataSnapshot.getValue(t);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: "+ databaseError.getDetails());
            }
        });
    }

    /**
     * checks if there's a gateway to the internet
     * @return true if connected
     */
    private boolean isConnectedToInternet(){
        ConnectivityManager cm =
                (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }

    /**
     * Alerts the user with the message being received. corresponding functionality
     * for interacting with the user is being added if needed.
     * @param messageCode constant which identify which message should be displayed
     */
    private void notifyUser(int messageCode) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        switch (messageCode){
            case NO_INTERNET:
                alertBuilder.setMessage(R.string.no_internet_content);
                alertBuilder.setTitle(R.string.no_internet_title);
                alertBuilder.setCancelable(false);
                alertBuilder.setPositiveButton(R.string.approve, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_DATA_ROAMING_SETTINGS);
                        startActivityForResult(intent, NO_INTERNET);
                    }
                });
                alertBuilder.setNegativeButton(R.string.exitApp, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                break;
            case SERVER_CON_FAILURE:
                Toast.makeText(this, R.string.serv_con_fail, Toast.LENGTH_SHORT).show();
        }
        alertBuilder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case NO_INTERNET:
                if (!isConnectedToInternet())
                    notifyUser(R.string.no_internet_content);
                break;
        }
    }

    /**
     * retreives all mDishesList from uppsale database
     */
    private void retreiveDishes() {
        GetDishAPI dishAPI = RetrofitBuilderHelper.getInstance().create(GetDishAPI.class);
        Call<List<Dish>> dishesCall = dishAPI.getAllDishes();
        dishesCall.enqueue(new Callback<List<Dish>>() {
            @Override
            public void onResponse(Call<List<Dish>> call, Response<List<Dish>> response) {
                mDishesList = response.body();
                initializeRecycler();
                loadBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<List<Dish>> call, Throwable t) {
                Log.d(TAG, "onFailure: "+t.toString());
                notifyUser(SERVER_CON_FAILURE);
                loadBar.setVisibility(View.GONE);
            }
        });
    }

    private View.OnClickListener itemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String dishID = mDishesList.get(mRecycler.getChildLayoutPosition(v)).getDishID();
            int count = 0;
            if (dishesPressed.containsKey(dishID))
                count = Integer.parseInt(dishesPressed.get(dishID).toString());
            myDBRef.child(dishID).setValue(++count);
            Toast.makeText(MainActivity.this, "counts: "+count, Toast.LENGTH_SHORT).show();
        }
    };
    private void initializeRecycler() {
        mRecycler.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecycler.setLayoutManager(layoutManager);
        mAdapter = new DishesAdapter(mDishesList, this, itemClickListener);
        mRecycler.setAdapter(mAdapter);
    }

}
