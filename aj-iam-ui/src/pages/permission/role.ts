import { XhrFetch } from '@ajaxjs/util';
import { CommonUI } from '@ajaxjs/ui';

export default {
    data(): {} {
        return {
            contextData: null,
            roleTreeData: [],
            roleForm: {
                isTop: false,
                isCreate: false
            }
        };
    },
    methods: {
        handleContextMenu(data: any): void {
            this.contextData = data;
        },
        editRole(): void {
            this.roleForm.isCreate = false;
            this.isShisShowRoleEditForm = true;

            this.roleForm.isTop = this.contextData.parentId == -1;

            XhrFetch.get(`${this.simpleApi}/role/${this.contextData.id}`, (j: ApiResponseResult) => {
                if (j.status) {
                    this.currentRole = j.data;
                } else
                    this.$Message.warning(j.message || '获取数据失败');
            });
        },
        createTopRoleNode(): void {
            this.roleForm.isTop = true;
            this.roleForm.isCreate = true;
            this.currentRole = {};
            this.contextData = { id: -1 };
            this.isShisShowRoleEditForm = true;
        },
        delRole(): void {
            let treeNodeName: string = this.contextData.title;

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
            this.currentRole = {};
            this.isShisShowRoleEditForm = true;
        },
        refreshRoleList(): void {
            XhrFetch.get(`${this.permissionApi}/role_tree`, (j: ApiResponseResult) => {
                if (j.status) {
                    this.roleTreeData = j.data;
                } else
                    this.$Message.warning(j.message || '获取数据失败');
            });
        },

        saveRole(): void {
            let data: any = CommonUI.List.copyBeanClean(this.currentRole);
            data.parentId = this.contextData.id;

            if (this.roleForm.isCreate) {
                XhrFetch.post(`${this.simpleApi}/role`, (j: ApiResponseResult) => {
                    if (j.status) {
                        this.$Message.success('创建成功');
                        this.refreshRoleList();
                    }
                }, data);
            } else {
                XhrFetch.put(`${this.simpleApi}/role/${data.id}`, (j: ApiResponseResult) => {
                    if (j.status) {
                        this.$Message.success('修改成功');
                        this.refreshRoleList();
                    }
                }, data);
            }
        },

        onTreeNodeClk(nodeArr: any[], node: any): void {
            // debugger
            this.currentRole = { name: node.title, id: node.id };
        }
    }
}