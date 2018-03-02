package cn.honry.inner.technical.pharmacy.service;

import java.util.List;

import cn.honry.base.bean.model.StoTerminal;
import cn.honry.base.service.BaseService;

/***
 * @Description:门诊终端维护接口
 * @author  wfj
 * @date 创建时间：2016年1月23日
 * @version 1.0
 * @since
 */
public interface PharmacyInInterService extends BaseService<StoTerminal>{

	
	/**
	 * 配药台和发药窗口的列表信息
	 * 当page、rows 为null时，获取所有信息
	 * 否则按照分页查询
	 * @author  wfj
	 * @date 创建时间：2016年1月19日 
	 * @version 1.0
	 * @since  terminal json信息
	 */
	List<StoTerminal> queryTerminalList(String type,String deptCode,StoTerminal stoTerminal,String page,String rows);
	
	/**
	 * @author  wfj
	 * @date 创建时间：2016年1月19日 
	 * @version 1.0
	 * @since  terminal json信息
	 */
	int queryTerminalCount(String type,String deptCode,StoTerminal stoTerminal);
	
}
