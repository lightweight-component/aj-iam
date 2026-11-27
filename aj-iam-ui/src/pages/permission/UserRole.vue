<template>
  <section class="user-role">
    <h2 style="margin-bottom: 20px">用户角色</h2>
    <div class="user-role-toolbar">
      <div class="user-role-filters">
        <Input v-model="filters.loginId" placeholder="搜索账号或用户名" aria-label="搜索账号或用户名" search clearable @on-search="applyFilters" @on-clear="applyFilters" />
        <Input v-model="filters.phone" placeholder="搜索手机" aria-label="搜索手机" search clearable @on-search="applyFilters" @on-clear="applyFilters" />
        <Input v-model="filters.email" placeholder="搜索邮件" aria-label="搜索邮件" search clearable @on-search="applyFilters" @on-clear="applyFilters" />
        <Select v-model="filters.tenantId" placeholder="全部租户" aria-label="租户筛选" clearable :loading="tenantsLoading" @on-change="applyFilters" @on-clear="applyFilters">
          <Option v-for="tenant in tenants" :key="tenant.id" :value="tenant.id">{{ tenant.name }}</Option>
        </Select>
      </div>
      <Button type="text" icon="ios-refresh" title="刷新列表" aria-label="刷新列表" :loading="loading" @click="loadPage(pageNo)" />
    </div>
    <Alert v-if="tenantError" type="error" show-icon>{{ tenantError }}</Alert>
    <Alert v-if="loadError" type="error" show-icon>{{ loadError }}</Alert>
    <Table :columns="columns" :data="rows" :loading="loading" no-data-text="暂无用户">
      <template #roles="{ row }">
        {{ roleNames(row) }}
      </template>
      <template #createDate="{ row }">
        {{ registrationDate(row) }}
      </template>
      <template #action="{ row }">
        <div class="user-role-actions">
          <a href="#" @click.prevent="showDetails(row)">查看</a>
          <a href="#" @click.prevent="showRoleAssignment(row)">分配角色</a>
        </div>
      </template>
    </Table>

    <Page class="user-role-pagination" :model-value="pageNo" :page-size="pageSize" :total="total" show-total show-elevator @on-change="loadPage" />

    <Modal v-model="detailsVisible" title="用户详情" footer-hide>
      <dl v-if="selectedUser" class="user-role-details">
        <template v-for="field in detailFields" :key="field.label">
          <dt>{{ field.label }}</dt>
          <dd>{{ field.value ?? '—' }}</dd>
        </template>
      </dl>
    </Modal>

    <Modal v-model="assignmentVisible" :title="assignmentTitle" :closable="!savingRoles" :mask-closable="!savingRoles">
      <Alert v-if="roleSaveError" type="error" show-icon>{{ roleSaveError }}</Alert>
      <p v-if="roleTreeLoading" role="status">正在加载角色…</p>
      <Alert v-else-if="roleTreeError" type="error" show-icon>{{ roleTreeError }}</Alert>
      <Tree v-else-if="assignmentVisible" :data="roleTree" :inert="savingRoles" show-checkbox check-strictly class="assignment-role-tree" @on-check-change="onRoleCheck">
        <template #title="{ data }">
          <span>{{ data.title }}</span>
          <span v-if="data.content" class="role-description">{{ data.content }}</span>
        </template>
      </Tree>
      <template #footer>
        <Button :disabled="savingRoles" @click="assignmentVisible = false">取消</Button>
        <Button type="primary" :disabled="changedRoleIds.size === 0" :loading="savingRoles" @click="saveRoleAssignment">保存</Button>
      </template>
    </Modal>
  </section>
</template>

<script setup lang="ts">
import { computed, getCurrentInstance, onMounted, reactive, ref } from 'vue';
import { XhrFetch } from '../../common/http';
import { dateFormat } from '../../common/utils';

interface UserRoleRow {
  id: number;
  loginId?: string;
  username?: string;
  phone?: string;
  email?: string;
  tenantName?: string;
  defaultRoleId?: number | null;
  defaultRoleName?: string | null;
  roles?: { id: number; name: string }[];
  createDate?: string;
}

interface RoleNode {
  id: number;
  title: string;
  content?: string | null;
  expand?: boolean;
  checked?: boolean;
  children?: RoleNode[];
}

const columns = [
  { title: 'ID', key: 'id', width: 72 },
  { title: '账号', key: 'loginId', minWidth: 110 },
  { title: '用户名', key: 'username', minWidth: 100 },
  { title: '手机', key: 'phone', minWidth: 128 },
  { title: '邮件', key: 'email', minWidth: 150, tooltip: true },
  { title: '租户名称', key: 'tenantName', minWidth: 110 },
  { title: '角色', slot: 'roles', minWidth: 140 },
  { title: '注册日期', slot: 'createDate', width: 158 },
  { title: '操作', slot: 'action', width: 132 },
];

const pageSize = 9;
const { $Message } = getCurrentInstance()!.appContext.config.globalProperties;
const pageNo = ref(1);
const total = ref(0);
const rows = ref<UserRoleRow[]>([]);
const loading = ref(false);
const loadError = ref('');
const filters = reactive({ loginId: '', phone: '', email: '', tenantId: '' as number | string });
const tenants = ref<{ id: number; name: string }[]>([]);
const tenantsLoading = ref(false);
const tenantError = ref('');
const detailsVisible = ref(false);
const selectedUser = ref<UserRoleRow>();
const assignmentVisible = ref(false);
const assignmentTitle = ref('');
const assignmentUserId = ref<number>();
const roleTree = ref<RoleNode[]>([]);
const roleTreeLoading = ref(false);
const roleTreeError = ref('');
const checkedRoleIds = ref<number[]>([]);
const changedRoleIds = ref(new Set<number>());
const savingRoles = ref(false);
const roleSaveError = ref('');
let assignedRoleIds = new Set<number>();
let defaultRoleId: number | null | undefined;
let appliedFilters = new URLSearchParams();
let latestRequest = 0;

function roleNames(user: UserRoleRow): string {
  const names = [...(user.roles?.map((role) => role.name) ?? []), user.defaultRoleName];
  return [...new Set(names.filter(Boolean))].join('、') || '—';
}

function registrationDate(user: UserRoleRow): string {
  return user.createDate ? dateFormat(user.createDate) : '—';
}

const detailFields = computed(() => {
  const user = selectedUser.value;
  if (!user) return [];

  return [
    { label: 'ID', value: user.id },
    { label: '账号', value: user.loginId },
    { label: '用户名', value: user.username },
    { label: '手机', value: user.phone },
    { label: '邮件', value: user.email },
    { label: '租户名称', value: user.tenantName },
    { label: '角色', value: roleNames(user) },
    { label: '注册日期', value: registrationDate(user) },
  ];
});

function showDetails(user: UserRoleRow): void {
  selectedUser.value = user;
  detailsVisible.value = true;
}

function toRoleTree(nodes: RoleNode[], checkedIds: ReadonlySet<number>, excludedId?: number | null): RoleNode[] {
  return nodes.flatMap(({ id, title, content, expand, children }) => {
    const visibleChildren = children?.length ? toRoleTree(children, checkedIds, excludedId) : undefined;
    if (id === excludedId) {
      return visibleChildren ?? [];
    }
    return [{ id, title, content: content?.trim(), expand, checked: checkedIds.has(id), children: visibleChildren }];
  });
}

async function showRoleAssignment(user: UserRoleRow): Promise<void> {
  if (savingRoles.value) return;

  assignmentTitle.value = `分配角色 · ${user.loginId || '#' + user.id}`;
  assignmentUserId.value = user.id;
  defaultRoleId = user.defaultRoleId;
  assignedRoleIds = new Set(user.roles?.map((role) => role.id));
  checkedRoleIds.value = [];
  changedRoleIds.value.clear();
  roleSaveError.value = '';
  assignmentVisible.value = true;
  if (roleTreeLoading.value) return;

  roleTree.value = [];
  roleTreeError.value = '';
  roleTreeLoading.value = true;
  try {
    await XhrFetch.get(`${window.config.iamApi}/permission/role_tree`, (result) => {
      if (result.status && Array.isArray(result.data)) {
        roleTree.value = toRoleTree(result.data, assignedRoleIds, defaultRoleId);
      } else {
        roleTreeError.value = result.message || '获取角色树失败';
      }
    });
  } finally {
    roleTreeLoading.value = false;
  }
}

function onRoleCheck(checkedNodes: RoleNode[], changedNode: RoleNode): void {
  checkedRoleIds.value = checkedNodes.map((node) => node.id);
  if (Boolean(changedNode.checked) === assignedRoleIds.has(changedNode.id)) {
    changedRoleIds.value.delete(changedNode.id);
  } else {
    changedRoleIds.value.add(changedNode.id);
  }
}

async function saveRoleAssignment(): Promise<void> {
  const userId = assignmentUserId.value;
  if (savingRoles.value || userId === undefined || changedRoleIds.value.size === 0) {
    return;
  }

  savingRoles.value = true;
  roleSaveError.value = '';
  try {
    const data = { userId, roleIds: checkedRoleIds.value };
    await XhrFetch.post(`${window.config.iamApi}/admin/role_to_user`, data, (result) => {
      const saved: unknown = result.data;
      if (!result.status || saved !== true) {
        roleSaveError.value = (!result.status && result.message) || '分配角色失败，请确认用户和角色仍然有效';
        return;
      }

      assignmentVisible.value = false;
      $Message.success('角色分配成功');
      void loadPage(pageNo.value);
    });
  } finally {
    savingRoles.value = false;
  }
}

function applyFilters(): void {
  appliedFilters = new URLSearchParams();
  for (const [key, value] of Object.entries(filters)) {
    const filter = String(value ?? '').trim();
    if (filter) appliedFilters.set(key, filter);
  }
  void loadPage(1);
}

async function loadTenants(): Promise<void> {
  tenantsLoading.value = true;
  try {
    await XhrFetch.get(`${window.config.iamApi}/common_api/tenant/list`, (result) => {
      const list = Array.isArray(result.data) ? result.data : result.data?.list;
      if (result.status && Array.isArray(list)) {
        tenants.value = list;
      } else {
        tenantError.value = result.message || '获取租户列表失败';
      }
    });
  } finally {
    tenantsLoading.value = false;
  }
}

async function loadPage(page: number): Promise<void> {
  const requestId = ++latestRequest;
  pageNo.value = page;
  rows.value = [];
  loadError.value = '';
  loading.value = true;

  try {
    const params = new URLSearchParams(appliedFilters);
    params.set('pageNo', String(page));
    params.set('pageSize', String(pageSize));
    const url = `${window.config.iamApi}/admin/user_role?${params}`;
    await XhrFetch.get(url, (result) => {
      if (requestId !== latestRequest) return;

      if (!result.status || !Array.isArray(result.data?.list)) {
        loadError.value = result.message || '获取用户角色列表失败';
        return;
      }

      rows.value = result.data.list;
      total.value = result.data.totalCount ?? 0;
    });
  } finally {
    if (requestId === latestRequest) loading.value = false;
  }
}

onMounted(() => {
  void loadPage(1);
  void loadTenants();
});
</script>

<style scoped>
.user-role-toolbar,
.user-role-filters {
  display: flex;
  align-items: flex-start;
  gap: 12px;
}

.user-role-toolbar {
  justify-content: space-between;
  margin-bottom: 16px;
}

.user-role-filters {
  flex-wrap: wrap;
}

.user-role-filters > * {
  width: 160px;
}

.user-role-actions {
  display: flex;
  gap: 12px;
  white-space: nowrap;
}

.user-role-pagination {
  margin-top: 20px;
  text-align: center;
}

.user-role-details {
  display: grid;
  grid-template-columns: 5em 1fr;
  gap: 12px;
}

.user-role-details dd {
  margin: 0;
  overflow-wrap: anywhere;
}

.assignment-role-tree {
  max-height: 60vh;
  overflow-y: auto;
}

.assignment-role-tree :deep(.ivu-tree-title) {
  max-width: calc(100% - 44px);
  white-space: normal;
  overflow-wrap: anywhere;
}

.role-description {
  margin-inline-start: 8px;
  color: #757575;
}
</style>
