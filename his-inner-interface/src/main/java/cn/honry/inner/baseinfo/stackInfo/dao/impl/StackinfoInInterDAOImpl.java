package cn.honry.inner.baseinfo.stackInfo.dao.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.BusinessDictionary;
import cn.honry.base.bean.model.BusinessStackinfo;
import cn.honry.base.bean.model.DrugUndrug;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.User;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inner.baseinfo.stackInfo.dao.StackinfoInInterDAO;
import cn.honry.utils.SessionUtils;

@Repository("stackinfoInInterDAO")
@SuppressWarnings({ "all" })
public class StackinfoInInterDAOImpl  extends HibernateEntityDao<BusinessStackinfo> implements StackinfoInInterDAO{
	
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {

		super.setSessionFactory(sessionFactory);
	}

	@Override
	public List<BusinessStackinfo> findStackInfoByFk(String fkStackId) {
		String hql="from BusinessStackinfo as bs where bs.del_flg = 0 and bs.stop_flg=0 and bs.stackId="+"'"+fkStackId+"'";
		List<BusinessStackinfo> stackInfoList=super.findByObjectProperty(hql, null);
		if(stackInfoList!=null && stackInfoList.size()>0){
			Map<String,String> map = new HashMap<String, String>();
			String hqlDrugAndUndrug="select t.drug_id as id, t.drug_name as name from t_drug_info t where"
					+ " t.stop_flg=0 and t.del_flg=0 union select t1.undrug_id as id, t1.undrug_name as name"
					+ " from t_drug_undrug t1 where t1.stop_flg=0 and t1.del_flg=0";
			SQLQuery queryObject = this.getSession().createSQLQuery(hqlDrugAndUndrug).addScalar("id",Hibernate.STRING).addScalar("name",Hibernate.STRING);
		    String h="from SysDepartment d where d.del_flg=0 and d.stop_flg=0";
		    String punitHql="from CodeDrugpackagingunit p where p.del_flg=0 and p.stop_flg=0 and p.type='drugPackagingunit'";
		    List<SysDepartment> sysList=super.find(h,null);
		    List<BusinessDictionary> punitList=super.find(punitHql,null);
			List<DrugUndrug> list = queryObject.setResultTransformer(Transformers.aliasToBean(DrugUndrug.class)).list();
			if(list!=null&&list.size()>0){
				for(DrugUndrug undrug : list){
					if(org.apache.commons.lang3.StringUtils.isNotBlank(undrug.getId())){
						map.put(undrug.getId(), undrug.getName());
					}
				}
			}
			if(sysList!=null&&sysList.size()>0){
				for(SysDepartment dept : sysList){
					if(org.apache.commons.lang3.StringUtils.isNotBlank(dept.getId())){
						map.put(dept.getId(), dept.getDeptName());
					}
				}
			}
			if(punitList!=null&&punitList.size()>0){
				for(BusinessDictionary unit : punitList){
					if(org.apache.commons.lang3.StringUtils.isNotBlank(unit.getId())){
						map.put(unit.getId(), unit.getName());
					}
				}
			}
			if(map.size()>0){
				for(BusinessStackinfo businessStackinfo:stackInfoList){
					if(map.containsKey(businessStackinfo.getStackInfoDeptid())){
						businessStackinfo.setStackInfoItemName(map.get(businessStackinfo.getStackInfoDeptid()));
					}
					if(map.containsKey(businessStackinfo.getStackInfoUnit())){
						businessStackinfo.setDrugPackagingunit(map.get(businessStackinfo.getStackInfoUnit()));
					}
					if(map.containsKey(businessStackinfo.getStackInfoItemId())){
						businessStackinfo.setStackInfoItemIdShow(map.get(businessStackinfo.getStackInfoItemId()));
						if(businessStackinfo.getIsDrug() !=null){
							if(businessStackinfo.getIsDrug()==1){
								businessStackinfo.setIsDrugShow("是");
							}
							if(businessStackinfo.getIsDrug()==2){
								businessStackinfo.setIsDrugShow("否");
							}
						}if(businessStackinfo.getMainDrug()!=null){
							if(businessStackinfo.getMainDrug()==1){
								businessStackinfo.setMainDrugshow("是");
							}
							if(businessStackinfo.getMainDrug()==2){
								businessStackinfo.setMainDrugshow("否");
							}
						}
					}
				}
			}
			return stackInfoList;
		}
		return new ArrayList<BusinessStackinfo>();
	}

	@Override
	public void batchDel(String ids) {
		//User user = WebUtils.getSessionUser();
		String idArg="";
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String[] idArgs=ids.split(",");
		if(idArgs.length>1){
			for(int i=0;i<idArgs.length;i++){
				idArg +="'"+idArgs[i]+"'"+",";
			}
			idArg=idArg.substring(0, idArg.length()-1);
		}else{
			idArg=ids;
		}
		String dateStr=format.format(new Date());
		StringBuilder sb = new StringBuilder();
		sb.append("update BusinessStackinfo t set DEL_FLG = 1 ,DELETETIME=to_date('"+dateStr+"','yyyy/MM/dd')  where t.stackId="+"'"+ids+"'");
		SQLQuery query = getSession().createSQLQuery(sb.toString());
		query.executeUpdate();
	}
	@Override
	public void deleteBusinessStackInfo(String stackId) {
		User user = (User) SessionUtils.getCurrentUserFromShiroSession();
		getSession().createQuery("update BusinessStackinfo t set t.del_flg = 1,t.deleteTime= ?,t.deleteUser= ? where t.stackId = ('"+stackId+"')")
		.setTimestamp(0, new Date())
		.setParameter(1, user.getAccount()).executeUpdate();
	}

}
