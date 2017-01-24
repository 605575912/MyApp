package cn.yzz.lol.share.packet;

import android.content.Context;
import android.content.SharedPreferences;

import com.support.loader.packet.TaskPacket;

import cn.yzz.lol.share.ShareApplication;
import cn.yzz.lol.share.bean.CodeItem;
import cn.yzz.lol.share.db.CodeItemHelper;
import cn.yzz.lol.share.utils.Action;

/**
 * Created by liangzhenxiong on 15/11/15.
 */
public class AddCodePacket extends TaskPacket {
    String code = "";
    String name = "";

    public AddCodePacket(String code, String name) {
        this.code = code;
        this.name = name;
    }

    @Override
    public void handle() {
        super.handle();
        SharedPreferences sharedPreferences = ShareApplication.application.getSharedPreferences(Action.sharename, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Action.CODE, code);
        editor.putString(Action.NAME, name);
        editor.commit();
        CodeItemHelper codeItemHelper = new CodeItemHelper(ShareApplication.application);
        CodeItem codeItem = new CodeItem();
        codeItem.setCode(code);
        codeItem.setName(name);
        codeItemHelper.AddMessage(codeItem);
    }
}
