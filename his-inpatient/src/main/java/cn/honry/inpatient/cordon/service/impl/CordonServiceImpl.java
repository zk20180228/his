package cn.honry.inpatient.cordon.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.InpatientInfo;
import cn.honry.base.bean.model.InpatientInfoNow;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.inpatient.cordon.dao.CordonDAO;
import cn.honry.inpatient.cordon.service.CordonService;
import cn.honry.inpatient.cordon.vo.CordonVo;
import cn.honry.utils.ShiroSessionUtils;
import cn.honry.utils.TreeJson;
/**
 * 护士站患者警戒线service实现类
 * @author  lyy
 * @createDate： 2016年4月1日 下午5:48:52 
 * @modifier lyy
 * @modifyDate：2016年4月1日 下午5:48:52
 * @param：    
 * @modifyRmk：  
 * @version 1.0
 */
@Service("cordonService")
@Transactional
@SuppressWarnings({"all"})
public class CordonServiceImpl implements CordonService {
	@Autowired
	@Qualifier("cordonDAO")
	private CordonDAO cordonDAO;
	@Override
	public void removeUnused(String id) {
		
	}

	@Override
	public InpatientInfoNow get(String id) {
		return cordonDAO.getInpatientInfo(id);
	}

	@Override
	public void saveOrUpdate(InpatientInfoNow entity) {
	}
	/**
	 * @Description：  获取护士站 (封装成josn)
	 * @Author：lyy
	 * @CreateDate：2015-12-9 下午06:36:07  
	 * @Modifier：lyy
	 * @ModifyDate：2015-12-9 下午06:36:07 
	 * @param： id  病区护士站Id 
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Override
	public List<TreeJson> TreeDeptCordon(String id) throws Exception  {
		String deptId = ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getId();
		List<SysDepartment> listDeptContact=new ArrayList<SysDepartment>();
		List<TreeJson> treeJsonList=new ArrayList<TreeJson>();
		if(StringUtils.isBlank(id)){//根节点
			//加入科室诊室间关系树的根节点
			TreeJson pTreeJson = new TreeJson();
			pTreeJson.setId("1");
			pTreeJson.setText("护士站信息");
			treeJsonList.add(pTreeJson);
		}
		listDeptContact=cordonDAO.findTree(deptId);
		if(listDeptContact != null && listDeptContact.size() > 0){
			for (SysDepartment deptContact : listDeptContact) {
				int count  =cordonDAO.countDept(deptContact.getDeptCode());
				String cou=String.valueOf(count);
				TreeJson treeJson = new TreeJson();
				treeJson.setId(deptContact.getId());
				treeJson.setText(deptContact.getDeptName());
				Map<String,String> attributes=new HashMap<String, String>();
				attributes.put("pid",StringUtils.isBlank(deptContact.getDeptParent())?"1":deptContact.getDeptParent());
				attributes.put("deptCode",deptContact.getDeptCode());
				attributes.put("count1", cou);
				treeJson.setAttributes(attributes);
				treeJsonList.add(treeJson);
			}
		}		
		return TreeJson.formatTree(treeJsonList);
		
	}
	/**
	 * 连表查询数据(总条数)
	 * @author  lyy
	 * @createDate： 2015年12月15日 下午5:28:01 
	 * @modifier lyy
	 * @modifyDate：2015年12月15日 下午5:28:01  
	 * @param entity 患者警戒线虚拟实体 deptId 登录科室
	 * @modifyRmk：  
	 * @version 1.0
	 */
	@Override
	public int getTotal(CordonVo entity,String deptId)throws Exception  {
		return cordonDAO.getTotal(entity,deptId);
	}
	/**
	 * 连表查询数据
	 * @author  lyy
	 * @createDate： 2015年12月15日 下午5:28:24 
	 * @modifier lyy
	 * @modifyDate：2015年12月15日 下午5:28:24  
	 * @param entity 患者警戒线虚拟实体  page 当前页   rows 当前页条数  deptId 登录科室
	 * @modifyRmk：  
	 * @version 1.0
	 */
	@Override
	public List<CordonVo> getPage(CordonVo entity, String page, String rows,String deptId)throws Exception  {
		return cordonDAO.getPage(entity, page, rows,deptId);
	}
	/**
	 * 设置警戒线（保存）
	 * @author  lyy
	 * @createDate： 2015年12月21日 上午11:15:15 
	 * @modifier lyy
	 * @modifyDate：2015年12月21日 上午11:15:15  
	 * @param cordonVo  患者警戒线虚拟实体
	 * @modifyRmk：  
	 * @version 1.0
	 */
	@Override
	public void saveCordonVo(CordonVo cordonVo) throws Exception {
		InpatientInfoNow inpatientInfo = cordonDAO.getInpatientInfo(cordonVo.getId());
		inpatientInfo.setAlterType(cordonVo.getAlterType());
		inpatientInfo.setMoneyAlert(cordonVo.getMoneyAlert());
		inpatientInfo.setAlterBegin(cordonVo.getAlterBegin());
		inpatientInfo.setAlterEnd(cordonVo.getAlterEnd());
		cordonDAO.save(inpatientInfo);
	}

}
