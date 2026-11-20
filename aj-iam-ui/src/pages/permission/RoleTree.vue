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
import { copyWriteFields } from '../../common/entity-payload';

type ContextData = {
    id?: number;
    parentId?: number;
    title?: string;
};

export default {
    emits: ['select-change'],
    data() {
        return {
            roleApi: `${window.config.iamApi}/common_api/role`,
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
            const node = this.contextData;
            const { id } = node;

            if (id == null || id <= 0) {
                this.$Message.warning('请选择要编辑的角色');
                return;
            }

            XhrFetch.get(`${this.roleApi}/${id}`, (j: ApiResponseResult) => {
                if (this.contextData !== node)
                    return;

                const role = j?.data as Role | null | undefined;
                if (!j?.status || role?.id == null || String(role.id) !== String(id)) {
                    this.$Message.warning(j?.status ? '未找到该角色的详情，请刷新角色列表后重试' : j?.message || '获取角色详情失败');
                    return;
                }

                this.currentRole = role;
                this.roleForm.isCreate = false;
                this.roleForm.isTop = node.parentId == -1;
                this.isShisShowRoleEditForm = true;
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
            const data = copyWriteFields(this.currentRole);
            if (this.roleForm.isCreate) {
                data.parentId = this.contextData.id;
                XhrFetch.post(this.roleApi, data, (j: ApiResponseResult) => {
                    if (j.status) {
                        this.$Message.success('创建成功');
                        this.refreshRoleList();
                    }
                });
            } else {
                XhrFetch.put(this.roleApi, { ...data, id: this.currentRole.id }, (j: ApiResponseResult) => {
                    if (j.status) {
                        this.$Message.success('修改成功');
                        this.refreshRoleList();
                    }
                });
            }
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
