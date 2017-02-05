package com.lzxmy.demo;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;

import com.library.uiframe.BaseActivity;
import com.lzxmy.demo.Algorithm.AlgorithmActivity;
import com.lzxmy.demo.DynamicLoad.DynamicLoadMainActivity;
import com.lzxmy.demo.Jumping.JumMainActivity;
import com.lzxmy.demo.allswithbutton.SwithButtonMainActivity;
import com.lzxmy.demo.animo.AnimoActivity;
import com.lzxmy.demo.cirelist.CirleListActivity;
import com.lzxmy.demo.crop.CropMainActivity;
import com.lzxmy.demo.damp.DampActivity;
import com.lzxmy.demo.draglistview.DragListViewActivity;
import com.lzxmy.demo.drawArc.ArcActivity;
import com.lzxmy.demo.drawstart.StartActivity;
import com.lzxmy.demo.drawwelcome.MainActivity58;
import com.lzxmy.demo.foldingmenu.FoldingMenuActivity;
import com.lzxmy.demo.imgheadlistview.IMGHeaderActivity;
import com.lzxmy.demo.iosswitch.IosSwitchActivity;
import com.lzxmy.demo.jswebview.AdvertisementActivity;
import com.lzxmy.demo.layer.LayerDrawableActivity;
import com.lzxmy.demo.listtitleview.ListTitleActivity;
import com.lzxmy.demo.marquee.DaggerAppComponentFactory;
import com.lzxmy.demo.marquee.HandlerFactory;
import com.lzxmy.demo.marquee.MarqueeFactory;
import com.lzxmy.demo.marquee.SingleFactory;
import com.lzxmy.demo.marquee.ViewInitFactory;
import com.lzxmy.demo.marquee.ViewOnTouchEventFactory;
import com.lzxmy.demo.marquee.ViewStubFactory;
import com.lzxmy.demo.marquee.ViewToPngFactory;
import com.lzxmy.demo.matrix.MatrixAcitivity;
import com.lzxmy.demo.multiaction.MultiActionActivity;
import com.lzxmy.demo.musicplay.MusciPlayActivity;
import com.lzxmy.demo.newInstance.NewInstanceFactory;
import com.lzxmy.demo.pobu.StaggeredGridActivity;
import com.lzxmy.demo.progress.ProgressActivity;
import com.lzxmy.demo.progressbutton.ProgressMainActivity;
import com.lzxmy.demo.shapedrawable.ShapeMainActivity;
import com.lzxmy.demo.signature.SignatureActivity;
import com.lzxmy.demo.slidingTab.SlidingTabActivity;
import com.lzxmy.demo.suspension.SuspensionActivity;
import com.lzxmy.demo.swithbutton.SwithActivity;
import com.lzxmy.demo.tag.LimitSelectedFragment;
import com.lzxmy.demo.waterdrop.WaterDropActivity;
import com.lzxmy.demo.wireframe.WireFrameActivity;
import com.support.loader.adapter.ItemData;
import com.support.loader.adapter.UIListAdapter;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import dalvik.system.DexClassLoader;

public class MainActivity extends BaseActivity {
    ListView lv_list;
    UIListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 隐去电池等图标和一切修饰部分（状态栏部分）
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // 隐去标题栏（程序的名字）
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            // Translucent status bar
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // Translucent navigation bar
//            window.setFlags(
//                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
//                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//            iskitkat = true;
        }
        setContentView(R.layout.activity_main);
        // CatchHandler.getInstance().init(getApplicationContext());
        lv_list = (ListView) findViewById(R.id.lv_list);
//        LayoutAnimationController controller = new LayoutAnimationController(animation);
//        //设置控件显示的顺序；
//
//        controller.setOrder(LayoutAnimationController.ORDER_REVERSE);
//
//        //设置控件显示间隔时间；
//
//        controller.setDelay(2000);
//
//        Calendar cal = Calendar.getInstance();
//        long s = 1453080209412l;
//        cal.setTimeInMillis(s);
//        int day = cal.get(Calendar.HOUR_OF_DAY);
//        Log.i("TAG", day + "===" + System.currentTimeMillis());
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        try {
//            Date date = simpleDateFormat.parse("2016-01-18 12:00:00");
//            Log.i("TAG", "date" + date.getTime());
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        Log.i("TAG", "===" + simpleDateFormat.format(new Date(s)));

        List<ItemData> datas = new ArrayList<ItemData>();
        datas.add(new ChooseItem("侧滑移动Item删除", 0));
        datas.add(new ChooseItem("仿QQ5.1侧滑底部菜单", 1));
        datas.add(new ChooseItem("折叠菜单菜单", 2));
        datas.add(new ChooseItem("圆形ListView", 3));
//        datas.add(new ChooseItem("仿IOS设置页面", 4));
        datas.add(new ChooseItem("拖曳删除Item", 5));
        datas.add(new ChooseItem("tag", 6));
        datas.add(new ChooseItem("自定义View", 7));
        datas.add(new ChooseItem("mp3播放", 8));
//        datas.add(new ChooseItem("扫描本地图片", 9));
        datas.add(new ChooseItem("ListView 头部图片拉大", 10));
        datas.add(new ChooseItem("Martix 应用", 11));
        datas.add(new ChooseItem("AIDL应用", 12));
        datas.add(new ChooseItem("ListView仿QQ好友列表分段滑动", 13));
        datas.add(new ChooseItem("下拉放大图片", 14));
        datas.add(new ChooseItem("图片叠层", 15));
        datas.add(new ChooseItem("画五角星", 16));
        datas.add(new ChooseItem("MainActivity58", 17));
        datas.add(new ChooseItem("ArcActivity", 18));
        datas.add(new ChooseItem("按钮SwithActivity", 19));
        datas.add(new ChooseItem("跳动的文字", 20));
        datas.add(new ChooseItem("签名", 21));
        datas.add(new ChooseItem("IOS设置按钮SwithActivity", 22));
        datas.add(new ChooseItem("文本部分变色可点击", 23));
        datas.add(new ChooseItem("滚动条", 24));
        datas.add(new ChooseItem("滚动条", 25));
        datas.add(new ChooseItem("截图", 26));
        datas.add(new ChooseItem("shape应用", 27));
        datas.add(new ChooseItem("按钮动画", 28));
        datas.add(new ChooseItem("开关按钮", 29));
        datas.add(new ChooseItem("常用算法", 30));
        datas.add(new ChooseItem("未安装的APK", 31));
        datas.add(new ChooseItem("JS交互的WebView", 32));
        datas.add(new ChooseItem("RecyclerView", 33));
        datas.add(new ChooseItem("DropDownListViewDemo", 34));
        datas.add(new ChooseItem("DropDownListViewDemo", 35));
        datas.add(new ChooseItem("仿微信图片全屏", 36));
        datas.add(new ChooseItem("瀑布流", 38));
        datas.add(new ChooseItem("流畅动画", 39));
        datas.add(new ChooseItem("反射", 40, NewInstanceFactory.class.getName()));
        datas.add(new ChooseItem("TextView 渐影", 41, MarqueeFactory.class.getName()));
        datas.add(new ChooseItem("View布局层次优化", 41, ViewStubFactory.class.getName()));
        datas.add(new ChooseItem("View自定义", 41, ViewInitFactory.class.getName()));
        datas.add(new ChooseItem("Views事件分发", 41, ViewOnTouchEventFactory.class.getName()));
        datas.add(new ChooseItem("依赖注入", 41, DaggerAppComponentFactory.class.getName()));
        datas.add(new ChooseItem("View 后台生成图片", 41, ViewToPngFactory.class.getName()));
        datas.add(new ChooseItem("自定义 Looper", 41, HandlerFactory.class.getName()));
        datas.add(new ChooseItem("各种单例", 41, SingleFactory.class.getName()));


//        Typeface typeFace = Typeface.createFromAsset(getAssets(),
//                "fonts/SchmottoPlotto.ttf");
        adapter = new UIListAdapter(MainActivity.this, datas);
        lv_list.setAdapter(adapter);
        lv_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // TODO Auto-generated method stub
                ChooseItem chooseItem = (ChooseItem) arg0.getAdapter().getItem(
                        arg2);
                Intent intent = new Intent();
                if (!TextUtils.isEmpty(chooseItem.classname)) {
                    intent = PoxyActivity.startIntent(MainActivity.this, chooseItem.classname);
                    startActivity(intent);
                    return;
                }

                switch (chooseItem.getTag()) {
                    case 0: {// 侧滑移动Item删除
                        intent.setClass(MainActivity.this,
                                DragListViewActivity.class);
                        startActivity(intent);

                    }
                    break;
                    case 1: {// 仿QQ5.1侧滑底部菜单
                        intent.setClass(
                                MainActivity.this,
                                com.lzxmy.demo.slidingpane.SlidingPaneActivity.class);
                        startActivity(intent);

                    }
                    break;
                    case 2: {// 折叠菜单菜单
                        intent.setClass(MainActivity.this,
                                FoldingMenuActivity.class);
                        startActivity(intent);

                    }
                    break;
                    case 3: {// 圆形ListView
                        intent.setClass(MainActivity.this, CirleListActivity.class);
                        startActivity(intent);

                    }
                    break;
                    case 4: {// 仿IOS设置页面
//                        intent.setClass(MainActivity.this, SettingActivity.class);
//                        startActivity(intent);

                    }
                    break;
                    case 5: {// 拖曳删除Item
                        intent.setClass(MainActivity.this, WaterDropActivity.class);
                        startActivity(intent);

                    }
                    break;
                    case 6: {// 微信左右滑动页面
                        intent.setClass(MainActivity.this, LimitSelectedFragment.class);
                        startActivity(intent);

                    }
                    break;
                    case 7: {// 自定义View
                        intent.setClass(MainActivity.this, WireFrameActivity.class);
                        startActivity(intent);

                    }
                    break;
                    case 8: {// mp3播放
                        intent.setClass(MainActivity.this, MusciPlayActivity.class);
                        startActivity(intent);

                    }
                    break;
                    case 9: {// 扫描本地图片
//                        intent.setClass(MainActivity.this,
//                                LocationImageActivity.class);
//                        startActivity(intent);

                    }
                    break;
                    case 10: {// ListView 头部图片拉大

                        intent.setClass(MainActivity.this, IMGHeaderActivity.class);
                        startActivity(intent);

                    }
                    break;
                    case 11: {// Martix 应用

                        intent.setClass(MainActivity.this, MatrixAcitivity.class);
                        startActivity(intent);
                    }
                    break;
                    case 12: {// AIDL 应用

//                        intent.setClass(MainActivity.this, AidlActivity.class);
//                        startActivity(intent);

                    }
                    break;
                    case 13: {// ListView仿QQ好友列表分段滑动

                        intent.setClass(MainActivity.this, ListTitleActivity.class);
                        startActivity(intent);

                    }
                    break;
                    case 14: {// 下拉放大图片 应用

                        intent.setClass(MainActivity.this, DampActivity.class);
                        startActivity(intent);

                    }
                    break;
                    case 15: {// 图片叠层

                        intent.setClass(MainActivity.this, LayerDrawableActivity.class);
                        startActivity(intent);

                    }
                    break;
                    case 16: {// 图片叠层

                        intent.setClass(MainActivity.this, StartActivity.class);
                        startActivity(intent);

                    }
                    break;
                    case 17: {// MainActivity58

                        intent.setClass(MainActivity.this, MainActivity58.class);
                        startActivity(intent);

                    }
                    break;
                    case 18: {// 图片叠层

                        intent.setClass(MainActivity.this, ArcActivity.class);
                        startActivity(intent);

                    }
                    break;
                    case 19: {// 图片叠层

                        intent.setClass(MainActivity.this, SwithActivity.class);
                        startActivity(intent);

                    }
                    break;
                    case 20: {// 图片叠层

                        intent.setClass(MainActivity.this, JumMainActivity.class);
                        startActivity(intent);

                    }
                    break;
                    case 21: {// 图片叠层

                        intent.setClass(MainActivity.this, SignatureActivity.class);
                        startActivity(intent);

                    }
                    break;
                    case 22: {// 图片叠层

                        intent.setClass(MainActivity.this, IosSwitchActivity.class);
                        startActivity(intent);

                    }
                    break;
                    case 23: {// 图片叠层

                        intent.setClass(MainActivity.this, MultiActionActivity.class);
                        startActivity(intent);

                    }
                    break;

                    case 24: {// 图片叠层

                        intent.setClass(MainActivity.this, ProgressActivity.class);
                        startActivity(intent);

                    }
                    break;
                    case 25: {// 图片叠层

                        intent.setClass(MainActivity.this, SlidingTabActivity.class);
                        startActivity(intent);

                    }
                    break;
                    case 26: {// 截图

                        intent.setClass(MainActivity.this, CropMainActivity.class);
                        startActivity(intent);

                    }
                    break;
                    case 27: {// 截图

                        intent.setClass(MainActivity.this, ShapeMainActivity.class);
                        startActivity(intent);

                    }
                    break;
                    case 28: {// 截图

                        intent.setClass(MainActivity.this, ProgressMainActivity.class);
                        startActivity(intent);

                    }
                    break;
                    case 29: {// 截图

                        intent.setClass(MainActivity.this, SwithButtonMainActivity.class);
                        startActivity(intent);

                    }
                    break;
                    case 30: {// 截图

                        intent.setClass(MainActivity.this, AlgorithmActivity.class);
                        startActivity(intent);

                    }
                    break;
                    case 31: {// 截图

                        intent.setClass(MainActivity.this, DynamicLoadMainActivity.class);
                        startActivity(intent);

//                        loadUninstallApk();

//                        Bundle paramBundle = new Bundle();
//                        paramBundle.putBoolean("KEY_START_FROM_OTHER_ACTIVITY", true);
//                        String dexpath = "/mnt/sdcard/XSW/classes.dex";
//                        String dexoutputpath = "/mnt/sdcard/";
//                        File dexOutputDir = getDir("dex", 0);
//                        LoadAPK(paramBundle, dexpath, dexOutputDir.getPath());
                    }
                    break;
                    case 32: {// 截图
                        intent.putExtra("url", "file:///android_asset/booking-class.html");
                        intent.setClass(MainActivity.this, AdvertisementActivity.class);
                        startActivity(intent);


                    }
                    break;
                    case 33: {//
                        intent.setClass(MainActivity.this, com.lzxmy.demo.recelyview.MainActivity.class);
                        startActivity(intent);


                    }
                    break;
                    case 34: {//
                        intent.setClass(MainActivity.this, com.lzxmy.demo.drawg.DropDownListViewDemo.class);
                        startActivity(intent);


                    }
                    break;
                    case 35: {//
                        intent.setClass(MainActivity.this, SuspensionActivity.class);
                        startActivity(intent);


                    }
                    break;
                    case 36: {//
//                        intent.setClass(MainActivity.this, CameraActivity.class);
//                        startActivity(intent);

                    }
                    break;
                    case 37: {//

                    }
                    break;
                    case 38: {//
                        intent.setClass(MainActivity.this, StaggeredGridActivity.class);
                        startActivity(intent);

                    }
                    break;
                    case 39: {//
                        intent.setClass(MainActivity.this, AnimoActivity.class);
                        startActivity(intent);

                    }
                    break;

                    default:
                        break;
                }
            }
        });


    }

    public void LoadAPK(Bundle paramBundle, String dexpath, String dexoutputpath) {
        ClassLoader localClassLoader = ClassLoader.getSystemClassLoader();
        DexClassLoader localDexClassLoader = new DexClassLoader(dexpath,
                dexoutputpath, null, localClassLoader);
        try {
            PackageInfo plocalObject = getPackageManager()
                    .getPackageArchiveInfo(dexpath, 1);

            if ((plocalObject.activities != null)
                    && (plocalObject.activities.length > 0)) {
                String activityname = plocalObject.activities[0].name;
//                Log.d(TAG, "activityname = " + activityname);

                Class localClass = localDexClassLoader.loadClass(activityname);
                Constructor localConstructor = localClass
                        .getConstructor(new Class[]{});
                Object instance = localConstructor.newInstance(new Object[]{});
//                Log.d(TAG, "instance = " + instance);

                Method localMethodSetActivity = localClass.getDeclaredMethod(
                        "setActivity", new Class[]{Activity.class});
                localMethodSetActivity.setAccessible(true);
                localMethodSetActivity.invoke(instance, new Object[]{this});

                Method methodonCreate = localClass.getDeclaredMethod(
                        "onCreate", new Class[]{Bundle.class});
                methodonCreate.setAccessible(true);
                methodonCreate.invoke(instance, new Object[]{paramBundle});
            }
            return;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * @return void
     * @throws
     * @Title: loadUninstallApk
     * @Description: 动态加载未安装的apk
     */
    private void loadUninstallApk() {
        String path = Environment.getExternalStorageDirectory() + File.separator + "XSW/";
        Log.i("TAG", "Path" + path);
        String filename = "hello-jni-debug.apk";

        // 4.1以后不能够将optimizedDirectory设置到sd卡目录， 否则抛出异常.
        File optimizedDirectoryFile = getDir("dex", 0);
        DexClassLoader classLoader = new DexClassLoader(path + filename, optimizedDirectoryFile.getAbsolutePath(),
                null, getClassLoader());

        try {
            // 通过反射机制调用， 包名为com.example.loaduninstallapkdemo, 类名为UninstallApkActivity
            String pkgName = "com.example.hellojni";
            Class mLoadClass = classLoader.loadClass(pkgName + ".HelloJni");
            Constructor constructor = mLoadClass.getConstructor(new Class[]{});
            Object testActivity = constructor.newInstance(new Object[]{});

            // 获取sayHello方法
//            Method helloMethod = mLoadClass.getMethod("sayHello", null);
//            helloMethod.setAccessible(true);
//            Object content = helloMethod.invoke(testActivity, null);
//            Toast.makeText(JarDexActivity.this, content.toString(), Toast.LENGTH_LONG).show();
            Class cls = getClassLoader().loadClass(pkgName + ".HelloJni");
            // 跳转到该Activity
            startActivity(new Intent(this, cls));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
