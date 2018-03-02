<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
</head>
<body>
<script type="text/javascript">
	var stype='${spedrug.speType}';
	$('#speName').textbox({
		missingMessage:'请回车选择!',
		required:true,
		editable:false
	});
	bindEnterEvent('speName',pop,'easyui');//绑定回车事件
	$(function(){
		if(stype == 1){
			$('#name').html('指定医生：');
		}else{
			$('#name').html('指定科室：');
		}
	});
	/**
	 *  确定（保存）事件
	 **/
	function queding(){
		if(!$('#updateForm').form('validate')){
			$.messager.alert('提示',"请确认输入信息正确!");
			setTimeout(function(){
				$(".messager-body").window('close');
			},3500);
			return false;
		}
		var drugName=$('#update_tradeName').val();//药品名
		var docId;//医生code
		var depId;//科室code
		var docName;//医生名
		var depName;//科室名
		if(stype == 1){
			docId=$('#speCode').val();
			docName=$('#speName').textbox('getText');
		}else{
			depId=$('#speCode').val();
			depName=$('#speName').textbox('getText');
		}
		var id=$('#id').val();
		$.ajax({
			url:"<%=basePath%>drug/spedrug/queryEqualExceptId.action",
			data:{drugName:drugName,depId:depId,docId:docId,id:id},
			type:'post',
			success:function(result){
				if(result.resMsg=="deptSame"){
					$.messager.alert('提示',result.resCode);
					//清空下拉树和隐藏域
					$('speName').textbox('setValue',"");
					$('#speCode').val("");
				}else if(result.resMsg=="docSame"){
					$.messager.alert('提示',result.resCode);
					//清空下拉树和隐藏域
					$('speName').textbox('setValue',"");
					$('#speCode').val("");
				}else if(result.resMsg=="success"){
					$.messager.progress({text:'保存中，请稍后...',modal:true});	// 显示进度条
					$('#updateForm').form('submit',{
						url:"<%=basePath%>drug/spedrug/updateSpeDrug.action",
						onSubmit:function(){//不可删除，
						},
						success:function(){
							$.messager.progress('close');
							$.messager.alert('提示',"修改成功");
							if(stype != '1'){
								reload2();
							}else{
								reload1();
							}
							quxiao();
						},error:function(){
							$.messager.progress('close');
							$.messager.alert('提示',"保存失败");
						}
					});
				}
			}
		});
	}
	function quxiao(){
		closeDialog();  
	}
	
	 /**
	   * 确定回车弹框是选择科室还是医生
	   */
	   function pop(){
		   if(stype == 1){
			   popWinToEmployee();
		   }else{
			   popWinToDept();
		   }
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
		    	$('#speCode').val(node.deptCode);
				$('#speName').textbox('setText',node.deptName);
			};
		
			var tempWinPath = "<%=basePath%>popWin/popWinDepartment/toDepartmentPopWin.action?textId=update_speDepCodeText";
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
			    	$('#speCode').val(node.jobNo);
					 $('#speName').textbox('setText',node.name);
				};
				var tempWinPath = "<%=basePath%>popWin/popWinEmployee/toEmployeePopWin.action?textId=update_speDocCodeText&employeeType=1";
				window.open (tempWinPath,'newwindow',' left=150,top=80,width='+ (screen.availWidth -300) +',height='+ (screen.availHeight-170)+',scrollbars,resizable=yes,toolbar=yes')
		 }
	
</script>
	<div id="updateWindow" class="easyui-panel" data-options="fit:'true',border:'false'">
		<form id="updateForm" method="post">
			<table id="updatetable" class="honry-table" style="width:98%;margin:5px 10px 5px 5px">
				<input id="id" name="spedrug.id" value="${spedrug.id }" type="hidden"/>
				<input id="speType" name="speType" value="${spedrug.speType }" type="hidden"/>
				<input id="speCode" name="spedrug.speCode" type="hidden" value="${spedrug.speCode }">
				<tr>
					<td class="honry-lable">
						药品名称：
					</td>
					<td>
						<input id="update_tradeName" name="spedrug.tradeName" value="${spedrug.tradeName }" class="easyui-textbox" readonly="readonly"/>
					</td>
				</tr>
				<tr>
					<td class="honry-lable">
						规格：
					</td>
					<td>
						<input id="update_specs" name="spedrug.specs" value="${spedrug.specs }" class="easyui-textbox" readonly="readonly"/>
					</td>
				</tr>
				<tr >
					<td class="honry-lable" id="name">指定科室：</td>
					<td>
		    			<input id="speName" value="${spedrug.speName }" name="spedrug.speName"  style="width: 150;"/>
						<a href="javascript:delSelectedData('speName');"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a>
					</td>
				</tr>
				<tr >
					<td class="honry-lable">备注：</td>
					<td>
						<textarea id="update_memo" name="spedrug.memo" style="height: 50px;width: 230px;border-color: #E0ECFF;">${spedrug.memo }</textarea>
					</td>
				</tr>
			</table>
			<div style="margin:5px 5px 5px 5px;text-align: center">
				<a href="javascript:void(0)"  class="easyui-linkbutton" onclick="queding()" data-options="iconCls:'icon-save'" style="margin:10px 0 0 20px" >保&nbsp;存&nbsp;</a>&nbsp;&nbsp;&nbsp;&nbsp;
				<a href="javascript:void(0)"  class="easyui-linkbutton" onclick="quxiao()" data-options="iconCls:'icon-cancel'" style="margin:10px 0 0 20px" >取&nbsp;消&nbsp;</a>&nbsp;&nbsp;&nbsp;&nbsp;
			</div>
		</form>
	</div>
</body>
</html>