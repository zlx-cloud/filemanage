package net.sinodata.business.rest;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import net.sf.json.JSONObject;
import net.sinodata.business.entity.User;
import net.sinodata.business.service.UserService;
import net.sinodata.business.util.DateUtil;
import net.sinodata.business.util.MD5Utils;
import net.sinodata.business.util.StringUtil;

@Controller
@RequestMapping(value = "/user")
public class UserController {

	@Autowired
	UserService userService;

	// 用户信息页面
	@RequestMapping(value = "/userListPage", method = RequestMethod.GET)
	public String userListPage() {
		return "business/user/userList";
	}

	// 用户信息列表
	@RequestMapping(value = "/userList", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public JSONObject userList(@RequestParam Map<String, Object> params) {
		JSONObject result = userService.userList(params);
		return result;
	}

	// 用户信息维护页面
	@RequestMapping(value = "/userEditPage", method = RequestMethod.GET)
	public String userEditPage(HttpServletRequest request) {
		String userId = request.getParameter("userId");
		if (!StringUtil.isEmpty(userId)) {
			User user = userService.getUserById(userId);
			request.setAttribute("user", user);
		}
		String type = request.getParameter("type");
		request.setAttribute("type", type);
		return "business/user/userEdit";
	}

	// 查询用户是否存在
	@RequestMapping(value = "/findUserByName", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public boolean findUserByName(@RequestParam(value = "userName") String userName) {
		List<User> userList = userService.findUserByLoginName(userName);
		if (userList.size() == 0) {
			return true;
		} else {
			return false;
		}
	}
	
	// 查询用户是否存在
	@RequestMapping(value = "/findUserByCode", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public boolean findUserByCode(@RequestParam(value = "uniqueCode") String uniqueCode) {
		User user = userService.findUserByCode(uniqueCode);
		if (null == user) {
			return true;
		}else {
			return false;
		}
	}

	// 用户信息维护
	@RequestMapping(value = "/userEdit", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String userEdit(User user) {
		int result = 0;
		String type = user.getType();
		if ("add".equals(type)) {
			user.setUserPwd(MD5Utils.getMD5String(user.getUserPwd()));
			user.setUserId(StringUtil.createUUID());
			user.setCreateTime(DateUtil.getNowDate("yyyy-MM-dd"));
			result = userService.addUser(user);
		} else if ("edit".equals(type)) {
			result = userService.updateUserById(user);
		}
		return result + "";
	}

	// 用户信息删除
	@RequestMapping(value = "/deleteUserById", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public void deleteUserById(@RequestParam(value = "userId") String userId) {
		userService.deleteUserById(userId);
	}

	// 用户密码修改页面
	@RequestMapping(value = "/userPwdPage", method = RequestMethod.GET)
	public String userPwdPage(HttpServletRequest request) {
		String userId = request.getParameter("userId");
		request.setAttribute("userId", userId);
		return "business/user/userPwd";
	}

	// 用户密码修改
	@RequestMapping(value = "/userPwd", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String userPwd(User user) {
		user.setUserPwd(MD5Utils.getMD5String(user.getUserPwd()));
		int result = userService.updateUserPwd(user);
		return result + "";
	}

}