package com.codegen.util;
/**
 * 字符串操作常用方法集
 * Created by zhh on 2017/08/16.
 */
public class StringUtils {
	
	/**
	 * 对象是否为无效值
	 * @param obj 要判断的对象
	 * @return 是否为有效值(不为null 和 ""字符串)
	 */
	public static boolean isNullOrEmpty(Object obj) {
		return obj == null || "".equals(obj.toString());
	}
	
	/**
	 * 将字符串的第一位转为小写
	 * @param str 需要转换的字符串
	 * @return 转换后的字符串
	 */
	public static String toLowerCaseFirstOne(String str) {
		if (Character.isLowerCase(str.charAt(0))) {
			return str;
		} else {
			char[] chars = str.toCharArray();
			chars[0] = Character.toLowerCase(chars[0]);
			return new String(chars);
		}
	}
	
	/**
	 * 将字符串的第一位转为大写
	 * @param str 需要转换的字符串
	 * @return 转换后的字符串
	 */
	public static String toUpperCaseFirstOne(String str) {
		if (Character.isUpperCase(str.charAt(0))) {
			return str;
		} else {
			char[] chars = str.toCharArray();
			chars[0] = Character.toUpperCase(chars[0]);
			return new String(chars);
		}
	}
	
	/**
	 * 下划线命名转为驼峰命名
	 * @param str 下划线命名格式
	 * @return 驼峰命名格式
	 */
	public static String underScoreCase2CamelCase(String str) {
		if (!str.contains("_")) {
			return str;
		}
		StringBuilder sb = new StringBuilder();
		char[] chars = str.toCharArray();
		boolean flag = false;
		for (int i = 0; i < chars.length; i++) {
			char ch = chars[i];
			if (ch == '_') {
				flag = true;
			} else {
				if (flag) {
					sb.append(Character.toUpperCase(ch));
					flag = false;
				} else {
					sb.append(ch);
				}
			}
		}
		return sb.toString();
	}
	
	/**
	 * 驼峰命名转为下划线命名
	 * @param str 驼峰命名格式
	 * @return 下划线命名格式
	 */
	public static String camelCase2UnderScoreCase(String str) {
		StringBuilder sb = new StringBuilder();
		char[] chars = str.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			char ch = chars[i];
			if (Character.isUpperCase(ch)) {
				sb.append("_").append(Character.toLowerCase(ch));
			} else {
				sb.append(ch);
			}
		}
		return sb.toString();
	}
}
