/*
    
    待完成:
        点击行,不选择,但可折叠...声明一个属性,如果
*/
/**
    @name Edo.lists.Tree
    @class
    @typeName tree
    @description 树状组件
    @extend Edo.lists.Table
    @example 


*/ 
Edo.lists.Tree = function(){
/**
    @name Edo.lists.Tree#beforetoggle
    @event 
    @description 节点折叠前事件
*/
/**
    @name Edo.lists.Tree#toggle
    @event 
    @description 节点折叠事件
*/
    Edo.lists.Tree.superclass.constructor.call(this);
    
    this.on('beforebodymousedown', this._onbeforebodymousedownHandler, this, 0);
}
Edo.lists.Tree.extend(Edo.lists.Table,{    

    elCls: 'e-tree e-table e-dataview e-div',
    /**
        @name Edo.lists.Tree#data
        @type Edo.data.DataTree
        @description 树形数据对象        
    */
    /**
        @name Edo.lists.Tree#treeColumn
        @property
        @type String
        @description 作为树形节点的列id
    */
    treeColumn: '',
    treeColumnCls: 'e-tree-treecolumn',
    
    collapseCls: 'e-tree-collapse',
    expandCls: 'e-tree-expanded',
    
    dragDropModel: 'treedragdrop',
    enableColumnSort: false,
    
    enableStripe: false,
    
    _onbeforebodymousedownHandler: function(e){
        
        var t = e.target;
        var r = e.record;
        if(e.button == Edo.util.MouseButton.left){        
            var nodeicon = Edo.util.Dom.hasClass(t, 'e-tree-nodeicon');
            
            if(nodeicon){
            
                if(this.fireEvent('beforetoggle', {
                        type: 'beforetoggle',
                        source: this,
                        record: r,
                        row: r
                    }) !== false){
                    
                    if(Edo.util.Dom.findParent(t, 'e-tree-collapse', 3)){
                        
                        this.submitEdit();
                        //this.data.expand(r);
                        this.data.expand.defer(1, this.data, [r]);
                        return false;
                    }else if(Edo.util.Dom.findParent(t, 'e-tree-expanded', 3)){            
                        this.submitEdit();
                        
                        //this.data.collapse(r);
                        this.data.collapse.defer(1, this.data, [r]);
                        
                        return false;
                    }
                    this.fireEvent('toggle', {
                        type: 'toggle',
                        source: this,
                        record: r,
                        row: r
                    });
                }else{
                
                    r.expanded = !r.expanded;
                    //this.data.toggle(r);
                    return false;
                }                
            }            
        }
    },
    _onDataChanged: function(e){
        if(e){
            switch(e.action){
            case 'expand':
                this.doExpand(e.record);
            break;
            case 'collapse':
                this.doCollapse(e.record);
            break;
            }
        }
        Edo.lists.Tree.superclass._onDataChanged.call(this, e);    
    },    
    doCollapse: function(record){    
        this.refresh();
        return;
        var d = this.getItemEl(record);
        if(d){
            d = Edo.util.Dom.getbyClass('e-treenode', d);
            Edo.util.Dom.removeClass(d, this.expandCls);
            Edo.util.Dom.addClass(d, this.collapseCls);
        }        
        this.data.iterateChildren(record, function(o){
            if(!this.data.isDisplay(o)){
                var d = this.getItemEl(o);
                if(d){                
                    d.style.display = 'none';
                }
            }
        }, this);
    },
    doExpand: function(record){
        this.refresh();
        return;
        var d = this.getItemEl(record);
        if(d){
            d = Edo.util.Dom.getbyClass('e-treenode', d);
            Edo.util.Dom.addClass(d, this.expandCls);
            Edo.util.Dom.removeClass(d, this.collapseCls);
        }
        //判断是否有子元素DOM, 如果没有, 则加上
//        if(record.children && record.children.length > 0){
//            //var cnode = record.children
//        }
        this.data.iterateChildren(record, function(o, i){
            if(this.data.isDisplay(o)){
                var p = this.data.findParent(o);
                var d = this.getItemEl(o);
                
//                if(!d){
//                    this.insertItem(i, [o], {parentNode: p});
//                    d = this.getItemEl(o);
//                }
                
                d.style.display = '';
            }
        }, this);
    },
    //刷新节点(以及子节点)
    refreshNode: function(node){
        
    },
    applyTreeColumn: function(){
        var column = this.getColumn(this.treeColumn);
        if(!column) column = this.columns[0];
        this.treeColumn = column.id;
        
        if(column.renderer == this.treeRenderer) return;
        if(column.renderer) column._renderer = column.renderer;
        column.renderer = this.treeRenderer;
        
        column.cls = this.treeColumnCls;
    },   
    treeRenderer: function(v, r, c, i, data, t){
        var left = r.__depth * 18;
        var w = c.width;
        if(w < 60) w = 60;
        var hasChildren = r.__hasChildren ||  r.__viewicon;        
        //var s = '<div class="e-treenode '+(hasChildren ? (r.expanded !== false ? 'e-tree-expanded' : 'e-tree-collapse') : '')+'" style="padding-left:'+left+'px;height:'+(r.__height)+'px;">';                                                
        
        var expanded = r.expanded;
        
        
        var rowCls = hasChildren ? (expanded == true ? t.expandCls : t.collapseCls) : '';
        
        //var s = '<div class="e-treenode '+(hasChildren ? (expanded == true ? t.expandCls : t.collapseCls) : '')+'" style="padding-left:'+left+'px;height:'+(r.__height)+'px;">';                                                
        var s = '<div class="e-treenode '+rowCls+'">';                                                
        if(r.__depth!=0){
        	s = '<div class="e-child-treenode '+rowCls+'">';       
        }
        var offset = left;
        if(r.__depth==0){
            s += '<a href="javascript:;" hidefocus class="e-tree-nodeicon"></a>';                        
        }else{
        	s += '<span class="e-tree-leaficon"></span>';  
        }
        
        offset += 18;
        
        if(r.icon) {
            s += '<div class="'+r.icon+'" style="width:18px;height:20px;overflow:hidden;position:absolute;top:0px;left:'+offset+'px;"></div>';
            offset += 16;
        }
        
        if(c._renderer && c._renderer != t.treeRenderer){
            v = c._renderer(v, r, c, i, data, t);
        }
        if(r.__depth==0){
	        s += '<div id="'+t.id+'-textnode-'+r.__id+'" class="e-tree-nodetext" style="left:59px;">'+v+'</div>';
	        s += '</div>';
        }else{
	        s += '<div id="'+t.id+'-textnode-'+r.__id+'" class="e-tree-nodechildtext" style="left:74px;">'+v+'</div>';
	        s += '</div>';     	
        }
        
        
        if(!data.isDisplay(r)) {
            r.__style = 'display:none';
        }else{
            r.__style = 'display:""';
        }
        
        return s;
    },
    _setColumns: function(columns){
        Edo.lists.Tree.superclass._setColumns.call(this, columns);    
         
        this.applyTreeColumn();
    },
    _setData: function(data){
        if(typeof data == 'string') data = window[data];
        if(!data) return;        
        if(data.componentMode != 'data') {
            if(!this.data){
                data = new Edo.data.DataTree(data);
            }else{
                this.data.load(data);
                return;
            }
        }
        Edo.lists.Tree.superclass._setData.call(this, data);     
    },
    getTextNode: function(record){
        record = this.getRecord(record);        
        return Edo.getDom(this.id+'-textnode-'+record.__id);
    },
    insertItem: function(index, records, e){        
        var p = e.parentNode;
        
        var targetRecord = p.children[index+1];
        var target = this.getItemEl(targetRecord);        
        for(var i=0,l=records.length; i<l; i++){
            var record = records[i];
            var s = this.getItemHtml(record, index+i);
            var el;
            if(target){
                el = Edo.util.Dom.before(target, s);
            }else{
                el = Edo.util.Dom.append(this.getCtEl(), s);
            }
            
            this.data.iterateChildren(record, function(o, j){
                var s = this.getItemHtml(o, j);
                el = Edo.util.Dom.after(el, s);
            }, this);
        }
    },
    removeItem: function(records){
        for(var i=0,l=records.length; i<l; i++){
            var record = records[i];
            var d = this.getItemEl(record);
            Edo.removeNode(d);
            
            this.data.iterateChildren(record, function(o){
                var d = this.getItemEl(o);
                Edo.removeNode(d);
            }, this);
        }
    },
    moveItem: function(index, records, e){//是否当发生一些不常见操作的时候, 直接refresh?
        
        this.refresh();
//        var p = e.parentNode;
//        var target = p.children[index];
//                   
//        for(var i=0,l=records.length; i<l; i++){
//            var record = records[i];
//            var ri = this.data.indexOf(record);
//            var d = Edo.getDom(this.createItemId(record));
//            
//            var t = 
//            Edo.util.Dom.before(t, d);
//        }
    }    
});

Edo.lists.Tree.regType('tree');