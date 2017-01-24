package com.support.loader.utils;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;

/**
 * 动态申请权限
 * Created by liangzhenxiong on 16/2/16.
 */
public class PermissionActivity extends Activity {
    final static int REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ArrayList<String> newPermissions = getIntent().getStringArrayListExtra("newPermissions");
        if (newPermissions == null) {
            finish();
            return;
        }
        handle(newPermissions);
    }

    private void handle(ArrayList<String> newPermissions) {
        for (String permission : newPermissions) {
            boolean already_request = ActivityCompat.shouldShowRequestPermissionRationale(PermissionActivity.this, permission);
            if (already_request) {
                PermissionListener.showWhy(newPermissions);
                finish();
                return;
            }
        }

        ActivityCompat.requestPermissions(PermissionActivity.this,
                newPermissions.toArray(new String[newPermissions.size()]),
                REQUEST_CODE);
    }

    public interface PermissionListener {
        void allow();//权限允许

        void decline();//权限拒绝

        void showWhy(ArrayList<String> permissions);//权限已经被拒绝，弹窗解析,第一次弹窗被拒绝
    }

    private static PermissionListener PermissionListener;

    public static boolean ShowRequestPermission(Context context, String[] permissions, PermissionListener permissionListener) {
        PermissionListener = null;
        if (permissionListener == null) {
            return false;
        } else {
            if (permissions == null || permissions.length == 0) {
                permissionListener.allow();
                return true;
            }
            int targetSDKVer = 0; //只有目标SDK大于等于23的才会有授权
            if (android.os.Build.VERSION.SDK_INT >= 4) {
                targetSDKVer = context.getApplicationInfo().targetSdkVersion;
            }
            if (targetSDKVer >= 23 && Build.VERSION.SDK_INT >= 23) {

                ArrayList<String> newPermissions = showPermission(context, permissions);
                if (newPermissions.size() == 0) {
                    permissionListener.allow();
                } else {
                    Intent intent = new Intent();
                    PermissionListener = permissionListener;
                    intent.putStringArrayListExtra("newPermissions", newPermissions);
                    intent.setClass(context, PermissionActivity.class);
                    context.startActivity(intent);
                }
                return true;
            } else {
                PackageManager packageManager = context.getPackageManager();
                String packageName = context.getPackageName();
                final int permissionCount = permissions.length;
                for (int i = 0; i < permissionCount; i++) {
                    int grant = packageManager.checkPermission(
                            permissions[i], packageName);
                    if (grant == PackageManager.PERMISSION_DENIED) {
                        permissionListener.decline();
                        return true;
                    }
                }
                permissionListener.allow();
                return true;
            }
        }
    }


    private static ArrayList<String> showPermission(Context context, String[] permissions) {
        ArrayList<String> newPermissions = new ArrayList<>(permissions.length);
        for (String permission : permissions) {
            int PERMISSION_GRANTED = ContextCompat.
                    checkSelfPermission(context, permission);
            if (PackageManager.PERMISSION_DENIED == PERMISSION_GRANTED) {
                newPermissions.add(permission);
            }
        }
        return newPermissions;

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PermissionListener = null;
    }

    @Override
    @SuppressLint("NewApi")
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (REQUEST_CODE == requestCode) {
            if (grantResults.length > 0) {
                for (int grant : grantResults) {
                    if (grant != PackageManager.PERMISSION_GRANTED) {
                        if (PermissionListener != null) {
                            PermissionListener.decline();
                            PermissionListener = null;
                            break;
                        }
                    }
                }
                if (PermissionListener != null) {
                    PermissionListener.allow();
                }
            }
        }
        PermissionListener = null;
        finish();
    }
}
