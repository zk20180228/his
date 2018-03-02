package cn.honry.outpatient.updateStack.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.BusinessFrequency;
import cn.honry.base.bean.model.BusinessStack;
import cn.honry.base.bean.model.DepartmentContact;
import cn.honry.outpatient.updateStack.dao.UpdateStackDao;
import cn.honry.outpatient.updateStack.service.UpdateStackService;
import cn.honry.outpatient.updateStack.vo.StackAndStockInfoVo;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.TreeJson;

@Service("updateStackService")
@Transactional
@SuppressWarnings({ "all" })
public class UpdateStackServiceImpl implements UpdateStackService {

	private Map<String , String> map=new HashMap<String, String>();
	@Autowired
  	@Qualifier(value="updateStackDao")
	private UpdateStackDao updateStackDao;
	public void setUpdateStackDao(UpdateStackDao updateStackDao) {
		this.updateStackDao = updateStackDao;
	}

	public BusinessStack get(String arg0) { 
		return null;
	}

	public void removeUnused(String arg0) {
		
	}

	public void saveOrUpdate(BusinessStack arg0) {
		
	}

	/**
	 * 组套树
	 * @author  zhenglin
	 * @createDate： 
	 * @modifier lyy
	 * @modifyDate：2016年4月11日 上午9:37:32
	 * @param： id 组套的id deptId 登录科室的id userid 登录人的id  stackObject(组套对象: 1是财务用，2是医嘱用)  remark(门诊还是住院，收费不区分，医嘱区分)
	 * @modifyRmk：  
	 * @version 1.0
	 */
	@Override
	public List<TreeJson> stackTree(String id,String deptId,String userId,String stackObject,String remark) {
		List<TreeJson> treeJsonList = new ArrayList<TreeJson>();
		if(StringUtils.isBlank(id)){
			Map<String,String> map = null;
			TreeJson treeJson = null;
			String array = "1,2,3";
			String ars[] = array.split(",");
			for (String string : ars) {
				if("1".equals(string)){
					treeJson = new TreeJson();
					map = new HashMap<String,String>();
					treeJson.setId("1");
					treeJson.setText("全院");
					treeJson.setIconCls("icon-2012081511913");
					treeJson.setState("open");
					treeJson.setAttributes(map);
					
					List<TreeJson> cTreeJsonList1 = new ArrayList<TreeJson>();
					List<BusinessStack> stackList= updateStackDao.getStackInfo(null, "1",deptId,userId,stackObject,remark,"ROOT");
					if(stackList!=null && stackList.size()>0){
						TreeJson treeJson1 = null;
						for(BusinessStack businessStack : stackList){
							treeJson1 = new TreeJson();
							treeJson1.setId(businessStack.getId());
							treeJson1.setText(businessStack.getName());
							
							List<TreeJson> cTreeJsonList2 = new ArrayList<TreeJson>();
							List<BusinessStack> stackList2= updateStackDao.getStackInfo(null, "1",deptId,userId,stackObject,remark,businessStack.getId());
							if(stackList2!=null && stackList2.size()>0){
								TreeJson treeJson2 = null;
								for (BusinessStack businessStack2 : stackList2) {
									treeJson2 = new TreeJson();
									treeJson2.setId(businessStack2.getId());
									treeJson2.setText(businessStack2.getName());
									
									List<TreeJson> cTreeJsonList3 = new ArrayList<TreeJson>();
									List<BusinessStack> stackList3= updateStackDao.getStackInfo(null, "1",deptId,userId,stackObject,remark,businessStack2.getId());
									if(stackList3!=null && stackList3.size()>0){
										TreeJson treeJson3 = null;
										for (BusinessStack businessStack3 : stackList3) {
											treeJson3 = new TreeJson();
											treeJson3.setId(businessStack3.getId());
											treeJson3.setText(businessStack3.getName());
											cTreeJsonList3.add(treeJson3);
										}
									}else{
										treeJson1.setState("closed");
									}
									treeJson2.setChildren(cTreeJsonList3);
									cTreeJsonList2.add(treeJson2);
								}
							}else{
								treeJson1.setState("closed");
							}
							treeJson1.setChildren(cTreeJsonList2);
							cTreeJsonList1.add(treeJson1);
						}
						treeJson.setChildren(cTreeJsonList1);
						treeJsonList.add(treeJson);
					}else{
						treeJson.setChildren(cTreeJsonList1);
						treeJsonList.add(treeJson);
					}
				}else if("2".equals(string)){
					treeJson = new TreeJson();
					map = new HashMap<String,String>();
					treeJson.setId("2");
					treeJson.setText("科室");
					treeJson.setIconCls("icon-03");
					treeJson.setState("open");
					treeJson.setAttributes(map);
					
					List<TreeJson> cTreeJsonList1 = new ArrayList<TreeJson>();
					List<BusinessStack> stackList= updateStackDao.getStackInfo(null, "2",deptId,userId,stackObject,remark,"ROOT");
					if(stackList!=null && stackList.size()>0){
						TreeJson treeJson1 = null;
						for(BusinessStack businessStack : stackList){
							treeJson1 = new TreeJson();
							treeJson1.setId(businessStack.getId());
							treeJson1.setText(businessStack.getName());
							
							List<TreeJson> cTreeJsonList2 = new ArrayList<TreeJson>();
							List<BusinessStack> stackList2= updateStackDao.getStackInfo(null, "2",deptId,userId,stackObject,remark,businessStack.getId());
							if(stackList2!=null && stackList2.size()>0){
								TreeJson treeJson2 = null;
								for (BusinessStack businessStack2 : stackList2) {
									treeJson2 = new TreeJson();
									treeJson2.setId(businessStack2.getId());
									treeJson2.setText(businessStack2.getName());
									
									List<TreeJson> cTreeJsonList3 = new ArrayList<TreeJson>();
									List<BusinessStack> stackList3= updateStackDao.getStackInfo(null, "2",deptId,userId,stackObject,remark,businessStack2.getId());
									if(stackList3!=null && stackList3.size()>0){
										TreeJson treeJson3 = null;
										for (BusinessStack businessStack3 : stackList3) {
											treeJson3 = new TreeJson();
											treeJson3.setId(businessStack3.getId());
											treeJson3.setText(businessStack3.getName());
											cTreeJsonList3.add(treeJson3);
										}
									}else{
										treeJson1.setState("closed");
									}
									treeJson2.setChildren(cTreeJsonList3);
									cTreeJsonList2.add(treeJson2);
								}
							}else{
								treeJson1.setState("closed");
							}
							treeJson1.setChildren(cTreeJsonList2);
							cTreeJsonList1.add(treeJson1);
						}
					}else{
						treeJson.setChildren(cTreeJsonList1);
						treeJsonList.add(treeJson);
					}
				}else if("3".equals(string)){
					treeJson = new TreeJson();
					map = new HashMap<String,String>();
					treeJson.setId("3");
					treeJson.setText("个人");
					treeJson.setIconCls("icon-03");
					treeJson.setState("open");
					treeJson.setAttributes(map);
					
					List<TreeJson> cTreeJsonList1 = new ArrayList<TreeJson>();
					List<BusinessStack> stackList= updateStackDao.getStackInfo(null, "3",deptId,userId,stackObject,remark,"ROOT");
					if(stackList!=null && stackList.size()>0){
						TreeJson treeJson1 = null;
						for(BusinessStack businessStack : stackList){
							treeJson1 = new TreeJson();
							treeJson1.setId(businessStack.getId());
							treeJson1.setText(businessStack.getName());
							
							List<TreeJson> cTreeJsonList2 = new ArrayList<TreeJson>();
							List<BusinessStack> stackList2= updateStackDao.getStackInfo(null, "3",deptId,userId,stackObject,remark,businessStack.getId());
							if(stackList2!=null && stackList2.size()>0){
								TreeJson treeJson2 = null;
								for (BusinessStack businessStack2 : stackList2) {
									treeJson2 = new TreeJson();
									treeJson2.setId(businessStack2.getId());
									treeJson2.setText(businessStack2.getName());
									
									List<TreeJson> cTreeJsonList3 = new ArrayList<TreeJson>();
									List<BusinessStack> stackList3= updateStackDao.getStackInfo(null, "3",deptId,userId,stackObject,remark,businessStack2.getId());
									if(stackList3!=null && stackList3.size()>0){
										TreeJson treeJson3 = null;
										for (BusinessStack businessStack3 : stackList3) {
											treeJson3 = new TreeJson();
											treeJson3.setId(businessStack3.getId());
											treeJson3.setText(businessStack3.getName());
											cTreeJsonList3.add(treeJson3);
										}
									}else{
										treeJson1.setState("closed");
									}
									treeJson2.setChildren(cTreeJsonList3);
									cTreeJsonList2.add(treeJson2);
								}
							}else{
								treeJson1.setState("closed");
							}
							treeJson1.setChildren(cTreeJsonList2);
							cTreeJsonList1.add(treeJson1);
						}
						treeJson.setChildren(cTreeJsonList1);
						treeJsonList.add(treeJson);
					}else{
						treeJson.setChildren(cTreeJsonList1);
						treeJsonList.add(treeJson);
					}
				}
			}
		}
		return treeJsonList;
	}
	/**
	 * 渲染频次
	 * @author  zhenglin
	 * @createDate： 
	 * @modifier lyy
	 * @modifyDate：2016年4月12日 上午9:09:35 
	 * @param：   
	 * @modifyRmk：  
	 * @version 1.0
	 */
	@Override
	public List<BusinessFrequency> getFreq() {
		return updateStackDao.getFreq();
	}
	/**
	 * 查看药品组套详情
	 * @author  zhenglin
	 * @createDate： 
	 * @modifier lyy
	 * @modifyDate：2016年4月11日 下午2:59:13
	 * @param： id 组套的编号   drugstoreId 诊断的选择的药房 feelType 是否是收费类型
	 * @modifyRmk：  
	 * @version 1.0
	 */
	@Override
	public List<StackAndStockInfoVo> queryStackInfoById(String id,String feelType) {
		return updateStackDao.queryStackInfoById(id,feelType);
	}

}
