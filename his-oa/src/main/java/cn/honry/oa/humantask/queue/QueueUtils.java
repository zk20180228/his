package cn.honry.oa.humantask.queue;


import cn.honry.mq.MessageSend;
import cn.honry.utils.JSONUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description:此模块的队列推送工具类
 * @Author: zhangkui
 * @CreateDate: 2018/2/1 17:12
 * @Modifier: zhangkui
 * @version: V1.0
 */
@Service("queueUtils")
public class QueueUtils {


    Logger log = Logger.getLogger(QueueUtils.class);

    @Resource
    private MessageSend messageSend;

    /**
     * msg_type:msg_type_reject 驳回消息推送
     * @param receiver 接受者
     * @param title 驳回的标题
     */
    public void push_msg_type_reject(String receiver,String title){

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("jid",receiver);//接收人员工号
        map.put("title",title);//标题
        map.put("createTime", new Date());//创建时间
        map.put("msg_type","msg_type_reject");//类型为驳回提醒
        try {
            messageSend.sendMessage(JSONUtils.toJson(map,"yyyy-MM-dd HH:mm:ss"));
        } catch (Exception e) {
            log.error("cn.honry.oa.humantask.queue.QueueUtils:push_msg_type_reject"+"驳回消息推送失败！详细信息---->"+e.getMessage());
        }

    }



}
