package cn.honry.inner.baseinfo.district.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.District;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inner.baseinfo.district.dao.DistrictInnerDao;


@Repository("districtInnerDAO")
@SuppressWarnings({"all"})
public class DistrictInnerDaoImpl extends HibernateEntityDao<District> implements DistrictInnerDao{
	
	
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	/**  
	 *  
	 * @Description：  分页查询－获得列表数据
	 * @Author：wujiao
	 * @CreateDate：2015-10-29 上午11:59:48  
	 * @Modifier：wujiao
	 * @ModifyDate：2015-10-29上午11:59:48  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public List<District> getPage(String page, String rows, District district) {
		String hql=joint(district);
		return super.getPage( hql,page, rows);
	}

	/**  
	 *  
	 * @Description：  分页查询－获得总条数
	 * @Author：wujiao
	 * @CreateDate：2015-10-29 上午11:59:48  
	 * @Modifier：wujiao
	 * @ModifyDate：2015-10-29上午11:59:48  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public int getTotal(District district) {
		String hql=joint(district);
		return  super.getTotal(hql);
	}
	private String joint(District district) {
		String hql="from District d where d.validFlag=1 ";
		if(district!=null){
			if(org.apache.commons.lang3.StringUtils.isNotEmpty(district.getCityName())){
			String queryName=district.getCityName();
				hql = hql+" AND d.cityName LIKE '%"+queryName+"%'"
				 + " OR d.pinyin LIKE '%"+queryName.toUpperCase()+"%'" 
				 + " OR d.wb LIKE '%"+queryName.toUpperCase()+"%'" 
				 + " OR d.defined LIKE '%"+queryName+"%'" 
				+ " OR d.cityCode LIKE '%"+queryName+"%'" ;
			}
		}
		hql+="ORDER BY d.path";
		return hql;
	}
	/**  
	 *  
	 * @Description：  通过父id和所有父级获得所有子级
	 * @Author：wujiao
	 * @CreateDate：2015-10-31 下午04:47:03  
	 * @Modifier：wujiao
	 * @ModifyDate：2015-10-31 下午04:47:03  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public List<District> getChildById(String id, String upperPath) {
		String hql="FROM District d WHERE d.upperPath LIKE '"+(StringUtils.isBlank(upperPath)?"":upperPath)+id+"%' ";
		List<District> districtList = super.findByObjectProperty(hql, null);
		if(districtList!=null&&districtList.size()>0){
			return districtList;
		}
		return new ArrayList<District>();
	}
	
	
	@Override
	public List<District> queryDistrictTree( String parId) {
		String hql="FROM District d WHERE d.validFlag=1  ";
		//zpty20160503如果添加的时候选中的节点作为父节点则,以下hql不能要了,否则无法渲染
//		if(parId!=null&&!"".equals(parId)){
//			hql+="AND d.id<>'"+parId+"'";
//		}
		hql+="ORDER BY d.path";
		List<District> disList = super.findByObjectProperty(hql, null);
		if(disList!=null&&disList.size()>0){
			return disList;
		}
		return null;
	}
	
	
	/**  
	 *  
	 * @Description：  获得栏目树 三级联动
	 * @Author：zpty
	 * @CreateDate：2015-12-16 上午12:16:53    
	 * @version 1.0
	 *
	 */
	@Override
	public List<District> queryDistrictTreeSJLD(Integer ld, String parId) {
		String hql="FROM District d WHERE d.validFlag=1 " ;
		if(ld>0){
			hql+=" and d.level="+ld;
		}
		if(ld==1){			
		}else{
			hql+=" AND d.parentId='"+parId+"'";
		}
		hql+=" ORDER BY d.path";
		List<District> disList = super.findByObjectProperty(hql, null);
		if(disList!=null&&disList.size()>0){
			return disList;
		}
		return null;
	}
	
	/**  
	 *  
	 * @Description：  获得栏目树 三级联动查询
	 * @Author：zpty
	 * @CreateDate：2015-12-16 上午12:16:53    
	 * @version 1.0
	 *
	 */
	@Override
	public List<District> queryDistrictTreeSJLDCX(String parId) {
		String hql="FROM District d WHERE d.validFlag=1  ";
		hql+=" AND d.cityCode='"+parId+"'";
		hql+=" ORDER BY d.path";
		List<District> disList = super.findByObjectProperty(hql, null);
		if(disList!=null&&disList.size()>0){
			return disList;
		}
		return null;
	}
	
	/**  
	 *  
	 * @Description：  删除
	 * @Author：wujiao
	 * @CreateDate：2015-11-2 上午12:16:53   
	 * @Modifier：wujiao
	 * @ModifyDate：2015-11-2 上午12:16:53   
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public void delDis(String id) {
		id=id.replaceAll(",", "','");
		 getSession().createQuery("DELETE FROM District WHERE id in('"+id+"')").executeUpdate();
	}
	
	/**
	 * 根据某一城市的cityCode查询它的下级城市
	 * @param pid
	 * @return
	 */
	public List<District> queryByPid(String pid){
		String hql="from District where validFlag=1 and parentId='"+pid+"' order by path";
		List<District> list = super.findByObjectProperty(hql, null);
		return list;
	}
	@Override
	public Integer vailCode(String disCode, String disId) {
		String sql = "from District where cityCode = " + disCode;
		if (StringUtils.isNotBlank(disId)) {
			sql += " and id != '" + disId + "'";
		}
		return super.getTotal(sql);
	}

}
