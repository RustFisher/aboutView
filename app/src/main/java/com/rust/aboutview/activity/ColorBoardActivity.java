package com.rust.aboutview.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.rust.aboutview.R;
import com.rust.aboutview.view.ColorBoardListAdapter;

import java.util.ArrayList;

public class ColorBoardActivity extends Activity {
    private static final String TAG = ColorBoardActivity.class.getSimpleName();
    private RecyclerView mColorBoard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_board);
        mColorBoard = (RecyclerView) findViewById(R.id.color_board_r_view);
        initColorBoard();
    }

    private void initColorBoard() {
        final ArrayList<ColorBoardListAdapter.ColorItemViewEntity> colorItemEntities = new ArrayList<>();
        colorItemEntities.add(new ColorBoardListAdapter.ColorItemViewEntity(1, Color.rgb(0, 10, 50)));
        colorItemEntities.add(new ColorBoardListAdapter.ColorItemViewEntity(2, Color.rgb(0, 10, 250)));
        colorItemEntities.add(new ColorBoardListAdapter.ColorItemViewEntity(3, Color.rgb(0, 210, 50)));
        colorItemEntities.add(new ColorBoardListAdapter.ColorItemViewEntity(4, Color.rgb(0, 70, 90)));
        colorItemEntities.add(new ColorBoardListAdapter.ColorItemViewEntity(5, Color.rgb(0, 110, 220)));
        colorItemEntities.add(new ColorBoardListAdapter.ColorItemViewEntity(6, Color.rgb(100, 222, 50)));
        colorItemEntities.add(new ColorBoardListAdapter.ColorItemViewEntity(7, Color.rgb(150, 10, 50)));
        colorItemEntities.add(new ColorBoardListAdapter.ColorItemViewEntity(8, Color.rgb(90, 80, 110)));
        colorItemEntities.add(new ColorBoardListAdapter.ColorItemViewEntity(9, Color.rgb(200, 70, 120)));
        colorItemEntities.add(new ColorBoardListAdapter.ColorItemViewEntity(10, Color.rgb(30, 40, 130)));
        colorItemEntities.add(new ColorBoardListAdapter.ColorItemViewEntity(11, Color.rgb(10, 50, 140)));
        colorItemEntities.add(new ColorBoardListAdapter.ColorItemViewEntity(12, Color.rgb(80, 160, 150)));
        colorItemEntities.add(new ColorBoardListAdapter.ColorItemViewEntity(13, Color.rgb(90, 150, 160)));
        colorItemEntities.add(new ColorBoardListAdapter.ColorItemViewEntity(14, Color.rgb(120, 140, 170)));
        colorItemEntities.add(new ColorBoardListAdapter.ColorItemViewEntity(15, Color.rgb(220, 120, 180)));
        colorItemEntities.add(new ColorBoardListAdapter.ColorItemViewEntity(16, Color.rgb(170, 100, 190)));
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        gridLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mColorBoard.setLayoutManager(gridLayoutManager);
        final ColorBoardListAdapter ColorBoardListAdapter = new ColorBoardListAdapter(colorItemEntities);
        ColorBoardListAdapter.setOnItemClickListener(new ColorBoardListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                int clickID = colorItemEntities.get(position).id;
                int color = colorItemEntities.get(position).color;
                ColorBoardListAdapter.setSelectedPosition(position);
                Log.d(TAG, "onItemClick: " + clickID + ";  color = " + color);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
        mColorBoard.setAdapter(ColorBoardListAdapter);
    }


}
