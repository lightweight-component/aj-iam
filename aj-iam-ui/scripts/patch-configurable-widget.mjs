import { readFileSync, writeFileSync } from 'node:fs';

const packageFile = new URL('../node_modules/@ajax-js/configurable-widget/package.json', import.meta.url);
const { version } = JSON.parse(readFileSync(packageFile, 'utf8'));

if (version !== '1.0.1')
    throw new Error(`Review the status compatibility patch for @ajax-js/configurable-widget@${version}.`);

const entry = new URL('./target/npm/index.js', packageFile);
const source = readFileSync(entry, 'utf8');
const guard = 'if (!l || typeof l != "object" || typeof l.status != "boolean") throw Error("接口响应格式错误：缺少 boolean 类型的 status 字段");';
const compatibility = 'if (l && typeof l == "object" && (l.status === 0 || l.status === 1)) l.status = l.status === 1;';

if (!source.includes(guard))
    throw new Error('Cannot locate the configurable-widget status validation.');

if (!source.includes(compatibility))
    writeFileSync(entry, source.replace(guard, `${compatibility}\n\t${guard}`), 'utf8');
