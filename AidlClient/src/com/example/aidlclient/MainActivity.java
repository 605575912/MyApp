package com.example.aidlclient;

import android.app.Activity;
import android.content.ComponentName;
import android.os.Bundle;
import android.os.RemoteException;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.auto.launcher.Bean;
import com.auto.launcher.ConnectListener;
import com.auto.launcher.IShareService;
import com.auto.launcher.ServiceInit;


public class MainActivity extends Activity {

    TextView text;
    Button button;
    IShareService iShareService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        text = (TextView) findViewById(R.id.text);
        button = (Button) findViewById(R.id.button);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (iShareService == null) {



                    ServiceInit.start(getApplicationContext(), new ConnectListener() {
                        @Override
                        public void onServiceConnected(ComponentName componentName, final IShareService iShareService) {

                            try {
//                    text.setText(iShareService.getBean().get(0).getStr() + "====");
                                MainActivity.this.iShareService = iShareService;
//                    iShareService.registerServiceReceiver();
                                Bean bean = new Bean();
                                bean.setStr("com.example.aidlclient.ACService");
                                iShareService.addBean(bean);
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onMessage(String message) {
                            text.setText(message);
                        }
                    });
//                } else {
//                    try {
////                    text.setText(iShareService.getBean().get(0).getStr() + "====");
////                    iShareService.registerServiceReceiver();
//                        Bean bean = new Bean();
//                        bean.setStr("com.example.aidlclient.ACService");
//                        iShareService.addBean(bean);
//                    } catch (RemoteException e) {
//                        e.printStackTrace();
//                    }
//                }
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ServiceInit.stop(this);
    }
}
