function searchRoles(callback){
    var o = {
    	dataString:"{UserID:0}",
        method: 0
    };
    Edo.util.Ajax.request({        
        url: 'roleAction!commitAjax',
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
function searchUsers(id,callback){
    var o = {
		dataString:"{index:0,size:0,RoleID:"+id+"}",
        method: 0
    };
    Edo.util.Ajax.request({        
        url: 'userAction!commitAjax',
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
        method: 0
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

function addRole(o, callback){
	//o=encodeJson(o);
    var o = {
        dataString: Edo.util.Json.encode(o),
        method: 1
    };
    Edo.util.Ajax.request({
        url: 'roleAction!commitAjax',
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

function deleteRole(o, callback){
	//o=encodeJson(o);
    Edo.util.Ajax.request({
    	url: 'roleAction!commitAjax',
        type: 'post',
        params: {
        	dataString: Edo.util.Json.encode(o),
            method: 2
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

function updateRole(o, callback){
	//o=encodeJson(o);
    Edo.util.Ajax.request({
    	url: 'roleAction!commitAjax',
        type: 'post',
        params: {
        	dataString: Edo.util.Json.encode(o),
            method: 3
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

//授权用户
function authorizeRole(o, callback){
	//var o=encodeJson(o);
    Edo.util.Ajax.request({
    	url: 'roleAction!commitAjax',
        type: 'post',
        params: {
        	dataString: Edo.util.Json.encode(o),
            method: 5
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
function startAndStopRole(o, callback){
	//var o=encodeJson(o);
    Edo.util.Ajax.request({
    	url: 'roleAction!commitAjax',
        type: 'post',
        params: {
        	dataString: Edo.util.Json.encode(o),
            method: 6
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

function setRoleUser(o, callback){
    Edo.util.Ajax.request({
    	url: 'roleAction!commitAjax',
        type: 'post',
        params: {
        	dataString: Edo.util.Json.encode(o),
            method: 7
        },
        onSuccess: function(text){        
            var o = Edo.util.Json.decode(text);
            if(o.error == 0){
                if(callback) callback(o);
                Info("设置成功！");
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
        	dataString: Edo.util.Json.encode(o),
            method: 2
        },
        onSuccess: function(text){        
            var o = Edo.util.Json.decode(text);
            if(o.error == 0){
                if(callback) callback();
                Info("设置成功！");
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

function showRoleForm(callback){
    if(!Edo.get('roleForm')) {
        //创建角色面板
        Edo.create({
            id: 'roleForm',            
            type: 'window',title: '角色录入',
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
                    children:[{id: 'name', type: 'text', name: 'Name', valid: noEmpty}]
                },
                {
                    type: 'formitem',label: '说明:',
                    children:[{type: 'textarea', name: 'Description'}]
                },
                {
                    type: 'formitem',layout:'horizontal', padding: [8,0,8, 0],
                    children:[
                        {name: 'submitBtn', type: 'button', text: '提交表单', 
                            onclick: function(){
                                if(roleForm.valid()){
                                    var o = roleForm.getForm();
                                    if(roleForm.callback) roleForm.callback(o);
                                    roleForm.hide();
                                }
                            }
                        }
                    ]
                }
            ]
        });
    }
    roleForm.callback = callback;
    roleForm.show('center', 'middle', true);
    return roleForm;
}

function showAccessForm(){
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
					id: "menuTree",
					type: 'tree',
				    cls: 'e-tree-allow',
				    width: '100%',
				    height: '100%',
				    horizontalAlign: "center",
				    editAction: 'click',
				    //autoColumns: true,
				    columns: [
				        {
				            header: '  栏目',
				            dataIndex: 'Name',
				            width:200
				        },
	                    Edo.lists.Table.createRadioColumn({
	                    	header: '操作权限', 
	                    	dataIndex: 'MenuAccess', 
	                    	headerAlign:'center',
	                    	align: 'center',
	                    	width:300,
	                    	data: MenuAccess, 
	                    	valueField: 'id', 
	                    	displayField: 'name',
	                    	onsubmitedit: function(e){
	                    		var r = menuTree.getSelected();    
	                    		if(r.MenuAccess==0){
	                    			var rc=menuTree.data.getChildren(r,true); 
	                    			rc.each(function(c){
	                    				menuTree.data.update(c, 'MenuAccess', r.MenuAccess);
	                    			});
	                    		}else{
	                    			setParentAccess(r);
	                    		}
	                    	}
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
                 					return "全部数据";
                 				}else{
		                            for(var i=0,l=DataAccess.length; i<l; i++){
		                                var g = DataAccess[i];
		                                if(g.id == v){
    		                                return g.name;
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
				        		if(!menuTree.data.isLeaf(r)){
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
                        	name: 'submitBtn', type: 'button', text: '提交设置', 
                            onclick: function(){
                        		var ras=new Array();
                        		var flag=0;
                                menuTree.data.view.each(function(o){  
									if(o.__status=="update"){
										var obj=new Object;
										obj.MenuID=o.ID;
										obj.RoleID=o.ID;
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
		var rp=menuTree.data.findParent(r);
		menuTree.data.update(rp, 'MenuAccess', r.MenuAccess);
		setParentAccess(rp);
	}
}

function setChildAccess(id){
	var o=menuTree.data.getById(id);
	var oc=menuTree.data.getChildren(o,true); 
	oc.each(function(c){
		menuTree.data.update(c, 'MenuAccess', o.MenuAccess);
		menuTree.data.update(c, 'DataAccess', o.DataAccess);
	});
}



