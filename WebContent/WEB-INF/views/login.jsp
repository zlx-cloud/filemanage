<%@ page import="org.apache.shiro.web.filter.authc.FormAuthenticationFilter"%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" type="text/css" href="${ctx}/static/fonts/font-awesome-4.7.0/css/font-awesome.min.css">
<link rel="stylesheet" type="text/css" href="${ctx}/static/css/util.css">
<link rel="stylesheet" type="text/css" href="${ctx}/static/css/main.css">
<title>文件同步</title>
<style>
.errorMsg {
	text-align: center;
	margin-top: 23px;
	height: 0px;
	color: red
}
</style>
</head>
<body>
	<div class="main">
		<div class="container-login">
			<div class="wrap-login">
				<div class="login-pic js-tilt">
					<img src="${ctx}/static/images/left.png" alt="IMG">
				</div>

				<form id="form" name="form" action="${ctx}/login" method="post">
					<span class="login-form-title">文件同步</span>

					<div class="wrap-input validate-input">
						<input type="text" class="input" id="username" name="username" placeholder="用户标识">
						<span class="focus-input"></span>
						<span class="symbol-input">
							<i class="fa fa-user" aria-hidden="true"></i>
						</span>
					</div>

					<div class="wrap-input validate-input">
						<input type="password" class="input" id="password" name="password" placeholder="密 码">
						<span class="focus-input"></span>
						<span class="symbol-input">
							<i class="fa fa-lock" aria-hidden="true"></i>
						</span>
					</div>

					<div class="container-login-form-btn">
						<button class="login-form-btn" id="loginBtn"
							onclick="javascript:document.getElementById('form').submit()">
							登&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;录</button>
					</div>
					<h3 class="errorMsg">${LOGIN_ERROR_MSG}</h3>
				</form>
			</div>
		</div>
	</div>
</body>
</html>