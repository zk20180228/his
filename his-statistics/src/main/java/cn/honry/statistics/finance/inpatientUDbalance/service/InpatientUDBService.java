package cn.honry.statistics.finance.inpatientUDbalance.service;

import java.util.List;

import javax.servlet.ServletOutputStream;

import cn.honry.base.service.BaseService;
import cn.honry.statistics.finance.inpatientUDbalance.vo.InpatientUDBVo;

public interface InpatientUDBService extends BaseService<InpatientUDBVo>{
	
	/**
	 * 
	 * <p> 收款员列表</p>
	 * @Author:
	 * @CreateDate: 2017年7月4日 下午5:36:06 
	 * @Modifier: zhangkui
	 * @ModifyDate: 2017年7月4日 下午5:36:06 
	 * @ModifyRmk:  添加注释
	 * @version: V1.0
	 * @param date 时间:yyyy-MM-dd
	 * @return List<InpatientUDBVo> 收款员列表集合
	 * @throws:
	 *
	 */
	List<InpatientUDBVo> queryDateInfo(String date);


	/**
	 * 
	 * <p>门诊收款员缴款单导出 </p>
	 * @Author: 
	 * @CreateDate: 2017年7月4日 下午5:37:10 
	 * @Modifier: zhangkui
	 * @ModifyDate: 2017年7月4日 下午5:37:10 
	 * @ModifyRmk:  添加注释
	 * @version: V1.0
	 * @param stream ServletOutputStream strea
	 * @param idbvol List<InpatientUDBVo> idbvol
	 * @throws:Exception
	 *
	 */
	void exportExcel(ServletOutputStream stream, List<InpatientUDBVo> idbvol)throws Exception;

}
