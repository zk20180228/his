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
<div class="easyui-panel" id="panelEast" border="none">
	<div style="padding:10px;">
   		<form id="editForm" method="post">
			<table class="honry-table" cellpadding="0" cellspacing="0" border="0" style="margin-left:auto;margin-right:auto;">
				<tr><input type="hidden" id='id' name="stoTerminalSpe.id" value="${stoTerminalSpe.id}"/>
					<td class="honry-lable">
						药房：
					</td>
					<td class="honry-info">
						<input class="easyui-combobox" id="dept" name="stoTerminalSpe.deptid"
						 value="${stoTerminalSpe.deptid }"
							data-options="required:true" missingMessage="请选择药房" style="width: 200"/>
					</td>
    			</tr>
				<tr>
					<td class="honry-lable">
						类别：
					</td>
					<td id="leibie" class="honry-info">
						<input class="easyui-combobox" id="itemType" name="stoTerminalSpe.itemType"
						 value="${stoTerminalSpe.itemType }"
							missingMessage="请选择要添加的特殊类别" style="width: 200"/>
					</td>
    			</tr>
				<tr>
					<td class="honry-lable">
						配药台：
					</td>
					<td id='peiyao' class="honry-info">
						<input class="easyui-combobox" id="code" name="stoTerminalSpe.code"
						value="${stoTerminalSpe.code}" 
							missingMessage="请选择配药台" style="width: 200"/>
					</td>
    			</tr>
    			<tr>
					<td class="honry-lable">
						备注：
					</td>
					<td class="honry-info">
						<textarea class="easyui-textbox" rows="2" cols="32" id="mark" name="stoTerminalSpe.mark"
							data-options="multiline:true" maxlength="25" style="width:200;height:80">${stoTerminalSpe.mark}</textarea>
					</td>
    			</tr>
				<tr>
					<td class="honry-lable">
						项目名称：
					</td>
					<td class="honry-info">
						<input id="itemName" type="text" name='stoTerminalSpe.itemName'
						 value="${stoTerminalSpe.itemName}"
							missingMessage="点击键盘" style="width: 200;height: 50"/>
							<input type="hidden" id="iCode" name="stoTerminalSpe.itemCode" value="${stoTerminalSpe.itemCode}">
					</td>
    			</tr>
				<tr style="display: none">
					<td class="honry-lable">
						数据：
					</td>
					<td class="honry-info">
						<input id="itemCode" type="text" name='jsDataJSON'
							style="width: 200;height: 50"/>
					</td>
    			</tr>
	    	</table>
	    	 <div style="text-align:center;padding:5px">
		    	<a href="javascript:markUpdata();" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a>
		    	<a href="javascript:closeLayout();" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'">关闭</a>
		    </div>
    	</form>
    </div>
</div>
<script type="text/javascript">

$(function(){
	
	<%-- 初始化类别，配药台，项目名称，备注  文本框 --%>
	$('#itemType').combobox({ 
		required:true,
		//readonly:true
	})
	//配药台 
	$('#code').combobox({
		required:true,
		//readonly:true
	})
	//项目名称 
	$('#itemName').textbox({ 
		required:true,
		multiline:true,
		editable:false
	})
	//数据 
	$('#itemCode').textbox({ 
		multiline:true,
		readonly:true
	})
	
	funitemType("${stoTerminalSpe.deptid }");
	funcode("${stoTerminalSpe.itemType }");

	$('#itemName').textbox('textbox').keydown(function(e){
		funadditem();
    });
	<%-- 加载药房     根据药房查找药房下配药台类别   根据药房和类别查找 配药台    --%>
	$('#dept').combobox({    
		url:'<%=basePath%>drug/specialDispensingtable/loadDept.action',
	    valueField:'deptCode',    
	    textField:'deptName',
	    editable:false,
	    onSelect:function(record){
	    	$('#dept').val(record);
	    	$('#itemType').combobox('destroy');
	    	$('#leibie').append(
		    	"<input class='easyui-combobox' id='itemType' name='itemType'"+
					"missingMessage='请选择要添加的特殊类别' style='width: 200'/>")
			$('#itemType').combobox({
				required:true,
				editable:false
			})	
			$('#code').combobox('destroy');
	    	$('#peiyao').append(
	    		"<input class='easyui-combobox' id='code' name='code'"+ 
					"missingMessage='请选择配药台' style='width: 200'/>")
	    	$('#code').combobox({ 
				required:true,
				editable:false,
				//readonly:true
			})	
			
	    	funitemType(record);
	    	$('#code').combobox('setValue', '');
	    	$('#itemCode').textbox('setValue', '');
	    	$('#itemName').textbox('setValue','');
// 	    	$('#itemName').textbox('disable');
	    	$('#itemName').textbox({
	    		disable:true
	        });
	    }
	});
});


<%--***  根据药房信息查找药房下配药台类别     ****************         添加修改js  begin           ***********************************--%>
function funitemType(dept){
	$('#itemType').combobox({    
		data:getitemTypeArray(),
	    valueField:'id',    
	    textField:'value',
	   // editable:false,
	    onChange:function(record){
	    	
	    	$('#code').combobox('destroy');
	    	$('#peiyao').append(
	    		"<input class='easyui-combobox' id='code' name='code'"+ 
					"missingMessage='请选择配药台' style='width: 200'/>")
	    	$('#code').combobox({ 
				required:true,
				editable:false
			})	

	    	funcode(record);
	    	
	    	$('#itemCode').textbox('setValue', '');
// 	    	$('#itemName').textbox('disable');
	    	$('#itemName').textbox({
	    		disable:true
	        });
	    }
	}); 
}

<%--***  根据药房信息  配药台类别     查找配药台***--%>
function funcode(record){
	$('#code').combobox({ 
		//readonly:true
	})
	var deptid=$("#dept").val();
// 	var deptid=$("#dept").combobox('getValue');
	$('#code').combobox({    
		url:'<%=basePath%>drug/specialDispensingtable/dispensingTableForDeptitemType.action?deptid='+deptid+'&stoTerminalSpe.itemType='+record,
	    valueField:'id',    
	    textField:'name',
	    onChange:function(record){
	    	$('#itemName').textbox('setValue', '');
	    	$('#itemCode').textbox('setValue', '');
	    	bindEnterEvent('itemName', funadditem, 'easyui')
	    	
// 	    	$('#itemName').textbox({
// 	    		disable:false
// 	        });
	    }
	}); 
}


<%-- 渲染，拼接 药房下配药台类别下拉   信息 --%>
function itemTypeData(deptid){
	var obj='';
	$.ajax({
		url:'<%=basePath %>drug/specialDispensingtable/dispensingTableForDept.action?deptid='+deptid,
		type:'post',
		async:false,
		success:function(data){
			obj=eval("("+data+")");
		}
	});
	if(obj.length>0){
		var rev="[";
		$.each(obj,function(i,n){
			if(rev.length>1){
				rev+=",";
			}
			rev+="{'id':'"+n+"','value':'"+itemTypeMap.get(n)+"'}";
		});
		rev+="]";
		return eval("("+ rev +")");
	}
}


<%--***  添加相应类别的项目名称    *******--%>
function funadditem(){
	var title=$('#itemType').combobox('getText');
	var dialog_itemType=$('#itemType').combobox('getValue');
	if($("#d_item")){
		$("#d_item").remove();
	}
	if(dialog_itemType==1){
		openWindow('<%=basePath %>drug/specialDispensingtable/dialogDrugTabView.action?isupdata=' + 1 + "&dialogItemType=" +dialog_itemType);
<%-- 		Adddilog(title,'<%=basePath %>drug/specialDispensingtable/dialogDrugTabView.action?isupdata='+1); --%>
	}else if(dialog_itemType==2||dialog_itemType==4){
		openWindow('<%=basePath %>drug/specialDispensingtable/dialogTreeView.action?isupdata='+1 + "&dialogItemType=" +dialog_itemType);
<%-- 		Adddilogtree(title,'<%=basePath %>drug/specialDispensingtable/dialogTreeView.action?isupdata='+1); --%>
	}else{
		openWindow('<%=basePath %>drug/specialDispensingtable/dialogView.action?isupdata='+1 + "&dialogItemType=" +dialog_itemType);
<%-- 		Adddilog(title,'<%=basePath %>drug/specialDispensingtable/dialogView.action?isupdata='+1); --%>
	}
	
}

/**  
 *  
 * 下拉弹框
 *
 */
function openWindow(tempWinPath){
	var aaa=window.open(tempWinPath,'newwindow',' left=400,top=200,width='+ (screen.availWidth - 800) +',height='+(screen.availHeight-300) +',scrollbars,resizable=yes,toolbar=no')
}

//edit的保存按钮
function setValue(code,name){
	$('#itemCode').textbox('setValue', code);
	 $('#itemName').textbox('setValue', name);
}
//edit的保存按钮
function markUpdata(){
	var dept = $('#dept').combobox('getValue');
	var itemType = $('#itemType').combobox('getValue');
	var code = $('#code').combobox('getValue');
	var itemName = $('#itemName').textbox('getValue');
	
	var isequals=1;		
	if(dept!="${stoTerminalSpe.deptid }"){
		isequals=0;	
	}else if(itemType!="${stoTerminalSpe.itemType }"){
		isequals=0;	
	}else if(code!="${stoTerminalSpe.code}"){
		isequals=0;	
	}else if(itemName!="${stoTerminalSpe.itemName}"){
		isequals=0;	
	}
	
	//没有修改关键数据，修改了备注
	if(isequals==1){
		$('#editForm').form('submit',{  
			url:'<%=basePath%>drug/specialDispensingtable/updateMark.action',
	        onSubmit:function(){
				if (!$('#editForm').form('validate')) {
					$.messager.show({
						title : '提示信息',
						msg : '验证没有通过,不能提交表单!'
					});
					return false;
				}
	        },  
	        success:function(data){
	        	if(data=='success'){
	        		$.messager.alert('提示','保存成功');
					$('#list').datagrid('reload');
					$('#divLayout').layout('remove', 'east');
	        	}
	        },
			error : function(data) {
				$.messager.alert('提示','保存失败!');	
			}							         
	    }); 
	}else{
		$('#editForm').form('submit',{  
			url:'<%=basePath%>drug/specialDispensingtable/jsDataSaveOrUpdate.action',
	        onSubmit:function(){
				if (!$('#editForm').form('validate')) {
					$.messager.show({
						title : '提示信息',
						msg : '验证没有通过,不能提交表单!'
					});
					return false;
				}
	        },  
	        success:function(data){
        		data = eval('('+data+')');
	        	if(data.resCode=='success'){
	        		$.messager.alert('提示','保存成功');
					$('#list').datagrid('reload');
					$('#divLayout').layout('remove', 'east');
	        	}else{
	        		var sto = data.resMsg;
	        		$.messager.alert('提示','操作失败；原因：项目名称     '+sto+'    已存在');
	        		setTimeout(function(){$(".messager-body").window('close')},1500);
	        	}
	        },
			error : function(data) {
				$.messager.alert('提示','保存失败!');	
			}							         
	    }); 
	}
}

</script>
<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
</body>
</html>