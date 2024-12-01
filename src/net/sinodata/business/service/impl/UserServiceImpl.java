package net.sinodata.business.service.impl;

import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;
import net.sinodata.business.dao.UserDao;
import net.sinodata.business.entity.PageVo;
import net.sinodata.business.entity.User;
import net.sinodata.business.service.UserService;
import net.sinodata.business.util.PageUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDao userDao;

	@Override
	public List<User> findUserByLoginName(String userName) {
		return userDao.findByLoginName(userName);
	}

	@Override
	public User findUserByCode(String uniqueCode) {
		return userDao.findUserByCode(uniqueCode);
	}

	@Override
	public JSONObject userList(Map<String, Object> map) {
		PageVo page = new PageVo(Integer.parseInt((String) map.get("page")), Long.parseLong((String) map.get("limit")));
		map.put("start", page.getStart());
		map.put("end", page.getEnd());
		List<User> userList = userDao.userList(map);
		int userCount = userDao.userCount(map);
		JSONObject result = PageUtil.LayuiData(userList, userCount);
		return result;
	}

	@Override
	public int addUser(User user) {
		return userDao.addUser(user);
	}

	@Override
	public int deleteUserById(String userId) {
		return userDao.deleteUserById(userId);
	}

	@Override
	public User getUserById(String userId) {
		return userDao.getUserById(userId);
	}

	@Override
	public int updateUserById(User user) {
		return userDao.updateUserById(user);
	}

	@Override
	public int updateUserPwd(User user) {
		return userDao.updateUserPwd(user);
	}

}