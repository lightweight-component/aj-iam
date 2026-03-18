<template>
  <div class="layout">
    <Layout :style="{ minHeight: '100vh' }">
      <Sider collapsible :collapsed-width="78" v-model="isCollapsed">
        <Menu active-name="" theme="dark" width="auto" :class="menuitemClasses" style="margin-top: 64px;">
          <Submenu name="user">
            <template #title>
              <Icon type="ios-people" />
              用户管理
            </template>
            <MenuItem name="2-1" to="/user/list">
            <Icon type="ios-person"></Icon>
            <span>用户列表</span>
            </MenuItem>
            <MenuItem name="user-org" to="/user/org">
            <Icon type="ios-cube-outline"></Icon>
            <span>组织机构管理</span>
            </MenuItem>
            <MenuItem name="2-3" to="/user/login_log">
            <Icon type="md-key"></Icon>
            <span>登录日志</span>
            </MenuItem>
          </Submenu>

          <Submenu name="per">
            <template #title>
              <Icon type="ios-people" />
              权限管理
            </template>
            <MenuItem name="per-rbac" to="/permission/rbac">
            <Icon type="ios-person"></Icon>
            <span>角色管理</span>
            </MenuItem>
            <MenuItem name="per-per" to="/permission/permission">
            <Icon type="ios-cube-outline"></Icon>
            <span>权限管理</span>
            </MenuItem>
          </Submenu>

          <Submenu name="auth">
            <template #title>
              <Icon type="md-aperture" />
              认证管理
            </template>
            <MenuItem name="2-4" to="/user/tenant">
            <Icon type="ios-contact-outline"></Icon>
            <span>租户管理</span>
            </MenuItem>
            <MenuItem name="2-5" to="/user/app">
            <Icon type="md-apps"></Icon>
            <span>应用管理</span>
            </MenuItem>
            <MenuItem name="2-6" to="/user/token">
            <Icon type="md-link"></Icon>
            <span>Token 管理</span>
            </MenuItem>
          </Submenu>

          <Submenu name="system">
            <template #title>
              <Icon type="ios-options" />
              系统管理
            </template>
            <MenuItem name="3-1" to="/system/list-mgr">
            <Icon type="ios-list"></Icon>
            <span>列表管理</span>
            </MenuItem>
            <MenuItem name="3-5" to="/system/form-mgr">
            <Icon type="ios-paper-outline" />
            <span>表单管理</span>
            </MenuItem>
            <MenuItem name="3-4" to="/system/data_dict">
            <Icon type="md-grid" />
            <span>数据字典</span>
            </MenuItem>
            <MenuItem name="3-2">
            <Icon type="settings"></Icon>
            <span>文件管理</span>
            </MenuItem>
            <MenuItem name="3-6" to="/system/schedule">
            <Icon type="search" />
            <span>任务调度</span>
            </MenuItem>
            <MenuItem name="3-3">
            <Icon type="search"></Icon>
            <span>操作日志</span>
            </MenuItem>
          </Submenu>
        </Menu>
      </Sider>
      <Layout>

        <Header :style="{ background: '#fff', boxShadow: '0 2px 3px 2px rgba(0,0,0,.1)' }">
          <div style="float: right; cursor: pointer;" @click="$router.push('/user/profile')">
            <Avatar shape="square" :src="avatarUrl || '/images/empty-avatar.png'" /> {{ userLoginId }} 已登录
          </div>
          <span style="font-size:18px;font-weight: bold;letter-spacing: 1px;">AJ-IAM 管理后台</span>
        </Header>
        <Content :style="{ padding: '0 16px 16px' }">
          <Breadcrumb :style="{ margin: '16px 0' }">
            <BreadcrumbItem>Home</BreadcrumbItem>
            <BreadcrumbItem>Components</BreadcrumbItem>
            <BreadcrumbItem>Layout</BreadcrumbItem>
          </Breadcrumb>
          <Card style="min-height: 600px;overflow: hidden;padding-bottom: 20px;">
            <router-view />
          </Card>
        </Content>
      </Layout>
    </Layout>
  </div>
</template>

<script lang="ts">
if (localStorage.getItem('isLoggedIn') !== 'true' && confirm('你未登录，是否跳转到登录窗口？'))
  location.assign('#/login');

export default {
  data() {
    return {
      userLoginId: '',
      avatarUrl: '',
      isCollapsed: false
    };
  },
  computed: {
    menuitemClasses() {
      return [
        'menu-item',
        this.isCollapsed ? 'collapsed-menu' : ''
      ]
    }
  },
  mounted(): void {
    // App 启动时恢复状态
    const storedInfo = localStorage.getItem('userInfo');

    if (storedInfo) {
      try {
        const userInfo: any = JSON.parse(storedInfo);
        this.userLoginId = userInfo.loginId;
        this.avatarUrl = 'data:image/;base64,' + userInfo.avatarBlob;
      } catch (e) {
        console.error('Failed to parse user info from localStorage:', e);
        localStorage.removeItem('userInfo');
      }
    }
  },
  watch: {
    '$route.query.login_ok'(newId, oldId): void {
      if (newId == 1) {
        const userInfo: any = JSON.parse(localStorage.getItem('userInfo') as string);
        this.userLoginId = userInfo.loginId;
        this.avatarUrl = 'data:image/;base64,' + userInfo.avatarBlob;
      }
    },
  }
}
</script>

<style scoped>
.layout-con {
  height: 100%;
  width: 100%;
}

.menu-item span {
  display: inline-block;
  overflow: hidden;
  width: 75px;
  text-overflow: ellipsis;
  white-space: nowrap;
  vertical-align: bottom;
  transition: width .2s ease .2s;
}

.menu-item i {
  transform: translateX(0px);
  transition: font-size .2s ease, transform .2s ease;
  vertical-align: middle;
  font-size: 16px;
}

.collapsed-menu span {
  width: 0px;
  transition: width .2s ease;
}

.collapsed-menu i {
  transform: translateX(5px);
  transition: font-size .2s ease .2s, transform .2s ease .2s;
  vertical-align: middle;
  font-size: 22px;
}

.dev-run-preview .dev-run-preview-edit {
  display: none
}
</style>
