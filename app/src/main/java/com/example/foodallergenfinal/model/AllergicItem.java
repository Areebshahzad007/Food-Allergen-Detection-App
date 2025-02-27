package com.example.foodallergenfinal.model;

public class AllergicItem {
    private int img;
    private String name;
    private boolean isSelected;

    public AllergicItem(int img, String name, boolean isSelected) {
        this.img = img;
        this.name = name;
        this.isSelected = isSelected;
    }

    public int getImg() { return img; }

    public void setImg(int img) {
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
