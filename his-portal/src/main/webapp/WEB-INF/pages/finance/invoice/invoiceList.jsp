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
<title>发票领取</title>
<style type="text/css">
.window .panel-header .panel-tool a{
  	  	background-color: red;	
	}
</style>
<script type="text/javascript">
		var emp="";
		var check1=$('#check1').val();
		var check2=$('#check2').val();
		$(function(){
			if(check1=="1"){
				$('#checkbox1').attr('checked',true);
			}
			$('#employeeId1').show();
			$('#employeeId').textbox('setValue','');	
			$('#employeeOrgroup').datagrid({  
				    fitColumns:true,
					singleSelect:true,
					border:false,
					fit:true,
					url: '<%=basePath%>finance/financeInvoice/employeeTable.action?menuAlias=${menuAlias}',
					columns:[[  
						{field:'jobNo',title:'人员编号',width:100},    
						{field:'name',title:'姓名',width:100}   
				    ]],
				    onClickRow:function(rowIndex, rowData){
				         emp=rowData.jobNo;
				    	 var invoiceType=$('#cc2').combobox('getValue');
						 if(invoiceType==null || invoiceType ==''){
						 	$('#list').datagrid('load',"<c:url value='/finance/financeInvoice/queryFinanceInvoice.action'/>?invoiceGetperson="+emp+"&flag="+1);
						 }else{
						 	$('#list').datagrid('load', "<c:url value='/finance/financeInvoice/queryFinanceInvoice.action'/>?invoiceGetperson="+emp+"&invoiceType="+invoiceType+"&flag="+1);
						 }
				    }    
			 });
			$('#checkbox1').click(function(){
				if($('#checkbox1').is(':checked')){
					$('#check1').val("1");
					$('#check2').val("0");
			    	$('#checkbox2').attr('checked',false);
			    	$('#groupId1').hide();
					$('#employeeId1').show();
					$('#employeeId').textbox('setValue','');
			    	$('#employeeOrgroup').datagrid({  
					    fitColumns:true,
						singleSelect:true,
						fit:true,
						url: '<%=basePath%>finance/financeInvoice/employeeTable.action?menuAlias=${menuAlias}',
						columns:[[  
							{field:'jobNo',title:'编号',width:100},    
							{field:'name',title:'姓名',width:100}   
					    ]],
					    onClickRow:function(rowIndex, rowData){
					         emp=rowData.jobNo;
					    	 var invoiceType=$('#cc2').combobox('getValue');
							 if(invoiceType==null || invoiceType ==''){
							 	$('#list').datagrid('load',"<c:url value='/finance/financeInvoice/queryFinanceInvoice.action'/>?invoiceGetperson="+emp+"&flag="+1);
							 }else{
							 	$('#list').datagrid('load', "<c:url value='/finance/financeInvoice/queryFinanceInvoice.action'/>?invoiceGetperson="+emp+"&invoiceType="+invoiceType+"&flag="+1);
							 }
					    }    
				 	});
				}else{
					$('#check1').val("0");
					$('#check2').val("1");
			    	$('#checkbox2').prop('checked','checked');
			    	$('#employeeId1').hide();
					$('#groupId1').show();
					$('#groupId').textbox('setValue','');	
			    	$('#employeeOrgroup').datagrid({  
		                fitColumns:true,
		                singleSelect:true, 
		                fit:true,
		                url: '<%=basePath%>finance/financeInvoice/queryUserGroup.action?menuAlias=${menuAlias}', 		         
					    columns:[[  
							{field:'no',title:'组编号',width:100}, 
							{field:'groupName',title:'财务组名称',width:100}     
					    ]],
					    onClickRow:function(rowIndex, rowData){
					    	 var invoiceType=$('#cc2').combobox('getValue');
							 emp=rowData.no;
							 if(invoiceType==null || invoiceType ==''){
							 	$('#list').datagrid('load', "<c:url value='/finance/financeInvoice/queryFinanceInvoice.action'/>?invoiceGetperson="+rowData.no+"&flag="+1);
							 }else{
							 	$('#list').datagrid('load', "<c:url value='/finance/financeInvoice/queryFinanceInvoice.action'/>?invoiceGetperson="+rowData.no+"&invoiceType="+invoiceType+"&flag="+1);
							 }
					    }      
					});
				}
			});
			$('#checkbox2').click(function(){
				if($('#checkbox2').is(':checked')){
					$('#check1').val("0");
					$('#check2').val("1");
			    	$('#checkbox1').prop('checked',false);
			    	$('#employeeId1').hide();
					$('#groupId1').show();
					$('#groupId').textbox('setValue','');	
			    	$('#employeeOrgroup').datagrid({  
		                fitColumns:true,
		                singleSelect:true, 
		                fit:true,
		                url: '<%=basePath%>finance/financeInvoice/queryUserGroup.action?menuAlias=${menuAlias}', 		         
					    columns:[[  
							{field:'no',title:'组编号',width:100}, 
							{field:'groupName',title:'财务组名称',width:100}     
					    ]],
					    onClickRow:function(rowIndex, rowData){
					    	 var invoiceType=$('#cc2').combobox('getValue');
							 emp=rowData.no;
							 if(invoiceType==null || invoiceType ==''){
							 	$('#list').datagrid('load', "<c:url value='/finance/financeInvoice/queryFinanceInvoice.action'/>?invoiceGetperson="+rowData.no+"&flag="+1);
							 }else{
							 	$('#list').datagrid('load', "<c:url value='/finance/financeInvoice/queryFinanceInvoice.action'/>?invoiceGetperson="+rowData.no+"&invoiceType="+invoiceType+"&flag="+1);
							 }
					    }      
					});
				}else{
					$('#check1').val("1");
					$('#check2').val("0");
			    	$('#checkbox1').prop('checked','checked');
			    	$('#groupId1').hide();
					$('#employeeId1').show();
					$('#employeeId').textbox('setValue','');
			    	$('#employeeOrgroup').datagrid({  
					    fitColumns:true,
						singleSelect:true,
						fit:true,
						url: '<%=basePath%>finance/financeInvoice/employeeTable.action?menuAlias=${menuAlias}',
						columns:[[  
							{field:'jobNo',title:'编号',width:100},    
							{field:'name',title:'姓名',width:100}   
					    ]],
					    onClickRow:function(rowIndex, rowData){
					         emp=rowData.jobNo;
					    	 var invoiceType=$('#cc2').combobox('getValue');
							 if(invoiceType==null || invoiceType ==''){
							 	$('#list').datagrid('load',"<c:url value='/finance/financeInvoice/queryFinanceInvoice.action'/>?invoiceGetperson="+emp+"&flag="+1);
							 }else{
							 	$('#list').datagrid('load', "<c:url value='/finance/financeInvoice/queryFinanceInvoice.action'/>?invoiceGetperson="+emp+"&invoiceType="+invoiceType+"&flag="+1);
							 }
					    }    
				 	});
				}
			});
			});
			$.extend($.fn.validatebox.defaults.rules, {    
				number: {    
			        validator: function (value){    
			        	var reg = /^[0-9]*$/;
			            return reg.test(value);    
			        },    
			        message: '请输入数字！'   
			    }    
			});
			var addAndEdit;
			var a = [{"id":1,"text":"人员"},{"id":2,"text":"组"}];
			
			/**
			 * 人员、组模糊查询			 
			 */
			function searchFrom() {	
				var bischecked1=$('#checkbox1').is(':checked');
				var bischecked2=$('#checkbox2').is(':checked');
				if(bischecked1==true){
					var employeeIdSerc = encodeURIComponent(encodeURIComponent($.trim($('#employeeId').textbox('getValue'))));				
					$('#employeeOrgroup').datagrid('load',"<%=basePath%>finance/financeInvoice/employeeTable.action?sysEmployee.name="+employeeIdSerc + "&menuAlias=${menuAlias}");					
				}
				if(bischecked2==true){
					var groupIdSerc = encodeURIComponent(encodeURIComponent($.trim($('#groupId').textbox('getValue'))));							
					$('#employeeOrgroup').datagrid('load',"<%=basePath%>finance/financeInvoice/queryUserGroup.action?financeUsergroup.groupName="+groupIdSerc + "&menuAlias=${menuAlias}");					
				}				
			}
					   
			//加载页面
			$(function() {
				/**
			   	 发票回车查询人员
				*/
				bindEnterEvent('employeeId',searchFrom,'easyui');	
				/**
			   	 发票回车查询组
				*/
				bindEnterEvent('groupId',searchFrom,'easyui');
				bindEnterEvent('endNum',sumNUM,'easyui');
				var winH=$("body").height();
				$('#p').height(winH-78-30-27-3);//78:页面顶部logo的高度，30:菜单栏的高度，27：tab栏的高度，
				$('#list').height(winH-78-30-23-26);
					var id = "${id}"; //存储数据ID
				//添加datagrid事件及分页
				$('#list').datagrid({
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
				$('#cc2').combobox({
					onSelect: function(rec){
						var invoiceType=$('#cc2').combobox('getValue');//发票类型
						if((checkbox1.checked==true ||checkbox2.checked==true)){
							var row = $('#employeeOrgroup').datagrid('getSelected');
							if(checkbox1.checked==true&&row!=null){	
								$('#list').datagrid('load',"<%=basePath%>finance/financeInvoice/findByGetPerson.action?invoiceGetperson="+row.jobNo+"&invoiceType="+invoiceType + "&menuAlias=${menuAlias}");
							}
							if(checkbox2.checked==true&&row!=null){
								$('#list').datagrid('load',"<%=basePath%>finance/financeInvoice/findByGetPerson.action?invoiceGetperson="+row.no+"&invoiceType="+invoiceType + "&menuAlias=${menuAlias}");
							}
							if((checkbox1.checked==true&&row==null)||(checkbox2.checked==true&&row==null)){
								var invoiceGetperson=null;
								$('#list').datagrid('load',"<%=basePath%>finance/financeInvoice/findByGetPerson.action?invoiceGetperson="+invoiceGetperson+"&invoiceType="+invoiceType + "&menuAlias=${menuAlias}");
							}
						}
						else{
							var invoiceGetperson=null;
							$('#list').datagrid('load',"<%=basePath%>finance/financeInvoice/findByGetPerson.action?invoiceGetperson="+invoiceGetperson+"&invoiceType="+invoiceType + "&menuAlias=${menuAlias}");
						}
					} 
				});	
				var sumbo="";
				//计算结束号
		         $('#num').numberbox('textbox').bind('keyup', function(event) {
					 var num = $('#num').numberbox('getText');
					 var invoiceType=$('#cc2').combobox('getValue');
					 var startNum = $('#startNum').textbox('getValue');
					 var len = startNum.length;
					 var enStr = startNum.substr(0,1);
					 var noStr = startNum.substr(1,startNum.length);
					 var str = parseInt(noStr)+parseInt(num)-1+"";
					 var strlen = str.length;
					 for(var i=0;i<len-strlen-1;i++){
					 	str = "0"+str;
					 }
					 jQuery.ajax({  
						    type : "post",  
						    url: "<c:url value='/finance/financeInvoice/sumfinance.action'/>?num="+num+"&invoiceType="+invoiceType,
						   	dataType:"json",
						    success : function(data){
						    	sumbo=data;
						    	if(sumbo.stateNo){
									 $('#endNum').textbox('setValue',sumbo.stateNo);
								 }else{
									 $('#endNum').textbox('setValue',enStr+str); 
								 }
						    }  
						});
					 
		         });
		 
			});
			function sumNUM() {
				var invoiceType=$('#cc2').combobox('getValue');
				var startNum = $('#startNum').textbox('getValue');
				var endNum = $('#endNum').textbox('getValue');
				if(startNum.length!=endNum.length){
					$.messager.alert('提示','与起始号的长度不相同，请核对后重新输入！');
					return;
				}
				 jQuery.ajax({  
					    type : "post",  
					    url: "<c:url value='/finance/financeInvoice/getNum.action'/>?endNum="+endNum+"&invoiceType="+invoiceType,
					   	dataType:"json",
					    success : function(data){
					    	$('#num').numberbox('setText',data);
					    }  
					});
			}
			function add(){
				var invoiceType=$('#cc2').combobox('getValue');
				if(invoiceType==null || invoiceType ==""){
					$.messager.alert("提示","请选择发票类型！");
					return false;
				}
				var check=checkbox1.checked;
				var checkt=checkbox2.checked;
				
				if(check==false&&checkt ==false){
					$.messager.alert("提示","请选择人员或组！");
					return false;
				}
				if(emp==null||emp==""){
					$.messager.alert("提示","请选择发票领用人或组！");
					return false;
				}
				
				
				jQuery.ajax({  
				    type : "post",  
				    url: "<c:url value='/finance/financeInvoice/findMaxStartNo.action'/>?invoiceType="+invoiceType,
				    dataType:'json',  
				    success : function(data){
				    	if(data==null || data==""){
				    		$('#startNum').textbox('setValue',"");
				    		$.messager.alert("提示","起始发票号为空,请到发票入库管理栏目添加！");
				    	}else{
					    	$('#invoiceId').val(data.id);
					    	Adddilog('添加');
							$('#addForm').form('clear');
					    	if(data.invoiceUsedno==null||data.invoiceUsedno==""){
					    		$('#startNum').textbox('setValue',data.invoiceStartno);
					    		
					    	}else{
					    		 var starNum=data.invoiceUsedno;
					    		 var len = starNum.length;
								 var enStr = starNum.substring(0,1);
								 var noStr = starNum.substr(1,starNum.length);
								 var str = parseInt(noStr)+1+"";
								 var strlen = str.length;
								 for(var i=0;i<len-strlen-1;i++){
								 	str = "0"+str;
								 }
								 var startNum=enStr+str;
					    		$('#startNum').textbox('setValue',startNum);
					    	}
				    }
				    }  
				});
				
			}
			
			
			
			function reload(){
				//实现刷新栏目中的数据
				$('#list').datagrid('reload');
			}
	         
					
			//增加信息-提交form表单
			function OKDialog() {
				var invoiceType=$('#cc2').combobox('getValue');
				var num = $('#num').numberbox('getText');
				$('#invoiceType').val(invoiceType);
				var u ="";
				if(checkbox1.checked){
						u = "<c:url value='/finance/financeInvoice/saveFinanceInvoice.action'/>?invoiceGetperson="+getbachIdUtil('#employeeOrgroup',1,0)+"&num="+num;
				}if (checkbox2.checked){
						u = "<c:url value='/finance/financeInvoice/saveFinanceInvoice.action'/>?invoiceGetperson="+getbachNoUtil('#employeeOrgroup',1,0)+"&num="+num;
				}
				$('#addForm').form('submit', {
					url : u,
					data : $('#addForm').serialize(),
					dataType : 'json',
					onSubmit : function() {
						if (!$('#addForm').form('validate')) {
							$.messager.show({
								title : '提示信息',
								msg : '验证没有通过,不能提交表单!'
							});
							return false;
						}
					},
					success : function(data) {
						closeDialog();
						data = eval("("+data+")");
						if(data.resCode == 'error'){
							$.messager.alert('操作提示',data.resMes,'warning');
						}else{
							$.messager.alert('操作提示',data.resMes,'info');
							var invoiceType=$('#cc2').combobox('getValue');
							 if(invoiceType==null || invoiceType ==''){
							 	$('#list').datagrid('load',"<c:url value='/finance/financeInvoice/queryFinanceInvoice.action'/>?invoiceGetperson="+emp+"&flag="+1);
							 }else{
							 	$('#list').datagrid('load', "<c:url value='/finance/financeInvoice/queryFinanceInvoice.action'/>?invoiceGetperson="+emp+"&invoiceType="+invoiceType+"&flag="+1);
							 }
							if (addAndEdit == 2) {
								$.messager.show({
									title : '提示信息',
									msg : '添加成功!'
								});
							}
						}
					},
					error : function(data) {
						closeDialog();
						if(data.resCode == 'error'){
							$.messager.alert('操作提示',data.resMes,'warning');
							return false;
						}
					}
				 });
				
			}
			//获取数据表格选中行的ID checked=0否则是获取勾选行的ID ，获取多个带有拼接''的ID str=0，否则不带有''，
			function getbachIdUtil(tableID, str, checked) {
				var row;
				if (checked == 0) {
					row = $(tableID).datagrid("getSelections");
				} else {
					row = $(tableID).datagrid("getChecked");
				}
				var dgID = "";
				var i = 0;
				for (i; i < row.length; i++) {
					if (str == 0) {
						dgID += "\'" + row[i].jobNo + "\'";
					} else {
						dgID += row[i].jobNo;
					}
					if (i < row.length - 1) {
						dgID += ',';
					} else {
						break;
					}
				}
				return dgID;
			}
			//获取数据表格选中行的no
			function getbachNoUtil(tableID, str, checked) {
				var row;
				if (checked == 0) {
					row = $(tableID).datagrid("getSelections");
				} else {
					row = $(tableID).datagrid("getChecked");
				}
				var dgID = "";
				var i = 0;
				for (i; i < row.length; i++) {
					if (str == 0) {
						dgID += "\'" + row[i].no + "\'";
					} else {
						dgID += row[i].no;
					}
					if (i < row.length - 1) {
						dgID += ',';
					} else {
						break;
					}
				}
				return dgID;
			}
			/**
			 * 动态添加标签页
			 * @author  sunshuo
			 * @param title 标签名称
			 * @param title 跳转路径
			 * @date 2015-05-21
			 * @version 1.0
			 */
			function Adddilog(title){
					$('#invoice').dialog({    
					    title: title,    
					    width: 350,    
					    height: 250,    
					    closed: false,    
					    cache: false,    
					    modal: true   
					   });    
			}
			//关闭dialog
			function closeDialog() {
				$('#invoice').dialog('close');  
			}
			//格式化发票使用状态
			function format(val,row){
				if (val == 0){
					return '未使用';
				}if(val == 1){ 
					return '使用';
				}if(val == -1){ 
					return '已用';
				}
			}
			function unSelect(val){
				var pager = $('#employeeOrgroup').datagrid('getPager');
				var dataDiv = pager.prevObject;
				var jobNoSpans = $(dataDiv).find('.datagrid-view2').find('.datagrid-header').find('td[field="jobNo"]').find('span');
				var nameSpans = $(dataDiv).find('.datagrid-view2').find('.datagrid-header').find('td[field="name"]').find('span');
				if($('#'+val).is(':checked')){
					$('#'+val).prop('checked',false);
				}else{
					$('#'+val).prop('checked',true);
				}
				if($('#checkbox1').is(':checked')){
					$('#groupId1').hide();
					$('#employeeId1').show();
				}else{
					$('#groupId1').show();
					$('#employeeId1').hide();
				}
				if(val=='checkbox2'){
					$(jobNoSpans[0]).html('人员编号');
					$(nameSpans[0]).html('姓名');
				}else{
					$(jobNoSpans[0]).html('组编号');
					$(nameSpans[0]).html('组名');
				}
				searchFrom();
			}
		</script>
<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
</head>
	<body>
		<div id="cc" class="easyui-layout" data-options="fit:true">   
		    <div data-options="region:'west',title:'发票查询',split:false,border:false" style="width:300px;">
		    	<div id="ccc" class="easyui-layout" data-options="fit:true,border:false">   
				    <div data-options="region:'north',split:false,border:false" style="height:55px;">
				    	<div style="padding: 5px 0px 5px 5px;">
							<span>人员：</span><input id="checkbox1" type='checkbox' checked = "checked" name='VoteOption1'">
							<span>&nbsp;组： </span><input id="checkbox2" type='checkbox' name='VoteOption2' ">
							<input type="hidden" id="check1" value="1">
							<input type="hidden" id="check2" value="0">
						</div>
						<div style="margin-left:5px;">
							<span id="employeeId1" style="display:none;"><input id="employeeId" class="easyui-textbox" data-options="prompt:'姓名、拼音码、五笔码、自定义码、员工号'" style="width:130px;"/><a href="javascript:void(0)" onclick="searchFrom()" class="easyui-linkbutton" iconCls="icon-search">查询</a></span>
							<span id="groupId1" style="display:none;"><input id="groupId" class="easyui-textbox" data-options="prompt:'组名、拼音码、五笔码、自定义码'" style="width:130px;"/><a href="javascript:void(0)" onclick="searchFrom()" class="easyui-linkbutton" iconCls="icon-search">查询</a></span>					
						</div>
				    </div>   
				    <div data-options="region:'center'" style="border-right:0">
				    	<table id="employeeOrgroup" ></table>
				    </div>   
				</div>  
		    </div>   
		    <div data-options="region:'center'" style="border-top:0;">
		    	<div id="ccc" class="easyui-layout" data-options="fit:true,border:false">   
				    <div data-options="region:'north',split:false,border:false" style="height:35px;">
				    	<table style="width: 100%;height:100%;">
							<tr>
								<td style="font-size: 14">
									&nbsp;发票类型：
									<input id="cc2" class="easyui-combobox" style='width: 120px' data-options="url:'${pageContext.request.contextPath}/baseinfo/pubCodeMaintain/queryDictionary.action?type=invoiceType&menuAlias=${menuAlias}',valueField:'encode',textField:'name'" />
								  </td>
							  </tr>
						</table>
				    </div>   
				    <div data-options="region:'center'" style="border-left:0;">
				    	<table id="list" data-options="url:'${pageContext.request.contextPath}/finance/financeInvoice/queryFinanceInvoice.action?menuAlias=${menuAlias}',method:'post',selectOnCheck:false,rownumbers:true,idField: 'Id',striped:true,border:true,singleSelect:true,checkOnSelect:true,border:false,fitColumns:true,pagination:true,pageSize:20,pageList:[20,40,60,80,100],showRefresh:false,toolbar:'#toolbarId',fit:true">
							<thead>
								<tr>
									<th field="ck" checkbox="true"></th>						
									<th data-options="field:'invoiceStartno', width : '10%'">
										发票开始号
									</th>
									<th data-options="field:'invoiceEndno', width : '10%'">
										发票终止号
									</th>
									<th data-options="field:'invoiceUsestate', width : '10%'" formatter="format">
										使用状态
									</th>
								</tr>
							</thead>
						</table>
				    </div>   
				</div> 
		    </div>   
		</div>  
		<div id="invoice">
			<form id="addForm" action="" method="post">
			<input id="invoiceType" type="hidden" name="invoiceType"/>
			<input id="invoiceId" type="hidden" name="id"/>
				<table class="honry-table" cellpadding="1" cellspacing="1" border="1px solid black">
					<tr>
						<td class="honry-lable" style="font-size: 14">
							开始号：
						</td>
						<td class="honry-view">
							<input id="startNum" name="invoiceStartno" class="easyui-textbox" data-options="required:true,missingMessage:'请填写开始号！'" readonly="readonly"/>
						</td>
					</tr>
					<tr>
						<td class="honry-lable" style="font-size: 14">
							领用数量：
						</td>
						<td class="honry-view" style="font-size: 14">
							<input class="easyui-numberbox" id="num" data-options="required:true,missingMessage:'请填写领用数量！'" />
						</td>
					</tr>
					<tr>
						<td class="honry-lable" style="font-size: 14">
							结束号：
						</td>
						<td class="honry-view">
							<input class="easyui-textbox" id="endNum" name="invoiceEndno" data-options="required:true,missingMessage:'输入终止号后回车'"/>
						</td>
					</tr>
				</table>
			</form>
			<div style="text-align: center; padding: 5px">
				<shiro:hasPermission name="${menuAlias}:function:save">
					<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-save'" onclick="OKDialog()">确定</a>
				</shiro:hasPermission>
				<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="closeDialog()">关闭</a>
			</div>
		</div>
		<div id="toolbarId">
		<shiro:hasPermission name="${menuAlias}:function:add">
			<a href="javascript:void(0)" onclick="add()" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true">添加</a>
		</shiro:hasPermission>
			<a href="javascript:void(0)" onclick="reload()" class="easyui-linkbutton" data-options="iconCls:'icon-reload',plain:true">刷新</a>
		</div>
	</body>
</html>