import { createRouter, createWebHashHistory, type Router } from 'vue-router';
import { FormInfo, FormMgr, ListInfo, ListMgr } from '@ajax-js/configurable-widget';
import { startLogin } from './utils';
import HomePage from '../pages/admin/Home.vue';
import Orgs from '../pages/user/Orgs.vue';
import LoginLog from '../pages/user/LoginLog.vue';
import IamApp from '../pages/auth/App.vue';
import Tenant from '../pages/auth/Tenant.vue';
import Token from '../pages/auth/Token.vue';
import User from '../pages/user/User.vue';
import UserCenter from '../pages/user/profile/index.vue';
import RoleTreeTable from '../pages/permission/RoleTreeTable.vue';
import UserRole from '../pages/permission/UserRole.vue';
import Permission from '../pages/permission/PermissionAuth.vue';
import PermissionList from '../pages/permission/PermissionList.vue';
import ModulePermission from '../pages/auth/Module.vue';

const routes = [
    {
        path: '/',
        name: 'home',
        component: HomePage
    },

    {
        path: '/login',
        component: { render: () => null },
        beforeEnter: () => {
            startLogin();
            return false;
        },
    },
    {
        path: '/user/org',
        meta: { breadcrumbs: ['用户管理', '组织机构管理'] },
        component: Orgs
    },
    {
        path: '/permission/rbac',
        meta: { breadcrumbs: ['角色权限', '角色管理'] },
        component: RoleTreeTable
    },
    {
        path: '/permission/user_role',
        meta: { breadcrumbs: ['角色权限', '用户角色'] },
        component: UserRole
    },
    {
        path: '/permission/list',
        meta: { breadcrumbs: ['角色权限', '权限管理'] },
        component: PermissionList
    },
    {
        path: '/permission/permission',
        meta: { breadcrumbs: ['角色权限', '角色授权'] },
        component: Permission
    },
    {
        path: '/permission/module',
        meta: { breadcrumbs: ['角色权限', '模块权限'] },
        component: ModulePermission
    },

    {
        path: '/user/list',
        meta: { breadcrumbs: ['用户管理', '用户列表'] },
        component: User
    },
    {
        path: '/user/login_log',
        meta: { breadcrumbs: ['用户管理', '登录日志'] },
        component: LoginLog
    },
    {
        path: '/user/tenant',
        meta: { breadcrumbs: ['认证管理', '租户管理'] },
        component: Tenant
    },
    {
        path: '/user/app',
        meta: { breadcrumbs: ['认证管理', '应用管理'] },
        component: IamApp
    },
    {
        path: '/user/token',
        meta: { breadcrumbs: ['认证管理', 'Token 管理'] },
        component: Token
    },
    {
        path: '/user/profile',
        meta: { breadcrumbs: ['个人中心'] },
        component: UserCenter
    },
    {
        path: '/system/list-mgr',
        meta: { breadcrumbs: ['系统管理', '列表管理'] },
        component: ListMgr
    },
    {
        path: '/list-info',
        meta: { breadcrumbs: ['系统管理', '列表管理', '列表详情'] },
        component: ListInfo
    },
    {
        path: '/system/form-mgr',
        meta: { breadcrumbs: ['系统管理', '表单管理'] },
        component: FormMgr
    },
    {
        path: '/system/form-info',
        alias: '/form-info',
        meta: { breadcrumbs: ['系统管理', '表单管理', '表单详情'] },
        component: FormInfo
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
        path: '/:pathMatch(.*)*', // Catch-all 通配符路由 (放在最后)
        meta: { breadcrumbs: ['页面不存在'] },
        name: 'NotFound',
        component: { template: '<div>404 - Page Not Found</div>' }
    }
];

// 3. 创建路由器实例
const router: Router = createRouter({ // 使用 createRouter 工厂函数
    history: createWebHashHistory(import.meta.env.BASE_URL), // 使用 HTML5 History 模式 (推荐)
    // history: createWebHashHistory(), // 使用 Hash 模式
    // @ts-ignore 
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
