package cn.honry.oa.activiti.bpm.form.vo;

/**
 * 用于在页面显示的表单字段
 * @author luyanshou
 *
 */
public class XformField {
    private String name;
    private String type;
    private Object value;
    private String contentType;
    private String label;

    private boolean required;//是否必填 true代表必填
    private boolean readonly;//是否只读 true代表只读
    private boolean display ;//是否显示 true代表显示
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

	public boolean isReadonly() {
		return readonly;
	}

	public void setReadonly(boolean readonly) {
		this.readonly = readonly;
	}

	public boolean isDisplay() {
		return display;
	}

	public void setDisplay(boolean display) {
		this.display = display;
	}

    
}
