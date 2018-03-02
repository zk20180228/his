package cn.honry.statistics.operation.operationArrange.service.impl;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.BusinessDictionary;
import cn.honry.base.bean.model.BusinessHospitalbed;
import cn.honry.base.bean.model.DrugUndrug;
import cn.honry.base.bean.model.OperationArrange;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.inner.baseinfo.code.service.CodeInInterService;
import cn.honry.inner.baseinfo.department.service.DeptInInterService;
import cn.honry.inner.baseinfo.employee.service.EmployeeInInterService;
import cn.honry.inner.operation.arrange.dao.ArrangementInnerDAO;
import cn.honry.inner.operation.record.vo.OpNameVo;
import cn.honry.inner.operation.record.vo.OperationUserVo;
import cn.honry.inner.vo.MenuListVO;
import cn.honry.statistics.operation.operationArrange.dao.OperationArrangeDao;
import cn.honry.statistics.operation.operationArrange.service.OperationArrangeService;
import cn.honry.statistics.operation.operationArrange.vo.OperationArrangeToReportSlave;
import cn.honry.statistics.operation.operationArrange.vo.OperationArrangeVo;
import cn.honry.utils.CommonStringUtils;
import cn.honry.utils.DateUtils;
import cn.honry.utils.FileUtil;
/***
 * 手术安排统计service实现层
 * @Description:
 * @author: tangfeishuai
 * @CreateDate: 2016年5月30日 
 * @version 1.0
 */
@Service("operationArrangeService")
@Transactional
@SuppressWarnings({ "all" })
public class OperationArrangeServiceImpl implements OperationArrangeService{
	
	/** 手术安排汇总 **/
	@Autowired
	@Qualifier(value = "operationArrangeDao")
	private OperationArrangeDao operationArrangeDao;
	
	@Autowired
	@Qualifier(value="arrangementInnerDAO")
	private ArrangementInnerDAO arrangementInnerDAO;
	@Autowired
	@Qualifier(value="employeeInInterService")
	private EmployeeInInterService employeeInInterService;
	@Autowired
	@Qualifier(value="innerCodeService")
	private CodeInInterService innerCodeService;
	@Autowired
	@Qualifier(value = "deptInInterService")
	private DeptInInterService deptInInterService;

	/**  
	 * 
	 * 手术安排统计列表查询
	 * @Author: zxl
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @Modifier: zxl
	 * @ModifyDate: 2017-7-3 下午15:30:31
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:startTime 开始时间
	 * @param:endTime结束时间
	 * @param:status手术状态
	 * @param:execDept执行科室
	 * @param:identityCard身份证号
	 * @param:page当前页数
	 * @param:rows行数
	 * @throws:
	 * @return: void
	 *
	 */
	@Override
	public List<OperationArrangeVo> getOperationArrangeVo(String beganTime, String endTime, String status,
			String execDept, String page, String rows,String identityCard) {
		List<OperationArrangeVo> list=operationArrangeDao.getOperationArrangeVo(beganTime,endTime,status,execDept,page,rows,identityCard);
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				//获取手术名称
				List<OpNameVo> mclist=operationArrangeDao.getOperationItem(list.get(i).getOpId());
				//获取巡回护士
				List<OperationUserVo> xunHuiList = arrangementInnerDAO.getOperationUserList(list.get(i).getOpId(), "tour",list.get(i).getStatus());//巡回
				//获取洗手护士
				List<OperationUserVo> xiShouList = arrangementInnerDAO.getOperationUserList(list.get(i).getOpId(), "wash",list.get(i).getStatus());//洗手
				//获取助手
				List<OperationUserVo> zsDocList = arrangementInnerDAO.getOperationUserList(list.get(i).getOpId(), "thelper",list.get(i).getStatus());//临时助手
				list.get(i).setZsDocList(zsDocList);
				list.get(i).setXhlist(xunHuiList);
				list.get(i).setXslist(xiShouList);
				if(mclist!=null && mclist.size()>0){
					list.get(i).setMclist(mclist);
				}
			}
			return list;
		}
		
		return new ArrayList<OperationArrangeVo>();
	}

	@Override
	public OperationArrange get(String id) {
		return null;
	}

	@Override
	public void removeUnused(String arg0) {
		
	}

	@Override
	public void saveOrUpdate(OperationArrange arg0) {
		
	}
	/**
	 * @Description:手术名称map
	 * @Author: tangfeishuai
	 * @CreateDate: 2016年5月27日
	 * @param:
	 * @return Map<String, String>
	 * @Modifier:
	 * @ModifyDate:
	 * @ModifyRmk:
	 * @version: 1.0
	 */
	@Override
	public Map<String, String> opNameMap() {
		HashMap<String, String> map=new HashMap<String,String>();
		List<DrugUndrug> undrugList=operationArrangeDao.getCombobox();
		if(undrugList.size()>0&&undrugList!=null){
			for (DrugUndrug d : undrugList) {
				map.put(d.getId(), d.getName());
			}
		}
		return map;
	}
	/**  
	 * 
	 * 手术安排统计列表查询总记录数
	 * @Author: zxl
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @Modifier: zxl
	 * @ModifyDate: 2017-7-3 下午15:30:31
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:startTime 开始时间
	 * @param:endTime结束时间
	 * @param:status手术状态
	 * @param:execDept执行科室
	 * @param:identityCard身份证号
	 * @throws:
	 * @return: void
	 *
	 */
	@Override
	public int getTotal(String beganTime, String endTime, String status, String execDept,String identityCard) {
		return operationArrangeDao.getTotal(beganTime, endTime, status, execDept,identityCard);
	}
	
	/**
	 * @Description:导出列表
	 * @Description:
	 * @author: tangfeishuai
	 * @CreateDate: 2016年6月24日 
	 * @version 1.0
	**/
	public FileUtil export(List<OperationArrangeVo> list, FileUtil fUtil) {
		 Map<String, String> map=employeeInInterService.queryEmpCodeAndNameMap();
		 List<BusinessDictionary> bus=innerCodeService.getDictionary("aneWay");
		 Map<String, String> inDeptMap=deptInInterService.querydeptCodeAndNameMap();
		 Map<String, String> busMap=new HashMap<String, String>();
		 if(bus!=null){
			 for(int i=0;i<bus.size();i++){
				 busMap.put(bus.get(i).getEncode(), bus.get(i).getName());
			 } 
		 }
		for (OperationArrangeVo model : list) {
			String record="";
			String mc="";
			String zs="";
			String xs="";
			String xh="";
			record = CommonStringUtils.trimToEmpty(inDeptMap.get(model.getInDept())) + ",";
			record += DateUtils.formatDateY_M_D_H_M(model.getPreDate()) + ",";
			record += CommonStringUtils.trimToEmpty(model.getPatientNo()) + ",";
			record += CommonStringUtils.trimToEmpty(model.getBedNo()) + ",";
			record += CommonStringUtils.trimToEmpty(model.getName()) + ",";
			record += CommonStringUtils.trimToEmpty("1".equals(model.getSex())?"男":("2".equals(model.getSex())?"女":"未知")) + ",";
			record += CommonStringUtils.trimToEmpty(model.getAge()) + ",";
			//多个手术名称用逗号拼接
			if(model.getMclist()!=null && model.getMclist().size()>0){
				for(int i=0;i<model.getMclist().size();i++){
					if(i==(model.getMclist().size()-1)){
						mc+=model.getMclist().get(i).getItemName();
					}
					mc+=model.getMclist().get(i).getItemName()+"，";
				}
			}
			
			record += CommonStringUtils.trimToEmpty(mc) + ",";
			record += CommonStringUtils.trimToEmpty(inDeptMap.get(model.getExecDept())) + ",";
			record += CommonStringUtils.trimToEmpty(map.get(model.getOpDoctor())) + ",";
			//多个助手用逗号拼接
			if(model.getZsDocList()!=null){
				for(int i=0;i<model.getZsDocList().size();i++){
					if(i==(model.getZsDocList().size()-1)){
						zs+=model.getZsDocList().get(i).getEmplName();
					}
					zs+=model.getZsDocList().get(i).getEmplName()+"，";
				}
			}
			//多个洗手护士用逗号拼接
			if(model.getXslist()!=null && model.getXslist().size()>0){
				for(int i=0;i<model.getXslist().size();i++){
					if(i==(model.getXslist().size()-1)){
						xs+=model.getXslist().get(i).getEmplName();
					}
					xs+=model.getXslist().get(i).getEmplName()+"，";
				}
			}
			//多个巡回护士用逗号拼接
			if(model.getXhlist()!=null && model.getXhlist().size()>0){
				for(int i=0;i<model.getXhlist().size();i++){
					if(i==(model.getXhlist().size()-1)){
						xh+=model.getXhlist().get(i).getEmplName();
					}
					xh+=model.getXhlist().get(i).getEmplName()+"，";
				}
			}
			record += CommonStringUtils.trimToEmpty(zs) + ",";
			record += CommonStringUtils.trimToEmpty(xh) + ",";
			record += CommonStringUtils.trimToEmpty(xs) + ",";
			record += CommonStringUtils.trimToEmpty(busMap.get(model.getAneType())) + ",";
			record += CommonStringUtils.trimToEmpty(map.get(model.getAnaeDocd())) + ",";
			record += CommonStringUtils.trimToEmpty(map.get(model.getAnaeHelper())) + ","; 
			
			
			try {
				fUtil.write(record);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return fUtil;
	}
	
	/**  
	 * 
	 * 导出手术计费信息汇总
	 * @Author: zxl
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @Modifier: zxl
	 * @ModifyDate: 2017-7-3 下午15:30:31
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:status手术状态
	 * @param:execDept执行科室
	 * @param:identityCard身份证号
	 * @throws:
	 * @return:  List<OperationArrangeVo> 
	 *
	 */
	@Override
	public List<OperationArrangeVo> getAllOperationArrangeVo(String beganTime, String endTime, String status,
			String execDept, String identityCard) {
		List<OperationArrangeVo> list=operationArrangeDao.getOperationArrangeVo2(beganTime,endTime,status,execDept,identityCard);
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				//获取手术名称
				List<OpNameVo> mclist=operationArrangeDao.getOperationItem(list.get(i).getOpId());
				//获取巡回护士
				List<OperationUserVo> xunHuiList = arrangementInnerDAO.getOperationUserList(list.get(i).getOpId(), "tour",list.get(i).getStatus());//巡回
				//获取洗手护士
				List<OperationUserVo> xiShouList = arrangementInnerDAO.getOperationUserList(list.get(i).getOpId(), "wash",list.get(i).getStatus());//洗手
				//获取助手
				List<OperationUserVo> zsDocList = arrangementInnerDAO.getOperationUserList(list.get(i).getOpId(), "thelper",list.get(i).getStatus());//临时助手
				list.get(i).setZsDocList(zsDocList);
				list.get(i).setXhlist(xunHuiList);
				list.get(i).setXslist(xiShouList);
				if(mclist!=null && mclist.size()>0){
					list.get(i).setMclist(mclist);
				}
			}
			return list;
		}
		
		return new ArrayList<OperationArrangeVo>();
	}

	/**
	 *
	 * @Description：获取病床
	 * @Author：zhangjin
	 * @CreateDate：2016年10月21日  
	 * @Modifier：
	 * @ModifyDate：
	 * @ModifyRmk：  
	 * @version： 1.0：
	 *
	 */
	@Override
	public List<BusinessHospitalbed> getBedno() {
		return operationArrangeDao.getBedno();
	}

	/**  
	 * 
	 * 手术安排统计报表打印
	 * @Author: zxl
	 * @CreateDate: 2017-7-3 下午15:30:31
	 * @Modifier: zxl
	 * @ModifyDate: 2017-7-3 下午15:30:31
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:status手术状态
	 * @param:execDept执行科室
	 * @param:identityCard身份证号
	 * @param:page当前页数
	 * @param:rows行数
	 * @throws:
	 * @return: void
	 *
	 */
	@Override
	public List<OperationArrangeToReportSlave> getOperationArrangeToReport(String beganTime, String endTime, String status,
			String execDept,String identityCard) {
		List<OperationArrangeToReportSlave> slaves = new ArrayList<OperationArrangeToReportSlave>();
		List<OperationArrangeVo> list=operationArrangeDao.getOperationArrangeToReport(beganTime,endTime,status,execDept,identityCard);
		if(list!=null&&list.size()>0){
			Map<String, String> map=employeeInInterService.queryEmpCodeAndNameMap();
			 List<BusinessDictionary> bus=innerCodeService.getDictionary("aneWay");
			 Map<String, String> deptMap=operationArrangeDao.getroomdept();
			 Map<String, String> busMap=new HashMap<String, String>();
			 Map<String, String> info=deptInInterService.querydeptCodeAndNameMap();
			 if(bus!=null){
				 for(int i=0;i<bus.size();i++){
					 busMap.put(bus.get(i).getEncode(), bus.get(i).getName());
				 } 
			 }
			 List<BusinessDictionary>  sexList = this.innerCodeService.getDictionary("sex");
			  Map<String, String> sexMap=new HashMap<String, String>();
			  if(sexList!=null && sexList.size()>0){
				  for(int i=0;i<sexList.size();i++){
					  sexMap.put(sexList.get(i).getEncode(), sexList.get(i).getName());
					 } 
			  }
			 
			  List<SysDepartment> opRoomList = likeSearchOpRoom();
			  Map<String, String> opRoomMap=new HashMap<String, String>();
			  if(opRoomList!=null && opRoomList.size()>0){
				  for(int i=0;i<opRoomList.size();i++){
					  opRoomMap.put(opRoomList.get(i).getDeptCode(), opRoomList.get(i).getDeptName());
					 } 
			  }

			for(int i=0;i<list.size();i++){
				OperationArrangeToReportSlave slave = new OperationArrangeToReportSlave();
				OperationArrangeVo vo = list.get(i);
				
				slave.setPreDate(vo.getPreDate());
				slave.setInDept(info.get(vo.getInDept()));
				slave.setPatientNo(vo.getPatientNo());
				slave.setBedNo(vo.getBedNo());
				slave.setName(vo.getName());
				slave.setSex(sexMap.get(vo.getSex()));
				slave.setAge(vo.getAge());
				slave.setRoomId(deptMap.get(vo.getRoomId()));
				slave.setOpId(vo.getOpId());
				
				slave.setItemName(vo.getItemName());
				slave.setAneType(busMap.get(vo.getAneType()));
				slave.setOpDoctor(map.get(vo.getOpDoctor()));
				slave.setThelper(vo.getThelper());
				slave.setWash(vo.getWash());
				slave.setTour(vo.getTour());
				slave.setAnaeDocd(map.get(vo.getAnaeDocd()));
				slave.setAnaeHelper(map.get(vo.getAnaeHelper()));
				slave.setOpStates(vo.getOpStates());
				slave.setExecDept(opRoomMap.get(vo.getExecDept()));
				
				
				
				List<OpNameVo> mclist=operationArrangeDao.getOperationItem(list.get(i).getOpId());
				//手术名称
				String mcs = "";
				if(mclist!=null&&mclist.size()>0){
					for(int a=0;a<mclist.size();a++){
						if(a!=mclist.size()-1){
							mcs+=mclist.get(a).getItemName()+",";
						}else{
							mcs+=mclist.get(a).getItemName();
						}
					}
				}
				slave.setMcs(mcs);
				//巡回护士
				List<OperationUserVo> xunHuiList = arrangementInnerDAO.getOperationUserList(list.get(i).getOpId(), "tour",list.get(i).getStatus());//巡回
				String xhs = "";
				if(xunHuiList!=null&&xunHuiList.size()>0){
					for(int b=0;b<xunHuiList.size();b++){
						if(b!=xunHuiList.size()-1){
							xhs+=xunHuiList.get(b).getEmplName()+",";
						}else{
							xhs+=xunHuiList.get(b).getEmplName();
						}
					}
				}
				slave.setXhs(xhs);
				//洗手护士
				List<OperationUserVo> xiShouList = arrangementInnerDAO.getOperationUserList(list.get(i).getOpId(), "wash",list.get(i).getStatus());//洗手
				String xss = "";
				if(xiShouList!=null&&xiShouList.size()>0){
					for(int c=0;c<xiShouList.size();c++){
						if(c!=xiShouList.size()-1){
							xss+=xiShouList.get(c).getEmplName()+",";
						}else{
							xss+=xiShouList.get(c).getEmplName();
						}
					}
				}
				slave.setXss(xss);
				
				List<OperationUserVo> zsDocList = arrangementInnerDAO.getOperationUserList(list.get(i).getOpId(), "thelper",list.get(i).getStatus());//临时助手
				//助手医生
				String zsDocs = "";
				if(zsDocList!=null&&zsDocList.size()>0){
					for(int d=0;d<zsDocList.size();d++){
						if(d!=zsDocList.size()-1){
							zsDocs+=zsDocList.get(d).getEmplName()+",";
						}else{
							zsDocs+=zsDocList.get(d).getEmplName();
						}
					}
				}
				slave.setZsDocs(zsDocs);
				slaves.add(slave);
			}
			return slaves;
		}
		
		return new ArrayList<OperationArrangeToReportSlave>();
	}
    /**
     * 手术室查询-用于报表打印
     * @Author：hedong
	 * @CreateDate：2017年3月1日  
	 * @Modifier：
	 * @ModifyDate：
	 * @ModifyRmk：  
	 * @version： 1.0：
     */
	public List<SysDepartment> likeSearchOpRoom() {
		return operationArrangeDao.likeSearchOpRoom();
	}

	/**
	 * @Description 查询科室信息
	 * @author  zxl
	 * @createDate： 2017-4-14 下午7:15:24 
	 * @modifier 
	 * @modifyDate：
	 * @param 
	 * @modifyRmk：  
	 * @version 1.0
	 */
	@Override
	public List<MenuListVO> querysysDeptment() {
		return operationArrangeDao.querysysDeptment();
	}
	
	
	
}
