package com.javalive09.letterlauncher.mode;

import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;

/**
 * Created by peter on 2019/3/22
 */
public class AppModel implements Comparable<AppModel>, Cloneable {

    public static final String NO_LETTER = "#";

    private String label;

    private String letter;

    private Drawable icon;

    private ResolveInfo info;

    private String favoriteKey;

    public AppModel() {

    }

    public AppModel(AppModel appModel) {
        label = appModel.getLabel();
        letter = appModel.getLetter();
        icon = appModel.getIcon();
        info = appModel.getInfo();
        favoriteKey = appModel.getFavoriteKey();
    }

    public String getLabel() {
        return label;
    }

    public AppModel setLabel(String label) {
        this.label = label;
        return this;
    }

    public String getLetter() {
        return letter;
    }

    public AppModel setLetter(String letter) {
        this.letter = letter;
        return this;
    }

    public Drawable getIcon() {
        return icon;
    }

    public AppModel setIcon(Drawable icon) {
        this.icon = icon;
        return this;
    }

    public ResolveInfo getInfo() {
        return info;
    }

    public AppModel setInfo(ResolveInfo info) {
        this.info = info;
        return this;
    }

    public String getFavoriteKey() {
        return favoriteKey;
    }

    public AppModel setFavoriteKey(String favoriteKey) {
        this.favoriteKey = favoriteKey;
        return this;
    }

    @Override
    public int compareTo(AppModel o) {
        return label.compareTo(o.label);
    }

}
