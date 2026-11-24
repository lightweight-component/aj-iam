import { createApp } from 'vue';
import ViewUIPlus from 'view-ui-plus';
import App from './pages/App.vue';
import route from './common/route';
import 'view-ui-plus/dist/styles/viewuiplus.css';
import '@ajax-js/configurable-widget/style.css';
import './style/style.css';
import { XhrFetch } from './common/http';
import { promptLogin } from './common/utils';

XhrFetch.setOn401(promptLogin);

createApp(App).use(ViewUIPlus).use(route).mount('#app');
