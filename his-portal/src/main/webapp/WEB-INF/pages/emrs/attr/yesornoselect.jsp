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
    <title>有无选择</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=Edge,chrome=1" >
</head>
<body>
<body>
	<div class="content">
		<form id="editForm" method="post">
		    <input id="attrType" type="hidden" name="attr.attrType" value="3"/>
		    <input id="attrId" type="hidden" name="attr.attrId" value="${attr.attrId }"/>
		    <input id="options" type="hidden" name="attr.options" value="${attr.options }"/>
	    	<input id="attrNotnull" type="hidden" name="attr.attrNotnull" value="${attr.attrNotnull }"/>
	    	<input id="attrMustSelect" type="hidden" name="attr.attrMustSelect" value="${attr.attrMustSelect }"/>
	    	<input id="attrStatflg" type="hidden" name="attr.attrStatflg" value="${attr.attrStatflg }"/>
	    	<input id="attrPrintflg" type="hidden" name="attr.attrPrintflg" value="${attr.attrPrintflg }"/>
		    <table class="honry-table" style="margin-left:auto;margin-right:auto;border-top:0">
				<tr>
		        	<td style="border-top:0" class="honry-lable"><span>控件代码</span> <span class="label label-important">*</span></td>
		        	<td style="border-top:0"><input id="attrCode" data-options="prompt:'控件代码'" type="text" class="easyui-textbox" name="attr.attrCode" value="${attr.attrCode }"/></td>
		        	<td style="border-top:0" class="honry-lable"><span>控件名称</span> <span class="label label-important">*</span></td>
		        	<td style="border-top:0"><input id="attrName" data-options="prompt:'控件名称'" type="text" class="easyui-textbox" name="attr.attrName" value="${attr.attrName }"/></td>
		    	</tr>
	    		<tr>
			    	<td class="honry-lable"><span>自定义码</span></td>
			    	<td><input id="inputcode" data-options="prompt:'自定义码'" type="text" class="easyui-textbox" name="attr.inputcode" value="${attr.inputcode }"/></td>
			    	<td class="honry-lable"><span>编码系统</span></td>
			    	<td><input id="attrCodetype" data-options="prompt:'编码系统'" type="text" class="easyui-textbox" name="attr.attrCodetype" value="${attr.attrCodetype }"/></td>
			    </tr>
	    		<tr>
			    	<td class="honry-lable"><span>前缀</span></td>
			    	<td><input id="attrPrefix" data-options="prompt:'前缀'" type="text" class="easyui-textbox" name="attr.attrPrefix" value="${attr.attrPrefix }"/></td>
			    	<td class="honry-lable"><span>后缀</span></td>
			    	<td><input id="attrSuffix" data-options="prompt:'后缀'" type="text" class="easyui-textbox" name="attr.attrSuffix" value="${attr.attrSuffix }"/></td>
			    </tr>
	    		<tr>
	    			<td class="honry-lable"><span>是否必选必填</span></td>
			    	<td><input id="attrNotnull1" checked="checked" name="attrNotnull" type="radio" value="1">  是  <input id="attrNotnull0" name="attrNotnull" type="radio" value="0"> 否</td>
			    	<td class="honry-lable"><span>是否必须选择</span></td>
			    	<td><input id="attrMustSelect1" checked="checked" name="attrMustSelect" type="radio" value="1"> 是 <input id="attrMustSelect0" name="attrMustSelect" type="radio" value="0"> 否 </td>
			    </tr>
	    		<tr>
	    			<td class="honry-lable"><span>是否用于科研</span></td>
			    	<td><input id="attrStatflg1" checked="checked" name="attrStatflg" type="radio" value="1"> 是 <input id="attrStatflg0" name="attrStatflg" type="radio" value="0"> 否</td>
			    	<td class="honry-lable"><span>是否打印</span></td>
			    	<td><input id="attrPrintflg1" checked="checked" name="attrPrintflg" type="radio" value="1"> 打印 <input id="attrPrintflg0" name="attrPrintflg" type="radio" value="0"> 不打印 </td>
			    </tr>
			</table>
		</form>
		<div style="padding-top: 5px">
	        <table class="honry-table" id="options_table" style="margin-left:auto;margin-right:auto;">
	            <tr>
	                <td class="honry-lable">是否选中</td>
	                <td class="honry-lable">选项值</td>
	                <td class="honry-lable">操作</td>
	            </tr>
	        </table>
	        <div style="padding-top: 5px ;text-align: center;" >
		        <a href="javascript:void(0)" onclick="fnAdd()" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true">添加选项</a>
		        <a href="javascript:void(0)" onclick="submit()" class="easyui-linkbutton" data-options="iconCls:'icon-save',plain:true">保存</a>
		        <a href="javascript:void(0)" onclick="closeLayout()" class="easyui-linkbutton" data-options="iconCls:'icon-close',plain:true">取消</a>
			</div>
		</div>
	</div>
	<script type="text/javascript">
	//页面加载
	$(function(){
			var gNotnullChecked = $('#attrNotnull').val();
			var gMustSelectChecked = $('#attrMustSelect').val();
			var gStatflgChecked = $('#attrStatflg').val();
			var gPrintflgChecked = $('#attrPrintflg').val();
			
			gNotnullChecked == 0? $('#attrNotnull0').checked = true : $('#attrNotnull1').checked = true;
			gMustSelectChecked == 0? $('#attrMustSelect0').checked = true : $('#attrMustSelect1').checked = true;
			gStatflgChecked == 0? $('#attrStatflg0').checked = true : $('#attrStatflg1').checked = true;
			gPrintflgChecked == 0? $('#attrPrintflg0').checked = true : $('#attrPrintflg1').checked = true;
			var options = eval($('#options').val());
	         if(options != undefined && options != ''){
	        	var length = options.length;
	            for(var i=0;i<length;i++)
	            {
                    fnAddComboTr(options[i]);
	            }
	        } 
	});
	/**
	 * 判断添加/修改表单提交
	 */
	function submit(){ 
		if( $('#attrCode').val() == '') {
			$.messager.alert('提示','控件代码不能为空！'); 
			setTimeout(function(){$(".messager-body").window('close')},3500);
	        return false;
	    }
	    if( $('#attrName').val() == '') {
	    	$.messager.alert('提示','控件名称不能为空！'); 
	    	setTimeout(function(){$(".messager-body").window('close')},3500);
	        return false;
	    }
	    var options = fnParseOptions("leipiNewField");
		if(!options)
		{
		    $.messager.alert('提示','请添加选项！'); 
		    setTimeout(function(){$(".messager-body").window('close')},3500);
		    return false;
		}
		$('#options').val(options);
		$('#attrNotnull').val($("input[name='attrNotnull']:checked").val());
		$('#attrMustSelect').val($("input[name='attrMustSelect']:checked").val());
		$('#attrStatflg').val($("input[name='attrStatflg']:checked").val());
		$('#attrPrintflg').val($("input[name='attrPrintflg']:checked").val());
	    var url;
		if($('#attrId').val() ==''){
			$.ajax({
	    		url: '<%=basePath %>emrs/attr/queryAttr.action?queryCode='+$('#attrCode').val(),
				success: function(data) {
					if(data.total > 0){
						$.messager.alert('提示',"您输入的控件编码已存在！！！！");
						setTimeout(function(){$(".messager-body").window('close')},3500);
						return false;
					}else{
						url = "<%=basePath %>emrs/attr/add.action"; 
						sub(url);
					}
				},
		    	error: function(){
		    	}
	    	});
		}else{
			url = "<%=basePath %>emrs/attr/edit.action";
			sub(url);
		}
	}	
		/**
		* 表单提交
		*/
		function sub(url) {
			$('#editForm').form('submit',{  
	        	url: url,  
	        	onSubmit : function() {
					if(!$('#editForm').form('validate')){
						$.messager.alert("操作提示",'请输入选项值！！');
						setTimeout(function(){$(".messager-body").window('close')},3500);
					       return false ;
					}
				},
		        success:function(data){  
		        	closeLayout();
		        },
				error : function(data) {
					$.messager.alert("提示",'保存失败！');	
				}							         
	   		}); 
		}
	
		/* 
		* 关闭界面
		*/
		function closeLayout(){
			$('#temWins').dialog('close'); 
			$('#list').datagrid('reload');
		}

		function createElement(type, name)
		{     
		    var element = null;     
		    try {        
		        element = document.createElement('<'+type+' name="'+name+'">');     
		    } catch (e) {}   
		    if(element==null) {     
		        element = document.createElement(type);     
		        element.name = name;     
		    } 
		    return element;     
		}

		//checkboxs
		function isIE()
		{
		    if(window.attachEvent){   
		        return true;
		    }
		    return false;
		}
		//moveRow在IE支持而在火狐里不支持！以下是扩展火狐下的moveRow
		if (!isIE()) {
		    function getTRNode(nowTR, sibling) {
		        while (nowTR = nowTR[sibling]) if (nowTR.tagName == 'TR') break;
		        return nowTR;
		    }
		    if (typeof Element != 'undefined') {
		        Element.prototype.moveRow = function(sourceRowIndex, targetRowIndex) //执行扩展操作
		        {
		            if (!/^(table|tbody|tfoot|thead)$/i.test(this.tagName) || sourceRowIndex === targetRowIndex) return false;
		            var pNode = this;
		            if (this.tagName == 'TABLE') pNode = this.getElementsByTagName('tbody')[0]; //firefox会自动加上tbody标签，所以需要取tbody，直接table.insertBefore会error
		            var sourceRow = pNode.rows[sourceRowIndex],
		            targetRow = pNode.rows[targetRowIndex];
		            if (sourceRow == null || targetRow == null) return false;
		            var targetRowNextRow = sourceRowIndex > targetRowIndex ? false: getTRNode(targetRow, 'nextSibling');
		            if (targetRowNextRow === false) pNode.insertBefore(sourceRow, targetRow); //后面行移动到前面，直接insertBefore即可
		            else { //移动到当前行的后面位置，则需要判断要移动到的行的后面是否还有行，有则insertBefore，否则appendChild
		                if (targetRowNextRow == null) pNode.appendChild(sourceRow);
		                else pNode.insertBefore(sourceRow, targetRowNextRow);
		            }
		        }
		    }
		}

		/*删除tr*/
		function fnDeleteRow(obj)
		{
		    var oTable = document.getElementById("options_table");
		    while(obj.tagName !='TR')
		    {
		        obj = obj.parentNode;
		    }
		    oTable.deleteRow(obj.rowIndex);
		}
		/*上移*/
		function fnMoveUp(obj)
		{
		    var oTable = document.getElementById("options_table");
		    while(obj.tagName !='TR')
		    {
		        obj = obj.parentNode;
		    }
		    var minRowIndex = 1;
		    var curRowIndex = obj.rowIndex;
		    if(curRowIndex-1>=minRowIndex)
		    {
		        oTable.moveRow(curRowIndex,curRowIndex-1); 
		    }
		    
		}
		/*下移*/
		function fnMoveDown(obj)
		{
		    var oTable = document.getElementById("options_table");
		    while(obj.tagName !='TR')
		    {
		        obj = obj.parentNode;
		    }
		    var maxRowIndex = oTable.rows.length;
		    var curRowIndex = obj.rowIndex;
		    if(curRowIndex+1<maxRowIndex)
		    {
		        oTable.moveRow(curRowIndex,curRowIndex+1); 
		    }
		}

		/*生成tr*/
		function fnAddComboTr(obj)
		{
		    var oTable = document.getElementById('options_table');
		    var new_tr_node= oTable.insertRow(oTable.rows.length);
		    var new_td_node0 = new_tr_node.insertCell(0),new_td_node1 = new_tr_node.insertCell(1),new_td_node2 = new_tr_node.insertCell(2);
		
		    var sChecked = '';
		    if(obj.optionDefaultflg) sChecked = 'checked="checked"';
		    if(!obj.optionName) obj.optionName = '';
		    if(!obj.optionValue) obj.optionValue = '';
		    new_td_node0.innerHTML = '<td><input type="checkbox" '+sChecked+' name="radio" ></td>';
		    new_td_node1.innerHTML = '<td><input type="text" class="check" value="'+obj.optionValue+'" ></td>';
		    new_td_node2.innerHTML ='<td><div ><a href="javascript:void(0)" class="upCls" onclick="fnMoveUp(this)"></a><a href="javascript:void(0)" class="downCls" onclick="fnMoveDown(this)"></a><a href="javascript:void(0)" class="removeCls" onclick="fnDeleteRow(this)"></a></div></td>';
		    $('.check').textbox({required:true,prompt:'选项值'}); 
		    $('.upCls').linkbutton({plain:true,iconCls:'icon-up'}); 
			$('.downCls').linkbutton({plain:true,iconCls:'icon-down'}); 
			$('.removeCls').linkbutton({plain:true,iconCls:'icon-remove'}); 
		    return true;
		}
		
		function fnAdd() {
		    fnAddComboTr({
		        "checked":false,
		        "name":'leipiNewField',
		        "value":''
		    });
		}
		/*组合checkbox*/
		function fnParseOptions(gName)
		{
			var oTable = document.getElementById('options_table');
		    var nTr = oTable.getElementsByTagName('tr'),trLength = nTr.length,html="";
		    var attrId = $('#attrId').val();
		    if( $("table#options_table tr:visible").length >1){
		    	html='{"options": [';
			    for(var i=0;i<trLength;i++)
			    {
			        var inputs = nTr[i].getElementsByTagName('input');
			        if(inputs.length>0)
			        {
			            if(!inputs[1].value) continue;
			            var sChecked = '';
			            if(inputs[0].checked) sChecked = ',"optionDefaultflg": "checked"';
			            html += '{"attrId": "'+attrId+'","optionName": "data_1","optionValue": "'+inputs[1].value+'"'+sChecked+'},';
			        }
			    }
			    html = html.substring(0, html.length-1)+']}';
		    }
		    return html;
		}
</script>
</body>
</html>