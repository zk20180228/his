<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/metas.jsp"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme() + "://"
		+ request.getServerName() + ":" + request.getServerPort()
		+ path + "/";
%>
<html>
<head>
	<title>门诊处方调剂界面</title>
<script>
	$(function(){
		var winH=$("body").height();
		if("${businessExtend1.numberProperty}" == 0){
			ListAverageGrid();
		}else{
			ListCompeteGrid();
		}
    });
	
	/**
	 * 列表信息（平均调剂）
	 **/
	function ListGrid(){
		if($("input[name='businessExtend1.numberProperty']:checked").val() ==0){
			ListAverageGrid();
		}else{
			ListCompeteGrid();
		}
	}
	/**
	 * 列表信息（平均调剂）
	 **/
	function ListAverageGrid(){
		$('#list').edatagrid({
			url:'<%=basePath %>drug/pharmacyManagement/queryTerminal.action?type=1&pid='+window.parent.window.getSelected(2),
			selectOnCheck:false,rownumbers:true,idField: 'id',pageSize:20,
			fitColumns:true,singleSelect:true,checkOnSelect:false,  pageNumber: 1, 
			autoSave:true,
			fit:true,
			rowStyler: function(index,row){
				if (row.closeFlag == 1){
					return 'background-color:red;';
				}
			},
			onBeforeLoad:function(){
				$('#list').edatagrid('clearChecked');
			},
			onDblClickRow:function(index, row){
				$('#list').edatagrid('beginEdit',index);
			},
			columns : [ [ {
				field : 'name',
				title : '配药台名称',
				width : '10%'
			}, {
				field : 'closeFlag',
				title : '是否关闭',
				width : '10%',
				formatter : function(val,row){
					if (val == 0){
						return '开放';
					} else if (val == 1){
						return '关闭';
					}
				}
			}, {
				field : 'sendQty',
				title : '已发送品种数',
				width : '10%',
				editor : {
					type : 'numberbox',
					options : {
						required:true,
						min:0,
						max:9909
					}
				}
			}, {
				field : 'drugQty',
				title : '待发送品种数',
				width : '10%'
			}, {
				field : 'averageNum',
				title : '均分次数',
				width : '10%'
			}
			]],onLoadSuccess:function(row, data){
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
				}}
		});
	}
	/**
	 * 列表信息（竞争调剂）
	 **/
	function ListCompeteGrid(){
		$('#list').edatagrid({
   			url:'<%=basePath %>drug/pharmacyManagement/queryTerminal.action?type=1&pid='+window.parent.window.getSelected(2),
   			selectOnCheck:false,rownumbers:true,idField: 'id',pageSize:20,
   			fitColumns:true,singleSelect:true,checkOnSelect:false,  pageNumber: 1, 
   			autoSave:true,
   			fit:true,
   			rowStyler: function(index,row){
				if (row.closeFlag == 1){
					return 'background-color:red;';
				}
			},
			onBeforeLoad:function(){
				$('#list').edatagrid('clearChecked');
			},
			onDblClickRow:function(index, row){
				$('#list').edatagrid('beginEdit',index);
			},
			columns : [ [ {
				field : 'name',
				title : '配药台名称',
				width : '10%'
			}, {
				field : 'closeFlag',
				title : '是否关闭',
				width : '10%',
				formatter : function(val,row){
					if (val == 0){
						return '开放';
					} else if (val == 1){
						return '关闭';
					}
				}
			}, {
				field : 'sendQty',
				title : '已发送品种数',
				width : '10%'
			}, {
				field : 'drugQty',
				title : '待发送品种数',
				width : '10%',
				editor : {
					type : 'numberbox',
					options : {
						required:true,
						min:0,
						max:9909
					}
				}
			}, {
				field : 'averageNum',
				title : '均分次数',
				width : '10%',
				editor : {
					type : 'numberbox',
					options : {
						required:true,
						min:0,
						max:9909
					}
				}
			}
			]],
			onLoadSuccess:function(row, data){
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
				}}
		});
	}
	
	function save(){
		$('#list').edatagrid('acceptChanges');
		$('#terminalJSON').val(JSON.stringify( $('#list').edatagrid("getRows")));
		$('#terminalForm').form('submit',{ 
			url:'<%=basePath %>drug/pharmacyManagement/recipeSave.action?pid='+window.parent.window.getSelected(2),
			onSubmit:function(){ 
				return $(this).form('validate');  
			},  
			success:function(data){  
				//实现刷新栏目中的数据
				reload();
			},
			error : function(data) {
				$.messager.alert('提示',"保存失败！");	
			}
		});
	}
	
	function reload(){
		window.location.href='<%=basePath %>drug/pharmacyManagement/recipeList.action?menuAlias=${menuAlias}&pid='+window.parent.window.getSelected(2)
		//$('#list').datagrid('reload');   
	}
</script>
</head>
	<body>
		<div id="divLayout" class="easyui-layout" fit=true style="min-width:900px;width:auto;">
		    <div data-options="region:'north',border: false" style="height:40px;">
		    	<table style="width:100%;border:0px;padding: 5px 0px 5px 0px;">
					<tr>
						<td style="font-size:14px" >
						<shiro:hasPermission name="${menuAlias}:function:save">
							<a href="javascript:void(0)" onclick="save()" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a>
						</shiro:hasPermission>
							<a href="javascript:void(0)" onclick="reload()" class="easyui-linkbutton" data-options="iconCls:'icon-reload'">刷新</a>
						</td>
					</tr>
				</table>
		    </div>   
		    <div data-options="region:'center'" style="padding:5px;">
		    	<form id="terminalForm" method="post">	
		    		<div style="height: 80px">	
						<input type="hidden" id="terminalJSON" name="terminalJSON"/>   
						<table style="width:100%;border:1px solid #95b8e7;padding:5px;">
							<tr>
								<td style="padding: 5px;width: 100">调剂方式：</td>
								<td style="padding: 5px;">
								<input type="hidden" name="businessExtend1.id" value="${businessExtend1.id }"/>
									<input type="radio" name="businessExtend1.numberProperty" value='0' onchange="ListGrid()"
										<c:if test="${businessExtend1.numberProperty==0}">checked="checked"</c:if>/>平均调剂
									&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									<input type="radio" name="businessExtend1.numberProperty" value='1' onchange="ListGrid()"
										 <c:if test="${businessExtend1.numberProperty==1}">checked="checked"</c:if>/>竞争调剂
								</td>
							</tr>
							<tr>
								<td style="padding: 5px;">调剂依据：</td>
								<td style="padding: 5px;">
								<input type="hidden" name="businessExtend2.id" value="${businessExtend2.id }"/>
									<input type="radio" name="businessExtend2.numberProperty" value="0" 
										<c:if test="${businessExtend2.numberProperty==0}">checked="checked"</c:if>/>发药
									&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									<input type="radio" name="businessExtend2.numberProperty" value="1" 
										<c:if test="${businessExtend2.numberProperty==1}">checked="checked"</c:if>/>配药
								</td>
							</tr>
						</table>
					</div>
		    		<div style="height: 89%">
							<table id="list" style="width:100%;height: 100%" >
							</table>
					</div>
		    	</form>
		    </div>   
		</div>  
	</body>
</html>