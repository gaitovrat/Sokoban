package com.vsb.kru13.sokoban;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.Arrays;

public class CanvasActivity extends AppCompatActivity {
    private SokoView sokoView;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canvas);

        Intent intent = getIntent();
        int[] level = intent.getIntArrayExtra("level");
        position = intent.getIntExtra("position", 0);

        sokoView = findViewById(R.id.sokoView);
        sokoView.setLevel(level);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.reload_item) {
            this.sokoView.reload();
            return true;
        } else if(item.getItemId() == R.id.main_item) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void win() {
        Intent intent = new Intent();
        intent.putExtra("position", position);
        setResult(0, intent);
        finish();
    }
}