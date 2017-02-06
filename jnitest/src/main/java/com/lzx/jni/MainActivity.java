package com.lzx.jni;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
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

//                        String outputurl = folderurl + "/sintel.yuv";
                        String outputurl = folderurl + "/sintel1.mp4";
//                        Do ds = new Do();
                        //ffmpeg -ss 2 -i /sdcard/XXX.mp4 -t 3 -vcodec copy -acodec copy /sdcard/YYY.mp4
                        ffmpegRunCommand("ffmpeg -ss 2 -i " + urltext_input + " -t 3 -vcodec copy -acodec copy " + outputurl);
//                        ds.decode(inputurl, outputurl);
//        textview.setText(ds.getstringfromC(1, new String[]{"ffmpeg -i /storage/emulated/0/a.mp4 -ss 0 -t 10 -vcodec copy -acodec copy -y /sdcard/Y0.mp4"}));
//                ds.stream(inputurl,"rtsp://10.9.112.211:8086");
//                        ds.intream();
                    }
                }.start();

            }
        });

    }

    public int ffmpegRunCommand(String command) {
        if (command.isEmpty()) {
            return 1;
        }
        String[] args = command.split("###");
        for (int i = 0; i < args.length; i++) {
            Log.d("ffmpeg-jni", args[i]);

        }
        Do ds = new Do();
        ds.getstringfromC(args.length, args);
        return 0;
//        return run(args.length, args);
    }
}
