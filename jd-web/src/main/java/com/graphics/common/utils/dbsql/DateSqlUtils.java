package com.graphics.common.utils.dbsql;

import java.util.Date;

import com.graphics.common.utils.DateUtils;
import com.graphics.common.utils.StringUtils;
import com.graphics.common.config.Global;


/**
 * 日期sql条件生成 如:
 * mysql:
 * oracle:
 */
public class DateSqlUtils {
	
	/**
	 * 生成日期比较的sql语句
	 * @param columnName  列名 
	 * @param date 日期
	 * @return  null 数据有误
	 */
	public static String getDateString(String columnName,Date date,String compara,String format){
		if(StringUtils.isBlank(format)){
			format="yyyymmdd";
		}
		if(StringUtils.isBlank(columnName)||date==null){
			return "";
		}
		String sql="";
		String dateStr = DateUtils.formatDateTime(date);
		format=getFormatPatten(format);
		if("mysql".equals(Global.getJdbcType())){
			sql=" date_format("+columnName.trim()+",'"+format+"') "+compara+" date_format(str_to_date('"
								+ dateStr
								+ "','%Y-%m-%d %H:%i:%s'),'"+format+"') ";
		}else{
			sql=" to_char("+columnName.trim()+",'"+format+"') "+compara+" to_char(to_date('"
					+ dateStr + "','yyyy-MM-dd HH24:mi:ss'),'"+format+"') ";
		}
		return sql;
	}
	
	/**
	 * 生成日期比较的sql语句
	 * @param columnName  列名 
	 * @param date 日期
	 * @return  null 数据有误
	 */
	public static String getDateString(String columnName,String dateStr,String compara,String format){
		if(StringUtils.isBlank(format)){
			format="yyyymmdd";
		}
		if(StringUtils.isBlank(columnName)||dateStr==null){
			return "";
		}
		String sql="";
		format=getFormatPatten(format);
		if("mysql".equals(Global.getJdbcType())){
			sql=" date_format("+columnName.trim()+",'"+format+"') "+compara+" date_format(str_to_date('"
								+ dateStr
								+ "','%Y-%m-%d %H:%i:%s'),'"+format+"') ";
		}else{
			sql=" to_char("+columnName.trim()+",'"+format+"') "+compara+" to_char(to_date('"
					+ dateStr + "','yyyy-MM-dd HH24:mi:ss'),'"+format+"') ";
		}
		return sql;
	}
	
	
	public static String getDateYMD(String dateStr){
		if(StringUtils.isBlank(dateStr)){
			return "";
		}
		String sql="";
		if("mysql".equals(Global.getJdbcType())){
			sql=" date_format('"+ dateStr+ "','%Y-%m-%d') ";
		}else{
			sql=" to_date('"+dateStr+"','yyyy-MM-dd') ";
		}
		return sql;
	}
	
	public static String getStringYMD(String dateStr){
		if(StringUtils.isBlank(dateStr)){
			return "";
		}
		String sql="";
		if("mysql".equals(Global.getJdbcType())){
			sql="str_to_date("+ dateStr+ ",'%Y-%m-%d')";
		}else{
			sql="to_char("+dateStr+",'yyyy-MM-dd')";
		}
		return sql;
	}
	
	/**
	 * 日期 update语句
	 * @param columnName
	 * @param date
	 * @return
	 */
	public static String getUpdateString(String columnName,Date date){
		if(StringUtils.isBlank(columnName)||date==null){
			return "";
		}
		String sql="";
		String dateStr = DateUtils.formatDateTime(date);
		if("mysql".equals(Global.getJdbcType())){
			sql= " "+columnName+" = str_to_date('"+dateStr+"','%Y-%m-%d %H:%i:%s') ";
		}else{
			sql = " "+columnName+" = to_date('"+dateStr+"' , 'yyyy-mm-dd hh24:mi:ss') ";
		}
		return sql;
	}
	
	/**
	 * 日期 update语句
	 * @param columnName
	 * @param date
	 * @return
	 */
	public static String getUpdateString(String columnName,String dateStr){
		if(StringUtils.isBlank(columnName)||dateStr==null){
			return "";
		}
		String sql="";
		//String dateStr = DateUtils.formatDateTime(date);
		if("mysql".equals(Global.getJdbcType())){
			sql= " "+columnName+" = str_to_date('"+dateStr+"','%Y-%m-%d %H:%i:%s') ";
		}else{
			sql = " "+columnName+" = to_date('"+dateStr+"' , 'yyyy-mm-dd hh24:mi:ss') ";
		}
		return sql;
	}
	/**
	 * hour大于0 计算几个小时前
	 * hour小于0  计算几个小时后
	 *  如果hour为空或者0 则返回当前世界
	 * @param hour
	 * @return
	 */
	public static String getFormatTime(Integer hour){
		String sql="";
		if("mysql".equals(Global.getJdbcType())){
			if(hour!=null&&hour!=0){
				sql=" SUBTIME(now(),'"+hour+":00:00') ";
			}else{
				sql=" now() ";
			}
		}else{
			if(hour>0){				
				sql=" SYSDATE - "+hour+"/"+24+" ";
			}else if(hour<0){
				sql=" sysdate +"+hour+"/"+24+" ";
			}else{
				sql=" sysdate ";
			}
		}
		return sql;
	}
	
	/**
	 * trunc DATE_FORMAT 函数
	 * @param date
	 * @param format
	 * @return
	 */
	public static String formatDateSql(String date, String format){
		if(StringUtils.isBlank(date)){
			return "";
		}
		String sql="";
		format=getFormatPatten(format);
		if("mysql".equals(Global.getJdbcType())){
			sql="DATE_FORMAT(str_to_date('"+date+"','"+format+"'), '%Y-%m-%d %H:%i:%s')";
		}else{
			sql="trunc( to_date ('"+date+"','"+format+"'))";
		}
		return sql;
	}
	
	/**
	 * 格式化日期格式
	 * @param format
	 * @return
	 */
	private static String getFormatPatten(String format){
		if(StringUtils.isBlank(format)){
			return "";
		}else{
			if("mysql".equals(Global.getJdbcType())){
				format=format.replace("yyyy", "%Y");
				format=format.replace("YYYY", "%Y");
				format=format.replace("mm", "%m");
				format=format.replace("MM", "%m");
				format=format.replace("dd", "%d");
				format=format.replace("DD", "%d");
				format=format.replace("HH", "%H");
				format=format.replace("hh", "%H");
				format=format.replace("ii", "%i");
				format=format.replace("II", "%i");
				format=format.replace("ss", "%s");
				format=format.replace("SS", "%s");
			}else{
				format=format.replace("hh", "hh24");
				format=format.replace("HH", "hh24");
				format=format.replace("ii", "mi");
				format=format.replace("II", "mi");
			}
		}
		return format;
	}
}
