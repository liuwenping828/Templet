package com.wenping.viewpagerguide;


public class GuideViewModel {
    public int bgColor;
    public String title;
    public String description;
    public int imageRes;

    public GuideViewModel(int imageRes) {
        this.imageRes = imageRes;
    }

    public GuideViewModel(int bgColor, String title, String description, int imageRes) {
        this.bgColor = bgColor;
        this.title = title;
        this.description = description;
        this.imageRes = imageRes;
    }
}
