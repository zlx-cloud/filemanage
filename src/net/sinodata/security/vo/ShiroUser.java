package net.sinodata.security.vo;

import net.sinodata.business.entity.User;

import java.io.Serializable;

import com.google.common.base.Objects;

public class ShiroUser implements Serializable {

	private static final long serialVersionUID = -2117324715101996906L;
	private String id;
	public String loginName;
	private String uniqueCode;
	private String password;
	private String fwcyfMs;

	public ShiroUser(User User) {
		this.id = User.getUserId();
		this.uniqueCode = User.getUniqueCode();
		this.loginName = User.getUserName();
		this.password = User.getUserPwd();
		this.fwcyfMs = User.getUserDesc();
	}

	/**
	 * 本函数输出将作为默认的<shiro:principal/>输出.
	 */
	@Override
	public String toString() {
		return id + "," + loginName;
	}

	/**
	 * 重载hashCode,只计算userName;
	 */
	@Override
	public int hashCode() {
		return Objects.hashCode(loginName);
	}

	/**
	 * 重载equals,只计算loginName;
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		ShiroUser other = (ShiroUser) obj;
		if (loginName == null) {
			if (other.loginName != null) {
				return false;
			}
		} else if (!loginName.equals(other.loginName)) {
			return false;
		}
		return true;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFwcyfMs() {
		return fwcyfMs;
	}

	public void setFwcyfMs(String fwcyfMs) {
		this.fwcyfMs = fwcyfMs;
	}

	public String getUniqueCode() {
		return uniqueCode;
	}

	public void setUniqueCode(String uniqueCode) {
		this.uniqueCode = uniqueCode;
	}

}