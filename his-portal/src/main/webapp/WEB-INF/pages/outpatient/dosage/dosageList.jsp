<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<%@ include file="/common/taglibs.jsp" %>
<%@ include file="/common/metas.jsp" %>
<title>门诊配药</title>
<style type="text/css">
.window .panel-header .panel-tool a{
    background-color: red;	
}
	.tableCss{
			border-collapse: collapse;
			border-spacing: 0;
			border-left: 1px solid #95b8e7;
			border-top: 1px solid #95b8e7;
	}
	.tableLabel{
		text-align: right;
		width:10%;
	}
	.tableCss td{
		border-right: 1px solid #95b8e7;
		border-bottom: 1px solid #95b8e7;
		padding: 5px 15px;
		word-break: keep-all;
		white-space:nowrap;
	}
	.panel-header{
		border-top:0;
	}
</style>
<script>
	var sexMap=new Map();
	
	//性别渲染
	$.ajax({
		url : "<%=basePath %>/baseinfo/pubCodeMaintain/queryDictionary.action",
		data:{"type":"sex"},
		type:'post',
		success: function(data) {
			var v = data;
			for(var i=0;i<v.length;i++){
				sexMap.put(v[i].encode,v[i].name);
			}
		}
	});
	
</script>
<script type="text/javascript">
			var xq = "1";	//tab页选择以打印
			var loginDeptFlag;
			$(function(){
				
				loginDeptFlag =document.getElementById('loginDeptFlag').value;
				$('#patientDiv').tabs({    
				    border:false,
				    selected:0,
				    onSelect:function(title,index){
				    	reashTree();
				    }
				}); 
				//加载科室树
				tree();
				//加载药品列表
				$('#list').datagrid({
					url:"<%=basePath%>outpatient/dosage/queryDrugApplyoutByRecipeNo.action?menuAlias=${menuAlias}",
					queryParams:{recipeNo:'',drugDeptCode:''},
					onLoadSuccess:function(data){
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
						$('#list').datagrid('selectAll');
						sumRetailPrice();
					},
					columns:[[
								{field:'applyNumber',title:'applyNumber',hidden:true},
								{field:'id',title:'id',hidden:true},
								{field:'drugCode',title:'drugCode',hidden:true},
								{field:'tradeName',title:'药品商品名', width : '28%',
									formatter: function(value,row,index){
										if(row.specs != undefined && row.specs != null && row.specs != ''){
											return value +'【'+(row.specs == undefined ? '' : row.specs)+'】' ;
										}else{
											return value + '【】';
										}
									}
								},
								{field:'packQty',title:'包装数量[单位]',width:'10%',
									formatter: function(value,row,index){
										if(value == 0){
											value = 1;
										}
										if(row.minUnit != undefined && row.minUnit != null && row.minUnit != ''){
											return value +'['+(row.specs == undefined ? '' : row.specs)+']' ;
										}else{
											return value + '[]';
										}
									}
								},
								{field:'doseOnce',title:'用量',width:'8%',
									formatter: function(value,row,index){
										if(value != undefined && value != null && value != ''){
											return value +' '+(row.doseUnit == undefined ? '' : row.doseUnit) ;
										}
									}
								},   
								{field:'dfqCexp',title:'频次',width:'8%'},    
								{field:'useName',title:'用法',width:'10%'},    
								{field:'retailPrice',title:'单价',width:'8%'},    
								{field:'drugRetailprice',title:'金额',width:'8%',
										formatter:function(value){
											return (value * 1).toFixed(2);
										}  
								}, 
								{field:'validState',title:'有效标记',hidden:true},
								{field:'mark',title:'备注',hidden:true},
								{field:'applyNum',title:'申请数量',width:'8%',
									formatter: function(value,row,index){
										return  (value * 1).toFixed(2) +' '+row.showUnit ;
									}
								}
					]],rowStyler: function(index,row){
				      	  if (row.validState == 0){
				      	  	return 'background-color:red;';
						  }
					},onCheck:function(rowIndex,rowData){
						sumRetailPrice();
					},onUncheck:function(rowIndex,rowData){
						sumRetailPrice();
					}
				});
				$('#patientDiv').tabs('disableTab',1);
				$('#patientDiv').tabs('disableTab',0);
			});
			//加载科室树
			function tree(){
				$('#tDt').tree({    
				    url:"<%=basePath%>outpatient/dosage/treeDrugstore.action?flag=" + loginDeptFlag,
				    method:'get',
				    animate:true,
				    lines:true,
				    formatter:function(node){//统计节点总数
						var s = node.text;
						if (node.children){
							s += '&nbsp;<span style=\'color:blue\'>(' + node.children.length + ')</span>';
						}
						return s;
					},onClick: function(node){//点击节点
						if(validoperator()){
							$(this).tree(node.state === 'closed' ? 'expand' : 'collapse', node.target);
							if($('#tDt').tree('isLeaf',node.target)){
								selectDispenTable();
								$('#list').datagrid('load',{
									recipeNo:'',
									drugDeptCode:'',
								});
							}
						}else{
							$.messager.alert('友情提示','非药剂师不可操作，请联系系统管理员');
						}
					}
				});
			}
			//删除处方号查询框数据
			function deleData(){
				delSelectedData('recipeNo');
				reashTree();
			}
			//弹出选择配药台界面
			function selectDispenTable(){
				Adddilog("选择配药台", "<%=basePath%>outpatient/dosage/selectDispenTable.action?drugDeptCode="+$('#tDt').tree('getSelected').id,'dispenTableDiv');
			}
			$('#patientDiv').tabs({
				  onSelect: function(title){
					if(title=="打印"){
						xq = "1";
					}else if(title=="未打印"){
						xq = "0";
					}
				  }
			});
<%-- 配药确认begin*****************************************************************************************--%>
			/***** 配药判断 *****/ 
			function save(){
				//判断保存出库申请记录是，是否提醒警戒线
				var rowAll=$('#list').datagrid('getRows');
				if(rowAll.length != 0){
			    	isSave(rowAll);
				}else{
					$.messager.alert("友情提示","该处方单内没有药品！");
				}
			}
			
			/***** 配药操作 *****/ 
			function isSave(rows){
		    	var datamap = new Map();
				var arr=new Array();
				if(rows.length>=0){
					for(var i=0; i<rows.length; i++){
						if(datamap.get(rows[i].drugCode)!=null&&datamap.get(rows[i].drugCode)!=''){
							datamap.put(rows[i].drugCode,datamap.get(rows[i].drugCode)+rows[i].applyNum);
						}else{
							datamap.put(rows[i].drugCode,rows[i].applyNum);
						}
					}
					datamap.each(function(key,value,index){
						var saveObj={};
						saveObj["drugCode"]=key;
						saveObj["storeSum"]=value;
						arr.push(saveObj);
					});
					var dataJosn = $.toJSON(arr);
					$.post('<%=basePath%>outpatient/dosage/judgeWarnLine.action',{'drugDeptCode':$('#tDt').tree('getSelected').id,'&numJson':dataJosn},function(data){
						var arr = data;
						for(var i=0; i<arr.length; i++){
							var map=arr[i];
							for (var key in map) {
						    	 if(key=="warnLine"){
						    		 var dataMes=map.warnLine;
						    		 var dataMesArr=dataMes.split(",");
						    		 if(dataMesArr[0]!='noUse'){
						    			 $.messager.alert('提示',dataMesArr[1]);
						    		 }
						    	 }
						    }
						}
		    			$.messager.confirm('确认', '确定要对该处方单进行配药?', function(res){
		    				 if (res){
		    					 var stoRecipeId="";
	    						stoRecipeId=$('#tUnPrint').tree('getSelected').id;
		    					if($('#list').datagrid('getRows').length>0){
		    						$('#list').datagrid('acceptChanges');
		    						$('#outstoreApplyJson').val(JSON.stringify( $('#list').datagrid("getRows")));
		    						$('#stoRecipeId').val(stoRecipeId);
		    						$('#outstoreApplyForm').form('submit',{
		    					        url:"<%=basePath%>outpatient/dosage/updateOutstoreApply.action", 
		    					        type:'post',
		    					        onSubmit:function(){ 
		    					        	if (!$('#outstoreApplyForm').form('validate')){
		    				                    $.messager.progress('close');
		    				                    $.messager.show({  
		    				                         title:'提示信息' ,   
		    				                         msg:'验证没有通过,不能提交表单!'  
		    				                    }); 
		    				                       return false ;
		    				                 }
		    				                $.messager.progress({text:'保存中，请稍后...',modal:true});
		    					        },  
		    					        success:function(data){
		    					        	$.messager.progress('close');
		    					        	if(data != 'success'){
		    					        		$.messager.alert('提示', data);	
		    					        	}else{
			    					        	$.messager.confirm('提示', '保存成功，是否打印配药标签？', function(res){
			    					        		if (res){
			    				    					pringPYBQ(0);
			    				    					$.ajax({
			    				    						url:"<%=basePath%>outpatient/dosage/updateStorecipe.action", 
			    				    						data:{"stoRecipeId" : stoRecipeId},
			    				    						type:'post',
			    				    						success : function(data){
			    				    						}
			    				    					});
			    				    					clearData();
			    				    					reashTree();
			    				    				 }else{
			    				    					clearData();
			    				    					reashTree();
			    				    				 }
			    					        		handReashMethods();
			    					        	});
			    					        	
		    					        	}
		    					        },
		    							error : function(data) {
		    								$.messager.progress('close');
		    								$.messager.alert('提示',"操作失败！");	
		    							}			         
		    						});
		    					 }else{
		    						 $.messager.alert('提示',"请选择保存的记录!");
		    					 }
		    				 }
		    			 });
					});
				} 
			}
			
			
<%--完成配药end*****************************************************************************************--%>
			
			//自动刷新
			function refashMethods(){
				$('#autorefash').linkbutton('disable');
				reashTree();
				t=setTimeout('refashMethods()',5000);
			}
			//暂停
			function pauseMethods(){
				$('#autorefash').linkbutton('enable');
				clearTimeout(t); 
			}
			//手动刷新
			function handReashMethods(){
				reashTree();
			}
			//打印处方单 
			function printchufangdan(){
// 				var name = "郑州大学第一附属医院"
// 				var his_name = encodeURIComponent(encodeURIComponent(name));
				var tab = $('#patientDiv').tabs('getSelected');
				var index = $('#patientDiv').tabs('getTabIndex',tab);
				var node;
				if(index == 0){
					node=$('#tUnPrint').tree('getSelected'); 
				}else{
					node=$('#tPrint').tree('getSelected'); 
				}
				if(node == null){
					$.messager.alert('友情提示','请选择要打印的患者！');
					return false;
				}
				var id = node.id;
				if(id!=null&&id!=''&&id!='1'){
					//POST提交页面药品信息
					var row=$('#list').datagrid('getRows');
					var len=row.length;
					var data='';//药品基本信息
					for(var i=0;i<len;i++){
						data+=row[i].tradeName+'【'+row[i].specs+'】'+',';
						data+=row[i].packQty+'['+row[i].minUnit+']'+',';
						data+=row[i].doseOnce+row[i].doseUnit+','
						data+=row[i].useName+',';
						data+=row[i].dfqCexp+',';
						data+=row[i].applyNum+row[i].showUnit+',';
						data+=row[i].mark+'--';
					}
					var timerStr = Math.random();
					var url="<%=basePath%>/outpatient/sendWicket/getStoRecipePDf.action?randomId="+timerStr+"&id="+id+"&flag=1";
					//var name='配药打印窗口'
					openPostWindow(url, data, '');
				}else{
					$.messager.alert('友情提示','请选择要打印的患者！');
					}
			}
			function pringPYBQ(flag){
				var node
				if(flag == 0){
					node=$('#tUnPrint').tree('getSelected');
				}else{
					node=$('#tPrint').tree('getSelected');
				}
				if(node != null){
					var id = node.id;
					var row=$('#list').datagrid('getRows');
					var len=row.length;
					var data='';//药品基本信息
					for(var i=0;i<len;i++){
						data+=row[i].tradeName+','+'【'+row[i].specs+'】'+',';//名称  规格 
						data+=row[i].applyNum+row[i].showUnit+'--';//数量
					}
					var timerStr = Math.random();
					var url="<%=basePath%>/outpatient/sendWicket/getStoRecipePDf.action?randomId="+timerStr+"&id="+id+'&flag=2';
					//var name='配药打印窗口'
					openPostWindow(url, data, '');
				}
			}
			//打印配药标签 
			function suppleMethod(){
				var node=$('#tPrint').tree('getSelected');
				if(node==null||node==''){
					$.messager.alert('友情提示','当前没有患者');
				}
				var id = node.id;
				if(id!=null&&id!=''&&id!='1'){
					pringPYBQ(1);//打印
					$.ajax({
   						url:"<%=basePath%>outpatient/dosage/updateStorecipe.action", 
   						data:{"stoRecipeId" : id},
   						type:'post',
   						success : function(data){
   							$('#invoiceNo').html("");
  								$('#clinicCode').html("");
  								$('#patientName').html("");
  						        $('#sexCode').html("");
  						        $('#birthday').html("");
  						        $('#regDate').html("");
  						        $('#feeOper').html("");
  						        $('#feeDate').html("");
  						        $('#doctName').html("");
  						        $('#doctDept').html("");
  						        $('#recipeNoh').val("");
  						        reashTree();
  						        $('#list').datagrid('loadData', { total: 0, rows: [] });
   						}
   					});
				}else{
					$.messager.alert('友情提示','请选择要补打配药标签的患者！');
					}
			}
<%-- 退出清除患者信息begin*****************************************************************************************--%>				
			//退出
			function exitStoTerminal(){
				var drugedTerminal = $('#drugedTerminalId').val();
				$.messager.confirm('确认','确定退出当前登录配药台？', function(r){
					if(r){
						$.ajax({
							url: "<%=basePath%>outpatient/dosage/updateStoTerminal.action?id="+drugedTerminal+"&drugDeptCode="+$('#drugDeptCode').val()+"&flag=2",
							type:'post',
							success: function() {
								window.location="<%=basePath%>outpatient/dosage/listDosage.action?menuAlias=${menuAlias}"
							}
						});	
					}
				});
			}
			
			function clearData(){
				$('#invoiceNo').html("");
				$('#clinicCode').html("");
				$('#patientName').html("");
		        $('#sexCode').html("");
		        $('#birthday').html("");
		        $('#regDate').html("");
		        $('#feeOper').html("");
		        $('#feeDate').html("");
		        $('#doctName').html("");
		        $('#doctDept').html("");
		        $('#recipeNoh').val("");
		        $('#list').datagrid('loadData', { total: 0, rows: [] });
			}
<%-- 退出清除患者信息end*****************************************************************************************--%>
			//计算金额
			function sumRetailPrice(){
				var rows=$('#list').datagrid('getRows');
				var cost = 0;
				if(rows.length>=0){
					for(var i=0; i<rows.length; i++){
						cost=cost+parseFloat(rows[i].drugRetailprice);
					};
				}
				$('#totalStoreCost').text(cost.toFixed(2));
			}
			function Adddilog(title, url,id) {
				$('#'+id).dialog({    
				    title: title,    
				    width: '40%',    
				    height:'280px',    
				    closed: false,    
				    cache: false,    
				    href: url,    
				    modal: true 
				   });  
			}
			function openDialog(id) {
				$('#'+id).dialog('open'); 
			}
			function closeDialog(id) {
				$('#'+id).dialog('close');  
			}
			
<%--加载以打印与为打印信息  begin*****************************************************************************************--%>	
			function reashTree(){
				var no = $.trim($('#recipeNo').textbox('getValue'));
				var tab = $('#patientDiv').tabs('getSelected');
				var index = $('#patientDiv').tabs('getTabIndex',tab);
				var drugedTerminal = $('#drugedTerminal').val();
				clearData();
				if(index==0){
					$('#buttonSubmit').linkbutton('enable');
					$('#supple').linkbutton('disable');
					$('#tUnPrint').tree({  
					    url:"<%=basePath %>outpatient/dosage/querydispenTableTree.action",
					    method:'post',
					    queryParams:{'drugedTerminal':drugedTerminal,'drugDeptCode':$('#drugDeptCode').val(),'recipeState':0,'recipeNo':no},
					    animate:true,
					    lines:true,
					    loadMsg : '正在处理，请稍待。。。',
					   onClick: function(node){//点击节点
						   $(this).tree(node.state === 'closed' ? 'expand' : 'collapse', node.target);
							paddingData(node,0);
						}
					});
				}else if(index==1){
					$('#buttonSubmit').linkbutton('disable');
					$('#supple').linkbutton('enable');
					$('#tPrint').tree({    
					    url:"<%=basePath %>outpatient/dosage/querydispenTableTree.action",
					    method:'post',
					    queryParams:{'drugedTerminal':drugedTerminal,'drugDeptCode':$('#drugDeptCode').val(),'recipeState':2,'recipeNo':no},
					    animate:true,
					    lines:true,
					    loadMsg : '正在处理，请稍待。。。',
						onClick: function(node){
							$(this).tree(node.state === 'closed' ? 'expand' : 'collapse', node.target);
							paddingData(node,1);
						}
					});
				}
			}
<%--同时加载以打印与未打印信息  end *****************************************************************************************--%>
		
<%--患者信息填充Begin *************************************************************************************************--%>
		function paddingData(node,state){
			$.post("<%=basePath%>/outpatient/dosage/getStoRecipeById.action?id="+node.id,function(data){
				var map = data;
				$('#invoiceNo').html(map.invoiceNo);
				$('#clinicCode').html(map.clinicCode);
				$('#patientName').html(map.patientName);
                $('#sexCode').html(sexMap.get(map.sexCode));
				$('#birthday').html(map.birthday);
				$('#regDate').html(map.regDate);
				$('#feeOper').html(map.feeOper);
				$('#feeDate').html(map.feeDate);
				$('#doctName').html(map.doctName);
				$('#doctDept').html(map.doctDept);
				$('#recipeNo').html(map.recipeNo);
			});
			$('#list').datagrid('load',{
				recipeNo:node.id,
				drugDeptCode:$('#tDt').tree('getSelected').id,
				state: state
			});
			sumRetailPrice();
		}
		
		/**
		 * 判断当前登录人的员工类型是否是药师
		 */
		function validoperator(){
			var validoperatorFlag=true;
			$.ajax({
				url:'<%=basePath %>outpatient/dosage/getEmpTpye.action',
				type:'post',
				async:false,
				success:function(data){
					obj=eval("("+data+")");
					if(!obj){
						validoperatorFlag=false;
					}
				}
			});
			return validoperatorFlag;
		}
		/**
		* 打印窗口
		*/
		function openPostWindow(url, data, name)       
		  {       
			 $('#printF').attr('action',url);
			 $('#printF').attr('target','printF');
			 $('#data').val(data);
		     $('#printF').submit(function(e){
					window.open('about:blank','printF','height=900,width=850,top=50,left=500,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no');   
				});
		     $('#printF').submit();     
		} 
		</script>
</head>
	<body>
		<div id="layout1" class="easyui-layout" class="easyui-layout" fit=true style="width: 100%; height: 100%; overflow-y: auto;">
			<div id="p" data-options="region:'west',split:true" title="药房管理"style="width:12%;height:36px;padding: 0px;">
				<div style="width: 100%; height: 100%; overflow-y: auto;">
					<input type="hidden" id="loginDeptFlag" value="${isOrNotDrugstore}">
					<input type="hidden" id="drugedTerminal" >
					<input type="hidden" id="drugedTerminalId" >
					<ul id="tDt"></ul>
				</div>
			</div>
			<div data-options="region:'center',border:false">
				<div id="divLayout" class="easyui-layout" fit=true style="width: 100%; height: 100%; overflow-y: auto;">
					<div id="buttonArea" data-options="region:'north'" style="padding:5px;height: 40px;border-top:0;">
						<shiro:hasPermission name="${menuAlias}:function:view">
							<a id="autorefash" href="javascript:void(0)" onclick="refashMethods()" class="easyui-linkbutton" data-options="iconCls:'icon-reload',disabled:true" >自动刷新</a>
							<a id="autorefashPause" href="javascript:void(0)" onclick="pauseMethods()" class="easyui-linkbutton" data-options="iconCls:'zanting',disabled:true">暂停刷新</a>
							<a id="handReash" href="javascript:void(0)" onclick="handReashMethods()" class="easyui-linkbutton" data-options="iconCls:'sdsx',disabled:true" >手动刷新</a>
						</shiro:hasPermission>
						<shiro:hasPermission name="${menuAlias}:function:print">
							<a id="supple" href="javascript:void(0)" onclick="suppleMethod()" class="easyui-linkbutton" data-options="iconCls:'buda',disabled:true" >补打配药标签</a>
							<a id="ordonnance"href="javascript:void(0)" onclick="printchufangdan()" class="easyui-linkbutton" data-options="iconCls:'icon-printer',disabled:true">打印处方单</a>
						</shiro:hasPermission>
						<shiro:hasPermission name="${menuAlias}:function:save">
							<a id="buttonSubmit"  href="javascript:void(0)" onclick="save()" class="easyui-linkbutton" data-options="iconCls:'icon-save',disabled:true">确认配药</a>
						</shiro:hasPermission>
						<a id="exitStoTerminal" href="javascript:void(0)" onclick="exitStoTerminal()" class="easyui-linkbutton" data-options="iconCls:'quit',disabled:true">退出</a>
						<span id='now' style="float: right;padding-top: 5;margin:0 15% 0 0">
			     		</span>
					</div>
					<div id="recipeNoArea" data-options="region:'west',border:false" style="height:100%; width:280px; padding: 0px;">
						<div id="layou" class="easyui-layout" data-options="fit:true,border:false" >
							<div id="buttonArea" data-options="region:'north'" style="height: 40px;width: 100%">
								<table>
									<tr>
										<td style="padding: 3px 0px 3px 5px;font-size:14px" nowrap="nowrap">
											处方号：<input class="easyui-textbox" id="recipeNo" name="recipeNo" data-options="prompt:'回车查询',disabled:true " >
											<a href="javascript:void(0);" onclick="deleData()" class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a>
										</td>
									</tr>
								</table>
							</div>
							<div id="buttonArea" data-options="region:'center'" style="width: 100%;">
								<div id="patientDiv" class="easyui-tabs" data-options="justified:true,fit:true">   
								    <div title="未配药" style="overflow: auto;height: 100%">   
								        <ul id="tUnPrint"></ul>  
								    </div>   
								    <div title="已配药" style="overflow: auto;height: 100%">   
								        <ul id="tPrint" ></ul>
								    </div>
								</div>
							</div>
						</div>
					</div>
					<div id='centercenter' data-options="region:'center',border:false" style="width: 100%;height: 60%">
						<div id="layou" class="easyui-layout" data-options="fit:true,border:false" >
		    				<div data-options="region:'north'" style="padding:5px 5px 0px 5px;height: 70px;border-left:0">
								<form id="search" method="post">
									<table id="table" class="tableCss" style="width: 100%;height:60px;">
										<tr>
											<td class="tableLabel" >发票号：</td>
											<td id="invoiceNo" ></td>
											<td class="tableLabel" >门诊号：</td>
											<td id="clinicCode" ></td>
											<td class="tableLabel" >姓名：</td>
											<td id="patientName" ></td>
											<td class="tableLabel" >性别：</td>
											<td id="sexCode" ></td>
											<td class="tableLabel" >出生年月：</td>
											<td id="birthday" ></td>
										</tr>
										<tr>
											<td class="tableLabel" >挂号日期：</td>
											<td id="regDate" ></td>
											<td class="tableLabel" >收费人：</td>
											<td id="feeOper" ></td>
											<td class="tableLabel" >收费时间：</td>
											<td id="feeDate" ></td>
											<td class="tableLabel" >看诊科室：</td>
											<td id="doctDept" ></td>
											<td class="tableLabel" >看诊医生：</td>
											<td id="doctName" ></td>
										</tr>
									</table>
								</form>
							</div>
							<div data-options="region:'center',border:false" style="width: 100%;">
								<table id="list" style="width: 100%;overflow-y: auto;"
									data-options="method:'post',striped:true,checkOnSelect:false,selectOnCheck:false,singleSelect:true,fitColumns:false,border:false">
								</table>
								<div style="width: 100%;text-align: center;">
									<font >合计：</font>
									<span ID="totalStoreCost"></span>
								</div>
								<form id="outstoreApplyForm" method="post">
									<input type="hidden" id="stoRecipeId" name="stoRecipeId">
									<input type="hidden" id="outstoreApplyJson" name="outstoreApplyJson">
								</form>
							</div>
							<div id="dispenTableDiv"></div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<form id="printF" action="" method="post">
				<input id="data" type="hidden" name="data">
			</form>
	</body>
</html>
