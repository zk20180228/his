package cn.honry.oa.portalWidget.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.OaPortalWidget;
import cn.honry.inner.system.utli.OperationUtils;
import cn.honry.oa.portalWidget.dao.PortalWidgetDAO;
import cn.honry.oa.portalWidget.service.PortalWidgetService;
import cn.honry.utils.DateUtils;
import cn.honry.utils.ShiroSessionUtils;

@Service("portalWidgetService")
@Transactional
@SuppressWarnings({ "all" })
public class PortalWidgetServiceImpl implements PortalWidgetService{
	/** 频次数据库操作类 **/
	@Autowired
	@Qualifier(value = "portalWidgetDAO")
	private PortalWidgetDAO portalWidgetDAO;
	
	@Override
	public void saveOrUpdate(OaPortalWidget entity) {
		String longinUserAccount = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
		if(StringUtils.isBlank(entity.getId())){
			//查询最大的id序号,加一,放入ID
			String maxId=portalWidgetDAO.getMaxId();
			entity.setId((Integer.valueOf(maxId)+1)+"");
			entity.setStatus(0);//添加的时候状态为正常
			entity.setCreateUser(longinUserAccount);
			entity.setCreateTime(new Date());
			portalWidgetDAO.save(entity);
			OperationUtils.getInstance().conserve(null,"OA系统首页组件管理","INSERT INTO","T_OA_PORTAL_WIDGET",OperationUtils.LOGACTIONINSERT);

		}else{
			OaPortalWidget oaPortalWidget=portalWidgetDAO.getById(entity.getId());
			Integer oldStatus=oaPortalWidget.getStatus();//原先的状态是正常还是禁用
			oaPortalWidget.setName(entity.getName());
			oaPortalWidget.setUrl(entity.getUrl());
			oaPortalWidget.setMoreUrl(entity.getMoreUrl());
			oaPortalWidget.setViewUrl(entity.getViewUrl());
			oaPortalWidget.setStatus(entity.getStatus());//状态
			oaPortalWidget.setUpdateTime(new Date());
			oaPortalWidget.setUpdateUser(longinUserAccount);
			portalWidgetDAO.save(oaPortalWidget);
			OperationUtils.getInstance().conserve(entity.getId(),"OA系统首页组件管理","UPDATE","T_OA_PORTAL_WIDGET",OperationUtils.LOGACTIONUPDATE);
			if(entity.getStatus()!=oldStatus){//如果状态有所改变,那么要去修改个人首页表内的组件的全局状态
				portalWidgetDAO.updateStatus(oaPortalWidget.getId(),entity.getStatus());//修改个人组件表中对应的组件数据
				OperationUtils.getInstance().conserve(oaPortalWidget.getId(),"OA系统首页管理","UPDATE","T_OA_USER_PORTAL",OperationUtils.LOGACTIONUPDATE);
			}
		}
	}

	@Override
	public void removeUnused(String id) {	
	}

	@Override
	public OaPortalWidget get(String id) {
		OaPortalWidget entity = portalWidgetDAO.getById(id);
		return entity;
	}

	@Override
	public List<OaPortalWidget> query(OaPortalWidget entity,String page,String rows) {
        return portalWidgetDAO.query(entity,page,rows);
	}

	@Override
	public void del(String ids) {
		String longinUserAccount = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
		String[] moreId=ids.split(",");
		for(int i=0;i<moreId.length;i++){
			OaPortalWidget oaPortalWidget=portalWidgetDAO.getById(moreId[i]);
			oaPortalWidget.setStatus(1);//0:正常,1:禁用
			oaPortalWidget.setDeleteTime(new Date());
			oaPortalWidget.setDeleteUser(longinUserAccount);
			portalWidgetDAO.save(oaPortalWidget);
			portalWidgetDAO.deleteLogicById(oaPortalWidget.getId());//关联删除个人组件表中对应的组件数据
			OperationUtils.getInstance().conserve(oaPortalWidget.getId(),"OA系统首页管理","UPDATE","T_OA_USER_PORTAL",OperationUtils.LOGACTIONUPDATE);
		}
		OperationUtils.getInstance().conserve(ids,"OA系统首页组件管理","UPDATE","T_OA_PORTAL_WIDGET",OperationUtils.LOGACTIONUPDATE);
	}

	@Override
	public int getTotal(OaPortalWidget entity) {
		int total = portalWidgetDAO.getTotal(entity);
		return total;
	}

}
