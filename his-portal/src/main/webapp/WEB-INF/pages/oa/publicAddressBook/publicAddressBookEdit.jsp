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
	<div class="easyui-panel" id="panelEast" data-options="iconCls:'icon-form',fit:true" style="width: 100%;border:0">
		<div>
    		<form id="editForm" method="post">
				<input type="hidden" id="id" name="publicAddressBook.id" value="${publicAddressBook.id }">
				<input type="hidden" id="nodeType" name="publicAddressBook.nodeType" value="${publicAddressBook.nodeType }">
				<input type="hidden" id="order" name="publicAddressBook.order" value="${publicAddressBook.order }">
				<input type="hidden" id="path" name="publicAddressBook.path" value="${publicAddressBook.path }">
				<input type="hidden" id="superPath" name="publicAddressBook.superPath" value="${publicAddressBook.superPath }">
				<input type="hidden" id="nodeLevel" name="publicAddressBook.nodeLevel" value="${publicAddressBook.nodeLevel }">
				<table class="honry-table" cellpadding="0" cellspacing="0" border="0" style="margin:10px auto 0;">
					<tr id="tr1">
						<td class="honry-lable">父级名称：</td>
		    			<td class="honry-info"><input id="parent" name="publicAddressBook.parentCode" style="width: 300" value="${publicAddressBook.parentCode }" />
		    			</td>
	    			</tr>
		    	</table>
			    <div style="text-align:center;padding:5px">
			    	<a id="save" href="javascript:submitTreeForm();" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a>
			    	<a href="javascript:clearForm();" class="easyui-linkbutton" data-options="iconCls:'icon-clear'">清除</a>
			    	<a href="javascript:closeLayout();" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'">关闭</a>
			    </div>
	    	</form>
	    </div>
	</div>    
	<div id="menuphoto"></div>
	<script type="text/javascript">
		function createDiv3(){
			$('#tr3').remove();
			$("#tr1").after("<tr id='tr3'><td class='honry-lable'>院区：</td><td class='honry-info'><input id='area' class='easyui-textbox'  name='publicAddressBook.name' style='width: 300' value='${publicAddressBook.name }' data-options='required:true' missingMessage='请输入院区名称'/></td></tr>");
			$.parser.parse($("#tr3").parent());
		}
		function createDiv4(){
			$('#tr4').remove();
			$("#tr1").after("<tr id='tr4'><td class='honry-lable'>楼号：</td><td class='honry-info'><input id='buildNo' class='easyui-textbox'  name='publicAddressBook.name' style='width: 300' value='${publicAddressBook.name }' data-options='required:true' missingMessage='请输入楼号'/></td></tr>");
			$.parser.parse($("#tr4").parent());
		}
		function createDiv5(){
			$('#tr5').remove();
			$("#tr1").after("<tr id='tr5'><td class='honry-lable'>楼层：</td><td class='honry-info'><input id='floorNo' class='easyui-textbox'  name='publicAddressBook.name' style='width: 300' value='${publicAddressBook.name }' data-options='required:true' missingMessage='请输入楼层'/></td></tr>");
			$.parser.parse($("#tr5").parent()); 
		}
		function createDiv6(){
			$('#tr6').remove();
			$("#tr1").after("<tr id='tr6'><td class='honry-lable'>类别名称：</td><td class='honry-info'><input id='deptType' class='easyui-combobox'  name='publicAddressBook.name' style='width: 300' value='${publicAddressBook.name }' data-options='required:true' missingMessage='请输入类别名称'/></td></tr>");
			$.parser.parse($("#tr6").parent());
		}
		function createDiv7(){
			$('#tr7').remove();
			$("#tr1").after("<tr id='tr7'><td class='honry-lable'>科室名称：</td><td class='honry-info'><input id='execDept' class='easyui-combobox'  name='publicAddressBook.name' style='width: 300' value='${publicAddressBook.name }' data-options='required:true' missingMessage='请输入科室'/></td></tr>");
			$.parser.parse($("#tr7").parent());
		}
		function createDiv8(){
			$('#tr8').remove();
			$("#tr1").after("<tr id='tr8'><td class='honry-lable'>工作站名称：</td><td class='honry-info'><input id='work' class='easyui-textbox'  name='publicAddressBook.name' style='width: 300' value='${publicAddressBook.name }' data-options='required:true' missingMessage='请输入工作站'/></td></tr>");
			$.parser.parse($("#tr8").parent()); 
		}
		function createDiv9(){
			$('#tr9').remove();
			$("#tr8").after("<tr id='tr9'><td class='honry-lable'>内线：</td><td class='honry-info'><input class='easyui-textbox' id='phone' name='publicAddressBook.phone' style='width: 300' value='${publicAddressBook.phone }'/></td></tr>");
			$.parser.parse($("#tr9").parent()); 
		}
		function createDiv10(){
			$('#tr10').remove();
			$("#tr9").after("<tr id='tr10'><td class='honry-lable'>移动电话：</td><td class='honry-info'><input class='easyui-textbox' id='minPhone' name='publicAddressBook.minPhone'  style='width: 300' value='${publicAddressBook.minPhone }'/></td></tr>");
			$.parser.parse($("#tr10").parent()); 
		}
		function createDiv11(){
			$('#tr11').remove();
			$("#tr10").after("<tr id='tr11'><td class='honry-lable'>小号：</td><td class='honry-info'><input class='easyui-textbox' id='officePhone' name='publicAddressBook.officePhone' style='width: 300' value='${publicAddressBook.officePhone }'/></td></tr>");
			$.parser.parse($("#tr11").parent()); 
		}
		function createDiv12(){
			$('#tr12').remove();
			$("#tr11").after("<tr id='tr12'><td class='honry-lable'>停用标志：</td><td><input type='hidden' id='stopFlgHidden' name='publicAddressBook.stop_flg' value='${publicAddressBook.stop_flg }'/><input type='checkBox' id='stopFlg' onclick='javascript:onclickBox(\"stopFlg\")'/></td></tr>");
			$.parser.parse($("#tr12").parent()); 
		}
		function createDiv13(){
			$('#tr13').remove();
			$("#tr12").after("<tr id='tr13'><td class='honry-lable'>状态：</td><td><input id='status' value='${publicAddressBook.status}' name='publicAddressBook.status' style='width: 300' class='easyui-combobox' /></td></tr>");
			$.parser.parse($("#tr13").parent()); 
			$('#status').combobox({
 				valueField:'id',    
 			    textField:'value',
 			   editable:false,
 			    data: [{
 			    	id: 0,
 			    	value: '普通'
 				},{
 					id: 1,
 					value: '常用'
 				}]
 			});
		}
		if('${nodeType}'=='00'){
			 createDiv3();
		 }else{ 
			 $('#tr3').remove();
		 }
		if('${nodeType}'=='11'){
			createDiv4();
		 }else{
			 $('#tr4').remove();
		 }
		if('${nodeType}'=='22'){
			createDiv5();
		 }else{
			 $('#tr5').remove();
		 }
		if('${nodeType}'=='33'){
			createDiv6();
		 }else{
			 $('#tr6').remove();
		 }
		if('${nodeType}'=='44'){
			createDiv7();
		 }else{
			 $('#tr7').remove();
		 }
		if('${nodeType}'=='55'){
			createDiv8();
			createDiv9();
			createDiv10();
			createDiv11();
			createDiv12();
			createDiv13();
		 }else{
			 $('#tr8').remove();
			 $('#tr9').remove();
			 $('#tr10').remove();
			 $('#tr11').remove();
			 $('#tr12').remove();
			 $('#tr13').remove();
		 }
		//加载页面
	  $("#parent").combotree({
		     onChange:function(node){//父级名称改变触犯事件
				 var id=$("#parent").combotree("getValue");
		    	 $.ajax({ 
		  			url:"<%=basePath%>oa/publicAddressBook/queryPublicAddressBook.action?id="+id,
				   	success:function(data){
				    	 if(data.nodeType==undefined){
				    		 createDiv3();
				    	 }else{
				    		 $('#tr3').remove();
				    	 }
				    	 if(data.nodeType=='00'){
				    		 createDiv4();
				    	 }else{
				    		 $('#tr4').remove();
				    	 }
				    	 if(data.nodeType=='11'){
				    		 createDiv5();
				    	 }else{
				    		 $('#tr5').remove();
				    	 }
				    	 if(data.nodeType=='22'){
				    		 createDiv6();
				    		 $('#deptType').combobox({
				 				url : "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action'/>?type=depttype",
				 				valueField : 'encode',
				 				textField : 'name',
				 				multiple : false,
				 			});
				    	 }else{
				    		 $('#tr6').remove();
				    	 }
				    	 if(data.nodeType=='33'){
				    		 createDiv7();
				    		 $('#execDept').combobox({
				 				url : "<c:url value='/oa/publicAddressBook/queryDepartments.action'/>?deptType="+encodeURI(encodeURI(data.name)),
				 				valueField : 'deptCode',
				 				textField : 'deptName',
				 				multiple : false
				 			});
				    	 }else{
				    		 $('#tr7').remove();
				    	 }
				    	 if(data.nodeType=='44'){
				    		createDiv8();
				 			createDiv9();
				 			createDiv10();
				 			createDiv11();
				 			createDiv12();
				 			createDiv13();
				 			cheHid();
				    	 }else{
				    		 $('#tr8').remove();
							 $('#tr9').remove();
							 $('#tr10').remove();
							 $('#tr11').remove();
							 $('#tr12').remove();
							 $('#tr13').remove();
				    	 }
				   	}
			   	});
	     	}    
	    });
		$(function(){
			 //加载父级名称树 
			 if('${publicAddressBook.name}'==''){
				 $('#parent').combotree({ 
					url: "<c:url value='/oa/publicAddressBook/publicBookTree.action'/>?type=super",   
					required: true, 
					missingMessage:'请选择所属栏目',
					width:'300',
				    editable:false
				}); 
			 }else{
				 $('#parent').combotree({ 
						url: "<c:url value='/oa/publicAddressBook/publicBookTree.action'/>?type="+'${publicAddressBook.nodeType}',   
						required: true, 
						missingMessage:'请选择所属栏目',
						width:'300',
					    editable:false,
					    //只能选子选项，父级不被选中
					    onBeforeSelect: function(node) {  
				            if (!$(this).tree('isLeaf', node.target)) {  
				                return false;  
				            }  
				        },  
				        onClick: function(node) {  
				            if (!$(this).tree('isLeaf', node.target)) {  
				                $('#parent').combo('showPanel');  
				            }  
				        } 
					}); 
			 }
			 //科室类别
			$('#deptType').combobox({
				url : "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action'/>?type=depttype",
				valueField : 'encode',
				textField : 'name',
				multiple : false,
			});
			 //科室
			$('#execDept').combobox({
				url : "<c:url value='/oa/publicAddressBook/queryDepartments.action'/>?deptType="+encodeURI(encodeURI('${deptType}')),
				valueField : 'deptCode',
				textField : 'deptName',
				multiple : false
			});
			cheHid();
		});
		
		//表单提交
		function submitTreeForm(){
			$('#editForm').form('submit',{  
				url:"<c:url value='/oa/publicAddressBook/savePublicAddressBook.action'/>",
	        	onSubmit:function(){
					if($("#area").length==1 && $("#area").textbox('getValue')==''){
						$.messager.show({  
					         title:'提示信息' ,   
					         msg:'请填写院区!'  
					    }); 
						return false ;
					}
					if($("#buildNo").length==1 && $("#buildNo").textbox('getValue')==''){
						$.messager.show({  
					         title:'提示信息' ,   
					         msg:'请填写楼号!'  
					    }); 
						return false ;
					}
					if($("#floorNo").length==1 && $("#floorNo").textbox('getValue')==''){
						$.messager.show({  
					         title:'提示信息' ,   
					         msg:'请填写楼层!'  
					    }); 
						return false ;
					}
					if($("#work").length==1 && $("#work").textbox('getValue')==''){
						$.messager.show({  
					         title:'提示信息' ,   
					         msg:'请填写工作站名称!'  
					    }); 
						return false ;
					}
					if($("#execDept").length==1 && $("#execDept").combobox('getValue')==''){
						$.messager.show({  
					         title:'提示信息' ,   
					         msg:'请填写科室名称!'  
					    }); 
						return false ;
					}
					if($("#deptType").length==1 && $("#deptType").combobox('getValue')==''){
						$.messager.show({  
					         title:'提示信息' ,   
					         msg:'请填写类别名称!'  
					    }); 
						return false ;
					}
					if($("#minPhone").length!=0 && $("#minPhone").textbox('getValue')!=''){
						 var rex=/^1[3-8]+\d{9}$/;
						 if(!rex.test($("#minPhone").textbox("getValue"))) {
							 $.messager.show({  
						         title:'提示信息' ,   
						         msg:'请输入正确的移动电话号码格式,格式如13800571506'  
						    });
						    return false; 
						 } 
					}
					$.messager.progress({text:'保存中，请稍后...',modal:true});  
	        	},  
		        success:function(result){ 
		        	$.messager.progress('close');
		        	if(result == 'success'){
		        		closeLayout();
		        		$("#tDt").tree("reload");
		        		searchFrom();
		        		comboboxAll();
		        		$.messager.alert('提示','保存成功！');
		        	}else{
						$.messager.alert('提示','保存失败！');	
		        	}
		        }						         
	   		}); 
			 $('.validatebox-tip').remove();
		}
		//清除页面填写信息
		function clearForm(){
			$('#editForm').form('reset');
			cheHid();
		}
		
		//关闭编辑窗口
		function closeLayout(){
			$('#dialog').dialog('close');
		}
		
		//复选框赋值
		function onclickBox(id){
			if($('#'+id).is(':checked')){
				$('#'+id+'Hidden').val(1);
			}else{
				$('#'+id+'Hidden').val(0);
			}
		}
		//复选框初始化
		function cheHid(){
			if($('#stopFlgHidden').val()==1){
				$('#stopFlg').prop("checked", true); 
			}
		}
		//自定义验证手机号  
       /* $.extend($.fn.validatebox.defaults.rules, {  
        phoneRex: {  
            validator: function(value){  
            var rex=/^1[3-8]+\d{9}$/;  
            //var rex=/^(([0\+]\d{2,3}-)?(0\d{2,3})-)(\d{7,8})(-(\d{3,}))?$/;  
            //区号：前面一个0，后面跟2-3位数字 ： 0\d{2,3}  
            //电话号码：7-8位数字： \d{7,8  
            //分机号：一般都是3位数字： \d{3,}  
             //这样连接起来就是验证电话的正则表达式了：/^((0\d{2,3})-)(\d{7,8})(-(\d{3,}))?$/          
            //var rex2=/^((0\d{2,3})-)(\d{7,8})(-(\d{3,}))?$/;  
            if(rex.test(value)){  
              return true;  
            }else{  
               return false;  
            }  
            },  
            message: '请输入正确的电话号码格式,格式如13800571506'  
        } ,  
        telNum:{  
            //验证座机号  
            validator: function(value){  
                var rex2=/^((0\d{2,3})-)(\d{7,8})(-(\d{3,}))?$/;  
                if(rex2.test(value)){  
                    return true;  
                }else{  
                    return false;  
                }  
                  
            },  
            message: '请输入正确的座机号码格式,格式如010-88888888'   
            }   
        });*/
	</script>
	<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
	</body>
</html>