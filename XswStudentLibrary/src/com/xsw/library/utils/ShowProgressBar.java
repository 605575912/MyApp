package com.xsw.library.utils;

import com.xsw.library.R;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

/**
 * 显示对话框 created by Bear at 2015-1-5 上午11:51:46 TODO
 */
public class ShowProgressBar {
	static Dialog dialog;
	static AnimationDrawable animationDrawable;

	public static void showDiolog(Context context, String text) {
		show(context, text);
	}

	public static void removeDiolog() {
		cancelDiolog();
	}

	static void show(Context context, String text) {
		ShowProgressBar.removeDiolog();
		dialog = new Dialog(context, R.style.theme_dialog_alert);
		dialog.setContentView(R.layout.loading);
		dialog.setCancelable(true);
		TextView dialogtext = (TextView) dialog.findViewById(R.id.dialogtext);
		dialogtext.setText(text);
		try {
			dialog.show();
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	static void cancelDiolog() {
		if (dialog != null) {
			if (dialog.isShowing()) {
				try {
					dialog.cancel();
				} catch (Exception e) {
					// TODO: handle exception
				}

			}

			if (animationDrawable != null) {
				if (animationDrawable.isRunning()) {
					animationDrawable.stop();
				}
			}
		}

	}

	// public static void showTitleDialog(Context context, String text) {
	// showTitleDialog(context, "提示", text, null, null);
	//
	// }

	/**
	 * 单按钮对话框
	 * 
	 * @param context
	 * @param text
	 * @return
	 */
	public static void showTitleDialog(Context context, String text, String ok,
			String cancel, final DialogHandler handler, final Object object) {
		ShowProgressBar.removeDiolog();
		if (context == null) {
			return;
		}
		dialog = new Dialog(context, R.style.theme_dialog_alert);
		dialog.setContentView(R.layout.alert_dialog);
		dialog.getWindow().setLayout(
				android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				android.view.ViewGroup.LayoutParams.MATCH_PARENT);

		// dialogWindow.setGravity(Gravity.CENTER_HORIZONTAL);

		// WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		// DisplayMetrics dm = new DisplayMetrics();
		// context.getWindowManager().getDefaultDisplay().getMetrics(dm);
		// lp.width = dm.widthPixels;
		// dialogWindow.setAttributes(lp);

		dialog.setCancelable(true);
		TextView messages = (TextView) dialog.findViewById(R.id.messages);
		Button bt_ok = (Button) dialog.findViewById(R.id.bt_ok);
		Button bt_cancel = (Button) dialog.findViewById(R.id.bt_cancel);
		bt_ok.setText(ok);
		bt_cancel.setText(cancel);
		bt_ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				if (handler != null) {
					handler.confirmButton(dialog, object);
				}
			}
		});
		bt_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				if (handler != null) {
					handler.cancelButton(dialog, object);
				}
			}
		});
		messages.setText(text);
		try {
			dialog.show();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	/**
	 * 单按钮对话框
	 * 
	 * @param context
	 * @param text
	 * @return
	 */
	public static void showDialog(Context context, String text, String ok,
			String cancel, final DialogHandler handler) {
		ShowProgressBar.removeDiolog();
		if (context == null) {
			return;
		}
		dialog = new Dialog(context, R.style.theme_dialog_alert);
		dialog.setContentView(R.layout.alert_blue_dialog);
		dialog.getWindow().setLayout(
				android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				android.view.ViewGroup.LayoutParams.MATCH_PARENT);

		// dialogWindow.setGravity(Gravity.CENTER_HORIZONTAL);

		// WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		// DisplayMetrics dm = new DisplayMetrics();
		// context.getWindowManager().getDefaultDisplay().getMetrics(dm);
		// lp.width = dm.widthPixels;
		// dialogWindow.setAttributes(lp);

		dialog.setCancelable(true);
		TextView messages = (TextView) dialog.findViewById(R.id.messages);
		Button bt_ok = (Button) dialog.findViewById(R.id.bt_ok);
		Button bt_cancel = (Button) dialog.findViewById(R.id.bt_cancel);
		bt_ok.setText(ok);
		bt_cancel.setText(cancel);
		bt_ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				if (handler != null) {
					handler.confirmButton(dialog, null);
				}
			}
		});
		bt_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				if (handler != null) {
					handler.cancelButton(dialog, null);
				}
			}
		});
		messages.setText(text);
		try {
			dialog.show();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	/**
	 * 单按钮对话框
	 * 
	 * @param context
	 * @param text
	 * @return
	 */
	public static void showTitleDialog(Context context, String text, String ok,
			final DialogHandler handler, final Object object) {
		ShowProgressBar.removeDiolog();
		if (context == null) {
			return;
		}
		dialog = new Dialog(context, R.style.theme_dialog_alert);
		dialog.setContentView(R.layout.alert_ok_dialog);
		dialog.getWindow().setLayout(
				android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				android.view.ViewGroup.LayoutParams.MATCH_PARENT);
		dialog.setCancelable(true);
		TextView messages = (TextView) dialog.findViewById(R.id.messages);
		Button bt_ok = (Button) dialog.findViewById(R.id.bt_ok);
		bt_ok.setText(ok);
		bt_ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				if (handler != null) {
					handler.confirmButton(dialog, object);
				}
			}
		});
		messages.setText(text);
		try {
			dialog.show();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	/**
	 * 单按钮对话框
	 * 

	 * @return
	 */
	public static void dimssDialog() {
		if (dialog != null && dialog.isShowing()) {


			try {
				dialog.dismiss();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 单按钮对话框
	 * 
	 * @param context
	 * @param text
	 * @return
	 */
	public static void showToastDialog(Context context, String text) {
		dialog = new Dialog(context, R.style.DialogTheme);
		dialog.setContentView(R.layout.toast_dialog);
		dialog.setCancelable(true);
		TextView messages = (TextView) dialog.findViewById(R.id.messages);
		messages.setText(text);
		try {
			dialog.show();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public interface DialogHandler {

		void confirmButton(Dialog dialog, final Object object);

		void cancelButton(Dialog dialog, final Object object);

	}

}
