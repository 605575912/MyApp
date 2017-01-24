package com.zx.zpush;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.aidlserver.R;

import org.teleal.cling.android.AndroidUpnpService;
import org.teleal.cling.controlpoint.ActionCallback;
import org.teleal.cling.model.action.ActionInvocation;
import org.teleal.cling.model.message.UpnpResponse;
import org.teleal.cling.model.meta.Device;
import org.teleal.cling.model.meta.LocalDevice;
import org.teleal.cling.model.meta.RemoteDevice;
import org.teleal.cling.model.meta.Service;
import org.teleal.cling.model.types.ServiceId;
import org.teleal.cling.model.types.UDAServiceId;
import org.teleal.cling.registry.DefaultRegistryListener;
import org.teleal.cling.registry.Registry;
import org.teleal.cling.registry.RegistryListener;
import org.teleal.cling.support.avtransport.callback.Play;
import org.teleal.cling.support.avtransport.callback.SetAVTransportURI;
import org.teleal.cling.support.connectionmanager.callback.GetProtocolInfo;
import org.teleal.cling.support.connectionmanager.callback.PrepareForConnection;
import org.teleal.cling.support.model.ProtocolInfos;


public class MainActivity extends Activity {

    private String s = "AVTransport";
    private String s1 = "ConnectionManager";
//	ProtocolInfo sink;

    private Dialog listdialog;
    private Button btn, search;
    private ListView devicelist;
    private ArrayAdapter<DeviceDisplay> listAdapter;

    private AndroidUpnpService upnpService;

    private RegistryListener registryListener = new BrowseRegistryListener();
    String mIp;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {

            upnpService = (AndroidUpnpService) service;

            // Refresh the list with all known devices
            listAdapter.clear();
            for (Device device : upnpService.getRegistry().getDevices()) {
                ((BrowseRegistryListener) registryListener).deviceAdded(device);
            }
            // Getting ready for future device advertisements
            upnpService.getRegistry().addListener(registryListener);
            // Search asynchronously for all devices
            upnpService.getControlPoint().search();
        }

        public void onServiceDisconnected(ComponentName className) {
            upnpService = null;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mIp = getIpAddr(this);
        btn = (Button) findViewById(R.id.start_btn);

        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (upnpService != null) {
                    upnpService.getRegistry().removeAllRemoteDevices();
                    upnpService.getControlPoint().search();
                }
                showDialog();
            }
        });

//		devicelist = (ListView) findViewById(R.id.devicelist);
//		listAdapter = new ArrayAdapter<DeviceDisplay>( this, android.R.layout.simple_list_item_1 );
//	    devicelist.setAdapter(listAdapter);
//	    
//	    devicelist.setOnItemClickListener(new OnItemClickListener(){
//	    	public void onItemClick(AdapterView<?> parent, View v,
//					int position, long id) {
//	    		Toast.makeText(getApplicationContext(), "����ת����"+position+"���",  Toast.LENGTH_SHORT).show();
//	    		DeviceDisplay devicePlay=listAdapter.getItem(position);
//	    		Device device= devicePlay.getDevice();
//	    		String url="http://home.ustc.edu.cn/~zx1064/messi.mp4";
////	    		String url="http://tv.sohu.com/20131219/n392032143.shtml";
////	    		String url="http://p.you.video.sina.com.cn/swf/quotePlayer20130723_V4_4_42_4.swf?vid=122142766&as=0";
////	    		String url="http://freedownloads.last.fm/download/533190694/more%2Bthan%2Ba%2Bdime.mp3";
////	    		GetInfo(device);
//	    		executeAVTransportURI(device,url);
//	    		executePlay(device);
//
//	    	}
//	    });
//
//	        getApplicationContext().bindService(
//	            new Intent(this, MyUpnpService.class),
//	            serviceConnection,
//	            Context.BIND_AUTO_CREATE
//	        );
    }

    String localIp = null;
    Device device;

    public void showDialog() {
        AlertDialog.Builder builder = new Builder(this);
        builder.setTitle("��ѡ���豸����");
        final LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.listview, null);

        getApplicationContext().bindService(
                new Intent(this, MyUpnpService.class),
                serviceConnection,
                Context.BIND_AUTO_CREATE
        );
        devicelist = (ListView) v.findViewById(R.id.devicelist);
        listAdapter = new ArrayAdapter<DeviceDisplay>(this, android.R.layout.simple_list_item_1);
        devicelist.setAdapter(listAdapter);

        builder.setView(v);
        builder.setNegativeButton("ȡ��", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("����", new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
//				if (upnpService != null) { 
//			        upnpService.getRegistry().removeAllRemoteDevices(); 
//			        upnpService.getControlPoint().search(); 
//			    } 
            }
        });
        listdialog = builder.create();
        listdialog.show();
        WifiManager var10 = (WifiManager) MainActivity.this.getSystemService(Context.WIFI_SERVICE);
        if (var10 != null) {
            WifiInfo var11 = var10.getConnectionInfo();
            int var13 = var11.getIpAddress();
            localIp = String.format("%d.%d.%d.%d", new Object[]{Integer.valueOf(var13 & 255),
                    Integer.valueOf(var13 >> 8 & 255), Integer.valueOf(var13 >> 16 & 255), Integer.valueOf(var13 >> 24 & 255)});
        }
        Log.i("TAG", "localIp====" + localIp);
        devicelist.setOnItemClickListener(new OnItemClickListener() {
                                              public void onItemClick(AdapterView<?> parent, View v,
                                                                      int position, long id) {
                                                  DeviceDisplay devicePlay = listAdapter.getItem(position);
                                                  device = devicePlay.getDevice();

//                                                  WifiManager var10 = (WifiManager) MainActivity.this.getSystemService(Context.WIFI_SERVICE);
//                                                  if (var10 != null) {
//                                                      WifiInfo var11 = var10.getConnectionInfo();
//                                                      int var13 = var11.getIpAddress();
//                                                      localIp = String.format("%d.%d.%d.%d", new Object[]{Integer.valueOf(var13 & 255),
//                                                              Integer.valueOf(var13 >> 8 & 255), Integer.valueOf(var13 >> 16 & 255), Integer.valueOf(var13 >> 24 & 255)});
//                                                  }


//	    		String url="http://stream2.ahtv.cn/ahws/cd/live.m3u8";
//                String url = Environment.getExternalStorageDirectory().getAbsolutePath() + "/2.jpg";
//                File file = new File(url);
//                if (file.exists()) {
//                    url = addShareFile(url);
//                    Log.i("TAG", "====");
//                }
//	    		String url="http://192.168.1.128:49153/media/L3N0b3JhZ2UvZW11bGF0ZWQvMC9EQ0lNL0NhbWVyYS9JTUdfMjAxNzAxMDVfMTUyNDQwLmpwZw==.jpg";
//                    String url = "http://www.iteye.com/upload/logo/user/119839/be2a0506-1f02-350a-91cb-a0f52958db7a.jpg?1246157583";

//	    		String url="http://video19.ifeng.com/video07/2013/11/11/281708-102-007-1138.mp4";
//                                                  Uri.parse(url);


                                                  listdialog.dismiss();
                                                  handler.sendEmptyMessage(0);

                                              }
                                          }

        );
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
//            String url = "http://www.51edu.com/uploadfile/2014/0822/20140822104359264.jpg";
//            final String url = "http://" + localIp + ":4477/a.jpg";
//            final String url = "rtsp://" + localIp + ":8086/im.mp4";
            final String url = "http://" + localIp + ":1394/281708.mp4";
//            final String url = "http://192.168.1.131:49152/media/L3N0b3JhZ2UvZW11bGF0ZWQvMC8yODE3MDgtMTAyLTAwNy0xMTM4Lm1wNA==.mp4";
            Log.i("TAG", "====" + url);
//            GetInfo(device);
            PrepareConn(device);
            executeAVTransportURI(device, url);
            executePlay(device);
//            handler.sendEmptyMessageDelayed(0, 10000);
        }
    };




    public static String getIpAddr(Context context) {
        String str = null;
        if (context != null) {
            WifiManager wm = (WifiManager) context.getSystemService("wifi");
            if (wm != null) {
                int temp = wm.getConnectionInfo().getIpAddress();
                str = intToIp(temp);
            } else {
                Log.e("NetUtil", "getIpAddr WifiManager is null");
            }
        } else {
            Log.e("NetUtil", "getIpAddr context is null");
        }

        Log.d("NetUtil", "getIpAddr ip:" + str);
        return str;
    }

    public static String intToIp(int hostip) {
        return (hostip & 255) + "." + (hostip >> 8 & 255) + "." + (hostip >> 16 & 255) + "." + (hostip >> 24 & 255);
    }

//    public String addShareFile(String filePath) {
//        if (filePath != null && filePath != "" && this.mIp != null && this.mIp != "" && mPort > 0) {
//            String filePathTemp = filePath.toLowerCase();
//            if (!filePathTemp.startsWith("http://") && !filePathTemp.startsWith("rtsp://")) {
//                File f = new File(filePath);
//                if (f.exists() && f.canRead()) {
//                    String extension = this.getExtensionName(filePath);
//                    String base64Name = Base64.encodeToString(filePath.getBytes(), 2);
//                    return String.format("http://%s:%d/media/%s.%s", new Object[]{this.mIp, Integer.valueOf(mPort), base64Name, extension});
//                } else {
//                    return null;
//                }
//            } else {
//                return filePath;
//            }
//        } else {
//            return null;
//        }
//    }

    protected void onDestroy() {
        super.onDestroy();
        if (upnpService != null) {
            upnpService.getRegistry().removeListener(registryListener);
        }
        getApplicationContext().unbindService(serviceConnection);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 0, 0, R.string.search)
                .setIcon(android.R.drawable.ic_menu_search);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == 0 && upnpService != null) {
            upnpService.getRegistry().removeAllRemoteDevices();
            upnpService.getControlPoint().search();
        }
        return false;
    }


    class BrowseRegistryListener extends DefaultRegistryListener {

        @Override
        public void remoteDeviceDiscoveryStarted(Registry registry, RemoteDevice device) {
            deviceAdded(device);
        }

        @Override
        public void remoteDeviceDiscoveryFailed(Registry registry, final RemoteDevice device, final Exception ex) {

            deviceRemoved(device);
        }

        @Override
        public void remoteDeviceAdded(Registry registry, RemoteDevice device) {
            deviceAdded(device);
        }

        @Override
        public void remoteDeviceRemoved(Registry registry, RemoteDevice device) {
            deviceRemoved(device);
        }

        @Override
        public void localDeviceAdded(Registry registry, LocalDevice device) {
            deviceAdded(device);
        }

        @Override
        public void localDeviceRemoved(Registry registry, LocalDevice device) {
            deviceRemoved(device);
        }

        public void deviceAdded(final Device device) {
            runOnUiThread(new Runnable() {
                public void run() {
                    DeviceDisplay d = new DeviceDisplay(device);
                    int position = listAdapter.getPosition(d);
                    if (position >= 0) {
                        // Device already in the list, re-set new value at same position
                        listAdapter.remove(d);
                        listAdapter.insert(d, position);
                    } else {
                        listAdapter.add(d);
                    }
//	                listAdapter.sort(DISPLAY_COMPARATOR);
                    listAdapter.notifyDataSetChanged();
                }
            });
        }

        public void deviceRemoved(final Device device) {
            runOnUiThread(new Runnable() {
                public void run() {
                    listAdapter.remove(new DeviceDisplay(device));
                }
            });
        }
    }

    public void executeAVTransportURI(Device device, final String uri) {

        ServiceId AVTransportId = new UDAServiceId(s);
        Service service = device.findService(AVTransportId);
        ActionCallback callback = new SetAVTransportURI(service, uri) {

            @Override
            public void failure(ActionInvocation arg0, UpnpResponse arg1,
                                String arg2) {
                // TODO Auto-generated method stub
                Log.i("TAG", "failed^^^^^^^" + uri);
            }

        };
        upnpService.getControlPoint().execute(callback);

    }

    public void executePlay(Device device) {
        ServiceId AVTransportId = new UDAServiceId(s);
        Service service = device.findService(AVTransportId);
        ActionCallback playcallback = new Play(service) {

            @Override
            public void failure(ActionInvocation arg0, UpnpResponse arg1,
                                String arg2) {
                // TODO Auto-generated method stub
                Log.i("TAG", "failed^^^^^^^");
            }

        };
        upnpService.getControlPoint().execute(playcallback);

    }

    public void GetInfo(Device device) {
        ServiceId AVTransportId = new UDAServiceId(s1);
        Service service = device.findService(AVTransportId);
        ActionCallback getInfocallback = new GetProtocolInfo(service) {

            @Override
            public void received(ActionInvocation actionInvocation,
                                 ProtocolInfos sinkProtocolInfos,
                                 ProtocolInfos sourceProtocolInfos) {
                // TODO Auto-generated method stub
                Log.i("TAG", sinkProtocolInfos.toString());
                Log.i("TAG", sourceProtocolInfos.toString());
            }

            @Override
            public void failure(ActionInvocation arg0, UpnpResponse arg1,
                                String arg2) {
                // TODO Auto-generated method stub
                Log.v("TAG", "failed^^^^^^^");
            }

        };
        upnpService.getControlPoint().execute(getInfocallback);
    }

    public void PrepareConn(Device device) {
        ServiceId AVTransportId = new UDAServiceId(s1);
        Service service = device.findService(AVTransportId);
        ActionCallback prepareConncallback = new PrepareForConnection(service, null, null, -1, null) {

            @Override
            public void received(ActionInvocation invocation, int connectionID,
                                 int rcsID, int avTransportID) {
                // TODO Auto-generated method stub
                Log.v("avTransportID", Integer.toString(avTransportID)+"=="+invocation.getInput());
            }

            @Override
            public void failure(ActionInvocation arg0, UpnpResponse arg1,
                                String arg2) {
                // TODO Auto-generated method stub
                Log.v("PrepareForConnection", "failed^^^^^^^");
            }

        };
        upnpService.getControlPoint().execute(prepareConncallback);
    }


}
