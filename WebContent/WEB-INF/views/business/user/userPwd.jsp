<%@ page import="java.util.Date,java.text.DateFormat,org.apache.shiro.SecurityUtils,net.sinodata.security.vo.ShiroUser"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<title>用户密码修改</title>
<link rel="stylesheet" type="text/css" href="${ctx}/static/layui/css/layui.css" />
<link rel="stylesheet" type="text/css" href="${ctx}/static/css/common.css" />
</head>
<body>
	<form class="layui-form" action="">
	
		<input type="hidden" id="userId" name="userId" value="${userId}">
		
		<div class="layui-form-item" style="margin-top: 15px;">
			<label class="layui-form-label form-label">新密码：</label>
			<div class="layui-input-block">
				<input type="password" id="pass1" name="userPwd"
					lay-verify="userPwd" autocomplete="off" placeholder="请输入新密码"
					class="layui-input form-option" oncopy='document.selection.empty()'>
			</div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label form-label">确认新密码：</label>
			<div class="layui-input-block">
				<input type="password" id="pass2" name="confirmPassword"
					lay-verify="confirmPassword" autocomplete="off" placeholder="请输入新密码"
					class="layui-input form-option">
			</div>
		</div>
		
		<div class="layui-form-item" style="margin-top: 30px;">
			<div class="layui-input-block">
				<button class="layui-btn" lay-submit lay-filter="submitBtn"
					style="margin-left: 20px;width: 80px;">提交</button>
				<button class="layui-btn layui-btn-primary" onclick="closeLayer()"
					style="margin-left: 50px;width: 80px;">取消</button>
			</div>
		</div>
	</form>
	<script src="${ctx}/static/layui/layui.all.js"></script>
	<script src="${ctx}/static/js/jquery.min.js"></script>
	<script>
		layui.use([ 'layer', 'form' ], function() {
			var form = layui.form;
			var layer = layui.layer;

			form.verify({
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
				}
			});

			form.on('submit(submitBtn)', function(data) {
				$.ajax({
					url : '${ctx}/user/userPwd',
					type : "POST",
					async : false,
					data : {
						userId : data.field.userId,
						userPwd : data.field.userPwd
					},
					dataType : 'JSON',
					success : function(data) {
						if(data==1){
							parent.layer.close(parent.layer.index);
							parent.submitSuccess();
						}else{
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
		
		function closeLayer(){
			parent.layer.close(parent.layer.index);
		}
	</script>
</body>
</html>