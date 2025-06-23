<%@ page pageEncoding="UTF-8" import="com.ajaxjs.util.JspBack, com.ajaxjs.user.admin.jsp.JspHelper"%>
<%@ taglib prefix="myTag" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="/ajaxjs"%>
<%
	JspHelper.getJspHelper(request);
	JspBack.list(request, "role", "角色");
%>
<myTag:list namespace="${namespace}" namespace_chs="${namespace_chs}">
	<script>
		tenantFilter();
	</script>
	
	<table class="aj-table even">
		<thead>
			<tr>
				<th>#</th>
				<th style="min-width: 200px;">${namespace_chs}名称</th>
				<th>${namespace_chs}编码</th>
				<th>${namespace_chs}类型</th>
				<th>所属应用</th>
				<th>所属系统</th>
				<th>所属租户</th>
				<th>状态</th>
				<th>创建日期</th>
				<th>修改日期</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
			<c:foreach items="${list}" var="item">
				<tr>
					<td>${item.id}</td>
					<td>${item.name}</td>
					<td>${item.code}</td>
					<td>${item.type}</td>
					<td>${item.appName}</td>
					<td>${item.sysName}</td>
					<td>${item.tenantName}</td>
					<myTag:list-common-rol style="31" item="${item}" namespace="${namespace}" namespace_chs="${namespace_chs}" >
						<a href="#">分配权限</a> | <a href="role/assignUsersToRole.jsp?roleId=${item.id}&roleName=${item.name}">分配用户</a> | 
					</myTag:list-common-rol>
				</tr>
			</c:foreach>
		</tbody>
	</table>
</myTag:list>