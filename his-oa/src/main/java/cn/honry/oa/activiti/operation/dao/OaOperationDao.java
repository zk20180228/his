package cn.honry.oa.activiti.operation.dao;

import cn.honry.oa.activiti.operation.vo.EducationVo;

import java.util.List;
import java.util.Map;

/**
 * @Description:只查询当前登录用户的学历
 * @Author: zhangkui
 * @CreateDate: 2018/1/22 20:35
 * @Modifier: zhangkui
 * @version: V1.0
 */
public interface OaOperationDao {

    public Map<String, String> findEdcuations();

}
