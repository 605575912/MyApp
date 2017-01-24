package com.lzxmy.demo.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lzxmy.demo.R;


/**
 * 显示统一的Toast created by Bear at 2015-1-7 下午4:03:35 TODO
 */
public class ShowToast {
	static Toast toast;

	/**
	 * 显示统一的Toast created by Bear at 2015-1-7 下午4:03:19 TODO
	 */
	public static void showTips(Context context, String text) {
		if (text == null | text.equals("")) {
			return;
		}
		if (toast == null) {
			toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
		}
		toast.setText(text);
		toast.show();
	}

	/**
	 * 显示统一的Toast created by Bear at 2015-1-7 下午4:03:19 TODO
	 */
	public static void cancelTips() {
		if (toast != null) {
			toast.cancel();
		}
	}

	/**
	 * 显示的Toast 2015-1-27 @author lzx
	 * 
	 */
	public static void showTipsView(int drawableid, Context context, String text) {
		if (text == null | text.equals("")) {
			return;
		}
		// if (toast == null) {
		Toast toast = new Toast(context);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.setDuration(Toast.LENGTH_SHORT);
		View view = LayoutInflater.from(context).inflate(R.layout.toast_dialog,
				null);
		ImageView timage = (ImageView) view.findViewById(R.id.timage);
		timage.setImageResource(drawableid);
		TextView messages = (TextView) view.findViewById(R.id.messages);
		messages.setText(text);
		toast.setView(view);

		// }
		toast.show();
	}
	/**
	 * 显示的Toast 2015-1-27 @author lzx
	 *
	 */
//	public static void showTopTips(Context context, String text) {
//		if (text == null | text.equals("")) {
//			return;
//		}
//		DrawerToast	drawerToast = DrawerToast.getInstance(context);
//
//		//入场动画
//		//旋转
//		RotateAnimation rAnim = new RotateAnimation(0, 720, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
//		rAnim.setDuration(500);
//		rAnim.setFillAfter(true);
//		//缩放
//		ScaleAnimation sAnim = new ScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
//		sAnim.setDuration(500);
//		sAnim.setFillAfter(true);
//		//透明度
//		AlphaAnimation aAnim1 = new AlphaAnimation(0, 1);
//		aAnim1.setDuration(500);
//		aAnim1.setFillAfter(true);
//		AnimationSet startAnim = new AnimationSet(false);
//		startAnim.addAnimation(rAnim);
//		startAnim.addAnimation(aAnim1);
//		startAnim.addAnimation(sAnim);
//
//		//离场动画
////				ScaleAnimation sAnim = new ScaleAnimation(1,0,1,0);
////				sAnim.setDuration(500);
////				sAnim.setFillAfter(true);
//		//移动
//		TranslateAnimation animTrans = new TranslateAnimation(	Animation.RELATIVE_TO_PARENT, 0f,
//				Animation.RELATIVE_TO_PARENT, 0f,
//				Animation.RELATIVE_TO_SELF, 0f,
//				Animation.RELATIVE_TO_SELF, 1f);
//		animTrans.setDuration(500);
//		animTrans.setFillAfter(true);
//		//透明度
//		AlphaAnimation aAnim2 = new AlphaAnimation(1, 0);
//		aAnim2.setDuration(500);
//		aAnim2.setFillAfter(true);
//		AnimationSet endAnim = new AnimationSet(false);
//		endAnim.addAnimation(animTrans);
//		endAnim.addAnimation(aAnim2);
//
//		drawerToast.show(text, null, startAnim, endAnim);
//	}
}
