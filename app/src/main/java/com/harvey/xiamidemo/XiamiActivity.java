package com.harvey.xiamidemo;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextPaint;
import android.util.DisplayMetrics;
import android.widget.TextView;

public class XiamiActivity extends AppCompatActivity {

    private TabLayout tablayout;
    private ViewPager viewpager;
    private String[] titles = {"乐库", "推荐", "趴间", "看点"};
    private int textMinWidth = 0;
    private int textMaxWidth = 0;
    private boolean isClickTab;
    private float mLastPositionOffsetSum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xiami);

        tablayout = findViewById(R.id.tablayout);
        viewpager = findViewById(R.id.viewpager);
        viewpager.setAdapter(new XiamiAdapter(this, getSupportFragmentManager()));
        tablayout.setupWithViewPager(viewpager);

        initSize();

        for (int i = 0; i < 4; i++) {
            TabLayout.Tab tab = tablayout.getTabAt(i);
            assert tab != null;
            tab.setCustomView(R.layout.tab_item);//给tab自定义样式
            assert tab.getCustomView() != null;
            AppCompatTextView textView = tab.getCustomView().findViewById(R.id.tab_text);
            textView.setText(titles[i]);
            if (i == 0) {
                tab.getCustomView().findViewById(R.id.tab_text).setSelected(true);//第一个tab被选中
                ((AppCompatTextView) tab.getCustomView().findViewById(R.id.tab_text)).setWidth(textMaxWidth);
                ((MusicWaveView) tab.getCustomView().findViewById(R.id.wave)).setAmplitudeDiv(2f);
            } else {
                ((AppCompatTextView) tab.getCustomView().findViewById(R.id.tab_text)).setWidth(textMinWidth);
                ((MusicWaveView) tab.getCustomView().findViewById(R.id.wave)).setAmplitudeDiv(4f);
            }
        }
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // 当前总的偏移量
                float currentPositionOffsetSum = position + positionOffset;
                // 上次滑动的总偏移量大于此次滑动的总偏移量，页面从右向左进入(手指从右向左滑动)
                boolean rightToLeft = mLastPositionOffsetSum <= currentPositionOffsetSum;
                if (currentPositionOffsetSum == mLastPositionOffsetSum) return;
                int enterPosition;
                int leavePosition;
                float percent;
                if (rightToLeft) {  // 从右向左滑
                    enterPosition = (positionOffset == 0.0f) ? position : position + 1;
                    leavePosition = enterPosition - 1;
                    percent = (positionOffset == 0.0f) ? 1.0f : positionOffset;
                } else {            // 从左向右滑
                    enterPosition = position;
                    leavePosition = position + 1;
                    percent = 1 - positionOffset;
                }
                if (!isClickTab) {
                    int width = (int) (textMinWidth + (textMaxWidth - textMinWidth) * (1 - percent));
                    ((AppCompatTextView) (tablayout.getTabAt(leavePosition).getCustomView().findViewById(R.id.tab_text)))
                            .setWidth(width);
                    ((AppCompatTextView) (tablayout.getTabAt(enterPosition).getCustomView().findViewById(R.id.tab_text)))
                            .setWidth((int) (textMinWidth + (textMaxWidth - textMinWidth) * percent));
                    ((MusicWaveView) tablayout.getTabAt(enterPosition).getCustomView().findViewById(R.id.wave)).setAmplitudeDiv(2f);
                    ((MusicWaveView) tablayout.getTabAt(leavePosition).getCustomView().findViewById(R.id.wave)).setAmplitudeDiv(4f);
                }

                mLastPositionOffsetSum = currentPositionOffsetSum;
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == 0) {
                    isClickTab = false;
                }
            }
        });
        tablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                isClickTab = true;
                tab.getCustomView().findViewById(R.id.tab_text).setSelected(true);
                viewpager.setCurrentItem(tab.getPosition());
                ((AppCompatTextView) (tab.getCustomView().findViewById(R.id.tab_text))).setWidth(textMaxWidth);
                ((MusicWaveView) tab.getCustomView().findViewById(R.id.wave)).setAmplitudeDiv(2f);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.getCustomView().findViewById(R.id.tab_text).setSelected(false);
                ((AppCompatTextView) (tab.getCustomView().findViewById(R.id.tab_text))).setWidth(textMinWidth);
                ((MusicWaveView) tab.getCustomView().findViewById(R.id.wave)).setAmplitudeDiv(4f);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {


            }
        });
    }

    private void initSize() {
        Resources resources = this.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        int width = dm.widthPixels;
        textMaxWidth = width / 4 - 40;
        TextView tv = new TextView(this);
        TextPaint textPaint = tv.getPaint();
        textMinWidth = (int) textPaint.measureText("乐库");
    }
}

