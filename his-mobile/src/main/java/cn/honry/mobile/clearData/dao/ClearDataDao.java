package cn.honry.mobile.clearData.dao;

public interface ClearDataDao {

	void clearSchedule() throws Exception;

	void clearNotepad() throws Exception;

	void clearAdvice() throws Exception;

	void clearTodo() throws Exception;

	/**  
	 * 
	 * 推送通知
	 * @Author: zxl
	 * @CreateDate: 2018年1月12日 下午3:41:48 
	 * @Modifier: zxl
	 * @ModifyDate: 2018年1月12日 下午3:41:48 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	void sendMes()  throws Exception ;
}
