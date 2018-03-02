package cn.honry.oa.information.dao;

import java.util.List;

import cn.honry.base.bean.model.InformationAttachDownAuth;
import cn.honry.base.dao.EntityDao;

public interface InformationAttachDownAuthDao extends EntityDao<InformationAttachDownAuth> {
	/**  
	 * <p>根据栏目id获取附件权限信息 </p>
	 * @Author: mrb
	 * @CreateDate: 2017年7月24日 下午5:35:29 
	 * @Modifier: mrb
	 * @ModifyDate: 2017年7月24日 下午5:35:29 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param menuid
	 * @return
	 * List<InformationAttachDownAuth>
	 */
	List<InformationAttachDownAuth> getAuthByMenuID(String menuid,boolean flag);
	/**  
	 * <p>删除该栏目下的附件信息 </p>
	 * @Author: mrb
	 * @CreateDate: 2017年7月25日 下午5:01:02 
	 * @Modifier: mrb
	 * @ModifyDate: 2017年7月25日 下午5:01:02 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param munuid
	 * @return
	 * int
	 */
	int delAuth(String munuid);
	/**  
	 * <p>通过地址删除上传的附件（仅删除表里的数据） </p>
	 * @Author: mrb
	 * @CreateDate: 2017年7月26日 下午5:38:09 
	 * @Modifier: mrb
	 * @ModifyDate: 2017年7月26日 下午5:38:09 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param fileurl
	 * int
	 */
	int deleteFile(String fileurl);
}
