<%@ page import="java.util.Date,java.text.DateFormat,org.apache.shiro.SecurityUtils,net.sinodata.security.vo.ShiroUser"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<title>用户信息列表</title>
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
						
							<div class="layui-form-mid">用户标识：</div>
							<div class="layui-input-inline" style="width: 150px;">
								<input type="text" id="uniqueCode" autocomplete="off" class="layui-input">
							</div>
							
							<div class="layui-form-mid" style="margin-left: 30px;">用户名：</div>
							<div class="layui-input-inline" style="width: 150px;">
								<input type="text" id="userName" autocomplete="off" class="layui-input">
							</div>

							<button class="layui-btn" onclick="return false;"
								data-type="reload" id="searchBtn">
								查询
							</button>
							<button class="layui-btn layui-bg-cyan" onclick="reset()">重置</button>
							<button class="layui-btn layui-btn-normal" onclick="add()" style="float: right">
								新增
							</button>
						</div>
					</div>

					<table class="layui-table" lay-filter="dataTable"
						lay-data="{url:'${ctx}/user/userList', method:'post',page:true,
							limit:10,limits:[10,20,30],id:'dataTable',loading:true}">
						<thead>
							<tr>
								<th lay-data="{type:'numbers',width:'10%'}">序号</th>
								<th lay-data="{field:'userId',hide:true}">用户ID</th>
								<th lay-data="{field:'uniqueCode',align:'center'}">用户标识</th>
								<th lay-data="{field:'userName',align:'center'}">用户名称</th>
								<th lay-data="{field:'userDesc',align:'center'}">用户描述</th>
								<th lay-data="{field:'createTime',align:'center'}">创建时间</th>
								<th lay-data="{field:'enabled',align:'center',templet: function (d) {
									if(d.enabled=='Y'){
										return '可用';
									}else if(d.enabled=='N'){
										return '禁用';
									}else{
										return '';
									}
								}}">状态</th>
								<th lay-data="{field:'right',align:'center',toolbar:'#toolBar',width:'18%',fixed:'right'}">操作</th>
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
  		<a class="layui-btn layui-btn-cz" lay-event="editUser">编辑</a>
  		<a class="layui-btn layui-btn-danger layui-btn-cz" lay-event="del">删除</a>
		<a class="layui-btn layui-btn-warm layui-btn-cz editPwd-btn" lay-event="editPwd">修改密码</a>
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
							userName : $('#userName').val(),
							uniqueCode : $('#uniqueCode').val()
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
							url : '${ctx}/user/deleteUserById',
							async : false,
							data : {
								userId : data.userId
							},
							success : function(data) {
							}
						});
						layer.close(index);
						$("#searchBtn").click();
					});
				} else if (obj.event === 'editUser') {
					layer.open({
						type : 2,
						title : '编辑用户',
						area : [ '450px', '300px' ],
						shade : 0.8,
						closeBtn : 1,
						shadeClose : true,
						content : '${ctx}/user/userEditPage?type=edit&userId=' + data.userId
					});
				} else if (obj.event === 'editPwd') {
					layer.open({
						type : 2,
						title : '修改密码',
						area : [ '450px', '250px' ],
						shade : 0.8,
						closeBtn : 1,
						shadeClose : true,
						content : '${ctx}/user/userPwdPage?userId=' + data.userId
					});
				}
			});
		});

		//添加
		function add() {
			layer.open({
				type : 2,
				title : '添加用户',
				area : [ '450px', '450px' ],
				shade : 0.8,
				closeBtn : 1,
				shadeClose : true,
				content : '${ctx}/user/userEditPage?type=add'
			});
		}

		//重置
		function reset() {
			$("input[type='text']").each(function() {
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