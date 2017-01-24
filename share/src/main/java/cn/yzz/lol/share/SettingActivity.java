package cn.yzz.lol.share;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.support.loader.ServiceLoader;
import com.support.loader.adapter.UIListAdapter;
import com.support.loader.adapter.ViewItemData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import cn.yzz.lol.share.activity.BaseActivity;
import cn.yzz.lol.share.bean.CodeItem;
import cn.yzz.lol.share.db.CodeItemHelper;
import cn.yzz.lol.share.packet.AddCodePacket;

/**
 * Created by liangzhenxiong on 15/11/10.
 */
public class SettingActivity extends BaseActivity {
    EditText editText;
    Button bt_add, bt_back, bt_addcode;
    ListView listView;
    LinearLayout linear_add;
    List<ViewItemData> lists = new ArrayList<ViewItemData>();
    UIListAdapter adapter;
    CodeItemHelper codeItemHelper;

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        if (msg.what == 1) {
            lists.addAll((Collection<? extends ViewItemData>) msg.obj);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.share_itme_view);
        editText = (EditText) findViewById(R.id.editText);
        bt_add = (Button) findViewById(R.id.bt_add);
        bt_back = (Button) findViewById(R.id.bt_back);
        bt_addcode = (Button) findViewById(R.id.bt_addcode);
        listView = (ListView) findViewById(R.id.listView);
        linear_add = (LinearLayout) findViewById(R.id.linear_add);
        adapter = new UIListAdapter(SettingActivity.this, lists);
        listView.setAdapter(adapter);
        bt_back.setOnClickListener(this);
        bt_add.setOnClickListener(this);
        bt_addcode.setOnClickListener(this);
        ServiceLoader.getInstance().submit(new Runnable() {
            @Override
            public void run() {
                codeItemHelper = new CodeItemHelper(SettingActivity.this);
                ArrayList<CodeItem> list = codeItemHelper.SelectMessage("10");
                if (list == null || list.isEmpty()) {
                    list = new ArrayList<CodeItem>();



                } else {

                }
                list.add(getCodeItem("启明信息","740"));
                list.add(getCodeItem("上证指数", "2318"));
                list.add(getCodeItem("陕西煤业", "2560"));
                list.add(getCodeItem("长江传媒", "3373"));
                list.add(getCodeItem("大启明", "947"));
                list.add(getCodeItem("比亚迪", "1102"));

                Message message = handler.obtainMessage(1);
                message.obj = list;
                handler.sendMessage(message);
            }
        });
    }

    CodeItem getCodeItem(String name, String code) {
        CodeItem codeItem2 = new CodeItem();
        codeItem2.setCode(code);
        codeItem2.setName(name);
        return codeItem2;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.bt_add) {
            if (!editText.getText().toString().equals("")) {
                Intent intent = new Intent();
                intent.putExtra("code", editText.getText().toString());
                AddCodePacket addCodePacket = new AddCodePacket(editText.getText().toString(), "");
                ServiceLoader.getInstance().sendPacket(addCodePacket);
                setResult(RESULT_OK, intent);
            }
            finish();
        } else if (v.getId() == R.id.bt_back) {
            finish();
        } else if (v.getId() == R.id.bt_addcode) {
            if (linear_add.getVisibility() == View.VISIBLE) {
                linear_add.setVisibility(View.GONE);
            } else {
                linear_add.setVisibility(View.VISIBLE);
            }

        }
    }
}
