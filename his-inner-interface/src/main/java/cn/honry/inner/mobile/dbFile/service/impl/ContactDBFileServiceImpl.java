package cn.honry.inner.mobile.dbFile.service.impl;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Service;

import cn.honry.base.bean.model.BusinessDictionary;
import cn.honry.base.bean.model.FictitiousContact;
import cn.honry.base.bean.model.FictitiousDept;
import cn.honry.base.bean.model.MContactDBVersion;
import cn.honry.base.bean.model.PublicAddressBook;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.inner.baseinfo.code.dao.CodeInInterDAO;
import cn.honry.inner.mobile.dbFile.dao.ContactDBFileDao;
import cn.honry.inner.mobile.dbFile.dao.PersonInfoXmlDao;
import cn.honry.inner.mobile.dbFile.dao.PublicBookOADao;
import cn.honry.inner.mobile.dbFile.service.ContactDBFileService;
import cn.honry.inner.system.upload.service.UploadFileService;
import cn.honry.utils.ReaderProperty;
@Service("contactDBFileService")
public class ContactDBFileServiceImpl implements ContactDBFileService{
	private Logger logger=Logger.getLogger(ContactDBFileServiceImpl.class);
	private static ReaderProperty readerProperty =  new ReaderProperty("jdbc.properties");
	@Autowired
	private PublicBookOADao publicBookOADao;
	@Autowired
	private ContactDBFileDao contactDBFileDao;
	@Autowired
	private PersonInfoXmlDao personInfoXmlDao;
	@Autowired
	private CodeInInterDAO innerCodeDao;
	@Autowired
	@Qualifier(value = "uploadFileService")
	private UploadFileService uploadFileService;
	//基础工具类,不支持参数名传参
	@Resource
	private JdbcTemplate jdbcTemplate;
	
	//处理虚拟科室人员数量
	private void dealVirtualDept_empCount(Connection conn) throws Exception {
		logger.info("处理科室人数");
		Class.forName("org.sqlite.JDBC");
		Statement stat = conn.createStatement();
		stat.executeUpdate("update M_FICTITIOUS_DEPT set COUNT_EMP=(select Count(*) from M_EMPLOYEE where VIRTUAL_DEPT_CODE=DEPT_CODE);");
		if(stat != null){
			stat.close();
		}
	}
	/**
	 * 删除联系人db文件的数据
	 * @param finalPath 本地db文件路径
	 * @return
	 * @throws Exception
	 */
	private Connection deleteDataFromDBFile(String finalPath) throws Exception {
		logger.info("清空本地数据库中表的历史数据......");
		Class.forName("org.sqlite.JDBC");
		//连接SQLite的JDBC
		String sqlconn="jdbc:sqlite://"+finalPath;
		//jdbc : sqlite://d:/20170317HisEclipseWorkSpace/mobile/build/classes/contacts.db
		//建立一个数据的连接
		Connection conn = DriverManager.getConnection(sqlconn);
		Statement stat = conn.createStatement();
		//删除
		stat.executeUpdate(" delete from M_DEPT_TYPE;");
		stat.executeUpdate(" delete from M_FICTITIOUS_DEPT;");
		stat.executeUpdate(" delete from M_EMPLOYEE;");
		stat.executeUpdate(" delete from T_OA_PUBLIC_BOOK;");
		return conn;
	}
	/**
	 * 更新科室类型数据至本地db文件
	 * @param deptTypes 数据字典中的科室类型
	 * @param finalPath 本地db文件路径
	 * @param conn  db连接
	 * @throws Exception
	 */
	private void updateDeptTypeToDBFile(List<BusinessDictionary> deptTypes, String finalPath, Connection conn) throws Exception{
		logger.info("向db文件中部门类型表中插入数据......");
		Class.forName("org.sqlite.JDBC");
		//连接SQLite的JDBC
		//jdbc:sqlite://d:/20170317HisEclipseWorkSpace/mobile/build/classes/contacts.db
		//建立一个数据的连接
		Statement stat = conn.createStatement();
		//重新维护新的数据
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH : mm : ss");
		String createTime = sf.format(new Date());
		for(int i = 0; i < deptTypes.size(); i++){
			BusinessDictionary deptType = deptTypes.get(i);
			String sql = "insert into M_DEPT_TYPE ("
			+ "ID,"  // 1
			+ "CODE_ENCODE,"//2
			+ "CODE_NAME, "// 3
			+ "CODE_ORDER, "//4
			+ "CREATETIME "//5
			+ ")"
			+ "values ("
			+ "'" + (deptType.getId() == null ? "" : deptType.getId()) + "', " //1
			+ "'" + (deptType.getEncode() == null ? "" : deptType.getEncode()) + "', "//2
			+ "'" + (deptType.getName() == null ? "" : deptType.getName()) + "', "//3
			+ "" + (deptType.getOrder() == null ? 0 : deptType.getOrder()) + ","//4
			+ "'" + createTime+"'"//5
			+ ")";
			stat.addBatch(sql);
			if((i != 0 && i % 1000 == 0) || i == deptTypes.size() -1){
				stat.executeBatch();
				stat.clearBatch();
			}
		}
		if(stat != null){
			stat.close();
		}
		logger.info("向db文件中部门类型表中插入数据条数："+deptTypes.size());
	}
	/**
	 * db文件只能维护虚拟科室数据
	 * @param virtualDepts 虚拟科室
	 * @param finalPath  本地db文件路径
	 * @param conn  db连接
	 * @throws Exception
	 */
	private void insertVirtualDepts(List<FictitiousDept> virtualDepts, String finalPath, Connection conn) throws Exception {
		logger.info("向db文件中虚拟科室表中插入数据......");
		Class.forName("org.sqlite.JDBC");
		//连接SQLite的JDBC
		//String sqlconn="jdbc:sqlite://"+finalPath;
		//jdbc:sqlite://d:/20170317HisEclipseWorkSpace/mobile/build/classes/contacts.db
		Statement stat = conn.createStatement();
		//重新维护新的数据
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH : mm : ss");
		String createTime = sf.format(new Date());
		for(int i = 0; i < virtualDepts.size(); i++){
			FictitiousDept fictitious_dept = virtualDepts.get(i);
			String sql = "insert into M_FICTITIOUS_DEPT ("
					+ "HOSPITAL_ID,"  // 1
					+ "DEPT_ID,"//2
					+ "DEPT_CODE, "// 3
					+ "DEPT_NAME, "//4
					+ "DEPT_BREV, "//5
					+ "DEPT_DISTRICT, "//6
					+ "DEPT_PINYIN, "//7
					+ "DEPT_WB, "//8
					+ "DEPT_INPUTCODE, "//9
					+ "DEPT_ORDER, "//10
					+ "DEPT_TYPE, "//11
					+ "CREATETIME, "//12
					+ "DEPT_DISTRICT_NAME"//13
					+ ")"
					+ "values ("
					+ "" + (fictitious_dept.getHospitalId() == null ? 0 : fictitious_dept.getHospitalId()) + ", " //1
					+ "'" + (fictitious_dept.getDeptId() == null ? "" : fictitious_dept.getDeptId()) + "', "//2
					+ "'" + (fictitious_dept.getDeptCode() == null ? "" : fictitious_dept.getDeptCode()) + "', "//3
					+ "'" + (fictitious_dept.getDeptName() == null ? "" : fictitious_dept.getDeptName()) + "', "//4
					+ "'" + (fictitious_dept.getDeptBrev() == null ? "" : fictitious_dept.getDeptBrev()) + "', "//5
					+ "" + (fictitious_dept.getDeptDistrict() == null ? 0 : fictitious_dept.getDeptDistrict()) + ", "//6
					+ "'" + (fictitious_dept.getDeptPinYin() == null ? "" : fictitious_dept.getDeptPinYin()) + "', "//7
					+ "'" + (fictitious_dept.getDeptWb() == null ? "" : fictitious_dept.getDeptWb()) + "', "//8
					+ "'" + (fictitious_dept.getDeptInputCode() == null ? "" : fictitious_dept.getDeptInputCode()) + "', "//9
					+ "" + (fictitious_dept.getDeptOrder() == null ? 0 : fictitious_dept.getDeptOrder()) + ", "//10
					+ "'" + (fictitious_dept.getDeptType() == null ? "" : fictitious_dept.getDeptType()) + "', "//11
					+ "'" + createTime+"',"//12
					+ "'" + (fictitious_dept.getDeptDistrictName() == null ? "" : fictitious_dept.getDeptDistrictName()) + "'"//13
					+ ");";
			stat.addBatch(sql);
			if((i != 0 && i % 1000 == 0) || i == virtualDepts.size() -1){
				stat.executeBatch();
				stat.clearBatch();
			}
		}
		if(stat != null){
			stat.close();
		}
		logger.info("向db文件中虚拟科室表中插入数据条数："+virtualDepts.size());
	}
	/**
	 * db文件中维护员工数据（虚拟科室对应实际科室下的员工数据）
	 * @param employees
	 * @param conn
	 * @throws Exception
	 */
	private void insertEmployeeInfo(List<SysEmployee> employees, Connection conn) throws Exception {
		logger.info("向db文件中员工表中插入数据......");
		Class.forName("org.sqlite.JDBC");
		//连接SQLite的JDBC
		//String sqlconn="jdbc:sqlite://"+finalPath;
		//jdbc:sqlite://d:/20170317HisEclipseWorkSpace/mobile/build/classes/contacts.db
		Statement stat = conn.createStatement();
		//重新维护新的数据
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH : mm : ss");
		String createTime = sf.format(new Date());
		for(int i = 0; i < employees.size(); i++){
			SysEmployee emp = employees.get(i);
			String empPost = emp.getPost();
			if(null != empPost){//获取员工职务 数据库中有时存的汉字 有的是编码 为返回正确数据故做此判断
				boolean isNum = empPost.matches("[0-9]+");
				if(isNum){
				  BusinessDictionary tmpDic= innerCodeDao.getDictionaryByCode("duties",empPost);;//获取职务名称
				  if(null != tmpDic){
					  emp.setPost(tmpDic.getName());
				  }
				}
			}
			String title = emp.getTitle();
			if(null != title){//获取员工职称  数据库中有时存的汉字 有的是编码 为返回正确数据故做此判断
				boolean isNum = title.matches("[0-9]+");
				if(isNum){
				  BusinessDictionary tmpDic= innerCodeDao.getDictionaryByCode("title",title);;//获取职称名称
				  if(null != tmpDic){
					  emp.setTitle(tmpDic.getName());;
				  }
				}
			}
			String sql = "insert into M_EMPLOYEE ("
					+ "HOSPITAL_ID,"  // 1
					+ "USER_ID,"//2
					+ "EMPLOYEE_ID, "// 3
					+ "DEPT_ID, "//4
					+ "EMPLOYEE_CODE, "//5
					+ "EMPLOYEE_JOBNO, "//6
					+ "EMPLOYEE_NAME, "//7
					+ "EMPLOYEE_SEX, "//17
					+ "EMPLOYEE_OFFICE, "//23
					+ "EMPLOYEE_FAX, "//24
					+ "EMPLOYEE_OFFICEPHONE, "//25
					+ "EMPLOYEE_MOBILE, "//26
					+ "EMPLOYEE_EMAIL, "//27
					+ "EMPLOYEE_PHOTO, "//30
					+ "EMPLOYEE_ORDER, "//33
					+ "CREATETIME, "//34
					+ "M_OFFICE_PHONE_VISIBLE, "//35
					+ "M_MOBILE_VISIBLE, "//36
					+ "M_EMAIL_VISIBLE, "//37
					+ "EMAIL_PWD, "//38
					+ "VIRTUAL_DEPT_CODE,"//39
					+ "EMPLOYEE_POST,"//40 职务
					+ "EMPLOYEE_TITLE"//41 职称
					+ ")"
					+ "values ("
					+ "" + (emp.getHospitalId() == null || emp.getHospitalId().getId() == null ? "0" : emp.getHospitalId().getId()) + ", " //1
					+ "'" + (emp.getJobNo() == null ? "" : emp.getJobNo()) + "', "//2  20170720 为方便通信端调用user_id employee_id employee_code 都存放employee_jobno=user_account
					+ "'" + (emp.getJobNo() == null ? "" : emp.getJobNo()) + "', "//3
					+ "'" + (emp.getDeptCode() == null ? "" : emp.getDeptCode()) + "', "//4
					+ "'" + (emp.getJobNo() == null ? "" : emp.getJobNo()) + "', "//5
					+ "'" + (emp.getJobNo() == null ? "" : emp.getJobNo()) + "', "//6
					+ "'" + (emp.getName() == null ? "" : emp.getName()) + "', "//7
					+ "'" + (emp.getSex() == null ? "" : emp.getSex()) + "', "//17
					+ "'" + (emp.getOffice() == null ? "" : emp.getOffice()) + "', "//23
					+ "'" + (emp.getFax() == null ? "" : emp.getFax()) + "', "//24
					+ "'" + (emp.getOfficePhone() == null ? "" : emp.getOfficePhone()) + "', "//25
					+ "'" + (emp.getMobile() == null ? "" : emp.getMobile()) + "', "//26
					+ "'" + (emp.getEmail() == null ? "" : emp.getEmail()) + "', "//27
					+ "'" + (emp.getPhoto() == null ? "" : emp.getPhoto()) + "', "//30
					+ "" + (emp.getOrder() == null ? 0 : emp.getOrder()) + ", "//33
					+ "'" + createTime+"', "//34
					+ "" + (emp.getMofficePhoneVisible() == null ? 0 : emp.getMofficePhoneVisible()) + ", "//35
					+ "" + (emp.getMmobileVisible() == null ? 0 : emp.getMmobileVisible()) + ", "//36
					+ "" + (emp.getMemailVisible() == null ? 0 : emp.getMemailVisible()) + ", "//37
					+ "'" + (emp.getEmailPwd() == null ? "" : emp.getEmailPwd()) + "', "//38
					+ "'" + (emp.getVirtualDeptCode() == null ? "" : emp.getVirtualDeptCode()) + "',"//39
					+ "'" + (emp.getPost() == null ? "" : emp.getPost()) + "',"//40 职务
					+ "'" + (emp.getTitle() == null ? "" : emp.getTitle()) + "'"//41 职称
					+ ");";
			stat.addBatch(sql);
			if((i != 0 && i % 1000 == 0) || i == employees.size() -1){
				stat.executeBatch();
				stat.clearBatch();
			}
		}
		if(stat != null){
			stat.close();
		}
		logger.info("插入员工数据："+employees.size()+" 条");
	}
	/**
	 * 将本地db文件路径上传至hias文件服务器 并在m_contactdb_version中维护记录（通过该表的触发器法发送db文件并更的广播app后台下载）
	 */
	public void uploadDBFileToServer(String finalPath) throws Exception {
		    logger.info("将服务下的db文件上传至文件服务器......");
//		    String fileServerIP = PingConnectUtil.getfileServerIP(null);
//			logger.info("ping文件服务器IP检查网络,ip:"+fileServerIP);
//			String connFlg = PingConnectUtil.ping(fileServerIP,5000);
//			logger.info("ping状态："+connFlg);
//			if(connFlg.equals("true")){//网络连接正常时进行文件上传
				File file=new File(finalPath);
				final String fileAddress = uploadFileService.fileUpload(file,file.getName(),"2","dbFile");
				logger.info("uploadFileService.fileUpload(...) fileAddress:"+fileAddress);
				if(StringUtils.isNotBlank(fileAddress)){
					System.out.println("文件上传完毕.....地址："+fileAddress); 
					logger.info("db文件已上传至文件服务器,上传地址:"+fileAddress);
					logger.info("将db文件版本保存至数据库......");
					//将最新上传的db文件地址保存至m_contactdb_version
					MContactDBVersion mcontactdbversion = new MContactDBVersion();
					mcontactdbversion.setId(0);
					mcontactdbversion.setDbAdress(fileAddress);
					mcontactdbversion.setCreateTime(new Date());
					contactDBFileDao.save(mcontactdbversion);
					
//					int count = jdbcTemplate.update("insert into M_CONTACTDB_VERSION values(?,?,?)",new PreparedStatementSetter() {    
//						public void setValues(PreparedStatement ps) throws SQLException { 
//							ps.setInt(1, 0);
//							ps.setString(2, fileAddress);
//							ps.setTimestamp(3,new Timestamp(new java.util.Date().getTime()));
//						}
//					});
					
					logger.info("db文件版本保存已保存至数据库！");
				}else{
					logger.info("db文件上传失败,上传地址:"+fileAddress);
				}
//			}else{
//				logger.info("无法进行文件上传,请检查网络状态  IP:"+fileServerIP);
//			}
	}
	@Override
	public void updateDataToDBFile(String finalPath) throws Exception {
			Connection conn = null;
			conn = this.deleteDataFromDBFile(finalPath);
			String pictureRootAddress = readerProperty.getValue_String("outfileUploadUrl");
			List<BusinessDictionary> deptTypes = null;//科室类型集合
			List<FictitiousDept> virtualDepts = null;//虚拟科室集合
			List<FictitiousContact> actualDepts = null;//实际科室集合
			List<SysEmployee> mEmployees = null;//员工信息集合
			
			deptTypes = innerCodeDao.getDictionary("depttype");
			this.updateDeptTypeToDBFile(deptTypes,finalPath,conn);
			if(deptTypes!=null && deptTypes.size()>0){
				for (BusinessDictionary deptType : deptTypes) {
					virtualDepts = personInfoXmlDao.findVirtualDept(deptType.getEncode());
					//更新
					 this.insertVirtualDepts(virtualDepts,finalPath,conn);
					if(virtualDepts!=null&&virtualDepts.size()>0){
						for (FictitiousDept virtualDept : virtualDepts){
							actualDepts = personInfoXmlDao.findActualDept(virtualDept);
							if(actualDepts!=null && actualDepts.size()>0){
								List<SysEmployee> employees = new ArrayList<SysEmployee>();
								for (FictitiousContact actualDept : actualDepts) {
									mEmployees = personInfoXmlDao.findPersonInfo(actualDept.getDeptCode());
									for (SysEmployee mEmployee : mEmployees) {
										if(StringUtils.isNotBlank(mEmployee.getPhoto())){
											mEmployee.setPhoto(pictureRootAddress+mEmployee.getPhoto());
										}
										if(StringUtils.isNotBlank(actualDept.getDeptCode())){
											mEmployee.setDeptCode(actualDept.getDeptCode());
										}
										mEmployee.setVirtualDeptCode(virtualDept.getDeptCode());
										
										employees.add(mEmployee);
									}
								}
								
								
								/*员工集合添加元素，
								 * 键为虚拟科室的科室dept_code,
								 * 该集合值为虚拟科室的dept_code所对应的员工信息,
								 * 虚拟科室的dept_code对应实际科室的fict_code,
								 * 实际科室的dept_code对应员工的dept_id,
								 */
								insertEmployeeInfo(employees,conn);
							}
						}
					}
				}
			}
			
			this.dealVirtualDept_empCount(conn);
			this.dealOAPublicBook(conn);
			if(conn != null){
				conn.close();
			}
			//更新完classpath下的db文件后上传至服务器
			this.uploadDBFileToServer(finalPath);
			logger.info("联系人db文件定时任务结束！！！！");
			System.out.println("联系人db文件定时任务结束！！！！");
		
	}
	/**
	 *查询最新版本信息 用于app端下载最新db文件
	 */
	@Override
	public MContactDBVersion selectNewestVersion() throws Exception {
		return contactDBFileDao.selectNewestVersion();
	}
	public void updateNullDBAdress(String finalPath, MContactDBVersion mcontactdbversion)
			throws Exception {
		//将文件上传至文件服务器
		File file=new File(finalPath);
		String fileAddress = uploadFileService.fileUpload(file,file.getName(),"2","dbFile");
		//将最新上传的db文件地址最新版本号m_contactdb_version
		mcontactdbversion.setDbAdress(fileAddress);
		System.out.println("updateNullDBAdress文件上传完毕.....地址："+fileAddress);//  
		contactDBFileDao.updateNullDBAdress(mcontactdbversion);
	}
	@Override
	public MContactDBVersion get(String arg0) {
		return contactDBFileDao.get(arg0);
	}
	@Override
	public void removeUnused(String arg0) {
		
	}
	@Override
	public void saveOrUpdate(MContactDBVersion arg0) {
		if(arg0.getId() != null){
			contactDBFileDao.update(arg0);
		}else {
			contactDBFileDao.save(arg0);
		}
	}
	
	private void dealOAPublicBook(Connection conn) throws Exception {
		 logger.info("向本地db文件公共通讯录表中插入数据....");
		 Class.forName("org.sqlite.JDBC");
		 //连接SQLite的JDBC
		 //建立一个数据的连接
		 //Connection conn = DriverManager.getConnection(sqlconn);
		Statement stat = conn.createStatement();
		//获得公共通讯录List
		List<PublicAddressBook> allPublicBookOA = publicBookOADao.getAllPublicBookOA();
		//遍历插入至db文件中的T_OA_PUBLIC_BOOK
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String createTime = sf.format(new Date());
		for(int i = 0 ; i < allPublicBookOA.size() ; i++){
			PublicAddressBook publicBookOA = allPublicBookOA.get(i);
			String sql = "INSERT INTO T_OA_PUBLIC_BOOK ("//公共通讯录
				+ "ID, "  // 1
				+ "NODE_NAME, "//2
				+ "PARENT_CODE, "// 3
				+ "NODE_LEVEL, "//4
				+ "PATH, "//5
				+ "SUPERPATH, "//6
				+ "ORDER_ID, "//7
				+ "NODE_TYPE, "//8
				+ "PHONE, "//9
				+ "MIN_PHONE, "//10
				+ "AREA_NAME, "//11
				+ "BUILDING_NAME, "//12
				+ "FLOOR_NAME, "//13
				+ "FLOOR_TYPE, "//14
				+ "FLOOR_DEPT, "//15
				+ "STATUS, "//16
				+ "CREATETIME, "//17
				+ "OFFICE_PHONE "//18
				+ ") "
				+ "VALUES ("
				+ "'"+(publicBookOA.getId()==null?"":publicBookOA.getId())+"', " //1
				+ "'"+(publicBookOA.getName()==null?"":publicBookOA.getName())+"', "//2  
				+ "'"+(publicBookOA.getParentCode()==null?"":publicBookOA.getParentCode())+"', "//3
				+ ""+(publicBookOA.getNodeLevel()==null?0:publicBookOA.getNodeLevel())+", "//4
				+ "'"+(publicBookOA.getPath()==null?"":publicBookOA.getPath())+"', "//5
				+ "'"+(publicBookOA.getSuperPath()==null?"":publicBookOA.getSuperPath())+"', "//6
				+ ""+(publicBookOA.getOrder()==null?0:publicBookOA.getOrder())+", "//7
				+ "'"+(publicBookOA.getNodeType()==null?"":publicBookOA.getNodeType())+"', "//8
				+ "'"+(publicBookOA.getPhone()==null?"":publicBookOA.getPhone())+"', "//9
				+ "'"+(publicBookOA.getMinPhone()==null?"":publicBookOA.getMinPhone())+"', "//10
				+ "'"+(publicBookOA.getAreaName()==null?"":publicBookOA.getAreaName())+"', "//11
				+ "'"+(publicBookOA.getBuildingName()==null?"":publicBookOA.getBuildingName())+"', "//12
				+ "'"+(publicBookOA.getFloorName()==null?"":publicBookOA.getFloorName())+"', "//13
				+ "'"+(publicBookOA.getFloorType()==null?"":publicBookOA.getFloorType())+"', "//14
				+ "'"+(publicBookOA.getFloorDept()==null?"":publicBookOA.getFloorDept())+"', "//15
				+ ""+(publicBookOA.getStatus()==null?0:publicBookOA.getStatus())+","//16
				+ "'"+createTime+"',"//17
				+ "'"+(publicBookOA.getOfficePhone()==null?"":publicBookOA.getOfficePhone())+"'"//18
				+ ");";
			stat.addBatch(sql);
			if((i != 0 && i % 1000 == 0) || i == allPublicBookOA.size() -1){
				stat.executeBatch();
				stat.clearBatch();
			}
		}
		if(stat != null){
			stat.close();
		}
		logger.info("向本地db文件公共通讯录表中插入数据条数："+allPublicBookOA.size());
	}
}
