package cn.honry.oa.formInfo.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.OaFormInfo;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.User;
import cn.honry.oa.formInfo.dao.FormInfoDAO;
import cn.honry.oa.formInfo.service.FormInfoService;
import cn.honry.oa.formInfo.vo.FielVo;
import cn.honry.oa.formInfo.vo.InfoVo;
import cn.honry.oa.formInfo.vo.KeyValVo;
import cn.honry.oa.formInfo.vo.SectVo;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.ShiroSessionUtils;

/**  
 *  
 * @className：FormInfoServiceImpl
 * @Description：  自定义表单维护
 * @Author：aizhonghua
 * @CreateDate：2017-7-17 下午18:56:31  
 * @Modifier：aizhonghua
 * @ModifyDate：2017-7-17 下午18:56:31  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
@Service("formInfoService")
@Transactional
@SuppressWarnings({ "all" })
public class FormInfoServiceImpl implements FormInfoService{

	@Autowired
	@Qualifier(value = "formInfoDAO")
	private FormInfoDAO formInfoDAO;

	@Override
	public void removeUnused(String id) {
		
	}

	@Override
	public OaFormInfo get(String id) {
		return formInfoDAO.get(id);
	}

	@Override
	public void saveOrUpdate(OaFormInfo formInfo) {
		formInfoDAO.save(formInfo);
	}

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
	@Override
	public int getFormInfoTotal(String search) {
		return formInfoDAO.getFormInfoTotal(search);
	}

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
	@Override
	public List<OaFormInfo> getFormInfoRows(String page, String rows, String search) {
		return formInfoDAO.getFormInfoRows(page,rows,search);
	}

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
	@Override
	public int stopflgFormInfo(String search, int flg) {
		List<String> stopflgList = Arrays.asList(search.split(","));
		return formInfoDAO.stopflgFormInfo(stopflgList,flg);
	}

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
	@Override
	public int delFormInfo(String search) {
		List<String> delList = Arrays.asList(search.split(","));
		return formInfoDAO.delFormInfo(delList);
	}

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
	@Override
	public Map<String, String> saveFormInfo(OaFormInfo formInfo, ArrayList<KeyValVo> keyValueList) {
		try {
			Map<String,String> retMap = new HashMap<String, String>();
			User user = ShiroSessionUtils.getCurrentUserFromShiroSession();
			Date date = new Date();
			if(StringUtils.isBlank(formInfo.getId())){
				int retNum = formInfoDAO.findFormCode(null,formInfo.getFormCode());
				if(retNum==0){
					formInfo.setId(null);
					formInfo.setFormState(0);
					formInfo.setFormTname("T_OA_FROM_"+formInfo.getFormCode().toUpperCase()+"_"+formInfoDAO.getSequece("SEQ_OA_TABLESQE"));
					formInfo.setFormInfo(Hibernate.createClob(formInfo.getFormInfoStr()));
					formInfo.setCreateUser(user.getAccount());
					SysDepartment dept = ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession();
					formInfo.setCreateDept(dept==null?null:dept.getDeptCode());
					formInfo.setCreateTime(date);
					formInfo.setUpdateUser(user.getAccount());
					formInfo.setUpdateTime(date);
					formInfo.setStop_flg(0);
					formInfo.setDel_flg(0);
					formInfoDAO.createTable(formInfo.getFormTname(),keyValueList);
					formInfoDAO.save(formInfo);
					retMap.put("resCode", "success");
					retMap.put("resMsg", "自定义表单创建成功!");
				}else{
					retMap.put("resCode", "error");
					retMap.put("resMsg", "表单标识已存在,请更换!");
				}
			}else{
				int retNum = formInfoDAO.findFormCode(formInfo.getId(),formInfo.getFormCode());
				if(retNum==0){
					OaFormInfo formInfoOld = formInfoDAO.get(formInfo.getId());
					formInfoOld.setFormInfoStr(formInfoOld.getFormInfo()!=null?ClobToString(formInfoOld.getFormInfo()):null);
					if(formInfoOld.getFormState()==0){
						formInfo.setFormTname("T_OA_FROM_"+formInfo.getFormCode().toUpperCase()+"_"+formInfoDAO.getSequece("SEQ_OA_TABLESQE"));
						if(!formInfo.getFormCode().equals(formInfoOld.getFormCode())||!formInfo.getFormInfoStr().equals(formInfoOld.getFormInfoStr())){
							formInfoDAO.createTable(formInfo.getFormTname(),keyValueList);
						}
						formInfoOld.setFormCode(formInfo.getFormCode());
						formInfoOld.setFormName(formInfo.getFormName());
						formInfoOld.setFormMula(formInfo.getFormMula());
						formInfoOld.setFormTname(formInfo.getFormTname());
						formInfoOld.setFormInfo(Hibernate.createClob(formInfo.getFormInfoStr()));
						formInfoOld.setUpdateUser(user.getAccount());
						formInfoOld.setUpdateTime(date);
						formInfoDAO.save(formInfoOld);
						retMap.put("resCode", "success");
						retMap.put("resMsg", "自定义表单修改成功!");
					}else{
						retMap.put("resCode", "error");
						retMap.put("resMsg", "修改失败，只能修改状态为[未使用]的表单!");
					}
				}else{
					retMap.put("resCode", "error");
					retMap.put("resMsg", "表单标识已存在,请更换!");
				}
			}
			return retMap;
		} catch (Exception e) {
			throw new RuntimeException();
		}
		
	}

	/**  
	 *  
	 * 接口-获取可用表单
	 * @Author：aizhonghua
	 * @CreateDate：2015-7-17 下午18:56:31  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-7-17 下午18:56:31  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public List<KeyValVo> getValidFormInfo() {
		return formInfoDAO.getValidFormInfo();
	}

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
	@Override
	public List<KeyValVo> getPartOfFormInfo(String code) {
		Clob sections = formInfoDAO.getPartOfFormInfo(code);
		if(sections==null){
			return null;
		}
		try {
			InfoVo vo = JSONUtils.fromJson(ClobToString(sections), InfoVo.class);
			ArrayList<KeyValVo> retList = new ArrayList<KeyValVo>();
			KeyValVo keyValVo = null;
			for(SectVo sectVo : vo.getSections()){
				if(sectVo.getFields()!=null&&sectVo.getFields().size()>0){
					for(FielVo fielVo : sectVo.getFields()){
						if(!"label".equals(fielVo.getType()) && !"LabelText".equals(fielVo.getType())){
							keyValVo = new KeyValVo();
							keyValVo.setCode(fielVo.getName());
							keyValVo.setName(fielVo.getName());
							retList.add(keyValVo);
						}
					}
				}
			}
			return retList;
		} catch (Exception e) {
			return null;
		}
	}
	
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
	@Override
	public String ClobToString(Clob clob) throws SQLException, IOException {   
        String reString = "";   
        java.io.Reader is = clob.getCharacterStream();// 得到流   
        BufferedReader br = new BufferedReader(is);   
        String s = br.readLine();   
        StringBuffer sb = new StringBuffer();   
        while (s != null) {  
            sb.append(s);   
            s = br.readLine();   
        }   
        reString = sb.toString();   
        return reString;   
    }

	@Override
	public OaFormInfo getFromByCode(String tableCode) {
		return formInfoDAO.getFromByCode(tableCode);
	} 

}
