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
</head>
<%-- <body style="overflow-x:auto;overflow-y:auto;">
<div style="padding: 20px;">
${content }
</div>
</body> --%>
<body>
<div id="mailcontainer" style="margin: 10px">
	<div style="margin-top: 10px;margin-left: 10px">
		<div >
			<a href="javascript:forwarding(1)" class="easyui-linkbutton" data-options="iconCls:'icon-disk_upload',plain:true" >回复</a>
			<a href="javascript:forwarding(2)" class="easyui-linkbutton" data-options="iconCls:'icon-disk_download',plain:true" >转发</a>
			<c:if test="${mailMessage.folder != 4}">
				<a href="javascript:delete1()" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true" >删除</a>
			</c:if>
			<a href="javascript:delete2()" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true" >彻底删除</a>
			<a href="javascript:getLastMail()" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true" >上一封</a>
			<a href="javascript:getNextMail()" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true" >下一封</a>
		</div>
	</div>
	<div style="margin-top: 10px;margin-left: 10px">
		<div id="subjectId" style="margin-top: 10px;margin-left: 10px">
			<h2><b>${mailMessage.subject }</b></h2>
		</div>
		<div style="margin-top: 10px;margin-left: 10px">
			<table>
				<tbody>
					<tr>
						<td><span>发件人：</span></td>
						<td><span>${mailMessage.sendEmail }</span></td>
						<td><input type="hidden" id="mailMessageId" value="${mailMessage.id }"></td>
					</tr>
					<tr>
						<td><span>收件人：</span></td>
						<td><span>${mailMessage.receiveEmail }</span></td>
					</tr>
					<tr id="ccId" style="display: none">
						<td><span>抄送人：</span></td>
						<td><span>${mailMessage.cc }</span></td>
					</tr>
					<tr>
						<td><span>时&nbsp&nbsp间：</span></td>
						<td><span>${mailMessage.sendDate }</span></td>
					</tr>
					<c:if test="${mailAttachmentList.size()>0 }">
						<tr>
							<td>附件：</td>
						</tr>
						<c:forEach var="attach" items="${mailAttachmentList }">
							<tr>
								<td></td>
								<td>
									<a href="<%=basePath%>oa/mail/downAttach.action?mailAttachment.id=${attach.id }">${attach.fileName }</a>
								</td>
							</tr>
						</c:forEach>
					</c:if>
				</tbody>
			</table>
		</div>
	</div>
	<div style="margin-top: 20px;margin-left: 10px;height: 70% ">
		<div style="margin-top: 10px; margin-left: 10px">${mailMessage.content }</div>
	</div>
</div>
<script type="text/javascript">
    var $tabs = window.parent.$('#tabs');
	function forwarding(type){
    	var id = $("#mailMessageId").val();
    	if(id == null || id.length === 0){
    		$.messager.alert('提示','未选中任何邮件!');
    		return false;
    	}
    	var subject = $("#subjectId").text();
    	if(type === 1){
	    	var title = "回复："+subject.substring(0,20);
    	}else if(type === 2){
	    	var title = "转发："+subject.substring(0,20);
    	}
    	var tab = $tabs.tabs("getTab",title);
		if(tab){
            $tabs.tabs("select",title);
		}else{
            if(type === 1){
                var url ='<%=basePath%>oa/mail/page/reply.action?id='+id;
            }else if(type === 2){
                var url ='<%=basePath%>oa/mail/page/forwarding.action?id='+id;
            }
            $tabs.tabs('add',{
			    title:title,
//			    href: url,
				content : "<iframe src="+url+" scrolling='no' frameborder='0' style='width:100%;height:100%;'></iframe>",
                closable:true,
			    bodyCls:"content"
			});
		}
	}
	function delete1(){
        var id = $("#mailMessageId").val();
        if(id == null || id.length === 0){
            $.messager.alert('提示','未选中任何邮件!');
            return false;
        }
        moveFolder(id, 4);
	}
	function delete2(){
        var id = $("#mailMessageId").val();
        if(id == null || id.length === 0){
            $.messager.alert('提示','未选中任何邮件!');
            return false;
        }
        $.messager.confirm('确认','彻底删除后邮件将无法恢复，您确定要删除吗？',function(r){
            if (r){
                markMail(id, 5);
            }
        });
	}
	function getLastMail() {
        var id = $("#mailMessageId").val();
	    var lastMailId = "";
	    var lastMailSubject = "";
        $.ajax({
            url : "<%=basePath%>oa/mail/getLastMailById.action",
            data : {
                "mailMessage.id" : id
            },
            type : "post",
            dataType : "json",
            success : function (data) {
                if(data.resCode === "success"){
                    lastMailId = data.data.id;
                    lastMailSubject = data.data.subject;
                    if(lastMailSubject.length > 20){
                        lastMailSubject = lastMailSubject.substring(0,20)+"...";
                    }
                    markMail(lastMailId, 1);
                    var url="<%=basePath%>/oa/mail/page/contentView.action?id="+lastMailId;
                    var tab = $tabs.tabs('getSelected');
                    $tabs.tabs("update",{
                        tab: tab,
                        options: {
                            title: lastMailSubject,
                            content : "<iframe src="+url+" frameborder='0' style='width:100%;height:100%;'></iframe>",
                        }
                    });
                }else if(data.resCode === "firstOne"){
                    $.messager.alert('提示','已经是第一封!');
                }
            }
        });

    }
    function getNextMail() {
        var id = $("#mailMessageId").val();
        var nextMailId = "";
        var nextMailSubject = "";
        $.ajax({
            url : "<%=basePath%>oa/mail/getNextMailById.action",
            data : {
                "mailMessage.id" : id
            },
            type : "post",
            dataType : "json",
            success : function (data) {
                if(data.resCode === "success"){
                    nextMailId = data.data.id;
                    nextMailSubject = data.data.subject;
                    if(nextMailSubject.length > 20){
                        nextMailSubject = nextMailSubject.substring(0,20)+"...";
                    }
                    markMail(nextMailId, 1);
                    var url="<%=basePath%>/oa/mail/page/contentView.action?id="+nextMailId;
                    var tab = $tabs.tabs('getSelected');
                    $tabs.tabs("update",{
                        tab: tab,
                        options: {
                            title: nextMailSubject,
                            content : "<iframe src="+url+" frameborder='0' style='width:100%;height:100%;'></iframe>",
                        }
                    });
                }else if(data.resCode === "lastOne"){
                    $.messager.alert('提示','已经是最后一封!');
				}
            }
        });

    }
    function markMail(ids, markType){
        $.ajax({
            url : "<%=basePath%>oa/mail/markMail.action",
            data : {
                "mailMessage.id" : ids,
                "markType" : markType
            },
            type : "post",
            async : false,//必须同步，否则查看上下一封信页面刷新后不能发出此ajax请求
            dataType : "json",
            success : function (data) {
                if(data.resCode === "success"){
                    if(markType === 5){
                        var tab = $tabs.tabs('getSelected');
                        $tabs.tabs("close",$tabs.tabs("getTabIndex", tab));
                    }
                }
            }
        });
    }
    function moveFolder(ids, folderType){
        $.ajax({
            url : "<%=basePath%>oa/mail/moveFolder.action",
            data : {
                "mailMessage.id" : ids,
                "markType" : folderType
            },
            type : "post",
            dataType : "json",
            success : function (data) {
                if(data.resCode === "success"){
                    var tab = $tabs.tabs('getSelected');
                    $tabs.tabs("close",$tabs.tabs("getTabIndex", tab));
                }
            }
        });
    }
</script>
</body>
</html>