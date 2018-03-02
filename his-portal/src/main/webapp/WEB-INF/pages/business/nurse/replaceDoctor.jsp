<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/metas.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<style type="text/css">
	.tableCss{
		border-collapse: collapse;border-spacing: 0;border: 1px solid #95b8e7;width:100%;
	}
	.tableCss td{
		border: 1px solid #95b8e7;
		padding: 5px 15px;
		height:35;
		word-break: keep-all;
		white-space:nowrap;
	}
	.tableCss .TDlabel{
		text-align: right;
		width:180px;
	}
</style>
</head>
<body>
<div id="cc" class="easyui-layout" data-options="fit:true">   
    <div data-options="region:'west'" style="width: 30%;height: 100%;border: 0">
    	<form id="form1" method="post">
	         <table class="tableCss"  style="border-left: 0px;border-right: 0px;">
		         <tr>
			         <td class="TDlabel">病历号：</td>
			         <td><input type="text" id="zhuyuanhao" name="info.medicalrecordId" value="${info.inpatientNo}"  readOnly style="border:0" ></td>
		         </tr>
		         <tr>
			         <td class="TDlabel">姓名：</td>
			         <td><input type="text" id="xingming"  value="${info.patientName}"  readOnly style="border:0" ></td>
		         </tr>
		         <tr>
			         <td class="TDlabel">性别：</td>
			         <td><input type="text" id="xingbie" value="${info.reportSex}"  readOnly style="border:0" ></td>
		         </tr>
		         <tr>
			         <td class="TDlabel">床号：</td>
			         <td><input type="text" id="chuanghao" value="${info.bedName}"  readOnly style="border:0" ></td>
		         </tr>
		         <tr>
			        <td class="TDlabel">住院医师：</td>
			        <td>
		         	<input id="zyys" style="width:90%" name="info.houseDocCode" value="${info.houseDocCode}"   /> 
		         	<input id="zyysn" name="info.houseDocName"  type="hidden"'/> 
		        	</td>
		         </tr>
		         <tr>
			         <td class="TDlabel">主治医师：</td>
			         <td>
			         <input id="zzys" style="width:90%" name="info.chargeDocCode" value="${info.chargeDocCode}"  />  
			         <input id="zzysn" name="info.chargeDocName"  type="hidden"'/> 
			         </td>
		         </tr>
		         <tr>
			         <td class="TDlabel">主任医师：</td>
			         <td>
			         <input id="zrys" style="width:90%" name="info.chiefDocCode" value="${info.chiefDocCode}"   />  
			          <input id="zrysn" name="info.chiefDocName"  type="hidden"'/> 
			         </td>
		         </tr>
		         <tr>
			         <td class="TDlabel">责任护士：</td>
			         <td>
			         <input id="zrhs" style="width:90%" name="info.dutyNurseCode" value="${info.dutyNurseCode}"  />
			          <input id="zrhsn" name="info.dutyNurseName"  type="hidden"'/> 
			         </td>
			         <td hidden><input name="info.inpatientNo" class="easyui-textbox" value="${info.inpatientNo }"/></td>
		         </tr>
	        </table>
            <div style="text-align: center; padding: 50px">
				<shiro:hasPermission name="${menuAlias}:function:save">
				<a onClick="saveDoctor()"  data-options="iconCls:'icon-save'" class="easyui-linkbutton">保&nbsp;存&nbsp;</a>
      		 	</shiro:hasPermission>
		 	</div>
		</form>
    </div>   
    <div data-options="region:'center'" style="width: 70%;height: 100%">
    	<table id="replaceDoctorList" class="easyui-datagrid" style="width:100%;height:79%" data-options="url:'<%=basePath%>nursestation/nurse/replaceDoctorList.action?inpatientNo=${info.inpatientNo}',rownumbers:true, singleSelect:true, pagination:true,pageSize:20,pageList:[20,50,100],fitColumns:true,fit:true">
		  <thead>   
	         <tr>   
	            <th data-options="field:'oldDataCode',width:'25%',align:'center'">换医类型</th>   
	            <th data-options="field:'oldDataName',width:'25%',align:'center'">原人员</th>   
	            <th data-options="field:'newDataName',width:'25%',align:'center'">现人员</th>  
	            <th data-options="field:'createTime',width:'25%',align:'center'">记录时间</th> 
	        </tr>   
	       </thead>  
		</table>
    </div>   
</div> 

<script type="text/javascript">
	$(function(){
		$("#replaceDoctorList").datagrid({
			onLoadSuccess : function(data){
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
		 * 住院医生
		 */
		$('#zyys').combobox({
			url:'<%=basePath%>nursestation/nurse/queryEmp.action?type=1',
			mode:'local',
			valueField:'jobNo',
			textField:'name',
			filter:function(q,row){
				var keys = new Array();
				keys[keys.length] = 'jobNo';
				keys[keys.length] = 'code';
				keys[keys.length] = 'name';
				keys[keys.length] = 'pinyin';
				keys[keys.length] = 'wb';
				keys[keys.length] = 'inputCode';
				return filterLocalCombobox(q, row, keys);
			}
		});
		/**
		 * 主治医生
		 */
		$('#zzys').combobox({
			url:'<%=basePath%>nursestation/nurse/queryEmp.action?type=1',
			mode:'local',
			valueField:'jobNo',
			textField:'name',
			filter:function(q,row){
				var keys = new Array();
				keys[keys.length] = 'jobNo';
				keys[keys.length] = 'code';
				keys[keys.length] = 'name';
				keys[keys.length] = 'pinyin';
				keys[keys.length] = 'wb';
				keys[keys.length] = 'inputCode';
				return filterLocalCombobox(q, row, keys);
			}
		});
		/**
		 * 主任医生
		 */
		$('#zrys').combobox({
			url:'<%=basePath%>nursestation/nurse/queryEmp.action?type=1',
			mode:'local',
			valueField:'jobNo',
			textField:'name',
			filter:function(q,row){
				var keys = new Array();
				keys[keys.length] = 'jobNo';
				keys[keys.length] = 'code';
				keys[keys.length] = 'name';
				keys[keys.length] = 'pinyin';
				keys[keys.length] = 'wb';
				keys[keys.length] = 'inputCode';
				return filterLocalCombobox(q, row, keys);
			}
		});
		
		/**
		 * 责任护士
		 */
		$('#zrhs').combobox({
			url:'<%=basePath%>nursestation/nurse/queryEmp.action?type=2',
			mode:'local',
			valueField:'jobNo',
			textField:'name',
			filter:function(q,row){
				var keys = new Array();
				keys[keys.length] = 'jobNo';
				keys[keys.length] = 'code';
				keys[keys.length] = 'name';
				keys[keys.length] = 'pinyin';
				keys[keys.length] = 'wb';
				keys[keys.length] = 'inputCode';
				return filterLocalCombobox(q, row, keys);
			}
		});
		
	});
	function saveDoctor(){
		
		$("#zyysn").val($("#zyys").combobox('getText'));
		$("#zzysn").val($("#zzys").combobox('getText'));
		$("#zrysn").val($("#zrys").combobox('getText'));
		$("#zrhsn").val($("#zrhs").combobox('getText'));
		
		$('#form1').form('submit',{
			url:'<%=basePath%>nursestation/nurse/saveDoctor.action',
			onSubmit:function(){
				if (!$('#form1').form('validate')) {
					$.messager.show({
						title : '提示信息',
						msg : '验证没有通过,不能提交表单!'
					});
					return false;
				}
				$.messager.progress({text:'正在换医师,请稍后...',modal:true});
			},
	        success:function(data){
	        		$.messager.progress('close');
	        		$.messager.alert('提示',"换医师成功！");
	        		setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
	        		$("#replaceDoctorList").datagrid('reload');
	        		//实现刷新栏目中的数据
	        		var node = window.parent.$('#treePatientInfo').tree('getSelected');
					$.ajax({  //根据患者住院流水号查询患者信息
						url:'<%=basePath%>nursestation/nurse/getInfobyId.action',
						data:{inpatientNo:node.attributes.inpatientNo},
						success:function(info){ //
							window.parent.$("#blh").val(info.medicalrecordId);
							window.parent.$("#xm").val(info.patientName);
							
							var ages=DateOfBirth(info.reportBirthday);
							 //年龄
							window.parent.$("#nl").val(ages.get("nianling")+ages.get('ageUnits'));
							window.parent.$("#hljb").val(info.tend);
					
							window.parent.$("#zyys").val(info.houseDocName);
							window.parent.$("#zzys").val(info.chargeDocName);
							window.parent.$("#zrys").val(info.chiefDocName);
							window.parent.$("#zrhs").val(info.dutyNurseName);
							window.parent.$("#cw").val(info.bedName);
							$("#zd").val(info.diagName);
							if(info.anaphyFlag=="1"){
								window.parent.$("#gm").val('有');
							}
							else{
								window.parent.$("#gm").val('无');
							}
						}
					});
	        },
			error : function(data) {
				$.messager.progress('close');
				$.messager.alert('提示',"保存失败！请联系管理员");	
			}			         
		});
	}
	
	/**  
	 *  
	 * @Description：过滤	
	 * @Author：zhuxiaolu
	 * @CreateDate：2016-11-1
	 * @version 1.0
	 * @throws IOException 
	 *
	 */ 
function filterLocalCombobox(q, row, keys){
	if(keys!=null && keys.length > 0){//
		for(var i=0;i<keys.length;i++){ 
			if(row[keys[i]]!=null&&row[keys[i]]!=''){
					var istrue = row[keys[i]].indexOf(q.toUpperCase()) > -1;
					if(istrue==true){
						return true;
					}
			}
		}
	}else{
		var opts = $(this).combobox('options');
		return row[opts.textField].indexOf(q.toUpperCase()) > -1;
	}
}
	
</script>
<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
</body>
</html>