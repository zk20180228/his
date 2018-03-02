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
</head>
<body>
<div class="easyui-layout" style="width:100%;height:100%;">
<div class="easyui-layout" style="width:10%;height:100%;" data-options="region:'west',split:true">
	<div id="p" data-options="region:'west'" title="患者管理">
			<ul id="tDt"></ul><ul id="tDt1"></ul>
	</div>
</div>
<div  class="easyui-layout"style="width:80%;height:100%;"  data-options="region:'center',split:true">  
    <div data-options="region:'center',title:'会诊单审核',split:true" style="width:80%;height:50%;">
    	<form id="editform" method="post">
   			<input type="hidden" id="createUser" name="createUser" value="${deptcontact.createUser  }">
			<input type="hidden" id="createDept" name="createDept" value="${deptcontact.createDept  }">
			<input  type="hidden" id="createTime" name="createTime" value="${deptcontact.createTime}"/>
    		<div>
				<div style="text-align: center;width: 80%;padding:0px 0px 0px 115px;">
					<table style="text-align: center;width: 100%;">
						<tr>
							<td><font size="7">会诊单审核</font></td>
							<td>
								<table>
									<tr>
										<td >
											<input type="hidden" id="bedNo" name="bedNo" >
											<input type="hidden" id="idid" name="id" >
											<input type="hidden" id="patientNo" name="patientNo" >
											<input id="urgentFlag" type="hidden" name="urgentFlag" value="${urgentFlag}" >
											<input type="radio" name="urgentFlagq" id="putong" onclick="javascript:onclickurgentFlag(0)">普通会诊
											<input type="radio" name="urgentFlagq" id="jinji" onclick="javascript:onclickurgentFlag(1)">紧急会诊
										</td>
									</tr>
									<tr>
										<td ><input type="checkbox" name="cnslKind" id="cnslKind" value="${cnslKind}" onclick="javascript:onclickCnslKind()">院外会诊
											<input type="checkbox" name="createOrderFlag" id="createOrderFlag" value="${createOrderFlag}" onclick="javascript:onclickCreateOrderFlag()">能否开立医嘱
										</td>
									</tr>
								</table>
							</td>
							</tr>
					</table>
				</div>
				<div style="width:80%;height:40%;border: 0px inset;">
					<table class="honry-table" cellpadding="1" cellspacing="1" style="width:80%;">
						<tr>
							<td class="honry-lable">患者姓名：</td>
							<td>
								<input id="patientName" class="easyui-textbox" data-options="required:true" readonly="readonly"/>
							</td>
							<td class="honry-lable">性别：</td>
							<td>
								<input id="reportSex" class="easyui-combobox" readonly="readonly" data-options="url : "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action'/>?type=sex ",valueField:'encode',textField:'name',required:true"/>
							</td>
							<td class="honry-lable">年龄：</td>
							<td>
								<input id="reportAge" readonly="readonly" class="easyui-textbox"/>
							</td>
						</tr>
						<tr>
							<td class="honry-lable">会诊地点：</td>
							<td>
								<input id="location" class="easyui-textbox" name="location"/>
							</td>
							<td class="honry-lable">会诊科室：</td>
							<td>
								<input id="cnslDeptcd"  name="cnslDeptcd"/>
								<a href="javascript:delSelectedData('cnslDeptcd');"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a>
							</td>
							<td class="honry-lable">会诊医师：</td>
							<td>
								<input id="cnslDoccd" name="cnslDoccd"/>
								<a href="javascript:delSelectedData('cnslDoccd');"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a>
							</td>
						</tr>
						<tr>
							<td class="honry-lable">会诊日期：</td>
							<td>
								<input id="cnslDate" class="easyui-DateTimeBox" name="cnslDate"/>
							</td>
							
							<td class="honry-lable">截止日期：</td>
							<td>
								<input id="moEddt" class="easyui-DateTimeBox" name="moEddt"/>
							</td>
							
							<td class="honry-lable">申请科室：</td>
							<td>
								<input id="deptCode"  name="deptCode"/>
								<a href="javascript:delSelectedData('deptCode');"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a>
							</td>
						</tr>
						<tr>
							<td class="honry-lable">申请医师：</td>
							<td> 
								<input id="docCode" name="docCode"/>
								<a href="javascript:delSelectedData('docCode');"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a>
							</td>
							<td class="honry-lable">申请日期：</td>
							<td>
								<input id="applyDate" class="easyui-DateTimeBox"  name="applyDate"/>
							</td>
							<td class="honry-lable">授权日期：</td>
							<td>
								<input id="moStdt" class="easyui-DateTimeBox" name="moStdt"/>
							</td>
						</tr>
						<tr>
							<td class="honry-lable">实际会诊日：</td>
							<td colspan="5"> 
								<input id="cnslExdt" class="easyui-datebox" name="cnslExdt" data-options="required:true"/>
							</td>
						</tr>
						<tr>
							
							<td class="honry-lable">会诊意见：</td>
							<td colspan = "5">
								<input  id="cnslNote" name="cnslNote" class="easyui-textbox" data-options="multiline:true"  style="width:750px;height:150px">
							</td>
						</tr>
						<tr>
							<td class="honry-lable">会诊结果：</td>
							<td  colspan="5">
								<input id="cnslRslt" class="easyui-textBox"  name="cnslRslt" data-options="multiline:true,required:true"   style="width:750px;height:150px"/>
							</td>
						</tr>
						<tr>
							<td class="honry-lable">检查结果：</td>
							<td  colspan="5">
								<input id="cnslNote2" name="cnslNote2"  class="easyui-textBox"  data-options="multiline:true,required:true"  style="width:750px;height:150px"/>
							</td>
						</tr>
						<tr>
							<td class="honry-lable">会诊记录：</td>
							<td colspan="5"> 
								<input id="cnslRecord" name="cnslRecord"  class="easyui-textBox" data-options="multiline:true,required:true"  style="width:750px;height:150px"/>
							</td>
						</tr>
						<tr>
							<td class="honry-lable">初步诊断意见：</td>
							<td colspan="5">
								<input id="cnslNote3" name="cnslNote3" class="easyui-textBox" data-options="multiline:true,required:true"  style="width:750px;height:150px"/>
							</td>
						</tr>
					</table>
					<table style="text-align:center; width: 70%;">
						<tr>
							<td style="align:'right'">
							<shiro:hasPermission name="${menuAlias}:function:check">
								<a href="javascript:auditing()" class="easyui-linkbutton" data-options="iconCls:'icon-save'">审核</a>&nbsp;&nbsp;&nbsp;&nbsp;
							</shiro:hasPermission>
								<a href="javascript:clear()"  class="easyui-linkbutton" data-options="iconCls:'icon-clear'" >取消审核</a>
							</td>
						</tr>
					</table>
				</div>
				
			</div>
		</form>
   	</div>   
	<div  data-options="region:'south',title:'会诊记录',split:true" style="height:35%;">
		<input type="hidden" id="status" name="status"/>
		<table id="listlist" class="easyui-datagrid" style="width:100%" data-options="fitColumns:true,singleSelect:true">   
			<thead>   
		        <tr>   
		            <th data-options="field:'cnslDeptcd',width:100,align:'center',formatter:functionDept">会诊科室</th>   
		            <th data-options="field:'cnslDoccd',width:100,align:'center',formatter:functionEmp">会诊专家</th>   
		            <th data-options="field:'docCode',width:100,align:'center',formatter:functionEmp">申请人</th>   
		            <th data-options="field:'applyDate',width:100,align:'center'">申请日期</th>   
		            <th data-options="field:'cnslNote3',width:200,align:'center'">会诊原因及病理诊断</th>   
		            <th data-options="field:'cnslRslt',width:100,align:'center'">会诊结果</th>   
		            <th data-options="field:'cnslStatus',width:100,align:'center',formatter:functionStatus">状态</th>   
		            <th data-options="field:'createOrderFlag',width:100,align:'center',formatter:functionOrderFlag">能否开立医嘱</th>   
		            <th data-options="field:'confirmDoccd',width:100,align:'center',formatter:functionEmp">审核人</th>   
		        </tr>   
			</thead>   
		</table>  
	</div> 
</div> 
</div>
<script type="text/javascript">
	var empMap="";			//员工Map对象
	var deptMap="";			//部门Map对象
	var empId="${sName}";
	var sexMap=new Map();
	$(function(){	
		function onclickurgentFlag(id){
			if($('#putong').is(':checked')){
				$('#urgentFlag').val(0);
			}else{
				$('#urgentFlag').val(1);
			}
		}
		function onclickCnslKind(){
			if($('#cnslKind').is(':checked')){
				$('#cnslKind').val(2);
			}else{
				$('#cnslKind').val(1);
			}
		}
		function onclickCreateOrderFlag(){
			if($('#createOrderFlag').is(':checked')){
				$('#createOrderFlag').val(1);
			}else{
				$('#createOrderFlag').val(2);
			}
		}
		//性别渲染
		$.ajax({
			url : "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action'/>?type=sex ",
			type:'post',
			success: function(data) {
				var v = data;
				for(var i=0;i<v.length;i++){
					sexMap.put(v[i].encode,v[i].name);
				}
			}
		});
		//渲染表单中的挂号专家
		$.ajax({
			url: "<%=basePath%>outpatient/changeDeptLog/queryempComboboxs.action",
			type:'post',
			success: function(empData) {
				empMap = eval("("+empData+")");
			}
		});
		//渲染表单中的挂号科室
		$.ajax({
			url: "<%=basePath%>outpatient/changeDeptLog/querydeptComboboxs.action", 
			type:'post',
			success: function(deptData) {
				deptMap = eval("("+deptData+")");
			}
		});
		
		//加载部门树
		$('#tDt').tree({
			url : '<%=basePath%>inpatient/QueryTreeContactShenqing.action',
			method : 'get',
			animate : true,
			lines : true,
			formatter : function(node) {//统计节点总数
				var s = node.text;
				if(node.children){
					s += '&nbsp;<span style=\'color:blue\'>('+ node.children.length + ')</span>';
				}
				return s;
			},onClick : function(node){
				$('#patientName').textbox('setValue',node.attributes.patientName);
				$('#patientSex').combobox('setValue',node.attributes.patientSex);
				$('#patientAge').textbox('setValue',node.attributes.patientAge);
				$('#location').textbox('setValue',node.attributes.location);
				$('#cnslDeptcd').datebox('setValue',node.attributes.cnslDeptcd);
				$('#moEddt').datebox('setValue',node.attributes.moEddt);
				$('#applyDate').datebox('setValue',node.attributes.applyDate);
				$('#moStdt').datebox('setValue',node.attributes.moStdt);
				$('#cnslDate').textbox('setValue',node.attributes.cnslDate);
				$('#deptCode').textbox('setValue',node.attributes.deptCode);
				$('#docCode').textbox('setValue',node.attributes.docCode);
				$('#cnslDoccd').textbox('setValue',node.attributes.cnslDoccd);
			}
		});
		//加载部门树
		$('#tDt1').tree({
			url : '<%=basePath%>inpatient/QueryTreeContactShenhe.action',
			method : 'get',
			animate : true,
			lines : true,
			formatter : function(node) {//统计节点总数
				var s = node.text;
				if(node.children){
					s += '&nbsp;<span style=\'color:blue\'>('+ node.children.length + ')</span>';
				}
				return s;
			}
		});
		$('#listlist').datagrid({
			onClickRow: function (rowIndex, rowData) {
				if(rowData.cnslStatus=="2"){
					$.messager.alert('提示',"已审核过，不能再审核");
				}else{
					mainId=rowData.id,
					$.ajax({
						url:"<%=basePath%>outpatient/auditing/queryAuditingById.action?mainId="+mainId,
						type: 'post',	
						success: function(consultationList){
							var consultationList = eval ("("+consultationList+")");
							var medicalrecordId=consultationList.patientNo;
							$('#idid').val(consultationList.id);
							$('#cnslDeptcd').combobox('setValue',consultationList.cnslDeptcd);
							$('#cnslDoccd').combobox('setValue',consultationList.cnslDoccd);
							$('#deptCode').combobox('setValue',consultationList.deptCode);
							$('#docCode').combobox('setValue',consultationList.docCode);
							$('#cnslDate').datebox('setValue',consultationList.cnslDate);
							$('#moEddt').datebox('setValue',consultationList.moEddt);
							$('#applyDate').datebox('setValue',consultationList.applyDate);
							$('#moStdt').datebox('setValue',consultationList.moStdt);
							$('#cnslNote').textbox('setValue',consultationList.cnslNote);
							$('#cnslRslt').textbox('setValue',consultationList.cnslRslt);
							$('#cnslNote2').textbox('setValue',consultationList.cnslNote2);
							$('#cnslRecord').textbox('setValue',consultationList.cnslRecord);
							$('#cnslNote3').textbox('setValue',consultationList.cnslNote3);
							$('#cnslExdt').datebox('setValue',consultationList.cnslExdt);
							$.ajax({
								url: "<%=basePath%>inpatient/queryAuditing.action?medicalrecordId="+medicalrecordId,
								type:'post',
								success: function(data) {		
									var  datalist=eval("("+data+")");
									$('#patientName').textbox('setValue',datalist.patientName);
									$('#reportSex').textbox('setValue',sexMap.get(datalist.reportSex));
									$('#reportAge').textbox('setValue',datalist.reportAge);
									$('#deptAddress').textbox('setValue',datalist.deptAddress);
									$('#bedName').textbox('setValue',datalist.bedName);
									$('#bedNo').val(datalist.bedNo);
									$('#bedwardName').textbox('setValue',datalist.bedwardName)
								}
							});
						}
					});
				}
			}
		});
	});

	//会诊科室(下拉框)
	$('#cnslDeptcd').combobox({   
		url: "<%=basePath%>register/deptCombobox.action",  
	    valueField:'id',    
	    textField:'deptName',
	    multiple:false,
	    editable:false,
	    onSelect:function(data){
	    	$('#cnslDoccd').combobox('setValue',"");
	    	$('#cnslDoccd').combobox('reload',"<%=basePath%>baseinfo/employee/employeeCombobox.action?departmentId="+data.id);
	    }
	});
	bindEnterEvent('cnslDeptcd',popWinToDeptCnsl,'easyui');//绑定回车事件
	//审核
	function auditing(){
		$('#editform').form('submit',{
			url : "<%=basePath %>outpatient/auditing/auditingSave.action",
			data: $('#editform').serialize(),
			dataType:'json',
			onSubmit:function(){ 
	  		 	if(!$('#editform').form('validate')){
					$.messager.show({  
					     title:'提示信息' ,   
					     msg:'验证没有通过,不能提交表单!'  
					}); 
					   return false ;
			     }
			},success:function(){
				$.messager.alert('提示','审核通过！');
				$("#listlist").datagrid("reload");
			},
			error:function(data){
				$.messager.alert('提示','审核失败！');
			}
		});		
	}
	// 清除页面填写信息
	function clear(){
		$('#editform').form('reset');
	}
	
	//申请科室(下拉框)
	$('#deptCode').combobox({   
		url: "<%=basePath%>register/deptCombobox.action",  
	    valueField:'id',    
	    textField:'deptName',
	    multiple:false,
	    editable:false,
	    onSelect:function(data){
	    	$('#docCode').combobox('setValue',"");
	    	$('#docCode').combobox('reload',"<%=basePath%>baseinfo/employee/employeeCombobox.action?departmentId="+data.id);
	    }
		
	});
	bindEnterEvent('deptCode',popWinToDept,'easyui');//绑定回车事件
	 //会诊医师(下拉框)
	$('#cnslDoccd') .combobox({    
	    url:"<%=basePath%>baseinfo/employee/employeeCombobox.action",    
	    valueField:'id',    
	    textField:'name',
	    multiple:false,
	    editable:false
    });
	bindEnterEvent('cnslDoccd',popWinToEmployee,'easyui');//绑定回车事件
	 //申请医师(下拉框)
	$('#docCode') .combobox({    
	    url:"<%=basePath%>baseinfo/employee/employeeCombobox.action",    
	    valueField:'id',    
	    textField:'name',
	    multiple:false,
	    editable:false
    });
	bindEnterEvent('docCode',popWinToEmployees,'easyui');//绑定回车事件
	//渲染科室		
	function functionDept(value,row,index){
		if(value!=null&&value!=''){
			return deptMap[value];
		}
	}
	//渲染人员
	function functionEmp(value,row,index){
		if(value!=null&&value!=''){
			return empMap[value];
		}
	}
	//状态显示渲染
	function functionStatus(value,row,index){
		if(value==1){
			return "申请";
		}else{
			return "确认";
		}
	}

	function functionOrderFlag(value,row,index){
			if(value==1){
				return "能开立医嘱";
			}else{
				return "不能开立医嘱";
			}
	}
	
	
	 /**
	   * 回车弹出会诊科室弹框
	   * @author  zhuxiaolu
	   * @param deptIsforregister 是否是挂号科室 1是 0否
	   * @param textId 页面上commbox的的id
	   * @date 2016-03-22 14:30   
	   * @version 1.0
	   */
	   function popWinToDeptCnsl(){
			$('#cnslDeptcd').combobox('setValue','');
			var tempWinPath = "<%=basePath%>popWin/popWinDepartment/toDepartmentPopWin.action?deptIsforregister=1&textId=cnslDeptcd";
			window.open (tempWinPath,'newwindow',' left=150,top=80,width='+ (screen.availWidth -300) +',height='+ (screen.availHeight-170)+',scrollbars,resizable=yes,toolbar=yes')
		}
	
	
   /**
	   * 回车弹出申请科室弹框
	   * @author  zhuxiaolu
	   * @param deptIsforregister 是否是挂号科室 1是 0否
	   * @param textId 页面上commbox的的id
	   * @date 2016-03-22 14:30   
	   * @version 1.0
	   */
	   function popWinToDept(){

			$('#deptCode').combobox('setValue','');
			var tempWinPath = "<%=basePath%>popWin/popWinDepartment/toDepartmentPopWin.action?deptIsforregister=1&textId=deptCode";
			window.open (tempWinPath,'newwindow',' left=150,top=80,width='+ (screen.availWidth -300) +',height='+ (screen.availHeight-170)+',scrollbars,resizable=yes,toolbar=yes')
		}
		
			
	   /**
		   * 回车弹出会诊医师弹框
		   * @author  zhuxiaolu
		   * @param textId 页面上commbox的的id
		   * @date 2016-03-22 14:30   
		   * @version 1.0
		   */
		   function popWinToEmployee(){
			   if($('#cnslDeptcd').textbox('getValue')){
				   var deptid=$('#cnslDeptcd').textbox('getValue');
				   $('#cnslDoccd').combobox('setValue',"");
					var tempWinPath = "<%=basePath%>popWin/popWinEmployee/toEmployeePopWin.action?textId=cnslDoccd&deptIds="+deptid;
					window.open (tempWinPath,'newwindow',' left=150,top=80,width='+ (screen.availWidth -300) +',height='+ (screen.availHeight-170)+',scrollbars,resizable=yes,toolbar=yes')
					
					
				}else{
					$.messager.alert('提示',"请选择选择科室");
				}
			}
   		/**
		   * 回车弹出申请医师弹框
		   * @author  zhuxiaolu
		   * @param textId 页面上commbox的的id
		   * @date 2016-03-22 14:30   
		   * @version 1.0
		   */
		   function popWinToEmployees(){
			   var deptid=$('#deptCode').textbox('getValue');
			   if(deptid){
				   
				   $('#docCode').combobox('setValue',"");
					var tempWinPath = "<%=basePath%>popWin/popWinEmployee/toEmployeePopWin.action?textId=docCode&deptIds="+deptid;
					window.open (tempWinPath,'newwindow',' left=150,top=80,width='+ (screen.availWidth -300) +',height='+ (screen.availHeight-170)+',scrollbars,resizable=yes,toolbar=yes')
					
					
				}else{
					$.messager.alert('提示',"请选择选择科室");
				}
		 }

</script>

</body>
</html>