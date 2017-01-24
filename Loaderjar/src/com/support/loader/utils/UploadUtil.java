package com.support.loader.utils;

import android.util.Log;

import com.support.loader.proguard.IProguard;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * 自定义的上传类，目前用来上传图片、语音文件 created by Bear at 2015-1-4 下午3:48:16 TODO
 */
public class UploadUtil implements IProguard{
	private final int TIME_OUT = 10 * 1000; // 超时时间
	public final String CHARSET = "UTF-8"; // 设置编码

	// /**
	// * filekey 文件key created by Bear at 2014-12-26 下午5:40:08 TODO
	// */
	// public static String uploadFile(String filekey, File file,
	// String RequestURL, UpLoadValue upLoadValue) {
	// String result = null;
	// String BOUNDARY = UUID.randomUUID().toString(); // 边界标识 随机生成
	// String PREFIX = "--", LINE_END = "\r\n";
	// String CONTENT_TYPE = "multipart/form-data"; // 内容类型
	// try {
	// URL url = new URL(RequestURL);
	// HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	// conn.setReadTimeout(TIME_OUT);
	// conn.setConnectTimeout(TIME_OUT);
	// conn.setDoInput(true); // 允许输入流
	// conn.setDoOutput(true); // 允许输出流
	// conn.setUseCaches(false); // 不允许使用缓存
	// conn.setRequestMethod("POST"); // 请求方式
	// conn.setRequestProperty("Charset", CHARSET); // 设置编码
	// conn.setRequestProperty("connection", "keep-alive");
	// conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary="
	// + BOUNDARY);
	//
	// if (file != null) {
	// /**
	// * 当文件不为空，把文件包装并且上传
	// */
	// DataOutputStream dos = new DataOutputStream(
	// conn.getOutputStream());
	// StringBuffer sb = new StringBuffer();
	// sb.append(PREFIX);
	// sb.append(BOUNDARY);
	// sb.append(LINE_END);
	// /**
	// * 这里重点注意： name里面的值为服务器端需要key 只有这个key 才可以得到对应的文件
	// * filename是文件的名字，包含后缀名的 比如:abc.png
	// */
	//
	// sb.append("Content-Disposition: form-data; name=\"" + filekey
	// + "\"; filename=\"" + file.getName() + "\"" + LINE_END);
	// sb.append("Content-Type: image/png "+LINE_END);
	// sb.append(LINE_END);
	// dos.write(sb.toString().getBytes());
	// InputStream is = new FileInputStream(file);
	// long length = file.length();
	// long uplength = 0;
	// byte[] bytes = new byte[1024];
	// int len = 0;
	// double value = 0;
	// while ((len = is.read(bytes)) != -1) {
	// if (upLoadValue != null) {
	// uplength = uplength + len;
	// double temp = ((double) uplength / length) * 100;
	// if (temp > value + 3 || temp >= 98) {
	// value = temp;
	// upLoadValue.upload((int) (value));
	// }
	// }
	// dos.write(bytes, 0, len);
	// }
	//
	// is.close();
	// dos.write(LINE_END.getBytes());
	// byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END)
	// .getBytes();
	// dos.write(end_data);
	// dos.flush();
	// if (upLoadValue != null) {
	// upLoadValue.upload(100);
	// }
	// /**
	// * 获取响应码 200=成功 当响应成功，获取响应的流
	// */
	// int res = conn.getResponseCode();
	// if (res == 200) {
	// InputStream input = conn.getInputStream();
	// StringBuffer sb1 = new StringBuffer();
	// int ss;
	// while ((ss = input.read()) != -1) {
	// sb1.append((char) ss);
	// }
	// result = sb1.toString();
	// }
	// }
	// } catch (MalformedURLException e) {
	// e.printStackTrace();
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// return result;
	// }

	public String upload(List<String> list, String urlString) {
		String relust = null;
		try {
			String BOUNDARY = "---------7d4a6d158c9"; // 定义数据分隔线
			URL url = new URL(urlString);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
			conn.setRequestProperty("Charsert", "UTF-8");
			conn.setRequestProperty("Content-Type",
					"multipart/form-data; boundary=" + BOUNDARY);

			OutputStream out = new DataOutputStream(conn.getOutputStream());
			byte[] end_data = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();// 定义最后数据分隔线
			int leng = list.size();
			for (int i = 0; i < leng; i++) {
				String fname = list.get(i);
				File file = new File(fname);
				StringBuilder sb = new StringBuilder();
				sb.append("--");
				sb.append(BOUNDARY);
				sb.append("\r\n");
				sb.append("Content-Disposition: form-data;name=\"file" + i
						+ "\";filename=\"" + file.getName() + "\"\r\n");
				sb.append("Content-Type:application/octet-stream\r\n\r\n");

				byte[] data = sb.toString().getBytes();
				out.write(data);
				DataInputStream in = new DataInputStream(new FileInputStream(
						file));
				int bytes = 0;
				byte[] bufferOut = new byte[1024];
				while ((bytes = in.read(bufferOut)) != -1) {
					out.write(bufferOut, 0, bytes);
				}
				out.write("\r\n".getBytes()); // 多个文件时，二个文件之间加入这个
				in.close();
			}
			out.write(end_data);
			out.flush();
			out.close();

			// 定义BufferedReader输入流来读取URL的响应
			// BufferedReader reader = new BufferedReader(new InputStreamReader(
			// conn.getInputStream()));
			if (conn.getResponseCode() == 200) {
				InputStream inStream = conn.getInputStream();
				StringBuffer sb1 = new StringBuffer();
				int ss;
				while ((ss = inStream.read()) != -1) {
					sb1.append((char) ss);
				}
				return sb1.toString();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return relust;
	}

	public String postString(String urlString, String params, boolean login,
			String sessionId, HttpListener httpListener) {
		byte[] data;
		try {
			// String BOUNDARY = UUID.randomUUID().toString(); // 边界标识 随机生成
			// String PREFIX = "--", LINE_END = "\r\n";
			// String CONTENT_TYPE = "multipart/form-data"; // 内容类型
			data = params.getBytes(CHARSET);
			URL url = new URL(urlString);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(TIME_OUT);
			conn.setConnectTimeout(TIME_OUT);
			conn.setDoInput(true); // 允许输入流
			conn.setDoOutput(true); // 允许输出流
			conn.setUseCaches(false); // 不允许使用缓存
			conn.setRequestMethod("POST"); // 请求方式
			conn.setRequestProperty("Charset", CHARSET); // 设置编码
			conn.setRequestProperty("connection", "keep-alive");
			if (!sessionId.equals("")) {
				conn.addRequestProperty("Cookie", sessionId);
			}
			try {
				conn.setConnectTimeout(5 * 1000);
				OutputStream outStream = conn.getOutputStream();
				outStream.write(data);
				outStream.flush();
				outStream.close();
				if (conn.getResponseCode() == 200) {
					if (login) {
						String key = "";
						for (int i = 1; (key = conn.getHeaderFieldKey(i)) != null; i++) {
							if (key.equalsIgnoreCase("Set-Cookie")) {
								sessionId = conn.getHeaderField(key);
								sessionId = sessionId.substring(0,
										sessionId.indexOf(";"));
								if (httpListener != null) {
									httpListener.getResult(sessionId);
								}
							}
						}

					}
					InputStream inStream = conn.getInputStream();
					StringBuffer sb1 = new StringBuffer();
					int ss;
					while ((ss = inStream.read()) != -1) {
						sb1.append((char) ss);
					}
					return sb1.toString();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
			}
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			// e1.printStackTrace();
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			// e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			// e1.printStackTrace();
		}

		return null;
	}

	public String getString(String urlString, String sessionId) {
		URL url = null;
		try {

			url = new URL(urlString);
			HttpURLConnection conn;
			try {
				conn = (HttpURLConnection) url.openConnection();
				conn.setReadTimeout(TIME_OUT);
				conn.setConnectTimeout(TIME_OUT);
//				conn.setDoInput(true); // 允许输入流
//				conn.setDoOutput(true); // 允许输出流
				conn.setUseCaches(false); // 不允许使用缓存
				conn.setRequestMethod("GET");
				conn.setRequestProperty("Charset", CHARSET); // 设置编码
				conn.setRequestProperty("connection", "keep-alive");
				if (!sessionId.equals("")) {
					conn.setRequestProperty("Cookie", sessionId);
				}

				try {
					int status = conn.getResponseCode();
					if (status == 200) {

						InputStream inStream = conn.getInputStream();
						StringBuffer sb1 = new StringBuffer();
						int ss;
						while ((ss = inStream.read()) != -1) {
							sb1.append((char) ss);
						}
						conn.disconnect();
						return sb1.toString();
					} else {
						conn.disconnect();
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					// e.printStackTrace();
				}
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				// e1.printStackTrace();
			}
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			 e1.printStackTrace();
			Log.i("TAG",e1.getMessage());

		}
		return null;
	}

}