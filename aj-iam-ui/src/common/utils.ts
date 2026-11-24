const defaultFormat = 'yyyy-MM-dd hh:mm';
const omittedFields = ['createDate', 'updateDate', 'creatorId', 'updaterId', 'creator', 'updater', 'extend'];

export function formatDate(this: Date, format = defaultFormat): string {
    if (Number.isNaN(this.getTime()))
        return '—';

    const fields: Record<string, number> = {
        y: this.getFullYear(),
        M: this.getMonth() + 1,
        d: this.getDate(),
        h: this.getHours(),
        m: this.getMinutes(),
        s: this.getSeconds(),
        q: Math.floor(this.getMonth() / 3) + 1,
        S: this.getMilliseconds(),
    };

    return format.replace(/y{2,4}|M{1,2}|d{1,2}|h{1,2}|m{1,2}|s{1,2}|q|S/g, token => {
        const value = String(fields[token.charAt(0)]);
        return token.startsWith('y') ? value.slice(-token.length) : value.padStart(token.length, '0');
    });
}

export function dateFormat(value: string | number, format = defaultFormat): string {
    return formatDate.call(new Date(value), format);
}

export function copyWriteFields(entity: Record<string, unknown>): Record<string, unknown> {
    const data = { ...entity };
    for (const field of omittedFields)
        delete data[field];

    return data;
}

export function startLogin(): void {
    const params = new URLSearchParams({ web_url: import.meta.env.DEV ? '/' : '/admin/' });
    location.assign('/iam_api/client/to_login?' + params);
}

export function promptLogin(): void {
    if (confirm('你未登录，是否跳转到登录窗口？'))
        startLogin();
}
