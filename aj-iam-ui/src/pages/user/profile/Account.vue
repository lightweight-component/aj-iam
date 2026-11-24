<template>
    <div class="user-center-account">
        <h3 class="aj-center-title">帐号管理</h3>
        <ul class="safe">
            <!--         <li>	
            <a href="javascript:openPopupTpl('modiflyPhone');">修改登录名</a>
            <div :class="$parent.username ? 'ok' : 'fail' ">登录名</div>
            <div>登录名即用户名</div>
        </li> -->
            <li>
                <a href="javascript:void(0);" @click="showSetPhone = true">设置手机</a>
                <div :class="$parent.USER.phone ? 'ok' : 'fail'">绑定手机</div>
                <div>{{ $parent.USER.phone || '未绑定手机' }}</div>
            </li>
            <li>
                <a href="javascript:void(0);" @click="showSetEmail = true">设置邮箱</a>
                <div :class="$parent.USER.email ? 'ok' : 'fail'">绑定邮箱</div>
                <div>{{ $parent.USER.email || '未绑定邮箱' }}</div>
            </li>
            <li>
                <a href="javascript:void(0);" @click="showChangePsw = true">修改密码</a>
                <div class="ok">设置密码</div>
                <div>已设置</div>
            </li>
            <li>
                <a href="javascript:void(0);" @click="delAccount">账号注销</a>
                <div style="padding-left:3%;">删除帐号</div>

                <div>删除该帐号以及所有该帐号关联的信息</div>
            </li>
        </ul>
        <aj-layer v-if="showSetPhone">
            <div style="width:800px; height:400px;text-align: center;">
                <h1>设置手机</h1>
                <aj-process-line />
                <form class="aj-form" style="width:300px; margin: 0 auto;">
                    <div>
                        <Input type="text" placeholder="请输入手机号码" v-model="userPhone" size="30" />
                    </div>
                    <div class="left-a">
                        <Input type="text" placeholder="请输入验证码" size="10" /> <Button
                            @click="sendPhoneCode">发送验证码</Button>
                    </div>
                    <div>
                        <Button type="primary" @click="savePhone">保存手机</Button> &nbsp;&nbsp;&nbsp;<a
                            href="javascript:void(0);" @click="showSetPhone = false">取消</a>
                    </div>
                </form>
            </div>
        </aj-layer>
        <aj-layer v-if="showSetEmail">
            <div style="width:800px; height:400px;text-align: center;">
                <h1>设置邮箱</h1>
                <aj-process-line />
                <form class="aj-form" style="width:300px; margin: 0 auto;">
                    <div>
                        <Input type="email" placeholder="请输入邮箱" v-model="userMail" size="30" />
                    </div>
                    <div class="left-a">
                        <Input type="text" placeholder="请输入邮箱验证码" size="10" /> <Button
                            @click="sendEmailCode">发送验证码</Button>
                    </div>
                    <div>
                        <Button type="primary" @click="saveEmail">保存邮箱</Button> &nbsp;&nbsp;&nbsp;<a
                            href="javascript:void(0);" @click="showSetEmail = false">取消</a>
                    </div>
                </form>
            </div>
        </aj-layer>
        <aj-layer v-if="showChangePsw">
            <div style="width:800px; height:400px;text-align: center;">
                <h1>修改密码</h1>
                <aj-process-line />
                <form class="aj-form" style="width:300px; margin: 0 auto;">
                    <div>
                        <Input type="password" placeholder="请输入原密码" v-model="modiflyPsw.oldPsw" size="30" />
                    </div>
                    <div>
                        <Input type="password" placeholder="请输入新密码" v-model="modiflyPsw.newPsw" size="30" />
                    </div>
                    <div>
                        <Input type="password" placeholder="请重复输入新密码" v-model="modiflyPsw.newPsw2" size="30" />
                    </div>
                    <div>
                        <Button type="primary" @click="doModiflyPsw">修改密码</Button> &nbsp;&nbsp;&nbsp;<a
                            href="javascript:void(0);" @click="showChangePsw = false">取消</a>
                    </div>
                </form>
            </div>
        </aj-layer>
    </div>
</template>

<script lang="ts">
import { XhrFetch } from '../../../common/http';
import Layer from './Layer.vue';
import ProcessLine from './ProcessLine.vue';

export default {
    emits: ['saved'],
    data() {
        return {
            userMail: null,
            userPhone: null,
            isEmailVerified: null,
            isShowDelAccount: false,
            showSetPhone: false,
            showSetEmail: false,
            showChangePsw: false,
            modiflyPsw: { oldPsw: '', newPsw: '', newPsw2: '' }
        };
    },
    components: { 'aj-layer': Layer, 'aj-process-line': ProcessLine },
    mounted() {
    },
    methods: {
        /**
         * 修改密码
         */
        doModiflyPsw(): void {
            const { oldPsw, newPsw, newPsw2 } = this.modiflyPsw;

            if (!oldPsw || !newPsw || !newPsw2) {
                this.$Message.success('请填写完整的信息');
                return;
            }

            if (newPsw !== newPsw2) {
                this.$Message.success('两次密码不一致');
                return;
            }

            XhrFetch.post(`${window.config.iamApi}/reset_psw/modify_psw`, { oldPsw, newPsw }, (resp: ApiResponseResult) => {
                console.log(resp);
                if (resp.status) {
                    this.$Message.success('修改密码成功！');
                    // this.$Modal.remove(); // 或者 this.$Modal.destroy() (取决于版本)

                    setTimeout(() => {
                        // this.$router.push('/login');
                    }, 2000);
                } else {
                    this.$Message.error('修改密码失败：' + resp.message);
                    console.error(resp.message || '修改密码失败');
                }
            });
        },
        delAccount(): void {
            this.$Modal.confirm({
                title: '确定删除帐号吗？⚠️ ',
                content: '注销账号将永久删除您的账户及所有数据❗<br />注销后，您的所有数据将被永久删除，无法恢复，请谨慎操作。',
                loading: true, // 显示加载状态，防止用户重复点击
                onOk: () => {
                    XhrFetch.del(`${window.config.dsApiRoot}/user`, (resp: ApiResponseResult) => {
                        console.log(resp);
                        if (resp.status) {
                            localStorage.removeItem('userInfo');
                            localStorage.removeItem('isLoggedIn');
                            this.$Message.success('注销成功！');
                            this.$Modal.remove(); // 或者 this.$Modal.destroy() (取决于版本)

                            setTimeout(() => {
                                this.$router.push('/login');
                            }, 2000);
                        } else
                            console.error(resp.message || '注销失败');
                    });
                }
            });
        },
        sendPhoneCode(): void {
            const phone: string | null = this.userPhone;

            if (!phone) {
                this.$Message.error('请输入手机号码');
                return;
            }
        },
        savePhone(): void {
            const phone: string | null = this.userPhone;

            if (!phone) {
                this.$Message.error('请输入手机号码');
                return;
            }

            XhrFetch.put(`${window.config.iamApi}/user`, { phone }, (result: ApiResponseResult) => {
                if (result.status) {
                    this.$Message.success('修改成功');
                    this.$emit('saved');
                }
            });
        },
        sendEmailCode(): void {
            const email: string | null = this.userMail;

            if (!email) {
                this.$Message.error('请输入邮箱');
                return;
            }
        },
        saveEmail(): void {
            const email: string | null = this.userMail;

            if (!email) {
                this.$Message.error('请输入邮箱');
                return;
            }

            XhrFetch.put(`${window.config.iamApi}/user`, { email }, (result: ApiResponseResult) => {
                if (result.status) {
                    this.$Message.success('修改成功');
                    this.$emit('saved');
                }
            });
        }
    }
};
</script>

<style lang="less">
.aj-form {
    &>div {
        margin-bottom: 15px;

        &.left-a {
            text-align: left;

            .ivu-input-wrapper {
                width: 193px;
            }
        }
    }
}
</style>
