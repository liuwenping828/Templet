package com.wenping.viewpagerguide;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.wenping.viewpagerguide.databinding.ViewPageItemViewBinding;

public class GuideAdapter extends BasePagerAdapter<GuideViewModel> {

    public GuideAdapter(Context c) {
        super(c);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        GuideViewModel model = getItem(position);

        ViewPageItemViewBinding binding = ViewPageItemViewBinding.inflate(super.mInflater, container, false);
        binding.setViewModel(model);
        container.addView(binding.getRoot());
        return binding.getRoot();

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        if(object instanceof View) {
            container.removeView((View) object);
        }
    }


    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
