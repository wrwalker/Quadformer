package com.williamrobertwalker.quadformer;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class SettingsActivity extends Activity {

    private float width;
    private float height;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        this.width = displayMetrics.widthPixels;
        this.height = displayMetrics.heightPixels;

        getWindow().setLayout((int) (this.width * 0.80), (int) (this.height * 0.80));

        final ListView listView = (ListView) findViewById(R.id.listView);

        final String[] values = new String[]{"Reset Game", "Reload Level"};

        final ArrayList<String> list = new ArrayList<>();
        Collections.addAll(list, values);

        final StableArrayAdapter adapter = new StableArrayAdapter(this, android.R.layout.simple_list_item_1, list);
        listView.setAdapter(adapter);

        // React to user clicks on item
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parentAdapter, View view, int position, long id) {

                if (id == 0) { // Reset Game

                    GameView.level = 1;
                    synchronized(GameView.syncLock) {
                        MainActivity.gameView.generateLevel();
                    }
                }

                else if(id == 1) { //Reload Level

                    synchronized(GameView.syncLock) {
                        MainActivity.gameView.generateLevel();
                    }
                }

                else{
                    new UnsupportedOperationException().printStackTrace();
                }
            }
        });
    }


    private class StableArrayAdapter extends ArrayAdapter<String> {

        HashMap<String, Integer> mIdMap = new HashMap<>();

        public StableArrayAdapter(Context context, int textViewResourceId, List<String> objects) {
            super(context, textViewResourceId, objects);
            for (int i = 0; i < objects.size(); ++i) {
                mIdMap.put(objects.get(i), i);
            }
        }

        @Override
        public long getItemId(int position) {
            String item = getItem(position);
            return mIdMap.get(item);
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

    }

}
