package com.cordite.cordite.Run;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.cordite.cordite.R;

import java.text.BreakIterator;

public class RunGraphViewActivity extends AppCompatActivity {

    private static final int NUM_PAGES = 2;
    private ViewPager mPager;
    private PagerAdapter pagerAdapter;
    private TextView graphTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_run_graph_view);
        graphTitle = findViewById(R.id.graphTxtView);
        graphTitle.setText("Time Vs Speed");
        createPager(); // set pager
        createButtons();
    }

    @Override
    protected void onPause() {
//        clearGraph();
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }

    private void createPager(){
        pagerAdapter = new RunGraphViewActivity.ScreenSlidePagerAdapter(getSupportFragmentManager());

        mPager = findViewById(R.id.pager);
        mPager.setAdapter(pagerAdapter);
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {}
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            public void onPageSelected(int position) {

                RunGraphViewFragment.TimeFrameType timeFrameType = RunGraphViewFragment.TimeFrameType.WEEK;
                createGraph(timeFrameType,"Weekly Progress");

                if(position == 0){
                    graphTitle.setText("Time Vs Avg Speed");

                }
                if(position == 1){
                    graphTitle.setText("Time Vs Distance");
                }
            }
        });

    }

    public class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        SparseArray<Fragment> registeredFragments = new SparseArray<>();

        ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return new RunGraphViewFragment();
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, final int position) {
            Fragment fragment = (Fragment) super.instantiateItem(container, position);
            registeredFragments.put(position, fragment);

            return fragment;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            registeredFragments.remove(position);
            super.destroyItem(container, position, object);
        }

        Fragment getRegisteredFragment(int position) {
            return registeredFragments.get(position);
        }
    }

    private void createButtons() {
        Button allViewBtn = findViewById(R.id.allViewBtn);
        Button weekViewBtn = findViewById(R.id.weekViewBtn);
        Button monthViewBtn = findViewById(R.id.monthViewBtn);

        allViewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View ref) {
                RunGraphViewFragment.TimeFrameType timeFrameType = RunGraphViewFragment.TimeFrameType.ALL;
                createGraph(timeFrameType, "All Time Progress");
            }
        });
        weekViewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View ref) {
                RunGraphViewFragment.TimeFrameType timeFrameType = RunGraphViewFragment.TimeFrameType.WEEK;
                createGraph(timeFrameType,"Weekly Progress");
            }
        });
        monthViewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View ref) {
                RunGraphViewFragment.TimeFrameType timeFrameType = RunGraphViewFragment.TimeFrameType.MONTH;
                createGraph(timeFrameType, "Monthly Progress");
            }

        });
    }

    public void createGraph(RunGraphViewFragment.TimeFrameType  timeFrame, String des){

        RunGraphViewFragment fragment = (RunGraphViewFragment) ((ScreenSlidePagerAdapter) pagerAdapter)
                .getRegisteredFragment(mPager.getCurrentItem());

        fragment.setDescription(des);
        fragment.enableGraph(timeFrame, mPager.getCurrentItem());
    }

}