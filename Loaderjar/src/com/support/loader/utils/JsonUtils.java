package com.support.loader.utils;

import com.support.loader.proguard.IProguard;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class JsonUtils implements IProguard {
	public static Object getobject(Object model, JSONObject json) {
		Field[] field = model.getClass().getDeclaredFields(); // 获取实体类的所有属性，返回Field数组
		final Class<?> objclazz = model.getClass();
		for (int j = 0; j < field.length; j++) { // 遍历所有属性
			String fieldname = field[j].getName(); // 获取属性的名字
			String name = fieldname.substring(0, 1).toUpperCase()
					+ fieldname.substring(1); // 将属性的首字符大写，方便构造get，set方法
			String type = field[j].getGenericType().toString(); // 获取属性的类型
			if (type.indexOf("String") > -1) { // 如果type是类类型，则前面包含"class "，后面跟类名
				if (json.has(fieldname)) {
					try {
						Field fieldv = objclazz.getDeclaredField(fieldname);
						try {
							fieldv.set(model, json.getString(fieldname));
							continue;
						} catch (IllegalAccessException e) {
							e.printStackTrace();
						} catch (JSONException e) {
							e.printStackTrace();
						}
					} catch (NoSuchFieldException e) {
						e.printStackTrace();
					}

					Method m;
					try {
						m = model.getClass().getMethod("set" + name,
								String.class);
						m.invoke(model, json.getString(fieldname));
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (NoSuchMethodException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				// Method m = model.getClass().getMethod("get" + name);
				// String value = (String) m.invoke(model); //
				// 调用getter方法获取属性值
				// if (value == null || value.equals("")) {
				// m = model.getClass().getMethod("set" + name,
				// String.class);
				// m.invoke(model, "1");
				// }
			}
			if (type.indexOf("int") > -1) {
				if (json.has(fieldname)) {

					try {
						Field fieldv = objclazz.getDeclaredField(fieldname);
						try {
							fieldv.set(model, json.getInt(fieldname));
							continue;
						} catch (IllegalAccessException e) {
							e.printStackTrace();
						} catch (JSONException e) {
							e.printStackTrace();
						}
					} catch (NoSuchFieldException e) {
						e.printStackTrace();
					}



					try {
						Method m = model.getClass().getMethod("set" + name,
								int.class);
						m.invoke(model, json.getInt(fieldname));
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (NoSuchMethodException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				// Method m = model.getClass().getMethod("get" + name);
				// Integer value = (Integer) m.invoke(model);
				// if (value == null) {
				// m = model.getClass().getMethod("set" + name,
				// Integer.class);
				// m.invoke(model, 0);
				// }
			}
			if (type.indexOf("long") > -1) {
				if (json.has(fieldname)) {
					try {
						Field fieldv = objclazz.getDeclaredField(fieldname);
						try {
							fieldv.set(model, json.getLong(fieldname));
							continue;
						} catch (IllegalAccessException e) {
							e.printStackTrace();
						} catch (JSONException e) {
							e.printStackTrace();
						}
					} catch (NoSuchFieldException e) {
						e.printStackTrace();
					}


					try {
						Method m = model.getClass().getMethod("set" + name,
								long.class);
						m.invoke(model, json.getLong(fieldname));
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (NoSuchMethodException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			if (type.indexOf("double") > -1) {
				if (json.has(fieldname)) {
					try {
						Field fieldv = objclazz.getDeclaredField(fieldname);
						try {
							fieldv.set(model, json.getDouble(fieldname));
							continue;
						} catch (IllegalAccessException e) {
							e.printStackTrace();
						} catch (JSONException e) {
							e.printStackTrace();
						}
					} catch (NoSuchFieldException e) {
						e.printStackTrace();
					}
					try {
						Method m = model.getClass().getMethod("set" + name,
								double.class);
						m.invoke(model, json.getDouble(fieldname));
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (NoSuchMethodException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			if (type.indexOf("boolean") > -1) {
				if (json.has(fieldname)) {

					try {
						Field fieldv = objclazz.getDeclaredField(fieldname);
						try {
							fieldv.set(model, json.getBoolean(fieldname));
							continue;
						} catch (IllegalAccessException e) {
							e.printStackTrace();
						} catch (JSONException e) {
							e.printStackTrace();
						}
					} catch (NoSuchFieldException e) {
						e.printStackTrace();
					}
					try {
						Method m = model.getClass().getMethod("set" + name,
								boolean.class);
						m.invoke(model, json.getBoolean(fieldname));
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (NoSuchMethodException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		return model;
	}
}
