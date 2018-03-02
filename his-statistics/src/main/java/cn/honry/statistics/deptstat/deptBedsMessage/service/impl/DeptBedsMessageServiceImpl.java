package cn.honry.statistics.deptstat.deptBedsMessage.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.hiasMongo.basic.MongoBasicDao;
import cn.honry.statistics.deptstat.deptBedsMessage.dao.DeptBedsMessageDao;
import cn.honry.statistics.deptstat.deptBedsMessage.service.DeptBedsMessageService;
import cn.honry.statistics.deptstat.deptBedsMessage.vo.DeptBedsMessageVo;

@Service("deptBedsMessageService")
@Transactional
@SuppressWarnings({"all"})
public class DeptBedsMessageServiceImpl implements DeptBedsMessageService{
	@Autowired
	@Qualifier(value = "deptBedsMessageDao")
	private DeptBedsMessageDao deptBedsMessageDao;
	/**  
	 * 
	 * 科室床位信息查询
	 * @Author: wangshujuan
	 * @CreateDate: 2017年7月6日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年7月6日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param deptCode 
	 *
	 */
	@Override
	public List<DeptBedsMessageVo> queryDeptBedsMessage(String deptCode,String page,String rows,String menuAlias) {
		boolean flag=new MongoBasicDao().isCollection("KSCWXXCX");
		List<DeptBedsMessageVo> list=new ArrayList<DeptBedsMessageVo>();
		if(flag){
			list=deptBedsMessageDao.queryDeptBedsMessageForDB(deptCode);
		}else{
			list=deptBedsMessageDao.queryDeptBedsMessage(deptCode,page, rows,menuAlias);
		}
		return list;
	}
	
	@Override
	public int getTotalDeptBedsMessage(String deptCode,String menuAlias) {
		return deptBedsMessageDao.getTotalDeptBedsMessage(deptCode,menuAlias);
	}
	
}
