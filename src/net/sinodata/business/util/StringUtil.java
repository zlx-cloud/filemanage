package net.sinodata.business.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {

	public static boolean isEmpty(String str) {
		if ("".equals(str) || str == null) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isNotEmpty(String str) {
		if (!"".equals(str) && str != null) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean existStrArr(String str, String[] strArr) {
		for (int i = 0; i < strArr.length; i++) {
			if (strArr[i].equals(str)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断字符串中是否包含中文
	 * 
	 * @param str 待校验字符串
	 * @return 是否为中文
	 * @warn 不能校验是否为中文标点符号
	 */
	public static boolean isContainChinese(String str) {
		Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
		Matcher m = p.matcher(str);
		if (m.find()) {
			return true;
		}
		return false;
	}

	public static boolean isValidDate(String str, String srtFormat) {
		boolean convertSuccess = true;
		// 指定日期格式为四位年 两位月份两位日期 两位小时 两位 分钟，注意yyyyMMddHHmm区分大小写；
		SimpleDateFormat format = new SimpleDateFormat(srtFormat);
		try {
			// 设置lenient为false.
			// 否则SimpleDateFormat会比较宽松地验证日期，比如2007/02/29会被接受，并转换成2007/03/01
			format.setLenient(false);
			format.parse(str);
		} catch (ParseException e) {
			// e.printStackTrace();
			// 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
			convertSuccess = false;
		}
		return convertSuccess;
	}

	public static String createUUID() {
		String uuid = UUID.randomUUID().toString().replaceAll("-", "");
		return uuid;
	}

	public static Boolean isHaveSuffix(String fileName) {
		int splitIndex = fileName.lastIndexOf(".");
		if (splitIndex > 0) {
			return true;
		} else {
			return false;
		}
	}
	
}