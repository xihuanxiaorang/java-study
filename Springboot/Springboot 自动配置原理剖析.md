---
title: Springboot 自动配置原理剖析
tags: java springboot 源码
created: 2022-05-06 21:24:27
modified: 2022-08-03 03:33:29
---

在开始剖析 Springboot 自动配置原理之前，首先得明白**Springboot 的自动配置是什么**？
Springboot 自动配置 => 英文是 AutoConfiguration，它是指根据你所添加的 jar 包 (依赖) 自动配置你的 Spring 应用程序，它为 Springboot 框架的 " 开箱即用 " 提供了基础支 0 撑。
![[Springboot 源码阅读环境构建]]

```ad-important
**阅读源码**的时候**带着目的去读**，**先抓主干**，**不要一开始就陷入到各个分支**(即一直F5进一个方法)当中无法自拔。需要注意的是，这篇文章我为了解释每个方法的作用，会进入到每个方法当中去。
```
咱们就先从这个最简单的例子开始，来一步步剖析 Springboot 的自动配置原理到底是如何实现的。
首先来到 SampleWebFreeMarkerApplication 启动类：

```java
@SpringBootApplication  
public class SampleWebFreeMarkerApplication {  
   public static void main(String[] args) {  
      SpringApplication.run(SampleWebFreeMarkerApplication.class, args);  
   }  
}
```
咱们运行的是 main 方法，那么先将注意力放到 main 方法中，看到其中只有一句代码，调用 SpringApplication 类的静态方法 run，将启动类的 Class 对象作为参数传入方法中。
```java
public static ConfigurableApplicationContext run(Class<?> primarySource, String... args) {  
   return run(new Class<?>[] { primarySource }, args);  
}

public static ConfigurableApplicationContext run(Class<?>[] primarySources, String[] args) {  
   return new SpringApplication(primarySources).run(args);  
}
```
最终调用重载的 run 方法，在方法中 new 一个 SpringApplication 对象，将启动类传入构造方法中，最后调用 SpringApplication 实例对象的 run 方法。先看一下 SpringApplication 的构造方法：
```java
public SpringApplication(Class<?>... primarySources) {  
   this(null, primarySources);  
}

public SpringApplication(ResourceLoader resourceLoader, Class<?>... primarySources) {  
   this.resourceLoader = resourceLoader;  
   Assert.notNull(primarySources, "PrimarySources must not be null");  
   this.primarySources = new LinkedHashSet<>(Arrays.asList(primarySources));  
   this.webApplicationType = WebApplicationType.deduceFromClasspath();
   this.bootstrapRegistryInitializers = new ArrayList<>(  
         getSpringFactoriesInstances(BootstrapRegistryInitializer.class));  
   setInitializers((Collection) getSpringFactoriesInstances(ApplicationContextInitializer.class));  
   setListeners((Collection) getSpringFactoriesInstances(ApplicationListener.class));  
   this.mainApplicationClass = deduceMainApplicationClass();  
}
```
在 SpringApplication 的构造方法中，有几个比较重要的方法：
第一个是 WebApplicationType.deduceFromClasspath(); 该方法用于判断当前 Springboot 应用是什么类型，可选值有 REACTIVE(响应式)、NONE、SERVLET(✔)。
```java
static WebApplicationType deduceFromClasspath() {  
   if (ClassUtils.isPresent(WEBFLUX_INDICATOR_CLASS, null) && !ClassUtils.isPresent(WEBMVC_INDICATOR_CLASS, null)  
         && !ClassUtils.isPresent(JERSEY_INDICATOR_CLASS, null)) {  
      return WebApplicationType.REACTIVE;  
   }  
   for (String className : SERVLET_INDICATOR_CLASSES) {  
      if (!ClassUtils.isPresent(className, null)) {  
         return WebApplicationType.NONE;  
      }  
   }   return WebApplicationType.SERVLET;  
}
```
🎯第二个方法是 getSpringFactoriesInstances(BootstrapRegistryInitializer.class); 该方法比较关键，为什么说比较关键呢？因为该用到了 Springboot 中的**SpringFactories 机制**。至于什么是 SpringFactories 机制，咱们接着往下看！
```java
private <T> Collection<T> getSpringFactoriesInstances(Class<T> type) {  
   return getSpringFactoriesInstances(type, new Class<?>[] {});  
}

private <T> Collection<T> getSpringFactoriesInstances(Class<T> type, Class<?>[] parameterTypes, Object... args) {  
   ClassLoader classLoader = getClassLoader();  
   // Use names and ensure unique to protect against duplicates  
   Set<String> names = new LinkedHashSet<>(SpringFactoriesLoader.loadFactoryNames(type, classLoader));  
   List<T> instances = createSpringFactoriesInstances(type, parameterTypes, classLoader, args, names);  
   AnnotationAwareOrderComparator.sort(instances);  
   return instances;  
}
```
在该方法中有一个关键性方法 SpringFactoriesLoader.loadFactoryNames(type, classLoader); 获取指定 Class 类型对应的类名称。
```java
public static List<String> loadFactoryNames(Class<?> factoryType, @Nullable ClassLoader classLoader) {  
   ClassLoader classLoaderToUse = classLoader;  
   if (classLoaderToUse == null) {  
      classLoaderToUse = SpringFactoriesLoader.class.getClassLoader();  
   }  
   String factoryTypeName = factoryType.getName();  
   return loadSpringFactories(classLoaderToUse).getOrDefault(factoryTypeName, Collections.emptyList());  
}
```
loadSpringFactories 方法 = ↓
```java
private static Map<String, List<String>> loadSpringFactories(ClassLoader classLoader) {  
   Map<String, List<String>> result = cache.get(classLoader);  
   if (result != null) {  
      return result;  
   }  
  
   result = new HashMap<>();  
   try {  
      Enumeration<URL> urls = classLoader.getResources(FACTORIES_RESOURCE_LOCATION);  
      while (urls.hasMoreElements()) {  
         URL url = urls.nextElement();  
         UrlResource resource = new UrlResource(url);  
         Properties properties = PropertiesLoaderUtils.loadProperties(resource);  
         for (Map.Entry<?, ?> entry : properties.entrySet()) {  
            String factoryTypeName = ((String) entry.getKey()).trim();  
            String[] factoryImplementationNames =  
                  StringUtils.commaDelimitedListToStringArray((String) entry.getValue());  
            for (String factoryImplementationName : factoryImplementationNames) {  
               result.computeIfAbsent(factoryTypeName, key -> new ArrayList<>())  
                     .add(factoryImplementationName.trim());  
            }  
         }      }  
      // Replace all lists with unmodifiable lists containing unique elements  
      result.replaceAll((factoryType, implementations) -> implementations.stream().distinct()  
            .collect(Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList)));  
      cache.put(classLoader, result);  
   }  
   catch (IOException ex) {  
      throw new IllegalArgumentException("Unable to load factories from location [" +  
            FACTORIES_RESOURCE_LOCATION + "]", ex);  
   }  
   return result;  
}
```
在这个方法中有一个常量 **FACTORIES_RESOURCE_LOCATION**，这个常量的值 = **META-INF/spring.factories**，通过 classLoader 类加载器检索类路径下所有的 META-INF/spring.factories 文件，主要位于两处地方，分别在 spring-boot 和 spring-boot-autoconfigure 模块的 resources/META-INF/spring.factories。
![|600](../Attachments/Pasted%20image%2020220507013216.png)
让咱们来看看 spring-boot-autoconfigure 模块下的 spring.factories 文件长什么样 =↓
![|800](../Attachments/Pasted%20image%2020220507013645.png)
上面的图只截取了部分，该文件类似于 Properties 配置文件，一个 key 对应多个值，每个值之间用逗号分隔。
找到所有的 META-INF/spring.factories 文件之后，之后会把每个 META-INF/spring.factories 文件中的内容转换之后存放到一个 Map 集合中，最后会把 这个 Map 集合放到一个 cache(缓存) 当中，方便下次获取的时候直接从缓存中拿，而不需要再重新解析 META-INF/spring.factories 文件获取。
![|1000](../Attachments/Pasted%20image%2020220507014543.png)
回到 SpringFactoriesLoader.loadFactoryNames(type, classLoader); 方法，一开始传进来的 type 是 BootstrapRegistryInitializer 类型，那么就从 Map 集合中获取 key = ApplicationContextInitializer 类型的所有值。
![|800](../Attachments/Pasted%20image%2020220507020045.png) 返回的 names 大小为 0，说明 META-INF/spring.factories 文件中没有配置 BootstrapRegistryInitializer 类型的值。
跳出 getSpringFactoriesInstances 方法，回到 SpringApplication 构造方法中，下一步执行 getSpringFactoriesInstances(ApplicationContextInitializer.class); 方法，这个方法是不是很熟悉，和上面是一样的解析过程是一模一样的，只是换了一个 type 类型，这次获取的是 ApplicationContextInitializer 类型的值。点进去这个方法，
```java
private <T> Collection<T> getSpringFactoriesInstances(Class<T> type) {  
   return getSpringFactoriesInstances(type, new Class<?>[] {});  
}  
  
private <T> Collection<T> getSpringFactoriesInstances(Class<T> type, Class<?>[] parameterTypes, Object... args) {  
   ClassLoader classLoader = getClassLoader();  
   // Use names and ensure unique to protect against duplicates  
   Set<String> names = new LinkedHashSet<>(SpringFactoriesLoader.loadFactoryNames(type, classLoader));  
   List<T> instances = createSpringFactoriesInstances(type, parameterTypes, classLoader, args, names);  
   AnnotationAwareOrderComparator.sort(instances);  
   return instances;  
}
```
SpringFactoriesLoader.loadFactoryNames(type, classLoader); 方法在上面已经详细分析过了，我们就不再 Debug 进这个方法了，直接选中这行代码，Ctrl +U，计算一下这行代码的返回值。
![|600](../Attachments/Pasted%20image%2020220507021036.png)
方法返回值有 7 个。接下来执行其中的 createSpringFactoriesInstances(type, parameterTypes, classLoader, args, names); 该方法用于根据返回的全限定名通过反射技术创建出相应的实例。
```java
private <T> List<T> createSpringFactoriesInstances(Class<T> type, Class<?>[] parameterTypes,  
      ClassLoader classLoader, Object[] args, Set<String> names) {  
   List<T> instances = new ArrayList<>(names.size());  
   for (String name : names) {  
      try {  
         Class<?> instanceClass = ClassUtils.forName(name, classLoader);  
         Assert.isAssignable(type, instanceClass);  
         Constructor<?> constructor = instanceClass.getDeclaredConstructor(parameterTypes);  
         T instance = (T) BeanUtils.instantiateClass(constructor, args);  
         instances.add(instance);  
      }  
      catch (Throwable ex) {  
         throw new IllegalArgumentException("Cannot instantiate " + type + " : " + name, ex);  
      }  
   }  
   return instances;  
}
```
当 type = ApplicationContextInitializer 类型时，将创建出来的实例返回并设置到 SpringApplication 类中的 initializers 属性中保存起来。
回到 SpringApplication 构造方法中，下一步执行 getSpringFactoriesInstances(ApplicationListener.class); 方法，和上面的两个方法类似，就不点进这个方法看了，直接计算一下这个方法的返回值得了。
![|600](../Attachments/Pasted%20image%2020220507022014.png)
当 type = ApplicationListener 类型时，将创建出来的实例返回并设置到 SpringApplication 类中的 listeners 属性中保存起来。
接着执行下一步 deduceMainApplicationClass(); 该方法推断出当前 Springboot 应用的主类是谁。当前程序主类肯定是 smoketest.freemarker.SampleWebFreeMarkerApplication 类呀！将推断出来的主类保存到 mainApplicationClass 属性中。
```java
private Class<?> deduceMainApplicationClass() {  
   try {  
      StackTraceElement[] stackTrace = new RuntimeException().getStackTrace();  
      for (StackTraceElement stackTraceElement : stackTrace) {  
         if ("main".equals(stackTraceElement.getMethodName())) {  
            return Class.forName(stackTraceElement.getClassName());  
         }  
      }  
   }  
   catch (ClassNotFoundException ex) {  
      // Swallow and continue  
   }  
   return null;  
}
```
🎨结论：SpringFactories 机制类似于 Java SPI 加载机制，通过 SpringFactoriesLoader 类检索类路径下所有的 META-INF/spring.factories 文件，将文件中的内容解析封装到一个 Map 集合中。？？？
SpringApplication 构造方法中的内容就全部 Debug 完毕，接下来会调用该 SpringApplication 实例的 run 方法。
```java
public ConfigurableApplicationContext run(String... args) {  
   long startTime = System.nanoTime();  
   DefaultBootstrapContext bootstrapContext = createBootstrapContext();  
   ConfigurableApplicationContext context = null;  
   configureHeadlessProperty();  
   SpringApplicationRunListeners listeners = getRunListeners(args);  
   listeners.starting(bootstrapContext, this.mainApplicationClass);  
   try {  
      ApplicationArguments applicationArguments = new DefaultApplicationArguments(args);  
      ConfigurableEnvironment environment = prepareEnvironment(listeners, bootstrapContext, applicationArguments);  
      configureIgnoreBeanInfo(environment);  
      Banner printedBanner = printBanner(environment);  
      context = createApplicationContext();  
      context.setApplicationStartup(this.applicationStartup);  
      prepareContext(bootstrapContext, context, environment, listeners, applicationArguments, printedBanner);  
      refreshContext(context);  
      afterRefresh(context, applicationArguments);  
      Duration timeTakenToStartup = Duration.ofNanos(System.nanoTime() - startTime);  
      if (this.logStartupInfo) {  
         new StartupInfoLogger(this.mainApplicationClass).logStarted(getApplicationLog(), timeTakenToStartup);  
      }  
      listeners.started(context, timeTakenToStartup);  
      callRunners(context, applicationArguments);  
   }  
   catch (Throwable ex) {  
      handleRunFailure(context, ex, listeners);  
      throw new IllegalStateException(ex);  
   }  
   try {  
      Duration timeTakenToReady = Duration.ofNanos(System.nanoTime() - startTime);  
      listeners.ready(context, timeTakenToReady);  
   }  
   catch (Throwable ex) {  
      handleRunFailure(context, ex, null);  
      throw new IllegalStateException(ex);  
   }  
   return context;  
}
```
对于上面的 run 方法不会每个方法都讲到，只会将一些比较的关键性的方法。
在该方法中首先声明了一个 ConfigurableApplicationContext 接口类型的变量，ConfigurableApplicationContext => 翻译过来就是可配置的应用上下文，在我看来所谓的上下文就是在处理一些操作时被保存起来的一些信息，方便后续操作继续使用。ConfigurableApplicationContext 接口的实现类中包含咱们经常提到的也是最重要的 **beanFactory** 属性。
程序运行到 getRunListeners(args); 方法，用于获取监听器。 = ↓
```java
private SpringApplicationRunListeners getRunListeners(String[] args) {  
   Class<?>[] types = new Class<?>[] { SpringApplication.class, String[].class };  
   return new SpringApplicationRunListeners(logger,  
         getSpringFactoriesInstances(SpringApplicationRunListener.class, types, this, args),  
         this.applicationStartup);  
}
```
有没有觉得很熟悉呢？是不是和前面介绍的几个方法类似，通过读取 META-INF/spring.factories 文件中 key = SpringApplicationRunListener 所对应的值，将对应的值实例化成对象并返回。既然这样，咱们就不 Debug 进去了，直接计算一下结果，发现只存在一个 EventPublishingRunListener 监听器在其中。
![|600](../Attachments/Pasted%20image%2020220507142924.png)
执行 listeners.starting(bootstrapContext, this.mainApplicationClass); 发布 Springboot 应用程序开始启动的时间。
接着执行 prepareEnvironment(listeners, bootstrapContext, applicationArguments); 方法，该方法用于创建并返回一个 ConfigurableEnvironment 接口类型的环境变量 = ↓
```java
private ConfigurableEnvironment prepareEnvironment(SpringApplicationRunListeners listeners,  
      DefaultBootstrapContext bootstrapContext, ApplicationArguments applicationArguments) {  
   // Create and configure the environment  
   ConfigurableEnvironment environment = getOrCreateEnvironment();  
   configureEnvironment(environment, applicationArguments.getSourceArgs());  
   ConfigurationPropertySources.attach(environment);  
   listeners.environmentPrepared(bootstrapContext, environment);  
   DefaultPropertiesPropertySource.moveToEnd(environment);  
   Assert.state(!environment.containsProperty("spring.main.environment-prefix"),  
         "Environment prefix cannot be set via properties.");  
   bindToSpringApplication(environment);  
   if (!this.isCustomEnvironment) {  
      environment = convertEnvironment(environment);  
   }  
   ConfigurationPropertySources.attach(environment);  
   return environment;  
}
```
在该方法中 Springboot 官方注释写的很清楚，创建并配置环境。getOrCreateEnvironment(); 从方法名就可以看出该方法用于获取或者创建环境，想都不用想，程序刚开始运行，怎么可能会有 Environment ，所以该方法此时用于创建 Environment = ↓
```java
private ConfigurableEnvironment getOrCreateEnvironment() {  
   if (this.environment != null) {  
      return this.environment;  
   }  
   switch (this.webApplicationType) {  
   case SERVLET:  
      return new ApplicationServletEnvironment();  
   case REACTIVE:  
      return new ApplicationReactiveWebEnvironment();  
   default:  
      return new ApplicationEnvironment();  
   }  
}
```
从前面的方法中，我们得知 webApplicationType 属性的值 = SERVLET，所以此处会 new 一个 ApplicationServletEnvironment 类型的变量返回。ApplicationServletEnvironment -> StandardServletEnvironment -> StandardEnvironment -> AbstractEnvironment，一路继承。我们知道，**在初始化一个子类的时候会先去初始化其父类**，所以会先执行父类的构造方法，再执行子类的构造方法。知道这一点之后，让我们来看看 AbstractEnvironment 类的构造方法 = ↓
```java
public AbstractEnvironment() {  
   this(new MutablePropertySources());  
}

protected AbstractEnvironment(MutablePropertySources propertySources) {  
   this.propertySources = propertySources;  
   this.propertyResolver = createPropertyResolver(propertySources);  
   customizePropertySources(propertySources);  
}

protected void customizePropertySources(MutablePropertySources propertySources) {  
}
```
在其构造方法中，customizePropertySources 使用 protected 修饰符，使得子类可以去重写，刚好我们的子类 StandardServletEnvironment 有重写该方法 = ↓
```java
@Override  
protected void customizePropertySources(MutablePropertySources propertySources) {  
   propertySources.addLast(new StubPropertySource(SERVLET_CONFIG_PROPERTY_SOURCE_NAME));  
   propertySources.addLast(new StubPropertySource(SERVLET_CONTEXT_PROPERTY_SOURCE_NAME));  
   if (jndiPresent && JndiLocatorDelegate.isDefaultJndiEnvironmentAvailable()) {  
      propertySources.addLast(new JndiPropertySource(JNDI_PROPERTY_SOURCE_NAME));  
   }  
   super.customizePropertySources(propertySources);  
}
```
而 StandardServletEnvironment 子类中的 customizePropertySources 方法中又会去调用父类的该方法 super.customizePropertySources(propertySources); 所以来到父类 StandardEnvironment 的 customizePropertySources 方法 =↓
```java
@Override  
protected void customizePropertySources(MutablePropertySources propertySources) {  
   propertySources.addLast(  
         new PropertiesPropertySource(SYSTEM_PROPERTIES_PROPERTY_SOURCE_NAME, getSystemProperties()));  
   propertySources.addLast(  
         new SystemEnvironmentPropertySource(SYSTEM_ENVIRONMENT_PROPERTY_SOURCE_NAME, getSystemEnvironment()));  
}
```
我们打个断点在该方法中，计算一下 getSystemProperties() 方法 和 getSystemEnvironment() 方法获取的是什么东西。
可以看到 getSystemProperties() 方法获取到的是系统配置 =>
![|600](../Attachments/Pasted%20image%2020220507151411.png)
而 getSystemEnvironment() 方法获取到的是系统环境 =>
![|600](../Attachments/Pasted%20image%2020220507151753.png)
以上就是创建 Environment 的整个流程，主要就是将系统环境和系统配置封装到 Environment 类中，至于怎么配置 Environment 的就不细说了。创建完 Environment 之后，就使用监听器 listeners 发布一个环境已经准备完毕的事件。
好，回到 run 方法中，执行到 createApplicationContext() 方法，在前面只是声明了一个 ConfigurableApplicationContext 接口类型的变量，此处就是将创建出来的实现类赋值给这个变量 = ↓
```java
protected ConfigurableApplicationContext createApplicationContext() {  
   return this.applicationContextFactory.create(this.webApplicationType);  
}

ApplicationContextFactory DEFAULT = (webApplicationType) -> {  
   try {  
      for (ApplicationContextFactory candidate : SpringFactoriesLoader  
            .loadFactories(ApplicationContextFactory.class, ApplicationContextFactory.class.getClassLoader())) {  
         ConfigurableApplicationContext context = candidate.create(webApplicationType);  
         if (context != null) {  
            return context;  
         }  
      }      return new AnnotationConfigApplicationContext();  
   }  
   catch (Exception ex) {  
      throw new IllegalStateException("Unable create a default ApplicationContext instance, "  
            + "you may need a custom ApplicationContextFactory", ex);  
   }  
};

static class Factory implements ApplicationContextFactory {  
  
   @Override  
   public ConfigurableApplicationContext create(WebApplicationType webApplicationType) {  
      return (webApplicationType != WebApplicationType.SERVLET) ? null  
            : new AnnotationConfigServletWebServerApplicationContext();  
   }  
  
}
```
可以看到，最终会创建出一个 AnnotationConfigServletWebServerApplicationContext 类型的对象，看其名字包含 注解 | 配置 | 原生 Servlet | Web 服务器 的应用上下文对象 = ↓
```java
public AnnotationConfigServletWebServerApplicationContext() {  
   this.reader = new AnnotatedBeanDefinitionReader(this);  
   this.scanner = new ClassPathBeanDefinitionScanner(this);  
}
```
需要注意的是，正如上文所说，子类在调用构造方法的时候，先执行父类的构造方法，而 AnnotationConfigServletWebServerApplicationContext -> ServletWebServerApplicationContext -> GenericWebApplicationContext -> GenericApplicationContext，而在 GenericApplicationContext 的构造方法中会创建一个 **DefaultListableBeanFactory** 的对象赋值给它的 beanFactory 属性。
```java
public GenericApplicationContext() {  
   this.beanFactory = new DefaultListableBeanFactory();  
}
```
回到 AnnotationConfigServletWebServerApplicationContext 的 构造方法，在该方法中创建了两个对象，一个是 AnnotatedBeanDefinitionReader 类型，一个是 ClassPathBeanDefinitionScanner 类型。其中，需要拿出来说一下的是 AnnotatedBeanDefinitionReader 构造方法 = ↓
```java
public AnnotatedBeanDefinitionReader(BeanDefinitionRegistry registry) {  
   this(registry, getOrCreateEnvironment(registry));  
}

public AnnotatedBeanDefinitionReader(BeanDefinitionRegistry registry, Environment environment) {  
   Assert.notNull(registry, "BeanDefinitionRegistry must not be null");  
   Assert.notNull(environment, "Environment must not be null");  
   this.registry = registry;  
   this.conditionEvaluator = new ConditionEvaluator(registry, environment, null);  
   AnnotationConfigUtils.registerAnnotationConfigProcessors(this.registry);  
}

public static void registerAnnotationConfigProcessors(BeanDefinitionRegistry registry) {  
   registerAnnotationConfigProcessors(registry, null);  
}

public static Set<BeanDefinitionHolder> registerAnnotationConfigProcessors(  
      BeanDefinitionRegistry registry, @Nullable Object source) {  
  
   DefaultListableBeanFactory beanFactory = unwrapDefaultListableBeanFactory(registry);  
   if (beanFactory != null) {  
      if (!(beanFactory.getDependencyComparator() instanceof AnnotationAwareOrderComparator)) {  
         beanFactory.setDependencyComparator(AnnotationAwareOrderComparator.INSTANCE);  
      }  
      if (!(beanFactory.getAutowireCandidateResolver() instanceof ContextAnnotationAutowireCandidateResolver)) {  
         beanFactory.setAutowireCandidateResolver(new ContextAnnotationAutowireCandidateResolver());  
      }  
   }  
   Set<BeanDefinitionHolder> beanDefs = new LinkedHashSet<>(8);  
  
   if (!registry.containsBeanDefinition(CONFIGURATION_ANNOTATION_PROCESSOR_BEAN_NAME)) {  
      RootBeanDefinition def = new RootBeanDefinition(ConfigurationClassPostProcessor.class);  
      def.setSource(source);  
      beanDefs.add(registerPostProcessor(registry, def, CONFIGURATION_ANNOTATION_PROCESSOR_BEAN_NAME));  
   }  
  
   if (!registry.containsBeanDefinition(AUTOWIRED_ANNOTATION_PROCESSOR_BEAN_NAME)) {  
      RootBeanDefinition def = new RootBeanDefinition(AutowiredAnnotationBeanPostProcessor.class);  
      def.setSource(source);  
      beanDefs.add(registerPostProcessor(registry, def, AUTOWIRED_ANNOTATION_PROCESSOR_BEAN_NAME));  
   }  
  
   // Check for JSR-250 support, and if present add the CommonAnnotationBeanPostProcessor.  
   if (jsr250Present && !registry.containsBeanDefinition(COMMON_ANNOTATION_PROCESSOR_BEAN_NAME)) {  
      RootBeanDefinition def = new RootBeanDefinition(CommonAnnotationBeanPostProcessor.class);  
      def.setSource(source);  
      beanDefs.add(registerPostProcessor(registry, def, COMMON_ANNOTATION_PROCESSOR_BEAN_NAME));  
   }  
  
   // Check for JPA support, and if present add the PersistenceAnnotationBeanPostProcessor.  
   if (jpaPresent && !registry.containsBeanDefinition(PERSISTENCE_ANNOTATION_PROCESSOR_BEAN_NAME)) {  
      RootBeanDefinition def = new RootBeanDefinition();  
      try {  
         def.setBeanClass(ClassUtils.forName(PERSISTENCE_ANNOTATION_PROCESSOR_CLASS_NAME,  
               AnnotationConfigUtils.class.getClassLoader()));  
      }  
      catch (ClassNotFoundException ex) {  
         throw new IllegalStateException(  
               "Cannot load optional framework class: " + PERSISTENCE_ANNOTATION_PROCESSOR_CLASS_NAME, ex);  
      }  
      def.setSource(source);  
      beanDefs.add(registerPostProcessor(registry, def, PERSISTENCE_ANNOTATION_PROCESSOR_BEAN_NAME));  
   }  
  
   if (!registry.containsBeanDefinition(EVENT_LISTENER_PROCESSOR_BEAN_NAME)) {  
      RootBeanDefinition def = new RootBeanDefinition(EventListenerMethodProcessor.class);  
      def.setSource(source);  
      beanDefs.add(registerPostProcessor(registry, def, EVENT_LISTENER_PROCESSOR_BEAN_NAME));  
   }  
  
   if (!registry.containsBeanDefinition(EVENT_LISTENER_FACTORY_BEAN_NAME)) {  
      RootBeanDefinition def = new RootBeanDefinition(DefaultEventListenerFactory.class);  
      def.setSource(source);  
      beanDefs.add(registerPostProcessor(registry, def, EVENT_LISTENER_FACTORY_BEAN_NAME));  
   }  
  
   return beanDefs;  
}
```
在 AnnotatedBeanDefinitionReader 类的构造方法中，调用了 AnnotationConfigUtils 工具类的静态方法 registerAnnotationConfigProcessors(); 往容器中添加了大量有关注解的后置处理器的 bean 定义信息。其中咱们以最典型的 **ConfigurationClassPostProcessor 后置处理器 (该后置处理器属于 BeanFactoryPostProcessor)** 为例，看看是添加进去到容器中的 = ↓
```java
if (!registry.containsBeanDefinition(CONFIGURATION_ANNOTATION_PROCESSOR_BEAN_NAME)) {  
      RootBeanDefinition def = new RootBeanDefinition(ConfigurationClassPostProcessor.class);  
      def.setSource(source);  
      beanDefs.add(registerPostProcessor(registry, def, CONFIGURATION_ANNOTATION_PROCESSOR_BEAN_NAME));  
   }  
```
首先判断容器中是否存在 CONFIGURATION_ANNOTATION_PROCESSOR_BEAN_NAME = "org.springframework.context.annotation.internalConfigurationAnnotationProcessor"; 名字的 bean 定义信息，如果不存在的话，则 new 一个 RootBeanDefinition 类型的 bean 定义类，参数为 ConfigurationClassPostProcessor 类，然后调用 registerPostProcessor 方法将该 bean 定义信息注册到容器中 = ↓
```java
private static BeanDefinitionHolder registerPostProcessor(  
      BeanDefinitionRegistry registry, RootBeanDefinition definition, String beanName) {  
  
   definition.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);  
   registry.registerBeanDefinition(beanName, definition);  
   return new BeanDefinitionHolder(definition, beanName);  
}
```
来到 registerPostProcessor 方法，此时的 registry 是 DefaultListableBeanFactory 类型的，所以调用 DefaultListableBeanFactory 中的 registerBeanDefinition 注册 bean 定义信息方法 = ↓
```java
@Override  
public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition)  
      throws BeanDefinitionStoreException {  
   ...
   BeanDefinition existingDefinition = this.beanDefinitionMap.get(beanName);  
   if (existingDefinition != null) {  
      if (!isAllowBeanDefinitionOverriding()) {  
         throw new BeanDefinitionOverrideException(beanName, beanDefinition, existingDefinition);  
      }
      this.beanDefinitionMap.put(beanName, beanDefinition);  
   }  
   else {  
      if (hasBeanCreationStarted()) {  
         // Cannot modify startup-time collection elements anymore (for stable iteration)  
         synchronized (this.beanDefinitionMap) {  
            this.beanDefinitionMap.put(beanName, beanDefinition);  
            List<String> updatedDefinitions = new ArrayList<>(this.beanDefinitionNames.size() + 1);  
            updatedDefinitions.addAll(this.beanDefinitionNames);  
            updatedDefinitions.add(beanName);  
            this.beanDefinitionNames = updatedDefinitions;  
            removeManualSingletonName(beanName);  
         }  
      }  
      else {  
         // Still in startup registration phase  
         this.beanDefinitionMap.put(beanName, beanDefinition);  
         this.beanDefinitionNames.add(beanName);  
         removeManualSingletonName(beanName);  
      }  
      this.frozenBeanDefinitionNames = null;  
   }  
   ...
}
```
在该方法中首先判断 beanDefinitionMap 集合中是否存在该名字的 bean 定义信息，如果存在，并且允许覆盖的话，则直接 put 到 Map 集合中；如果不存在的话，则加锁之后再将 bean 定义信息 put 到 beanDefinitionMap 集合中。**在后续调用 AbstractApplicationContext 的 refresh 方法时，其中的 13 个方法中有一个 finishBeanFactoryInitialization 方法会根据放在该集合中的 bean 定义信息创建出对应的单例对象**。
此时，经过这一步，就已经将 **ConfigurationClassPostProcessor 后置处理器** 的 bean 定义信息放到 spring 容器中。
好，再次回到 run 方法，下一步执行 prepareContext(bootstrapContext, context, environment, listeners, applicationArguments, printedBanner); 方法 = ↓
```java
private void prepareContext(DefaultBootstrapContext bootstrapContext, ConfigurableApplicationContext context,  
      ConfigurableEnvironment environment, SpringApplicationRunListeners listeners,  
      ApplicationArguments applicationArguments, Banner printedBanner) {  
   context.setEnvironment(environment);  
   postProcessApplicationContext(context);  
   applyInitializers(context);  
   listeners.contextPrepared(context);  
   bootstrapContext.close(context);  
   if (this.logStartupInfo) {  
      logStartupInfo(context.getParent() == null);  
      logStartupProfileInfo(context);  
   }  
   // Add boot specific singleton beans  
   ConfigurableListableBeanFactory beanFactory = context.getBeanFactory();  
   beanFactory.registerSingleton("springApplicationArguments", applicationArguments);  
   if (printedBanner != null) {  
      beanFactory.registerSingleton("springBootBanner", printedBanner);  
   }  
   if (beanFactory instanceof AbstractAutowireCapableBeanFactory) {  
      ((AbstractAutowireCapableBeanFactory) beanFactory).setAllowCircularReferences(this.allowCircularReferences);  
      if (beanFactory instanceof DefaultListableBeanFactory) {  
         ((DefaultListableBeanFactory) beanFactory)  
               .setAllowBeanDefinitionOverriding(this.allowBeanDefinitionOverriding);  
      }  
   }  
   if (this.lazyInitialization) {  
      context.addBeanFactoryPostProcessor(new LazyInitializationBeanFactoryPostProcessor());  
   }  
   // Load the sources  
   Set<Object> sources = getAllSources();  
   Assert.notEmpty(sources, "Sources must not be empty");  
   load(context, sources.toArray(new Object[0]));  
   listeners.contextLoaded(context);  
}
```
将 environment 对象设置到 Context 的 属性。
执行 applyInitializers(context) 方法 => 循环遍历 initializers 属性，执行每个初始化器的 initialize 方法；心细的小伙伴，相比从上面就已经知道 initializers 属性的值不正是在 new 一个 SpringApplication 对象，执行 SpringApplication 构造方法时，其中有一个 setInitializers((Collection) getSpringFactoriesInstances(ApplicationContextInitializer.class)); 方法，通过 SpringFactories 机制获取到所有的 ApplicationContextInitializer 初始化器。
listeners.contextPrepared(context); 发布应用上下文已经准备完毕的事件。
接着往容器注册了 springApplicationArguments 和 springBootBanner 的单例对象。
接着执行 load(context, sources.toArray(new Object[0])); 方法，往容器中注册 SampleWebFreeMarkerApplication 启动类的 bean 定义信息。
![|1000](../Attachments/Pasted%20image%2020220507170650.png)
最后，listeners.contextLoaded(context); 发布应用上下文已经加载完毕的事件。
回到 run 方法，开始**执行其中最最关键的一个方法 refreshContext(context);** 该方法连接 Springboot 与 Spring，前面已经讲了 Springboot 是如何解析 META-INF/spring.factories 文件的，后面会开始讲 **Spring 中最最重要的 AbstractApplicationContext 类中的 refresh 方法中的 13 个方法** =↓
```java
private void refreshContext(ConfigurableApplicationContext context) {  
   if (this.registerShutdownHook) {  
      shutdownHook.registerApplicationContext(context);  
   }  
   refresh(context);  
}

protected void refresh(ConfigurableApplicationContext applicationContext) {  
   applicationContext.refresh();  
}

@Override  
public final void refresh() throws BeansException, IllegalStateException {  
   try {  
      super.refresh();  
   }  
   catch (RuntimeException ex) {  
      WebServer webServer = this.webServer;  
      if (webServer != null) {  
         webServer.stop();  
      }  
      throw ex;  
   }  
}
```
一步步 Debug 到当前 AnnotationConfigServletWebServerApplicationContext 父类 ServletWebServerApplicationContext 中的 refresh 方法，在此方法中又调用了父类的 refresh 方法，ServletWebServerApplicationContext 的父类是 AbstractApplicationContext，**执行 AbstractApplicationContext 类中的 refresh 方法**。终于来到咱们心心念念的 refresh 方法 = ↓
```java
public void refresh() throws BeansException, IllegalStateException {  
   synchronized (this.startupShutdownMonitor) {  
      StartupStep contextRefresh = this.applicationStartup.start("spring.context.refresh");  
      // Prepare this context for refreshing.  
      prepareRefresh();  
      // Tell the subclass to refresh the internal bean factory.  
      ConfigurableListableBeanFactory beanFactory = obtainFreshBeanFactory();  
      // Prepare the bean factory for use in this context.  
      prepareBeanFactory(beanFactory);  
      try {  
         // Allows post-processing of the bean factory in context subclasses.  
         postProcessBeanFactory(beanFactory);  
         StartupStep beanPostProcess = this.applicationStartup.start("spring.context.beans.post-process");  
         // Invoke factory processors registered as beans in the context.  
         invokeBeanFactoryPostProcessors(beanFactory);  
         // Register bean processors that intercept bean creation.  
         registerBeanPostProcessors(beanFactory);  
         beanPostProcess.end();  
         // Initialize message source for this context.  
         initMessageSource();  
         // Initialize event multicaster for this context.  
         initApplicationEventMulticaster();  
         // Initialize other special beans in specific context subclasses.  
         onRefresh();  
         // Check for listener beans and register them.  
         registerListeners();  
         // Instantiate all remaining (non-lazy-init) singletons.  
         finishBeanFactoryInitialization(beanFactory);  
         // Last step: publish corresponding event.  
         finishRefresh();  
      }  
      catch (BeansException ex) {  
         if (logger.isWarnEnabled()) {  
            logger.warn("Exception encountered during context initialization - " +  
                  "cancelling refresh attempt: " + ex);  
         }  
         // Destroy already created singletons to avoid dangling resources.  
         destroyBeans();  
         // Reset 'active' flag.  
         cancelRefresh(ex);  
         // Propagate exception to caller.  
         throw ex;  
      }  
      finally {  
         // Reset common introspection caches in Spring's core, since we  
         // might not ever need metadata for singleton beans anymore...         resetCommonCaches();  
         contextRefresh.end();  
      }  
   }  
}
```
该 refresh 方法中有 13 个方法，咱们这次只讲与本次主题 (**Springboot 自动配置原理**) 有关的方法 invokeBeanFactoryPostProcessors(beanFactory); 执行 beanFactory 后置处理器 = ↓
```java
protected void invokeBeanFactoryPostProcessors(ConfigurableListableBeanFactory beanFactory) {  
   PostProcessorRegistrationDelegate.invokeBeanFactoryPostProcessors(beanFactory, getBeanFactoryPostProcessors());  
  
   // Detect a LoadTimeWeaver and prepare for weaving, if found in the meantime  
   // (e.g. through an @Bean method registered by ConfigurationClassPostProcessor)   if (!NativeDetector.inNativeImage() && beanFactory.getTempClassLoader() == null && beanFactory.containsBean(LOAD_TIME_WEAVER_BEAN_NAME)) {  
      beanFactory.addBeanPostProcessor(new LoadTimeWeaverAwareProcessor(beanFactory));  
      beanFactory.setTempClassLoader(new ContextTypeMatchClassLoader(beanFactory.getBeanClassLoader()));  
   }  
}
```
在该方法中，调用 PostProcessorRegistrationDelegate 代理类的 invokeBeanFactoryPostProcessors 静态方法 ，该方法比较长，咱们只讲涉及到本次主题相关的部分代码 = ↓
```java
public static void invokeBeanFactoryPostProcessors(  
      ConfigurableListableBeanFactory beanFactory, List<BeanFactoryPostProcessor> beanFactoryPostProcessors) {
    ...
    // First, invoke the BeanDefinitionRegistryPostProcessors that implement PriorityOrdered.  
    String[] postProcessorNames =  
          beanFactory.getBeanNamesForType(BeanDefinitionRegistryPostProcessor.class, true, false);  
    for (String ppName : postProcessorNames) {  
       if (beanFactory.isTypeMatch(ppName, PriorityOrdered.class)) {  
          currentRegistryProcessors.add(beanFactory.getBean(ppName, BeanDefinitionRegistryPostProcessor.class));  
          processedBeans.add(ppName);  
       }  
    }  
    sortPostProcessors(currentRegistryProcessors, beanFactory);  
    registryProcessors.addAll(currentRegistryProcessors);  
    invokeBeanDefinitionRegistryPostProcessors(currentRegistryProcessors, registry, beanFactory.getApplicationStartup());  
    currentRegistryProcessors.clear();
    ...
}
```
首先，从容器中获取 BeanDefinitionRegistryPostProcessor 类型的 bean 名称，而我们上面提到的 **ConfigurationClassPostProcessor 后置处理器** 正好是 BeanDefinitionRegistryPostProcessor 接口的实现类，并且在上面就已经将 ConfigurationClassPostProcessor 后置处理器的 bean 定义信息注册到容器中了，所以此处就可以得到 ConfigurationClassPostProcessor 后置处理器的 beanName。
![|1000](../Attachments/Pasted%20image%2020220507180913.png)
S 循环遍历获取到的后置处理器名称，判断该后置处理器是否实现 PriorityOrdered 接口，ConfigurationClassPostProcessor 刚好也是 PriorityOrdered 接口的实现类，满足条件。
调用 **beanFactory.getBean 方法从容器中获取 ConfigurationClassPostProcessor 后置处理器，容器中不存在的话则会创建，并存放到容器中**。beanFactory.getBean 方法非常重要，也非常复杂，此处就不细讲了，以后有机会会详细地说一遍 getBean 方法的整体流程。将创建出来的 ConfigurationClassPostProcessor 后置处理器放到 currentRegistryProcessors 临时的集合中。
接着执行 invokeBeanDefinitionRegistryPostProcessors(currentRegistryProcessors, registry, beanFactory.getApplicationStartup()); 方法 =↓
```java
private static void invokeBeanDefinitionRegistryPostProcessors(  
      Collection<? extends BeanDefinitionRegistryPostProcessor> postProcessors, BeanDefinitionRegistry registry, ApplicationStartup applicationStartup) {  
   for (BeanDefinitionRegistryPostProcessor postProcessor : postProcessors) {  
      StartupStep postProcessBeanDefRegistry = applicationStartup.start("spring.context.beandef-registry.post-process")  
            .tag("postProcessor", postProcessor::toString);  
      postProcessor.postProcessBeanDefinitionRegistry(registry);  
      postProcessBeanDefRegistry.end();  
   }  
}
```
该方法循环遍历刚才用来存放 ConfigurationClassPostProcessor 后置处理器的临时集合，依次执行每个后置处理器中 postProcessBeanDefinitionRegistry 方法。此时，我们将目光转移到 ConfigurationClassPostProcessor 后置处理器中的 postProcessBeanDefinitionRegistry 方法 =↓
```java
public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) {  
   int registryId = System.identityHashCode(registry);  
   if (this.registriesPostProcessed.contains(registryId)) {  
      throw new IllegalStateException(  
            "postProcessBeanDefinitionRegistry already called on this post-processor against " + registry);  
   }  
   if (this.factoriesPostProcessed.contains(registryId)) {  
      throw new IllegalStateException(  
            "postProcessBeanFactory already called on this post-processor against " + registry);  
   }  
   this.registriesPostProcessed.add(registryId);  
  
   processConfigBeanDefinitions(registry);  
}
```
在此方法中有一个关键性方法 processConfigBeanDefinitions(registry); 从名字可以看出该方法用于处理配置的 bean 定义信息。该方法内容比较长，咱们分成几个部分来解读，第一部分代码如下 = ↓
```java
List<BeanDefinitionHolder> configCandidates = new ArrayList<>();
String[] candidateNames = registry.getBeanDefinitionNames();  
  
for (String beanName : candidateNames) {  
   BeanDefinition beanDef = registry.getBeanDefinition(beanName);  
   if (beanDef.getAttribute(ConfigurationClassUtils.CONFIGURATION_CLASS_ATTRIBUTE) != null) {  
      if (logger.isDebugEnabled()) {  
         logger.debug("Bean definition has already been processed as a configuration class: " + beanDef);  
      }  
   }   else if (ConfigurationClassUtils.checkConfigurationClassCandidate(beanDef, this.metadataReaderFactory)) {  
      configCandidates.add(new BeanDefinitionHolder(beanDef, beanName));  
   }  
}
```
从容器中获取 bean 定义信息名称将其放到 candidateNames 数组中，循环遍历 candidateNames 数组，根据 beanName 去容器中获取相应的 bean 定义信息。调用 ConfigurationClassUtils 工具类的静态方法 checkConfigurationClassCandidate 来判断 bean 定义信息的元数据中是否存在 @Configuration 注解，如果存在，则将该 bean 定义信息放到 configCandidates 集合中。
![|1000](../Attachments/Pasted%20image%2020220507224549.png)
Debug 可以看到在容器中存在 7 条 bean 定义信息，可是满足元数据中存在 @Configuration 注解的 bean 定义信息只有一条，那就是咱们的启动类 sampleWebFreeMarkerApplication。为什么启动类满足呢？我们回过头去可以看到在咱们的启动类上方有一个 @SpringBootApplication 注解，该注解是一个组合注解，它由 @SpringBootConfiguration 、@EnableAutoConfiguration、@ComponentScan 三个注解组成。
```java
@SpringBootConfiguration  
@EnableAutoConfiguration  
@ComponentScan(excludeFilters = { @Filter(type = FilterType.CUSTOM, classes = TypeExcludeFilter.class),  
      @Filter(type = FilterType.CUSTOM, classes = AutoConfigurationExcludeFilter.class) })  
public @interface SpringBootApplication {
```
其中， @SpringBootConfiguration -> @Configuration -> @Component，@SpringBootConfiguration 注解底层由 @Configuration 构成，而 @Configuration 注解又由 @Component 注解构成，所以将 @SpringBootConfiguration 注解标注在启动类上说明启动类是一个配置类，这也就说明为什么启动类满足条件。至于后面两个注解 @EnableAutoConfiguration 和 @ComponentScan 后面会一一道来，别急。
第一部分代码分析结束之后，开始第二部分代码的分析 = ↓
```java
// Parse each @Configuration class  
ConfigurationClassParser parser = new ConfigurationClassParser(  
      this.metadataReaderFactory, this.problemReporter, this.environment,  
      this.resourceLoader, this.componentScanBeanNameGenerator, registry);  
  
Set<BeanDefinitionHolder> candidates = new LinkedHashSet<>(configCandidates);  
Set<ConfigurationClass> alreadyParsed = new HashSet<>(configCandidates.size());  
do {  
   StartupStep processConfig = this.applicationStartup.start("spring.context.config-classes.parse");  
   parser.parse(candidates);  
   parser.validate();  
  
   Set<ConfigurationClass> configClasses = new LinkedHashSet<>(parser.getConfigurationClasses());  
   configClasses.removeAll(alreadyParsed);  
  
   // Read the model and create bean definitions based on its content  
   if (this.reader == null) {  
      this.reader = new ConfigurationClassBeanDefinitionReader(  
            registry, this.sourceExtractor, this.resourceLoader, this.environment,  
            this.importBeanNameGenerator, parser.getImportRegistry());  
   }  
   this.reader.loadBeanDefinitions(configClasses);  
   alreadyParsed.addAll(configClasses);  
   processConfig.tag("classCount", () -> String.valueOf(configClasses.size())).end();  
  
   candidates.clear();  
   if (registry.getBeanDefinitionCount() > candidateNames.length) {  
      String[] newCandidateNames = registry.getBeanDefinitionNames();  
      Set<String> oldCandidateNames = new HashSet<>(Arrays.asList(candidateNames));  
      Set<String> alreadyParsedClasses = new HashSet<>();  
      for (ConfigurationClass configurationClass : alreadyParsed) {  
         alreadyParsedClasses.add(configurationClass.getMetadata().getClassName());  
      }  
      for (String candidateName : newCandidateNames) {  
         if (!oldCandidateNames.contains(candidateName)) {  
            BeanDefinition bd = registry.getBeanDefinition(candidateName);  
            if (ConfigurationClassUtils.checkConfigurationClassCandidate(bd, this.metadataReaderFactory) &&  
                  !alreadyParsedClasses.contains(bd.getBeanClassName())) {  
               candidates.add(new BeanDefinitionHolder(bd, candidateName));  
            }  
         }      }      candidateNames = newCandidateNames;  
   }  
}  
while (!candidates.isEmpty());
```
在该部分代码中，首先创建了一个 ConfigurationClassParser 类的实例，看名字以及注释可以知道该解析器专门用来解析每一个配置类。将第一部分获取出来的启动类 (配置类) 的 bean 定义信息放到 ConfigurationClassParser 中的 parse 方法中进行处理，该 parse 方法经过重载的 parse 方法，在该方法中有一段代码 this.deferredImportSelectorHandler.process(); 会在后续处理完注解信息之后回到这里进行执行，此处也不过多描述，放到后面处理 @Import 注解连贯起来讲，这里给大家混个眼熟。
```java
public void parse(Set<BeanDefinitionHolder> configCandidates) {  
   for (BeanDefinitionHolder holder : configCandidates) {  
      BeanDefinition bd = holder.getBeanDefinition();  
      try {  
         if (bd instanceof AnnotatedBeanDefinition) {  
            parse(((AnnotatedBeanDefinition) bd).getMetadata(), holder.getBeanName());  
         }  
         else if (bd instanceof AbstractBeanDefinition && ((AbstractBeanDefinition) bd).hasBeanClass()) {  
            parse(((AbstractBeanDefinition) bd).getBeanClass(), holder.getBeanName());  
         }  
         else {  
            parse(bd.getBeanClassName(), holder.getBeanName());  
         }  
      }  
      catch (BeanDefinitionStoreException ex) {  
         throw ex;  
      }  
      catch (Throwable ex) {  
         throw new BeanDefinitionStoreException(  
               "Failed to parse configuration class [" + bd.getBeanClassName() + "]", ex);  
      }  
   }  
  
   this.deferredImportSelectorHandler.process();  
}

protected final void parse(AnnotationMetadata metadata, String beanName) throws IOException {  
   processConfigurationClass(new ConfigurationClass(metadata, beanName), DEFAULT_EXCLUSION_FILTER);  
}
```
最终会调用到该类中的 processConfigurationClass 方法，该方法看样子就知道是专门用来处理配置类信息的 = ↓
```java
protected void processConfigurationClass(ConfigurationClass configClass, Predicate<String> filter) throws IOException {  
   if (this.conditionEvaluator.shouldSkip(configClass.getMetadata(), ConfigurationPhase.PARSE_CONFIGURATION)) {  
      return;  
   }  
  
   ConfigurationClass existingClass = this.configurationClasses.get(configClass);  
   if (existingClass != null) {  
      if (configClass.isImported()) {  
         if (existingClass.isImported()) {  
            existingClass.mergeImportedBy(configClass);  
         }  
         // Otherwise ignore new imported config class; existing non-imported class overrides it.  
         return;  
      }  
      else {  
         // Explicit bean definition found, probably replacing an import.  
         // Let's remove the old one and go with the new one.         this.configurationClasses.remove(configClass);  
         this.knownSuperclasses.values().removeIf(configClass::equals);  
      }  
   }  
  
   // Recursively process the configuration class and its superclass hierarchy.  
   SourceClass sourceClass = asSourceClass(configClass, filter);  
   do {  
      sourceClass = doProcessConfigurationClass(configClass, sourceClass, filter);  
   }  
   while (sourceClass != null);  
  
   this.configurationClasses.put(configClass, configClass);  
}
```
该方法首先**会调用 shouldSkip 方法，该方法会根据配置类上的 @Conditional 注解判断该配置类是否应该跳过，如果@Confitional 注解条件不满足，则会直接跳过，不会处理该配置类**。接着再判断该配置类是否存在 configurationClasses 集合中，如果存在则表示已经解析过该配置类，直接忽略返回或者被覆盖掉重新走一遍解析流程；如果不存在的话，则开始递归处理配置类，调用 doProcessConfigurationClass 方法 = ↓
```java
protected final SourceClass doProcessConfigurationClass(  
      ConfigurationClass configClass, SourceClass sourceClass, Predicate<String> filter)  
      throws IOException {  
  
   if (configClass.getMetadata().isAnnotated(Component.class.getName())) {  
      // Recursively process any member (nested) classes first  
      processMemberClasses(configClass, sourceClass, filter);  
   }  
  
   // Process any @PropertySource annotations  
   for (AnnotationAttributes propertySource : AnnotationConfigUtils.attributesForRepeatable(  
         sourceClass.getMetadata(), PropertySources.class,  
         org.springframework.context.annotation.PropertySource.class)) {  
      if (this.environment instanceof ConfigurableEnvironment) {  
         processPropertySource(propertySource);  
      }  
      else {  
         logger.info("Ignoring @PropertySource annotation on [" + sourceClass.getMetadata().getClassName() +  
               "]. Reason: Environment must implement ConfigurableEnvironment");  
      }  
   }  
  
   // Process any @ComponentScan annotations  
   Set<AnnotationAttributes> componentScans = AnnotationConfigUtils.attributesForRepeatable(  
         sourceClass.getMetadata(), ComponentScans.class, ComponentScan.class);  
   if (!componentScans.isEmpty() &&  
         !this.conditionEvaluator.shouldSkip(sourceClass.getMetadata(), ConfigurationPhase.REGISTER_BEAN)) {  
      for (AnnotationAttributes componentScan : componentScans) {  
         // The config class is annotated with @ComponentScan -> perform the scan immediately  
         Set<BeanDefinitionHolder> scannedBeanDefinitions =  
               this.componentScanParser.parse(componentScan, sourceClass.getMetadata().getClassName());  
         // Check the set of scanned definitions for any further config classes and parse recursively if needed  
         for (BeanDefinitionHolder holder : scannedBeanDefinitions) {  
            BeanDefinition bdCand = holder.getBeanDefinition().getOriginatingBeanDefinition();  
            if (bdCand == null) {  
               bdCand = holder.getBeanDefinition();  
            }  
            if (ConfigurationClassUtils.checkConfigurationClassCandidate(bdCand, this.metadataReaderFactory)) {  
               parse(bdCand.getBeanClassName(), holder.getBeanName());  
            }  
         }  
      }  
   }  
  
   // Process any @Import annotations  
   processImports(configClass, sourceClass, getImports(sourceClass), filter, true);  
  
   // Process any @ImportResource annotations  
   AnnotationAttributes importResource =  
         AnnotationConfigUtils.attributesFor(sourceClass.getMetadata(), ImportResource.class);  
   if (importResource != null) {  
      String[] resources = importResource.getStringArray("locations");  
      Class<? extends BeanDefinitionReader> readerClass = importResource.getClass("reader");  
      for (String resource : resources) {  
         String resolvedResource = this.environment.resolveRequiredPlaceholders(resource);  
         configClass.addImportedResource(resolvedResource, readerClass);  
      }  
   }  
  
   // Process individual @Bean methods  
   Set<MethodMetadata> beanMethods = retrieveBeanMethodMetadata(sourceClass);  
   for (MethodMetadata methodMetadata : beanMethods) {  
      configClass.addBeanMethod(new BeanMethod(methodMetadata, configClass));  
   }  
  
   // Process default methods on interfaces  
   processInterfaces(configClass, sourceClass);  
  
   // Process superclass, if any  
   if (sourceClass.getMetadata().hasSuperClass()) {  
      String superclass = sourceClass.getMetadata().getSuperClassName();  
      if (superclass != null && !superclass.startsWith("java") &&  
            !this.knownSuperclasses.containsKey(superclass)) {  
         this.knownSuperclasses.put(superclass, configClass);  
         // Superclass found, return its annotation metadata and recurse  
         return sourceClass.getSuperClass();  
      }  
   }  
  
   // No superclass -> processing is complete  
   return null;  
}
```
该方法里的注释写的很清楚，**用来处理配置类上标注的 @Component 、@PropertySource、@ComponentScan、@Import、@ImportResource、@Bean 注解**。这些注解有没有觉得很熟悉，为什么咱们平时往一个类上标注 @Component 注解，该类就能被放到 spring 容器中，没错，spring 就是在这一步进行处理的。
和本次主题相关的是 @ComponentScan 和 @Import 注解，咱们就来着重看下处理 @ComponentScan 和 @Import 注解部分的代码。
首先是**处理 @ComponentScan 注解**，先获取配置类上的 @ComponentScan 注解中的属性信息，根据注解中的属性信息开始使用 componentScanParser 中的 parse 方法开始扫描组件，并**将扫描到的组件的 bean 定义信息注册到 spring 容器中**。需要注意的是，如果 **@ComponentScan 注解没有明确配置 basePackage 属性，那么就会把该注解标注所在类的包当作 basePackage 属性的值**，此时我们解析的是启动类上的 @ComponentScan，即 basePackage = smoketest.freemarker，**扫描启动类所在包及其子包下的所有组件**，照咱们的例子只能扫描到一个 WelcomeController 组件。
![|1000](../Attachments/Pasted%20image%2020220507234243.png)
接下来是**处理 @Import 注解**，大家是否还记得上面提到的启动类上标注的 @SpringBootApplication 注解是一个组合注解，由三个注解组成，@SpringBootConfiguration 和 @ComponentScan 注解已经说过了，咱们就来看下最后一个注解 @EnableAutoConfiguration 注解，看名字就知道跟 Springboot 的自动配置有关，**开启自动配置功能** = ↓
```java
@AutoConfigurationPackage  
@Import(AutoConfigurationImportSelector.class)  
public @interface EnableAutoConfiguration {

@Import(AutoConfigurationPackages.Registrar.class)  
public @interface AutoConfigurationPackage {
```
该注解也是一个组合注解，其中的 @AutoConfigurationPackage 注解内部也是由一个 @Import 注解构成。咱们刚才 Debug 到用来处理 @Import 注解的方法 processImports(configClass, sourceClass, getImports(sourceClass), filter, true); 其中的 getImports(sourceClass) 方法是用来递归获取 配置类上 @Import 注解中的内容，即需要注册到 spring 容器中的 Class。
![|1000](../Attachments/Pasted%20image%2020220507235456.png)
计算得出两条记录，这两条记录不正是启动类上 @Import 注解中的内容嘛！
咱们来到 processImports 方法内部 = ↓
```java
private void processImports(ConfigurationClass configClass, SourceClass currentSourceClass,  
      Collection<SourceClass> importCandidates, Predicate<String> exclusionFilter,  
      boolean checkForCircularImports) {  
   for (SourceClass candidate : importCandidates) {  
        if (candidate.isAssignable(ImportSelector.class)) {  
           // Candidate class is an ImportSelector -> delegate to it to determine imports  
           Class<?> candidateClass = candidate.loadClass();  
           ImportSelector selector = ParserStrategyUtils.instantiateClass(candidateClass, ImportSelector.class,  
                 this.environment, this.resourceLoader, this.registry);  
           Predicate<String> selectorFilter = selector.getExclusionFilter();  
           if (selectorFilter != null) {  
              exclusionFilter = exclusionFilter.or(selectorFilter);  
           }  
           if (selector instanceof DeferredImportSelector) {  
              this.deferredImportSelectorHandler.handle(configClass, (DeferredImportSelector) selector);  
           }  
           else {  
              String[] importClassNames = selector.selectImports(currentSourceClass.getMetadata());  
              Collection<SourceClass> importSourceClasses = asSourceClasses(importClassNames, exclusionFilter);  
              processImports(configClass, currentSourceClass, importSourceClasses, exclusionFilter, false);  
           }  
        }  
        else if (candidate.isAssignable(ImportBeanDefinitionRegistrar.class)) {  
           // Candidate class is an ImportBeanDefinitionRegistrar ->  
           // delegate to it to register additional bean definitions               
           Class<?> candidateClass = candidate.loadClass();  
           ImportBeanDefinitionRegistrar registrar =  
                 ParserStrategyUtils.instantiateClass(candidateClass, ImportBeanDefinitionRegistrar.class,  
                       this.environment, this.resourceLoader, this.registry);  
           configClass.addImportBeanDefinitionRegistrar(registrar, currentSourceClass.getMetadata());  
        }  
        else {  
           // Candidate class not an ImportSelector or ImportBeanDefinitionRegistrar ->  
           // process it as an @Configuration class               
           this.importStack.registerImport(  
                 currentSourceClass.getMetadata(), candidate.getMetadata().getClassName());  
           processConfigurationClass(candidate.asConfigClass(configClass), exclusionFilter);  
        }  
    }
}
```
循环遍历想要注册到 spring 容器的 Class 集合 ，首先判断该类是否实现 ImportSelector 接口，集合中的一条记录 AutoConfigurationImportSelector 类，AutoConfigurationImportSelector -> DeferredImportSelector -> ImportSelector，AutoConfigurationImportSelector 实现了 DeferredImportSelector 接口，而 DeferredImportSelector 接口继承自 ImportSelector 接口，所以满足条件。然后接着判断是否实现 DeferredImportSelector 接口，如果实现了 DeferredImportSelector 接口的话，则将其添加 deferredImportSelectors 集合中，后续会使用到；如果没有实现 DeferredImportSelector 接口的话，则会执行接口中的 selectImports 方法。而 AutoConfigurationImportSelector 刚好也实现了 DeferredImportSelector 接口，所以被添加到 deferredImportSelectors 集合中。集合中另外一条记录在此就不再过多描述，相信看到这大家有能力自己搞定。
回到 ConfigurationClassParser 类中重载的 parse 方法，在前面咱们提过一嘴，执行 this.deferredImportSelectorHandler.process(); 方法，此处代码不好从正面跟踪，咱们直接在 AutoConfigurationImportSelector 类中的 getAutoConfigurationEntry 方法中打一个断点 (这是因为我已经知道结果，为了不贴大量代码以及方便描述，就直接这样做了)，看一下方法调用栈情况，看一下经过了哪些方法来到这里。
![|800](../Attachments/Pasted%20image%2020220508010802.png)
好，现在咱们的程序来到 AutoConfigurationImportSelector 类中的 getAutoConfigurationEntry 方法 = ↓
```java
protected AutoConfigurationEntry getAutoConfigurationEntry(AnnotationMetadata annotationMetadata) {  
   if (!isEnabled(annotationMetadata)) {  
      return EMPTY_ENTRY;  
   }  
   AnnotationAttributes attributes = getAttributes(annotationMetadata);  
   List<String> configurations = getCandidateConfigurations(annotationMetadata, attributes);  
   configurations = removeDuplicates(configurations);  
   Set<String> exclusions = getExclusions(annotationMetadata, attributes);  
   checkExcludedClasses(configurations, exclusions);  
   configurations.removeAll(exclusions);  
   configurations = getConfigurationClassFilter().filter(configurations);  
   fireAutoConfigurationImportEvents(configurations, exclusions);  
   return new AutoConfigurationEntry(configurations, exclusions);  
}

protected List<String> getCandidateConfigurations(AnnotationMetadata metadata, AnnotationAttributes attributes) {  
   List<String> configurations = SpringFactoriesLoader.loadFactoryNames(getSpringFactoriesLoaderFactoryClass(),  
         getBeanClassLoader());  
   Assert.notEmpty(configurations, "No auto configuration classes found in META-INF/spring.factories. If you "  
         + "are using a custom packaging, make sure that file is correct.");  
   return configurations;  
}

protected Class<?> getSpringFactoriesLoaderFactoryClass() {  
   return EnableAutoConfiguration.class;  
}
```
在此方法中会调用 getCandidateConfigurations(annotationMetadata, attributes); 方法，而在 getCandidateConfigurations 方法中咱们是不是看到点熟悉的东西😮，SpringFactoriesLoader.loadFactoryNames() 方法呀，此方法在最上面提过是从 META-INF/spring.factories 文件中获取 type = EnableAutoConfiguration 类型所对应的值，这里获取到的就是需要注册到 spring 容器中的所有的自动配置类，让我们计算一下看看都有哪些
![|1000](../Attachments/Pasted%20image%2020220508012153.png)
在这里告诉你一个结论，并不是这 133 个自动配置类都会被注册到 spring 容器中，Springboot 可是按需配置的，会根据自动配置类上的 @Conditional 注解进行过滤筛选。
![|1000](../Attachments/Pasted%20image%2020220508012929.png)
接下来返回到上图的方法中 = ↓
```java
public void processGroupImports() {  
   for (DeferredImportSelectorGrouping grouping : this.groupings.values()) {  
      Predicate<String> exclusionFilter = grouping.getCandidateFilter();  
      grouping.getImports().forEach(entry -> {  
         ConfigurationClass configurationClass = this.configurationClasses.get(entry.getMetadata());  
         try {  
            processImports(configurationClass, asSourceClass(configurationClass, exclusionFilter),  
                  Collections.singleton(asSourceClass(entry.getImportClassName(), exclusionFilter)),  
                  exclusionFilter, false);  
         }  
         catch (BeanDefinitionStoreException ex) {  
            throw ex;  
         }  
         catch (Throwable ex) {  
            throw new BeanDefinitionStoreException(  
                  "Failed to process import candidates for configuration class [" +  
                        configurationClass.getMetadata().getClassName() + "]", ex);  
         }  
      });  
   }  
}
```
把获取到的自动配置类 xxxAutoConfiguration 当成配置类，调用 processImports 方法开始新一轮的解析配置类的流程。
程序回到 ConfigurationClassPostProcessor 类的 processConfigBeanDefinitions 方法，执行完 parse 方法之后，开始执行 this.reader.loadBeanDefinitions(configClasses); 方法，该方法用于将配置类的 bean 定义信息注册到 spring 容器中。
在执行该方法之前，咱们来看看几个重要的变量。
① beanDefinitionMap，熟悉的小伙伴应该知道这是存放 bean 定义信息的集合，看下图发现此时还没有自动配置类的 bean 定义信息。
![|1200](../Attachments/Pasted%20image%2020220508014003.png)② configClasses => 配置类集合，此集合包含启动类 (SampleWebFreeMarkerApplication)、扫描的组件 (WelcomeController) 以及获取到的自动配置类 (xxxAutoConfiguration)
![|1200](../Attachments/Pasted%20image%2020220508014452.png) 执行完该方法之后，再次查看 beanDefinitionMap 变量，发现集合中的数量变多了，说明已经将自动配置类中的组件的 bean 定义信息添加到该集合中了。
![|1200](../Attachments/Pasted%20image%2020220508014914.png) 至于这个 this.reader.loadBeanDefinitions(configClasses); 怎么运行的，有兴趣的小伙伴可以自己看一下，这里我将其作为一个 ==TODO==，以后有机会分析一下该方法。
bean 定义信息都已经存在 spring 容器中，至于怎么将 bean 定义信息转换成单例对象的，相信自己看过 spring 源码的小伙伴都知道在 AbstractApplicationContext 类中的 refresh 方法中，有 13 个方法，其中最为关键的一个方法是 finishBeanFactoryInitialization(beanFactory); 方法，该方法就是用来将 bean 定义信息转换成单例对象然后存放在 spring 容器中的。这里我也将其作为一个 ==TODO==，以后有机会分析一下该方法。
至此，Springboot 的自动配置原理就已经全部分析完毕🥳，篇幅是有点长，毕竟花了我两天的时间🥺，对于其中有哪些讲的不好的地方或者错误的地方，小伙伴可以自己去 Debug 调试一下，可以把这篇文章当作一个辅助工具。学习源码一定要学习 Debug，不然看完之后还是我的，而不是你的。学习源码肯定痛苦，但是熬过来之后会发现自己收获满满，加油，骚年！💪
