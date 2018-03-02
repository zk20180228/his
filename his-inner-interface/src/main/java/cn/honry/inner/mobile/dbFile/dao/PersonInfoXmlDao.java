package cn.honry.inner.mobile.dbFile.dao;

import java.util.List;

import cn.honry.base.bean.model.FictitiousContact;
import cn.honry.base.bean.model.FictitiousDept;
import cn.honry.base.bean.model.SysEmployee;

public interface PersonInfoXmlDao {
	//查修虚拟科室(第一字节点)
	public List<FictitiousDept> findVirtualDept(String code_encode) throws Exception;
	//查询实际科室(第二子节点)
	public List<FictitiousContact> findActualDept(FictitiousDept tfd) throws Exception;
	//查询人员信息
	public List<SysEmployee> findPersonInfo(String dept_code) throws Exception;

}
