package com.lzx.jni;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {
    TextView textview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);
        textview = (TextView) findViewById(R.id.textview);
        textview.setText("============");
        textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        String folderurl = Environment.getExternalStorageDirectory().getPath();

                        String urltext_input = "sintel.mp4";
                        String inputurl = folderurl + "/" + urltext_input;

                        String outputurl = folderurl + "/sintel.yuv";
                        Do ds = new Do();
//                        ds.decode(inputurl, outputurl);
//        textview.setText(ds.getstringfromC(1, new String[]{"ffmpeg -i /storage/emulated/0/a.mp4 -ss 0 -t 10 -vcodec copy -acodec copy -y /sdcard/Y0.mp4"}));
//                ds.stream(inputurl,"rtsp://10.9.112.211:8086");
                        ds.intream();
                    }
                }.start();

            }
        });

    }
}
