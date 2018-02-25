package com.graphics.modules.sys.web;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.graphics.common.utils.DynamicBean;
import com.graphics.common.persistence.Page;
import com.graphics.common.service.ServiceException;
import com.graphics.common.web.ApiCode;
import com.graphics.common.web.ApiData;
import com.graphics.common.web.BaseController;
import com.graphics.modules.sys.entity.Office;
import com.graphics.modules.sys.service.OfficeService;

/**
 * 部门Controller
 */
@RestController
@RequestMapping("${adminPath}/system/office")
public class OfficeController extends BaseController {
	@Autowired
	private OfficeService officeService;

	@RequiresPermissions("permission:office:view")
	@RequestMapping(value = "/get")
	public ApiData<Office> get(String id, HttpServletRequest request,
			HttpServletResponse response) {
		Office office = officeService.get(id);
		ApiData<Office> apiData = apiReturn(1, ApiCode.SUCCESS, office);
		return apiData;
	}

	/**
	 * 获取所有部门
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/all")
	public ApiData<List<Office>> all(HttpServletRequest request,
			HttpServletResponse response) {
		List<Office> officeList = officeService.allListTree();
		ApiData<List<Office>> apiData = apiReturn(1, ApiCode.SUCCESS,
				officeList);
		return apiData;
	}

	/**
	 * 搜索部门
	 * 
	 * @param name
	 * @param pid
	 * @param start
	 *            分页查询开始位置
	 * @param length
	 *            长度
	 * @return
	 */
	@RequiresPermissions("permission:office:view")
	@RequestMapping(value = "/search")
	public ApiData<Object> search(String name, String pid,
			@RequestParam(value = "start", defaultValue = "0") int start,
			@RequestParam(value = "length", defaultValue = "10") int length) {

		// 设置查询条件
		Page<Office> page = new Page<Office>();
		page.setFirstResult(start);
		page.setPageSize(length);
		page.setOrderBy("code");
		Office office = new Office();
		office.setName(name);
		office.setPid(pid);

		// 查询
		page = officeService.find(page, office);

		// 构造返回数据
		List<Object> officeList = new ArrayList<Object>();
		for (Office o : page.getList()) {
			Object officeBean = new DynamicBean.Builder().setPV("id", o.getId())
					.setPV("name", o.getName())
					.setPV("code", o.getCode())
					.setPV("collapsed",true)
					.build().getObject();
			officeList.add(officeBean);
		}
		
        Object data = new DynamicBean.Builder()
        		.setPV("total", page.getCount())
        		.setPV("count", page.getCount())
				.setPV("data", officeList, List.class)
				.build().getObject();
        
		ApiData<Object> apiData = apiReturn(1, ApiCode.SUCCESS,data);

		return apiData;
	}

	/**
	 * 删除部门 同时删除所有子部门
	 * 
	 * @param id
	 * @return
	 */
	@RequiresPermissions("permission:office:del")
	@RequestMapping(value = "/delete")
	public ApiData<Object> delete(String id) {
		officeService.delete(id);
		// 构造响应数据
		ApiData<Object> apiData = apiReturn(1, ApiCode.SUCCESS, null);
		return apiData;
	}

	/**
	 * 新建部门
	 * 
	 * @param name
	 * @param pid
	 * @param sort
	 * @return
	 */
	@RequiresPermissions("permission:office:add")
	@RequestMapping(value = "/create")
	public ApiData<Object> create(String name,
			@RequestParam(value = "pid", defaultValue = "0") String pid,
			@RequestParam(value = "sort", defaultValue = "999") int sort) {
		try {
			officeService.create(pid, sort, name);
		} catch (ServiceException ex) {
			ApiData<Object> apiData = apiReturn(0, ex.getApiCode(), null);
			return apiData;
		}
		ApiData<Object> apiData = apiReturn(1, ApiCode.SUCCESS, null);
		return apiData;
	}

	/**
	 * 修改部门，如果修改了pid则所有自部门的code和level都会发生响应修改
	 * 
	 * @param id
	 * @param name
	 * @param sort
	 * @param pid
	 *            如果为空则不修改pid
	 * @return
	 */
	@RequiresPermissions("permission:office:edit")
	@RequestMapping(value = "/update")
	public ApiData<Object> update(String id, String name,
			@RequestParam(value = "sort", defaultValue = "999") int sort,
			String pid) {
		try {
			officeService.update(id, name, sort, pid);
		} catch (ServiceException ex) {
			ApiData<Object> apiData = apiReturn(0, ex.getApiCode(), null);
			return apiData;
		}
		ApiData<Object> apiData = apiReturn(1, ApiCode.SUCCESS, null);
		return apiData;
	}

}
