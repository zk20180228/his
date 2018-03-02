package cn.honry.inner.baseinfo.code.dao.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.BusinessDictionary;
import cn.honry.base.bean.model.DutiesContrast;
import cn.honry.base.bean.model.TitleContrast;
import cn.honry.base.bean.model.TypeContrast;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inner.baseinfo.code.dao.CodeInInterDAO;
import cn.honry.inner.vo.ComboGroupVo;
import cn.honry.utils.HisParameters;
/**
 * 
 * @Description 公共编码资料DAO实现层
 * @author  lyy
 * @createDate： 2016年7月5日 下午3:33:21 
 * @modifier lyy
 * @modifyDate：2016年7月5日 下午3:33:21
 * @param：  
 * @modifyRmk：  
 * @version 1.0
 */
@Repository("innerCodeDao")
@SuppressWarnings({"all"})
public class CodeInInterDAOImpl extends HibernateEntityDao<BusinessDictionary> implements CodeInInterDAO {

	@Resource(name = "sessionFactory")
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	
	@Override
	public int getTotal(BusinessDictionary entity) {
		String hql = joint(entity);
		return super.getTotal(hql);
	}

	@Override
	public List<BusinessDictionary> getPage(String page, String rows, BusinessDictionary entity) {
		String hql = joint(entity);
		return super.getPage(hql, page, rows);
	}
	/**
	 * 
	 * @Description 
	 * @author  lyy
	 * @createDate： 2016年7月5日 下午3:22:09 
	 * @modifier lyy
	 * @modifyDate：2016年7月5日 下午3:22:09
	 * @param：  
	 * @modifyRmk：  
	 * @version 1.0
	 */
	private String joint(BusinessDictionary entity){
		String hql = "from BusinessDictionary t where t.stop_flg = 0 and t.del_flg = 0 ";
		if(StringUtils.isNotBlank(entity.getId())){
			hql = hql+" and t.id = '"+entity.getId()+"'";
		}	
		if(StringUtils.isNotBlank(entity.getType())){
			hql = hql+" and t.type = '"+entity.getType()+"'";
		}
		if(StringUtils.isNotBlank(entity.getName())){
			hql = hql+" and t.name like '%"+entity.getName()+"%'";
		}	
		hql = hql+" ORDER BY t.createTime";
		return hql;
	}
	//GH 2017年2月15日17:47:09   添加排序字段
	@Override
	public List<BusinessDictionary> getDictionary(String type) {
		String hql = "from BusinessDictionary t where t.stop_flg = 0 and t.del_flg = 0 and t.type = '"+type+"' order by t.encode";
		List<BusinessDictionary> dictionaryList=super.find(hql, null);
//				findByObjectProperty(hql, null);
		if(dictionaryList!=null&&dictionaryList.size()>0){
			return dictionaryList;
		}
		return null;
	}
	
	@Override
	public BusinessDictionary getDictionaryByCode(String type, String code) {
		String hql = "from BusinessDictionary t where t.stop_flg = 0 and t.del_flg = 0 and t.type = '"+type+"' and t.encode='"+code+"'";
		List<BusinessDictionary> dicList=super.find(hql, null);
		if(dicList!=null&&dicList.size()>0){
			return dicList.get(0);
		}
		return null;
	}
	
	/**
	 * 下拉框查询
	 * @param code 参数  查询条件的参数
	 * @return List<CodeCenterfeecode>
	 */
	@Override
	public List<BusinessDictionary> likeSearch(String code) {
		String hql="from BusinessDictionary t where t.stop_flg = 0 and t.del_flg = 0 ";
		if(StringUtils.isNotBlank(code)){
			hql = hql+" and (  t.name like '%"+code+"%' or t.pinyin like '%"+code+"%' or t.wb like '%"+code+"%' or t.inputCode like '%"+code+"%') ";
		}
		hql=hql+" order by t.name";
		List<BusinessDictionary> list=super.findByObjectProperty(hql, null);
		if(list!=null&&list.size()>0){
			return list;
		}
		return new ArrayList<BusinessDictionary>();
	}
	/**
	 * 
	 * @Description 根据类型和名称 查询公共编码资料
	 * @author  aizhonghua
	 * @createDate： 2016年7月5日 下午3:25:51 
	 * @modifier aizhonghua
	 * @modifyDate：2016年7月5日 下午3:25:51
	 * @param：  type 类型     name 名称
	 * @modifyRmk：  
	 * @version 1.0
	 */
	@Override
	public BusinessDictionary getDictionaryByName(String type, String name) {
		String hql = "from BusinessDictionary t where t.stop_flg = 0 and t.del_flg = 0 and t.type = ? and t.name = ?";
		BusinessDictionary dicList = (BusinessDictionary) super.excHqlGetUniqueness(hql, type,name);
		if(dicList!=null&&StringUtils.isNotBlank(dicList.getId())){
			return dicList;
		}
		return null;
	}

	@Override
	public List<BusinessDictionary> likeTypeSearch(String type, String code,String page,String  rows) {
		String hql=" from BusinessDictionary t  where t.del_flg='0' and t.stop_flg='0' and t.type = '"+type+"' ";
		if(StringUtils.isNotBlank(code)){
			hql = hql+" and (  t.name like '%"+code+"%' or t.pinyin like '%"+code+"%' or t.wb like '%"+code+"%' or t.inputCode like '%"+code+"%') ";
		}
		hql=hql+" order by t.name";
		List<BusinessDictionary> list=super.getPage(hql, page, rows);
		if(list!=null&&list.size()>0){
			return list;
		}
		return new ArrayList<BusinessDictionary>();
	}

	@Override
	public int getTypeTotal(String type, String code) {
		String hql=" from BusinessDictionary t  where t.del_flg='0' and t.stop_flg='0' and  t.type = '"+type+"' ";
		if(StringUtils.isNotBlank(code)){
			hql = hql+" and (  t.name like '%"+code+"%' or t.pinyin like '%"+code+"%' or t.wb like '%"+code+"%' or t.inputCode like '%"+code+"%') ";
		}
		hql=hql+" order by t.name";
		int total=super.getTotal(hql);
		return total;
	}

	@Override
	public List<BusinessDictionary> searchCode(String type, String code) {
		String hql = "from BusinessDictionary t where t.stop_flg = 0 and t.del_flg = 0 and t.type = '"+type+"'";
		if(StringUtils.isNotBlank(code)){
			hql+=" AND  (t.name LIKE '%"+code+"%' "
					 + " OR t.encode LIKE '%"+code+"%'"
					 + " OR upper(t.pinyin) LIKE '%"+code.toUpperCase()+"%'" 
					 + " OR upper(t.wb) LIKE '%"+code.toUpperCase()+"%'" 
					 + " OR upper(t.inputCode) LIKE '%"+code.toUpperCase()+"%')";
		}
		List<BusinessDictionary> dicList=super.findByObjectProperty(hql, null);
		if(dicList!=null&&dicList.size()>0){
			return dicList;
		}
		return new ArrayList<BusinessDictionary>();
	}

	@Override
	public List<BusinessDictionary> querybkackList(String type) {
		String hql="from BusinessDictionary t where t.stop_flg = 0 and t.del_flg = 0 and t.type = '"+type+"'";
		List<BusinessDictionary> dicList=super.find(hql, null);
		if(dicList!=null&&dicList.size()>0){
			return dicList;
		}
		return new ArrayList<BusinessDictionary>();
	}

	/**  
	 * 
	 * <p> 获取全部（非）药品单位分组 </p>
	 * @Author: aizhonghua
	 * @CreateDate: 2016年10月18日 下午1:39:08 
	 * @Modifier: aizhonghua
	 * @ModifyDate: 2016年10月18日 下午1:39:08 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @return: List<ComboGroupVo>
	 *
	 */
	@Override
	public List<ComboGroupVo> getUnitAllGroup() {
		final StringBuffer hql = new StringBuffer();
		hql.append("SELECT  ltrim(rtrim(D.CODE_ENCODE)) AS code, ltrim(rtrim(D.CODE_NAME)) AS name,ltrim(rtrim(D.CODE_TYPE)) AS organize, D.CODE_PINYIN AS pinyin, D.CODE_WB AS wb, D.CODE_INPUTCODE AS inputCode FROM ");
		hql.append(HisParameters.HISPARSCHEMAHISUSER).append("T_BUSINESS_DICTIONARY D WHERE D.CODE_TYPE = '").append(HisParameters.DRUGPACKUNIT).append("' OR D.CODE_TYPE = '");
		hql.append(HisParameters.DRUGMINUNIT).append("' OR D.CODE_TYPE = '").append(HisParameters.DOSEUNIT).append("' OR D.CODE_TYPE = '").append(HisParameters.UNDRUGUNIT).append("' ORDER BY D.CODE_TYPE");
		List<ComboGroupVo> voList = (List<ComboGroupVo>) this.getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException,SQLException {
				SQLQuery queryObject = session.createSQLQuery(hql.toString())
						.addScalar("code")
						.addScalar("name")
						.addScalar("organize")
						.addScalar("pinyin")
						.addScalar("wb")
						.addScalar("inputCode");
				return queryObject.setResultTransformer(Transformers.aliasToBean(ComboGroupVo.class)).list();
			}
		});
		return voList;
	}

	/**  
	 * 
	 * 根据编码类别获得map
	 * @Author: aizhonghua
	 * @CreateDate: 2016年10月31日 下午4:42:39 
	 * @Modifier: aizhonghua
	 * @ModifyDate: 2016年10月31日 下午4:42:39 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	@Override
	public Map<String, String> getBusDictionaryMap(String type) {
		if(StringUtils.isNotBlank(type)){
			List<BusinessDictionary> list=this.getDictionary(type);
			if(list!=null){
				Map<String, String> map = new HashMap<String, String>();
				for (BusinessDictionary code : list) {
					map.put(code.getEncode(), code.getName());
				}
				return map;
			}
		}
		return new HashMap<String, String>();
	}
	
	/**  
	 * 
	 * 根据编码类别获得map
	 * @Author: aizhonghua
	 * @CreateDate: 2016年10月31日 下午4:42:39 
	 * @Modifier: aizhonghua
	 * @ModifyDate: 2016年10月31日 下午4:42:39 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	@Override
	public Map<String,BusinessDictionary> getBusDicMap(String type) {
		if(StringUtils.isNotBlank(type)){
			List<BusinessDictionary> list=this.getDictionary(type);
			if(list!=null){
				Map<String, BusinessDictionary> map = new HashMap<String, BusinessDictionary>();
				for (BusinessDictionary code : list) {
					map.put(code.getEncode(),code);
				}
				return map;
			}
		}
		return new HashMap<String, BusinessDictionary>();
	}

	@Override
	public List<BusinessDictionary> getDictionaryICD(String type) {
		final String hql="SELECT  T.CODE_ENCODE as encode,T.CODE_NAME as name,T.CODE_PINYIN as pinyin,T.CODE_WB as wb,T.CODE_INPUTCODE as inputCode FROM T_BUSINESS_DICTIONARY T WHERE T.CODE_TYPE='"+type+"' GROUP BY T.CODE_ENCODE,T.CODE_NAME,T.CODE_PINYIN,T.CODE_WB,T.CODE_INPUTCODE";
		List<BusinessDictionary> voList = (List<BusinessDictionary>) this.getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException,SQLException {
				SQLQuery queryObject = session.createSQLQuery(hql)
						.addScalar("encode")
						.addScalar("name")
						.addScalar("pinyin")
						.addScalar("wb")
						.addScalar("inputCode");
				return queryObject.setResultTransformer(Transformers.aliasToBean(BusinessDictionary.class)).list();
			}
		});
		return voList;
	}

	@Override
	public List<TitleContrast> getTitleContrasts() {
		final StringBuffer sqlBuffer = new StringBuffer();
		sqlBuffer.append("select t.code_encode as titleCode,t.code_name as titleName,c.title_level as titleLevel,");
		sqlBuffer.append("c.belong_typecode as belongTypeCode,c.belong_typename as belongTypeName,");
		sqlBuffer.append("t.code_pinyin as titlePinyin,t.code_wb as titleWb,t.code_inputcode as titleInputCode");
		sqlBuffer.append(" from t_business_dictionary t left join t_title_contrast c on t.code_encode = c.title_code");
		sqlBuffer.append(" where t.del_flg = 0 and t.stop_flg = 0 and t.code_type = 'title'");
		List<TitleContrast> contrasts = (List<TitleContrast>) this.getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException,SQLException {
				SQLQuery queryObject = session.createSQLQuery(sqlBuffer.toString())
						.addScalar("titleCode")
						.addScalar("titleName")
						.addScalar("titleLevel")
						.addScalar("belongTypeCode")
						.addScalar("belongTypeName")
						.addScalar("titlePinyin")
						.addScalar("titleWb")
						.addScalar("titleInputCode");
				return queryObject.setResultTransformer(Transformers.aliasToBean(TitleContrast.class)).list();
			}
		});
		if(contrasts != null && contrasts.size() > 0){
			return contrasts;
		}else {
			return new ArrayList<TitleContrast>();
		}
	}

	@Override
	public List<DutiesContrast> getDutiesContrasts() {
		final StringBuffer sqlBuffer = new StringBuffer();
		sqlBuffer.append("select t.code_encode as dutiesCode,t.code_name as dutiesName,c.duties_level as dutiesLevel,");
		sqlBuffer.append("c.BELONG_LEVEL as belongLevel,c.BELONG_LEVELNAME as belongLevelName,");
		sqlBuffer.append("t.code_pinyin as dutiesPinyin,t.code_wb as dutiesWb,t.code_inputcode as dutiesInputCode");
		sqlBuffer.append(" from t_business_dictionary t left join t_duties_contrast c on t.code_encode = c.duties_code");
		sqlBuffer.append(" where t.del_flg = 0 and t.stop_flg = 0 and t.code_type = 'duties' ");
		List<DutiesContrast> contrasts = (List<DutiesContrast>) this.getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException,SQLException {
				SQLQuery queryObject = session.createSQLQuery(sqlBuffer.toString())
						.addScalar("dutiesCode")
						.addScalar("dutiesName")
						.addScalar("dutiesLevel")
						.addScalar("belongLevel")
						.addScalar("belongLevelName")
						.addScalar("dutiesPinyin")
						.addScalar("dutiesWb")
						.addScalar("dutiesInputCode");
				return queryObject.setResultTransformer(Transformers.aliasToBean(DutiesContrast.class)).list();
			}
		});
		if(contrasts != null && contrasts.size() > 0){
			return contrasts;
		}else {
			return new ArrayList<DutiesContrast>();
		}
	}
	@Override
	public List<TypeContrast> getTypeContrasts() {
		final StringBuffer sqlBuffer = new StringBuffer();
		sqlBuffer.append("select t.code_encode as employeeType,t.code_name as employeeTypeName,");
		sqlBuffer.append("t.code_pinyin as employeePinyin,t.code_wb as employeeWb,t.code_inputcode as employeeInputCode");
		sqlBuffer.append(" from t_business_dictionary t");
		sqlBuffer.append(" where t.del_flg = 0 and t.stop_flg = 0 and t.code_type = 'employeeType'");
		List<TypeContrast> contrasts = (List<TypeContrast>) this.getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException,SQLException {
				SQLQuery queryObject = session.createSQLQuery(sqlBuffer.toString())
						.addScalar("employeeType")
						.addScalar("employeeTypeName")
						.addScalar("employeePinyin")
						.addScalar("employeeWb")
						.addScalar("employeeInputCode");
				return queryObject.setResultTransformer(Transformers.aliasToBean(TypeContrast.class)).list();
			}
		});
		if(contrasts != null && contrasts.size() > 0){
			return contrasts;
		}else {
			return new ArrayList<TypeContrast>();
		}
	}
}
