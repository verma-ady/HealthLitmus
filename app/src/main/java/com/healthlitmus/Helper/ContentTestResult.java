package com.healthlitmus.Helper;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Mukesh on 1/7/2016.
 */
public class ContentTestResult {
    public List<DummyItem> ITEMS = new ArrayList<DummyItem>();
    public Map<String, DummyItem> ITEM_MAP = new HashMap<String, DummyItem>();

    public void addItem(DummyItem item ) {
        ITEMS.add(item);
        ITEM_MAP.put(item.place, item);
    }

    public void clear(){
        this.ITEMS.clear();
    }

    public static class DummyItem {
        public String place;
        public String near;
        public int price;


        public DummyItem(String vPlace, String vNear, int vPrice) {
            this.place = vPlace;
            this.near = vNear;
            this.price = vPrice;
        }

        @Override
        public String toString() {
            return place;
        }
    }
}
