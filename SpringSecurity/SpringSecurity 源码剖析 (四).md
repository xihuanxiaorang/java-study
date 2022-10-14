---
title: SpringSecurity 源码剖析 (四)
tags: springsecurity 源码
created: 2022-08-03 23:48:04
modified: 2022-10-14 14:34:36
---

SpringSecurity 最重要的功能就是认证。**认证就是用来识别当前访问系统的用户是不是系统合法用户**，常见的认证方式包括 **用户名密码认证**，**手机验证码认证**，**第三方登录认证** 等等。 由上面分析的内容可以知道，SpringSecurity 是通过一系列过滤器来实现认证和授权功能的。SpringSecurity 默认提供了其中最为常见的一种认证方法，用户名密码认证，这种认证方式是通过 **`UsernamePasswordAuthenticationFilter`** 过滤器来实现的。让我们通过该过滤器来熟悉 SpringSecurity 认证的整个模式，从而扩展到其他过滤器。  
**一个过滤器中最主要的方法就是 `doFilter` 方法**。我们直接来到 `UsernamePasswordAuthenticationFilter` 的 `doFilter` 方法，看看这个过滤器到底是怎样实现认证功能的。

```java
public class UsernamePasswordAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
	public static final String SPRING_SECURITY_FORM_USERNAME_KEY = "username";  
	public static final String SPRING_SECURITY_FORM_PASSWORD_KEY = "password";  
	private static final AntPathRequestMatcher DEFAULT_ANT_PATH_REQUEST_MATCHER = new AntPathRequestMatcher("/login", "POST");  
	private String usernameParameter = SPRING_SECURITY_FORM_USERNAME_KEY;  
	private String passwordParameter = SPRING_SECURITY_FORM_PASSWORD_KEY;  
	private boolean postOnly = true;
	
	@Override  
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)  
	      throws AuthenticationException {  
	   if (this.postOnly && !request.getMethod().equals("POST")) {  
	      throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());  
	   }  
	   String username = obtainUsername(request);  
	   username = (username != null) ? username.trim() : "";  
	   String password = obtainPassword(request);  
	   password = (password != null) ? password : "";  
	   UsernamePasswordAuthenticationToken authRequest = UsernamePasswordAuthenticationToken.unauthenticated(username, password);  
	   // Allow subclasses to set the "details" property  
	   setDetails(request, authRequest);  
	   return this.getAuthenticationManager().authenticate(authRequest);  
	}
}
```

发现 `UsernamePasswordAuthenticationFilter` 过滤器中并没有 `doFilter` 方法，那么肯定在其父类 `AbstractAuthenticationProcessingFilter` 中。

```java
public abstract class AbstractAuthenticationProcessingFilter {
	@Override  
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)  
	      throws IOException, ServletException {  
	   doFilter((HttpServletRequest) request, (HttpServletResponse) response, chain);  
	}  
	  
	private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)  
	      throws IOException, ServletException {  
	   if (!requiresAuthentication(request, response)) {  
	      chain.doFilter(request, response);  
	      return;   }  
	   try {  
	      Authentication authenticationResult = attemptAuthentication(request, response);  
	      if (authenticationResult == null) {  
	         // return immediately as subclass has indicated that it hasn't completed  
	         return;  
	      }  
	      this.sessionStrategy.onAuthentication(authenticationResult, request, response);  
	      // Authentication success  
	      if (this.continueChainBeforeSuccessfulAuthentication) {  
	         chain.doFilter(request, response);  
	      }  
	      successfulAuthentication(request, response, chain, authenticationResult);  
	   }  
	   catch (InternalAuthenticationServiceException failed) {  
	      this.logger.error("An internal error occurred while trying to authenticate the user.", failed);  
	      unsuccessfulAuthentication(request, response, failed);  
	   }  
	   catch (AuthenticationException ex) {  
	      // Authentication failed  
	      unsuccessfulAuthentication(request, response, ex);  
	   }  
	}
}
```

可以看到，当请求经过该过滤器的时候，首先会先判断该请求是否

```java
protected boolean requiresAuthentication(HttpServletRequest request, HttpServletResponse response) {  
   if (this.requiresAuthenticationRequestMatcher.matches(request)) {  
      return true;  
   }  
   if (this.logger.isTraceEnabled()) {  
      this.logger  
            .trace(LogMessage.format("Did not match request to %s", this.requiresAuthenticationRequestMatcher));  
   }  
   return false;  
}
```
