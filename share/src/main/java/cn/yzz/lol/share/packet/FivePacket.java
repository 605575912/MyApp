package cn.yzz.lol.share.packet;

import android.os.Handler;
import android.os.Message;

import com.support.loader.packet.TaskPacket;
import com.support.loader.utils.JsonUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import cn.yzz.lol.share.bean.FiveItem;
import cn.yzz.lol.share.bean.Transaction;
import cn.yzz.lol.share.utils.HTTPUtil;
import cn.yzz.lol.share.utils.ShareUtil;

/**
 * Created by liangzhenxiong on 15/11/6.
 */
public class FivePacket extends TaskPacket {
    Handler handler;
    int what = 1;
    String code = "";
    FiveItem fiveItem;

    public void setHandler(Handler handler, int what, String code) {
        this.handler = handler;
        this.what = what;
        this.code = code;
    }

    public void setFiveItem(FiveItem fiveItem) {
        this.fiveItem = fiveItem;
    }

    @Override
    public void handle() {
        if (handler != null) {

//            Log.i("TAG","=====http://image.sinajs.cn/newchart/min/n/sz002296.png");886

//            String string = HTTPUtil.getString("http://hq.niuguwang.com/aquote/quotedata/stockshare.ashx?code=804&s=meizu&version=3.3.5&packtype=1");
            String string = HTTPUtil.getString("http://hq.niuguwang.com/aquote/quotedata/detailfivedish.ashx?code=" + code + "&s=meizu&version=3.3.5&packtype=1");
            try {
                string = new String(string.getBytes("ISO-8859-1"), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            if (fiveItem == null) {
                fiveItem = new FiveItem();

            }
            Message message = handler.obtainMessage(what);
            message.arg1 = 0;
            if (string != null && !string.equals("")) {
                try {
                    JSONObject jsonObject = new JSONObject(string);
                    JsonUtils.getobject(fiveItem, jsonObject);
                    if (ShareUtil.gethighcode().equals(code)) {


                        String high = fiveItem.getAsk5p().replace("+", "");
                        high = high.replace("-", "");
                        float fhigh = Float.valueOf(high);
                        if (ShareUtil.getHighprice() <= fhigh && ShareUtil.getHighprice() > 0) {

                            message.arg1 = 1;
                        }
                        String low = fiveItem.getBid5p().replace("+", "");
                        low = low.replace("-", "");
                        float flow = Float.valueOf(low);
                        if (ShareUtil.getLowprice() >= flow) {

                            message.arg1 = 1;
                        }
                    }
                    if (jsonObject.has("transaction")) {
                        JSONArray ti = jsonObject.getJSONArray("transaction");
                        Transaction[] transactions = new Transaction[ti.length()];
                        for (int i = 0; i < ti.length(); i++) {
                            Transaction transaction = new Transaction();
                            JsonUtils.getobject(transaction, ti.getJSONObject(i));
                            transactions[ti.length() - i - 1] = transaction;

                        }
                        fiveItem.setTransactions(transactions);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            message.obj = fiveItem;

            handler.sendMessage(message);
        }

    }

    @Override
    public void stop() {

    }
}
