package com.lzx.hb;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.Enumeration;


public class MainActivity extends Activity {

    Button button2;
    boolean islock;
    Button button;
    PowerManager pManager;
    String TAG = "LOCK_LZX";
    PowerManager.WakeLock mWakeLock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        http://p.i139.cn/mm/l/?s=1&src=5410287501
        Uri uri = Uri.parse("http://p.i139.cn/mm/l/?s=1&src=5410287501");
       String host =  uri.getHost();
       String Authority =  uri.getAuthority();
       String getPath =  uri.getPath();
        final SharedPreferences sharedPreferences = getSharedPreferences("hb_lzx", Context.MODE_PRIVATE);
        pManager = ((PowerManager) getSystemService(POWER_SERVICE));
        mWakeLock = pManager.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, TAG);
        button = (Button) findViewById(R.id.button);
        button2 = (Button) findViewById(R.id.button2);
        if (QiangHongBaoService.isRunning()) {
            button.setText("已经开启辅助功能服务");
//            openAccessibilityServiceSettings();
        } else {
            button.setText("点击开启辅助功能服务");

        }


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!QiangHongBaoService.isRunning()) {
                    openAccessibilityServiceSettings();
                } else {
                    button.setText("已经开启辅助功能服务");

                    Intent intent  = new Intent();
                    intent.setAction("android.net.conn.Discon");
                    sendBroadcast(intent);
                    Intent vpnIntent = new Intent();
                    vpnIntent.setAction("android.net.vpn.SETTINGS");
                    vpnIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(vpnIntent);
//                        Toast.makeText(MainActivity.this, "don't onclik", Toast.LENGTH_LONG).show();
                }
            }
        });
//String CODENAME = Build.VERSION.CODENAME;
//String RELEASE = Build.VERSION.RELEASE;
//int PREVIEW_SDK_INT = Build.VERSION.PREVIEW_SDK_INT;
        islock = sharedPreferences.getBoolean("islock", false);

        lockstatue(islock);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (islock) {
                    islock = false;
                } else {
                    islock = true;
                }
                lockstatue(islock);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("islock", islock);
                editor.commit();
            }
        });
    }

    void lockstatue(boolean islock) {
        if (islock) {
            button2.setText("已经禁止锁屏");
            if (mWakeLock != null) {
                mWakeLock.acquire();
            }
        } else {
            button2.setText("开启禁止锁屏");
            if (mWakeLock != null) {
                if (mWakeLock.isHeld()) {
                    mWakeLock.release();
                }
            }
        }
    }
//    private BroadcastReceiver qhbConnectReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            if(isFinishing()) {
//                return;
//            }
//            String action = intent.getAction();
//            Log.d("MainActivity", "receive-->" + action);
//            if(Config.ACTION_QIANGHONGBAO_SERVICE_CONNECT.equals(action)) {
//                if (mTipsDialog != null) {
//                    mTipsDialog.dismiss();
//                }
//            } else if(Config.ACTION_QIANGHONGBAO_SERVICE_DISCONNECT.equals(action)) {
//                showOpenAccessibilityServiceDialog();
//            }
//        }
//    };
public static boolean isVpnUsed() {
    try {
        Enumeration<NetworkInterface> niList = NetworkInterface.getNetworkInterfaces();
        Log.i("TAG","==="+niList);
        if(niList != null) {
            for (NetworkInterface intf : Collections.list(niList)) {
                if(!intf.isUp() || intf.getInterfaceAddresses().size() == 0) {
                    continue;
                }
                Log.i("TAG", "isVpnUsed() NetworkInterface Name: " + intf.getName());
                if ("tun0".equals(intf.getName()) || "ppp0".equals(intf.getName())){
                    return true; // The VPN is up
                }
            }
        }
    } catch (SocketException e) {
        Log.i("TAG","==="+e.toString());
        e.printStackTrace();
    }
    return false;
}
    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        try {
//            unregisterReceiver(qhbConnectReceiver);
//        } catch (Exception e) {}

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem item = menu.add(0, 0, 0, R.string.open_service_button);
        item.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_NEVER);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == 0) {
            openAccessibilityServiceSettings();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * 打开辅助服务的设置
     */
    private void openAccessibilityServiceSettings() {
        try {
            Intent intent = new Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS);
            startActivity(intent);
            Toast.makeText(this, R.string.tips, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
