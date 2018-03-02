package cn.honry.base.bean.business;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>需要持久化业务实体的基类, 定义了一个持久化类所共同的属性, 注意系统中的业务实体继承这个类
 * 的相等性, 就是基于标识符判断相等性, 对于没有<code>id</code>属性的业务实体比如具有<code>code</code>
 * 属性的业务实体应该通过覆盖方法
 * <pre>
 *     public String getId() {
 *         return getCode();
 *     }
 * </pre>来保证缺省相等性</p>
 * <p>按照约定系统中所有的业务实体对应的数据库表的列名称和长度<ul>
 * <li>{@link #id}:id,VARCHAR(50)</li>
 * <li>{@link #createUser}:CREATEUSER,VARCHAR(50),向后兼容的名称</li>
 * <li>{@link #updateUser}:UPDATEUSER,VARCHAR(50),向后兼容的名称</li>
 * <li>{@link #createTime}:CREATETIME,timestamp</li>
 * <li>{@link #updateTime}:UPDATETIME,timestamp</li></ul>
 * @version $Id: Entity.
 * @since 2015-5-26
 */
public abstract class Entity implements Serializable{
	/**唯一编号(主键)**/
	private String id;
	/**建立人员**/
	private String createUser;
	/**建立部门**/
	private String createDept;
	/**建立时间**/
	private Date createTime;
	/**修改人员**/
	private String updateUser;
	/**修改时间**/
	private Date updateTime;
	/**删除人员**/
	private String deleteUser;
	/**删除时间**/
	private Date deleteTime;	
	/**停用标志**/
	private Integer stop_flg=0;
	/**删除标志**/
	private Integer del_flg=0;
	
	public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        String oid = ((Entity) obj).getId();
        if (oid == null || oid.trim().length() == 0) {
            return false;
        }
        return oid.equals(getId());
    }

    public int hashCode() {
        if (getId() == null || getId().trim().length() == 0) {
            return super.hashCode();
        } else {
            return getClass().hashCode() * 29 + getId().hashCode();
        }
    }
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCreateUser() {
		return createUser;
	}
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	public String getCreateDept() {
		return createDept;
	}
	public void setCreateDept(String createDept) {
		this.createDept = createDept;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getUpdateUser() {
		return updateUser;
	}
	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public String getDeleteUser() {
		return deleteUser;
	}
	public void setDeleteUser(String deleteUser) {
		this.deleteUser = deleteUser;
	}
	public Date getDeleteTime() {
		return deleteTime;
	}
	public void setDeleteTime(Date deleteTime) {
		this.deleteTime = deleteTime;
	}
	public Integer getStop_flg() {
		return stop_flg;
	}
	public void setStop_flg(Integer stop_flg) {
		this.stop_flg = stop_flg;
	}
	public Integer getDel_flg() {
		return del_flg;
	}
	public void setDel_flg(Integer del_flg) {
		this.del_flg = del_flg;
	}
}