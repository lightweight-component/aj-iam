import { defineComponent as j, resolveComponent as h, createElementBlock as w, openBlock as g, createElementVNode as n, createVNode as o, createBlock as b, createCommentVNode as $, withCtx as r, createTextVNode as l, withDirectives as D, vModelRadio as M, toDisplayString as C, Fragment as U, renderList as A, vModelSelect as G } from "vue";
const R = "auth_access_token", W = "auth_refresh_token", Q = async (t, e) => {
  console.log("Tokens saved:", { accessToken: t, refreshToken: e }), localStorage.setItem(R, t), localStorage.setItem(W, e), await localStorage.setItem(R, t), localStorage.setItem(R, t), console.log("getItem", localStorage.getItem(R));
}, X = async () => await localStorage.getItem(R), Y = async () => "207c3ff6-c55a-4dfd-b926-6682470d5421", Z = async () => {
  localStorage.removeItem(R), await localStorage.removeItem(W);
}, _ = "r3fgD3x43ft5H", x = "zKvmMA4KmJ2CIijl9ubqbXpHm1", ee = "http://127.0.0.1:8082/iam_api/oidc/refresh_token";
async function te() {
  const t = await Y();
  if (!t)
    throw new Error("No refresh token available");
  const e = {
    grant_type: "refresh_token",
    refresh_token: t
  }, s = await fetch(ee, {
    method: "POST",
    headers: {
      "Content-Type": "application/x-www-form-urlencoded",
      Authorization: se(_, x)
    },
    body: ie(e)
  });
  if (!s.ok)
    throw new Error("Token refresh failed");
  const i = await s.json(), { status: f, message: c, data: p } = i;
  if (f === 1)
    return await Q(p.id_token, p.refresh_token || t), { accessToken: p.id_token, refreshToken: p.refresh_token || t };
  throw new Error(`Token refresh failed. Reason: ${c}`);
}
function se(t, e) {
  if (t.trim() === "" || e.trim() === "")
    throw new Error("Missing the arguments: clientId/clientSecret");
  const s = `${t}:${e}`;
  let i;
  return typeof window < "u" && window.btoa ? i = btoa(unescape(encodeURIComponent(s))) : i = Buffer.from(s).toString("base64"), `Basic ${i}`;
}
function ie(t) {
  const e = [];
  for (const [s, i] of Object.entries(t))
    i != null && typeof i != "function" && e.push(`${encodeURIComponent(s)}=${encodeURIComponent(String(i))}`);
  return e.join("&");
}
let V = !1, N = [];
const B = (t, e = null) => {
  N.forEach((s) => {
    t ? s.reject(t) : s.resolve(e);
  }), N = [];
};
async function oe(t) {
  if (V)
    return new Promise((e, s) => {
      N.push({ resolve: e, reject: s });
    }).then(async () => await t());
  V = !0;
  try {
    const e = await te();
    return B(null, e.accessToken), await t();
  } catch (e) {
    throw B(e, null), Z(), e;
  } finally {
    V = !1;
  }
}
let F = !1;
function T(t, e, s, i, f) {
  const c = () => new Promise((p, d) => {
    let k = { ...f };
    X().then((P) => {
      P && (k.Authorization = `Bearer ${P}`), e = e.toUpperCase();
      let y = s;
      s && (e === "POST" || e === "PUT") && (k["Content-Type"] === "application/json" ? y = JSON.stringify(s) : k["Content-Type"] === "application/x-www-form-urlencoded" && (y = ne(s))), fetch(t, {
        method: e,
        headers: k,
        body: y,
        credentials: "include"
      }).then((a) => {
        if (a.status === 404)
          throw new Error("Not found 404: " + t);
        if (a.status === 500)
          throw new Error("Server error: " + t);
        if (a.status === 401 && !F)
          return F = !0, oe(() => c()).finally(() => {
            F = !1;
          });
        if (!a.ok)
          throw new Error(`Unexpected status: ${a.status}`);
        return a.json();
      }).then((a) => {
        i && i(a), p();
      }).catch((a) => {
        a.message.includes("401") ? console.error("Authentication failed, please login again.") : console.error("Network error when fetching from: " + t, a), d(a);
      });
    });
  });
  c().catch(() => {
  });
}
function ne(t) {
  const e = [];
  for (const [s, i] of Object.entries(t))
    i != null && typeof i != "function" && e.push(`${encodeURIComponent(s)}=${encodeURIComponent(String(i))}`);
  return e.join("&");
}
function L(t, e, s) {
  T(t, "GET", null, e, s);
}
function z(t, e, s, i) {
  T(t, "POST", e, s, { "Content-Type": "application/json", ...i });
}
function re(t, e, s, i) {
  T(t, "PUT", e, s, { "Content-Type": "application/json", ...i });
}
function le(t, e, s) {
  T(t, "DELETE", null, e, s);
}
function E(t, e, s, i, f) {
  let c = {};
  if (f)
    for (const d in f)
      c[d] = f[d];
  e = e.toUpperCase();
  let p = s;
  s && (e === "POST" || e === "PUT") && (c["Content-Type"] == "application/json" ? p = JSON.stringify(s) : c["Content-Type"] == "application/x-www-form-urlencoded" && (p = ae(s))), fetch(t, {
    method: e,
    headers: c,
    body: p,
    credentials: "include"
  }).then((d) => {
    if (d.status === 404)
      throw new Error("Not found 404: " + t);
    if (d.status === 500)
      throw new Error("Server error: " + t);
    if (!d.ok)
      throw new Error(`Unexpected status: ${d.status}`);
    return d.json();
  }).then((d) => {
    i && i(d);
  }).catch((d) => {
    console.error("Network error when fetching from: " + t, d);
  });
}
function ae(t) {
  const e = [];
  for (const [s, i] of Object.entries(t))
    i != null && typeof i != "function" && e.push(`${encodeURIComponent(s)}=${encodeURIComponent(String(i))}`);
  return e.join("&");
}
function q(t, e, s) {
  E(t, "GET", null, e, s);
}
function ue(t, e, s, i) {
  E(t, "POST", e, s, { "Content-Type": "application/json", ...i });
}
function de(t, e, s, i) {
  E(t, "PUT", e, s, { "Content-Type": "application/json", ...i });
}
function H(t, e, s) {
  E(t, "DELETE", null, e, s);
}
function pe(t) {
  var e, s = {
    "M+": this.getMonth() + 1,
    // 月份，从0开始算
    "d+": this.getDate(),
    // 日期
    "h+": this.getHours(),
    // 小时
    "m+": this.getMinutes(),
    // 分钟
    "s+": this.getSeconds(),
    // 秒钟
    // 季度 quarter
    "q+": Math.floor((this.getMonth() + 3) / 3),
    S: this.getMilliseconds()
    // 千秒
  }, i, f;
  /(y+)/.test(t) && (e = RegExp.$1, t = t.replace(e, String(this.getFullYear()).substr(4 - e)));
  for (i in s)
    new RegExp("(" + i + ")").test(t) && (e = RegExp.$1, // @ts-ignore
    f = String(s[i]), f = e.length == 1 ? f : ("00" + f).substr(f.length), t = t.replace(e, f));
  return t;
}
function me(t, e) {
  return pe.call(new Date(t), e);
}
const S = {
  afterDelete(t) {
    return function(e) {
      e.status ? (t && t(e), this.$Message.success("删除成功")) : this.$Message.info("删除失败。" + e.message);
    };
  },
  delInfo(t) {
    H(`${this.API}/${this.list.data[t].id}`, (e) => {
      e.status ? (this.list.data.splice(t, 1), this.list.total--, this.$Message.success("删除成功")) : this.$Message.info("删除失败。" + e.message);
    });
  },
  /**
   * id 列
   */
  id: { title: "#", width: 60, key: "id", align: "center" },
  /**
   * 创建日期
   */
  createDate: {
    title: "创建日期",
    /*  key: 'createDate', */
    width: 160,
    align: "center",
    render(t, e) {
      return t("div", me(e.row.createDate, "yyyy-MM-dd hh:mm"));
    }
  },
  /**
   * 分类标签
   */
  tags: { title: "分类标签", minWidth: 100, key: "tagsNames", align: "center", ellipsis: !0 },
  status: {
    title: "状态",
    width: 80,
    render(t, e) {
      let s = "", i = "";
      switch (e.row.stat) {
        case -1:
          s = "草稿", i = "gray";
          break;
        case 2:
          i = "red", s = "禁用";
          break;
        case 1:
          i = "red", s = "已删除";
          break;
        case null:
        case 0:
        default:
          i = "green", s = "启用";
      }
      return t("div", {
        style: {
          color: i
        }
      }, s);
    }
  },
  getPageList(t, e, s) {
    return (i) => {
      i.status ? (e.total = i.data.totalCount, e.data = i.data.list, s && s()) : t.$Message.warning(i.message || "获取数据失败");
    };
  },
  copyBeanClean(t) {
    const e = JSON.parse(JSON.stringify(t));
    return delete e.createDate, delete e.updateDate, delete e.updateDate, delete e.creatorId, delete e.updaterId, delete e.creator, delete e.updater, delete e.extend, e;
  }
}, fe = j({
  props: {
    isPickup: {
      type: Boolean,
      default: !1
    },
    onPickup: {
      type: Function
    },
    simpleApi: {
      type: String,
      required: !0
    }
  },
  data() {
    return {
      isCreate: !0,
      isShowEditWin: !1,
      permissionData: {},
      columnsDef: [
        S.id,
        {
          title: "权限名称",
          key: "name"
        },
        {
          title: "权限编码",
          key: "code",
          ellipsis: !0
        },
        S.status,
        S.createDate,
        {
          title: "操作",
          slot: "action",
          width: 120
        }
      ],
      listData: [],
      list: {
        total: 0,
        limit: 5,
        current: 1,
        data: []
      },
      ruleValidate: {
        name: [
          { required: !0, message: "该字段非空约束", trigger: "blur" }
        ],
        code: [
          { required: !0, message: "该字段非空约束", trigger: "blur" }
        ]
      }
    };
  },
  mounted() {
    this.getData();
  },
  methods: {
    getData(t) {
      let e = `${this.simpleApi}/permission/page?pageNo=${this.list.current}&limit=${this.list.limit}`;
      this.isPickup && (e += "&q_stat=0"), t && (e += `&${t}`), q(e, S.getPageList(this, this.list));
    },
    onPageNoChange(t) {
      this.list.current = t, this.getData();
    },
    handleChangePageSize(t) {
      this.list.limit = t, this.getData();
    },
    pickup(t) {
    },
    doSearch(t) {
      this.list.current = 1, this.getData(`ql_name=${t}&ql_code=${t}`);
    },
    showCreate() {
      this.permissionData = {}, this.isShowEditWin = !0, this.isCreate = !0;
    },
    doDelete(t) {
      H(`${this.simpleApi}/permission/${t}`, (e) => {
        e.status && (this.$Message.success("删除成功"), this.getData());
      });
    },
    edit(t) {
      this.isShowEditWin = !0, this.isCreate = !1, q(`${this.simpleApi}/permission/${t}`, (e) => {
        e.status && (this.permissionData = e.data);
      });
    },
    save() {
      let t = S.copyBeanClean(this.permissionData);
      this.isCreate ? ue(`${this.simpleApi}/permission`, (e) => {
        e.status && (this.$Message.success("创建成功"), this.getData());
      }, t) : de(`${this.simpleApi}/permission/${this.permissionData.id}`, (e) => {
        e.status && (this.$Message.success("修改成功"), this.getData());
      }, t);
    }
  },
  watch: {
    /**
     * 分页
     * 
     * @param v 
     */
    isPickup(t) {
      if (this.getData(), t) {
        for (let e = 0; e < this.columnsDef.length; e++)
          if (this.columnsDef[e].title === "状态") {
            this.columnsDef.splice(e, 1);
            break;
          }
      }
    }
  }
}), J = (t, e) => {
  const s = t.__vccOpts || t;
  for (const [i, f] of e)
    s[i] = f;
  return s;
}, he = { style: { "margin-bottom": "20px" } }, ge = { style: { float: "right" } }, ce = ["onClick"], ke = { key: 1 }, we = ["onClick"];
function ye(t, e, s, i, f, c) {
  const p = h("Button"), d = h("Input"), k = h("Poptip"), P = h("Table"), y = h("Page"), a = h("FormItem"), I = h("Form"), O = h("Modal");
  return g(), w("div", null, [
    n("div", he, [
      n("span", ge, [
        t.isPickup ? $("", !0) : (g(), b(p, {
          key: 0,
          onClick: t.showCreate,
          type: "primary",
          icon: "ios-add",
          style: { "margin-right": "10px" }
        }, {
          default: r(() => [...e[7] || (e[7] = [
            l("增加权限", -1)
          ])]),
          _: 1
        }, 8, ["onClick"])),
        o(p, {
          onClick: e[0] || (e[0] = (m) => t.getData()),
          icon: "ios-refresh"
        }, {
          default: r(() => [...e[8] || (e[8] = [
            l("刷新", -1)
          ])]),
          _: 1
        })
      ]),
      o(d, {
        style: { width: "30%" },
        onOnSearch: t.doSearch,
        search: "",
        "enter-button": "",
        placeholder: "权限的名称或编码皆可搜索"
      }, null, 8, ["onOnSearch"])
    ]),
    o(P, {
      border: "",
      columns: t.columnsDef,
      data: t.list.data,
      style: { "min-height": "250px" }
    }, {
      action: r(({ row: m }) => [
        t.isPickup ? (g(), w("a", {
          key: 0,
          style: { "margin-right": "5px" },
          onClick: (v) => t.onPickup(m)
        }, "选择", 8, ce)) : $("", !0),
        t.isPickup ? $("", !0) : (g(), w("span", ke, [
          o(k, {
            confirm: "",
            title: "确定删除？",
            onOnOk: (v) => t.doDelete(m.id)
          }, {
            default: r(() => [...e[9] || (e[9] = [
              n("a", {
                style: { "margin-right": "5px", color: "red" },
                icon: "ios-trash"
              }, "删除", -1)
            ])]),
            _: 2
          }, 1032, ["onOnOk"]),
          e[10] || (e[10] = l(" | ", -1)),
          n("a", {
            style: { "margin-right": "5px", color: "green" },
            onClick: (v) => t.edit(m.id),
            icon: "ios-edit"
          }, "编辑", 8, we)
        ]))
      ]),
      _: 1
    }, 8, ["columns", "data"]),
    o(y, {
      style: { margin: "20px auto", "text-align": "center" },
      "page-size": t.list.limit,
      total: t.list.total,
      "model-value": t.list.current,
      onOnChange: t.onPageNoChange,
      onOnPageSizeChange: t.handleChangePageSize,
      size: "small",
      "show-total": "",
      "show-elevator": "",
      "show-sizer": ""
    }, null, 8, ["page-size", "total", "model-value", "onOnChange", "onOnPageSizeChange"]),
    o(O, {
      modelValue: t.isShowEditWin,
      "onUpdate:modelValue": e[6] || (e[6] = (m) => t.isShowEditWin = m),
      title: t.isCreate ? "创建权限" : "编辑权限" + t.permissionData.id,
      width: "600",
      onOnOk: t.save
    }, {
      default: r(() => [
        o(I, {
          model: t.permissionData,
          rules: t.ruleValidate,
          "label-width": 100,
          style: { "margin-right": "10%", "margin-left": "3%" }
        }, {
          default: r(() => [
            o(a, {
              label: "权限名称",
              prop: "name"
            }, {
              default: r(() => [
                o(d, {
                  modelValue: t.permissionData.name,
                  "onUpdate:modelValue": e[1] || (e[1] = (m) => t.permissionData.name = m),
                  placeholder: "请输入权限名称……"
                }, null, 8, ["modelValue"])
              ]),
              _: 1
            }),
            o(a, {
              label: "权限编码",
              prop: "code"
            }, {
              default: r(() => [
                o(d, {
                  modelValue: t.permissionData.code,
                  "onUpdate:modelValue": e[2] || (e[2] = (m) => t.permissionData.code = m),
                  placeholder: "请输入权限编码……"
                }, null, 8, ["modelValue"])
              ]),
              _: 1
            }),
            o(a, { label: "权限说明" }, {
              default: r(() => [
                o(d, {
                  type: "textarea",
                  rows: 4,
                  modelValue: t.permissionData.content,
                  "onUpdate:modelValue": e[3] || (e[3] = (m) => t.permissionData.content = m),
                  placeholder: "请输入权限说明……"
                }, null, 8, ["modelValue"])
              ]),
              _: 1
            }),
            o(a, { label: "权限状态" }, {
              default: r(() => [
                n("label", null, [
                  D(n("input", {
                    type: "radio",
                    "onUpdate:modelValue": e[4] || (e[4] = (m) => t.permissionData.stat = m),
                    value: "0"
                  }, null, 512), [
                    [M, t.permissionData.stat]
                  ]),
                  e[11] || (e[11] = l(" 启用", -1))
                ]),
                e[13] || (e[13] = l("   ", -1)),
                n("label", null, [
                  D(n("input", {
                    type: "radio",
                    "onUpdate:modelValue": e[5] || (e[5] = (m) => t.permissionData.stat = m),
                    value: "2"
                  }, null, 512), [
                    [M, t.permissionData.stat]
                  ]),
                  e[12] || (e[12] = l(" 禁用", -1))
                ])
              ]),
              _: 1
            }),
            t.isCreate ? $("", !0) : (g(), b(a, {
              key: 0,
              style: { color: "gray" }
            }, {
              default: r(() => [
                l(" 创建于 " + C(t.permissionData.createDate) + " 修改于 " + C(t.permissionData.updateDate), 1)
              ]),
              _: 1
            }))
          ]),
          _: 1
        }, 8, ["model", "rules"])
      ]),
      _: 1
    }, 8, ["modelValue", "title", "onOnOk"])
  ]);
}
const Ce = /* @__PURE__ */ J(fe, [["render", ye]]), $e = j({
  components: { PermissionMgr: Ce },
  data() {
    return {
      simpleApi: "http://localhost:8082/iam_api/common_api",
      permissionApi: "http://localhost:8082/iam_api/permission",
      isShisShowRoleEditForm: !1,
      isShowPermissionMgr: !1,
      isPermissionMgrPickup: !0,
      currentRole: {
        name: ""
      },
      permission: {
        inheritPermissionList: [],
        permissionList: []
      },
      selectedPermissions: [],
      contextData: null,
      roleTreeData: [],
      roleForm: {
        isTop: !1,
        isCreate: !1
      }
    };
  },
  mounted() {
    this.refreshRoleList();
  },
  methods: {
    handleContextMenu(t) {
      this.contextData = t;
    },
    editRole() {
      this.roleForm.isCreate = !1, this.isShisShowRoleEditForm = !0, this.roleForm.isTop = this.contextData.parentId == -1, L(`${this.simpleApi}/role/${this.contextData.id}`, (t) => {
        t.status ? this.currentRole = t.data : this.$Message.warning(t.message || "获取数据失败");
      });
    },
    createTopRoleNode() {
      this.roleForm.isTop = !0, this.roleForm.isCreate = !0, this.currentRole = {}, this.contextData = { id: -1 }, this.isShisShowRoleEditForm = !0;
    },
    delRole() {
      let t = this.contextData.title;
      this.$Modal.confirm({
        title: "删除角色",
        content: `<p>确定删除 ${t} 这个节点吗？<br />注意：该节点下<b>所有的子节点</b>也会一并被删除！</p>`,
        onOk: () => {
          le(`${this.permissionApi}/role/${this.contextData.id}`, (e) => {
            e.status ? (this.$Message.success("删除成功"), this.refreshRoleList()) : this.$Message.warning(e.message || "获取数据失败");
          });
        }
      });
    },
    addSubNode() {
      this.roleForm.isTop = !1, this.roleForm.isCreate = !0, this.currentRole = {}, this.isShisShowRoleEditForm = !0;
    },
    refreshRoleList() {
      L(`${this.permissionApi}/role_tree`, (t) => {
        t.status ? this.roleTreeData = t.data : this.$Message.warning(t.message || "获取数据失败");
      });
    },
    saveRole() {
      let t = S.copyBeanClean(this.currentRole);
      t.parentId = this.contextData.id, this.roleForm.isCreate ? z(`${this.simpleApi}/role`, (e) => {
        e.status && (this.$Message.success("创建成功"), this.refreshRoleList());
      }, t) : re(`${this.simpleApi}/role/${t.id}`, (e) => {
        e.status && (this.$Message.success("修改成功"), this.refreshRoleList());
      }, t);
    },
    onTreeNodeClk(t, e) {
      this.currentRole = { name: e.title, id: e.id };
    },
    //--------------------------- permission -----------------------
    addPermission() {
      this.showPermissionMgr(!0);
    },
    removePermission() {
      for (const t of this.selectedPermissions) {
        const e = this.permission.permissionList.findIndex((s) => s.id === t);
        e !== -1 && this.permission.permissionList.splice(e, 1);
      }
    },
    clearPermission() {
      this.permission.permissionList = [];
    },
    savePermission() {
      let t = [];
      this.permission.permissionList.forEach((s) => t.push(s.id));
      let e = {
        roleId: this.currentRole.id,
        permissionIds: t.join(",")
      };
      z(`${this.permissionApi}/add_permissions_to_role`, (s) => {
        s.status && this.$Message.success("保存权限成功");
      }, e);
    },
    showPermissionMgr(t) {
      this.isShowPermissionMgr = !0, this.isPermissionMgrPickup = t;
    },
    pickupPermission(t) {
      let e = this.permission.permissionList;
      for (let s = 0; s < e.length; s++)
        if (e[s].id == t.id) {
          this.$Message.warning("已经添加了权限" + t.name);
          return;
        }
      this.permission.permissionList.push({
        id: t.id,
        name: t.name
      }), this.$Message.success(`添加权限[${t.name}]成功`);
    },
    handlePermissionList(t) {
      this.permission.inheritPermissionList = [], this.permission.permissionList = [], t.forEach((e) => {
        e.isInherit ? this.permission.inheritPermissionList.push(e) : this.permission.permissionList.push({
          id: e.id,
          name: e.name
        });
      });
    }
  },
  watch: {
    currentRole(t) {
      t && t.id && L(`${this.permissionApi}/permission_list_by_role/${t.id}`, (e) => {
        e.status ? this.handlePermissionList(e.data) : this.$Message.warning(e.message || "获取数据失败");
      });
    }
  }
}), Pe = { class: "main" }, Se = { class: "left" }, Re = { class: "btns" }, De = { class: "tree" }, ve = { class: "right" }, be = { class: "panel" }, Me = { class: "inherited-permission" }, Te = { key: 0 }, Ee = {
  key: 0,
  style: { "font-weight": "normal", "font-size": "14px" }
}, Ie = { class: "permission-list" }, Oe = ["value"], Ve = { class: "permission-bts" };
function Fe(t, e, s, i, f, c) {
  const p = h("Button"), d = h("Icon"), k = h("DropdownItem"), P = h("Tree"), y = h("Input"), a = h("FormItem"), I = h("Checkbox"), O = h("Form"), m = h("Modal"), v = h("PermissionMgr");
  return g(), w("div", null, [
    n("div", Pe, [
      n("div", Se, [
        n("div", null, [
          n("span", Re, [
            o(p, {
              type: "primary",
              icon: "ios-add",
              onClick: t.createTopRoleNode
            }, {
              default: r(() => [...e[9] || (e[9] = [
                l("创建顶级角色", -1)
              ])]),
              _: 1
            }, 8, ["onClick"]),
            o(p, {
              type: "info",
              icon: "ios-refresh",
              onClick: t.refreshRoleList
            }, {
              default: r(() => [...e[10] || (e[10] = [
                l("刷新", -1)
              ])]),
              _: 1
            }, 8, ["onClick"])
          ])
        ]),
        e[14] || (e[14] = n("h2", null, "角色管理", -1)),
        n("div", De, [
          o(P, {
            data: t.roleTreeData,
            onOnSelectChange: t.onTreeNodeClk,
            onOnContextmenu: t.handleContextMenu
          }, {
            contextMenu: r(() => [
              o(k, {
                onClick: t.editRole,
                style: { color: "cornflowerblue" }
              }, {
                default: r(() => [
                  o(d, { type: "ios-create-outline" }),
                  e[11] || (e[11] = l(" 编辑角色 ", -1))
                ]),
                _: 1
              }, 8, ["onClick"]),
              o(k, {
                onClick: t.addSubNode,
                style: { color: "green" }
              }, {
                default: r(() => [
                  o(d, { type: "md-add" }),
                  e[12] || (e[12] = l(" 添加子节点 ", -1))
                ]),
                _: 1
              }, 8, ["onClick"]),
              o(k, {
                onClick: t.delRole,
                style: { color: "#ed4014" }
              }, {
                default: r(() => [
                  o(d, { type: "md-close" }),
                  e[13] || (e[13] = l(" 删除角色 ", -1))
                ]),
                _: 1
              }, 8, ["onClick"])
            ]),
            _: 1
          }, 8, ["data", "onOnSelectChange", "onOnContextmenu"])
        ])
      ]),
      n("div", ve, [
        e[23] || (e[23] = n("p", { class: "note" }, " 你可以维护角色的权限，可以给角色分配权限，也可以给角色分配子角色。一个角色对应多个权限；角色可以继承，拥有父级的所有权限。 ", -1)),
        n("fieldset", be, [
          e[15] || (e[15] = n("legend", null, "通过继承父级的权限：", -1)),
          n("div", Me, [
            (g(!0), w(U, null, A(t.permission.inheritPermissionList, (u, K) => (g(), w("span", {
              key: u.id
            }, [
              l(C(u.roleName) + "-" + C(u.name) + " ", 1),
              K < t.permission.inheritPermissionList.length - 1 ? (g(), w("span", Te, "、")) : $("", !0)
            ]))), 128))
          ])
        ]),
        e[24] || (e[24] = n("br", null, null, -1)),
        e[25] || (e[25] = n("br", null, null, -1)),
        n("div", null, [
          n("h2", null, [
            l(C(t.currentRole ? "角色 " + t.currentRole.name + " 的权限" : "请选择一个角色") + " ", 1),
            t.currentRole.id == null ? (g(), w("span", Ee, "请从左侧选择一个角色以继续操作")) : $("", !0)
          ]),
          n("div", Ie, [
            D(n("select", {
              multiple: "",
              "onUpdate:modelValue": e[0] || (e[0] = (u) => t.selectedPermissions = u)
            }, [
              (g(!0), w(U, null, A(t.permission.permissionList, (u) => (g(), w("option", {
                key: u.name,
                value: u.id
              }, C(u.name), 9, Oe))), 128))
            ], 512), [
              [G, t.selectedPermissions]
            ]),
            n("div", Ve, [
              o(p, {
                disabled: t.currentRole.id == null,
                type: "primary",
                icon: "ios-add",
                onClick: t.addPermission
              }, {
                default: r(() => [...e[16] || (e[16] = [
                  l("添加权限", -1)
                ])]),
                _: 1
              }, 8, ["disabled", "onClick"]),
              o(p, {
                disabled: !t.selectedPermissions.length,
                type: "warning",
                icon: "ios-remove",
                onClick: t.removePermission
              }, {
                default: r(() => [...e[17] || (e[17] = [
                  l("移除权限", -1)
                ])]),
                _: 1
              }, 8, ["disabled", "onClick"]),
              o(p, {
                disabled: t.currentRole.id == null,
                type: "error",
                icon: "ios-close",
                onClick: t.clearPermission
              }, {
                default: r(() => [...e[18] || (e[18] = [
                  l("清空权限", -1)
                ])]),
                _: 1
              }, 8, ["disabled", "onClick"]),
              e[20] || (e[20] = n("br", null, null, -1)),
              o(p, {
                disabled: t.currentRole.id == null,
                type: "success",
                icon: "ios-add-circle-outline",
                onClick: t.savePermission
              }, {
                default: r(() => [...e[19] || (e[19] = [
                  l("   保 存   ", -1)
                ])]),
                _: 1
              }, 8, ["disabled", "onClick"])
            ]),
            n("p", null, [
              e[21] || (e[21] = l("增加、删除权限请到", -1)),
              n("a", {
                onClick: e[1] || (e[1] = (u) => t.showPermissionMgr(!1))
              }, "权限管理"),
              e[22] || (e[22] = l("。", -1))
            ])
          ])
        ])
      ])
    ]),
    o(m, {
      modelValue: t.isShisShowRoleEditForm,
      "onUpdate:modelValue": e[7] || (e[7] = (u) => t.isShisShowRoleEditForm = u),
      title: "角色" + (t.roleForm.isCreate ? "" : " #" + t.currentRole.id),
      onOnOk: t.saveRole
    }, {
      default: r(() => [
        o(O, {
          model: t.currentRole,
          "label-width": 100,
          style: { "margin-right": "10%", "margin-left": "3%" }
        }, {
          default: r(() => [
            o(a, { label: "角色名称" }, {
              default: r(() => [
                o(y, {
                  modelValue: t.currentRole.name,
                  "onUpdate:modelValue": e[2] || (e[2] = (u) => t.currentRole.name = u),
                  placeholder: "Enter something..."
                }, null, 8, ["modelValue"])
              ]),
              _: 1
            }),
            o(a, { label: "角色说明" }, {
              default: r(() => [
                o(y, {
                  type: "textarea",
                  rows: 4,
                  modelValue: t.currentRole.content,
                  "onUpdate:modelValue": e[3] || (e[3] = (u) => t.currentRole.content = u),
                  placeholder: "Enter something..."
                }, null, 8, ["modelValue"])
              ]),
              _: 1
            }),
            t.roleForm.isTop ? $("", !0) : (g(), b(a, { key: 0 }, {
              default: r(() => [
                o(I, {
                  modelValue: t.currentRole.isInheritedParent,
                  "onUpdate:modelValue": e[4] || (e[4] = (u) => t.currentRole.isInheritedParent = u)
                }, {
                  default: r(() => [...e[26] || (e[26] = [
                    l("继承父级权限", -1)
                  ])]),
                  _: 1
                }, 8, ["modelValue"])
              ]),
              _: 1
            })),
            o(a, { label: "角色状态" }, {
              default: r(() => [
                n("label", null, [
                  D(n("input", {
                    type: "radio",
                    "onUpdate:modelValue": e[5] || (e[5] = (u) => t.currentRole.stat = u),
                    value: "0"
                  }, null, 512), [
                    [M, t.currentRole.stat]
                  ]),
                  e[27] || (e[27] = l(" 启用", -1))
                ]),
                e[29] || (e[29] = l("   ", -1)),
                n("label", null, [
                  D(n("input", {
                    type: "radio",
                    "onUpdate:modelValue": e[6] || (e[6] = (u) => t.currentRole.stat = u),
                    value: "2"
                  }, null, 512), [
                    [M, t.currentRole.stat]
                  ]),
                  e[28] || (e[28] = l(" 禁用", -1))
                ])
              ]),
              _: 1
            }),
            t.roleForm.isCreate ? $("", !0) : (g(), b(a, {
              key: 1,
              style: { color: "gray" }
            }, {
              default: r(() => [
                l(" 创建于 " + C(t.currentRole.createDate) + " ", 1),
                e[30] || (e[30] = n("br", null, null, -1)),
                l(" 修改于 " + C(t.currentRole.updateDate), 1)
              ]),
              _: 1
            }))
          ]),
          _: 1
        }, 8, ["model"])
      ]),
      _: 1
    }, 8, ["modelValue", "title", "onOnOk"]),
    o(m, {
      modelValue: t.isShowPermissionMgr,
      "onUpdate:modelValue": e[8] || (e[8] = (u) => t.isShowPermissionMgr = u),
      width: "1000",
      title: "权限管理列表"
    }, {
      default: r(() => [
        o(v, {
          "is-pickup": t.isPermissionMgrPickup,
          "on-pickup": t.pickupPermission,
          "simple-api": t.simpleApi
        }, null, 8, ["is-pickup", "on-pickup", "simple-api"])
      ]),
      _: 1
    }, 8, ["modelValue"])
  ]);
}
const Ne = /* @__PURE__ */ J($e, [["render", Fe], ["__scopeId", "data-v-55e27d44"]]);
export {
  Ne as Role
};
