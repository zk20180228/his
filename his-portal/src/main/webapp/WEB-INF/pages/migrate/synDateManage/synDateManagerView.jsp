<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>同步数据添加</title>
<%@ include file="/common/metas.jsp"%>
</head>
<body>
<div id="divLayout" class="easyui-layout" data-options="fit:true">
		<div data-options="region:'center',border:false" style="width: 100%;height: 100%;padding:5px" align="center">
			<table class="honry-table"  cellpadding="1" cellspacing="1"	border="1px solid black" style="width:100%;padding:5px">
				<tr>
					<td class="honry-lable">
						同步代码：
					</td>
					<td >
						${dataSynch.code}
					</td>
					<td class="honry-lable">
						相关表：
					</td>
					<td >
						${dataSynch.tableName}
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						表名：
					</td>
					<td>
						${dataSynch.tableZhName}
					</td>
					<td class="honry-lable">
						所属用户：
					</td>
					<td>
						${dataSynch.tableFromUser}
					</td>
					
				</tr>
				<tr style="height:100px;">
					<td class="honry-lable">
						所在视图：
					</td>
					<td>
						<div style="width:300px;height:80px;">
							<textarea style="width:300px;height:80px;" readonly="readonly">${dataSynch.viewName}
							</textarea>
						</div>
					</td>
					<td class="honry-lable">
						视图名称：
					</td>
					<td>
						<div style="width:300px;height:80px;">
							<textarea style="width:300px;height:80px;" readonly="readonly">${dataSynch.viewZhName}
						</textarea>
						</div>
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
							同步方式：
					</td>
					<td id="synchSign">
					
					</td>
					<td class="honry-lable">
						增量字段：
					</td>
					<td>
						${dataSynch.synchCond}
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						开启线程数：
					</td>
					<td colspan="3">
						${dataSynch.threadNum}
					</td>
					
				</tr>
				<tr>
					<td class="honry-lable">
							同步间隔：
					</td>
					<td>
						${dataSynch.timeSpace }
						<span id="timeUnit"></span>
					</td>
						　　
					<td  class="honry-lable">
						同步时长：
					</td>
					<td>
						${ dataSynch.synchLength}
						<span id="synchUnit"></span>
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						schema(账户)：
					</td>
					<td >
						${dataSynch.schema}
					</td>
					<td class="honry-lable">
						分区字段：
					</td>
					<td >
						${dataSynch.tablePartition}
					</td>
				</tr>
				<tr>
					<td class="honry-lable" >
						查询字段：
					</td>
					<td>
						<div style="width:300px;height:80px;">
							<textarea style="width:300px;height:80px;" readonly="readonly">${dataSynch.queryField}
							</textarea>
						</div>
					</td>
					<td class="honry-lable">
						查询条件：
					</td>
					<td>
						${dataSynch.queryCond}
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						分组字段：
					</td>
					<td>
						${dataSynch.groupFiled}
					</td>
					<td class="honry-lable">
						排序字段：
					</td>
					<td>
						${dataSynch.orderFiled}
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						视图排序：
					</td>
					<td >
						${dataSynch.viewOrder}
					</td>
					<td class="honry-lable">
						排序条件：
					</td>
					<td>
						${dataSynch.orderCond}
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						表排序：
					</td>
					<td >
						${dataSynch.tableOrder}
					</td>
					<td class="honry-lable">
						主键：
					</td>
					<td>
						${dataSynch.primaryColum}
						
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						执行服务分类(主)：
					</td>
					<td id="serveCode"></td>
					<td class="honry-lable">
					        执行服务分类(备)：
					</td>
					<td id="serveCodeprepare"></td>
				</tr>
				<tr>
					<td  class="honry-lable">
						默认同步时间：
					</td>
					<td>
						${dataSynch.defaTimeStr } 
					</td>
					<td  class="honry-lable">
						最新同步时间：
					</td>
					<td>
						${dataSynch.newesTimeStr }
					</td>
				</tr>
				<tr>
					<td  class="honry-lable">
						状态：
					</td>
					<td id="state">
						
					</td>
					<td class="honry-lable">
							是否业务关联：
						</td>
						<td id="workSign"></td>
				</tr>
				<tr>
					<td class="honry-lable">
						备注：
					</td>
					<td colspan="3">
						<div style="width:600px;height:80px;">
							<textarea style="width:600px;height:80px;" readonly="readonly">${dataSynch.remarks }</textarea>
						</div>
					</td>
				</tr>
			</table>
			<table style="width: 100%;">
				<tr >
					<shiro:hasPermission name="${menuAlias}:function:cancel">
						<a href="javascript:void(0);" onclick="closeLayout();" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" style="text-align: right ;">取&nbsp;消&nbsp;</a>
					</shiro:hasPermission>
				</tr>
			</table>
		</div>
		<div id="proc"></div>
</div>
</body>
<script type="text/javascript">
	var timeMap={'S':'秒','M':'分','H':'时','D':'天','W':'周'};
	var workSignMap={'0':'是','1':'否'};
	var synchSignMap={'0':'增量','1':'全量'};
	var stateMap={'0':'启用','1':'停用'};
	var serverMap;//服务渲染
	$(function(){
		$.ajax({
			url: "<%=basePath%>migrate/outInterfaceManager/renderServer.action",		
			type:'post',
			async:false,
			success: function(date) {					
				serverMap= date;	
			}
		});
		var synchSign='${dataSynch.synchSign}';//同步方式
		if(synchSign=='0'||synchSign!=null&&synchSign!=''){
			$('#synchSign').html(synchSignMap[synchSign]);
		}
		var timeUnit='${dataSynch.timeUnit}';
		if(timeUnit!=null&&timeUnit!=''){
			$('#timeUnit').html(timeMap[timeUnit]);
		}
		var synchUnit='${dataSynch.synchUnit }';
		if(synchUnit!=null&&synchUnit!=''){
			$('#synchUnit').html(timeMap[synchUnit]);
		}
		var serveCode='${dataSynch.serveCode }';
		if(serveCode!=null&&serveCode!=''){
			$('#serveCode').html(serverMap[serveCode]);
		}
		var serveCodeprepare='${dataSynch.serveCodeprepare }';
		if(serveCodeprepare!=null&&serveCodeprepare!=''){
			$('#serveCodeprepare').html(serverMap[serveCodeprepare]);
		}
		var state='${dataSynch.state }';
		if(state=='0'||state!=null&&state!=''){
			$('#state').html(stateMap[state]);
		}
		var workSign='${dataSynch.workSign }';
		if(workSign=='0'||workSign!=null&&workSign!=''){
			$('#workSign').html(workSignMap[workSign]);
		}
	});
	function closeLayout(){
		window.close();
	}
</script>	

</html>