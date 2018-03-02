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
	<title>星标邮件</title>
</head>
<body>
	<table id="starBox" title="星标邮件" 
       data-options="">
    <thead>
        <tr>
			<th data-options="field:'ck',checkbox:true"></th>
			<!-- <th data-options="field:'starFlg',width:'5%'">状态</th> -->
			<th data-options="field:'sendName',width:'15%'">发件人</th>
			<th data-options="field:'subject',width:'55%'">主题</th>
			<th data-options="field:'sendDate',width:'13%'">时间</th>
			<th data-options="field:'emailSize',width:'5%',formatter:formatterSize">大小</th>
			<th data-options="field:'starFlg',width:'2%',align:'center',formatter:formatterStar">星标</th>
        </tr>
    </thead>
</table>
	<script type="text/javascript">
        var $tabs = window.parent.$('#tabs');
	    function getSelectionsIds(){
	    	var starBox = $("#starBox");
	    	var sels = starBox.datagrid("getSelections");
	    	var ids = [];
	    	for(var i in sels){
	    		if(sels[i].id){
	    			ids.push(sels[i].id);
	    		}
	    	}
	    	if(ids.length != 0){
		    	ids = ids.join(",");
	    	}
	    	return ids;
	    }
        $('#starBox').datagrid({
            fit:true,
            url:'<%=basePath%>oa/mail/queryStarBoxMail.action?cid=${id}',
            method:'get',
            singleSelect:false,
            fitColumns:true,
            pagination:true,
            pageSize:20,
            rowStyler: function(index,row){
                if (row.unreadFlg == 0){
                    return 'font-weight:bold;color:000000;';
                }
            },
            toolbar: [{
                text:'删除',
                iconCls:'icon-remove',
                handler:function(){
                    var ids = getSelectionsIds();
                    if(ids.length === 0){
                        $.messager.alert('提示','未选中任何邮件!');
                        return false;
                    }
                    moveFolder(ids, 4);
                }
            },'-',{
                text:'彻底删除',
                iconCls:'icon-remove',
                handler:function(){
                    var ids = getSelectionsIds();
                    if(ids.length === 0){
                        $.messager.alert('提示','未选中任何邮件!');
                        return ;
                    }
                    $.messager.confirm('确认','彻底删除后邮件将无法恢复，您确定要删除吗？',function(r){
                        if (r){
                            markMail(ids, 5);
                        }
                    });
                }
            },'-',{
                text:'转发',
                iconCls:'icon-add',
                handler:function(){
                    var ids = getSelectionsIds();
                    if(ids.length === 0){
                        $.messager.alert('提示','未选中任何邮件!');
                        return false;
                    }
                    if(ids.indexOf(',') > 0){
                        $.messager.alert('提示','只能转发一条!');
                        return false;
                    }
                    var row = $("#starBox").datagrid("getSelections")[0];
                    var subject;
                    if(row.subject != null){
                        subject = row.subject.substring(0,20);
                    }else{
                        subject = "";
                    }
                    var title = "转发：" + subject;
                    
                    var tab = $tabs.tabs("getTab",title);
                    if(tab){
                        $tabs.tabs("select",title);
                    }else{
                        $tabs.tabs('add',{
                            title:title,
                            <%--href: '<%=basePath%>oa/mail/page/forwarding.action?id='+ids,--%>
                            content : "<iframe src="+"<%=basePath%>oa/mail/page/forwarding.action?id="+ids+" scrolling='no' frameborder='0' style='width:100%;height:100%;'></iframe>",
                            closable:true,
                            bodyCls:"content"
                        });
                    }
                }
            },'-',{
                text:'标记为已读',
                iconCls:'icon-add',
                type:'easyui-menubutton',
                handler:function(){
                    var ids = [];
                    var rows = $("#starBox").datagrid("getSelections");
                    if(rows.length === 0){
                        $.messager.alert('提示','未选中任何邮件!');
                        return false;
                    }
                    for(var i=0; i<rows.length; i++){
                        if(rows[i].unreadFlg === 0){
                            if(rows[i].id){
                                ids.push(rows[i].id);
                            }
                        }
                    }
                    if (ids.length !== 0) {
                        ids = ids.join(",");
                        markMail(ids, 1);
                    }
                }
            },'-',{
                text:'标记为未读',
                iconCls:'icon-add',
                type:'easyui-menubutton',
                handler:function(){
                    var ids = [];
                    var rows = $("#starBox").datagrid("getSelections");
                    if(rows.length === 0){
                        $.messager.alert('提示','未选中任何邮件!');
                        return false;
                    }
                    for(var i=0; i<rows.length; i++){
                        if(rows[i].unreadFlg === 1){
                            if(rows[i].id){
                                ids.push(rows[i].id);
                            }
                        }
                    }
                    if (ids.length !== 0) {
                        ids = ids.join(",");
                        markMail(ids, 2);
                    }
                }
            },'-',{
                text:'标记为星标邮件',
                iconCls:'icon-add',
                handler:function(){
                    var ids = [];
                    var rows = $("#starBox").datagrid("getSelections");
                    if(rows.length === 0){
                        $.messager.alert('提示','未选中任何邮件!');
                        return false;
                    }
                    for(var i=0; i<rows.length; i++){
                        if(rows[i].starFlg === 0){
                            if(rows[i].id){
                                ids.push(rows[i].id);
                            }
                        }
                    }
                    if (ids.length !== 0) {
                        ids = ids.join(",");
                        markMail(ids, 3);
                    }
                }
            },'-',{
                text:'取消星标',
                iconCls:'icon-add',
                handler:function(){
                    var ids = [];
                    var rows = $("#starBox").datagrid("getSelections");
                    if(rows.length === 0){
                        $.messager.alert('提示','未选中任何邮件!');
                        return false;
                    }
                    for(var i=0; i<rows.length; i++){
                        if(rows[i].starFlg === 1){
                            if(rows[i].id){
                                ids.push(rows[i].id);
                            }
                        }
                    }
                    if (ids.length !== 0) {
                        ids = ids.join(",");
                        markMail(ids, 4);
                    }
                }
            }],
            onDblClickRow:function(index, row){
                var url="<%=basePath%>/oa/mail/page/contentView.action?id="+row.id;
                var subject;
                if(row.subject != null){
                    subject = row.subject;
                    if(subject.length > 20){
                        subject = subject.substring(0,20)+"...";
                    }
                }else{
                    subject = "无主题";
                }
                var title = subject;
                
                var tab = $tabs.tabs("getTab",title);
                if(tab){
                    $tabs.tabs("select",title);
                }else{
                    $tabs.tabs('add',{
                        title:title,
//					    href: url,
                        content : "<iframe src="+url+" frameborder='0' style='width:100%;height:100%;'></iframe>",
                        closable:true,
                        bodyCls:"content"
                    });
                }
                if(row.unreadFlg === 0){//如果是未读标记为已读
                    markMail(row.id, 1);
                }
            },
            onLoadSuccess:function () {
                $("a.toggleStar").linkbutton();
            }
        });
        function markMail(ids, markType){
            $.ajax({
                url : "<%=basePath%>oa/mail/markMail.action",
                data : {
                    "mailMessage.id" : ids,
                    "markType" : markType
                },
                type : "post",
                dataType : "json",
                success : function (data) {
                    if(data.resCode === "success"){
                        $('#starBox').datagrid("reload");
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
                        $('#starBox').datagrid("reload");
                    }
                }
            });
        }
        function formatterSize(value) {
            if(value < 0){
                return 0;
            }else if(value < 1024*1024){
                return (value/1024).toFixed(1)+"K";
            }else if(value < 1024*1024*1024){
                return (value/1024/1024).toFixed(2)+"M";
            }else if(value < 1024*1024*1024*1024){
                return (value/1024/1024/1024).toFixed(2)+"G";
            }
        }
        function formatterStar(value,row) {
            if(value === 0){
                return '<a class="toggleStar" id="'+row.id+'" starFlg="0" onclick="toggleStar(this)" data-options="plain:true,iconCls:\'icon-up\'" style="height:20px"></a>';
            }else if(value === 1){
                return '<a class="toggleStar" id="'+row.id+'" starFlg="1" onclick="toggleStar(this)" data-options="plain:true,iconCls:\'icon-down\'" style="height:20px"></a>';
            }
        }
        function toggleStar(dom) {
            var starFlg = $(dom).attr("starFlg");
            var id = $(dom).attr("id");
            if(starFlg === "0"){
                markMail(id, 3);
            }else if(starFlg === "1"){
                markMail(id, 4);
            }
        }
	</script>
</body>
</html>