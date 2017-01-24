package cn.yzz.lol.share;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.yzz.lol.share.View.DrowFive;
import cn.yzz.lol.share.View.DrowLine;
import cn.yzz.lol.share.activity.BaseActivity;
import cn.yzz.lol.share.bean.FiveItem;
import cn.yzz.lol.share.factory.ChatListen;
import cn.yzz.lol.share.factory.DataItem;
import cn.yzz.lol.share.utils.Action;

public class ShareActivity extends BaseActivity implements OnClickListener,
        ChatListen {

    private CatchService.ChatServerIBinder myService = null;
    boolean isrun = false;
    Button bt_stop;
    private ServiceConnection serviceConnection = new ServiceConnection() {

        public void onServiceConnected(ComponentName name, IBinder service) {
            // 获得服务对象
            myService = (CatchService.ChatServerIBinder) IMyService.Stub
                    .asInterface(service);
            myService.setChatListens(ShareActivity.this);
            try {
                SharedPreferences sharedPreferences = getSharedPreferences(Action.sharename, Context.MODE_PRIVATE);
                String code = sharedPreferences.getString(Action.CODE, "2318");
                tv_name.setText(sharedPreferences.getString(Action.NAME, "上证指数"));
                setVise(code);
                myService.answerRingingCall(code);
                myService.getValue(isrun);
            } catch (RemoteException e) {
                e.printStackTrace();
            }

        }

        public void onServiceDisconnected(ComponentName name) {
        }
    };

    void setVise(String code) {
        if (drowFive == null) {
            return;
        }
        if (code.equals("2318")) {
            drowFive.setVisibility(View.GONE);
        } else {
            drowFive.setVisibility(View.VISIBLE);
        }
    }


    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_stop:
                if (myService != null) {
                    try {
                        if (isrun) {
                            isrun = false;
                            bt_stop.setText("START");

                        } else {
                            isrun = true;
                            bt_stop.setText("STOP");
                        }
                        myService.getValue(isrun);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case R.id.bt_set:
                Intent intent = new Intent();
                intent.setClass(ShareActivity.this, SettingActivity.class);
                startActivityForResult(intent, 0);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null && resultCode == RESULT_OK) {
            try {
                if (myService != null) {
                    String code = data.getStringExtra("code");
                    setVise(code);
                    myService.answerRingingCall(code);
                    isrun = true;
                    bt_stop.setText("STOP");
                    myService.getValue(isrun);
                }

            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    protected void onPause() {
        super.onPause();

//        if (myService != null) {
//            try {
//                myService.getValue(false);
//            } catch (RemoteException e) {
//                e.printStackTrace();
//            }
//        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (myService != null) {
            try {
                myService.getValue(isrun);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        unbindService(serviceConnection);

    }

    //    ListView listView;
//    ListSharesAdapter adapter;
//    List<ViewItemData> datas = new ArrayList<ViewItemData>();
    TextView tv_price, tv_1, tv_name;
    DrowLine drowLine;
    DrowFive drowFive;
    LinearLayout linear;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        // 隐去标题栏（程序的名字）
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.gupiao_layout);
//        listView = (ListView) findViewById(R.id.listview);
//
//        adapter = new ListSharesAdapter(this, datas);
//        listView.setAdapter(adapter);,tv_1
        tv_price = (TextView) findViewById(R.id.tv_price);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_1 = (TextView) findViewById(R.id.tv_1);
        linear = (LinearLayout) findViewById(R.id.linear);
        drowLine = (DrowLine) findViewById(R.id.drowLine);
        drowFive = (DrowFive) findViewById(R.id.drowFive);
        // 绑定AIDL服务
        Intent intent = new Intent();
        intent.setClass(ShareActivity.this, CatchService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        startService(intent);
        bt_stop = (Button) findViewById(R.id.bt_stop);
        bt_stop.setOnClickListener(this);
        findViewById(R.id.bt_set).setOnClickListener(this);
    }

    @Override
    public void connectSuccess(Object object) {
        // TODO Auto-generated method stub
//        datas.clear();
        if (object instanceof DataItem) {
//            datas.addAll((Collection<? extends ViewItemData>) object);
//            adapter.notifyDataSetChanged();
            DataItem dataItem = (DataItem) object;
            drowLine.setDataItem(dataItem);

            if (dataItem.getStockname() == null) {
                return;
            }
            if (dataItem.getUpdownrate().indexOf("-") > -1) {
                linear.setBackgroundColor(Color.argb(200, 4, 160, 77));
            } else {
                linear.setBackgroundColor(Color.argb(255, 253, 120, 120));
            }
            tv_price.setText(dataItem.getNowv() + "\t \t" + dataItem.getUpdownrate());
            tv_1.setText("震幅:" + dataItem.getAmplitude() + "    " + dataItem.getTurnoverrate() + "\n" + dataItem.getLitotalvolumetrade() + "\t内盘" + dataItem.getInnervol() + " " + dataItem.getOutervol());
            tv_name.setText(dataItem.getStockname());
            drowLine.invalidate(true);

        } else if (object instanceof FiveItem) {
            FiveItem fiveItem = (FiveItem) object;
            drowFive.setFiveItem(fiveItem);
            drowFive.invalidate(false);
        }

    }

    @Override
    public void vibrator(boolean object) {
        if (object) {
//            setVibrator(ShareActivity.this);
        } else {
//            vibrator.cancel();
        }
    }
}
