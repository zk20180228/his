package cn.honry.report.dao.impl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.j2ee.servlets.BaseHttpServlet;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.BirthCertificate;
import cn.honry.base.bean.model.CriticalNotice;
import cn.honry.base.bean.model.DepositReminder;
import cn.honry.base.bean.model.EmrRecordMain;
import cn.honry.base.bean.model.StoRecipeNow;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.report.dao.IReportDao;

@Repository("iReportDao")
@SuppressWarnings({ "all" })
public class IReportDaoImpl extends HibernateEntityDao implements IReportDao{
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	
	@Override
	public void doReport(HttpServletRequest request,
			HttpServletResponse response, String fileName, HashMap parameterMap) throws Exception {
		StringBuffer path=new StringBuffer();
		path.append(request.getSession().getServletContext().getRealPath("/"));
		path.append("WEB-INF"+File.separator);
		path.append("reportFormat"+File.separator+"jasper"+File.separator);
		fileName=path+fileName;
		//获取文件流
		DataSource ds = SessionFactoryUtils.getDataSource(this.getSessionFactory());
		Connection con = ds.getConnection();
		ByteArrayOutputStream os=this.getOutput(fileName, parameterMap,con);
		response.setCharacterEncoding("UTF-8"); 
		response.setContentType("application/pdf");
		response.setContentLength(os.size()); 
		ServletOutputStream out = response.getOutputStream();
		os.writeTo(out); 
		out.flush();
		out.close();
		con.close();
	}
	
	/**
	 * @Description： 获得输出流
	 * @Author：hedong
	 * @CreateDate：2016-03-08  上午10:50:11  
	 * @ModifyRmk：  
	 * @version 1.0
	 * @param filename 文件名称
	 * @param parameter  参数map
	 * @param con  数据库连接
	 * @return
	 */
	private ByteArrayOutputStream getOutput(String filename, Map parameter, Connection con)  {
		      ByteArrayOutputStream os = new ByteArrayOutputStream();
		      JasperPrint jasperPrint;
		      String jasperFilename = filename + "." + "jasper";
		      File f = new File(jasperFilename);
		      if (!(f.exists()))
		      {
		         System.out.println(jasperFilename+">>>不存在!!!");
		      }
			  try {
				jasperPrint = JasperFillManager.fillReport(jasperFilename, parameter, con);
				JasperExportManager.exportReportToPdfStream(jasperPrint, os);
			  } catch (Exception e) {
				System.out.println(e.getMessage());
			  }
		      return os;
	}


	@Override
	public SysDepartment queryDept(String drugstore) {
		String hql="from SysDepartment where del_flg=0 and stop_flg=0 and id='"+drugstore+"'";
		List<SysDepartment> list=super.find(hql, null);
		if(list!=null&&list.size()>0){
			return list.get(0);
		}
		return null;
	}

	@Override
	public void doReportToJavaBean(HttpServletRequest request, HttpServletResponse response, String reportFilePath,
			Map<String, Object> parameters,JRDataSource dataSource)throws Exception  {
		    JasperReport report = (JasperReport)JRLoader.loadObjectFromFile(reportFilePath);
		    JasperPrint jasperPrint = JasperFillManager.fillReport(report, parameters, dataSource);
		    OutputStream ouputStream = response.getOutputStream();  
		    // 使用JRPdfExproter导出器导出pdf  
		    JRPdfExporter exporter = new JRPdfExporter();  
		    exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		    exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, ouputStream);  
		    exporter.exportReport();
		    ouputStream.close();
	}
	/**
	 * @Description：jdbc批量报表打印，在一张pdf
	 */
	@Override
	public void doReport2(HttpServletRequest request,
			HttpServletResponse response, String fileName,  List<HashMap<String,Object>> o)
			throws Exception {
		StringBuffer path=new StringBuffer();
		path.append(request.getSession().getServletContext().getRealPath("/"));
		path.append("WEB-INF"+File.separator);
		path.append("reportFormat"+File.separator+"jasper"+File.separator);
		fileName=path+fileName + "." + "jasper";
		DataSource ds = SessionFactoryUtils.getDataSource(this.getSessionFactory());
		Connection con = ds.getConnection();	 
	    List jasperPrintList = new ArrayList();
	    ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    JasperPrint jasperPrint;
	    for (HashMap<String,Object> map : o) {
	    	jasperPrint = JasperFillManager.fillReport(fileName, map, con);
			JasperExportManager.exportReportToPdfStream(jasperPrint, baos);
	    	jasperPrintList.add(jasperPrint);
		}	   
	    // 使用JRPdfExproter导出器导出pdf  
	    JRPdfExporter exporter = new JRPdfExporter();  
	    exporter.setParameter(JRExporterParameter.JASPER_PRINT_LIST, jasperPrintList);
	    exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, baos);  
	    exporter.exportReport();
	    response.setCharacterEncoding("UTF-8"); 
		response.setContentLength(baos.size()); 
		ServletOutputStream out = response.getOutputStream();
		baos.writeTo(out); 
		out.flush();
		out.close();
		con.close();
	    baos.close();
		
	}
	/**
	 * @Description：javaBean批量报表打印，在一张pdf
	 */
	@Override
	public void doReportToJavaBean2(HttpServletRequest request,
			HttpServletResponse response, String reportFilePath,
			List<HashMap<String, Object>> o)
			throws Exception {
		 	JasperReport report = (JasperReport)JRLoader.loadObjectFromFile(reportFilePath);
		 	List jasperPrintList = new ArrayList();
		 	for (HashMap<String, Object> hashMap : o) {
		 		JasperPrint jasperPrint = JasperFillManager.fillReport(report, hashMap, (JRDataSource)hashMap.get("dataSource"));
			 	jasperPrintList.add(jasperPrint);
			}		   
		    OutputStream ouputStream = response.getOutputStream();  
		    // 使用JRPdfExproter导出器导出pdf  
		    JRPdfExporter exporter = new JRPdfExporter();  
		    exporter.setParameter(JRExporterParameter.JASPER_PRINT_LIST, jasperPrintList);
		    exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, ouputStream);  
		    exporter.exportReport();
		    ouputStream.close();
	}
	/**
	 * @Description：jdbc和javaBean批量报表打印，在一张pdf
	 */
	@Override
	public void doReport3(HttpServletRequest request,
			HttpServletResponse response, List<HashMap<String, Object>> o)
			throws Exception {
		StringBuffer path=new StringBuffer();
		path.append(request.getSession().getServletContext().getRealPath("/"));
		
		path.append("WEB-INF"+File.separator);
		path.append("reportFormat"+File.separator+"jasper"+File.separator);	
		
	    List jasperPrintList = new ArrayList();
	    ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    JasperPrint jasperPrint;
	    String fileName;
	    DataSource ds = SessionFactoryUtils.getDataSource(this.getSessionFactory());
		Connection con = ds.getConnection();
	    for (HashMap<String,Object> map : o) {	    	
	    	if (map.get("JRDataSource")!=null) {
	    		JasperReport report = (JasperReport)JRLoader.loadObjectFromFile((String)map.get("reportFilePath"));
	    		jasperPrint = JasperFillManager.fillReport(report, map,(JRDataSource)map.get("JRDataSource"));
			}else{
				fileName=(String) map.get("fileName");
		    	fileName=path+fileName+ "." + "jasper";
			   	jasperPrint = JasperFillManager.fillReport(fileName, map, con);	
			   	JasperExportManager.exportReportToPdfStream(jasperPrint, baos);
			}	 
			
	    	jasperPrintList.add(jasperPrint);
		}	   
	    // 使用JRPdfExproter导出器导出pdf  
	    JRPdfExporter exporter = new JRPdfExporter();  
	    exporter.setParameter(JRExporterParameter.JASPER_PRINT_LIST, jasperPrintList);
	    exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, baos);  
	    exporter.exportReport();
	    response.setCharacterEncoding("UTF-8"); 
		response.setContentLength(baos.size()); 
		ServletOutputStream out = response.getOutputStream();
		baos.writeTo(out); 
		out.flush();
		out.close();
		con.close();
	    baos.close();
		
	}

	@Override
	public String getSendDrugWin(String invoiceNo) {
		final StringBuffer sb = new StringBuffer();
		sb.append(" select Distinct t.SEND_TERMINAL_NAME sendTerminalName from t_sto_recipe_now t ");
		sb.append(" where t.RECIPE_NO in( select o.recipe_no from t_outpatient_feedetail_now o Where o.Invoice_No In ('"+invoiceNo+"')) ");
		List<StoRecipeNow> list = this.getHibernateTemplate().execute(new HibernateCallback<List<StoRecipeNow>>() {

			@Override
			public List<StoRecipeNow> doInHibernate(Session session)
					throws HibernateException, SQLException {
				SQLQuery query = session.createSQLQuery(sb.toString());
				query.addScalar("sendTerminalName");
				return query.setResultTransformer(Transformers.aliasToBean(StoRecipeNow.class)).list();
			}
		});
		String win = "";
		if(list!=null&&list.size()>0){
			for (StoRecipeNow stoRecipeNow : list) {
				if(StringUtils.isNotBlank(win)){
					win += ",";
				}
				win += stoRecipeNow.getSendTerminalName();
			}
			return win;
		}
		return null;
	}

	@Override
	public List<CriticalNotice> queryCriticalNotice(String inpatientNo) {
		String sql="select (select Hospital_name from T_HOSPITAL where Hospital_id = '1') hospitalName,info.patient_name,"
				+ "sysdate datesj,info.bedward_name,info.house_doc_name,info.duty_nurse_name from t_Inpatient_Info_Now info where "
				+ "info.inpatient_no ='"+inpatientNo+"'";
		SQLQuery query = this.getSession().createSQLQuery(sql);
		query.addScalar("hospitalName").addScalar("patient_name").addScalar("datesj").addScalar("bedward_name").addScalar("house_doc_name")
		.addScalar("duty_nurse_name").setResultTransformer(Transformers.aliasToBean(CriticalNotice.class));
		List<CriticalNotice> list=query.list();
		return list;
	}

	@Override
	public List<BirthCertificate> queryBirthCertificate(String id) {
		String sql="select t.name,t.sex_code,t.birthday,t.gestation,t.weight,t.height,d.city_name,(select x.city_name as pcityname "
				+ " from (select d.city_parentid  from t_inpatient_babyinfo_now t, t_district d where t.birt_place = d.id) baby,t_district x "
				+ "where baby.city_parentid = x.id) as shi,(select z.city_name  from (select x.city_parentid as pcityname from (select d.city_parentid "
				+ "from t_inpatient_babyinfo_now t, t_district d where t.birt_place = d.id) baby,t_district x where baby.city_parentid = x.id) f, t_district z"
				+ " where f.pcityname = z.id) as sheng,t.facility,t.mother_name,t.mother_age,(SELECT c.CODE_NAME FROM T_BUSINESS_DICTIONARY c "
				+ "WHERE t.mother_nationality = c.code_encode and c.code_type='country') as mcountry,(SELECT n.CODE_NAME FROM T_BUSINESS_DICTIONARY n WHERE "
				+ "t.mother_nation = n.code_encode and n.code_type='nationality') as mnation,t.home,t.mother_card_no,t.father_name,t.father_age,(SELECT co.CODE_NAME"
				+ " FROM T_BUSINESS_DICTIONARY co WHERE t.father_nationality = co.code_encode and co.code_type='country') as fcountry,(SELECT na.CODE_NAME "
				+ "FROM T_BUSINESS_DICTIONARY na WHERE t.father_nation = na.code_encode and na.code_type='nationality') as fnation,t.home,t.father_cardn_no,"
				+ "t.facility,t.issue_date,t.birth_certificate_no  from t_inpatient_babyinfo_now t  Left join t_district d on t.birt_place = d.id where "
				+ "t.id ='"+id+"'";
		SQLQuery query = this.getSession().createSQLQuery(sql);
		query.addScalar("name").addScalar("sex_code").addScalar("birthday",Hibernate.DATE).addScalar("gestation").addScalar("weight",Hibernate.DOUBLE).addScalar("height",Hibernate.DOUBLE)
		.addScalar("city_name").addScalar("shi").addScalar("sheng").addScalar("facility").addScalar("mother_name").addScalar("mother_age").addScalar("mcountry")
		.addScalar("mnation").addScalar("mother_card_no").addScalar("father_name").addScalar("father_age").addScalar("fcountry")
		.addScalar("fnation").addScalar("home").addScalar("father_cardn_no").addScalar("issue_date",Hibernate.DATE).addScalar("birth_certificate_no")
		.setResultTransformer(Transformers.aliasToBean(BirthCertificate.class));
		List<BirthCertificate> list = query.list();
		return list;
	}

	@Override
	public List<DepositReminder> queryDepositReminder(String inpatientNo) {
		String sql="select to_char(sysdate, 'yyyy-MM-dd') as newdate,i.NURSE_CELL_NAME as BEDWARD,i.bed_name as BEDNAME,i.patient_name as PATIENTNAME,"
				+ "to_char(i.PREPAY_COST) as CHANGECOST,to_char(i.TOT_COST) as TOTCOST,to_char(i.FREE_COST) as FREECOST,i.inpatient_no as INPATIENTNO,"
				+ " i.MEDICALRECORD_ID as medicalrecordId,to_char(i.in_date, 'yyyy-MM-dd') as INDATE from t_inpatient_info_now i where "
				+ "i.inpatient_no in('"+inpatientNo+"') and i.del_flg = 0 and i.stop_flg = 0";
		SQLQuery query = this.getSession().createSQLQuery(sql);
		query.addScalar("newdate",Hibernate.DATE).addScalar("bedward").addScalar("medicalrecordId").addScalar("bedname").addScalar("patientname").addScalar("changecost",Hibernate.DOUBLE).addScalar("totcost",Hibernate.DOUBLE)
		.addScalar("freecost",Hibernate.DOUBLE).addScalar("inpatientno").addScalar("indate",Hibernate.DATE).setResultTransformer(Transformers.aliasToBean(DepositReminder.class));
		List<DepositReminder> list = query.list();
		return list;
	}
	
	
	//病案查询打印
	public List<EmrRecordMain> recordPrint(String deptCode, String recCode,
			String patientName, String sex, Integer ageStart, Integer ageEnd,
			String outDateS, String outDateE, String digNose,
			String birthStart, String birthEnd) {
//		int p = StringUtils.isNotBlank(page)?Integer.valueOf(page):1;
//		int r = StringUtils.isNotBlank(rows)?Integer.valueOf(rows):20;
		StringBuffer sb = new StringBuffer();
		sb.append("from EmrRecordMain  where state = 1 and stop_flg=0 and del_flg=0 ");
		if(StringUtils.isNotBlank(deptCode)){
			sb.append(" and outDept in(:deptCode) ");
		}
		if(StringUtils.isNotBlank(recCode)){
			sb.append(" and code = :recCode ");
		}
		if(StringUtils.isNotBlank(patientName)){
			sb.append(" and patientName like '%"+patientName+"%' ");
		}
		if(StringUtils.isNotBlank(sex)){
			sb.append(" and patientSex = :sex ");
		}
		if(ageStart!=null){
			sb.append(" and patientAge > :ageStart");
		}
		if(ageEnd!=null){
			sb.append(" and patientAge <= :ageEnd");
		}
		if(StringUtils.isNotBlank(outDateS)){
			sb.append(" and trunc(outDate,'dd') > to_date(:outDateS,'yyyy-MM-dd') ");
		}
		if(StringUtils.isNotBlank(outDateE)){
			sb.append(" and trunc(outDate,'dd') > to_date(:outDateE,'yyyy-MM-dd') ");
		}
		if(StringUtils.isNotBlank(digNose)){
			sb.append(" and dignose = :digNose ");
		}
		if(StringUtils.isNotBlank(birthStart)){
			sb.append(" and trunc(patientBirth,'dd') > to_date(:birthStart,'yyyy-MM-dd') ");
		}
		if(StringUtils.isNotBlank(birthEnd)){
			sb.append(" and trunc(patientBirth,'dd') <= to_date(:birthEnd,'yyyy-MM-dd') ");
		}
		
		Query query = this.createQuery(sb.toString());
		if(StringUtils.isNotBlank(deptCode)){
			query.setParameterList("deptCode", Arrays.asList(deptCode.split(",")));
		}
		if(StringUtils.isNotBlank(recCode)){
			query.setParameter("recCode", recCode);
		}
		if(StringUtils.isNotBlank(sex)){
			query.setParameter("sex", sex);
		}
		if(ageStart!=null){
			query.setParameter("ageStart", ageStart);
		}
		if(ageEnd!=null){
			query.setParameter("ageEnd", ageEnd);
		}
		if(StringUtils.isNotBlank(outDateS)){
			query.setParameter("outDateS", outDateS);
		}
		if(StringUtils.isNotBlank(outDateE)){
			query.setParameter("outDateE", outDateE);
		}
		if(StringUtils.isNotBlank(digNose)){
			query.setParameter("digNose", digNose);
		}
		if(StringUtils.isNotBlank(birthStart)){
			query.setParameter("birthStart", birthStart);
		}
		if(StringUtils.isNotBlank(birthEnd)){
			query.setParameter("birthEnd", birthEnd);
		}
//		//打印和查询将调用相同的方法，故这里进行判断
//		if(StringUtils.isNotBlank(page)&&StringUtils.isNotBlank(rows)){
//			query.setFirstResult((p-1)*r).setMaxResults(p*r);
//		}
		List<EmrRecordMain> list = query.list();
		if(list!=null&&list.size()>0){
			return list;
		}
		return new ArrayList<EmrRecordMain>();
	}
	
	
	



}
