package com.support.loader.utils;

import android.os.Environment;
import android.text.TextUtils;
import android.util.Base64;

import com.support.loader.proguard.IProguard;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;

/**
 * 
 * <br>
 * 类描述: <br>
 * 功能详细描述:
 * 
 * @author linhang
 * @date [2013-3-22]
 */
public class FileUtils implements IProguard{

	public static void deleteDirectory(File f) {
		if (f.exists() && f.isDirectory()) {
			// 判断是文件还是目录
			if (f.listFiles().length == 0) {
				// 若目录下没有文件则直接删除
				f.delete();

			} else {
				// 若有则把文件放进数组，并判断是否有下级目录
				File delFile[] = f.listFiles();
				int i = f.listFiles().length;
				for (int j = 0; j < i; j++) {
					if (delFile[j].isDirectory()) {
						deleteDirectory(delFile[j]); // 递归调用del方法并取得子目录路径
					}
					delFile[j].delete(); // 删除文件
				}
			}
		}
	}

	public static boolean isSDCardExist() {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}

	public static String getSDPath() {
		return Environment.getExternalStorageDirectory().toString();
	}

	public static String readFileToString(String filePath) {
		if (filePath == null || "".equals(filePath)) {
			return null;
		}
		File file = new File(filePath);
		if (!file.exists()) {
			return null;
		}

		InputStream inputStream = null;
		try {
			inputStream = new FileInputStream(file);
			return readToString(inputStream, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
		return null;
	}

	public static byte[] readFileToByte(String filePath) throws Exception {
		if (TextUtils.isEmpty(filePath)) {
			return null;
		}
		File file = new File(filePath);
		if (!file.exists()) {
			throw new FileNotFoundException();
		}
		InputStream inputStream = null;
		inputStream = new FileInputStream(file);
		byte[] data = toByteArray(inputStream);
		return data;
	}

	public static void saveByteToSDFile(final byte[] byteData,
			final String filePathName) throws Exception {
		File newFile = createNewFile(filePathName, false);
		FileOutputStream fileOutputStream = new FileOutputStream(newFile);
		fileOutputStream.write(byteData);
		fileOutputStream.flush();
		fileOutputStream.close();
	}

	/**
	 * 
	 * @param path
	 *            ：文件路径
	 * @param append
	 *            ：若存在是否插入原文件
	 * @return
	 */
	public static File createNewFile(String path, boolean append) {
		File newFile = new File(path);
		if (!append) {
			if (newFile.exists()) {
				newFile.delete();
			}
		}
		if (!newFile.exists()) {
			try {
				File parent = newFile.getParentFile();
				if (parent != null && !parent.exists()) {
					parent.mkdirs();
				}
				newFile.createNewFile();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return newFile;
	}

	/**
	 * 指定路径文件是否存在
	 * 
	 * @author huyong
	 * @param filePath
	 * @return
	 */
	public static boolean isFileExist(String filePath) {
		boolean result = false;
		if (!TextUtils.isEmpty(filePath)) {
			try {
				File file = new File(filePath);
				result = file.exists();
				file = null;
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		return result;
	}

	public static boolean deleteFile(String filePath) {
		if (TextUtils.isEmpty(filePath)) {
			return false;
		}
		File file = new File(filePath);
		if (file.exists()) {
			return file.delete();
		}
		return false;
	}

	/**
	 * 
	 * @param inputStream
	 * @param encoding
	 * @return
	 */
	public static String readToString(InputStream inputStream, String encoding) {

		InputStreamReader in = null;
		try {
			StringWriter sw = new StringWriter();
			in = new InputStreamReader(inputStream, encoding);
			copy(in, sw);
			return sw.toString();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
		return null;
	}

	private static int copy(Reader input, Writer output) throws IOException {
		char[] buffer = new char[1024 * 4];
		int count = 0;
		int n = 0;
		while (-1 != (n = input.read(buffer))) {
			output.write(buffer, 0, n);
			count += n;
		}
		return count;
	}

	public static int copy(InputStream input, OutputStream output)
			throws IOException {
		byte[] buffer = new byte[1024 * 4];
		int count = 0;
		int n = 0;
		while (-1 != (n = input.read(buffer))) {
			output.write(buffer, 0, n);
			count += n;
		}
		return count;
	}

	public static byte[] toByteArray(InputStream input) throws IOException {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		copy(input, output);
		return output.toByteArray();
	}

	/**
	 * @param base64
	 * @return <p>
	 *         将字符串转换成字节
	 *         </p>
	 */
	public static byte[] transform(String base64) {
		byte[] result = null;
		/*
		 * if (base64 == null || base64.equals("") || base64.equals("null")) {
		 * return null; }
		 */
		if (base64 == null || base64.startsWith("data") == false) {
		} else {
			base64 = base64.substring(base64.indexOf(",") + 1); // 这里要把上传上来的base64
			// 里面的data:image/png;base64,处理一下才能保存成文件
			result = Base64.decode(base64, Base64.DEFAULT);
		}
		return result;
	}

	public static String checkFileName(String fileName) {
		String checkedFileName = fileName.replace("/", "").replace("?", "")
				.replace("*", "").replace(":", "").replace("<", "")
				.replace(">", "").replace("|", "");

		return checkedFileName;
	}

	public static boolean isLegalFileName(String fileName) {
		boolean isLegal = true;
		if (fileName.contains("/") || fileName.contains("?")
				|| fileName.contains("*") || fileName.contains(":")
				|| fileName.contains("<") || fileName.contains(">")
				|| fileName.contains("|")) {
			isLegal = false;
		}

		return isLegal;
	}

}
