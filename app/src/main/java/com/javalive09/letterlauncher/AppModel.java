package com.javalive09.letterlauncher;

import java.io.File;
import java.util.Objects;

import com.github.promeg.pinyinhelper.Pinyin;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;

public class AppModel implements Comparable<AppModel> {

    static final String NO_LETTER = "#";
    private String letter;
    String label;
    Context context;
    Drawable icon;
    ResolveInfo info;
    private File apkFile;
    private boolean mounted;

    AppModel(AppModel appModel) {
        this.letter = appModel.letter;
        this.context = appModel.context;
        this.label = appModel.label;
        this.icon = appModel.icon;
        this.info = appModel.info;
        this.apkFile = appModel.apkFile;
        this.mounted = appModel.mounted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AppModel appModel = (AppModel) o;
        return Objects.equals(letter, appModel.letter) &&
                Objects.equals(label, appModel.label);
    }

    @Override
    public int hashCode() {
        return Objects.hash(letter, label, getApplicationPackageName(), getCls());
    }

    AppModel(Context context, ResolveInfo resolveInfo) {
        this.context = context;
        this.info = resolveInfo;
        this.apkFile = new File(resolveInfo.activityInfo.applicationInfo.sourceDir);
    }

    public ComponentName getComponentName() {
        return new ComponentName(info.activityInfo.packageName, info.activityInfo.name);
    }

    public String getCls() {
        return info.activityInfo.name;
    }

    public String getApplicationPackageName() {
        return info.activityInfo.packageName;
    }

    public String getFavoriteKey() {
        return info.activityInfo.packageName + ":" + info.activityInfo.name;
    }

    public String getDir() {
        return this.info.activityInfo.applicationInfo.sourceDir;
    }

    public Drawable getIcon() {
        if (icon == null) {
            if (apkFile.exists()) {
                icon = info.loadIcon(context.getPackageManager());
                return icon;
            } else {
                mounted = false;
            }
        } else if (!mounted) {
            if (apkFile.exists()) {
                mounted = true;
                icon = info.loadIcon(context.getPackageManager());
                return icon;
            }
        } else {
            return icon;
        }

        return context.getResources().getDrawable(android.R.drawable.sym_def_app_icon, null);
    }

    public String getLabel() {
        if (label == null || !mounted) {
            if (!apkFile.exists()) {
                mounted = false;
                label = info.activityInfo.packageName;
            } else {
                mounted = true;
                CharSequence label = info.loadLabel(context.getPackageManager());
                this.label = label != null ? label.toString() : info.activityInfo.packageName;
            }
        }
        return label;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }

    public String getLetter() {
        if (letter == null) {
            String label = getLabel();
            char c = label.charAt(0);
            if (Pinyin.isChinese(c)) {
                letter = String.valueOf(Pinyin.toPinyin(c).charAt(0));
            } else if (Character.isLetter(c)) {
                letter = String.valueOf(c).toUpperCase();
            } else {
                letter = NO_LETTER;
            }
        }
        return letter;
    }

    public boolean isShortcutAvailable() {
        return Build.VERSION.SDK_INT >= 25;
    }

    @Override
    public String toString() {
        return "AppModel{" +
                "letter='" + letter + '\'' +
                ", apkFile=" + apkFile +
                ", label='" + label + '\'' +
                ", context=" + context +
                ", icon=" + icon +
                ", info=" + info +
                ", mounted=" + mounted +
                '}';
    }

    @Override
    public int compareTo(@NonNull AppModel o) {
        return label.compareTo(o.label);
    }
}
