package net.sinodata.business.service;

import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;
import net.sinodata.business.entity.User;

public interface UserService {

	List<User> findUserByLoginName(String userName);

	User findUserByCode(String uniqueCode);

	JSONObject userList(Map<String, Object> map);

	int addUser(User user);

	int deleteUserById(String userId);

	User getUserById(String userId);

	int updateUserById(User user);

	int updateUserPwd(User user);

}