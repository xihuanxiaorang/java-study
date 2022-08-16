---
title: SpringSecurity 源码剖析 (一)
tags: SpringSecurity 源码
created: 2022-07-30 18:46:06
modified: 2022-08-05 20:51:45
---

> [Architecture :: Spring Security](https://docs.spring.io/spring-security/reference/servlet/architecture.html#servlet-delegatingfilterproxy)

Spring Security 中 ` 认证 `、` 授权 ` 等功能都是基于 ` 过滤器 ` 实现的。**
![|300](../Attachments/Pasted%20image%2020220803162142.png)
在一个 web 项目中，请求从客户端发起(例如浏览器)，然后穿过层层 `Filter`，最终来到 `Servlet` 上，被 `Servlet` 处理。
上图中的 `Filter` 我们称之为 `Web Filter`，Spring Security 中的 `Filter` 我们称之为 `Security Filter`，关系图如下：
![|500](../Attachments/Pasted%20image%2020220803162245.png)
可以看到，**Spring Security Filter0...n 并不是直接嵌入到 `Web Filter` 中的，而是通过 `FilterChainProxy` 来统一管理 Spring Security Filter0...n，`FilterChainProxy` 本身则通过 Spring 提供的 `DelegatingFilterProxy` 代理过滤器嵌入到 Web Filter 之中**。
`FilterChainProxy` 可以存在多个过滤器链，如下图：
![|600](../Attachments/Pasted%20image%2020220803162311.png)
可以看到，**当请求到达 `FilterChainProxy` 之后，`FilterChainProxy` 会根据请求的路径，将请求转发到不同的 `SecurityFilterChain` 上面去，不同的 `SecurityFilterChain` 包含了不同的过滤器，也就是不同的请求会经过不同的过滤器**。
**

## DelegatingFilterProxy

**
> Spring 提供了一个名为 **DelegatingFilterProxy** 的过滤器实现，**它允许在 Servlet 容器的生命周期和 Spring 的 ApplicationContext 之间进行桥接**。 **Servlet 容器允许使用自己的标准注册过滤器，但它不知道 Spring 定义的 Bean**。 **DelegatingFilterProxy 可以通过标准的 Servlet 容器机制注册，但将所有工作委托给 FilterChainProxy**。

接下来，就让我们分析一下 DelegatingFilterProxy 是如何实现将所有工作都委托给 FilterChainProxy 的呢？**一个过滤器最重要的方法就是 `doFilter`** 。
```java
@Override  
public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)  
      throws ServletException, IOException {  
  
   // Lazily initialize the delegate if necessary.  
   Filter delegateToUse = this.delegate;  
   if (delegateToUse == null) {  
      synchronized (this.delegateMonitor) {  
         delegateToUse = this.delegate;  
         if (delegateToUse == null) {  
            WebApplicationContext wac = findWebApplicationContext();  
            if (wac == null) {  
               throw new IllegalStateException("No WebApplicationContext found: " +  
                     "no ContextLoaderListener or DispatcherServlet registered?");  
            }  
            delegateToUse = initDelegate(wac);  
         }  
         this.delegate = delegateToUse;  
      }  
   }  
  
   // Let the delegate perform the actual doFilter operation.  
   invokeDelegate(delegateToUse, request, response, filterChain);  
}
```
通过双**重检查锁机制**确保 delegateToUse 是一个单例对象，当 delegateToUse 为 null 的时候，调用 `initDelegate` 方法来初始化代理类，将 Spring 容器对象传入进去，一看就是准备从 Spring 容器中获取。
```java
protected Filter initDelegate(WebApplicationContext wac) throws ServletException {  
   String targetBeanName = getTargetBeanName();  
   Assert.state(targetBeanName != null, "No target bean name set");  
   Filter delegate = wac.getBean(targetBeanName, Filter.class);  
   if (isTargetFilterLifecycle()) {  
      delegate.init(getFilterConfig());  
   }  
   return delegate;  
}
```
debug(也就是发起一个请求) 之后可以发现，其中的 targetBeanName = "springSecurityFilterChain"，delegate 是一个 `FilterChainProxy` 过滤器的实例。至于 Spring 容器中为什么存在一个 beanName="springSecurityFilterChain" 的 FilterChainProxy 的实例？在后面的内容会被详细介绍，到时候回过头再看一遍就会很有感觉。
![|600](../Attachments/Pasted%20image%2020220803175933.png)
在获取到 `FilterChainProxy` 过滤器实例之后，就开始执行 `invokeDelegate` 方法。
```java
protected void invokeDelegate(  
      Filter delegate, ServletRequest request, ServletResponse response, FilterChain filterChain)  
      throws ServletException, IOException {  
   delegate.doFilter(request, response, filterChain);  
}
```
这个方法非常简单，就是调用 `FilterChainProxy` 过滤器的 `doFilter` 方法。之后所有后续的流程都将来到 `FilterChainProxy` 过滤器的 `doFilter` 方法，这也就是 `DelegatingFilterProxy` 是如何实现将所有工作都委托给 `FilterChainProxy` 的整个过程，其实并不复杂。

## FilterChainProxy

> Spring Security 的 Servlet 支持包含在 FilterChainProxy 中。 **FilterChainProxy 是 Spring Security 提供的一个特殊 Filter，它允许通过 SecurityFilterChain 委托给多个 Filter 实例**。由于 FilterChainProxy 是一个 Bean，它通常被包装在一个 DelegatingFilterProxy 中。

```java
public class FilterChainProxy extends GenericFilterBean {
	private static final String FILTER_APPLIED = FilterChainProxy.class.getName().concat(".APPLIED");
	private List<SecurityFilterChain> filterChains;

	@Override  
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)  
	      throws IOException, ServletException {  
	   boolean clearContext = request.getAttribute(FILTER_APPLIED) == null;  
	   if (!clearContext) {  
	      doFilterInternal(request, response, chain);  
	      return;   
	  }  
	   try {  
	      request.setAttribute(FILTER_APPLIED, Boolean.TRUE);  
	      doFilterInternal(request, response, chain);  
	   }  
	   catch (RequestRejectedException ex) {  
	      this.requestRejectedHandler.handle((HttpServletRequest) request, (HttpServletResponse) response, ex);  
	   }  
	   finally {  
	      SecurityContextHolder.clearContext();  
	      request.removeAttribute(FILTER_APPLIED);  
	   }  
	}
}
```
- FILTER_APPLIED 变量是一个标记，用来标记过滤器是否已经执行过了。
- filterChains 是过滤器链集合，其中的泛型是过滤器链，配置的多个过滤器链就保存在 filterChains 变量中。etc. 至于集合中的数据是怎么来的？ 在这里简单剧透一下，是在 `WebSecurity` 执行 `build` 方法构建 `FilterChainProxy` 过滤器的时候赋值的。这块的内容在 WebSecurity 的时候在详细分析。
**一个过滤器中最主要的方法就是 `doFilter` 方法**。在 `doFilter` 方法中，正常来说，clearContext 参数每次都应该为 true，于是每次都先给 request 标记上 FILTER_APPLIED 属性，然后执行 `doFilterInternal` 方法去走过滤器，执行完毕后，最后在 finally 代码块中清除 `SecurityContextHolder` 中保存的用户信息，同时移除 request 中的标记。
```java
private void doFilterInternal(ServletRequest request, ServletResponse response, FilterChain chain)  
	      throws IOException, ServletException {  
   FirewalledRequest firewallRequest = this.firewall.getFirewalledRequest((HttpServletRequest) request);  
   HttpServletResponse firewallResponse = this.firewall.getFirewalledResponse((HttpServletResponse) response);  
   List<Filter> filters = getFilters(firewallRequest);  
   if (filters == null || filters.size() == 0) {  
	  if (logger.isTraceEnabled()) {  
		 logger.trace(LogMessage.of(() -> "No security for " + requestLine(firewallRequest)));  
	  }  
	  firewallRequest.reset();  
	  chain.doFilter(firewallRequest, firewallResponse);  
	  return;   }  
   if (logger.isDebugEnabled()) {  
	  logger.debug(LogMessage.of(() -> "Securing " + requestLine(firewallRequest)));  
   }  
   VirtualFilterChain virtualFilterChain = new VirtualFilterChain(firewallRequest, chain, filters);  
   virtualFilterChain.doFilter(firewallRequest, firewallResponse);  
}
```
1. 首先将 request 请求封装成一个 `FirewalledRequest` 对象，在这个封装的过程中，也会判断请求是否合法。
2. 调用 `getFilters()` 方法，该方法用于从符合条件的过滤器链中取出所有过滤器。根据当前的请求，从 filterChains 中找到对应的过滤器链，然后由该过滤器链去处理请求。
```java
private List<Filter> getFilters(HttpServletRequest request) {  
   int count = 0;  
   for (SecurityFilterChain chain : this.filterChains) {  
      if (logger.isTraceEnabled()) {  
         logger.trace(LogMessage.format("Trying to match request against %s (%d/%d)", chain, ++count,  
               this.filterChains.size()));  
      }  
      if (chain.matches(request)) {  
         return chain.getFilters();  
      }  
   }  
   return null;  
}
```
3. 如果找不到满足条件的过滤器链，或者满足条件的滤器链中没有过滤器，那就是说明当前请求不需要经过 SpringSecurity 的过滤器链。直接执行 `chain.doFilter`，这个时候就又回到原生过滤器中去了。那么什么时候会发生这种情况呢？那就是针对项目中的静态资源，如果我们配置了资源放行，如 `web.ignoring().antMatchers("/hello")`，那么当你请求 /hello 接口时就会走到这里来，也就是说不经过 Spring Security Filters。
4. 如果查询到的 filters 是有值的，那么这个 filters 集合中存放的就是我们要经过的一个个过滤器了。此时它会作为参数被包装到构造出来的虚拟过滤器链 `VirtualFilterChain` 中，并执行虚拟过滤器链中的 `doFilter` 方法。

## VirtualFilterChain

```java
private static final class VirtualFilterChain implements FilterChain {  
   private final FilterChain originalChain;  
   private final List<Filter> additionalFilters;  
   private final FirewalledRequest firewalledRequest;  
   private final int size;  
   private int currentPosition = 0;

	public void doFilter(ServletRequest request, ServletResponse response) throws IOException, ServletException {  
	   if (this.currentPosition == this.size) {  
	      if (logger.isDebugEnabled()) {  
	         logger.debug(LogMessage.of(() -> "Secured " + requestLine(this.firewalledRequest)));  
	      }  
	      // Deactivate path stripping as we exit the security filter chain  
	      this.firewalledRequest.reset();  
	      this.originalChain.doFilter(request, response);  
	      return;   
	   }  
	   this.currentPosition++;  
	   Filter nextFilter = this.additionalFilters.get(this.currentPosition - 1);  
	   if (logger.isTraceEnabled()) {  
	      logger.trace(LogMessage.format("Invoking %s (%d/%d)", nextFilter.getClass().getSimpleName(),  
	            this.currentPosition, this.size));  
	   }  
	   nextFilter.doFilter(request, response, this);  
	}
```
1. `VirtualFilterChain` 类中首先声明了 5 个全局属性
	1. originalChain 表示原生的过滤器链，也就是 Web Filter；
	2. additionalFilters 表示 过滤器链中包含的过滤器集合；
	3. firewalledRequest 表示当前被包装过的请求；
	4. size 表示 Spring Security 中的过滤器链中过滤器的个数；
	5. currentPosition 则是遍历过滤器集合时的下标。
2. `doFilter` 方法就是 过滤器链中每个过滤器挨个执行 doFilter 方法的过程，如果 `currentPosition == size`，表示过滤器链已经执行完毕，此时通过调用 `originalChain.doFilter` 进入到原生过滤器链方法中，同时退出了 Spring Security 过滤器链。否则就依次遍历过滤器链中的每个过滤器，挨个调用过滤器的 `doFilter` 方法。
以上就是 SpringSecurity 的整个大致流程，比如说请求先经过 DelegatingFilterProxy 的 doFilter 方法，DelegatingFilterProxy 把工作委托给 FilterChainProxy，执行 FilterChainProxy 的 doFilter 方法，可以说 FilterChainProxy 是整个 SpringSecurity 的入口，当请求进来时，先执行其 doFilter 方法，然后才会依次执行每个过滤器中的 doFilter 方法。

```ad-pit
1. Spring 容器中为什么存在一个 beanName="springSecurityFilterChain" 的 FilterChainProxy 的实例？
2. FilterChainProxy中的过滤器链集合filterChains是什么时候被赋值的？
3. 资源放行是什么东西？
```
