package cn.yzz.lol.share.packet;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;

import com.support.loader.packet.TaskPacket;
import com.support.loader.utils.JsonUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import cn.yzz.lol.share.ShareApplication;
import cn.yzz.lol.share.bean.CodeItem;
import cn.yzz.lol.share.bean.Timedata;
import cn.yzz.lol.share.db.CodeItemHelper;
import cn.yzz.lol.share.factory.DataItem;
import cn.yzz.lol.share.utils.Action;
import cn.yzz.lol.share.utils.HTTPUtil;

/**
 * Created by liangzhenxiong on 15/11/6.
 */
public class SharePacket extends TaskPacket {
    Handler handler;
    int what = 1;
    String code = "";

    public void setHandler(Handler handler, int what, String code) {
        this.handler = handler;
        this.what = what;
        this.code = code;
    }

    DataItem dataItem;


    public void setDataItem(DataItem dataItem) {
        this.dataItem = dataItem;
    }

    @Override
    public void handle() {
        if (handler != null) {

//            Log.i("TAG","=====http://image.sinajs.cn/newchart/min/n/sz002296.png");886

//            String string = HTTPUtil.getString("http://hq.niuguwang.com/aquote/quotedata/stockshare.ashx?code=804&s=meizu&version=3.3.5&packtype=1");
            String string = HTTPUtil.getString("http://hq.niuguwang.com/aquote/quotedata/stockshare.ashx?code=" + code + "&s=meizu&version=3.3.5&packtype=1");
            try {
                string = new String(string.getBytes("ISO-8859-1"), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
//            String[] strings = string.split("var");
//            String regex2 = "\".*?\"";
//            Pattern pa = Pattern.compile(regex2);
//            List<DataItem> list = new ArrayList<DataItem>();
//            for (int i = 0; i < strings.length; i++) {
//                 Matcher ma = pa.matcher(strings[i]);
//                if (ma.find()) {
//                    String body = ma.group();
//                    body =   body.replace("\"","");
//                    String[] datas = body.split(",");
//                    DataItem dataItem = new DataItem();
//                    dataItem.setStrings(datas);
//                    list.add(dataItem);
//                }
//            }
            //http://hq.niuguwang.com/aquote/quotedata/detailfivedish.ashx?code=" + code + "&s=meizu&version=3.3.5&packtype=1
            if (dataItem == null) {
                dataItem = new DataItem();
            }
            if (!string.equals("")) {
                try {
                    JSONObject jsonObject = new JSONObject(string);
                    JsonUtils.getobject(dataItem, jsonObject);
                    if (jsonObject.has("timedata")) {
                        JSONArray ti = jsonObject.getJSONArray("timedata");
                        Timedata[] timedatas = new Timedata[ti.length()];
                        for (int i = 0; i < ti.length(); i++) {
                            Timedata timedata = new Timedata();
                            JsonUtils.getobject(timedata, ti.getJSONObject(i));
                            timedatas[ti.length() - i - 1] = timedata;

                        }
                        dataItem.setTimedata(timedatas);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {

            }
//            list.add(dataItem);
            Message message = handler.obtainMessage(what);
            message.obj = dataItem;
            handler.sendMessage(message);

            SharedPreferences sharedPreferences = ShareApplication.application.getSharedPreferences(Action.sharename, Context.MODE_PRIVATE);
            String codename = sharedPreferences.getString(Action.NAME, "上证指数");
            if (codename.equals("") || !codename.equals(dataItem.getStockname())) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(Action.NAME, dataItem.getStockname());
                editor.apply();
//                editor.commit();
                CodeItemHelper codeItemHelper = new CodeItemHelper(ShareApplication.application);
                CodeItem codeItem = new CodeItem();
                codeItem.setCode(code);
                codeItem.setName(dataItem.getStockname());
                codeItemHelper.AddMessage(codeItem);
            }

        }

    }

    @Override
    public void stop() {

    }
}
