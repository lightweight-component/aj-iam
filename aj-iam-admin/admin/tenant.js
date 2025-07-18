const apiPrefix = 'http://localhost/iam_api/common_api/tenant';

GENDER = {};

new Vue({
    el: '#vue',
    data: {
        mapList: [],
        create: {},
        isShowCreate: false,
        editingId: 0,
        page: {
            pageSize: 5, // Number of items per page
            totalCount: 0,
            currentPage: 1,
        },
        STAT: STAT
    },
    mounted() {
        this.fetchData();
    },
    computed: {
        // Calculate MySQL-style START (offset)
        start() {
            return (this.page.currentPage - 1) * this.page.pageSize;
        },
        // Calculate total pages
        totalPages() {
            return Math.ceil(this.page.totalCount / this.page.pageSize);
        },
    },
    methods: {
        fetchData() {
            aj.xhr.get(`${apiPrefix}/page?start=${this.start}&limit=${this.page.pageSize}`, json => {
                this.mapList = json.data.list;
                this.page.totalCount = json.data.totalCount;
            }, {
                header: {
                    Authorization: 'Bearer ' + accessToken
                }
            });
        },
        // Go to previous page
        prevPage() {
            if (this.page.currentPage > 1) {
                this.page.currentPage--;
                this.fetchData();
            }
        },
        // Go to next page
        nextPage() {
            if (this.page.currentPage < this.totalPages) {
                this.page.currentPage++;
                this.fetchData();
            }
        },
        // Optional: Go to specific page
        goToPage(page) {
            if (page >= 1 && page <= this.totalPages) {
                this.page.currentPage = page;
                this.fetchData();
            }
        },
        doCreate() {
            let url = "../admin_api/common/objectType/create";

            form("POST", url, convertKeysToUnderscore(this.create), (json) => {
                console.log(json);
                if (json && json.status == 1) {
                    alert('创建成功');
                    location.reload();
                }

            }, {
                header: {
                    Authorization: 'Bearer ' + accessToken
                }
            });
        },
        del(id) {
            if (confirm('确定删除？')) {
                let url = "../admin_api/common/objectType/delete/" + id;
                aj.xhr.postForm(url, {}, (json) => {
                    if (json && json.status == 1) {
                        alert('删除成功');
                        location.reload();
                    }
                }, {
                    header: {
                        Authorization: 'Bearer ' + accessToken
                    }
                });
            }
        },
        save(entity) {
            let url = "../admin_api/common/objectType/update";
            let _e = convertKeysToUnderscore(entity);
            let e = {
                object_type_id: _e.object_type_id,
                combat_party: _e.combat_party,
                obj_type_name: _e.obj_type_name,
                obj_group: _e.obj_group,
                obj_type: _e.obj_type,
                obj_type_img: _e.obj_type_img,
                obj_type_num: _e.obj_type_num
            };

            aj.xhr.postForm(url, e, (json) => {
                console.log(json);
                if (json && json.status == 1) {
                    alert('修改成功');
                    location.reload();
                }

            }, {
                header: {
                    Authorization: 'Bearer ' + accessToken
                }
            });
        }
    }
});