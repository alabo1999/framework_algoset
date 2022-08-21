package com.abc.example.common.utils;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.Date;

/**
 * @className		: TimeSplitUtil
 * @description	: 时间分表工具类
 * @summary		:
 * @history		:
 * ------------------------------------------------------------------------------
 * date			version		modifier		remarks                   
 * ------------------------------------------------------------------------------
 * 2021/01/01	1.0.0		sheng.zheng		初版
 *
 */
public class TimeSplitUtil {

	/**
	 * 
	 * @methodName		: dateToLocalDateTime
	 * @description	: Date转LocalDateTime
	 * @param date		: Date类型
	 * @return			: LocalDateTime类型
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	public static LocalDateTime dateToLocalDateTime(Date date) {
	    try {
	        Instant instant = date.toInstant();
	        ZoneId zoneId = ZoneId.systemDefault();
	        return instant.atZone(zoneId).toLocalDateTime();
	    } catch (Exception e) {
	    	LogUtil.error(e);
	    }
	    return null;
	}

	/**
	 * 
	 * @methodName		: localDateTimeToDate
	 * @description	: LocalDateTime转Date
	 * @param localDateTime	: LocalDateTime类型
	 * @return			: Date类型
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	public static Date localDateTimeToDate(LocalDateTime localDateTime) {
	    try {
	        ZoneId zoneId = ZoneId.systemDefault();
	        ZonedDateTime zdt = localDateTime.atZone(zoneId);
	        return Date.from(zdt.toInstant());
	    } catch (Exception e) {
	    	LogUtil.error(e);
	    }
	    return null;
	}
	
	/**
	 * 
	 * @methodName		: getPeriodStartTime
	 * @description	: 获取期限开始时间
	 * @param refTime	: 参考时间
	 * @param periodType	: 期数类型：1:year,2:month,3:week,4:day,5:half month
	 * @param periodCnt	: 期数
	 * @return			: 期限开始时间
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	public static LocalDateTime getPeriodStartTime(LocalDateTime refTime, byte periodType, int periodCnt) {	
		LocalDateTime ldt = null;
		switch(periodType) {
		case 1:
		{
			// year
			// 当年1月1日
			ldt = LocalDateTime.of(refTime.getYear() - periodCnt,1,1, 0, 0, 0);			
		}
			break;
		case 2:
		{
			// month
			// 当月1日
			ldt = LocalDateTime.of(refTime.getYear(),refTime.getMonthValue(),1, 0, 0, 0);	
			// 减去periodCnt月
			if (periodCnt > 0) {
				ldt = ldt.minusMonths(periodCnt);				
			}
		}
			break;
		case 3:
		{
			// week
			// 获取当周的第几天,周一值为1，周日为7
			int dayofweek = refTime.getDayOfWeek().getValue();
			// 获取当天日期
			ldt = LocalDateTime.of(refTime.getYear(), refTime.getMonthValue(),refTime.getDayOfMonth(),0,0,0);
			if(dayofweek > 1) {
				// 当周的第一天
				ldt = ldt.minusDays(dayofweek - 1);
			}
			// offset periodCnt 周
			if (periodCnt > 0) {
				ldt = ldt.minusDays(periodCnt * 7);
			}
		}
			break;
		case 4:
		{
			// day
			// 当天
			ldt = LocalDateTime.of(refTime.getYear(),refTime.getMonthValue(),refTime.getDayOfMonth(), 0, 0, 0);
			if (periodCnt > 0) {
				ldt = ldt.minusDays(periodCnt);
			}
		}
			break;
		case 5:
		{
			// half month
			int dayOfMonth = refTime.getDayOfMonth();
			if (dayOfMonth > 15) {
				// 下半月
				dayOfMonth = 16;
			}else {
				// 上半月
				dayOfMonth = 1;
			}
			ldt = LocalDateTime.of(refTime.getYear(),refTime.getMonthValue(),dayOfMonth, 0, 0, 0);
			if (periodCnt > 0) {
				if (periodCnt % 2 == 0) {
					ldt = ldt.minusMonths(periodCnt/2);
				}else {
					if (dayOfMonth > 15) {
						// 调整月份
						ldt = ldt.minusMonths(periodCnt/2);
						// 日期-15天
						ldt = ldt.minusDays(15);
					}else {
						// 多调整1个月
						ldt = ldt.minusMonths(periodCnt/2+1);
						// 日期+15天
						ldt = ldt.plusDays(15);
					}
				}
			}
		}
			break;
		default:
			ldt = LocalDateTime.of(refTime.getYear(),refTime.getMonthValue(),refTime.getDayOfMonth(), 0, 0, 0);
			break;
		}
		return ldt;
	}
	
	/**
	 * 
	 * @methodName		: getFitableStartTime
	 * @description	: 取得合适的分表开始时间
	 * @param refTime	: 参考时间
	 * @param thresholdPeriodType	: 期数类型：1:year,2:month,3:week,4:day,5:half month
	 * @param thresholdCnt	: 分表期数
	 * @return			: 分表的开始时间
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	public static LocalDateTime getFitableStartTime(LocalDateTime refTime, byte thresholdPeriodType, int thresholdCnt) {
		LocalDateTime ldt = null;
		if (thresholdCnt == 0) {
			return null;
		}
		switch(thresholdPeriodType) {
		case 1:
		{
			// year
			// 当年的1月1日
			ldt = LocalDateTime.of(refTime.getYear() - thresholdCnt,1,1, 0, 0, 0);	
		}
			break;
		case 2:
		{
			// month
			int count = (refTime.getMonthValue()-1) / thresholdCnt;
			// 当年的1月1日
			ldt = LocalDateTime.of(refTime.getYear(),1,1, 0, 0, 0);
			if (count > 0) {
				ldt = ldt.plusMonths(thresholdCnt * count);
			}			
		}
			break;
		case 3:
		{
			// week
			WeekFields weekFields = WeekFields.of(DayOfWeek.MONDAY,1);
			// 获取1年中的第几周
			int weeks = refTime.get(weekFields.weekOfYear());
			// 获取当周的第几天,周一值为1，周日为7
			int dayofweek = refTime.getDayOfWeek().getValue();
			// 获取当天日期
			ldt = LocalDateTime.of(refTime.getYear(), refTime.getMonthValue(),refTime.getDayOfMonth(),0,0,0);
			if(dayofweek > 1) {
				// 当周的第一天
				ldt = ldt.minusDays(dayofweek - 1);
			}
			// 取得当年第一周的日期
			ldt = ldt.minusDays((weeks -1) * 7);
			// 计算thresholdCnt个周数
			int count = (weeks - 1) / thresholdCnt;
			if (count > 0) {
				ldt = ldt.plusDays(thresholdCnt * 7 * count);
			}			
		}
			break;
		case 4:
		{
			// day
			// 当年1月1日
			ldt = LocalDateTime.of(refTime.getYear(),1,1, 0, 0, 0);
			// 计算日期差
			Duration duration = Duration.between(ldt,refTime);
			int days = (int)duration.toDays();
			int count = days / thresholdCnt;
			if (count > 0) {
				ldt = ldt.plusDays(thresholdCnt * count);
			}			
		}
			break;
		case 5:
		{
			// half month
			// 当年1月1日
			ldt = LocalDateTime.of(refTime.getYear(),1,1, 0, 0, 0);
			int dayOfMonth = refTime.getDayOfMonth();
			int months = refTime.getMonthValue();
			int half = (months - 1) * 2;
			if (dayOfMonth > 15) {
				half += 1;
			}
			int count = half / thresholdCnt;
			if (count > 0) {
				if (count % 2 == 0) {
					ldt = ldt.plusMonths(count / 2 * thresholdCnt);	
				}else {
					ldt = ldt.plusMonths(count / 2 * thresholdCnt);
					// +15天
					ldt = ldt.plusDays(15);
				}				
			}				
		}
			break;
		default:
			ldt = LocalDateTime.of(refTime.getYear(),refTime.getMonthValue(),refTime.getDayOfMonth(), 0, 0, 0);
			break;
		}
		return ldt;
	}
	
	/**
	 * 
	 * @methodName		: getTimeNo
	 * @description	: 获取历史表的日期编号
	 * @param refTime	: 参考时间
	 * @param thresholdPeriodType	: 期数类型：1:year,2:month,3:week,4:day,5:half month
	 * @return			: 编号值
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	public static int getTimeNo(LocalDateTime refTime,byte thresholdPeriodType) {
		int timeNo = 0;
		switch(thresholdPeriodType) {
		case 1:
			timeNo = refTime.getYear();
			break;
		case 2:
			timeNo = refTime.getYear()*100 + refTime.getMonthValue();
			break;
		case 3:
		case 4:
		case 5:
			timeNo = (refTime.getYear()*100 + refTime.getMonthValue())*100 + refTime.getDayOfMonth();
			break;
		default:
			timeNo = (refTime.getYear()*100 + refTime.getMonthValue())*100 + refTime.getDayOfMonth();
			break;			
		}
		return timeNo;
	}
	
	/**
	 * 
	 * @methodName		: parseStringToDateTime
	 * @description	: 根据日期时间的格式，将字符串转换为LocalDateTime类型
	 * @param time		: 日期时间的字符串
	 * @param format	: 格式
	 * @return			: LocalDateTime对象
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	public static LocalDateTime parseStringToDateTime(String time, String format) {
		DateTimeFormatter df = DateTimeFormatter.ofPattern(format);
		return LocalDateTime.parse(time, df);
	}	
	
	private static String[] parsePatterns = {"yyyy-MM-dd","yyyy年MM月dd日",
	        "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy/MM/dd",
	        "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyyMMdd"};

	/**
	 * 
	 * @methodName		: parseDateTime
	 * @description	: 尝试解析日期字符串
	 * @param src		: 待解析的字符串
	 * @return			: 解析成功返回LocalDateTime对象
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2021/01/01	1.0.0		sheng.zheng		初版
	 *
	 */
	public static LocalDateTime parseDateTime(String src) {
	    if (src == null) {
	        return null;
	    }
    	LocalDateTime retValue = null;
    	for(int i = 0; i < parsePatterns.length; i++) { 
    		String format = parsePatterns[i];
    	    try {
    	    	retValue = parseStringToDateTime(src,format);
    	    	break;
    	    } catch (Exception e) {
    	        continue;
    	    }    		
    	}
    	
    	return retValue;
	}		

	/**
	 * 
	 * @methodName		: long2LocalDateTime
	 * @description	: 将时间戳转为LocalDateTime类型
	 * @param ts		: 时间戳
	 * @return			: LocalDateTime类型
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2022/06/15	1.0.0		sheng.zheng		初版
	 *
	 */
	public static LocalDateTime long2LocalDateTime(long ts) {
		LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(ts), ZoneId.of("+8"));
		return localDateTime;
	}
	
	/**
	 * 
	 * @methodName		: format
	 * @description	: 格式化
	 * @param ldt		: LocalDateTime类型对象
	 * @param formatter	: 格式化串
	 * @return			: 格式化后的字符串
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2022/06/15	1.0.0		sheng.zheng		初版
	 *
	 */
	public static String format(LocalDateTime ldt,String formatter) {
		DateTimeFormatter df = DateTimeFormatter.ofPattern(formatter);
		String strLdt = ldt.format(df);
		return strLdt;
	}
	
	/**
	 * 
	 * @methodName		: parseDateTime
	 * @description	: 将字符串转为LocalDateTime对象
	 * @param src		: 日期时间字符串
	 * @param formatter	: 格式化串
	 * @return			: LocalDateTime类型对象
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2022/06/15	1.0.0		sheng.zheng		初版
	 *
	 */
	public static LocalDateTime parseDateTime(String src,String formatter) {
		LocalDateTime retValue = null;
		DateTimeFormatter df = DateTimeFormatter.ofPattern(formatter);
	    try {
	    	retValue = LocalDateTime.parse(src,df);
	    } catch (Exception e) {
	    } 	
	    return retValue;
	}

	/**
	 * 
	 * @methodName		: format
	 * @description	: 格式化
	 * @param ldt		: LocalDate类型对象
	 * @param formatter	: 格式化串
	 * @return			: 格式化后的字符串
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2022/06/15	1.0.0		sheng.zheng		初版
	 *
	 */
	public static String format(LocalDate ld,String formatter) {
		DateTimeFormatter df = DateTimeFormatter.ofPattern(formatter);
		String strLdt = ld.format(df);
		return strLdt;
	}
	
	/**
	 * 
	 * @methodName		: parseDateTime
	 * @description	: 将字符串转为LocalDate对象
	 * @param src		: 日期时间字符串
	 * @param formatter	: 格式化串
	 * @return			: LocalDate类型对象
	 * @history		:
	 * ------------------------------------------------------------------------------
	 * date			version		modifier		remarks                   
	 * ------------------------------------------------------------------------------
	 * 2022/06/15	1.0.0		sheng.zheng		初版
	 *
	 */
	public static LocalDate parseDate(String src,String formatter) {
		LocalDate retValue = null;
		DateTimeFormatter df = DateTimeFormatter.ofPattern(formatter);
	    try {
	    	retValue = LocalDate.parse(src,df);
	    } catch (Exception e) {
	    } 	
	    return retValue;
	}		
}
