package cn.honry.oa.activiti.listener;

import cn.honry.base.bean.model.OaTaskInfo;


public interface HumanTaskListener {
    void onCreate(OaTaskInfo taskInfo) throws Exception;

    void onComplete(OaTaskInfo taskInfo) throws Exception;
}
