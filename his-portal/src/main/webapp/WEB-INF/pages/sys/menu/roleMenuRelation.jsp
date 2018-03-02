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
<div class="easyui-layout" data-options="fit">
	<div data-options="region:'center',split:false,title:'角色列表',iconCls:'icon-book'" style="width:100%;height:100%;border:0">
		<input type="hidden" id="menuId" value="${menu.id }">
		<table id="roleMenuList" data-options="url:'${pageContext.request.contextPath}/sys/queryRoleTreegrid.action?showAll=true&menuId=${menu.id }',fitColumns:true,idField:'id',treeField:'name',toolbar:'#toolbarIdRoleMenu',fit:true">
			<thead>
				<tr>
					<th data-options="field:'id',rowspan:2,hidden:true">主键</th>
					<th data-options="field:'name',rowspan:2">名称</th>
					<th data-options="field:'manage',colspan:2">权限管理</th>
					<th data-options="field:'dataRight',rowspan:2,formatter: functionCombo">数据权限</th>
					<th data-options="field:'menuBut',rowspan:2,formatter: functionCheck">功能权限</th>
					<th data-options="field:'iconCls',rowspan:2,hidden:true">图标</th>
					<th data-options="field:'_parentId',rowspan:2,hidden:true">父节点</th>
					<th data-options="field:'path',rowspan:2,hidden:true">层级路径</th>
				</tr>
				<tr>
					<th data-options="field:'manageDar',formatter: funManageDar">数据权限管理</th>
					<th data-options="field:'manageBut',formatter: funManageBut">功能权限管理</th>
				</tr>
			</thead>
		</table>
	</div>
</div>
	<div id="toolbarIdRoleMenu">
		<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-disk', plain:true"  onclick="saveRoleMenu()">保存</a> 
		<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-reload',plain:true" onclick="reloadRoleMenu()">刷新</a>
		<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-open',plain:true" onclick="openRoleMenu()">展开</a>
		<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-fold',plain:true"  onclick="closedRoleMenu()">折叠</a>
	</div>
	<script type="text/javascript">
		var dataRightList="";
		var staMap= new Map();
		var endMap= new Map();
		//初始化页面
		$(function(){
			if("${menu.haveson}"==1){
				$.ajax({
					url: "<c:url value='/sys/queryDataRightCombobox.action'/>",
					type:'post',
					success: function(dataRight) {
						dataRightList = dataRight;
					}
				});
			}
			//加缓冲时间1毫秒
			setTimeout(function(){
				$('#roleMenuList').treegrid({
					onLoadSuccess: function (row, data) {//加载成功
						if(data.rows!=null&&data.rows.length>0){
							$.each(data.rows, function(i,r){//初始化数据   
								var key = r.id;
								if(key!=null&&key!=""){
									var rightObj=new Object();
									var rightObj1=new Object();
									rightObj['dataRight']=(r.dataRight==null||r.dataRight=='')?'-1':r.dataRight;
									rightObj1['dataRight']=(r.dataRight==null||r.dataRight=='')?'-1':r.dataRight;
									if(r.menuBut==null||r.menuBut==''){
										rightObj['funRight']=[];
										rightObj1['funRight']=[];
										
									}else{
										rightObj['funRight']=r.menuBut.split(",");
										rightObj1['funRight']=r.menuBut.split(",");
									}
									staMap.put(key,rightObj);
									endMap.put(key,rightObj1);
								}
							});  
						}
			        }
				});
            },1);
		});
		
		//初始化数据权限
		function functionCombo(value,row,index){
			var retVal = "";
			if(dataRightList!=null&&dataRightList!=""){
				retVal = '<select type="select" name="dataRight_'+row.path+'"  id="dataRight_'+row.id+'" onchange="funSelect(this)">';
				for(var i=0;i<dataRightList.length;i++){
					if(value==dataRightList[i].id){
						retVal += '<option value="'+dataRightList[i].id+'" selected="selected" >'+dataRightList[i].name+'</option>';
					}else{
						retVal += '<option value="'+dataRightList[i].id+'" >'+dataRightList[i].name+'</option>';
					}
				}
				retVal += '</select>';
			}
			return '&nbsp;&nbsp;'+retVal+'&nbsp;&nbsp;';
		}
		
		//初始化功能权限
		function functionCheck(value,row,index){
			var retVal = ""; 
			var buts = row.but;
			if(buts!=null&&buts!=""){
				for(var i=0;i<buts.length;i++){
					if(value!=null&&value!=""){
						var id = value.split(",");
						var checked = false;
						for(var j=0;j<id.length;j++){
							if(buts[i].id==id[j]){
								checked = true;
							}
						}
						if(checked){
							retVal += '<input type="checkbox" id="menuBut_'+row.id+'_'+buts[i].id+'" name="path_'+row.path+'" value="1" checked="checked" onchange="funCheckBox(this)" ><span>'+buts[i].name+'</span>&nbsp;&nbsp;';
						}else{
							retVal += '<input type="checkbox" id="menuBut_'+row.id+'_'+buts[i].id+'" name="path_'+row.path+'" value="0" onchange="funCheckBox(this)" ><span>'+buts[i].name+'</span>&nbsp;&nbsp;';
						}
					}else{
						retVal += '<input type="checkbox" id="menuBut_'+row.id+'_'+buts[i].id+'" name="path_'+row.path+'"  value="0"  onchange="funCheckBox(this)" ><span>'+buts[i].name+'</span>&nbsp;&nbsp;';
					}
				}
			}
			return '&nbsp;&nbsp;'+retVal+'&nbsp;&nbsp;';
		}	
		
		//数据权限管理
		function funManageDar(value,row,index){
			var retVal="";
			if(dataRightList!=null&&dataRightList!=""){
				if($('#roleMenuList').treegrid('getChildren',row.id).length>0){
					retVal = '<select type="select" id="manageDar_'+row.id+'" name="manageDarName&'+row.path+'" onchange="funSelManageDar(this)">';
					for(var i=0;i<dataRightList.length;i++){
						retVal += '<option value="'+dataRightList[i].id+'" >'+dataRightList[i].name+'</option>';
					}
					retVal += '</select>';
				}
			}
			return '&nbsp;&nbsp;'+retVal+'&nbsp;&nbsp;';
		}
		
		//数据权限管理操作
		function funSelManageDar(selfObj){
			var name = $(selfObj).prop("name").split("&");
			var selects = $("select[name^='dataRight_"+name[1]+"']");
			var value = $(selfObj).val();
			$("select[name^='manageDarName&"+name[1]+"']").val(value);
			selects.each(function(){
				$(this).val(value);
				var id = $(this).prop("id").split("_");
				endMap.get(id[1]).dataRight=$(selfObj).val();
			});
		}
		
		//功能权限管理
		function funManageBut(value,row,index){
			var retVal="";
			retVal += '<input type="checkbox" id="manageBut_'+row.id+'" name="manageButName&'+row.path+'" onchange="funCheManageBut(this)" ><span>管理</span>&nbsp;&nbsp;';
			if($('#roleMenuList').treegrid('getChildren',row.id).length>0){
				retVal += '<input type="checkbox" id="manageButSameId&'+row.path+'" onchange="funCheManageSame(this)" ><span>下级与上级相同</span>&nbsp;&nbsp;';
			}
			return '&nbsp;&nbsp;'+retVal+'&nbsp;&nbsp;';
		}
		
		//功能权限管理操作
		function funCheManageBut(cheObj){
			var name = $(cheObj).prop("name").split("&");
			if($(cheObj).is(':checked')){//选中
				var oName = name[1].split("_");
				var pName = "path";
				for(var i=0;i<oName.length;i++){
					pName+="_"+oName[i];
					var pChecks = $("input:checkbox[name='"+pName+"']");
					pChecks.each(function(){
						$(this).prop("checked",true);
						var pid = this.id.split("_");
						if(!endMap.get(pid[1]).funRight.in_array(pid[2])){//当前没有此权限-增加
							endMap.get(pid[1]).funRight.push(pid[2]);
						}
					});
				}
				if($("input:checkbox[id='manageButSameId&"+name[1]+"']").is(':checked')){//下级与上级相同选中
					var cChecks = $("input:checkbox[name^='path_"+name[1]+"']");	
					cChecks.each(function(){
						$(this).prop("checked",true);
						var cid = this.id.split("_");
						if(!endMap.get(cid[1]).funRight.in_array(cid[2])){//当前没有此权限-增加
							endMap.get(cid[1]).funRight.push(cid[2]);
						}
					});
				}
			}else{//取消选中
				var cChecks = $("input:checkbox[name^='path_"+name[1]+"']");	
				cChecks.each(function(){
					$(this).prop("checked",false);
					var cid = $(this).prop("id").split("_");
					if(endMap.get(cid[1]).funRight.in_array(cid[2])){//当前有此权限-删除
						endMap.get(cid[1]).funRight.remove(cid[2]);
					}
				});
			}
		}
		
		//功能权限下级与上级相同操作
		function funCheManageSame(cheObj){
			var name = $(cheObj).prop("id").split("&");
			if($(cheObj).is(':checked')){//选中
				if($("input:checkbox[name^='manageButName&"+name[1]+"']").is(':checked')){//管理选中
					var ches = $("input:checkbox[name^='path_"+name[1]+"']");
					ches.each(function(){
						$(this).prop("checked",true);
						var cid = this.id.split("_");
						if(!endMap.get(cid[1]).funRight.in_array(cid[2])){//当前没有此权限-增加
							endMap.get(cid[1]).funRight.push(cid[2]);
						}
					});
				}
			}else{//取消选中
				if($("input:checkbox[name^='manageButName&"+name[1]+"']").is(':checked')){//管理选中
					var ches = $("input:checkbox[name^='path_"+name[1]+"']");
					ches.each(function(){
						$(this).prop("checked",false);
						var cid = $(this).prop("id").split("_");
						if(endMap.get(cid[1]).funRight.in_array(cid[2])){//当前有此权限-删除
							endMap.get(cid[1]).funRight.remove(cid[2]);
						}
					});
					var oches = $("input:checkbox[name='path_"+name[1]+"']");
					oches.each(function(){
						$(this).prop("checked",true);
						var ocid = this.id.split("_");
						if(!endMap.get(ocid[1]).funRight.in_array(ocid[2])){//当前没有此权限-增加
							endMap.get(ocid[1]).funRight.push(ocid[2]);
						}
					});
				}
			}
		}
		
		//数据权限下拉操作
		function funSelect(selfObj){
			var id = selfObj.id.split("_");
			endMap.get(id[1]).dataRight=$(selfObj).val();
		}
		
		//功能权限复选操作
		function funCheckBox(selfObj){
			var id = selfObj.id.split("_");//menuBut_角色id_按钮id
			if($(selfObj).is(':checked')){//选中
				$(selfObj).val(1);
				$(selfObj).prop("checked",true);
				var text = $(selfObj).next().text();
				var name = selfObj.name.split("_");
				var checName = "path";
				for(var i=1;i<name.length;i++){
					checName += "_"+name[i];
					var inputs = $("input[name='"+checName+"']");
					inputs.each(function(){
						if($(this).next().text()=="访问"||$(this).next().text()==text){
							$(this).prop("checked",true);
							var cid = this.id.split("_");
							if(!endMap.get(cid[1]).funRight.in_array(cid[2])){//当前没有此权限-增加
								endMap.get(cid[1]).funRight.push(cid[2]);
							}
						}
					});
				}
				if(!endMap.get(id[1]).funRight.in_array(id[2])){//当前无此权限-新增
					endMap.get(id[1]).funRight.push(id[2]);
				}
			}else{//取消
				$(selfObj).val(0);
				$(selfObj).prop("checked",false);
				if($(selfObj).next().text()=="访问"){
					var name = selfObj.name;
					var inputs = $("input[name^='"+name+"']");
					inputs.each(function(){
						$(this).prop("checked",false);
						var cid = $(this).prop("id").split("_");
						if(endMap.get(cid[1]).funRight.in_array(cid[2])){//当前有此权限-删除
							endMap.get(cid[1]).funRight.remove(cid[2]);
						}
					});
				}else{
					var text = $(selfObj).next().text();
					var name = selfObj.name;
					var inputs = $("input[name^='"+name+"']");
					inputs.each(function(){
						if($(this).next().text()==text){
							$(this).prop("checked",false);
							var cid = this.id.split("_");
							if(endMap.get(cid[1]).funRight.in_array(cid[2])){//当前有此权限-删除
								endMap.get(cid[1]).funRight.remove(cid[2]);
							}
						}
					});
				}
				if(endMap.get(id[1]).funRight.in_array(id[2])){//当前有此权限-删除
					endMap.get(id[1]).funRight.remove(id[2]);
				}
			}
		}
		
		//保存
	   	function saveRoleMenu(){
			var menuId = $('#menuId').val();
			if(menuId==null||menuId==""){
				$.messager.alert('提示',"请选择栏目!");
				close_alert();
				return;
			}
	   		var arr=new Array();
	   		for(var i=0;i<staMap.keys.length;i++){
	   			var orRightObj=staMap.get(staMap.keys[i]);
	   			var newRightObj=endMap.get(staMap.keys[i]);
	   			var saveObj={};
				saveObj["roleId"]=staMap.keys[i];
	   			if(orRightObj.dataRight!=newRightObj.dataRight){
	   				saveObj["dataRightFlag"]="edit";//1-update
	   				saveObj["dataRight"]=newRightObj.dataRight;
	   			}else{
	   				saveObj["dataRightFlag"]="no";//other-no opr
	   				saveObj["dataRight"]=orRightObj.dataRight;
	   			}
	   			var tempStr="";
	   			for(var j=0;j<orRightObj.funRight.length;j++){
	   				if(newRightObj.funRight.indexOf(orRightObj.funRight[j])<0){// 删除的功能
	   					tempStr+=orRightObj.funRight[j]+"_del,";
	   				}
	   			}
	   			for(var j=0;j<newRightObj.funRight.length;j++){
	   				if(orRightObj.funRight.indexOf(newRightObj.funRight[j])<0){// 新增的功能
	   					tempStr+=newRightObj.funRight[j]+"_new,";
	   				}
	   			}
	   			if(tempStr!=""){
	   				saveObj["functionRight"]=tempStr.substring(0, tempStr.length-1);
	   			}else{
	   				saveObj["functionRight"]="";
	   			}
	   			if(!(saveObj["dataRightFlag"]=="no"&&saveObj["functionRight"]=="")){
		   			arr.push(saveObj);
	   			}
	   		}
	   		if(arr.length!=0){
	   			$.messager.confirm('提示', '确定保存修改信息?', function(result){
		                if(result) { 
			                $.messager.progress({text:'保存中，请稍后...',modal:true});
							     $.post("<c:url value='/sys/saveMenuRole.action'/>",{"menuId":menuId,"jsonData":$.toJSON(arr)},function(dataMap){
						   			 if(dataMap.resMsg=="success"){
						   				 $.messager.alert('提示',dataMap.resCode);
						   				 $('#menuWin').dialog('close');
						   				 AddOrShowEast('EditForm',"<c:url value='/sys/viewRoleMenuRelation.action'/>?menuAlias=LMGL&id="+menuId,'50%');
						   				 $.messager.progress('close');
						   			 }else{
						   				 $.messager.alert('提示',dataMap.resCode);
						   				 $.messager.progress('close');
						   			 }
							   	 });
			              }else{
					        	return;
				          } 
		            });  
	  		}else{
	  			$.messager.alert('提示',"没有修改的权限!");
	  			close_alert();
	  			return;
	  		}
	   	}
		
	//刷新
	function reloadRoleMenu(){
		$('#roleMenuList').treegrid('reload');
	}
	
	//展开
	function openRoleMenu(){
		$('#roleMenuList').treegrid('expandAll');
	}
	
	//折叠
	function closedRoleMenu(){
		$('#roleMenuList').treegrid('collapseAll');
	}
	</script>
	</body>
</html>
