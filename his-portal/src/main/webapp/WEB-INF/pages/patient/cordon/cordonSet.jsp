<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp" %>
<%@ include file="/common/metas.jsp" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
</head>
<body>
	<div id="p" class="easyui-panel"  style="width:100%;padding: 10px; background: #fafafa;" data-options="fit:'true',border:'false'">
		<form id="editForm" style="text-align: center;">
			<input type="hidden" name="cordonVo.id" value="${inpatientInfo.id}">
			<table class="honry-table" style="width:100%;text-align: center;padding:5px 5px 5px 5px;margin-left:auto;margin-right:auto;">
				<tr>
					<td class="honry-lable"><span style="font-size: 13">设置：</span></td>
					<td style="text-align: left;">
						<input  class="easyui-textbox" id="name"  style="width: 170px" name="cordonVo.patientName" value="${inpatientInfo.patientName }" readonly="readonly" /></td>
				</tr>
				<tr >
					<td class="honry-lable"><span style="font-size: 13;">设置类别：</span></td>
					<td style="text-align: left;"><input class="easyui-combobox" id="alterType"  style="width: 170px"  name="cordonVo.alterType" value="${inpatientInfo.alterType }" data-options="valueField:'id',textField:'value',data:[{id:'M',value:'金额'},{id:'D',value:'时间段'}],required:true"/> </td>
				</tr>
				<tr>
					<td class="honry-lable"><span style="font-size: 13">催款警戒线：</span></td>
					<td style="text-align: left;">
						<input class="easyui-numberbox" id="moneyAlert"  style="width: 170px" name="cordonVo.moneyAlert" data-options="${inpatientInfo.alterType=='D' ? 'min:0,precision:1,disabled:true':'min:0,precision:1' }" value="${inpatientInfo.moneyAlert}">
					</td>
				</tr>
				<tr>
					<td class="honry-lable"><span style="font-size: 13">开始时间：</span></td>
					<td style="text-align: left;">
<%-- 						<input id="alterBegin" class="easyui-datetimebox"  style="width: 170px" name="cordonVo.alterBegin" data-options=" ${inpatientInfo.alterType=='M' ? 'disabled:true':'' }" value="${inpatientInfo.alterBegin}"/> --%>
						<input id="alterBegin" class="Wdate" name="cordonVo.alterBegin" type="text" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" style="width:170px;border: 1px solid #95b8e7;border-radius: 5px;"/>
					</td>
				</tr>
				<tr style="margin-top: 50px">
					<td class="honry-lable"><span style="font-size: 13">结束时间：</span></td>
					<td  style="text-align: left;">
<%-- 						<input id="alterEnd" class="easyui-datetimebox"  style="width: 170px" name="cordonVo.alterEnd" data-options=" ${inpatientInfo.alterType=='M' ? 'disabled:true':'' }"  value="${inpatientInfo.alterEnd}"/>	 --%>
						<input id="alterEnd" name="cordonVo.alterEnd" class="Wdate" type="text" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" style="width:170px;border: 1px solid #95b8e7;border-radius: 5px;"/>
					</td>
				</tr>
			</table>
		</form>
		<br>
		<div style="text-align: center; padding: 5px">
			<a href="javascript:submit();" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a>
			<a href="javascript:cleard()" data-options="iconCls:'icon-clear'" class="easyui-linkbutton">重置</a>
			<a href="javascript:void(0)" data-options="iconCls:'icon-cancel'" onclick="closeDialog()" class="easyui-linkbutton">关闭</a>
		</div>	
	</div>
<script type="text/javascript">	
			/**
			 *表单提交submit信息
			 */
		  	function submit(){
			  	$('#editForm').form('submit',{
			  		url:"<%=basePath%>inpatient/cordon/saveCordon.action",
			  		 onSubmit:function(){ 
			  			 /**
			  			  *获得开始时间
			  			  */
			  			var sTime=$('#alterBegin').val();
			  			 /**
			  			  *获得结束时间
			  			  */
			  			var endTime=$('#alterEnd').val();
			  			if($('#alterType').combobox('getValue')==""||$('#alterType').combobox('getValue')==null){
			  				$.messager.alert('操作提示', '请选择设置类别！'); 
			  				setTimeout(function(){
			  					$(".messager-body").window('close');
			  				},3500);
	                        return false;
			  			}
			  			if (!$('#editForm').form('validate')) {
							$.messager.show({
								title : '提示信息',
								msg : '验证没有通过,不能提交表单!'
							});
							return;
						}
			  			if($('#alterType').combobox('getValue')=="D"){
			  				if(endTime ==""&&sTime==""){
			  					$.messager.alert('操作提示', '开始预警时间,结束预警时间都要填写！');  
			  					setTimeout(function(){
			  						$(".messager-body").window('close');
			  					},3500);
		                        return false; 
				  			}
				  			if(endTime !="" && sTime==""){
				  				$.messager.alert('操作提示', '开始预警时间要填写！');  
				  				setTimeout(function(){
				  					$(".messager-body").window('close');
				  				},3500);
		                        return false; 
				  			}if(endTime =="" && sTime!=""){
				  				$.messager.alert('操作提示', '结束预警时间都要填写！'); 
				  				setTimeout(function(){
				  					$(".messager-body").window('close');
				  				},3500);
		                        return false; 
				  			}
				  			if(!compareTime(sTime,endTime)){
				  				$.messager.alert('操作提示', '开始预警时间大于结束预警时间！');
				  				setTimeout(function(){
				  					$(".messager-body").window('close');
				  				},3500);
		                        return false; 
				  			}
			  			}
			  			 if($('#alterType').combobox('getValue')=="M"){
			  				if($('#moneyAlert').numberbox("getValue")==""){
			  					$.messager.alert('操作提示', '请填写预警金额！'); 
			  					setTimeout(function(){
			  						$(".messager-body").window('close');
			  					},3500);
		                        return false;
			  				}
			  			}
			  			 $.messager.progress({text:'正在设置,请稍等...',modal:true});
					 },
					success:function(data){
						$.messager.progress('close');
						  if(data=="no"){
							  $.messager.alert('提示',"设置失败!");
							  setTimeout(function(){
									$(".messager-body").window('close');
								},3500);
							  closeDialog();
						  }else if(data=="yes"){
							  $.messager.alert('提示',"设置成功！");
							  setTimeout(function(){
									$(".messager-body").window('close');
								},3500);
							  closeDialog();
							  $('#list').datagrid('reload');
						  }
					 },
					error:function(date){
						$.messager.progress('close');
						$.messager.alert('提示',"设置失败");
						setTimeout(function(){
							$(".messager-body").window('close');
						},3500);
						closeDialog();
					}
			  	});
		  	}
		/**
		 *清除所填信息
		 */
		function cleard() {
			$('#editForm').form('reset');
		}
		/**
		 *比较时间
		 */
		function compareTime(sTime,endTime){
			var d1 = new Date(sTime.replace(/\-/g, "\/")); 
			var d2 = new Date(endTime.replace(/\-/g, "\/"));
			if(d1>=d2){
				return false;
			}else{
				return true;
			}
		} 
	/**
	 *根据不同的选择类别获得不同的值
	 */
	$('#alterType').combobox({    
	    onSelect:function(record){
	    	if(record.id=="M"){
	    		$('#moneyAlert').numberbox('enable');
				$('#alterBegin').hide();
				$('#alterEnd').hide();
			}else if(record.id=="D"){
 				$('#moneyAlert').numberbox('disable');
 				$('#alterBegin').show();
				$('#alterEnd').show();
			}
	    }
	});
</script>
</body>
</html>
