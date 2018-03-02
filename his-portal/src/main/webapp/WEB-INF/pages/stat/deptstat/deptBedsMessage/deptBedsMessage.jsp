<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<%@ include file="/common/metas.jsp"%>
<title>科室床位信息统计</title>
</head>
<script type="text/javascript" src="<%=basePath%>javascript/js/jquery.deptMenu.js"></script>
<script type="text/javascript">
var menuAlias = '${menuAlias}';
var flag;//全局科室
$(function(){
	//科室下拉
	$(".deptInput").MenuList({
		width :530, //设置宽度，不写默认为530，不要加单位
		height :400, //设置高度，不写默认为400，不要加单位
		menulines:2, //设置菜单每行显示几列（1-5），默认为2
		dropmenu:"#m3",//弹出层id，必须要写
		isSecond:false,	//是否是二级联动的第二级，false为第一级，true为第二级，不写默认为false
		haveThreeLevel:false,//是否有三级菜单
		para:"I",	//要传的参数，多个之间用逗号隔开，如果不写默认为查询全部
		firsturl:"<%=basePath%>baseinfo/department/getAuthorNoFic.action?menuAlias="+menuAlias+"&deptTypes=", //获取列表的url，必须要写
	});
	$('#m3 .addList h2 input').click();
	$('a[name=\'menu-confirm\']').click();
	flag=$('#deptName').getMenuIds();
	$('a[name=\'menu-confirm-clear\']').click();
	if(flag==''||flag==null){
		$("body").setLoading({
			id:"body",
			isImg:false,
			text:"无数据权限"
		});
	}else{
		var deptCode = $('#deptName').getMenuIds();
		$('#tableList').datagrid({ 
			pagination:true,
			pageSize:20,
			pageList:[20,30,50,80,100],
			fitColumns:true,
			singleSelect:true,
			border:true,
			fit:true,
			rownumbers:true,
			checkOnSelect:true,
			selectOnCheck:false,
			data:{total:0,rows:[]}
		});
	}
});

//查询按钮
 function searchFrom(){
	 var deptCode = $('#deptName').getMenuIds();
	 if(deptCode==''){
		 deptCode=flag;
	 }
	 $('#tableList').datagrid({    
			url: '<%=basePath%>statistics/deptBedsMessage/queryDeptBedsMessage.action',
			queryParams: {
				menuAlias:menuAlias,
				deptCode: deptCode,
			}
		}); 
 }
 
//重置按钮
 function clear(){
 	$('#deptName').val('');
	$('#deptName').attr('name','');
 	$("#tableList").datagrid('loadData', { total: 0, rows: [] });
 }
 
//导出列表
 function exportList() {
 	var rows = $('#tableList').datagrid('getRows');
 	if(rows==null||rows==""){
 		$.messager.alert("提示", "列表无数据,无法导出！");
 		return;
 	}
 	$.messager.confirm('确认', '确定要导出吗?', function(res) {//提示是否导出
 		if (res) {
 			var deptCode = $('#deptName').getMenuIds();
 			 if(deptCode==''){
 				 deptCode=flag;
 			 }
 			$('#exportFrom').val(deptCode);
 			$('#saveForm').form('submit', {
 				url :"<%=basePath%>statistics/deptBedsMessage/exportDeptBedsMessage.action",
 				queryParams:{menuAlias:menuAlias},
 				success : function(data) {
 					$.messager.alert("操作提示", "导出失败！", "error");
 				},
 				error : function(data) {
 					$.messager.alert("操作提示", "导出失败！", "error");
 				}
 			});
 		}
 	});
 }
 
//报表打印
 function exportPDF() {
 	var rowsCount=$('#tableList').datagrid('getRows')
 	if(''!=rowsCount&&null!=rowsCount){
 		 var deptCode = $('#deptName').getMenuIds();
 		if(deptCode==''){
 			 deptCode=flag;
 		 }
 		 var timer=new Date().getTime()
 		window.open ("<c:url value='printDeptBedsMessage.action?stime="+timer+"&deptCode="+deptCode+"'/>",'newwindow','height=900,width=850,top=50,left=500,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no');
 	}else{
 		$.messager.alert('提示','列表无数据,无法打印！');
 	}	
 }
</script>
<body style="margin: 0px; padding: 0px;">
	<div id="cc" class="easyui-layout" data-options="fit:true">   
	 	<form id="saveForm" action="" method="post" style="height:80px;width: 100%;padding-top: 8px;padding-top: 8px;padding-right: 10px;padding-left: 10px;">
	 		<input type="hidden" name="deptCode" id="exportFrom">
	 		<table border="0" style="width: 100%;height: 10%">
	 			<tr>
		 			<td style="width: 40px;"> 科室</td>
				    <td  class="newMenu" style="width:160px;z-index:1;position: relative;">
							<div class="deptInput menuInput" style="margin-top:0px;">
								<input id="deptName" class="ksnew"  readonly/>
								<span></span>
							</div>
							<div id="m3" class="xmenu" style="display: none;">
								<div class="searchDept">
									<input type="text" name="searchByDeptName" placeholder="回车查询"/>
									<span class="searchMenu"><i></i>查询</span>
									<a name="menu-confirm-cancel" href="javascript:void(0);" class="a-btn">
										<span class="a-btn-text">取消</span>
									</a>						
									<a name="menu-confirm-clear" href="javascript:void(0);" class="a-btn">
										<span class="a-btn-text">清空</span>
									</a>
									<a name="menu-confirm" href="javascript:void(0);" class="a-btn">
										<span class="a-btn-text">确定</span>
									</a>
								</div>
								<div class="select-info" style="display:none">
									<label class="top-label">已选部门：</label>
									<ul class="addDept"></ul>
								</div>	
								<div class="depts-dl">
									<div class="addList">
									</div>
									<div class="tip" style="display:none">没有检索到数据</div>
								</div>	
							</div>
		    		</td>
		    		<td>
		    			<shiro:hasPermission name="${menuAlias}:function:query">
			    		<a href="javascript:void(0)" class="easyui-linkbutton" onclick="searchFrom()" iconCls="icon-search">查询</a>
			 			</shiro:hasPermission>
			 			<a href="javascript:clear();void(0)" class="easyui-linkbutton" onclick="clear()" iconCls="reset">重置</a>
						<a href="javascript:void(0)" class="easyui-linkbutton" onclick="exportList()" iconCls="icon-down">导出</a>
<!-- 暂时注释掉,之后会打开注释,务删<a href="javascript:void(0)" class="easyui-linkbutton" onclick="exportPDF()" iconCls="icon-printer">打印</a> -->
		    		</td>
	    		</tr>
	    	 	
	    	 	<tr>
	    	 		<td align="center" colspan="3"  ><font style = "font-size: 32px !important;" class="empWorkTit" size="6">科室床位信息统计</font></td>
	    	 	</tr>
	    	 </table>
	    </form>
	    <div data-options="region:'center',border:false" style="width: 100%;height: 89%;">
	    	<table id="tableList" class="easyui-datagrid" style="padding:5px 5px 5px 5px;" data-options="singleSelect:'true',striped:true,fitColumns:true,rownumbers:true,fit:true">
				<thead>
					<tr>
						<th data-options="field:'bedName',width:'15%'">床位编号</th>
						<th data-options="field:'bedWardName',width:'15%'">房间号</th>
						<th data-options="field:'bedNum',width:'15%'">床号</th>
						<th data-options="field:'deptName',width:'18%'">科室名称</th>
						<th data-options="field:'bedLevel',width:'17%'">床位等级</th>
						<th data-options="field:'bedState',width:'17%'">床位状态</th>
					</tr>
				</thead>
			</table>
	    </div>   
	</div>
</body>
</html>
