<%@ page import="java.util.Date,java.text.DateFormat,org.apache.shiro.SecurityUtils,net.sinodata.security.vo.ShiroUser"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<title>远程获取文件信息列表</title>
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
							<div class="layui-form-mid">方式：</div>
							<div class="layui-input-inline" style="width: 150px;">
								<select id="method" name="method" lay-verify="enabled">
									<option value="">请选择</option>
									<option value="FTP">FTP</option>
									<option value="SFTP">SFTP</option>
									<option value="HADOOP">HADOOP</option>
								</select>
							</div>
							<div class="layui-form-mid" style="margin-left: 30px;">IP：</div>
							<div class="layui-input-inline" style="width: 150px;">
								<input type="text" id="ip" autocomplete="off"
									class="layui-input">
							</div>
							<div class="layui-form-mid" style="margin-left: 30px;">用户名：</div>
							<div class="layui-input-inline" style="width: 150px;">
								<input type="text" id="userName" autocomplete="off"
									class="layui-input">
							</div>
							<div class="layui-form-mid" style="margin-left: 30px;">路径：</div>
							<div class="layui-input-inline" style="width: 150px;">
								<input type="text" id="route" autocomplete="off"
									class="layui-input">
							</div>
							<button class="layui-btn" onclick="return false;"
								data-type="reload" id="searchBtn">查询</button>
							<button class="layui-btn layui-bg-cyan" onclick="reset()">重置</button>
							<button class="layui-btn layui-btn-normal" onclick="add()"
								style="float: right">新增</button>
						</div>
					</div>

					<table class="layui-table" lay-filter="dataTable"
						lay-data="{url:'${ctx}/remoteUploadFileInfo/list', method:'post',page:true,
							limit:10,limits:[10,20,30],id:'dataTable',loading:true}">
						<thead>
							<tr>
								<th lay-data="{type:'numbers',width:'5%'}">序号</th>
								<th lay-data="{field:'id',hide:true}">ID</th>
								<th lay-data="{field:'method',align:'center',width:'8%'}">方式</th>
								<th lay-data="{field:'ip',align:'center',width:'12%'}">IP</th>
								<th lay-data="{field:'port',align:'center',width:'8%'}">端口</th>
								<th lay-data="{field:'userName',align:'center',width:'12%'}">用户名</th>
								<th lay-data="{field:'password',align:'center',width:'13%'}">密码</th>
								<th lay-data="{field:'route',align:'center',width:'20%'}">路径</th>
								<th lay-data="{field:'createTime',align:'center',width:'13%'}">创建时间</th>
								<th lay-data="{field:'updateTime',align:'center',width:'13%'}">修改时间</th>
								<th lay-data="{field:'right',align:'center',toolbar:'#toolBar',fixed:'right',width:'13%'}">操作</th>
							</tr>
						</thead>
					</table>
				</div>
			</div>
		</div>
	</div>
	<script src="${ctx}/static/js/jquery.min.js"></script>
	<script src="${ctx}/static/layui/layui.all.js"></script>

	<script type="text/html" id="toolBar">
  		<a class="layui-btn layui-btn-cz" lay-event="edit">编辑</a>
  		<a class="layui-btn layui-btn-danger layui-btn-cz" lay-event="del">删除</a>
	</script>
	<script>
		layui.use('table', function() {
			var table = layui.table;
			var $ = layui.$, active = {
				//表格刷新
				reload : function() {
					table.reload('dataTable', {
						page : {
							curr : 1
						},
						method : 'post',
						where : {
							method : $('#method').val(),
							ip : $('#ip').val(),
							userName : $('#userName').val(),
							route : $('#route').val()
						}
					});
				}
			};
			$('#searchBtn').on('click', function() {
				var type = $(this).data('type');
				active[type] ? active[type].call(this) : '';
			});

			//表格工具栏按钮触发事件
			table.on('tool(dataTable)', function(obj) {
				var data = obj.data;
				if (obj.event === 'del') {
					layer.confirm('删除后将无法恢复,确定要删除？', function(index) {
						$.ajax({
							type : "POST",
							url : '${ctx}/remoteUploadFileInfo/deleteById',
							async : false,
							data : {
								id : data.id
							},
							success : function(data) {
							}
						});
						layer.close(index);
						$("#searchBtn").click();
					});
				} else if (obj.event === 'edit') {
					layer.open({
						type : 2,
						title : '编辑',
						area : [ '450px', '450px' ],
						shade : 0.8,
						closeBtn : 1,
						shadeClose : true,
						content : '${ctx}/remoteUploadFileInfo/editPage?type=edit&id=' + data.id
					});
				}
			});
		});

		//添加
		function add() {
			layer.open({
				type : 2,
				title : '新增',
				area : [ '450px', '450px' ],
				shade : 0.8,
				closeBtn : 1,
				shadeClose : true,
				content : '${ctx}/remoteUploadFileInfo/editPage?type=add'
			});
		}

		//重置
		function reset() {
			$("input[type='text']").each(function() {
				$(this).val("");
			});
			$("select").each(function() {
				$(this).val("");
			});
			$("#searchBtn").click();
		}

		//提交成功提示
		function submitSuccess() {
			$("#searchBtn").click();
			layer.msg('提交成功！', {
				icon : 1,
				time : 2000
			});
		}

		//提交失败提示
		function submitFail() {
			$("#searchBtn").click();
			layer.msg('提交失败！', {
				icon : 1,
				time : 2000
			});
		}
	</script>
</body>
</html>