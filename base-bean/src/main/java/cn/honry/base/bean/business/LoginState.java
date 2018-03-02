package cn.honry.base.bean.business;
/**
 * 登录状态
 *
 */
public enum LoginState {
	SUCCESS{public String getName(){return "登录成功!";}},
	NOACCOUNT{public String getName(){return "账号不存在!";}},
	PASSWORDERROR{public String getName(){return "密码错误!";}},
	NULLERROR{public String getName(){return "请输入正确的登录信息!";}},
	SECURITYCODEERROR{public String getName(){return "验证码错误!";}},
	URLERROR{public String getName(){return "访问路径错误!";}};
    public abstract String getName();
}


