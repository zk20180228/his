package cn.honry.oa.activiti.bpm.form.vo;


import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import cn.honry.base.bean.model.OaKVProp;
import cn.honry.base.bean.model.OaKVRecord;
import cn.honry.oa.activiti.task.service.TaskDefaultService;
import cn.honry.utils.JSONUtils;

/**
 * 工具类.
 * 用于将数据库中的表单模板数据 解析成在页面能够展示的数据结构
 * @author luyanshou
 *
 */
@SuppressWarnings({"all"})
public class XformBuilder {

	private Xform xform = new Xform();
	private TaskDefaultService taskDefaultService;
	
	public XformBuilder setTaskDefaultService(TaskDefaultService taskDefaultService){
		this.taskDefaultService=taskDefaultService;
		return this;
	}
	
	/**
	 * 设置内容
	 * @param content
	 * @return
	 */
	public XformBuilder setContent(String content){
		 xform.setContent(content);//设置内容
		 handleStructure();//解析内容
		return this;
	}
	
	/**
	 * 设置记录(结果)
	 * @param record
	 * @return
	 */
	public XformBuilder setRecord(OaKVRecord record){
		if(record==null){
			return this;
		}
		Map<String, OaKVProp> props = record.getProps();
		if(props==null){
			return this;
		}
		for(OaKVProp prop : props.values()){
			String name = prop.getCode();
            String value = prop.getValue();
            XformField xformField = xform.findXformField(name);

            if (xformField == null) {
                continue;
            }

            String type = xformField.getType();
            if ("fileupload".equals(type)){//'文件上传'类型
            	//获取上传文件信息如:URL、上传文件名等
            	
//            }else if ("userpicker".equals(type)){//'选择人员'类型
//            	//获取员工数据(员工号和姓名)
//            	if(StringUtils.isBlank(value)){
//            		continue;
//            	}
//            	xformField.setValue(value);
//            	StringBuilder buff = new StringBuilder();
//            	
//            	for (String userId : value.split(",")){
//            		if (StringUtils.isBlank(userId)) {
//                        continue;
//                    }
//            		
//            		String empName = taskDefaultService.findUnique(
//            				"select e.name from SysEmployee e where e.jobNo=? and e.stop_flg=0 and e.del_flg=0", "", userId);
//            		if(StringUtils.isBlank(empName)){
//            			continue;
//            		}
//            		buff.append("empName").append(",");
//            	}
//            	if (buff.length() > 0) {//去掉最后面那个逗号
//                    buff.deleteCharAt(buff.length() - 1);
//                }
//            	xformField.setLabel(buff.toString());
//            	
            }else{//普通的表单类型如;文本框、输入框、单选框、多选框等等
            	xformField.setValue(value);
            }
		}
		
		return this;
	}
	
	
	 public Xform build() {
	        return xform;
	    }
	
	public void handleStructure(){
		String content = xform.getContent();
		if (content == null){
			return;
		}
		try {
			Map map = JSONUtils.fromJson(content, Map.class);
			if(map==null){
				return;
			}
			List<Map> sections = (List<Map>) map.get("sections");
			for (Map section : sections) {
	            if (!"grid".equals(section.get("type"))) {
	                continue;
	            }

	            List<Map> fields = (List<Map>) section.get("fields");

	            for (Map field : fields) {
	                this.handleField(field);
	            }
	        }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void handleField(Map map) {
        XformField xformField = new XformField();
        xformField.setName((String) map.get("name"));
        xformField.setType((String) map.get("type"));
        xform.addXformField(xformField);
    }
}
