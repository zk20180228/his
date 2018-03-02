package cn.honry.inner.emr.emrMain.dao;

import java.sql.Clob;
import java.util.List;

import cn.honry.base.bean.model.EmrMain;
import cn.honry.base.dao.EntityDao;

public interface EmrMainInnerDao extends EntityDao<EmrMain> {
	/** 电子病历分页(用于住院医生站)
	* @Title: emrCount 电子病历分页
	* @Description: 电子病历分页
	* @param medicalrecordId 病历号
	* @param emrName 查询变量
	* @author dtl 
	* @date 2017年4月5日
	*/
	public int emrCount(String medicalrecordId, String emrName);
	/** 根据患者病历号、查询变量、患者姓名查询电子病历列表(用于住院医生站)
	* @Title: emrList 根据患者病历号、查询变量、患者姓名查询电子病历列表
	* @Description: 根据患者病历号、查询变量、患者姓名查询电子病历列表
	* @param medicalrecordId 病历号
	* @param emrName 查询变量
	* @param name 患者姓名，显示用，不用查询患者表
	* @param rows 行数
	* @param page 页数
	* @author dtl 
	* @date 2017年4月5日
	*/
	public List<EmrMain> emrList(String medicalrecordId, String emrName,
			String name, String rows, String page);
	
	/** 大数据类型Clob转换为String 
	* @Title: ClobToString 
	* @Description: 大数据类型Clob转换为String
	* @param clob 大数据类型
	* @author dtl 
	* @date 2017年4月5日
	*/
	String ClobToString(Clob clob);
}
