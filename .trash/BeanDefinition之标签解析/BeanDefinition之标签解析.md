---
title:
tags: 
created: 2022-09-23 15:53:11
modified: 2022-09-24 15:01:14
number headings: auto, first-level 1, max 6, _.1.1.
---

## 1. 默认标签解析

## 2. 自定义标签解析

```java
public BeanDefinition parseCustomElement(Element ele, @Nullable BeanDefinition containingBd) {  
   /**  
    * 获取标签元素的命名空间 uri，  
    * 例如:  
    * AOP 的命名空间 uri 为："http://www.springframework.org/schema/aop"  
    * 事务 的命名空间 uri 为："http://www.springframework.org/schema/tx"  
    * 包扫描的命名空间 uri 为："http://www.springframework.org/schema/context"  
    */   
   String namespaceUri = getNamespaceURI(ele);  
   if (namespaceUri == null) {  
      return null;  
   }  
  
   // 基于 META-INFO/spring.handlers 配置文件，获取命名空间 uri 对应的命名空间处理器  
   NamespaceHandler handler = this.readerContext.getNamespaceHandlerResolver().resolve(namespaceUri);  
   if (handler == null) {  
      error("Unable to locate Spring NamespaceHandler for XML schema namespace [" + namespaceUri + "]", ele);  
      return null;   }  
   // 解析标签元素  
   return handler.parse(ele, new ParserContext(this.readerContext, this, containingBd));  
}
```

1. 根据当前标签找出对应的命名空间 uri
	- `<aop:aspectj-autoproxy />` 标签对应的命名空间 uri："http://www.springframework.org/schema/aop"
	- `<context: component-scan />` 标签对应的命名空间 uri："http://www.springframework.org/schema/context"  
2. 加载所有 `META-INF` 目录下的 `spring.handlers` 配置文件，建立命名空间 uri 与命名空间处理器的映射关系  
![](attachments/Pasted%20image%2020220923161016.png)  
![](attachments/Pasted%20image%2020220923160852.png)
3. 通过映射关系获取命名空间处理器的全限定类名，如 `org.springframework.aop.config.AopNamespaceHandler`
4. 反射创建该命名空间处理器的实例对象

```java
NamespaceHandler namespaceHandler = (NamespaceHandler) BeanUtils.instantiateClass(handlerClass);
```

5. 初始化该命名空间处理器，以 `AopNamespaceHandler` 为例，用于建立某标签元素和对应 bean 定义信息解析器的映射关系。

```java
public class AopNamespaceHandler extends NamespaceHandlerSupport {  
  
   /**  
    * Register the {@link BeanDefinitionParser BeanDefinitionParsers} for the  
    * '{@code config}', '{@code spring-configured}', '{@code aspectj-autoproxy}'  
    * and '{@code scoped-proxy}' tags.  
    */   
    @Override  
   public void init() {  
      // In 2.0 XSD as well as in 2.5+ XSDs  
      registerBeanDefinitionParser("config", new ConfigBeanDefinitionParser());  
  
      // 处理标签元素 aop:aspectj-autoproxy，注册 AspectJAutoProxyBeanDefinitionParser，是一个专门解析 BeanDefinition 的解析器  
      registerBeanDefinitionParser("aspectj-autoproxy", new AspectJAutoProxyBeanDefinitionParser());  
  
      registerBeanDefinitionDecorator("scoped-proxy", new ScopedProxyBeanDefinitionDecorator());  
  
      // Only in 2.0 XSD: moved to context namespace in 2.5+  
      registerBeanDefinitionParser("spring-configured", new SpringConfiguredBeanDefinitionParser());  
   }  
  
}
```

6. 找到该标签所对应的 bean 定义信息解析器，开始解析该标签

```java
public BeanDefinition parse(Element element, ParserContext parserContext) {  
   // 为标签元素匹配合适的解析器  
   BeanDefinitionParser parser = findParserForElement(element, parserContext);  
   // 解析标签元素  
   return (parser != null ? parser.parse(element, parserContext) : null);  
}

private BeanDefinitionParser findParserForElement(Element element, ParserContext parserContext) {  
   // 获取标签名称，例如 AOP 相关的：aop:aspectj-autoproxy  
   String localName = parserContext.getDelegate().getLocalName(element);  
   // 从解析器 Map 中，根据标签名称获取之前注册的解析器，例如 AOP 相关的：AspectJAutoProxyBeanDefinitionParser  
   BeanDefinitionParser parser = this.parsers.get(localName);  
   if (parser == null) {  
      parserContext.getReaderContext().fatal(  
            "Cannot locate BeanDefinitionParser for element [" + localName + "]", element);  
   }  
   return parser;  
}
```
