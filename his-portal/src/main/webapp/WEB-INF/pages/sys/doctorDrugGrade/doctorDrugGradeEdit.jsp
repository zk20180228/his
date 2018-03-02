<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/metas.jsp"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme() + "://"+ request.getServerName() + ":" + request.getServerPort()+ path + "/";
%>
<body>
	<div class="easyui-panel" id = "panelEast" data-options="title:'',iconCls:'icon-form',fit:true" >
		<form id="editForms" method="post">
				<table class="honry-table" cellpadding="1" cellspacing="1" style="width:100%;border-left:0;border-top:0">
					<input id="sddsid" name="sdds.id" type="hidden" value="${sdds.id}"/>
					<input id="look" type="hidden" value="${paramType}"/>
	    			<tr>
		    			<td class="honry-lable" style="border-top:0">医生级别名称:</td>
		    			<td class="honry-info" style="border-top:0"><input id="postname" name="sdds.postname" value="${sdds.postname }" data-options="required:true" style="width:200px" editable="false"/></td>
		    		</tr>
					<tr>
						<td class="honry-lable">医生职级代码:</td>
		    			<td class="honry-info" ><input class="easyui-textbox" id="tpost1" name="sdds.tpost"  readOnly="true" style="width:200px" value="${sdds.tpost }" /></td>
	    			</tr>
	    			<tr>
						<td class="honry-lable">药品等级名称:</td>
		    			<td class="honry-info"><input class="easyui-combobox" id="graadename" name="sdds.graadename"  data-options="required:true" value="${sdds.graadename}"  style="width:200px" editable="false"/></td>
	    			</tr>
					<tr>
						<td class="honry-lable">药品等级代码:</td>
		    			<td class="honry-info" ><input class="easyui-textbox" id="druggraade1" name="sdds.druggraade"  style="width:200px"  readOnly="true" value="${sdds.druggraade  }" /></td>
	    			</tr>
	    			<tr>
		    			<td class="honry-lable">医院:</td>
		    			<td class="honry-info"> 
		    				<table border="0" cellspacing="0">
								    <tr>
									    <td id="buttonListId">不适用医院:<br>
										    <select multiple="multiple" id="selectAll" name="hosidNo" style="width:150px;height:100px;">
										        <c:if test="${null!=hospitalList && !hospitalList.isEmpty() }">
											        <c:forEach var="list" items="${hospitalList }">
											             <option value="${list.id }">${list.name }</option>
											        </c:forEach>
										        </c:if>
										    </select>
									    </td>
									    <td>
									    <input type="button" style="text-align: center;" value="&gt;&nbsp;" id="add_this"><br/>
									    <input type="button" style="text-align: center;" value="&lt;&nbsp;" id="remove_this"><br/>
									    <input type="button" style="text-align: center;" value="&gt;&gt; " id="add_all"><br/>
									    <input type="button" style="text-align: center;" value="&lt;&lt; " id="remove_all"><br/>
									    </td>
									    <td>适用的医院:<br>
										    <select multiple="multiple" id="selectBut" name="hosidIs" style="width:150px;height:100px;">
										         <c:forEach var="list" items="${useList }">
										             <c:if test="${list.id!=null }">
											             <option value="${list.id }">${list.name }</option>
											         </c:if>
											     </c:forEach>
										    </select>
									    </td>
								    </tr>
								</table>
						</td>					
					</tr>
					<tr style="display:none">					
						<td class="honry-lable">排序:</td>
		    			<td class="honry-info"><input class="easyui-textbox" id="order" name="sdds.order" value="${sdds.order }" style="width:200px"/></td>
	    			</tr>
	    			<tr style="width:98%;height:150px">
						<td class="honry-lable">说明:</td>
						<td colspan="3">
							<textarea rows="4" cols="50" id="description" 
								name="sdds.description"  data-options="multiline:true,prompt:'内容限制：150字以内'" maxlength="150" style="width:98%;height:100%">${sdds.description}</textarea>
						</td>
					</tr>
	    	</table>
	    	<div  style="text-align:center;padding:5px">
		    	<a id="bc" href="javascript:submit();" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a>
		    	<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="closeLayout()" >关闭</a>
		    </div>
		</form>			
	</div>
	
<script type="text/javascript">
	var refId = '${param.id}';
	$(function(){
		a=$('#look').val();
		if(a==2){
			 $('#postname').combobox({ readonly:true});
			 $('#graadename').combobox({ readonly:true});
			 $('#hospital').combobox({ readonly:true});
			 $('#nohospital').combobox({ readonly:true});
			 $('#description').attr("disabled",true);
			 $('#bc').hide();
			 $('#qc').hide();
		}
		
		//初始化医生职级名称代码
		$('#postname').combobox({    
		 	url:'<%=basePath%>queryTitleCombobox.action',   
		    valueField:'name',    
		    textField:'name',
		    multiple:false,
		    onSelect:function(record){
		    	$('#tpost1').empty();
		    	$('#tpost1').textbox('setValue',record.encode);
		    }
		});
		
		/**
    	* 绑定医生级别名称选择窗口
    	* @author  zhuxiaolu
    	* @date 2016-03-22 14:30   
    	* @version 1.0
    	*/
		bindEnterEvent('postname',popWinToCode,'easyui');//绑定回车事件
		//初始化药品职级名称代码
		$('#graadename').combobox({    
		 	url:'<%=basePath%>queryDruggrCombobox.action',   
		    valueField:'name',    
		    textField:'name',
		    multiple:false,
		    onSelect:function(record){
		    	$('#druggraade1').empty();
		    	$('#druggraade1').textbox('setValue',record.encode);
		    }
		});
		
		//初始化使用医院列表
		$('#hospital').combobox({    
			url:"<%=basePath%>baseinfo/hospital/findAllHospital.action", 
		  	valueField:'id',
	     	textField:'name'
		});
		//初始化不适用医院列表
		$('#nohospital').combobox({    
			url:"<%=basePath%>baseinfo/hospital/findAllHospital.action", 
		  	valueField:'id',
	     	textField:'name'
		});
		
	});

	//清除所填信息
	function clear(){
		$('#editForms').form('clear');
	}
	//关闭
	function closeLayout(){
		$('#divLayout').layout('remove','east');
	}
	
	//提交新增信息
	function submit(){
		$('#selectBut option').prop("selected",true);
		$('#selectAll option').prop("selected",true);
	  	$('#editForms').form('submit',{
	  		url: '<%=basePath%>addContrast.action?id='+$('#sddsid').val()+'&refId='+refId,
	  		 onSubmit:function(){ 
			     return $(this).form('validate');  
			 },  
			success:function(){
					$.messager.alert('提示','保存成功');
		          	$("#list").datagrid("reload");
			   		$('#divLayout').layout('remove','east');
			 },
			error:function(){
				$.messager.alert('提示','保存失败');
			}
	  	});
	 }
	//适用医院
	 //移到右边
    $('#add_this').click(function() {
    //获取选中的选项，删除并追加给对方
        $('#selectAll option:selected').appendTo('#selectBut');
    });
    //移到左边
    $('#remove_this').click(function() {
        $('#selectBut option:selected').appendTo('#selectAll');
    });
    //全部移到右边
    $('#add_all').click(function() {
        //获取全部的选项,删除并追加给对方
        if(!$('#selectAll').prop("disabled")){
        	 $('#selectAll option').appendTo('#selectBut');
        }
    });
    //全部移到左边
    $('#remove_all').click(function() {
        $('#selectBut option').each(function(){
        	if(!$(this).prop("disabled")){
        		$(this).appendTo('#selectAll');
        	}
        });
    });
    //双击选项
    $('#selectAll').dblclick(function(){ //绑定双击事件
        //获取全部的选项,删除并追加给对方
        $("option:selected",this).appendTo('#selectBut'); //追加给对方
    });
    //双击选项
    $('#selectBut').dblclick(function(){
       $("option:selected",this).appendTo('#selectAll');
    });	

    /**
    	* 回车弹出医生级别名称选择窗口
    	* @author  zhuxiaolu
    	* @param textId 页面上commbox的的id
    	* @date 2016-03-22 14:30   
    	* @version 1.0
    	*/

    	function popWinToCode(){
    		popWinEmpTitleBackFn = function(node){
    			$('#tpost').textbox('setValue',node.encode);
				$('#postname').combobox('setValue',node.name); 
			};
    		var tempWinPath = "<%=basePath%>popWin/popWinTitle/toTitlePopWin.action?textId=postname";
    		window.open (tempWinPath,'newwindow',' left=400,top=200,width='+ (screen.availWidth -900) +',height='+ (screen.availHeight-370) 
    	+',scrollbars,resizable=yes,toolbar=yes')
    	}


</script>
</body>