<template>
    <div class="mask">
        <Login @on-submit="handleSubmit" class="login">
            <h1>欢迎登录歪觅机器人管理后台</h1>
            <UserName name="username" />
            <Password name="password" />
            <div class="auto-login">
                <span class="msg">{{ msg }}</span>
                &nbsp;
                <!-- <Checkbox v-model="autoLogin" size="large">自动登录</Checkbox> -->
                <a @click="forgetPsw">忘记密码</a>
            </div>
            <Submit />
        </Login>
    </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue';
import { XhrFetch } from '@ajaxjs/util'; 

export default defineComponent({
    data() {
        return {
            msg: "",
            autoLogin: false,
        };
    },
    methods: {
        hide() {
            this.$emit("hide");
        },
        handleSubmit(valid: boolean, { username, password }: { username: string; password: string }): void {
            if (valid) {
                const data = {  username, password, appId: 'lKi9p9FyicBd6eA'};

                XhrFetch.postForm(`${window.config.iamApi}/user/login`, data, (resp: ApiResponseResult) => {
                    if (resp.status) {
                        XhrFetch.get(`${window.config.iamApi}/user/info`, (resp: ApiResponseResult) => {
                            console.log(resp);
                            this.msg = '登录成功';
                            localStorage.setItem('isLoggedIn', 'true');
                            localStorage.setItem('userInfo', JSON.stringify(resp.data))

                            setTimeout(() => {
                                this.$router.push('/?login_ok=1');
                            }, 2000);
                        });
                    } else
                        this.msg = resp.message || '登录失败';

                });
            }
        },
        forgetPsw(): void {
            this.msg = "请联系管理员重置密码";
            // this.$Modal.info({ title: '抱歉', content: '请联系管理员重置密码' });
        }
    },
});
</script>

<style lang="less" scoped>
.mask {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    z-index: 9999;
    backdrop-filter: blur(2px) opacity(.7);
    background: rgba(0, 0, 0, 0.7);
}

.login {
    width: 400px;
    margin: 0 auto;
    margin-top: 10%;
    background-color: white;
    padding: 3%;
    border-radius: 5px;
    box-shadow: 2px 4px 5px rgba(0, 0, 0, 0.4);

    h1 {
        font-size: 20px;
        letter-spacing: 2px;
        margin-bottom: 20px;
    }

    .auto-login {
        margin-bottom: 14px;
        text-align: left;

        span {
            color: red;
        }
    }

    a {
        float: right;
    }
}
</style>