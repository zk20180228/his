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
	<div class="easyui-panel departmentContactEdit" id="panelEast" data-options="title:'部门科室间关系编辑',iconCls:'icon-form'" style="width:100%;">
			<div style="width:100%;">
			<form id="editForm" method="post" enctype="multipart/form-data">
				<input type="hidden" id="id" name="id" value="${deptcontact.id }"><!-- 列表中当前行的id -->
				<input type="hidden" id="treeId" name="treeId" value="${treeId }"><!-- 当前行的父级（树节点）的id -->
				<input type="hidden" id="sortId" name="sortId" value="${deptcontact.sortId }">
				<input type="hidden" id="ordertopath" name="ordertopath" value="${deptcontact.ordertopath }">
				<input type="hidden" id="path" name="path" value="${deptcontact.path  }">
				<input type="hidden" id="upperpath" name="upperpath" value="${deptcontact.upperpath  }">
				<input type="hidden" id="gradeCode" name="gradeCode" value="${deptcontact.gradeCode  }">
				<input  type="hidden" id="referenceType" name="referenceType" value="${deptcontact.referenceType}"/>
				<input type="hidden" id="createUser" name="createUser" value="${deptcontact.createUser  }">
				<input type="hidden" id="createDept" name="createDept" value="${deptcontact.createDept  }">
				<input  type="hidden" id="createTime" name="createTime" value="${deptcontact.createTime}"/>
				<table class="honry-table" cellpadding="0" cellspacing="1" border="0"  style="width:100%;border:0px;">
					<tr>
						<td class="honry-lable" style="border-left:0;border-top:0;">
							<span>父级名称:</span>&nbsp;&nbsp;
						</td>
						<td style="text-align: left;border-top:0;">
								<input id="pardeptIdHidden" type="hidden"></input>
								<input id="pardeptId"  name="pardeptId" type="hidden" value="${treeId }" ></input>
								<input id="pardeptIdText"  name="treeName" value="${treeName }" onkeydown="popWinToDept()" class="easyui-textbox" data-options="required:true,editable:false" missingMessage="请选择所属科室"></input>
								<a href="javascript:delSelectedData('pardeptIdText,pardeptId');"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a>
						</td>
					</tr>
					<tr>
						<td class="honry-lable" style="border-left:0">
							<span>部门分类:</span>&nbsp;&nbsp;
						</td>
						<td style="text-align: left;">
							<input  id="deptCalss" value="${deptcontact.deptCalss}" name="deptCalss"  />
						</td>
					</tr>
					<tr>
						<td class="honry-lable" style="border-left:0">
							<span id="dep">部门名称:</span>&nbsp;&nbsp;
						</td>
						<td style="text-align: left;">
							<input id="deptId" type="hidden"  name="deptId" value="${deptcontact.deptId}" />
							<input id="deptCode" type="hidden" name="deptCode"  value="${deptcontact.deptCode}"  />
							<input id="deptName" type="hidden"  name="deptName" value="${deptcontact.deptName}" />
							<div id="deptTextDiv"><input id="deptText"      value="${deptcontact.deptName}" class="easyui-combobox" data-options="required:true"   missingMessage="选择部门分类" ></input></div>
						</td>
					</tr>
					<tr>
						<td class="honry-lable" style="border-left:0">
							<span>科室类型:</span>&nbsp;&nbsp;
						</td>
						<td style="text-align: left;">
						<input id="deptTypeCode" type="hidden" name="deptType" value="${deptcontact.deptType}">
						<input  id="deptType" class="easyui-textbox" readonly=true />
						</td>
					</tr>
				    <tr>	
						<td class="honry-lable" style="border-left:0">
							<span>自定义码:</span>&nbsp;&nbsp;
						</td>
						<td style="text-align: left;">
							<input id="userDefinedCode" class="easyui-textbox" value="${deptcontact.userDefinedCode}" name="userDefinedCode"></input>
						</td>						
					<tr>
				  	    <td class="honry-lable" style="border-left:0">
							<span>英文名:</span>&nbsp;&nbsp;
						</td>
						<td style="text-align: left;">
							<input id="deptEnCode"  class="easyui-textbox" value="${deptcontact.deptEnCode}" name="deptEnCode" missingMessage="请输入选择性别"></input>
						</td>
					</tr>
					<tr>
				  	    <td class="honry-lable" style="border-left:0">
							<span>状态:</span>&nbsp;&nbsp;
						</td>
						<td style="text-align: left;">
							<input id="validState" value="${deptcontact.validState}" name="validState"
								 class="easyui-combobox" data-options="valueField:'id',textField:'value',data:[{id:1,value:'在用'},{id:0,value:'停用'},{id:2,value:'废弃'}],required:true" />
						</td>
					</tr>
				   <tr>
						<td class="honry-lable" style="border-left:0">
							<span>备注:</span>&nbsp;&nbsp;
						</td>
						<td style="text-align: left;">
						<textarea rows="5" cols="30" id="mark" name="mark">${deptcontact.mark}</textarea>
						</td>
					</tr>
				</table>
				 <div style="text-align:center;padding:5px">
			    	<c:if test="${empty deptcontact.id}">			    	
			    	<a href="javascript:submit(1);" class="easyui-linkbutton" data-options="iconCls:'icon-save'">连续添加</a>
			    	</c:if>
			    	<a id="save" href="javascript:submit(0);" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a>
			    	<a href="javascript:clear();" class="easyui-linkbutton" data-options="iconCls:'icon-clear'">清除</a>
			    	<a href="javascript:closeLayout()" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'">关闭</a>
			    </div>
	    	</form>
	    </div>
	</div>
	<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/js/easyui-combobox-filterLocal.js"></script>
	<script type="text/javascript">
	    var deptTypeToName;
	    var urlPath;
	    var deptCodeToType=new Map();
	    var str = "${deptcontact.deptCalss}";
	    //页面加载
		$(function(){
			$('#deptType').textbox({});
			//部门code和部门类型渲染
			$('#deptCalss').combobox({
				url : "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action'/>?type=deptclass ",
				valueField:'encode',    
			    textField:'name',
				multiple : false,
			    onSelect:function(record){
			    	if(record.encode==2){ //部门分类为科室
			    		$('#dep').text('部门名称');
			    		urlPath="<%=basePath %>baseinfo/department/departmentCombobox.action";
						Dept(urlPath);
						$('#deptText').combobox('setValue','');
						$('#deptType').textbox('setText','');
			    	}else if(record.encode==3){    //部门分类为终极科室（诊室）
			    		urlPath="<%=basePath %>baseinfo/departmentContact/clinicCodeAndName.action";
						$('#dep').text('诊室名称');
						Contact(urlPath);
						$('#deptText').combobox('setValue','');
						$('#deptType').textbox('setText','');
			    	}else{   //部门分类为分类
			    		$('#dep').text('部门名称');
			    		$('#deptTextDiv').html("<input id='deptText'  class='easyui-textbox'  data-options='required:true'  missingMessage='请输入科室'></input>");
			    		$('#deptId').val("");
				    	$('#deptCode').val("");
				    	$('#deptText').textbox({});
			    	}
			    	$('#deptId').val("");   //清空部门编码
			    	$('#deptCode').val(""); //清空部门表/诊所表中的部门代码
			    	$('#deptName').val(""); //清空部门名称
			    	$('#deptType').textbox('setText','');  //清空科室类型
			    	//$('#deptTree').combobox('clear');
			    	//$('#clinic').combobox('clear');  
			    	//$('#types').val("");
			    }
			});
			$.ajax({
				url: "<%=basePath %>baseinfo/departmentContact/ClinicCodeToDeptType.action",
				dataType:'json',
				async:false,
				success: function(data) {
					var len=data.length;
					for(var i=0;i<len;i++){
						deptCodeToType.put(data[i].deptCode,data[i].deptType);
						}
					}
			});
			if($('#id').val()!=null){
				if($('#pardeptId').val()==""||$('#pardeptId').val()==null){
					$('#pardeptId').val('root');
				}
			}
			if(str==2){ //部门分类为科室
				urlPath="<%=basePath %>baseinfo/department/departmentCombobox.action";
				$('#dep').html('部门名称');
				Dept(urlPath);
				
			}else if(str==3){ //部门分类为终极科室（诊室）
				urlPath="<%=basePath %>baseinfo/departmentContact/clinicCodeAndName.action";
				$('#dep').html('诊室名称');
				Contact(urlPath);
				//$("#clinicDiv").show();
			}else{
				$('#deptTextDiv').html("<input id='deptText' value='${deptcontact.deptName}' class='easyui-textbox'  data-options='required:true'  missingMessage='请输入科室'/>");
			}
			
			
			
			//加载科室诊室树
			$.ajax({
				url: "<%=basePath %>baseinfo/pubCodeMaintain/queryDictionary.action",
				data:{'type':'depttype'},
				async:false,
				dataType:'json',
				success: function(data) {
				deptTypeToName=data;
				var deptCodeType=$('#deptTypeCode').val();
				var len=data.length;
				for(var i=0;i<len;i++){
					if(deptCodeType==data[i].encode){
						$('#deptType').textbox('setText',data[i].name);
						break;
					}
				}
				}
			});
		});
		$('#pardeptIdText').textbox({
			inputEvents: $.extend({},$.fn.textbox.defaults.inputEvents,{
				keyup: popWinToDept
				})
			
		});
	  function formaterEncode(encode){
	    	var deptcode1=null;
	    	var len=deptTypeToName.length;
	    	for(var i=0;i<len;i++){
	    		deptcode1=deptTypeToName[i].encode;
	    		if(deptcode1==encode){
	    			$('#deptType').textbox('setText',deptTypeToName[i].name);
	    			break;
	    		}
	    	}
		}
	 //部门分类为终极科室（诊室）
	function Contact(url){
		 //分诊下拉
		$('#deptText').combobox({
				required : true,
				mode:'remote',
				url :url,
				valueField:'id',
				textField:'clinicName',
				onSelect:function(parm){
					$('#deptName').val(parm.clinicName);
					$('#deptCode').val(parm.clinicDeptId);
					$('#deptId').val(parm.id);
					var type1= parm.clinicDeptId;
					var type=deptCodeToType.get(type1);
					$('#deptTypeCode').val(type);
					formaterEncode(type);
					},filter: function(q, row){
					    var keys = new Array();
					    keys[keys.length] = 'id';
					    keys[keys.length] = 'clinicName';
					    keys[keys.length] = 'clinicPiyin';
					    keys[keys.length] = 'clinicWb"';
					    keys[keys.length] = 'clinicInputcode';
					    return filterLocalCombobox(q, row, keys);
					}
			});
	}
	 function Dept(url){
		 //科室下拉
		 $('#deptText').combobox({
				required : true,
				pagination : true,//是否显示分页栏
				fitColumns : true,//自适应列宽
				mode:'remote',
				url :url,
				valueField : 'deptCode',
				textField : 'deptName',
				columns : [[
				{field : 'deptCode',title : 'deptCode',hidden:true},
				{field : 'deptName',title : 'deptName', width: '100%'},
				] ],
				onSelect:function(parm){
					$('#deptId').val(parm.id)
					$('#deptName').val(parm.deptName);
					$('#deptCode').val(parm.deptCode);
					$('#deptTypeCode').val(parm.deptType);
					formaterEncode(parm.deptType);
				},filter: function(q, row){
				    var keys = new Array();
				    keys[keys.length] = 'deptCode';
				    keys[keys.length] = 'deptName';
				    keys[keys.length] = 'deptPinyin';
				    keys[keys.length] = 'deptWb"';
				    keys[keys.length] = 'deptInputcode';
				    return filterLocalCombobox(q, row, keys);
				}
			});
	 }
	 //表单提交submit信息
	  function submit(flg){
		if($('#deptName').val()==null||$('#deptName').val()==""){
			$('#deptName').val($('#deptText').textbox('getText'));
		}
		if($('#deptCalss').combobox('getValue')!=3){
			$('#clinicDiv').text('');
		}
	  	$('#editForm').form('submit',{
	  		url:"<%=basePath %>baseinfo/departmentContact/saveContion.action",
	  		 onSubmit:function(){ 
	  		 	 if(!$('#editForm').form('validate')){
					$.messager.alert('提示信息','验证没有通过,不能提交表单!','warning');
					close_alert();
					   return false ;
			     } 
	  		 	 return true;
			 },  
			success:function(data){
				if(flg==0){
					$.messager.alert('提示','保存成功');
			   		$('#divLayout').layout('remove','east');
				    //实现刷新
		          	$("#list").datagrid("reload");
				    $("#tDt").tree("reload");
			   }else if(flg==1){
					//清除editForm
					$('#editForm').form('reset');
			  	}
			 },
			error:function(data){
				$.messager.alert('提示','保存失败');
			}
	  	});
	 }
	  
	//关闭
  	function closeLayout(){
		$('#divLayout').layout('remove','east');
		$("#list").datagrid("reload");
	}
		
	// 清除页面填写信息
	function clear(){
		$('#editForm').form('reset');
	}
	/**
	* 回车1父级名称选择窗口
	* @author  zhuxiaolu
	* @param textId 页面上commbox的的id
	* @date 2016-03-22 14:30   
	* @version 1.0
	*/
	function popWinToDept(e){
		popWinDeptCallBackFn = function(node){
	    	$("#pardeptId").val(node.id);
			$('#pardeptIdText').textbox('setValue',node.deptName);
		};
		var timer=new Date().getTime();
		var tempWinPath = "<%=basePath%>popWin/popWinDepartmentContact/toDepartmentContactPopWin.action?textId="+$('#pardeptIdText').val()+'&timer='+timer;
		window.open (tempWinPath,'newwindow',' left=150,top=80,width='+ (screen.availWidth -300) +',height='+ (screen.availHeight-170) 
	+',scrollbars,resizable=yes,toolbar=yes')
	
	}
</script>
<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
</body>
</html>