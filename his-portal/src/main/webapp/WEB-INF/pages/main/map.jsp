<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"> 
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<head>
<title>网站地图</title>
	<link href="<%=basePath%>themes/system/css/edocss/edo-all.css" rel="stylesheet" type="text/css" />
	<link href="<%=basePath%>themes/system/css/300piaoIcon.css" rel="stylesheet" type="text/css" />
	<script src="<%=basePath%>javascript/edojs/edo.js" type="text/javascript"></script>
	<script language="javascript">
	<!--
		function init(){
		    searchMenus(function(o){
		    	var json=Edo.util.Json.decode(o.data);
		    	
		    	var tree = new Edo.data.DataTree(json);	
		    	menuTree.set('data',tree);
		    });
		}
		function searchMenus(callback){
		    var o = {
		    	dataString:"{parent:${menuId},user:${userId}}",
		        method: 7
		    };
		    Edo.util.Ajax.request({        
		        url: 'menuAction!commitAjax',
		        type: 'post',
		        params: o,
		        onSuccess: function(text){ 
		            var o = Edo.util.Json.decode(text);
		            if(o.error == 0){
		                if(callback) callback(o);                
		            }else{
		                alert(o.msg);
		            }
		        },
		        onFail: function(code){
		            alert("加载错误,"+code);
		        }
		    });
		}
	//-->
	</script>
</head>
<body>

</body>
</html>
<script>
Edo.util.Dom.on(window, 'domload', function(e){
	Edo.create(
		{
	        type: "app", render: "#body",border: [0,0,0,0],padding: 0,horizontalAlign: "center",
	        children : [
   	            {
   		            type:'space',height:20
   	            },
	            {
					type: "panel", id: "menuPanel", title:'&nbsp;&nbsp;本模块包括以下栏目：', titleIcon:'icon_map', headerHeight:30, style:'font-size:18',layout: "vertical", width: "80%",height:300,   
					enableCollapse: true,  
				    titlebar:[
						{
				        	cls:'e-titlebar-toggle',
				        	onclick: function(e){
				            	this.parent.owner.toggle();
				        	}
						}
				    ],
                    children : [
						{
							id : "menuTree",
				            type : "tree",
				            cls : "e-tree-allow",
				            headerVisible:false,
						    horizontalAlign: "center",
						    horizontalLine: false,
						    verticalLine: false,
				            width : '100%',
				            height : "100%",
				            rowHeight: 26,
				            autoColumns: true,
				            autoExpandColumn: 'desc',
				            //enableStripe : true,
				            columns : [
		                        {		
									id: 'name',
  									header: '栏目',
		                            dataIndex: 'Name',
		                            headerAlign: 'center',
		                            cls: 'verticalMiddle26',
		                            width:180,
		                            renderer: function(v){
										return "&nbsp;<font size='2'>"+v+"</font>";
			                        }
		                        },
		                        {
									id: 'desc',
		                            header: '描述',
		                            dataIndex: 'Description',
		                            headerAlign: 'center',
		                            cls: 'verticalMiddle26',
		                            width:'100%',
		                            renderer: function(v){
										return "&nbsp;<font size='2'>"+v+"</font>";
			                        }
		                        }              
				  		    ]
				        }
     				]
	            },
	            {
		            type:'space',height:20
	            },
	            {
					type: "panel", id: "tipPanel", title:'&nbsp;&nbsp;系统操作图标友情提示：', titleIcon:'icon_tip', headerHeight:30, style:'font-size:18',layout: "vertical", width: "80%", height: "120",   
					enableCollapse: true,  
				    titlebar:[
						{
				        	cls:'e-titlebar-toggle',
				        	onclick: function(e){
				            	this.parent.owner.toggle();
				        	}
						}
				    ],
                    children : [
                        {
							type: "ct", layout: "horizontal",
							children : [
								{
									type:"space",width:5
								},
							    {
							    	id:"searchBtn",
                                    type: 'button',
                                    icon: 'icon_search32',
                                    width: 32,
                                    height: 32,  
                                    onclick: function(e){
									    Info("此图标为查询按钮！");
                                    }
								},
								{
									type:"space",width:2
								},
								{
									type: "label",
									cls: "fontWRYH14",
									text: "查询"
								},
								{
									type:"space",width:20
								},
							    {
							    	id:"adBtn",
                                    type: 'button',
                                    icon: 'icon_adsearch32',
                                    width: 32,
                                    height: 32,  
                                    onclick: function(e){
									    Info("此图标为高级查询设置按钮！");
                                    }
								},
								{
									type: "space", width:2
								},
								{
									type: "label",
									cls: "fontWRYH14",
									text: "高级筛选"
								},
								{
									type:"space",width:20
								},
							    {
							    	id:"saveBtn",
                                    type: 'button',
                                    icon: 'icon_save32',
                                    width: 32,
                                    height: 32,  
                                    onclick: function(e){
									    Info("此图标为保存按钮！");
                                    }
								},
								{
									type: "space", width:2
								},
								{
									type: "label",
									cls: "fontWRYH14",
									text: "保存"
								},
								{
									type:"space",width:20
								},
							    {
							    	id:"addBtn",
                                    type: 'button',
                                    icon: 'icon_add32',
                                    width: 32,
                                    height: 32,  
                                    onclick: function(e){
									    Info("此图标为添加按钮！");
                                    }
								},
								{
									type:"space",width:2
								},
								{
									type: "label",
									cls: "fontWRYH14",
									text: "添加"
								},
								{
									type:"space",width:20
								},
							    {
							    	id:"deleteBtn",
                                    type: 'button',
                                    icon: 'icon_delete32',
                                    width: 32,
                                    height: 32,  
                                    onclick: function(e){
									    Info("此图标为删除按钮！");
                                    }
								},
								{
									type: "space", width:2
								},
								{
									type: "label",
									cls: "fontWRYH14",
									text: "删除"
								},
								{
									type:"space",width:20
								},
							    {
							    	id:"authorizeBtn",
                                    type: 'button',
                                    icon: 'icon_authorize32',
                                    width: 32,
                                    height: 32,  
                                    onclick: function(e){
									    Info("此图标为授权许可按钮！");
                                    }
								},
								{
									type: "space", width:2
								},
								{
									type: "label",
									cls: "fontWRYH14",
									text: "授权许可"
								},
								{
									type:"space",width:20
								},
							    {
							    	id:"authorizecancelBtn",
                                    type: 'button',
                                    icon: 'icon_authorizecancel32',
                                    width: 32,
                                    height: 32,  
                                    onclick: function(e){
									    Info("此图标为取消授权许可按钮！");
                                    }
								},
								{
									type: "space", width:2
								},
								{
									type: "label",
									cls: "fontWRYH14",
									text: "取消授权许可"
								}
                        	]
                        },
                        {
							type: "ct", layout: "horizontal",
							children : [
								{
									type:"space",width:5
								},
							    {
							    	id:"startBtn",
                                    type: 'button',
                                    icon: 'icon_start32',
                                    width: 32,
                                    height: 32,  
                                    onclick: function(e){
									    Info("此图标为启用按钮！");
                                    }
								},
								{
									type:"space",width:2
								},
								{
									type: "label",
									cls: "fontWRYH14",
									text: "启用"
								},
								{
									type:"space",width:20
								},
							    {
							    	id:"stopBtn",
                                    type: 'button',
                                    icon: 'icon_stop32',
                                    width: 32,
                                    height: 32,  
                                    onclick: function(e){
									    Info("此图标为停用按钮！");
                                    }
								},
								{
									type: "space", width:2
								},
								{
									type: "label",
									cls: "fontWRYH14",
									text: "停用"
								},
								{
									type:"space",width:20
								},
							    {
							    	id:"setuserBtn",
                                    type: 'button',
                                    icon: 'icon_setuser32',
                                    width: 32,
                                    height: 32,  
                                    onclick: function(e){
									    Info("此图标为设置用户按钮！");
                                    }
								},
								{
									type: "space", width:2
								},
								{
									type: "label",
									cls: "fontWRYH14",
									text: "设置用户"
								},
								{
									type:"space",width:20
								},
							    {
							    	id:"setaccessBtn",
                                    type: 'button',
                                    icon: 'icon_setaccess32',
                                    width: 32,
                                    height: 32,  
                                    onclick: function(e){
									    Info("此图标为设置权限按钮！");
                                    }
								},
								{
									type:"space",width:2
								},
								{
									type: "label",
									cls: "fontWRYH14",
									text: "设置权限"
								}
                        	]
                        }
     				]
	            }
	        ]
		}
	)
});

init();
</script>