package com.rust.aboutview;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DrawerActivity extends Activity {

    SimpleAdapter contentLeftAdapter;
    RelativeLayout leftDrawer;
    ListView leftList;

    Button exitBtn;
    Button confirmExitBtn;
    Button cancelBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer_layout);
        exitBtn = (Button) findViewById(R.id.btn_left_exit);
        confirmExitBtn = (Button) findViewById(R.id.btn_left_confirm);
        cancelBtn = (Button) findViewById(R.id.btn_left_cancel);

        confirmExitBtn.setVisibility(View.GONE);
        cancelBtn.setVisibility(View.GONE);

        /* 左抽屉 */
        leftDrawer = (RelativeLayout) findViewById(R.id.left_relative_drawer);

        /* 左列表在左抽屉里 */
        leftList = (ListView) leftDrawer.findViewById(R.id.left_list);

        /* 适配器装载数据;即初始化导航列表；这里使用SimpleAdapter，加载自定义的LinearLayout作为按钮 */
        contentLeftAdapter = new SimpleAdapter(this, leftDrawerListData(),
                R.layout.list_item_linearlayout, new String[]{"image", "text"},
                new int[]{R.id.image_left_item, R.id.tv_left_item});

        leftList.setAdapter(contentLeftAdapter);

        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitBtn.setVisibility(View.GONE);
                cancelBtn.setVisibility(View.VISIBLE);
                confirmExitBtn.setVisibility(View.VISIBLE);
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitBtn.setVisibility(View.VISIBLE);
                cancelBtn.setVisibility(View.GONE);
                confirmExitBtn.setVisibility(View.GONE);
            }
        });

        confirmExitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private List<Map<String, Object>> leftDrawerListData() {
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        map.put("image", R.drawable.delicious_orange_01);
        map.put("text", "橘子");
        list.add(map);

        map = new HashMap<>();
        map.put("image", R.drawable.sixtraveltransportation);
        map.put("text", "路标");
        list.add(map);

        map = new HashMap<>();
        map.put("image", R.drawable.train);
        map.put("text", "火车");
        list.add(map);

        map = new HashMap<>();
        map.put("image", R.drawable.ecologytree);
        map.put("text", "树苗");
        list.add(map);

        map = new HashMap<>();
        map.put("image", R.drawable.ecology);
        map.put("text", "插头");
        list.add(map);

        return list;
    }
}
