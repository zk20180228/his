/*
    1.样式皮肤
    2.关闭按钮
    3.滚动条(多的时候)
    4.下拉选择项(多的时候)
*/
/**
    @name Edo.navigators.TabBar
    @class
    @typeName tabbar
    @description tab切换器
    @extend Edo.navigators.ToggleBar
*/
Edo.navigators.TabBar = function(config){
    Edo.navigators.TabBar.superclass.constructor.call(this);     
    
};

Edo.navigators.TabBar.extend(Edo.navigators.ToggleBar,{
    
    /**
        @name Edo.navigators.TabBar#defaultHeight
        @property
        @default 22
    */
    defaultHeight: 35,
    /**
        @name Edo.navigators.TabBar#border
        @property
        @default [1,1,1,1]
    */
    border: [1,1,1,1],
    /**
        @name Edo.navigators.TabBar#padding
        @property
        @default [2,5,2,5]
    */
    padding: [0,5,0,5],
    /**
        @name Edo.navigators.TabBar#width
        @property
        @default '100%'
    */
    width: '100%',
    /**
        @name Edo.navigators.TabBar#verticalAlign
        @property
        @default 'bottom'
    */
    verticalAlign: 'bottom',
    /**
        @name Edo.navigators.TabBar#horizontalGap
        @property
        @default 2
    */
    horizontalGap: 2,
    /**
        @name Edo.navigators.TabBar#minHeight
        @property
        @default 17
    */
    minHeight: 17,
    
    selectedCls: 'e-tabbar-selected',  
    itemCls: 'e-tabbar-item',   //这个本来是为了做button的加粗效果,现在缓一缓,后续在做.主要是button的动态生成获取坐标,破坏了样式影响性!这是一个"框架级"的"严重"问题.
    
    elCls: 'e-tabbar e-togglebar e-nav e-box e-ct e-div',
    //selectedIndex: -1,
    
    /**
        @name Edo.navigators.TabBar#position
        @property
        @type String        
        @default top (left, top, right, bottom)
        @description tabbar位置
    */    
    position: 'top',
    
    _setPosition: function(value){        
        if(this.position != value){
            this.position = value;
            switch(value){
            case 'top':
                this.verticalAlign = 'bottom';
            break;
            case 'bottom':
                this.verticalAlign = 'top';
            break;
            case 'left':
                this.horizontalAlign = 'right';
            break;
            case 'right':
                this.horizontalAlign = 'left';
            break;
            }

            this.changeProperty('position', this.position);
            this.relayout('position', this);
        }
    },

    getInnerHtml: function(sb){
        this.elCls += ' e-tabbar'; 
        this.elCls += ' e-tabbar-' + this.position;
        
        Edo.navigators.TabBar.superclass.getInnerHtml.call(this, sb);
        sb[sb.length] = '<div class="e-tabbar-line"></div>';
    },
    createChildren: function(el){
        Edo.navigators.TabBar.superclass.createChildren.call(this, el);
        this.stripEl = Edo.util.Dom.append(this.scrollEl, '<div class="e-tabbar-line-strip"></div>');
        this.lineEl = this.scrollEl.nextSibling;        
    },
    syncSize: function(){    //设置组件尺寸,并设置容器子元素的所有尺寸!
        Edo.navigators.TabBar.superclass.syncSize.call(this);                
        
        //this.doSyncLine.defer(100, this);
        this.doSyncLine();
    },
    doSyncLine: function(){
        var box = this._getBox(true);
        var scrollBox = Edo.util.Dom.getBox(this.scrollEl);
        
        var selBox = this.selectedItem ? this.selectedItem._getBox(true) : {x:0,y:0,width:0,height:0,right:0,bottom:0};
        switch(this.position){
        case "top":
            selBox.x += 1;
            selBox.y = selBox.bottom - 1;
            selBox.width -= 2;
            selBox.height = 1;
            
            box.y = scrollBox.bottom-1;
            box.height = 5;//box.bottom - scrollBox.bottom;
        break;
        case "bottom":
            selBox.x += 1;
            selBox.width -= 2;
            selBox.height = 1;
                            
            box.height = scrollBox.y - box.y +1;                
        break;
        case "left":
            selBox.x = selBox.right - 1;
            selBox.width = 1;
            selBox.y += 1;
            selBox.height -= 2;
            
            box.x = scrollBox.right-1;
            box.width = 5;//box.right - scrollBox.right; 
        break;
        case "right":                
            selBox.y += 1;
            selBox.height -= 2;
            selBox.width = 1;
            
            //box.x = selBox.right-1;
            box.width =  scrollBox.x - box.x+1; 
            
            
        }
        Edo.util.Dom.setBox(this.stripEl, {
            x: selBox.x,
            y: selBox.y,
            width: selBox.width,
            height: selBox.height
        });
        
        Edo.util.Dom.setBox(this.lineEl, box);
    },
    addChildAt: function(){
        var a = arguments[1];
        if(this.position == 'top' || this.position == 'bottom'){
            a.height = "100%";
            //a.autoHeight = false;
        }else{
            a.width = "100%";
            //a.autoWidth = false;
        }
        var c = Edo.navigators.TabBar.superclass.addChildAt.apply(this, arguments);      
        c.addCls('e-tabbar-item');
        return c;
    }
});

Edo.navigators.TabBar.regType('tabbar');