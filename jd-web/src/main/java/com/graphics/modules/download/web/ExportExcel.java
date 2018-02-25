package com.graphics.modules.download.web;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.graphics.modules.download.service.ExportExcelService;

@RestController
@RequestMapping("${adminPath}/download/exportExcel")
public class ExportExcel {
	@Autowired
	private ExportExcelService exportExcelService;
	@RequestMapping(value = "/financeOrderExcel")
	public void financeOrderExcel(HttpServletResponse response){
		exportExcelService.export(response);
	}
	@RequestMapping(value = "/courseOrderExcel")
	public void courseOrderExcel(HttpServletResponse response){
		exportExcelService.courseExport(response);
	}
}
