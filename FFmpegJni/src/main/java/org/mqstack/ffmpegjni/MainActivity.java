package org.mqstack.ffmpegjni;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;

/**
 * Created by mqstack on 2015/11/23.
 */
public class MainActivity extends Activity implements OnItemClickListener {

    FFmpegJni ffmpegJni = null;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private String[] texts;
    private String[] commands;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ffmpegJni = new FFmpegJni();
        listView = (ListView) findViewById(R.id.orderList);
        texts = getResources().getStringArray(R.array.texts);
        commands = getResources().getStringArray(R.array.commands);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, texts);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position,
                            long id) {


        new Thread() {
            @Override
            public void run() {
                super.run();
                String testCommand = commands[position];

                File file = new File("/storage/emulated/0/a.mp4");
                if (!file.exists()) {
                    Log.d("testCommand", testCommand);
                    return;
                }
                long i = System.currentTimeMillis();
                int result = -1;
                if (position == 5) {
                    result = ffmpegJni.ImageToVideo("/storage/emulated/0/ffmpeg/a1.mp4", "/storage/emulated/0/ffmpeg/image%d.jpg");
                } else {
                    result = ffmpegJni.ffmpegRunCommand(testCommand);
                }
                if (result == 0) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "Cut success", Toast.LENGTH_LONG).show();
                        }
                    });

                    Log.i("FFmpegJni", " success");
                    Log.i("FFmpegJni", "command time " + (System.currentTimeMillis() - i));

                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "Cut failed", Toast.LENGTH_LONG).show();
                        }
                    });

                    Log.i("FFmpegJni", "Cut failed");
                }
            }
        }.start();

    }
}
