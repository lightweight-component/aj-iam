<template>
    <div class="holder">
        <h2 style="margin-bottom:25px;">模块选择器<span></span></h2>

        <div style="margin-bottom: 20px;">
            <span style="float:right;">
                <Button @click="getData" icon="ios-refresh">刷新</Button>
            </span>
            <Input style="width: 30%;" @on-search="doSearch" search enter-button placeholder="搜索权限的名称或者说明" />
        </div>
        <Table border :columns="columnsDef" :data="list.data" style="min-height:250px">
            <template v-slot:action="{ row }">
                <a style="margin-right: 5px" @click="onPickup(row)">选择</a>
            </template>
        </Table>
        <Page style="margin:20px auto;text-align: center;" :total="list.total" v-model:current="list.current"
            :page-size="list.limit" @on-page-size-change="handleChangePageSize" @on-change="handlePageChange"
            size="small" show-total show-elevator show-sizer />
    </div>
</template>

<script lang="ts">
import { h } from 'vue';
import { dateFormat, promptLogin } from '../../common/utils';

const permissionStatuses: Record<number, { label: string; color: string }> = {
    [-1]: { label: '草稿', color: 'gray' },
    0: { label: '启用', color: 'green' },
    1: { label: '已删除', color: 'red' },
    2: { label: '禁用', color: 'red' },
};

export default {
    props: {
        onPickup: { type: Function, default() { } }
    },
    data() {
        return {
            api: `${window['config'].iamApi}/common_api`,
            permissionData: {} as PermissionEntry,
            listData: [] as PermissionEntry[],
            list: {
                total: 0,
                limit: 5,
                current: 1,
                data: [] as PermissionEntry[]
            },
        };
    },
    mounted(): void {
        this.getData();
    },
    computed: {
        columnsDef() {
            return [
                { title: '#', width: 60, key: 'id', align: 'center' },
                {
                    title: "模板名称",
                    key: "name",
                },
                {
                    title: "模块编码",
                    key: "code",
                    ellipsis: true
                },
                {
                    title: '状态',
                    width: 80,
                    render: (_render: typeof h, { row }: { row: PermissionEntry }) => {
                        const { label, color } = permissionStatuses[Number(row.stat)] ?? permissionStatuses[0];
                        return h('div', { style: { color } }, label);
                    },
                },
                {
                    title: '创建日期',
                    width: 180,
                    align: 'center',
                    render: (_render: typeof h, { row }: { row: PermissionEntry }) =>
                        h('div', this.formatPermissionDate(row.createDate)),
                },
                {
                    title: "操作",
                    slot: "action",
                    key: 'action', // 添加一个唯一的 key
                    width: 120,
                },
            ];
        },

    },
    methods: {
        formatPermissionDate(value?: string): string {
            if (!value || Number.isNaN(Date.parse(value)))
                return '—';

            return dateFormat(value, 'yyyy-MM-dd hh:mm:ss');
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
            const path: string = `/module/page_no?pageNo=${this.list.current}&limit=${this.list.limit}&q_stat=0`;
            const result = await this.requestPermission(path, '获取权限列表失败');

            if (result) {
                this.list.total = result.data.totalCount ?? 0;
                this.list.data = result.data.list ?? [];
            }
        },
        pickup(index: number): void { },
        doSearch(): void {
            alert(9);
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
    },
    watch: {
        isPickup(): void {
            this.getData();
        }
    }
};
</script>