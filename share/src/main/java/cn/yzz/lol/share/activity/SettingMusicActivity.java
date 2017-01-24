package cn.yzz.lol.share.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import cn.yzz.lol.share.R;
import cn.yzz.lol.share.ShareApplication;
import cn.yzz.lol.share.View.ShSwitchView;
import cn.yzz.lol.share.utils.Action;
import cn.yzz.lol.share.utils.ShareUtil;

/**
 * Created by liangzhenxiong on 15/11/15.
 */
public class SettingMusicActivity extends BaseActivity {
    ShSwitchView switch_view, switch_low;
    String code, name;
    EditText et_high, et_low;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settingmusic_layout);
        switch_view = (ShSwitchView) findViewById(R.id.switch_view);
        switch_low = (ShSwitchView) findViewById(R.id.switch_low);
        et_high = (EditText) findViewById(R.id.et_high);
        et_low = (EditText) findViewById(R.id.et_low);
        if (getIntent().getExtras() != null) {
            code = getIntent().getExtras().getString("code");
            name = getIntent().getExtras().getString("name");
            TextView tv_name = (TextView) findViewById(R.id.tv_name);
            tv_name.setText(name);

        }

//        switch_view.setOnSwitchStateChangeListener();
        findViewById(R.id.bt_set).setOnClickListener(this);
        findViewById(R.id.bt_back).setOnClickListener(this);
        handler.sendEmptyMessageDelayed(1, 1000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        et_high = null;
        et_low = null;
        Log.i("TAG","=======");
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        if (code != null && code.equals(ShareUtil.gethighcode())) {
            if (ShareUtil.getHighprice() > 0) {
                switch_view.setOn(true, true);
//                et_high.setText(String.valueOf(ShareUtil.getHighprice()));
            }
            if (ShareUtil.getLowprice() > 0) {
                switch_low.setOn(true, true);
//                et_low.setText(String.valueOf(ShareUtil.getLowprice()));
            }
        }
    }

    @Override
    public void onClick(View arg0) {
        super.onClick(arg0);
        int id = arg0.getId();
        if (id == R.id.bt_back) {
            finish();
        } else if (id == R.id.bt_set) {
            SharedPreferences sharedPreferences = ShareApplication.application.getSharedPreferences(Action.sharename, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            editor.putString(Action.HighCODE, "");

            ShareUtil.highcode = "";
            ShareUtil.lowprice = 0;
            ShareUtil.highprice = 0;
            if (switch_view.isOn()) {
//                ShareUtil.highprice = Float.valueOf(et_high.getText().toString());
                ShareUtil.highcode = code;
            }
            if (switch_low.isOn()) {
//                ShareUtil.lowprice = Float.valueOf(et_low.getText().toString());
                ShareUtil.highcode = code;
            }
            editor.putString(Action.HighCODE, code);
            editor.putFloat(Action.LOWPRICE, ShareUtil.lowprice);
            editor.putFloat(Action.HighPRICE, ShareUtil.highprice);
            editor.commit();
            finish();

        }
    }
}
