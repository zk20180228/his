//审核
var NeedChecks = [
    {id: 1, name: '需要'},
    {id: 2, name: '不需'}
];

//状态
var States = [
    {id: 1, name: '停用'},
    {id: 2, name: '启用'}
];

//打开目标
var Targets = [
    {id: 'main', name: '主窗口'},
    {id: '_blank', name: '新窗口'}
]; 

//图标
var Icons = [
    {icon:'',name:'请选择图标！',src:''},
    {icon: 'menuicon_index', name:'首页',src: imagePath+'menuicon/16/index.png'},
    {icon: 'menuicon_user', name:'用户',src: imagePath+'menuicon/user.png'},
    {icon: 'menuicon_role', name:'角色',src: imagePath+'menuicon/role.png'},
    {icon: 'menuicon_menu', name:'栏目',src: imagePath+'menuicon/menu.png'},
    {icon: 'menuicon_article', name:'文章',src: imagePath+'menuicon/article.png'},
    {icon: 'menuicon_notice', name:'通知',src: imagePath+'menuicon/notice.png'}
];         

//栏目权限
var MenuAccess = [
    {id: 0, name: '<font size=2>&nbsp;无权限&nbsp;</font>'},
    {id: 1, name: '<font size=2>&nbsp;浏览&nbsp;</font>'},
    {id: 2, name: '<font size=2>&nbsp;编辑&nbsp;</font>'},
    {id: 3, name: '<font size=2>&nbsp;审核&nbsp;</font>'},
    {id: 4, name: '<font size=2>&nbsp;管理&nbsp;</font>'}
];

//数据权限
var DataAccess = [
    {id: 'A', name: '全部数据'},
    {id: 'B', name: '商户数据'},
    {id: 'C', name: '场馆数据'},
    {id: 'D', name: '企业数据'},
    {id: 'E', name: '个人数据'}
];

//功能分类
var FunClass = [
    {id: 1, name: '通用'},
    {id: 2, name: '系统'},
    {id: 3, name: '首页'},
    {id: 4, name: '文档'},
    {id: 5, name: '数据'},
    {id: 6, name: '服务'},
    {id: 7, name: '个人'},
    {id: 8, name: '搜索'}
]; 
