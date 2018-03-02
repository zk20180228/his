<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>转科转床管理</title>
<%@ include file="/common/metas.jsp" %>

<script type="text/javascript">
	var bedTypeMap=new Map();//获得病床状态map对象
	var payKindMap=new Map();
	var bedOrganMap=new Map();//获得病床状态map对象
	
$(function(){
	/**
	 * 双击患者列表回显值
	 */
	$('#list').datagrid({
		url:'<%=basePath %>nursestation/Transfer/searchInpatientList.action?menuAlias=${menuAlias}',
		//双击击节点点到右边查看详细页面
		onDblClickRow: function (rowIndex, rowData) {
			if(rowData.inState!='I'){
				$.messager.alert("提示","该患者还没接诊");
				setTimeout(function(){
					$(".messager-body").window('close');
				},3500);
				return;
			}		
			obtainValue(rowData);
		},onLoadSuccess : function(data){
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
	
	/**
	 * 把当前科室和当前病床号带到上面
	 */
	$('#list1').datagrid({
		//双击击节点获得病床号和病床id带回到上面
	    onDblClickRow: function (rowIndex, rowData) {
	    	$('#bedIds').val(rowData.bedId);    //当前病床号
	    	$('#bedNo').val(rowData.bedId);
	    	$('#bedName').val(rowData.bedName);
	    	$('#bedwardId').val(rowData.bedwardId);
	    	$('#bedwardName').val(rowData.bedwardName);
			$('#bedId').textbox('setValue',rowData.bedName);    //当前病床号
	    }  ,onLoadSuccess : function(data){
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
	$("#list1").datagrid('loadData', { total: 0, rows: [] });
	//下拉框获取部门，根据部门查询该科室的患者，双击患者列表回显值
	$('#deptCode').combobox({  
		url:'<%=basePath %>nursestation/Transfer/inpatientDept.action',
		valueField:"deptId",    
		textField:"deptName",
		onSelect:function(none){
			var id=(none.deptId!=null&&none.deptId!="")?none.deptId:null;
			$('#list').datagrid({
				url:'<%=basePath %>nursestation/Transfer/searchInpatientList.action?deptId='+id + "&menuAlias=${menuAlias}",
				pagination:true,
				pageSize:10,
				pageList:[5,10,20,50,100],
				//双击击节点点到右边查看详细页面
				onDblClickRow: function (rowIndex, rowData) {
					if(rowData.inState!='I'){
						$.messager.alert("提示","该患者还没接诊");
						setTimeout(function(){
							$(".messager-body").window('close');
						},3500);
						return;
					}
					obtainValue(rowData);
				}
			});
		},onLoadSuccess : function(node) {
			if(node.resCode=='error'){
				   $("body").setLoading({
						id:"body",
						isImg:false,
						text:node.resMsg
					});
			   }
		}
	});
	//加载转入到那个病区下树
	$('#tDt').tree({
		url:'<%=basePath %>nursestation/Transfer/treeDepartmen.action',
		animate:true,
		lines:true,
		border:false,
		formatter:function(node){//统计节点总数
			var s = node.text;
			return s;
		},
		//双击击节点点到右边查看详细页面
		onDblClick:function(node){
			if(node.id!=""){
				var id=node.id;
				var deptCode=node.attributes.deptCode;
				var text=node.text;
				$('#deptCodess').val(deptCode);     //当前病区id
				$('#deptCodes').textbox('setValue',text);    //当前病区
				$('#bedIds').val('');
				$('#bedId').textbox('clear');
				$('#list1').datagrid({
					url:"<%= basePath %>nursestation/Transfer/searchTransferList.action?menuAlias=${menuAlias}",
					queryParams: {deptId: deptCode}
				});
				$('#lesionDept').combobox({
					valueField:'deptCode',
				    textField:'deptName',
					url:"<%=basePath %>nursestation/Transfer/lesionDept.action",
					queryParams:{queryLesion:id},
					onLoadSuccess: function () { //加载完成后,设置选中第一项  
						var data = $('#lesionDept').combobox('getData');
						 $("#lesionDept ").combobox('select',data[0].deptCode);
					}  
				});
			}
		}
	});
	$.ajax({
	    url:  "<%=basePath %>baseinfo/pubCodeMaintain/queryDictionary.action",
	    data:{type:"paykind"},
		type:'post',
		success: function(data) {
			var type = data;
			for(var i=0;i<type.length;i++){
				payKindMap.put(type[i].encode,type[i].name);
			}
		}
	});
	//获得病床状态map对象
	$.ajax({
	    url:  "<%=basePath %>baseinfo/pubCodeMaintain/queryDictionary.action",
	    data:{type:"bedtype"},
		type:'post',
		success: function(data) {
			var bedtype = data;
			for(var i=0;i<bedtype.length;i++){
				bedTypeMap.put(bedtype[i].encode,bedtype[i].name);
			}
		}
	});
	//获得病床状态map对象
	$.ajax({
	    url:  "<%=basePath %>baseinfo/pubCodeMaintain/queryDictionary.action",
	    data:{type:"badorgan"},
		type:'post',
		success: function(data) {
			var ortype = data;
			for(var i=0;i<ortype.length;i++){
				bedOrganMap.put(ortype[i].encode,ortype[i].name);
			}
		}
	});
	bindEnterEvent('queryName',searchFrom,'easyui');   //查询条件
	bindEnterEvent('queryDept',searchDeptTree,'easyui');    //部门树的查询
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
//表单提交
function submit(){
	//验证
	if($('#names').val()==""){
		$.messager.alert('提示',"请选择患者");
		setTimeout(function(){
			$(".messager-body").window('close');
		},3500);
		$('#editForm').form('clear');
		$('#tDt').tree('reload');
		clearText();
		$("#list1").datagrid('reload',{
			deptId:0,
		});
		return ;
	}
	if($('#inState').val()!="I"){
		$.messager.alert('提示',"该患者未接诊");
		setTimeout(function(){
			$(".messager-body").window('close');
		},3500);
		$('#editForm').form('clear');
		$('#tDt').tree('reload');
		clearText();
		$("#list1").datagrid('reload',{
			deptId:0,
		});
		return;
	}
	if($('#babyFlag').val()!=0){
		$.messager.alert('提示',"婴儿不能单独转科，必须同父母一起转床");
		setTimeout(function(){
			$(".messager-body").window('close');
		},3500);
		$('#editForm').form('clear');
		$('#tDt').tree('reload');
		clearText();
		$("#list1").datagrid('reload',{
			deptId:0,
		});
		return ;
	}
	if($('#lesionDept').combobox('getValue')==""&& $('#bedId').val()==""){
		$.messager.alert('提示',"请选择转入的科室和转入的病床");
		setTimeout(function(){
			$(".messager-body").window('close');
		},3500);
		return ;
	}
	if($('#lesionDept').combobox('getValue')==""){
		$.messager.alert('提示',"请选择转入的科室");
		setTimeout(function(){
			$(".messager-body").window('close');
		},3500);
		return ;
	}
	if($('#bedId').val()==""){
		$.messager.alert('提示',"请选择转入的病床");
		setTimeout(function(){
			$(".messager-body").window('close');
		},3500);
		return ;
	}
	if($('#deptNames').val()==$('#deptCodes').val()){
		if($('#bedNames').val()==$('#bedId').val()){
			$.messager.alert('提示',"该患者科室病床都相同，不能转科");
			setTimeout(function(){
				$(".messager-body").window('close');
			},3500);
			$('#editForm').form('clear');
			clearText();
			$('#tDt').tree('reload');
			$("#list1").datagrid('reload',{
				deptId:0,
			});
			return;
		}
	}
	
	$('#inpatientdeptName').val($('#lesionDept').combobox('getText'));
	$('#editForm').form('submit',{
		url:'<%=basePath %>nursestation/Transfer/saveShift.action', 
		queryParams:{bedId:$('#bedNames').val(),deptCode:$('#deptNames').val()},
		onSubmit:function(data){
			if (!$('#editForm').form('validate')) {
				$.messager.show({
					title : '提示信息',
					msg : '验证没有通过,不能提交表单!'
				});
				return ;
			}
			$.messager.progress({text:'转科转床,请稍等...',modal:true});
		},
		success:function(data){ 
			$.messager.progress('close');
			if(data=="error"){
				$.messager.confirm('确认', '该用户已申请，申请和现在转入的科室和病床不同,确认按新的走吗?', function(res){
					if(res){
						$.messager.progress({text:'正在转科转床,请稍等...',modal:true});
						$.ajax({
							url:'<%=basePath %>nursestation/Transfer/saveInpatient.action', 
							queryParams:{bedId:$('#bedNames').val(),deptCode:$('#deptNames').val()},
						});
					}
				});
			}
			$.messager.progress('close');
			$.messager.alert('提示','保存成功');
			setTimeout(function(){
				$(".messager-body").window('close');
			},3500);
			$('#editForm').form('clear');
			clearText();
			//实现刷新栏目中的数据
			$("#list").datagrid("reload");
			$('#tDt').tree('reload');
			$("#list1").datagrid('reload',{
				deptId:0,
			});
		},
		error : function(data) {
			$.messager.progress('close');
			$.messager.alert('提示','保存失败！');	
		}
	}); 
}
/**
 * 双击患者获得值给td复值
 */
function obtainValue(Data){
	clearText();
	$('#pid').val(Data.id);     //获得id
	$('#inState').val(Data.inState);   //接诊状态
	$('#babyFlag').val(Data.babyFlag);   //是否是婴儿
	$('#medicalrecordId').val(Data.medicalrecordId);  //病历号
	$('#inpatientNo').val(Data.inpatientNo);       //流水号
	$('#name').text(Data.patientName);              //姓名
	$('#names').val(Data.patientName);              //获取姓名
	$('#dutyNurseCode').text(Data.dutyNurseName);    //责任护士
	$('#dutyNurseName').val(Data.dutyNurseName);    //责任护士
	$('#dutyNurse').val(Data.dutyNurseCode);    //获取责任护士
	$('#sex').text(Data.reportSexName);		//性别
	$('#sexs').val(Data.reportSex);		//获取性别
	$('#houseDocCode').text(Data.houseDocName);  //住院医师
	$('#houseDocName').val(Data.houseDocName);    //责任护士

	$('#docCode').val(Data.houseDocCode);  //获取住院医师
	$('#inDate').text(Data.inDate);		//入院日期
	$('#inDates').val(Data.inDate);		//获取入院日期
	$('#chargeDocCode').text(Data.chargeDocName);  //主治医师
	$('#chargeDocName').val(Data.chargeDocName);    //责任护士

	$('#chargeDoc').val(Data.chargeDocCode);  //获取主治医师
	$('#paykindCode').text(payKindMap.get(Data.paykindCode));    //结算类别
	$('#paykind').val(Data.paykindCode);		//获取结算类别
	$('#chiefDocCode').text(Data.chiefDocName);  //主任医师
	$('#chiefDocName').val(Data.chiefDocName);    //责任护士

	$('#chiefDoc').val(Data.chiefDocCode);  //获取主任医师
	$('#deptName').text(Data.deptName);		//住院科室
	$('#deptNames').val(Data.deptCode);		//获取住院科室
	$('#bingchuanghao').text(Data.bedName);  //病床号
	$('#bedNames').val(Data.bedId);  //获取病床号
}
/**
 * 清除td的值
 */
function clearText(){
	$('#name').text("");              //姓名
	$('#dutyNurseCode').text("");    //责任护士
	$('#sex').text("");		//性别
	$('#houseDocCode').text("");  //住院医师
	$('#inDate').text("");		//入院日期
	$('#chargeDocCode').text("");  //主治医师
	$('#paykindCode').text("");		//结算类别
	$('#chiefDocCode').text("");  //主任医师
	$('#deptName').text("");		//住院科室
	$('#bingchuanghao').text("");  //病床号
	$('#deptCodess').text("");     //当前科室
	$('#bedIds').text("");    //当前病床号
}
/**
 * 查询病区树
 */
function searchDeptTree(){
	var queryDept=$('#queryDept').val();
	$.ajax({
		url : "<%= basePath %>nursestation/Transfer/searchTree.action",
		data:{queryName:queryDept},//通过ajax查询出树节点id,这里因为也可以输入汉字来查询科室名称,所以需要转码
		type : 'post',
		success : function(data) {
			if(data!=null&&data.length>0){
				var nodes = $('#tDt').tree('find',data[0].id);
				$('#tDt').tree('expandTo', nodes.target).tree('scrollTo',nodes.target).tree('select',nodes.target); 
			}
		}
	});
}
/**
 * 根据病历号查询
 */
function searchFrom() {
	var queryName = $('#queryName').val();
	if(queryName.length==0){
		$('#list').datagrid({
			url:'<%=basePath%>nursestation/Transfer/searchInpatientList.action?menuAlias=${menuAlias}'
		});
		$('#editForm').form('clear');
		$('#tDt').tree('reload');
		clearText();
		$("#list1").datagrid('reload',{
			deptId:0,
		});
		return;
	}
	else if(queryName == ''){
		$.messager.alert('提示','请输入病历号！');
		setTimeout(function(){
			$(".messager-body").window('close');
		},3500);
		return false;
	}
	$('#list').datagrid('load', {
		inpatientNo : queryName
	});
	$.ajax({
		url:"<%=basePath %>nursestation/Transfer/searchInpatient.action",
		data:{queryName:queryName},
		type:'post',
		success: function(Data) {
			if(Data.id==null){
				$.messager.alert('提示',"该患者还未住院");
				setTimeout(function(){
					$(".messager-body").window('close');
				},3500);
			}else{
				obtainValue(Data);
			}
		}
	});	
}
//渲染床位编制
function funBedOrgan(value,row,index){
	if(value!=null&&value!=''){
		return bedOrganMap.get(value);
	}
}

//根据map对象渲染病床状态
function formatCheckbedType(value,row,index){
	if(value!=null&&value!=''){
		return bedTypeMap.get(value);
	}
}
</script>
<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
</head>
<body style="margin: 0px; padding: 0px;">
<div class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'west',collapsible:false,split:true" style="width:480px;height: 100%;min-width: 280px;border-top:0">
		<div class="easyui-layout" data-options="fit:true,border:false">
			<div data-options="region:'north',border:false" style="width:100%;height: 46%">
				<div class="easyui-layout" data-options="fit:true,border:false">
					<div  data-options="region:'north',border:false" style="width:100%;height: 50px;">
						<table style="width: 100%;height: 100%">
							<tr>
								<td style="padding: 4px 15px">当前科室：<input class="easyui-combotree" ID="deptCode" style="width:170px"/></td>
							</tr>
						</table>
					</div>
					<div  data-options="region:'center'" style="width:100%;border-right:0">
						<table id="list" class="easyui-datagrid" data-options="singleSelect:'true',fit:true,pagination:true,pageSize:10,pageList:[10,20,30,50,100],rownumbers:true,border:false">   
							<thead>   
								<tr>
									<th data-options="field:'patientName'" width="33%" align="center">患者姓名</th>
									<th data-options="field:'bedName'" width="30%" align="center">病床</th>   
									<th data-options="field:'medicalrecordId'" width="33%" align="center">病历号</th> 
								</tr> 
							</thead> 
						</table>
					</div>
				</div>
			</div>
			<div data-options="region:'center',title:'转入的科室',border:false" style="height: 70%;width:100%;padding-top: 10px;border: 0px;">
				<div class="easyui-layout" data-options="fit:true" style="border: 0px;">
					<div  data-options="region:'north',border:false" style="width:100%;height: 40px;border: 0px;">
						<table style="width: 100%;height: 100%;border: 0px;">
							<tr>
								<td style="padding: 5px 10px"> 
									转入科室：<input  class="easyui-textbox" id="queryDept" data-options="prompt:'请输入护士站信息回车查询'" style="width:200px"/>
								</td>
							</tr>
						</table>
					</div>
					<div  data-options="region:'center',border:false" style="width:100%;">
						<ul id="tDt">加载数据...</ul>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div data-options="region:'center'" style="width:76%;height:100%;border-top:0">
		<div class="easyui-layout" data-options="fit:true,border:false">
			<div data-options="region:'north',border:false" style="width:100%;height: 50px">
				<table style="width: 100%; height: 100%">
					<tr>
						<td style="padding: 5px 15px">查询条件：<input  class="easyui-textbox" id="queryName" data-options="prompt:'请输入病历号查询'"/>
					  	<shiro:hasPermission name="${menuAlias}:function:query">
							&nbsp;&nbsp;<a href="javascript:void(0)" onclick="searchFrom()" class="easyui-linkbutton" iconCls="icon-search">查询</a>&nbsp;
						</shiro:hasPermission>
						<shiro:hasPermission name="${menuAlias}:function:readCard">
							<a href="javascript:void(0)" cardNo="" onclick="read_card_ic()" class="easyui-linkbutton" data-options="iconCls:'icon-bullet_feed'">读卡</a>&nbsp;
						</shiro:hasPermission>
			        	<shiro:hasPermission name="${menuAlias}:function:readIdCard">
			        		<a href="javascript:void(0)" cardNo="" onclick="read_card_sfz()" class="easyui-linkbutton" data-options="iconCls:'icon-bullet_feed'">读身份证</a>
						</shiro:hasPermission>
						</td>
					</tr>
				</table>
			</div>
			<div data-options="region:'center',border:false" style="height: 30%;width:100%;border-left:0px;">
				<form id="editForm" method="post" style="width: 100%;height:100%;">
					<table  id="div1" class="honry-table" style="width:100%;">
						<tr>
							<td colspan="8" style="border-left:0"><font style="font-weight: bold;font-size: 14px;">患者基本信息</font></td>
						</tr>
						<tr>
							<td class="honry-lable"  style="width: 10%;border-left:0">姓名：
								<input type="hidden" id="pid"  name="inpatientInfo.id">
								<input type="hidden" id="inpatientNo" name="inpatientInfo.inpatientNo"/>
								<input type="hidden" id="medicalrecordId" name="inpatientInfo.medicalrecordId"/>
								<input type="hidden" id="names" name="inpatientInfo.patientName"/>
								<input type="hidden" id="inState" name="inpatientInfo.inState"/>
								<input type="hidden" id="babyFlag" name="inpatientInfo.babyFlag"/>
								<input id="bedNo" name="inpatientInfo.bedNo" type="hidden">
								<input id="bedName" name="inpatientInfo.bedName" type="hidden">
								<input id="bedwardId" name="inpatientInfo.bedwardId" type="hidden">
							    <input id="bedwardName" name="inpatientInfo.bedwardName" type="hidden">
							</td>
							<td id="name"  style="width: 15%"></td>
							<td class="honry-lable" style="width: 10%">责任护士：<input id="dutyNurse" type="hidden" name="inpatientInfo.dutyNurseCode"/></td>
							<td id="dutyNurseCode"  style="width: 15%"></td>
							<input id="dutyNurseName"  name ="inpatientInfo.dutyNurseName" type="hidden"/>
							<td class="honry-lable" style="width: 10%">性别：<input id="sexs" type="hidden" name="inpatientInfo.reportSex"/></td>
							<td id="sex"  style="width: 15%"></td>
							<td class="honry-lable" style="width: 10%">住院医师：<input id="docCode" type="hidden"  name="inpatientInfo.houseDocCode"/></td>
							<td id="houseDocCode"  style="width: 15%"></td>
							<input id="houseDocName" name = "inpatientInfo.houseDocName" type="hidden"/>
						</tr>
						<tr>
							<td class="honry-lable"  style="width: 10%;border-left:0">入院日期：<input id="inDates" type="hidden" name="inpatientInfo.inDate"/></td>
							<td id="inDate" style="width: 15%"></td>
							<td class="honry-lable" style="width: 10%">主治医师：<input id="chargeDoc" type="hidden" name="inpatientInfo.chargeDocCode"/></td>
							<td id="chargeDocCode"   style="width: 15%"></td>
							<input id="chargeDocName"  name="inpatientInfo.chargeDocName" type="hidden"/>
							<td class="honry-lable" style="width: 10%">结算类别：<input id="paykind" type="hidden" name="inpatientInfo.paykindCode" /></td>
							<td id="paykindCode" style="width: 15%"></td>
							<td class="honry-lable" style="width: 10%">主任医师：<input id="chiefDoc" type="hidden" name="inpatientInfo.chiefDocCode"/></td>
							<td id="chiefDocCode"   style="width: 15%"></td>
							<input id="chiefDocName"  name="inpatientInfo.chiefDocName" type="hidden"/>
						</tr>
						<tr>
							<td class="honry-lable" style="width: 10%;border-left:0">住院科室：<input id="deptNames" type="hidden" /></td>
							<td id="deptName" style="width: 15%"></td>
							<td class="honry-lable" style="width: 10%">病床号：<input id="bedNames" type="hidden"/></td>
							<td id="bingchuanghao" colspan="5" style="width: 15%"></td>
						</tr>
						<tr>
							<td colspan="8" style="border-left:0"><font style="font-weight: bold;font-size: 14px;">患者转入科室和病床</font></td>
						</tr>
						<tr>
							<td class="honry-lable" style="width: 10%;border-left:0">当前病区：<input type="hidden" id="deptCodess" name="inpatientInfo.nurseCellCode"></td>
							<td style="width: 15%"><input class="easyui-textbox" id="deptCodes"  name="inpatientInfo.nurseCellName" data-options="required:true,editable:false"/></td>
							<td class="honry-lable" style="width: 10%">当前科室：</td>
							<td style="width: 15%"><input  class="easyui-combobox" id="lesionDept" name ="inpatientInfo.deptCode" data-options="required:true,editable:false"/>
								<input type="hidden" id="inpatientdeptName" name="inpatientInfo.deptName">
							</td>
							<td class="honry-lable" style="width: 10%">当前病床：<input type="hidden" id="bedIds" name="inpatientInfo.bedId"></td>
							<td style="width: 15%"><input class="easyui-textbox" id="bedId" data-options="required:true,editable:false">
							</td>
							<td colspan="2">
								<shiro:hasPermission name="${menuAlias}:function:save">
									&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:submit();" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保&nbsp;存&nbsp;</a>
								</shiro:hasPermission>
							</td>
						</tr>
					</table>
				</form>
			</div>
			<div data-options="region:'south'" style="height: 68%;width:100%;border-left: 0px;">
				<table id="list1" class="easyui-datagrid" style="border: 0px;" data-options="fitColumns:true,singleSelect:true,fit:true,border:false,rownumbers:true,pagination:true,pageSize:20,pageList:[20,30,50,80,100]">   
					<thead>
						<tr>
							<th data-options="field:'bedName'" width="20%" align="center">病床号</th>
							<th data-options="field:'bedOrgan',formatter:funBedOrgan" width="20%" align="center">属性</th>
							<th data-options="field:'bedLevel'" width="20%" align="center">等级</th>
							<th data-options="field:'bedwardName'" width="20%" align="center">病房</th>
							<th data-options="field:'bedState',formatter:formatCheckbedType" width="15%" align="center" >状态</th>
						</tr>
					</thead>
				</table>
			</div>
		</div>
	</div>
</div>
</body>
</html>