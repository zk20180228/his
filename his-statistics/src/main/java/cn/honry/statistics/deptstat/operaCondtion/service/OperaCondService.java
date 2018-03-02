package cn.honry.statistics.deptstat.operaCondtion.service;

import java.util.List;

import cn.honry.statistics.deptstat.operaCondtion.vo.OperaCondVo;
/**
 * 
 * 
 * <p>手术情况service </p>
 * @Author: XCL
 * @CreateDate: 2017年7月15日 下午5:00:45 
 * @Modifier: XCL
 * @ModifyDate: 2017年7月15日 下午5:00:45 
 * @ModifyRmk:  
 * @version: V1.0:
 *
 */
public interface OperaCondService {
	/**
	 * 
	 * 
	 * <p>手术情况查询 </p>
	 * @Author: XCL
	 * @CreateDate: 2017年7月15日 下午4:58:08 
	 * @Modifier: XCL
	 * @ModifyDate: 2017年7月15日 下午4:58:08 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param tnL 分区表
	 * @param begin 开始时间
	 * @param end 结束时间
	 * @param menuAlias 栏目别名
 	 * @param depts 科室code
	 * @return:
	 * @throws Exception 
	 *
	 */
	public List<OperaCondVo> queryOperaList(String begin,String end,String menuAlias,String depts,String page,String rows) throws Exception;

	/**
	 * 
	 * 
	 * <p>查询手术情况总条数 </p>
	 * @Author: XCL
	 * @CreateDate: 2017年7月15日 下午6:30:14 
	 * @Modifier: XCL
	 * @ModifyDate: 2017年7月15日 下午6:30:14 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param tnL
	 * @param begin
	 * @param end
	 * @param menuAlias
	 * @param depts
	 * @return
	 * @throws Exception:
	 *
	 */
	public int queryOperaTotal(String begin,String end,String menuAlias,String depts) throws Exception;
}
