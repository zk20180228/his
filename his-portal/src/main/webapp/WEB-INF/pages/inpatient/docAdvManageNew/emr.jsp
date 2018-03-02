<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>电子病历</title>
<%@ include file="/common/metas.jsp"%>
</head>
	<body>
		<div id="divLayoutEmr"  class="easyui-layout" data-options="fit:true,border:false">
			<div data-options="region:'west',split:true" style="width:300px;border-top:0;border-left:0">
				<ul id="emrTempTree">数据加载中...</ul>
			</div>
			<div id="emrContent" data-options="region:'center',border:true" style="border-top:0">
				<iframe  name="iframe" id="iframe"
					style="width:100%; height:100%;float:right" frameborder=0 marginwidth=0 marginheight=0></iframe>
			</div>
		</div>
		<div id="temWins"></div>
	<script type="text/javascript">
		$(function(){
			emrTempTreeTree();
			$('iframe').attr('src',src="<%=basePath %>emrs/writeMedRecord/toEmrMainListViewForInpatient.action?menuAlias=${menuAlias}");
			
		});
		//加载模板树
		function emrTempTreeTree(){
			$('#emrTempTree').tree({
				url : '<%=basePath %>emrs/emrTemplate/treeErTypeForInpatient.action',
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
				onClick : function(node) {//点击节点
					$(this).tree(node.state === 'closed' ? 'expand' : 'collapse', node.target);
					var pNode = $('#tDt').tree('getSelected');//患者树选中节点
					var node = $('#emrTempTree').tree('getSelected');//病历树选中节点
					if(pNode == null || pNode.attributes.pid == 'root'){
						$.messager.alert('提示',"请选择患者！！！");
						setTimeout(function(){$(".messager-body").window('close')},1500);
						return false;
					}
					var erType = getErType();//病理类型
					var frameFlag = $(window.frames["iframe"].document).find("#flag").val();//iframe
					var patId = pNode.attributes.medicalrecordId;//病历号
					if(frameFlag == 'editMain'){//电子病历编辑界面
						$.messager.confirm('确认', '是否保存当前病历？', function(res){//提示是否保存
							if (res){
								iframe.window.templateDesign.fnCheckForm('save');
							}else{
								if(node.id == 'emrMaintenance'){//护理信息节点
									var nurInpatientNo = pNode.attributes.inpatientNo;
									$('iframe').attr('src','<%=basePath%>emrs/maintenance/toViewMaintenanceListForInpatient.action?menuAlias=${menuAlias}&nurInpatientNo='+nurInpatientNo);
								}else{
									$('iframe').attr('src','<%=basePath %>emrs/writeMedRecord/toEmrMainListViewForInpatient.action?menuAlias=${menuAlias }');
								} 
							}
						});
					}else if(frameFlag == 'emrMaintenance'){//护理信息编辑界面
						$.messager.confirm('确认', '是否保存当前信息？', function(res){//提示是否保存
							if (res){
								iframe.window.submit();
							}
						});
						if(node.id == 'emrMaintenance'){//护理信息节点
							var nurInpatientNo = pNode.attributes.inpatientNo;
							$('iframe').attr('src','<%=basePath%>emrs/maintenance/toViewMaintenanceListForInpatient.action?menuAlias=${menuAlias}&nurInpatientNo='+nurInpatientNo);
						}else{
							$('iframe').attr('src','<%=basePath %>emrs/writeMedRecord/toEmrMainListViewForInpatient.action?menuAlias=${menuAlias }');
						}
					}else if(frameFlag == 'Mainlist'){//电子病历列表界面
						if(node.id == 'merFirst'){//病案首页节点
							var cardId = pNode.attributes.inpatientNo;//住院号
							$('iframe').attr('src','<%=basePath%>emrs/emrFirst/toEmrFitstEditViewForInpatient.action?menuAlias=${menuAlias}&patId='+cardId+'&cardNo='+cardId);
						}else if(node.id == 'emrMaintenance'){//护理信息节点
							var nurInpatientNo = pNode.attributes.inpatientNo;
							$('iframe').attr('src','<%=basePath%>emrs/maintenance/toViewMaintenanceListForInpatient.action?menuAlias=${menuAlias}&nurInpatientNo='+nurInpatientNo);
						}else{
							var obj=self.frames[0];
							obj.load(pNode.attributes.medicalrecordId,erType);
						}
					}else{
						if(node.id == 'merFirst'){//病案首页节点
							var cardId = pNode.attributes.inpatientNo;//住院号
							$('iframe').attr('src','<%=basePath%>emrs/emrFirst/toEmrFitstEditViewForInpatient.action?menuAlias=${menuAlias}&patId='+cardId+'&cardNo='+cardId);
						}else if(node.id == 'emrMaintenance'){//护理信息节点
							var nurInpatientNo = pNode.attributes.inpatientNo;
							$('iframe').attr('src','<%=basePath%>emrs/maintenance/toViewMaintenanceListForInpatient.action?menuAlias=${menuAlias}&nurInpatientNo='+nurInpatientNo);
						}else{
							$('iframe').attr('src','<%=basePath %>emrs/writeMedRecord/toEmrMainListViewForInpatient.action?menuAlias=${menuAlias }');
						}
					} 
				}
			});
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
		
		//跳转添加方法 
		function add(){
			var patId = getPaType();
			var erType = getErType();
			if(patId == ''){
				$.messager.alert('提示',"请选择患者！！！");
				setTimeout(function(){$(".messager-body").window('close')},1500);
				return;
			}
			if(erType == ''){
				$.messager.alert('提示',"请选择模板！！！");
				setTimeout(function(){$(".messager-body").window('close')},1500);
				return;
			}
			Adddilog('选择模板',"<%=basePath %>emrs/writeMedRecord/toSelectTemplateView.action?menuAlias=${menuAlias}&erType="+erType+"&patId="+patId,'900px','600px');
		}
		
		/**
		*修改电子病历
		*/
		function edit(idd){
			$('iframe').attr('src','<%=basePath %>emrs/writeMedRecord/toEmrMainEditViewForInpatient.action?menuAlias=${menuAlias}&id='+idd);
		}
		/**
		*删除电子病历
		*/
		function del(idd){
			$.ajax({
				url: "<%=basePath %>emrs/writeMedRecord/delEmrMain.action?ids="+idd,
				success: function(data) {
					if(data == 'success'){
						$.messager.alert('提示',"删除成功");
						$('iframe').attr('src','<%=basePath %>emrs/writeMedRecord/toEmrMainListViewForInpatient.action?menuAlias=${menuAlias}');
						treeUnSelect();
					}
				}
			});
			
		}
		var url = '';
		/**
		*保存电子病历
		*/
		function save(strContent,iddOrErType,flag){
			var patId = $('#tDt').tree('getSelected').attributes.medicalrecordId;
			var tempId = $(window.frames["iframe"].document).find("#tempId").val()
			var clinic = $('#tDt').tree('getSelected').attributes.inpatientNo;
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
						$('iframe').attr('src','<%=basePath %>emrs/writeMedRecord/toEmrMainListViewForInpatient.action?menuAlias=${menuAlias}');
						$.messager.alert('提示',"保存成功");
					}
				}
			});
			
		}
		
		/**
		*患者树节点
		*/
		function getPaType(){
			var node = $('#tDt').tree('getSelected');//病历树选中节点
			if(node != null && node.attributes.pid != 'root'){
				return node.attributes.medicalrecordId;
			}else{
				return '';
			}
		}
		/**
		*模板书节点
		*/
		function getErType(){
			var node = $('#emrTempTree').tree('getSelected');
			var erType = "";
			if(node == null || node == 'undefined' || node == ''){
				return erType;
			}
			var level = node.attributes.level;
			if(level == '0'){
				erType="root";
			}else if(level == '1'){
				if(node.id == 'emrMaintenance'){
					return node.id;
				}
				var nodes = $('#emrTempTree').tree('getChildren',node.target);
				for(var i = 0;i < nodes.length-1; i++){
					erType += nodes[i].id + ",";
				}
				erType += nodes[nodes.length-1].id;
			}else if(level == '2'){
				erType = node.id;
			}
			return erType;
		}
		
		/**
		*加载模式窗口
		*/
		function getInpatientNo(){
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
		*加载模式窗口
		*/
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
	</script>
	</body>
</html>