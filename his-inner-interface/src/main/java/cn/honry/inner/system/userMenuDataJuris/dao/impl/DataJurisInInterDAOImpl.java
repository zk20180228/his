package cn.honry.inner.system.userMenuDataJuris.dao.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.BusinessDictionary;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.SysUserMenuDatajuris;
import cn.honry.base.bean.model.User;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inner.baseinfo.code.dao.CodeInInterDAO;
import cn.honry.inner.system.userMenuDataJuris.dao.DataJurisInInterDAO;
import cn.honry.inner.vo.AreaVo;
import cn.honry.inner.vo.DeptVO;
import cn.honry.utils.HisParameters;
import cn.honry.utils.ShiroSessionUtils;

/**  
 *  
 * @className：DataJurisInInterDAOImpl
 * @Description：  用户栏目数据权限维护接口
 * @Author：aizhonghua
 * @CreateDate：2017-6-23 下午18:59:31  
 * @Modifier：aizhonghua
 * @ModifyDate：2017-6-23 下午18:59:31  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
@Repository("dataJurisInInterDAO")
@SuppressWarnings({ "all" })
public class DataJurisInInterDAOImpl extends HibernateEntityDao<SysUserMenuDatajuris> implements DataJurisInInterDAO{
	
	/**为父类HibernateDaoSupport注入sessionFactory的值**/
	@Resource(name = "sessionFactory")
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	
	@Autowired
	@Qualifier(value = "innerCodeDao")
	private CodeInInterDAO innerCodeDao;

	/**  
	 *  
	 * 根据用户账户和栏目别名获得用户院区权限
	 * @Author：aizhonghua
	 * @CreateDate：2017-6-28 下午18:59:31  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2017-6-28 下午18:59:31  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public List<String> getHlAreaList(final String account,final String menuAlias) {
		final String sql = "SELECT md.JURIS_CODE jurisCode FROM T_SYS_USER_MENU_DATAJURIS md WHERE md.USER_ACC = ? AND md.MENU_ALIAS = ? AND md.JURIS_TYPE = 1";
		List<String> hlAreaList = (List<String>) this.getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException,SQLException {
				SQLQuery queryObject = session.createSQLQuery(sql);
				queryObject.setString(0,account).setString(1,menuAlias);
				return queryObject.list();
			}
		});
		if(hlAreaList!=null&&hlAreaList.size()>0){
			return hlAreaList;
		}
		return null;
	}
	
	/**  
	 *  
	 * 根据栏目别名得用户栏目权限（院区）
	 * @Author：aizhonghua
	 * @CreateDate：2017-7-12 下午18:59:31  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2017-7-12 下午18:59:31    
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public List<AreaVo> getDataJurisAreaList(String account,String menuAlias) {
		final List<String> hlAreaList = this.getHlAreaList(account, menuAlias);
		if(hlAreaList!=null){
			if(hlAreaList.size()==1&&"ALL".equals(hlAreaList.get(0))){
				List<BusinessDictionary> bdList = innerCodeDao.getDictionary(HisParameters.HOSPITALAREA);
				if(bdList!=null&&bdList.size()>0){
					List<AreaVo> areaVoList = new ArrayList<AreaVo>();
					AreaVo areaVo = null;
					for(BusinessDictionary bd : bdList){
						areaVo = new AreaVo();
						areaVo.setCode(bd.getEncode());
						areaVo.setName(bd.getName());
						areaVo.setPinyin(bd.getPinyin());
						areaVo.setWb(bd.getWb());
						areaVo.setInp(bd.getInputCode());
						areaVoList.add(areaVo);
					}
					return areaVoList;
				}
			}else{
				final StringBuffer buffer = new StringBuffer();
				buffer.append("SELECT b.CODE_ENCODE code,b.CODE_NAME name,b.CODE_PINYIN pinyin,b.CODE_WB wb,b.CODE_INPUTCODE inp FROM T_BUSINESS_DICTIONARY b ");
				buffer.append("WHERE b.DEL_FLG = ? AND b.STOP_FLG = ? AND b.CODE_TYPE = ? AND b.CODE_ENCODE IN (:hlAreaList) ");
				List<AreaVo> areaVoList = (List<AreaVo>) this.getHibernateTemplate().execute(new HibernateCallback() {
					@Override
					public Object doInHibernate(Session session) throws HibernateException,SQLException {
						SQLQuery queryObject = session.createSQLQuery(buffer.toString())
								.addScalar("code")
								.addScalar("name")
								.addScalar("pinyin")
								.addScalar("wb")
								.addScalar("inp");
						queryObject.setInteger(0, 0).setInteger(1, 0).setString(2, HisParameters.HOSPITALAREA).setParameterList("hlAreaList", hlAreaList);
						return queryObject.setResultTransformer(Transformers.aliasToBean(AreaVo.class)).list();
					}
				});
				if(areaVoList!=null&&areaVoList.size()>0){
					return areaVoList;
				}
			}
		}
		return null;
	}

	/**  
	 *  
	 * 根据用户账户、栏目别名及院区获得用户科室权限<br>
	 * 用户账户、栏目别名、院区都不为空时以院区为准<br>
	 * 如果院区不为空，获得院区下的全部科室<br>
	 * 如果院区为空，通过用户账户及栏目别名查询用户授权的科室<br>
	 * 院区为空时用户账户或栏目别名不能为空否则返回null<br>
	 * 用户账户和栏目别名为空时，院区不能为空否则返回null<br>
	 * 用户账户、栏目别名、院区都为空时返回null
	 * @Author：aizhonghua
	 * @CreateDate：2017-6-28 下午18:59:31  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2017-6-28 下午18:59:31  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public List<DeptVO> getJurisDept(final String userAcc, final String menuAlias,final List<String> hlAreaList) {
		final StringBuffer buffer = new StringBuffer();
		if(hlAreaList!=null){//如果院区不为空，获得院区下的全部科室
			if(hlAreaList.size()==1&&"ALL".equals(hlAreaList.get(0))){
				hlAreaList.remove(0);
				List<BusinessDictionary> bdList = innerCodeDao.getDictionary(HisParameters.HOSPITALAREA);
				if(bdList!=null&&bdList.size()>0){
					for(BusinessDictionary bd : bdList){
						hlAreaList.add(bd.getEncode());
					}
				}
			}
			buffer.append("SELECT d.DEPT_ID id,d.DEPT_NAME name,d.DEPT_TYPE type,d.DEPT_CODE code,d.DEPT_PINYIN pinyin,d.DEPT_WB wb,d.DEPT_INPUTCODE inputCode ");
			buffer.append("FROM T_DEPARTMENT d WHERE d.STOP_FLG = ? AND d.DEL_FLG = ? AND DEPT_AREA_CODE IN (:hlAreaList) ORDER BY d.DEPT_PATH ");
		}else if(StringUtils.isNotBlank(userAcc)&&StringUtils.isNotBlank(menuAlias)){//如果院区为空，通过用户账户及栏目别名查询用户授权的科室
			buffer.append("SELECT d.DEPT_ID id,d.DEPT_NAME name,d.DEPT_TYPE type,d.DEPT_CODE code,d.DEPT_PINYIN pinyin,d.DEPT_WB wb,d.DEPT_INPUTCODE inputCode ");
			buffer.append("FROM T_DEPARTMENT d WHERE d.STOP_FLG = ? AND d.DEL_FLG = ? ");
			buffer.append("AND DEPT_CODE IN (SELECT md .JURIS_CODE FROM T_SYS_USER_MENU_DATAJURIS md WHERE md.USER_ACC = ? AND md.MENU_ALIAS = ? AND md.JURIS_TYPE = ?) ");
			buffer.append("ORDER BY d.DEPT_PATH ");
		}else{
			return null;
		}
		List<DeptVO> list = (List<DeptVO>) this.getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException,SQLException {
				SQLQuery queryObject = session.createSQLQuery(buffer.toString())
						.addScalar("id")
						.addScalar("name")
						.addScalar("type")
						.addScalar("code")
						.addScalar("pinyin")
						.addScalar("wb")
						.addScalar("inputCode");
				if(hlAreaList!=null){
					queryObject.setInteger(0, 0).setInteger(1, 0).setParameterList("hlAreaList", hlAreaList);
				}else{
					queryObject.setInteger(0, 0).setInteger(1, 0).setString(2, userAcc).setString(3, menuAlias).setInteger(4, 2);
				}
				return queryObject.setResultTransformer(Transformers.aliasToBean(DeptVO.class)).list();
			}
		});
		if(list!=null&&list.size()>0){
			return list;
		}
		return null;
	}

	/**  
	 *  
	 * 根据栏目别名及科室类别获得用户栏目权限<br>
	 * 无权限返回null<br>
	 * (Integer)map.get("type")1院区级2科室级<br>
	 * (List<String>)map.get("code")院区或科室List编码
	 * @Author：aizhonghua
	 * @CreateDate：2017-6-28 下午18:59:31  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2017-6-28 下午18:59:31  
	 * @ModifyRmk：  
	 * @param:menuAlias栏目别名
	 * @param:deptTypes科室类别
	 * @version 1.0
	 *
	 */
	@Override
	public Map<String, Object> queryDataJurisByMenu(final String menuAlias,final String deptTypes) {
		if(StringUtils.isBlank(menuAlias)){
			return null;
		}
		final User user = ShiroSessionUtils.getCurrentUserFromShiroSession();
		final String areaSql = "SELECT JURIS_CODE FROM T_SYS_USER_MENU_DATAJURIS md WHERE md.USER_ACC = ? AND md.MENU_ALIAS = ? AND JURIS_TYPE = ?";
		List<String> areaList = (List<String>) this.getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException,SQLException {
				SQLQuery queryObject = session.createSQLQuery(areaSql);
				queryObject.setString(0,user.getAccount()).setString(1,menuAlias).setInteger(2, 1);
				return queryObject.list();
			}
		});
		if(areaList!=null&&areaList.size()>0){
			if(areaList.size()==1){
				if("ALL".equals(areaList.get(0))){
//					List<BusinessDictionary> bdList = innerCodeDao.getDictionary(HisParameters.HOSPITALAREA);
//					areaList.remove(0);
//					if(bdList!=null&&bdList.size()>0){
//						for(BusinessDictionary bd : bdList){
//							areaList.add(bd.getEncode());
//						}
//					}
				}
			}
			Map<String, Object> retMap = new HashMap<String, Object>();
			retMap.put("type", 1);
			retMap.put("code", areaList);
			return retMap;
		}
		final StringBuffer buffer = new StringBuffer();
		buffer.append("SELECT d.DEPT_CODE FROM T_DEPARTMENT d WHERE d.STOP_FLG = ? AND d.DEL_FLG = ? AND d.DEPT_CODE IN ( ");
		buffer.append("SELECT md.JURIS_CODE FROM T_SYS_USER_MENU_DATAJURIS md WHERE md.USER_ACC = ? AND md.MENU_ALIAS = ? AND JURIS_TYPE = ?) ");
		if(StringUtils.isNotBlank(deptTypes)){
			buffer.append("AND d.DEPT_TYPE IN (:deptTypeList) ");
		}
		List<String> deptList = (List<String>) this.getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException,SQLException {
				SQLQuery queryObject = session.createSQLQuery(buffer.toString());
				queryObject.setInteger(0,0).setInteger(1,0).setString(2,user.getAccount()).setString(3,menuAlias).setInteger(4, 2);
				if(StringUtils.isNotBlank(deptTypes)){
					List<String> deptTypeList = Arrays.asList(deptTypes.split(","));
					queryObject.setParameterList("deptTypeList", deptTypeList);
				}
				return queryObject.list();
			}
		});
		if(deptList!=null&&deptList.size()>0){
			Map<String, Object> retMap = new HashMap<String, Object>();
			retMap.put("type", 2);
			retMap.put("code", deptList);
			return retMap;
		}
		return null;
	}

	/**  
	 *  
	 * 根据栏目别名及用户账户获得全部科室权限<br>
	 * @Author：aizhonghua
	 * @CreateDate：2017-6-28 下午18:59:31  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2017-6-28 下午18:59:31  
	 * @ModifyRmk：  
	 * @param:menuAlias栏目别名
	 * @param:deptTypes科室类别
	 * @version 1.0
	 *
	 */
	@Override
	public List<SysDepartment> getJurisDeptList(final String menuAlias, final String userAcc) {
		if(StringUtils.isBlank(menuAlias)){
			return new ArrayList<SysDepartment>();
		}
		final String sql = "FROM SysDepartment d WHERE d.deptCode IN("+getJurisDeptHql(menuAlias,userAcc)+")";
		List<SysDepartment> deptList = (List<SysDepartment>) this.getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException {
				Query queryObject = session.createQuery(sql);
				return queryObject.list();
			}
		});
		if(deptList!=null&&deptList.size()>0){
			return deptList;
		}
		return new ArrayList<SysDepartment>();
	}
	
	/**  
	 *  
	 * 获得用户栏目科室SQL用于子查询<br>
	 * menuAlias:栏目别名<br>
	 * userAcc：用户帐户
	 * @Author：aizhonghua
	 * @CreateDate：2017-6-28 下午18:59:31  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2017-6-28 下午18:59:31  
	 * @ModifyRmk：  
	 * @param:menuAlias栏目别名
	 * @param:deptTypes科室类别
	 * @version 1.0
	 *
	 */
	@Override
	public String getJurisDeptSql(String menuAlias,String userAcc) {
		List<String> hlList = getHlAreaList(userAcc, menuAlias);
		if(hlList!=null){
			if(hlList.size()==1&&"ALL".equals(hlList.get(0))){
				return "SELECT d.DEPT_CODE FROM T_DEPARTMENT d WHERE d.DEL_FLG = 0 AND d.STOP_FLG = 0";
			}else{
				String hlStr = "";
				for(String hl : hlList){
					if(StringUtils.isNotBlank(hlStr)){
						hlStr += "','";
					}
					hlStr += hl;
				}
				return "SELECT d.DEPT_CODE FROM T_DEPARTMENT d WHERE d.DEL_FLG = 0 AND d.STOP_FLG = 0 AND d.DEPT_AREA_CODE IN ('"+hlStr+"')";
			}
		}else{
			return "SELECT md.JURIS_CODE FROM T_SYS_USER_MENU_DATAJURIS md WHERE md.USER_ACC = '"+userAcc+"' AND md.MENU_ALIAS = '"+menuAlias+"' ";
		}
	}
	
	
	/**  
	 *  
	 * 获得用户栏目科室SQL用于子查询<br>
	 * menuAlias:栏目别名<br>
	 * userAcc：用户帐户
	 * @Author：aizhonghua
	 * @CreateDate：2017-6-28 下午18:59:31  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2017-6-28 下午18:59:31  
	 * @ModifyRmk：  
	 * @param:menuAlias栏目别名
	 * @param:deptTypes科室类别
	 * @version 1.0
	 *
	 */
	@Override
	public String getJurisDeptHql(String menuAlias,String userAcc) {
		List<String> hlList = getHlAreaList(userAcc, menuAlias);
		if(hlList!=null){
			if(hlList.size()==1&&"ALL".equals(hlList.get(0))){
				return "SELECT d.deptCode FROM SysDepartment d WHERE d.del_flg = 0 AND d.stop_flg = 0";
			}else{
				String hlStr = "";
				for(String hl : hlList){
					if(StringUtils.isNotBlank(hlStr)){
						hlStr += "','";
					}
					hlStr += hl;
				}
				return "SELECT d.deptCode FROM SysDepartment d WHERE d.del_flg = 0 AND d.del_flg = 0 and d.areaCode IN ('"+hlStr+"')";
			}
		}else{
			return "SELECT md.jurisCode FROM SysUserMenuDatajuris md WHERE md.userAcc = '"+userAcc+"' AND md.menuAlias = '"+menuAlias+"' ";
		}
	}

}
