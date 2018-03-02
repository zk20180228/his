<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp" %>
<%@ include file="/common/metas.jsp" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	
	<head>
		<title>mongo预处理</title>
		<style type="text/css">
			*{
				box-sizing: border-box;
			}
			 #Mytabs .panel-body-noheader{
			 	height: 100% !important;
			 }
			 #MytabsTop {
			 	height:40px;
			 	padding: 5px;
			 	position: absolute;
			 	top: 0;
			 	left: 0;
			 }
			 #MytabsBottom{
				 width: 100%;
				 height: 100%;
				 padding-top: 45px;
			 }
			 .window-mask{
			 	height: 100% !important;
			 	width: 100% !important;
			 }
			 #tool4 .textbox.combo{
			 	float: left;
			 	margin:0 5px;
			 }
		</style>
	</head>

	<body style="margin: 0px;padding: 0px;">
	<div id = "MytabsTop"  >
		<input id="sName" class="easyui-textbox" data-options="prompt:'栏目别名'" style="width:150px;"/>
		<shiro:hasPermission name="${menuAlias}:function:query">
			<a href="javascript:void(0)" onclick="searchFrom()" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
		</shiro:hasPermission>
		<a href="javascript:void(0)" onclick="searchClears()" class="easyui-linkbutton" iconCls="reset">重置</a>
	</div>
	<div id = "MytabsBottom"   >
		<table id="DataMainTable" border="1" cellspacing="0" cellpadding="0" style="width: 100%;text-align: center;"></table>
		<div id="logwindow" class="easyui-dialog" title="查看日志" data-options="modal:true,closed:true" style="width:60%;height:75%;overflow: visible;">
			<div id="logwindowInfo" style="margin-top: -20px;height: 100%">
				<table id="DatalogTable" border="1" cellspacing="0" cellpadding="0" overflow: visible;></table>
				<div id="tool" style="text-align: left;padding: 10px 0;">
					<input class="easyui-combobox" id="logWinCombo" name="" value="" style="width:120px"/>
					<input class="easyui-combobox" id="logWinStateCombo" name="" value="" style="width:120px"/>
					<input id="firstDate" class="Wdate" type="text" onClick="WdatePicker({maxDate:'#F{$dp.$D(\'endDate\')}'})" style="width:150px;border: 1px solid #95b8e7;border-radius: 5px;" /> 至
					<input id="endDate" class="Wdate" type="text" onClick="WdatePicker({minDate:'#F{$dp.$D(\'firstDate\')}'})" style="width:150px;border: 1px solid #95b8e7;border-radius: 5px;" />
					<a href="javascript:retrievalLog()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
				</div>
			</div>
		</div>
		<div id="reslutwindow" class="easyui-dialog" title="查看结果" data-options="modal:true,closed:true" style="width:60%;height:75%;overflow: visible;">
			<div id="reslutwindowInfo"  style="margin-top: -20px;height: 100%">
				<table class="easyui-datagrid" id="DatareslutTable" border="1" cellspacing="0" cellpadding="0" overflow: visible;></table>
				<div id="tool2" style="text-align: left;padding: 10px 0;">
					<input class="easyui-combobox" id="resultWinCombo" name="" value="" style="width:120px"/>
					<input id="resultFirstDate" class="Wdate" type="text" onClick="WdatePicker({maxDate:'#F{$dp.$D(\'resultEndDate\')}'})" style="width:150px;border: 1px solid #95b8e7;border-radius: 5px;" /> 至
					<input id="resultEndDate" class="Wdate" type="text" onClick="WdatePicker({minDate:'#F{$dp.$D(\'resultFirstDate\')}'})" style="width:150px;border: 1px solid #95b8e7;border-radius: 5px;" />
					<a href="javascript:searchResult()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
					<a href="javascript:reCalculate()" class="easyui-linkbutton" iconCls="icon-recalculate">重新计算</a>
				</div>
				<div style="text-align:center;">
					<textarea id="textareaL" rows="12" cols="112"></textarea>
				</div>
			</div>
		</div>
		<div id="pretreatwindow" class="easyui-dialog" title="操作" data-options="modal:true,closed:true" style="width:40%;height:55%;overflow: visible;">
			<form>
				<table class="honry-table" id="DatapreTable" cellpadding="0" cellspacing="0" border="0"  style="width:100%;padding:5px">
					<tr>
						<td class="honry-lable">类别:</td>
						<td>
							<input class="easyui-combobox" id="pretreatWinCombo" name="" value="" style="width:120px"/>
						</td>
					</tr>
					<tr>
						<td class="honry-lable">开始时间:</td>
						<td>
							<input id="pretreatFirstDate" class="Wdate daySta setOperation" type="text" onClick="WdatePicker({ dateFmt:'yyyy-MM-dd',maxDate:'#F{$dp.$D(\'pretreatEndDate\')}'})" style="width:150px;border: 1px solid #95b8e7;border-radius: 5px;" />
							<input class="Wdate monthSta setOperation" type="text" onClick="WdatePicker({dateFmt:'yyyy-MM',maxDate:'#F{$dp.$D(\'pretreatEndDate\')}'})" style="width:150px;border: 1px solid #95b8e7;border-radius: 5px;display: none;" /> 
							<input class="Wdate yeatSta setOperation" type="text" onClick="WdatePicker({dateFmt:'yyyy',maxDate:'#F{$dp.$D(\'pretreatEndDate\')}'})" style="width:150px;border: 1px solid #95b8e7;border-radius: 5px;display: none;" /> 
						</td>
					</tr>
					<tr>
						<td class="honry-lable">结束时间:</td>
						<td>
							<input id="pretreatEndDate" class="Wdate dayEnd setOperation" type="text" onClick="WdatePicker({ dateFmt:'yyyy-MM-dd' ,minDate:'#F{$dp.$D(\'pretreatFirstDate\')}'})" style="width:150px;border: 1px solid #95b8e7;border-radius: 5px;" />
							<input class="Wdate monthEnd setOperation" type="text" onClick="WdatePicker({dateFmt:'yyyy-MM',minDate:'#F{$dp.$D(\'pretreatFirstDate\')}'})" style="width:150px;border: 1px solid #95b8e7;border-radius: 5px;display: none;" />
							<input class="Wdate yeatEnd setOperation"  type="text" onClick="WdatePicker({dateFmt:'yyyy',minDate:'#F{$dp.$D(\'pretreatFirstDate\')}'})" style="width:150px;border: 1px solid #95b8e7;border-radius: 5px;display: none;" />
						</td>
					</tr>
				</table>
			</form>
				<div id="tool3" style="text-align: center;padding: 10px 0;">
					<a href="javascript:pretreatment()" class="easyui-linkbutton" iconCls="icon-save">确定</a>
					<a href="javascript:$('#pretreatwindow').window('close');" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'">关闭</a>
				</div>
			<div id="pretreatwindowInfo"  style="margin-top: -20px;height: 100%"></div>
		</div>
		<div id="settingwindow" class="easyui-dialog" title="设置" data-options="modal:true,closed:true" style="width:30%;height:40%;overflow: visible;">
			<div id="Mytabs" class="easyui-tabs" style="width:100%;height:100%;margin-top: -20px;"></div>
		</div>
		<div id="handwindow" class="easyui-dialog" title="查看预处理操作结果" data-options="modal:true,closed:true" style="width:60%;height:75%;overflow: visible;">
			<div id="handwindowInfo"  style="margin-top: -20px;height: 100%">
				<table class="easyui-datagrid" id="handTable" border="1" cellspacing="0" cellpadding="0" overflow: visible;></table>
				<div id="tool4" style="text-align: left;padding: 10px 0;">
					<input class="easyui-combobox" id="handWinCombo" name="" value="" style="width:120px;float: left;"/>
					<input class="easyui-combobox" id="handWinComboState" name="" value="" style="width:120px;float: left;"/>
					<input id="handFirstDate" class="Wdate handFirstdaySta handDate" type="text" onClick="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate:'#F{$dp.$D(\'handEndDate\')}'})" style="width:150px;border: 1px solid #95b8e7;border-radius: 5px;;float: left;" />
					<input class="Wdate handFirstmonthSta handDate" type="text" onClick="WdatePicker({dateFmt:'yyyy-MM',maxDate:'#F{$dp.$D(\'handEndDate\')}'})" style="width:150px;border: 1px solid #95b8e7;border-radius: 5px;display: none;;float: left;" />
					<input class="Wdate handFirstyeaysSta handDate" type="text" onClick="WdatePicker({dateFmt:'yyyy',maxDate:'#F{$dp.$D(\'handEndDate\')}'})" style="width:150px;border: 1px solid #95b8e7;border-radius: 5px;display: none;float: left;" />
					<span style="float: left;margin: 0 5px;">至</span> 
					<input id="handEndDate" class="Wdate handDate handFirstdayEnd" type="text" onClick="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'handFirstDate\')}'})"  style="width:150px;border: 1px solid #95b8e7;border-radius: 5px;;float: left;" />
					<input class="Wdate handDate handFirstmonthEnd" type="text" onClick="WdatePicker({dateFmt:'yyyy-MM',minDate:'#F{$dp.$D(\'handFirstDate\')}'})"  style="width:150px;border: 1px solid #95b8e7;border-radius: 5px;display: none;float: left;" />
					<input class="Wdate handDate handFirstyeaysEnd" type="text" onClick="WdatePicker({dateFmt:'yyyy',minDate:'#F{$dp.$D(\'handFirstDate\')}'})"  style="width:150px;border: 1px solid #95b8e7;border-radius: 5px;display: none;float: left;" />
					<a style="margin-left: 5px;" href="javascript:searchHand()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
				</div>
			</div>
		</div>
		<div id="usertoolbarId">
			<shiro:hasPermission name="${menuAlias}:function:add">
				<a  href="javascript:btnAdd();" class="easyui-linkbutton" data-options="iconCls:'icon-add' ,plain:true">添加</a>
			</shiro:hasPermission>
			<shiro:hasPermission name="${menuAlias}:function:edit">
				<a  href="javascript:btnEdit();" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true">修改</a>
			</shiro:hasPermission>
			<shiro:hasPermission name="${menuAlias}:function:delete">
				<a  href="javascript:btnDelete();" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true">删除</a>
			</shiro:hasPermission>
			<div id="dialogDivId1"></div>
		</div>
	</div>
	</body>
	<script type="text/javascript">
	var menuMap = "";
		$(function() {
			$('#logwindow').dialog('close')
			$('#reslutwindow').dialog('close')
			$('#settingwindow').dialog('close')
			$.ajax({
				url:'<%=basePath%>sys/pretreatment/getMenuMap.action',
				success:function(data){
					menuMap = data;
				}
			});
				$('#DataMainTable').datagrid({
					rownumbers: true,
					pageSize: "20",
					fit:true,
					singleSelect:true,
					pageList: [10, 20, 30, 50, 80, 100],
					pagination: true,
					method: "post",
					url: '<%=basePath%>sys/pretreatment/getMongoCount.action',
					toolbar:'#usertoolbarId',
					columns: [
						[{
								field: 'id',
								hidden: true
							},
							{
								field: 'menuTypeName',
								title: '栏目名称',
								width: "11%",
								align: 'center'
							},
							{
								field: 'startTime',
								title: '开始日期',
								width: "11%",
								align: 'center'
							},
							{
								field: 'endTime',
								title: '结束日期',
								width: "11%",
								align: 'center'
							},
							{
								field: 'setting',
								title: '执行方式',
								width: "8%",
								align: 'center',
								formatter: settingfn
							},
							{
								field: 'reslut',
								title: '结果',
								width: "8%",
								align: 'center',
								formatter: reslutfn
							},
							{
								field: 'log',
								title: '日志',
								width: "8%",
								align: 'center',
								formatter: logfn
							},
							{
								field: 'state',
								title: '状态',
								width: "8%",
								align: 'center',
								formatter: formatterState
							},
							{
								field: 'oper',
								title: '操作',
								width: "11%",
								align: 'center',
								formatter: operfn
							},
							{
								field: 'view',
								title: '预处理操作查看',
								width: "11%",
								align: 'center',
								formatter: handfn
							}
						]
					]
				});
			//日志窗口下拉
			$('#logWinCombo').combobox({
				valueField: 'id',
				textField: 'text',
				data:[{
					'id' : 3,
					'text' : '年'
				},{
					'id' : 2,
					'text' : '月'
				},{
					'id' : 1,
					'text' : '日'
				}]
			});
			//状态窗口下拉
			$('#logWinStateCombo').combobox({
				valueField: 'id',
				textField: 'text',
				data:[{
					'id' : 1,
					'text' : '成功'
				},{
					'id' : 0,
					'text' : '失败',
					"selected":true
				}]
			});
			$('#handWinComboState').combobox({
				valueField: 'id',
				textField: 'text',
				data:[{
					'id' : 2,
					'text' : '执行中'
				},{
					'id' : 1,
					'text' : '执行成功'
				},{
					'id' : 0,
					'text' : '执行失败',
					"selected":true
				}]
			});
			//结果窗口下拉
			$('#resultWinCombo').combobox({
				valueField: 'id',
				textField: 'text',
				data:[{
					'id' : 3,
					'text' : '年'
				},{
					'id' : 2,
					'text' : '月'
				},{
					'id' : 1,
					'text' : '日',
					"selected":true
				}],
				onSelect : function(record){
					//动态改变日期控件格式
					if(record.id==3){
						$('#resultFirstDate').unbind('foucs');
						$('#resultEndDate').unbind('foucs');
						$('#resultFirstDate').bind('focus',function(){
							WdatePicker({dateFmt:'yyyy'});
						});
						$('#resultEndDate').bind('focus',function(){
							WdatePicker({dateFmt:'yyyy'});
						});
					}
					if(record.id==2){
						
						$('#resultFirstDate').unbind('foucs');
						$('#resultEndDate').unbind('foucs');
						$('#resultFirstDate').bind('focus',function(){
							WdatePicker({dateFmt:'yyyy-MM'});
						});
						$('#resultEndDate').bind('focus',function(){
							WdatePicker({dateFmt:'yyyy-MM'});
						});
					}
					if(record.id==1){
						$('#resultFirstDate').unbind('foucs');
						$('#resultEndDate').unbind('foucs');
						$('#resultFirstDate').bind('focus',function(){
							WdatePicker({dateFmt:'yyyy-MM-dd'});
						});
						$('#resultEndDate').bind('focus',function(){
							WdatePicker({dateFmt:'yyyy-MM-dd'});
						});
					}
				}
			});
			//结果窗口下拉
			$('#handWinCombo').combobox({
				valueField: 'id',
				textField: 'text',
				data:[{
					'id' : 3,
					'text' : '年'
				},{
					'id' : 2,
					'text' : '月'
				},{
					'id' : 1,
					'text' : '日',
					"selected":true
				}],
				onSelect : function(record){
					if(record.id==1){
						$(".handDate").css("display","none")
						$("#handFirstDate").val("").attr("id","")
						$("#handEndDate").val("").attr("id","")
						$(".handFirstdaySta").attr("id","handFirstDate").css("display","block")
						$(".handFirstdayEnd").attr("id","handEndDate").css("display","block")
					}
					if(record.id==2){
						$(".handDate").css("display","none")
						$("#handFirstDate").val("").attr("id","")
						$("#handEndDate").val("").attr("id","")
						$(".handFirstmonthSta").attr("id","handFirstDate").css("display","block")
						$(".handFirstmonthEnd").attr("id","handEndDate").css("display","block")
					}
					if(record.id==3){
						$(".handDate").css("display","none")
						$("#handFirstDate").val("").attr("id","")
						$("#handEndDate").val("").attr("id","")
						$(".handFirstyeaysSta").attr("id","handFirstDate").css("display","block")
						$(".handFirstyeaysEnd").attr("id","handEndDate").css("display","block")
					}
				}
			});
			$('#pretreatWinCombo').combobox({
				valueField: 'id',
				textField: 'text',
				data:[{
					'id' : 3,
					'text' : '年'
				},{
					'id' : 2,
					'text' : '月'
				},{
					'id' : 1,
					'text' : '日',
					"selected":true
				}],
				onSelect : function(record){
					//动态改变日期控件格式
					if(record.id==1){
						$(".setOperation").css("display","none")
						$("#pretreatFirstDate").val("").attr("id","")
						$("#pretreatEndDate").val("").attr("id","")
						$(".daySta").attr("id","pretreatFirstDate").css("display","block")
						$(".dayEnd").attr("id","pretreatEndDate").css("display","block")
					}
					if(record.id==2){
						$(".setOperation").css("display","none")
						$("#pretreatFirstDate").val("").attr("id","")
						$("#pretreatEndDate").val("").attr("id","")
						$(".monthSta").attr("id","pretreatFirstDate").css("display","block")
						$(".monthEnd").attr("id","pretreatEndDate").css("display","block")
					}
					if(record.id==3){
						$(".setOperation").css("display","none")
						$("#pretreatFirstDate").val("").attr("id","")
						$("#pretreatEndDate").val("").attr("id","")
						$(".yeatSta").attr("id","pretreatFirstDate").css("display","block")
						$(".yeatEnd").attr("id","pretreatEndDate").css("display","block")
					}
				}
			});
			$('#Mytabs').tabs({
				border: false,
				onSelect: function(title) {}
			});
			$('#handTable').datagrid({
				rownumbers: true,
				pageSize: "20",
				pageList: [10, 20, 30, 50, 80, 100],
				pagination: true,
				fit:true,
				toolbar: '#tool4',
				columns: [
					[{
							field: 'id',
							hidden: true
						},{
							field: 'menuAlias',
							title: '栏目名称',
							width: "15%",
							formatter: formatterMenuYMD,
						}, 
						{
							field: 'startTime',
							title: '开始时间',
							width: "12.5%",
							align: 'center',
						}, {
							field: 'endTime',
							title: '结束时间',
							width: "12.5%",
							align: 'center',
						},
						{
							field: 'type',
							title: '类型',
							width: "12.5%",
							align: 'center',
							formatter : formatterYMD
						}, {
							field: 'state',
							title: '状态',
							width: "12.5%",
							align: 'center',
							formatter: fmtStat
						}, {
							field: 'handWay',
							title: '处理方式',
							width: "12.5%",
							align: 'center',
							formatter: fmthandWay
						}
					]
				]
			})
			$('#DatalogTable').datagrid({
				rownumbers: true,
				pageSize: "20",
				pageList: [10, 20, 30, 50, 80, 100],
				pagination: true,
				fit:true,
				toolbar: '#tool',
				columns: [
					[{
							field: 'id',
							hidden: true
						},{
							field: 'menuType',
							title: '栏目名称',
							width: "12.5%",
							formatter: formatterMenuYMD,
						}, 
						{
							field: 'startTime',
							title: '开始时间',
							width: "12.5%",
							align: 'center',
						}, {
							field: 'endTime',
							title: '结束时间',
							width: "12.5%",
							align: 'center',
						},
						{
							field: 'countStartTime',
							title: '计算开始时间',
							width: "12.5%",
							align: 'center',
						},
						{
							field: 'countEndTime',
							title: '计算结束时间',
							width: "12.5%",
							align: 'center',
						},
						{
							field: 'executeTime',
							title: '执行时间',
							width: "10%",
							align: 'center',
							formatter: fmtexecuteTime
						},
						{
							field: 'totalNum',
							title: '总条数',
							width: "10%",
							align: 'center',
						}, {
							field: 'state',
							title: '状态',
							width: "10%",
							align: 'center',
							formatter: fmtLogStat
						}
					]
				]
			})
			$("#DatareslutTable").datagrid({
				toolbar: '#tool2',
			})

			//设置标题
			$('#Mytabs').parent().prepend("<div id = 'titleInfo' style='font-size: 16px;text-align: center;position: relative;top: -25px;height: 21px;width: calc(100% - 20px);'></div>")
			$("#logwindowInfo").parent().prepend("<div id = 'resuittitleInfo' style='font-size: 16px;text-align: center;position: relative;top: -25px;height: 21px;width: calc(100% - 20px);'>aaaa</div>")
			$("#reslutwindowInfo").parent().prepend("<div id = 'selecttitleInfo' style='font-size: 16px;text-align: center;position: relative;top: -25px;height: 21px;width: calc(100% - 20px);'>aaaa</div>")
			$("#pretreatwindowInfo").parent().prepend("<div id = 'preInfo' style='font-size: 16px;text-align: center;position: relative;top: -25px;height: 21px;width: calc(100% - 20px);'>aaaa</div>")
			$("#handwindowInfo").parent().prepend("<div id = 'handtitleInfo' style='font-size: 16px;text-align: center;position: relative;top: -25px;height: 21px;width: calc(100% - 20px);'>aaaa</div>")

			function settingfn(value, row, index) {
				var exeTime = row.executeTime;
				var val = "年";
				if(exeTime==3){
					val = "年";
				}else if(exeTime==2){
					val = "月";
				}else{
					val = "日";
				}
				
				return "<input  type='button' value='设置' style='width: 60%;'onclick='settingclick(" + index + ",\"年月日\",\"" + row.munyType + "\",\"" + row.id + "\")' />"
			}

			function reslutfn(value, row, index) {
				return "<input type='button' value='查看' style='width: 60%;'onclick='reslutclick(" + index + ",\"" + row.munyType + "\",\"" + row.executeTime + "\")' />"
			}

			function logfn(value, row, index) {
				return "<input type='button' value='查看' style='width: 60%;' onclick='logclick(" + index + ",\"" + row.munyType + "\",\"" + row.id + "\")' />"
			}
			function operfn(value, row, index) {
				return "<input type='button' value='执行预处理' style='width: 60%;' onclick='preclick(" + index + ",\"" + row.munyType + "\",\"" + row.id + "\")' />"
			}
			function handfn(value, row, index) {
				return "<input type='button' value='处理状态' style='width: 60%;' onclick='handclick(" + index + ",\"" + row.munyType + "\",\"" + row.id + "\")' />"
			}
		})
		function preclick(index,type,id){
			$('#pretreatwindow').dialog('open')
			var val = $.trim(type);
			type = menuMap[val];
			$('#preInfo').html("栏目名称：" + type)
		}
		function handclick(index,type,id){
			$('#handwindow').dialog('open')
			var val = $.trim(type);
			var name = menuMap[val];
			$('#handtitleInfo').html("栏目名称：" + name)
			var type = $('#handWinCombo').combobox('getValue');
			var state = $('#handWinComboState').combobox('getValue');
			var firstDate = $('#handFirstDate').val();
			var endDate = $('#handEndDate').val();
			$('#handTable').datagrid({
				url:'<%=basePath%>sys/pretreatment/getHandState.action',
				queryParams:{startTime:firstDate,endTime:endDate,menuType:val,type:type,state:state}
			});
		}
		function logclick(index, name, id) {
			var endDate = $('#endDate').val();
			var firstDate = $('#firstDate').val();
			var ymd = $('#logWinCombo').combobox('getValue');
			var state = $('#handWinComboState').combobox('getValue');
			$('#DatalogTable').datagrid({
				url:'<%=basePath%>sys/pretreatment/getMongoLog.action',
				queryParams:{startTime:firstDate,endTime:endDate,menuType:name,ymd:ymd,state:state}
			});
			$('#logwindow').dialog('open')
			var val = $.trim(name);
			name = menuMap[val];
			$('#resuittitleInfo').html("栏目名称：" + name)
		}
		
		function reslutclick(index, name, executeTime) {
			var ymd = $('#resultWinCombo').combobox('getValue');
			var resultFirstDate = $('#resultFirstDate').val();
			var resultEndDate = $('#resultEndDate').val();
			$.ajax({
				url:'<%=basePath%>sys/pretreatment/getMongoView.action',
				data:{"menuType":name,"executeTime":ymd,startTime:resultFirstDate,endTime:resultEndDate},
				success : function(result){
					var keys = [];//所有的字段属性
					var coulmusData = [];
					var i=0;
					for(var p in result.rows[0]){
						if(i==0){
							keys.push({'field':'ck',checkbox:true});
						}
						i++;
						if(p=="_id"){
							keys.push({'field':p,'title':p,'width':'10%','formatter':formatterID});
						}else{
							keys.push({'field':p,'title':p,'width':'10%'});
						}
					};
					coulmusData.push(keys);
					$('#textareaL').val(result);
					$('#DatareslutTable').datagrid({
						url:'<%=basePath%>sys/pretreatment/getMongoView.action',
						queryParams:{"menuType":name,"executeTime":ymd,startTime:resultFirstDate,endTime:resultEndDate},
						columns:coulmusData,
						data:result,
						pageSize:"20",
						pageList:[10,20,30,50,80,100],
						pagination:true,
						autoRowHeight : false,
						rownumbers:true,
						striped:true,border:false,checkOnSelect:true,selectOnCheck:false,singleSelect:true,
						loadMsg:'加载中,请稍后...',
						fit:true,striped:true,border:false
						
					});
					$('#reslutwindow').dialog('open')
					var val = $.trim(name);
					name = menuMap[val];
					$('#selecttitleInfo').html("栏目名称：" + name)
				}
			});
		}
		function searchResult(){
			var row = $('#DataMainTable').datagrid('getSelected');
			var type = row.munyType;
			var ymd = $('#resultWinCombo').combobox('getValue');
			var resultFirstDate = $('#resultFirstDate').val();
			var resultEndDate = $('#resultEndDate').val();
			$('#DatareslutTable').datagrid('reload',{
				"menuType":type
				,"executeTime":ymd
				,'startTime':resultFirstDate
				,'endTime':resultEndDate
			});
		}
		function searchHand(){
			var type = $('#handWinCombo').combobox('getValue');
			var state = $('#handWinComboState').combobox('getValue');
			var firstDate = $('#handFirstDate').val();
			var endDate = $('#handEndDate').val();
			var row = $('#DataMainTable').datagrid('getSelected');
			var val = row.munyType;
			$('#handTable').datagrid('reload',{startTime:firstDate,endTime:endDate,menuType:val,type:type,state:state});
		}
		var submitObj = {
			"年": "年接口",
			"月": "月接口",
			"日": "日接口"
		}
		// tabs 内容 

		function settingclick(index, value, name, id) {
			var rows = $('#DataMainTable').datagrid('getRows');
			var row = rows[index];
			var exeSQL = " ";
			var $Mytabs = $('#Mytabs')
			var val = $.trim(name);
			name = menuMap[val];
			$('#titleInfo').html("栏目名称：" + name)
			closeAll($Mytabs)
			var btnVAL = "启用";
			var inputID = "inputStateY";
			for(var i = 0; i < value.length; i++) {
				var ischeckedS = '';
				var ischeckedD = '';
				var ischeckedJ = '';
				if(0==i){//年
					inputID = "inputStateY";
					if(row.stateY==1){
						btnVAL = '停用';
					}else{
						btnVAL = "启用";
					}
					exeSQL = row.executeSQLY;
					if(row.executeWayY==1){
						ischeckedS = ' checked = "checked"';
					}else if(row.executeWayY==2){
						ischeckedD = ' checked = "checked"';
					}else{
						ischeckedJ = ' checked = "checked"';
					}
				}else if(1==i){//月
					inputID = "inputStateM";
					if(row.stateM==1){
						btnVAL = '停用';
					}else{
						btnVAL = "启用";
					}
					exeSQL = row.executeSQLM;
					if(row.executeWayM==1){
						ischeckedS = ' checked = "checked"';
					}else if(row.executeWayM==2){
						ischeckedD = ' checked = "checked"';
					}else{
						ischeckedJ = ' checked = "checked"';
					}
				}else if(2==i){//日
					inputID = "inputStateD";
					if(row.stateD==1){
						btnVAL = '停用';
					}else{
						btnVAL = "启用";
					}
					exeSQL = row.executeSQLD;
					if(row.executeWayD==1){
						ischeckedS = ' checked = "checked"';
					}else if(row.executeWayD==2){
						ischeckedD = ' checked = "checked"';
					}else{
						ischeckedJ = ' checked = "checked"';
					}
				}
					if(exeSQL === undefined){
						exeSQL = ""
					}
				$Mytabs.tabs('add', {
					title: value[i],
					content: '<form id = "form'+i+'" style="height:100%;width:100%;position: relative;" method="post">' + "<input name='index' value=" + id + " style='display:none' />" + 			'<div class = "labelBox" style="width: 100%;height: 100px;line-height:100px;text-align: center;" >' +
					'<label  style="font-size: 18px;margin: 0 20px"><input class = sqlClick'+i+' '+ischeckedS+' name="typeInfo" type="radio" value="1" />SQL</label>' +
					'<label style="font-size: 18px;margin: 0 20px"><input name="typeInfo" type="radio" '+ischeckedD+' value="2" />大数据</label>' +
					'<label style="font-size: 18px;margin: 0 20px"><input name="typeInfo" type="radio" '+ischeckedJ+' value="3" />间接 </label>' +
					'</div>' +
					//暂时取消sql填写界面
					'<div style="width: 100%;height: 20%;text-align: center; ">' +
					'<input style="font-size: 16px;padding: 5px; margin: 0 30px;" class="easyui-linkbutton"  type="button" onclick = "submitMongo('+i+')" value="提交"/>' +
					'<input style="font-size: 16px;padding: 5px; margin: 0 30px;" class="easyui-linkbutton" type="button" onclick = "closeShezhi()" value="取消"/>' +
					'<input style="font-size: 16px;padding: 5px; margin: 0 30px;" class="easyui-linkbutton" type="button" onclick = "switchYMD()" value="'+btnVAL+'" id="'+inputID+'" />' +
					'</div>' +
					'</form>',
					closable: false
				});
			}
			if(row.executeWayY==1){
				$(".sqldiv0").css("display","block")
			}else{
				$(".sqldiv0").css("display","none")
			}
			if(row.executeWayM==1){
				$(".sqldiv1").css("display","block")
			}else{
				$(".sqldiv1").css("display","none")
			}
			if(row.executeWayD==1){
				$(".sqldiv2").css("display","block")
			}else{
				$(".sqldiv2").css("display","none")
			}
			$(".labelBox label").on("click",function(){
				for(var i = 0; i < value.length; i++) {
					if(!$(".sqlClick"+i+"").prop("checked")){
						$(".sqldiv"+i+"").css("display","none")
					}else{
						$(".sqldiv"+i+"").css("display","block")
					}
				}
			})
			
			$('#settingwindow').dialog('open')
		}
		//删除所有tabs
		function closeAll($Mytabs) {
			var tiles = new Array();
			var tabs = $Mytabs.tabs('tabs');
			var len = tabs.length;
			if(len > 0) {
				for(var j = 0; j < len; j++) {
					var a = tabs[j].panel('options').title;
					tiles.push(a);
				}
				for(var i = 0; i < tiles.length; i++) {
					$Mytabs.tabs('close', tiles[i]);
				}
			}
		}

		function closeShezhi() {
			$('#settingwindow').dialog('close')
		}

		function retrievalLog() {
			var row = $('#DataMainTable').datagrid('getSelected');
			var type = row.munyType;
			$('#DatalogTable').datagrid('reload', {
				startTime: $("#firstDate").val(),
				endTime: $("#endDate").val(),
				ymd:$('#logWinCombo').combobox('getValue'),
				state:$('#logWinStateCombo').combobox('getValue'),
				menuType: type
			});
		}

		function retrievalReslut() {
			// 假数据
			var data = {
				row: [{
					statTime: 2017,
					endTime: 2018,
					AllNum: 1000,
					custatTime: 10,
					cuendTime: 10,
					elapsedTime: 10,
					state: "ok"
				}, {
					statTime: 2017,
					endTime: 2018,
					AllNum: 1000,
					custatTime: 10,
					cuendTime: 10,
					elapsedTime: 10,
					state: "ok"
				}],
				columns: [{
						field: 'id',
						hidden: true
					},
					{
						field: 'statTime',
						title: '开始时间',
						width: "14.28%",
						align: 'center',
					}, {
						field: 'endTime',
						title: '结束时间',
						width: "14.28%",
						align: 'center',
					},
					{
						field: 'custatTime',
						title: '计算开始时间',
						width: "14.28%",
						align: 'center',
					},
					{
						field: 'cuendTime',
						title: '计算结束时间',
						width: "14.28%",
						align: 'center',
					},
					{
						field: 'elapsedTime',
						title: '计算耗时',
						width: "14.28%",
						align: 'center',
					}, {
						field: 'AllNum',
						title: '总条数',
						width: "14.28%",
						align: 'center',
					}, {
						field: 'state',
						title: '状态',
						width: "14.28%",
						align: 'center',
					}
				]
			}
			if($("#resultFirstDate").val() != "" && $("#resultEndDate").val() != "") {
				$.ajax({
					type: "post",
					url: "结果的接口",
					success: function(data) {
						setdata(data)
					}
				});
				//真实需要删除  假方法
				setdata(data)
			} else {
				$.messager.alert("请选择开始（结束）时间")
			}

			function setdata(data) {
				$("#DatareslutTable").datagrid({
					toolbar: '#tool2',
					rownumbers: true,
					pageSize: "20",
					fit:true,
					pageList: [10, 20, 30, 50, 80, 100],
					pagination: true,
					data: data.row,
					columns: [
						data.columns
					]
				})
			}

		}
		function submitMongo(valIndex){
			var row = $('#DataMainTable').datagrid('getSelected');
			var tab = $('#Mytabs').tabs('getSelected');
			var way = $("#form"+valIndex+" input[name='typeInfo']:checked").val();
			var index = $('#Mytabs').tabs('getTabIndex',tab);
			var sql = $('#pexecuteSQL'+index).val();
			var exeTime = 1;
			if(index==0){
				exeTime = 3;
			}
			if(index==1){
				exeTime = 2;
			}
			$.messager.progress({text:'提交中，请稍后...',modal:true});
			$.ajax({
				url:'<%=basePath%>sys/pretreatment/saveMongoCount.action',
				data:{'executeTime':exeTime,'executeWay':way,'id':row.id,'sql':sql},
				success : function(data){
					$.messager.progress('close');
					$.messager.alert('提示',data.resMsg,'info',function(){
					});
				}
			});
		}
		function formatterMenu(value,row,index){
			value = $.trim(value);
			return menuMap[value];
		}
		function formatterMenuYMD(value,row,index){
			value = $.trim(value);
			var strs = value.split("_");
			var menu = menuMap[strs[0]];
			var ymd = "";
			if(strs[1]=="MONTH"){
				ymd = "月";
			}
			if(strs[1]=="YEAR"){
				ymd = "年";
			}
			if(strs[1]=="DAY"){
				ymd = "日";
			}
			if(ymd==""||ymd==null){
				return menu;
			}
			return menu+"("+ymd+")";
		}
		function formatterState(value,row,index){
			value = $.trim(value);
			var showValue = "";
			if(value=="1"){
				showValue = "启用";
			}else{
				showValue = "停用";
			}
			return "<input type='button' value='"+showValue+"' style='width: 60%;' onclick='btnSwitch(" + index + ",\"" + row.munyType + "\",\"" + row.id + "\")' />"
		}
		function searchFrom(){
			var name = $('#sName').textbox('getText');
			$('#DataMainTable').datagrid('reload',{name:name});
		}
		function searchClears(){
			$('#sName').textbox('setText','');
			searchFrom();
		}
		function formatterID(value,row,index){
			return JSON.stringify(value);
		}
		//加载模式窗口
		function AdddilogModel(title, url, width, height) {
			$('#dialogDivId1').dialog({    
			    title: title,    
			    width: width,    
			    height: height,    
			    closed: false,    
			    cache: false,    
			    href: url,    
			    modal: true   
			   });    
		}
		//加载页面
		function btnAdd(){
			AdddilogModel("添加","<%=basePath%>sys/pretreatment/toEdit.action","50%","70%");
			}	
		function btnEdit(){
			if(getIdUtil("#DataMainTable").length!=0){
				AdddilogModel('修改',"<%=basePath%>sys/pretreatment/toEdit.action?id="+getIdUtil("#DataMainTable"),"50%","70%");
			 }
		}
		function submit(){
			$.messager.progress({text:'提交中，请稍后...',modal:true});
			$('#editForm').form('submit',{
				url:'<%=basePath%>sys/pretreatment/saveCount.action',
				success : function(data){
					$.messager.progress('close');
					data = JSON.parse(data);
					$.messager.alert('提示',data.resMsg,'info',function(){
						$('#dialogDivId1').dialog('close');
						$('#DataMainTable').datagrid('reload');
					});
				}
				,error : function(){
					$.messager.progress('close');
					$.messager.alert('提示','网络繁忙，请稍后重试...');
				}
			});
		}
		function btnDelete(){
			var row = $('#DataMainTable').datagrid('getSelected');
			if(row==null||row==""){
				$.messager.alert('提示','请选择一行');
				return false;
			}
			$.ajax({
				url : '<%=basePath%>sys/pretreatment/deleteCount.action',
				data : {"id":row.id},
				success : function(data){
					$.messager.alert('提示',data.resMsg,'info',function(){
						$('#DataMainTable').datagrid('reload');
					});
				}
			});
		}
		function closeLayout(){
			$('#dialogDivId1').dialog('close');
			$('#DataMainTable').datagrid('reload');
		}
		function btnSwitch(value,menuType,id){
			$.ajax({
				url : '<%=basePath%>sys/pretreatment/switchOper.action',
				data : {"id":id},
				success : function(data){
					$.messager.alert('提示',data.resMsg,'info',function(){
						$('#DataMainTable').datagrid('reload');
					});
				}
			});
		}
		function pretreatment(){
			var begain = $('#pretreatFirstDate').val();
			var end = $('#pretreatEndDate').val();
			if(begain==null||begain==""){
				$.messager.alert('提示','请选择开始时间...');
				return ;
			}
			if(end==null||end==""){
				$.messager.alert('提示','请选择结束时间...');
				return ;
			}
			//年、月、日
			var type = $('#pretreatWinCombo').combobox('getValue');
			var row = $('#DataMainTable').datagrid('getSelected');
			$.messager.confirm('请确认开始时间结束时间',"您选择的时间是："+begain+"至"+end,function(r){
				if(r){
					$.messager.progress({text:'处理中，请稍后...',modal:true});
					$.ajax({
						url:'<%=basePath%>mongoDataInit/mongoDataInit.action?menuName='+row.munyType+'&startTime='+begain+'&endTime='+end+'&type='+type,
						data : {},
						success : function(result){
							$.messager.progress('close');
							$.messager.alert('提示',result,'info',function(){
								$('#DatareslutTable').datagrid('reload');
							});
						}
						,error : function(){
							$.messager.progress('close');
							$.messager.alert('提示','网络繁忙,请稍后重试...','info',function(){
								$('#DatareslutTable').datagrid('reload');
							});
						}
					});
				}
			});
		}
		function reCalculate(){
			var checkRows = $('#DatareslutTable').datagrid('getChecked');
			var date = "";
			if(checkRows!=""&&checkRows.length>0){
				for(var i=0; i<checkRows.length;i++){
					if(date!=""){
						date +=",";
					}
					date += checkRows[i].date;
				}
			}else{
				$.messager.alert('提示','请选择选择要重新计算的行...');
				return ;
			}
			//年、月、日
			var type = $('#resultWinCombo').combobox('getValue');
			var row = $('#DataMainTable').datagrid('getSelected');
			$.messager.confirm('请确认时间',"您选择的时间是："+date,function(r){
				if(r){
					$.messager.progress({text:'重新计算中，请稍后...',modal:true});
					$.ajax({
						url:'<%=basePath%>mongoDataInit/reCalculate.action?menuName='+row.munyType+'&startTime='+date+'&type='+type,
						data : {},
						success : function(result){
							$.messager.progress('close');
							$.messager.alert('提示',result,'info',function(){
								$('#DatareslutTable').datagrid('reload');
							});
						}
						,error : function(){
							$.messager.progress('close');
							$.messager.alert('提示','网络繁忙,请稍后重试...','info',function(){
								$('#DatareslutTable').datagrid('reload');
							});
						}
					});
				}
			});
		}
		function switchYMD(){
			var row = $('#DataMainTable').datagrid('getSelected');
			var tab = $('#Mytabs').tabs('getSelected');
			var index = $('#Mytabs').tabs('getTabIndex',tab);
			var exeTime = 1;
			if(index==0){
				exeTime = 3;
			}
			if(index==1){
				exeTime = 2;
			}
			$.ajax({
				url : '<%=basePath%>sys/pretreatment/switchYMD.action',
				data : {'id':row.id,'ymd':exeTime,},
				success : function(data){
					if(data.resCode=="success"){
						var state = "";
						var btnID = "";
						if(index==0){//年
							state =  $('#inputStateY').val();
							btnID = "inputStateY";
						}else if(index==1){//月
							state =  $('#inputStateM').val();
							btnID = "inputStateM";
						}else if(index==2){//日
							state =  $('#inputStateD').val();
							btnID = "inputStateD";
						}
						$.messager.alert('提示',data.resMsg+'成功!','',function(){
							if('启用'==data.resMsg){
								$('#'+btnID).val("停用");
							}else{
								$('#'+btnID).val("启用");
							}
						});
					}
				}
			});
		}
			function fmtLogStat(value,row,index){
				if(value==1){
					return "成功";
				}else{
					return "<span color='red'>失败</span>";
				}
			};
			function fmthandWay(value,row,index){
				if(value=="1"){
					return "手动";
				}else{
					return "自动";
				}
			}
			function fmtStat(value,row,index){
				if(value=="2"){
					return "执行中";
				}else if(value=="1"){
					return "执行成功";
				}else{
					return "执行失败";
				}
			}
			function formatterYMD(value,row,index){
				if(value=="3"){
					return "年";
				}
				if(value=="2"){
					return "月";
				}
				if(value=="1"){
					return "日";
				}
			}
			function fmtexecuteTime(value,row,index){
				var startTime = row.countStartTime;
				var endTime = row.countEndTime;
				var executeTime = new Date(endTime).getTime()-new Date(startTime).getTime();
				return executeTime/1000+"秒";
			}
	</script>

</html>