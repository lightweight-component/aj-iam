<template>
    <div class="user-center">
        <div class="left">
            <div class="avatar">
                <div class="imgHolder userAvatar">
                    <img :src="avatarUrl || defaultAvatar" />
                </div>

                <h3 class="userName">{{ userLoginId }}</h3>
            </div>
            <menu>
                <ul>
                    <li :class="{ actived: showing == 'main' }"><a href="javascript:void(0);"
                            @click="showing = 'main'">概 览</a></li>
                    <li :class="{ actived: showing == 'account' }" @click="showing = 'account'">帐号管理</li>
                    <li :class="{ actived: showing == 'oauth' }" @click="showing = 'oauth'">第三方登录</li>
                    <li :class="{ actived: showing == 'loginLog' }" @click="showing = 'loginLog'">登录历史</li>
                    <li><a href="javascript:void(0);" @click="logout">退出登录</a></li>
                </ul>
            </menu>
        </div>
        <div class="right">
            <Main v-if="showing == 'main' && USER" :user="USER" @saved="loadCurrentUser" />
            <Account v-if="showing == 'account'" @saved="loadCurrentUser" />
            <Oauth v-if="showing == 'oauth'" />
            <LoginLog v-if="showing == 'loginLog'" />
        </div>
        <div class="copyright">Powered by AJ-IAM.</div>
    </div>
</template>

<script lang="ts">
import Main from './Main.vue';
import Account from './Account.vue';
import Oauth from './Oauth.vue';
import LoginLog from './LoginLog.vue';
import { XhrFetch } from '../../../common/http';

export default {
    components: { Main, Account, Oauth, LoginLog },
    data() {
        return {
            showing: 'main',
            isShow: false,
            loginState: false,
            userLoginId: '',
            avatarUrl: '',
            USER: null as ApiResponseResultData | null,
            defaultAvatar: import.meta.env.BASE_URL + 'images/empty-avatar.png',
        }
    },

    mounted() {
        this.loadCurrentUser();
    },
    methods: {
        loadCurrentUser(): void {
            XhrFetch.get(`${window.config.iamApi}/curr_user`, result => {
                if (!result.status || !result.data) {
                    this.$Message.error(result.message || '获取用户信息失败');
                    return;
                }

                this.USER = result.data;
                this.userLoginId = result.data.loginId || '';
                this.avatarUrl = result.data.avatarBlob ? 'data:image/;base64,' + result.data.avatarBlob : '';
            });
        },
        logout(): void {
            this.$Modal.confirm({
                title: '确定退出吗？',
                content: '登出当前账号',
                loading: true, // 显示加载状态，防止用户重复点击
                onOk: () => {
                    XhrFetch.get(`${window.config.iamApi}/user_login/logout`, (resp: ApiResponseResult) => {
                        this.$Modal.remove();
                        if (resp.status && resp.data !== false) {
                            localStorage.removeItem('userInfo');
                            localStorage.removeItem('isLoggedIn');
                            this.$Message.success('登出成功！');

                            setTimeout(() => {
                                this.$router.push('/login');
                            }, 2000);
                        } else
                            this.$Message.error(resp.message || '登出失败');

                    });
                }
            });
        }
    }
};
</script>

<style lang="less">
@background_color_1: #f5f5f5;
@background_color_2: white;
@background_color_3: #fefefe;

.user-center {
    min-height: 700px;
    clear: both;
    margin: auto;
    margin-top: 30px;
    height: 82%;
    max-width: 1200px;

    h4 {
        padding: 0 0 1% 3%;
        font-weight: bold;
        letter-spacing: 3px;
        font-size: 1.1rem;
    }

    .aj-hr {
        width: 95%;
    }

    .left {
        border-radius: 20px 0 0 20px;
        background-color: @background_color_1;
        overflow: hidden;
        width: 22%;
        height: 500px;
        float: left;

        menu {
            width: 100%;
            margin: 0;
            padding: 0;
            margin-top: 5%;
            padding-bottom: 10%;

            a {
                text-decoration: none;
            }

            li {
                border-top: 1px solid lightgray;
                list-style-type: none;
                padding: 5%;
                text-align: center;
                font-size: .95rem;
                cursor: pointer;

                &.actived {
                    background-color: @background_color_2;
                    font-weight: bold;
                }
            }
        }
    }

    .right {
        background-color: @background_color_3;
        width: 78%;
        float: left;
        border-radius: 0 10px 10px 10px;
        display: flex;
        flex-direction: column;
        border: 1px solid #e6e3e3;
        box-sizing: border-box;
        min-height: 600px;
        padding: 4%;

        h3 {
            width: 80%;
            margin: 0% auto;
            margin-bottom: 4%;
            text-align: center;
            font-weight: 700;
            letter-spacing: 5px;
            font-size: 1.4rem;
        }

        iframe {
            height: 100%;
            width: 100%;
            border: 0;
            min-height: 600px;
        }

        .achor {
            text-align: right;
            padding-right: 5%;
            font-size: .9rem;
            margin-bottom: 3%;
        }
    }


    .avatar {
        margin: 10% auto;

        div {
            margin: 5% auto;
        }

        div.imgHolder {
            width: 100px;
            height: 100px;

            img {
                border-radius: 50%;
                width: 100px;
                height: 100px;
            }
        }

        h3 {
            text-align: center;
        }
    }

    .user-center-main {
        table {
            width: 100%;
            font-size: .95rem;

            td {
                vertical-align: middle;

                input,
                textarea {
                    border: none;
                    background-color: transparent;
                    outline: none;
                }
            }
        }
    }

    .user-center-profile {
        font-size: .95rem;

        .loginId {
            padding: 2% 5%;
        }

        table {
            td {
                vertical-align: middle;

                .aj-file-upload {
                    width: 130px;
                    margin: 0 auto;

                    .image-preview img {
                        max-width: 100px;
                    }
                }

            }
        }

        /* 选中表格中每一行的奇数列（第1、3、5...列） */
        table tr td:nth-child(odd),
        table tr th:nth-child(odd) {
            color: gray;
        }

        .button-1 {
            display: block;
            margin: 2% auto;
        }
    }

    .user-center-account {
        min-height: 600px;
        clear: both;
        overflow: hidden;

        .safe {
            margin: 0 5%;

            li {
                border-bottom: 1px solid lightgray;
                padding: 4% 0;
                list-style: none;

                div {
                    display: inline-block;
                    min-width: 25%;
                    box-sizing: border-box;

                    &.ok {
                        color: green;

                        &::before {
                            content: "\2714";
                            border: 2px solid;
                            text-align: center;
                            padding: 3px 3px 0px 2px;
                            border-radius: 50%;
                            margin-right: 10px;
                            font-size: 12px;
                        }
                    }

                    &.fail {
                        color: red;

                        &::before {
                            content: "\2716";
                            border: 2px solid;
                            text-align: center;
                            padding: 3px 3px 0px 2px;
                            border-radius: 50%;
                            margin-right: 10px;
                            font-size: 12px;
                        }
                    }

                }

                a {
                    float: right;
                    font-weight: bold;
                }
            }
        }


        >.center {
            margin: 5% auto;
            height: 82%;

            .left {
                border-radius: 20px 0 0 20px;
                background-color: #f5f5f5;
                ;
                overflow: hidden;
                width: 22%;
                height: 100%;
                float: left;

                menu {
                    width: 100%;
                    margin: 0;
                    padding: 0;
                    margin-top: 5%;
                    padding-bottom: 10%;

                    li {
                        border-top: 1px solid lightgray;
                        list-style-type: none;
                        padding: 5%;
                        text-align: center;
                        font-size: .9rem;
                    }

                    li.selected {
                        background-color: white;
                    }
                }
            }

            .right {
                background-color: #fefefe;
                width: 78%;
                float: left;
                border-radius: 0 10px 10px 10px;
                display: flex;
                flex-direction: column;
                border: 1px solid #e6e3e3;
                box-sizing: border-box;
                min-height: 600px;

                >h3 {
                    width: 80%;
                    margin: 0% auto;
                    text-align: center;
                    font-weight: 700;
                    letter-spacing: 5px;
                    font-size: 1.8rem;
                }

                iframe {
                    height: 100%;
                    width: 100%;
                    border: 0;
                    min-height: 600px;
                }

                .achor {
                    text-align: right;
                    padding-right: 5%;
                    font-size: .9rem;
                    margin-bottom: 3%;
                }
            }
        }
    }

    .copyright {
        text-align: center;
        color: gray;
        font-size: .9rem;
        padding-top: 2%;
        clear: both;
    }
}

hr {
    border: none;
    border-top: 1px solid #e3e3e3;
}

.aj-form-table {
    width: 95%;
    border-collapse: collapse;
    border-spacing: 0;

    th {
        background-color: #efefef;
        letter-spacing: 3px;
    }

    td,
    th {
        line-height: 160%;
        height: 120%;
        padding: 10px 15px;
    }

    tr {
        transition: background-color 400ms ease-out;

        &:nth-child(odd) {
            background: #f5f5f5;
            box-shadow: 0 1px 0 rgba(255, 255, 255, .8) inset;
        }

        &:hover {
            background-color: #fbf8e9;
        }
    }
}
</style>
