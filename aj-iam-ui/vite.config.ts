import vue from '@vitejs/plugin-vue';
import { defineConfig } from 'vite';

const iamProxy = {
  target: 'https://wyndme.cn',
  changeOrigin: true,
  autoRewrite: true,
  protocolRewrite: 'http',
  cookieDomainRewrite: '',
};

export default defineConfig({
  server: {
    allowedHosts: ['local.iam.com'],
    proxy: {
      '^/iam_api(?=/|\\?|$)': iamProxy,
      '^/iam_login(?=/|\\?|$)': iamProxy,
      '^/iam_admin_api(?=/|\\?|$)': {
        ...iamProxy,
        rewrite: (path) => path.replace(/^\/iam_admin_api/, '/iam_api'),
      },
    },
  },
  plugins: [vue()],
  optimizeDeps: {
    // 本地 UI 包直接提供 Vue 源码，由 Vue 插件编译。
    exclude: ['@ajaxjs/ui'],
    // 显式预构建源码组件内部使用的 UMD 依赖，转换为浏览器 ESM。
    include: ['vuedraggable'],
  },
  base: './', // <-- 关键：设置为相对路径
});
