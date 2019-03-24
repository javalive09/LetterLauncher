package com.javalive09.letterlauncher.mode;

import java.util.ArrayList;
import java.util.List;

import android.text.TextUtils;

/**
 * Created by peter on 2019/3/22
 */
public class AppGroup implements Comparable<AppGroup> {

    private String letter;

    private List<AppModel> appModelList = new ArrayList<>();

    public String getLetter() {
        return letter;
    }

    public AppGroup setLetter(String letter) {
        this.letter = letter;
        return this;
    }

    public List<AppModel> getAppModelList() {
        return appModelList;
    }

    public AppGroup setAppModelList(List<AppModel> appModelList) {
        this.appModelList = appModelList;
        return this;
    }

    @Override
    public int compareTo(AppGroup o) {
        if (TextUtils.equals(letter, AppModel.NO_LETTER)) {
            char nextZ = 'Z' + 1;
            return String.valueOf(nextZ).compareTo(o.letter);
        }
        return letter.compareTo(o.letter);
    }

}
