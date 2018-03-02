<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>

<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<style type="text/css">
.window .panel-header .panel-tool a{
  	  	background-color: red;	
}
.toolbox td{
	width:310px;
	height:22px;
}
.mouse_color{
	background-color: #CFCFCF;
	cursor:pointer;
} 
</style>
<title>电子病历模板维护</title>
	<base href="<%=basePath%>">
	<%@ include file="/common/metas.jsp"%>
	<script type="text/javascript">
	$(function(){
		tDtTree();
		tDt1Tree();
	});
	/**
	* 鼠标移到的颜色
	*/
	$("#toolbox tr").mouseover(function(){
	$(this).addClass("mouse_color");
	});

	/**
	* 鼠标移出的颜色
	*/
	$("#toolbox tr").mouseout(function(){
	$(this).removeClass("mouse_color");
	}); 

	//加载患者树
	function tDtTree(){
		$('#tDt').tree({
			url : '<%=basePath%>emrs/writeMedRecord/patientTree.action?menuAlias=${menuAlias}',
			method : 'get',
			animate : true,
			lines : true,
			onlyLeafCheck:true,
			formatter : function(node) {//统计节点总数
				var s = node.text;
				if(node.attributes.dateState!=null&&node.attributes.dateState!=''){
					s += node.attributes.dateState;
				}
				if(node.attributes.leaveFlag!=null&&node.attributes.leaveFlag!=''){
					s += node.attributes.leaveFlag;
				} 	
				if (node.children.length > 0) {					 						
					s += '<span style=\'color:blue\'>('
							+ node.children.length + ')</span>';					
				}					
				return s;
			},onClick : function(node){	
				$(this).tree(node.state === 'closed' ? 'expand' : 'collapse', node.target);
				var nod = $('#tDt1').tree('getSelected');
				var patId = '';
				var erType = getErType();
				if(node.id.length > 1){
					var patId = node.attributes.medicalrecordId;//病历号
					var url = '<%=basePath %>emrs/writeMedRecord/toEmrMainListView.action?menuAlias=${menuAlias}';
					if(erType == 'merFirst'){
						var cardId = node.attributes.inpatientNo;//住院号
						url = '<%=basePath%>emrs/emrFirst/toEmrFitstEditView.action?menuAlias=${menuAlias}&patId='+cardId+'&cardNo='+cardId;
					}else if(erType == 'emrMaintenance'){
						var nurInpatientNo = node.attributes.inpatientNo;
						url = '<%=basePath%>emrs/maintenance/toViewMaintenanceList.action?menuAlias=${menuAlias}&nurInpatientNo='+nurInpatientNo;
					}
					var frameFlag = $(window.frames["iframe"].document).find("#flag").val();
					patId = node.attributes.medicalrecordId;
					if(frameFlag == 'editMain'){//电子病历编辑界面
						$.messager.confirm('确认', '是否保存当前病历？', function(res){//提示是否保存
							if (res){
								iframe.window.templateDesign.fnCheckForm('save');
							}else{
								$('iframe').attr('src',url);
							}
						});
					}else if(frameFlag == 'merFirst'){//病案首页
						$.messager.confirm('确认', '是否保存当前信息？', function(res){//提示是否保存
							if (res){
								iframe.window.submit();
								$('iframe').attr('src',url);
							}else{
								$('iframe').attr('src',url);
							}
						});
					}else if(frameFlag == 'emrMaintenance'){//护理信息编辑界面
						$.messager.confirm('确认', '是否保存当前信息？', function(res){//提示是否保存
							if (res){
								iframe.window.submit();
								$('iframe').attr('src',url);
							}else{
								$('iframe').attr('src',url);
							}
						});
					}else {
						$('iframe').attr('src',url);
					} 
				}else if(node.id.length <= 1){
					$.messager.alert('提示','请选择患者进行操作！');
					setTimeout(function(){$(".messager-body").window('close')},3500);
				}
			}
		});
	}
		//加载模板树
		function tDt1Tree(){
			$('#tDt1').tree({
				url : '<%=basePath %>emrs/emrTemplate/treeErType.action?menuAlias=${menuAlias}',
				method:'get',
				lines : true,
				cache : false,
				animate : true,
				formatter : function(node) {//统计节点总数
					var s = node.text;
					if (node.children.length > 0) {
						s += '&nbsp;<span style=\'color:blue\'>('
							+ node.children.length + ')</span>';
					}
					return s;
				},
				onLoadSuccess: function(node, data) {
					var root = $('#tDt1').tree('getRoot',node);
				},
				onClick : function(node) {//点击节点
					var children = $(this).tree('getChildren', node.target);//判断是否有子节点
					 if(children.length>0){
						 $(this).tree(node.state === 'closed' ? 'expand' : 'collapse', node.target);
					 	return false;
					 }
					var pNode = $('#tDt').tree('getSelected');//患者树选中节点
					var erType = getErType();//病理类型
					var frameFlag = $(window.frames["iframe"].document).find("#flag").val();//iframe
					if(pNode == null || pNode.attributes.pid == 'root'){
						$.messager.alert('提示',"请选择患者！！！");
						setTimeout(function(){$(".messager-body").window('close')},3500);
						return false;
					}
					var patId = pNode.attributes.medicalrecordId;//病历号
					if(frameFlag == 'editMain'){//电子病历编辑界面
						$.messager.confirm('确认', '是否保存当前病历？', function(res){//提示是否保存
							if (res){
								iframe.window.templateDesign.fnCheckForm('save');
							}else{
								if(node.id == 'merFirst'){//病案首页节点
									var cardId = pNode.attributes.inpatientNo;//住院号
									$('iframe').attr('src','<%=basePath%>emrs/emrFirst/toEmrFitstEditView.action?menuAlias=${menuAlias}&patId='+cardId+'&cardNo='+cardId);
								}else if(node.id == 'emrMaintenance'){//护理信息节点
									var nurInpatientNo = pNode.attributes.inpatientNo;
									$('iframe').attr('src','<%=basePath%>emrs/maintenance/toViewMaintenanceList.action?menuAlias=${menuAlias}&nurInpatientNo='+nurInpatientNo);
								}else{
									$('iframe').attr('src','<%=basePath %>emrs/writeMedRecord/toEmrMainListView.action?menuAlias=${menuAlias }');
								} 
							}
						});
					}else if(frameFlag == 'emrMaintenance'){//护理信息编辑界面
						$.messager.confirm('确认', '是否保存当前信息？', function(res){//提示是否保存
							if (res){
								iframe.window.submit();
							}
						});
						if(node.id == 'merFirst'){//病案首页节点
							var cardId = pNode.attributes.inpatientNo;//住院号
							$('iframe').attr('src','<%=basePath%>emrs/emrFirst/toEmrFitstEditView.action?menuAlias=${menuAlias}&patId='+cardId+'&cardNo='+cardId);
						}else if(node.id == 'emrMaintenance'){//护理信息节点
							var nurInpatientNo = pNode.attributes.inpatientNo;
							$('iframe').attr('src','<%=basePath%>emrs/maintenance/toViewMaintenanceList.action?menuAlias=${menuAlias}&nurInpatientNo='+nurInpatientNo);
						}else{
							$('iframe').attr('src','<%=basePath %>emrs/writeMedRecord/toEmrMainListView.action?menuAlias=${menuAlias }');
						}
					}else if(frameFlag == 'merFirst'){//病案首页
						$.messager.confirm('确认', '是否保存当前信息？', function(res){//提示是否保存
							if (res){
								iframe.window.submit();
								if(node.id == 'merFirst'){//病案首页节点
									var cardId = pNode.attributes.inpatientNo;//住院号
									$('iframe').attr('src','<%=basePath%>emrs/emrFirst/toEmrFitstEditView.action?menuAlias=${menuAlias}&patId='+cardId+'&cardNo='+cardId);
								}else if(node.id == 'emrMaintenance'){//护理信息节点
									var nurInpatientNo = pNode.attributes.inpatientNo;
									$('iframe').attr('src','<%=basePath%>emrs/maintenance/toViewMaintenanceList.action?menuAlias=${menuAlias}&nurInpatientNo='+nurInpatientNo);
								}else{
									$('iframe').attr('src','<%=basePath %>emrs/writeMedRecord/toEmrMainListView.action?menuAlias=${menuAlias }');
								}
							}else{
								if(node.id == 'merFirst'){//病案首页节点
									var cardId = pNode.attributes.inpatientNo;//住院号
									$('iframe').attr('src','<%=basePath%>emrs/emrFirst/toEmrFitstEditView.action?menuAlias=${menuAlias}&patId='+cardId+'&cardNo='+cardId);
								}else if(node.id == 'emrMaintenance'){//护理信息节点
									var nurInpatientNo = pNode.attributes.inpatientNo;
									$('iframe').attr('src','<%=basePath%>emrs/maintenance/toViewMaintenanceList.action?menuAlias=${menuAlias}&nurInpatientNo='+nurInpatientNo);
								}else{
									$('iframe').attr('src','<%=basePath %>emrs/writeMedRecord/toEmrMainListView.action?menuAlias=${menuAlias }');
								}
							}
						});
						
					}else if(frameFlag == 'Mainlist'){//电子病历列表界面
						if(node.id == 'merFirst'){//病案首页节点
							var cardId = pNode.attributes.inpatientNo;//住院号
							$('iframe').attr('src','<%=basePath%>emrs/emrFirst/toEmrFitstEditView.action?menuAlias=${menuAlias}&patId='+cardId+'&cardNo='+cardId);
						}else if(node.id == 'emrMaintenance'){//护理信息节点
							var nurInpatientNo = pNode.attributes.inpatientNo;
							$('iframe').attr('src','<%=basePath%>emrs/maintenance/toViewMaintenanceList.action?menuAlias=${menuAlias}&nurInpatientNo='+nurInpatientNo);
						}else{
							var obj=self.frames[0];
							obj.load(pNode.attributes.medicalrecordId,erType);
						}
					}else{
						if(node.id == 'merFirst'){//病案首页节点
							var cardId = pNode.attributes.inpatientNo;//住院号
							$('iframe').attr('src','<%=basePath%>emrs/emrFirst/toEmrFitstEditView.action?menuAlias=${menuAlias}&patId='+cardId+'&cardNo='+cardId);
						}else if(node.id == 'emrMaintenance'){//护理信息节点
							var nurInpatientNo = pNode.attributes.inpatientNo;
							$('iframe').attr('src','<%=basePath%>emrs/maintenance/toViewMaintenanceList.action?menuAlias=${menuAlias}&nurInpatientNo='+nurInpatientNo);
						}else{
							$('iframe').attr('src','<%=basePath %>emrs/writeMedRecord/toEmrMainListView.action?menuAlias=${menuAlias }');
						}
					}
				}
			});
		}
		/**
		* 刷新树
		* @author  yeguanqun
		* @date 2016-4-12 10:53
		* @version 1.0
		*/
		function refresh(){
			$('#tDt').tree('reload'); 
		}
		/**
		* 展开树
		* @author  yeguanqun
		* @date 2016-4-12 10:53
		* @version 1.0
		*/
		function expandAll(){
			$('#tDt').tree('expandAll');
		}
		/**
		* 关闭树
		* @author  yeguanqun
		* @date 2016-4-12 10:53
		* @version 1.0
		*/
		function collapseAll(){
			$('#tDt').tree('collapseAll');
		}
		
		/**
		* 刷新树
		* @author  yeguanqun
		* @date 2016-4-12 10:53
		* @version 1.0
		*/
		function refresh1(){
			$('#tDt1').tree('reload'); 
		}
		/**
		* 展开树
		* @author  yeguanqun
		* @date 2016-4-12 10:53
		* @version 1.0
		*/
		function expandAll1(){
			$('#tDt1').tree('expandAll');
		}
		/**
		* 关闭树
		* @author  yeguanqun
		* @date 2016-4-12 10:53
		* @version 1.0
		*/
		function collapseAll1(){
			var roots = $('#tDt1').tree('getChildren',$('#tDt1').tree('getRoots'));
			for(var i = 0; i < roots.length; i++){
				if(roots[i].attributes.level == 1){
					$('#tDt1').tree('collapse',roots[i].target);
				}
			}
		}
		
		//跳转添加方法 
		function add(){
			var nod = $('#tDt1').tree('getSelected');//模板类型树选中节点
			var node = $('#tDt').tree('getSelected');//患者树选中节点
			if(node == 'undefined' || node == null || node.id.length <= 1){
				$.messager.alert('提示',"请选择患者！！！");
				setTimeout(function(){$(".messager-body").window('close')},3500);
				return;
			}
			if(nod == null || nod == 'undefined'){
				$.messager.alert('提示',"请选择模板！！！");
				setTimeout(function(){$(".messager-body").window('close')},3500);
				return;
			}
			var patId = node.attributes.medicalrecordId;
			var clinic = '';
			if($('#emrSource').val() == '1'){
				clinic = node.attributes.clinicCode
			}else if($('#emrSource').val() == '2'){
				clinic = node.attributes.inpatientNo;
			}
			$('#emrClinic').val(clinic);
			var erType = getErType();
			Adddilog('选择模板',"<%=basePath %>emrs/writeMedRecord/toSelectTemplateView.action?menuAlias=${menuAlias}&erType="+erType+"&patId="+patId,'900px','600px');
		}
		/**
		* 提交审核
		*/
		function tick(ids){
			$.messager.progress({text:'提交中，请稍后...',modal:true});	// 显示进度条 
			$.ajax({
				url: "<%=basePath %>emrs/writeMedRecord/conformEmrMain.action",
					data:{'ids':ids, emrState:'1'},
					success: function(data) {
						$.messager.progress('close');
						if(data == 'success'){
							$.messager.alert('提示',"提交成功");
							iframe.window.reload();
						}
				}
			});
		}
		//跳转修改界面
		function edit(idd){
			$('iframe').attr('src','<%=basePath %>emrs/writeMedRecord/toEmrMainEditView.action?menuAlias=${menuAlias}&id='+idd);
		}
		//跳转修改界面
		function del(idd){
			$.ajax({
				url: "<%=basePath %>emrs/writeMedRecord/delEmrMain.action?ids="+idd,
					success: function(data) {
						if(data == 'success'){
							$.messager.alert('提示',"删除成功");
							$('iframe').attr('src','<%=basePath %>emrs/writeMedRecord/toEmrMainListView.action?menuAlias=${menuAlias}');
							treeUnSelect();
						}
				}
			});
			
		}
		var url = '';
		//跳转修改界面
		function save(strContent,iddOrErType,flag){
			var patId = $('#tDt').tree('getSelected').attributes.medicalrecordId;
			var tempId = $(window.frames["iframe"].document).find("#tempId").val();
			var clinic = $('#emrClinic').val();
			if(flag=='1'){
				url="<%=basePath %>emrs/writeMedRecord/editEmrMain.action?menuAlias=${menuAlias}&id="+iddOrErType;
			}else{
				url="<%=basePath %>emrs/writeMedRecord/addEmrMain.action?menuAlias=${menuAlias}&erType="+iddOrErType+"&patId="+patId +"&emrClinic="+clinic+"&tempId="+tempId;
			}
			$.ajax({
				url: url,
				type:"POST",
				data : {strContent : strContent},
				success: function(data) {
					if(data == 'success'){
						$('iframe').attr('src','<%=basePath %>emrs/writeMedRecord/toEmrMainListView.action?menuAlias=${menuAlias}');
						$.messager.alert('提示',"保存成功");
					}
				}
			});
			
		}
		
		//加载模式窗口
		function Adddilog(title, url, width, height) {
			$('#temWins').dialog({    
			    title: title,    
			    width: width,    
			    height: height,    
			    closed: false,    
			    cache: false,    
			    href: url,    
			    modal: true,
			   });   
		}
		//获得模板类型
		function getTreeType(node){
			if(node.id == 'merFirst'){
				if($('#tDt').tree('getSelected').attributes.pid == 'root'){
					$.messager.alert('提示',"请选择患者！！！");
					setTimeout(function(){$(".messager-body").window('close')},3500);
				}else{
					var patId = $('#tDt').tree('getSelected').attributes.inpatientNo;
					$('iframe').attr('src','<%=basePath%>emrs/emrFirst/toEmrFitstEditView.action?menuAlias=${menuAlias}&patId='+patId+'&cardNo='+patId);
				}
			}
		}
		/**
		 获得患者树选中节点
		 */
		function getPnode(){
			var node = $('#tDt').tree('getSelected');
			if(node == null || node == 'undefined' || node == ''){
				return '';
			}
			return node;
		}
		/**
		 *获得患者树住院号
		 */
		function getPaMeid(){
			var node = $('#tDt').tree('getSelected');
			if(node == null || node == 'undefined' || node == ''){
				return '';
			}
			if(node.attributes.pid == 'root'){
				return '';
			}
			return node.attributes.inpatientNo;
		}
		/**
		 *获得患者树病历号
		 */
		function getPaType(){
			var node = $('#tDt').tree('getSelected');
			if(node == null || node == 'undefined' || node == ''){
				return '';
			}
			if(node.attributes.pid == 'root'){
				return '';
			}
			return node.attributes.medicalrecordId;
		}
		//获得模板类型
		function getErType(){
			var node = $('#tDt1').tree('getSelected');
			var erType = "";
			if(node == null || node == 'undefined' || node == ''){
				return erType;
			}
			var level = node.attributes.level;
			if(level == '0'){
				erType="root";
			}else if(level == '1'){
				if(node.id == 'merFirst'){
					return node.id;
				}
				if(node.id == 'emrMaintenance'){
					return node.id;
				}
				var nodes = $('#tDt1').tree('getChildren',node.target);
				for(var i = 0;i < nodes.length-1; i++){
					erType += nodes[i].id + ",";
				}
				erType += nodes[nodes.length-1].id;
			}else if(level == '2'){
				erType = node.id;
			}
			return erType;
		}
		function treeUnSelect(){
			$('#tDt').find('.tree-node-selected').removeClass('tree-node-selected');
			$('#tDt1').find('.tree-node-selected').removeClass('tree-node-selected');
		}
</script>
<style type="text/css">
.panel-header{
	border-top:0
}
</style>
</head>
<body>
<div  id="divLayout" class="easyui-layout" fit=true> 
	<div id="p" data-options="region:'west',tools:'#toolSMId',split:true" title="患者管理" style="width: 20%; padding: 10px;">
    	<input type="hidden" id="emrSource" value="${emrSource}" />
		<input type="hidden" id="emrClinic" />
    	<div id="toolSMId">
					<a href="javascript:void(0)" class="icon-reload" onclick="refresh()" ></a>
					<a href="javascript:void(0)" class="icon-fold" onclick="collapseAll()"></a>
					<a href="javascript:void(0)" class="icon-open" onclick="expandAll()" ></a>
		</div> 
		<div id="treeDiv" style="width: 100%; height: 100%; overflow-y: auto;">
    		<ul id="tDt"></ul>
    	</div>
	</div>
	
	<div id="q" data-options="region:'center',tools:'#toolSMId1',split:true" title="病历分类" style="width: 15%;">
    	<div id="toolSMId1">
			<a href="javascript:void(0)" class="icon-reload" onclick="refresh1()" ></a>
			<a href="javascript:void(0)" class="icon-fold" onclick="collapseAll1()"></a>
			<a href="javascript:void(0)" class="icon-open" onclick="expandAll1()" ></a>
		</div> 
		<div id="treeDiv1" style="width: 100%; height: 100%; overflow-y: auto;">
    		<ul id="tDt1">数据加载中...</ul>
    	</div>
	</div>
	
	<div id="iframe" data-options="region:'east',border:false,split:true" style="width: 65%;">
		<iframe src="<%=basePath %>emrs/writeMedRecord/toEmrMainListView.action?menuAlias=${menuAlias }" name="iframe" id="iframe"
			style="width:100%; height:100%;float:right" frameborder=0 marginwidth=0 marginheight=0></iframe>
	</div>
</div>
<div id="temWins"></div>
</body>
</html>