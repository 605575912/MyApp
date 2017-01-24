package cn.yzz.lol.share.factory;

import cn.yzz.lol.share.bean.Timedata;

/**
 * Created by liangzhenxiong on 15/11/7.
 */
public class DataItem {

    String stockname;
    Timedata[] timedata;
    String nowv;//现价17.16
    String highp;//最高+17.35
     String lowp;//最低+16.64
     String openp;//今开+16.67
     String preclose;//昨收16.62
     String turnoverrate;//换手率
     String updownrate;//+3.25%
     String amplitude;//+3.25% 震幅
     String updown; //涨跌+0.54
     String innervol; //涨跌+0.54
     String outervol; //涨跌+0.54
     String litotalvolumetrade; //成交量
//    String litotalvolumetrade; //涨跌+0.54

    public String getAmplitude() {
        return amplitude;
    }

    public void setAmplitude(String amplitude) {
        this.amplitude = amplitude;
    }

    public String getHighp() {
        return highp;
    }

    public void setHighp(String highp) {
        this.highp = highp.replace("+", "");
        this.highp = this.highp.replace("-", "");
    }

    public String getInnervol() {
        return innervol;
    }

    public void setInnervol(String innervol) {
        this.innervol = innervol;
    }

    public String getOutervol() {
        return outervol;
    }

    public void setOutervol(String outervol) {
        this.outervol = outervol;
    }

    public String getLowp() {
        return lowp;
    }

    public void setLowp(String lowp) {

        this.lowp = lowp.replace("+", "");
    }

    public String getNowv() {
        return nowv;
    }

    public void setNowv(String nowv) {
        this.nowv = nowv;
    }

    public String getOpenp() {
        return openp;
    }

    public void setOpenp(String openp) {
        this.openp = openp;
    }

    public String getPreclose() {
        return preclose;
    }


    public void setPreclose(String preclose) {
        this.preclose = preclose;
    }

    public String getTurnoverrate() {
        return turnoverrate;
    }

    public void setTurnoverrate(String turnoverrate) {
        this.turnoverrate = turnoverrate;
    }

    public String getUpdown() {
        return updown;
    }

    public void setUpdown(String updown) {
        this.updown = updown;
    }

    public String getUpdownrate() {
        return updownrate;
    }

    public void setUpdownrate(String updownrate) {
        this.updownrate = updownrate;
    }

    public Timedata[] getTimedata() {
        return timedata;
    }

    public void setTimedata(Timedata[] timedata) {
        this.timedata = timedata;
    }

    public String getStockname() {
        return stockname;
    }

    public void setStockname(String stockname) {
        this.stockname = stockname;
    }

    public String getLitotalvolumetrade() {
        return litotalvolumetrade;
    }

    public void setLitotalvolumetrade(String litotalvolumetrade) {
        this.litotalvolumetrade = litotalvolumetrade;
    }
}
