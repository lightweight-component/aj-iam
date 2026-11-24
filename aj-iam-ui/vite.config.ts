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
  base: './', // <-- 关键：设置为相对路径
});
