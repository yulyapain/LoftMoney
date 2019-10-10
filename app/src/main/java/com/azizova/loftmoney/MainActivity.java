package com.azizova.loftmoney;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {

    private ItemsAdapter mAdapter;

    public MainActivity(){
        super();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button callAddButton = findViewById(R.id.call_item_activity);
        callAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                startActivityForResult(new Intent(MainActivity.this, AddItemActivity.class),100);
            }
        });

        RecyclerView recyclerView = findViewById(R.id.budget_item_list);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), linearLayoutManager.getOrientation()));

        mAdapter = new ItemsAdapter();
        recyclerView.setAdapter(mAdapter);
        mAdapter.addItem(new Item("Молоко", 70));
        mAdapter.addItem(new Item("Зубная щётка", 70));
        mAdapter.addItem(new Item("Новый телевизор", 20000));

    }

    @Override
    protected void onActivityResult(final int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        int price;
        try{
            price = Integer.parseInt(data.getStringExtra("price"));
        }catch (NumberFormatException e){
            price = 0;
        }
        if(requestCode == 100 && resultCode == RESULT_OK){
            mAdapter.addItem(new Item(data.getStringExtra("name"), price));

        }
    }
}
