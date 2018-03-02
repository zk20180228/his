package cn.honry.statistics.doctor.registerInfoGzltj.dao.impl;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.UserLogin;
import cn.honry.hiasMongo.basic.MongoBasicDao;
import cn.honry.inner.baseinfo.department.service.DeptInInterService;
import cn.honry.inner.statistics.registerInfoGzltj.vo.RegisterInfoVo;
import cn.honry.inner.system.userMenuDataJuris.dao.DataJurisInInterDAO;
import cn.honry.inner.vo.KeyValueVo;
import cn.honry.inner.vo.MenuListVO;
import cn.honry.inner.vo.MenuVO;
import cn.honry.statistics.doctor.registerInfoGzltj.dao.RegisterInfoStatDao;
import cn.honry.statistics.doctor.registerInfoGzltj.vo.DoctorVo;
import cn.honry.statistics.doctor.registerInfoGzltj.vo.RegisterInfoGzltjVo;
import cn.honry.utils.DateUtils;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.ShiroSessionUtils;

import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

@Repository("registerInfoStatDao")
@SuppressWarnings({ "all" })
public class RegisterInfoStatDaoImpl implements RegisterInfoStatDao{
	
	@Autowired
	@Qualifier(value = "deptInInterService")
	private DeptInInterService deptInInterService;
	public void setDeptInInterService(DeptInInterService deptInInterService) {
		this.deptInInterService = deptInInterService;
	}
	
	//基础工具类,不支持参数名传参
	@Resource
	private JdbcTemplate jdbcTemplate;
	
	//扩展工具类,支持参数名传参
	@Resource
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	@Autowired
	@Qualifier(value = "dataJurisInInterDAO")
	private DataJurisInInterDAO dataJurisInInterDAO;
	
	public static final String TABLENAME_GZLDAY = "T_TJ_GZLDAY";//医生工作量
	
	//统计
	@Override
	public int count() {
		
		String sql="select COUNT(ID) from T_INPATIENT_EXECUNDRUG";
		
		return jdbcTemplate.queryForObject(sql, java.lang.Integer.class);
	}
	
	//查询
	@Override
	public List<UserLogin> getAll() {
		String sql="select this_.LOGIN_ID as LOGIN1_318_0_, this_.LOGIN_HTTP as LOGIN2_318_0_, this_.LOGIN_IP as LOGIN3_318_0_, this_.LOGIN_TIME as LOGIN4_318_0_, this_.LOGIN_SESSION as LOGIN5_318_0_, this_.LOGIN_USERID as LOGIN6_318_0_ from T_SYS_USERLOGIN this_";
		List<UserLogin> result =  jdbcTemplate.query(sql,new RowMapper<UserLogin>() {
			@Override
			public UserLogin mapRow(ResultSet rs, int rowNum) throws SQLException {
				UserLogin userInfo = new UserLogin();
				userInfo.setId(rs.getString("LOGIN1_318_0_"));
				userInfo.setUserId(rs.getString("LOGIN6_318_0_"));
				userInfo.setIp(rs.getString("LOGIN3_318_0_"));
				userInfo.setHttp(rs.getString("LOGIN2_318_0_"));
				userInfo.setSessionId(rs.getString("LOGIN5_318_0_"));
				userInfo.setLoginTime(rs.getDate("LOGIN4_318_0_"));
				
				return userInfo;
			}});
		
		return result; 
	}
	
	//新增
	@Override
	public int save(final UserLogin login){
		int t = jdbcTemplate.update("insert into T_SYS_USERLOGIN values(?,?,?,?,?,?)",new PreparedStatementSetter() {    
			public void setValues(PreparedStatement ps) throws SQLException { 
				ps.setString(1, login.getId());
				ps.setString(2, login.getUserId());
				ps.setString(3, login.getIp());
				ps.setString(4, login.getHttp());
				ps.setString(5, login.getSessionId());
				ps.setDate(6, new Date(login.getLoginTime().getTime()));
			}
		});
		
		return t;
	}
	
	//修改
	@Override
	public int update(final UserLogin login){
		String sql = "update T_SYS_USERLOGIN set LOGIN_IP = ? ,LOGIN_TIME = ? where LOGIN_ID = ?";  
		Object args[] = new Object[]{login.getIp(),new Date(login.getLoginTime().getTime()),login.getId()};  
		int t = jdbcTemplate.update(sql,args);  
		
		return t;
	}
	
	//删除
	@Override  
	public int delete(String id){  
		String sql = "delete from T_SYS_USERLOGIN where LOGIN_ID = ?";
		int t = jdbcTemplate.update(sql,id);
		
		return t;
	}
	
	@Override
	public List<RegisterInfoGzltjVo> statRegDorWork(List<String> tnL,String stime, String etime, String dept, String expxrt,String page,String rows) {
		if(tnL==null||tnL.size()<0){
			return new ArrayList<RegisterInfoGzltjVo>();
		}
		int p = StringUtils.isNotBlank(page)?Integer.valueOf(page):1;
		int r = StringUtils.isNotBlank(rows)?Integer.valueOf(rows):50;
		final StringBuffer sb = new StringBuffer();
		String sql = this.getSQL(tnL, stime, etime, dept, expxrt);
		sb.append(" Select * From (Select tab.*,Rownum rn From ( ");
		sb.append(sql);
		sb.append("  ) tab Where Rownum <=").append(p*r).append(")Where rn>").append((p-1)*r);
		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("stime", stime);
		paraMap.put("etime", DateUtils.formatDateY_M_D((DateUtils.addDay(DateUtils.parseDateY_M_D(etime),1))));
		if(dept!=null){
			List<String> deptcode = Arrays.asList(dept.split(","));
			paraMap.put("dept", deptcode);
		}
		if(expxrt!=null){
			List<String> doctCode = Arrays.asList(expxrt.split(","));
			paraMap.put("expxrt", doctCode);
		}
		List<RegisterInfoGzltjVo> voList =  namedParameterJdbcTemplate.query(sb.toString(),paraMap,new RowMapper<RegisterInfoGzltjVo>() {
			@Override
			public RegisterInfoGzltjVo mapRow(ResultSet rs, int rowNum) throws SQLException {
				RegisterInfoGzltjVo vo = new RegisterInfoGzltjVo();
				vo.setDept(rs.getString("DEPT_CODE"));
				vo.setExpxrt(rs.getString("DOCT_CODE"));
				vo.setTitle(rs.getString("REGLEVL_NAME"));
				vo.setMonNum(rs.getInt("monNum"));
				vo.setMonCost(rs.getDouble("monCost"));
				vo.setTueNum(rs.getInt("tueNum"));
				vo.setTueCost(rs.getDouble("tueCost"));
				vo.setWedNum(rs.getInt("wedNum"));
				vo.setWedCost(rs.getDouble("wedCost"));
				vo.setThuNum(rs.getInt("thuNum"));
				vo.setThuCost(rs.getDouble("thuCost"));
				vo.setFriNum(rs.getInt("friNum"));
				vo.setFriCost(rs.getDouble("friCost"));
				vo.setSatNum(rs.getInt("satNum"));
				vo.setSatCost(rs.getDouble("satCost"));
				vo.setSunNum(rs.getInt("sunNum"));
				vo.setSunCost(rs.getDouble("sunCost"));
				vo.setNum(rs.getInt("num"));
				vo.setCost(rs.getDouble("cost"));
				return vo;
		}});
		
		return voList;
	}
	
	public String getSQL(List<String> tnL,String stime, String etime, String dept, String expxrt){
		StringBuffer sb = new StringBuffer();
		sb.append("select t.DEPT_CODE ,t.DOCT_CODE ,t.REGLEVL_NAME ,sum(t.r1) monNum ,sum(t.w1) monCost ,sum(t.r2) tueNum ,sum(t.w2) tueCost ,sum(t.r3) wedNum ");
		sb.append(",sum(t.w3) wedCost ,sum(t.r4) thuNum ,sum(t.w4) thuCost ,sum(t.r5) friNum ,sum(t.w5) friCost ,sum(t.r6) satNum ,sum(t.w6) satCost ");
		sb.append(",sum(t.r7) sunNum ,sum(t.w7) sunCost ,sum(t.r8) num ,sum(t.w8) cost from ( ");
		for(int i=0;i<tnL.size();i++){
			if(i>0){
				sb.append("UNION ALL ");
			}
			sb.append("SELECT n.DEPT_CODE ,n.DOCT_CODE ,n.REGLEVL_NAME");
			sb.append(",DECODE(TO_NUMBER(to_char(n.REG_DATE,'D')), 1, count(to_char(n.REG_DATE,'D'))) as r7 ");
			sb.append(",DECODE(TO_NUMBER(to_char(n.REG_DATE,'D')), 2, count(to_char(n.REG_DATE,'D'))) as r1 ");
			sb.append(",DECODE(TO_NUMBER(to_char(n.REG_DATE,'D')), 3, count(to_char(n.REG_DATE,'D'))) as r2 ");
			sb.append(",DECODE(TO_NUMBER(to_char(n.REG_DATE,'D')), 4, count(to_char(n.REG_DATE,'D'))) as r3 ");
			sb.append(",DECODE(TO_NUMBER(to_char(n.REG_DATE,'D')), 5, count(to_char(n.REG_DATE,'D'))) as r4 ");
			sb.append(",DECODE(TO_NUMBER(to_char(n.REG_DATE,'D')), 6, count(to_char(n.REG_DATE,'D'))) as r5 ");
			sb.append(",DECODE(TO_NUMBER(to_char(n.REG_DATE,'D')), 7, count(to_char(n.REG_DATE,'D'))) as r6 ");
			sb.append(",DECODE(TO_NUMBER(to_char(n.REG_DATE,'D')), 1, SUM(n.REG_FEE+n.CHCK_FEE+n.DIAG_FEE+n.OTH_FEE+n.BOOK_FEE)) as w7 ");
			sb.append(",DECODE(TO_NUMBER(to_char(n.REG_DATE,'D')), 2, SUM(n.REG_FEE+n.CHCK_FEE+n.DIAG_FEE+n.OTH_FEE+n.BOOK_FEE)) as w1 ");
			sb.append(",DECODE(TO_NUMBER(to_char(n.REG_DATE,'D')), 3, SUM(n.REG_FEE+n.CHCK_FEE+n.DIAG_FEE+n.OTH_FEE+n.BOOK_FEE)) as w2 ");
			sb.append(",DECODE(TO_NUMBER(to_char(n.REG_DATE,'D')), 4, SUM(n.REG_FEE+n.CHCK_FEE+n.DIAG_FEE+n.OTH_FEE+n.BOOK_FEE)) as w3 ");
			sb.append(",DECODE(TO_NUMBER(to_char(n.REG_DATE,'D')), 5, SUM(n.REG_FEE+n.CHCK_FEE+n.DIAG_FEE+n.OTH_FEE+n.BOOK_FEE)) as w4 ");
			sb.append(",DECODE(TO_NUMBER(to_char(n.REG_DATE,'D')), 6, SUM(n.REG_FEE+n.CHCK_FEE+n.DIAG_FEE+n.OTH_FEE+n.BOOK_FEE)) as w5 ");
			sb.append(",DECODE(TO_NUMBER(to_char(n.REG_DATE,'D')), 7, SUM(n.REG_FEE+n.CHCK_FEE+n.DIAG_FEE+n.OTH_FEE+n.BOOK_FEE)) as w6 ");
			sb.append(",count(1) AS r8 ,SUM(n.REG_FEE+n.CHCK_FEE+n.DIAG_FEE+n.OTH_FEE+n.BOOK_FEE) as w8 ");
			sb.append("FROM	").append(tnL.get(i)).append(" n ");
			sb.append("WHERE n.DOCT_CODE is not null AND n.DEL_FLG = 0 ");
			if(StringUtils.isNotBlank(stime)){
				sb.append("AND n.REG_DATE >= to_date(:stime,'yyyy-MM-dd')");
			}
			if(StringUtils.isNotBlank(etime)){
				sb.append("AND n.REG_DATE < to_date(:etime,'yyyy-MM-dd')");
			}
			if(StringUtils.isNotBlank(dept)){
				sb.append("AND n.DEPT_CODE in (:dept) ");
			}
			if(StringUtils.isNotBlank(expxrt)){
				sb.append("AND n.DOCT_CODE in (:expxrt) ");
			}
			sb.append("GROUP BY DEPT_CODE,DOCT_CODE,TO_NUMBER(to_char(n.REG_DATE,'D')),n.REGLEVL_NAME ");
		}
		sb.append(")t group by DEPT_CODE,DOCT_CODE ,REGLEVL_NAME order by DEPT_CODE");
		
		return sb.toString();
	}
	
	@Override
	public void textZone() {
		String zone = "SYS_P621,SYS_P362,SYS_P341,SYS_P245,SYS_P180,SYS_P179,SYS_P178,SYS_P177,SYS_P176,SYS_P175,SYS_P174,SYS_P173,SYS_P172,SYS_P171,SYS_P170,SYS_P169,SYS_P168,SYS_P167,SYS_P166,SYS_P165,SYS_P164,SYS_P163,SYS_P162,SYS_P161,SYS_P160,SYS_P159,SYS_P158,SYS_P157,SYS_P156,SYS_P155,SYS_P154,SYS_P153,SYS_P152,SYS_P151,SYS_P150,SYS_P149,SYS_P148,SYS_P147,P1";
		String[] zoneArr = zone.split(",");
		//long s1 = System.currentTimeMillis();
		String sql = "SELECT d.DRUG_CODE D,COUNT(1) C,sum(d.STORE_COST) B FROM T_DRUG_OUTSTORE d where d.stop_flg = ? GROUP BY d.DRUG_CODE,d.STORE_COST";
		
		List<KeyValueVo> voList =  jdbcTemplate.query(sql,new RowMapper<KeyValueVo>() {
			@Override
			public KeyValueVo mapRow(ResultSet rs, int rowNum) throws SQLException {
				KeyValueVo vo = new KeyValueVo();
				vo.setKey(rs.getString("D"));
				vo.setVal(rs.getString("C"));
				return vo;
		}},0);
		//long s2 = System.currentTimeMillis();
		//System.err.println("共计："+voList.size()+" 条数据，查询全表用时："+(s2-s1));
		
		int num = 38;
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT D ,COUNT(D) C,sum(B) FROM( ");
		for(int i=0;i<num;i++){
			if(i!=0){
				sb.append("UNION ALL ");
			}
			sb.append("SELECT d.DRUG_CODE D,d.STORE_COST B FROM T_DRUG_OUTSTORE PARTITION(").append(zoneArr[i]).append(") d where d.stop_flg = :stop_flg ");
		}
		sb.append(")GROUP BY D ,B");
		Map<String,Object> pMap = new HashMap<String, Object>();
		pMap.put("stop_flg", 0);
		//long s3 = System.currentTimeMillis();
		List<KeyValueVo> voList2 =  namedParameterJdbcTemplate.query(sb.toString(),pMap,new RowMapper<KeyValueVo>() {
			@Override
			public KeyValueVo mapRow(ResultSet rs, int rowNum) throws SQLException {
				KeyValueVo vo = new KeyValueVo();
				vo.setKey(rs.getString("D"));
				vo.setVal(rs.getString("C"));
				return vo;
		}});
		//long s4 = System.currentTimeMillis();
//下面的代码可能会用到，暂时注释了，2017-07-03，zhang kui
//		System.err.println("共计："+voList2.size()+" 条数据，查询  "+num+" 个分区用时："+(s4-s3));
//		if((s2-s1)<(s4-s3)){
//			System.err.println("---------------------------------------------");
//			System.err.println("| 当联查分区数量为："+num+" 时，效率较全查低! |");
//			System.err.println("|     多余用时："+((s4-s3)-(s2-s1))+"!       |");
//			System.err.println("---------------------------------------------");
//		}
	}
	
	@Override
	public int getTotal(List<String> tnL, String stime, String etime,
	String dept, String expxrt) {
		String sql = this.getSQL(tnL, stime, etime, dept, expxrt);
		StringBuffer sb = new StringBuffer();
		sb.append(" select count(1) from ( ").append(sql).append(")");
		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("DEL_FLG", 0);
		paraMap.put("stime", stime);
		paraMap.put("etime", DateUtils.formatDateY_M_D((DateUtils.addDay(DateUtils.parseDateY_M_D(etime),1))));
		if(dept!=null){
			List<String> deptcode = Arrays.asList(dept.split(","));
			paraMap.put("dept", deptcode);
		}
		if(expxrt!=null){
			List<String> doctCode = Arrays.asList(expxrt.split(","));
			paraMap.put("expxrt", doctCode);
		}
		
		return namedParameterJdbcTemplate.queryForObject(sb.toString(), paraMap, java.lang.Integer.class);
	}
	
	@Override
	public List<DoctorVo> getDoctorBydeptCodes(String deptCodes) {
		String hql = "select EMPLOYEE_JOBNO as jobNo,EMPLOYEE_NAME as name,EMPLOYEE_PINYIN as pinyin,EMPLOYEE_WB as wb,EMPLOYEE_INPUTCODE as inputCode from T_EMPLOYEE where DEPT_ID in (:deptCodes) and stop_flg=0 and del_flg=0 ";
		Map<String,Object> paraMap = new HashMap<String,Object>();
		paraMap.put("deptCodes",Arrays.asList(deptCodes.split("','")));
		List<DoctorVo> list = namedParameterJdbcTemplate.query(hql, paraMap, new RowMapper<DoctorVo>(){
			@Override
			public DoctorVo mapRow(ResultSet rs, int rowNum)throws SQLException {
				DoctorVo vo = new DoctorVo();
				vo.setJobNo(rs.getString("jobNo"));
				vo.setName(rs.getString("name"));
				vo.setInputCode(rs.getString("inputCode"));
				vo.setPinyin(rs.getString("pinyin"));
				vo.setWb(rs.getString("wb"));
				return vo;
			}
		});
		if(list!=null&&list.size()>0){
			return list;
		}
		
		return new ArrayList<DoctorVo>();
	}
	
	@Override
	public List<MenuListVO> getDoctorList(String deptTypes, String menuAlias) {
		List<String> deptTypeList = null;
		if(StringUtils.isNotBlank(deptTypes)){
			deptTypeList=Arrays.asList(deptTypes.split(","));
		}
		List<SysDepartment> deptList = deptInInterService.getDeptByMenutypeAndUserCode(menuAlias,ShiroSessionUtils.getCurrentEmployeeFromShiroSession().getJobNo());
		List<String> deptCodeList = new ArrayList<String>();
		StringBuffer buffer = new StringBuffer("SELECT ");
		buffer.append(" DE.DEPT_CODE as deptId,em.EMPLOYEE_jobNo as jobNo,EM.EMPLOYEE_NAME as name,de.DEPT_TYPE as type,");
		buffer.append(" em.EMPLOYEE_PINYIN as pinyin,em.EMPLOYEE_WB as wb,em.EMPLOYEE_INPUTCODE as inputCode ");
		buffer.append(" from T_EMPLOYEE em,T_DEPARTMENT de ");
		buffer.append(" where EM.DEPT_ID =DE.DEPT_CODE and DE.DEL_FLG=0 and DE.STOP_FLG=0 and em.STOP_FLG=0 and EM.DEL_FLG=0 ");
		buffer.append(" AND EM.employee_type = '1' ");
		buffer.append(" AND DE.dept_type in(:deptType) ");
		if(deptList == null || deptList.size() == 0){
			//buffer.append("and em.EMPLOYEE_JOBNO = '"+ShiroSessionUtils.getCurrentEmployeeFromShiroSession().getJobNo()+"'");
			return new ArrayList<MenuListVO>();
		}else{
			if(deptList.size()<999){
				for (SysDepartment dept : deptList) {
					String code = dept.getDeptCode();
					if(StringUtils.isNotBlank(code)){
						deptCodeList.add(code);
					}
				}
				buffer.append(" AND DE.dept_code IN(:deptCode)");
			}else{
				buffer.append(" AND DE.dept_code IN(").append(dataJurisInInterDAO.getJurisDeptSql(menuAlias, ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount())).append(") ");
			}
			buffer.append(" UNION ");
			buffer.append(" select l.DEPT_ID as deptId,l.USER_ID AS jobNo,E.EMPLOYEE_NAME AS name,d.DEPT_TYPE as type, ");
			buffer.append(" e.EMPLOYEE_PINYIN as pinyin,e.EMPLOYEE_WB as wb,e.EMPLOYEE_INPUTCODE as inputCode ");
			buffer.append(" FROM T_SYS_USER_LOGINDEPT l ");
			buffer.append(" INNER  JOIN T_DEPARTMENT D ON D .DEPT_CODE = l.dept_id ");
			buffer.append(" AND D.dept_type in(:deptType) ");
			if(deptList.size()<999){
				buffer.append(" AND l.dept_id IN (:deptCode)");
			}else{
				buffer.append(" AND l.dept_id IN (").append(dataJurisInInterDAO.getJurisDeptSql(menuAlias, ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount())).append(")");
			}
			buffer.append(" INNER JOIN T_EMPLOYEE E ON E .EMPLOYEE_ID = l.USER_ID and E.EMPLOYEE_type= '1' ");
		}
		
		List<MenuListVO> depts=new ArrayList<MenuListVO>();
		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("deptCode", deptCodeList);
		if(deptTypeList!=null){
			paraMap.put("deptType", deptTypeList);
		}else{
			paraMap.put("deptType", deptTypes);
		}
		
		List<MenuVO> voList = namedParameterJdbcTemplate.query(buffer.toString(),paraMap, new RowMapper<MenuVO>(){
			@Override
			public MenuVO mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				MenuVO vo = new MenuVO();
				vo.setRelativeId(rs.getString("deptId"));
				vo.setType(rs.getString("type"));
				vo.setId(rs.getString("jobNo"));
				vo.setName(rs.getString("name"));
				vo.setInputCode(rs.getString("inputCode"));
				vo.setPinyin(rs.getString("pinyin"));
				vo.setWb(rs.getString("wb"));
				return vo;
			}
		});
		List<MenuListVO> doctors=new ArrayList<MenuListVO>();
		String[] arr=new String[]{"C-门诊","I-住院","F-财务","L-后勤","PI-药库","T-医技(终端)","0-其它","D-机关(部门)","P-药房","N-护士站","S-科研","O-其他","OP-手术","U-自定义"};
		Map<String, MenuVO> userMap = new HashMap<String, MenuVO>();
		for(MenuVO vo : voList){
			if(userMap.get(vo.getId())!=null){
				MenuVO vo1 = new MenuVO();
				vo1 = userMap.get(vo.getId());
				vo1.setRelativeId(vo.getUserDeptId()+"-"+vo1.getRelativeId());
				userMap.remove(userMap.get(vo.getId()));
				userMap.put(vo1.getId(), vo1);
			}else{
				userMap.put(vo.getId(), vo);
				continue;
			}
		}
		List<MenuVO> voList1 = new ArrayList<MenuVO>();
		for(MenuVO mvo : userMap.values()){
			voList1.add(mvo);
		}
		for(int i=0;i<arr.length;i++){
			String[] arr1=arr[i].split("-");
			MenuListVO d=new MenuListVO();
			d.setParentMenu(arr1[1]);
			List<MenuVO> rs=new ArrayList<MenuVO>();
			for(MenuVO v:voList1){
				if(arr1[0].equals(v.getType())){
					rs.add(v);
				}				
			}
			d.setMenus(rs);
			doctors.add(d);
		}
		if(doctors!=null&&doctors.size()>0){
			return doctors;
		}
		
		return new ArrayList<MenuListVO>();
	}
	
	/**
	 * mongodb查询工作量
	 */
	@Override
	public Map<String, Object> statRegDorWorkByMongo(String stime, String etime, String dept, String expxrt,
	String page, String rows,String menuAlias)throws Exception {
		List<RegisterInfoGzltjVo> voList = new ArrayList<RegisterInfoGzltjVo>();
		Map<String,Object> map = new HashMap<String,Object>();
		
			BasicDBObject bdbObject = new BasicDBObject();
			BasicDBObject bdObjectTimeS = new BasicDBObject();
			BasicDBObject bdObjectTimeE = new BasicDBObject();
			BasicDBList condList = new BasicDBList(); 
			BasicDBList mongoDeptList = new BasicDBList(); 
			BasicDBList mongoDocList = new BasicDBList(); 
			DBObject _group = new BasicDBObject("dateToday", "$dateToday");  
			_group.put("deptCode", "$deptCode");  
			_group.put("doctCode", "$doctCode");  
			_group.put("doctName", "$doctName");  
			_group.put("reglevlName", "$reglevlName");  
			if(StringUtils.isBlank(dept)&&StringUtils.isBlank(expxrt)){
				List<SysDepartment> deptList = deptInInterService.getDeptByMenutypeAndUserCode(menuAlias,ShiroSessionUtils.getCurrentEmployeeFromShiroSession().getJobNo());
				if(deptList == null && deptList.size() == 0){
					expxrt = ShiroSessionUtils.getCurrentEmployeeFromShiroSession().getJobNo();
				}else{
					for(int i = 0;i<deptList.size();i++){
						mongoDeptList.add(new BasicDBObject("deptCode",deptList.get(i).getDeptCode()));
					}
					bdbObject.put("$or", mongoDeptList);
				}
			}
			DBObject groupFields = new BasicDBObject("_id", _group);                    
			//总数  
			//groupFields.put("count", new BasicDBObject("$sum", 1));  
			 //求和        
			groupFields.put("regNum", new BasicDBObject("$sum","$regNum"));  
			groupFields.put("totalFee", new BasicDBObject("$sum","$totalFee")); 
			if(stime!=null){//开始时间,"$gt"大于
				bdObjectTimeS.put("regDate",new BasicDBObject("$gte",stime));
				condList.add(bdObjectTimeS);
			}
			if(etime!=null){//截止时间,"$lte"小于
				bdObjectTimeE.put("regDate",new BasicDBObject("$lt",etime));
				condList.add(bdObjectTimeE);
			}
			if(stime!=null||etime!=null){
				bdbObject.put("$and", condList);
			}
			if(StringUtils.isNotBlank(dept)){
				bdbObject.put("deptCode",dept);
			}
			if(StringUtils.isNotBlank(expxrt)){
				List<String> asList = Arrays.asList(expxrt.split(","));
				for (String doctCode : asList) {
					mongoDocList.add(new BasicDBObject("doctCode",doctCode));
				}
				bdbObject.put("$or", mongoDocList);
			}
			
			DBObject match = new BasicDBObject("$match", bdbObject); 
			DBObject group = new BasicDBObject("$group", groupFields); 
			AggregationOutput output = new MongoBasicDao().findGroupBy(TABLENAME_GZLDAY, match, group);
			Iterator< DBObject > it = output.results().iterator();
			Map<String, RegisterInfoGzltjVo> gbMap = new HashMap<String, RegisterInfoGzltjVo>();
			while(it.hasNext()){
				RegisterInfoGzltjVo vo = new RegisterInfoGzltjVo();
				BasicDBObject dbo = ( BasicDBObject ) it.next();
				//给对象赋值
				BasicDBObject keyValus = (BasicDBObject)dbo.get("_id");
				vo.setDept(keyValus.get("deptCode").toString());
				String rg = null;
				String dc = null;
				String dcCode = null;
				if(keyValus.get("doctName")!=null){
					dc = keyValus.get("doctName").toString();
					dcCode = keyValus.get("doctCode").toString();
					
				}
				if(keyValus.get("reglevlName")!=null){
					rg = keyValus.get("reglevlName").toString();
				}
				vo.setExpxrt(dcCode);
				vo.setTitle(rg);
				Integer dateToday = Integer.parseInt(keyValus.get("dateToday").toString());
				Integer regNum = Integer.parseInt(dbo.get("regNum").toString());
				Double monCost = Double.parseDouble(dbo.get("totalFee").toString());
				//把科室和医生组合成Key,vo为value,put进入map
				String deptCode = keyValus.get("deptCode").toString();
				String doctCode = dc;
				String mapKey = dept + "-" + doctCode;
				
				if(gbMap.get(mapKey)!=null){
					vo = gbMap.get(mapKey);
				}
				switch (dateToday) {
				case 1:
					vo.setMonNum(regNum);
					vo.setMonCost(monCost);
					break;
				case 2:
					vo.setTueNum(regNum);
					vo.setTueCost(monCost);
					break;
				case 3:
					vo.setWedNum(regNum);
					vo.setWedCost(monCost);
					break;
				case 4:
					vo.setThuNum(regNum);
					vo.setThuCost(monCost);
					break;
				case 5:
					vo.setFriNum(regNum);
					vo.setFriCost(monCost);
					break;
				case 6:
					vo.setSatNum(regNum);
					vo.setSatCost(monCost);
					break;
				case 7:
					vo.setSunNum(regNum);
					vo.setSunCost(monCost);
					break;

				default:
					break;
				}
				gbMap.put(mapKey, vo);
			}
			Integer r = StringUtils.isNotBlank(rows)?Integer.parseInt(rows):50;
			Integer p = StringUtils.isNotBlank(page)?Integer.parseInt(page):1;
			int start = 1;
			for(RegisterInfoGzltjVo voMap : gbMap.values()){
				start++;
				if(start>(r*(p-1))){
					voMap.setNum(voMap.getMonNum()+voMap.getTueNum()+voMap.getWedNum()+voMap.getThuNum()+voMap.getFriNum()+voMap.getSatNum()+voMap.getSunNum());
					voMap.setCost(voMap.getMonCost()+voMap.getTueCost()+voMap.getWedCost()+voMap.getThuCost()+voMap.getFriCost()+voMap.getSatCost()+voMap.getSunCost());
					voList.add(voMap);
				}
				if(start>(r*p)){
					break;
				}
			}
			Integer totalNum = gbMap.size();
			
			map.put("rows", voList);
			map.put("total", totalNum);

		
		return map;
	}
	
	@Override
	public List<RegisterInfoVo> findRegisterDeptInfo(String collectionName,
			List<String> date, List<String> deptCode)throws Exception {
		List<RegisterInfoVo> list = new ArrayList<RegisterInfoVo>();
		
		BasicDBObject dbobj = new BasicDBObject();
		dbobj.put("date", new BasicDBObject("$in",date));
		dbobj.put("deptCode", new BasicDBObject("$in", deptCode));
		DBCursor cursor = new MongoBasicDao().findAlldata(collectionName, dbobj);
		while (cursor.hasNext()) {
			Object object = cursor.next().get("value");
			RegisterInfoVo vo = JSONUtils.fromJson(object.toString(),
					RegisterInfoVo.class);
			list.add(vo);
		}
			
		return list;
	}
}
