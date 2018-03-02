<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/metas.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<script type="text/javascript" src="<%=basePath%>javascript/js/jquery.deptMenu.js"></script>
<script type="text/javascript" src="<%=basePath%>javascript/js/jqloadingA.js"></script>
	<script type="text/javascript">
	var menuAlias = '${menuAlias}';
	var deptMap=new Map();
	var empMap=new Map();
	var packunit= new Map();
	$(function(){
		//加载数据表格
		$("#countList").datagrid({
			pagination : true,
			pageSize : 20,
			pageList : [ 20, 30, 50, 80, 100 ],
			idField:'id',
			border:true,
			checkOnSelect:true,
			selectOnCheck:false,
			singleSelect:true,
			fitColumns:false,
			pagination:true,
			rownumbers:true,
			fit:true,
			<%-- queryParams:{Stime:null,Etime:null,drug:null},
			url:'<%=basePath %>statistics/InventoryLog/queryInventoryLog.action', --%>
			onLoadSuccess:function(data){
				var pager = $(this).datagrid('getPager');
				var aArr = $(pager).find('a');
				var iArr = $(pager).find('input');
				$(iArr[0]).tooltip({content:'回车跳转',showEvent:'focus',hideEvent:'blur',hideDelay:1});
				for(var i=0;i<aArr.length;i++){
					$(aArr[i]).tooltip({
						content:toolArr[i],
						hideDelay:1
					});
					$(aArr[i]).tooltip('hide');
				}
			}
		});

	function searchFrom(){
		
	}
	
	function searchReload(){
		
	}
	});
	
	</script>
<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
</head>
<body>
	<div id="divLayout"  class="easyui-layout" data-options="fit:true" >
		<div data-options="region:'west',title:'地域',split:true,border:false" style="width:20%;height: 100%">
			<ul id="tt" class="easyui-tree">
				<li>
					<span>地域列表</span>
					<ul>
						<li>
							<span>河南省</span>
							<ul>
								<li>
									<span>郑州市</span>
								</li>
								<li>
									<span>新乡市</span>
								</li>
							</ul>
						</li>
						<li>
							<span>山东省</span>
						</li>
						<li>
							<span>山西省</span>
						</li>
					</ul>
				</li>
			</ul>
		</div>
		<div data-options="region:'center',border:false" style="height:100%;width: 80%">
			<div class="easyui-layout" data-options="fit:true" >
				<div data-options="region:'north',border:false" style="height:35px;width: 100%">
					<form id="serchForm">
						<table cellspacing="0" cellpadding="0" border="0" style="padding: 5px 5px 0px 5px;" >
							<tr>
								<td  nowrap="nowrap">
									级别：<input class="easyui-combobox" ID="level"  style="width:200px;" value="1" data-options="valueField:'id',textField:'text',data: [{id: '1',text: '全部'},{id: '2',text: '三甲'},{id: '3',text: '三级'},{id: '4',text: '二级'}]" />
									&nbsp;医疗机构：<input class="easyui-textbox" id="queryName" style="width:140px" data-options="prompt:'名称，编号，拼音'" />
									<a href="javascript:void(0)" onclick="searchFrom()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
									<a href="javascript:void(0)" onclick="searchReload()" class="easyui-linkbutton" iconCls="reset">重置</a>
								</td>
							</tr>
						</table>
					</form>
				</div>
				<div data-options="region:'center',border:false" style="height:100%;width: 100%">
					<table id="countList"  data-options="fit:true,method:'post'">
						<thead>
							<tr>
								<th data-options="field:'deptCode'," width="16%" align="center">机构编号</th>
								<th data-options="field:'deptName'" width="16%" align="center">机构名称</th>
								<th data-options="field:'outNum'" width="16%" align="center" halign="center">级别</th>
								<th data-options="field:'exOutNum'" width="16%" align="center">地域</th>
								<th data-options="field:'operationNum'" width="16%"align="center" halign="center">合作关系</th>
								<th data-options="field:'proportion'" width="16%" align="center">备注</th>
							</tr>
						</thead>
					</table>
				</div>
			</div>
		</div>
	</div>
	</body>
</html>
