package com.support.loader.utils;

import com.support.loader.proguard.IProguard;

public class ImageSize implements IProguard {
	private final int width;
	private final int height;

	public ImageSize(int width, int height) {
		this.width = width;
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
	
	
	
}
