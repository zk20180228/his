package cn.honry.oa.messagersend.service;

import java.util.List;
import java.util.Map;

public interface MessagerSendService {
	/**  
	 * <p>根据code查询手机号 </p>
	 * @Author: mrb
	 * @CreateDate: 2017年10月24日 上午11:57:12 
	 * @Modifier: mrb
	 * @ModifyDate: 2017年10月24日 上午11:57:12 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param acceptUser
	 * @return
	 * List<String>
	 */
	List<String> getAcceptUser(String acceptUser);
	/**  
	 * <p>短信发送 </p>
	 * @Author: mrb
	 * @CreateDate: 2017年10月24日 下午12:01:29 
	 * @Modifier: mrb
	 * @ModifyDate: 2017年10月24日 下午12:01:29 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param acceptUser
	 * @param content
	 * @return
	 * Map<String,String>
	 */
	Map<String, String> msgSend(String acceptUser,String content,String definedaccept);
}
