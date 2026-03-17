import { createApp } from 'vue';
import ViewUIPlus from 'view-ui-plus';
import App from './pages/App.vue';
import route from './common/route';
import 'view-ui-plus/dist/styles/viewuiplus.css';
// import './style/style.css';
import { XhrFetch } from '@ajaxjs/util';

XhrFetch.setOn401(() => {
    if (confirm('你未登录，是否跳转到登录窗口？'))
        location.assign('#/login');
});

alert(XhrFetch.setOn401)

createApp(App).use(ViewUIPlus).use(route).mount('#app');