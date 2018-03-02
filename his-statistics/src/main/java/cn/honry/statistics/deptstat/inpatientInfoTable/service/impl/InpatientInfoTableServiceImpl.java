package cn.honry.statistics.deptstat.inpatientInfoTable.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.hiasMongo.basic.MongoBasicDao;
import cn.honry.inner.system.parameter.dao.ParameterInnerDAO;
import cn.honry.statistics.deptstat.inpatientInfoTable.dao.InpatientInfoTableDao;
import cn.honry.statistics.deptstat.inpatientInfoTable.service.InpatientInfoTableService;
import cn.honry.statistics.deptstat.inpatientInfoTable.vo.InpatientInfoTableVo;
import cn.honry.statistics.deptstat.outPatientMessage.vo.OutPatientMessageVo;

@Service("inpatientInfoTableService")
@Transactional
@SuppressWarnings({"all"})
public class InpatientInfoTableServiceImpl implements InpatientInfoTableService{
	@Autowired
	@Qualifier(value = "inpatientInfoTableDao")
	private InpatientInfoTableDao inpatientInfoTableDao;
	
	/** 参数管理接口 **/
	@Autowired
	@Qualifier(value = "parameterInnerDAO")
	private ParameterInnerDAO parameterInnerDAO;
	
	/**  
	 * 住院病人动态报表 
	 * @Author: wangshujuan
	 * @CreateDate: 2017年7月20日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年7月20日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param deptCode 
	 */
	@Override
	public List<InpatientInfoTableVo> queryInpatientInfoTable(String startTime,String endTime,String deptCode,String menuAlias,String page,String rows) {
		boolean flag=new MongoBasicDao().isCollection("ZZBRDTBB");
		List<InpatientInfoTableVo> list=new ArrayList<InpatientInfoTableVo>();
		if(flag){
			list=inpatientInfoTableDao.queryInpatientInfoTableForDB(startTime,endTime,deptCode,menuAlias,page,rows);
		}else{
			return inpatientInfoTableDao.queryInpatientInfoTable(startTime,endTime,deptCode,menuAlias,page, rows);
		}
		return list;
	}
	/**  
	 * 住院病人动态报表  总条数
	 * @Author: wangshujuan
	 * @CreateDate: 2017年7月20日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年7月20日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param deptCode 
	 */
	@Override
	public int getTotalInpatientInfoTable(String startTime,String endTime,String deptCode,String menuAlias,String page,String rows) {
		boolean flag=new MongoBasicDao().isCollection("SSKSSSFJTJ");
		List<InpatientInfoTableVo> list=new ArrayList<InpatientInfoTableVo>();
		if(flag){
		}else{
			return inpatientInfoTableDao.getTotalInpatientInfoTable(startTime,endTime,deptCode,menuAlias,page, rows);
		}
		return list.size();
	}
}
