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
<title>医疗组变更</title>
<style>
.layout-panel,.panel-body{
 	overflow:visible !important; 
}
</style>
</head>
<body style="margin: 0px; padding: 0px;">
		<div id="cc" class="easyui-layout informationChange" data-options="fit:true">   
	    	<table style="height:7%;">
	    		<tr>
	    			<td>
	    				查询名称：<input id="querydrug" class="easyui-textbox"  style="width:155px;" data-options="prompt:'医疗组名称查询'">
	    			</td>
	    			<td>&nbsp;科室:</td>
					<td id = "classA" class="newMenu" style="width:110px;z-index:30;position: relative;">
						<div class="deptInput menuInput">
							<input id="deptName" class="ksnew" readonly/><span></span>
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
								<ul class="addDept">
								</ul>											
							</div>	
							<div class="depts-dl">
								<div class="addList"></div>
								<div class="tip" style="display:none">没有检索到数据</div>
							</div>
						</div>	
					</td>
					<td>
						&nbsp;院区:<input id="areaCode" class="easyui-combobox" style="width: 155px"/>
					</td>
	    			<td width="20px;"></td>
	    			<td>
	    				查询时间：	<input id="beginDate" class="Wdate" type="text" value="${beginDate}" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',maxDate:'#F{$dp.$D(\'endDate\')}'})" style="height:22px; width:175px;border: 1px solid #95b8e7;border-radius: 5px;"/> 
	    				到 <input id="endDate" class="Wdate" type="text" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'#F{$dp.$D(\'beginDate\')}'})"  value="${endDate }" style="height:22px; width:175px;border: 1px solid #95b8e7;border-radius: 5px;"/>
	    			</td>
	    			<td>
						&nbsp;&nbsp;<a href="javascript:void(0)" class="easyui-linkbutton" onclick="searchFrom()" iconCls="icon-search">查询</a>
		    		</td>
	    			<td>
						&nbsp;&nbsp;<a href="javascript:void(0)" class="easyui-linkbutton" onclick="print()" iconCls="icon-2012081511202">打印</a>
		    		</td>
					<td>
						&nbsp;&nbsp;<a href="javascript:void(0)" class="easyui-linkbutton" onclick="exportList()" iconCls="icon-down">导出</a>
					</td>
	    		</tr>
	    	</table>
		   <div data-options="region:'south',border:false" style="width: 100%;height: 93%">
		    	<table id="billSearchHzList" class="easyui-datagrid" style="padding:5px 5px 5px 5px;" data-options="singleSelect:'true',fitColumns:true,pagination:true,pageSize:20,pageList:[20,40,60,80,100],rownumbers:true,fit:true">
					<thead>
						<tr>
							<th data-options="field:'id',hidden:'true'"></th>
							<th data-options="field:'itemName',width:'12%',align:'center'">医疗组名称</th>
							<th data-options="field:'deptName',width:'8%',align:'center'">科室</th>
							<th data-options="field:'areaName',width:'8%',align:'center'">院区</th>
							<th data-options="field:'newDataCode',width:'12%',align:'center'">变更字段名</th>
							<th data-options="field:'oldDataName',width:'12%',align:'center'">变更前数据</th>
							<th data-options="field:'newDataName',width:'12%',align:'center'">变更后数据</th>
							<th data-options="field:'changeCause',width:'12%',align:'center'">变更原因</th>
							<th data-options="field:'operCode',width:'8%',align:'center'">更改人</th>
							<th data-options="field:'operDate',width:'10%',align:'center'">更改时间</th>
						</tr>
					</thead>
				</table>
				<form id="saveForm" method="post"/>
		    </div>    
		</div> 
		<div id="hisMenu" class="easyui-menu" style="width:100px; display: none;">
			<div id="hisMenuBtn" data-options="iconCls:'icon-edit'" onclick="copyHis()">修改变更原因</div>
		</div>
		<div id="addMatKindinfo"></div>
		<script type="text/javascript">
		var menuAlias = '${menuAlias}'
		var changeCause="";
		var id = "";
		$(function(){
			$(".deptInput").MenuList({
				width :530, //设置宽度，不写默认为530，不要加单位
				height :400, //设置高度，不写默认为400，不要加单位
				menulines:2, //设置菜单每行显示几列（1-5），默认为2
				dropmenu:"#m3",//弹出层id，必须要写
				isSecond:false,	//是否是二级联动的第二级，false为第一级，true为第二级，不写默认为false
				//haveThreeLevel:true,//是否有三级菜单
				para:"I",	//要传的参数，多个之间用逗号隔开，如果不写默认为查询全部
				firsturl:"<%=basePath%>baseinfo/department/getAuthorNoFic.action?menuAlias="+menuAlias+"&deptTypes=", //获取列表的url，必须要写
				relativeInput:".doctorInput",	//与其级联的文本框，必须要写
				relativeDropmenu:"#m3"			//与其级联的弹出层，如果是第一级必须要写，第二级不需要写
			});
			//院区下拉框
			$('#areaCode').combobox({
				url : "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action'/>?type=hospitalArea",
				valueField : 'encode',
				textField : 'name',
				multiple : false,
			});
			
			$('#billSearchHzList').datagrid({
				url:'<%=basePath%>baseinfo/medicalgroupChange/queryBusinessChangeRecord.action',
				queryParams:{itemName:$("#querydrug").textbox('getText'),beginDate:$('#beginDate').val(),endDate:$('#endDate').val(),deptCodes:$('#deptName').getMenuIds(),areaCode:$('#areaCode').combobox('getValue')},
				onRowContextMenu:function(e,index,row){
					e.preventDefault();
					$('#hisMenu').menu('show', {//显示右键菜单
		                left: e.pageX,//在鼠标点击处显示菜单
		                top: e.pageY
		            });
					changeCause = row.changeCause;
					id = row.id;
				},
				onLoadSuccess: function(data){
					//分页工具栏作用提示
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
		})
		function copyHis(){
			Adddilogs("修改变更原因","<c:url value='/baseinfo/medicalgroupChange/updateMark.action'/>?changeCause="+encodeURIComponent(encodeURIComponent(changeCause))+"&id="+id);
		}
		function Adddilogs(title, url) {
			$('#addMatKindinfo').dialog({    
			    title: title,    
			    width: '15%',    
			    height:'20%',    
			    closed: false,    
			    cache: false,    
			    href: url,    
			    modal: true   
			});    
		}
		//查询
		function searchFrom(){
			var itemName = $('#querydrug').textbox('getText');
			var beginDate = $('#beginDate').val();
			var endDate = $('#endDate').val();
			var deptCodes = $('#deptName').getMenuIds();
			var areaCode = $('#areaCode').combobox('getValue')
			$('#billSearchHzList').datagrid('load',{
				itemName: itemName,
				beginDate:beginDate,
				endDate:endDate,
				deptCodes:deptCodes,
				areaCode:areaCode
			});
		}
		//复选按钮赋值抬价
		function onclickBoxtaijia(){
			if($('#queryundrug').is(':checked')){
				$('#queryundrug').val(1);
				searchFrom()
			}else{
				$('#queryundrug').val("");
				searchFrom()
		   }
		}
		//导出列表
		function exportList() {
			var itemName = $('#querydrug').textbox('getText');
			var beginDate = $('#beginDate').val();
			var endDate = $('#endDate').val();
			var deptCodes = $('#deptName').getMenuIds();
			var areaCode = $('#areaCode').combobox('getValue')
			var rows=$('#billSearchHzList').datagrid('getRows');
			if(beginDate==null||beginDate==''||endDate==null||endDate==''){
			    $.messager.alert("提示","日期不能为空！");
			    return;
			}
			if(rows==null||rows.length==0){
				$.messager.alert("提示","没有数据，不能导出！");
				return;
			}else{
				$.messager.confirm('确认', '确定要导出吗?', function(res) {//提示是否导出
					if (res) {
						$('#saveForm').form('submit', {
							url :"<%=basePath%>baseinfo/medicalgroupChange/expChangeRecordList.action",
							queryParams:{itemName:itemName,beginDate:beginDate,endDate:endDate,deptCodes:deptCodes,areaCode:areaCode},
							onSubmit : function() {
								return $(this).form('validate');
							},
							success : function(data) {
								$.messager.alert("操作提示", "导出成功！", "success");
							},
							error : function(data) {
								$.messager.alert("操作提示", "导出失败！", "error");
							}
						});
					}
				});
			}
		}
		//打印方法  
	function print() {
		var itemName = $('#querydrug').textbox('getText');
		var beginDate = $('#beginDate').val();
		var endDate = $('#endDate').val();
		var deptCodes = $('#deptName').getMenuIds();
		var areaCode = $('#areaCode').combobox('getValue')
		var rows=$('#billSearchHzList').datagrid('getRows');
		if(rows==null||rows==""){
			$.messager.alert("提示", "列表无数据,无法打印！");
			return;
		}
		$.messager.confirm('确认', '确定要打印吗?', function(res) {//提示是否打印
			if (res) {
				window.open ("<%=basePath%>baseinfo/medicalgroupChange/irpeChangeRecordList.action?beginDate="+beginDate+"&endDate="+endDate+"&deptCodes="+deptCodes+"&areaCode="+areaCode+"&itemName="+itemName+"&fileName="+"YLZBG",'newwindow','height=900,width=850,top=50,left=500,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no');
			}
		});
	}
	</script>
	<script type="text/javascript" src="<%=basePath%>javascript/js/jquery.deptMenu.js"></script>
	<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
</body>
</html>