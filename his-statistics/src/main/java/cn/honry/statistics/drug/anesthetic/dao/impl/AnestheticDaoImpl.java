package cn.honry.statistics.drug.anesthetic.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.honry.statistics.drug.anesthetic.dao.AnestheticDao;
import cn.honry.statistics.drug.anesthetic.vo.Anestheticvo;
@Repository("anestheticDao")
@SuppressWarnings({"all"})
public class AnestheticDaoImpl implements AnestheticDao{
	
	@Resource
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	/**
	 *
	 * @Description：麻醉精神药品统计
	 * @Author：zhangjin
	 * @CreateDate：2016年6月22日
	 * @Modifier：
	 * @ModifyDate：
	 * @ModifyRmk：  
	 * @version： 1.0：
	 *setParameter("sTime", login).setParameter("eTime", end).setParameter("deptCode", deptId)
	 */
	@Override
	public List<Anestheticvo> getAnestheList(String login,String end,String drug,String deptId, String rows, String page, String flag,Map<String, List<String>> map) {
		Map<String, Object> pMap = new HashMap<>();
		if ("0".equals(flag)) {
			int start = Integer.parseInt(page == null ? "1" : page);
			int count = Integer.parseInt(rows == null ? "20" : rows);
			pMap.put("page", start);
			pMap.put("rows", count);
		}
		pMap.put("sTime", login);
		pMap.put("eTime", end);
		pMap.put("deptCode", deptId);
		if (!"1".equals(drug)) {
			pMap.put("drugType", drug);
		}
		StringBuffer sqlBuffer = new StringBuffer();
		if ("0".equals(flag)) {
			sqlBuffer.append("SELECT * from (select rownum as n,deptName,drugName,drugedName,drugSpec,drugPack,num,drugedDate,meark,pno,patientName,doctName,doctCode from (");
		}
		sqlBuffer.append(getSql(drug, deptId,"0",map));
		if ("0".equals(flag)) {
			sqlBuffer.append(")where rownum <= :page * :rows ) where n > (:page - 1) * :rows");
		}
		List<Anestheticvo> list = namedParameterJdbcTemplate.query(sqlBuffer.toString(), pMap, new RowMapper<Anestheticvo>(){

			@Override
			public Anestheticvo mapRow(ResultSet rs, int rowNum) throws SQLException {
				Anestheticvo vo = new Anestheticvo();
				vo.setDeptName(rs.getString("deptName"));
				vo.setPno(rs.getString("pno"));
				vo.setPatientName(rs.getString("patientName"));
				vo.setDoctName(rs.getString("doctName"));
				vo.setDrugName(rs.getString("drugName"));
				vo.setDrugSpec(rs.getString("drugSpec"));
				vo.setDrugPack(rs.getString("drugPack"));
				vo.setNum(rs.getDouble("num"));
				vo.setDrugedDate(rs.getTimestamp("drugedDate"));
				vo.setMeark(rs.getString("meark"));
				vo.setDoctCode(rs.getString("doctCode"));
				return vo;
			}
			
		});
		if(list.size()>0&&list!=null){
			return list;
		}
		return new ArrayList<Anestheticvo>();
	}
	/** 拼接sql
	* @Title: getSql 
	* @param drug 1：全部
	* @param deptId 1：全部
	* @param countFlag 1：查条数
	* @author dtl 
	* @date 2017年3月27日
	*/
	StringBuffer getSql(String drug, String deptId, String countFlag,Map<String, List<String>> map){
		StringBuffer sqlBuffer = new StringBuffer();
		sqlBuffer.append("select di.drug_commonname as drugName,dout.id,dout.deptName,dout.drugedName,dout.drugSpec,dout.drugPack,");
		sqlBuffer.append("dout.drugedDate,dout.meark,");
		if ("0".equals(countFlag)) {
			sqlBuffer.append("patient.medicalrecordid as pno,patient.patientName,doct.doctName,doct.doctCode,");
		}
		sqlBuffer.append("case when di.show_flag = 1 then trunc(dout.num,2) when di.show_flag = 0 then trunc(dout.num / di.drug_packagingnum,2) end as num ");
		sqlBuffer.append("from (");
		/**
		 * 出库记录
		 */
		List<String> list = map.get("T_DRUG_OUTSTORE");
		for (int i = 0; i < list.size(); i++) {
			if(i!=0){
				sqlBuffer.append("union all ");
			}
			sqlBuffer.append("select outo.id as id,outo.drug_storage_name as deptName,outo.drug_storage_code as deptCode,outo.druged_name as drugedName,");
			sqlBuffer.append("outo.specs as drugSpec,outo.pack_unit as drugPack,outo.drug_type as drugType,outo.out_num as num,outo.out_date as drugedDate,");
			sqlBuffer.append("outo.remark as meark,outo.drug_code as drug_code,outo.op_type as op_type,outo.recipe_no as recipe_no,outo.sequence_no as sequence_no,outo.get_person as get_person,");
			sqlBuffer.append("outo.pack_unit as packUnit,outo.min_unit as minUnit ");
			sqlBuffer.append("from "+list.get(i)+" outo ");
		}
		sqlBuffer.append(" ) dout ");
//		sqlBuffer.append("select outn.id as id,outn.drug_storage_name as deptName,outn.drug_storage_code as deptCode,outn.druged_name as drugedName,");
//		sqlBuffer.append("outn.specs as drugSpec,outn.pack_unit as drugPack,outn.drug_type as drugType,outn.out_num as num,outn.out_date as drugedDate,");
//		sqlBuffer.append("outn.remark as meark,outn.drug_code as drug_code,outn.op_type as op_type,outn.recipe_no as recipe_no,outn.sequence_no as sequence_no,outn.get_person as get_person,");
//		sqlBuffer.append("outn.pack_unit as packUnit,outn.min_unit as minUnit ");
//		sqlBuffer.append("from t_drug_outstore_now outn) dout ");
		if ("0".equals(countFlag)) {
			List<String> list2 = map.get("T_REGISTER_MAIN");
			List<String> list3 = map.get("T_INPATIENT_INFO");
			List<String> list4 = map.get("T_OUTPATIENT_FEEDETAIL");
			List<String> list5 = map.get("T_INPATIENT_MEDICINELIST");
			/**
			 * 病历号、患者姓名
			 */
			sqlBuffer.append("left join (");
			for (int i = 0; i < list2.size(); i++) {
				if(i!=0){
					sqlBuffer.append("union all ");
				}
				sqlBuffer.append("select rmo.medicalrecordid as medicalrecordid,rmo.clinic_code as get_person,rmo.patient_name as patientName ");
				sqlBuffer.append("from "+list2.get(i)+" rmo ");
			}
//			sqlBuffer.append("union all ");
//			sqlBuffer.append("select rmn.medicalrecordid as medicalrecordid,rmn.clinic_code as get_person,rmn.patient_name as patientName ");
//			sqlBuffer.append("from t_register_main_now rmn ");
			sqlBuffer.append("union all ");
			for (int i = 0; i < list3.size(); i++) {
				if(i!=0){
					sqlBuffer.append("union all ");
				}
				sqlBuffer.append("select iio.medicalrecord_id as medicalrecordid,iio.inpatient_no as get_person,iio.patient_name as patientName ");
				sqlBuffer.append("from "+list3.get(i)+" iio ");
			}
			sqlBuffer.append(" ) patient on dout.get_person = patient.get_person ");
//			sqlBuffer.append("union all ");
//			sqlBuffer.append("select iin.medicalrecord_id as medicalrecordid,iin.inpatient_no as get_person,iin.patient_name as patientName ");
//			sqlBuffer.append("from t_inpatient_info_now iin) patient on dout.get_person = patient.get_person ");
			/**
			 * 开方医师
			 */
			sqlBuffer.append("left join (");
			for (int i = 0; i < list4.size(); i++) {
				if(i!=0){
					sqlBuffer.append("union all ");
				}
				sqlBuffer.append("select ofo.recipe_no as recipe_no,ofo.sequence_no as sequence_no,ofo.doct_code as doctCode,ofo.doct_codename as doctName ");
				sqlBuffer.append("from "+list4.get(i)+" ofo ");
			}
//			sqlBuffer.append("union all ");
//			sqlBuffer.append("select ofn.recipe_no as recipe_no,ofn.sequence_no as sequence_no,ofn.doct_code as doctCode,ofn.doct_codename as doctName ");
//			sqlBuffer.append("from t_outpatient_feedetail_now ofn ");
			sqlBuffer.append("union all ");
			for (int i = 0; i < list5.size(); i++) {
				if(i!=0){
					sqlBuffer.append("union all ");
				}
				sqlBuffer.append("select imo.recipe_no as recipe_no,imo.sequence_no as sequence_no,imo.recipe_doccode as doctCode,imo.recipe_docname as doctName ");
				sqlBuffer.append("from "+list5.get(i)+" imo ");
			}
			sqlBuffer.append(" ) doct on dout.recipe_no = doct.recipe_no and dout.sequence_no = doct.sequence_no ");
//			sqlBuffer.append("union all ");
//			sqlBuffer.append("select imn.recipe_no as recipe_no,imn.sequence_no as sequence_no,imn.recipe_doccode as doctCode,imn.recipe_docname as doctName ");
//			sqlBuffer.append("from t_inpatient_medicinelist_now imn) doct on dout.recipe_no = doct.recipe_no and dout.sequence_no = doct.sequence_no ");
		}
		/**
		 * 药品信息
		 */
		sqlBuffer.append("left join t_drug_info di on dout.drug_code = di.drug_code ");
		/**
		 * 查询条件
		 */
		sqlBuffer.append("where dout.op_type in ('1', '3', '4', '5') and di.drug_nature in ('S','P') ");
		sqlBuffer.append("and dout.drugedDate >= to_date(:sTime, 'yyyy-mm-dd hh24:mi:ss') and dout.drugedDate < to_date(:eTime, 'yyyy-mm-dd hh24:mi:ss') ");
		if (!"1".equals(deptId)) {
			sqlBuffer.append("and dout.deptCode = :deptCode ");
		}
		if (!"1".equals(drug)) {
			sqlBuffer.append(" and dout.drugType=:drugType");
		}
		return sqlBuffer;
	}
	/**
	 *
	 * @Description：获取当前登陆科室
	 * @Author：zhangjin
	 * @CreateDate：2016年6月22日
	 * @Modifier：
	 * @ModifyDate：
	 * @ModifyRmk：  
	 * @version： 1.0：
	 *
	 */
	@Override
	public String getDeptName(String deptId) {
		Map<String, Object> map = new HashMap<String, Object>();
		String hql="select dept_name from t_department where dept_Code='"+deptId+"' and stop_flg=0 and del_flg=0";
		String name = namedParameterJdbcTemplate.queryForObject(hql,map,java.lang.String.class);
		if (StringUtils.isNotBlank(name)) {
			return name;
		}
		return "";
	}
	@Override
	public Integer getAnestheTotal(String login, String end, String drug, String deptId,Map<String, List<String>> map) {
		StringBuffer sqlBuffer = new StringBuffer();
		Map<String, Object> pMap = new HashMap<>();
		pMap.put("sTime", login);
		pMap.put("eTime", end);
		pMap.put("deptCode", deptId);
		if (!"1".equals(drug)) {
			pMap.put("drugType", drug);
		}
		sqlBuffer.append("select count(1) from (");
		sqlBuffer.append(getSql(drug, deptId,"1",map));
		sqlBuffer.append(")");
		int total =  namedParameterJdbcTemplate.queryForObject(sqlBuffer.toString(),pMap,java.lang.Integer.class); 
		return total; 
	}

}
