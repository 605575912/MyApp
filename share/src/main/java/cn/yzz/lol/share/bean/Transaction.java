package cn.yzz.lol.share.bean;

/**
 * Created by liangzhenxiong on 15/11/13.
 */
public class Transaction {
    String time;
    String vol;
    String price;

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getVol() {
        return vol;
    }

    public void setVol(String vol) {
        this.vol = vol;
    }
}
