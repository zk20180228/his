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
	<div class="easyui-panel"id="panelEast" data-options="title:'职称',iconCls:'icon-form',border:false">
		<div data-options="region:'center',border:false" style="padding: 10px">
				
			<form id="editForm" method="post">
			<input type="hidden" id="id" name="id" value="${codeTitle.id }">
				<table class="honry-table" data-options="border:false" cellpadding="0" cellspacing="0" style="width:100%"
					border="0">
					<tr>
						<td class="honry-lable">
							职称名称:
						</td>
						<td class="honry-info">
							<input class="easyui-validatebox" name="codeTitle.name"
								value="${codeTitle.name}" data-options="required:true"
								style="width: 290px" />
							&nbsp;
						</td>
					</tr>
					<input type="hidden" name="codeTitle.parent"  id="parentId" value="${codeTitle.parent}">
					<input type="hidden" name="codeTitle.levell"  id="levell" value="${codeTitle.levell}" >
					<tr>
						<td class="honry-lable">
							代码:
						</td>
						<td class="honry-info">
							<input class="easyui-validatebox" name="codeTitle.encode"
								value="${codeTitle.encode}" data-options="required:true"
								style="width: 290px" />
							&nbsp;
						</td>
					</tr>
					<c:if test="${not empty codeTitle.id }">

						<tr>
							<td class="honry-lable">
								拼音码:
							</td>
							<td class="honry-info">
								${codeTitle.pinyin }
								&nbsp;
							</td>
						</tr>
						<tr>
							<td class="honry-lable">
								五笔码:
							</td>
							<td class="honry-info">
								${codeTitle.wb}
								&nbsp;
							</td>
							</tr>
				
					</c:if>
					<tr>
	  				<td class="honry-lable">排序:</td>
	  				<td class="honry-info">
	  				<input class="easyui-validatebox" disabled="disabled" type="text" id="paixu" name="codeTitle.order" value="${codeTitle.order }" data-options="required:true" style="width: 290px" />
	  				</td>
	  			</tr>
					<tr>
						<td class="honry-lable">
							自定义码:
						</td>
						
						<td class="honry-info">
							<input class="easyui-validatebox" name="codeTitle.inputcode"
								value="${codeTitle.inputcode }" data-options="required:true"
								style="width: 290px" />
							&nbsp;
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							说明:
						</td>
						<td class="honry-info">
							<input class="easyui-validatebox" name="codeTitle.description"
								value="${codeTitle.description}" data-options="required:true"
								style="width: 290px" />
							&nbsp;
						</td>
					</tr>
					
					<tr>					
							<td class="honry-lable">可选标志:</td>
			    			<td>
				    			<input type="hidden"  id="canSelect" name="codeTitle.canSelect" value="${codeTitle.canSelect }"/>
				    			<input type="checkBox" id="canSelectcomb"  onclick="javascript:checkBoxSelect('canSelect','canSelectcomb')" />
			    			</td>
		    			</tr>
	    				<tr>
			    			<td class="honry-lable">默认标志:</td>
							<td>
				    			<input type="hidden"   id="isdefault" name="codeTitle.isdefault" value="${codeTitle.isdefault }"/>
				    			<input type="checkBox" id="isdefaultcomb"   onclick="javascript:checkBoxSelect('isdefault','isdefaultcomb')"/>
			    			</td>
		    			</tr>
					
					<tr>
						<td class="honry-lable">
							适用医院:
						</td>
						<td class="honry-info">
							<input class="easyui-validatebox" name="codeTitle.hospital"
								value="${codeTitle.hospital}" 
								style="width: 290px" />
							&nbsp;
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							不适用医院:
						</td>
						<td class="honry-info">
							<input class="easyui-validatebox" name="codeTitle.nonhospital"
								value="${codeTitle.nonhospital}" 
								style="width: 290px" />
							&nbsp;
						</td>
					</tr>
					
				</table>
				</form>
				<div style="text-align: center; padding: 5px">
				 <c:if test="${empty codeTitle.id}">
	  		      <a href="javascript:submit(1);" class="easyui-linkbutton" data-options="iconCls:'icon-save'">连续添加</a>
	  		  </c:if>
					<a href="javascript:submit(0);" class="easyui-linkbutton"
						data-options="iconCls:'icon-save'">保存</a>
					<a href="javascript:clear();" class="easyui-linkbutton"
						data-options="iconCls:'icon-clear'">清除</a>
					<a href="javascript:closeLayout();" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'">关闭</a>
				</div>
			
		</div>
	</div>
	<script>
//默认加载
$(function(){
		 $('#tanchu1').panel('close');// 默认状态 隐藏
});
//表单提交submit信息
  	function submit(flg){
  		var order=$('#paixu').val();
  	$('#editForm').form('submit',{
  		url:'saveOrUpdateTitle.action?order='+order,
  		 onSubmit:function(){ 
		     return $(this).form('validate');  
		 },  
		success:function(data){
			if(flg==0){
				$.messager.alert('提示',"保存成功");
		   		$('#divLayout').layout('remove','east');
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
	function closeLayout() {
		$('#divLayout').layout('remove', 'east');
	}

	function KeyDown()  
				{  
				    if (event.keyCode == 32)  
				    { 
				    //event.keyCode==32
				        event.returnValue=false;  
				        event.cancel = true;  
				        //action参数为要读取的xml名字及是否多层  1位多层为单层
				        showWin("请选择","<c:url value='/TextOut.action'/>?xml="+"CodeTitle,1","50%","80%");
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
		 * @author  liujinliang
		 * @date 2015-5-25 10:53
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