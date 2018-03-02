package cn.honry.statistics.deptstat.internalCompare1.service;

import java.util.List;

import javax.servlet.ServletOutputStream;

import cn.honry.statistics.deptstat.internalCompare1.vo.InternalCompare1Vo;

/**  
 *  
 * @className：InternalCompare1DaoImpl
 * @Description： 郑州大学第一附属医院儿外一门诊和内二医学部对比表1
 * @Author：gaotiantian
 * @CreateDate：2017-6-6 上午9:19:52
 * @Modifier：
 * @ModifyDate：
 * @ModifyRmk：  
 * @version 1.0
 *
 */
public interface InternalCompare1Service {
	/**
	 * 查询对比信息
	 * @author gaotiantian
	 * @createDate：2017-6-6 上午9:19:52
	 * @version 1.0
	 */
	List<InternalCompare1Vo> getInternalCompare1(String prevTime, String time, String depts1, String deptName,boolean sign);
	/**
	 * 初始化，向mongodb里面存数据
	 * @author gaotiantian
	 * @createDate：2017-6-7  下午4:35:21
	 * @version 1.0
	 */
	void initCompare1List(String begin, String end, String[] dept);
	/**
	 * 导出Excel
	 * @author gaotiantian
	 * @createDate：2017-6-12  下午11:53:21
	 * @version 1.0
	 */
	void exportExcel(ServletOutputStream stream, List<InternalCompare1Vo> journalVos,String date);
	/**
	 * <p>根据虚拟科室code获取科室id </p>
	 * @Author: gaotiantian
	 * @CreateDate: 2017年7月11日 上午10:18:29 
	 * @Modifier: gaotiantian
	 * @ModifyDate: 2017年7月11日 上午10:18:29 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param ficDeptCode
	 * @return:String
	 * @throws:
	 *
	 */
	String getDept(String ficDeptCode);
	
	/**
	 * 医院内科医学部和内二医学部对比表21预处理
	 * @param begin
	 * @param end
	 * @param type
	 */
	public void initCompare1(String begin, String end, Integer type) throws Exception;
}

