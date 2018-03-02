package cn.honry.statistics.deptstat.out_inpatient_work.service;

import java.util.List;

import cn.honry.statistics.deptstat.out_inpatient_work.vo.OutInpatientWorkVo;
import cn.honry.utils.FileUtil;

public interface OutInpatientWorkService{

	/**
	 * @Description:门诊住院工作(某)月份同期对比表
	 * void 
	 * @exception:
	 * @param Btime 开始时间: 年月 yyyy-MM
	 * @param:Etime 结束时间 ：年月 yyyy-MM
	 * @author: zhangkui
	 * @time:2017年6月2日 下午1:46:22
	 */
	public List<OutInpatientWorkVo> outInpatientWorkList(String Btime,String Etime,String areaCode)throws Exception;

	public FileUtil export(List<OutInpatientWorkVo> list, FileUtil fUtil)throws Exception;

	/**
	 * @Description:mongo存的数据依次是：急诊人次：科室统计--门诊住院工作同期对比表    按月存：急诊人次，时间，院区，标记
	 * 入院人次：科室统计--门诊住院工作同期对比表    按月存：入院人次，时间，院区，标记
	 * 出院人次：科室统计--门诊住院工作同期对比表     按月存：出院人次,时间(月),时间和(出院时间-入院时间),院区，标记
	 * 病床使用率：科室统计--门诊住院工作同期对比表    按天：当天是占有状态的床位数 ，当天总的床位数，院区，标记
	 * @param Btime 查询开始时间
	 * @param Etime 查询结束时间
	 * @param areaCode 院区编号
	 * @return
	 * @throws Exception
	 * List<OutInpatientWorkVo>
	 * @exception:
	 * @author: zhangkui
	 * @time:2017年6月8日 下午5:12:47
	 */
	public List<OutInpatientWorkVo> outInpatientWorkListByMongo(String Btime,String Etime,String areaCode) throws Exception;
	
}
