package com.support.loader.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DisturbCodeOutputStream extends OutputStream {
//    private static final String TAG = DisturbCodeOutputStream.class.getSimpleName();
    public static final byte [] DISTURB_CODE = {0x00,0x4D,0x49,0x43,0x00};
    private OutputStream mOrigOutputStream ;
    private boolean	mIsInjected = false;
    public DisturbCodeOutputStream(OutputStream os){
	mOrigOutputStream = os;
	mIsInjected = false;
    }
    private void injectDisturbCode() throws IOException{
	if (mIsInjected)
	    return ;
	mOrigOutputStream.write(DISTURB_CODE);
	mIsInjected = true;
    }
    
    public static int getDisturbCodeLength(){
	return DISTURB_CODE.length;
    }
    
    public static boolean isDisturbCodeMatched(byte [] code){
	if (code == null || code.length < DISTURB_CODE.length){
	    return false;
	}
	final int N = DISTURB_CODE.length;
	for (int k = 0; k < N; k ++){
	    if (code[k] != DISTURB_CODE[k])
		return false;
	}
	return true;
    }
    
    public static boolean isDisturbCodeMatched(InputStream is){
	byte [] header = new byte[DISTURB_CODE.length];
	int count;
	boolean mark_support = is.markSupported();
	try {
	    if (mark_support){
		is.mark(0);
	    }
	    count = is.read(header);
	    if (count < header.length) {
		return false;
	    }
	    return isDisturbCodeMatched(header);
	} catch (IOException e) {
	    e.printStackTrace();
	    return false;
	} finally{
	    if (mark_support){
		IoUtils.resetQuietly(is);
	    }
	}
    }
    
    @Override
    public void write(int arg0) throws IOException {
	injectDisturbCode();
	mOrigOutputStream.write(arg0);
    }
    @Override
    public void write(byte[] buffer, int offset, int count) throws IOException {
	injectDisturbCode();
	mOrigOutputStream.write(buffer, offset, count);
    }
    @Override
    public void write(byte[] buffer) throws IOException {
	injectDisturbCode();
	mOrigOutputStream.write(buffer);
    }
    @Override
    public void close() throws IOException {
	mOrigOutputStream.close();
	super.close();
    }
    @Override
    public void flush() throws IOException {
	mOrigOutputStream.flush();
	super.flush();
    }
}
