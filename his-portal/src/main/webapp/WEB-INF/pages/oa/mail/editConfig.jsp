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
	<title>编辑邮箱配置</title>
</head>
<body>
		<div style="padding: 5px" data-options="border:false">
			<form id="editConfigForm" method="post">
				<div style="padding-left: 20px;padding-bottom: 20px">
					<a class="easyui-linkbutton" onclick="toggleConfig(this)"
					   data-options="iconCls:'icon-config'">手动设置</a>
					<a class="easyui-linkbutton" onclick="configHelp()"
					   data-options="iconCls:'icon-help'">帮助</a>
				</div>
				<div style="padding: 0px 5px 5px 5px;">
					<table class="honry-table" cellpadding="0" cellspacing="0" border="0" style="margin-left:auto;margin-right:auto;">
						<tr>
							<td class="honry-lable">
								邮箱账号：
							</td>
							<td >
								<input class="easyui-textbox" id="email" name="mailConfig.email" value="${mailConfig.email }"
									   data-options="readonly:true,required:true,validType:'email',missingMessage:'请填写邮箱账号!',invalidMessage:'邮箱格式不正确!'" />
								<input type="hidden" id = "id" name="mailConfig.id" value="${mailConfig.id }" />
							</td>
						<tr>
							<td class="honry-lable">
								密码或授权码：
							</td>
							<td>
								<input class="easyui-textbox" id="pwd" name="mailConfig.pwd"
									data-options="type:'password',required:true,missingMessage:'请填写邮箱密码!'"/>
							</td>
						</tr>
						<tr>
							<td class="honry-lable">
								帐户昵称：
							</td>
							<td>
								<input class="easyui-textbox" id="nickName" name="mailConfig.nickName" data-options="required:true" value="${mailConfig.nickName }"/>
							</td>
						</tr>
						<tr>
							<td class="honry-lable">
								收件类型：
							</td>
							<td>
								<input class="easyui-combobox" id="receiveType" name="mailConfig.receiveType"
									   value="${mailConfig.receiveType }"
									   data-options="required:true,missingMessage:'请选择收件服务器类型!',
									valueField:'text',textField:'text',editable:false,
									data:[{
										id:'1',
										text:'pop3',
									}<%--,{
										id:'2',
										text:'imap',
									}--%>
									]"/>
							</td>
						</tr>
					</table>
					<table id="handConfig" class="honry-table" cellpadding="0" cellspacing="0" border="0" style="margin-left:auto;margin-right:auto;display: none;">
						<tr>
							<td class="honry-lable">
								收件服务器：
							</td>
							<td>
								<input class="easyui-textbox" id="receiveHost" name="mailConfig.receiveHost" 
									value="${mailConfig.receiveHost }"
									data-options="required:true,missingMessage:'请选择收件服务器!'"/>
								&nbsp&nbsp

								<input id="receiveSecurity" name="mailConfig.receiveSecurity" type="checkbox" 
									value="${mailConfig.receiveSecurity }" style="width:15px;height:15px" onchange="receiveS(this)"
									   <c:if test="${mailConfig.receiveSecurity == 'ssl'}">checked="checked"</c:if>>

									<span>&nbspSSL&nbsp</span>
									<span>端口：</span>
									<input class="easyui-numberbox" id="receivePort" name="mailConfig.receivePort"
										value="${mailConfig.receivePort }" data-options="required:true,missingMessage:'请填写收件端口!'"/>
							</td>
						</tr>
						<tr>
							<td class="honry-lable">
								发件服务器：
							</td>
							<td>
								<input class="easyui-textbox" id="sendHost" name="mailConfig.sendHost" 
									value="${mailConfig.sendHost }" data-options="required:true,missingMessage:'请选择发件服务器!'"/>
								&nbsp&nbsp	
								<input id="sendSecurity" name = "mailConfig.sendSecurity" type="checkbox" 
									value="${mailConfig.sendSecurity }" style="width:15px;height:15px" onchange="sendSec(this)"
									   <c:if test="${mailConfig.sendSecurity == 'ssl'}">checked="checked"</c:if>/>
								<span>&nbspSSL&nbsp</span>
								<span>端口：</span>
								<input class="easyui-numberbox" id="sendPort" name="mailConfig.sendPort"
									   <c:if test="${mailConfig.sendPort==null }">value=25</c:if>
									   <c:if test="${mailConfig.sendPort!=null }">value="${mailConfig.sendPort}"</c:if>
									   data-options="required:true,missingMessage:'请填写发件端口!'"/>
							</td>
						</tr>
						<tr>
							<td class="honry-lable">
								定时收取：
							</td>
							<td>
								<input class="easyui-numberbox" id="timing" name="mailConfig.timing"
									   <c:if test="${mailConfig.timing==null }">value=15</c:if>
									   <c:if test="${mailConfig.timing!=null }">value="${mailConfig.timing}"</c:if>/>
								<span>&nbsp&nbsp&nbsp单位（分钟）&nbsp</span>
							</td>
						</tr>
					</table>
				</div>
				<div style="text-align: center; padding: 5px">
					<a href="javascript:testSend()" class="easyui-linkbutton"
						data-options="iconCls:'icon-folder_go'">测试发件</a>
					<a href="javascript:submit()" class="easyui-linkbutton"
						data-options="iconCls:'icon-save'">保存</a>
					<a href="javascript:close()" class="easyui-linkbutton"
						data-options="iconCls:'icon-close'">关闭</a>
				</div>
			</form>
		</div>
	<script>
    var $email = $("#email");
	if($email.val()){//修改
        $.ajax({
            url : "<%=basePath%>oa/mail/queryMailFromEmp.action",
            success: function(data1){
                if(data1){
                    if($email.val() === data1){
                        $("#receiveType").combobox({
                            readonly : true
                        });
                    }else if($email.val() !== data1){
                        $.messager.alert('提示',"员工管理的邮箱与您添加过的邮箱不一致，请删除旧邮箱后重新添加新邮箱！");
                        close();
                    }
                }else{
                    $.messager.alert('提示',"请先在组织机构管理-员工管理中设置邮箱！");
                    close();
                }
            }
        });
	}else{//新增
        $.ajax({
            url : "<%=basePath%>oa/mail/queryMailFromEmp.action",
            success: function(data1){
                if(data1){
                    $.ajax({
                        url : "<%=basePath%>oa/mail/queryMailFromConf.action",
                        success: function(data2){
                            if(data2){
                                if(data1 === data2){
                                    $.messager.alert('提示',"您已经添加过邮箱！");
                                    close();
                                }else if(data1 !== data2){
                                    $.messager.alert('提示',"员工管理的邮箱与您添加过的邮箱不一致，请删除旧邮箱后重新添加新邮箱！");
                                    close();
                                }
							}else{
                                $("#email").textbox("setText", data1);
                                $("#email").textbox("setValue", data1);
							}
                        }
                    });
                }else{
                    $.messager.alert('提示',"请先在组织机构管理-员工管理中设置邮箱！");
                    close();
                }
            }
        });
	}
	$("#receiveType").combobox({
		onChange : function(newValue){
		    if(isNeedSsl($("#email").textbox("getValue"))){
                if(newValue === "pop3"){
                    $("#receivePort").numberbox("setValue", 995);
                }else if(newValue === "imap"){
                    $("#receivePort").numberbox("setValue", 993);
                }
            }else{
                $("#receiveSecurity")[0].checked = false;
                $("#sendSecurity")[0].checked = false;
                $("#sendPort").numberbox("setValue", 25);
                if(newValue === "pop3"){
                    $("#receivePort").numberbox("setValue", 110);
                }else if(newValue === "imap"){
                    $("#receivePort").numberbox("setValue", 143);
                }
			}
			if($("#email").textbox("isValid")){
                var receiveHost = getHost($("#email").textbox("getValue"),newValue);
                $("#receiveHost").textbox('setText',receiveHost);
                $("#receiveHost").textbox('setValue',receiveHost);
            }else{
                $("#receiveHost").textbox('setText',"");
                $("#receiveHost").textbox('setValue',"");
			}
		}
	});
    $("#email").textbox({
        onChange : function(newValue) {
            if ($("#email").textbox("isValid")) {
                var receiveHost = getHost(newValue, $("#receiveType").combobox("getValue"));
                var sendHost = getHost(newValue, "smtp");
                $("#receiveHost").textbox('setText', receiveHost);
                $("#receiveHost").textbox('setValue', receiveHost);
                $("#sendHost").textbox('setText', sendHost);
                $("#sendHost").textbox('setValue', sendHost);
				if(isNeedSsl(newValue)){
                    $("#receiveSecurity")[0].checked = true;
                    if($("#receiveType").combobox("getValue") === "pop3"){
                        $("#receivePort").numberbox("setValue", 995);
					}else if($("#receiveType").combobox("getValue") === "imap"){
                        $("#receivePort").numberbox("setValue", 993);
					}
                    $("#sendSecurity")[0].checked = true;
                    $("#sendPort").numberbox("setValue", 465);
				}else{
                    $("#receiveSecurity")[0].checked = false;
                    if($("#receiveType").combobox("getValue") === "pop3"){
                        $("#receivePort").numberbox("setValue", 110);
                    }else if($("#receiveType").combobox("getValue") === "imap"){
                        $("#receivePort").numberbox("setValue", 143);
                    }
                    $("#sendSecurity")[0].checked = false;
                    $("#sendPort").numberbox("setValue", 25);
                }
            }else{
                $("#receiveHost").textbox('setText',"");
                $("#receiveHost").textbox('setValue',"");
                $("#sendHost").textbox('setText',"");
                $("#sendHost").textbox('setValue',"");
            }
        }
    });
    function isNeedSsl(email) {
        var i = email.indexOf("@");
        if(i !== -1) {
            var em = email.substring(i+1);
            //qq邮箱必须ssl
            if (em.indexOf("qq") === 0 || em.indexOf("vip.qq") === 0 || em.indexOf("foxmail") === 0) {
                return true;
            } else {
                return false;
            }
        }
    }
	function getHost(email,type){
        var i = email.indexOf("@");
        if(i !== -1){
            var em = email.substring(i+1);
            if(em.indexOf("hotmail") === 0 || em.indexOf("live") === 0 || em.indexOf("msn") === 0){
                if("pop3" === type){
                    return "pop-mail.outlook.com";
				}else if("imap" === type){
                    return "imap-mail.outlook.com";
				}else if("smtp" === type){
                    return "smtp-mail.outlook.com";
                }
            }else if(em.indexOf("yahoo") === 0){
                if("pop3" === type){
                    return "pop.mail.yahoo.com";
                }else if("imap" === type){
                    return "imap.mail.yahoo.com";
                }else if("smtp" === type){
                    return "smtp.mail.yahoo.com";
                }
            }else if(em.indexOf("foxmail") === 0){
                if("pop3" === type){
                    return "pop.qq.com";
                }else if("imap" === type){
                    return "imap.qq.com";
                }else if("smtp" === type){
                    return "smtp.qq.com";
                }
            }else if(em.indexOf("vip.") === 0){
                em = em.substring(4);
                if("pop3" === type){
                    return "pop."+em;
                }else if("imap" === type){
                    return "imap."+em;
                }else if("smtp" === type){
                    return "smtp."+em;
                }
			}else{
                if("pop3" === type){
                    return "pop."+em;
                }else if("imap" === type){
                    return "imap."+em;
                }else if("smtp" === type){
                    return "smtp."+em;
                }
			}
        }
	}
	function receiveS(dom){
		var type = $("#receiveType").combobox("getValue");
		if(type === "pop3" && dom.checked){
			$("#receivePort").numberbox("setValue", 995);
		}else if(type === "pop3" && !dom.checked){
			$("#receivePort").numberbox("setValue", 110);
		}else if(type === "imap" && dom.checked){
			$("#receivePort").numberbox("setValue", 993);
		}else if(type === "imap" && !dom.checked){
			$("#receivePort").numberbox("setValue", 143);
		}
	}
	function sendSec(dom){
		if(dom.checked){
			$("#sendPort").numberbox("setValue", 465);
		}else{
			$("#sendPort").numberbox("setValue", 25);
		}
	}
	function submit() {
		$('#editConfigForm').form('submit', {
			url : "<%=basePath%>oa/mail/addOrUpdateConfig.action",
			onSubmit : function() {
				if (!$('#editConfigForm').form('validate')) {
					$.messager.alert('提示',"填写有误，请检查!");
					return false;
				}
				if($("#timing").val() < 10){
                    $.messager.alert('提示',"定时收取不能小于10分钟!");
                    return false;
				}
				if($("#receiveSecurity:checked")){
					$("#receiveSecurity").val("ssl");
				}
				if($("#sendSecurity:checked")){
					$("#sendSecurity").val("ssl");
				}
			},
			success : function(data) {
				data = jQuery.parseJSON(data);
		    	if(data.resCode === "success"){
                    $.messager.alert('提示',"保存成功！");
					close();
					$("#configBox").datagrid("reload");
		    	}else if(data.resCode === "163"){
                    $.messager.confirm('提示',data.resMsg,function(r){
                        if(r){
							window.open(data.href);
						}
					});
		    	}else if(data.resCode === "needSsl"){
                    $.messager.alert('提示',data.resMsg);
                    if ($("#handConfig").css("display") === "none") {
                        $("#handConfig").show();
                        $(dom).linkbutton({
                            text: "自动设置"
                        });
                    }
                }else if(data.resCode === "error"){
                    $.messager.alert('提示',data.resMsg);
					if ($("#handConfig").css("display") === "none") {
                        $("#handConfig").show();
                        $(dom).linkbutton({
                            text: "自动设置"
                        });
                    }
                }
			},
			error : function(data) {
				$.messager.alert('提示',data.resMsg);
			}
		});
	}
	function toggleConfig(dom) {
        if ($("#handConfig").css("display") === "none") {
            $("#handConfig").show();
            $(dom).linkbutton({
                text: "自动设置"
            });
        } else {
            $("#handConfig").hide();
            $(dom).linkbutton({
                text: "手动设置"
            });
        }
    }
    function configHelp() {
		window.open("http://service.mail.qq.com/cgi-bin/help?subtype=1&&id=28&&no=371");
    }
	function testSend() {
		$('#editConfigForm').form('submit', {
			url : "<%=basePath%>oa/mail/testSend.action",
			onSubmit : function() {
				if (!$('#editConfigForm').form('validate')) {
					$.messager.alert('提示',"填写有误，请检查!");
					return false;
				}
				if($("#receiveSecurity:checked")){
					$("#receiveSecurity").val("ssl")
				}
				if($("#sendSecurity:checked")){
					$("#sendSecurity").val("ssl")
				}
			},
			success : function(data) {
                data = jQuery.parseJSON(data);
                if(data.resCode === "success"){
                    $.messager.alert("提示","配置正确！");
		    	}else if(data.resCode === "163"){
                    $.messager.confirm('提示',data.resMsg,function(r){
                        if(r){
                            window.open(data.href);
                        }
                    });
                }else if(data.resCode === "needSsl"){
                    $.messager.alert('提示',data.resMsg);
                    if ($("#handConfig").css("display") === "none") {
                        $("#handConfig").show();
                        $(dom).linkbutton({
                            text: "自动设置"
                        });
                    }
                }else if(data.resCode === "error"){
                    $.messager.alert("提示",data.resMsg);
					if ($("#handConfig").css("display") === "none") {
                        $("#handConfig").show();
                        $(dom).linkbutton({
                            text: "自动设置"
                        });
                    }
		    	}
			},
			error : function(data) {
				$.messager.alert('提示',data.resMsg);
			}
		});
	}

	function close() {
		$("#configMailWindow").window('close');
	}

</script>	
</body>
</html>