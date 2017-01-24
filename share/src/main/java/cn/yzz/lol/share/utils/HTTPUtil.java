package cn.yzz.lol.share.utils;


import com.support.loader.utils.UploadUtil;

public class HTTPUtil {
    static UploadUtil uploadUtil;

    static String getString(String urlString, String PHPSESSID) {
        if (uploadUtil == null) {
            uploadUtil = new UploadUtil();
        }

        String relust = uploadUtil.getString(urlString, "");
        return relust;


    }

    public static String getString(String urlString) {
        return getString(urlString, "");
    }


}
