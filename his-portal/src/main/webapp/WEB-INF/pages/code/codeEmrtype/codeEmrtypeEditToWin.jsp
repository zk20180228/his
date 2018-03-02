<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<jsp:include page="/javascript/default.jsp"></jsp:include>
<html>
<body>
	<div class="easyui-panel" data-options="fit:true,iconCls:'icon-form',border:false">
		<div data-options="region:'center',border:false"  style="padding: 10px">
				
			<form id="editForm" method="post">
			<input type="hidden" id="id" name="id" value="${codeEmrtype.id }">
				<table class="honry-table" cellpadding="0" cellspacing="0" style="width:100%" data-options="border:false"
					border="0">
					<tr>
						<td class="honry-lable">
							电子病历分类名称:
						</td>
						<td class="honry-info">
							<input class="easyui-validatebox" name="codeEmrtype.name"
								value="${codeEmrtype.name}" data-options="required:true"
								style="width: 290px" />
							&nbsp;
						</td>
					</tr>
					<input type="hidden"" name="codeEmrtype.parent"  id="parentId"value="${codeEmrtype.parent}">
					<input type="hidden" name="codeEmrtype.levell"  id="levell" value="${codeEmrtype.levell}" >
					<tr>
						<td class="honry-lable">
							代码:
						</td>
						<td class="honry-info">
							<input class="easyui-validatebox" name="codeEmrtype.encode"
								value="${codeEmrtype.encode}" data-options="required:true"
								style="width: 290px" />
							&nbsp;
						</td>
					</tr>
					<c:if test="${not empty codeEmrtype.id }">

						<tr>
							<td class="honry-lable">
								拼音码:
							</td>
							<td class="honry-info">
								${codeEmrtype.pinyin }
								&nbsp;
							</td>
						</tr>
						<tr>
							<td class="honry-lable">
								五笔码:
							</td>
							<td class="honry-info">
								${codeEmrtype.wb}
								&nbsp;
							</td>
							</tr>
					</c:if>
					
					<tr>
						<td class="honry-lable">
							自定义码:
						</td>
						
						<td class="honry-info">
							<input class="easyui-validatebox" name="codeEmrtype.inputcode"
								value="${codeEmrtype.inputcode }" data-options="required:true"
								style="width: 290px" />
							&nbsp;
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							说明:
						</td>
						<td class="honry-info">
							<input class="easyui-validatebox" name="codeEmrtype.description"
								value="${codeEmrtype.description}" data-options="required:true"
								style="width: 290px" />
							&nbsp;
						</td>
					</tr>
					<tr>
	  				<td class="honry-lable">排序:</td>
	  				<td class="honry-info">
	  				<input class="easyui-validatebox" disabled="disabled" type="text" id="paixu" name="codeEmrtype.order" value="${codeEmrtype.order }" data-options="required:true" style="width: 290px" />
	  				</td>
	  			</tr>
					<tr>					
							<td class="honry-lable">可选标志:</td>
			    			<td>
				    			<input type="hidden"  id="canSelect" name="codeEmrtype.canSelect" value="${codeEmrtype.canSelect }"/>
				    			<input type="checkBox" id="canSelectcomb"  onclick="javascript:checkBoxSelect('canSelect','canSelectcomb')" />
			    			</td>
		    			</tr>
	    				<tr>
			    			<td class="honry-lable">默认标志:</td>
							<td>
				    			<input type="hidden"   id="isdefault" name="codeEmrtype.isdefault" value="${codeEmrtype.isdefault }"/>
				    			<input type="checkBox" id="isdefaultcomb"   onclick="javascript:checkBoxSelect('isdefault','isdefaultcomb')"/>
			    			</td>
		    			</tr>
					
					<tr>
						<td class="honry-lable">
							适用医院:
						</td>
						<td class="honry-info">
							<input class="easyui-validatebox" name="codeEmrtype.hospital"
								value="${codeEmrtype.hospital}"
								style="width: 290px" />
							&nbsp;
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							不适用医院:
						</td>
						<td class="honry-info">
							<input class="easyui-validatebox" name="codeEmrtype.nonhospital"
								value="${codeEmrtype.nonhospital}"
								style="width: 290px" />
							&nbsp;
						</td>
					</tr>
					
				</table>
				</form>
				<div style="text-align: center; padding: 5px">
				 <c:if test="${empty codeEmrtype.id}">
	  		      <a href="javascript:submit(1);" class="easyui-linkbutton" data-options="iconCls:'icon-save'">连续添加</a>
	  		  </c:if>
					<a href="javascript:submit(0);" class="easyui-linkbutton"
						data-options="iconCls:'icon-save'">保存</a>
					<a href="javascript:clear();" class="easyui-linkbutton"
						data-options="iconCls:'icon-clear'">清除</a>
				</div>
			
		</div>
	</div>
	<script>
//默认加载
var nodeid="${nodeid}";
$(function(){
		 $('#tanchu1').panel('close');// 默认状态 隐藏
});
//表单提交submit信息
  	function submit(flg){
	var order=$('#paixu').val();
  	$('#editForm').form('submit',{
  		url:'saveOrUpdateEmrtype.action?order='+order,
  		 onSubmit:function(){ 
		     return $(this).form('validate');  
		 },  
		success:function(data){
			if(flg==0){
				$.messager.alert('提示',"保存成功");
				parent.win.dialog('close');
			   //实现刷新
	          	$("#list").datagrid("reload");
	          	window.parent.frames.refresh();
		   }else if(flg==1){
				//清除editForm
				$('#editForm').form('reset');
		  	}
		 },
		error:function(date){
			$.messager.alert('提示',"保存失败");
		}
  	});
  	}



	//清除所填信息
	function clear() {
		$('#editForm').form('clear');
	}

	function KeyDown()  
				{  
				    if (event.keyCode == 32)  
				    { 
				    //event.keyCode==32
				        event.returnValue=false;  
				        event.cancel = true;  
				        //action参数为要读取的xml名字及是否多层  1位多层为单层
				        showWin("请选择","<c:url value='/TextOut.action'/>?xml="+"CodeEmrtype,1","50%","80%");
				    }  
				    
				} 
			//添加弹出框   将 win声明为全局变量其他窗口才能调用
			var win;	
			function showWin ( title,url, width, height) {
			//下面是调用方法的栗子  
			// showWin("请选择",'/tanchu.action',"50%","80%");
			   	var content = '<iframe id="myiframe" src="' + url + '" width="100%" height="100%" frameborder="0" scrolling="no"></iframe>';
			    var divContent = '<div id="treeDeparWin">';
			    win = $('<div id="treeDeparWin"><div/>').dialog({
			        content: content,
			        width: width,
			        height: height,
			        modal: true,
			        resizable:true,
			        shadow:true,
			        center:true,
			        title: title
			    });

			    win.dialog('open');
			}
			
		/**
		 * 复选框选中
		 * @param defalVal 默认值
		 * @param selVal 选中值
		 * 
		 * 
		 * @version 1.0
		 */
		function checkBoxSelect(id,ids){
		var checkbox = document.getElementById(ids);
		var inputvalue = document.getElementById(id);
			if(checkbox.checked=true){    //如果选中        
			    inputvalue.value = "1";  
			 }else{                   //如果没选中     
			    inputvalue.value = "0";     
			 }
	}
</script>
</body>
</html>
