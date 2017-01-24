package com.support.loader.utils;

import java.io.Closeable;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class IoUtils {
    	public static void resetQuietly(InputStream is){
    	    try{
    		is.reset();
    	    }catch(Exception ignored){
    		
    	    }
    	}
    	
    	public static void closeQuietly(Closeable  closeable_obj){
    	    try{
    		closeable_obj.close();
    	    } catch (RuntimeException rethrown) {
                throw rethrown;
            } catch (Exception ignored) {
            }
    	}
    	
//	public static void closeQuietly(InputStream reader){
//		try {
//            reader.close();
//        } catch (RuntimeException rethrown) {
//            throw rethrown;
//        } catch (Exception ignored) {
//        }
//	}
//	
//	public static void closeQuietly(OutputStream writer){
//		try {
//			writer.close();
//        } catch (RuntimeException rethrown) {
//            throw rethrown;
//        } catch (Exception ignored) {
//        }
//	}
	
    public static boolean sync(FileOutputStream stream) {
        try {
            if (stream != null) {
                stream.getFD().sync();
            }
            return true;
        } catch (IOException e) {
        }
        return false;
    }
    
    public static int copy(InputStream in, OutputStream out) throws IOException {
        int total = 0;
        byte[] buffer = new byte[8192];
        int c;
        while ((c = in.read(buffer)) != -1) {
            total += c;
            out.write(buffer, 0, c);
        }
        return total;
    }
}
