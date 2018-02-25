package com.graphics.common.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SolarTermsUtil {

	// 格式化字符串
	private String formatData(Date date, String pattern) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(date);
	}

	public String getSolar(Date now) {
		int day = 0;
		Calendar calendar = Calendar.getInstance();
		for (int i = day; i >= 0; i--) {
			// 每当循环一次重新给Calendar赋值
			calendar.setTime(now);
			// 添加天数完毕
			calendar.add(Calendar.DAY_OF_YEAR, i - day);
			String solar = ChineseTwentyFourDay(calendar.getTime()); // 根据日期获取节气
			if (!solar.trim().equals("")) {
				// 当前节气
				return solar;
			}
		}
		return null;
	}

	public String ChineseTwentyFourDay(Date date1) {

		/*
		 * String[] SolarTerm = new String[] { "小寒", "大寒", "立春", "雨水", "惊蛰",
		 * "春分", "清明", "谷雨", "立夏", "小满", "芒种", "夏至", "小暑", "大暑", "立秋", "处暑",
		 * "白露", "秋分", "寒露", "霜降", "立冬", "小雪", "大雪", "冬至" };
		 */
		String[] SolarTerm = new String[] { "xh", "dh", "lc", "ys", "jz", "cf",
				"qm", "gy", "lx", "xm", "mz", "xz", "xs", "ds", "lq", "ch",
				"bl", "qf", "hl", "sj", "ld", "xx", "dx", "dz" };
		int[] sTermInfo = new int[] { 0, 21208, 42467, 63836, 85337, 107014,
				128867, 150921, 173149, 195551, 218072, 240693, 263343, 285989,
				308563, 331033, 353350, 375494, 397447, 419210, 440795, 462224,
				483532, 504758 };
		Calendar cal = Calendar.getInstance();
		cal.set(1900, 0, 6, 2, 5, 0);// 月份从0开始
		final Date baseDateAndTime = cal.getTime();
		double num;
		int y;
		String tempStr = "";
		// 获取年份
		y = Integer.parseInt(formatData(date1, "yyyy"));

		for (int i = 1; i <= 24; i++) {
			num = 525948.76 * (y - 1900) + sTermInfo[i - 1];
			Calendar calendarMin = Calendar.getInstance();
			calendarMin.setTime(baseDateAndTime);
			// 添加分钟
			calendarMin.add(Calendar.MINUTE, (int) Math.ceil(num) + 5);
			// 对传递的天数进行处理
			Calendar calendarDate1 = Calendar.getInstance();
			calendarDate1.setTime(date1);
			// 对时间进行格式化字符串比较
			if (formatData(calendarMin.getTime(), "yyyy-MM-dd").trim().equals(
					formatData(calendarDate1.getTime(), "yyyy-MM-dd").trim())) {
				tempStr = SolarTerm[i - 1];
				break;
			}
		}
		return tempStr;
	}
}
