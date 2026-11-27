<template>
    <div class="holder">
        <h2 style="margin-bottom:25px;">用户角色管理<span>角色管理是将所有权限进行分类，形成角色，什么样的角色该拥有什么样的权限，分配给相应的管理人员</span></h2>

        <Button type="primary" style="margin-bottom:10px;" @click="createRole()">新建顶级角色</Button>

        <Table row-key="id" :columns="columns" :data="rows" :loading="loading" border size="small">
            <template #action="{ row }">
                <Button size="small" icon="md-add" type="success" @click="createRole(row)">新建</Button>
                <Button size="small" icon="md-create" type="info" @click="editRole(row)">编辑</Button>
                <Poptip transfer placement="left">
                    <Button size="small" icon="md-trash" type="error">删除</Button>
                    <template #content>
                        <b>请选择删除模式：</b>
                        <p>- 仅删除没有子角色的当前角色。</p>
                        <p>- 删除当前角色及其所有下级角色。</p>
                        <br />
                        <div style="text-align:center">
                            <Button size="small" type="primary" :disabled="!!row.children?.length"
                                @click="confirmDelete(row)">删除该角色</Button>
                            <Button size="small" type="primary" @click="confirmDelete(row)">删除所有角色</Button>
                        </div>
                    </template>
                </Poptip>
            </template>
        </Table>

        <Modal v-model="showEditor" :title="editorTitle" width="650" :closable="!saving" :mask-closable="!saving">
            <p v-if="editingId === null && form.parentId === ROOT_ROLE">你将新建顶级的角色。</p>
            <p v-else-if="editingId === null">你将在所选父角色下新建子角色。</p>

            <Form :label-width="100" :model="form" style="margin:5%">
                <FormItem label="父亲角色" v-if="editingId !== null || form.parentId !== ROOT_ROLE">
                    <Select v-model="form.parentId">
                        <Option v-if="editingId !== null" :value="ROOT_ROLE">顶级角色</Option>
                        <Option v-for="parent in parentOptions" :key="parent.id" :value="parent.id">
                            {{ parent.label }}
                        </Option>
                    </Select>
                </FormItem>
                <FormItem label="角色名称" prop="name">
                    <Input v-model="form.name" placeholder="名称为必填" />
                </FormItem>
                <FormItem label="角色说明" prop="content">
                    <Input type="textarea" v-model="form.content" :rows="4" placeholder="简介、说明、备注，选填" />
                </FormItem>
                <FormItem v-if="form.parentId !== ROOT_ROLE">
                    <Checkbox v-model="form.isInheritedParent">继承父级权限</Checkbox>
                </FormItem>
                <FormItem label="角色状态">
                    <RadioGroup v-model="form.stat">
                        <Radio :label="0">启用</Radio>
                        <Radio :label="2">禁用</Radio>
                    </RadioGroup>
                </FormItem>
            </Form>
            <template #footer>
                <Button :disabled="saving" @click="showEditor = false">取消</Button>
                <Button type="primary" :loading="saving" @click="saveRole">保存</Button>
            </template>
        </Modal>
    </div>
</template>

<script lang="ts">
import { defineComponent, h } from 'vue';
import { promptLogin } from '../../common/utils';
import { UserRole_List } from './list-cfg';

const ROOT_ROLE: number = -1;

type RoleNode = {
    id: number;
    parentId: number;
    title: string;
    code?: string | null;
    content?: string;
    permissionValue?: string;
    moduleValue?: string;
    updateDate?: string | number | null;
    children?: RoleNode[];
};

type RoleRow = Omit<RoleNode, 'children'> & {
    name: string;
    _showChildren: boolean;
    children?: RoleRow[];
};

function toTableRows(nodes: RoleNode[]): RoleRow[] {
    return nodes.map(({ children, ...node }) => ({
        ...node,
        name: node.title,
        _showChildren: true,
        ...(children?.length ? { children: toTableRows(children) } : {}),
    }));
}

function formatUpdateDate(value: RoleNode['updateDate']): string {
    if (typeof value === 'string')
        return value.replace('T', ' ').slice(0, 16);

    if (typeof value === 'number' && Number.isFinite(value))
        return new Date(value).toLocaleString('zh-CN', { dateStyle: 'short', timeStyle: 'short' });

    return '';
}

async function request<T>(path: string, method = 'GET', data?: object): Promise<T> {
    const response = await fetch(window.config.iamApi + path, {
        method,
        ...(data ? { headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(data) } : {}),
    });

    if (response.status === 401) {
        promptLogin();
        throw new Error('请先登录');
    }

    const result = await response.json().catch(() => null);
    if (!response.ok || !result?.status || result.data === false)
        throw new Error(result?.message || '角色请求失败');

    return result.data as T;
}

export default defineComponent({
    data() {
        return {
            ROOT_ROLE,
            rows: [] as RoleRow[],
            columns: [
                { title: '#', width: 100, align: 'center', key: 'id' },
                ...UserRole_List,
                { title: '角色编码', key: 'code', minWidth: 140, align: 'center' },
                { title: '权限总值', key: 'permissionValue', minWidth: 160, align: 'center' },
                { title: '模块权限总值', key: 'moduleValue', minWidth: 160, align: 'center' },
                {
                    title: '修改日期',
                    align: 'center',
                    width: 150,
                    render: (_render: typeof h, { row }: { row: RoleRow }) =>
                        h('span', formatUpdateDate(row.updateDate)),
                },
                { title: '编辑', align: 'center', width: 270, slot: 'action' },
            ],
            loading: false,
            loadingEditor: false,
            showEditor: false,
            saving: false,
            deleting: false,
            editingId: null as number | null,
            form: { parentId: ROOT_ROLE, name: '', content: '', isInheritedParent: true, stat: 0 },
        };
    },
    computed: {
        editorTitle(): string {
            if (this.editingId !== null)
                return '编辑[' + this.form.name + ']';

            return this.form.parentId === ROOT_ROLE ? '新建顶级节点' : '新建子角色';
        },
        parentOptions(): { id: number; label: string }[] {
            const options: { id: number; label: string }[] = [];
            const visit = (rows: RoleRow[], depth = 0) => {
                for (const row of rows) {
                    if (row.id === this.editingId) continue;
                    options.push({ id: row.id, label: '　'.repeat(depth) + (depth ? '└─' : '') + row.name });
                    if (row.children) visit(row.children, depth + 1);
                }
            };
            visit(this.rows);
            return options;
        },
    },
    mounted() {
        void this.loadRoles();
    },
    methods: {
        async loadRoles() {
            this.loading = true;
            try {
                this.rows = toTableRows(await request<RoleNode[]>('/permission/role_tree'));
            } catch (error) {
                this.showError(error);
            } finally {
                this.loading = false;
            }
        },
        createRole(parent?: RoleRow) {
            if (this.loadingEditor) return;
            this.editingId = null;
            this.form = { parentId: parent?.id ?? ROOT_ROLE, name: '', content: '', isInheritedParent: true, stat: 0 };
            this.showEditor = true;
        },
        async editRole(row: RoleRow) {
            if (this.loadingEditor) return;
            this.loadingEditor = true;
            try {
                const role = await request<Role & { parentId: number }>('/common_api/role/' + row.id);
                this.editingId = row.id;
                this.form = {
                    parentId: role.parentId ?? row.parentId ?? ROOT_ROLE,
                    name: role.name,
                    content: role.content ?? '',
                    isInheritedParent: !!role.isInheritedParent,
                    stat: Number(role.stat ?? 0),
                };
                this.showEditor = true;
            } catch (error) {
                this.showError(error);
            } finally {
                this.loadingEditor = false;
            }
        },
        async saveRole() {
            if (this.saving) return;
            const name = this.form.name.trim();
            if (!name) {
                this.$Message.warning('请输入角色名称');
                return;
            }
            const id = this.editingId;
            const data = { ...this.form, name, ...(id === null ? {} : { id }) };
            if (data.parentId === ROOT_ROLE) data.isInheritedParent = false;

            this.saving = true;
            try {
                await request('/common_api/role', id === null ? 'POST' : 'PUT', data);
                this.showEditor = false;
                this.$Message.success(id === null ? '创建成功' : '修改成功');
                await this.loadRoles();
            } catch (error) {
                this.showError(error);
            } finally {
                this.saving = false;
            }
        },
        confirmDelete(row: RoleRow) {
            if (this.deleting) return;
            this.$Modal.confirm({
                title: '删除角色',
                render: (render: typeof h) => render('p', `确定删除「${row.name}」吗？下级角色也会被删除。`),
                onOk: () => this.deleteRole(row.id),
            });
        },
        async deleteRole(id: number) {
            if (this.deleting) return;
            this.deleting = true;
            try {
                await request('/permission/role/' + id, 'DELETE');
                this.$Message.success('删除成功');
                await this.loadRoles();
            } catch (error) {
                this.showError(error);
            } finally {
                this.deleting = false;
            }
        },
        showError(error: unknown) {
            this.$Message.error(error instanceof Error ? error.message : '角色请求失败');
        },
    },
});
</script>

<style scoped>
Button {
    margin-right: 10px;
}
</style>
