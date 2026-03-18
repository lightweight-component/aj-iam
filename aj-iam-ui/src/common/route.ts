import '@ajaxjs/ui/style.css';
import { createRouter, createWebHashHistory, type Router } from 'vue-router';
import { admin, system, IAM, shop, ConfigWdiget } from '@ajaxjs/ui';
import Org from '../pages/user/org.vue';
import RBAC from '../pages/rbac/rbac.vue';
import Permission from '../pages/permission/permission.vue';

const UserView = { template: '<div>User: {{ $route.params.id }}</div>' }

// 2. 定义路由数组
const routes = [
    {
        path: '/',
        name: 'home',
        component: admin.HomePage
    },

    {
        path: '/login',
        component: admin.Login
    },
    {
        path: '/user/org',
        component: Org
    },
    {
        path: '/permission/rbac',
        component: RBAC
    },
    {
        path: '/permission/permission',
        component: Permission
    },

    {
        path: '/pay/transaction',
        component: shop.Transaction
    },
    {
        path: '/user/list',
        component: IAM.User
    },
    {
        path: '/user/login_log',
        component: IAM.LoginLog
    },
    {
        path: '/user/tenant',
        component: IAM.Tenant
    },
    {
        path: '/user/app',
        component: IAM.App
    },
    {
        path: '/user/token',
        component: IAM.Token
    },
    {
        path: '/user/profile',
        component: IAM.UserCenter
    },
    {
        path: '/system/data_dict',
        component: system.DataDict
    },
    {
        path: '/system/list-mgr',
        component: ConfigWdiget.ListMgr
    },
    {
        path: '/list-info',
        component: ConfigWdiget.ListInfo
    },
    {
        path: '/system/form-mgr',
        component: ConfigWdiget.FormMgr
    },
    {
        path: '/system/form-info',
        component: ConfigWdiget.FormInfo
    },
    {
        path: '/system/schedule',
        component: system.Schedule
    },
    {
        path: '/resource/article',
        component: system.Article
    },

    {
        path: '/resource/article_edit',
        component: system.ArticleEdit
    },
    {
        path: '/user/:id', // 动态路径参数
        name: 'user',
        component: UserView,
        // props: true // 将路由参数作为 props 传递给组件 (推荐做法)
        props: (route: any) => ({ id: parseInt(route.params.id) }) // 函数模式，可以进行类型转换
    },
    {
        path: '/users/:userId/posts/:postId',
        name: 'userPost',
        component: { template: '<div>User ID: {{ $route.params.userId }}, Post ID: {{ $route.params.postId }}</div>' }
        // props: true // 多个动态参数也会作为 props 传递
    },
    {
        path: '/search',
        name: 'search',
        component: { template: '<div>Search Query: {{ $route.query.q }}</div>' }, // 访问查询参数 ?q=something
        props: (route: any) => ({ query: route.query.q || '' })
    },
    {
        path: '/admin',
        name: 'admin',
        component: { template: '<div><h1>Admin Area</h1><router-view /></div>' }, // 嵌套路由
        children: [
            {
                // 当 /admin/profile 匹配成功时
                path: 'profile',
                name: 'adminProfile',
                component: { template: '<p>Admin Profile</p>' }
            },
            {
                // 当 /admin/posts 匹配成功时
                path: 'posts',
                name: 'adminPosts',
                component: { template: '<p>Admin Posts</p>' }
            }
        ]
    },
    {
        path: '/redirect-home',
        redirect: '/' // 重定向到 '/'
        // redirect: { name: 'home' } // 也可以重定向到命名路由
        // redirect: to => { // 动态重定向
        //   // 方法接收目标路由作为参数
        //   // return 重定向的字符串路径/路径对象
        // }
    },
    {
        path: '/old-about',
        redirect: '/about' // 别名重定向示例
    },
    {
        path: '/about-us', // 别名
        alias: '/about',
        // component: AboutView // 访问 /about-us 和 /about 都会渲染 AboutView
    },
    {
        path: '/:pathMatch(.*)*', // Catch-all 通配符路由 (放在最后)
        name: 'NotFound',
        component: { template: '<div>404 - Page Not Found</div>' }
    }
];

// 3. 创建路由器实例
const router: Router = createRouter({ // 使用 createRouter 工厂函数
    history: createWebHashHistory(import.meta.env.BASE_URL), // 使用 HTML5 History 模式 (推荐)
    // history: createWebHashHistory(), // 使用 Hash 模式
    routes
});

// 4. 可选：添加全局前置守卫
router.beforeEach((to, from) => {
    // console.log('Navigating from', from.path, 'to', to.path);
    // 返回 false 取消导航
    // 返回一个路由地址 (string 或 Location 对象) 进行重定向
    // 不返回或返回 true 允许导航
    if (to.name === 'admin' && !isAdmin()) { // 假设有一个 isAdmin() 函数检查权限
        // 用户未登录且尝试访问 admin 页面，则重定向到登录页 (假设有登录页)
        // return { name: 'login' }
        alert('Access denied!')
        return false; // Cancel navigation
    }
});

function isAdmin(): boolean {
    // Mock check
    return false;
}

// 5. 导出路由器实例
export default router;