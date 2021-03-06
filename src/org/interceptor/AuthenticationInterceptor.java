package org.interceptor;

import java.util.Map;
import org.been.Userinfo;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;


public class AuthenticationInterceptor implements Interceptor  {

	 private static final long serialVersionUID = 1L;
	 
	 private String message;
	
	 public static final String USER_SESSION_KEY = "UserSessionKey";
	 
	 public void destroy() {}

	 public void init() {}

     public String intercept(ActionInvocation actionInvocation) throws Exception {
		 //取得Session
         Map session = actionInvocation.getInvocationContext().getSession();
		 //从Session里获得登录时保存进session的User类
         Userinfo userinfo = (Userinfo) session.get(USER_SESSION_KEY);
         //如果用户为空，返回登录页面  	 
         if (userinfo==null) {
        	 this.setMessage("您没登陆");
            return Action.INPUT;           
         }        
         return actionInvocation.invoke();//返回验证通过         
    }

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}