package com.agh.goaltracker.util;

import com.agh.goaltracker.R;

public enum ProgressDrawable {

    PLANT1(R.drawable.plant1), PLANT2(R.drawable.plant2), PLANT3(R.drawable.plant3), PLANT4(R.drawable.plant4), WATERING_CAN(R.drawable.watering_can);

    public int resource_id;

    ProgressDrawable(int resource_id) {
        this.resource_id = resource_id;
    }
}
