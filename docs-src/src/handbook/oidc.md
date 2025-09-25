---
title: OIDC 协议简介（高级内容）
subTitle: 2024-12-05 by Frank Cheung
description: 协议简介（高级内容）
date: 2022-01-05
tags:
  - 协议简介
layout: layouts/docs.njk
---
# 资源所有者密码凭证（ROPC）
 <table class="oidc-doc-table">
                <tr>
                    <th width="100">分類</th>
                    <th width="160">授權類型</th>
                    <th width="160">支持情況</th>
                    <th>說明</th>
                </tr>
                <tr>
                    <td rowspan="4">有用戶參與</td>
                    <td>
                        授權碼模式<br />
                        authorization code
                    </td>
                    <td>
                        <code class="btn-success">支持</code>
                    </td>
                    <td>
                        最常用的安全模式。<a href="/Home/MFA/UML#auth-code" target="_blank">點擊查看流程圖</a>
                        <br />此模式下中央授權中心同時支持OAuth2.0擴展協議 <a href="javascript:void(0)" onclick="commonWinShow('/Developer/Document/OIDC/PKCE', 'PKCE及令牌安全', 780, 700)">PKCE</a>。
                    </td>
                </tr>
                <tr>
                    <td>
                        密碼模式<br />
                        password
                    </td>
                    <td>
                        <code class="btn-warning">支持</code>
<samll style="font-size: 13px;color: #888;">經安全考量后可適當支持</samll>
                    </td>
                    <td>
                        涉及到用戶相信密碼，不對外開放。
                    </td>
                </tr>
                <tr>
                    <td>
                        隱式授權模式<br />
                        implicit
                    </td>
                    <td>
                        <code class="btn-dark">不支持</code>
                    </td>
                    <td>
                        redirect_uri直接帶上Token，不安全。
                    </td>
                </tr>
                <tr>
                    <td>
                        混合模式<br />
                        hybrid
                    </td>
                    <td>
                        <code class="btn-dark">不支持</code>
                    </td>
                    <td>
                        相當于authorization code與implicit模式混合。<br />
                        同樣redirect_uri直接帶上Token，不安全。
                    </td>
                </tr>
                <tr>
                    <td>無用戶參與</td>
                    <td>
                        客戶端模式<br />
                        client credentials
                    </td>
                    <td>
                        <code class="btn-success">支持</code>
                    </td>
                    <td>
                        僅可得到AccessToken，無IDToken、RefreshToken。<br />無須刷新Token。
                    </td>
                </tr>
            </table>