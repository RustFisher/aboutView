package com.rust.aboutview;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class ClipChildrenActivity extends Activity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clip_children);
        View farLeftChild = findViewById(R.id.child_far_left);
        View leftChild = findViewById(R.id.child_left);
        View middleChild = findViewById(R.id.child_middle);
        View rightChild = findViewById(R.id.child_right);
        View farRightChild = findViewById(R.id.child_far_right);
        farLeftChild.setOnClickListener(this);
        leftChild.setOnClickListener(this);
        middleChild.setOnClickListener(this);
        rightChild.setOnClickListener(this);
        farRightChild.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.child_far_left:
                Toast.makeText(getApplicationContext(), "click far left", Toast.LENGTH_SHORT).show();
                break;
            case R.id.child_left:
                Toast.makeText(getApplicationContext(), "click left", Toast.LENGTH_SHORT).show();
                break;
            case R.id.child_middle:
                Toast.makeText(getApplicationContext(), "click middle", Toast.LENGTH_SHORT).show();
                break;
            case R.id.child_right:
                Toast.makeText(getApplicationContext(), "click right", Toast.LENGTH_SHORT).show();
                break;
            case R.id.child_far_right:
                Toast.makeText(getApplicationContext(), "click far right", Toast.LENGTH_SHORT).show();
                break;

        }
    }
}
