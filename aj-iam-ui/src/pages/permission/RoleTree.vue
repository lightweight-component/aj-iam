<template>
    <div>
        <span class="btns">
            <Button type="primary" icon="ios-add" @click="createTopRoleNode">创建顶级角色</Button>
            <Button type="info" icon="ios-refresh" @click="refreshRoleList">刷新</Button>
        </span>
    </div>
    <h2>角色管理</h2>

    <div class="tree">
        <Tree :data="roleTreeData" @on-select-change="onTreeNodeClk" @on-contextmenu="handleContextMenu">
            <template #contextMenu>
                <DropdownItem @click="editRole" style="color: cornflowerblue">▶ 编辑角色</DropdownItem>
                <DropdownItem @click="addSubNode" style="color: green">+ 添加子节点</DropdownItem>
                <DropdownItem @click="delRole" style="color: #ed4014">✖ 删除角色</DropdownItem>
            </template>
        </Tree>
    </div>

    <Modal v-model="isShisShowRoleEditForm" :title="'角色' + (!roleForm.isCreate ? ' #' + currentRole.id : '')"
        @on-ok="saveRole">
        <Form :model="currentRole" :label-width="100" style="margin-right: 10%;margin-left: 3%;">
            <FormItem label="角色名称">
                <Input v-model="currentRole.name" placeholder="Enter something..."></Input>
            </FormItem>
            <FormItem label="角色说明">
                <Input type="textarea" :rows="4" v-model="currentRole.content" placeholder="Enter something..."></Input>
            </FormItem>
            <FormItem v-if="!roleForm.isTop">
                <Checkbox v-model="currentRole.isInheritedParent">继承父级权限</Checkbox>
            </FormItem>
            <FormItem label="角色状态">
                <label><input type="radio" v-model="currentRole.stat" value="0" /> 启用</label> &nbsp;
                <label><input type="radio" v-model="currentRole.stat" value="2" /> 禁用</label>
            </FormItem>
            <FormItem v-if="!roleForm.isCreate" style="color:gray;">
                创建于 {{ currentRole.createDate }} 修改于 {{ currentRole.updateDate }}
            </FormItem>
        </Form>
    </Modal>
</template>

<script lang="ts">
import { XhrFetch } from '@ajaxjs/util';
import { CommonUI } from '@ajaxjs/ui';

type ContextData = {
    id?: number;
    parentId?: number;
    title?: string;
};

export default {
    emits: ['select-change'],
    data() {
        return {
            roleApi: `${window.config.iamApi}/role`,
            permissionApi: `${window.config.iamApi}/permission`,
            isShisShowRoleEditForm: false,
            contextData: {} as ContextData,
            roleTreeData: [] as ContextData[],
            // 当前角色
            currentRole: {
                name: ''
            } as Role,
            roleForm: {
                isTop: false,
                isCreate: false
            }
        };
    },
    mounted(): void {
        this.refreshRoleList();
    },
    methods: {
        handleContextMenu(data: ContextData): void {
            this.contextData = data;
        },
        editRole(): void {
            this.roleForm.isCreate = false;
            this.isShisShowRoleEditForm = true;

            this.roleForm.isTop = this.contextData.parentId == -1;

            XhrFetch.get(`${this.roleApi}/${this.contextData.id}`, (j: ApiResponseResult) => {
                if (j.status) {
                    this.currentRole = j.data as Role;
                } else
                    this.$Message.warning(j.message || '获取数据失败');
            });
        },
        createTopRoleNode(): void {
            this.roleForm.isTop = true;
            this.roleForm.isCreate = true;
            this.contextData = { id: -1 };
            this.currentRole = {} as Role;
            this.isShisShowRoleEditForm = true;
        },
        delRole(): void {
            const treeNodeName: string = this.contextData.title || '';

            this.$Modal.confirm({
                title: '删除角色',
                content: `<p>确定删除 ${treeNodeName} 这个节点吗？<br />注意：该节点下<b>所有的子节点</b>也会一并被删除！</p>`,
                onOk: () => {
                    XhrFetch.del(`${this.permissionApi}/role/${this.contextData.id}`, (j: ApiResponseResult) => {
                        if (j.status) {
                            this.$Message.success('删除成功');
                            this.refreshRoleList();
                        } else
                            this.$Message.warning(j.message || '获取数据失败');
                    });
                }
            });
        },
        addSubNode(): void {
            this.roleForm.isTop = false;
            this.roleForm.isCreate = true;
            this.currentRole = {} as Role;
            this.isShisShowRoleEditForm = true;
        },
        refreshRoleList(): void {
            XhrFetch.get(`${this.permissionApi}/role_tree`, (j: ApiResponseResult) => {
                if (j.status)
                    this.roleTreeData = j.data as ContextData[];
                else
                    this.$Message.warning(j.message || '获取数据失败');
            });
        },

        saveRole(): void {
            const data: any = CommonUI.List.copyBeanClean(this.currentRole);
            data.parentId = this.contextData.id;

            if (this.roleForm.isCreate)
                XhrFetch.post(this.roleApi, data, (j: ApiResponseResult) => {
                    if (j.status) {
                        this.$Message.success('创建成功');
                        this.refreshRoleList();
                    }
                });
            else
                XhrFetch.put(`${this.roleApi}/${data.id}`, data, (j: ApiResponseResult) => {
                    if (j.status) {
                        this.$Message.success('修改成功');
                        this.refreshRoleList();
                    }
                });
        },
        onTreeNodeClk(nodeArr: any[], node: any): void {
            // debugger
            this.currentRole = { name: node.title, id: node.id } as Role;
            this.$emit('select-change', this.currentRole);
        }
    }
}
</script>

<style lang="less" scoped>
h2 {
    font-weight: bold;
    padding: 10px 0;
}

.btns {
    float: right;
    margin-right: 10%;
    margin-top: 10px;

    .ivu-btn {
        margin-left: 10px;
    }
}

.tree {
    border: 1px solid lightgray;
    margin: 20px 0;
    border-radius: 3px;
    width: 90%;
    padding: 5px;
    min-height: 600px;
}
</style>