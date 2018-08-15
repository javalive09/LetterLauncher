package com.javalive09.letterlauncher;

import java.util.ArrayList;
import java.util.List;

import android.support.annotation.NonNull;
import android.text.TextUtils;

public class AppGroup implements Comparable<AppGroup> {

    String letter;

    List<AppModel> appModelList = new ArrayList<>();

    AppGroup(String letter, AppModel appModel) {
        this.letter = letter;
        this.appModelList.add(appModel);
    }

    @Override
    public int compareTo(@NonNull AppGroup o) {
        if (TextUtils.equals(letter, AppModel.NO_LETTER)) {
            char nextZ = 'Z' + 1;
            return String.valueOf(nextZ).compareTo(o.letter);
        }
        return letter.compareTo(o.letter);
    }

    @Override
    public String toString() {
        return "AppGroup{" +
                "letter='" + letter + '\'' +
                ", appModelList=" + appModelList +
                '}';
    }
}
