<%@ page import="java.util.Date,java.text.DateFormat,org.apache.shiro.SecurityUtils,net.sinodata.security.vo.ShiroUser"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<title>用户信息编辑</title>
<link rel="stylesheet" type="text/css" href="${ctx}/static/layui/css/layui.css" />
<link rel="stylesheet" type="text/css" href="${ctx}/static/css/common.css" />
<style>
.layui-unselect{
	width: 87%!important;
}
</style>
</head>
<body>
	<form class="layui-form" action="">

		<input type="hidden" id="type" name="type" value="${type}">
		<input type="hidden" id="userId" name="userId" value="${user.userId}">

		<c:if test="${user.userId=='' or user.userId==null}">
		<div class="layui-form-item" style="margin-top: 15px;">
			<label class="layui-form-label form-label">用户标识：</label>
			<div class="layui-input-block">
				<input type="text" id="uniqueCode" name="uniqueCode"
					lay-verify="uniqueCode" autocomplete="off" placeholder="请输入用户名"
					class="layui-input form-option" value="${user.uniqueCode}">
			</div>
		</div>
		</c:if>
		
		<div class="layui-form-item" style="margin-top: 15px;">
			<label class="layui-form-label form-label">用户名：</label>
			<div class="layui-input-block">
				<input type="text" id="userName" name="userName"
					lay-verify="userName" autocomplete="off" placeholder="请输入用户名"
					class="layui-input form-option" value="${user.userName}">
			</div>
		</div>

		<c:if test="${user.userId=='' or user.userId==null}">
			<div class="layui-form-item">
				<label class="layui-form-label form-label">密码：</label>
				<div class="layui-input-block">
					<input type="password" id="pass1" name="userPwd"
						lay-verify="userPassword" autocomplete="off" placeholder="请输入密码"
						class="layui-input form-option"
						oncopy='document.selection.empty()'>
				</div>
			</div>
			<div class="layui-form-item">
				<label class="layui-form-label form-label">确认密码：</label>
				<div class="layui-input-block">
					<input type="password" id="pass2" name="confirmPassword"
						lay-verify="confirmPassword" autocomplete="off"
						placeholder="请输入密码" class="layui-input form-option">
				</div>
			</div>
		</c:if>

    	<div class="layui-form-item">
			<label class="layui-form-label form-label">状态：</label>
			<div class="layui-input-block" style="width: 70%;">
				<select id="enabled" name="enabled" lay-verify="enabled">
					<option value="">请选择</option>
					<option value="Y">可用</option>
					<option value="N">禁用</option>
				</select>
			</div>
		</div>
    		
		<div class="layui-form-item">
			<label class="layui-form-label form-label">描述：</label>
			<div class="layui-input-block">
				<input type="text" name="userDesc" autocomplete="off"
					placeholder="请输入描述" class="layui-input form-option"
					value="${user.userDesc}">
			</div>
		</div>

		<div class="layui-form-item" style="margin-top: 30px;">
			<div class="layui-input-block">
				<button class="layui-btn" lay-submit lay-filter="submitBtn"
					style="margin-left: 20px; width: 80px;">提交</button>
				<button class="layui-btn layui-btn-primary" onclick="closeLayer()"
					style="margin-left: 50px; width: 80px;">取消</button>
			</div>
		</div>
	</form>
	<script src="${ctx}/static/layui/layui.all.js"></script>
	<script src="${ctx}/static/js/jquery.min.js"></script>
	<script>
		layui.use([ 'layer', 'form' ], function() {
			var form = layui.form;
			var layer = layui.layer;

			if('${type}'=='edit'){
				$("#enabled").val('${user.enabled}');
				form.render('select');
			}
			
			//表单校验
			form.verify({
				uniqueCode : function(value) {
					if (value == '') {
						return '用户标识不能为空';
					}

					if (value != '${user.uniqueCode}') {
						var message = "";
						$.ajax({
							type : "POST",
							url : '${ctx}/user/findUserByCode',
							async : false,
							data : {
								uniqueCode : value
							},
							dataType : 'json',
							success : function(data) {
								if (data) {
									message = "";
								} else {
									$("#uniqueCode").val('');
									message = "用户标识已存在，请重新输入！"
								}
							}
						});
						if (message != "") {
							return message;
						}
					}
				},
				userName : function(value) {
					if (value == '') {
						return '用户名不能为空';
					}

					if (value != '${user.userName}') {
						var message = "";
						$.ajax({
							type : "POST",
							url : '${ctx}/user/findUserByName',
							async : false,
							data : {
								userName : value
							},
							dataType : 'json',
							success : function(data) {
								if (data) {
									message = "";
								} else {
									$("#userName").val('');
									message = "用户名已存在，请重新输入！"
								}
							}
						});
						if (message != "") {
							return message;
						}
					}
				},
				userPwd : function(value) {
					if (value == '') {
						return '密码不能为空';
					}
				},
				confirmPassword : function(value) {
					if (value == '') {
						return '确认密码不能为空';
					}
					if (value != $("#pass1").val()) {
						$("#pass2").val('');
						return '密码不一致';
					}
				},
				enabled : function(value) {
					if (value == '') {
						return '请选择用户状态';
					}
				}
			});

			//表单提交
			form.on('submit(submitBtn)', function(data) {
				$.ajax({
					url : '${ctx}/user/userEdit',
					type : "POST",
					async : false,
					data : {
						userId : data.field.userId,
						uniqueCode : data.field.uniqueCode,
						userName : data.field.userName,
						userPwd : data.field.userPwd,
						enabled : data.field.enabled,
						userDesc : data.field.userDesc,
						type : data.field.type
					},
					dataType : 'JSON',
					success : function(data) {
						if (data == 1) {
							parent.layer.close(parent.layer.index);
							parent.submitSuccess();
						} else {
							parent.layer.close(parent.layer.index);
							parent.submitFail();
						}
					},
					error : function(data) {
					}
				});
			});
			return false;
		});

		//关闭弹出层
		function closeLayer() {
			parent.layer.close(parent.layer.index);
		}
	</script>
</body>
</html>