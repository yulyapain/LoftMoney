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
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.List;
import java.util.ListIterator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BudgetFragment extends Fragment {

    private ItemsAdapter mAdapter;
    public static final int REQUEST_CODE = 100;
    private Api mApi;
    private static final String TYPE = "fragmentType";
    private SwipeRefreshLayout mSwipeRefreshLayout;

    public static BudgetFragment newInstance(final String type) {
        BudgetFragment budgetFragment = new BudgetFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TYPE, type);
        budgetFragment.setArguments(bundle);
        return budgetFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mApi = ((LoftApp)getActivity().getApplication()).getApi();
        loadItems();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_budget, null);

        RecyclerView recyclerView = view.findViewById(R.id.budget_item_list);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), linearLayoutManager.getOrientation()));

        mSwipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadItems();
            }
        });

        mAdapter = new ItemsAdapter(getArguments().getString(TYPE));
        recyclerView.setAdapter(mAdapter);
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
        final int realPrice = price;
        if(requestCode == 100 && resultCode == Activity.RESULT_OK){
            final String name = data.getStringExtra("name");

            final String token = PreferenceManager.getDefaultSharedPreferences(getContext()).getString(MainActivity.TOKEN, "");
            Call<Status> call = mApi.addItem(new AddItemRequest(name, getArguments().getString(TYPE), price), token);
            call.enqueue(new Callback<Status>() {
                @Override
                public void onResponse(Call<Status> call, Response<Status> response) {
                    if (response.body().getStatus().equals("success"))
                        mAdapter.addItem(new Item(name, realPrice));
                }

                @Override
                public void onFailure(Call<Status> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        }
    }

    public void loadItems(){
        final String token = PreferenceManager.getDefaultSharedPreferences(getContext()).getString(MainActivity.TOKEN, "");
        Call<List<Item>> items = mApi.getItems(getArguments().getString(TYPE), token);
        items.enqueue(new Callback<List<Item>>() {
            @Override
            public void onResponse(Call<List<Item>> call, Response<List<Item>> response) {
                mAdapter.clearItems();
                mSwipeRefreshLayout.setRefreshing(false);
                List<Item> items = response.body();
                ListIterator iterator = items.listIterator(items.size());
                while (iterator.hasPrevious()){
                    mAdapter.addItem((Item)iterator.previous());
                }
            }

            @Override
            public void onFailure(Call<List<Item>> call, Throwable t) {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }
}
