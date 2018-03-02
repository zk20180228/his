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
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>写信</title>
</head>
<body style="overflow-x:hidden;overflow-y:auto;">
<div style="margin-top: 10px;margin-left: 10px">
	<a href="javascript:sendOrDraft(1)" class="easyui-linkbutton" data-options="iconCls:'icon-disk_upload',plain:true" >发送</a>
	<a href="javascript:sendOrDraft(2)" class="easyui-linkbutton" data-options="iconCls:'icon-disk_download',plain:true" >存为草稿</a>
	<a href="javascript:reload()" class="easyui-linkbutton" data-options="iconCls:'icon-reload',plain:true" >刷新</a>
</div>
<div style="padding: 10px;">
	<form id="editMailForm" method="post">
	<table style="width: 70%;height: 22px">
		<tr>
			<td style="width: 80px"><span>发件人：</span></td>
			<td>
				<div style="margin-top: 8px;">
					<input id="cid" name="mailMessage.cid" style="width: 30%">
				</div>
			</td>
			<td>
				<input type="hidden" id="mid" name="mailMessage.id" value="${mailMessage.id}"/>
			</td>
		</tr>
		<tr>
			<td style="width:80px;height: 20px"><span>收件人：</span></td>
			<td>
				<div style="margin-top: 8px;">
					<input  id="reveiveEmail" name="mailMessage.receiveEmail" value="${mailMessage.receiveEmail}"
						class="easyui-textbox" data-options="required:true" style="width: 100%">
				</div>
			</td>
		</tr>
		<tr style="display: none">
			<td style="width:80px;height: 20px"><span>抄送人：</span></td>
			<td>
				<div style="margin-top: 8px;">
					<input  id="cc" name="mailMessage.cc"
						class="easyui-textbox" data-options="validType:'email'" style="width: 100%">
				</div>
			</td>
		</tr>
		<tr style="display: none">
			<td style="width:80px;height: 20px"><span>密送人：</span></td>
			<td>
				<div style="margin-top: 8px;">
					<input  id="bcc" name="mailMessage.bcc"
						class="easyui-textbox" data-options="validType:'email'" style="width: 100%">
				</div>
			</td>
		</tr>
		<tr>
			<td style="width:80px;height: 20px"><span>主题：</span></td>
			<td>
				<div style="margin-top: 8px;">
					<input  id="subject" name="mailMessage.subject" value="${mailMessage.subject}"
						class="easyui-textbox" style="width: 100%">
				</div>
			</td>
		</tr>
		<tr>
			<td style="width:80px;height: 20px"></td>
			<td>
				<div style="margin-top: 5px;">
					<a href="javascript:addAtta()" class="easyui-linkbutton" data-options="iconCls:'',plain:true" >添加附件</a><span>20M</span>
					<input type="file" id="attachment" name="uploadFile" style="display: none;" onchange="ajaxFileUpload()"/>
					<input type="hidden" id="attachInfo" name="mailMessage.attachInfo" value="${mailMessage.attachInfo}" />
				</div>
				<div id="attaList">
					<c:forEach items="${mailAttachmentList}" var="mailAttachment">
						<div title="${mailAttachment.fileName}" url="${mailAttachment.fileUrl}" size="${mailAttachment.fileSize}" style="background:#DFE0E4;margin-right:5px;padding:2px;float:left">
							<span>${mailAttachment.fileName}</span><span>(${mailAttachment.fileSize})</span>
							<a onclick="removeAtta(this)" class="easyui-linkbutton" data-options="plain:true"><span style="color:#034EF7;">删除</span></a>
						</div>
					</c:forEach>
				</div>
			</td>
		</tr>
	</table>
		<div style="margin-top: 5px;">
			<table style="width: 100%">
				<tr>
					<td>
						<textarea id="htmlbody" name="htmlbody" style="width: 80%;height: 500px">${mailMessage.content}</textarea>
						<input type="hidden" id="content" name="mailMessage.content"/>
					</td>
				</tr>
			</table>
		</div>
		<div style="margin-top: 10px;" >
			<span>其他选项：</span>
<%--			<input type="checkbox" id="saveToSend" name="mailMessage.saveToSend" value=${mailMessage.saveToSend}
				<c:if test="${mailMessage.saveToSend == 1}">checked="checked"</c:if> />
			<label for="saveToSend">保存到“已发送”</label>--%>
			<input type="checkbox" id="priority" name="mailMessage.priority" value="${mailMessage.priority}"
				   <c:if test="${mailMessage.priority == 1}">checked="checked"</c:if>/>
			<label for="priority">紧急</label>
			<input type="checkbox" id="receipt" name="mailMessage.receipt" value="${mailMessage.receipt}"
				   <c:if test="${mailMessage.receipt == 1}">checked="checked"</c:if>/>
			<label for="receipt">需要回执</label>
		</div>
	</form>

</div>
<link rel="stylesheet" href="<%=basePath %>kissy_editor/themes/default/editor-min.css" type="text/css"/>
<script type="text/javascript" src="<%=basePath %>kissy_editor/editor-aio.js"></script>
<script type="text/javascript" src="<%=basePath %>javascript/js/ajaxfileupload.js"></script>

<script type="text/javascript">
var $tabs = window.parent.$("#tabs");
var editorObj = KISSY.Editor("htmlbody");
function getContent() {
    return editorObj.getData();
}
$('#cid').combobox({
    url:'<%=basePath%>oa/mail/querySenderByEmpCode.action',
    queryParams: {
        "mid" : $("#mid").val(),
    },
    required:true,
    showItemIcon:true,
    editable:false,
    valueField:'id',
    textField:'sender',
    formatter: function(row){
		return row.nickName+"&lt"+row.email+"&gt";
	},
});
/***
 * 发送操作
 */
function sendOrDraft(markType){
    var url = "";
    if(markType ===1){
        url = "<%=basePath%>oa/mail/send.action";
	}else if(markType ===2){
        url = "<%=basePath%>oa/mail/saveToDraft.action";
    }
    $('#editMailForm').form('submit',{
	    url : url,
	    onSubmit : function(){
	    	var isValid = $(this).form('validate');
			if (!isValid){
				$.messager.show({
					title:'友情提示',
					msg:'请输入收件人!',
					timeout:5000,
					showType:'slide'
				});
				return false;
			}
	    	//将内容赋值
	    	var chtml = getContent();
	    	if(chtml == ""){
				$.messager.show({
					title:'友情提示',
					msg:'请输入正文!',
					timeout:5000,
					showType:'slide'
				});
				return false;
	    	}
	    	$('#content').val(chtml);
            $("#attachInfo").val("");
	    	var attachInfo = "";
	    	$("#attaList div").each(function(i){
                if(i === 0){
                    attachInfo = $(this).attr("title") +";"+ $(this).attr("url") +";"+ $(this).attr("size");
                }else{
                    attachInfo += "#"+ $(this).attr("title") +";"+ $(this).attr("url") +";"+ $(this).attr("size");
				}
			});
            $("#attachInfo").val(attachInfo);
            if($("#priority:checked")){
                $("#priority").val("1");
			}
            if($("#receipt:checked")){
                $("#receipt").val("1");
			}

        },
	    success:function(data){
//	    	data = jQuery.parseJSON(data);
	    	var data = eval("("+data+")");
	    	if(data.resCode === "success"){
	    	    var content = "";
	    	    if(markType === 1){
					content = "发送成功！是否继续写信？";
				}else if(markType === 2){
        			content = "保存成功！是否继续写信？";
				}
                $.messager.confirm('提示',content,function(r){
                    var tab = $tabs.tabs('getSelected');
                    if(r){
                        var tab1 = $tabs.tabs("getTab","发邮件");
                        $tabs.tabs('update', {
							tab: tab,
							options: {
								title : "发邮件",
								content : "<iframe id='writeIframe' src="+"<%=basePath%>oa/mail/page/editMail.action frameborder='0' style='width:100%;height:100%;'></iframe>"
							}
						});
						if(tab1){
                            $tabs.tabs("close",$tabs.tabs("getTabIndex",tab));
                            $tabs.tabs("select", $tabs.tabs("getTabIndex",tab1));
                        }
                    }else{
                        $tabs.tabs('update', {
                            tab: tab,
                            options: {
                                title : "发邮件",
                                content : "<iframe id='writeIframe' src="+"<%=basePath%>oa/mail/page/editMail.action frameborder='0' style='width:100%;height:100%;'></iframe>"
                            }
                        });
                        $tabs.tabs("close",$tabs.tabs("getTabIndex",tab));
					}
                });
				
	    	}else{
	    		$.messager.alert("提示",data.resMsg);
	    	}
	    }    
	}); 
}

/***
 * 刷新页面
 */
function reload(){
	var content = editorObj.getData();
	if(content || $("#attaList div").length>0){
    	$.messager.confirm('确认','刷新后内容将丢失，是否刷新？',function(r){
    	    if (r){
				var tab = $tabs.tabs('getSelected');
                $tabs.tabs('update', {
                    tab: tab,
                    options: {
                        content : "<iframe id='writeIframe' src="+"<%=basePath%>oa/mail/page/editMail.action frameborder='0' style='width:100%;height:100%;'></iframe>"
                    }
                });
    	    }
    	});
	}else{
		var tab = $tabs.tabs('getSelected');
        $tabs.tabs('update', {
            tab: tab,
            options: {
                content : "<iframe id='writeIframe' src="+"<%=basePath%>oa/mail/page/editMail.action frameborder='0' style='width:100%;height:100%;'></iframe>"
            }
        });
	}
}
function addAtta(){
	$("#attachment").click();
}
function removeAtta(dom){
	$(dom).parent().remove();
}
function ajaxFileUpload(){
	var fileName = $("#attachment").val().substring($("#attachment").val().lastIndexOf("\\")+1);
	if($("#attaList div[title='"+fileName+"']").size() > 0){
		$("#attachment").val("");
		$.messager.alert("提示","下列文件已存在，请重新命名后上传</br>"+fileName);
		return false;
	}
	var html = '<div title=\"'+fileName+'\" style="background:#DFE0E4;margin-right:5px;padding:3px;float:left"><span>'+fileName+'</span><span>正在上传。。。</span>' +
		'<a onclick="removeAtta(this)" class="easyui-linkbutton l-btn l-btn-small l-btn-plain" data-options="plain:true">' +
        '<span class="l-btn-left"><span class="l-btn-text"><span style="color:#034EF7;">删除</span></span></span></a></div>';
	$("#attaList").append(html);
	$.ajaxFileUpload({
    	url: '<%=basePath%>oa/mail/uploadFile.action', //用于文件上传的服务器端请求地址
		type: 'post',
		data: { 'mailMessage.attachInfo': fileName}, //此参数非常严谨，写错一个引号都不行
		secureuri: false, //一般设置为false
		fileElementId: 'attachment', //文件上传空间的id属性  <input type="file" id="file" name="file" />
		dataType: 'json', //返回值类型 一般设置为json
		success: function (data, status){  //服务器成功响应处理函数
			if(data.resCode === "success"){
				$("#attaList div[title='"+fileName+"']").attr("url",data.url);
				$("#attaList div[title='"+fileName+"']").attr("size",data.size);
				$("#attaList span:contains('"+fileName+"')+span").html("("+data.size+")");
			}else if(data.resCode === "error"){
                $("#attaList div[title='"+fileName+"']").remove();
				$.messager.alert("提示",data.resMsg);
			}
		},
		error: function (data, status, e){//服务器响应失败处理函数
			$.messager.alert("提示",e);
		}
	})
    return false;
}

</script>
</body>
</html>