<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<%@ include file="/common/metas.jsp" %>
<title>门诊发药</title>
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
</head>
<body style="margin: 0px;padding: 0px;">   
<div id="layout1" class="easyui-layout" data-options="fit:true" >
    <div id="p" data-options="region:'west',split:true,tools:'#toolSMId'" title="门诊药房" style="width:12%; height:500px;padding: 0px; overflow: hidden;">
		<input type="hidden" id="dept" value="${dept }">
		<div id="toolSMId">
			<a href="javascript:void(0)" onclick="collapseAll()" class="icon-fold"></a>
			<a href="javascript:void(0)" onclick="expandAll()" class="icon-open"></a>
		</div>
		<div id="treeDiv" style="width: 100%; height: 100%; overflow-y: auto;">
			<ul id="tDt">数据加载中...</ul>
		</div>
	</div>
    <div data-options="region:'center'" style="border-top:0;">
    	<div id="bb" class="easyui-layout" data-options="fit:true">
    		 <div id="layout" data-options="region:'north',border:false" style="height:40px;">
		     	<div style="text-align: left; padding: 5px 5px 5px 0px;">
		     		<shiro:hasPermission name="${menuAlias}:function:view">
			     		<a id='reload' href="javascript:void(0)" onclick="reload()" class="easyui-linkbutton" data-options="iconCls:'icon-reload',disabled:true" >自动刷新</a>
			     		<a id='unreload' href="javascript:void(0)" onclick="unreload()" class="easyui-linkbutton" data-options="iconCls:'zanting',disabled:true">暂停刷新</a>
					</shiro:hasPermission>
						<a id='ordonnance' href="javascript:void(0)"onclick="print()" class="easyui-linkbutton" data-options="iconCls:'icon-printer',disabled:true">打印处方单</a>
					<shiro:hasPermission name="${menuAlias}:function:save">
						<a id='save' href="javascript:void(0)" onclick="save()" class="easyui-linkbutton" data-options="iconCls:'icon-save',disabled:true">确认发药</a>
					</shiro:hasPermission>
					<a id='exitStoTerminal' href="javascript:void(0)" onclick="exitStoTerminal()" class="easyui-linkbutton" data-options="iconCls:'quit',disabled:true">退出</a>
		     		<span id='now' style="float: right;padding-top: 5;margin:0 15% 0 0"">
		     		</span>
		     	</div>
    		</div>   
			<div id='centerwest' data-options="region:'west'" style="height:100%; width:280px; padding: 0px;border-left:0;">
				<div id="layou" class="easyui-layout" data-options="fit:true,border:false" >
					<div data-options="region:'north',border:false" style="width:100%;height: 40px;">
						<table style="width:100%;">
							<tr>
								<td style="padding: 5px 0px 5px 0px;font-size:14px" nowrap="nowrap" >处方号：
									<input  id="medicalrecord"  class="easyui-textbox"  data-options="prompt:'回车查询',disabled:true "/>
									<a href="javascript:void(0);" onclick="deleData()" class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a>
								</td>
							</tr>
						</table>
					</div> 
					<div id="buttonArea" data-options="region:'center'" style="width: 100%;border-left:0;border-right:0;">
						<div id="tt" class="easyui-tabs" style="width:100%;height:100%;" data-options="justified:true,fit:true">   
							<input type="hidden" id="sendWicket"/>
							<div title="未发送" style="overflow:auto;"> 
								<ul id="unsend"></ul>
						    </div>   
						    <div title="已发送" style="overflow:auto;">   
						         <ul id="send"></ul>
						    </div>   
						</div> 
					</div>
				</div>
		    </div> 
		    <div id="dialog"></div>   
		     <div id="sendWicketInpatient" class="easyui-dialog" title="患者选择" style="width:800;height:500;padding:30 60 40 60" data-options="modal:true, closed:true">   
		    <table id="infoDatagrid"  style="width:100%;height:100%" data-options="fitColumns:true,singleSelect:true">   
		  </table>
		  </div>
		  
		    <div id='centercenter' data-options="region:'center',border:false" style="width: 100%;height: 60%">
		    	<div id="layou" class="easyui-layout" data-options="fit:true,border:false" >
		    		<div data-options="region:'north'" style="padding:5px 5px 0px 5px;height: 70px">
						<table id="table" class="tableCss" style="width: 100%;height:60px">
							<tr>
								<td class="tableLabel" >
									发票号：
								</td>
								<td id="invoiceNo" ></td>
								<td class="tableLabel" >
									门诊号：
								</td>
								<td id="clinicCode" ></td>
								<td class="tableLabel" >
									姓名：
								</td>
								<td id="patientName" ></td>
								<td class="tableLabel" >
									性别：
								</td>
								<td id="sexCode" ></td>
								<td class="tableLabel" >
									出生日期：
								</td>
								<td id="birthday" ></td>
							</tr>
							<tr>
								<td class="tableLabel" >
									挂号日期：
								</td>
								<td id="regDate" ></td>
								<td class="tableLabel" >
									收费人：
								</td>
								<td id="feeOper" ></td>
								<td class="tableLabel" >
									收费时间：
								</td>
								<td id="feeDate" ></td>
								<td class="tableLabel" >
									看诊科室：
								</td>
								<td id="doctDept" ></td>
								<td class="tableLabel" >
									看诊医生：
								</td>
								<td id="doctCode" ></td>
							</tr>
						</table>
					</div>
					<div data-options="region:'center',border:false" style="width: 100%;">
						<table id="list" fit="true" class="easyui-datagrid" style="width: 100%;" border="none"></table>
					</div>
				</div>
		    </div>   
		</div>  
    </div>
</div>
<form id="printF" action="" method="post">
	<input id="data" type="hidden" name="data">
</form>   
</body> 
<script type="text/javascript">
	var DrugpackagingunitMap=null;  //药品包装单位Map
	var login = $('#dept').val();
	var sendWicktDate;
	$(function(){
		$('#tt').tabs({    
		    border:false,
		    selected:0,
		    onSelect:function(title,index){
		    	tabTree();
		    }
		}); 
		//渲染药品单位信息
		$.ajax({
			url:'<%=basePath%>outpatient/sendWicket/DrugpackagingunitList.action',
			type:'post',
			success: function(payData) {
				DrugpackagingunitMap = payData;
			}
		});	
		$('#tt').tabs('disableTab',0);
		$('#tt').tabs('disableTab',1);
		trr();
	});
	//删除处方号查询框数据
	function deleData(){
		delSelectedData('medicalrecord');
		tabTree();
	}
	//药房树初始化
	function trr(){
		$('#tDt').tree({
				url : '<%=basePath %>outpatient/sendWicket/treeList.action',
				method:'get',
				lines : true,
				cache : false,
				animate : true,
				formatter : function(node) {//统计节点总数
					var s = node.text;
					if (node.children.length > 0) {
						s += '&nbsp;<span style=\'color:blue\'>('
								+ node.children.length + ')</span>';
					}
					return s;
				},onClick : function(node) {//点击节点
					$(this).tree(node.state === 'closed' ? 'expand' : 'collapse', node.target);
					if($('#tDt').tree('isLeaf',node.target)){
						$.ajax({
							url:'<%=basePath%>outpatient/sendWicket/statusVerification.action',
							type:'post',
							success: function(data) {
								obj=data;
								if(obj){
									Adddilog('双击选择发药窗口','<%=basePath%>outpatient/sendWicket/sendWicketListURL.action');
								}else{
									$.messager.alert('友情提示','非药剂师不可操作，请联系系统管理员'); 
								}
							}
						});	
					}
				},onLoadSuccess : function(node, data) {//默认选中
					$('#tDt').tree('select',$('#tDt').tree('find', '1').target);
				}
		});
	}
	
	//tab中，未发送与已发送列表树
	function tabTree(){
		var no = $('#medicalrecord').textbox('getValue');
		node=$('#sendWicket').val();
   		tab = $('#tt').tabs('getSelected');
		index = $('#tt').tabs('getTabIndex',tab);
	    if(index==0){
	    	$('#save').linkbutton('enable');
        	cleardetail();
        	$('#unsend').tree({ 
    			url : '<%=basePath %>outpatient/sendWicket/unsendList.action',
    		    method:'post',
    		    queryParams:{'type':1,'sendTerminal':node,'classMeaningCode':getSelected(1),'recipeNo':no},
    		    animate:true,
    		    lines:true,
    		    loadMsg : '正在处理，请稍待。。。',
    		    onClick: function(node){//点击节点
    		    	$(this).tree(node.state === 'closed' ? 'expand' : 'collapse', node.target);
    				adddetail(node,index,1);
    			}
    		});
        }else if(index==1){
        	$('#save').linkbutton('disable');
        	cleardetail();
        	$('#send').tree({    
				url : '<%=basePath %>outpatient/sendWicket/unsendList.action',
			    method:'post',
			    queryParams:{'type':3,'sendTerminal':node,'classMeaningCode':getSelected(1),'recipeNo':no},
			    animate:true,
			    lines:true,
			    loadMsg : '正在处理，请稍待。。。',
			    onClick: function(node){//点击节点
			    	$(this).tree(node.state === 'closed' ? 'expand' : 'collapse', node.target);
					adddetail(node,index,3);
				}
			});
        }
	}
	
	
	//患者处方详细信息填充
	function adddetail(node,index,type){
		if(node.id==1){
			cleardetail();
		}else{
			$.post('<%=basePath %>outpatient/sendWicket/getStoRecipeForId.action', { id:node.id,type:type}, function (data) {
				var obj = data;
				$('#invoiceNo').text(obj.invoiceNo);
				$('#clinicCode').text(obj.clinicCode);
				$('#patientName').text(obj.patientName);
				$('#sexCode').text(sexMap.get(obj.sexCode));
				$('#birthday').text(obj.birthday);
				$('#regDate').text(obj.regDate);
				$('#feeOper').text(obj.feeOperName);
				$('#feeDate').text(obj.feeDate);
				$('#doctDept').text(obj.doctDeptName);
				$('#doctCode').text(obj.doctName);
			});
			index = index + 1;
			setTimeout(function(){
				$('#list').datagrid({
					url:'<%=basePath %>outpatient/sendWicket/getDrugApplyoutForRecipeNo.action?menuAlias=${menuAlias}',
					queryParams: {
						RecipeNo: node.id,
						index: index,
						dept : login
					},
					selectOnCheck:false,rownumbers:true,idField: 'id',pageSize:20,
		   			fitColumns:true,singleSelect:true,checkOnSelect:false,  pageNumber: 1, 
		   			columns:[[{field:'applyNumber',title:'applyNumber',hidden:true},
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
								{field:'applyNum',title:'申请数量',width:'8%',
									formatter: function(value,row,index){
										return  (value * 1).toFixed(2) +' '+row.showUnit ;
									}
								}
					  ]],
					  onLoadSuccess:function(data){
						  sendWicktDate=data;
		   				}
					  
				});
			},100);
		}
	}
	//患者处方详细信息清除
	function cleardetail(){
		$('#invoiceNo').text("");
		$('#clinicCode').text("");
		$('#patientName').text("");
		$('#sexCode').text("");
		$('#birthday').text("");
		$('#regDate').text("");
		$('#feeOper').text("");
		$('#feeDate').text("");
		$('#doctDept').text("");
		$('#doctCode').text("");
		
		$('#list').datagrid('loadData',{total:0,rows:[]});
	}
	
	//（确定按钮）发送指定处方单的药品
	function save(){
		var node = $('#unsend').tree('getSelected');
		if(node == null){
			$.messager.alert('友情提示','请选择要发药的患者！！');
			return false;
		}
		var sendTerminal = $('#sendWicket').val();
		if(node.id!=1){
			$.messager.confirm('确认','是否确认发药？',function(r){ 
			    if (r){  
			    	$.messager.progress({text:'保存中，请稍后...',modal:true});
			    	$.post('<%=basePath %>outpatient/sendWicket/ConfirmSend.action', { sendTerminal:sendTerminal,deptid:getSelected(1),id:node.id,recipeNo:node.id}, function (data) {
			    		 
			    		if(data.resCode=="Y"){
			    			$.messager.progress('close');
				        	$.messager.alert('提示',"操作成功！");
							$('#unsend').tree('reload');
							cleardetail();
							$('#list').datagrid('loadData',{total:0,rows:[]});
			    		}else if(data.resCode=="N"){
			    			$.messager.progress('close');
			    			$.messager.alert('友情提示',data.resMes);
			    		}else{
			    			$.messager.progress('close');
			    			$.messager.alert('警告','药品发送出现未知错误');
			    		}
					});  
			    }    
			});
		}
	}
	
	//退出
	function exitStoTerminal(){
		$.messager.confirm('确认','确定退出当前登录配药台？', function(r){
			if(r){
				$.ajax({
					url: "<%=basePath%>outpatient/dosage/updateStoTerminal.action?id="+getId('single')+"&drugDeptCode="+$('#drugDeptCode').val()+"&flag=2",
					type:'post',
					success: function() {
						window.location="<%=basePath%>outpatient/sendWicket/sendWicketURL.action?menuAlias=${menuAlias}";
					}
				});
			}
		});
	}
	
	//自动刷新
	function reload(){
		$('#reload').linkbutton('disable');
		timer1= setInterval('tabTree()',5000);
	}
	//暂停刷新
	function unreload(){
		$('#reload').linkbutton('enable');
		 clearInterval(timer1);
	}
	//加载dialog
	function Adddilog(title, url) {
		$('#dialog').dialog({    
		    title: title,    
		    width: '40%',    
		    height:'280px',    
		    closed: false,    
		    cache: false,    
		    href: url,    
		    modal: true   
	   });    
	}
	
	//打开dialog
	function openDialog() {
		$('#dialog').dialog('open'); 
	}
	//关闭dialog
	function closeDialog() {
		$('#dialog').dialog('close');  
	}
	function expandAll(){//展开树
		$('#tDt').tree('expandAll');
	}
	function collapseAll(){//关闭树
		$('#tDt').tree('collapseAll');
	}
	
	function print(){//打印处方单 
			tab = $('#tt').tabs('getSelected');
			index = $('#tt').tabs('getTabIndex',tab);
			if(index==0){
				var node=$('#unsend').tree('getSelected');
				if(node == null){
					$.messager.alert('提示',"请选择要打印的患者！");
					return false;
				}
				var id = node.id;
				if(id!=null&&id!=''&&id!='1'){
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
					openPostWindow(url, data, '');
				}else{
					$.messager.alert('提示',"请选择已打印列表下的患者！");
					}
			}else if(index==1){
				var node=$('#send').tree('getSelected'); 
				if(node == null){
					$.messager.alert('提示',"请选择要打印的患者！");
					return false;
				}
				var id = node.id;
				if(id!=null&&id!=''&&id!='1'){
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
					openPostWindow(url, data, '');
				}else{
					$.messager.alert('提示',"请选择已打印列表下的患者！");
					}
			}
	}
	
	/**
	 * tag=1	获取选中节点ID
	 * tag=2 	父节点ID  
	 * tag=3 	判断选中的是否是叶子节点，如果是叶子节点则获取id，否则赋值1
	 * tag=4 	所选节点名称
	 */
	function getSelected(tag) {
		var node = $('#tDt').tree('getSelected');//获取所选节点
		if (node != null) {
			var Pnode = $('#tDt').tree('getParent', node.target);
			if (Pnode) {
				if (tag == 1) {
					var id = node.id;
					return id;
				}
				if (tag == 2) {
					var pid = Pnode.id;
					return pid;
				}
				if (tag == 3) {
					if ($('#tDt').tree('isLeaf', node.target)) {//判断是否是叶子节点
						var id = node.id;
						return id;
					} else {
						return 1;
					}
				}
				if(tag==4){
					var text = node.text;
					return text;
				}
			}
		} else {
			return null;
		}
	}
	
</script>
<script>     
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