package ru.dseymo.libutils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ReflUtil {
	
	public static void setValue(Object object, String fieldName, Object value) {
		try {
			Field field = object.getClass().getDeclaredField(fieldName);
			field.setAccessible(true);
			field.set(object, value);
		} catch (Exception e) {e.printStackTrace();}
	}
	
	public static Object getValue(Object object, String fieldName) {
		try {
			Field field = object.getClass().getDeclaredField(fieldName);
			field.setAccessible(true);
			return field.get(object);
		} catch (Exception e) {e.printStackTrace(); return null;}
	}
	
	public static Object getStaticValue(Class<?> clazz, String fieldName) {
		try {
			Field field = clazz.getDeclaredField(fieldName);
			field.setAccessible(true);
			return field.get(null);
		} catch (Exception e) {e.printStackTrace(); return null;}
	}
	
	public static Object instance(Class<?> clazz, Class<?>[] clazzArgs, Object...args) {
		try {
			Constructor<?> constr = clazz.getDeclaredConstructor(clazzArgs);
			constr.setAccessible(true);
			return constr.newInstance(args);
		} catch (Exception e) {e.printStackTrace(); return null;}
	}
	
	public static Object instance(Class<?> clazz) {
		try {
			return clazz.newInstance();
		} catch (Exception e) {e.printStackTrace(); return null;}
	}
	
	public static Object invokeStatic(Class<?> clazz, String methodName, Class<?>[] clazzArgs, Object...args) {
		try {
			Method meth = clazz.getDeclaredMethod(methodName, clazzArgs);
			meth.setAccessible(true);
			return meth.invoke(null, args);
		} catch (Exception e) {e.printStackTrace(); return null;}
	}
	
	public static Object invokeStatic(Class<?> clazz, String methodName) {
		try {
			Method meth = clazz.getDeclaredMethod(methodName);
			meth.setAccessible(true);
			return meth.invoke(null);
		} catch (Exception e) {e.printStackTrace(); return null;}
	}
	
	public static Method getMethod(Object object, String methodName, Class<?>...args) {
		Class<?> clazz = object.getClass();
		Method meth = null;
		try {
			meth = clazz.getMethod(methodName, args);
		} catch (Exception e) {
			while(meth == null && clazz != null) {
				try {
					meth = clazz.getDeclaredMethod(methodName, args);
				} catch (Exception e2) {}
				
				clazz = clazz.getSuperclass();
			}
		}
		
		if(meth != null)
			meth.setAccessible(true);
		
		return meth;
	}
	
	public static Object invoke(Object object, String methodName, Class<?>[] clazzArgs, Object...args) {
		try {
			return getMethod(object, methodName, clazzArgs).invoke(object, args);
		} catch (Exception e) {e.printStackTrace(); return null;}
	}
	
	public static Object invoke(Object object, String methodName) {
		try {
			return getMethod(object, methodName).invoke(object);
		} catch (Exception e) {e.printStackTrace(); return null;}
	}
	
	public static boolean isMethod(Object object, String methodName) {
		return getMethod(object, methodName) != null;
	}
	
	public static boolean isMethod(Object object, String methodName, Class<?>[] clazzArgs) {
		return getMethod(object, methodName, clazzArgs) != null;
	}
	
	@SuppressWarnings({"unchecked", "rawtypes"})
	public static Object getEnumValue(Class<?> enumz, String value) {
		try {
			return Enum.valueOf((Class<Enum>)enumz, value);
		} catch (Exception e) {e.printStackTrace(); return false;}
	}
	
}
