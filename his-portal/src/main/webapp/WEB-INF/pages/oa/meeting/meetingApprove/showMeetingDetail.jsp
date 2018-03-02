<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/metas.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<style type="text/css">
input:disabled{
    border: 1px solid #DDD;
    background-color: #F5F5F5;
    color:#ACA899;
}
</style>
</head>
<body>
<div id="panelEast" class="easyui-panel"
		data-options="iconCls:'icon-form',border:false" fit='true'>
		<div style="padding: 5px;hight:80%;overflow-y:auto;" id="panelEasttable" data-options="border:false" >
			<form id="editForm" method="post"  >
				<table class="honry-table" cellpadding="0" cellspacing="0"   id="table1"
					border="0" style="margin-left:auto;margin-right:auto;margin-down:auto; overflow: auto;" >
					<tr>
						<td class="honry-lable">出席人员：</td>
						<td colspan="3">
							<textarea class="easyui-validatebox" rows="3" cols="50" id="meetingAttendance"  style="width: 514px; "
								name="oaMeetingApply.meetingAttendance" data-options="multiline:true">${oaMeetingApply.meetingAttendance }</textarea>
						</td>
					</tr>
					<tr style="display: none;" >
						<td class="honry-lable">查看范围：</td>
						<td colspan="3">
							<textarea class="easyui-validatebox" rows="3" cols="50" id="meetingDept"  style="width: 514px; "
								name="oaMeetingApply.meetingDept" data-options="multiline:true">${oaMeetingApply.meetingDept }</textarea>
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							会议名称：
						</td>
						<td>
							<input id="meetingName" class="easyui-textbox" value="${oaMeetingApply.meetingName }" name="oaMeetingApply.meetingName"
								data-options="required:true,missingMessage:'请填写会议名称!'"></input>
						</td>
						<td class="honry-lable">
							会议主题：
						</td>
						<td>
							<input id="meetingTheme" class="easyui-textbox" value="${oaMeetingApply.meetingTheme }" name="oaMeetingApply.meetingTheme"
								data-options="required:true,missingMessage:'请填写会议主题!'"></input>
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							会议室：
						</td>
						<td>
							<input id="meetName" class="easyui-textbox" value="${oaMeetingApply.meetName }" name="oaMeetingApply.meetName"
								data-options="required:true,missingMessage:'请填写会议室!'"></input>
						</td>
						<td class="honry-lable">
							会议室管理员：
						</td>
						<td>
								<input class="easyui-textbox"
								value="${oaMeetingApply.meetingAdmin }"
								name="oaMeetingApply.meetingAdmin" data-options=""></input>
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							周期性会议申请：
						</td>
						<td colspan="3">
							 <input type="radio" name="cricleApply" value="no" checked="checked">否
							 <input type="radio" name="cricleApply" value="week">按周
							 <input type="radio" name="cricleApply" value="month">按月
							 
							 <input id="meetingPeriodicity" type="hidden" value="${oaMeetingApply.meetingPeriodicity }" name="oaMeetingApply.meetingPeriodicity" />
						</td>
<!-- 						<td class="honry-lable"> -->
<!-- 							当前在线会议室管理员： -->
<!-- 						</td> -->
<!-- 						<td> -->
<!-- 						</td> -->
					</tr>
					<tr id="cricleNo">
						<td class="honry-lable">
							开始时间：
						</td>
						<td>
							<input id="meetingStarttimeNo" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'%y-%M-%d %H:%m:%s',maxDate:'2099-12-30 00:00:00'})" 
								class="Wdate" type="text" onClick="WdatePicker()"  style="border: 1px solid #95b8e7;border-radius: 5px;"/>
						</td>
						<td class="honry-lable">
							结束时间：
						</td>
						<td>
							<input id="meetingEndtimeNo" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'%y-%M-%d %H:%m:%s',maxDate:'2099-12-30 00:00:00'})" 
								class="Wdate" type="text" onClick="WdatePicker()"  style="border: 1px solid #95b8e7;border-radius: 5px;"/>
								
							<input id="meetingStarttime" type="hidden" value="${oaMeetingApply.meetingStarttime }" name="oaMeetingApply.meetingStarttime" />
							<input id="meetingEndtime" type="hidden" value="${oaMeetingApply.meetingEndtime }" name="oaMeetingApply.meetingEndtime" />
						</td>
					</tr>
<!-- ==========================		星期星期星期星期星期星期星期星期星期星期星期星期星期星期		========================== -->
					<tr  id="cricleWeekTime" style="display:none">
						<td class="honry-lable">
							会议日期：
						</td>
						<td>
							<input id="startWeekDate" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'%y-%M-%d',maxDate:'2099-12-30',onpicked:week_select})" 
								class="Wdate" type="text" onClick="WdatePicker();" style="border: 1px solid #95b8e7;border-radius: 5px;"/>
								至
							<input id="endWeekDate" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'%y-%M-%d',maxDate:'2099-12-30',onpicked:week_select})" 
								class="Wdate" type="text" onClick="WdatePicker();" style="border: 1px solid #95b8e7;border-radius: 5px;"/>
								
							<input id="meetingStart" type="hidden" value="${oaMeetingApply.meetingStart }" name="oaMeetingApply.meetingStart" />
							<input id="meetingEnd" type="hidden" value="${oaMeetingApply.meetingEnd }" name="oaMeetingApply.meetingEnd" />
							<input id="meetingApplyweek" type="hidden" value="${oaMeetingApply.meetingApplyweek }" name="oaMeetingApply.meetingApplyweek" />
								
						</td>
						<td class="honry-lable">
							会议时间：
						</td>
						<td>
							<input id="startWeekTime" onclick="WdatePicker({dateFmt:'H:mm:ss',minDate:'00:00:00',maxDate:'23:59:59'})" 
								class="Wdate" type="text" style="border: 1px solid #95b8e7;border-radius: 5px;"/>
								至
							<input id="endWeekTime" onclick="WdatePicker({dateFmt:'H:mm:ss',minDate:'00:00:00',maxDate:'23:59:59'})"
								class="Wdate" type="text" style="border: 1px solid #95b8e7;border-radius: 5px;"/>
						</td>
					</tr>
					<tr  id="cricleWeek" style="display:none">
						<td class="honry-lable">
							申请星期：
						</td>
						<td colspan="3">
							<label id="WEEKEND0" for="W7" style="display:none"><input type="checkbox" id="W0" name="W7" data-value="0">星期日</label>
			                <label id="WEEKEND1" for="W1" style="display:none"><input type="checkbox" id="W1" name="W1" data-value="1">星期一</label>
			                <label id="WEEKEND2" for="W2" style="display:none"><input type="checkbox" id="W2" name="W2" data-value="2">星期二</label>
			                <label id="WEEKEND3" for="W3" style="display:none"><input type="checkbox" id="W3" name="W3" data-value="3">星期三</label>
			                <label id="WEEKEND4" for="W4" style="display:none"><input type="checkbox" id="W4" name="W4" data-value="4">星期四</label>
			                <label id="WEEKEND5" for="W5" style="display:none"><input type="checkbox" id="W5" name="W5" data-value="5">星期五</label>
			                <label id="WEEKEND6" for="W6" style="display:none"><input type="checkbox" id="W6" name="W6" data-value="6">星期六</label>
						</td>
					</tr>
<!-- ==========================		月月月月月月月月月月月月月月月月月月月		========================== -->
					<tr  id="cricleMonthTime" style="display:none">
						<td class="honry-lable">
							会议日期：
						</td>
						<td>
							<input id="startMonthDate" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'%y-%M-%d',maxDate:'2099-12-30',onpicked:date_select})" 
								class="Wdate" type="text" onClick="WdatePicker();" style="border: 1px solid #95b8e7;border-radius: 5px;"/>
								至
							<input id="endMonthDate" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'%y-%M-%d',maxDate:'2099-12-30',onpicked:date_select})" 
								class="Wdate" type="text" onClick="WdatePicker();" style="border: 1px solid #95b8e7;border-radius: 5px;"/>
						</td>
						<td class="honry-lable">
							会议时间：
						</td>
						<td>
							<input id="startMonthTime" onclick="WdatePicker({dateFmt:'H:mm:ss',minDate:'00:00:00',maxDate:'23:59:59'})" 
								class="Wdate" type="text" style="border: 1px solid #95b8e7;border-radius: 5px;"/>
								至
							<input id="endMonthTime" onclick="WdatePicker({dateFmt:'H:mm:ss',minDate:'00:00:00',maxDate:'23:59:59'})"
								class="Wdate" type="text" style="border: 1px solid #95b8e7;border-radius: 5px;"/>
						</td>
					</tr>
					<tr  id="cricleMonth" style="display:none">
						<td class="honry-lable">
							申请日期：
						</td>
						<td colspan="2" id='monthselect'>
			            </td>
			            <td>
							当前选择的日期：
			                <input type='text' id="dateSelectedId" readonly="readonly" name='selectdates' class='BigInput' value=''>
			            </td>
					</tr>
<!-- 					<tr> -->
<!-- 						<td class="honry-lable"> -->
<!-- 							会议室设备： -->
<!-- 						</td> -->
<!-- 						<td colspan="3"> -->
<!-- 							<input class="easyui-textbox" -->
<%-- 								value="${patient.patientInputcode }" --%>
<!-- 								name="patient.patientInputcode" data-options="width:340"></input> -->
<!-- 						</td> -->
<!-- 					</tr> -->
					<tr style="display: none;">
<!-- 						<td class="honry-lable"> -->
<!-- 							会议纪要员： -->
<!-- 						</td> -->
<!-- 						<td> -->
<!-- 							<input class="easyui-textbox" -->
<%-- 								value="${oaMeetingApply.meetingMinutes }" --%>
<!-- 								name="oaMeetingApply.meetingMinutes" data-options=""></input> -->
<!-- 						</td> -->
						<td class="honry-lable">
							是否电子邮件提醒：
						</td>
						<td>
							<input type="hidden" id="emailSelects" name="oaMeetingApply.meetingEmail" value="${oaMeetingApply.meetingEmail }"/>
							<input type="checkBox" id="emailSelect" onclick="javascript:checkBoxSelect(this,0,1)"/>
							是
						</td>
					</tr>
					<tr >
						<td class="honry-lable">
							提醒设置：
						</td>
						<td>
							提前
<!-- 							<input class="easyui-numberbox" -->
<!-- 								name="oaMeetingApply.meetingNoticehour" -->
<%-- 								value="${oaMeetingApply.meetingNoticehour }" data-options="width:36,min:0,precision:0" /> --%>
							<input class="easyui-combobox" style="width:25%" id="meetingNoticehour" name="oaMeetingApply.meetingNoticehour" value="${oaMeetingApply.meetingNoticehour }" 
								data-options="required:true,panelHeight:'120',editable:false, valueField: 'value', 
								textField: 'text',data:[{value: '1',text: '1小时'},{value: '3',text: '3小时'},{value: '5',text: '5小时'},{value: '24',text: '1天'}]"/>
							提醒
						</td>
						<td class="honry-lable">
							通知出席人员：
						</td>
						<td>
							<input type="hidden" id="noticeSelects" name="oaMeetingApply.meetingNotice" value="${oaMeetingApply.meetingNotice }"/>
							<input type="checkBox" id="noticeSelect" onclick="javascript:checkBoxSelect(this,0,1)"/>
							发送事物提醒消息
						</td>
					</tr>
					<tr style="display: none;">
						<td class="honry-lable">
							提醒会议室管理员：
						</td>
						<td>
							<input type="hidden" id="reminderSelects" name="oaMeetingApply.meetingRemind" value="${oaMeetingApply.meetingRemind }"/>
							<input type="checkBox" id="reminderSelect" onclick="javascript:checkBoxSelect(this,0,1)"/>
							发送事物提醒消息
						</td>
						<td class="honry-lable">
							写入日程安排：
						</td>
						<td>
							<input type="hidden" id="diarySelects" name="oaMeetingApply.meetingSchedule" value="${oaMeetingApply.meetingSchedule }"/>
							<input type="checkBox" id="diarySelect" onclick="javascript:checkBoxSelect(this,0,1)"/>
							是
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							提前签到时间：
						</td>
						<td>
							<input class="easyui-combobox" style="width:50%" id="signBeforeTime" name="oaMeetingApply.signBeforeTime" value="${oaMeetingApply.signBeforeTime }" 
								data-options="required:true,panelHeight:'120',editable:false, valueField: 'value', 
								textField: 'text',data:[{value: '15',text: '15分钟'},{value: '30',text: '30分钟'},{value: '45',text: '45分钟'},{value: '60',text: '60分钟'}
								,{value: '75',text: '75分钟'},{value: '90',text: '90分钟'}]"/>
						</td>
						<td class="honry-lable">
							会议纪要员：
						</td>
						<td>
							<input class="easyui-textbox"
								value="${oaMeetingApply.meetingMinutes }"
								name="oaMeetingApply.meetingMinutes" data-options=""></input>
						</td>
					</tr>
				</table>
				<div style="margin-top: 10px;">
					<h3>会议描述：</h3>
					<table style="width: 100%">
						<tr>
							<td>
								<textarea id="editor" style="width: 100%">${oaMeetingApply.meetingDescribeString}</textarea>
								<input type="hidden" id="meetingDescribeString" name="oaMeetingApply.meetingDescribeString"/>
								<input type="hidden" name='oaMeetingApply.id' value="${oaMeetingApply.id}"/>
							</td>
						</tr>
					</table>
				</div>
				<div id="attach"></div>
			</form>
		</div>
<!-- 		<div style="text-align: center; hight:10%"> -->
<%-- 				<c:if test="${empty idcard.id}"> --%>
<!-- 					<a href="javascript:submit(1);" class="easyui-linkbutton" -->
<!-- 						data-options="iconCls:'icon-save'">连续添加</a> -->
<%-- 				</c:if> --%>
<!-- 				<a href="javascript:submit(0);void(0)" class="easyui-linkbutton" -->
<!-- 					data-options="iconCls:'icon-save'">申请</a> -->
<!-- 				<a href="javascript:clear();void(0)" class="easyui-linkbutton" -->
<!-- 					data-options="iconCls:'icon-clear'">清除</a> -->
<!-- 				<a href="javascript:void(0)" class="easyui-linkbutton" -->
<!-- 					data-options="iconCls:'icon-cancel'" onclick="closeLayout()">关闭</a> -->
<!-- 		</div> -->
	</div>
<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/js/easyui-combobox-filterLocal.js"></script>
<script type="text/javascript" src="<%=path%>/ueditor1_4_3_2/ueditor.config.js"></script>
<script type="text/javascript" src="<%=path%>/ueditor1_4_3_2/ueditor.all.min.js"></script>
<script type="text/javascript" src="<%=path%>/ueditor1_4_3_2/ueditor.all.js"></script>
	<script>
	UE.getEditor("editor",{
	     initialFrameHeight: 300,
	     readonly : true,
	     focus: true
	     , toolbars: [[
		          'fullscreen', 'source', '|', 'undo', 'redo', '|',
		          'bold', 'italic', 'underline', 'fontborder', 'strikethrough', 'superscript', 'subscript', 'removeformat', 'formatmatch', 'autotypeset', 'blockquote', 'pasteplain', '|', 'forecolor', 'backcolor', 'insertorderedlist', 'insertunorderedlist', 'selectall', 'cleardoc', '|',
		          'rowspacingtop', 'rowspacingbottom', 'lineheight', '|',
		          'customstyle', 'paragraph', 'fontfamily', 'fontsize', '|',
		          'directionalityltr', 'directionalityrtl', 'indent', '|',
		          'justifyleft', 'justifycenter', 'justifyright', 'justifyjustify', '|', 'touppercase', 'tolowercase', '|',
		          'link', 'unlink', 'anchor', '|', 'imagenone', 'imageleft', 'imageright', 'imagecenter', '|',
		          'simpleupload', 'insertimage', 'emotion',
// 		          'attachment',
		          'map',
		          'insertframe', 
		          'template', 'background', '|',
		          'horizontal', 'date', 'time', 'spechars', 
		          '|','inserttable', 'deletetable', 'insertparagraphbeforetable', 'insertrow', 'deleterow', 'insertcol', 'deletecol', 'mergecells', 'mergeright', 'mergedown', 'splittocells', 'splittorows', 'splittocols', 'charts', '|',
		          'print', 'preview', 'searchreplace', 
	      ]]
	});
	var check = "";
	var codeCertificateList="";	
	var filenames = '${oaMeetingApply.meetingFileName}';
	var fileurls = '${oaMeetingApply.meetingFile}';
	var fileServersURL = '';
	$(function(){
		if (filenames != null && filenames != "") {
			var filename = filenames.split(";");
			var fileurl = fileurls.split(";");
			for (var i = 0; i < fileurl.length; i++) {
				console.log(fileServersURL);
				var html = '<a href="'+fileServersURL+fileurl[i]+'" download="'+filename[i]+'">'
						+ filename[i] + '</a></br>';
				$('#attach').append(html);
			}
		}
		var winH=$("body").height();
		$('#panelEasttable').height(winH-78-30);
		
		if($('#emailSelects').val()==1){
			document.getElementById('emailSelect').checked=true;
		}
		if($('#diarySelects').val()==1){
			document.getElementById('diarySelect').checked=true;
		}
		if($('#reminderSelects').val()==1){
			document.getElementById('reminderSelect').checked=true;
		}
		if($('#noticeSelects').val()==1){
			document.getElementById('noticeSelect').checked=true;
		}
		
		var mp = $("#meetingPeriodicity").val();
		var mst = $("#meetingStarttime").val();
		var met = $("#meetingEndtime").val();
		var maw = $("#meetingApplyweek").val();
		$("#dateSelectedId").val(maw);
		
		$("input[name='cricleApply'][value=" + mp + "]").attr("checked", true);
		if("no" == mp){
			  $("#cricleNo").show();
			  $("#cricleWeekTime").hide();
			  $("#cricleWeek").hide();
			  $("#cricleMonthTime").hide();
			  $("#cricleMonth").hide();
			  $("#meetingStarttimeNo").val(mst);
			  $("#meetingEndtimeNo").val(met);
		  }
		if("week" == mp){
			$("#cricleNo").hide();
			$("#cricleWeekTime").show();
			$("#cricleWeek").show();
			$("#cricleMonthTime").hide();
			$("#cricleMonth").hide();
			$("#startWeekDate").val(mst.substring(0,10)); 
			$("#startWeekTime").val(mst.substring(11,19)); 
			$("#endWeekDate").val(met.substring(0,10)); 
			$("#endWeekTime").val(met.substring(11,19));
			
			document.getElementById("WEEKEND1").style.display = "";
			document.getElementById("WEEKEND2").style.display = "";
			document.getElementById("WEEKEND3").style.display = "";
			document.getElementById("WEEKEND4").style.display = "";
			document.getElementById("WEEKEND5").style.display = "";
			document.getElementById("WEEKEND6").style.display = "";
			document.getElementById("WEEKEND0").style.display = "";
			
			while(maw.indexOf(",") >= 0){
				document.getElementById("W"+maw.split(",")[0]).checked = true;
				maw = maw.substring(2,maw.length);
			}
			if(""!=maw){
				document.getElementById("W"+maw).checked = true;
			}
		}
		if("month" == mp){
			$("#cricleNo").hide();
			$("#cricleWeekTime").hide();
			$("#cricleWeek").hide();
			$("#cricleMonthTime").show();
			$("#cricleMonth").show();
			$("#startMonthDate").val(mst.substring(0,10)); 
			$("#startMonthTime").val(mst.substring(11,19)); 
			$("#endMonthDate").val(met.substring(0,10)); 
			$("#endMonthTime").val(met.substring(11,19)); 
			while(maw.indexOf(",") >= 0){
				var str = ('<label for=""><input type="checkbox" checked="true" disabled="disabled" value="'+ maw.split(",")[0] +'" name="dateselects" onclick="selectdate()" />'+ maw.split(",")[0]+'号' +'</label>');
				$("#monthselect").append(str);
				maw = maw.substring(maw.indexOf(",")+1,maw.length);
			}
			if(""!=maw){
				var str = ('<label for=""><input type="checkbox" checked="true" disabled="disabled" value="'+ maw +'" name="dateselects" onclick="selectdate()" />'+ maw+'号' +'</label>');
			    $("#monthselect").append(str);
			}
		}
	});
	
		$("input").attr("disabled", true);
		$("textarea").attr("disabled", true);
// 		$("textarea").attr("disabled", true);
// 		$("textarea").attr("disabled", true);
	
	
	/**
	 * 表单提交
	 * @author  lt
	 * @date 2015-6-1
	 * @version 1.0
	 */
	 function submit(flg) {
		 $.messager.progress({text:'保存中，请稍后...',modal:true});	
		 if (!$('#editForm').form('validate')) {
			 $.messager.progress('close');	
				$.messager.alert('提示',"请输入完整信息!");
				setTimeout(function(){
					$(".messager-body").window('close');
				},3500);
				return false;
			}else{
				submits(flg);
			}
	 }
	function submits(flg) {
		$('#editForm').form('submit', {
			url : "<%=basePath%>meeting/meetingApply/saveMeetingApply.action",
			onSubmit : function() {
				//将内容赋值
		    	var chtml = UE.getEditor('editor').getContent();
		    	var imgs = $(chtml).find('img');
		    	for(var i=0;i<imgs.length;i++){
		    		var img = imgs[i];
		    		var src = $(img).attr('src');
		    		var servers = src.split('/');
		    		if("fileTypeImages"==servers[servers.length-2]){
		    			continue;
		    		}
		    		var newsrc = "";
		    		for(var j=servers.length-1;j>=0;j--){
			    		newsrc = "/"+servers[j]+newsrc;
			    		if(servers[j]=="upload"){
			    			chtml = chtml.replace(src,newsrc);
			    			newsrc = "";
			    			break;
			    		}
		    		}
		    	}
		    	var ahrefs = $(chtml).find('a');
		    	for(var i=0;i<ahrefs.length;i++){
		    		var ahref = ahrefs[i];
		    		var src = $(ahref).attr('href');
		    		var servers = src.split('/');
		    		var newsrc = "";
		    		for(var j=servers.length-1;j>=0;j--){
			    		newsrc = "/"+servers[j]+newsrc;
			    		if(servers[j]=="upload"){
			    			chtml = chtml.replace(src,newsrc);
			    			newsrc = "";
			    			break;
			    		}
		    		}
		    	}
		    	$('#meetingDescribeString').val(chtml);
			},
			success : function(data) {
				if (flg == 0) {
					$.messager.progress('close');
					window.opener.reload(); 
					$.messager.alert('提示','保存成功');
					parent.$("#list").datagrid("reload");
					closeLayout();
				} else if (flg == 1) {
					//清除editForm
					$('#editForm').form('reset');
// 					$('#CodeCertificate').combobox('reload');//身份证
					$('#CodeCertificate').combobox({
						url : "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action'/>?type=certificate",
						valueField : 'encode',
						textField : 'name',
						multiple : false,
						onLoadSuccess: function (data) { //加载完成后,设置选中第一项
							for(var i=0;i<data.length;i++){
				                for (var item in data[i]) {
				                    if (item == "encode" && data[i].name=="身份证") {
				                        $(this).combobox("select", data[i][item]);
				                    }
				                }
							}
			            }
					});
					$('#businessContractunit').combobox('reload');//合同单位
					//加载合同单位
					$('#businessContractunit').combobox({   
						valueField:'encode',
						textField:'name',
						url: "<%=basePath%>patient/patinent/queryUnitCombobox.action",    
					    onLoadSuccess:function(data){
					    	for(var i=0;i<data.length;i++){
				                for (var item in data[i]) {
				                    if (item == "encode" && data[i].name=="自费") {
				                        $(this).combobox("select", data[i][item]);
				                    }
				                }
							}
					    }
					});
				}
			},
			error : function(data) {	
				$.messager.progress('close');
				$.messager.alert('提示',"保存失败！");
			}
		});
	}
	/**
	 * 复选框选中
	 * @author  zxh
	 * @date 2017-07-18 10:53
	 * @version 1.0
	 */
	function checkBoxSelect(obj,defalVal,selVal){
		var name = obj.id+"s";
		var element = document.getElementById(name);
		if(obj.checked==true){
			element.value=selVal;
		}else{
			element.value=defalVal;
		}
	}
	/**
	 * 清除页面填写信息
	 * @author  lt
	 * @date 2015-6-19 10:53
	 * @version 1.0
	 */
	function clear() {
		$('#editForm').form('reset');
	}
	/**
	 * 关闭编辑窗口
	 * @author  lt
	 * @date 2015-6-19 10:53
	 * @version 1.0
	 */
	function closeLayout() {
		parent.$("#list").datagrid("reload");
		self.close();//关闭当前窗口
	}


	/**
	 * 弹出框
	 * @author  lt
	 * @date 2015-06-29
	 * @version 1.0
	 */
	var win;
	function showWin(title, url, width, height) {
		var content = '<iframe id="myiframe" src="'
				+ url
				+ '" width="100%" height="100%" frameborder="0" scrolling="no"></iframe>';
		var divContent = '<div id="treeDeparWin">';
		win = $('<div id="treeDeparWin"><div/>').dialog({
			content : content,
			width : width,
			height : height,
			modal : true,
			minimizable : false,
			maximizable : true,
			resizable : true,
			shadow : true,
			center : true,
			title : title
		});
		win.dialog('open');
	}
	
	/**
	 * 回车弹出联系人关系选择窗口
	 * @author  wanxing
	 * @date 2016-03-22  15:10
	 * @version 1.0
	 */
	 function popWinToRelation(){
			$('#CodeRelation').combobox('setValue','');
			var tempWinPath = "<%=basePath%>popWin/popWinCode/toCodePopWinDB.action?type=relation&textId=CodeRelation";
			window.open (tempWinPath,'newwindowRelation',' left=150,top=80,width='+ (screen.availWidth-300) +',height='+ (screen.availHeight-170) +',scrollbars,resizable=yes,toolbar=no')
	}
	 /**
		* 回车弹出地址一级选择窗口
		* @author  zhuxiaolu
		* @param textId 页面上commbox的的id
		* @date 2016-03-22 14:30   
		* @version 1.0
		*/

		function popWinToDistrict(){
			$('#hometwo').textbox('setValue','');
			$('#homethree').textbox('setValue','');
			$('#homefour').textbox('setValue','');
			var tempWinPath = "<%=basePath%>popWin/popWinDistrict/toDistrictPopWin.action?textId=homeone&level=1&parentId=1";
			window.open (tempWinPath,'newwindowSJLD',' left=150,top=80,width='+ (screen.availWidth -300) +',height='+ (screen.availHeight-170) 

		+',scrollbars,resizable=yes,toolbar=yes')
		}
		
		/**
		* 回车弹出合同单位选择窗口
		* @author  zpty
		* @param textId 页面上commbox的的id
		* @date 2016-04-27  
		* @version 1.0
		*/
		function popWinToUnit(){
			var tempWinPath = "<%=basePath%>popWin/popWinUnit/toUnitPopWin.action?textId=businessContractunit";
			window.open (tempWinPath,'newwindowUnit',' left=150,top=80,width='+ (screen.availWidth -300) +',height='+ (screen.availHeight-170) 

		+',scrollbars,resizable=yes,toolbar=yes')
		}
		
		
		
		
			
</script>
<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>	
</body>
</html>