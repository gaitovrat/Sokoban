package com.vsb.kru13.sokoban;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.level_list);
        listView.setAdapter(new LevelAdapter(getAssets(), this));

        listView.setOnItemClickListener((parent, view, position, id) -> {
            Level level = (Level) listView.getItemAtPosition(position);
            Intent intent = new Intent(this, CanvasActivity.class);
            intent.putExtra("level", level.getRaw());
            intent.putExtra("position", position);
            startActivityForResult(intent, 0);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0) {
            assert data != null;

            int position = data.getIntExtra("position", 0);

            if ((position + 1) >= listView.getAdapter().getCount()) {
                return;
            }

            Level level = (Level) listView.getItemAtPosition(position + 1);
            Intent intent = new Intent(this, CanvasActivity.class);

            intent.putExtra("level", level.getRaw());
            intent.putExtra("position", position + 1);
            startActivityForResult(intent, 0);
        }
    }
}
