<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp" %>

<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<title>临床正台</title>
	<%@ include file="/common/metas.jsp" %>
	<style type="text/css">
		.panel-header{
			border-top:0;
		}
	</style>
</head>
<body style="margin: 0px;padding: 0px;">  
	<div class="easyui-layout" data-options="fit:true">  
		<div id="p" data-options="region:'west',iconCls:'icon-form'" style="width: 70%;height:100%;border-top:0;" >
			<input type="hidden" id="treeId">
			<input type="hidden" id="rtext">
	    		<form id="editForm" method="post" >
					<table class="honry-table" cellpadding="0" cellspacing="0" border="0" style="padding:5px;border-top:0;width: 100%;">
						<tr>
							<td class="honry-lable" style="border-top:0">手术科室：</td>
							<td style="border-top:0"><input id="opsDpcd" name="arrangeconsole.opsDpcd" class="easyui-combobox" data-options="required:true" missingMessage="请选择手术科室">
							<a href="javascript:delSelectedDatass('opsDpcd');"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a>
							</td>
		    			</tr>
						<tr>
							<td class="honry-lable">临床科室：</td>
							<td><input id="opsCode" class="easyui-combobox" disabled="disabled" name="arrangeconsole.deptCode" data-options="required:true" missingMessage="请选择临床科室（选择临床科室前请先选择手术科室）">
							<a href="javascript:delSelectedData('opsCode');"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a>
							</td>
		    				
		    			</tr>
		    			<tr>
							<td class="honry-lable">正台数量：</td>
							<td>
			    				<input class="easyui-numberbox" id="limitedqty" name="arrangeconsole.limitedqty" data-options="required:true,max:999"  missingMessage="请输入正台数量,最大值为999" > 
			    			</td>
		    			</tr>
						<tr>
							<td class="honry-lable">星期一：</td>
			    			<td><input class="easyui-numberbox" id="one"  value="0"  missingMessage="请输入星期一所用台数"/></td>
		    			</tr>
		    			<tr>
							<td class="honry-lable">星期二：</td>
			    			<td><input class="easyui-numberbox" id="two"  value="0"  missingMessage="请输入星期二所用台数"/></td>
		    			</tr>
		    			<tr>
							<td class="honry-lable">星期三：</td>
			    			<td><input class="easyui-numberbox" id="three"  value="0"  missingMessage="请输入星期三所用台数"/></td>
		    			</tr>
		    			<tr>
							<td class="honry-lable">星期四：</td>
			    			<td><input class="easyui-numberbox" id="four"  value="0" missingMessage="请输入星期四所用台数"/></td>
		    			</tr>
		    			<tr>
							<td class="honry-lable">星期五：</td>
			    			<td><input class="easyui-numberbox" id="five"  value="0" missingMessage="请输入星期五所用台数"/></td>
		    			</tr>
		    			<tr>
							<td class="honry-lable">星期六：</td>
			    			<td><input class="easyui-numberbox" id="six" value="0"   missingMessage="请输入星期六所用台数"/></td>
		    			</tr>
		    			<tr>
							<td class="honry-lable">星期日：</td>
			    			<td><input class="easyui-numberbox" id="seven" value="0" missingMessage="请输入星期日所用台数"/></td>
		    			</tr>
		    			<tr>
							<td style="text-align: center;padding-top: 10px;" colspan="2"> 
								<shiro:hasPermission name="${menuAlias }:function:add">
		    					<a href="javascript:submit();" class="easyui-linkbutton" data-options="iconCls:'icon-save'">分配</a>
		  						</shiro:hasPermission>
		  						<shiro:hasPermission name="${menuAlias }:function:cancel">
		    					&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:clear();" class="easyui-linkbutton" data-options="iconCls:'icon-clear'">取消分配</a>
								</shiro:hasPermission>
							</td>
		    			</tr>
		    		</table>
	    		</form>
		</div>
		<div data-options="region:'east',title:'星期',split:true" style="width:30%;border-top:0">
			<ul id="tDt"></ul>  
		</div> 
	</div>
	<script type="text/javascript">
	var opsDpcd ;//手术室
	var opsCode ;//临床科室
		//页面加载
		$(function(){
			//科室分类下拉框
			$('#opsDpcd').combobox({    
			    url:"<%=basePath%>operation/arrangeconsole/bbdeptName.action",    
			    valueField:'deptCode',    
			    textField:'deptName',
			    onChange:function(newValue,oldValue){
			    	if(newValue!=oldValue){
			    		if(newValue){
			    			var id="1,2,3,4,5,6,7";
			    			$('#opsCode').combobox({
			    				required:true,
			    				disabled:false
			    			});
			    			var opsCode=$('#opsCode').combobox("getValue");
			    			$("#tDt").tree("options").url="<%=basePath%>operation/arrangeconsole/treeArrangeconsole.action?odpcd="+newValue+"&id="+id+"&ocode="+opsCode;
			    		    $("#tDt").tree("reload");
			    		}else{
			    			$('#editForm').form('reset');
			    			$("#tDt").tree("options").url='<%=basePath%>operation/arrangeconsole/treeArrangeconsole.action';
			    		    $("#tDt").tree("reload");
			    		    return ;
			    		}
			    	}
			    	query();
			    },
			    filter:function(q,row){
					var keys = new Array();
					keys[keys.length] = 'deptCode';
					keys[keys.length] = 'deptName';
					keys[keys.length] = 'deptPinyin';
					keys[keys.length] = 'deptWb';
					keys[keys.length] = 'deptInputCode';
					return filterLocalCombobox(q, row, keys);
				}
			});
			bindEnterEvent('opsDpcd',popWinToDpcd,'easyui');//绑定回车事件
			//科室下拉框
			$('#opsCode').combobox({    
			    url:'<%=basePath%>operation/arrangeconsole/deptNameAll.action',    
			    valueField:'deptCode',    
			    textField:'deptName',
			    required:true,
			    onChange:function(newValue,oldValue){
			    	if(newValue!=oldValue){
			    		var opsDpcd=$('#opsDpcd').combobox("getValue");
		    			var id="1,2,3,4,5,6,7";
		    			if(opsDpcd){
		    				if(newValue){
				    			$("#tDt").tree("options").url="<%=basePath%>operation/arrangeconsole/treeArrangeconsole.action?odpcd="+opsDpcd+"&id="+id+"&ocode="+newValue;
				    		    $("#tDt").tree("reload");
				    		}else{
				    			$("#tDt").tree("options").url="<%=basePath%>operation/arrangeconsole/treeArrangeconsole.action?odpcd="+opsDpcd+"&id="+id;
				    		    $("#tDt").tree("reload");
				    		}
		    				query();
		    			}else{
		    				if(newValue){
		    					$.messager.alert("提示","请先选择手术科室！","info");
		    					alert_autoClose("提示","请先选择手术科室！","info");
		    					$('#opsCode').combobox("clear");
			    				return ;
		    				}
		    			}
			    	}
			    },
			 filter:function(q,row){
					var keys = new Array();
					keys[keys.length] = 'deptCode';
					keys[keys.length] = 'deptName';
					keys[keys.length] = 'deptPinyin';
					keys[keys.length] = 'deptWb';
					keys[keys.length] = 'deptInputCode';
					return filterLocalCombobox(q, row, keys);
				},
			    onHidePanel:function(none){
				    var data = $(this).combobox('getData');
				    var val = $(this).combobox('getValue');
				    var result = true;
				    for (var i = 0; i < data.length; i++) {
				        if (val == data[i].deptCode) {
				            result = false;
				        }
				    }
				    if (result) {
				        $(this).combobox("clear");
				    }else{
				        $(this).combobox('unselect',val);
				        $(this).combobox('select',val);
				    }
				}
			});
			bindEnterEvent('opsCode',popWinToDept,'easyui');//绑定回车事件
			//加载星期台数的树
		   	$('#tDt').tree({    
			    url:'<%=basePath%>operation/arrangeconsole/treeArrangeconsole.action',
			    method:'post',
			    animate:true,
			    lines:true,
			    formatter:function(node){//统计节点总数
					var s = node.text;
					if (node.children.length>0){
						var c = 0;
						for(var i=0;i<node.children.length;i++){
							c+=parseInt(node.children[i].attributes.pnum);
						}
						s += '&nbsp;<span style=\'color:blue\'>共' + c + '台</span>';
					}
					return s;
					
				},onClick: function(node){//点击节点
					$(this).tree(node.state === 'closed' ? 'expand' : 'collapse', node.target);
					if(node.text.indexOf("星期")>-1){
						$("#rtext").val(node.text);
						return ;
					}else{
						$("#rtext").val("");
					}
					$('#treeId').val(node.id);
					$('#dg').datagrid('load', {
						id: node.id
					});
					$('#modelWeek').val();
					opsDpcd = node.attributes.opsDpcd;
			    	opsCode = node.attributes.deptCode;
				}
			});
       });
	
		/**
		 * @Description:提示框自动消失
		 * @Author: zhangjin
		 * @CreateDate: 2017年2月10日
		 * @param:
		 * @return:
		 * @Modifier:
		 * @ModifyDate:
		 * @ModifyRmk:
		 * @version: 1.0
		 */
		function alert_autoClose(title,msg,icon){  
			 var interval;  
			 var time=3500;  
			 var x=1;    //设置时间2s
			$.messager.alert(title,msg,icon,function(){});  
			 interval=setInterval(fun,time);  
			        function fun(){  
			      --x;  
			      if(x==0){  
			          clearInterval(interval);  
			  $(".messager-body").window('close');    
			       }  
			}; 
			}
		//表单提交 
		function submit(){ 
			
			var one = $('#one').numberbox('getValue');
			if(one==null||""==one){
				one=0;
			}
			var two = $('#two').numberbox('getValue');
			if(two==null||""==two){
				two=0;
			}
			var three = $('#three').numberbox('getValue');
			if(three==null||""==three){
				three=0;
			}
			var four = $('#four').numberbox('getValue');
			if(four==null||""==four){
				four=0;
			}
			var five = $('#five').numberbox('getValue');
			if(five==null||""==five){
				five=0;
			}
			var six = $('#six').numberbox('getValue');
			if(six==null||""==six){
				six=0;
			}
			var seven = $('#seven').numberbox('getValue');
			if(seven==null||""==seven){
				seven=0;
			}
			var limitedqty=$('#limitedqty').numberbox('getValue');
			var usedQtys = one+";"+two+";"+three+";"+four+";"+five+";"+six+";"+seven;
			var sum=parseInt(one)+parseInt(two)+parseInt(three)+parseInt(four)+parseInt(five)+parseInt(six)+parseInt(seven);
			//每周的分配数量只能等于存储数量
			if(sum>limitedqty){
				$.messager.alert("提示","实际数量大于总正台数量！","info");
				alert_autoClose("提示","实际数量大于总正台数量！","info");
				return ;
			}
			if(sum<limitedqty){
				$.messager.alert("提示","实际数量小于总正台数量！","info");
				alert_autoClose("提示","实际数量小于总正台数量！","info");
				return;
			}
		    $('#editForm').form('submit',{  
		        url:'<%=basePath%>operation/arrangeconsole/saveArrangeconsole.action?usedQtys='+usedQtys,     
		        onSubmit:function(){
					if (!$('#editForm').form('validate')) {
						$.messager.progress('close');
						$.messager.show({
							title : '提示信息',
							msg : '验证没有通过,不能提交表单!'
						});
						return false;
					}
					$.messager.progress({text:'保存中，请稍后...',modal:true});
		        },  
		        success:function(data){ 
		        	$.messager.progress('close');
					var res = eval("(" + data + ")");
					$.messager.alert('提示',res.resMsg);
					if("success"==res.resCode){
						var opsDpcd = "";
				    	var opsCode = "";
				    	$('#tDt').tree('options').url ='<%=basePath%>operation/arrangeconsole/treeArrangeconsole.action?odpcd='+opsDpcd+'&ocode='+opsCode;
				    	$('#tDt').tree('reload');
					}
					setTimeout(function(){
       					$(".messager-body").window('close');
       				},3500);
		        }							         
		    }); 
		    $('#editForm').form('reset');
		    $('#opsCode').combobox({disabled:true});
	    }	 
		

		//将该分配信息删除
		function clear(opsDpcd,opsCode){
			$.messager.confirm('删除','确认取消分配信息吗？', function(res){//提示是否删除
				if (res){
					if(opsDpcd==""||opsDpcd==null){
						opsDpcd= $('#opsDpcd').combobox('getValue');
					}
					if(opsCode==""||opsCode==null){
						opsCode= $('#opsCode').combobox('getValue');
					}
					var id=$('#treeId').val();
					var rtext=$("#rtext").val();
					if(!opsDpcd&&!opsCode&&!id){
						$.messager.alert("提示","请选择要取消分配的科室节点或者输入要取消分配的科室");
						return ;
					}
					if(rtext.indexOf("星期")>-1){
						$.messager.alert("提示","请选择要取消分配的科室节点");
						return ;
					}
					
				
					$.ajax({
						url: "<c:url value='/operation/arrangeconsole/delArrangeCon.action'/>",
						data:{odpcd :opsDpcd,ocode:opsCode,id:id},
						type:'post',
						success: function(data) {
							$.messager.progress('close');
							$.messager.alert('提示',data.resMsg);
							if("success"==data.resCode){
								query();
						    	$('#tDt').tree('options').url ='<%=basePath%>operation/arrangeconsole/treeArrangeconsole.action';
						    	$('#tDt').tree('reload');
							}
							setTimeout(function(){
		       					$(".messager-body").window('close');
		       				},3500);
							
						}
					}); 
				}
	    	});
			
		} 
		 /**
		   * 回车弹出临床科室弹框
		   * @author  zhuxiaolu
		   * @param deptIsforregister 是否是挂号科室 1是 0否
		   * @param textId 页面上combobox的的id
		   * @date 2016-03-22 14:30   
		   * @version 1.0
		   */
			
		function popWinToDept(){
			var tempWinPath = "<%=basePath%>popWin/popWinDepartment/toDepartmentPopWin.action?textId=opsCode&deptType=C,I,N";
			window.open (tempWinPath,'newwindow',' left=150,top=80,width='+ (screen.availWidth -300) +',height='+ (screen.availHeight-170) +',scrollbars,resizable=yes,toolbar=yes')
		}	
		
		 /**
		   * 回车弹出手术科室弹框
		   * @author  zhuxiaolu
		   * @param deptIsforregister 是否是挂号科室 1是 0否
		   * @param textId 页面上combobox的的id
		   * @date 2016-03-22 14:30   
		   * @version 1.0
		   */
			
		function popWinToDpcd(){
			$('#opsDpcd').combobox('setValue','');
			var tempWinPath = "<%=basePath%>popWin/popWinDepartment/toDepartmentPopWin.action?textId=opsDpcd&deptType=OP";
			window.open (tempWinPath,'newwindow',' left=150,top=80,width='+ (screen.availWidth -300) +',height='+ (screen.availHeight-170) +',scrollbars,resizable=yes,toolbar=yes')
		}	
		/**
		 * @Description:条件查询安排信息 并将信息回显
		 * @Author: tangfeishuai
		 * @CreateDate: 2016年4月29日
		 * @param:
		 * @return:
		 * @Modifier:
		 * @ModifyDate:
		 * @ModifyRmk:
		 * @version: 1.0
		 */	
		function query(){
			 var opsDpcd = $('#opsDpcd').combobox('getValue');
	    	 var opsCode = $('#opsCode').combobox('getValue');
	    	 $('#tDt').tree('options').url ='<%=basePath%>operation/arrangeconsole/treeArrangeconsole.action?odpcd='+opsDpcd+'&ocode='+opsCode;
			 $('#tDt').tree('reload');
			 $.ajax({
				url: "<c:url value='/operation/arrangeconsole/getEveryWeek.action'/>",
				data:{odpcd :opsDpcd,ocode:opsCode},
				type:'post',
				success: function(date) {
					arrcolvo = date;
					var one = $('#one').numberbox('setValue',arrcolvo.one?arrcolvo.one:0);
					var two = $('#two').numberbox('setValue',arrcolvo.two?arrcolvo.two:0);
					var three = $('#three').numberbox('setValue',arrcolvo.three?arrcolvo.three:0);
					var four = $('#four').numberbox('setValue',arrcolvo.four?arrcolvo.four:0);
					var five = $('#five').numberbox('setValue',arrcolvo.five?arrcolvo.five:0);
					var six = $('#six').numberbox('setValue',arrcolvo.six?arrcolvo.six:0);
					var seven = $('#seven').numberbox('setValue',arrcolvo.seven?arrcolvo.seven:0);
					var limitedqty=$('#limitedqty').numberbox('setValue',arrcolvo.limitedqty?arrcolvo.limitedqty:0);
				}
			});
		}
		/**  
		 *  
		 * @Description：过滤	
		 * @Author：zhangjin
		 * @CreateDate：2016-11-29
		 * @version 1.0
		 * @throws IOException 
		 *
		 */ 
	function filterLocalCombobox(q, row, keys){
		if(keys!=null && keys.length > 0){//
			for(var i=0;i<keys.length;i++){ 
				if(row[keys[i]]!=null&&row[keys[i]]!=''){
						var istrue = row[keys[i]].indexOf(q.toUpperCase()) > -1;
						if(istrue==true){
							return true;
						}
				}
			}
		}else{
			var opts = $(this).combobox('options');
			return row[opts.textField].indexOf(q.toUpperCase()) > -1;
		}
	}
	
	/**  
	 *  
	 * @Description：清空下拉框值
	 * @Author：zhangjin
	 * @CreateDate：2016-11-29
	 * @version 1.0
	 * @throws IOException 
	 *
	 */ 
	function delSelectedDatass(popWinId){
		var selArr = popWinId.split(',');
		for (var i = 0; i < selArr.length; i++) {
			if ($('#' + selArr[i]).attr("class")
					&& /combobox/ig.test($('#' + selArr[i]).attr("class"))) {
				$('#' + selArr[i]).combobox('clear');
			} else if ($('#' + selArr[i]).attr("class")
					&& /textbox/ig.test($('#' + selArr[i]).attr("class"))) {
				$('#' + selArr[i]).textbox('clear');
			} else {
				$('#' + selArr[i]).val('');
			}

		}
		$('#opsCode').combobox('clear');
		$('#opsCode').combobox({disabled:true});
	}
	</script>
	<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>	
	</body>
</html>