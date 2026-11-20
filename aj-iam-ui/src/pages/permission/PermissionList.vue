<template>
    <div>
        <div style="margin-bottom: 20px;">
            <span style="float:right;">
                <Button @click="showCreate" v-if="!isPickup" type="primary" icon="ios-add"
                    style="margin-right: 10px;">增加权限</Button>
                <Button @click="getData" icon="ios-refresh">刷新</Button>
            </span>
            <Input style="width: 30%;" @on-search="doSearch" search enter-button placeholder="搜索权限的名称或者说明" />
        </div>
        <Table border :columns="columnsDef" :data="list.data" style="min-height:250px">
            <template v-slot:action="{ row }">
                <a v-if="isPickup" style="margin-right: 5px" @click="onPickup(row)">选择</a>
                <span v-if="!isPickup">
                    <Poptip confirm transfer title="确定删除？" @on-ok="doDelete(row.id)">
                        <a style="margin-right: 5px;color:red" icon="ios-trash">删除</a>
                    </Poptip>
                    |
                    <a style="margin-right: 5px;color:green;" @click="edit(row.id)" icon="ios-edit">编辑</a>
                </span>
            </template>
        </Table>

        <Page style="margin:20px auto;text-align: center;" :total="list.total" v-model:current="list.current"
            :page-size="list.limit" @on-page-size-change="handleChangePageSize" @on-change="handlePageChange"
            size="small" show-total show-elevator show-sizer />

        <Modal v-model="isShowEditWin" :title="isCreate ? '创建权限' : '编辑权限' + permissionData.id" width="600"
            :closable="!isSaving" :mask-closable="!isSaving">
            <Form :model="permissionData" :rules="ruleValidate" :label-width="100"
                style="margin-right: 10%;margin-left: 3%;">
                <FormItem label="权限名称" prop="name">
                    <Input v-model="permissionData.name" placeholder="请输入权限名称……"></Input>
                </FormItem>
                <FormItem label="权限编码" prop="code">
                    <Input v-model="permissionData.code" placeholder="请输入权限编码……"></Input>
                </FormItem>
                <FormItem label="权限说明">
                    <Input type="textarea" :rows="4" v-model="permissionData.content" placeholder="请输入权限说明……"></Input>
                </FormItem>
                <FormItem label="权限状态">
                    <label><input type="radio" v-model="permissionData.stat" value="0" /> 启用</label> &nbsp;
                    <label><input type="radio" v-model="permissionData.stat" value="2" /> 禁用</label>
                </FormItem>
                <FormItem v-if="!isCreate" style="color:gray;">
                    创建于 {{ formatPermissionDate(permissionData.createDate) }}
                    修改于 {{ formatPermissionDate(permissionData.updateDate) }}
                </FormItem>
            </Form>
            <template #footer>
                <Button :disabled="isSaving" @click="isShowEditWin = false">取消</Button>
                <Button type="primary" :loading="isSaving" @click="save">确定</Button>
            </template>
        </Modal>
    </div>
</template>

<script lang="ts">
import { CommonUI } from '@ajaxjs/ui';
import { DateFormat } from '@ajaxjs/util';
import { promptLogin } from '../../common/login';

export default {
    props: {
        isPickup: { type: Boolean, default: false },
        onPickup: { type: Function, default() { } }
    },
    data() {
        return {
            api: `${window['config'].iamApi}/common_api`,
            isCreate: true,
            isShowEditWin: false,
            isSaving: false,
            permissionData: {} as PermissionEntry,
            listData: [] as PermissionEntry[],
            list: {
                total: 0,
                limit: 5,
                current: 1,
                data: []
            },
            ruleValidate: {
                name: [
                    { required: true, message: '该字段非空约束', trigger: 'blur' }
                ],
                code: [
                    { required: true, message: '该字段非空约束', trigger: 'blur' }
                ],
            }
        };
    },
    mounted(): void {
        this.getData();
    },
    computed: {
        columnsDef() {
            return [
                CommonUI.List.id,
                {
                    title: "权限名称",
                    key: "name",
                },
                {
                    title: "权限编码",
                    key: "code",
                    ellipsis: true
                },
                ...(this.isPickup ? [] : [CommonUI.List.status]),
                CommonUI.List.createDate,
                {
                    title: "操作",
                    slot: "action",
                    key: 'action', // 添加一个唯一的 key
                    width: 120,
                },
            ];
        },
        modalTitle(): string {  // 使用计算属性来动态生成标题，避免在模板中进行字符串拼接
            return this.isCreate ? '创建权限' : `编辑权限 ${this.permissionData.id || ''}`;
        }
    },
    methods: {
        formatPermissionDate(value?: string): string {
            if (!value || Number.isNaN(Date.parse(value)))
                return '—';

            return DateFormat.dateFormat(value, 'yyyy-MM-dd hh:mm:ss');
        },
        async requestPermission(path: string, failure: string, options: RequestInit = {}): Promise<ApiResponseResult | null> {
            try {
                const response = await fetch(this.api + path, { credentials: 'include', ...options });
                const result = await response.json().catch(() => null);

                if (!response.ok || !result?.status || result.data === false) {
                    const detail = result?.message || (!response.ok ? `HTTP ${response.status}` : '');
                    this.$Message.error(detail ? `${failure}：${detail}` : failure);
                    if (response.status === 401)
                        promptLogin();
                    return null;
                }

                return result;
            } catch {
                this.$Message.error(`${failure}：无法连接服务器，请稍后重试`);
                return null;
            }
        },
        async getData(): Promise<void> {
            let path = `/permission/page_no?pageNo=${this.list.current}&limit=${this.list.limit}`;
            if (this.isPickup)
                path += '&q_stat=0';

            const result = await this.requestPermission(path, '获取权限列表失败');
            if (result)
                CommonUI.List.getPageList(this, this.list)(result);
        },
        pickup(index: number): void { },
        doSearch(): void {
            alert(9);
        },
        showCreate(): void {
            this.permissionData = { id: 0, name: '' };
            this.isShowEditWin = true;
            this.isCreate = true;
        },
        async doDelete(id: number): Promise<void> {
            if (await this.requestPermission(`/permission/${id}`, '删除权限失败', { method: 'DELETE' })) {
                this.$Message.success('删除成功');
                this.getData();
            }
        },
        async edit(id: number): Promise<void> {
            const result = await this.requestPermission(`/permission/${id}`, '获取权限详情失败');
            if (!result) return;
            if (!result.data) {
                this.$Message.error('未找到该权限的详情');
                return;
            }

            this.permissionData = result.data as PermissionEntry;
            this.isShowEditWin = true;
            this.isCreate = false;
        },
        handleChangePageSize(p: number): void {
            this.list.limit = p;
            this.list.current = 1;// 通常改变每页大小后，需要回到第一页
            this.getData();
        },
        // 新增方法来处理页码改变
        handlePageChange(page: number): void {
            this.list.current = page;
            this.getData();
        },
        async save(): Promise<void> {
            if (this.isSaving) return;
            const data = CommonUI.List.copyBeanClean(this.permissionData);
            const isCreate = this.isCreate;

            this.isSaving = true;
            try {
                const result = await this.requestPermission('/permission', isCreate ? '创建权限失败' : '修改权限失败', {
                    method: isCreate ? 'POST' : 'PUT',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify(isCreate ? data : { ...data, id: this.permissionData.id }),
                });
                if (result) {
                    this.isShowEditWin = false;
                    this.$Message.success(isCreate ? '创建成功' : '修改成功');
                    this.getData();
                }
            } finally {
                this.isSaving = false;
            }
        }
    },
    watch: {
        isPickup(): void {
            this.getData();
        }
    }
};
</script>
