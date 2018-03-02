/**
    @name Edo.navigators.ToggleBar
    @class
    @typeName togglebar
    @description selectedIndex,overClickable(true时,当鼠标移动到某一按钮上,此按钮被激发fire click事件)
    @extend Edo.navigators.Navigator
*/
Edo.navigators.ToggleBar = function(config){
    
    /**
        @name Edo.navigators.ToggleBar#beforeselectionchange
        @event
        @description 选中状态改变前事件
        @property {Number} selectedIndex 新选中索引        
    */      
    
    /**
        @name Edo.navigators.ToggleBar#selectionchange
        @event
        @description 选中状态改变事件
        @property {Number} selectedIndex 当前选中索引
    */        
            
    Edo.navigators.ToggleBar.superclass.constructor.call(this);         
};
Edo.navigators.ToggleBar.extend(Edo.navigators.Navigator,{
    componentMode: 'control',
    valueField: 'selectedIndex',   
    /**
        @name Edo.navigators.ToggleBar#selectedIndex
        @property
        @type Number
        @description 选中的index
    */ 
    selectedIndex: -1,
    /**
        @name Edo.navigators.ToggleBar#selectedItem
        @property
        @type Object
        @description 选中的对象
    */    
    selectedItem: null,  
    
    selectedCls: 'e-togglebar-selected',  
    
    elCls: 'e-togglebar e-nav e-box e-ct e-div',
    
    addChildAt: function(index, c){
        var c = Edo.navigators.ToggleBar.superclass.addChildAt.apply(this, arguments);
        c.on('click', this._onChildrenClick, this);        
        return c;
    },
    removeChildAt: function(index){
        var c = Edo.navigators.ToggleBar.superclass.removeChildAt.apply(this, arguments);
        if(index == this.selectedIndex){            
            var child = this.getChildAt(index);                    
            if(!child){
                child = this.getChildAt(index-1);
            }
            if(child){
                this.set('selectedItem', child);                  
            }else{
                this._setSelectedIndex(-1);
            }
        }       
               
        return c;
    },
        
    _setSelectedItem: function(value){
        var index = this.children.indexOf(value);
        if(index != -1 && value != this.selectedItem){
            this.selectedIndex = -1;
        }
        this._setSelectedIndex(index);
    },
    _setSelectedIndex: function(value){
        
        if(!this.rendered){
            this.selectedIndex = value;
            return;
        }
        if(value < 0){
            value = -1;
        }
        if(value >= this.children.length){
            value = this.children.length -1;
        }
        if(this.selectedIndex === value) return;   
        
        if(this.fireEvent('beforeselectionchange', {
            type: 'beforeselectionchange',
            source: this,
            selectedIndex: value
        }) !== false){
            this.selectedIndex = value;        
            this.children.each(function(o,i){
                if(i==value) {
                    o.set('pressed', true);
                    o.addCls(this.selectedCls);
                }
                else {
                    o.set('pressed', false);
                    o.removeCls(this.selectedCls);
                }
            },this);
            
            this.selectedItem = this.getDisplayChildren()[value];
            this.fireEvent('selectionchange', {
                type: 'selectionchange',
                source: this,
                index: this.selectedIndex,
                selectedIndex: this.selectedIndex
            });
            
            this.changeProperty('selectedIndex', value);
            this.el.style.visible = 'hidden';
            //this.el.style.display = 'none';
            this.relayout('selectedIndex');
        }
    },  
    _onChildrenClick: function(e){    
        var index = this.children.indexOf(e.source);       
        if(index != this.selectedIndex){
            this._setSelectedIndex(index);
        }
    },
    render: function(){
        Edo.navigators.ToggleBar.superclass.render.apply(this, arguments);
        var index = this.selectedIndex;
        delete this.selectedIndex;
        this._setSelectedIndex(index);
    }
});

Edo.navigators.ToggleBar.regType('togglebar');