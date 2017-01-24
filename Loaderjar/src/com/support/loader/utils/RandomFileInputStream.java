package com.support.loader.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;

public class RandomFileInputStream extends FileInputStream {
	long	mLastMark = 0L;
	RandomAccessFile	mRandomAccessFile ;
	
	public RandomFileInputStream(RandomAccessFile rf) throws IOException {
	    super(rf.getFD());
	    mRandomAccessFile = rf;
	}
	@Override
	public void close() throws IOException {
	    super.close();
	    mRandomAccessFile.close();
	}

	@Override
	public void mark(int readlimit) {
	    super.mark(readlimit);
	    try {
		mLastMark = mRandomAccessFile.getFilePointer();
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	}

	@Override
	public boolean markSupported() {
	    return true;
	}

	@Override
	public synchronized void reset() throws IOException {
	    mRandomAccessFile.seek(mLastMark);
	}
}
