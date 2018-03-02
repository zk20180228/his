<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ include file="/common/taglibs.jsp" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>患者警戒线</title>
<%@ include file="/common/metas.jsp"%>
<script type="text/javascript">
//添加颜色标识
function functionColour(value,row,index){
	var myDate = new Date();
	var month=myDate.getMonth()+1;
	var startDate = myDate.getFullYear()+"-"+month+"-"+myDate.getDate()+" 00:00:00";
	var endDate = row.alterBegin+"";
	if(row.freeCost<=row.moneyAlert){
		if(row.moneyAlert!=0){
			return 'background-color:red;color:black;';
		}
	}
	if(endDate.indexOf(":") >= 0){
		if(!compareTime(startDate,endDate)){
			return 'background-color:red;color:black;';
		}
	}
}
	/**
	 *加载页面
	 */
	$(function(){
		
		var deptFlg = "${deptFlg}"
		 console.log(deptFlg)
		
		$('#list').datagrid({
			pagination:true,
			url:"<%=basePath%>inpatient/cordon/queryCordon.action?menuAlias=${menuAlias}&type=HSZ",
			pageSize:20,
			pageList:[20,30,50,80,100],
			onLoadSuccess : function(data){
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
			},
			onBeforeLoad:function(param){
				$('#list').datagrid('clearChecked');
				$('#list').datagrid('clearSelections');
			}
	   });
		bindEnterEvent('queryName',searchFrom,'easyui');
		var deptId="${deptId}";
// 		$("#queryName").textbox('textbox').bind('keyup',function(){
// 			searchFrom();
// 		});
		/**
	 	 *加载护士站树
	 	 */
		$('#tDt').tree({   
		    url:"<%=basePath %>inpatient/cordon/TreeDeptcordon.action",
		    method:'get',
		    animate:true,   //点在展开或折叠的时候是否显示动画效果
		    lines:true,      //是否显示树控件上的虚线
		    formatter:function(node){//统计节点总数
				var s = node.text;
				if(node.children){
					if (node.children&&node.attributes!=null&&node.attributes!=''){
						s += '&nbsp;<span style=\'color:blue\'>(' + node.attributes.count1 + ')</span>';
					}
				}
				return s;
			},onClick : function(node) {//点击节点
				if(node.id!='1'){
					var deptCode=node.attributes.deptCode;
					$('#list').datagrid('uncheckAll');
					$('#list').datagrid('load', {
						deptId : deptCode
					});
				}else if(node.id=='1'){
					$.messager.alert('提示','请选择具体的护士站');
					setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
				}
				
			},onLoadSuccess:function(node,data){
				$('#tDt').tree('select',$('#tDt').tree('find', 1).target);
			}
	 	});
	});
	
	
	/*******************************开始读卡***********************************************/
	//定义一个事件（读卡）
	function read_card_ic(){
		var card_value = app.read_ic();
		if(card_value=='0'||card_value==undefined||card_value==''){
			$.messager.alert('提示','此卡号['+card_value+']无效');
			return;
		}
		$.ajax({
			url: "<%=basePath%>inpatient/info/queryMedicalrecordId.action",
			data:{idcardOrRe:card_value},
			type:'post',
			async:false,
			success: function(data) { 
				if(data==null||data==''){
					$.messager.alert('提示','此卡号无效');
					return;
				}
				$('#queryName').textbox('setValue',data);
				searchFrom();
			}
		});
	};
	/*******************************结束读卡***********************************************/
	/*******************************开始读身份证***********************************************/
		//定义一个事件（读身份证）
		function read_card_sfz(){
			var card_value = app.read_sfz();
			if(card_value=='0'||card_value==undefined||card_value==''){
				$.messager.alert('提示','此卡号['+card_value+']无效');
				return;
			}
			$.ajax({
				url: "<%=basePath%>inpatient/info/queryMedicalrecordId.action",
				data:{idcardOrRe:card_value},
				type:'post',
				async:false,
				success: function(data) {
					if(data==null||data==''){
						$.messager.alert('提示','此卡号无效');
						return;
					}
					$('#queryName').textbox('setValue',data);
					searchFrom();
				}
			});
		};
	/*******************************结束读身份证***********************************************/
	function clear(){
	  	$('#queryName').textbox('setValue','');
	  	searchFrom();
  	}
 	
 	
	
	/**
	 * 查询
	 */
	function searchFrom() {
		var queryName = $('#queryName').val();
		$('#list').datagrid('load', {
			queryName:queryName,
			deptId:$('#tDt').tree('getSelected').id,
		});
	}
	/**
	 * 室列表页 显示
	 */
	function deptFamater(value,row,index){
		if(value!=null&&value!=''){
			return deptMap[value];
		}
	}
 	
	/**
	 * 保存设置警戒线
	 */	
	function setCordon(){
		var row = $('#list').datagrid('getSelected'); //获取当前选中行 
		if(row){
			Adddilog("设置警戒线","<%=basePath%>inpatient/cordon/setCordonUrl.action?pids="+row.id);
		}else{
			$.messager.alert('提示','请选择一条记录');
			setTimeout(function(){
				$(".messager-body").window('close');
			},3500);
		}
	}
	/**
	 * 比较时间大小
	 */
	function compareTime(startDate,endDate){
		if(startDate.length>0&&endDate.length>0){
			var startDateTemp = startDate.split(" ");
			var endDateTemp = endDate.split(" ");
			var arrStartDate = startDateTemp[0].split("-");
			var arrEndDate = endDateTemp[0].split("-");
			var arrStartTime = startDateTemp[1].split(":");
			var arrEndTime = endDateTemp[1].split(":");
			var allStartDate = new Date(arrStartDate[0],arrStartDate[1],arrStartDate[2],arrStartTime[0],arrStartTime[1],arrStartTime[2]);
			var allEndDate = new Date(arrEndDate[0],arrEndDate[1],arrEndDate[2],arrEndTime[0],arrEndTime[1],arrEndTime[2]);
			if(allStartDate.getTime()>allEndDate.getTime()){
				return false;
			}
		}
		return true;
	}
	/**
	 * 设置类型渲染
	 */
	function funType(value,row,index){
		if(value=="D"){
			 return "时间";
		}else if(value=="M"){
			return "金钱";
		}else{
			return "无类别";
		}
	}
	/**
	 * 加载dialog
	 */
	function Adddilog(title, url) {
		$('#addCordon').dialog({    
		    title: title,    
		    width: '400px',    
		    height:'300px',    
		    closed: false,    
		    cache: false,
		    closable:true,
		    href: url,    
		    modal: true   
		   });    
	}
	/**
	 * 打开dialog
	 */
	function openDialog() {
		$('#addCordon').dialog('open'); 
	}
	/**
	 * 关闭dialog
	 */
	function closeDialog() {
		$('#addCordon').dialog('close');  
	}
 </script>
 <style type="text/css">
 	.panel-header{
 		border-top:0;
 	}
 </style>
</head>
<body>
  <input  id="typeId" name="type" type="hidden" value="${type}"><!-- 用于区分是全院还是护士站警戒线  全院=ALL,护士站=HSZ -->
  <div id="divLayout" class="easyui-layout" data-options="border:false" fit=true>
	<div id="p" data-options="region:'west',split:true" title="病区患者关系" style="width:250px; padding: 10px;min-width: 103px;">
		<ul id="tDt"></ul>
	</div>
	<div data-options="region:'center',border:false">
		<div  class="easyui-layout" fit=true  style="width:100%;height:100%;overflow-y: auto;">
			<div data-options="region:'north'" style="width:100%;height: 45px;padding-top: 7px;border-width:0 0 0 1px;">
				<table cellspacing="0" cellpadding="0" border="0">
					<tr>
						<td style="padding: 6px 15px;">过滤条件：<input class="easyui-textbox" ID="queryName" style="width: 190px;"data-options="prompt:'姓名,病历号,就诊卡号,床号'" /></td>
						<td>
					  	<shiro:hasPermission name="${menuAlias}:function:query">
							&nbsp;&nbsp;<a href="javascript:void(0)" onclick="searchFrom()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
						</shiro:hasPermission>
						</td>
						<td>
							<shiro:hasPermission name="${menuAlias}:function:readCard">
								&nbsp;<a href="javascript:void(0)" cardNo="" onclick="read_card_ic()" class="easyui-linkbutton" data-options="iconCls:'icon-bullet_feed'">读卡</a>
							</shiro:hasPermission>
							</td>
							<td>
				        	<shiro:hasPermission name="${menuAlias}:function:readIdCard">
				        		&nbsp;<a href="javascript:void(0)" cardNo="" onclick="read_card_sfz()" class="easyui-linkbutton" data-options="iconCls:'icon-bullet_feed'">读身份证</a>
							</shiro:hasPermission>
						</td>
						<td>
							&nbsp;<a href="javascript:clear();" class="easyui-linkbutton" data-options="iconCls:'reset'">重置</a>
						</td>
						<td>
							<shiro:hasPermission name="${menuAlias}:function:set">
						  		&nbsp;<a href="javascript:void(0)" onclick="setCordon()" class="easyui-linkbutton" data-options="iconCls:'icon-cog_edit'">设置警戒线</a>
							</shiro:hasPermission>
						</td>
					</tr>
				</table>
			</div>
			<div data-options="region:'center',split:false,iconCls:'icon-book',border:false">
				<input type="hidden" id="treeId"/>
				<table id="list" data-options="method:'post',striped:true,border:true,checkOnSelect:true,selectOnCheck:true,singleSelect:true,fitColumns:false,toolbar:'#toolbarId',fit:true" >
<!-- 				<table id="list" data-options="method:'post',rownumbers:true,striped:true,border:true,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false,toolbar:'#toolbarId'"> -->
					<thead>
						<tr>
							<th data-options="field:'orderId',width:24,align:'center',styler:functionColour" ></th>
		    				<th data-options="field:'inpatientNo'" style="width:100px">病历号</th>
		    				<th data-options="field:'patientName'" style="width: 100px">姓名</th>
		    				<th data-options="field:'bedName'" style="width: 100px">床号</th>
		    				<th data-options="field:'deptName'" style="width: 160px">住院科室</th>
		    				<th data-options="field:'nurseCellName'" style="width: 160px">住院护士站</th>
		    				<th data-options="field:'prepayCost'" style="width: 150px" align="right" halign="left">预交金</th>    <!--手动写的一个假name  所有未交的预交金额   --><!-- 为完成 -->
		    				<th data-options="field:'totCost'" style="width: 150px" align="right" halign="left">花费总额</th>   <!-- 住院金额 -->
		    				<th data-options="field:'freeCost'" style="width: 120px" align="right" halign="left">余额</th>  <!--手动写的一个假name，所有的余额-->  <!-- 为完成 -->
		    				<th data-options="field:'alterType',formatter:funType " style="width: 150px">警戒线类别</th>
		    				<th data-options="field:'moneyAlert'"style="width: 150px" align="right" halign="left">金额警戒线</th>
		    				<th data-options="field:'alterBegin'"style="width: 150px">警戒线开始时间</th>
		    				<th data-options="field:'alterEnd'"style="width: 150px">警戒线结束时间</th>
		    			</tr>
					</thead>
				</table>
			</div>
		</div>
  	</div>
  </div>
  <div id="addCordon"></div>
</body>
</html>
