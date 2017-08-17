var navs = [{
	"title": "调度管理",
	"icon": "fa-navicon",
	"spread": true,
	"children": [ {
		"title": "集群管理",
		"icon": "&#xe63c;",
		"href": "cluster/list"
	}, {
        "title": "任务管理",
        "icon": "&#xe63c;",
        "href": "job/list"
    }, {
        "title": "调度日志",
        "icon": "&#xe63c;",
        "href": "dispatch/log/list"
    }, {
        "title": "正在运行任务",
        "icon": "&#xe63c;",
        "href": "job/running/list"
    }, {
        "title": "已删除的任务",
        "icon": "&#xe63c;",
        "href": "job/deleted/list"
    }]
},{
    "title": "系统管理",
    "icon": "fa-navicon",
    "spread": true,
    "children": [ {
        "title": "用户管理",
        "icon": "&#xe63c;",
        "href": "user/list"
    }, {
        "title": "报警人管理",
        "icon": "&#xe63c;",
        "href": "alarm-contacts/list"
    }]
}];