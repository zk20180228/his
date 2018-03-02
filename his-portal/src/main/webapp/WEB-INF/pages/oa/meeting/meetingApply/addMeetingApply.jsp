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
.numberbox .textbox-text {
text-align: right;
}
body,html{
	width: 100%;
	height: 100%;
	background-color: #fff;
}
#monthselect label{
	float: left;
	margin-left:10px;
}
.window-mask {
	width: 100% !important;
	height: 100% !important;
}
</style>

</head>
<body>
<div id="panelEast"  onclick="changeUdite()">
			<form class="clearfix" style="width: 100%;height: 100%;padding: 15px 0;box-sizing: border-box;"  id="editForm" method="post" enctype="multipart/form-data" >
				<table class="honry-table" cellpadding="0" cellspacing="0"   id="table1"
					border="0" style="margin-left:auto;margin-right:auto;" >
					<tr>
						<td class="honry-lable"><span id="spanSideId">出席人员(内部):</span>
							<input id="meetingAttendance" value="${oaMeetingApply.meetingAttendance }" name="oaMeetingApply.meetingAttendance" type="hidden"/>
							<br><a id="aRangeId" onclick="showEmpRange()">添加外部人员</a>&nbsp;
						</td>
						<td colspan="3">
							<textarea  onclick="setAuthority(1)" style="width:95%;height: 60px;font-size: 14px" id="inSidePerson" readonly="readonly"
								data-options="multiline:true" placeholder="---------点击选择内部出席人员---------"></textarea>
							<input id="inSidePersonCode" value="${oaMeetingApply.inSidePersonCode }" name="oaMeetingApply.inSidePersonCode" type="hidden"/>
						</td>
					</tr>
					<tr style="display: none;" id="outSideTr">
						<td class="honry-lable"><span id="spanSideId">出席人员(外部):</span>
						</td>
						<td colspan="3">
							<textarea style="width:95%;height: 60px;font-size: 14px" id="outSidePerson"
								data-options="multiline:true" placeholder="---------请输入外部出席人员(用、隔开)---------"></textarea>
						</td>
					</tr>
					<tr id="rangeId" style="display: none;">
						<td class="honry-lable"><span id="spanRangeId">查看范围:</span>
						</td>
						<td colspan="3">
							<input id="meetingDeptHidden" value="${oaMeetingApply.meetingDept }" type="hidden"/>
							<input id="meetingDeptCode"  value="${oaMeetingApply.meetingDeptCode }" name="oaMeetingApply.meetingDeptCode" type="hidden"/>
							<textarea style="width:95%;height: 60px;font-size: 14px" id="meetingDept" readonly="readonly" name="oaMeetingApply.meetingDept" 
								data-options="multiline:true" onclick="setAuthority(0)" placeholder="---------点击选择查看范围---------"></textarea>
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
							<input id="meetName" class="easyui-combobox" value="${oaMeetingApply.meetName }" name="oaMeetingApply.meetName"
								data-options="required:true,panelHeight:150,missingMessage:'请填写会议室!'"></input>
							<input id="meetId" value="${oaMeetingApply.meetId }" name="oaMeetingApply.meetId" type="hidden"/>
							<a href="javascript:showHistory();void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-search'">预约情况</a>
						</td>
						<td class="honry-lable">
							会议室管理员：
						</td>
						<td>
								<input id="meetingAdminCode" value="${oaMeetingApply.meetingAdminCode }" name="oaMeetingApply.meetingAdminCode" type="hidden"/>
								<input class="easyui-textbox" readonly="true" id="meetingAdmin" 
								value="${oaMeetingApply.meetingAdmin }"
								name="oaMeetingApply.meetingAdmin" data-options=""></input>
						</td>
					</tr>
					<tr>
						<td class="honry-lable">
							周期性会议申请：
						</td>
						<td >
							 <input type="radio" name="cricleApply" value="no" checked="checked">否
							 <input type="radio" name="cricleApply" value="week">按周
							 <input type="radio" name="cricleApply" value="month">按月
							 
							 <input id="meetingPeriodicity" type="hidden" value="${oaMeetingApply.meetingPeriodicity }" name="oaMeetingApply.meetingPeriodicity" />
						</td>
						<td class="honry-lable">
							会议性质：
						</td>
						<td>
							<input id="meetingNature" name="oaMeetingApply.meetingNature" value="${oaMeetingApply.meetingNature }"class="easyui-combobox" />
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
					<tr id="cricleNo">
						<td class="honry-lable">
							开始时间：
						</td>
						<td>
							<input id="meetingStarttimeNo" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'%y-%M-%d %H:%m:%s',maxDate:'2099-12-30 00:00:00',onpicked:checkTime})" 
								class="Wdate" type="text" onClick="WdatePicker()"  style="border: 1px solid #95b8e7;border-radius: 5px;"/>
						</td>
						<td class="honry-lable">
							结束时间：
						</td>
						<td>
							<input id="meetingEndtimeNo" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'%y-%M-%d %H:%m:%s',maxDate:'2099-12-30 00:00:00',onpicked:checkTime})" 
								class="Wdate" type="text" onClick="WdatePicker()"  style="border: 1px solid #95b8e7;border-radius: 5px;"/>
								
							<input id="meetingStarttime" type="hidden" value="${oaMeetingApply.startTime }" name="oaMeetingApply.meetingStarttime" />
							<input id="meetingEndtime" type="hidden" value="${oaMeetingApply.endTime }" name="oaMeetingApply.meetingEndtime" />
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
							<input id="startWeekTime" onclick="WdatePicker({dateFmt:'H:mm:ss',minDate:'00:00:00',maxDate:'23:59:59',onpicked:checkTime})" 
								class="Wdate" type="text" style="border: 1px solid #95b8e7;border-radius: 5px;"/>
								至
							<input id="endWeekTime" onclick="WdatePicker({dateFmt:'H:mm:ss',minDate:'00:00:00',maxDate:'23:59:59',onpicked:checkTime})"
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
						<td colspan="2">
							<div style="width: 100%;" id = "monthselect"></div>
			            </td>
			            <td>
							当前选择的日期：
			                <input type='text' readonly="readonly" id="dateSelectedId" name='selectdates' class='BigInput' value=''>
			            </td>
					</tr>
					<tr style="display: none;">
<!-- 						<td class="honry-lable"> -->
<!-- 							会议纪要员： -->
<!-- 						</td> -->
<!-- 						<td> -->
<%-- 							<input id="meetingMinutesCode" type="hidden" value="${oaMeetingApply.meetingMinutesCode }" name="oaMeetingApply.meetingMinutesCode" /> --%>
<!-- 							<input class="easyui-combobox" id="meetingMinutes" -->
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
								data-options="panelHeight:'120',editable:false, valueField: 'value', 
								textField: 'text',data:[{value: '1',text: '1小时'},{value: '3',text: '3小时'},{value: '5',text: '5小时'},{value: '24',text: '1天'}]"/>
							提醒
<!-- 							<input class="easyui-numberbox" style="text-align: right;" id="meetingNoticeminute" -->
<!-- 								name="oaMeetingApply.meetingNoticeminute" -->
<%-- 								value="${oaMeetingApply.meetingNoticeminute }" data-options="width:36,min:0,max:60,precision:0" /> --%>
<!-- 							分提醒，提醒 -->
<!-- 							<input class="easyui-numberbox" style="text-align: right;" id="meetingNoticefrequency" -->
<!-- 								name="oaMeetingApply.meetingNoticefrequency" -->
<%-- 								value="${oaMeetingApply.meetingNoticefrequency }" data-options="width:36,min:0,precision:0" /> --%>
<!-- 							次 -->
						</td>
						<td class="honry-lable">
							通知出席人员：
						</td>
						<td>
							<input type="hidden" id="noticeSelects" name="oaMeetingApply.meetingNotice" value="${oaMeetingApply.meetingNotice }"/>
							<input type="checkBox" id="noticeSelect" onclick="javascript:checkBoxSelect(this,0,1);"/>
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
								data-options="panelHeight:'120',editable:false, valueField: 'value', 
								textField: 'text',data:[{value: '15',text: '15分钟'},{value: '30',text: '30分钟'},{value: '45',text: '45分钟'},{value: '60',text: '60分钟'}
								,{value: '75',text: '75分钟'},{value: '90',text: '90分钟'}]"/>
						</td>
						<td class="honry-lable">
							会议纪要员：
						</td>
						<td>
							<input id="meetingMinutesCode" type="hidden" value="${oaMeetingApply.meetingMinutesCode }" name="oaMeetingApply.meetingMinutesCode" />
							<input class="easyui-combobox" id="meetingMinutes"
								value="${oaMeetingApply.meetingMinutes }"
								name="oaMeetingApply.meetingMinutes" data-options=""></input>
						</td>
					</tr>
				</table>
				<div style="width: 95%;margin: 10px auto;">
					<h3>会议描述：</h3>
					<table style="width: 100%">
						<tr>
							<td>
								<textarea id="editor" style="width: 100%">${oaMeetingApply.meetingDescribeString}</textarea>
								<input type="hidden" id="meetingDescribeString" name="oaMeetingApply.meetingDescribeString"/>
								<input type="hidden" id="ownId" name='oaMeetingApply.id' value="${oaMeetingApply.id}"/>
								<input type="hidden" name='oaMeetingApply.createUser' value="${oaMeetingApply.createUser}"/>
								<input type="hidden" name='oaMeetingApply.createTime' value="${oaMeetingApply.createTime}"/>
							</td>
						</tr>
					</table>
				</div>
				<div style="margin-top: 10px;margin-bottom: 10px;">
					<table id="filetable" style="width: auto;margin-left: 2.5%;">
						<tr id="trid0">
							<td>附件上传:</td>
							<td>
								<input id="fb0" name="upload" style="width:575px;height: 33px"/>
								<input id="oldName0" name="oldFileName" type="hidden"/>
								<input id="oldURL0" name="oldFileURL" type="hidden"/>
							</td>
							<td class="fileaddclass">&nbsp;<a id="add0" href="javascript:addfileimput(0)" class="easyui-linkbutton" data-options="iconCls:'icon-add'" ></a></td>
							<td id="deletefileTR0">&nbsp;<a id="deletefile0" href="javascript:deleteFile(0)" class="easyui-linkbutton" >删除附件</a></td>
						</tr>
					</table>
					<div style="text-align: center; height: 50px; line-height: 50px;text-align: center;width: 100%;">
	<%-- 				<c:if test="${empty idcard.id}"> --%>
	<!-- 					<a href="javascript:submit(1);" class="easyui-linkbutton" -->
	<!-- 						data-options="iconCls:'icon-save'">连续添加</a> -->
	<%-- 				</c:if> --%>
						<a href="javascript:submit(0);void(0)" class="easyui-linkbutton"
							data-options="iconCls:'icon-save'">
								<c:choose>
								   <c:when test="${flag=='YZBGS'}">  
										保存   
								   </c:when>
								   <c:otherwise> 
										申请
								   </c:otherwise>
								</c:choose>
						</a>
						<a href="javascript:clear();void(0)" class="easyui-linkbutton"
							data-options="iconCls:'icon-clear'">清除</a>
						<a href="javascript:void(0)" class="easyui-linkbutton"
							data-options="iconCls:'icon-cancel'" onclick="closeLayout()">关闭</a>
					</div>
				</div>
			</form>

		<div id="winidSet">
			<iframe id="iframeid" style = 'width:100%;height:100%;margin: 0;padding: 0;border: none;' ></iframe>
		</div>
		<div id="winHistory" class="easyui-window" title="预约情况" data-options="modal:true,closed:true,iconCls:'icon-save',modal:true,collapsible:false,minimizable:false,maximizable:false" style="width:60%;height:50%;">
			    <table class="honry-table" cellpadding="0" cellspacing="0"   id="tableHistory"
					border="0" style="width: 100%;height: 100%" >
				</table>
				<!--<div style="text-align: center;padding: 5px">
					<a href="javascript:void(0)" onclick="closeWinHistory()" class="easyui-linkbutton" style="height:25px;" iconCls="icon-search">关闭</a>
				</div>-->
		</div>
	</div>
<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/js/easyui-combobox-filterLocal.js"></script>
<script type="text/javascript" src="<%=basePath%>javascript/js/jquery.deptMenu.js"></script>
<script type="text/javascript" src="<%=path%>/ueditor1_4_3_2/ueditor.config.js"></script>
<script type="text/javascript" src="<%=path%>/ueditor1_4_3_2/ueditor.all.min.js"></script>
<script type="text/javascript" src="<%=path%>/ueditor1_4_3_2/ueditor.all.js"></script>
	<script type="text/javascript">
	UE.getEditor("editor",{
	     initialFrameHeight: 300,
	     focus: true,
	     toolbars: [[
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
	function produceHTML(i,fileurl,filename){
		var j = parseInt(i)-1;
		var html = '<tr id="trid'+i+'">'
						+'<td>附件上传:</td>'
					+'<td>'
						+'<input id="fb'+i+'" name="upload" style="width:575px;height: 33px"/>'
						+'<input id="oldName'+i+'" name="oldFileName" type="hidden"/>'
						+'<input id="oldURL'+i+'" name="oldFileURL" type="hidden"/>'
					+'</td>'
					+'<td class="fileaddclass">&nbsp;<a id="add'+i+'" href="javascript:addfileimput('+i+')" class="easyui-linkbutton" data-options="iconCls:\'icon-add\'" ></td>'
					+'<td id="deletefileTR'+i+'">&nbsp;<a id="deletefile'+i+'" href="javascript:deleteFile('+i+')" class="easyui-linkbutton" >删除附件</a></td>'
				+'</tr>';
		$('#add'+j).hide();
		$('#filetable').append(html);
		$('#add'+i).show();
		$('#oldName'+i).val(filename);
		$('#oldURL'+i).val(fileurl);
		$('#fb'+i).val(filename);
		$('#deletefile'+i).linkbutton({
			
		});
		$('#fb'+i).filebox({    
		    buttonText: '选择文件', 
		    buttonAlign: 'right'
		})
		$('#add'+i).linkbutton({    
		    iconCls: 'icon-add'   
		});
		
	};
	function deleteFile(value){
		var inputlength = $("input[class='fileaddclass']");
// 		console.log("inputlength:"+inputlength);
		$('#trid'+value).remove();
		if(inputlength.length==1){
			addfileimput(-1);
		}
	};
	$(function(){
		$('#deletefileTR0').hide();
		var fileurl = '${oaMeetingApply.meetingFile}';
		var fileName = '${oaMeetingApply.meetingFileName}';
		console.log("fileurl:"+fileurl);
		if(fileurl!=null&&fileurl!=''){
			var oldurl = fileurl.split(';');
			var oldName = fileName.split(';');
			for (var i = 0; i < oldurl.length; i++) {
				if(i==0){
					$('#fb0').val(oldName[i]);
					$('#oldName0').val(oldName[i]);
					$('#oldURL0').val(oldurl[i]);
					$('#deletefileTR0').show();
				}else{
					//调用增加方法
					produceHTML(i,oldurl[i],oldName[i]);
				}
			}
		}
		$('#fb0').filebox({    
		    buttonText: '选择文件', 
		    buttonAlign: 'right'
		});
		$('#meetingNature').combobox({
		 	url: "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action?type=meetingNature'/>", 
		    valueField:'name',     
		    textField:'name'
		});	
		/**
		 * 会议室下拉
		 */
		$('#meetName').combobox({
			url : "<%=basePath%>meeting/meetingInfo/findMeetingRoom.action",   
		    valueField:'meetName',    
		    textField:'meetName',
		    filter:function(q,row){
		    	var keys = new Array();
				keys[keys.length] = 'meetName';
				return filterLocalCombobox(q, row, keys);
		    },
		    onSelect:function(record){
		    	$('#meetingAdmin').textbox('setValue',record.meetAdmin);
		    	$('#meetId').val(record.meetCode);
		    	$('#meetingAdminCode').val(record.meetAdminCode);
		    }
		});
		
		//会议纪要员
		$('#meetingMinutes').combobox({    
			url:'<%=basePath%>baseinfo/employee/employeeCombobox.action',   
		    valueField:'name',    
		    textField:'name',
		    filter:function(q,row){
		    	var keys = new Array();
				keys[keys.length] = 'name';
				return filterLocalCombobox(q, row, keys);
		    },
		    onSelect:function(record){
// 		    	console.log(record);
		    	$('#meetingMinutesCode').val(record.jobNo);
		    }
		});
		
		$('#winidSet').window({    
		    width:980,    
		    height:620,    
		    modal:true,
		    title:"选择"
		}); 
		$('#winidSet').window('close');
		
		
		var mp = $("#meetingPeriodicity").val();
		var mst = $("#meetingStarttime").val();
		var met = $("#meetingEndtime").val();
		var maw = $("#meetingApplyweek").val();
		$("#dateSelectedId").val(maw);
		if(""!=mp){
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
		}
		
		

		
		
		
		
		//单选按钮事件
		$(":radio").click(function(){
		  if("no" == $(this).val()){
			  $("#cricleNo").show();
			  $("#cricleWeekTime").hide();
			  $("#cricleWeek").hide();
			  $("#cricleMonthTime").hide();
			  $("#cricleMonth").hide();
		  }
		  if("week" == $(this).val()){
			  $("#cricleNo").hide();
			  $("#cricleWeekTime").show();
			  $("#cricleWeek").show();
			  $("#cricleMonthTime").hide();
			  $("#cricleMonth").hide();
		  }
		  if("month" == $(this).val()){
			  $("#cricleNo").hide();
			  $("#cricleWeekTime").hide();
			  $("#cricleWeek").hide();
			  $("#cricleMonthTime").show();
			  $("#cricleMonth").show();
		  }
		});
		
		//科室下拉框
		 $.extend($.fn.datagrid.methods, {
		        autoMergeCells: function (jq, fields) {
		            return jq.each(function () {
		                var target = $(this);
		                if (!fields) {
		                    fields = target.datagrid("getColumnFields");
		                }
		                var rows = target.datagrid("getRows");
		                var i = 0,
		                j = 0,
		                temp = {};
		                for (i; i < rows.length; i++) {
		                    var row = rows[i];
		                    j = 0;
		                    for (j; j < fields.length; j++) {
		                        var field = fields[j];
		                        var tf = temp[field];
		                        if (!tf) {
		                            tf = temp[field] = {};
		                            tf[row[field]] = [i];
		                        } else {
		                            var tfv = tf[row[field]];
		                            if (tfv) {
		                                tfv.push(i);
		                            } else {
		                                tfv = tf[row[field]] = [i];
		                            }
		                        }
		                    }
		                }
		                $.each(temp, function (field, colunm) {
		                    $.each(colunm, function () {
		                        var group = this;
		                        if (group.length > 1) {
		                            var before,
		                            after,
		                            megerIndex = group[0];
		                            for (var i = 0; i < group.length; i++) {
		                                before = group[i];
		                                after = group[i + 1];
		                                if (after && (after - before) == 1) {
		                                    continue;
		                                }
		                                var rowspan = before - megerIndex + 1;
		                                if (rowspan > 1) {
		                                    target.datagrid('mergeCells', {
		                                        index: megerIndex,
		                                        field: field,
		                                        rowspan: rowspan
		                                    });
		                                }
		                                if (after && (after - before) != 1) {
		                                    megerIndex = after;
		                                }
		                            }
		                        }
		                    });
		                });
		            });
		        }
		    });
		
		
		if(""!=$("#meetingAttendance").val()){
			var attendance = $("#meetingAttendance").val();
			document.getElementById("inSidePerson").value = attendance.substring(5,attendance.length).split('内部人员：')[1];
			$("#outSidePerson").val(attendance.substring(5,attendance.length).split('内部人员：')[0]);
		}
		
		if(""!=$("#meetingDeptHidden").val()){
			if($("#meetingDeptHidden").val().indexOf("&&")!=-1){
				var deptHidden = $("#meetingDeptHidden").val().split("&&");
				$("#meetingDept").val("查看范围（人员）：" + deptHidden[0] + "\n查看范围（部门）：" + deptHidden.length==2 ? deptHidden[1] : "");
			}else{
				$("#meetingDept").val($("#meetingDeptHidden").val());
			}
		}
		
		var winH=$("body").height();
		//$('#panelEasttable').height(winH-78-30);
		
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
		
		
	});

	/*
	 * 设置权限弹窗
	 */
	function setAuthority(value){
		$('#iframeid')[0].src = "<%=basePath%>/meeting/meetingApply/toAuth.action?findType="+value+"&flag=${flag}";
		$('#winidSet').window('open');
	}
	/**
	 * 添加外部人员
	 * @author  zxh
	 * @date 2017-07-20
	 * @version 1.0
	 */
	function showEmpRange(){
		if($("#outSideTr").css("display")=="none"){
			document.getElementById("aRangeId").innerText = '隐藏外部人员';
			$("#outSideTr").show();
		}else{
			document.getElementById("aRangeId").innerText = '添加外部人员';
			$("#outSideTr").hide();
		}
	}
	//改变富文本层级
	var isFirstClick = true;
	function changeUdite(){
		if(isFirstClick){
			if(document.getElementById("edui1")!=null){
				document.getElementById("edui1").style.zIndex=0;
				isFirstClick = false;
			}
		}
	}
	
	
	var clearInsiderFlag = true;
	function clearInside(){
		if(clearInsiderFlag){
			$("#doctornew").val("");
			clearInsiderFlag = false;
		}
	}
	var clearKsnewFlag = true;
	function clearKsnew(){
		if(clearKsnewFlag){
			$("#ksnewDep").val("");
			clearKsnewFlag = false;
		}
	}
	var clearKsnewEmpFlag = true;
	function clearKsnewEmp(){
		if(clearKsnewEmpFlag){
			$("#ksnewEmp").val("");
			clearKsnewEmpFlag = false;
		}
	}
	
	
	/**
	 * 根据时间。显示星期
	 * @author  zxh
	 * @date 2017-07-21
	 * @version 1.0
	 */
		function week_select() {
			document.getElementById("WEEKEND1").style.display = "none";
			document.getElementById("WEEKEND2").style.display = "none";
			document.getElementById("WEEKEND3").style.display = "none";
			document.getElementById("WEEKEND4").style.display = "none";
			document.getElementById("WEEKEND5").style.display = "none";
			document.getElementById("WEEKEND6").style.display = "none";
			document.getElementById("WEEKEND0").style.display = "none";

			var x = $("#startWeekDate").val();
			y = new Date(x);

			var z = $("#endWeekDate").val();
			r = new Date(z);

			a = r - y;
			a = a / 24 / 60 / 60 / 1000 + 1;

			if (a > 6) {
				document.getElementById("WEEKEND1").style.display = "";
				document.getElementById("WEEKEND2").style.display = "";
				document.getElementById("WEEKEND3").style.display = "";
				document.getElementById("WEEKEND4").style.display = "";
				document.getElementById("WEEKEND5").style.display = "";
				document.getElementById("WEEKEND6").style.display = "";
				document.getElementById("WEEKEND0").style.display = "";
			} else {
				for (var i = x; i <= z; i = DateNextDay(i)) {
					i = i.replace(/-/, ",");
					i = i.replace(/-/, "/");
					v = new Date(i);
					s = v.getDay();
					if (/^[-]?\d+$/.test(s)) {
						w = "WEEKEND" + s;
						document.getElementById(w).style.display = "";
					}
				}
			}
		}
		function checkTime(){
			if(""!=$("#meetingStarttimeNo").val()&&""!=$("#meetingEndtimeNo").val()){
				if($("#meetingStarttimeNo").val()>$("#meetingEndtimeNo").val()){
					$.messager.alert('提示','开始时间不能大于结束时间');
					$("#meetingEndtimeNo").val("");
				}else if(""==$("#meetName").combobox("getValues")){
					$.messager.alert('提示',"请选择会议室！");
					$("#meetingEndtimeNo").val("");
				}else{
					checkTimeRunBack();
				}
			}	
			if(""!=$("#startWeekTime").val()&&""!=$("#endWeekTime").val()&&""!=$("#startWeekDate").val()&&""!=$("#endWeekDate").val()){
				if(""==$("#meetName").combobox("getValues")){
					$.messager.alert('提示',"请选择会议室！");
					$("#endWeekTime").val("");
				}else{
					checkTimeRunBack();
				}
			}	
			if(""!=$("#startMonthTime").val()&&""!=$("#endMonthTime").val()&&""!=$("#startMonthDate").val()&&""!=$("#endMonthDate").val()){
				if(""==$("#meetName").combobox("getValues")){
					$.messager.alert('提示',"请选择会议室！");
					$("#endMonthTime").val("");
				}else{
					checkTimeRunBack();
				}
			}	
		}
		/**
		 * 查后台时间
		 * @author  zxh
		 * @date 2017-07-21
		 * @version 1.0
		 */
		function checkTimeRunBack(){
			//根据单选按钮。存放周期时间
			var cricleApplyChecked = $("input[type='radio']:checked").val();
			$("#meetingPeriodicity").val(cricleApplyChecked);
			  if("no" == cricleApplyChecked){
				 $("#meetingStarttime").val($("#meetingStarttimeNo").val());
				 $("#meetingEndtime").val($("#meetingEndtimeNo").val());
			  }
			  if("week" == cricleApplyChecked){
				 var sWeekTime = changeHMS($("#startWeekTime").val());
				 var eWeekTime = changeHMS($("#endWeekTime").val());
				 $("#meetingStarttime").val($("#startWeekDate").val()+" "+sWeekTime);
				 $("#meetingEndtime").val($("#endWeekDate").val()+" "+eWeekTime);
				 selectWeek();
			  }
			  if("month" == cricleApplyChecked){
				 var sMonthTime = changeHMS($("#startMonthTime").val());
				 var eMonthTime = changeHMS($("#endMonthTime").val());
				 $("#meetingStarttime").val($("#startMonthDate").val()+" "+sMonthTime);
				 $("#meetingEndtime").val($("#endMonthDate").val()+" "+eMonthTime);
				 
				 $("#meetingApplyweek").val($("#dateSelectedId").val());
			  }
			  if(""!=$("#meetingStarttime").val()&&""!=$("#meetingEndtime").val()){
					if($("#meetingStarttime").val()>$("#meetingEndtime").val()){
						$.messager.alert('提示','开始时间不能大于结束时间');
						$("#meetingEndtime").val("");
					}else if(""==$("#meetName").combobox("getValues")){
						$.messager.alert('提示',"请选择会议室！");
						$("#meetingEndtime").val("");
					}
				}
			//把时间后面的小数点去掉。不然后台接收不到
	    	$("#meetingStarttime").val($("#meetingStarttime").val().substring(0,19));
			$("#meetingEndtime").val($("#meetingEndtime").val().substring(0,19));
			var msTime = $("#meetingStarttime").val();
			var meTime = $("#meetingEndtime").val();
			var meetId = $("#meetId").val();
			var id = $("#ownId").val();
			var meetingPeriodicity = $("#meetingPeriodicity").val();
			var meetingApplyweek = $("#meetingApplyweek").val();
			
			$.ajax({
				url : "<%=basePath%>meeting/meetingApply/checkHaveUsed.action",
				data: {msTime:msTime,meTime:meTime,meetId:meetId,meetingPeriodicity:meetingPeriodicity,meetingApplyweek:meetingApplyweek,id:id},
				type:"post",
				success: function(data){
					if("ok"!=data){
						$.messager.alert('提示',data);
					}
				}
			});
// 			$('#editForm').form('submit',{
<%-- 				url : "<%=basePath%>meeting/meetingApply/checkHaveUsed.action", --%>
// 				success : function(data) {
// 					console.log(data);
// 				}
// 			});
		}
		/**
		 * 根据时间。显示日期
		 * @author  zxh
		 * @date 2017-07-21
		 * @version 1.0
		 */
		function date_select() {
			var x = $("#startMonthDate").val();
			var datex = x.substr(x.length - 2, x.length);
			var monthx = x.substr(5, x.length - 8);
			var y = $("#endMonthDate").val();
			var datey = y.substr(y.length - 2, y.length);
			var monthy = y.substr(5, y.length - 8);
			var monthselect = document.getElementById('monthselect');
			monthselect.innerHTML = '';
			if (x != '' && y != '') {
				if (x > y) {
					$.messager.alert('提示','开始时间不能大于结束时间');
				}
			}
			//判断是否跨越
			if (monthy > monthx) {
				for (var i = 0; i <= (31 - datex); i++) {
					createElv(monthselect, datex, i);
				}
				if ((monthy - monthx) == 1) {
					if (datey >= datex) {
						for (var i = 1; i <= datex - 1; i++) {
							createElv(monthselect, 0, i);
						}
					} else {
						for (var i = 1; i <= datey; i++) {
							createElv(monthselect, 0, i);
						}
					}
				} else {
					for (var i = 1; i <= datex - 1; i++) {
						createElv(monthselect, 0, i);
					}
				}

			} else if (monthy == monthx) {
				for (var i = 0; i <= (datey - datex); i++) {
					createElv(monthselect, datex, i);
				}
			}
		}
		function DateNextDay(d2) {
			//slice返回一个数组
			var str = d2.slice(5) + "-" + d2.slice(0, 4);
			var d = new Date(str);
			var d3 = new Date(d.getFullYear(), d.getMonth(), d.getDate() + 1);
			var month = returnMonth(d3.getMonth());
			var day = d3.getDate();
			day = day < 10 ? "0" + day : day;
			var str2 = d3.getFullYear() + "-" + month + "-" + day;
			return str2;
		}
		function returnMonth(num) {
			var str = "";
			switch (num) {
			case 0:
				str = "01";
				break;
			case 1:
				str = "02";
				break;
			case 2:
				str = "03";
				break;
			case 3:
				str = "04";
				break;
			case 4:
				str = "05";
				break;
			case 5:
				str = "06";
				break;
			case 6:
				str = "07";
				break;
			case 7:
				str = "08";
				break;
			case 8:
				str = "09";
				break;
			case 9:
				str = "10";
				break;
			case 10:
				str = "11";
				break;
			case 11:
				str = "12";
				break;
			}
			return str;
		}
		function createElv(obj,x,y)
		{
		    var span = document.createElement("span");
		    var option = document.createElement("input");
		    option.setAttribute("value", eval(x+'+'+y));
		    option.setAttribute("type", 'checkbox');
		    option.setAttribute("name", 'dateselects');
		    option.setAttribute("onclick", 'selectdate()');

		    var str = ('<label for=""><input type="checkbox" value="'+ eval(x+'+'+y) +'" name="dateselects" onclick="selectdate()" />'+ eval(x+'+'+y)+'号' +'</label>');
		    jQuery(obj).append(str);
		}
		//将选择的日期拼成字符串
		function selectdate(){
		    var str=document.getElementsByName("dateselects");
		    var objarray=str.length;
		    var chestr="";
		    for (i=0;i<objarray;i++)
		    {
		        if(str[i].checked == true)
		        {
		            chestr+=str[i].value+",";
		        }
		    }
		    $("#dateSelectedId").val(chestr.substring(0,chestr.length-1));
		}
		//将选择的星期拼成字符串
		function selectWeek(){
			var weeks = "";
			$("#cricleWeek").find('[type="checkbox"]:checked').each(function(k, v){
	            var $this = jQuery(this);
	            weeks += $this.attr('data-value')+',';
	        });
			$("#meetingApplyweek").val(weeks.substring(0,weeks.length-1));
		}
		
		function changeHMS(time){
			  var sHour = time.split(":")[0].length==1?"0"+time.split(":")[0]:time.split(":")[0];
			  var sMin = time.split(":")[1].length==1?"0"+time.split(":")[1]:time.split(":")[1];
			  var sSec = time.split(":")[2].length==1?"0"+time.split(":")[2]:time.split(":")[2];
			  var hmsTime = sHour+":"+sMin+":"+sSec;
			  return hmsTime;
		}
		
		/**
		 * 表单提交
		 * @author  zxh
		 * @date 2017-07-20
		 * @version 1.0
		 */
		function submit(flg) {
			if(''==$("#meetingAdmin").val()){
				$.messager.progress('close');
				$.messager.alert({
					title : '提示',
					msg : '请选择正确的会议室',
				});
				return false;
			}
// 			var cricleApplyChecked = $("input[type='radio']:checked").val();
			//根据单选按钮。存放周期时间
			var cricleApplyChecked = $("input[type='radio']:checked").val();
			$("#meetingPeriodicity").val(cricleApplyChecked);
			  if("no" == cricleApplyChecked){
					 $("#meetingStarttime").val($("#meetingStarttimeNo").val());
				 $("#meetingEndtime").val($("#meetingEndtimeNo").val());
			  }
			  if("week" == cricleApplyChecked){
				  if($("#startWeekTime").val()!=""){
					 var sWeekTime = changeHMS($("#startWeekTime").val());
					 $("#meetingStarttime").val($("#startWeekDate").val()+" "+sWeekTime);
				  }
				  if($("#endWeekTime").val()!=""){
					 var eWeekTime = changeHMS($("#endWeekTime").val());
					 $("#meetingEndtime").val($("#endWeekDate").val()+" "+eWeekTime);
				  }
				 selectWeek();
			  }
			  if("month" == cricleApplyChecked){
				  if($("#startMonthTime").val()!=""){
					 var sMonthTime = changeHMS($("#startMonthTime").val());
					 $("#meetingStarttime").val($("#startMonthDate").val()+" "+sMonthTime);
				  }
				  if($("#endMonthTime").val()!=""){
					 var eMonthTime = changeHMS($("#endMonthTime").val());
					 $("#meetingEndtime").val($("#endMonthDate").val()+" "+eMonthTime);
				  }
				 
				 $("#meetingApplyweek").val($("#dateSelectedId").val());
			  }
			$.messager.progress({
				text : '保存中，请稍后...',
				modal : true
			});
			if (!$('#editForm').form('validate')) {
				$.messager.progress('close');
				$.messager.alert('提示', "请输入完整信息!");
				setTimeout(function() {
					$(".messager-body").window('close');
				}, 3500);
				return false;
			} else if ("" == $("#meetingStarttime").val() || "" == $("#meetingEndtime").val()) {//判断开始时间结束时间是否填写
				$.messager.progress('close');
				$.messager.alert({
					title : '提示',
					msg : '开始时间或结束时间为空',
// 					top : 100
				});
				return false;
			}  else if ($("#meetingStarttime").val() > $("#meetingEndtime").val()) {//判断开始时间结束时间是否填写
				$.messager.progress('close');
				$.messager.alert({
					title : '提示',
					msg : '开始时间不能大于结束时间',
// 					top : 100
				});
				return false;
			}else {
				submits(flg);
			}
		}
		
		function submits(flg) {
			$('#editForm').form('submit',{
				url : "<%=basePath%>meeting/meetingApply/saveMeetingApply.action?flag=${flag}",
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
		    	//将内部人员和外部人员放入出席人员中
		    	$("#meetingAttendance").val("外部人员："+$("#outSidePerson").val()+"  内部人员："+$("#inSidePerson").val());
// 		    	$("#meetingDept").val("查看范围(部门)："+$("#ksnewDep").val()+"  查看范围(员工)："+$("#ksnewEmp").val());

				//把时间后面的小数点去掉。不然后台接收不到
		    	$("#meetingStarttime").val($("#meetingStarttime").val().substring(0,19));
				$("#meetingEndtime").val($("#meetingEndtime").val().substring(0,19));
			},
			success : function(data) {
				if("ok"!=data&&""!=data){
					$.messager.progress('close');
					$.messager.alert('提示',data);
				}else if (flg == 0) {
					$.messager.progress('close');
					//window.opener.location.reload(); 
// 					$.messager.alert({title:'提示',
// 	                        msg:'操作成功',
// 	                        top:100});
					if("${flag}"=='YZBGS'){
						window.opener.$("#dg1").datagrid("reload");
					}else{
						window.opener.$("#list").datagrid("reload");
					}
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
		if("noticeSelect"==obj.id){
			if(obj.checked==true){
				if(""==$("#meetingNoticehour").numberbox('getValue')){
					document.getElementById("noticeSelect").checked = false;
					$.messager.alert('提示',"未进行提醒设置！");
				}else if(""==$.trim($("#inSidePerson").val())&&""==$.trim($("#inSidePerson").val())){
					document.getElementById("noticeSelect").checked = false;
					$.messager.alert('提示',"未选择出席人员！");
				}
			}
		}
		if("reminderSelect"==obj.id){
			if(obj.checked==true){
				if(""==$("#meetingNoticehour").numberbox('getValue')){
					document.getElementById("reminderSelect").checked = false;
					$.messager.alert('提示',"未进行提醒设置！");
				}
			}
		}
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
	 */
	function clear() {
		$('#editForm').form('reset');
		$(".easyui-textbox").textbox("setValue","");
		$(".easyui-combobox").combobox("setValue","");
		$(".easyui-numberbox").numberbox("setValue","");
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
		
		/**
		 *	预约情况 
		 */
		function showHistory(){
			if(""==$("#meetName").combobox("getValues")){
				$.messager.alert('提示',"请选择会议室！");
			}else{
				$('#winHistory').window('open');
				var meetId = $("#meetId").val();
				var nowDay = GetDateStr(0);
				var ownId = $("#ownId").val();
// 				$("#tableHistory").datagrid("load",{
// 					meetId : meetId,
// 					meetingStarttime : nowDay
// 				});
				$("#tableHistory").datagrid({
					url : "<%=basePath%>meeting/meetingApply/queryMeetingApply.action", 
					method: 'get',
					rownumbers: true,
// 					pagination: true,
					fitColumns: true,
					nowrap:false,
					fit: true,
					singleSelect:true,
// 					pageSize: 10,
// 					pageList: [10, 20, 30, 40, 50],
					queryParams: {
						meetId : meetId,
						meetingStarttime : nowDay,
						id: ownId,
						appointmentFLag: 1
					},
					columns:[[
						{
							field: 'meetingName',
							title: '会议名称',
   							width: '15%',
						},{
							field: 'meetingStarttime',
							title: '开始时间',
   							width: '13%',
							formatter:timeFormatter
						},{
							field: 'meetingEndtime',
							title: '结束时间',
   							width: '13%',
							formatter:timeFormatter
						},{
							field: 'meetingApplyweek',
							title: '会议日期',
   							width: '40%',
							formatter:cricleFormatter
						},{
							field: 'meetingPeriodicity',
							title: '周期模式',
   							width: '17%',
							formatter:modelFormatter
						},       
					]]
				});
			}
		}
		/**
		 *	关闭预约情况弹窗 
		 */
		function closeWinHistory(){
			$('#winHistory').window('close');
		}
		/**
		 *	预约情况时间渲染 
		 */
		function timeFormatter(value, row, index){
			if("undefined"!=typeof(value)){
				return value.substring(11,19);
// 				if("no"!=row.meetingPeriodicity){
// 				}else{
// 					return value;
// 				}
			}
		}
		/**
		 *	预约情况周期渲染 
		 */
		function cricleFormatter(value, row, index){
			if("no"==row.meetingPeriodicity){
				var sTime = row.meetingStarttime.substring(0,10);
// 				var eTime = row.meetingEndtime.substring(0,10);
				return sTime;
			}
			if("week"==row.meetingPeriodicity){
				var sTime = row.meetingStarttime.substring(0,10);
				var eTime = row.meetingEndtime.substring(0,10);
				value = value.replace("1","一").replace("2","二").replace("3","三").replace("4","四").replace("5","五").replace("6","六").replace("0","日")
				return sTime+"至"+eTime+":每周"+value;
			}
			if("month"==row.meetingPeriodicity){
				var sTime = row.meetingStarttime.substring(0,10);
				var eTime = row.meetingEndtime.substring(0,10);
				return sTime+"至"+eTime+"每月"+value+"号";
			}
		}
		/**
		 *	预约情况周期渲染 
		 */
		 function modelFormatter(value, row, index){
				if("no"==row.meetingPeriodicity){
					return "非周期会议";
				}
				if("week"==row.meetingPeriodicity){
					return "周期会议（按周）";
				}
				if("month"==row.meetingPeriodicity){
					return "周期会议（按月）";
				}
			}
		//距离当前多少天的日期
		function GetDateStr(AddDayCount) {
			var dd = new Date();
			dd.setDate(dd.getDate()+AddDayCount);//获取AddDayCount天后的日期
			var y = dd.getFullYear();
			var m = dd.getMonth()+1;//获取当前月份的日期
			if(Number(m)<10){
			       m = "0"+m;
			     }
		     var d = dd.getDate();
		     if(Number(d)<10){
		       d = "0"+d;
		     }
			return y+"-"+m+"-"+d;
		}
</script>
<script type="text/javascript">
var fileServersURL = "${fileServersURL}";
var acount = '${acount }';
UE.Editor.prototype._bkGetActionUrl = UE.Editor.prototype.getActionUrl;
UE.Editor.prototype.getActionUrl = function(action) {
    if (action == 'uploadimage' || action == 'uploadscrawl' || action == 'uploadimage') 
    {
        return '<%=basePath%>sys/ueditorUpload/upload.action?comefrom=1&acount='+acount;
    }
    else if(action == 'catchimage')
    { 
    	var editor = UE.getEditor("editor");
    	var content = editor.getContent();
    	var imgs = UE.dom.domUtils.getElementsByTagName(this.document, "img");
    	var source =new Array();
    	$.each(imgs,function(index, element){
    		var src=$(element).attr('src');
    		source.push(src);
    	});
    	var url = fileServersURL+"/uploadFile/catcherImgUpload.action?comefrom=1&acount="+acount;
    	UE.ajax.getJSONP(url, {
    		source:source,fileServersURL:fileServersURL
        }, function(result) {
        	var list = result.list;
        	for(var i=0;i<imgs.length;i++){
        		var img = imgs[i];
        		$(img).attr('_src',list[i].source);
        		$(img).attr('src',list[i].source);
        	} 
        });
    	return;
    }
    else if(action=="uploadfile"||action=="listfile")
    {
    	 return '<%=basePath%>sys/ueditorUpload/uploadfile.action?comefrom=1&acount='+acount;
    }
    else
    {
        return this._bkGetActionUrl.call(this, action);
    }
}
function addfileimput(value){
	var i = parseInt(value)+1;
	var html = '<tr id="trid'+i+'">'
					+'<td>附件上传:</td>'
					+'<td><input id="fb'+i+'" name="upload" style="width:575px;height: 33px;"></td>'
					+"<td class=\"fileaddclass\">&nbsp;<a id=\"add"+i+"\" onclick=\"addfileimput("+i+")\"></a>"
						+"&nbsp;<a id=\"remove"+i+"\" onclick=\"removeTr("+i+")\"></a>"
					+'</td>'
				+'</tr>'
	$('#add'+value).hide();
	$('#remove'+value).hide();
	$('#filetable').append(html);
	$('#fb'+i).filebox({    
	    buttonText: '选择文件', 
	    buttonAlign: 'right'
	})
	$('#add'+i).linkbutton({    
	    iconCls: 'icon-add'   
	});  
	$('#remove'+i).linkbutton({    
	    iconCls: 'icon-remove'   
	});  
}
function removeTr(value){
	var i = parseInt(value)-1;
	$('#trid'+value).remove();
	$('#add'+i).show();
	$('#remove'+i).show();
}


</script>
<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>	
</body>
</html>