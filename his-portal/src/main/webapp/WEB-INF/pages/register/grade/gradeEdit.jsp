<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/metas.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<body>
	<div class="easyui-panel" id = "panelEast" data-options="title:'',iconCls:'icon-form',fit:true,border:false" style="width:100%">
		<input type="hidden" id="id" name="id" value="${registerGrade.id }">
			<input type="hidden" id="names" value="${registerGrade.encode }">
		<form id="editForms" method="post">
			<input type="hidden" id="title" name="encode" value="${registerGrade.encode}">
				<table class="honry-table" cellpadding="1" cellspacing="1" border="1" style="width:100%;border-left:0">
					<tr>
						<td class="honry-lable" style="border-left:0">级别代码:</td>
		    			<td class="honry-info"><input class="easyui-textbox" id="enCode" name="code" value="${registerGrade.code }" data-options="required:true,prompt:'请先选择级别名称'" style="width:200px" readonly="readonly"/></td>
	    			</tr>
	    			<tr>
		    			<td class="honry-lable" style="border-left:0">级别名称:</td>
		    			<input type="hidden" id="gradeName" name="name">
		    			<td class="honry-info"><input id="nameCode" value="${registerGrade.name }" data-options="required:true,editable:false" value="${registerGrade.specialistno}" style="width:200px"/>
		    			<a href="javascript:delSelectedData('nameCode');"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a></td>
		    		</tr>
					<tr>
						<td class="honry-lable" style="border-left:0">是否是专家号:</td>
		    			<td class="honry-info"><input class="easyui-combobox"  name="expertno" data-options=" valueField: 'value',textField: 'label',data: [{label: '是',value: '1'},{label: '否',value: '0'}],required:true,editable:false" id="expertno" name="expertno" value="${registerGrade.expertno }"  style="width:200px" /></td>
	    			</tr>
	    			<tr>
						<td class="honry-lable" style="border-left:0">是否是专科号:</td>
		    			<td class="honry-info"><input class="easyui-combobox" name="specialistno"  data-options=" valueField: 'value',textField: 'label',data: [{label: '是',value: '1'},{label: '否',value: '0'}],required:true,editable:false" value="${registerGrade.specialistno}"  style="width:200px" /></td>
	    			</tr>
	    			<tr>
		    			<td class="honry-lable" style="border-left:0">是否是特诊号:</td>
		    			<td class="honry-info"><input class="easyui-combobox"  name="specialdiagnosisno" data-options=" valueField: 'value',textField: 'label',data: [{label: '是',value: '1'},{label: '否',value: '0'}],required:true,editable:false" value="${registerGrade.specialdiagnosisno }" style="width:200px" /></td>					
					</tr>
					<tr>
						<td class="honry-lable" style="border-left:0">默认标示:</td>
		    			<td class="honry-info"><input class="easyui-combobox"  name="isdefault" value="${registerGrade.isdefault }"  data-options=" valueField: 'value',textField: 'label',data: [{label: '是',value: '1'},{label: '否',value: '0'}],required:true,editable:false" style="width:200px"/></td>
	    			</tr>
	    			<tr>
						<td class="honry-lable" style="border-left:0">自定义码:</td>
						<td class="honry-info"><input class="easyui-textbox" id="codeInputcode" name="codeInputcode" value="${registerGrade.codeInputcode }" style="width:200px" /></td>
					</tr>
					<tr>					
						<td class="honry-lable" style="border-left:0">说明:</td>
		    			<td class="honry-info"><input class="easyui-textbox" id="description" name="description" value="${registerGrade.description }" style="width:200px"/></td>
	    			</tr>
	    	</table>
	    	<div style="text-align:center;padding:5px">
		    	<a href="javascript:submit(0);" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a>
		    	<a href="javascript:clear();" class="easyui-linkbutton" data-options="iconCls:'icon-clear'">清除</a>
		    	<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="closeLayout()">关闭</a>
		    </div>
		</form>			
	</div>
	
<script type="text/javascript">
	$('#nameCode').combobox({    
	 	url: "<c:url value='/outpatient/grade/titleCombobox.action'/>",   
	    valueField:'encode',    
	    textField:'name',
	    multiple:false,
	    onSelect:function(record){
	    	$.ajax({
	    		url: "<c:url value='/outpatient/grade/findGradeSize.action'/>?id="+record.encode,
				type:'post',
				success: function(idCardObj) {
					if(idCardObj.resMsg=="Success"){
						$('#enCode').textbox('setValue',record.encode);
	    				$('#title').val(record.mark);
					}else if(idCardObj.resMsg=="Failure"){
						var feel = $('#names').val();
						if(feel!=""){
							if(record.id==feel){
								$('#enCode').textbox('setValue',record.encode);
			    				$('#title').val(record.mark);
							}
							else{
								var list=idCardObj.list;
								for(var i=0;i<list.length;i++){
									var name=$('#nameCode').combobox('getText');
									$.messager.alert('提示',"职称为"+name+"对应挂号级别为"+list[i].name+"，请重新选择");
								}
								$('#nameCode').combobox('setValue',"");
								$('#gradeName').val("");
								$('#enCode').textbox('setValue',"");
			    				$('#title').val("");
							}
						}else{
							var list=idCardObj.list;
							for(var i=0;i<list.length;i++){
								var name=$('#nameCode').combobox('getText');
								$.messager.alert('提示',"职称为"+name+"对应挂号级别为"+list[i].name+"，请重新选择");
							}
							
							$('#nameCode').combobox('setValue',"");
							$('#gradeName').val("");
							$('#enCode').textbox('setValue',"");
			    			$('#title').val("");
						}
					}
				}
			});	
	    	
	    }
	});
		
	//清除所填信息
	function clear(){
		$('#editForms').form('clear');
	}
	function closeLayout(){
		$('#divLayout').layout('remove','east');
	}
	/**
	 * 格式化复选框
	 * @author  
	 * @date 2015-5-26 9:25       
	 * @version 1.0
	 */
	function formatCheckBox(val,row){
		if (val == 1){
			return '是';
		} else {
			return '否';
		}
	}
	function submit(flg){
		var name = $('#nameCode').combobox('getText');
		$('#gradeName').val(name);
		var id="${registerGrade.id }";
	  	$('#editForms').form('submit',{
	  		url: "<c:url value='/outpatient/grade/saveOrUpdagrade.action?id='/>"+id,
	  		 onSubmit:function(){ 
			     return $(this).form('validate');  
			     $.messager.progress({text:'保存中，请稍后...',modal:true});
			 },  
			success:function(data){
				$.messager.progress('close');
				if(flg==0){
					$.messager.alert('提示',"保存成功");
					setTimeout(function(){
						  $(".messager-body").window('close');  
						},3500);
			   		$('#divLayout').layout('remove','east');
				   //实现刷新
		          	$("#list").datagrid("reload");
			   }else if(flg==1){
					//清除editForm
					$('#editForms').form('reset');
			  	}
			 },
			error:function(date){
				$.messager.progress('close');
				$.messager.alert('提示',"保存失败");
			}
	  	});
	 }
	$(function(){
		/**
		 * 回车弹出级别名称选择窗口
		 * @author  wanxing
		 * @date 2016-03-22  15:34
		 * @version 1.0
		 */
		 bindEnterEvent('nameCode',popWinToNameCode,'easyui');//绑定回车事件
		 function popWinToNameCode(){
				var tempWinPath = "<%=basePath%>popWin/popWinCode/toCodePopWinDB.action?type=title&textId=nameCode";
				window.open (tempWinPath,'newwindow',' left=400,top=200,width='+ (screen.availWidth-900) +',height='+ (screen.availHeight-450) +',scrollbars,resizable=yes,toolbar=no')
		}
	});
	
</script>
</body>