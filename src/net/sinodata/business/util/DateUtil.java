package net.sinodata.business.util;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateUtil {

	public static String getCurrTime() {
		LocalDateTime now = LocalDateTime.now();
		String format = now.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
		return format;
	}
	
	/**
	 * 获取现在时间
	 * 
	 * @param format 日期格式
	 * @return 返回时间
	 */
	public static String getNowDate(String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		String date = sdf.format(new Date());
		return date;
	}

}