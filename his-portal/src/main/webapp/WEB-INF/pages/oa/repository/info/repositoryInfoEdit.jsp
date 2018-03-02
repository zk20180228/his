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
<!-- 引入两个js -->
<script type="text/javascript" src="<%=path%>/ueditor1_4_3_2/ueditor.config.js"></script>
<script type="text/javascript" src="<%=path%>/ueditor1_4_3_2/ueditor.all.min.js"></script>
<script type="text/javascript" src="<%=path%>/ueditor1_4_3_2/ueditor.all.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/easyui1.4.5/jquery.easyui.min.js"></script>
<script type="text/javascript" charset="utf-8" src="<%=path%>/ueditor1_4_3_2/kityformula-plugin/addKityFormulaDialog.js"></script>
<script type="text/javascript" charset="utf-8" src="<%=path%>/ueditor1_4_3_2/kityformula-plugin/getKfContent.js"></script>
<script type="text/javascript" charset="utf-8" src="<%=path%>/ueditor1_4_3_2/kityformula-plugin/defaultFilterFix.js"></script>
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
</script>

<title>文本编辑</title>
<style type="text/css">
.accordion .accordion-body {
    border-width: 0px 0 0px;
}
</style>
<script type="text/javascript">
var selectMap = new Map();//选中的权限
var infoAttachName = '${info.attachName}';//文件名称
var infoAttachURL = '${info.attach}';//文件地址
function GetRequest() {
	var url = location.search; //获取url中"?"符后的字串  
	var theRequest = new Object();
	if(url.indexOf("?") != -1) {
		var str = url.substr(1);
		strs = str.split("&");
		for(var i = 0; i < strs.length; i++) {
			theRequest[strs[i].split("=")[0]] = decodeURI(decodeURI(strs[i].split("=")[1]));
		}
	}
	return theRequest;
}

var tabName =  GetRequest()["tabName"];
var tabID = GetRequest()["tabID"];
var patName = GetRequest()["patName"];
$(function(){
	$('#win').window('close');
	$('#fileFileName').hide();
	$('#hesign').hide();
	$('#deletefile0').hide();
	addTr();//调用回显方法
	$('#fb0').filebox({    
	    buttonText: '选择文件', 
	    buttonAlign: 'right'
	});
	 $('#infoMenuid').combotree({ 
		url: '<%=basePath%>oa/repositoryCateg/getCategTree.action',   
		required: true, 
		missingMessage:'请选择所属栏目',
		width:'300',
	    editable:true,
	    onSelect : function(node){
	    	if(node.nodeType == "0"){
		    	$('#infoCateName').val(node.text);
		    	$('#ispub').val(0);
	    	}else{
		    	$('#ispub').val(1);
	    		$.messager.alert('提示','请选择子级分类...','info');
	    	}
	    },
	    onLoadSuccess : function (){
	    	var t = $('#infoMenuid').combotree('tree');	// 获取树对象
	    	var node = t.tree('getSelected');		// 获取选择的节点
	    	if(node!=null){
		    	$('#infoCateName').val(node.text);
	    	}
	    }
	});
	$('#winidSet').window({    
	    width:980,    
	    height:620,    
	    modal:true,
	    title:"授权"
	}); 
	$('#winidSet').window('close');
});
function setTitleImg(){
	$('#win').window('open');
}

function addfileimput(value){
	var i = parseInt(value)+1;
// 	<tr id="hadauthtr0">
// 	<input id="hadauthinput0" class="easyui-textbox"style="width:300px"> 
// </tr>
	var html = '<tr id="trid'+i+'">'
					+'<td>附件上传:</td>'
					+'<td><input id="fb'+i+'" name="file" style="width:575px;height: 33px"></td>'
					+"<td class=\"fileaddclass\">&nbsp;<a id=\"add"+i+"\" onclick=\"addfileimput("+i+")\"></a>"
					+"&nbsp;<a id=\"remove"+i+"\" onclick=\"removeTr("+i+")\"></a>"
					+'&nbsp;<input id="checkbox'+i+'" type="checkbox" value="'+i+'" />同上</td>'
				+'</tr>';
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
/**
 * 动态添加
 */
function addTr(){
	if(infoAttachName==null||infoAttachName==""){
		return ;
	}
	var attachurl = infoAttachURL.split('#');
	var attachName = infoAttachName.split('#');
	for(var i=0;i<attachName.length;i++){
		if(i==0){
			$('#fileurl0').val(attachurl[0]);
			$('#fb0').val(attachName[0]);
			//显示隐藏的删除标签
			$('#deletefile0').show();
		}else{
			produceHTML(i,attachName[i],attachurl[i]);
		}
	}

};
/*
 * value  该文件的权限（特殊处理的结果）
 */
function produceHTML(num,filename,authid){
	var i = num;
	var j = parseInt(num)-1;
	var html = '<tr id="trid'+i+'">'
				+'<td>附件上传:</td>'
				+'<td><input id="fb'+i+'" name="file" style="width: 575px;height: 33px;" value="'+filename+'"></td>'
				+"<td class=\"fileaddclass\">&nbsp;<a id=\"add"+i+"\" onclick=\"addfileimput("+i+")\"></a>"
				+'<td>&nbsp;<a id="deletefile'+i+'" href="javascript:deleteFile('+i+')" class="easyui-linkbutton" >删除附件</a></td>'
				+'<td><input id="fileurl'+i+'" type="hidden"  value="'+authid+'"/></td>'
				+'</tr>';
	$('#add'+j).hide();
	$('#remove'+j).hide();
	$('#filetable').append(html);
	$('#deletefile'+i).linkbutton({
		
	});
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
	return html;
}

function removeTr(value){
	var i = parseInt(value)-1;
	$('#trid'+value).remove();
	$('#hadauthtr'+value).remove();
	$('#add'+i).show();
	$('#remove'+i).show();
}
function showback(inputid,value){
	$('#hadauthtr'+inputid).show();
	$('#hadauthinput'+inputid).textbox('setValue',value);
};
/**
 * 删除已上传的文件
 */
function deleteFile(value){
	var fileurl = $('#fileurl'+value).val();
	$.ajax({
		url : '<%=basePath%>oa/information/deleteFile.action?oldfileurl='+fileurl,
		success : function(result){
			if(result.resCode=="success"){
				if(value=="0"){
					$('#auth0').val("");
					$('#fb0').val("");
					$('#fb0').filebox ('setText',"");
					$('#fileurl0').val("");
					$('#deletefile0').show();
					$('#hadauthinput0').textbox("setText","");
				}else{
					removeTr(value);
				}
				$.messager.alert('提示','删除成功!','info');
				return ;
			}else{
				$.messager.alert('提示','删除失败!','info');
				return ;
			}
		},
		error : function(result){
			$.messager.alert('提示','删除失败!','info');
			return ;
		}
	});
}
</script>
</head>
<body style="overflow-x:hidden;overflow-y:auto;">
<div style="padding: 20px;">
	<form id="editForm" method="post" enctype="multipart/form-data">
	<input  id="ispub" type="hidden" value="0"/>
	<input  id="createUser" type="hidden" name="info.createUser" value="${info.createUser}"/>
	<input  id="createDept" type="hidden" name="info.createDept" value="${info.createDept}"/>
	<input  id="createTime" type="hidden" name="info.createTime" value="${info.createTime}"/>
	<input  id="updateUser" type="hidden" name="info.updateUser" value="${info.updateUser}"/>
	<input  id="updateTime" type="hidden" name="info.updateTime" value="${info.updateTime}"/>
	<input  id="isCollect" type="hidden" name="info.isCollect" value="${info.isCollect}"/>
	<input  id="content" type="hidden" name="info.content" value="${info.content}"/>
	<input  id="stop_flg" type="hidden" name="info.stop_flg" value="${info.stop_flg}"/>
	<input  id="del_flg" type="hidden" name="info.del_flg" value="${info.del_flg}"/>
		<div style="margin-top: 10px;">
			<span style="display:inline-block;height: 33px;width: 150px;text-align: justify;">标题：</span>
			<input  id="infoTitle" name="info.name" value="${info.name}"
				class="easyui-textbox" data-options="required:true" style="width: 800px;height: 33px">
		</div>
		<div style="margin-top: 10px; width: 1500px;height: 33px;"  >
			<span style="display:inline-block;height: 33px;width: 150px;text-align: justify;">类别：</span>
			<input  id="infoMenuid" name="info.categCode" value="${info.categCode}"
				class="easyui-combotree" data-options="required:true" style="width: 500px;height: 33px">
			<input id="infoCateName" type="hidden" name="info.categName" value="${info.categName }">
			<div style="float:right;" >
				<span>备注：&nbsp</span>
				<input id="infoBrev" name="info.remark" value="${info.remark }" 
					class="easyui-textbox" style="width:575px;height:33px;">
			</div>
		</div>
		<div style="margin-top: 10px;width: 1500px;height: 33px;">
			<div style="float: left;" >
				<span style="display:inline-block;height: 33px;width: 150px;text-align: justify;">关键字：</span>
				<input id="infoKeyword" name="info.keyWord" value="${info.keyWord }"
					 class="easyui-textbox" style="width: 575px;height: 33px;">
			</div>
			<div style="float: right;">
				<span>来源：&nbsp</span>
				<input id="infoSource" name="info.origin" value="${info.origin }"
					 class="easyui-textbox" style="width: 575px;height: 33px;">
			</div>
		</div>	
		<div style="margin-top: 10px; width: 1500px;height: 33px;"  >
			<span style="display:inline-block;height: 33px;width: 150px;text-align: justify;">文章状态：</span>
			<input  id="isOvertCombobox" name="info.isOvert" value="${info.isOvert}"
				class="easyui-combobox" style="width: 500px;height: 33px"
				data-options="required:true,editable:false,
				valueField: 'value',
				textField: 'label',
				data: [{label: '公开',value: '1'},{label: '不公开',	value: '0'}]" />
		</div>
		<div style="margin-top: 10px;">
			<h3>内容：</h3>
			<table style="width: 100%">
				<tr>
					<td>
						<textarea id="editor" style="width: 100%">${info.contentHtml}</textarea>
						<input type="hidden" id="infoContent" name="content"/>
						<input type="hidden" name='info.id' value="${info.id}"/>
					</td>
				</tr>
			</table>
		</div>
		
		<div style="margin-top: 10px;">
		<table id="filetable" style="width: auto">
			<tr>
				<td>附件上传:</td>
				<td><input id="fb0" name="file" style="width:575px;height: 33px"></td>
<!-- 				<td><input id="auth0" type="hidden" name="authority" style="width:575px;height: 33px"></td> -->
<!-- 				<td>&nbsp;<a id="setAuth" href="javascript:setAuthority(0)" class="easyui-linkbutton" >设置附件权限</a></td> -->
				<td class="fileaddclass">&nbsp;<a id="add0" href="javascript:addfileimput(0)" class="easyui-linkbutton" data-options="iconCls:'icon-add'" ></td>
				<td><input id="fileurl0" type="hidden"  value=""/></td>
			</tr>
			
		</table>
		</div>
	</form>
	<div style="margin-top: 10px;">
		<a id='' href="javascript:saveForm(0)" class="easyui-linkbutton" data-options="iconCls:'icon-disk_upload'" >发布</a>
		<a id='' href="javascript:funCancel()" class="easyui-linkbutton" data-options="iconCls:'icon-clear'" >取消</a>
	</div>
</div>

<script type="text/javascript">
//获取浏览器宽度
UE.getEditor("editor",{
     initialFrameHeight: 300,
     focus: true
     , toolbars: [[
	          'fullscreen', 'source', '|', 'undo', 'redo', '|',
	          'bold', 'italic', 'underline', 'fontborder', 'strikethrough', 'superscript', 'subscript', 'removeformat', 'formatmatch', 'autotypeset', 'blockquote', 'pasteplain', '|', 'forecolor', 'backcolor', 'insertorderedlist', 'insertunorderedlist', 'selectall', 'cleardoc', '|',
	          'rowspacingtop', 'rowspacingbottom', 'lineheight', '|',
	          'customstyle', 'paragraph', 'fontfamily', 'fontsize', '|',
	          'directionalityltr', 'directionalityrtl', 'indent', '|',
	          'justifyleft', 'justifycenter', 'justifyright', 'justifyjustify', '|', 'touppercase', 'tolowercase', '|',
	          'link','unlink',//禁用超链接
	           'anchor', '|', 'imagenone', 'imageleft', 'imageright', 'imagecenter', '|',
	          'simpleupload', 'insertimage', 'emotion',
// 	          'attachment',//禁用附件上传
	          'map',
	          'insertframe', 
	          'template', 'background', '|',
	          'horizontal', 'date', 'time', 'spechars', 
	          '|','inserttable', 'deletetable', 'insertparagraphbeforetable', 'insertrow', 'deleterow', 'insertcol', 'deletecol', 'mergecells', 'mergeright', 'mergedown', 'splittocells', 'splittorows', 'splittocols', 'charts', '|',
	          'print', 'preview', 'searchreplace', 
      ]]
});

/***
 * 发布操作
 */
function saveForm(param){
	var ispub = $('#ispub').val();
	if(ispub != 0){
		$.messager.alert('提示','请在类别中选择子级栏目','info');
		return ;
	}
	$.messager.progress({text:'保存中，请稍后...',modal:true});
	var menuid = $('#infoMenuid').combotree('getValue');
	var imgfile = $('#fileFileName').val();
	var filenames = "";//新文件名称
	var oldfilename = "";//旧文件名称
	var oldfileurl = "";//旧文件地址
	var oldfileauth = "";//旧文件的权限
	var fileinput = $('input[name="file"]');
	for(var j=0;j<fileinput.length;j++){
		var filename = $('#fb'+j).filebox('getValue');
		if(fileinput.length>1){
			var authType = $('#auth'+j).attr('name');
			if((filename==null||filename=="")&&authType!="oldfileauth"){
				$.messager.alert('提示','请选择文件!','info');
				$.messager.progress('close');
				return ;
			}
			if($('#checkbox'+j).is(':checked')){
				var last = $('#auth'+(j-1)).val();
				$('#auth'+j).val(last);
			}
		}
		var fileurl = $('#fileurl'+j).val();
		if(fileurl==""||fileurl==null){
			if(filenames!=""){
				filenames += "#";
			}
			filenames += filename;
		}else{
			if(oldfilename!=""){
				oldfilename += "#";
				oldfileurl += "#";
			}
			oldfilename += $('#fb'+j).val();
			oldfileurl += $('#fileurl'+j).val();
		}
	}
	$('#editForm').form('submit',{
	    url:'<%=basePath%>oa/repositoryInfo/saveinfo.action?info.pubFlg='+param+'&imgfilename='+imgfile,   
	    queryParams : {attachname:filenames,oldfileurl:oldfileurl,oldfilename:oldfilename},
	    onSubmit: function(){ 
	    	var isValid = $(this).form('validate');
			if (!isValid){
				$.messager.alert('提示','请输入标题!','info');
				$.messager.progress('close');
				return isValid;
			}
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
	    	$('#infoContent').val(chtml);
	    	if(chtml.length == 0){
	    		$.messager.alert('提示','请输入内容!','info');
	    		$.messager.progress('close');
	    		return false;
	    	}
	    }, 
	    success:function(data){ 
	    	$.messager.progress('close');
	    	data = jQuery.parseJSON(data);
	    	if(data.resCode == 'success'){
	    		$.messager.alert('提示','操作完成!','info',function(){
					funCancel();
					upDataPage();
	    			$.messager.progress('close');
	    		});
	    	}else{
	    		$.messager.alert('提示',data.resMsg,'info');
	    		$.messager.progress('close');
	    	}
	    },
	    error :function(){
	    	$.messager.progress('close');
	    	$.messager.alert('提示','网络繁忙,请稍后重试...','info');
	    }
	}); 
}

/***
 * 取消操作
 */
function funCancel(){
	var $window;
	upPagecallback (tabName,patName,function(self){
		$window = self
	});
	setTimeout(function(){
		$window.$("#list").datagrid('reload');
		clearNav(tabID,tabName); 
	},100)
}
/*
 * 设置权限弹窗
 */
function setAuthority(value){
	$('#iframeid')[0].src = "<%=basePath%>oa/information/toAuth.action?whichfile="+value;
	$('#winidSet').window('open');
}
function openWindow(url,name,width,height,top,left){
	window.open(url, name, 'height=' + height + ',,innerHeight=' + height + ',width=' + width + ',innerWidth=' + width + ',top=' + top + ',left=' + left + ',status=no,toolbar=no,menubar=no,location=no,resizable=no,scrollbars=0,titlebar=no'); 
}
function onCheckESIGN(filePicker) {
	var fName = document.getElementById("fileFileName").value;
	$("#copyEsign").hide();
	$("#hesign").val(fName);
	$("#hesign").show();
	if (fName != null && fName != "") {
		var ftype = fName.toLowerCase().split(".");
		var fTypeName = ftype[ftype.length - 1];
		if (!fTypeName == '') {
			if (fTypeName != "jpg" && fTypeName != "jpeg"
					&& fTypeName != "png" && fTypeName != "gif") {
				$.messager.alert('提示',"上传的文件格式不正确，请选jpg,jpeg,png,gif格式的照片上传！");
				close_alert();
				checkFlag = "esignPatternNo";//提交时验证代表格式不对
			} else {
				if (filePicker.files[0].size > 20 * 1024 * 1024) {
					$.messager.alert('提示',"上传的文件大小不能大于20M，请重新上传！");
					close_alert();
					checkFlag = "esignSizeNo";//提交时验证代表大小不对
				}else{
					checkFlag = "";
				}
			}

		} else {
			$.messager.alert('提示',"上传的文件格式不正确，请选jpg,jpeg,png,gif格式的照片上传！");
			close_alert();
			checkFlag = "esignPatternNo";//提交时验证代表格式不对
		}
	}

}
// if($('#tmpDisengagedTime').val()){
// 	var tmpVal = $('#tmpDisengagedTime').val()
// 	$('#disengagedTime').val(tmpVal.substring(0,19))
// }
</script>
</body>
</html>