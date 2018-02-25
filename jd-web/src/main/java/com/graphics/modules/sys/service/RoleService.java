package com.graphics.modules.sys.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.graphics.common.utils.StringUtils;
import com.google.common.collect.Lists;
import com.graphics.common.persistence.Page;
import com.graphics.common.service.BaseService;
import com.graphics.common.service.ServiceException;
import com.graphics.common.web.ApiCode;
import com.graphics.modules.sys.dao.MenuDao;
import com.graphics.modules.sys.dao.RoleDao;
import com.graphics.modules.sys.dao.RoleMenuDao;
import com.graphics.modules.sys.entity.Menu;
import com.graphics.modules.sys.entity.Office;
import com.graphics.modules.sys.entity.Role;
import com.graphics.modules.sys.entity.RoleMenu;

/**
 * 角色Service
 */
@Service
@Transactional(readOnly = true)
public class RoleService extends BaseService {

	@Autowired
	private RoleDao roleDao;
	@Autowired
	private RoleMenuDao roleMenuDao;
	@Autowired
	private MenuDao menuDao;
	
	public Role get(String id) {
		return roleDao.get(id);
	}
	
	/**
	 * 查找所有角色
	 * @return
	 */
	public List<Role> findAll(){
		List<Role> list = roleDao.findAll();
		return list;
	}
	
	public List<Menu> findMenusByRoleId(String roleId){
		return roleMenuDao.findMenusByRoleId(roleId);
	}
	public List<RoleMenu> findRoleMenuByRoleId(String roleId){
		List<String> munuIds = new ArrayList<String>();
//		List<Menu> menuList = Lists.newArrayList();
		List<Menu> all = menuDao.findAll("level asc,sort asc");
		//Map<String, Menu> menuMap = new HashMap<String, Menu>();
		Map<String, Menu> menuMapPid = new HashMap<String, Menu>();
		for(Menu menu:all){
			//menuMap.put(menu.getId(), menu);
			menuMapPid.put(menu.getPid(), menu);
		}
		for(Menu menu:all){
			Menu munu = menuMapPid.get(menu.getId());
			if(munu == null){
				munuIds.add(menu.getId());
			}
			/*if("0".equals(menu.getPid())){
//				menuList.add(menu);
			}else{
				Menu me = menu;
				String menuId = "";
				boolean flag = false;
				Menu parent = menuMap.get(menu.getId());
				while(true){
					
					if(parent!=null){
						if("0".equals(parent.getPid())){
							if(){
								
							}
							munuIds.add(menu.getId());
							menuId = menu.getId();
						}else{
							parent = menuMap.get(menu.getId());
						}
					}else{
						break;
					}
				}
				if(flag){
					munuIds.add(menu.getId());
				}
				
			}*/
		}
		
		List<RoleMenu> RoleMenus = new ArrayList<RoleMenu>();
		List<RoleMenu> list = roleMenuDao.findAllByRoleId(roleId);
		for(RoleMenu l : list){
			for(String munuId : munuIds){
				if((l.getMenuId()).equals(munuId)){
					RoleMenus.add(l);
					break;
				}
			}
		}
		return RoleMenus;
	}
	
	public Page<Role> find(Page<Role> page, Role role) {
		DetachedCriteria dc = roleDao.createDetachedCriteria();
		if (StringUtils.isNotEmpty(role.getName())) {
			dc.add(Restrictions.like("name", "%" + role.getName() + "%"));
		}
		
		dc.add(Restrictions.eq(Office.FIELD_DEL_FLAG, Office.DEL_FLAG_NORMAL));
		return roleDao.find(page, dc);
	}
	
	
	@Transactional(readOnly = false)
	public void save(Role role) {
		roleDao.save(role);
	}
	
	/**
	 * 新增角色
	 * @param name	名称
	 * @permissions 角色菜单集合
	 * @throws ServiceException
	 */
	@Transactional(readOnly = false)
	public void create(String name,List<String> permissions) throws ServiceException{
		
		if(StringUtils.isBlank(name)){
			throw new ServiceException(ApiCode.PERMISSION_ROLE_NAME_REQUIRED);
		}
		//创建角色
		Role role = new Role();
		role.setName(name);
		role.setPid("0");
		roleDao.save(role);
		
		//创建角色菜单
		if(permissions!=null){
			
			Set<String> parentNode = new HashSet<String>();
			List<String> vessel = new ArrayList<String>(permissions);
			List<Menu> all = menuDao.findAll("level asc,sort asc");
			Map<String, Menu> menuMap = new HashMap<String, Menu>();
			for(Menu menu:all){
				menuMap.put(menu.getId(), menu);
			}
			for(String v : vessel){
				for(Menu menu:all){
					if(v.equals(menu.getId())){
						String menuId = menu.getPid();
						while(true){
							if("0".equals(menuId)){
								break;
							}else{
								Menu parent = menuMap.get(menuId);
								if(parent!=null){
									menuId = parent.getPid();
									parentNode.add(parent.getId());
								}else{
									break;
								}
							}
						}	
					}
				}
			}
			
			for(String p : parentNode){
				if(!permissions.contains(p)){
					permissions.add(p);
				}
			}
			
			List<RoleMenu> rmList = Lists.newArrayList();
			for(String permission:permissions){
				RoleMenu rm = new RoleMenu();
				rm.setMenuId(permission);
				rm.setRoleId(role.getId());
				rmList.add(rm);
			}
			roleMenuDao.save(rmList);
		}
	}
	
	/**
	 * 更新角色
	 * @param id
	 * @param name
	 * @param permissions 如果permissions为空会删除角色的菜单
	 * @throws ServiceException
	 */
	@Transactional(readOnly = false)
	public void update(String id,String name,List<String> permissions) throws ServiceException{
		
		if(StringUtils.isBlank(name)){
			throw new ServiceException(ApiCode.PERMISSION_ROLE_NAME_REQUIRED);
		}
		
		Role role = roleDao.get(id);
		if(role==null){
			throw new ServiceException(ApiCode.PERMISSION_ROLE_NOTEXIST);
		}
		
		role.setName(name);
		roleDao.save(role);
		
		if(permissions != null){
			Set<String> parentNode = new HashSet<String>();
			List<String> vessel = new ArrayList<String>(permissions);
			List<Menu> all = menuDao.findAll("level asc,sort asc");
			Map<String, Menu> menuMap = new HashMap<String, Menu>();
			for(Menu menu:all){
				menuMap.put(menu.getId(), menu);
			}
			for(String v : vessel){
				for(Menu menu:all){
					if(v.equals(menu.getId())){
						String menuId = menu.getPid();
						while(true){
							if("0".equals(menuId)){
								break;
							}else{
								Menu parent = menuMap.get(menuId);
								if(parent!=null){
									menuId = parent.getPid();
									parentNode.add(parent.getId());
								}else{
									break;
								}
							}
						}	
					}
				}
			}
			
			for(String p : parentNode){
				if(!permissions.contains(p)){
					permissions.add(p);
				}
			}
		}
		
		//更新角色菜单
		List<RoleMenu> roleMenuList = roleMenuDao.findAllByRoleId(id);
		List<RoleMenu> deleteList = Lists.newArrayList();//需要删除的角色菜单
		List<RoleMenu> addList = Lists.newArrayList();//需要新增的角色菜单
		if(permissions==null){
			deleteList.addAll(roleMenuList);
			//删除全部
			for(RoleMenu rm:roleMenuList){
				rm.setDelFlag(RoleMenu.DEL_FLAG_DELETE);//删除
				deleteList.add(rm);
			}
		}else{
			//确定需要新增的角色菜单
			for(String permission:permissions){
				boolean has = false;
				for(RoleMenu rm:roleMenuList){
					if(rm.getMenuId().equals(permission)){
						has = true;
						break;
					}
				}
				if(!has){//需要新增
					RoleMenu rm = new RoleMenu();
					rm.setRoleId(role.getId());
					rm.setMenuId(permission);
					addList.add(rm);
				}
			}
			
			//确定需要删除的菜单
			for(RoleMenu rm:roleMenuList){
				boolean has = false;
				for(String permission:permissions){
					if(rm.getMenuId().equals(permission)){
						has = true;
						break;
					}
				}
				if(!has){
					rm.setDelFlag(RoleMenu.DEL_FLAG_DELETE);//删除
					deleteList.add(rm);
				}
			}
		}
		

		
		roleMenuDao.save(addList);
		roleMenuDao.save(deleteList);
		
	}
	
	@Transactional(readOnly = false)
	public void delete(String roleId){
		roleDao.deleteById(roleId);
		roleMenuDao.deleteByRoleId(roleId);
	}
	
}
