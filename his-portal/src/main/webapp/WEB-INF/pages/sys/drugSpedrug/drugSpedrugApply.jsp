<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>特限药品审批</title>
<%@ include file="/common/metas.jsp"%>

<style type="text/css">
.window .panel-header .panel-tool a{
  	  	background-color: red;	
	}
</style>
<script>
		var deptMap="";//科室
		var empMap="";//医生
		var ageUnit=null;//保存年龄
		var drugCodeAndName=new Map();//药品渲染
		//默认加载
		$(function(){
			var first='${newdate}';
			var second= '${newdate1}';
			$('#first').val(first);
			$('#second').val(second);
			$('#div1').hide();
			$('#div2').hide();
				 //初始化下拉框
				 //申请医生
				 $('#applicDoctor') .combobox({    
					    url:"<%=basePath%>inpatient/permission/queryLoginUserDept.action",    
					    valueField:'jobNo',    
					    textField:'name',
					    multiple:false,
					    editable:false
		   		 });
				 //审批医生
				 $('#examDoctor') .combobox({    
					 url:'<%=basePath%>publics/consultation/queryLoginUserDept.action',    
					    valueField:'jobNo',    
					    textField:'name',
					    mode:'remote',
					    onLoadSuccess:function(){
					    	queryList();
					    }
		   		 });
				 $('#CodeUseage').combobox({
					 	url: "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action?type=useage'/>", 
					    valueField:'encode',     
					    textField:'name'
				});	
				 //科室
				$.ajax({
					url: "<%=basePath%>publics/consultation/querydeptComboboxs.action", 
					success: function(deptData) {
						deptMap = deptData;
					}
				});
				//医生
				$.ajax({
					url: "<%=basePath%>inpatient/permission/queryLoginUserDept.action",
					async:false,
					success: function(empData) {
						empMap = empData;
					}
				});
				//药品
				$.ajax({
					url: "<%=basePath%>publics/drugSpedrug/queryDrugInfoCodeAndName.action",
					async:false,
					success: function(data) {
						for(var i=0;i<data.length;i++){
							drugCodeAndName.put(data[i].code,data[i].name);
						}
					}
				});
				$('#radio1').prop("checked","checked");
				$('#isexam').combobox({
					    data:[{"id":0,"text":"是"},{	"id":1,"text":"否"}],    
					    valueField:'id',    
					    textField:'text',
					    multiple:false
				 });
				$('#isbacterial').combobox({
					    data:[{"id":0,"text":"是"},{	"id":1,"text":"否"}],    
					    valueField:'id',    
					    textField:'text',
					    multiple:false
			    });
				$('#issensitive').combobox({
					    data:[{"id":0,"text":"是"},{	"id":1,"text":"否"}],     
					    valueField:'id',    
					    textField:'text',
					    disabled:false,
					    multiple:false

				});	
				$('#purpose').combobox({
						data:[{"id":1,"text":"预防性"},{	"id":2,"text":"治疗性"},{"id":3,"text":"经验性用药"}],
						valueField:'id',    
					    textField:'text',
						width:150,
				});
				 $('#table1').datagrid({
				 })
				 //设置审批医生为当前登录医生
				 $('#examDoctor').combobox('select','${empId}');
		});
	   //从xml文件中解析，读到下拉框//只有性别一个,所以修改成性别专用
		function idCombobox(param){
			$('#'+param).combobox({
			    url : "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action'/>?type=sex ",
			    valueField:'encode',    
			    textField:'name',
			    multiple:false
			});
		}	
		/**
		*表单提交submit信息
	  	*/
		function submit(flag){
			var age1= $('#age1').textbox('getText');
			$('#age').val(age1.substr(0,age1.length-1));
			if(!$('#editForm').form('validate')){
		    	 $.messager.alert('提示','请补全信息后保存！');
		    	 close_alert();
		    	 return false;
		     } 
		  	$('#editForm').form('submit',{
		  		 url:'<%=basePath%>publics/drugSpedrug/saveOrUpdateDrugSpedrug.action?type='+flag,
 		  		 onSubmit:function(){ 
 		  			 $.messager.progress({text:'保存中，请稍后...',modal:true});
 		  			 return $(this).form('validate');
 				 },  
				success:function(data){
					$.messager.progress('close');
					if(data!=null&&data!=''&&data!='undefind'&&data=='true'){
					  $.messager.alert('提示','审批成功');
					  //实现刷新
					  $('#editForm').form('clear');
					  $('#saveId').hide();
					  $('#wuxiaoId').hide();
					  queryList();
				   }else{
					   $.messager.progress('close');
					   $.messager.alert('提示','药品【'+drugCodeAndName.get(data)+'】为特限管理类特限药,您申请的特限药品暂时未通过审核,请向副高级以上医师提交申请');
					   close_alert();
				   }
				 },
				error:function(data){
					$.messager.alert('提示',"审批失败");
				}
		  	});

	  	}
		function queryList(){
			var date1=$('#first').val();
			var date2=$('#second').val();
			if(date1==''||date2==''){
				$.messager.alert('提示','请选择完整的时间');
				close_alert();
				return;
			}
			var state=null;
			if($('#radio1').is(":checked")){
				$('#div1').show();
				$('#div2').hide();
				state="1";
				
				$('#div1').show();
				$('#div2').hide();
				$('#table1').datagrid({
					url:'<%=basePath%>publics/drugSpedrug/queryDatagridT1.action',
					queryParams:{date1:date1,date2:date2,state:state},
					columns:[[		
					            {field:'name',title:'姓名',width:'15%',align:'center'} ,
					            {field:'patientDept',title:'患者科室',width:'20%',align:'center',formatter:functionDept} ,
					            {field:'applicDoctor',title:'申请医生',width:'20%',align:'center',formatter:functionEmp} ,
					            {field:'createTime',title:'申请时间',width:'20%',align:'center'} ,
					            {field:'drugCode',title:'药品名称',width:'25%',align:'center',formatter:function(value,row,index){
					            	
					            	if(drugCodeAndName.get(value)){
										return drugCodeAndName.get(value);
									}
					            	if(row.drugName){
					            		return row.drugName;
					            	}
					            	return value;
								}} ]],
					 onDblClickRow:function(rowIndex,rowData){
						    clear();
						 	$('#mainId').val(rowData.id);
					 	 	$('#patientDept').textbox('setValue',deptMap[rowData.patientDept]);
					 	 	$('#name').textbox('setValue',rowData.name);
					 	 	$('#CodeSex').combobox('setValue',rowData.sex);
					 	 	$('#diagnose').textbox('setValue',rowData.diagnose);
					 	 	$('#drugCode').val(rowData.drugCode);
					 	 	if(rowData.drugName!=null){
					 	 		$('#drugName').textbox('setValue',rowData.drugName);	
					 	 	}else{
					 	 		$('#drugName').textbox('setValue',drugCodeAndName.get(rowData.drugCode));
					 	 	}
					 	 	$('#spec').textbox('setValue',rowData.spec);
					 	 	$('#CodeUseage').combobox('setValue',rowData.usage);
					 	 	$('#dosage').textbox('setValue',rowData.dosage);
					 	 	$('#purpose').combobox('setValue',rowData.purpose);
					 	 	$('#drugBased').textbox('setValue',rowData.drugBased);
					 	 	$('#infectiondiagnosis').textbox('setValue',rowData.infectiondiagnosis);
					 	 	$('#isexam').combobox('setValue',rowData.isexam);
					 	 	$('#sampleType').textbox('setValue',rowData.sampleType);
					 	 	$('#issensitive').combobox('setValue',rowData.issensitive);
					 	 	$('#isbacterial').combobox('setValue',rowData.isbacterial);
					 	 	$('#bacterial').textbox('setValue',rowData.bacterial);
					 	 	if(empMap[rowData.applicDoctor]){
					 	 		$('#applicDoctor').combobox('setValue',empMap[rowData.applicDoctor]);
					 	 	}else{
					 	 		$('#applicDoctor').combobox('setValue',rowData.applicDoctor);
					 	 	}
					 	 	$('#applyReason').textbox('setValue',rowData.applyReason);
					 	 	$('#groupOpinion').textbox('setValue',rowData.groupOpinion);
							$('#useddrug').textbox('setValue',rowData.useddrug);
							$('#clinicCode').textbox('setValue',rowData.clinicCode);
							//根据病历号查询患者年龄单位
					 		var inDate=null;
							var bedNo=null;
							$.ajax({
							url: '<%=basePath%>publics/drugSpedrug/querylistInpatientInfo.action?medId='+rowData.clinicCode,
								async:false,
								success: function(data) {
									if(data.inDate){
									$('#inDate').val(data.inDate);
									}else{
									$('#inDate').attr('readonly',false)
									}
									if(data.bedName){
										$('#bedNo').textbox('setValue',data.bedName);
									}else{
										$('#bedNo').textbox({
											readonly:false
										});
									}
							 	 	//如果年龄查询失败  年龄复选框可输入
							 	 	if(data.reportBirthday){
							 	 	ageUnit=DateOfBirth(data.reportBirthday);
							 	 	$('#age1').textbox('setValue',ageUnit.get('nianling')+ageUnit.get('ageUnits'));
							 	 	}else{
							 	 		$('#age1').textbox({
							 	 			readonly:false,
							 	 			required:true
							 	 		});
							 	 	}
										}
									});
// 					 	 	$('#bedNo').textbox('setValue',rowData.bedNo);
					 		//病床号
							
					 	 	$('#saveId').show();
					 	 	$('#wuxiaoId').show();
					 }
				});
			}else if($('#radio2').is(":checked")){
				state="2";
				$('#div2').show();
				$('#div1').hide();
				$('#table2').datagrid({
					url:'<%=basePath%>publics/drugSpedrug/queryDatagridT1.action',
					queryParams:{date1:date1,date2:date2,state:state},
					columns:[[
					            {field:'name',title:'姓名',width:'15%',align:'center'} ,
					            {field:'patientDept',title:'患者科室',width:'20%',align:'center',formatter:functionDept} ,
					            {field:'examDoctor',title:'审批医生',width:'20%',align:'center',formatter:functionEmp} ,
					            {field:'examDate',title:'审批时间',width:'20%',align:'center'} ,
					            {field:'drugCode',title:'药品名称',width:'25%',align:'center',formatter:function(value,row,index){
					            	if(drugCodeAndName.get(value)){
										return drugCodeAndName.get(value);
									}
					            	if(row.drugName){
					            		return row.drugName;
					            	}
					            	return value;
									
								}} 
					]],
					 onDblClickRow:function(rowIndex,rowData){
						 	clear();
						 	$('#mainId').val(rowData.id);
					 	 	$('#patientDept').textbox('setValue',deptMap[rowData.patientDept]);
					 	 	$('#name').textbox('setValue',rowData.name);
					 	 	$('#CodeSex').combobox('setValue',rowData.sex);
					 	 	$('#diagnose').textbox('setValue',rowData.diagnose);
					 	 	$('#drugCode').val(rowData.drugCode);
					 	 	if(rowData.drugName!=null){
					 	 		$('#drugName').textbox('setValue',rowData.drugName);	
					 	 	}else{
					 	 		$('#drugName').textbox('setValue',drugCodeAndName.get(rowData.drugCode));
					 	 	}
					 	 	$('#spec').textbox('setValue',rowData.spec);
					 	 	$('#CodeUseage').combobox('setValue',rowData.usage);
					 	 	$('#dosage').textbox('setValue',rowData.dosage);
					 	 	$('#purpose').combobox('setValue',rowData.purpose);
					 	 	$('#drugBased').textbox('setValue',rowData.drugBased);
					 	 	$('#infectiondiagnosis').textbox('setValue',rowData.infectiondiagnosis);
					 	 	$('#isexam').combobox('setValue',rowData.isexam);
					 	 	$('#sampleType').textbox('setValue',rowData.sampleType);
					 	 	$('#issensitive').combobox('setValue',rowData.issensitive);
					 	 	$('#isbacterial').combobox('setValue',rowData.isbacterial);
					 	 	$('#bacterial').textbox('setValue',rowData.bacterial);
					 	 	$('#useddrug').textbox('setValue',rowData.useddrug);
					 	 	if(empMap[rowData.applicDoctor]){
					 	 		$('#applicDoctor').combobox('setValue',empMap[rowData.applicDoctor]);
					 	 	}else{
					 	 		$('#applicDoctor').combobox('setValue',rowData.applicDoctor);
					 	 	}
					 	 	$('#applyReason').textbox('setValue',rowData.applyReason);
					 	 	$('#groupOpinion').textbox('setValue',rowData.groupOpinion);
					 	 	if(empMap[rowData.examDoctor]){
					 	 		$('#examDoctor').combobox('setValue',empMap[rowData.examDoctor]);
					 	 	}else{
					 	 		$('#examDoctor').combobox('setValue',rowData.examDoctor);
					 	 	}
					 	 	$('#examOpinion').textbox('setValue',rowData.examOpinion);
					 	 	$('#examDoctor2').textbox('setValue',rowData.examDoctor2);
					 	 	$('#examOpinion2').textbox('setValue',rowData.examOpinion2);
					 	 	$('#clinicCode').textbox('setValue',rowData.clinicCode);
					 	 	$.ajax({
								url: '<%=basePath%>publics/drugSpedrug/querylistInpatientInfo.action?medId='+rowData.clinicCode,
									async:false,
									success: function(data) {
										if(data.inDate){
										$('#inDate').val(data.inDate);
										}else{
										$('#inDate').attr('readonly',false);
										}
										if(data.bedName){
											$('#bedNo').textbox('setValue',data.bedName);
										}else{
											$('#bedNo').textbox({
												readonly:false
											});
										}
								 	 	//如果年龄查询失败  年龄复选框可输入
								 	 	if(data.reportBirthday){
								 	 	ageUnit=DateOfBirth(data.reportBirthday);
								 	 	$('#age1').textbox('setValue',ageUnit.get('nianling')+ageUnit.get('ageUnits'));
								 	 	}else{
								 	 		$('#age1').textbox({
								 	 			readonly:false,
								 	 			required:true
								 	 		});
								 	 	}
											}
										});
					 	 	$('#saveId').hide();
					 	 	$('#wuxiaoId').hide();
					 }
				});
			}else{
				$.messager.alert('提示',"请选择要查询已审或查询未审");
				close_alert();
				return false;
			}
		}
		//清除所填信息
		function clear() {
			$('#editForm').form('clear');
			$('#examDoctor').combobox('setValue','${empId}');
		}
		//渲染科室
		function functionDept(value,row,index){
			if(value!=null&&value!=''){
				return deptMap[value];
			}
		}
		//渲染医生
		function functionEmp(value,row,index){
			if(value!=null&&value!=''){
				if(!empMap[value]){
					return value;
				}
				return empMap[value];
			}
		}
		//添加弹出框   将 win声明为全局变量其他窗口才能调用
		var win;	
		function showWin ( title,url, width, height) {
		//下面是调用方法的栗子  
		   	var content = '<iframe id="myiframe" src="' + url + '" width="100%" height="100%" frameborder="0" scrolling="no"></iframe>';
		    var divContent = '<div id="treeDeparWin">';
		    win = $('<div id="treeDeparWin"><div/>').dialog({
		        content: content,
		        width: width,
		        height: height,
		        modal: true,
		        minimizable:false,
		        maximizable:true,
		        resizable:true,
		        shadow:true,
		        center:true,
		        title: title
		    });
		    win.dialog('open');
		}
		
		// 列表查询重置
		function searchReload() {
			first='${newdate}';
			second='${newdate1}';
			$('#first').val(first);
			$('#second').val(second);
			$('#radio1').prop("checked","checked");
			queryList();
			$('#editForm').form('clear');
			$('#examDoctor').combobox('select','${empId}');
			$('#saveId').hide();
			$('#wuxiaoId').hide();
		}
</script>
<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
<style type="text/css">
.panel-header{
	border-top:0
}
</style>
</head>
<body>
<div class="easyui-layout" id="treeLayOut" data-options="fit:true">
<!--	左边树-->
			<div data-options="region:'north',title:'特限药申请(审核)记录'" style="width:100%;height: 29%;">
		    	<div id="treeDiv" class="easyui-layout" data-options="fit:true" style="padding:2px;">
					<div data-options="region:'north',border:false" style="width: 100%;height: 35px;" >
						<table id="table3" style="padding:3px 2px 2px 3px;" >
							<tr>
								<td class="DrugSpedrugApply">
									<input type="radio" id="radio1" name="radio1" onclick="queryList()"/>未审&nbsp;<input type="radio" id="radio2" name="radio1" onclick="queryList()"/>已审
									&nbsp;
									日期：
									<input id="first" class="Wdate" type="text" onClick="WdatePicker({maxDate:'%y-%M-%d-%H-%m-%s',dateFmt:'yyyy-MM-dd HH:mm:ss'})"  style="width:170px;border: 1px solid #95b8e7;border-radius: 5px;" readonly="readonly"/>
									-
									<input id="second" class="Wdate" type="text" onClick="WdatePicker({maxDate:'%y-%M-%d-%H-%m-%s',dateFmt:'yyyy-MM-dd HH:mm:ss'})"  style="width:170px;border: 1px solid #95b8e7;border-radius: 5px;" readonly="readonly"/>
									<a href="javascript:void(0)"  class="easyui-linkbutton" onclick="queryList()" data-options="iconCls:'icon-search'">查询</a>
									<a href="javascript:void(0)" onclick="searchReload()" class="easyui-linkbutton" iconCls="reset">重置</a>
									<a id="saveId"  href="javascript:submit(1);" style="display: none;"  class="easyui-linkbutton" data-options="iconCls:'icon-save'">审批</a>
									<a id="wuxiaoId"  href="javascript:submit(2);" style="display: none;"  class="easyui-linkbutton" data-options="iconCls:'icon-wuxiao'">无效</a>
								</td>
							</tr>
						</table>
					</div>
					<div  data-options="region:'center',border:false" style="width: 100%;">
						<div id="div1" style="width: 100%;height: 100%;display: none">
							<table id="table1"  class="easyui-datagrid"  data-options="fit:true,singleSelect:true">
							</table>
						</div>
							<div id="div2" data-options="fit:true" style="width: 100%;height: 100%;display: none">
								<table id="table2"  class="easyui-datagrid"  data-options="fit:true,singleSelect:true">
								</table>
 							</div> 
					</div>
			
				</div>
		   </div>   
<!--	右边面板-->
		<div  data-options="title:'特限药申请（审核）信息',iconCls:'icon-form',region:'center'"  style="width:100%;height:71%;padding: 4px;" id="list">
		<form id="editForm"  method="post" style="width: 100%;height: 100%;overflow: auto;">
			<input type="hidden" id="mainId" name="id" >
				<table class="honry-table" cellpadding="0" cellspacing="0" border="0" style="width: 100%;height: 100%;overflow: auto;" >
					<tr>
						<td colspan="8">
						患者基本信息
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							患者姓名：
						</td>
						<td >
							<input id="name" class="easyui-textbox" name="name" readonly="readonly"/>
						</td>
						<td  class="honry-lable">
							性别：
						</td>
						<td >
							<input  id="CodeSex" class="easyui-combobox" name="sex" readonly="readonly" data-options="url : '<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action'/>?type=sex ',valueField:'encode',textField:'name'"/>
						</td>
						<td  class="honry-lable">
							年龄：
						</td>
						<td  >
							<input id="age1" class="easyui-textbox"    readonly="readonly"/>
							<input id="age" name="age" type="hidden">
						</td>
					</tr>
					<tr>
						<td   class="honry-lable">
						 科室：
						</td>
						<td >
							<input id="patientDept" class="easyui-textbox" name="patientDept" readonly="readonly"   />
						</td>
						<td class="honry-lable">
							床号：
						</td>
						<td >
							<input class="easyui-textbox" name="bedNo" id="bedNo"   readonly="readonly"/>
						</td>
						<td  class="honry-lable">
							看诊号\住院流水号：
						</td>
						<td >
							<input id="clinicCode" class="easyui-textbox" name="clinicCode"   readonly="readonly"/>
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							入院时间：
						</td>
						<td class="DrugSpedrugApplyDateSize">
							<input id="inDate" class="Wdate" name="inDate" type="text" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH',maxDate:'%y-%M-%d'})"  style="width:172px;border: 1px solid #95b8e7;border-radius: 5px;"  readonly="readonly"/>
						</td>
						<td class="honry-lable">
							入院诊断：
						</td>
						<td colspan="3">
							<input id="diagnose"  class="easyui-textbox" name="diagnose" style="width: 73%" readonly="readonly"/>
						</td>
					</tr>
					<tr>
						<td colspan="8">
						药品信息
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							药品名称：
						</td>
						<td >
							<input id="drugName" class="easyui-textbox" name="drugName"  readonly="readonly"/>
							<input  type="hidden" id="drugCode" name="drugCode">
						</td>
						<td class="honry-lable">
							规格：
						</td>
						<td >
						<input id="spec"class="easyui-textbox" name="spec"   readonly="readonly"/>
						</td>
						<td class="honry-lable">
							用法：
						</td>
						<td  >
						<input id="CodeUseage" name="usage" class="easyui-combobox"    readonly="readonly"/>
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							用量：
						</td>
						<td >
						<input id="dosage" class="easyui-textbox" name="dosage"  readonly="readonly"/>
						</td>
						<td class="honry-lable">
							用药目的：
						</td>
						<td >
							<input id="purpose" name="purpose" class="easyui-combobox"  readonly="readonly"/> 
						</td>
						<td class="honry-lable">
							用药依据：
						</td>
						<td >
							<input id="drugBased" name="drugBased" class="easyui-textbox" readonly="readonly"/>
						</td>
					</tr>
					<td colspan="8">
					患者基本病情
					</td>
					<tr>
						<td class="honry-lable">
							已送病院学检查：
						</td>
						<td >
							<input id="isexam"  name="isexam" class="easyui-combobox"  readonly="readonly"/>
						</td>
						<td class="honry-lable">
							样本类型：
						</td>
						<td>
							<input id="sampleType" class="easyui-textbox" name="sampleType" readonly="readonly"/>
						</td>
						<td class="honry-lable" rowspan="2">
							所申请药物对该病原菌过敏感：
						</td>
						<td  rowspan="2">
							<input id="issensitive"  name="issensitive" class="easyui-combobox" readonly="readonly"/>
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							已有细菌培养及药敏结果：
						</td>
						<td >
							<input  id="isbacterial" name="isbacterial" class="easyui-combobox" readonly="readonly"/>
						</td>
						<td class="honry-lable">
							请填写病菌：
						</td>
						<td >
						<input id="bacterial" name="bacterial" class="easyui-textbox" readonly="readonly" />
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							申请医师：
						</td>
						<td >
							<input id="applicDoctor" class="easyui-combobox" name="applicDoctor"   readonly="readonly" />
						</td>
						<td class="honry-lable">
							申请依据：
						</td>
						<td colspan="3">
							<input id="applyReason" style="width: 73%" class="easyui-textbox" name="applyReason" readonly="readonly" />
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							感染诊断或可能感染诊断：
						</td>
						<td colspan="5">
							<input id="infectiondiagnosis" style="width:84%" class="easyui-textbox" name="infectiondiagnosis"   readonly="readonly"/>
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							住院期间已使用抗菌药物：
						</td>
						<td colspan="5">
							<input id="useddrug" style="width:84%" class="easyui-textbox" name="useddrug"  readonly="readonly" />
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							特殊使用级抗菌药物专家组意见：
						</td>
						<td colspan="5">
						<input id="groupOpinion" style="width:84%" class="easyui-textbox" name="groupOpinion"  readonly="readonly" />
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							审批医生：
						</td>
						<td >
							<input id="examDoctor" class="easyui-combobox" name="examDoctor" value="${empId}"  data-options="required:true"/>
						</td>
						<td class="honry-lable">
							审批意见：
						</td>
						<td colspan="5">
							<input  id="examOpinion" style="width:84%;" class="easyui-textbox" name="examOpinion" data-options="required:true" />
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							医务科主任签字：
						</td>
						<td >
							<input id="examDoctor2" class="easyui-textbox" name="examDoctor2" data-options="required:true"/>
						</td>
						<td class="honry-lable">
							医务科意见：
						</td>
						<td colspan="5">
							<input  id="examOpinion2" style="width:84%" class="easyui-textbox" name="examOpinion2" data-options="required:true"/>
						</td>
					</tr>
				</table>
				</form>
		</div>
	</div>
</body>
</html>