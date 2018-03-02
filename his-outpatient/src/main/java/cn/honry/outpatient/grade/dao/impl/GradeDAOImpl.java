package cn.honry.outpatient.grade.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import cn.honry.base.bean.model.RegisterGrade;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.outpatient.grade.dao.GradeDAO;
import cn.honry.utils.DateUtils;
import cn.honry.utils.ShiroSessionUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@Repository("gradeDAO")
@SuppressWarnings({ "all" })
public class GradeDAOImpl extends HibernateEntityDao<RegisterGrade> implements GradeDAO{
	
	
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	
	
	@Override
	public List<RegisterGrade> queryAll() {
		String hql="FROM RegisterGrade r WHERE  r.del_flg = 0 ";
		List<RegisterGrade> gradeList = super.findByObjectProperty(hql, null);
		if(gradeList!=null&&gradeList.size()>0){
			return gradeList;
		}
		return null;
	}

	@Override
	public List<RegisterGrade> getPage(RegisterGrade entity, String page,
			String rows) {
		String hql = joint(entity);
		return super.getPage(hql, page, rows);
	}

	@Override
	public int getTotal(RegisterGrade entity) {
		String hql = joint(entity);
		return super.getTotal(hql);
		
	}

	public String joint(RegisterGrade entity){
		String hql="FROM RegisterGrade r WHERE  r.del_flg = 0 ";
		if(entity!=null){
			if(StringUtils.isNotBlank(entity.getCode())){
				String queryCode= entity.getCode().toUpperCase();
				hql = hql+" AND (upper(r.name) LIKE '%"+queryCode+"%'"+
					      " or upper(r.codePinyin) LIKE '%"+queryCode+"%'"+
					      " or upper(r.code) LIKE '%"+queryCode+"%'"+
					      " or upper(r.codeWb) LIKE '%"+queryCode+"%'"+
					      " or  upper(r.codeInputcode) LIKE '%"+queryCode+"%'" +
					      " )";
			}
			if(StringUtils.isNotBlank(entity.getName())){
				hql = hql+" and name = '"+entity.getName()+"'  ";
			}
		}
		return hql;
	}

	@Override
	public boolean saveOrUpdate(String registerGradeJson) {
		String pinyin="";
		String wb="";
		Gson gson = new Gson();
		List<RegisterGrade> modelList = gson.fromJson(registerGradeJson, new TypeToken<List<RegisterGrade>>(){}.getType());
		for(RegisterGrade modl : modelList){
			if(StringUtils.isBlank(modl.getId())){
				modl.setId(null);
				String str = super.getSpellCode(modl.getName());
				int index=str.indexOf("$");
				pinyin=str.substring(0,index);
				wb=str.substring(index+1);
				modl.setCodePinyin(pinyin);
				modl.setCodeWb(wb);
				modl.setCreateUser(ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
				modl.setCreateDept(ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession().getDeptCode());
				modl.setCreateTime(DateUtils.getCurrentTime());
				modl.setDel_flg(0);
				modl.setStop_flg(0);
			}else{
				String str = super.getSpellCode(modl.getName());
				int index=str.indexOf("$");
				pinyin=str.substring(0,index);
				wb=str.substring(index+1);
				modl.setCodePinyin(pinyin);
				modl.setCodeWb(wb);
				modl.setCreateTime(new Date());
			}
			super.save(modl);
		}
		return true;
	}

	@Override
	public List<RegisterGrade> getCombobox(String time) {
		String hql=null;
		 hql="FROM RegisterGrade r WHERE  r.del_flg = 0 ";
		 List<RegisterGrade> gradeList=new ArrayList<RegisterGrade>();
		  gradeList=super.findByObjectProperty(hql, null);
		if(gradeList.size()==0 || gradeList==null){
			 hql="FROM RegisterGrade r WHERE  r.del_flg = 0 ";
			  gradeList=super.findByObjectProperty(hql, null);
		}
		return gradeList;
	}

	/**   
	*  
	* @description：查询修改的那一条级别信息
	* @author：ldl
	* @createDate：2015-10-15 上午09:13:36  
	* @modifyRmk：  
	* @version 1.0
	*/
	@Override
	public List<RegisterGrade> findGradeEdit(String id) {
		id=id.replaceAll(",", "','");
		String hql = "from RegisterGrade where id in ('"+id+"') and del_flg = 0 ";
		List<RegisterGrade> gradeList = super.find(hql, null);
		if(gradeList==null||gradeList.size()<=0){
			return new ArrayList<RegisterGrade>();
		}
		return gradeList;
	}

	
	/**   
	*  
	* @description：唯一验证
	* @author：ldl
	* @createDate：2015-11-4 上午09:13:36  
	* @modifyRmk：  
	* @version 1.0
	*/
	@Override
	public List<RegisterGrade> findGradeSize(String id) {
		String hql = "from RegisterGrade where del_flg = 0  and encode = '"+id+"' " ;
		List<RegisterGrade> gradeList = super.find(hql, null);
		if(gradeList==null||gradeList.size()<=0){
			return new ArrayList<RegisterGrade>();
		}
		return gradeList;
	}
	
	/**
	 * 
	 * <p>获取排序号 </p>
	 * @Author: zhangkui
	 * @CreateDate: 2017年9月30日 上午10:24:16 
	 * @Modifier: zhangkui
	 * @ModifyDate: 2017年9月30日 上午10:24:16 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @return
	 * @throws:
	 *
	 */
	public Long getMaxOrder(){
		
		Query q =this.createQuery("select max(r.order) from RegisterGrade r");
		Long maxint=(Long)q.uniqueResult(); 
		
		return maxint;
	}
	
	
	
}
	


