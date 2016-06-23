package com.wenping.viewpagerguide;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;


/**
 * 应用引导页
 */
public class ViewPageActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {
    private ViewPageDotView mWelcomeDotView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        ViewPager mViewPager = (ViewPager) findViewById(R.id.view_page);

        GuideAdapter guideAdapter = new GuideAdapter(ViewPageActivity.this);
        guideAdapter.addAll(getGuideViewModels());
        mViewPager.setAdapter(guideAdapter);
        mViewPager.addOnPageChangeListener(this);

        mWelcomeDotView = (ViewPageDotView) findViewById(R.id.dot_view);
        mWelcomeDotView.setNumOfCircles(guideAdapter.getCount(), getResources().getDimensionPixelSize(R.dimen.height_very_small));

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        mWelcomeDotView.onPageScrolled(position, positionOffset, positionOffsetPixels);
    }

    @Override
    public void onPageSelected(int position) {
        mWelcomeDotView.onPageSelected(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        mWelcomeDotView.onPageScrollStateChanged(state);
    }


    private List<GuideViewModel> getGuideViewModels() {

        return new ArrayList<GuideViewModel>() {{
            add(new GuideViewModel(getResources().getColor(R.color.guide_bg_color_1), getString(R.string.guide_title_1),
                    getString(R.string.guide_description_1), R.drawable.guide_1));

            add(new GuideViewModel(getResources().getColor(R.color.guide_bg_color_2), getString(R.string.guide_title_2),
                    getString(R.string.guide_description_2), R.drawable.guide_2));

            add(new GuideViewModel(getResources().getColor(R.color.guide_bg_color_3), getString(R.string.guide_title_3),
                    getString(R.string.guide_description_3), R.drawable.guide_3));
        }};
    }
}
