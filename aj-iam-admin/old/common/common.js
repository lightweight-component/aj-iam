/**
 * 原生 JS 框架
 */
jsp = {};



function tenantFilter() {
	var url = location.search;
	var tenantId = localStorage.getItem("tenantId");

	if (tenantId) {
		if (url.indexOf('tenantId') != -1) { // 已有，看能否匹配
			var nowTenantId = url.match(/tenantId=(\d+)/)[1];

			if (tenantId != nowTenantId) {
				url = url.replace(/tenantId=\d+/, 'tenantId=' + tenantId);
				location.assign(url);
			}
		} else {// 没有，加上
			url += url.indexOf('?') != -1 ? '&tenantId=' + tenantId : ('?tenantId=' + tenantId);
			location.assign(url);
		}
	} else {
		if (url.indexOf('tenantId') != -1) { // 取消过滤
			url = url.replace(/tenantId=\d+/, '');
			location.assign(url);
		}
	}
}

; (() => {
	function request(method, url, params, cb, cfg) {
		var xhr = new XMLHttpRequest();
		xhr.open(method, url);
		xhr.onreadystatechange = function() {
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
					}
				} catch (e) {
					alert('AJAX 错误:\n' + e + '\nThe url is:' + cb.url); // 提示用户 异常
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

		cfg.header = { "Content-Type": "application/x-www-form-urlencoded" };
		request(method, url, params, cb, cfg);
	}

	function json(method, url, params, cb, cfg) {
		if (typeof params != 'string' && !(params instanceof FormData))
			params = JSON.stringify(params);

		if (!cfg)
			cfg = {};

		cfg.header = { "Content-Type": "application/json" };
		request(method, url, params, cb, cfg);
	}

	jsp.xhr = {
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
			formData.forEach(function(value, key) {
				json[key] = value;
			});

			return json;
		}
	};

})();