function searchMenus(callback){
    var o = {
        method: 0
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

function searchAccess(id,callback){
    var o = {
    	dataString:"{ID:"+id+"}",
        method: 1
    };
    Edo.util.Ajax.request({        
        url: 'accessAction!commitAjax',
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

function searchFuns(o,callback){
	var dataString=Edo.util.Json.encode(o);
    var o = {
    	dataString:dataString,
        method: 8
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

function addMenu(o, callback){
	//o=encodeJson(o);
    var o = {
        dataString: Edo.util.Json.encode(o),
        method: 1
    };
    Edo.util.Ajax.request({
        url: 'menuAction!commitAjax',
        type: 'post',
        params: o,
        onSuccess: function(text){        
            var o = Edo.util.Json.decode(text);
            if(o.error == 0){
                if(callback) callback(o)
            }else{
                alert(o.msg);
            }
        },
        onFail: function(code){
            alert("新增错误,"+code);
        }
    });
}

function deleteMenu(o, callback){
	//o=encodeJson(o);
    Edo.util.Ajax.request({
    	url: 'menuAction!commitAjax',
        type: 'post',
        params: {
            method: 2,
            dataString: Edo.util.Json.encode(o)
        },
        onSuccess: function(text){        
            var o = Edo.util.Json.decode(text);
            if(o.error == 0){
                if(callback) callback(o)
            }else{
                alert(o.msg);
            }
        },
        onFail: function(code){
            alert("新增错误,"+code);
        }
    });
}

function updateMenu(o, callback){
	//o=encodeJson(o);
    Edo.util.Ajax.request({
    	url: 'menuAction!commitAjax',
        type: 'post',
        params: {
            method: 3,
            dataString: Edo.util.Json.encode(o)
        },
        onSuccess: function(text){       
            var o = Edo.util.Json.decode(text);
            if(o.error == 0){
                if(callback) callback(o);
            }else{
                alert(o.msg);
            }
        },
        onFail: function(code){
            alert("新增错误,"+code);
        }
    });
}

//栏目审核
function needcheckMenu(o, callback){
    Edo.util.Ajax.request({
    	url: 'menuAction!commitAjax',
        type: 'post',
        params: {
            method: 5,
            dataString: Edo.util.Json.encode(o)
        },
        onSuccess: function(text){   
            var o = Edo.util.Json.decode(text);
            if(o.error == 0){
                if(callback) callback(o)
            }else{
                alert(o.msg);
            }
        },
        onFail: function(code){
            alert("新增错误,"+code);
        }
    });
}

//启用和停用用户
function startAndStopMenu(o, callback){
    Edo.util.Ajax.request({
    	url: 'menuAction!commitAjax',
        type: 'post',
        params: {
            method: 6,
            dataString: Edo.util.Json.encode(o)
        },
        onSuccess: function(text){        
            var o = Edo.util.Json.decode(text);
            if(o.error == 0){
                if(callback) callback(o)
            }else{
                alert(o.msg);
            }
        },
        onFail: function(code){
            alert("新增错误,"+code);
        }
    });
}

function setAccess(o, callback){
    Edo.util.Ajax.request({
    	url: 'accessAction!commitAjax',
        type: 'post',
        params: {
            method: 2,
            dataString: Edo.util.Json.encode(o)
        },
        onSuccess: function(text){        
            var o = Edo.util.Json.decode(text);
            if(o.error == 0){
                if(callback) callback();
            }else{
                alert(o.msg);
            }
        },
        onFail: function(code){
            alert("新增错误,"+code);
        }
    });
}

function addFun(o, callback){
	//o=encodeJson(o);
    var o = {
    	dataString: Edo.util.Json.encode(o),
        method: 9
    };
    Edo.util.Ajax.request({
        url: 'menuAction!commitAjax',
        type: 'post',
        params: o,
        onSuccess: function(text){        
            var o = Edo.util.Json.decode(text);
            if(o.error == 0){
                if(callback) callback(o)
            }else{
                alert(o.msg);
            }
        },
        onFail: function(code){
            alert("新增错误,"+code);
        }
    });
}

function deleteFun(o, callback){
	//o=encodeJson(o);
    Edo.util.Ajax.request({
    	url: 'menuAction!commitAjax',
        type: 'post',
        params: {
            method: 10,
            dataString: Edo.util.Json.encode(o)
        },
        onSuccess: function(text){        
            var o = Edo.util.Json.decode(text);
            if(o.error == 0){
                if(callback) callback(o)
            }else{
                alert(o.msg);
            }
        },
        onFail: function(code){
            alert("新增错误,"+code);
        }
    });
}

function updateFun(o, callback){
	//o=encodeJson(o);
	
    Edo.util.Ajax.request({
    	url: 'menuAction!commitAjax',
        type: 'post',
        params: {
            method: 11,
            dataString: Edo.util.Json.encode(o)
        },
        onSuccess: function(text){        
            var o = Edo.util.Json.decode(text);
            if(o.error == 0){
                if(callback) callback(o)
            }else{
                alert(o.msg);
            }
        },
        onFail: function(code){
            alert("新增错误,"+code);
        }
    });
}

function saveFun(o, callback){
	//o=encodeJson(o);
    var o = {
    	dataString: Edo.util.Json.encode(o),
        method: 12
    };
    Edo.util.Ajax.request({
        url: 'menuAction!commitAjax',
        type: 'post',
        params: o,
        onSuccess: function(text){        
            var o = Edo.util.Json.decode(text);
            if(o.error == 0){
                if(callback) callback(o)
            }else{
                alert(o.msg);
            }
        },
        onFail: function(code){
            alert("新增错误,"+code);
        }
    });
}

//表单验证器函数
function noEmpty(v){
  if(v == "") return "不能为空";
}

function showMenuForm(callback){
    if(!Edo.get('MenuForm')) {
        //创建栏目面板
        Edo.create({
            id: 'MenuForm',            
            type: 'window',title: '栏目录入',
            width:300,
            render: document.body,
            titlebar: [
                {
                    cls: 'e-titlebar-close',
                    onclick: function(e){
                        this.parent.owner.hide();
                    }
                }
            ],
            children: [
                {
                    type: 'formitem',label: '名称<span style="color:red;">*</span>:',
                    children:[{id: 'name', type: 'text', name: 'Name', width:180,valid: noEmpty}]
                },
                {
                    type: 'formitem',label: '别名:',
                    children:[{type: 'text', name: 'Alias', width:180}]
                },
                {
                    type: 'formitem',label: '功能:',
                    children:[
                        {
                        	type: 'combo', name: 'FunctionID', readOnly: true, width:180, data: Functions, valueField: 'id', displayField: 'name', selectedIndex:0,
                            tableConfig: {
                                headerVisible: true,
                                verticalLine: true,
                                horizontalLine: true,
                                autoColumns: true,
                                columns: [
                                    {header: '类别', dataIndex: 'Class'},
                                    {header: '名称', dataIndex: 'name'}
                                ]
                            }
                        }
                    ]
                },
                {
                    type: 'formitem',label: '网址:',
                    children:[{type: 'text', name: 'URL', width:180}]
                },
                {
                    type: 'formitem',label: '参数:',
                    children:[{type: 'text', name: 'Parameter', width:180}]
                },
                {
                    type: 'formitem',label: '图标:',
                    children:[
	                    {
	                      	type: 'combo', name: 'Icon', readOnly: true, width:180, data: Icons, valueField: 'icon', displayField: 'name',selectedIndex:0,
                            tableConfig: {
                                headerVisible: true,
	                            verticalLine: true,
	                            horizontalLine: true,
	                            autoColumns: true,
	                            rowHeight:40,
	                            columns: [
	                                {
	                                	header: '名称', dataIndex: 'name', cls: 'verticalMiddle40',
	                                	renderer: function(v){
		                                	return "&nbsp;&nbsp;"+v;
                                		}	                                	
	                                },
	                                {
	                                	header: '图标', dataIndex: 'src',height:40,
	                                	renderer: function(v){
		                                	if(v==""){
		                                		return v;
		                                	}else{
		                                		return "<img src=" + v + " height='40' width='40'>";
		                                	}
	                                	}
	                                }
	                            ]
                            }
	                    }
	                ]
                },
                {
                    type: 'formitem',label: '目标:',
                    children:[{type: 'combo', name: 'OpenMode', readOnly: true, required: true, width:180, data: Targets,valueField: 'id', displayField: 'name', selectedIndex:0}]
                },
                {
                    type: 'formitem',label: '审核:',
                    children:[{type: 'checkbox', name: 'NeedCheck', width:180}]
                },
                {
                    type: 'formitem',label: '说明:',
                    children:[{type: 'textarea', name: 'Description', width:180}]
                },
                {
                    type: 'formitem',layout:'horizontal', padding: [8,0,8, 0],
                    children:[
                        {name: 'submitBtn', type: 'button', text: '提交表单', 
                            onclick: function(){
                                if(MenuForm.valid()){
                                    var o = MenuForm.getForm();
                                    if(o.FunctionID==0 && o.URL==""){
                                    	Error("功能和网址不能都为空！");
                                    	return false;
                                    }
                                    if(MenuForm.callback) MenuForm.callback(o);
                                    MenuForm.hide();
                                }
                            }
                        }
                    ]
                }
            ]
        });
    }
    MenuForm.callback = callback;
    MenuForm.show('center', 'middle', true);
    return MenuForm;
}

function showMenuEditForm(Menu,callback){
	if(Menu.icon.substring(Menu.icon.length-2)=="16") Menu.Icon=Menu.icon.substring(0,Menu.icon.length-2);
	
	if(!Edo.get('MenuEditForm')) {
        //创建栏目面板
        Edo.create({
            id: 'MenuEditForm',            
            type: 'window',title: '栏目编辑',
            width:300,
            render: document.body,
            titlebar: [
                {
                    cls: 'e-titlebar-close',
                    onclick: function(e){
                        this.parent.owner.hide();
                    }
                }
            ],
            children: [
                {
                   type: 'formitem',label: '编号:',
                   children:[{type: 'text', name: 'ID', width:180,readOnly:true}]
                },
                {
                    type: 'formitem',label: '名称<span style="color:red;">*</span>:',
                    children:[{type: 'text', name: 'Name', width:180,valid: noEmpty}]
                },
                {
                    type: 'formitem',label: '别名:',
                    children:[{type: 'text', name: 'Alias', width:180}]
                },
                {
                    type: 'formitem',label: '功能:',
                    children:[
                        {
                        	type: 'combo', name: 'FunctionID', readOnly: true, width:180, data: Functions, valueField: 'id', displayField: 'name', selectedIndex:0,
                            tableConfig: {
                                headerVisible: true,
                                verticalLine: true,
                                horizontalLine: true,
                                autoColumns: true,
                                columns: [
                                    {header: '类别', dataIndex: 'Class'},
                                    {header: '名称', dataIndex: 'name'}
                                ]
                            }
                        }
                    ]
                },
                {
                    type: 'formitem',label: '网址:',
                    children:[{type: 'text', name: 'URL', width:180}]
                },
                {
                    type: 'formitem',label: '参数:',
                    children:[{type: 'text', name: 'Parameter', width:180}]
                },
                {
                    type: 'formitem',label: '图标:',
                    children:[
	                    {
	                      	type: 'combo', name: 'Icon', readOnly: true, width:180, data: Icons, valueField: 'icon', displayField: 'name', selectedIndex:0,
                            tableConfig: {
                                headerVisible: true,
	                            verticalLine: true,
	                            horizontalLine: true,
	                            autoColumns: true,
	                            rowHeight:40,
	                            columns: [
	  	                                {
		                                	header: '名称', dataIndex: 'name', cls: 'verticalMiddle40',
		                                	renderer: function(v){
			                                	return "&nbsp;&nbsp;"+v;
	                                		}	                                	
		                                },	                                {
	                                	header: '图标', dataIndex: 'src',height:40,
	                                	renderer: function(v){
		                                	if(v==""){
		                                		return v;
		                                	}else{
		                                		return "<img src=" + v + " height='40' width='40'>";
		                                	}
	                                	}
	                                }
	                            ]
                            }
	                    }
	                ]
                },
                {
                    type: 'formitem',label: '目标:',
                    children:[
                        {
                        	type: 'combo', name: 'OpenMode', readOnly: true, width:180, data: Targets,valueField: 'id', displayField: 'name', selectedIndex:0
                        }
                    ]
                },
                {
                    type: 'formitem',label: '审核:',
                    children:[{type: 'checkbox', name: 'NeedCheck', width:180}]
                },
                {
                    type: 'formitem',label: '说明:',
                    children:[{type: 'textarea', name: 'Description', width:180}]
                },
                {
                    type: 'formitem',layout:'horizontal', padding: [8,0,8, 0],
                    children:[
                        {name: 'submitBtn', type: 'button', text: '提交表单', 
                            onclick: function(){
                                if(MenuEditForm.valid()){
                                    var o = MenuEditForm.getForm();
                                    if(o.FunctionID==0 && o.URL==""){
                                    	Error("功能和网址不能都为空！");
                                    	return false;
                                    }
                                    o.Mode='1';
                                    if(MenuEditForm.callback) MenuEditForm.callback(o);
                                    MenuEditForm.hide();
                                }
                            }
                        }
                    ]
                }
            ]
        });
    }
    MenuEditForm.setForm(Menu);
    MenuEditForm.callback = callback;
    MenuEditForm.show('center', 'middle', true);
    return MenuEditForm;
}

function showAccessForm(id){
	if(!Edo.get('AccessForm')) {
        //创建栏目面板
        Edo.create({
            id: 'AccessForm',            
            type: 'window',title: '权限设置',
            width:800,
            height:400,
            render: document.body,
            titlebar: [
                {
                	cls: 'e-titlebar-close',
                    onclick: function(e){
                        this.parent.owner.hide();
                    }
                }
            ],
            children: [
                {
					id: "roleTree",
					type: 'tree',
				    cls: 'e-tree-allow',
				    width: '100%',
				    height: '100%',
				    horizontalAlign: "center",
				    editAction: 'click',
				    enableStripe : true,
				    autoExpandColumn: 'name',
				    //autoColumns: true,
				    columns: [
				        {
				            id : 'name',
				        	header: '  角色',
				            dataIndex: 'Name',
				            width:200,
				            renderer: function(v){
				            	return "<font size='2'>" + v + "</font>";
				            }
				        },
	                    Edo.lists.Table.createRadioColumn({
	                    	header: '操作权限', 
	                    	dataIndex: 'MenuAccess', 
	                    	headerAlign:'center',
	                    	align: 'center',
	                    	width:300,
	                    	data: MenuAccess, 
	                    	valueField: 'id', 
	                    	displayField: 'name'
	                    	/*
	                    	onsubmitedit: function(e){
	                    		var r = roleTree.getSelected();    
	                    		if(r.MenuAccess==0){
	                    			var rc=roleTree.data.getChildren(r,true); 
	                    			rc.each(function(c){
	                    				roleTree.data.update(c, 'MenuAccess', r.MenuAccess);
	                    			});
	                    		}else{
	                    			setParentAccess(r);
	                    		}
	                    	}
	                    	*/
	                    }),       	    								       
				        {
				            header: '数据权限',
				            dataIndex: 'DataAccess',
				            headerAlign:'center',
				            align: 'center',
				            width:80,
	                        editor: {
				        		id: 'dataAccess', type: 'combo', data: DataAccess, valueField: 'id', displayField: 'name'
				        	},
                 			renderer:function(v){
                 				if(v==''){
                 					return "<font size='2'>全部数据</font>";
                 				}else{
		                            for(var i=0,l=DataAccess.length; i<l; i++){
		                                var g = DataAccess[i];
		                                if(g.id == v){
    		                                return "<font size='2'>" + g.name + "</font>";
		                                }
		                            }
                 				}
                 			}
				        },
				        {
				            header: '操作',
				            dataIndex: 'ID',
				            headerAlign:'center',
				            align: 'center',
				            width:180,
	                        renderer: function(record,r){
				        		if(!roleTree.data.isLeaf(r)){
				        			return "<a href='javascript:setChildAccess("+r.__id+")'>所有子节点均设置为相同的权限</a>"
				        		}else{
				        			return "";
				        		}
				        	}
				        }
				    ]
                },
                {
                    type: 'formitem',layout:'horizontal', horizontalAlign: 'center', width:'100%', padding: [8,0,8,0],
                    children:[
                        {
                        	name: 'submitBtn', type: 'button', text: '<font size=2>提交设置</font>', 
                            onclick: function(){
                        		var ras=new Array();
                        		var flag=0;
                                roleTree.data.view.each(function(o){  
									if(o.__status=="update"){
										var obj=new Object;
										obj.MenuID=o.MenuID;
										obj.RoleID=o.ID;
										obj.UserID=userID;
					                	obj.OpMenuID=menuID;
										obj.MenuAccess=o.MenuAccess;
										obj.DataAccess=o.DataAccess;
										ras[flag]=obj;
										flag++;
										obj=null;
									}
                                });
                                if(ras.length>0){
    								Edo.MessageBox.saveing('权限设置', '数据保存中...');
    								setAccess({roleaccesss:ras},function(){ 
    									Edo.MessageBox.hide();	
    								});
                                }
                                AccessForm.hide();
                            }
                        }
                    ]
                }
            ] 
        });
    }
    searchAccess(id,function(o){
		var json=Edo.util.Json.decode(o.data);
    	var tree = new Edo.data.DataTree(json);	
		roleTree.set('data',tree);
    });
    AccessForm.show('center', 'middle', true);
    return AccessForm;
}

function setParentAccess(r){
	if(r.__pid!=-1){
		var rp=roleTree.data.findParent(r);
		roleTree.data.update(rp, 'MenuAccess', r.MenuAccess);
		setParentAccess(rp);
	}
}

function setChildAccess(id){
	var o=roleTree.data.getById(id);
	var oc=roleTree.data.getChildren(o,true); 
	oc.each(function(c){
		roleTree.data.update(c, 'MenuAccess', o.MenuAccess);
		roleTree.data.update(c, 'DataAccess', o.DataAccess);
	});
}
