<%@ page
	import="java.util.Date,java.text.DateFormat,org.apache.shiro.SecurityUtils,net.sinodata.security.vo.ShiroUser"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<title>编辑</title>
<link rel="stylesheet" type="text/css"
	href="${ctx}/static/layui/css/layui.css" />
<link rel="stylesheet" type="text/css"
	href="${ctx}/static/css/common.css" />
<style>
.layui-unselect {
	width: 87% !important;
}
</style>
</head>
<body>
	<form class="layui-form" action="">

		<input type="hidden" id="type" name="type" value="${type}">
		<input type="hidden" id="id" name="id" value="${file.id}">

		<div class="layui-form-item" style="margin-top: 15px;">
			<label class="layui-form-label form-label">方式：</label>
			<div class="layui-input-block" style="width: 70%;">
				<select id="method" name="method" lay-verify="method">
					<option value="">请选择</option>
					<option value="FTP">FTP</option>
					<option value="SFTP">SFTP</option>
					<option value="HADOOP">HADOOP</option>
				</select>
			</div>
		</div>

		<div class="layui-form-item" style="margin-top: 15px;">
			<label class="layui-form-label form-label">IP：</label>
			<div class="layui-input-block">
				<input type="text" id="ip" name="ip" lay-verify="ip"
					autocomplete="off" placeholder="请输入IP"
					class="layui-input form-option" value="${file.ip}">
			</div>
		</div>

		<div class="layui-form-item" style="margin-top: 15px;">
			<label class="layui-form-label form-label">端口：</label>
			<div class="layui-input-block">
				<input type="text" id="port" name="port" lay-verify="port"
					autocomplete="off" placeholder="请输入端口"
					class="layui-input form-option" value="${file.port}">
			</div>
		</div>

		<div class="layui-form-item" style="margin-top: 15px;">
			<label class="layui-form-label form-label">用户名：</label>
			<div class="layui-input-block">
				<input type="text" id="port" name="userName" lay-verify="userName"
					autocomplete="off" placeholder="请输入用户名"
					class="layui-input form-option" value="${file.userName}">
			</div>
		</div>

		<div class="layui-form-item" style="margin-top: 15px;">
			<label class="layui-form-label form-label">密码：</label>
			<div class="layui-input-block">
				<input type="text" id="port" name="password" lay-verify="password"
					autocomplete="off" placeholder="请输入密码"
					class="layui-input form-option" value="${file.password}">
			</div>
		</div>

		<div class="layui-form-item">
			<label class="layui-form-label form-label">路径：</label>
			<div class="layui-input-block">
				<input type="text" id="route" name="route" lay-verify="route"
					autocomplete="off" placeholder="请输入路径"
					class="layui-input form-option" value="${file.route}">
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

			if ('${type}' == 'edit') {
				$("#method").val('${file.method}');
				form.render('select');
			}

			//表单校验
			form.verify({
				method : function(value) {
					if (value == '') {
						return '请选择方式';
					}
				},
				ip : function(value) {
					if (value == '') {
						return 'IP不能为空';
					}
				},
				port : function(value) {
					if (value == '') {
						return '端口不能为空';
					}
				},
				userName : function(value) {
					if (value == '') {
						return '用户名不能为空';
					}
				},
				password : function(value) {
					if (value == '') {
						return '密码不能为空';
					}
				},
				route : function(value) {
					if (value == '') {
						return '路径不能为空';
					}
				}
			});

			//表单提交
			form.on('submit(submitBtn)', function(data) {
				$.ajax({
					url : '${ctx}/remoteGetFileInfo/edit',
					type : "POST",
					async : false,
					data : {
						id : data.field.id,
						method : data.field.method,
						ip : data.field.ip,
						port : data.field.port,
						userName : data.field.userName,
						password : data.field.password,
						route : data.field.route,
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