package cn.honry.hiasMongo.loginLog.service;

import java.util.Map;

import org.bson.Document;

import com.mongodb.BasicDBObject;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import cn.honry.base.bean.model.UserLogin;
import cn.honry.base.service.BaseService;
@SuppressWarnings({"all"})
public interface LoginLogService extends BaseService<UserLogin>{
	/**
	 * 保存用户登录信息
	 * @Author：hedong
	 * @CreateDate：2017-03-27
     * @version 1.0
	 * @param userLogin 用户登录实体
	 */
	void insertLoginByMongo(Document document);
	
	JSONArray getLoginLogByPage(String page, String rows, UserLogin userLogin, String ud);
	Long getTotalByPage(UserLogin userLogin);

}
