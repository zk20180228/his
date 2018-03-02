<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<title>医技设备维护</title>
	<%@ include file="/common/metas.jsp" %>
</head>
<body>
	<form id="editForm" method="post">
		<input type="hidden" id="ids" name="tecCarrier.id" value="${tecCarrier.id }"/>
		<input type="hidden" name="tecCarrier.createUser" value="${tecCarrier.createUser }"/>
		<input type="hidden" name="tecCarrier.createDept" value="${tecCarrier.createDept }"/>
		<input type="hidden" name="tecCarrier.createTime" value="${tecCarrier.createTime }"/>
		<input type="hidden" name="tecCarrier.deptCode" value="${tecCarrier.deptCode}"/>
		<table class="honry-table" cellpadding="0" cellspacing="0" border="0" style="width: 100%;border-top:0;border-left:0;border-right:0">
			<tr>
				<td colspan="8" style="border-top:0;border-left:0;border-right:0"><font  style="font-weight: bold;font-size: 14px;">基本属性</font></td>
			</tr>
			<tr>
				<td class="honry-lable" style="border-left:0">设备编码：</td>
				<td><input class="easyui-textbox" name="tecCarrier.deviceCode" value="${tecCarrier.deviceCode }" data-options="required:true"></td>
				<td class="honry-lable">设备名称：</td>
				<td><input class="easyui-textbox" name="tecCarrier.deviceName" value="${tecCarrier.deviceName }" data-options="required:true"></td>
				<td class="honry-lable">载体编码：</td>
				<td><input class="easyui-textbox" name="tecCarrier.carrierCode" value="${tecCarrier.carrierCode }" data-options="required:true"></td>
				<td class="honry-lable">载体名称：</td>
				<td style="border-right:0"><input class="easyui-textbox" name="tecCarrier.carrierName" value="${tecCarrier.carrierName }" data-options="required:true"></td>
			</tr>
			<tr>
				<td class="honry-lable" style="border-left:0">自定义码：</td>
				<td><input class="easyui-textbox" name="tecCarrier.userCode" value="${tecCarrier.userCode }" ></td>
				<td class="honry-lable">型号：</td>
				<td><input class="easyui-combobox" id="model" name="tecCarrier.model" value="${tecCarrier.model }" ></td>
				<td class="honry-lable">设备类型：</td>
				<td><input class="easyui-combobox" id="deviceType" name="tecCarrier.deviceType" value="${tecCarrier.deviceType }"></td>
				<td class="honry-lable">载体类型：</td>
				<td style="border-right:0"><input class="easyui-combobox" id="carrierType" name="tecCarrier.carrierType" value="${tecCarrier.carrierType }" ></td>
			</tr>
			<tr>
				<td class="honry-lable" style="border-left:0">排列序号：</td>
				<td><input class="easyui-textbox" name="tecCarrier.sortId" value="${tecCarrier.sortId }"></td>
				<td class="honry-lable">所处建筑物：</td>
				<td><input class="easyui-textbox" name="tecCarrier.building" value="${tecCarrier.building }"></td>
				<td class="honry-lable">所处楼房：</td>
				<td><input class="easyui-textbox"  name="tecCarrier.floor" value="${tecCarrier.floor }"></td>
				<td class="honry-lable">所处房间：</td>
				<td style="border-right:0"><input class="easyui-textbox"  name="tecCarrier.room" value="${tecCarrier.room }"></td>
			</tr>
			<tr>
				<td class="honry-lable" style="border-left:0;">预存空闲日期：</td>
				<td>
					<input id="disengagedTime" class="Wdate" type="text" name="tecCarrier.disengagedTime" value="" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',maxDate:'2099-12-31 23:59:59'})" style="width:150px;border: 1px solid #95b8e7;border-radius: 5px;"/>
				    <input type="hidden" id="tmpDisengagedTime" value="${tecCarrier.disengagedTime}">
				</td>
				<td class="honry-lable">平均周转时间：</td>
				<td><input class="easyui-textbox"  name="tecCarrier.avgTurnoverTime" value="${tecCarrier.avgTurnoverTime }">&nbsp;分</td>
				<td colspan="4" style="border-right:0">&nbsp;&nbsp;
				<input type="hidden" id="isDisengagedH" name="tecCarrier.isDisengaged" value="${tecCarrier.isDisengaged }"/>
				<input type="checkbox" id="isDisengaged" checked="checked" onclick="javascript:checkBoxSelect('isDisengaged')"/>&nbsp;是否空闲</td>
			</tr>
			<tr>
				<td class="honry-lable" style="border-left:0">备注：</td>
				<td colspan="7"  style="border-right:0"><input class="easyui-textbox"  name="tecCarrier.carrierMemo" value="${tecCarrier.carrierMemo }" ></td>
			</tr>
			<tr>
				<td colspan="8" style="border-left:0;border-right:0"><font  style="font-weight: bold;font-size: 14px;">限额控制</font></td>
			</tr>
			<tr>
				<td class="honry-lable" colspan="2" style="border-left:0">医生直接预约限额：</td>
				<td colspan="2"><input class="easyui-textbox" name="tecCarrier.doctorQuota" value="${tecCarrier.doctorQuota }"></td>
				<td class="honry-lable" colspan="2">患者自助预约限额：</td>
				<td colspan="2" style="border-right:0"><input class="easyui-textbox" name="tecCarrier.selfQuota" value="${tecCarrier.selfQuota }"></td>
			</tr>
			<tr>
				<td class="honry-lable" colspan="2" style="border-left:0">患者自助预约限额（Web）：</td>
				<td colspan="2"><input class="easyui-textbox" name="tecCarrier.webQuota" value="${tecCarrier.webQuota }"></td>
				<td class="honry-lable" colspan="2">日限额：</td>
				<td colspan="2" style="border-right:0"><input class="easyui-textbox" name="tecCarrier.dayQuota" value="${tecCarrier.dayQuota }" ></td>
			</tr>
			<tr>
				<td colspan="8" style="border-left:0;border-right:0"><font  style="font-weight: bold;font-size: 14px;">停用时间控制</font></td>
			</tr>
			<tr>
				<td class="honry-lable" colspan="2" style="border-left:0">预停止时间：</td>
				<td colspan="2">
<%-- 				<input class="easyui-datetimebox" id="preStoptime" name="tecCarrier.preStoptime" value="${tecCarrier.preStoptime }"> --%>
					<input id="preStoptime" class="Wdate" type="text" name="tecCarrier.preStoptime" value="${tecCarrier.preStoptime }" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',maxDate:'#F{$dp.$D(\'preStarttime\')}'})" style="width:150px;border: 1px solid #95b8e7;border-radius: 5px;"/>
					<input type="hidden" id="tmpPreStoptime" value="${tecCarrier.preStoptime }" />
				</td>
				<td class="honry-lable" colspan="2">预启动时间：</td>
				<td colspan="2" style="border-right:0">
<%-- 					<input class="easyui-datetimebox" id="preStarttime" name="tecCarrier.preStarttime" value="${tecCarrier.preStarttime }"> --%>
					<input id="preStarttime" class="Wdate" type="text" name="tecCarrier.preStarttime" value="${tecCarrier.preStarttime }"  onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'#F{$dp.$D(\'preStoptime\')}',maxDate:'2099-12-31 23:59:59'})" style="width:150px;border: 1px solid #95b8e7;border-radius: 5px;"/>
					<input type="hidden" id="tmpPreStarttime" value="${tecCarrier.preStarttime}" />
				</td>
			</tr>
			<tr>
				<td colspan="8" style="border-left:0;border-right:0">&nbsp;&nbsp;
				<input type="hidden" id="isValidH" name="tecCarrier.isValid"  value="${tecCarrier.isValid }"/>
				<input type="checkbox" id="isValid" checked="checked" onclick="javascript:checkBoxSelect('isValid')"/>&nbsp;
				<font style="color: red" class="tecCarrierTip">是否有效</font> &nbsp;&nbsp;
				<font style="color: red" class="tecCarrierTip">预计空闲日期必须大于当前日期，预启动日期必须大于预停止日期</font> </td>
			</tr>
		</table>
		<div style="text-align:center;padding:5px">
			<a href="javascript:submit();" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a>
			<a href="javascript:closeDialog();" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'">关闭</a>
		</div>
	</form>
<script type="text/javascript">
$(function(){
	var isDisengagedH = '${tecCarrier.isDisengaged }';
	var isValidH = '${tecCarrier.isValid }';
	if(isDisengagedH=="1"){
		$('#isDisengagedH').val(1);
		$('#isDisengaged').prop('checked',true);
	}else{
		$('#isDisengaged').prop('checked',false);
		$('#isDisengagedH').val(0);
	}
	if(isValidH=="1"){
		$('#isValid').prop('checked',true);
		$('#isValidH').val(1);
	}else{
		$('#isValid').prop('checked',false);
		$('#isValidH').val(0);
	}
});
	if($("#ids").val()==null||$("#ids").val()==''){
		$("#isValidH").val(1);
		$("#isDisengagedH").val(1);
		$('#isDisengaged').prop('checked',true);
		$('#isValid').prop('checked',true);
	}
	//型号下拉框
	$("#model").combobox({
		valueField: 'encode',
		textField: 'name',
		url: '<%=basePath %>baseinfo/pubCodeMaintain/queryDictionary.action?type=version'
	});
	//设备类型下拉框
	$("#deviceType").combobox({
		valueField: 'encode',
		textField: 'name',
		url: '<%=basePath %>baseinfo/pubCodeMaintain/queryDictionary.action?type=sbtype'
	});
	//载体类型下拉框
	$("#carrierType").combobox({
		valueField: 'encode',
		textField: 'name',
		url: '<%=basePath %>baseinfo/pubCodeMaintain/queryDictionary.action?type=carriertype'
	});
	//保存方法
	function submit(){
		$('#editForm').form('submit',{ 
			url:"<%=basePath%>technical/TecCarrier/saveOrUpdateTecCarrier.action",
			onSubmit: function(){
// 				var disTime=$("#disengagedTime").datetimebox('getValue');
// 				var sTime=$('#preStarttime').datetimebox('getValue');   //预启动时间
// 				var endTime=$('#preStoptime').datetimebox('getValue');   //预停止时间
				var disTime=$("#disengagedTime").val();//预计空闲时间
				var sTime=$('#preStarttime').val();   //预启动时间
				var endTime=$('#preStoptime').val();   //预停止时间
				if(!$('#editForm').form('validate')){
					$.messager.show({
						title:'提示信息' ,
						msg:'验证没有通过,不能提交表单!'
					});
					return false ;
				}
				if(disTime!=null&&disTime!=''){
					var now=new Date();  //获取当前时间
					var dis = new Date(disTime.replace(/\-/g, "\/"));
					if(now>dis){
						$.messager.alert('操作提示', '预计空闲日期必须大于当前日期！');
						setTimeout(function(){
							$(".messager-body").window('close');
						},3500);
						return false;
					}else{
						return true;
					}
				}else{
					$.messager.alert('操作提示', '预计空闲日期不能为空！');
					setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
					return false;
				}
				if(sTime!=null&&sTime!=''&&endTime!=null&&endTime!=''){
					var d1 = new Date(sTime.replace(/\-/g, "\/")); 
					var d2 = new Date(endTime.replace(/\-/g, "\/"));
					if(d1>d2){
						$.messager.alert('操作提示', '预启动日期必须大于预停止日期！');
						setTimeout(function(){
							$(".messager-body").window('close');
						},3500);
						return false;
					}else{
						return true;
					}
				}
			},
			success:function(data){
				$.messager.alert('操作提示', '保存成功！');
				closeDialog();
				$('#list').datagrid('reload');
			},error:function(){
				$.messager.alert('操作提示', '保存失败！');
				closeDialog();
			}
		});
	}
	//关闭
	function closeDialog(){
		$('#addWin').dialog('close');
	}
	//复选框
	function checkboxFormer(value,row,index){
		if(value=='1'){
			 return "<input type='checkbox' disabled='disabled' checked='checked'>";
		}else{
			 return "<input type='checkbox' disabled='disabled'>";
		}
	}
	//是否有效
	function checkBoxSelect(id){
		if($("#"+id).is(':checked')){
			$("#"+id+'H').val(1);
		}else{
			$("#"+id+'H').val(0);
		}
	}
	
	if($('#tmpDisengagedTime').val()){
		var tmpVal = $('#tmpDisengagedTime').val()
		$('#disengagedTime').val(tmpVal.substring(0,19))
	}
	if($('#tmpPreStoptime').val()){
		var tmpVal1 = $('#tmpPreStoptime').val()
		$('#preStoptime').val(tmpVal1.substring(0,19))
	}
	if($('#tmpPreStarttime').val()){
		var tmpVal2 = $('#tmpPreStarttime').val()
		$('#preStarttime').val(tmpVal2.substring(0,19))
	}
</script>
</body>
</html>