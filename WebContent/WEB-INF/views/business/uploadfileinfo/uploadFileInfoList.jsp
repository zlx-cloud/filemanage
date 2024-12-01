<%@ page import="java.util.Date,java.text.DateFormat,org.apache.shiro.SecurityUtils,net.sinodata.security.vo.ShiroUser"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<title>上传文件信息列表</title>
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
						
							<div class="layui-form-mid">上传用户：</div>
							<div class="layui-input-inline" style="width: 150px;">
								<input type="text" id="userName" autocomplete="off" class="layui-input">
							</div>
							
							<div class="layui-form-mid" style="margin-left: 30px;">文件名：</div>
							<div class="layui-input-inline" style="width: 150px;">
								<input type="text" id="fileName" autocomplete="off" class="layui-input">
							</div>
							
							<div class="layui-form-mid" style="margin-left: 30px;">上传时间：</div>
							<div class="layui-input-inline" style="width: 150px;">
								<input type="text" id="uploadDate" autocomplete="off" class="layui-input">
							</div>

							<button class="layui-btn" onclick="return false;"
								data-type="reload" id="searchBtn">
								查询
							</button>
							<button class="layui-btn layui-bg-cyan" onclick="reset()">重置</button>
							<button class="layui-btn" id="file2" name="file2"><i class="layui-icon"></i>上传二类区</button>
							<button class="layui-btn" id="file3" name="file3"><i class="layui-icon"></i>上传三类区</button>
						</div>
					</div>

					<table class="layui-table" lay-filter="dataTable"
						lay-data="{url:'${ctx}/uploadFileInfo/uploadFileInfoList', method:'post',page:true,
							where : {uploadDate : '${time}'},
							limit:10,limits:[10,20,30],id:'dataTable',loading:true}">
						<thead>
							<tr>
								<th lay-data="{type:'numbers',width:'10%'}">序号</th>
								<th lay-data="{field:'id',hide:true}">文件ID</th>
								<th lay-data="{field:'userName',align:'center',width:'20%'}">上传用户</th>
								<th lay-data="{field:'fileName',align:'center',width:'35%'}">文件名</th>
								<th lay-data="{field:'fileSize',align:'center',width:'15%'}">文件大小</th>
								<th lay-data="{field:'uploadTime',align:'center',width:'20%'}">上传时间</th>
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
		var table,laydate,upload;
		layui.use(['table','laydate','upload'], function() {
			laydate = layui.laydate,table = layui.table,upload = layui.upload;
			
			//常规用法
			laydate.render({
				elem: '#uploadDate',
				format: 'yyyyMMdd',
				trigger: 'click',
				value: '${time}'
			});
			
			//二类区上传
			upload.render({
				elem : '#file2',
				url : '${ctx}/pageUpload/upload',
				accept : 'file',
				data: {
					"networkAreaCode" : "2"
				},
				done : function(res) {
					layer.msg(res.message);
					reloadTable();
				}
			});
			
			//三类区上传
			upload.render({
				elem : '#file3',
				url : '${ctx}/pageUpload/upload',
				accept : 'file',
				data: {
					"networkAreaCode" : "3"
				},
				done : function(res) {
					layer.msg(res.message);
					reloadTable();
				}
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
							userName : $('#userName').val(),
							fileName : $('#fileName').val(),
							uploadDate : $('#uploadDate').val()
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
				url:'${ctx}/uploadFileInfo/uploadFileInfoList',
				page : {
					curr : 1
				},
				method : 'post',
				where : {
					userName : $('#userName').val(),
					fileName : $('#fileName').val(),
					uploadDate : $('#uploadDate').val()
				}
			})
		}
	</script>
</body>
</html>