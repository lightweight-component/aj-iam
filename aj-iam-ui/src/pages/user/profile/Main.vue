<template>
    <div class="user-center-main">
        <h3 class="aj-center-title">概览</h3>
        <h4>个人信息</h4>
        <hr class="aj-hr" />
        <br />
        <table class="aj-form-table">
            <tbody>
                <tr>
                    <td>用户 id：</td>
                    <td>#{{ user.id }}</td>
                    <td>用户账号：</td>
                    <td>{{ user.loginId }}</td>
                </tr>
                <tr>
                    <td>昵称</td>
                    <td><input type="text" v-model="user.username" :class="{ editMode }" /></td>
                    <td>真实姓名</td>
                    <td><input type="text" v-model="user.realName" :class="{ editMode }" /></td>
                </tr>
                <tr>
                    <td>性别</td>
                    <td>
                        <span v-if="!editMode">
                            {{ user.gender == 'MALE' ? '男' : '' }}
                            {{ user.gender == 'FEMALE' ? '女' : '' }}
                            {{ user.gender == 'UNKNOWN' ? '保密' : '' }}
                        </span>

                        <span v-else>
                            <label><input v-model="user.gender" value="MALE" type="radio"> 男 </label>
                            <label><input v-model="user.gender" value="FEMALE" type="radio"> 女 </label>
                            <label><input v-model="user.gender" value="UNKNOWN" type="radio"> 保密 </label>
                        </span>
                    </td>
                    <td>生日</td>
                    <td>
                        <span v-if="!editMode">{{ user.birthday }}</span>
                        <DatePicker v-else :model-value="user.birthday" type="date" placeholder="选择生日"
                            style="width: 180px" @on-change="user.birthday = $event" />
                    </td>
                </tr>
                <tr>
                    <td>电话</td>
                    <td><input type="text" v-model="user.phone" /></td>
                    <td>邮件</td>
                    <td><input type="text" v-model="user.email" /></td>
                </tr>
                <tr>
                    <td>地区</td>
                    <td colspan="3"><input type="text" v-model="user.location" :class="{ editMode }" /></td>
                </tr>
                <tr>
                    <td>租户</td>
                    <td>{{ user.tenantName }}#{{ user.tenantId }} </td>
                    <td>组织</td>
                    <td></td>
                </tr>
                <tr>
                    <td>简介</td>
                    <td colspan="3"><textarea :class="{ editMode }" v-model="user.content"></textarea>
                    </td>
                </tr>
            </tbody>
        </table>
        <div class="note">
            <span style="float:right">注册于 {{ dateFilter(user.createDate) }}</span>
            1、登录账号不可修改；2、欲修改电话或邮件请移步至<a href="javascript:void(0)" @click="$parent.showing = 'account'">“账号管理”</a>。
        </div>
        <div class="btn">
            <Button type="primary" @click="onEdit">{{ editMode ? '保存' : '编辑' }}</Button>
        </div>
    </div>
</template>

<script lang="ts">
import { XhrFetch } from '../../../common/http';
import { dateFormat } from '../../../common/utils';

export default {
    emits: ['saved'],
    props: {
        user: {
            type: Object,
            default: () => { },
        }
    },
    data() {
        return {
            editMode: false,
        }
    },
    methods: {
        onEdit(): void {
            if (!this.editMode)
                this.editMode = true;
            else {
                // save
                const { username, realName, gender, birthday, content } = this.user;

                XhrFetch.put(`${window.config.iamApi}/user`, { username, realName, gender, birthday, content }, (result: ApiResponseResult) => {
                    if (result.status) {
                        this.$Message.success('修改成功');
                        this.editMode = false;
                        this.$emit('saved');
                    }
                });
            }
        },
        dateFilter(v: string): string {
            return dateFormat(v);
        }

    }
};
</script>

<style lang="less">
.note {
    color: gray;
    margin: 20px 0;
}

.btn {
    text-align: center;
    padding: 20px;
    width: 100%;

    button {
        width: 150px;
    }
}

input.editMode {
    border-bottom: 1px solid lightgray !important;
}

textarea {
    width: 92% !important;
    min-height: 50px !important;
    padding: 5px !important;

    &.editMode {
        border: 1px solid lightgray !important;
        background-color: white !important;
        border-radius: 5px !important;
    }
}
</style>
