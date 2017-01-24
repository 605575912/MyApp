package com.support.loader.packet;

import android.content.Context;
import android.graphics.Bitmap;

import com.support.loader.utils.ImageUtils;


public class ImageOptions {
	private int stubImage;
	private Bitmap stubImageBitmap, imageOnFailBitmap;
	private int imageForEmptyUri;
	private int imageOnFail = 0;
	Context context;

	public ImageOptions(Context context, int stubImage) {
		this.context = context.getApplicationContext();
		this.stubImage = stubImage;
	}

	public int getStubImage() {
		return stubImage;
	}

	public void setStubImage(int stubImage) {
		this.stubImage = stubImage;
	}

	public int getImageForEmptyUri() {
		return imageForEmptyUri;
	}

	public void setImageForEmptyUri(int imageForEmptyUri) {
		this.imageForEmptyUri = imageForEmptyUri;
	}

	public void setImageOnFail(int imageOnFail) {
		this.imageOnFail = imageOnFail;
	}

	public Bitmap getStubImageBitmap() {
		if (stubImageBitmap == null) {
			try {
				// stubImageBitmap = BitmapFactory.decodeResource(
				// context.getResources(), stubImage);
				stubImageBitmap = ImageUtils.getimage(context.getResources(),
						stubImage, 800, 800);
			} catch (Exception e) {
				// TODO: handle exception
			}

		}
		return stubImageBitmap;
	}

	public Bitmap getImageOnFail() {
		if (imageOnFailBitmap == null && imageOnFail != 0) {
			try {
				// imageOnFailBitmap = BitmapFactory.decodeResource(
				// context.getResources(), imageOnFail);
				imageOnFailBitmap = ImageUtils.getimage(context.getResources(),
						imageOnFail, 800, 800);
			} catch (Exception e) {
				// TODO: handle exception

			}
		}
		if (imageOnFailBitmap == null) {
			return getStubImageBitmap();
		}

		return imageOnFailBitmap;
	}
}
