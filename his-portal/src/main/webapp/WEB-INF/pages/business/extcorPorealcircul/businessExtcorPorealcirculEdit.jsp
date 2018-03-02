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
<script type="text/javascript">
var Doctor="${businessExtcorPorealcircul.operater}";
var Doctor1="${businessExtcorPorealcircul.affuseDoctor}";
var sexMap=new Map();
var empMap
var flag;
	//医生（渲染）
	$.ajax({
		url:"<%=basePath%>publics/consultation/queryEmpMapPublic.action",
		async:false,
		success:function(data){
			empMap=data;
		}
	});
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
			var sex = $('#sex').val();
			$('#sexString').textbox('setText',sexMap.get(sex));
		}
	});
	
	$(function(){
		flag = $('#deptType').val();
		var id = $('#treeId').val();
		if(id != ''){
			var span = $('#clearBtn').find('span').find('span');
			$(span[0]).html('还原');
		}
		if(flag == 'I'){
			$('#noName').html("住院号");
		}
		com();
	});
	/**
	 * 表单提交
	 * @author  ldl
	 * @date 2015-7-2 10:53
	 * @version 1.0
	 */
	 function submit(){
		 var doc=empMap[$('#operater').combobox('getValue')];
		 var doc1=empMap[$('#affuseDoctor').combobox('getValue')];
		 if(null==doc||null==doc1){
			 $.messager.alert('提示信息','请选择灌注医师和手术者');
			 setTimeout(function(){
					$(".messager-body").window('close');
				},3500);
			 return false;
		 }
		 var timeArr=new Array(['turntime'],['blocktime'],['assisttime'],['dhctime'])
		 for(var i=0;i<timeArr.length;i++){
			if(''==$('#'+timeArr[i]).val()){
				$.messager.alert('提示信息','时间输入不能为空');
				setTimeout(function(){
					$(".messager-body").window('close');
				},3500);
				return false;
			}
		 }
		 //温度格式校验
		 var vali=$('#temperange').textbox('getText');
		 var spli=vali.split('-');
		 if(spli.length!=2){
			 $.messager.alert('提示','温度范围格式不对');
			 setTimeout(function(){
					$(".messager-body").window('close');
				},3500);
			 return false;
		 }
		 if(isNaN(spli[0])||isNaN(spli[1])){
			 $.messager.alert('提示','温度含有非法字符');
			 setTimeout(function(){
					$(".messager-body").window('close');
				},3500);
			 return false;
		 }
		 //血压校验
		 vali=$('#piesisFront').textbox('getText');
		 var spli=vali.split('-');
		 if(spli.length!=2){
			 $.messager.alert('提示','血压(转前)格式不对');
			 setTimeout(function(){
					$(".messager-body").window('close');
				},3500);
			 return false;
		 }
		 if(isNaN(spli[0])||isNaN(spli[1])){
			 $.messager.alert('提示','血压(转前)含有非法字符');
			 setTimeout(function(){
					$(".messager-body").window('close');
				},3500);
			 return false;
		 }
		 //转中
		 vali=$('#piesisMiddle').textbox('getText');
		 var spli=vali.split('-');
		 if(spli.length!=2){
			 $.messager.alert('提示','血压(转中)格式不对');
			 setTimeout(function(){
					$(".messager-body").window('close');
				},3500);
			 return false;
		 }
		 if(isNaN(spli[0])||isNaN(spli[1])){
			 $.messager.alert('提示','血压(转中)含有非法字符');
			 setTimeout(function(){
					$(".messager-body").window('close');
				},3500);
			 return false;
		 }
		 //转后
		 vali=$('#piesisEnd').textbox('getText');
		 var spli=vali.split('-');
		 if(spli.length!=2){
			 $.messager.alert('提示','血压(转后)格式不对');
			 setTimeout(function(){
					$(".messager-body").window('close');
				},3500);
			 return false;
		 }
		 if(isNaN(spli[0])||isNaN(spli[1])){
			 $.messager.alert('提示','血压(转后)含有非法字符');
			 setTimeout(function(){
					$(".messager-body").window('close');
				},3500);
			 return false;
		 }
	    $('#editForm').form('submit',{  
	        url:'<%=basePath %>publics/businessExtcorPorealcircul/saveBusinessExtcorPorealcircul.action',  
	        onSubmit:function(){
				if (!$('#editForm').form('validate')) {
					$.messager.show({
						title : '提示信息',
						msg : '验证没有通过,不能提交表单!'
					});
					return false;
				}
	        },  
	        success:function(){  
	        	$.messager.alert('提示','保存成功');
	             //实现刷新栏目中的数据
                $('#editForm').form('clear');
	            window.parent.reloadExList();
	        },
			error : function(data) {
				$.messager.alert('提示','保存失败');
			}							         
	    }); 
     }	
     /**  
	 *  
	 * @Description：  灌注医师下拉框
	 */
	function com(){
		$('#affuseDoctor').combogrid({    
				url:'<%=basePath %>publics/businessExtcorPorealcircul/affuseDoctorCombobox.action', 
				idField : 'jobNo',
				textField : 'name',
				multiple:false,
				mode:"remote",
				panelAlign:'right',
				panelWidth:340,
				pageList:[10,20,30,40,50],
				pageSize:"10",
				pagination:true,
				columns:[[   
						{field:'jobNo',title:'工作号',width:'130'},
					{field:'name',title:'名称',width:'160'} 
					
					]], 
				onLoadSuccess: function () {
					var id=$(this).prop("id");
					if ($("#"+id).combogrid('getValue')&&empMap[$("#"+id).combogrid('getValue')]) {
				         $("#"+id).combogrid('setText',empMap[$("#"+id).combogrid('getValue')]);
				        } 

			}
		});
		/**  
		*  
		* @Description：  手术者下拉框
		*/
		$("#operater").combogrid({
			url:'<%=basePath %>publics/businessExtcorPorealcircul/affuseDoctorCombobox.action',
			idField : 'jobNo',
			textField : 'name',
			delay:500,
			multiple:false,
			mode:"remote",
			panelAlign:'right',
			panelWidth:340,
			pageList:[10,20,30,40,50],
			pageSize:"10",
			rownumbers: true,//是否显示编号
			collapsible: false,//是否可折叠
			pagination:true,
			columns:[[   
					{field:'jobNo',title:'工作号',width:'130'},
				{field:'name',title:'名称',width:'160'} 
				]],
				Handler: {
                    up: function() {},
                    down: function() {},
                    enter: function() {},
                    query: function(q) {
                      //动态搜索
                       $('#operater').combogrid("grid").datagrid("reload", { 'keyword': q });
                         $('#operater').combogrid("setValue", q);
                    }
                },
                onLoadSuccess: function () {
					var id=$(this).prop("id");
					if ($("#"+id).combogrid('getValue')&&empMap[$("#"+id).combogrid('getValue')]) {
				         $("#"+id).combogrid('setText',empMap[$("#"+id).combogrid('getValue')]);
				        } 

			}
		});
	}
     
	//按回车键提交表单！
	$('#searchTab').find('input').on('keyup', function(event) {
		if (event.keyCode == 13) {
			searchFrom();
		}
	});
	 /**
	   * 回车弹出灌注医师弹框
	   * @author  zhuxiaolu
	   * @param textId 页面上commbox的的id
	   * @date 2016-03-22 14:30   
	   * @version 1.0
	   */
	   function popWinToEmployee(){
		  
		   var tempWinPath = "<%=basePath%>popWin/popWinEmployee/toEmployeePopWin.action?textId=affuseDoctor&employeeType=1";
		   window.open (tempWinPath,'newwindow',' left=150,top=80,width='+ (screen.availWidth -300) +',height='+ (screen.availHeight-170)+',scrollbars,resizable=yes,toolbar=yes')
	   }
	   /**
		   * 回车弹出灌注医师弹框
		   * @author  zhuxiaolu
		   * @param textId 页面上commbox的的id
		   * @date 2016-03-22 14:30   
		   * @version 1.0
		   */
		   function popWinToEmployeeOperater(){
			
			   var tempWinPath = "<%=basePath%>popWin/popWinEmployee/toEmployeePopWin.action?textId=operater&employeeType=1";
			   window.open (tempWinPath,'newwindow',' left=150,top=80,width='+ (screen.availWidth -300) +',height='+ (screen.availHeight-170)+',scrollbars,resizable=yes,toolbar=yes')
		   } 
		   /**
		   * 清除form
		   */
		   function cle(){
			   var sex = $('#sexString').textbox('getText');
			   $('#editForm').form('reset');
			   if(sex != null && sex != ''){
				   $('#sexString').textbox('setText',sex);
				   $('#sexString').textbox({
					   required:true
				   });
			   }
		   } 

		   
</script>
<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
</head>
<body>
			<form id="editForm" method="post">
				<input type="hidden" id="treeId" name="businessExtcorPorealcircul.id" value="${businessExtcorPorealcircul.id }" />
				<input type="hidden" id="deptType" value="${deptType }"/>
				<input type="hidden" id="patientFlg" value="${businessExtcorPorealcircul.patientFlg }" name="businessExtcorPorealcircul.patientFlg" />
				<div style="padding:5px 5px 5px 5px;" class="businessExtcor">		
					<table  class="honry-table" cellpadding="1" cellspacing="1"	border="1px solid black" id="list" style="width:100%">
						<tr>
							<td class="honry-lable" id="noName">门诊号：</td>
							<td>
								<input id="inpatientNo" class="easyui-textbox" value="${businessExtcorPorealcircul.inpatientNo }" name="businessExtcorPorealcircul.inpatientNo" data-options="required:true" readonly="readonly" />
					   	    </td>
					   	    <td class="honry-lable">病历号：</td>
					        <td>
						        <input id="patientNo" class="easyui-textbox" value="${businessExtcorPorealcircul.patientNo }" name="businessExtcorPorealcircul.patientNo"  data-options="required:true"  readonly="readonly"  />
					        </td>
					        <td class="honry-lable">姓名：</td>
					        <td>
						        <input id="name" class="easyui-textbox" value="${businessExtcorPorealcircul.name }" name="businessExtcorPorealcircul.name" data-options="required:true" readonly="readonly" />
					        </td>
						</tr>
						<tr>
							<td class="honry-lable">性别：</td>
					        <td>
								<input id="sex" type="hidden" value="${businessExtcorPorealcircul.sex }" name="businessExtcorPorealcircul.sex" /> 
								<input id="sexString"  class="easyui-textbox"  data-options="required:true"  readonly="readonly" style="color: #000"/>
					   	    </td>
					   	    <td class="honry-lable">年龄：</td>
					        <td>
						        <input id="age" class="easyui-textbox" value="${businessExtcorPorealcircul.age }"   name="businessExtcorPorealcircul.age" data-options="required:true,editable:false"/>
					        </td>
					        <td class="honry-lable">体重：</td>
					        <td>
						        <input id="weight" class="easyui-numberbox" value="${businessExtcorPorealcircul.weight }" name="businessExtcorPorealcircul.weight"  data-options="required:true" />
					        </td>
						</tr>
						<tr>
							<td class="honry-lable">体表面积：</td>
					        <td>
								<input id="bodyarea" class="easyui-numberbox" value="${businessExtcorPorealcircul.bodyarea }" name="businessExtcorPorealcircul.bodyarea"  data-options="required:true" />
					   	    </td>
					   	    <td class="honry-lable">手术名称：</td>
					        <td>
						        <input id="operationName" class="easyui-textbox" value="${businessExtcorPorealcircul.operationName }" data-options="required:true" name="businessExtcorPorealcircul.operationName" />
					        </td>
					        <td class="honry-lable">手术者：</td>
					        <td>
						        <input id="operater"  name="businessExtcorPorealcircul.operater" value="${businessExtcorPorealcircul.operater }" class="easyui-combogrid"  data-options=""/>
						        <a href="javascript:delSelectedData('operater');"  class="easyui-linkbutton" 
								data-options="iconCls:'icon-opera_clear',plain:true,editable:false"></a>
					        </td>
						</tr>
						<tr>
							<td class="honry-lable">灌注医师：</td>
					        <td>
								<input id="affuseDoctor" name="businessExtcorPorealcircul.affuseDoctor" value="${businessExtcorPorealcircul.affuseDoctor }" class="easyui-combogrid" data-options="" />
								<a href="javascript:delSelectedData('affuseDoctor');"  class="easyui-linkbutton" 
								data-options="iconCls:'icon-opera_clear',plain:true"></a>
					   	    </td>
					   	    <td class="honry-lable">转流时间：</td>
					        <td>
						       <!--<input id="turntime" class="easyui-datebox"  name="businessExtcorPorealcircul.turntime" value="${businessExtcorPorealcircul.turntime }" data-options="required:true,editable:false" />-->
					       		<input id="turntime" class="Wdate" type="text"  name="businessExtcorPorealcircul.turntime"  value="${businessExtcorPorealcircul.turntime }" readonly="true" onClick="WdatePicker({maxDate:'{%y+1}-%M-%d'})" style="width:150px;border: 1px solid #95b8e7;border-radius: 5px;"/>
					        </td>
					        <td class="honry-lable">阻断时间：</td>
					        <td>
						        <!--  <input id="blocktime" class="easyui-datebox"  name="businessExtcorPorealcircul.blocktime" value="${businessExtcorPorealcircul.blocktime }" data-options="required:true,editable:false" />-->
					        	<input id="blocktime" class="Wdate" type="text" name="businessExtcorPorealcircul.blocktime" value="${businessExtcorPorealcircul.blocktime }" onClick="WdatePicker({maxDate:'{%y+1}-%M-%d'})"  style="width:150px;border: 1px solid #95b8e7;border-radius: 5px;" readonly="true"/>
					        </td>
						</tr>
						<tr>
							<td class="honry-lable">辅助时间：</td>
					        <td>
								<!--  <input id="assisttime" class="easyui-datebox" name="businessExtcorPorealcircul.assisttime" value="${businessExtcorPorealcircul.assisttime }"  onkeyup="KeyDown()" data-options="required:true,editable:false" />-->
					   	    	<input id="assisttime" class="Wdate" type="text" onClick="WdatePicker({maxDate:'{%y+1}-%M-%d'})"  name="businessExtcorPorealcircul.assisttime" value="${businessExtcorPorealcircul.assisttime }" style="width:150px;border: 1px solid #95b8e7;border-radius: 5px;" readonly="true"/>
					   	    </td>
					   	    <td class="honry-lable">停循环时间：</td>
					        <td>
						       <!--   <input id="dhctime" class="easyui-datebox" name= "businessExtcorPorealcircul.dhctime" value="${businessExtcorPorealcircul.dhctime }" data-options="required:true,editable:false" />-->
					        	<input id="dhctime" class="Wdate" type="text" name= "businessExtcorPorealcircul.dhctime" value="${businessExtcorPorealcircul.dhctime }" onClick="WdatePicker({maxDate:'{%y+1}-%M-%d'})"  style="width:150px;border: 1px solid #95b8e7;border-radius: 5px;" readonly="true"/>
					        </td>
					        <td class="honry-lable">温度范围：</td>
					        <td>
						        <input id="temperange" class="easyui-textbox" name="businessExtcorPorealcircul.temperange" value="${businessExtcorPorealcircul.temperange }"  data-options="required:true,prompt:'例如:16-30'" />
					        </td>
						</tr>
						<tr>
							<td class="honry-lable">血压（转前）：</td>
					        <td>
								<input id="piesisFront" class="easyui-textbox" name="businessExtcorPorealcircul.piesisFront" value="${businessExtcorPorealcircul.piesisFront }"  onkeyup="KeyDown()" data-options="required:true,prompt:'例如:80-100'" />
					   	    </td>
					   	    <td class="honry-lable">血压（转中）：</td>
					        <td>
						        <input id="piesisMiddle" class="easyui-textbox" name="businessExtcorPorealcircul.piesisMiddle" value="${businessExtcorPorealcircul.piesisMiddle }"  data-options="required:true,prompt:'例如:80-100'" />
					        </td>
					        <td class="honry-lable">血压(转后)：</td>
					        <td>
						        <input id="piesisEnd" class="easyui-textbox" name="businessExtcorPorealcircul.piesisEnd" value="${businessExtcorPorealcircul.piesisEnd }"  data-options="required:true,prompt:'例如:80-100'" />
					        </td>
						</tr>
						<tr>
							<td class="honry-lable">超滤：</td>
					        <td>
								<input id="filtRate" class="easyui-textbox" name="businessExtcorPorealcircul.filtRate" value="${businessExtcorPorealcircul.filtRate }"  data-options="required:true"  />
					   	    </td>
					   	    <td class="honry-lable">预冲量：</td>
					        <td>
						        <input id="impulsevoLume" class="easyui-textbox" name="businessExtcorPorealcircul.impulsevoLume" value="${businessExtcorPorealcircul.impulsevoLume }"  data-options="required:true"  />
					        </td>
					        <td class="honry-lable">尿量：</td>
					        <td>
						        <input id="urinevoLume" class="easyui-textbox" name="businessExtcorPorealcircul.urinevoLume" value="${businessExtcorPorealcircul.urinevoLume }"   data-options="required:true" />
					        </td>
						</tr>
						<tr>
							<td class="honry-lable">尿色：</td>
					        <td>
								<input id="urineColour" class="easyui-textbox" name="businessExtcorPorealcircul.urineColour" value="${businessExtcorPorealcircul.urineColour }" onkeyup="KeyDown()" data-options="required:true" />
					   	    </td>
					   	    <td class="honry-lable">通气量：</td>
					        <td>
						        <input id="aeratevoLume" class="easyui-textbox" name="businessExtcorPorealcircul.aeratevoLume" value="${businessExtcorPorealcircul.aeratevoLume }" data-options="required:true"  />
					        </td>
					        <td class="honry-lable">输血量：</td>
					        <td>
						        <input id="transFusionvolLume" class="easyui-textbox" name="businessExtcorPorealcircul.transFusionvolLume" value="${businessExtcorPorealcircul.transFusionvolLume }"  data-options="required:true"  />
					        </td>
						</tr>
						<tr>
							<td class="honry-lable">肺型：</td>
					        <td>
								<input id="lungsType" class="easyui-textbox" name="businessExtcorPorealcircul.lungsType" value="${businessExtcorPorealcircul.lungsType }"  onkeyup="KeyDown()" data-options="required:true" />
					   	    </td>
					   	    <td class="honry-lable" >痊愈后情况：</td>
					        <td colspan="3">
						        <input id="recoverycase" class="easyui-textbox" name="businessExtcorPorealcircul.recoverycase" value="${businessExtcorPorealcircul.recoverycase }"  data-options="required:true"   style="width:500px;height:60px;"/>
					        </td>
						</tr>
					</table>
					<table style="padding-top: 10px;width: 100%">
						<tr>
							<td style="text-align: right;">
								<shiro:hasPermission name="${menuAlias}:function:add">
								<a href="javascript:submit();void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a>
								</shiro:hasPermission>
							</td>
							<td style="text-align: left;">
								&nbsp;<a href="javascript:cle();void(0)" id="clearBtn" class="easyui-linkbutton" data-options="iconCls:'icon-clear'">清除</a>
							</td>
						</tr>
					</table>
				</div>
			</form>

</body>
