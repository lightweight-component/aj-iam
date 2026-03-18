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
                <a v-if="isPickup" style="margin-right: 5px" @click="onPickup ? (row)">选择</a>
                <span v-if="!isPickup">
                    <Poptip confirm title="确定删除？" @on-ok="doDelete(row.id)">
                        <a style="margin-right: 5px;color:red" icon="ios-trash">删除</a>
                    </Poptip>
                    |
                    <a style="margin-right: 5px;color:green;" @click="edit(row.id)" icon="ios-edit">编辑</a>
                </span>
            </template>
        </Table>

        <Page style="margin:20px auto;text-align: center;" :total="list.total" :current.sync="list.current"
            :page-size="list.limit" @on-page-size-change="handleChangePageSize" size="small" show-total show-elevator
            show-sizer />

        <Modal v-model="isShowEditWin" :title="isCreate ? '创建权限' : '编辑权限' + permissionData.id" width="600"
            @on-ok="save">
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
                    创建于 {{ permissionData.createDate }} 修改于 {{ permissionData.updateDate }}
                </FormItem>
            </Form>
        </Modal>
    </div>
</template>

<script lang="ts">
import { XhrFetch } from '@ajaxjs/util';
import { CommonUI } from '@ajaxjs/ui';

export default {
    props: {
        isPickup: { type: Boolean, default: false },
        onPickup: { type: Function }
    },
    data() {
        return {
            isCreate: true,
            isShowEditWin: false,
            permissionData: {} as PermissionEntry,
            columnsDef: [
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
                CommonUI.List.status,
                CommonUI.List.createDate,
                {
                    title: "操作",
                    slot: "action",
                    width: 120,
                },
            ],
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
    methods: {
        getData(): void {
            let api: string = `${window['config'].iamApi}/permission/page`;
            if (this.isPickup)
                api += '?q_stat=0';

            XhrFetch.get(api, CommonUI.List.getPageList(this, this.list), {
                limit: this.list.limit,
                pageNo: this.list.current,
            });
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
        doDelete(id: number): void {
            XhrFetch.del(`${this.simpleApi}/permission/${id}`, (j: ApiResponseResult) => {
                if (j.status) {
                    this.$Message.success('删除成功');
                    this.getData();
                }
            });
        },
        edit(id: number): void {
            this.isShowEditWin = true;
            this.isCreate = false;

            XhrFetch.get(`${this.simpleApi}/permission/${id}`, (j: ApiResponseResult) => {
                if (j.status)
                    this.permissionData = j.data as PermissionEntry;
            });
        },
        handleChangePageSize(p: number): void {
            this.list.limit = p;
            this.getData();
        },
        save(): void {
            let data: any = CommonUI.List.copyBeanClean(this.permissionData);

            if (this.isCreate) {
                XhrFetch.post(`${this.simpleApi}/permission`, (j: ApiResponseResult) => {
                    if (j.status) {
                        this.$Message.success('创建成功');
                        this.getData();
                    }
                }, data);
            } else {
                XhrFetch.put(`${this.simpleApi}/permission/${this.permissionData.id}`, (j: ApiResponseResult) => {
                    if (j.status) {
                        this.$Message.success('修改成功');
                        this.getData();
                    }
                }, data);
            }
        }
    },
    watch: {
        /**
         * 分页
         * 
         * @param v 
         */
        'list.current'(v: number): void {
            this.getData();
        },
        isPickup(v: boolean): void {
            this.getData();

            if (v) {
                for (let i: number = 0; i < this.columnsDef.length; i++)
                    if (this.columnsDef[i].title === '状态') {
                        this.columnsDef.splice(i, 1);
                        break;
                    }
            } else
                this.columnsDef.splice(3, 0, CommonUI.List.status);
        }
    }
};
</script>