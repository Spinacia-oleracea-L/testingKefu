package com.xnx3.j2ee.controller;

import java.awt.Font;
import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xnx3.StringUtil;
import com.xnx3.j2ee.Func;
import com.xnx3.j2ee.entity.User;
import com.xnx3.j2ee.service.SqlService;
import com.xnx3.j2ee.service.UserService;
import com.xnx3.j2ee.shiro.ShiroFunc;
import com.xnx3.j2ee.util.ActionLogUtil;
import com.xnx3.j2ee.util.ApplicationPropertiesUtil;
import com.xnx3.j2ee.util.SystemUtil;
import com.xnx3.j2ee.vo.BaseVO;
import com.xnx3.j2ee.vo.LoginVO;
import com.xnx3.kefu.core.Global;
import com.xnx3.kefu.core.entity.Kefu;
import com.xnx3.kefu.core.util.SessionUtil;

/**
 * 登录、注册
 * @author 管雷鸣
 */
@Controller(value="WMLoginController")
@RequestMapping("/")
public class LoginController extends BaseController {
	@Resource
	private UserService userService;
	@Resource
	private SqlService sqlService;
	

	/**
	 * 验证码图片显示，直接访问此地址可查看图片
	 */
	@RequestMapping("/captcha${url.suffix}")
	public void captcha(HttpServletRequest request,HttpServletResponse response) throws IOException{
		ActionLogUtil.insert(request, "获取验证码显示");

		com.xnx3.media.CaptchaUtil captchaUtil = new com.xnx3.media.CaptchaUtil();
		captchaUtil.setCodeCount(5);								//验证码的数量，若不增加图宽度的话，只能是1～5个之间
		captchaUtil.setFont(new Font("Fixedsys", Font.BOLD, 21));	//验证码字符串的字体
		captchaUtil.setHeight(18);									//验证码图片的高度
		captchaUtil.setWidth(110);									//验证码图片的宽度
//		captchaUtil.setCode(new String[]{"我","是","验","证","码"});	//如果对于数字＋英文不满意，可以自定义验证码的文字！
		com.xnx3.j2ee.util.CaptchaUtil.showImage(captchaUtil, request, response);
	}
	

	/**
	 * 登录
	 */
	@RequestMapping("login${url.suffix}")
	public String login(HttpServletRequest request){
		if(getUser() != null){
			ActionLogUtil.insert(request, "进入登录页面", "已经登录成功，无需再登录，进行跳转");
			return redirect(getRedirectByUserRole(getUser(), request));
		}
		
		ActionLogUtil.insert(request, "进入登录页面");
//		return redirect("wm/login/login.jsp");
		return "wm/login/kefuLogin";
	}
	
	/**
	 * 根据当前用户权限不同，跳转到不同的页面
	 * @param user
	 * @param request
	 * @return
	 */
	private String getRedirectByUserRole(User user, HttpServletRequest request) {
		if(Func.isAuthorityBySpecific(user.getAuthority(), Global.ROLE_ID_SUPERADMIN+"")) {
			//总管理后台
			return "admin/index/index.do";
		}else if(Func.isAuthorityBySpecific(user.getAuthority(), Global.ROLE_ID_ADMIN+"")) {
			//客服后台
			return "kefu/admin/index/index.jsp?token="+request.getSession().getId();
		}else if(Func.isAuthorityBySpecific(user.getAuthority(), Global.ROLE_ID_ZUOXI+"")) {
			//坐席后台
			return "admin/index/index.do";
		}
		
		return "/500.do";
	}
	
	
	/**
	 * 登陆请求验证
	 * @param username 登录的用户名或邮箱 <required> <example=admin>
	 * @param password 登录密码 <required> <example=admin>
	 * @param code 图片验证码的字符 <example=1234> <required>
	 * @return 登录结果
	 */
	@RequestMapping(value="login.json", method = RequestMethod.POST)
	@ResponseBody
	public LoginVO loginSubmit(HttpServletRequest request,Model model){
		LoginVO vo = new LoginVO();
		String wmLogin = ApplicationPropertiesUtil.getProperty("wm.login");
		if(wmLogin != null && wmLogin.equalsIgnoreCase("false")) {
			vo.setBaseVO(LoginVO.FAILURE, "此接口不允许被使用！您可修改application.properties中的wm.login=true ，重启项目，即可使用本接口");
			return vo;
		}
		
		//验证码校验
		BaseVO capVO = com.xnx3.j2ee.util.CaptchaUtil.compare(request.getParameter("code"), request);
		if(capVO.getResult() == BaseVO.FAILURE){
			ActionLogUtil.insert(request, "用户名密码模式登录失败", "验证码出错，提交的验证码："+StringUtil.filterXss(request.getParameter("code")));
			vo.setBaseVO(capVO);
			return vo;
		}else{
			//验证码校验通过
			
			BaseVO baseVO =  userService.loginByUsernameAndPassword(request);
			vo.setBaseVO(baseVO);
			if(baseVO.getResult() == BaseVO.SUCCESS){
				User user = getUser();
				ActionLogUtil.insert(request, "用户名密码模式登录成功");
				
				
				//判断是否拥有客服后台管理权限，也就是user.authority 是否是客服后台的权限
				if(Func.isAuthorityBySpecific(user.getAuthority(), Global.ROLE_ID_ADMIN+"")){
					//客服管理员，缓存客服信息
					Kefu kefu = sqlService.findAloneByProperty(Kefu.class, "userid", getUserId());
					if(kefu == null){
						vo.setBaseVO(BaseVO.FAILURE, "账号未发现kefu信息");
						return vo;
					}
					com.xnx3.kefu.admin.util.SessionUtil.setKefu(kefu);
				}else if(Func.isAuthorityBySpecific(user.getAuthority(), Global.ROLE_ID_ZUOXI+"")) {
					//坐席
//					vo.setBaseVO(BaseVO.FAILURE, "登录失败，您当前登录的是坐席的账号，坐席账号请通过 /login.do 进行登录，并不是这个登录地址。");
//					return vo;
				}else if(Func.isAuthorityBySpecific(user.getAuthority(), Global.ROLE_ID_SUPERADMIN+"")) {
					//超级管理
					
				}
				
				//登录成功后，将当前sessionid进行缓存，以便可以根据sessionid直接取User信息
				com.xnx3.kefu.core.util.SessionUtil.setActiveUserForNotRedis(request.getSession().getId(), SessionUtil.getActiveUser());
				
				//登录成功,BaseVO.info字段将赋予成功后跳转的地址，所以这里要再进行判断
				vo.setToken(request.getSession().getId());
				vo.setUser(user);
				vo.setInfo(getRedirectByUserRole(user, request));
			}else{
				ActionLogUtil.insert(request, "用户名密码模式登录失败",baseVO.getInfo());
			}
			
			return vo;
		}
	}
	

	
	/**
	 * 退出登录状态
	 */
	@RequestMapping(value="logout.json", method = RequestMethod.POST)
	@ResponseBody
	public BaseVO logout(HttpServletRequest request,Model model){
		SessionUtil.logout();
		return success();
	}
	
	/**
	 * 退出登录状态，兼容旧版本
	 */
	@RequestMapping(value="logout.do", method = RequestMethod.POST)
	@ResponseBody
	public BaseVO logout_old(HttpServletRequest request,Model model){
		SessionUtil.logout();
		return success();
	}
	

	/**
	 * 获取token，也就是获取 sessionid
	 * @return info便是sessionid
	 */
	@RequestMapping(value="getToken${api.suffix}", method = RequestMethod.POST)
	@ResponseBody
	public BaseVO getToken(HttpServletRequest request){
		HttpSession session = request.getSession();
		String token = session.getId();
		ActionLogUtil.insert(request, "获取token", token);
		return success(token);
	}
}
