package com.azizova.loftmoney;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BalanceFragment extends Fragment {

    private TextView myExpences;
    private TextView myIncome;
    private TextView totalFinances;
    private Api mApi;
    private DiagramView mDiagramView;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    public static BalanceFragment newInstance() {
        return new BalanceFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mApi = ((LoftApp)getActivity().getApplication()).getApi();
        loadBalance();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_balance, null);
        myExpences = view.findViewById(R.id.my_expences);
        myIncome = view.findViewById(R.id.my_income);
        totalFinances = view.findViewById(R.id.total_finances);
        mDiagramView = view.findViewById(R.id.diagram_view);
        mSwipeRefreshLayout = view.findViewById(R.id.balance_swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadBalance();
            }
        });
        return view;
    }

    public void loadBalance(){
        String token = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(MainActivity.TOKEN, "");
        Call<BalanceResponce> responceCall = mApi.getBalance(token);
        responceCall.enqueue(new Callback<BalanceResponce>() {
            @Override
            public void onResponse(Call<BalanceResponce> call, Response<BalanceResponce> response) {
                final float totalExpences = response.body().getTotalExpences();
                final float totalIncome = response.body().getTotalIncome();

                myExpences.setText(myExpences.getContext().getResources().getString(R.string.price_with_currency, String.valueOf(totalExpences)));

                myIncome.setText(myIncome.getContext().getResources().getString(R.string.price_with_currency, String.valueOf(totalIncome)));
                totalFinances.setText(totalFinances.getContext().getResources().getString(R.string.price_with_currency, String.valueOf(totalIncome - totalExpences)));
                mDiagramView.update(totalExpences, totalIncome);
                mSwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<BalanceResponce> call, Throwable t) {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }
}
