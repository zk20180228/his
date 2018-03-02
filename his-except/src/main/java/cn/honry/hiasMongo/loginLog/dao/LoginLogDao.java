package cn.honry.hiasMongo.loginLog.dao;

import java.util.Map;

import org.bson.Document;

import com.mongodb.BasicDBObject;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import cn.honry.base.bean.model.UserLogin;
import cn.honry.base.dao.EntityDao;
@SuppressWarnings({"all"})
public interface LoginLogDao extends EntityDao<UserLogin>{
	JSONArray getLoginLogByPage(String page, String rows, UserLogin userLogin, String ud);
	Long getTotalByPage(UserLogin userLogin);
	void insertLoginByMongo(Document document);
}
