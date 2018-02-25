package com.graphics.common.utils.dbsql;

import com.graphics.common.config.Global;

/**
 * 分页sql条件生成 如:
 * mysql:
 * oracle:
 */
public class PageSqlUtils {
	
	/**
     * 将sql变成分页sql语句,提供将offset及limit使用占位符号(placeholder)替换.
     * <pre>
     * 如mysql
     * dialect.getLimitString("select * from user", 12, ":offset",0,":limit") 将返回
     * select * from user limit :offset,:limit
     * </pre>
     *
     * @param sql               实际SQL语句
     * @param offset            分页开始纪录条数
     * @param offsetPlaceholder 分页开始纪录条数－占位符号
     * @param limitPlaceholder  分页纪录条数占位符号
     * @return 包含占位符的分页sql
     */
	public static String getLimitString(String sql, boolean offset, String offsetPlaceholder, String limitPlaceholder) {
		
		if("mysql".equals(Global.getJdbcType())){
			StringBuilder stringBuilder = new StringBuilder(sql);
			stringBuilder.append(" limit ");
	        if (offset) {
	            stringBuilder.append(offsetPlaceholder).append(",").append(limitPlaceholder);
	        } else {
	            stringBuilder.append(limitPlaceholder);
	        }
	        return stringBuilder.toString();
		}else{
			sql = sql.trim();
	        boolean isForUpdate = false;
	        if (sql.toLowerCase().endsWith(" for update")) {
	            sql = sql.substring(0, sql.length() - 11);
	            isForUpdate = true;
	        }
	        StringBuilder pagingSelect = new StringBuilder(sql.length() + 100);

	        if (offset) {
				pagingSelect.append("select * from ( select row_.*, rownum rownum_ from ( ");
			} else {
				pagingSelect.append("select * from ( ");
			}
			pagingSelect.append(sql);
			if (offset) {
				String endString = offsetPlaceholder + "+" + limitPlaceholder;
				pagingSelect.append(" ) row_ where rownum <= "+endString+") where rownum_ > ").append(offsetPlaceholder);
			} else {
				pagingSelect.append(" ) where rownum <= "+limitPlaceholder);
			}

	        if (isForUpdate) {
	            pagingSelect.append(" for update");
	        }

	        return pagingSelect.toString();
		}
		 
	}
}
