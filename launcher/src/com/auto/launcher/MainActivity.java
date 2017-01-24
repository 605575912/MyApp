package com.auto.launcher;

import android.app.Activity;
import android.content.ComponentName;
import android.os.Bundle;


public class MainActivity extends Activity {

//    TextView text;
//    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        text = (TextView) findViewById(R.id.text);
//        button = (Button) findViewById(R.id.button);
        ServiceInit.start(this, new ConnectListener() {
            @Override
            public void onServiceConnected(ComponentName componentName, final IShareService iShareService) {

//                button.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        try {
//                            iShareService.getBean();
//                        } catch (RemoteException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });
            }


        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ServiceInit.stop(this);
    }
}
