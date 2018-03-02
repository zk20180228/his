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
	<title>邮箱管理</title>
</head>
<body>
	<div id="analyzeEl" class="easyui-layout" data-options="fit:true">

		<div data-options="region:'west',split:false,collapsible:false" title="功能" style="width:230px;">
			<div id="analyzeCenEl" class="easyui-layout" data-options="fit:true">
				<div data-options="region:'north',split:false,border:false" style="padding-top:5px;padding-bottom:5px;">
					<a id="writeMail" onclick="writeMail()" data-options="iconCls:'icon-edit',plain:false" class="easyui-linkbutton" style="height: 30px;width: 45%">写信</a>
					<a id="getMail" data-options="hasDownArrow:true,iconCls:'icon-folder_up',plain:false" class="easyui-menubutton" style="height: 30px;width: 45%">收信</a>
					<div id="mm" style="display: none">
						<div onclick="getMail('all')" data-options="iconCls:'icon-undo'">收取所有账号</div>
						<div class="menu-sep"></div>
						<c:forEach items="${mailConfigList}" var="mailConfig">
							<div id=${mailConfig.id} onclick="getMail('${mailConfig.id}')"> ${mailConfig.email} </div>
						</c:forEach>
					</div>
					<%--<a id="config" onclick="config()" data-options="iconCls:'icon-folder_go',plain:true" class="easyui-linkbutton">设置</a>--%>
					<%--<a id="printMail" onclick="printMail()" data-options="iconCls:'icon-2012081511202',plain:true" class="easyui-linkbutton">打印</a>--%>
					<%--<div style="padding-right: 20px;float: right" >
						<label for="search"></label><input type="text" class="easyui-textbox" id="search" data-options="prompt:'主题、发件人、收件人、内容'" style="width:300px;" />
						<a href="javascript:searchMail()" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true" style="margin-top: 2px;">搜索</a>
					</div>--%>
				</div>
				<div data-options="region:'center',border:false,tools:'#toolSMId'" title="邮箱列表" style="">
					<div>
						<ul id="folder" style="margin-top: 10px;margin-left: 5px;">
						</ul>
					</div>
					<div id="tDtmm" class="easyui-menu" style="width:100px;">
						<div id="menuGet" onclick="menuGet()" data-options="iconCls:'icon-add'">收信</div>
					</div>
				</div>
			</div>
			<div id="toolSMId">
				<a href="javascript:void(0)" onclick="refresh()" class="icon-reload"></a>
				<a href="javascript:void(0)" onclick="collapseAll()" class="icon-fold"></a>
				<a href="javascript:void(0)" onclick="expandAll()" class="icon-open"></a>
			</div>
		</div>
		<div data-options="region:'center',border:false,collapsible:false">
			<div id="tabs" data-options="fit:true"></div>
		</div>
	</div>
</body>
<script type="text/javascript">
	var $tabs = $("#tabs");
	var $tree = $('#folder');
    var $getMail = $("#getMail");
    var configCount = ${mailConfigList.size()};
	if(configCount > 0){
        $getMail.menubutton({
            menu: '#mm'
        });
        $getMail.click(function() {
            getMail('all');
        });
    }else{
        $getMail.click(function() {
			$.messager.alert("提示","未设置邮箱或未刷新页面！");
        })
	}
    $tree.tree({
		url:'<%=basePath%>oa/mail/mailTree.action',
		onClick: function(node){
			if($tree.tree("isLeaf",node.target)){
				var id = $(this).tree('getParent',node.target).id;
				var tab = $tabs.tabs("getTab",node.text);
				if(tab){
					$tabs.tabs("select",node.text);
					tab = $tabs.tabs('getSelected');  // 获取选择的面板
					$tabs.tabs('update', {
						tab: tab,
						options: {
							content : "<iframe src="+getUrl(node.text, id)+" scrolling='no' frameborder='0' style='width:100%;height:100%;'></iframe>",
						}
					});
				}else{
					$tabs.tabs('add',{
						title:node.text,
//						href : getUrl(node.text, id),
						content : "<iframe src="+getUrl(node.text, id)+" scrolling='no' frameborder='0' style='width:100%;height:100%;'></iframe>",
						closable:true,
						bodyCls:"content"
					});
				}
			}else{
				$tabs.tabs("select", "邮箱首页");
				var homeTab = $tabs.tabs('getSelected');  // 获取选择的面板
				$tabs.tabs('update', {
					tab: homeTab,
					options: {
						href : "<%=basePath%>oa/mail/mailHome.action"
					}
				});
			}
		},onContextMenu: function(e,node){//添加右键菜单
			$(this).tree('select',node.target);
			if(node.id){
				e.preventDefault();
				$('#tDtmm').menu('show',{
					left: e.pageX,
					top: e.pageY
				});
			}
		}
	});
	$tabs.tabs({
		onBeforeClose : function (title,index){
			var target = this;
			if(title === "发邮件"){
				$tabs.tabs("select",title);
				if(typeof($("#writeIframe")[0].contentWindow.getContent) === "function"){
					var content = $("#writeIframe")[0].contentWindow.getContent();
				}
				var $attach = $("#writeIframe").contents().find("#attaList a");
				if(content || $attach.length>0){
					$.messager.confirm('确认','关闭后内容将丢失，是否关闭？',function(r){
						if (r){
							var opts = $(target).tabs('options');
							var bc = opts.onBeforeClose;
							opts.onBeforeClose = function(){};  // 允许现在关闭
							$(target).tabs('close',index);
							opts.onBeforeClose = bc;  // 还原事件函数
						}
					});
					return false;
				}
			}else if(title.indexOf("编辑草稿") === 0){
				$tabs.tabs("select",title);
				if(typeof($("#editDraftIframe")[0].contentWindow.getContent) === "function"){
					var content1 = $("#editDraftIframe")[0].contentWindow.getContent();
				}
				var $attach1 = $("#editDraftIframe").contents().find("#attaList a");
				if(content1 || $attach1.length>0){
					$.messager.confirm('确认','关闭后内容将丢失，是否关闭？',function(r){
						if (r){
							var opts = $(target).tabs('options');
							var bc = opts.onBeforeClose;
							opts.onBeforeClose = function(){};  // 允许现在关闭
							$(target).tabs('close',index);
							opts.onBeforeClose = bc;  // 还原事件函数
						}
					});
					return false;
				}
			}
		}
	});
	$tabs.tabs('add',{
		title:"邮箱首页",
		<%--href : "<%=basePath%>oa/mail/mailHome.action"--%>
	});
    function getUrl(text, id) {
        switch(text) {
            case "未读邮件" :
                return "<%=basePath%>oa/mail/page/unreadBox.action?id="+id;
            case "收件箱" :
                return "<%=basePath%>oa/mail/page/inBox.action?id="+id;
            case "草稿箱" :
                return "<%=basePath%>oa/mail/page/draftBox.action?id="+id;
            case "星标邮件" :
                return "<%=basePath%>oa/mail/page/starBox.action?id="+id;
            case "已发送" :
                return "<%=basePath%>oa/mail/page/sendBox.action?id="+id;
            case "已删除" :
                return "<%=basePath%>oa/mail/page/deleteBox.action?id="+id;
            case "垃圾邮件" :
                return "<%=basePath%>oa/mail/page/spamBox.action?id="+id;
        }
    }
    function refresh(){//刷新树
        $tree.tree('options').url = '<%=basePath%>oa/mail/mailTree.action';
        $tree.tree('reload');
    }
    function expandAll(){//展开树
        $tree.tree('expandAll');
    }
    function collapseAll(){//关闭树
        $tree.tree('collapseAll');
    }
    function menuGet(){//邮件收信按钮事件
        var cid = $tree.tree('getSelected').id;
        getMail(cid);
    }

    function writeMail(){
        var tab = $tabs.tabs("getTab","发邮件");
        if(tab){
            $tabs.tabs("select","发邮件");
        }else{
            $tabs.tabs('add',{
                title:"发邮件",
                <%--href: "<%=basePath%>oa/mail/editMail.action",--%>
                content : "<iframe id='writeIframe' src="+"<%=basePath%>oa/mail/page/editMail.action frameborder='0' style='width:100%;height:100%;'></iframe>",
                closable:true,
                bodyCls:"content"
            });
        }
    }
    function getMail(cid){
        $.messager.alert('提示信息','系统将在后台自动收取，稍后回来查看收取结果。');
        if(cid === "all"){
            $("#mm div[id]").each(function () {
                getMail1(this.id);
            });
        }else{
            getMail1(cid);
        }
    }
    function getMail1(cid){
        $.ajax({
            url: "<%=basePath%>oa/mail/receive.action",
            data: {
                cid: cid
            }
//            ,success: function (data) {
//                if (data.resCode === "error") {
//                    $.messager.alert('提示信息',data.resMsg);
//                }
//            }
        });
	}
    $("#mm div[id]").each(function () {
        var timing;//15分钟
        var cid = this.id;
        $.ajax({
			url: "<%=basePath%>oa/mail/queryReceiveTiming.action",
			data: {
                "mailConfig.id": cid
			},
			success: function (data) {
				if(data.resCode === "success"){
					timing = data.data*1000*60;
                    scheduleGetMail(cid,timing);
				}
			}
		});

    });
    //循环执行收信
	function scheduleGetMail(cid,timing) {
        window.setInterval(function(){
            getMail1(cid);
        }, timing);
    }

    function config(){
//        var $tabs = $("#tabs");
        var title = "设置邮箱";
        var tab = $tabs.tabs("getTab",title);
        if(tab){
            $tabs.tabs("select",title);
        }else{
            $tabs.tabs('add',{
                title:title,
                href: "<%=basePath%>oa/mail/page/config.action",
                closable:true,
                bodyCls:"content"
            });
        }
    }
    function searchMail() {
        var title = "搜索结果";
        var url = "<%=basePath%>oa/mail/page/searchMailList.action";
        var tab = $tabs.tabs("getTab",title);
        if(tab){
            $tabs.tabs("select",title);
            tab = $tabs.tabs('getSelected');
            tab.panel('refresh', url);
        }else{
            $tabs.tabs('add',{
                title:title,
                href: url,
                closable:true,
                bodyCls:"content"
            });
        }
    }
    function isPositiveInteger(s){//是否为正整数
        var re = /^[0-9]+$/ ;
        return re.test(s)
    }
</script>
</html>