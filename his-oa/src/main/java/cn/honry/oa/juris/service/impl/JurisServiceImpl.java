package cn.honry.oa.juris.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.OaActivitiDept;
import cn.honry.base.bean.model.OaBpmCategory;
import cn.honry.base.bean.model.OaBpmProcess;
import cn.honry.base.bean.model.OaJuris;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.bean.model.User;
import cn.honry.oa.activiti.bpm.process.vo.OaProcessVo;
import cn.honry.oa.juris.dao.JurisDAO;
import cn.honry.oa.juris.service.JurisService;
import cn.honry.oa.juris.vo.AreaVo;
import cn.honry.oa.juris.vo.CustVo;
import cn.honry.oa.juris.vo.DutiesVo;
import cn.honry.oa.juris.vo.EchoVo;
import cn.honry.oa.juris.vo.JurisVo;
import cn.honry.oa.juris.vo.LevelVo;
import cn.honry.oa.juris.vo.PersVo;
import cn.honry.oa.juris.vo.TypeVo;
import cn.honry.utils.HisParameters;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.ShiroSessionUtils;
import cn.honry.utils.TreeJson;

/** 
 * 
 * @Author：aizhonghua
 * @CreateDate：2018-2-1 下午20:32:31  
 * @Modifier：aizhonghua
 * @ModifyDate：2018-2-1 下午20:32:31  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
@Service("jurisService")
@Transactional
@SuppressWarnings({ "all" })
public class JurisServiceImpl implements JurisService{

	@Autowired
	@Qualifier(value = "jurisDAO")
	private JurisDAO jurisDAO;

	@Override
	public void removeUnused(String id) {
	}

	@Override
	public OaJuris get(String id) {
		return jurisDAO.get(id);
	}

	@Override
	public void saveOrUpdate(OaJuris info) {
		jurisDAO.save(info);
	}

	/**  
	 * 保存流程权限
	 * @Author：aizhonghua
	 * @CreateDate：2018-2-3 下午16:19:20  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2018-2-3 下午16:19:20  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public void saveJuris(JurisVo vo) {
		//1.删除原有权限信息
		jurisDAO.delJuris(vo.getId());
		//2.保存非自定义权限信息
		if(vo.isAll()){//当非自定义权限为true时
			//3.保存一条ALL的权限记录
			OaJuris juris = getJurisInfo(vo.getId(),vo.getName(),HisParameters.JURISALL,HisParameters.JURISALLCN,HisParameters.JURISALL,HisParameters.JURISALLCN);
			jurisDAO.save(juris);
		}else{//当非自定义权限为false时,逐条保存人员类别,职务,级别,院区,个人的权限记录
			//4.人员类别
			ArrayList<TypeVo> typeList = vo.getType();
			if(typeList!=null&&typeList.size()>0){
				List<OaJuris> list = new ArrayList<OaJuris>();
				OaJuris juris = null;
				for(TypeVo type : typeList){
					juris = getJurisInfo(vo.getId(), vo.getName(), HisParameters.JURISTYPE, HisParameters.JURISTYPECN, type.getCode(), type.getName());
					list.add(juris);
				}
				jurisDAO.saveOrUpdateList(list);
			}
			//5.职务
			ArrayList<DutiesVo> dutiesList = vo.getDuties();
			if(dutiesList!=null&&dutiesList.size()>0){
				List<OaJuris> list = new ArrayList<OaJuris>();
				OaJuris juris = null;
				for(DutiesVo duties : dutiesList){
					juris = getJurisInfo(vo.getId(), vo.getName(), HisParameters.JURISDUTIES, HisParameters.JURISDUTIESCN, duties.getCode(), duties.getName());
					list.add(juris);
				}
				jurisDAO.saveOrUpdateList(list);
			}
			//6.级别
			ArrayList<LevelVo> levelList = vo.getLevel();
			if(levelList!=null&&levelList.size()>0){
				List<OaJuris> list = new ArrayList<OaJuris>();
				OaJuris juris = null;
				for(LevelVo level : levelList){
					juris = getJurisInfo(vo.getId(), vo.getName(), HisParameters.JURISLEVEL, HisParameters.JURISLEVELCN, level.getCode(), level.getName());
					list.add(juris);
				}
				jurisDAO.saveOrUpdateList(list);
			}
			//7.院区
			ArrayList<AreaVo> areaList = vo.getArea();
			if(areaList!=null&&areaList.size()>0){
				List<OaJuris> list = new ArrayList<OaJuris>();
				OaJuris juris = null;
				for(AreaVo area : areaList){
					juris = getJurisInfo(vo.getId(), vo.getName(), HisParameters.JURISAREA, HisParameters.JURISAREACN, area.getCode(), area.getName());
					list.add(juris);
				}
				jurisDAO.saveOrUpdateList(list);
			}
			//8.个人
			ArrayList<PersVo> persList = vo.getPers();
			if(persList!=null&&persList.size()>0){
				List<OaJuris> list = new ArrayList<OaJuris>();
				OaJuris juris = null;
				for(PersVo pers : persList){
					juris = getJurisInfo(vo.getId(), vo.getName(), HisParameters.JURISPERS, HisParameters.JURISPERSCN, pers.getCode(), pers.getName());
					list.add(juris);
				}
				jurisDAO.saveOrUpdateList(list);
			}
		}
		//9.保存自定义权限信息
		CustVo cust = vo.getCust();
		if(cust!=null&&StringUtils.isNotBlank(cust.getCode())&&StringUtils.isNotBlank(cust.getName())){
			OaJuris juris = getJurisInfo(vo.getId(),vo.getName(),HisParameters.JURISCUST,HisParameters.JURISCUSTCN,cust.getCode(),cust.getName());
			jurisDAO.save(juris);
		}
	}

	/**  
	 * 获取流程权限
	 * @Author：aizhonghua
	 * @CreateDate：2018-2-3 下午16:19:20  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2018-2-3 下午16:19:20  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	private OaJuris getJurisInfo(String flowCode, String flowName, String jurisCode, String jurisName,String rangeCode,String rangeName) {
		OaJuris juris = new OaJuris();
		juris.setFlowCode(flowCode);
		juris.setFlowName(flowName);
		juris.setJurisCode(jurisCode);
		juris.setJurisName(jurisName);
		juris.setRangeCode(rangeCode);
		juris.setRangeName(rangeName);
		return juris;
	}

	/**  
	 * 根据流程id获取流程权限对照key:权限分类编码(编码)value:{key:范围编码value:权限分类编码(编码)}
	 * @Author：aizhonghua
	 * @CreateDate：2018-2-3 下午16:19:20  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2018-2-3 下午16:19:20  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public EchoVo getJurisMap(String id) {
		List<OaJuris> list = jurisDAO.getJurisByFlowCode(id);
		if(list!=null){
			EchoVo echoVo = new EchoVo();
			boolean isAll = false;//全部
			HashMap<String,String> typeMap = new HashMap<String,String>();//人员类别
			HashMap<String,String> dutiesMap = new HashMap<String,String>();//职务
			HashMap<String,String> levelMap = new HashMap<String,String>();//级别
			HashMap<String,String> areaMap = new HashMap<String,String>();//院区
			HashMap<String,String> persMap = new HashMap<String,String>();//个人
			CustVo cust = new CustVo();
			for(OaJuris juris : list){
				switch(juris.getJurisCode()){
					case HisParameters.JURISALL:isAll=true;break;//全部
					case HisParameters.JURISTYPE:typeMap.put(juris.getRangeCode(), juris.getRangeName());break;//人员类别
					case HisParameters.JURISDUTIES:dutiesMap.put(juris.getRangeCode(), juris.getRangeName());break;//职务
					case HisParameters.JURISLEVEL:levelMap.put(StringUtils.isNotBlank(juris.getRangeCode())?juris.getRangeCode():"", juris.getRangeName());break;//级别
					case HisParameters.JURISAREA:areaMap.put(juris.getRangeCode(), juris.getRangeName());break;//院区
					case HisParameters.JURISPERS:persMap.put(juris.getRangeCode(), juris.getRangeName());break;//个人
					case HisParameters.JURISCUST:cust.setCode(juris.getRangeCode());cust.setName(juris.getRangeName());break;//自定义
					default:break;
				}
			}
			echoVo.setAll(isAll);
			echoVo.setType(typeMap);
			echoVo.setDuties(dutiesMap);
			echoVo.setLevel(levelMap);
			echoVo.setArea(areaMap);
			echoVo.setPers(persMap);
			echoVo.setCust(cust);
			return echoVo;
		}
		return null;
	}
	
	/**  
	 * 获取有权限的流程id,返回id集合或null
	 * @Author：aizhonghua
	 * @CreateDate：2018-2-3 下午16:19:20  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2018-2-3 下午16:19:20  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public List<String> getJurisList() {
		SysEmployee emp = ShiroSessionUtils.getCurrentEmployeeFromShiroSession();
		if(emp!=null){
			return jurisDAO.getJurisListByJobNo(emp.getJobNo());
		}
		return null;
	}
	
	/**  
	 * 获取有权限的流程分类树
	 * @Author：aizhonghua
	 * @CreateDate：2018-2-3 下午16:19:20  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2018-2-3 下午16:19:20  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public List<TreeJson> getProcessTree() {
		List<TreeJson> parTree = new ArrayList<TreeJson>();
		TreeJson parTreeJson = new TreeJson();
		parTreeJson.setId("root");
		parTreeJson.setText("流程导航");
		parTreeJson.setState(TreeJson.STATEOPEN);
		parTree.add(parTreeJson);
		SysEmployee emp = ShiroSessionUtils.getCurrentEmployeeFromShiroSession();
		if(emp!=null){
			List<String> idList = jurisDAO.getJurisListByJobNo(emp.getJobNo());
			//获取分类
			List<OaBpmCategory> cList = jurisDAO.getCategoryById(idList);
			if(cList!=null){
				TreeJson cTree = new TreeJson();
				cTree.setId("category");
				cTree.setText("分类");
				Map<String, String> cMap = new HashMap<String, String>();
				cMap.put("pid", "root");
				cTree.setAttributes(cMap);
				parTree.add(cTree);
				TreeJson ccTree = null;
				Map<String, String> ccMap = null;
				for(OaBpmCategory c : cList){
					ccTree = new TreeJson();
					ccTree.setId(c.getId());
					ccTree.setText(c.getName());
					ccMap = new HashMap<String, String>();
					ccMap.put("pid", "category");
					ccTree.setAttributes(ccMap);
					parTree.add(ccTree);
				}
			}
			//获取科室
			List<OaActivitiDept> dList = jurisDAO.getActivitiDeptById(idList);
			if(dList!=null){
				TreeJson dTree = new TreeJson();
				dTree.setId("deptcode");
				dTree.setText("科室");
				Map<String, String> dMap = new HashMap<String, String>();
				dMap.put("pid", "root");
				dTree.setAttributes(dMap);
				parTree.add(dTree);
				TreeJson ddTree = null;
				Map<String, String> ddMap = null;
				for(OaActivitiDept d : dList){
					ddTree = new TreeJson();
					ddTree.setId(d.getDeptCode());
					ddTree.setText(d.getDeptName());
					ddMap = new HashMap<String, String>();
					ddMap.put("pid", "deptcode");
					ddTree.setAttributes(ddMap);
					parTree.add(ddTree);
				}
			}
		}
		return TreeJson.formatTree(parTree);
	}

	/**  
	 * 获取有权限的流程列表-分页数据
	 * @Author：aizhonghua
	 * @CreateDate：2018-2-3 下午16:19:20  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2018-2-3 下午16:19:20  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public List<OaProcessVo> getProcessListByJuris(int page, int rows, String name, String category, String deptcode,List<String> idList) {
		return jurisDAO.getProcessListByJuris(page, rows, name, category, deptcode, idList);
	}

	/**  
	 * 获取有权限的流程列表-总条数
	 * @Author：aizhonghua
	 * @CreateDate：2018-2-3 下午16:19:20  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2018-2-3 下午16:19:20  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public int getProcessTotalByJuris(String name, String category, String deptcode,List<String> idList) {
		return jurisDAO.getProcessTotalByJuris(name, category, deptcode, idList);
	}

}
