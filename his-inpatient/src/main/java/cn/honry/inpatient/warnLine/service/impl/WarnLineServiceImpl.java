package cn.honry.inpatient.warnLine.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.InpatientInfo;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.inpatient.warnLine.dao.WarnLineDAO;
import cn.honry.inpatient.warnLine.service.WarnLineService;
import cn.honry.inpatient.warnLine.vo.WarnLineVo;
import cn.honry.utils.RedisUtil;
import cn.honry.utils.TreeJson;
/**
 * 全院警戒线设置Service实现类
 * @author  lyy
 * @createDate： 2016年4月1日 下午2:48:48 
 * @modifier lyy
 * @modifyDate：2016年4月1日 下午2:48:48  
 * @modifyRmk：  
 * @version 1.0
 */
@Service("warnLineService")
@Transactional
@SuppressWarnings({"all"})
public class WarnLineServiceImpl implements WarnLineService{
	private Logger logger=Logger.getLogger(WarnLineServiceImpl.class);
	@Resource
	private RedisUtil redisUtil;
	@Autowired
	private WarnLineDAO warnLineDAO;
	
	/**
	 * @Description: 获取分页列表
	 * @Author：  kjh
	 * @CreateDate： 2015-6-29
	 * @Modifier：lyy
	 * @ModifyDate：2016-3-30下午08:32:12
	 * @param  page 当前页   rows 当前页条数    inpatientInfoSerch 住院主表实体
	 * @return List<InpatientInfo> 返回一个集合
	 * @version 1.0
	**/
	@Override
	public List<InpatientInfo> getPage(String page, String rows, InpatientInfo entity) throws Exception {
		return warnLineDAO.getPage(page, rows, entity);
	}
	
	/**
	 * @Description: 获取总条数
	 * @Author：kjh
	 * @CreateDate： 2015-6-29
	 * @Modifier：lyy
	 * @ModifyDate：2016-3-30下午08:32:12
	 * @param inpatientInfoSerch  住院主表实体
	 * @return int   返回一个int
	 * @version 1.0
	**/
	@Override
	public int getTotal(InpatientInfo entity) throws Exception  {
		return warnLineDAO.getTotal(entity);
	}

	@Override
	public void removeUnused(String id) {
	
	}

	@Override
	public InpatientInfo get(String id) {
		return null;
	}

	@Override
	public void saveOrUpdate(InpatientInfo entity) {
		
	}
	
	/**
	 * @Description： 连表查询数据
	 * @Author：lyy
	 * @CreateDate：2015-12-3 下午02:36:53  
	 * @Modifier：lyy
	 * @ModifyDate：2015-12-3 下午02:36:53  
	 * @param vo 患者警戒线虚拟实体  page 当前页   rows 当前页条数 
	 * @return List<InpatientInfo> 返回一个集合
	 * @ModifyRmk：  代码规范
	 * @version 1.0
	 */
	@Override
	public List<WarnLineVo> getWarnLine(WarnLineVo vo, String page, String rows) throws Exception  {
		return warnLineDAO.listWarnLine(vo, page, rows);
	}
	
	/**
	 * @Description：患者警戒线设置总条数
	 * @Author：lyy
	 * @CreateDate：2015-12-3 下午03:20:00  
	 * @Modifier：lyy
	 * @ModifyDate：2015-12-3 下午03:20:00
	 * @param   vo 患者警戒线虚拟实体  
	 * @return int 返回一个int值
	 * @ModifyRmk：  代码规范
	 * @version 1.0
	 */
	@Override
	public int getTotalCount(WarnLineVo vo)  throws Exception {
		return  warnLineDAO.getTotalCountBySql(vo);
	}
	 /**
	  * 住院部全院患者警戒线病区的树
	  * @author  lyy
	  * @createDate： 2015年12月19日 下午2:23:01 
	  * @modifier lyy
	  * @modifyDate：2015年12月19日 下午2:23:01
	  * @param    id   病区主键id 
	  * @return  返回一个List<TreeJson> list集合json
	  * @modifyRmk：  代码规范
	  * @version 1.0
	  */
	@Override
	public List<TreeJson> queryTreeNurse(String id) throws Exception  {
		List<SysDepartment> sysDepartmentList = new ArrayList<SysDepartment>();
		List<TreeJson> treeJsonList = new ArrayList<TreeJson>();
		
		//根节点
		if(StringUtils.isBlank(id)){
			
			//加入转入科室的根节点
			TreeJson pTreeJson = new TreeJson();
			pTreeJson.setId("1");
			pTreeJson.setText("护士站");
			treeJsonList.add(pTreeJson);
		}
		String key=SysDepartment.class.getName()+"-queryAllLesion";//缓存所有病区
		if(redisUtil.exists(key)){
			sysDepartmentList= (List<SysDepartment>) redisUtil.get(key);
			logger.info("读取科室病区缓存：key="+key);
		}else{
			sysDepartmentList = warnLineDAO.findTreeType();
			if(sysDepartmentList!=null&&sysDepartmentList.size()>0){
				redisUtil.set(key, sysDepartmentList);
				logger.info("添加科室病区缓存：key="+key);
			}
		}
		if(sysDepartmentList!=null&&sysDepartmentList.size()>0){
			for(SysDepartment sysDepartment : sysDepartmentList){
				int count  = 0;
				String cou=String.valueOf(count);
				TreeJson treeJson = new TreeJson();
				treeJson.setId(sysDepartment.getId());
				treeJson.setText(sysDepartment.getDeptName());
				Map<String,String> attributes = new HashMap<String, String>();
				attributes.put("pid",StringUtils.isBlank(sysDepartment.getDeptParent())?"1":sysDepartment.getDeptParent());
				attributes.put("hasson",sysDepartment.getDeptHasson().toString());
				attributes.put("deptCode",sysDepartment.getDeptCode().toString());
				attributes.put("count1",cou);
				treeJson.setAttributes(attributes);
				treeJsonList.add(treeJson);
			}
		}
		return TreeJson.formatTree(treeJsonList);
	}
	@Override
	public List<SysDepartment> getDeptName(String queryName) throws Exception  {
		return  warnLineDAO.getDeptName(queryName);
	}

}
