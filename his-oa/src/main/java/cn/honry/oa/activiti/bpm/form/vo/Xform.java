package cn.honry.oa.activiti.bpm.form.vo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import cn.honry.utils.JSONUtils;

/**
 * 用于解析数据库中存的表单模板数据,在页面展示
 * @author luyanshou
 *
 */
public class Xform {
    private String content;//表单内容
    
    private Map<String, XformField> fieldMap = new HashMap<String, XformField>();//表单中的字段集合

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Map<String, XformField> getFieldMap() {
        return fieldMap;
    }

    public void addXformField(XformField xformField) {
        fieldMap.put(xformField.getName(), xformField);
    }

    public XformField findXformField(String name) {
        return fieldMap.get(name);
    }

    public String getJsonData() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();

        for (Map.Entry<String, XformField> entry : fieldMap.entrySet()) {
            XformField xformField = entry.getValue();

            if (xformField.getName() == null) {
                continue;
            }

            if ("fileupload".equals(xformField.getType())) {
                if (xformField.getValue() == null) {
                    continue;
                }

                Map<String, Object> data = new HashMap<String, Object>();

                data.put("key", xformField.getValue());
                data.put("label", xformField.getLabel());
                map.put(xformField.getName(), data);
            } else if ("userpicker".equals(xformField.getType())) {
                if (xformField.getValue() == null) {
                    continue;
                }

                Map<String, Object> data = new HashMap<String, Object>();

                data.put("key", xformField.getValue());
                data.put("label", xformField.getLabel());
                map.put(xformField.getName(), data);
            } else {
                map.put(xformField.getName(), xformField.getValue());
            }
        }

        return JSONUtils.toJson(map);
    }

    public Map<String, Object> getMapData() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();

        for (Map.Entry<String, XformField> entry : fieldMap.entrySet()) {
            XformField xformField = entry.getValue();

            if (xformField.getName() == null) {
                continue;
            }

            if ("userpicker".equals(xformField.getType())) {
                Object value = xformField.getValue();
                String text = "";

                if (value != null) {
                    text = value.toString();
                }

                map.put(xformField.getName(),
                        new ArrayList<String>(Arrays.asList(text.split(","))));
            } else {
                map.put(xformField.getName(), xformField.getValue());
            }
        }

        return map;
    }
}
