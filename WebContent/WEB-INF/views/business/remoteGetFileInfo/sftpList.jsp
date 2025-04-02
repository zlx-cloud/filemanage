<%@ page import="java.util.Date,java.text.DateFormat,org.apache.shiro.SecurityUtils,net.sinodata.security.vo.ShiroUser"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<title>SFTP文件获取信息</title>
<link rel="stylesheet" type="text/css" href="${ctx}/static/layui/css/layui.css" />
<link rel="stylesheet" type="text/css" href="${ctx}/static/layui/css/view.css" />
<link rel="stylesheet" type="text/css" href="${ctx}/static/css/common.css" />
</head>
<body class="layui-view-body">
	<div class="layui-content">
		<div class="layui-card">
			<div class="layui-card-body">
				<div class="form-box">
					<div class="layui-form layui-form-item">
						<div class="layui-inline" style="width: 100%;">
						
							<div class="layui-form-mid" style="margin-left: 30px;">原文件名：</div>
							<div class="layui-input-inline" style="width: 150px;">
								<input type="text" id="fileName" autocomplete="off" class="layui-input">
							</div>
							
							<div class="layui-form-mid" style="margin-left: 30px;">获取时间：</div>
							<div class="layui-input-inline" style="width: 150px;">
								<input type="text" id="getDate" autocomplete="off" class="layui-input">
							</div>

							<button class="layui-btn" onclick="return false;" data-type="reload" id="searchBtn"
								style="margin-left: 50px;">
								查询
							</button>
							<button class="layui-btn layui-bg-cyan" onclick="reset()">重置</button>
						</div>
					</div>

					<table class="layui-table" lay-filter="dataTable"
						lay-data="{url:'${ctx}/remoteGetFileInfo/sftpList', method:'post',page:true,
							limit:10,limits:[10,20,30],id:'dataTable',loading:true}">
						<thead>
							<tr>
								<th lay-data="{type:'numbers',width:'7%'}">序号</th>
								<th lay-data="{field:'userName',align:'center',width:'10%'}">关联用户</th>
								<th lay-data="{field:'getTime',align:'center',width:'13%'}">获取时间</th>
								<th lay-data="{field:'fileName',align:'center',width:'15%'}">原文件名</th>
								<th lay-data="{field:'targetFileName',align:'center',width:'25%'}">文件名</th>
								<th lay-data="{field:'fileSize',align:'center',width:'10%'}">文件大小</th>
								<th lay-data="{field:'filePath',align:'center',width:'30%'}">文件存放路径</th>
								<th lay-data="{field:'route',align:'center',width:'30%'}">远程文件路径</th>
								<th lay-data="{field:'serviceName',align:'center',width:'15%'}">远程文件地址名称</th>
							</tr>
						</thead>
					</table>
				</div>
			</div>
		</div>
	</div>
	<script src="${ctx}/static/js/jquery.min.js"></script>
	<script src="${ctx}/static/layui/layui.all.js"></script>
	<script>
		var table,laydate;
		layui.use(['table','laydate',], function() {
			laydate = layui.laydate,table = layui.table;
			
			//常规用法
			laydate.render({
				elem: '#getDate',
				format: 'yyyyMMdd',
				trigger: 'click'
			});
			
			var $ = layui.$, active = {
				//表格刷新
				reload : function() {
					table.reload('dataTable', {
						page : {
							curr : 1
						},
						method : 'post',
						where : {
							fileName : $('#fileName').val(),
							getDate : $('#getDate').val()
						}
					});
				}
			};
			$('#searchBtn').on('click', function() {
				var type = $(this).data('type');
				active[type] ? active[type].call(this) : '';
			});
		});

		//重置
		function reset() {
			$("input[type='text']").each(function() {
				$(this).val("");
			});
			$("#searchBtn").click();
		}
		
		//页面刷新
		function reloadTable(){
			table.reload('dataTable', {
				url:'${ctx}/remoteGetFileInfo/sftpList',
				page : {
					curr : 1
				},
				method : 'post',
				where : {
					fileName : $('#fileName').val(),
					getDate : $('#getDate').val()
				}
			})
		}
	</script>
</body>
</html>