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
<%-- 这里注释掉这一行,是因为前台报错需要使用的话就放开<script type="text/javascript" src="<%=basePath%>easyui1.4.5/custom_js/home_page.js"></script> --%>
<script type="text/javascript" src="<%=basePath%>javascript/js/keydown.js"></script>
<script src="${pageContext.request.contextPath}/javascript/echarts/echarts.js"></script>
<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
<script type="text/javascript">
	$(function() {
		$('#deptCode').combobox({
			url: '<%=basePath%>publics/consultation/queryDeptList.action?menuAlias=${menuAlias}',
			valueField: 'deptCode',
			textField: 'deptName',
			multiple: false,
			editable: true,
			required: true,
			mode: 'remote',
		});
	})
	function open(url,title){
		if (window.parent.$('#tabs').tabs('exists',title)){
			window.parent.$('#tabs').tabs('select', title);//选中并刷新
			var currTab = window.parent.$('#tabs').tabs('getSelected');
			if (url != undefined) {
				window.parent.$('#tabs').tabs('update', {
					tab : currTab,
					options : {
						content : createFrame(url)
					}
				});
			}
		} else {
	// 		var content = '<iframe scrolling="auto" frameborder="0"  src="'+url+'" style="width:100%;height:100%;"></iframe>';
			window.parent.$('#tabs').tabs('add',{
				title : title,
				content:createFrame(url),
				closable:true
			});
			window.parent.tabClose();
		}
	}
	
	function searchFrom(){
		
	}
	
	function save(){
		
	}
		</script>
	</head>

	<body class="easyui-layout" data-options="fit:true">
		<div id="cc" class="easyui-layout" style="width:100%;height:100%;">
			<div id = "leftBox" data-options="region:'west',title:'待审核转诊患者列表',split:false" style="width:30%;">

				<div style="width: 100%;height: 35px;padding-top: 5px;">
					<div style="margin-left: 10px;">
						<input class="easyui-textbox" data-options="prompt:'医疗机构名称'" style="width:35%" /> &nbsp;
						<input id="combo" class="easyui-combobox" style="width:25%;margin-left:10px" data-options="valueField:'id',textField:'text',data: [{id: '1',text: '全部',selected:true },{id: '2',text: '三甲'},{id: '3',text: '三级'},{id: '4',text: '二级'}]" /> &nbsp;
						<a href="javascript:void(0)" onclick="searchFrom()" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
					</div>
				</div>

				<div data-options="region:'center',border:false" style="height:calc(100% - 40px);">
					<table class="easyui-datagrid" id="list">
						<thead>
							<tr>
								<th data-options="field:'check',width:'18%',align:'center'">患者姓名</th>
								<th data-options="field:'treatment',width:'10%',align:'center'">年龄</th>
								<th data-options="field:'radiation',width:'18%',align:'center'">医疗机构</th>
								<th data-options="field:'test',width:'18%',align:'center'">优先级</th>
								<th data-options="field:'blood',width:'18%',align:'center'">健康状态</th>
								<th data-options="field:'other',width:'18%',align:'center'">审核状态</th>
							</tr>
						</thead>
					</table>
				</div>
			</div>
			<div data-options="region:'center'" style="height:100%">

				<form id="nuilfcheditForm" method="post" style="width: 100%;height:100%">
						<table class="honry-table" id="nuilfcheditTable" cellpadding="1" cellspacing="1" border="1px solid black" style="width: 100%;height:100%">
							<tr>
								<td align="center" colspan="6" style="height:60px">
									<font size="3" class="empWorkTit">转诊审批</font>
								</td>
							</tr>
							<tr>
								<td class="honry-lable" style="height:55px">申请医疗机构:</td>
								<td colspan="3"><input id="name" name="name" class="easyui-textbox"></td>
								<td class="honry-lable">机构编号:</td>
								<td><input id="name" name="name" class="easyui-textbox"></input>
									</input>
								</td>
							</tr>
							<tr>
								<td class="honry-lable" style="height:55px">住院流水号/门诊号:</td>
								<td>
									<input id="patientName" class="easyui-textbox" readonly="readonly" />
								</td>
								<td class="honry-lable">患者姓名:</td>
								<!-- 		    			<td ><input id="name" name="name"   type="text" style="border: 0; width: 70px" ></input></td> -->
								<td>
									<input id="patientName" class="easyui-textbox" readonly="readonly" />
								</td>
								<td class="honry-lable">性别:</td>
								<td><input id="reportSex" class="easyui-textbox" readonly="readonly" /></td>
							</tr>
							<tr>
								<td class="honry-lable" style="height:55px">年龄:</td>
								<td>
									<input id="reportAge" class="easyui-textbox" readonly="readonly" />
									<span id="reportAgeunit" readonly="readonly"></span>
								</td>
								<td class="honry-lable">出生日期:</td>
								<td colspan="3"><input id="moStdt" name="moStdt" class="Wdate" type="text" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" style="width:150px;border: 1px solid #95b8e7;border-radius: 5px;" /></td>
							</tr>
							<tr>
								<td class="honry-lable">诊断：</td>
								<td colspan="5">
									<input id="cnslNote" name="cnslNote" class="easyui-textbox" data-options="multiline:true" style="width:75%;height:100px">
								</td>
							</tr>
							<tr>
								<td class="honry-lable">病情描述：</td>
								<td colspan="5">
									<input id="cnslNote" name="cnslNote" class="easyui-textbox" data-options="multiline:true" style="width:75%;height:100px">
								</td>
							</tr>
							<tr>
								<td class="honry-lable">备注：</td>
								<td colspan="5">
									<input id="cnslNote" name="cnslNote" class="easyui-textbox" data-options="multiline:true" style="width:75%;height:100px">
								</td>
							</tr>
							<tr>
								<td align="center" colspan="2" style="height:55px">
									<a href="javascript:open('<%=basePath%>emrs/emrFirst/listEmrFitst.action?menuAlias=EMRS-BASY','病案首页');" >病案首页</a>
								</td>
								<td align="center">
									<a href="javascript:open('<%=basePath%>emrs/emrTemplate/toViewTemplateList.action','电子病历');">电子病历</a>
								</td>
								<td align="center">
									<a>医嘱信息</a>
								</td>
								<td align="center" colspan="2">
									<a href="javascript:open('<%=basePath%>nursestation/nuilfch/listNuilfch.action ','生命体征信息');">生命体征信息</a>
								</td>
							</tr>
							<tr>
								<td class="honry-lable" style="height:55px">申请科室:</td>
								<td><input id="patientName" class="easyui-textbox" readonly="readonly" /></td>
								<td class="honry-lable">申请医生:</td>
								<td><input id="patientName" class="easyui-textbox" readonly="readonly" /></td>
								<td class="honry-lable">联系方式:</td>
								<td><input id="patientName" class="easyui-textbox" readonly="readonly" /></td>
							</tr>
							<tr>
								<td class="honry-lable" style="height:55px">申请时间:</td>
								<td><input id="moStdt" name="moStdt" class="Wdate" type="text" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" style="width:175px;border: 1px solid #95b8e7;border-radius: 5px;" /></td>
								<td class="honry-lable">转诊至科室:</td>
								<td colspan="3"><input id="deptCode" name="deptCode" class="easyui-combobox" data-options="required:true" />
									<a href="javascript:delSelectedData('deptCode,docCode');" class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a>
								</td>
							</tr>
						</table>
						<div align="center" style="margin-top:20px">
							<shiro:hasPermission name="${menuAlias}:function:save">
								<a href="javascript:void(0)" id="savesave" class="easyui-linkbutton" onclick="save()" data-options="iconCls:'icon-save'">确&nbsp;认&nbsp;</a>
							</shiro:hasPermission>
						<div>
				</form>
				</div>
				</div>
	</body>

</html>