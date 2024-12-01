package net.sinodata.business.dao;

import net.sinodata.business.entity.User;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface UserDao {

	// 用户登录查询
	List<User> findByLoginName(@Param("username") String username);

	// 根据用户唯一标志查询用户信息
	User findUserByCode(@Param("uniqueCode") String uniqueCode);

	// ==========用户信息维护==========
	// 用户信息列表
	List<User> userList(Map<String, Object> map);

	// 用户信息个数
	int userCount(Map<String, Object> map);

	// 添加用户
	int addUser(User user);

	// 删除用户
	int deleteUserById(@Param("userId") String userId);

	// 查询用户信息
	User getUserById(@Param("userId") String userId);

	// 编辑用户信息
	int updateUserById(User user);

	// 修改用户密码
	int updateUserPwd(User user);

}