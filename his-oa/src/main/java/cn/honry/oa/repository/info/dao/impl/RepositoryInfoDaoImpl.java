package cn.honry.oa.repository.info.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.bson.Document;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.springframework.stereotype.Repository;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import cn.honry.base.bean.model.RepositoryInfo;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.hiasMongo.basic.MongoBasicDao;
import cn.honry.oa.repository.info.dao.RepositoryInfoDao;
import cn.honry.utils.HisParameters;
import cn.honry.utils.ShiroSessionUtils;
@Repository(value="repositoryInfoDao")
public class RepositoryInfoDaoImpl extends HibernateEntityDao<RepositoryInfo> implements
		RepositoryInfoDao {
	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	@Override
	public List<RepositoryInfo> getRepositoryInfo(String code, String name,
			int isOvert, int isCollect, String page, String rows,int pubFlg,String nodeType) {
		StringBuffer sb = new StringBuffer();
		sb.append(" from RepositoryInfo where stop_flg=0 and del_flg=0 ");
		sb.append(" and isOvert = "+isOvert);
		sb.append(" and isCollect = "+isCollect);
		if(isCollect == 0){
			sb.append(" and pubFlg = "+isCollect);
		}
		if(StringUtils.isNotBlank(code)){
			if("1".equals(nodeType)){
				sb.append(" and categCode in ( select t.code from RepositoryCateg t where t.diseaseCode = '"+code+"' and t.stop_flg=0 and t.del_flg=0 ) ");
			}else if("0".equals(nodeType)){
				sb.append(" and categCode = '"+code+"' ");
			}
		}
		if(StringUtils.isNotBlank(name)){
			sb.append(" and name like '%"+name+"%' ");
		}
		if(isOvert == 0 || isCollect == 1){
			String account = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
			sb.append(" and createUser = '"+account+"' ");
		}
		sb.append(" order by updateTime desc ");
		List<RepositoryInfo> list = this.getPage(sb.toString(), page, rows);
		if(list!=null&&list.size()>0){
			return list;
		}
		return new ArrayList<RepositoryInfo>();
	}

	@Override
	public int getRepositoryInfoTotal(String code, String name, int isOvert,
			int isCollect,int pubFlg,String nodeType) {
		StringBuffer sb = new StringBuffer();
		sb.append(" from RepositoryInfo where stop_flg=0 and del_flg=0 ");
		sb.append(" and isOvert = "+isOvert);
		sb.append(" and isCollect = "+isCollect);
		if(isCollect == 0){
			sb.append(" and pubFlg = "+isCollect);
		}
		if(StringUtils.isNotBlank(code)){
			if("1".equals(nodeType)){
				sb.append(" and categCode in ( select t.code from RepositoryCateg t where t.diseaseCode = '"+code+"' and t.stop_flg=0 and t.del_flg=0 ) ");
			}else if("0".equals(nodeType)){
				sb.append(" and categCode = '"+code+"' ");
			}
		}
		if(StringUtils.isNotBlank(name)){
			sb.append(" and name like '%"+name+"%' ");
		}
		if(isOvert == 0 || isCollect == 1){
			String account = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
			sb.append(" and createUser = '"+account+"' ");
		}
		return this.getTotal(sb.toString());
	}

	@Override
	public void insertIntoMongo(String content, String contentId) {
		Document doc = new Document();
		doc.append("contentId", contentId);
		doc.append("content", content);
		new MongoBasicDao().insertData("REPOSITORY_CONTENT", doc);
		
	}

	@Override
	public String getContentFromMongo(String contentId) {
		BasicDBObject where = new BasicDBObject();
		where.put("contentId", contentId);
		DBCursor cursor = new MongoBasicDao().findAlldata("REPOSITORY_CONTENT", where );
		String content = null;
		while(cursor.hasNext()){
			DBObject next = cursor.next();
			content = (String) next.get("content");
			if(StringUtils.isNotBlank(content)){
				content = content.replace("/upload/", HisParameters.getfileURI(ServletActionContext.getRequest())+"/upload/");
			}
		}
		return content;
	}

	@Override
	public void deleteFromMongo(String contentId) {
		BasicDBObject where = new BasicDBObject();
		where.put("contentId", contentId);
		new MongoBasicDao().remove("REPOSITORY_CONTENT", where);
	}

	@Override
	public void updateViews(String infoid) {
		StringBuffer sb = new StringBuffer();
		sb.append("Update t_repository_info Set views = views+1 Where Id='"+infoid+"'");
		Session session = this.getSessionFactory().getCurrentSession();
		SQLQuery query = session.createSQLQuery(sb.toString());
		query.executeUpdate();
	}

	
}
