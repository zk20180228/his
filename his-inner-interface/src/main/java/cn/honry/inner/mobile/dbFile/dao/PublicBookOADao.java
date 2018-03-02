package cn.honry.inner.mobile.dbFile.dao;

import java.util.List;

import cn.honry.base.bean.model.PublicAddressBook;
import cn.honry.base.dao.EntityDao;

public interface PublicBookOADao extends EntityDao<PublicAddressBook>{
	//查询 T_OA_PUBLIC_BOOK表中所有数据
	List<PublicAddressBook> getAllPublicBookOA();
}
