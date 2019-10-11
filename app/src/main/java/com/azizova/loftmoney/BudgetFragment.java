package com.azizova.loftmoney;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class BudgetFragment extends Fragment {

    private ItemsAdapter mAdapter;
    private static final int REQUEST_CODE = 100;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_budget, null);
        Button callAddButton = view.findViewById(R.id.call_item_activity);
        callAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                startActivityForResult(new Intent(getActivity(), AddItemActivity.class),REQUEST_CODE);
            }
        });

        RecyclerView recyclerView = view.findViewById(R.id.budget_item_list);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), linearLayoutManager.getOrientation()));

        mAdapter = new ItemsAdapter();
        recyclerView.setAdapter(mAdapter);
        mAdapter.addItem(new Item("Молоко", 70));
        mAdapter.addItem(new Item("Зубная щётка", 70));
        mAdapter.addItem(new Item("Новый телевизор", 20000));
        return view;
    }

    @Override
    public void onActivityResult(final int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        int price;
        try{
            price = Integer.parseInt(data.getStringExtra("price"));
        }catch (NumberFormatException e){
            price = 0;
        }
        if(requestCode == 100 && resultCode == Activity.RESULT_OK){
            mAdapter.addItem(new Item(data.getStringExtra("name"), price));

        }
    }
}
