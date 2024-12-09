<%@ page import="java.util.Date,java.text.DateFormat"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<title>文件同步</title>
<link rel="stylesheet" href="${ctx}/static/layui/css/layui.css">
</head>
<body class="layui-layout-body">
	<div class="layui-layout layui-layout-admin">
		<div class="layui-header">
			<div class="layui-logo">文件同步</div>
			<ul class="layui-nav layui-layout-right">
				<li class="layui-nav-item">
					<a href="javascript:;" style="font-size: 15px;"> 欢迎你，
					<shiro:principal property="loginName"></shiro:principal>
				</a></li>
				<li class="layui-nav-item">
					<a class="layui-icon layui-icon-logout" 
						style="font-size: 25px; color: #ffffff;" href="${ctx}/logout"
						title="退出">
					</a>
				</li>
			</ul>
		</div>

		<div class="layui-side layui-bg-black">
			<div class="layui-side-scroll">
				<ul class="layui-nav layui-nav-tree" lay-filter="test">
					<li class="layui-nav-item">
						<a href="javascript:;">
							<i class="layui-icon layui-icon-upload-drag"></i> &nbsp;文件上传
						</a>
						<dl class="layui-nav-child layui-show">
							<dd class="layui-this">
								<a href="${ctx}/uploadFileInfo/uploadFileInfoListPage" target="iframeMain">上传文件信息
								</a>
							</dd>
						</dl>
					</li>
					<li class="layui-nav-item">
						<a href="javascript:;">
							<i class="layui-icon layui-icon-download-circle"></i> &nbsp;文件下载
						</a>
						<dl class="layui-nav-child">
							<dd>
								<a href="${ctx}/notDownloadFileInfo/notDownloadFileInfoListPage" target="iframeMain">待下载文件信息</a>
							</dd>
							<dd>
								<a href="${ctx}/fileDownloadLog/fileDownloadLogListPage" target="iframeMain">文件下载日志</a>
							</dd>
						</dl>
					</li>
					<li class="layui-nav-item">
						<a href="${ctx}/remoteGetFileInfo/listPage" target="iframeMain">
							<i class="layui-icon layui-icon-file"></i>&nbsp;远程获取文件信息
						</a>
					</li>
					<li class="layui-nav-item">
						<a href="${ctx}/remoteUploadFileInfo/listPage" target="iframeMain">
							<i class="layui-icon layui-icon-file"></i>&nbsp;远程上传文件信息
						</a>
					</li>
					<shiro:hasRole name="admin">
						<li class="layui-nav-item">
							<a href="${ctx}/user/userListPage" target="iframeMain">
							<i class="layui-icon layui-icon-user"></i>&nbsp;用户管理
						</a></li>
					</shiro:hasRole>
				</ul>
			</div>
		</div>
		<div class="layui-body" style="bottom: 0px;">
			<!-- 内容主体区域 -->
			<div class="layadmin-tabsbody-item layui-show">
				<iframe frameborder="0" class="layadmin-iframe" name="iframeMain"
					src="${ctx}/uploadFileInfo/uploadFileInfoListPage"
					style="width: 100%; height: 562px;" seamless></iframe>
			</div>
		</div>
	</div>
	<script src="${ctx}/static/layui/layui.js"></script>
	<script>
		//JavaScript代码区域
		layui.use('element', function() {
			var element = layui.element;
		});
	</script>
</body>
</html>