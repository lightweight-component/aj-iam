type ResponseCallback = (result: ApiResponseResult) => void;
type RequestHeaders = Record<string, string | number | boolean>;

let onUnauthorized: (() => void) | undefined;

async function request(
    url: string,
    method: string,
    callback: ResponseCallback,
    body?: BodyInit,
    headerValues: RequestHeaders = {},
): Promise<void> {
    const headers = new Headers();
    for (const [name, value] of Object.entries(headerValues))
        headers.set(name, String(value));

    let response: Response;
    try {
        response = await fetch(url, { method, headers, body, credentials: 'include' });
    } catch {
        callback({ status: 0, message: '无法连接服务器', data: {}, traceId: '' });
        return;
    }

    if (response.status === 401 && onUnauthorized) {
        onUnauthorized();
        return;
    }

    const result = await response.json().catch(() => null);
    if (!response.ok || result == null || typeof result !== 'object') {
        callback({
            status: 0,
            message: result?.message || (response.ok ? '接口未返回有效数据' : `请求失败（HTTP ${response.status}）`),
            data: {},
            traceId: '',
        });
        return;
    }

    callback(result);
}

function formBody(data: Record<string, unknown>): URLSearchParams {
    const params = new URLSearchParams();
    for (const [name, value] of Object.entries(data)) {
        if (value != null && typeof value !== 'function')
            params.append(name, String(value));
    }
    return params;
}

export const XhrFetch = {
    setOn401(callback: () => void) {
        onUnauthorized = callback;
    },
    get(url: string, callback: ResponseCallback, headers?: RequestHeaders) {
        return request(url, 'GET', callback, undefined, headers);
    },
    del(url: string, callback: ResponseCallback, headers?: RequestHeaders) {
        return request(url, 'DELETE', callback, undefined, headers);
    },
    post(url: string, data: unknown, callback: ResponseCallback, headers?: RequestHeaders) {
        return request(url, 'POST', callback, JSON.stringify(data), { 'Content-Type': 'application/json', ...headers });
    },
    put(url: string, data: unknown, callback: ResponseCallback, headers?: RequestHeaders) {
        return request(url, 'PUT', callback, JSON.stringify(data), { 'Content-Type': 'application/json', ...headers });
    },
    postForm(url: string, data: Record<string, unknown>, callback: ResponseCallback, headers?: RequestHeaders) {
        return request(url, 'POST', callback, formBody(data), headers);
    },
};
