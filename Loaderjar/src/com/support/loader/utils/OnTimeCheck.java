package com.support.loader.utils;

import android.view.View;

/**
 * 防止二次点击 2015-4-24 @author lzx
 * 
 */
public class OnTimeCheck {
	static long time = 0;
	static int hashcode = 0;
	static int viewid = 0;

	/**
	 * 防止二次点击 2015-4-24 @author lzx
	 * 
	 */
	public static boolean onclick(View view) {
		if (hashcode == view.hashCode()) {
			if (System.currentTimeMillis() - time > 500) {
				time = System.currentTimeMillis();
				return true;
			} else {
				return false;
			}

		} else {
			hashcode = view.hashCode();
			time = System.currentTimeMillis();
		}
		return true;
	}

	/**
	 * 防止二次点击 2015-4-24 @author lzx
	 * 
	 */

	public static boolean onclick(int id) {
		if (viewid == id) {
			if (System.currentTimeMillis() - time > 1000) {
				time = System.currentTimeMillis();
				return true;
			} else {
				return false;
			}

		} else {
			viewid = id;
			time = System.currentTimeMillis();
		}
		return true;
	}

}
