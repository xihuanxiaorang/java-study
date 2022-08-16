---
title: SpringSecurity 源码剖析 (二)
tags: SpringSecurity 源码 
created: 2022-08-04 00:51:26
modified: 2022-08-15 23:06:29
---
先让我们来回顾一下上一篇文章中的内容，我们已经知道 SpringSecurity 的认证授权功能是通过过滤器链来实现的，至于留下的几个坑在后续的文章中都被会解决的。我们只要先有个大概的印象，到后面自己自己慢慢照着文档调试源码的时候就会有更加深刻的认识。

> 纸上学来终觉浅，绝知此事要躬行

好了，废话不多说，让我们进入赶快开始今天的内容，我都有点迫不及待了！
大家已经知道 SpringSecurity 的认证授权功能是通过过滤器链实现的，那么大家有没有想过 **过滤器是怎么构建出来的**？**一个个过滤器又是怎样被构建成一条过滤器链的** 呢？这就是该篇文章需要着重解决的问题。

## 构建🎈

### SecurityBuilder

```java
public interface SecurityBuilder<O> {
    O build() throws Exception;
}
```
该接口就定义了一个方法，构建一个泛型实例。

### HttpSecurityBuilder

`HttpSecurityBuilder` 是一个接口，继承自 `SecurityBuilder` 接口，其中泛型为 `DefaultSecurityFilterChain`，那么 `HttpSecurityBuilder` 接口的作用就是对 **默认过滤器链 `DefaultSecurityFilterChain`** 的构建进行了增强，为其构建定义了一些额外的获取配置和管理配置的方法。
```java
public interface HttpSecurityBuilder<H extends HttpSecurityBuilder<H>> extends SecurityBuilder<DefaultSecurityFilterChain> {
	<C extends SecurityConfigurer<DefaultSecurityFilterChain, H>> C getConfigurer(Class<C> clazz);
	
	<C extends SecurityConfigurer<DefaultSecurityFilterChain, H>> C removeConfigurer(Class<C> clazz);
	
	<C> void setSharedObject(Class<C> sharedType, C object);
	
	<C> C getSharedObject(Class<C> sharedType);
	
	H authenticationProvider(AuthenticationProvider authenticationProvider);
	
	H userDetailsService(UserDetailsService userDetailsService) throws Exception;
	
	H addFilterAfter(Filter filter, Class<? extends Filter> afterFilter);
	
	H addFilterBefore(Filter filter, Class<? extends Filter> beforeFilter);
	
	H addFilter(Filter filter);
}
```
1. `getConfigurer` 方法用于获取一个 `SecurityConfigurer` 配置类。
2. `removeConfigurer` 方法用于移除一个 `SecurityConfigurer` 配置类。
3. `setSharedObject` 和 `getSharedObject` 方法用于维护共享的对象。
4. **`authenticationProvider`** 方法用于添加具体的认证提供器，后面会详细介绍 `AuthenticationProvider` 接口。
5. **`userDetailsService`** 方法用于配置数据源接口，后面会详细介绍 `UserDetailsService` 接口。
6. `addFilterAfter` 方法用于在某一个过滤器之后添加一个过滤器。
7. `addFilterBefore` 方法用于在某一个过滤器之前添加一个过滤器。
8. `addFilter` 方法用于添加一个过滤器，该过滤器必须是现有过滤器链中的某一个过滤器或其扩展。注意：如果使用该方法添加一个自定义的过滤器，则会报错！
`HttpSecurityBuilder` 接口中定义的方法都将在 `HttpSecurity` 中被实现。

### AbstractSecurityBuilder

`AbstractSecurityBuilder` 是对 `SecurityBuilder` 的实现。
```java
public abstract class AbstractSecurityBuilder<O> implements SecurityBuilder<O> {  
   private AtomicBoolean building = new AtomicBoolean();  
   private O object;  
  
   @Override  
   public final O build() throws Exception {  
      if (this.building.compareAndSet(false, true)) {  
         this.object = doBuild();  
         return this.object;  
      }  
      throw new AlreadyBuiltException("This object has already been built");  
   }  
  
   public final O getObject() {  
      if (!this.building.get()) {  
         throw new IllegalStateException("This object has not been built");  
      }  
      return this.object;  
   }  
   
   protected abstract O doBuild() throws Exception;  
}
```
可以看到，这里重写了 `build` 方法，并且加了 `final` 关键字，不可被覆写！并且通过原子类 `AtomicBoolean` 对构建方法 `build` 方法进行了调用限制：**每个目标对象只能被构建一次**。构建的核心逻辑通过预留的钩子方法 `doBuild` 方法来扩展，典型的模板方法模式，父类定义抽象方法，交给子类去实现具体逻辑。`AbstractSecurityBuilder` 还提供了获取已经构建好的目标对象的方法 `getObject`。

### AbstractConfiguredSecurityBuilder

`AbstractConfiguredSecurityBuilder` 是 `AbstractSecurityBuilder` 的子类。
```java
public abstract class AbstractConfiguredSecurityBuilder<O, B extends SecurityBuilder<O>> extends AbstractSecurityBuilder<O> {}
```
`AbstractConfiguredSecurityBuilder` 定义了一个枚举类，将整个构建过程分为 5 种状态，也可以理解为构建过程生命周期的五个阶段，如下：
```java
private enum BuildState {
	UNBUILT(0),
	INITIALIZING(1),
	CONFIGURING(2),
	BUILDING(3),
	BUILT(4);
	
	private final int order;
	
	BuildState(int order) {
		this.order = order;
	}
	
	public boolean isInitializing() {
		return INITIALIZING.order == order;
	}
	
	public boolean isConfigured() {
		return order >= CONFIGURING.order;
	}
}
```
五种状态分别是 `UNBUILT` 、 `INITIALIZING` 、`CONFIGURING` 、 `BUILDING` 以及 `BUILT`。另外还提供了两个判断方法，`isInitializing` 判断是否正在初始化阶段，`isConfigured` 表示是否已经构建完成。
`AbstractConfiguredSecurityBuilder` 中的方法比较多，在这里只列出两个关键的方法：
第一个是 `add` 方法，这相当于在收集所有的配置。将 **所有的 `SecurityConfigurer` 收集存储到 `configurers` 集合中，将来在执行 `doBuild` 方法中统一配置**。
🤔现在是不是对 `SecurityConfigurer` 感到疑惑，为什么在该类中定义一个 `configurers` 集合用来存储 `SecurityConfigurer` ？并且 `SecurityConfigurer` 是什么东西？有什么用？别急，这个 `SecurityConfigurer` 也是 SpringSecurity 中比较重量级别的接口，在后面我们会详细介绍，所以让我们接着往下看。
```java
private <C extends SecurityConfigurer<O, B>> void add(C configurer) {  
   Assert.notNull(configurer, "configurer cannot be null");  
   Class<? extends SecurityConfigurer<O, B>> clazz = (Class<? extends SecurityConfigurer<O, B>>) configurer  
         .getClass();  
   synchronized (this.configurers) {  
      if (this.buildState.isConfigured()) {  
         throw new IllegalStateException("Cannot apply " + configurer + " to already built object");  
      }  
      List<SecurityConfigurer<O, B>> configs = null;  
      if (this.allowConfigurersOfSameType) {  
         configs = this.configurers.get(clazz);  
      }  
      configs = (configs != null) ? configs : new ArrayList<>(1);  
      configs.add(configurer);  
      this.configurers.put(clazz, configs);  
      if (this.buildState.isInitializing()) {  
         this.configurersAddedInInitializing.add(configurer);  
      }  
   }  
}
```
第二个方法就是重写父类 `AbstractSecurityBuilder` 中的 `doBuild` 方法。
```java
@Override  
protected final O doBuild() throws Exception {  
   synchronized (this.configurers) {  
      this.buildState = BuildState.INITIALIZING;  
      beforeInit();  
      init();  
      this.buildState = BuildState.CONFIGURING;  
      beforeConfigure();  
      configure();  
      this.buildState = BuildState.BUILDING;  
      O result = performBuild();  
      this.buildState = BuildState.BUILT;  
      return result;  
   }  
}

protected abstract O performBuild() throws Exception;

private void init() throws Exception {  
   Collection<SecurityConfigurer<O, B>> configurers = getConfigurers();  
   for (SecurityConfigurer<O, B> configurer : configurers) {  
      configurer.init((B) this);  
   }  
   for (SecurityConfigurer<O, B> configurer : this.configurersAddedInInitializing) {  
      configurer.init((B) this);  
   }  
}  
  
private void configure() throws Exception {  
   Collection<SecurityConfigurer<O, B>> configurers = getConfigurers();  
   for (SecurityConfigurer<O, B> configurer : configurers) {  
      configurer.configure((B) this);  
   }  
}  
  
private Collection<SecurityConfigurer<O, B>> getConfigurers() {  
   List<SecurityConfigurer<O, B>> result = new ArrayList<>();  
   for (List<SecurityConfigurer<O, B>> configs : this.configurers.values()) {  
      result.addAll(configs);  
   }  
   return result;  
}
```
`doBuild` 方法就是在一遍更新状态一边进行构建目标对象。
- `beforeInit` 是一个预留方法，没有任何实现；
- `init` 方法是在获取到所有的 `SecurityConfigurer` 之后，依次调用每个 `SecurityConfigurer` 的 init 方法进行初始化；
- `beforeConfigure` 方法也是一个预留方法，没有任何实现；
- `configure` 方法是在获取到所有的 `SecurityConfigurer` 之后，**依次调用每个 `SecurityConfigurer` 的 `configure` 方法进行配置**；
- **`performBuild` 方法是真正执行构建目标对象的方法**，得知该方法是一个抽象方法，真正的逻辑留给子类去实现，不得不说，SpringSecurity 真是将模板方法模式用到极致👍。

### HttpSecurity📌

```java
public final class HttpSecurity extends AbstractConfiguredSecurityBuilder<DefaultSecurityFilterChain, HttpSecurity>  
      implements SecurityBuilder<DefaultSecurityFilterChain>, HttpSecurityBuilder<HttpSecurity> {
}
```
其中，`HttpSecurity` 的继承关系图如下所示：
![|800](../Attachments/image%201.png)
可以看到，`HttpSecurity` 继承自 `AbstractConfiguredSecurityBuilder`，同时实现了 `SecurityBuilder` 和 `HttpSecurityBuilder` 两个接口。
结合上面 `HttpSecurity` 的源码，`HttpSecurity` 实现 `SecurityBuilder` 接口时传入的泛型为 `DefaultSecurityFilterChain`，那么可以很清楚的知道 `HttpSecurity` 就是用来构建 `DefaultSecurityFilterChain` 对象的了。
至于什么是 `DefaultSecurityFilterChain` 呢🤔？相信看过上一篇文章的小伙伴会知道 `SecurityFilterChain` 就是我们平时说的过滤器链。
```java
public interface SecurityFilterChain {  
   boolean matches(HttpServletRequest request);  
  
   List<Filter> getFilters();  
}
```
`SecurityFilterChain` 接口中只定义了两个方法，一个是 `matches` 方法用来匹配请求；另外一个是 `getFilters` 方法返回一个 `List` 集合，集合中放着过滤器 `Filter` 对象。当一个请求到来时，用 `matches` 方法去比较请求是否和当前过滤器链匹配，如果匹配的话，那么当前请求会逐个经过 `getFilters` 方法返回的 `List` 集合中的过滤器。
`SecurityFilterChain` 接口只有一个实现类，那就是 `DefaultSecurityFilterChain`。
```java
public final class DefaultSecurityFilterChain implements SecurityFilterChain {  
   private final RequestMatcher requestMatcher;  
   private final List<Filter> filters;  
  
   public DefaultSecurityFilterChain(RequestMatcher requestMatcher, Filter... filters) {  
      this(requestMatcher, Arrays.asList(filters));  
   }  
  
   public DefaultSecurityFilterChain(RequestMatcher requestMatcher, List<Filter> filters) {  
      this.requestMatcher = requestMatcher;  
      this.filters = new ArrayList<>(filters);  
   }  
  
   public RequestMatcher getRequestMatcher() {  
      return this.requestMatcher;  
   }  
  
   @Override  
   public List<Filter> getFilters() {  
      return this.filters;  
   }  
  
   @Override  
   public boolean matches(HttpServletRequest request) {  
      return this.requestMatcher.matches(request);  
   }  
}
```
`DefaultSecurityFilterChain` 只是对 `SecurityFilterChain` 中的方法进行了实现，并没有特别值得说的地方。从上面的介绍中，大家可以看到，`DefaultSecurityFilterChain` 其实就相当于 Spring Security 中的过滤器链，一个 `DefaultSecurityFilterChain` 代表一个过滤器链，如果系统中存在多个过滤器链，则会存在多个 `DefaultSecurityFilterChain` 对象。
总结一下，**`HttpSecurity` 就是用来构建过滤器链**。
说回到 `HttpSecurity`，`HttpSecurity` 做的事情还有进行各种各样的 `SecurityConfigurer` 配置。如：
```java
public CorsConfigurer<HttpSecurity> cors() throws Exception {
	return getOrApply(new CorsConfigurer<>());
}

public CsrfConfigurer<HttpSecurity> csrf() throws Exception {
	ApplicationContext context = getContext();
	return getOrApply(new CsrfConfigurer<>(context));
}

public FormLoginConfigurer<HttpSecurity> formLogin() throws Exception {
	return getOrApply(new FormLoginConfigurer<>());
}

public ExceptionHandlingConfigurer<HttpSecurity> exceptionHandling() throws Exception {
	return getOrApply(new ExceptionHandlingConfigurer<>());
}
```
`HttpSecurity` 中有大量类似的方法，可以在此处对过滤器链中的过滤器一个一个进行配置。
每个配置方法的结尾都会使用 `getOrApply` 方法，这个方法是干嘛的呢？🤔
```java
private <C extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity>> C getOrApply(C configurer) throws Exception {
	C existingConfig = (C) getConfigurer(configurer.getClass());
	if (existingConfig != null) {
		return existingConfig;
	}
	return apply(configurer);
}
```
`getConfigurer` 方法是在它的父类 `AbstractConfiguredSecurityBuilder` 中定义的，目的是就是去查看当前这个 `SecurityConfigurer` 是否已经配置过了。如果当前 `xxxConfigurer` 已经配置过了，则直接返回，否则调用 `apply` 方法，这个 `apply` 方法最终会调用父类 `AbstractConfiguredSecurityBuilder` 中的 `add` 方法，将当前 `xxxConfigurer` 配置存储起来。
`HttpSecurity` 中还有一个 `addFilter` 方法：
```java
public HttpSecurity addFilter(Filter filter) {
	Class<? extends Filter> filterClass = filter.getClass();
	if (!comparator.isRegistered(filterClass)) {
		throw new IllegalArgumentException(
		"The Filter class "
		  + filterClass.getName()
		  + " does not have a registered order and cannot be added without a specified order. Consider using addFilterBefore or addFilterAfter instead.");
	}
	this.filters.add(filter);
	return this;
}
```
这个 `addFilter` 方法的作用主要是在各个 `SecurityConfigurer` 执行 `configure` 配置方法的时候，会调用这个方法，（`SecurityConfigurer` 就是用来配置过滤器的），把 `Filter` 过滤器都添加到 `filters` 变量中。比如在父类 `AbstractAuthenticationFilterConfigurer` 的 `configure` 方法中就被用到，将配置好的 `UsernamePasswordAuthenticationFilter` 用户名密码认证过滤器添加到 `filters` 变量中。
最终统一在 `HttpSecurity` 重写父类的 `performBuild` 方法中，构建出来一条过滤器链：
```java
@Override
protected DefaultSecurityFilterChain performBuild() {
	filters.sort(comparator);
	return new DefaultSecurityFilterChain(requestMatcher, filters);
}
```
先给过滤器排序，然后构造出 `DefaultSecurityFilterChain` 过滤器链。

### WebSecurity

## 配置

### SecurityConfigurer

```java
public interface SecurityConfigurer<O, B extends SecurityBuilder<O>> {  
   void init(B builder) throws Exception;  
  
   void configure(B builder) throws Exception;  
}
```
可以看到，在 `SecurityConfigurer` 接口中定义了两个方法：`init` 初始化方法和 `configure` 配置方法。
需要注意的是，两个方法的参数类型都是一个泛型，继承自 `SecurityBuilder` 。
`SecurityConfigurer` 接口存在大量的实现类：
![|1200](../Attachments/Pasted%20image%2020220804184337.png)
我们就以平常开发中最为常见的表单登录配置类 `FormLoginConfigurer` 为例来进行分析，以点扩面，通过搞懂这一个配置类来摸清楚套路，从而依葫芦画瓢搞懂其他一大堆配置类。
先来看下 `FormLoginConfigurer` 配置类的继承关系图：
![|400](../Attachments/Pasted%20image%2020220804185453.png)

### SecurityConfigurerAdapter

`SecurityConfigurerAdapter` 实现了 `SecurityConfiugurer` 接口，我们使用的大部分 `xxxConfigurer` 都可以算作是 `SecurityConfigurerAdapter` 的子类。
![|600](../Attachments/Pasted%20image%2020220804233459.png)
SecurityConfigurerAdapter 除了实现了 `SecurityConfiugurer` 接口的 `init` 和 `configurer` 方法之外，还扩展出了几个非常好用的方法：
```java
public abstract class SecurityConfigurerAdapter<O, B extends SecurityBuilder<O>> implements SecurityConfigurer<O, B> {
	private B securityBuilder;
	private CompositeObjectPostProcessor objectPostProcessor = new CompositeObjectPostProcessor();
	
	public void init(B builder) throws Exception {}
	
	public void configure(B builder) throws Exception {}

	public B and() {
		return getBuilder();
	}
	
	protected final B getBuilder() {
		if (securityBuilder == null) {
			throw new IllegalStateException("securityBuilder cannot be null");
		}
		return securityBuilder;
	}
	
	protected <T> T postProcess(T object) {
		return (T) this.objectPostProcessor.postProcess(object);
	}
	
	public void addObjectPostProcessor(ObjectPostProcessor<?> objectPostProcessor) {
		this.objectPostProcessor.addObjectPostProcessor(objectPostProcessor);
	}
	
	public void setBuilder(B builder) {
		this.securityBuilder = builder;
	}
	
	private static final class CompositeObjectPostProcessor implements ObjectPostProcessor<Object> {
		private List<ObjectPostProcessor<?>> postProcessors = new ArrayList<>();

		@SuppressWarnings({ "rawtypes", "unchecked" })
		public Object postProcess(Object object) {
			for (ObjectPostProcessor opp : postProcessors) {
				Class<?> oppClass = opp.getClass();
				Class<?> oppType = GenericTypeResolver.resolveTypeArgument(oppClass, ObjectPostProcessor.class);
				if (oppType == null || oppType.isAssignableFrom(object.getClass())) {
					 object = opp.postProcess(object);
				}
			}
			return object;
		}
		
		private boolean addObjectPostProcessor(ObjectPostProcessor<?> objectPostProcessor) {
			boolean result = this.postProcessors.add(objectPostProcessor);
			postProcessors.sort(AnnotationAwareOrderComparator.INSTANCE);
			return result;
		}
	}
}
```
1. 首先一开始声明了一个 `CompositeObjectPostProcessor` 实例，`CompositeObjectPostProcessor` 是 `ObjectPostProcessor` 的一个实现，`ObjectPostProcessor` 本身是一个后置处理器，该后置处理器默认有两个实现，`AutowireBeanFactoryObjectPostProcessor` 和 `CompositeObjectPostProcessor`。其中，`AutowireBeanFactoryObjectPostProcessor` 主要是利用了 `AutowireCapableBeanFactory` 对 `Bean` 进行手动注册，因为在 Spring Secuirty 中，很多对象都是手动 new 出来的，这个 new 出来的对象和容器没有任何关系，利用 `AutowireCapableBeanFactory` 可以将这些手动 new 出来的对象注入到容器中，`AutowireBeanFactoryObjectPostProcessor` 的主要作用就是完成这件事；`CompositeObjectPostProcessor` 则是一个复合的对象处理器，里面维护了一个 `list` 集合，这个 `list` 集合中，大部分情况下只存储一条数据，那就是 `AutowireBeanFactoryObjectPostProcessor` 用来完成对象注入到容器的操作，如果用户手动调用了 `addObjectPostProcessor` 方法，那么 `CompositeObjectPostProcessor` 集合中维护的数据就会多出来一条，在 `CompositeObjectPostProcessor#postProcess` 方法中，会遍历集合中的所有 `ObjectPostProcessor`，挨个调用 `postProcess` 方法对对象进行后置处理。😵晕，这里看不懂有什么用，不影响，我们先继续让下看，别死扣，或许到后面你就会明白了呢。
2. `and` 方法，该方法返回值是一个泛型继承自 `SecurityBuilder`，当对某个 SecurityBuilder 进行不同配置的时候可以使用 `and` 方法进行链式调用。

### AbstractHttpSecurityConfigurer

继承自 `SecurityConfigurerAdapter`，并扩展了两个好使的方法：`disable` 和 `withObjectPostProcessor` 方法。
```java
public abstract class AbstractHttpConfigurer<T extends AbstractHttpConfigurer<T, B>, B extends HttpSecurityBuilder<B>>  
      extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, B> {  
	public B disable() {  
		getBuilder().removeConfigurer(getClass());  
		return getBuilder();  
	}  
	
	public T withObjectPostProcessor(ObjectPostProcessor<?> objectPostProcessor) {  
		addObjectPostProcessor(objectPostProcessor);  
		return (T) this;  
	}  
}
```
1. `disable` 方法很简单，就是从 `AbstractConfiguredSecurityBuilder` 类维护的 `configurers` 配置类集合中移除掉当前配置类，这样在 `AbstractConfiguredSecurityBuilder` 执行 `doBuild` 方法中的 `init` 和 `configure` 方法时，就不会执行当前配置类的初始化和配置方法。
2. `withObjectPostProcessor` 方法，其实调用的就是父类 `SecurityConfigurerAdapter` 中的 `addObjectPostProcessor` 方法，将 `ObjectPostProcessor` 添加到父类维护的 `CompositeObjectPostProcessor` 中。该方法与父类的 `addObjectPostProcessor` 不同的地方在于它有返回值，返回的时当前配置类，如果当前配置类想添加多个 `ObjectPostProcessor` 的时候可以使用该方法进行链式调用。至于 ObjectProcessor 类有什么用？如果小伙伴们有看过 Spring 源码，就会知道一个叫 BeanPostProcessor 的类，用来增强 Bean 的，这个 ObjectProcessor 也是一样的功能，用来增强对象。

### AbstractAuthenticationFilterConfigurer

AbstractAuthenticationFilterConfigurer 类中的方法比较多，源码比较长，不过不用慌，**对于一个 `SecurityConfigurer` 来说最重要的就是 `init` 和 `configure` 方法**，因为这两个方法是 `SecurityConfigurer` 的灵魂。
```java
@Override  
public void init(B http) throws Exception {  
   updateAuthenticationDefaults();  
   updateAccessDefaults(http);  
   registerDefaultAuthenticationEntryPoint(http);  
}
```
1. `init` 方法只干了三件事：
	- `updateAuthenticationDefaults` 方法主要是配置默认的登录处理地址，登录失败跳转地址，注销成功跳转地址。
	- `updateAccessDefaults` 方法主要是对登录页，登录处理地址和失败跳转地址默认进行全部放行配置。
	- `registerDefaultAuthenticationEntryPoint` 方法主要是注册默认的登录认证入口。这样当登录认证失败的时候，则会重新跳转到登录认证入口，即常说的登录页。
```java
@Override  
public void configure(B http) throws Exception {  
   PortMapper portMapper = http.getSharedObject(PortMapper.class);  
   if (portMapper != null) {  
      this.authenticationEntryPoint.setPortMapper(portMapper);  
   }  
   RequestCache requestCache = http.getSharedObject(RequestCache.class);  
   if (requestCache != null) {  
      this.defaultSuccessHandler.setRequestCache(requestCache);  
   }  
   this.authFilter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));  
   this.authFilter.setAuthenticationSuccessHandler(this.successHandler);  
   this.authFilter.setAuthenticationFailureHandler(this.failureHandler);  
   if (this.authenticationDetailsSource != null) {  
      this.authFilter.setAuthenticationDetailsSource(this.authenticationDetailsSource);  
   }  
   SessionAuthenticationStrategy sessionAuthenticationStrategy = http  
         .getSharedObject(SessionAuthenticationStrategy.class);  
   if (sessionAuthenticationStrategy != null) {  
      this.authFilter.setSessionAuthenticationStrategy(sessionAuthenticationStrategy);  
   }  
   RememberMeServices rememberMeServices = http.getSharedObject(RememberMeServices.class);  
   if (rememberMeServices != null) {  
      this.authFilter.setRememberMeServices(rememberMeServices);  
   }  
   SecurityContextConfigurer securityContextConfigurer = http.getConfigurer(SecurityContextConfigurer.class);  
   if (securityContextConfigurer != null && securityContextConfigurer.isRequireExplicitSave()) {  
      SecurityContextRepository securityContextRepository = securityContextConfigurer  
            .getSecurityContextRepository();  
      this.authFilter.setSecurityContextRepository(securityContextRepository);  
   }  
   F filter = postProcess(this.authFilter);  
   http.addFilter(filter);  
}
```
2. configure 方法中的逻辑其实很简单，就是给 `authFilter` 过滤器赋值，然后 `authFilter` 过滤器再去 postProcess 方法中走一圈注册到 Spring 容器中，最后再将配置好的过滤器添加到过滤器链中。至于为什么是被添加到过滤器链中，在后面会给出答案。

### FormLoginConfigurer📌

```java
public final class FormLoginConfigurer<H extends HttpSecurityBuilder<H>> extends  
      AbstractAuthenticationFilterConfigurer<H, FormLoginConfigurer<H>, UsernamePasswordAuthenticationFilter> {
	public FormLoginConfigurer() {  
		super(new UsernamePasswordAuthenticationFilter(), null);  
		usernameParameter("username");  
		passwordParameter("password");  
	}
}
```
`FormLoginCongurer` 继承自 `AbstractAuthenticationFilterConfigurer` 类，明确了 `AbstractAuthenticationFilterConfigurer` 中的泛型是 `UsernamePasswordAuthenticationFilter`，也就是说 `FormLoginCongurer` 最终要配置的是 `UsernamePasswordAuthenticationFilter` 过滤器，然后在父类 `AbstractAuthenticationFilterConfigurer` 的 `configure` 方法中将 `UsernamePasswordAuthenticationFilter` 过滤器添加到 HttpSecurity 所构建的过滤器链中。
我们在日常开发中有关表单登录的配置都出自这里：
![|600](../Attachments/Pasted%20image%2020220805164821.png)
好啦，这就是 `FormLoginCongurer` 这个配置类，用于配置 `UsernamePasswordAuthenticationFilter` 过滤器。其他的 `SecurityConfigurer` 十分类似，每个 `SecurityConfigurer` 都对应了一个不同的 `Filter` 过滤器。

```ad-important
`FormLoginCongurer` 配置好的 `UsernamePasswordAuthenticationFilter` 过滤器，将在父类 `AbstractAuthenticationFilterConfigurer` 的 `configure` 方法中被添加到 HttpSecurity 所构建的过滤器链中。
```

回顾一下这篇文章讲了什么：
1. **过滤器是如何配置的**，
2. **配置好的过滤器是如何被添加到过滤器链中的**，
3. **过滤器链是如何构建出来的**。
