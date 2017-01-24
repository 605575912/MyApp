package cn.yzz.lol.share.bean;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.support.loader.adapter.Holder;
import com.support.loader.adapter.UIListAdapter;
import com.support.loader.adapter.ViewItemData;

import cn.yzz.lol.share.R;
import cn.yzz.lol.share.activity.SettingMusicActivity;
import cn.yzz.lol.share.factory.CodeViewHolder;
import cn.yzz.lol.share.utils.Action;

/**
 * Created by liangzhenxiong on 15/11/10.
 */
public class CodeItem   implements ViewItemData, View.OnClickListener {
    String id;
    String name;
    String code;
    Activity activity;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        if (id == null) {
            id = System.currentTimeMillis() + "code";
        }
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public View getView(LayoutInflater listContainer, int position, View convertView, ViewGroup arg2) {
        convertView = listContainer.inflate(R.layout.code_item, null);
        return convertView;
    }

    @Override
    public void updateView(UIListAdapter listImageAdapter, Holder vHolder, int position, View convertView) {
        CodeViewHolder codeViewHolder = (CodeViewHolder) vHolder;
        codeViewHolder.tv_code.setText(code);
        codeViewHolder.tv_name.setText(name);
        convertView.setOnClickListener(this);
        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("code", code);
                intent.putExtra("name", name);
                intent.setClass(activity, SettingMusicActivity.class);
                activity.startActivityForResult(intent, 0);
                return false;
            }
        });
    }



    @Override
    public Holder setHolder(Activity activity, Holder vHolder, View convertView) {
        this.activity = activity;
        if (vHolder == null) {
            CodeViewHolder codeViewHolder = new CodeViewHolder();
            codeViewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            codeViewHolder.tv_code = (TextView) convertView.findViewById(R.id.tv_code);
            return codeViewHolder;
        }
        return vHolder;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        intent.putExtra("code", code);
        activity.setResult(Activity.RESULT_OK, intent);
        activity.finish();
        SharedPreferences sharedPreferences = activity.getSharedPreferences(Action.sharename, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Action.CODE, code);
        editor.commit();
    }
}
