package cn.honry.migrate.firmMainTain.service.Impl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.FirmMainTain;
import cn.honry.base.bean.model.ServiceManagement;
import cn.honry.migrate.firmMainTain.dao.FirmMainTainDao;
import cn.honry.migrate.firmMainTain.service.FirmMainTainService;
import cn.honry.migrate.serviceManagement.service.ServiceManagementService;

@Service("firmMainTainService")
@Transactional
@SuppressWarnings({"all"})
public class FirmMainTainServiceImpl implements FirmMainTainService{
	
	@Autowired
	@Qualifier(value = "firmMainTainDao")
	private FirmMainTainDao firmMainTainDao;
	/**  
	 * 厂商维护列表查询
	 * @Author: wangshujuan
	 * @CreateDate: 2017年9月27日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年9月27日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 */
	@Override
	public List<FirmMainTain> queryFirmMainTain(String code,String page,String rows,String menuAlias) {
		return firmMainTainDao.queryFirmMainTain(code,page, rows, menuAlias);
	}
	/**  
	 * 厂商维护列表查询(总条数)
	 * @Author: wangshujuan
	 * @CreateDate: 2017年9月27日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年9月27日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 */
	@Override
	public int queryTotal(String code) {
		return firmMainTainDao.queryTotal(code);
	}
	/**  
	 * 厂商维护 (添加/修改)
	 * @Author: wangshujuan
	 * @CreateDate: 2017年9月27日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年9月27日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 */
	@Override
	public void saveFirmMainTain(FirmMainTain firmMainTain) {
		if(StringUtils.isBlank(firmMainTain.getId())){
			firmMainTain.setId(null);
			firmMainTain.setCreateTime(new Date());
			FirmMainTain mainTain = firmMainTainDao.getMaxCode(firmMainTain.getId());
			int FirmCode = Integer.valueOf(mainTain.getFirmCode());
			String code = "";
			if(FirmCode<9){
				FirmCode = FirmCode+1;
				code = "0" +FirmCode;
			}else{
				FirmCode = FirmCode+1;
				code = FirmCode+"";
			}
			firmMainTain.setFirmCode(code);
			firmMainTainDao.save(firmMainTain);
		}else{
			FirmMainTain firmMainTain1 = firmMainTainDao.getOnedata(firmMainTain.getId());
			firmMainTain1.setFirmCode(firmMainTain.getFirmCode());
			firmMainTain1.setFirmName(firmMainTain.getFirmName());
			firmMainTain1.setPassWord(firmMainTain.getPassWord());
			firmMainTain1.setCreateTime(firmMainTain.getCreateTime());
			firmMainTain1.setFirmView(firmMainTain.getFirmView());
			firmMainTain1.setFirmUser(firmMainTain.getFirmUser());
			firmMainTain1.setFirmPassword(firmMainTain.getFirmPassword());
			firmMainTain1.setUpdateTime(new Date());
			firmMainTainDao.save(firmMainTain1);
		}
	}
	
	/**  
	 * 厂商维护  (删除)
	 * @Author: wangshujuan
	 * @CreateDate: 2017年9月19日 下午4:09:43 
	 * @Modifier: wangshujuan
	 * @ModifyDate: 2017年9月19日 下午4:09:43 
	 * @ModifyRmk:  
	 * @version: V1.0
	 */
	@Override
	public FirmMainTain getOnedata(String id) {
		return firmMainTainDao.getOnedata(id);
	}
		@Override
	public void delFirmMainTain(String id) throws Exception {
		if(StringUtils.isNotBlank(id)){
			id=id.replaceAll(".*([';]+|(--)+).*","");
			firmMainTainDao.delFirmMainTain(id);
		}else{
			throw new Exception("参数为空");
		}
	}
		@Override
		public void updatePasswor(String id, String pass) throws Exception {
			if(StringUtils.isNotBlank(id)){
				id=id.replaceAll(".*([';]+|(--)+).*","");
				pass=pass.replaceAll(".*([';]+|(--)+).*","");
				firmMainTainDao.updatePasswor(id, pass);
			}else{
				throw new Exception("参数异常");
			}
		}
		@Override
		public List<FirmMainTain> queryFirm() {
			return firmMainTainDao.queryFirm();
		}
	
	

}
