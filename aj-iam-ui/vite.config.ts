import vue from '@vitejs/plugin-vue';
import { defineConfig } from 'vite';

export default defineConfig({
  server: {
    allowedHosts: ['local.iam.com']
  },
  plugins: [vue()],
  base: './', // <-- 关键：设置为相对路径
});