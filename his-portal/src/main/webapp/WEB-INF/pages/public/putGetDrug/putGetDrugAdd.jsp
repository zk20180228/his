<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/metas.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<base href="<%=basePath%>">
		<title>添加页面</title>
	</head>
	<body>
	<div>
		<div id="p" class="easyui-panel" style="padding: 10px; background: #fafafa;" data-options="fit:'true',border:false">
			<form id="addDForm" method="post">
				<table class="honry-table" cellpadding="1" cellspacing="1" border="1px solid black" style="margin-left:auto;margin-right:auto;">
				<input type="hidden" name="pgd.id" value="${pgd.id}"/>
				<input type="hidden" id='putdrug1' value="${pgd.putdrug}"/>
				<input type="hidden" id="getdrug1" value="${pgd.getdrug}"/>
				<input type="hidden" id="drugtype1" value="${pgd.drugtype}"/>
					<tr>
						<td class="honry-lable">
							<span>摆药科室:</span>
						</td>
						<td class="honry-info"><input id="putdrug" type="hidden" name="pgd.putdrug" value="${pgd.putdrug}" data-options="required:true" style="width:200px"/>
							<input id="putdrugText" name="putDeptName" value="${putDeptName}" data-options="required:true" style="width:200px"/>
						<a href="javascript:delSelectedData('putdrugText');"  class="easyui-linkbutton" 
						    data-options="iconCls:'icon-opera_clear',plain:true"></a>
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							<span>取药科室:</span>
						</td>
						<td class="honry-info">
							<input id="getdrug" type="hidden" name="pgd.getdrug" value="${pgd.getdrug}" data-options="required:true" style="width:200px"/>
							<input id="getdrugText" value="${deptName}" data-options="required:true,editable:false,prompt:'请回车选择取药科室'" style="width:200px"/>
							<a href="javascript:delSelectedData('getdrugText');"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a>
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							<span>药品类别:</span>
						</td>
						<td style="text-align: left;">
							<input style="width: 200px;" class="easyui-combobox" id=drugtype
									name="pgd.drugtype"
									value="${pgd.drugtype }"
									missingMessage="请选择药品类别" 
									data-options="required:true"
									editable="false"/>
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							<span>开始时间:</span>
						</td>
						<td style="text-align: left;">
							<input id="begintime" name="pgdbegintime" value="${pgdbegintime }" style="width: 200px;" class="Wdate" type="text"  onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',maxDate:'#F{$dp.$D(\'endtime\')}'})"/>
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							<span>结束时间:</span>
						</td>
						<td style="text-align: left;">
							<input id="endtime" name="pgdendtime" value="${pgdendtime }" style="width: 200px;" class="Wdate" type="text"  onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'#F{$dp.$D(\'begintime\')}'})"/>
						</td>
					</tr>
					<tr style="width:98%;height:300px">
						<td class="honry-lable">
							备注:
						</td>
						<td class="honry-info">
							<textarea class="easyui-textbox" rows="4" cols="32" id="mark" name="pgd.mark"
								data-options="multiline:true" maxlength="50" style="width:98%;height:100%">${pgd.mark}</textarea>
							
						</td>
					</tr>
				</table>
			</form>
			<div style="text-align: center; padding: 5px">
				<a id="hospitalOKbtn" href="javascript:addForisExist()" data-options="iconCls:'icon-save'" class="easyui-linkbutton">保存</a>
				<a id="hospitalClearbtn" href="javascript:void(0)" data-options="iconCls:'icon-cancel'" onclick="closeLayout()" class="easyui-linkbutton">关闭</a>
			</div>
		</div>
	</div>
	<script type="text/javascript">
	$(function(){
		//摆药科室
		$('#putdrugText').textbox({    
		}); 
		/**
		* 绑定摆药科室回车事件
		* @author  zhuxiaolu
		* @date 2016-03-22 14:30   
		* @version 1.0
		*/
		bindEnterEvent('putdrugText',popWinToDeptPutdrug,'easyui');//绑定回车事件
		//取药科室
		$('#getdrugText').textbox({    
		}); 
		bindEnterEvent('getdrugText',popWinToDeptGetdrug,'easyui');//绑定回车事件
		//药品类别
		$('#drugtype').combobox({ 
			url: '<%=basePath %>publics/putGetDrug/querydrugtype.action',
			valueField:'encode',    
			textField:'name'
		});
	});
	
	//数据验证
	function addForisExist() {
		var isequals=1;		
		if($('#putdrug').val()!=$('#putdrug1').val()){
			isequals=0;	
		}else if($('#getdrug').val()!=$('#getdrug1').val()){
			isequals=0;	
		}else if($('#drugtype').combotree('getValue')!=$('#drugtype1').val()){
			isequals=0;	
		}
		if(isequals!=1){
			$.ajax({
				url:'<%=basePath %>publics/putGetDrug/addForisExist.action',
				type:'post',
				data:$('#addDForm').serialize(),
				success:function(data){
					if(data.resCode=='F'){
						$.messager.alert('提示','数据已存在，操作失败！');
						setTimeout(function(){$(".messager-body").window('close')},1500);
					}else{
						onClickOKbtn();
					}
				}
			});
		}else{
			onClickOKbtn();
		}
	}
	
	//确定添加
	function onClickOKbtn() {
		var startDate=$('#begintime').val();
		var endDate=$('#endtime').val();
		if(''==startDate){
			$.messager.alert('提示信息','开始时间不能为空！');
			return false
		}
		if(''==endDate){
			$.messager.alert('提示信息','结束时间不能为空！');
			return false
		}
		$('#addDForm').form('submit', {
				url : "<%=basePath%>publics/putGetDrug/addPutGetDrug.action?begin="+$('#begintime').val()+"&end="+$('#endtime').val(),
				onSubmit : function() {
					if(!$(this).form('validate')){
						$.messager.alert('提示','请检查数据正确性！');
						setTimeout(function(){$(".messager-body").window('close')},1500);
						return false;  
					}
					$.messager.progress({text:'保存中，请稍后...',modal:true});
				},
				success : function(prompt) {
					$.messager.progress('close');
					$.messager.alert('提示','操作成功！');
					closeLayout();
					listReload();
				},
				error : function(data) {
					$.messager.progress('close');
					$.messager.show({
						title : '提示信息',
						msg : '添加失败!'
					});
				}
		});
	}
	//清除
	function clear(){
		$('#addDForm').form('clear');
	}
	/**
	* 回车弹出摆药科室选择窗口
	* @author  zhuxiaolu
	* @param deptIsforregister 是否是挂号科室 1是 0否
	* @param textId 页面上commbox的的id
	* @date 2016-03-22 14:30   
	* @version 1.0
	*/

	function popWinToDeptPutdrug(){
		popWinDeptCallBackFn = function(node){
	    	$("#putdrug").val(node.deptCode);
			 $('#putdrugText').textbox('setValue',node.deptName);
		};
		var timer=new Date().getTime();
		var tempWinPath = "<%=basePath%>popWin/popWinDepartment/toDepartmentPopWin.action?deptIsforregister=0&textId=putdrugText&deptType=PI,P&timer="+timer;
		window.open (tempWinPath,'newwindow',' left=400,top=200,width='+ (screen.availWidth -700) +',height='+ (screen.availHeight-500) 
	    +',scrollbars,resizable=yes,toolbar=yes')
	}
	/**
	* 回车弹出取药科室选择窗口
	* @author  zhuxiaolu
	* @param deptIsforregister 是否是挂号科室 1是 0否
	* @param textId 页面上commbox的的id
	* @date 2016-03-22 14:30   
	* @version 1.0
	*/

	function popWinToDeptGetdrug(){
		popWinDeptCallBackFn = function(node){
	    	$("#getdrug").val(node.deptCode);
	    	$('#getdrug1').val(node.deptCode);
			 $('#getdrugText').textbox('setValue',node.deptName);
		};
		var tempWinPath = "<%=basePath%>popWin/popWinDepartment/toDepartmentPopWin.action?textId=getdrugText";
		window.open (tempWinPath,'newwindow',' left=400,top=200,width='+ (screen.availWidth -700) +',height='+ (screen.availHeight-370) 
	    +',scrollbars,resizable=yes,toolbar=yes')
	}
</script>
	</body>
</html>
