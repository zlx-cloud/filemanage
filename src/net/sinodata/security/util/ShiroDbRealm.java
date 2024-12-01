package net.sinodata.security.util;

import java.util.HashSet;
import java.util.Set;

import net.sinodata.business.entity.User;
import net.sinodata.business.service.UserService;
import net.sinodata.business.util.MD5Utils;
import net.sinodata.business.util.StringUtil;
import net.sinodata.security.vo.ShiroUser;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class ShiroDbRealm extends AuthorizingRealm {

	Logger logger = LoggerFactory.getLogger(ShiroDbRealm.class);

	@Autowired
	protected UserService userService;

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken)
			throws AuthenticationException {

		UsernamePasswordToken token = (UsernamePasswordToken) authcToken;

		String password = String.valueOf(token.getPassword());

		User user = userService.findUserByCode(token.getUsername());
		if (user != null) {
			ShiroUser shiroUser = new ShiroUser(user);
			if (StringUtil.isNotEmpty(password)) {
				if (user.getUserPwd().equals(MD5Utils.getMD5String(password))) {
					return new SimpleAuthenticationInfo(shiroUser, password, getName());
				} else {
					throw new AuthenticationException();
				}
			} else {
				throw new UnknownAccountException();
			}
		} else {
			throw new AuthenticationException();
		}
	}

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		ShiroUser shiroUser = (ShiroUser) principals.getPrimaryPrincipal();
		String uniqueCode = shiroUser.getUniqueCode();
		if ("admin".equals(uniqueCode)) {
			Set<String> roles = new HashSet<String>();
			roles.add("admin");
			// 创建权限对象
			SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
			// 创建角色权限
			info.addRoles(roles);
			return info;
		} else {
			return null;
		}
	}

}