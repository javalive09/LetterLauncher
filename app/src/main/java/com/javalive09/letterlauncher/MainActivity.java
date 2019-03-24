package com.javalive09.letterlauncher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.github.promeg.pinyinhelper.Pinyin;
import com.javalive09.letterlauncher.adapter.AppGroupsAdapter;
import com.javalive09.letterlauncher.databinding.ActivityMainLayoutBinding;
import com.javalive09.letterlauncher.mode.AppGroup;
import com.javalive09.letterlauncher.mode.AppModel;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.ResolveInfo;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

public class MainActivity extends Activity {

    private AppGroupsAdapter appGroupAdapter;
    private LinearLayoutManager linearLayoutManager;
    private List<AppGroup> groupDataList;
    private ActivityMainLayoutBinding activityMainLayoutBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initMainView();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        refreshLetterRecyclerViewColor();
    }

    private void initMainView() {
        groupDataList = getGroupDataList();
        activityMainLayoutBinding = DataBindingUtil.setContentView(this, R.layout.activity_main_layout);
        appGroupAdapter = new AppGroupsAdapter(groupDataList);
        activityMainLayoutBinding.appGroupList.setAdapter(appGroupAdapter);
        linearLayoutManager = new LinearLayoutManager(MainActivity.this);
        activityMainLayoutBinding.appGroupList.setLayoutManager(linearLayoutManager);
        activityMainLayoutBinding.letterList.setData(groupDataList)
                .setTouchLetterListener(new LetterRecyclerView.TouchLetterListener() {
                    @Override
                    public void onShow(String currentLetter, int index) {
                        activityMainLayoutBinding.letterHint.setVisibility(View.VISIBLE);
                        activityMainLayoutBinding.letterHint.setText(currentLetter);
                        activityMainLayoutBinding.letterList.setBackgroundResource(R.color.letter_hint_bg);
                        linearLayoutManager.scrollToPositionWithOffset(index, 0);
                    }

                    @Override
                    public void onHide() {
                        activityMainLayoutBinding.letterHint.setVisibility(View.GONE);
                        activityMainLayoutBinding.letterList.setBackground(null);
                    }
                }).build(getApplicationContext());
        activityMainLayoutBinding.appGroupList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                refreshLetterRecyclerViewColor();
            }
        });
        registerReceiver();
    }

    private void refreshLetterRecyclerViewColor() {
        final int firstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition();
        final int lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
        activityMainLayoutBinding.letterList.refreshItemColor(firstVisibleItem, lastVisibleItem);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }

    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            Uri uri = intent.getData();
            String action = intent.getAction();
            if (uri != null && !TextUtils.isEmpty(action)) {
                String packageName = uri.getSchemeSpecificPart();
                switch (action) {
                    case Intent.ACTION_PACKAGE_ADDED:
                    case Intent.ACTION_EXTERNAL_APPLICATIONS_AVAILABLE:
                        installGroupList(packageName, groupDataList);
                        sort(groupDataList);
                        refresh(groupDataList);
                        break;
                    case Intent.ACTION_PACKAGE_REMOVED:
                    case Intent.ACTION_EXTERNAL_APPLICATIONS_UNAVAILABLE:
                        List<AppGroup> removeAppGroupList = new ArrayList<>();
                        List<AppModel> removeAppModelList = new ArrayList<>();
                        for (AppGroup appGroup : groupDataList) {
                            removeAppModelList.clear();
                            for (AppModel appModel : appGroup.getAppModelList()) {
                                if (TextUtils.equals(appModel.getInfo().activityInfo.packageName, packageName)) {
                                    removeAppModelList.add(appModel);
                                    break;
                                }
                            }
                            for (AppModel appModel : removeAppModelList) {
                                appGroup.getAppModelList().remove(appModel);
                            }
                            if (appGroup.getAppModelList().size() == 0) {
                                removeAppGroupList.add(appGroup);
                            }
                        }
                        for (AppGroup appGroup : removeAppGroupList) {
                            groupDataList.remove(appGroup);
                        }
                        sort(groupDataList);
                        refresh(groupDataList);
                        break;
                    case Intent.ACTION_PACKAGE_CHANGED:
                        break;
                    default:
                        break;
                }

            }
        }
    };

    private void refresh(List<AppGroup> groupDataList) {
        appGroupAdapter.setList(groupDataList);
        appGroupAdapter.notifyDataSetChanged();
        activityMainLayoutBinding.letterList.refresh(groupDataList);
        activityMainLayoutBinding.letterList.post(refreshLetterRecyclerViewColor);
    }

    private Runnable refreshLetterRecyclerViewColor = new Runnable() {
        @Override
        public void run() {
            refreshLetterRecyclerViewColor();
        }
    };

    private void registerReceiver() {
        IntentFilter filter = new IntentFilter(Intent.ACTION_PACKAGE_ADDED);
        filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        filter.addAction(Intent.ACTION_PACKAGE_CHANGED);
        filter.addDataScheme("package");
        registerReceiver(broadcastReceiver, filter);

        IntentFilter sdFilter = new IntentFilter();
        sdFilter.addAction(Intent.ACTION_EXTERNAL_APPLICATIONS_AVAILABLE);
        sdFilter.addAction(Intent.ACTION_EXTERNAL_APPLICATIONS_UNAVAILABLE);
        registerReceiver(broadcastReceiver, sdFilter);
    }

    private List<AppGroup> getGroupDataList() {
        List<ApplicationInfo> applicationInfoList = getPackageManager().getInstalledApplications(0);
        List<AppGroup> groupList = new ArrayList<>();
        for (int i = 0, len = applicationInfoList.size(); i < len; i++) {
            String packageName = applicationInfoList.get(i).packageName;
            installGroupList(packageName, groupList);
        }
        sort(groupList);
        return groupList;
    }

    private void sort(List<AppGroup> groupDataList) {
        Collections.sort(groupDataList);
        for (AppGroup appGroup : groupDataList) {
            Collections.sort(appGroup.getAppModelList());
        }
    }

    private void installGroupList(String packageName, List<AppGroup> groupList) {
        if (!TextUtils.equals(getPackageName(), packageName)) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.setPackage(packageName);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            List<ResolveInfo> resolveInfoList = getPackageManager().queryIntentActivities(intent, 0);
            for (ResolveInfo info : resolveInfoList) {
                AppModel appModel = new AppModel();
                appModel.setInfo(info);
                appModel.setIcon(info.loadIcon(getPackageManager()));
                appModel.setLabel(info.loadLabel(getPackageManager()).toString());
                appModel.setLetter(getLetter(appModel));
                appModel.setFavoriteKey(
                        appModel.getInfo().activityInfo.packageName + ":" + appModel.getInfo().activityInfo.name);
                installData(groupList, appModel);
            }
        }
    }

    public String getLetter(AppModel appModel) {
        String letter;
        String label = appModel.getLabel();
        char c = label.charAt(0);
        if (Pinyin.isChinese(c)) {
            letter = String.valueOf(Pinyin.toPinyin(c).charAt(0));
        } else if (Character.isLetter(c)) {
            letter = String.valueOf(c).toUpperCase();
        } else {
            letter = AppModel.NO_LETTER;
        }
        return letter;
    }

    private void installData(List<AppGroup> groupList, AppModel appModel) {
        AppGroup currentGroupData = null;
        for (AppGroup groupData : groupList) {
            if (TextUtils.equals(groupData.getLetter(), appModel.getLetter())) {
                currentGroupData = groupData;
                break;
            }
        }
        if (currentGroupData == null) {
            currentGroupData = new AppGroup();
            currentGroupData.setLetter(appModel.getLetter());
            currentGroupData.getAppModelList().add(appModel);
            groupList.add(currentGroupData);
        } else if (!currentGroupData.getAppModelList().contains(appModel)) {
            currentGroupData.getAppModelList().add(appModel);
        }
    }

    public void onBackPressed() {
    }

}
