<%@ page pageEncoding="UTF-8" import="com.ajaxjs.user.admin.jsp.JspHelper"%>
<%@ taglib prefix="myTag" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="/ajaxjs"%>
<%
	String sql = "SELECT t.*, u.name AS userName, c.name AS clientName, z.name AS tenantName FROM oauth_access_token t "+
	"LEFT JOIN user u ON t.user_id = u.id " +
	"LEFT JOIN tenant z ON u.tenant_id = z.id " +  
	"LEFT JOIN system_app c ON t.id = c.id WHERE 1=1";

if (request.getParameter("tenantId") != null) 
	sql = sql.replace("1=1", "1=1 AND c.tenantId = " + JspHelper.safeGet(request, "tenantId"));

if (request.getParameter("keyword") != null) 
	sql = sql.replace("1=1", "1=1 AND t.accessToken LIKE '%" + JspHelper.safeGet(request, "keyword") + "%'");

	JspHelper.init(request);
	JspHelper.parepreListSql(request, sql, "token", "Token");
	JspHelper.getList(request, sql);
	
	JspHelper.closeConn(request); 
%>
<myTag:list namespace="token" namespace_chs="Token" show_create="false">
	<script>
		tenantFilter();
	</script>
	
	<table class="aj-table">
		<thead>
			<tr>
				<th>#</th>
				<th>AccessToken</th>
				<th>租户</th>
				<th>客户端</th>
				<th>用户</th>
				<th>权限范围</th>
				<th>有效期至</th>
				<th>创建日期</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
			<c:foreach items="${list}" var="item">
				<tr>
					<td>${item.id}</td>
					<td>${item.accessToken}</td>
					<td title="tenantId：${item.userId}">${item.tenantName}</td>
					<td title="clientId：${item.clientId}">${item.clientName}</td>
					<td title="userId：${item.userId}">${item.userName}</td>
					<td>${item.scope}</td>
					<td>
						<script>
						;(function(){
							document.write(new Date(new Date().getTime() + ${item.expiresIn}).format('yyyy-MM-dd hh:mm'));
						})();
						</script>
					</td>
					<myTag:list-common-rol style="34" item="${item}" namespace="${namespace}" namespace_chs="${namespace_chs}" />
				</tr>
			</c:foreach>
		</tbody>
	</table>
</myTag:list>