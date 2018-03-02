package cn.honry.oa.formInfo.service;

import java.io.IOException;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.honry.base.bean.model.OaFormInfo;
import cn.honry.base.service.BaseService;
import cn.honry.oa.formInfo.vo.KeyValVo;

/**  
 *  
 * @className：FormInfoService
 * @Description：  自定义表单维护
 * @Author：aizhonghua
 * @CreateDate：2017-7-17 下午18:56:31  
 * @Modifier：aizhonghua
 * @ModifyDate：2017-7-17 下午18:56:31  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
public interface FormInfoService  extends BaseService<OaFormInfo>{

	/**  
	 *  
	 * 获取列表-获取总条数
	 * @Author：aizhonghua
	 * @CreateDate：2015-7-17 下午18:56:31  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-7-17 下午18:56:31  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	int getFormInfoTotal(String search);

	/**  
	 *  
	 * 获取列表-获取展示信息
	 * @Author：aizhonghua
	 * @CreateDate：2015-7-17 下午18:56:31  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-7-17 下午18:56:31  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	List<OaFormInfo> getFormInfoRows(String page, String rows, String search);

	/**  
	 *  
	 * 停用/启用
	 * @Author：aizhonghua
	 * @CreateDate：2015-7-17 下午18:56:31  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-7-17 下午18:56:31  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	int stopflgFormInfo(String search, int flg);

	/**  
	 *  
	 * 删除
	 * @Author：aizhonghua
	 * @CreateDate：2015-7-17 下午18:56:31  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-7-17 下午18:56:31  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	int delFormInfo(String search);

	/**  
	 *  
	 * 保存
	 * @Author：aizhonghua
	 * @CreateDate：2015-7-18 下午18:56:31  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-7-18 下午18:56:31  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	Map<String, String> saveFormInfo(OaFormInfo formInfo, ArrayList<KeyValVo> keyValueList);

	/**  
	 *  
	 * 获取可用表单
	 * @Author：aizhonghua
	 * @CreateDate：2015-7-17 下午18:56:31  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-7-17 下午18:56:31  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	List<KeyValVo> getValidFormInfo();

	/**  
	 *  
	 * 接口-获取表单主件<br>
	 * code表单编码
	 * @Author：aizhonghua
	 * @CreateDate：2015-7-17 下午18:56:31  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-7-17 下午18:56:31  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	List<KeyValVo> getPartOfFormInfo(String code);
	
	/**  
	 *  
	 * ClobToString
	 * @Author：aizhonghua
	 * @CreateDate：2015-7-17 下午18:56:31  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-7-17 下午18:56:31  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	String ClobToString(Clob clob) throws SQLException, IOException;

	/**
	 * <p>根据code查找from</p>
	 * @Author: yuke
	 * @CreateDate: 2017年9月26日 下午2:30:16 
	 * @Modifier: yuke
	 * @ModifyDate: 2017年9月26日 下午2:30:16 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: OaFormInfo
	 *
	 */
	OaFormInfo getFromByCode(String tableCode);

}
