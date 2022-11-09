package com.vsb.kru13.sokoban;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class LevelAdapter extends BaseAdapter {
    private final List<Level> levels;
    private final Context context;

    public LevelAdapter(AssetManager assetManager, Context context) {
        this.levels = new ArrayList<>();
        this.context = context;

        try {
            InputStream inputStream = assetManager.open("levels.txt");
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            inputStream.close();

            String[] strings = new String(buffer).split("FFFFFF");

            for (String levelText : strings) {
                Level level = new Level();
                level.setLevel(levelText);
                levels.add(level);
            }
        } catch (IOException e) {
            Log.e("level", e.getMessage());
        }
    }

    @Override
    public int getCount() {
        return levels.size();
    }

    @Override
    public Object getItem(int position) {
        return levels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        }

        TextView text = convertView.findViewById(R.id.list_item_text);
        String displayText = String.format("Level %d", position);
        text.setText(displayText);

        return convertView;
    }
}
