if (!window.aj)
    aj = {};

function request(method, url, params, cb, cfg) {
    var xhr = new XMLHttpRequest();
    xhr.open(method, url);
    xhr.onreadystatechange = function () {
        if (this.readyState === 4) {
            var responseText = this.responseText.trim();
            if (!responseText) {
                alert('服务端返回空的字符串!');
                return;
            }

            var data = null;
            try {
                var parseContentType = cfg && cfg.parseContentType;
                switch (parseContentType) {
                    case 'text':
                        data = responseText;
                        break;
                    case 'xml':
                        data = this.responseXML;
                        break;
                    case 'json':
                    default:
                        data = JSON.parse(responseText);
                        businessCheck(data);
                }
            } catch (e) {
                console.log(e);
                // alert('AJAX 错误:\n' + e + '\nThe url is:' + cb.url); // 提示用户 异常
            }

            cb && cb(data, this);
            /*				if (this.status === 200) {
                            } else if (this.status === 500) {
                            }*/
        }
    }

    if (cfg && cfg.header) {
        for (var i in cfg.header) {
            xhr.setRequestHeader(i, cfg.header[i]);
        }
    }

    xhr.send(params || null);
}

function json2fromParams(param) {
    let result = "";

    for (let name in param) {
        if (typeof param[name] != "function")
            result += "&" + name + "=" + encodeURIComponent(param[name]);
    }

    return result.substring(1);
}

function form(method, url, params, cb, cfg) {
    if (typeof params != 'string' && !(params instanceof FormData))
        params = json2fromParams(params);

    if (!cfg)
        cfg = {};

    if (!cfg.header)
        cfg.header = { "Content-Type": "application/x-www-form-urlencoded" };
    else
        Object.assign(cfg.header, { "Content-Type": "application/x-www-form-urlencoded" });

    request(method, url, params, cb, cfg);
}

aj.xhr = {
    get(url, cb, cfg) {
        request('GET', url, null, cb, cfg);
    },
    postForm(url, params, cb, cfg) {
        form("POST", url, params, cb, cfg);
    },
    postJson(url, params, cb, cfg) {
        json("POST", url, params, cb, cfg);
    },
    putForm(url, params, cb, cfg) {
        form("PUT", url, params, cb, cfg);
    },
    putJson(url, params, cb, cfg) {
        json("PUT", url, params, cb, cfg);
    },
    del(url, cb, cfg) {
        request('DELETE', url, null, cb, cfg);
    },
    formData(form) {
        if (!window.FormData)
            throw 'The version of your browser is too old, please upgrade it.';

        if (typeof form == 'string')
            form = document.querySelector(form);
        var json = {};

        var formData = new FormData(form);
        formData.forEach(function (value, key) {
            json[key] = value;
        });

        return json;
    }
};

// 业务处理
function businessCheck(json) {
    if (json && !json.status)
        alert(json.message || '操作失败');
}

function convertKeysToUnderscore(obj) {
    const convertedObj = {};

    for (const key in obj) {
        if (obj.hasOwnProperty(key)) {
            const convertedKey = key.replace(/([A-Z])/g, "_$1").toLowerCase();
            convertedObj[convertedKey] = obj[key];
        }
    }

    return convertedObj;
}


/**
* 日期格式化
* @param 	{String} format
* @return	{String}
*/
Date.prototype.format = function (format) {
    var $1, o = {
        "M+": this.getMonth() + 1,		// 月份，从0开始算
        "d+": this.getDate(),   		// 日期
        "h+": this.getHours(),   		// 小时
        "m+": this.getMinutes(), 		// 分钟
        "s+": this.getSeconds(), 		// 秒钟
        // 季度 quarter
        "q+": Math.floor((this.getMonth() + 3) / 3),
        "S": this.getMilliseconds()	// 千秒
    };
    var key, value;

    if (/(y+)/.test(format))
        $1 = RegExp.$1, format = format.replace($1, String(this.getFullYear()).substr(4 - $1));

    for (key in o) { // 如果没有指定该参数，则子字符串将延续到 stringvar 的最后。
        if (new RegExp("(" + key + ")").test(format)) {
            $1 = RegExp.$1,
                value = String(o[key]),
                value = $1.length == 1 ? value : ("00" + value).substr(value.length),
                format = format.replace($1, value);
        }
    }

    return format;
}