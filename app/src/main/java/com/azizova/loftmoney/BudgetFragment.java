package com.azizova.loftmoney;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.ListIterator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BudgetFragment extends Fragment implements ItemsAdapterListener, ActionMode.Callback {

    private ItemsAdapter mAdapter;
    public static final int REQUEST_CODE = 100;
    private Api mApi;
    private static final String TYPE = "fragmentType";
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private View mView;
    private FloatingActionButton mFab;

    private ActionMode mActionMode;

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
        mView = inflater.inflate(R.layout.fragment_budget, null);
        mFab = getActivity().findViewById(R.id.fab);

        RecyclerView recyclerView = mView.findViewById(R.id.budget_item_list);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), linearLayoutManager.getOrientation()));

        mSwipeRefreshLayout = mView.findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadItems();
            }
        });

        mAdapter = new ItemsAdapter(getArguments().getString(TYPE));
        mAdapter.setListener(this);
        recyclerView.setAdapter(mAdapter);
        return mView;
    }

    @Override
    public void onActivityResult(final int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 100 && resultCode == Activity.RESULT_OK){
            int price;
            try{
                price = Integer.parseInt(data.getStringExtra("price"));
            }catch (NumberFormatException e){
                price = 0;
            }
            final int realPrice = price;
            final String name = data.getStringExtra("name");

            final String token = PreferenceManager.getDefaultSharedPreferences(getContext()).getString(MainActivity.TOKEN, "");
            Call<Status> call = mApi.addItem(new AddItemRequest(name, getArguments().getString(TYPE), price), token);
            call.enqueue(new Callback<Status>() {
                @Override
                public void onResponse(Call<Status> call, Response<Status> response) {
                    if (response.body().getStatus().equals("success"))
                        loadItems();
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

    @Override
    public void onItemClick(Item item, int position) {
        mAdapter.clearItem(position);
        if (mActionMode != null)
           mActionMode.setTitle(getString(R.string.selected, String.valueOf(mAdapter.getSelectedSize())));
        if (mAdapter.getSelectedSize() == 0)
            mActionMode.finish();
    }

    @Override
    public void onItemLingClick(Item item, int position) {
        if (mActionMode == null) {
            getActivity().startActionMode(this);
        }
        mAdapter.toggleItem(position);
        if (mActionMode != null)
            mActionMode.setTitle(getString(R.string.selected, String.valueOf(mAdapter.getSelectedSize())));
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        mFab.hide();
        MenuInflater menuInflater = new MenuInflater(getActivity());
        menuInflater.inflate(R.menu.menu_delete, menu);
        mActionMode = mode;
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return true;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        if (item.getItemId() == R.id.remove){
            new AlertDialog.Builder(getContext())
                    .setMessage(R.string.confirmation)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            removeItems();
                            mActionMode.finish();
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).show();
        }
        return true;
    }

    private void removeItems() {
        String token = PreferenceManager.getDefaultSharedPreferences(getContext()).getString(MainActivity.TOKEN, "");
        List<Integer> selectedItems = mAdapter.getSelectedItemsId();
        for (Integer itemId : selectedItems) {
            Call<Status> call = mApi.removeItem(String.valueOf(itemId.intValue()), token);
            call.enqueue(new Callback<Status>() {
                @Override
                public void onResponse(Call<Status> call, Response<Status> response) {
                    loadItems();
                    mAdapter.clearSelections();
                }

                @Override
                public void onFailure(Call<Status> call, Throwable t) {

                }
            });
        }
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        mFab.show();
        mActionMode = null;
        mAdapter.clearSelections();
    }
}
