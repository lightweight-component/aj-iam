document.writeln(`<link rel="stylesheet" href="../common/common.css" />
                  <link rel="stylesheet" href="../common/admin.css" />
                  <link href="https://cdn.bootcss.com/font-awesome/5.13.0/css/all.css" rel="stylesheet" />
                  <script src="../common/vue.js"></script>
                  <script src="../common/common.js"></script>`);
//                  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.2.0/css/all.min.css" />
//                  <script src="https://cdn.bootcdn.net/ajax/libs/vue/2.6.14/vue.min.js"></script>


const accessToken = localStorage.getItem("accessToken");

if(location.href.indexOf('jsp') != -1 && !accessToken) {
//    alert('你未登录！');
//    location.assign('index.jsp');
}
