package com.graphics.modules.download.service;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.graphics.common.service.BaseService;

@Service
@Transactional(readOnly = true)
public class ExportExcelService extends BaseService {
	public void export(HttpServletResponse response){
		try {
			String fileName = "曲目订单.xlsx";
			String destFilePath = "";
			String os = System.getProperty("os.name");
			if(os.toLowerCase().startsWith("win")){
				destFilePath = "D:\\orderExcel.xlsx";
			}else{
				destFilePath = "/home/orderExcel.xlsx";
			}
			download(response, destFilePath, fileName);
			if(os.toLowerCase().startsWith("win")){  
				//删除服务器上文件
				File fileExists=new File("D:\\orderExcel.xlsx");    
				if(fileExists.exists()){      
					fileExists.delete();    	
				}
			}else{
				//删除服务器上文件
				File fileExists=new File("/home/orderExcel.xlsx");    
				if(fileExists.exists()){      
					fileExists.delete();    	
				}
			}
		} catch (Exception e) {
			
		}
	}
	
	/**
	 * 课程订单Excel下载
	 * @param response
	 */
	public void courseExport(HttpServletResponse response){
		try {
			String fileName = "课程订单.xlsx";
			String destFilePath = "";
			String os = System.getProperty("os.name");
			if(os.toLowerCase().startsWith("win")){
				destFilePath = "D:\\courseOrderExcel.xlsx";
			}else{
				destFilePath = "/home/courseOrderExcel.xlsx";
			}
			download(response, destFilePath, fileName);
			if(os.toLowerCase().startsWith("win")){  
				//删除服务器上文件
				File fileExists=new File("D:\\courseOrderExcel.xlsx");    
				if(fileExists.exists()){      
					fileExists.delete();    	
				}
			}else{
				//删除服务器上文件
				File fileExists=new File("/home/courseOrderExcel.xlsx");    
				if(fileExists.exists()){      
					fileExists.delete();    	
				}
			}
		} catch (Exception e) {
			
		}
	}
	
	/**
	 * 下载Excel文件方法
	 * 
	 * @param response
	 * @param destFilePath
	 *            文件路径
	 * @param fileName
	 *            文件名称
	 * @throws IOException
	 */
	public static void download(HttpServletResponse response,
			String destFilePath, String fileName) throws IOException {
		OutputStream os = response.getOutputStream();
		try {
			File file = new File(destFilePath);
			// 以流的形式下载文件。
			InputStream fis = new BufferedInputStream(new FileInputStream(
					destFilePath));
			byte[] buffer = new byte[fis.available()];
			fis.read(buffer);
			fis.close();
			response.reset();
			// 设置response的Header
			/*
			 * response.addHeader("Content-Disposition", "attachment;filename="
			 * + new String(fileName.getBytes(), "UTF-8"));
			 */
			String filename = new String(fileName.getBytes("gbk"), "iso8859-1");
			response.setHeader("Content-Disposition", "attachment; filename=\""
					+ filename + "\"");
			response.addHeader("Content-Length", "" + file.length());
			response.setContentType("application/octet-stream");
			os.write(buffer);
			os.flush();
		} finally {
			if (os != null) {
				os.close();
			}
		}
	}
}
