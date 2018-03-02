package cn.honry.outpatient.recipedetail.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.OutpatientRecipedetail;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.outpatient.recipedetail.dao.RecipedetailDAO;
import cn.honry.outpatient.recipedetail.vo.KeyValueVo;
import cn.honry.utils.HisParameters;

@Repository("recipedetailDAO")
@SuppressWarnings({ "all" })
public class RecipedetailDAOImpl extends HibernateEntityDao<OutpatientRecipedetail> implements RecipedetailDAO {
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	/**
	 * 查询
	 */
	@Override
	public List query(String entity) {
		String hql=" from OutpatientRecipedetail as d where d.del_flg=0 and d.stop_flg=0 and patientNo= '"+entity+"'";
		List<OutpatientRecipedetail> OutpatientRecipedetaillist=super.getPage(hql, null, null);
		return OutpatientRecipedetaillist;
	}
	
	@Override
	public int getTotal(OutpatientRecipedetail entity) {
		String hql=" from OutpatientRecipedetail as bf where bf.del_flg=0 and bf.stop_flg=0 ";
		int c = super.getTotal(hql);
		return c;
	}
	/**
	 * 查询date
	 */
	@Override
	public List queryDate() {
		String hql="select distinct to_char(oper_date,'yyyy/mm/dd') from OutpatientRecipedetail order by 1 asc";
		List list=this.getSession().createQuery(hql).list();
		return list;
	}
	/**  
	 *  
	 * @Description： 获得医嘱信息
	 * @Author：aizhonghua
	 * @CreateDate：2016-01-25 上午11:10:39  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2016-01-25 上午11:10:39  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public List<OutpatientRecipedetail> getAdviceListByIds(String id) {
		id = id.replaceAll(",", "','");
		String hql="FROM OutpatientRecipedetail r WHERE r.id IN ('"+id+"') ";
		List<OutpatientRecipedetail> list=this.getSession().createQuery(hql).list();
		if(list!=null&&list.size()>0){
			return list;
		}
		return null;
	}
	
	
	
	/**  
	 *  
	 * @Description： 根据门诊号和病历号查询所有的处方号
	 * @Author：wanxing
	 * @CreateDate：2016-03-09 下午18:20:39  
	 * @ModifyRmk：  
	 * @version 1.0
	 */
	@Override
	public List<String> findRecipeNo(String clinicCode, String patientNo) {
		String sql = "SELECT DISTINCT o.RECIPE_NO AS id FROM T_OUTPATIENT_RECIPEDETAIL_NOW o WHERE o.CLINIC_CODE ='"+clinicCode+"' AND o.PATIENT_NO ='"+patientNo+"' AND"
				+ " ( o.CLASS_CODE NOT IN ('402880b751f104d10151f192e0970005','402880b751f104d10151f192b0630004'))";
		List<KeyValueVo> voList = this.getSession().createSQLQuery(sql).addScalar("id").setResultTransformer(Transformers.aliasToBean(KeyValueVo.class)).list();
		List<String> list = new ArrayList<String>();
		for(KeyValueVo vo :voList){
			list.add(vo.getId());
		}
		return list;
	}
	
	/**  
	 *  
	 * @Description： 根据就诊卡号获得患者信息
	 * @Author：aizhonghua
	 * @CreateDate：2016-01-25 上午11:10:39  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2016-01-25 上午11:10:39  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public KeyValueVo queryPatient(String no) {
		String sql = "SELECT i.IDCARD_ID AS id,P.PATIENT_NAME AS NAME FROM "+HisParameters.HISPARSCHEMAHISUSER+"T_PATIENT P INNER JOIN "+HisParameters.HISPARSCHEMAHISUSER+"T_PATIENT_IDCARD i ON P.PATINENT_ID = i.PATINENT_ID WHERE i.IDCARD_NO = '"+no+"'";
		List<KeyValueVo> list = this.getSession().createSQLQuery(sql).addScalar("id").addScalar("name").setResultTransformer(Transformers.aliasToBean(KeyValueVo.class)).list();
		if(list!=null&&list.size()>0){
			return list.get(0);
		}else{
			return null;
		}
	}
	
	/**  
	 *  
	 * @Description：  根据看诊号获得就诊卡号
	 * @Author：aizhonghua
	 * @CreateDate：2016-01-25 上午11:10:39  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2016-01-25 上午11:10:39  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public String getIdcardNoByRegisterNo(String no) {
		//
		String sql = "SELECT p.IDCARD_NO AS id FROM "+HisParameters.HISPARSCHEMAHISUSER+"T_PATIENT_IDCARD p WHERE p.IDCARD_ID = (SELECT i.IDCARD_ID FROM "+HisParameters.HISPARSCHEMAHISUSER+"T_REGISTER_INFO i WHERE i.REGISTER_NO = '"+no+"')";
		List<KeyValueVo> list = this.getSession().createSQLQuery(sql).addScalar("id").setResultTransformer(Transformers.aliasToBean(KeyValueVo.class)).list();
		if(list!=null&&list.size()>0){
			return list.get(0).getId();
		}
		return null;
	}
}
