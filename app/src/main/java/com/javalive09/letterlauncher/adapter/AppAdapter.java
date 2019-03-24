package com.javalive09.letterlauncher.adapter;

import java.util.List;

import com.javalive09.letterlauncher.BR;
import com.javalive09.letterlauncher.R;
import com.javalive09.letterlauncher.databinding.AppItemLayoutBinding;
import com.javalive09.letterlauncher.mode.AppModel;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.LauncherApps;
import android.content.pm.ShortcutInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Process;
import android.os.UserHandle;
import android.provider.Settings;
import android.view.View;

/**
 * Created by peter on 2019/2/13
 */
public class AppAdapter extends BaseRecycleAdapter<AppModel, AppItemLayoutBinding> {

    public AppAdapter(List<AppModel> list) {
        super(list);
    }

    @Override
    protected int getItemLayoutId(int viewType) {
        return R.layout.app_item_layout;
    }

    @Override
    protected void onBind(final Holder<AppItemLayoutBinding> holder, final AppModel appModel, int position) {
        holder.dataBinding.setVariable(BR.appMode, appModel);
        holder.dataBinding.executePendingBindings();
        holder.itemView.setTag(appModel);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = holder.itemView.getContext().getPackageManager()
                        .getLaunchIntentForPackage(appModel.getInfo().activityInfo.packageName);
                if (intent != null) {
                    intent.setComponent(new ComponentName(appModel.getInfo().activityInfo.packageName,
                            appModel.getInfo().activityInfo.name));
                    holder.itemView.getContext().startActivity(intent);
                }
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final Context context = holder.itemView.getContext();
                AlertDialog.Builder builder = new AlertDialog.Builder(context, android.R.style.Theme_DeviceDefault_Light_Dialog_Alert);
                builder.setTitle(appModel.getLabel());
                builder.setIcon(appModel.getIcon());
                if (Build.VERSION.SDK_INT >= 25) {
                    final List<ShortcutInfo> shortcutInfoList = getShortcutListInfo(context, appModel);
                    if (shortcutInfoList != null && shortcutInfoList.size() > 0) {
                        CharSequence[] arrayOfCharSequence = getItems(shortcutInfoList);
                        builder.setItems(arrayOfCharSequence, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ShortcutInfo shortcutInfo = shortcutInfoList.get(i);
                                launchShortcutAPP(holder.itemView.getContext(), shortcutInfo);
                            }
                        });
                    }
                }
                builder.setPositiveButton(R.string.detail, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        showDetailStopView(context, appModel);
                    }
                }).show();
                return true;
            }
        });
    }

    private void showDetailStopView(Context context, AppModel paramAppModel) {
        if (paramAppModel != null) {
            Intent localIntent = new Intent();
            localIntent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            localIntent.setData(Uri.fromParts("package", paramAppModel.getInfo().activityInfo.packageName, null));
            context.startActivity(localIntent);
        }
    }

    @TargetApi(Build.VERSION_CODES.N_MR1)
    public CharSequence[] getItems(List<ShortcutInfo> paramList) {
        if (paramList != null) {
            CharSequence[] arrayOfCharSequence = new CharSequence[paramList.size()];
            for (int i = 0, len = paramList.size(); i < len; i++) {
                arrayOfCharSequence[i] = paramList.get(i).getShortLabel();
            }
            return arrayOfCharSequence;
        }
        return null;
    }

    @TargetApi(Build.VERSION_CODES.N_MR1)
    public List<ShortcutInfo> getShortcutListInfo(Context context, AppModel appModel) {
        LauncherApps localLauncherApps =
                (LauncherApps) context.getSystemService(Context.LAUNCHER_APPS_SERVICE);
        if (localLauncherApps != null && localLauncherApps.hasShortcutHostPermission()) {
            int queryFlags =
                    LauncherApps.ShortcutQuery.FLAG_MATCH_DYNAMIC | LauncherApps.ShortcutQuery.FLAG_MATCH_MANIFEST
                            | LauncherApps.ShortcutQuery.FLAG_MATCH_PINNED;
            return localLauncherApps.getShortcuts(
                    new LauncherApps.ShortcutQuery().setPackage(appModel.getInfo().activityInfo.packageName).setQueryFlags(queryFlags),
                    UserHandle.getUserHandleForUid(appModel.getInfo().activityInfo.applicationInfo.uid));
        }
        return null;
    }

    @TargetApi(Build.VERSION_CODES.N_MR1)
    public void launchShortcutAPP(Context context, ShortcutInfo shortcutInfo) {
        LauncherApps localLauncherApps = (LauncherApps) context.getSystemService(Context.LAUNCHER_APPS_SERVICE);
        if (localLauncherApps != null) {
            localLauncherApps.startShortcut(shortcutInfo.getPackage(), shortcutInfo.getId(), null, null,
                    Process.myUserHandle());
        }
    }
}
