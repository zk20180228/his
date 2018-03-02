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
	<div id="addWindow" class="easyui-panel" data-options="fit:'true',border:'false'">
		<form id="addForm" method="post">
			<table id="addtable" class="honry-table" style="width:99%;margin:5px 5px 5px 5px">
				<tr>
					<td colspan="4">
						<input id="deptCheckBox" name="deptCheckBox" type="checkbox" style="margin:6px 5px 5px 6px" onclick="keshi()"/>科室
						<input id="docCheckBox" name="docCheckBox" type="checkbox" style="margin:5px 5px 5px 20px" onclick="yisheng()"/>医生
					</td>
				</tr>
				<tr  style="display: none;">
					<td colspan="4">
						<input id="specs" name="spedrug.specs" type="hidden"/>
						<input id="type1" name="type1" type="hidden"/>
						<input id="type0" name="type0" type="hidden"/>
						<input id="doctor" name="doctor" type="hidden"/>
						<input id="dept" name="dept" type="hidden"/>
						<input id="docName" name="docName" type="hidden"/>
						<input id="deptName" name="deptName" type="hidden"/>
						<input id="drugCode" name="spedrug.drugCode" type="hidden"/>
					</td>
				</tr>
				<tr>
					<td class="honry-lable">指定科室：</td>
					<td>
						<input id="speDepCodeHidden" type="hidden"  data-options="url:'<%=basePath%>baseinfo/departmentContact/treeDept.action'"/>
						<input id="speDepCode" type="hidden" data-options="url:'<%=basePath%>baseinfo/departmentContact/treeDept.action'">
		    			<input id="speDepCodeText" type="text"  style="width: 150"/>
						<a href="javascript:deldept();"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a>
					</td>
					<td class="honry-lable">指定医生：</td>
					<td>
						<input id="speDocCodeHidden" type="hidden" data-options="url:'<%=basePath%>drug/spedrug/queryEmpCombotree.action'"/>
						<input id="speDocCode" type="hidden" data-options="url:'<%=basePath%>drug/spedrug/queryEmpCombotree.action'">
		    			<input id="speDocCodeText" type="text"  style="width: 150"/>
						<a href="javascript:deldoc();"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a>
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						药品名称：
					</td>
					<td>
						<input id="add_tradeName" name="spedrug.tradeName" class="easyui-textbox" readonly="readonly"/>
					</td>
					<td class="honry-lable">备注：</td>
					<td>
						<textarea id="update_memo" name="spedrug.memo" style="height: 50px;width: 230px;border-color: #E0ECFF;"></textarea>
					</td>
				</tr>
			</table>
			<div style="margin:5px 5px 5px 5px;text-align: center">
			<a href="javascript:void(0)"  class="easyui-linkbutton" onclick="queding()" data-options="iconCls:'icon-save'" style="margin:10px 0 0 20px" >保&nbsp;存&nbsp;</a>&nbsp;&nbsp;&nbsp;&nbsp;
			<a href="javascript:void(0)"  class="easyui-linkbutton" onclick="quxiao()" data-options="iconCls:'icon-cancel'" style="margin:10px 0 0 20px" >取&nbsp;消&nbsp;</a>&nbsp;&nbsp;&nbsp;&nbsp;
			</div>
		</form>
	</div>
	<script type="text/javascript">
	var rowDataLeft='${rowDataLeft}';
	var row=eval("("+rowDataLeft+")");
	
	$(function(){
		$('#add_tradeName').val(row.name);
		$('#specs').val(row.spec);
		$('#drugCode').val(row.id);
		//指定科室下拉树的点击处理
		$('#speDepCodeText').textbox({
				disabled:true
			}); 
		$('#speDocCodeText').textbox({
				disabled:true
			}); 
		
		$('#speDepCodeText').textbox({
			onSelect:function(node){
				//只有科室节点才能被选中
				if(node.attributes.pid==1||node.attributes.pid==""||node.attributes.pid==null){
					$('#speDepCodeText').textbox('clear');
				}else{
					$('#dept').val(node.deptCode);
				}
			}
		});
	});
	//医生复选框的点击处理
	function yisheng(){
		if($('#docCheckBox').is(":checked")){
			$('#speDocCodeText').textbox({
				required:true,
				prompt:'请回车选择医生',
				disabled:false
			});
			$('#type1').val("yes");
			bindEnterEvent('speDocCodeText',popWinToEmployee,'easyui');//绑定回车事件
		}else{
			$('#speDocCodeText').textbox('setValue','');
			$('#speDocCodeText').textbox({
				required:false,
				disabled:true
			});
			$('#doctor').val("");
			$('#speDocCode').val("");
			$('#type1').val("no");
		}
	}
	
	 //科室复选框的点击处理
	function keshi(){
		if($('#deptCheckBox').is(":checked")){
			$('#speDepCodeText').textbox({
				required:true,
				prompt:'请回车选择科室',
				disabled:false
			}); 
			$('#type0').val("yes");
			bindEnterEvent('speDepCodeText',popWinToDept,'easyui');//绑定回车事件
		}else{
			$('#speDepCodeText').textbox('setValue','');
			$('#speDepCodeText').textbox({
				required:false,
				disabled:true
			});
			$('#dept').val("");
			$('#speDepCode').val("");
			$('#type0').val("no");
		}
	}
	/**
	*删除科室
	**/
	function deldept(){
		$('#dept').val("");
		$('#speDepCode').val("");
		delSelectedData('speDocCodeText');
	}
	/**
	*删除医生
	**/
	function deldoc(){
		$('#doctor').val("");
		$('#speDocCode').val("");
		delSelectedData('speDocCodeText');
	}
	
	//确定（保存）事件
	function queding(){
		var docId=$('#doctor').val();
		var depId=$('#dept').val();
		var docName=$('#speDepCodeText').textbox('getText');
		var depName=$('#speDocCodeText').textbox('getText');
		if($('#docCheckBox').is(":checked")){
			if(docId==null||docId==''){
				  $.messager.alert('警告','请选择医生！'); 
				  setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
				  return;
			}
		}
		if($('#deptCheckBox').is(":checked")){
			if(depId==null||depId==''){
				  $.messager.alert('警告','请选择科室！'); 
				  setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
				  return;
			}
		}
		var drugName=$('#add_tradeName').val();
		if($('#docCheckBox').is(":checked")||$('#deptCheckBox').is(":checked")){
			//判断相同的药品指定科室和指定医生有没有相同的记录
			$.ajax({
				url:"<%=basePath%>drug/spedrug/queryEqual.action",
				data:{drugName:drugName,depId:depId,docId:docId},
				type:'post',
				success:function(result){
					if(result.resMsg=="deptDocSame"){
						$.messager.alert('提示',result.resCode);
					}else if(result.resMsg=="deptSame"){
						$.messager.alert('提示',result.resCode);
					}else if(result.resMsg=="docSame"){
						$.messager.alert('提示',result.resCode);
					}else if(result.resMsg=="success"){
						$.messager.progress({text:'保存中，请稍后...',modal:true});	// 显示进度条
						$('#addForm').form('submit',{
							url:"<%=basePath%>drug/spedrug/saveBothDatagrid.action",
							data:{docId:docId,depId:depId,docName:docName,depName:depName},
							success:function(){
								$.messager.progress('close');
								$.messager.alert('提示',"保存成功");
								quxiao();
								reloadRightTable1();
								reloadRightTable2();
							},error:function(){
								$.messager.progress('close');
								$.messager.alert('提示',"保存失败");
							}
						});
					}else{
						$.messager.alert('提示',"未知错误请联系管理员");
					}
				}
			});
		}else{
			$.messager.alert('操作提示',"请选择指定科室或者指定医生");
			setTimeout(function(){
				$(".messager-body").window('close');
			},3500);
		}
		
	}
	function quxiao(){
		closeDialogAdd();
	}
	 /**
	   * 回车弹出指定科室弹框
	   * @author  zhuxiaolu
	   * @param deptIsforregister 是否是挂号科室 1是 0否
	   * @param textId 页面上commbox的的id
	   * @date 2016-03-22 14:30   
	   * @version 1.0
	   */
	   function popWinToDept(){
		   popWinDeptCallBackFn = function(node){
		    	$("#speDepCode").val(node.deptCode);
		    	$('#dept').val(node.deptCode);
		    	$('#deptName').val(node.deptName);
				 $('#speDepCodeText').textbox('setValue',node.deptName);
			};
			var tempWinPath = "<%=basePath%>popWin/popWinEmployee/toEmployeePopWin.action?textId=speDepCodeText&employeeType=1";
			window.open (tempWinPath,'newwindow',' left=150,top=80,width='+ (screen.availWidth -300) +',height='+ (screen.availHeight-170)+',scrollbars,resizable=yes,toolbar=yes')
			var tempWinPath = "<%=basePath%>popWin/popWinDepartment/toDepartmentPopWin.action?&textId=speDepCode";
			window.open (tempWinPath,'newwindow',' left=150,top=80,width='+ (screen.availWidth -300) +',height='+ (screen.availHeight-170)+',scrollbars,resizable=yes,toolbar=yes')
		}
	
	   /**
		   * 回车弹出申请医师弹框
		   * @author  zhuxiaolu
		   * @param textId 页面上commbox的的id
		   * @date 2016-03-22 14:30   
		   * @version 1.0
		   */
		   function popWinToEmployee(){
			   popWinEmpCallBackFn = function(node){
			    	$("#speDocCode").val(node.jobNo);
			    	$('#doctor').val(node.jobNo);
			    	$('#docName').val(node.name);
					 $('#speDocCodeText').textbox('setValue',node.name);
				};
				var tempWinPath = "<%=basePath%>popWin/popWinEmployee/toEmployeePopWin.action?textId=speDocCodeText&employeeType=1";
				window.open (tempWinPath,'newwindow',' left=150,top=80,width='+ (screen.availWidth -300) +',height='+ (screen.availHeight-170)+',scrollbars,resizable=yes,toolbar=yes')
		 }
</script>
</body>
</html>