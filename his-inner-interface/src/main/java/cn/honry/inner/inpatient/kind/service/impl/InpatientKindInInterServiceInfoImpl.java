package cn.honry.inner.inpatient.kind.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.InpatientKind;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.User;
import cn.honry.inner.inpatient.kind.dao.InpatientKindInInterDAO;
import cn.honry.inner.inpatient.kind.service.InpatientKindInInterService;
import cn.honry.inner.system.utli.OperationUtils;
import cn.honry.utils.DateUtils;
import cn.honry.utils.SessionUtils;

@Service("inpatientKindInInterService")
@Transactional
@SuppressWarnings({ "all" })
public class InpatientKindInInterServiceInfoImpl  implements InpatientKindInInterService{

	/**
	 * 医嘱类型维护
	 * @author  liguikang
	 * @date 2016-03-23
	 * @version 1.0
	 */
	@Autowired
	@Qualifier(value = "inpatientKindInInterDAO")
	private  InpatientKindInInterDAO  inpatientKindInInterDAOInfo;



	@Override
	public void saveOrUpdate(InpatientKind entity) {
		
	}

	
	
	@Override
	public void removeUnused(String id) {
		
	}

	
	@Override
	public InpatientKind get(String id) {
		InpatientKind entity = inpatientKindInInterDAOInfo.get(id);
		return entity;
	}




	@Override
	public void del(String ids) {
		User user = (User) SessionUtils.getCurrentUserFromShiroSession();
		inpatientKindInInterDAOInfo.del(ids,user.getId());	
		OperationUtils.getInstance().conserve(ids,"医嘱类型","UPDATE","T_INPATIENT_KIND",OperationUtils.LOGACTIONDELETE);
		
	}
	
	@Override
	public int getTotal(InpatientKind entity) {
		int total =inpatientKindInInterDAOInfo.getTotal(entity);
		return total;
	}
	
	@Override
	public List<InpatientKind> queryKindInfo() {
		
		return inpatientKindInInterDAOInfo.queryKindInfo();
	}


	@Override
	public List<InpatientKind> getPage(String page, String rows, InpatientKind entity) {
		return inpatientKindInInterDAOInfo.getPage(page, rows, entity);
	}



	/**
	 * 添加$修改
	 * @author  liguikang
	 * @date 2016-03-23
	 * @version 1.0
	 */
		@Override
		public void saveInpatientKind(InpatientKind entity) {
			if(StringUtils.isBlank(entity.getId())){
				entity.setId(null);
				User user = (User) SessionUtils.getCurrentUserFromShiroSession();
				SysDepartment dept = (SysDepartment) SessionUtils.getCurrentUserLoginDepartmentFromShiroSession();
				entity.setCreateUser(user.getId());
				entity.setCreateDept(dept.getId());
				entity.setCreateTime(DateUtils.getCurrentTime());
				entity.setTypeCode(inpatientKindInInterDAOInfo.getSequece("seq_inpatientkind_sequence"));//生成编号
				inpatientKindInInterDAOInfo.save(entity);
				OperationUtils.getInstance().conserve(null,"医嘱类型维护","INSERT INTO","T_INPATIENT_KIND",OperationUtils.LOGACTIONINSERT);
			}else{
				entity.setUpdateTime(DateUtils.getCurrentTime());
				inpatientKindInInterDAOInfo.update(entity);
				OperationUtils.getInstance().conserve(entity.getId(),"医嘱类型维护","UPDATE","T_INPATIENT_KIND",OperationUtils.LOGACTIONUPDATE);
			}
			
		}

	@Override
	public String queryKindInfoByName(String name) {
		return inpatientKindInInterDAOInfo.queryKindInfoByName(name);
	}

}
