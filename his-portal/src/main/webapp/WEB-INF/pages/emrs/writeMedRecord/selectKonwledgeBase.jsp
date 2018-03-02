<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>

<%@ include file="/common/taglibs.jsp" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>在线知识库</title>
<base href="<%=basePath%>">
	<%@ include file="/common/metas.jsp" %>
</head>
<body>
	<div class="easyui-layout" data-options="fit:true">
		<div data-options="region:'north',border:'true'"  style="width: 100%;height: 10%">
			<dir>
				<input type="radio" name="konwLib" value='01' checked="checked" onclick="to_change()">治愈好转标准库
				<input type="radio" name="konwLib" value='02' onclick="to_change()">&nbsp;医疗护理技术操作常规
				<input type="radio" name="konwLib" value='03' onclick="to_change()">&nbsp;药品库
				<input type="radio" name="konwLib" value='04' onclick="to_change()">&nbsp;法律规范库
				<input type="radio" name="konwLib" value='05' onclick="to_change()">&nbsp;鉴别诊断知识库
				<input type="radio" name="konwLib" value='06' onclick="to_change()">&nbsp;&nbsp;诊疗计划知识库
			</dir>
		</div>
		<div data-options="region:'west',border:'true'"  style="height: 85%;width: 35%">
				<ul id="tKonw">数据加载中...</ul>
		</div>
		<div id="divLhmMessage" data-options="region:'center',border:'true'"  style="width: 65%">
		</div>
	</div>
	<script type="text/javascript">
	
	//加载页面
	$(function(){
		trr('01');
		
		
	});
	
	//知识库树
	function trr(lib){
		$('#tKonw').tree({
				url : '<%=basePath %>emrs/konwledgeBase/treeKonw.action?lib='+lib,
				method:'get',
				lines : true,
				cache : false,
				animate : true,
				formatter : function(node) {//统计节点总数
					var s = node.text;
					if (node.children) {
						s += '&nbsp;<span style=\'color:blue\'>('
							+ node.children.length + ')</span>';
					}
					return s;
				},onLoadSuccess: function(node, data) {
					var root = $('#tKonw').tree('getRoot',node);
					$('#tKonw').tree('select', root.target); 
				},onClick : function(node) {//点击节点
					var idd = node.id;
					var flag = node.attributes.flag;
					if(flag == '0'){
						document.getElementById("divLhmMessage").innerHTML = '';
						return;
					}else{
						$.ajax({
							url: '<%=basePath %>emrs/konwledgeBase/queryKonw.action?idd='+idd,
								success: function(data) {
									var content = '<p style="text-align: center;"><span style="font-size: 25px;">郑州大学第一附属医院</span></p><hr style=" height:2px;border:none;border-top:2px solid #000000;" /><br>'
									+ data.strContent;
									document.getElementById("divLhmMessage").innerHTML = content;
							}
						});
					}
				}
			});
		}
	
	var konwLib ="";
	function to_change(){
		var obj  = document.getElementsByName('konwLib');
		for(var i = 0;i < obj.length;i++){
			if(obj[i].checked == true){
				if(obj[i].value != konwLib){
					document.getElementById("divLhmMessage").innerHTML = '';
					konwLib = obj[i].value;
					trr(konwLib);
				}
			}
		}
	}
	/* 
	 * 关闭界面 并给编辑器赋值
	 */
	function closeLayout(content){
		$('#temWins').dialog('close'); 
	}
	</script>
</body>