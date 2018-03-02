<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<title>医技项目维护</title>
	<%@ include file="/common/metas.jsp" %>
</head>
<body style="margin: 0px;padding: 0px">
<div class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'north',border:false" style="width: 100%;height: 40px">
		<table style="width:100%;padding:5px 5px 1px 5px;">
			<tr>
				<td>
					<a href="javascript:saveDeptItem()"  class="easyui-linkbutton" iconCls="icon-save">保存</a>&nbsp;
					<a href="javascript:delDeptItem()"  class="easyui-linkbutton" iconCls="icon-cancel">删除</a>&nbsp;
					<a href="javascript:void(0)" onclick="clears()" class="easyui-linkbutton" iconCls="reset">重置</a>
				</td>
			</tr>
		</table>
	</div>
	<div data-options="region:'west',collapsible:false,border:true" style="width:360px;">
		<div class="easyui-layout" data-options="fit:true">
			<div data-options="region:'north',border:false" style="width: 100%;height: 35px;padding-top: 5px">
				<table>
					<tr>
						<td>条件查询：<input class="easyui-textbox" id="queryName" /></td>
					</tr>
				</table>
			</div>
			<div data-options="region:'center'" style="border-right:0;border-left:0;">
				<table id="list" >
					<thead>
						<tr>
							<th data-options="field:'id',width:'2%',hidden:true">项目编号</th>
							<th data-options="field:'name',width:'58%'">项目名称</th>
							<th data-options="field:'undrugSystype',width:'5%',hidden:true">系统类别</th>
							<th data-options="field:'defaultprice',align:'right',width:'10%'">默认价</th>
							<th data-options="field:'childrenprice',align:'right',width:'10%'">儿童价</th>
							<th data-options="field:'specialprice',align:'right',width:'10%'">特诊价</th>
						</tr>
					</thead>
				</table>
			</div>
		</div>		
	</div>
	<div data-options="region:'center',title:'要维护的项目'">
		<div data-options="region:'center',border:false">
			<form id="editForm" action=""  method="post">
				<table id="list1"></table>
			</form>
		</div>
	</div>
	<div data-options="region:'east',collapsible:false,border:true,title:'已维护的项目'" style="width:350px;">
		<div class="easyui-layout" data-options="fit:true">
			<div data-options="region:'center',border:false">
				<table id="list2" >
					<thead>
						<tr>
							<th data-options="field:'id',width:'2%',hidden:true">项目编号</th>
							<th data-options="field:'itemName',width:'90%'">项目名称</th>
						</tr>
					</thead>
				</table>
			</div>
		</div>		
	</div>
</div> 
<div id="newwindow"></div>  
<script type="text/javascript">
var arrIds=[];//记录已添加的条目Id
	$(function(){
		initEdatagrid();
		bindEnterEvent('queryName',searchFrom,'easyui');   //查询条件
		$("#queryName").textbox('textbox').bind('keyup',function(event){
			searchFrom();
		})
		
		
		$("#list").datagrid({
			selectOnCheck:false,
			singleSelect:false ,
			rownumbers:true,
			border:false,
			singleSelect:true,
			pagination:true,
			pageSize:20,
			pageList:[20,30,40,50,100],
			fit:true,
			fitColumns:false,
			url:"<%=basePath%>technical/essentialsItem/queryUnDrugAll.action",
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
			},
			onDblClickRow:function(rowIndex, rowData){
				var row = $('#list').datagrid('getSelected');
				if (row) {
					//判断记录是否已经添加到右侧列表中
					var rs=$('#list1').datagrid('getRows');
					var tag=row.id;
					if(rs.length>0){
						var i=arrIds.indexOf(tag);
						if(i!=-1){
							$.messager.alert("提示", "列表中已有此条记录！","warning");
							setTimeout(function(){
								$(".messager-body").window('close');
							},3500);
							$('#list1').datagrid('uncheckAll');//取消勾选当前页的所有行
							$('#list1').datagrid('checkRow',i);//勾选一行，行索引从0开始
							return;
						}
					}
					//添加一行并赋值
					$('#list1').datagrid('appendRow',{
						itemCode:row.code,
						itemName:row.name,
						classCode:row.undrugSystype,
						hurtFlag:1,
						selfbookFlag:1,
						reasonableFlag:1
					});
					arrIds.push(tag);
				}
			}
		});
		/**
		*已维护项目
		*time：2016年10月19日14:48:53
		**/
		setTimeout(function(){
			$("#list2").datagrid({
				selectOnCheck:false,
				singleSelect:false ,
				rownumbers:true,
				border:false,
				singleSelect:true,
				pagination:true,
				pageSize:20,
				pageList:[20,30,40,50,100],
				fit:true,
				fitColumns:false,
				url:'<%=basePath%>technical/essentialsItem/getEssentialItem.action?time='+new Date(),
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
				},
				toolbar:[{
					 id:'btnEdit',
						text:'修改',	
						iconCls:'icon-edit',
						handler:function(){
							var row = $('#list2').datagrid('getSelected'); //获取当前选中行     
				            if(row){
			                   	Adddilog("<c:url value='/technical/essentialsItem/editTecDeptItem.action'/>?id="+row.id);
			                   	$('#list2').datagrid('reload');
					   		}
					   }
				},{
					 id:'btnDelete',
						text:'删除',	
						iconCls:'icon-remove',
						handler:function(){
						  //删除处理
						  var row = $('#list2').datagrid('getSelected')
							$.ajax({
								url:'<%=basePath%>technical/essentialsItem/delTecDeptItem.action?id='+row.id,
								type:"post",
								success:function(data){
									$.messager.alert('提示',data.resMsg);
									$('#list2').datagrid('reload');
								}
							});
					   }
				}
				
				]
			});
		},200);
	})
	function searchFrom(){
		var queryName=$("#queryName").textbox('getText');
		$("#list").datagrid('load',{
			queryName:queryName
		});
	}
	/**
	 * 重置
	 * @author huzhenguo
	 * @date 2017-03-21
	 * @version 1.0
	 */
	function clears(){
		$('#queryName').textbox('setValue','');
		searchFrom();
	}
	
	//加载dialog
	function Adddilog(url) {//是否有滚动条,是否居中显示,是否可以改变大小
		    return window.open(url,'newwindow',' left=273,top=49,width='+ (screen.availWidth -505) +',height='+ (screen.availHeight-152));
	}
	function initEdatagrid(){
		$('#list1').edatagrid({
			autoSave:true,
			checkOnSelect:true,
			selectOnCheck:false,
			border:false,
			columns:[[
				{field:'ck',checkbox:'true'},
				{field:'itemCode',title:'项目编号',width:100,hidden:true,editor:{type:'textbox'}},
				{field:'itemName',title:'项目名称',width:100,halign:'center',editor:{type:'textbox',options:{required:true,readonly:true}}},
				{field:'classCode',title:'系统类别',width:100,halign:'center',hidden:true,editor:{type:'textbox'}},
				{field:'bookLocate',title:'预约地',width:100,halign:'center',editor:{type:'textbox'}},
				{field:'bookDate',title:'预约固定时间',width:100,halign:'center',editor:{type:'datetimebox'}},
				{field:'executeLocate',title:'执行地点',width:100,halign:'center',editor:{type:'textbox'}},
				{field:'reportDate',title:'取报告时间',width:100,halign:'center',editor:{type:'datetimebox'}},
				{field:'hurtFlag',title:'有创无创',width:70,halign:'center',editor:{type:'combobox'
					,options:{valueField:'id',textField:'text',
						data:[{"id":1,"text":"无效"},{"id":2,"text":"有效"}],
				}
				},formatter:checkboxFormer},
				{field:'selfbookFlag',title:'是否科内预约',width:100,halign:'center',editor:{type:'combobox'
					,options:{valueField:'id',textField:'text',
						data:[{"id":'1',"text":'是'},{"id":'0',"text":'否'}]}
				},formatter:checkboxFormer},
				{field:'reasonableFlag',title:'知情同意书',width:80,halign:'center',editor:{type:'combobox'
					,options:{valueField:'id',textField:'text',
						data:[{"id":'1',"text":'是'},{"id":'0',"text":'否'}]}
				},formatter:checkboxFormer},
				{field:'clinicMeaning',title:'临床意义',width:100,halign:'center',editor:{type:'textbox'}},
				{field:'sampleKind',title:'标本',width:100,halign:'center',editor:{type:'textbox'}},
				{field:'sampleWay',title:'采样方法',width:100,halign:'center',editor:{type:'textbox'}},
				{field:'sampleUnit',title:'标本单位',width:100,halign:'center',editor:{type:'combobox',options:{
					valueField : 'encode',
					textField : 'name',
					url: "<%=basePath %>baseinfo/pubCodeMaintain/queryDictionary.action?type=laboratorysample",
				}}},
				{field:'sampleQty',title:'标本量',width:100,halign:'center',editor:{type:'textbox'}},
				{field:'sampleContainer',title:'容量',width:100,halign:'center',editor:{type:'textbox'}},
				{field:'scope',title:'正常值范围',width:100,halign:'center',editor:{type:'textbox'}},
				{field:'machineType',title:'设备类型',width:100,halign:'center',editor:{type:'combobox', options:{
					valueField : 'encode',
					textField : 'name',
					url: "<%=basePath %>baseinfo/pubCodeMaintain/queryDictionary.action?type=sbtype",
				}}},
				{field:'remark',title:'备注',width:100,halign:'center',editor:{type:'textbox'}}
			]]
		});
	}
	//保存
	function saveDeptItem(){
		var rows=$("#list1").edatagrid("getRows");
		if(rows.length==0){
			$.messager.alert("提示", "没有数据,无法提交", "info");
			setTimeout(function(){
				$(".messager-body").window('close');
			},3500);
			return;
		}
		//取消预约地验证
// 		for (var i = 0; i < rows.length; i++) {
// 			if(rows[i].bookLocate==null||rows[i].bookLocate==""){
// 				$.messager.alert("提示","["+rows[i].itemName+"]预约地不能为空", "info");
// 				return;
// 			}
// 		}
		//标本量验证
		for (var i = 0; i < rows.length; i++) {
			if(rows[i].sampleQty!=null&&rows[i].sampleQty!=""){
				var sampleQty = rows[i].sampleQty;
				if(isNaN(sampleQty)){
					$.messager.alert("提示","标本量必须是纯数字！", "info");
					setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
					return;
				}
			}
		}
		$('#list1').datagrid('acceptChanges');
		var deptItemsJson=JSON.stringify(rows,"yyyy-MM-dd HH:mm:ss");
		$('#editForm').form('submit',{
			url:"<%=basePath%>technical/essentialsItem/saveEssentialsItem.action",
			queryParams:{deptItemsJson:deptItemsJson},
			success:function(dataMap){
				var data = eval('('+dataMap+')');
				$.messager.alert('提示',data.resMsg);
				$("#list2").datagrid('load');
			},
			error : function(data) {
				$.messager.alert('提示','保存失败');
				setTimeout(function(){
					$(".messager-body").window('close');
				},3500);
			}	
		});
	}
	//删除
	function delDeptItem(){
		var rows=$("#list1").datagrid("getChecked");
		if (rows.length > 0) {
			$.messager.confirm('确认', '确定要移出选中信息吗?', function(res) {//提示是否删除
				if (res) {
					for ( var i = 0; i < rows.length; i++) {
						var dd = $('#list1').edatagrid('getRowIndex',rows[i]);//获得行索引
						$('#list1').edatagrid('deleteRow', dd);//通过索引删除该行
					}
				} else {
					return false;
				}
			});
		}else{
			$.messager.alert('提示','请选择要移除的项目！');
			setTimeout(function(){
				$(".messager-body").window('close');
			},3500);
		}
	}
	//复选框
	function checkboxFormer(value,row,index){
		if(value=='1'){
			 return "<input type='checkbox' disabled='disabled' checked='checked'>";
		}else{
			 return "<input type='checkbox' disabled='disabled'>";
		}
	}
</script>
</body>
</html>