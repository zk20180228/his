<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/metas.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<body>
	<div class="easyui-panel" id = "panelEast" data-options="title:'编辑',iconCls:'icon-form',border: false" >
		<div style="padding:5px">
			<form id="editForm" method="post">
			<input type="hidden" name="id" value="${minfeeStatCode.id }">
				<table class="honry-table" cellpadding="1" cellspacing="1" border="false" style="margin-left:auto;margin-right:auto;">
	    			<tr>
						<td class="honry-lable">报表名称：</td>
		    			<td class="honry-info"><input  id="reportName" name="reportName" value="${minfeeStatCode.reportName }" data-options="required:true" style="width:200px"/>
		    			<input type="hidden" id="reportType" name="reportType" value="${minfeeStatCode.reportType }"/>
		    			</td>
	    			</tr>
	    			<tr>
						<td class="honry-lable">报表代码：</td>
		    			<td class="honry-info"><input class="easyui-textbox" id="reportCode" name="reportCode" value="${minfeeStatCode.reportCode }" readonly="readonly" data-options="required:true"  style="width:200px" /></td>
	    			</tr>
	    			<tr>
						<td class="honry-lable">统计费用名称：</td>
		    			<td class="honry-info"><input  id="feeStatName" name="feeStatName" value="${minfeeStatCode.feeStatName }" data-options="required:true" style="width:200px" />
	    				<a href="javascript:delSelectedData('feeStatName');"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a></td>
	    			</tr>
	    			<tr>
						<td class="honry-lable">统计费用代码：</td>
		    			<td class="honry-info"><input class="easyui-textbox" id="feeStatCode" name="feeStatCode" value="${minfeeStatCode.feeStatCode }" readonly="readonly" data-options="required:true" style="width:200px" /></td>
	    			</tr>
	    			<tr>
						<td class="honry-lable">医保中心统计费用名称：</td>
		    			<td class="honry-info"><input id="centerStatName" name="centerStatName" value="${minfeeStatCode.centerStatName }" style="width:200px" /></td>
	    			</tr>
	    			<tr>
						<td class="honry-lable">医保中心统计费用代码：</td>
		    			<td class="honry-info"><input class="easyui-textbox" id="centerStatCode" name="centerStatCode" value="${minfeeStatCode.centerStatCode }" readonly="readonly"  style="width:200px" /></td>
	    			</tr>
	    			<tr>
						<td class="honry-lable">最小费用名称：</td>
		    			<td class="honry-info"><input  id="minfeeName" name="minfeeName" value="${minfeeStatCode.minfeeName }" data-options="required:true" style="width:200px" /></td>
	    			</tr>
	    			<tr>
						<td class="honry-lable">最小费用代码：</td>
		    			<td class="honry-info"><input class="easyui-textbox" id="minfeeCode" name="minfeeCode" value="${minfeeStatCode.minfeeCode }" readonly="readonly" data-options="required:true" style="width:200px" /></td>
	    			</tr>
	    			<tr>
						<td class="honry-lable">执行科室：</td>
		    			<td class="honry-info"><input id="exeDeptIdHidden" type="hidden" data-options="required:true" style="width:200px"/>
			    			
			    			<input id="exeDeptId"  name="exeDeptId" type="hidden" value="${deptId }" data-options="required:true" style="width:200px"/>
		    				<input id="exeDeptIdText" data-options="prompt:'回车执行'" style="width: 200" value="${name }"/>
		    			<a href="javascript:delSelectedData('exeDeptIdText');"  class="easyui-linkbutton" 
		    			   data-options="iconCls:'icon-opera_clear',plain:true"></a>
		    			</td>
		    			
	    			</tr>
				</table>
				<div style="text-align:center;padding:5px">
<%--			    	<a href="javascript:addContinue();" class="easyui-linkbutton" data-options="iconCls:'icon-save'">连续添加</a>--%>
			    	<a href="javascript:submitForm();" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a>
			    	<a href="javascript:clear();void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-clear'">清除</a>
			    	<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="closeLayout()">关闭</a>
			    </div>
			</form>
		</div>
	</div>
	<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/js/easyui-combobox-filterLocal.js"></script>
<script type="text/javascript">
var popWinDeptCallBackFn = null;
//清除
function clear(){
	$('#editForm').form('reset');
}
//关闭
function closeLayout(){
	$('#divLayout').layout('remove','east');
}
//报表名称
$('#reportName').combobox({    
	url:"<%=basePath %>baseinfo/minFeeStatCode/comboxReportName.action", 
	queryParams:{dicType:'feecodestat'},
    valueField:'name',    
    textField:'name',
    onSelect:function(record){
    	$('#reportCode').textbox('setValue',record.encode);
    	$('#reportType').val(record.pinyin);
    },
    onLoadSuccess:function(){
    	/* var node = $('#tDt').tree('getSelected');
    	$('#reportName').combobox('select',node.text);
        $('#reportCode').textbox('setValue',node.id);
    	$('#reportType').val(node.attributes.pinyin); */
    }
}); 
//统计费用名称
$('#feeStatName').combobox({    
	url:"<%=basePath %>baseinfo/minFeeStatCode/comboxReportName.action", 
	queryParams:{dicType:'casminfee'},
    valueField:'name',    
    textField:'name',
    onSelect:function(record){
    	$('#feeStatCode').textbox('setValue',record.encode);
    }
});

//医保中心统计费用名称
$('#centerStatName').combobox({    
	url:"<%=basePath %>baseinfo/minFeeStatCode/comboxReportName.action", 
	queryParams:{dicType:'centerfeecode'},
    valueField:'name',    
    textField:'name',
    onSelect:function(record){
    	$('#centerStatCode').textbox('setValue',record.encode);
    }
});
//最小费用名称
$('#minfeeName').combobox({    
	url:"<%=basePath %>baseinfo/minFeeStatCode/comboxReportName.action", 
	queryParams:{dicType:'drugMinimumcost'},
    valueField:'name',    
    textField:'name',
 //   mode:'remote',
    onSelect:function(record){
    	$('#minfeeCode').textbox('setValue',record.encode);
    },onHidePanel:function(none){
        var data = $(this).combobox('getData');
        var val = $(this).combobox('getValue');
        var result = true;
        for (var i = 0; i < data.length; i++) {
            if (val == data[i].name) {
                result = false;
            }
        }
        if (result) {
            $(this).combobox("clear");
        }else{
            $(this).combobox('unselect',val);
            $(this).combobox('select',val);
        }
    },
    filter: function(q, row){
        var keys = new Array();
        keys[keys.length] = 'encode';
        keys[keys.length] = 'name';
        keys[keys.length] = 'pinyin';
        keys[keys.length] = 'wb';
        keys[keys.length] = 'inputCode';
        return filterLocalCombobox(q, row, keys);
    }
});

function submit(){ 
	$('#editForm').form('submit',{  
    	url:"<%=basePath %>baseinfo/minFeeStatCode/saveMinFeeStatCode.action",  
    	onSubmit:function(){
			if (!$('#editForm').form('validate')) {
				$.messager.show({
					title : '提示信息',
					msg : '验证没有通过,不能提交表单!'
				});
				return false;
			}
			$.messager.progress({text:'保存中，请稍后...',modal:true});
    	},  
        success:function(){ 
        	$.messager.progress('close');
        	$.messager.alert('提示','保存成功');
        	$("#list").datagrid("reload");
        	closeLayout();
        },
		error : function(data) {
			$.messager.progress('close');
			$.messager.alert('提示','保存失败！');	
		}							         
	}); 
}
//验证唯一
function submitForm(){
	var id = $('#id');
	var reportCode = $('#reportCode').textbox('getValue');
	var feeStatCode = $('#feeStatCode').textbox('getValue');
	var centerStatCode = $('#centerStatCode').textbox('getValue');
	var minfeeCode = $('#minfeeCode').textbox('getValue');
	if(id == null){
		$.ajax({
			url: "<c:url value='/baseinfo/minFeeStatCode/submitForm.action'/>?reportCode="+reportCode+"&feeStatCode="+feeStatCode+"&centerStatCode="+centerStatCode+"&minfeeCode="+minfeeCode,
			type:'post',
			success: function(dataMap) {
		   			if(dataMap.resMsg=="error"){
		   				$.messager.alert('提示','数据已存在，请重新填写');
		   				close_alert();
		        	}else if(dataMap.resMsg=="success"){
		        		submit();
		        	}
			}
		});	
	}else{
		submit();
	}
	
}
/**
 * 用法回车弹出事件高丽恒
 * 2016-03-22 14:41
 */
bindEnterEvent('feeStatName',popWinToFeeStatName,'easyui');
function popWinToFeeStatName(){
	popWinCommCallBackFn = function(node){
		 $('#feeStatName').combobox('setValue',node.name);
		 $('#feeStatCode').textbox('setValue',node.encode);
	};
	var tempWinPath = "<%=basePath%>popWin/popWinCode/toCodePopWinXMLList.action?type=casminfee&textId=feeStatName";
	var aaa=window.open (tempWinPath,'newwindow',' left=100,top=50,width='+ (screen.availWidth - 200) +',height='+ (screen.availHeight-110) +',scrollbars,resizable=yes,toolbar=no')
}
		/**
		* 回车弹出执行科室选择窗口
		* @author  zhuxiaolu
		* @param deptIsforregister 是否是挂号科室 1是 0否
		* @param textId 页面上commbox的的id
		* @date 2016-03-22 14:30   
		* @version 1.0
		*/
	
		$('#exeDeptIdText').textbox({
			readonly:true,
			required:true,
		});
		bindEnterEvent('exeDeptIdText',popWinToDept,'easyui');//绑定回车事件
		function popWinToDept(){
			 popWinDeptCallBackFn = function(node){
				 $("#exeDeptId").val(node.id);
				 $('#exeDeptIdText').textbox('setValue',node.deptName);
			   };
			var tempWinPath = "<%=basePath%>popWin/popWinDepartment/toDepartmentPopWin.action?textId=exeDeptIdText";
			window.open (tempWinPath,'newwindow',' left=100,top=50,width='+ (screen.availWidth -200) +',height='+ (screen.availHeight-110) 
		    +',scrollbars,resizable=yes,toolbar=yes')
		}
		
</script>
</body>
</html>