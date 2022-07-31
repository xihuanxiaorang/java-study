---
title:
tags: SpringSecurity 源码
created: 2022-07-29 04:22:31
modified: 2022-07-29 04:31:47
---
SecurityBuilder 用来构建安全对象，其主要的实现类有 HttpSecurity、WebSecurity，AuthenticationManagerBuilder；其中，HttpSecurity 用来构建 DefaultSecurityFilterChain(默认安全过滤器链)，WebSecurity 用来构建 Filter(过滤器)，AuthenticationManagerBuilder 用来构建 AuthenticationManager(认证管理器)。
