export function startLogin(): void {
    const params = new URLSearchParams({ web_url: import.meta.env.DEV ? '/' : '/admin/' });
    location.assign('/iam_api/client/to_login?' + params);
}

export function promptLogin(): void {
    if (confirm('你未登录，是否跳转到登录窗口？'))
        startLogin();
}
