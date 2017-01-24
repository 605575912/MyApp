package com.support.loader.utils;

import com.support.loader.proguard.IProguard;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class HttpPostUtil implements IProguard {
	String urlString;
	String sessionId;
	HttpURLConnection conn;
	String boundary = "--------httppost123";
	Map<String, String> textParams = new HashMap<String, String>();
	Map<String, File> fileparams = new HashMap<String, File>();
	DataOutputStream ds;

	/**
	 * 注意上次的文件类似 2015-3-20 @author lzx
	 * 
	 */
	public HttpPostUtil(String url,String sessionId) {
		this.urlString = url;
		this.sessionId = sessionId;
	}

	// 重新设置要请求的服务器地址，即上传文件的地址。
	public void setUrl(String url,String sessionId) throws Exception {
		this.urlString = url;
		this.sessionId = sessionId;
	}

	// 增加一个普通字符串数据到form表单数据中
	public void addTextParameter(String name, String value) {
		textParams.put(name, value);
	}

	// 增加一个文件到form表单数据中
	public void addFileParameter(String name, File value) {
		fileparams.put(name, value);
	}

	// 清空所有已添加的form表单数据
	public void clearAllParameters() {
		textParams.clear();
		fileparams.clear();
	}

	// 发送数据到服务器，返回一个字节包含服务器的返回结果的数组
	public String uploadImage(UpLoadValue upLoadValue, long maxlenght) {

		String relut = null;
		try {
			initConnection(new URL(urlString));
			try {
				conn.connect();
			} catch (SocketTimeoutException e) {
				// something
				throw new RuntimeException();
			}
			ds = new DataOutputStream(conn.getOutputStream());
			writeFileParams(upLoadValue, maxlenght);
			writeStringParams();
			paramsEnd();
			clearAllParameters();
			int res = conn.getResponseCode();
			if (res == 200) {
				InputStream input = conn.getInputStream();
				StringBuffer sb1 = new StringBuffer();
				int ss;
				while ((ss = input.read()) != -1) {
					sb1.append((char) ss);
				}
				ds.close();
				input.close();
				relut = sb1.toString();
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} finally {
			conn.disconnect();
		}

		// InputStream in = conn.getInputStream();
		// ByteArrayOutputStream out = new ByteArrayOutputStream();
		// int b;
		// while ((b = in.read()) != -1) {
		// out.write(b);
		// }

		return relut;
		// return out.toByteArray();
	}

	// 文件上传的connection的一些必须设置
	private void initConnection(URL url) throws Exception {
		conn = (HttpURLConnection) url.openConnection();
		conn.setDoOutput(true);
		conn.setUseCaches(false);
		conn.setConnectTimeout(10000); // 连接超时为10秒
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Charset", "utf-8"); // 设置编码
		conn.setRequestProperty("connection", "keep-alive");
		if (!sessionId.equals("")) {
			conn.setRequestProperty("Cookie", sessionId);
		}
		conn.setRequestProperty("Content-Type",
				"multipart/form-data; boundary=" + boundary);
	}

	// 普通字符串数据
	private void writeStringParams() throws Exception {
		Set<String> keySet = textParams.keySet();
		for (Iterator<String> it = keySet.iterator(); it.hasNext();) {
			String name = it.next();
			String value = textParams.get(name);
			ds.writeBytes("--" + boundary + "\r\n");
			ds.writeBytes("Content-Disposition: form-data; name=\"" + name
					+ "\"\r\n");
			ds.writeBytes("\r\n");
			ds.writeBytes(encode(value) + "\r\n");
		}
	}

	// 文件数据
	private void writeFileParams(UpLoadValue upLoadValue, long maxlenght)
			throws Exception {
		Set<String> keySet = fileparams.keySet();
		for (Iterator<String> it = keySet.iterator(); it.hasNext();) {
			String name = it.next();
			File value = fileparams.get(name);
			ds.writeBytes("--" + boundary + "\r\n");
			ds.writeBytes("Content-Disposition: form-data; name=\"" + name
					+ "\"; filename=\"" + encode(value.getName()) + "\"\r\n");
			ds.writeBytes("Content-Type: " + "image/png" + "\r\n");
			ds.writeBytes("\r\n");
			ds.write(getBytes(value, upLoadValue, maxlenght));
			ds.writeBytes("\r\n");
		}
	}

	// //获取文件的上传类型，图片格式为image/png,image/jpg等。非图片为application/octet-stream
	// private String getContentType(File f) throws Exception {
	//
	// // return "application/octet-stream"; //
	// 此行不再细分是否为图片，全部作为application/octet-stream 类型
	// ImageInputStream imagein = ImageIO.createImageInputStream(f);
	// if (imagein == null) {
	// return "application/octet-stream";
	// }
	// Iterator<ImageReader> it = ImageIO.getImageReaders(imagein);
	// if (!it.hasNext()) {
	// imagein.close();
	// return "application/octet-stream";
	// }
	// imagein.close();
	// return "image/" +
	// it.next().getFormatName().toLowerCase();//将FormatName返回的值转换成小写，默认为大写
	//
	// }
	// 把文件转换成字节数组
	private byte[] getBytes(File f, UpLoadValue upLoadValue, long maxlenght)
			throws Exception {
		FileInputStream in = new FileInputStream(f);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] b = new byte[1024];
		int len;
		long uplength = 0;
		double value = 0;
		while ((len = in.read(b)) != -1) {
			if (upLoadValue != null && maxlenght > 0) {
				uplength = uplength + len;
				double temp = ((double) uplength / maxlenght) * 100;
				if (temp > value + 3 || temp >= 98) {
					value = temp;
					upLoadValue.upload((int) (value));
				}
			}
			out.write(b, 0, len);
		}
		in.close();
		return out.toByteArray();
	}

	// 添加结尾数据
	private void paramsEnd() throws Exception {
		ds.writeBytes("--" + boundary + "--" + "\r\n");
		ds.writeBytes("\r\n");
	}

	// 对包含中文的字符串进行转码，此为UTF-8。服务器那边要进行一次解码
	private String encode(String value) throws Exception {
		return URLEncoder.encode(value, "UTF-8");
	}
	// public static void main(String[] args) throws Exception {
	//
	//
	// }

}
