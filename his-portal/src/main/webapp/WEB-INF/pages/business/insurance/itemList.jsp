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
<title>药品信息</title>
<script type="text/javascript"> 
			//加载页面
			$(function(){
			var winH=$("body").height();
			$('#list').height(winH-78-30-27-26);
				var id='${id}'; //存储数据ID
				//添加datagrid事件及分页
				$('#list').datagrid({
					pagination:true,
					pageSize:20,
					pageList:[20,30,50,80,100],
					onLoadSuccess: function (data) {//默认选中
			            var rowData = data.rows;
			            $.each(rowData, function (index, value) {
			            	if(value.id == id){
			            		$('#list').datagrid('checkRow', index);
			            	}
			            });
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
			        },
					onBeforeLoad:function(){
						$('#list').datagrid('clearChecked');
						$('#list').datagrid('clearSelections');
					}
				});
					bindEnterEvent('queryName',searchFrom,'easyui');
					bindEnterEvent('itemName',searchFrom,'easyui');
					bindEnterEvent('itemType',searchFrom,'easyui');
				});
			
				function formatflag(val,row,index){
					if (val == 1){
						return '是';
					} else {
						return '否';
					}
				}				
	         	function reload(){
					//实现刷新栏目中的数据
					 $("#list").datagrid("reload");
				}
	         	
				function searchReload() {
					$('#queryName').textbox('setValue','');
					$('#itemName').textbox('setValue','');
					$('#itemType').textbox('setValue','');
					searchFrom();
				}
				
				function searchFrom() {
					var queryName = $.trim($('#queryName').val());
					var itemType=$('#itemType').combobox('getValue');
					var itemName = $.trim($('#itemName').val());
					$('#list').datagrid('load', {
						str : queryName,
						itemType:itemType,
						itemName:itemName
						        
					});
				}
// 				function searchHisInfo(druginfo) {
// 					 $('#list1').datagrid({
//                  		url:"${pageContext.request.contextPath}/baseinfo/insurance/queryHisList.action?menuAlias=${menuAlias}",
//                  		queryParams: {
// 	                        	page: 1,
// 	                        	rows:10,
// 	                        	str:druginfo
// 	                    	}
//               })
// 	    	}
        //数据格式化
		  function formatzz(val,row,index){
				if(val=='0'){
					return "否"
				}
				if(val=='1'){
					return "是 "
				}else{
					return val
				}
			}
			function formatcostype(val,row,index){
				if(val=='01'){
					return "西药"
				}
				if(val=='02'){
					return "中成药 "
				}
				if(val=='03'){
					return "中草药 "
				}else{
					return val
				}
			}
			function formatprescFlag(val,row,index){
				if(val=='0'){
					return "非处方药"
				}
				if(val=='1'){
					return "处方药 "
				}else{
					return val
				}
			}
			function formatlevel(val,row,index){
				if(val=='1'){
					return "甲类"
				}
				if(val=='2'){
					return "乙类"
				}
				if(val=='3'){
					return "自费"
				}
				if(val=='5'){
					return "丁类"
				}else{
					return val
				}
			}
			function formatitemType(val,row,index){
				if(val=='0'){
					return "药品"
				}
				if(val=='1'){
					return "诊疗项目"
				}
				if(val=='2'){
					return "服务设施"
				}
			}
			function formatHisCostype(val,row,index){
				if(val=='01'){
					return "西药"
				}
				if(val=='02'){
					return "中成药 "
				}
				if(val=='03'){
					return "中草药 "
				}
				if(val=='04'){
					return "常规检查"
				}
				if(val=='05'){
					return "CT "
				}
				if(val=='06'){
					return "核磁 "
				}
				if(val=='07'){
					return "B超"
				}
				if(val=='08'){
					return "治疗费 "
				}
				if(val=='09'){
					return "化验费 "
				}
				if(val=='10'){
					return "手术费"
				}
				if(val=='11'){
					return "输氧费"
				}
				if(val=='12'){
					return "放射费 "
				}
				if(val=='13'){
					return "输血费"
				}
				if(val=='14'){
					return "注射费 "
				}
				if(val=='15'){
					return "透析 "
				}
				if(val=='16'){
					return "化疗"
				}
				if(val=='22'){
					return "特殊检查费 "
				}
				if(val=='32'){
					return "特殊治疗费 "
				}
				if(val=='34'){
					return "床位费"
				}
				if(val=='91'){
					return "其他费用 "
				}else{
					return val
				}
			}
	</script>
</head>
<body style="margin: 0px;padding: 0px">
<div id="divLayout" class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'north',border:false" style="height: 30px;">
	<input type="hidden" name="type" id="id" />
		<table >
			<tr>
				<td>
				   <span>本地类别</span>
				   <input id="itemType" class="easyui-combobox" name="itemType"   
                      data-options="valueField:'id',textField:'text',data:[{id:'',text:'全部'},{id:'0',text:'药品'},{id:'1',text:'诊疗项目'}	,{id:'2',text:'服务设施'}]"/>  			   
					<span>中心信息</span>
					<input type="text" id="queryName" name="queryName"  class="easyui-textbox" data-options="prompt:'名称、代码'" />
					<span>本地信息</span>
					<input type="text" id="itemName" name="itemName"  class="easyui-textbox" data-options="prompt:'名称、代码、辅助码'" />
					<a href="javascript:void(0)" onclick="searchFrom()" class="easyui-linkbutton" style="height:25px;" iconCls="icon-search">查询</a>
					<a href="javascript:void(0)" onclick="searchReload()" class="easyui-linkbutton" iconCls="reset">重置</a>
				</td>
			</tr>
		</table>
	</div>	
	<div data-options="region:'center',title:'项目对照信息',split:true">
	<table id="list" style="width:100%;" data-options="url:'${pageContext.request.contextPath}/baseinfo/insurance/queryHisList.action?menuAlias=${menuAlias}',method:'post',rownumbers:true,idField: 'id',striped:true,border:false,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false,fit:true">
			<thead>
				<tr>				
<!-- 				    <th data-options="field:'ck',checkbox:true" ></th> -->
				    <th data-options="field:'id'"width="6%"hidden=true>ID</th>
					<th data-options="field:'serialNo'"width="3%" editor="text">组内序号</th>
					<th data-options="field:'insuranceNo'"width="6%">医疗机构编号</th>
					<th data-options="field:'itemCode'"width="9%" editor="text">院内编码</th>
					<th data-options="field:'itemName'"width="9%" editor="text">院内名称</th>
					<th data-options="field:'itemType',formatter:formatitemType"width="6%">项目类别</th>
					<th data-options="field:'ybItemCode'"width="6%">中心编码</th>
					<th data-options="field:'ybItemName'"width="6%">中心名称</th>
					<th data-options="field:'costType',formatter:formatHisCostype"width="6%" >院内收费类别</th>
					<th data-options="field:'itemDosageform'"width="6%" editor="text">院内项目剂型</th>
					<th data-options="field:'itemSpec'"width="6%" editor="text">院内规格</th>
					<th data-options="field:'itemUnit'"width="6%" editor="text">院内单位</th>
					<th data-options="field:'itemPrice'"width="6%" editor="text">院内价格</th>	
					<th data-options="field:'inSelfProportion'"width="6%">住院自付比例</th>
					<th data-options="field:'operator'"width="6%">经办人</th>            
				</tr>
			</thead>
		</table>
</div>
</body>
</html>