package study.ian.ptt.util;

import android.content.res.Resources;

import org.jetbrains.annotations.NotNull;

import androidx.core.content.res.ResourcesCompat;
import study.ian.ptt.R;

public class CountTextConverter {

    @NotNull
    public static String getUserCountText(String c) {
        if (c.equals("")) {
            return "";
        }

        int count = Integer.valueOf(c);
        if (count <= 99) {
            return String.valueOf(count);
        } else if (count <= 999) {
            return "HOT";
        } else {
            return "爆";
        }
    }

    public static int getUserCountColor(Resources resources, String c) {
        if (c.equals("")) {
            return ResourcesCompat.getColor(resources, R.color.userCount_0, null);
        }

        int count = Integer.valueOf(c);
        if (count <= 10) {
            return ResourcesCompat.getColor(resources, R.color.userCount_1_10, null);
        } else if (count <= 49) {
            return ResourcesCompat.getColor(resources, R.color.userCount_11_49, null);
        } else if (count <= 99) {
            return ResourcesCompat.getColor(resources, R.color.userCount_50_99, null);
        } else if (count <= 999) {
            return ResourcesCompat.getColor(resources, R.color.userCount_100_999, null);
        } else if (count <= 1999) {
            return ResourcesCompat.getColor(resources, R.color.userCount_1000_1999, null);
        } else if (count <= 4999) {
            return ResourcesCompat.getColor(resources, R.color.userCount_2000_4999, null);
        } else if (count <= 9999) {
            return ResourcesCompat.getColor(resources, R.color.userCount_5000_9999, null);
        } else if (count <= 29999) {
            return ResourcesCompat.getColor(resources, R.color.userCount_10000_29999, null);
        } else if (count <= 59999) {
            return ResourcesCompat.getColor(resources, R.color.userCount_30000_59999, null);
        } else if (count <= 99999) {
            return ResourcesCompat.getColor(resources, R.color.userCount_60000_99999, null);
        } else {
            return ResourcesCompat.getColor(resources, R.color.userCount_100000, null);
        }
    }

    public static int getPushCountColor(Resources resources, String c) {
        if (c.equals("")) {
            return ResourcesCompat.getColor(resources, R.color.pushCount_0, null);
        } else if (c.startsWith("X")) {
            return ResourcesCompat.getColor(resources, R.color.pushCount_X, null);
        } else if (c.equals("爆")) {
            return ResourcesCompat.getColor(resources, R.color.pushCount_100, null);
        }

        int count = Integer.valueOf(c);
        if (count <= 9) {
            return ResourcesCompat.getColor(resources, R.color.pushCount_1_9, null);
        } else {
            return ResourcesCompat.getColor(resources, R.color.pushCount_10_99, null);
        }
    }
}
