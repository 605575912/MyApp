package com.support.loader.utils;

import android.content.ContentValues;
import android.database.Cursor;

import com.support.loader.proguard.IProguard;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class DB_Model implements IProguard{
	/**
	 * 获取更新的SQL 2015-4-16 @author lzx
	 * 
	 */
	public ArrayList<Object> getUpdateString(StringBuilder sb, Object model) {
		Field[] field = model.getClass().getDeclaredFields(); // 获取实体类的所有属性，返回Field数组
		ArrayList<Object> args = new ArrayList<Object>();
		try {
			for (int j = 0; j < field.length; j++) { // 遍历所有属性
				String fieldname = field[j].getName(); // 获取属性的名字
				String type = field[j].getGenericType().toString(); // 获取属性的类型
				if (type.indexOf("int") > -1) {
					args.add(append(sb, fieldname, model).toString());
				} else if (type.indexOf("String") > -1) {
					args.add(append(sb, fieldname, model).toString());
				} else if (type.indexOf("long") > -1) {
					args.add(append(sb, fieldname, model).toString());
				} else if (type.indexOf("boolean") > -1) {
					args.add(append(sb, fieldname, model, true).toString());
				} else if (type.indexOf("double") > -1) {
					args.add(append(sb, fieldname, model).toString());
				} else if (type.indexOf("float") > -1) {
					args.add(append(sb, fieldname, model).toString());
				}
			}
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return args;
	}

	Object append(StringBuilder sb, String fieldname, Object model)
			throws NoSuchMethodException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		// 获取属性的类型
		String name = fieldname.substring(0, 1).toUpperCase()
				+ fieldname.substring(1); // 将属性的首字符大写，方便构造get，set方法
		if (sb.length()==0) {
			sb.append(fieldname + " = ?");
		}else {
			sb.append(" , "+fieldname + " = ?");
		}
		
		Method m;
		m = model.getClass().getMethod("get" + name);
		return m.invoke(model);
	}

	Object append(StringBuilder sb, String fieldname, Object model, boolean is)
			throws NoSuchMethodException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		// 获取属性的类型
		String name = fieldname.substring(0, 1).toUpperCase()
				+ fieldname.substring(1); // 将属性的首字符大写，方便构造get，set方法
		if (sb.length()==0) {
			sb.append(fieldname + " = ?");
		}else {
			sb.append(" , "+fieldname + " = ?");
		}
		Method m;
		if (is) {
			m = model.getClass().getMethod("is" + name);
			boolean reult = (Boolean) m.invoke(model);
			if (reult) {
				return 1;
			} else {
				return 0;
			}

		} else {
			m = model.getClass().getMethod("get" + name);
			return m.invoke(model);
		}

	}


	public void CreateMeassageDb(StringBuilder sb, Class<?> model) {
		Field[] field = model.getDeclaredFields(); // 获取实体类的所有属性，返回Field数组
		for (int j = 0; j < field.length; j++) { // 遍历所有属性
			String fieldname = field[j].getName(); // 获取属性的名字
			if (!fieldname.equals("id")) {
				String type = field[j].getGenericType().toString(); // 获取属性的类型
				if (type.indexOf("int") > -1) {
					sb.append(" , [" + fieldname + "] integer");
				} else if (type.indexOf("String") > -1) {
					sb.append(" , [" + fieldname + "] TEXT");
				} else if (type.indexOf("long") > -1) {
					sb.append(" , [" + fieldname + "] long");
				} else if (type.indexOf("boolean") > -1) {
					sb.append(" , [" + fieldname + "] integer");
				} else if (type.indexOf("double") > -1) {
					sb.append(" , [" + fieldname + "] double");
				} else if (type.indexOf("float") > -1) {
					sb.append(" , [" + fieldname + "] float");
				}
			}

		}
		sb.append(" )");
	}

	/**

	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public void InserMeassageDb(Object model, ContentValues contentValues)
			throws NoSuchMethodException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		Field[] field = model.getClass().getDeclaredFields(); // 获取实体类的所有属性，返回Field数组
		for (int j = 0; j < field.length; j++) { // 遍历所有属性
			String fieldname = field[j].getName(); // 获取属性的名字
			String type = field[j].getGenericType().toString(); // 获取属性的类型
			String name = fieldname.substring(0, 1).toUpperCase()
					+ fieldname.substring(1); // 将属性的首字符大写，方便构造get，set方法
			Method m;
			if (type.indexOf("int") > -1) {
				m = model.getClass().getMethod("get" + name);
				contentValues.put(fieldname, (Integer) m.invoke(model));
			} else if (type.indexOf("String") > -1) {
				m = model.getClass().getMethod("get" + name);
				contentValues.put(fieldname, (String) m.invoke(model));
			} else if (type.indexOf("long") > -1) {
				m = model.getClass().getMethod("get" + name);
				contentValues.put(fieldname, (Long) m.invoke(model));
			} else if (type.indexOf("boolean") > -1) {
				m = model.getClass().getMethod("is" + name);
				boolean reult = (Boolean) m.invoke(model);
				if (reult) {
					contentValues.put(fieldname, 1);
				} else {
					contentValues.put(fieldname, 0);
				}
			} else if (type.indexOf("double") > -1) {
				m = model.getClass().getMethod("get" + name);
				contentValues.put(fieldname, (Double) m.invoke(model));
			} else if (type.indexOf("float") > -1) {
				m = model.getClass().getMethod("get" + name);
				contentValues.put(fieldname, (Float) m.invoke(model));
			}
		}
	}

	public void SelectMeassageDb(Object model, Cursor cursor)
			throws NoSuchMethodException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		if (model == null) {
			return;
		}
		Field[] field = model.getClass().getDeclaredFields(); // 获取实体类的所有属性，返回Field数组
		for (int j = 0; j < field.length; j++) { // 遍历所有属性
			String fieldname = field[j].getName(); // 获取属性的名字
			String type = field[j].getGenericType().toString(); // 获取属性的类型
			if (type.indexOf("int") > -1) {
				int index = cursor.getColumnIndex(fieldname);
				if (index > -1) {
					String name = fieldname.substring(0, 1).toUpperCase()
							+ fieldname.substring(1); // 将属性的首字符大写，方便构造get，set方法
					Method m = model.getClass().getMethod("set" + name,
							int.class);
					
					m.invoke(model, cursor.getInt(index));
					
				}
			} else if (type.indexOf("String") > -1) {
				int index = cursor.getColumnIndex(fieldname);
				if (index > -1) {
					String name = fieldname.substring(0, 1).toUpperCase()
							+ fieldname.substring(1); // 将属性的首字符大写，方便构造get，set方法
					Method m = model.getClass().getMethod("set" + name,
							String.class);
					
					m.invoke(model, cursor.getString(index));
					
					
				}
			} else if (type.indexOf("long") > -1) {
				int index = cursor.getColumnIndex(fieldname);
				if (index > -1) {
					String name = fieldname.substring(0, 1).toUpperCase()
							+ fieldname.substring(1); // 将属性的首字符大写，方便构造get，set方法
					
					Method m = model.getClass().getMethod("set" + name,
							long.class);
					m.invoke(model, cursor.getLong(index));
				}
			} else if (type.indexOf("boolean") > -1) {
				int index = cursor.getColumnIndex(fieldname);
				if (index > -1) {
					String name = fieldname.substring(0, 1).toUpperCase()
							+ fieldname.substring(1); // 将属性的首字符大写，方便构造get，set方法
					Method m = model.getClass().getMethod("set" + name,
							boolean.class);
					int result = cursor.getInt(index);
					if (result == 0) {
						m.invoke(model, false);
					} else {
						m.invoke(model, true);
					}
				}
			} else if (type.indexOf("double") > -1) {
				int index = cursor.getColumnIndex(fieldname);
				if (index > -1) {
					String name = fieldname.substring(0, 1).toUpperCase()
							+ fieldname.substring(1); // 将属性的首字符大写，方便构造get，set方法
					Method m = model.getClass().getMethod("set" + name,
							double.class);
					m.invoke(model, cursor.getDouble(index));
				}
			} else if (type.indexOf("float") > -1) {
				int index = cursor.getColumnIndex(fieldname);
				if (index > -1) {
					String name = fieldname.substring(0, 1).toUpperCase()
							+ fieldname.substring(1); // 将属性的首字符大写，方便构造get，set方法
					Method m = model.getClass().getMethod("set" + name,
							float.class);
					m.invoke(model, cursor.getFloat(index));
				}
			}
		}
	}
}
