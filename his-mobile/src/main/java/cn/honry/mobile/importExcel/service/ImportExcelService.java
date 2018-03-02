package cn.honry.mobile.importExcel.service;

import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.service.BaseService;


public interface ImportExcelService extends BaseService<SysEmployee>{

	/** 根据用户账号更新用户信息
	* @return emp 用户实体
	* @author zxl
	* @date 2017年6月16日
	*/
	void updateEmp(SysEmployee emp);

}
