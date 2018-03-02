package cn.honry.oa.workProcessCount.constant;
/**
 * 
 * <p>任务类的业务标记常量类，没别的含义，只是为了判断请求的类型而已，常用的类型是待办，办结，关注，挂起,委托，全部，当然了，你完全可以自定义 </p>
 * @Author: zhangkui
 * @CreateDate: 2017年7月24日 下午2:08:39 
 * @Modifier: zhangkui
 * @ModifyDate: 2017年7月24日 下午2:08:39 
 * @ModifyRmk:  
 * @version: V1.0
 * @throws:
 *
 */

public class WorkProcessCountConstant {
	
	//待办，办结，关注，挂起,委托，全部
	/**
	 * 待办任务标记，业务标记，数据库底层没有该标记，只是为了判断请求的类别
	 */
	public final static String WORK_FLAG_TODO="1";
	
	/**
	 * 办结任务标记，业务标记，数据库底层没有该标记，只是为了判断请求的类别
	 */
	public final static String WORK_FLAG_YEDO="2";
	
	/**
	 * 关注任务标记，业务标记，数据库底层没有该标记，只是为了判断请求的类别
	 */
	public final static String WORK_FLAG_ANDO="3";
	
	/**
	 * 挂起任务标记，业务标记，数据库底层没有该标记，只是为了判断请求的类别
	 */
	public final static String WORK_FLAG_HUDO="4";
	
	/**
	 * 委托任务标记，业务标记，数据库底层没有该标记，只是为了判断请求的类别
	 */
	public final static String WORK_FLAG_ETDO="5";
	
	/**
	 * 全部任务标记，业务标记，数据库底层没有该标记，只是为了判断请求的类别
	 */
	public final static String WORK_FLAG_ALDO="6";
	
	/**
	 * 代表任务,正在"处理中"
	 */
	public final static String STATE_SERVEN="处理中";
	
	/**
	 * 代表任务,正在"进行中"
	 */
	public final static String STATE_EIGHT="进行中";
	
	/**
	 * 代表任务,正在"已办结"
	 */
	public final static String STATE_NINE="已办结";

	/**
	 * 代表(流程实例)任务,正在"已挂起",更侧重于流程实例，因为任务挂起的前提就是对应的流程挂起
	 */
	public final static String STATE_HUP="已挂起";
	
	
	/**
	 * 步骤:流程变量的步骤key：可以从流程变量中取出value
	 */
	public final static String STEP="step";
	
	/**
	 * 流程的发起人,流程变量的步骤key：可以从流程变量中取出value
	 */
	public final static String CREATE_USER="user";
	
	
	
}
