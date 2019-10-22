package com.azizova.loftmoney;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {

    public static final String EXPENSE = "expense";
    public static final String INCOME = "income";
    public static final String TOKEN = "token";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TabLayout tabLayout = findViewById(R.id.tabs);

        final ViewPager viewPager = findViewById(R.id.viewpager);
        final BudgetPagerAdapter adapter = new BudgetPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(adapter);

        FloatingActionButton floatingActionButton = findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int activeFragmentIndex = viewPager.getCurrentItem();
                Intent intent = new Intent(MainActivity.this, AddItemActivity.class);
                switch (activeFragmentIndex) {
                    case 0:
                        intent.putExtra("type", EXPENSE);
                        break;
                    case 1:
                        intent.putExtra("type", INCOME);
                        break;
                    default:
                        intent.putExtra("type", "");
                        break;
                }

                Fragment activeFragment = getSupportFragmentManager().getFragments().get(activeFragmentIndex);
                activeFragment.startActivityForResult(intent,BudgetFragment.REQUEST_CODE);
                overridePendingTransition(R.anim.from_right_in, R.anim.from_left_out);
            }
        });

        /*viewPager.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY > oldScrollY) {
                    FloatingActionButton.hide();
                } else {
                    FloatingActionButton.show();
                }
            }
        });*/
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setText(R.string.expences);
        tabLayout.getTabAt(1).setText(R.string.income);

        for (Fragment fragment :getSupportFragmentManager().getFragments()) {
            if (fragment instanceof BudgetFragment)
                ((BudgetFragment) fragment).loadItems();
        }

    }

    static class BudgetPagerAdapter extends FragmentPagerAdapter{

        public BudgetPagerAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return BudgetFragment.newInstance(EXPENSE);
                case 1:
                    return BudgetFragment.newInstance(INCOME);
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    }

}
