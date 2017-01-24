package com.support.loader.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.util.Log;

import com.support.loader.ServiceLoader;
import com.support.loader.proguard.IProguard;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 文件下载工具类 created by Bear at 2015-1-5 上午11:53:03 TODO
 */
public class DownloadUtil implements IProguard {
    // static final String GET = "GET";
    // static final String POST = "POST";
     static String FILEPATH = null;
    private static DownloadUtil INSTANCE;

    private DownloadUtil() {
    }

    public static DownloadUtil getInstance() {
        if (INSTANCE == null) {
            init(ServiceLoader.getInstance().app);
            INSTANCE = new DownloadUtil();
        }
        return INSTANCE;
    }

    public  String getFILEPATH() {
        return FILEPATH;
    }



    /**
     * 必须初始化 2015-2-11 @author lzx
     */
    static void init(Context context) {
        // 判断是否挂载了SD卡
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            File file = context.getExternalCacheDir();
            if (file != null) {
                FILEPATH = file.getPath();
            } else {
//				FILEPATH = Environment.getExternalStorageDirectory()
//						.getPath();// 存放照片的文件夹
//				FILEPATH = FILEPATH + "/APP/image";
//				File filetemp = new File(DownloadUtil.FILEPATH + "/");
//				if (!filetemp.exists()) {
//					filetemp.mkdir();
//				}
//                file = Environment.getDownloadCacheDirectory();// 存放照片的文件夹

//            if (file != null) {
                FILEPATH = "/sdcard/Blue/App/";
                File dir = new File(FILEPATH);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
//            } else {
//                FILEPATH = file.getPath();
//            }
            }
        } else {

            FILEPATH = context.getCacheDir().getPath();
            Log.i("TAG",FILEPATH+"===");
        }
    }

    public String setPath(String url) {
        if (url == null | FILEPATH == null) {
            return "";
        }
//		if (!Environment.getExternalStorageState().equals(
//				Environment.MEDIA_MOUNTED)
//				| url.equals("")) {
//			return "";
//		}

        // http://pic.shenaili.com/11
        // http://wx.qlogo.cn/mmopen/7WYgjHzFfU6DlzkSfGpKf8anibTfCaHQERpVBgic5XRJ8OByTibP0x5KoAVLDJ2cF0ogXTsXDrA4CNuJLdMo2lafwYI4otj9qGV/0
        // http://192.168.1.222:8081/var/upload/images/20141011/20141011153204_466_400x400.jpg

        String id = generate(url);
//		Log.i("TAG", id);

//		String id = "";
//		int start = url.lastIndexOf("/");
//		int end = url.lastIndexOf(".");
//		if (start > -1 && end > -1 && start < end) {
//			id = url.substring(start + 1, end);
//		} else {
//			int end2 = url.lastIndexOf("/");
//			if (end2 > 0 && start < end2) {
//				id = url.substring(start + 1, end2);
//			} else {
//				if (end2 > 0) {
//					id = url.substring(end2 + 1, url.length());
//				} else {
//					id = url.substring(start + 1, url.length());
//				}
//			}
//		}
        // File saveDir = new File(sdcard + IMAGE_PATH);
        // if (!saveDir.exists()) {
        // saveDir.mkdirs();
        // }
        return FILEPATH + "/" + id + ".jpg";
        // }
        // return "";
    }

    private static final String HASH_ALGORITHM = "MD5";
    private static final int RADIX = 10 + 26; // 10 digits + 26 letters

    public String generate(String imageUri) {
        byte[] md5 = getMD5(imageUri.getBytes());
        BigInteger bi = new BigInteger(md5).abs();
        return bi.toString(RADIX);
    }

    private byte[] getMD5(byte[] data) {
        byte[] hash = null;
        try {
            MessageDigest digest = MessageDigest.getInstance(HASH_ALGORITHM);
            digest.update(data);
            hash = digest.digest();
        } catch (NoSuchAlgorithmException e) {
        }
        return hash;
    }

    /**
     * 下载图片
     */
    public String downimage(String url) {
        String path = null;
        if (FILEPATH == null) {
            return null;
        }
        path = setPath(url);
        File ApkFile = new File(path);
        if (ApkFile.exists()) {
            return path;
        }
        boolean interceptFlag = false;
        URL myFileUrl = null;
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(path);
        } catch (FileNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
            return null;
        }
        try {
            myFileUrl = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HttpURLConnection conn;
        try {
            conn = (HttpURLConnection) myFileUrl.openConnection();
//            HttpsURLConnection.setDefaultSSLSocketFactory();
//            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
//                @Override
//                public boolean verify(String hostname, SSLSession session) {
//                    return true;
//                }
//            });
            conn.setDoInput(true);
            conn.setConnectTimeout(3000);
            conn.setReadTimeout(3000);
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
            return null;
        }


        byte buf[] = new byte[2048];
        int count = 0;
        int time = 0;
        while (time < 3) {
            try {
                time++;
                conn.connect();
                int length = conn.getContentLength();
                InputStream is = conn.getInputStream();
                do {
                    int numread = is.read(buf);
                    count += numread;
                    fos.write(buf, 0, numread);
                    if (numread <= 0) {
                        interceptFlag = true;

                        break;
                    }
                    if (count >= length) {
                        interceptFlag = true;
                        time = 3;
                        break;
                    }

                } while (!interceptFlag);// 点击取消就停止下载.

                interceptFlag = false;
                fos.close();
                is.close();
                return path;
            } catch (Exception e) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                e.printStackTrace();
                return "";
            }
        }

        return path;
    }

    /**
     * 下载语音
     */
    public String downVoice(String url) {
        String path = null;
        if (FILEPATH == null) {
            return null;
        }
        path = setSpxPath(url);

        File ApkFile = new File(path);
        if (ApkFile.exists()) {
            return path;
        }
        boolean interceptFlag = false;
        URL myFileUrl = null;

        try {
            myFileUrl = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            HttpURLConnection conn = (HttpURLConnection) myFileUrl
                    .openConnection();
            conn.setDoInput(true);
            conn.connect();
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            InputStream is = conn.getInputStream();

            FileOutputStream fos = new FileOutputStream(path);
            byte buf[] = new byte[1024];
            do {
                int numread = is.read(buf);
                if (numread <= 0) {
                    interceptFlag = true;
                    break;
                }
                fos.write(buf, 0, numread);
            } while (!interceptFlag);// 点击取消就停止下载.
            interceptFlag = false;
            fos.close();
            is.close();
            return path;
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    public String setSpxPath(String url) {
        if (FILEPATH == null) {
            return "";
        }
        String id = "ti.spx";
        int start = url.lastIndexOf("/");
        int end = url.length();
        id = url.substring(start + 1, end);
        // File saveDir = new File(DownloadUtil.sdcard + VOICE_PATH);
        // if (!saveDir.exists()) {
        // saveDir.mkdirs();
        // }
        // File ApkFile = new File(IMAGE_PATH + "/" + id + ".jpg");
        // if (ApkFile.exists()) {
        return FILEPATH + "/" + id;
        // }
        // return "";
    }

    public String download(String urlStr)

    {

        StringBuffer sb = new StringBuffer();

        String line = null;

        BufferedReader buffer = null;

        try {

            URL url = new URL(urlStr);

            HttpURLConnection urlConn = (HttpURLConnection) url
                    .openConnection();
            urlConn.setConnectTimeout(2000);
            urlConn.setReadTimeout(2000);
            buffer = new BufferedReader(new InputStreamReader(
                    urlConn.getInputStream()));

            while ((line = buffer.readLine()) != null)

            {

                sb.append(line);

            }
            if (urlConn != null) {
                urlConn.disconnect();
            }

        } catch (Exception e) {

            // TODO Auto-generated catch block

            e.printStackTrace();

        } finally {

            try {
                if (buffer != null) {
                    buffer.close();
                }

            } catch (Exception e) {

                e.printStackTrace();

            }

        }

        return sb.toString();

    }

    public boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager
                    .getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }
}
