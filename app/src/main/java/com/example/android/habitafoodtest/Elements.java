package com.example.android.habitafoodtest;

public class Elements {
    private String item;
    private String probability;

    public Elements(String item, String probability) {
        this.item = item;
        this.probability = probability;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getProbability() {
        return probability;
    }

    public void setProbability(String probability) {
        this.probability = probability;
    }
}
