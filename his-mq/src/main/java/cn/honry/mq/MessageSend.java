package cn.honry.mq;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import cn.honry.inner.oa.common.dao.CommonInterDao;
import cn.honry.utils.ReaderProperty;
import cn.honry.utils.RedisUtil;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
@SuppressWarnings("all")
public class MessageSend {

	private Logger logger = LoggerFactory.getLogger(MessageSend.class);

	@Resource(name = "menuTemplate")
	private AmqpTemplate menuTemplate;
	@Autowired
	private CommonInterDao commonInterDao;
	private static GsonBuilder gsonBuilder = new GsonBuilder();
	private static Gson gson = gsonBuilder.create();
	private static ReaderProperty readerProperty = new ReaderProperty(
			"sms.properties");
	@Autowired
	private RedisUtil redisUtil;

	// 发送文章信息
	public void sendMessage(Object message) throws IOException {
		menuTemplate.convertAndSend("sendMenuInfoKey", message);

		HashMap hashMap = gson.fromJson((String) message, HashMap.class);
		// 获取消息类型，对业务审批类型的消息进行处理
		String msg_type = (String) hashMap.get("msg_type");
		String sms_push_type = readerProperty.getValue_String("sms_push_type");
		if (null != msg_type && sms_push_type.equals(msg_type)) {
			// 获取业务审批短信标记
			String sms_push_mark = readerProperty
					.getValue_String("sms_push_mark");
			// 接口短信人的账号
			String jid = (String) hashMap.get("jid");
			// 获取业务申请标题（信息处-李郁鸿-住院医师规范化培训工作督查通知）[最后一个-后面的标题]
			String allTitle = (String) hashMap.get("title");
			String title = "";
			if (StringUtils.isNotBlank(allTitle)) {
				String[] split = allTitle.split("-");
				if (null != split && split.length > 0) {
					title = split[split.length - 1];
				}
			}
			// 业务ID
			String id = (String) hashMap.get("id");
			// 业务申请人的姓名
			String createUserName = commonInterDao.queryCreateUserNameById(id);
			// 短信内容的集合
			List<String> smsMsgList = new ArrayList<>();
			String sms_msg = createUserName + "-" + title;
			smsMsgList.add(sms_msg);
			// 先判断redis中是否存在此类信息
			boolean b = redisUtil.hexists(sms_push_mark, jid);
			if (b) {
				// 如果存在就取出来value 然后把新内容追加进去
				List<String> s = (List<String>) redisUtil.hget(sms_push_mark, jid);
				if (null != s && s.size() > 0) {
					s.add(sms_msg);
					redisUtil.hset(sms_push_mark, jid, s);
				} else {
					redisUtil.hset(sms_push_mark, jid, smsMsgList);
				}
			} else {
				// 如果不存在就把新的放进去
				redisUtil.hset(sms_push_mark, jid, smsMsgList);
			}
			redisUtil.expire(sms_push_mark, 86400);
		}
	}
}
