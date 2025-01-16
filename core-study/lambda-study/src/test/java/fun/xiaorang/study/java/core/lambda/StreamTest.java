package fun.xiaorang.study.java.core.lambda;

import fun.xiaorang.study.java.core.lambda.pojo.User;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * @author xiaorang
 * @description <p style = " font-weight:bold ; "><p/>
 * @github <a href="https://github.com/xihuanxiaorang/java-study">java-study</a>
 * @Copyright 博客：<a href="https://docs.xiaorang.fun">小让的糖果屋</a>  - show me the code
 * @date 2025/01/16 18:36
 */
public class StreamTest {
  @Test
  public void createStreamTest() {
    final User user1 = new User(1, "张三", 20, "123456", "zs@qq.com");
    final User user2 = new User(2, "李四", 21, "123456", "ls@qq.com");
    final User user3 = new User(3, "王五", 22, "123456", "ww@qq.com");
    final User user4 = new User(4, "赵六", 22, "123456", "zl@qq.com");
    final User user5 = new User(5, "钱七", 25, "123456", "q7@qq.com");

    // 从List集合创建Stream流 useListToStream
    final List<User> userList = List.of(user1, user2, user3, user4, user5);
    final Stream<User> useListToStream = userList.stream();

    //从Map集合创建Stream流 useMapEntrySetToStream
    final Map<Integer, User> userMap = Map.of(user1.getId(), user1, user2.getId(), user2, user3.getId(), user3, user4.getId(), user4, user5.getId(), user5);
    final Stream<Map.Entry<Integer, User>> useMapEntrySetToStream = userMap.entrySet().stream();

    // 从数组创建Stream流 useArrayToStream
    User[] userArray = {user1, user2, user3, user4, user5};
    final Stream<User> useArrayToStream = Arrays.stream(userArray);

    // 使用Stream.of()创建Stream流 useStreamOfToStream
    final Stream<User> useStreamOfToStream = Stream.of(user1, user2, user3, user4, user5);

    // 使用Stream.builder()创建Stream流 useStreamBuilderToStream
    final Stream.Builder<User> builder = Stream.builder();
    builder.add(user1).add(user2).add(user3).add(user4).add(user5);
    final Stream<User> useStreamBuilderToStream = builder.build();
    useStreamBuilderToStream.forEach(System.out::println);

    System.out.println("===========================");

    // 从文件创建Stream流 useFileToStream
    final Path path = Paths.get("src", "test", "resources", "stream.txt");
    try (Stream<String> useFileToStream = Files.lines(path)) {
      useFileToStream.forEach(System.out::println);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    System.out.println("===========================");

    // 从正则表达式创建Stream流 useRegexToStream
    String str = "张三,李四,王五,赵六,钱七";
    String regex = ",";
    final Pattern pattern = Pattern.compile(regex);
    try (Stream<String> useRegexToStream = pattern.splitAsStream(str)) {
      useRegexToStream.forEach(System.out::println);
    }

    System.out.println("===========================");

    // 使用Stream.iterate()创建Stream流 useIterateToStream
    try (Stream<Integer> useIterateToStream = Stream.iterate(1, n -> n + 1).limit(10)) {
      useIterateToStream.forEach(System.out::println);
    }

    System.out.println("===========================");

    // 使用Stream.generate()创建Stream流 useGenerateToStream
    try (Stream<Integer> useGenerateToStream = Stream.generate(() -> new Random().nextInt(10)).limit(5)) {
      useGenerateToStream.forEach(System.out::println);
    }
  }

  @Test
  public void filterStreamTest() {
    final User user1 = new User(1, "张三", 20, "123456", "zs@qq.com");
    final User user2 = new User(2, "李四", 21, "123456", null);
    final User user3 = new User(3, "王五", 22, "123456", "");
    final User user4 = new User(4, "赵六", 22, "123456", "zl@qq.com");
    final User user5 = new User(5, "钱七", 25, "123456", "q7@qq.com");

    // 从List集合创建Stream流 useListToStream
    final List<User> userList = List.of(user1, user2, user3, user4, user5);
    final Stream<User> useListToStream = userList.stream();

    // filter过滤1：筛选年龄在22（含）以上的用户 filterUserList1
  /*final List<User> filterUserList1 = useListToStream.filter(user -> user.getAge() >= 22).collect(Collectors.toList());
  filterUserList1.forEach(System.out::println);*/

    // filter过滤2：筛选填写过邮箱的用户 filterUserList2
  /*final List<User> filterUserList2 = useListToStream.filter(user -> user.getEmail() != null && !user.getEmail().isEmpty()).collect(Collectors.toList());
  filterUserList2.forEach(System.out::println);*/


    // filter过滤3：自定义方法，筛选填写过邮箱的用户 filterUserList3
    final List<User> filterUserList3 = useListToStream.filter(user -> checkUserEmail(user.getEmail())).collect(Collectors.toList());
    filterUserList3.forEach(System.out::println);
  }

  @Test
  public void distinctStreamTest() {
    // distinct去重1：基本数据类型去重
    final Stream<Integer> integerStream = Stream.of(1, 1, 2, 3, 4, 4, 5);
    integerStream.distinct().forEach(System.out::println);
    System.out.println("================================");
    final Stream<String> stringStream = Stream.of("Xiaomi", "Apple", "Huawei", "Lenovo", "Apple");
    stringStream.distinct().forEach(System.out::println);
    System.out.println("================================");

    // distinct去重2：自定义对象去重，将重复的User对象去除
    final User user1 = new User(1, "张三", 20, "123456", "zs@qq.com");
    final User user2 = new User(2, "李四", 21, "123456", "ls@qq.com");
    final User user3 = new User(3, "王五", 22, "123456", "ww@qq.com");
    final User user4 = new User(4, "赵六", 22, "123456", "zl@qq.com");
    final User user5 = new User(5, "钱七", 25, "123456", "q7@qq.com");
    final User user6 = new User(5, "钱七", 25, "123456", "q8@qq.com");
    // 从List集合创建Stream流 useListToStream
    final List<User> userList = List.of(user1, user2, user3, user4, user5, user6);
    final Stream<User> useListToStream = userList.stream();
    useListToStream.distinct().forEach(System.out::println);
  }

  @Test
  public void limitStreamTest() {
    final User user1 = new User(1, "张三", 20, "123456", "zs@qq.com");
    final User user2 = new User(2, "李四", 21, "123456", null);
    final User user3 = new User(3, "王五", 22, "123456", "");
    final User user4 = new User(4, "赵六", 22, "123456", "zl@qq.com");
    final User user5 = new User(5, "钱七", 25, "123456", "q7@qq.com");

    // 从List集合创建Stream流 useListToStream
    final List<User> userList = List.of(user1, user2, user3, user4, user5);
    final Stream<User> useListToStream = userList.stream();

    // limit截取：前三个用户
    useListToStream.limit(3).forEach(System.out::println);
  }

  @Test
  public void skipStreamTest() {
    final User user1 = new User(1, "张三", 20, "123456", "zs@qq.com");
    final User user2 = new User(2, "李四", 21, "123456", null);
    final User user3 = new User(3, "王五", 22, "123456", "");
    final User user4 = new User(4, "赵六", 22, "123456", "zl@qq.com");
    final User user5 = new User(5, "钱七", 25, "123456", "q7@qq.com");

    // 从List集合创建Stream流 useListToStream
    final List<User> userList = List.of(user1, user2, user3, user4, user5);
    final Stream<User> useListToStream = userList.stream();

    // skip跳过：跳过年龄最小的用户
    useListToStream.sorted(Comparator.comparingInt(User::getAge)).skip(1).forEach(System.out::println);
  }

  @Test
  public void sortedStreamTest() {
    // sorted排序1：基本数据类型排序
    final Stream<Integer> integerStream = Stream.of(4, 2, 3, 5, 1);
    integerStream.sorted().forEach(System.out::println);
    System.out.println("================================");
    final Stream<String> stringStream = Stream.of("apple", "lenovo", "Huawei", "Xiaomi", "Lenovo");
    stringStream.sorted().forEach(System.out::println);
    System.out.println("================================");

    // sorted排序2：自定义对象排序，按照年龄从大到小排序
    final User user1 = new User(1, "张三", 20, "123456", "zs@qq.com");
    final User user2 = new User(2, "李四", 21, "123456", "ls@qq.com");
    final User user3 = new User(3, "王五", 22, "123456", "ww@qq.com");
    final User user4 = new User(4, "赵六", 23, "123456", "zl@qq.com");
    final User user5 = new User(5, "钱七", 24, "123456", "q7@qq.com");
    // 从List集合创建Stream流 useListToStream
    final List<User> userList = List.of(user1, user2, user3, user4, user5);
    final Stream<User> useListToStream = userList.stream();
    /*useListToStream.sorted().forEach(System.out::println);*/
    /*useListToStream.sorted(Comparator.comparingInt(User::getAge)).forEach(System.out::println); // 升序*/
    useListToStream.sorted(Comparator.comparingInt(User::getAge).reversed()).forEach(System.out::println); // 降序
  }

  @Test
  public void mapStreamTest() {
    final User user1 = new User(1, "张三", 20, "123456", "zs@qq.com");
    final User user2 = new User(2, "李四", 21, "123456", null);
    final User user3 = new User(3, "王五", 22, "123456", "");
    final User user4 = new User(4, "赵六", 22, "123456", "zl@qq.com");
    final User user5 = new User(5, "钱七", 25, "123456", "q7@qq.com");

    // 从List集合创建Stream流 useListToStream
    final List<User> userList = List.of(user1, user2, user3, user4, user5);
    final Stream<User> useListToStream = userList.stream();

    // map映射1：将邮箱的域名更改为 "@sina.com"
  /*final List<String> mapUserList1 = useListToStream.map(user -> user.getEmail().replace("@qq.com", "@sina.com")).collect(Collectors.toList());
    mapUserList1.forEach(System.out::println);*/

    // map映射2：将邮箱的域名更改为 "@sina.com"，返回user集合
  /*final List<User> mapUserList2 = useListToStream.map(user -> {
      user.setEmail(user.getEmail().replace("@qq.com", "@sina.com"));
      return user;
    }).collect(Collectors.toList());
    mapUserList2.forEach(System.out::println);*/

    // map映射3：筛选填写过邮箱的用户，将邮箱的域名更改为 "@sina.com"
    final List<User> mapUserList3 = useListToStream
            .filter(user -> checkUserEmail(user.getEmail()))
            .map(user -> {
              user.setEmail(user.getEmail().replace("@qq.com", "@sina.com"));
              return user;
            }).collect(Collectors.toList());
    mapUserList3.forEach(System.out::println);
  }

  @Test
  public void flatMapStreamTest() {
    final User user1 = new User(1, "张三", 20, "123456", "zs@qq.com");
    final User user2 = new User(2, "李四", 21, "123456", "ls@qq.com");
    final User user3 = new User(3, "王五", 22, "123456", "ww@qq.com");
    final User user4 = new User(4, "赵六", 23, "123456", "zl@qq.com");
    final User user5 = new User(5, "钱七", 24, "123456", "q7@qq.com");

    // 从List集合创建Stream流 useListToStream
    final List<User> userList1 = List.of(user1, user2);
    final List<User> userList2 = List.of(user3, user4);
    final List<User> userList3 = List.of(user5);
    final List<List<User>> userList = List.of(userList1, userList2, userList3);
    final Stream<List<User>> useListToStream = userList.stream();

    // flatMap扁平化：将集合 userList1、userList2、userList3 中超过20岁的用户重新组合到一个新的集合中
    final List<User> collect = useListToStream.flatMap(List::stream).filter(user -> user.getAge() > 20).collect(Collectors.toList());
    collect.forEach(System.out::println);
  }

  @Test
  public void flatMapStreamTest2() {
    Stream.of("Hello", "World")
            .map(s -> s.split(""))  // 转换成['H','e','l','l','o'],['W','o','r','l','d'] 两个数组
            .flatMap(Arrays::stream) // 将两个数组扁平化成为 ['H','e','l','l','o','W','o','r','l','d'] 一个数组
            .distinct() // 去除重复元素
            .forEach(System.out::print); // 打印结果：HeloWrd
  }

  @Test
  public void peekStreamTest() {
    final User user1 = new User(1, "张三", 20, "123456", "zs@qq.com");
    final User user2 = new User(2, "李四", 21, "123456", null);
    final User user3 = new User(3, "王五", 22, "123456", "");
    final User user4 = new User(4, "赵六", 22, "123456", "zl@qq.com");
    final User user5 = new User(5, "钱七", 25, "123456", "q7@qq.com");

    // 从List集合创建Stream流 useListToStream
    final List<User> userList = List.of(user1, user2, user3, user4, user5);
    final Stream<User> useListToStream = userList.stream();

    // peek遍历1：将集合userList中的用户打印出来
    /*useListToStream.peek(System.out::println).collect(Collectors.toList());*/

    // peek遍历2：将集合userList中未填写邮件的用户打印出来
    useListToStream.filter(user -> !checkUserEmail(user.getEmail())).peek(System.out::println).collect(Collectors.toList());
  }

  @Test
  public void forEachStreamTest() {
    final User user1 = new User(1, "张三", 20, "123456", "zs@qq.com");
    final User user2 = new User(2, "李四", 21, "123456", "ls@qq.com");
    final User user3 = new User(3, "王五", 22, "123456", "ww@qq.com");
    final User user4 = new User(4, "赵六", 23, "123456", "zl@qq.com");
    final User user5 = new User(5, "钱七", 24, "123456", "q7@qq.com");

    // 从List集合创建Stream流 useListToStream
    final List<User> userList = List.of(user1, user2, user3, user4, user5);
    // final Stream<User> useListToStream = userList.stream(); // 串行流
    final Stream<User> useListToStream = userList.parallelStream(); // 并行流

    // forEach遍历
    // useListToStream.forEach(System.out::println);
    // forEachOrdered遍历
    useListToStream.forEachOrdered(System.out::println);
  }

  @Test
  public void collectStreamTest() {
    final User user1 = new User(1, "张三", 20, "123456", "zs@qq.com");
    final User user2 = new User(2, "李四", 21, "123456", "ls@qq.com");
    final User user3 = new User(3, "王五", 22, "123456", "ww@qq.com");
    final User user4 = new User(4, "赵六", 22, "123456", "zl@qq.com");
    final User user5 = new User(5, "钱七", 24, "123456", "q7@qq.com");
    final User user6 = new User(6, "钱七", 24, "123456", "q7@qq.com");

    // 从List集合创建Stream流 useListToStream
    final List<User> userList = List.of(user1, user2, user3, user4, user5, user6);
    final Stream<User> useListToStream = userList.stream(); // 串行流

    // 使用joining将用户的用户名连接起来
  /*final String str = useListToStream.map(User::getName).collect(Collectors.joining(", ", "[", "]"));
    System.out.println(str);*/

    // 统计用户数量
  /*final long count = useListToStream.collect(Collectors.counting());
    System.out.println(count);*/

    // 统计用户的年龄总和
  /*final Integer sumAge = useListToStream.collect(Collectors.summingInt(User::getAge));
    System.out.println(sumAge);*/

    // 统计用户的平均年龄
  /*final Double averageAge = useListToStream.collect(Collectors.averagingInt(User::getAge));
    System.out.println(averageAge);*/

    // 统计用户的最小年龄
  /*final Optional<User> optionalUser = useListToStream
            .collect(Collectors.minBy(Comparator.comparing(User::getAge)));
    optionalUser.ifPresent(user -> System.out.println(user.getAge()));*/

    // 统计用户的最大年龄
  /*useListToStream
            .collect(Collectors.maxBy(Comparator.comparing(User::getAge)))
            .ifPresent(user -> System.out.println(user.getAge()));*/

    // 对用户年龄的汇总统计
  /*final IntSummaryStatistics summaryStatistics = useListToStream.collect(Collectors.summarizingInt(User::getAge));
    System.out.println(summaryStatistics);*/

    // 根据用户年龄进行分组
  /*final Map<Integer, List<User>> collect = useListToStream.collect(Collectors.groupingBy(User::getAge));
    collect.forEach((k, v) -> System.out.println("年龄为 " + k + " 岁的有:" + v));*/
    // 根据用户年龄进行分组并取出id最大的用户
  /*final Map<Integer, Optional<User>> collect = useListToStream.collect(Collectors.groupingBy(User::getAge, Collectors.reducing(BinaryOperator.maxBy(Comparator.comparing(User::getId)))));
    collect.forEach((k, v) -> System.out.println("年龄为 " + k + " 岁的并且id最大的为:" + v.get()));*/

    // 根据用户年龄是否大于22进行分区
  /*final Map<Boolean, List<User>> collect = useListToStream.collect(Collectors.partitioningBy(user -> user.getAge() > 22));
    collect.forEach((k, v) -> {
      String str = k ? "年龄大于22岁的有:" : "年龄小于等于22岁的有:";
      System.out.println(str + v);
    });*/

    // 使用reducing实现求和的功能
  /*final Integer sumAge = useListToStream.collect(Collectors.reducing(0, User::getAge, Integer::sum));
    System.out.println(sumAge);*/

    // 使用reducing实现求最大值的功能
  /*useListToStream
            .collect(Collectors.reducing(BinaryOperator.maxBy(Comparator.comparing(User::getAge))))
            .ifPresent(user -> System.out.println(user.getAge()));*/

    // 将用户信息收集到Map集合中
    final Map<String, User> collect = useListToStream.collect(Collectors.toMap(User::getName, Function.identity(), (u1, u2) -> u2));
    collect.forEach((k, v) -> System.out.println(k + ":" + v));
  }

  @Test
  public void collectStreamTest2() {
    // 仿Collectors.toList()方法实现一个toList()
    final ArrayList<Integer> collect = Stream.of(1, 2, 3, 4).collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    System.out.println(collect); // [1, 2, 3, 4]

    // 仿Collectors.toMap()方法实现一个toMap()
    final User user1 = new User(1, "张三", 20, "123456", "zs@qq.com");
    final User user2 = new User(2, "李四", 21, "123456", "ls@qq.com");
    final User user3 = new User(3, "王五", 22, "123456", "ww@qq.com");
    final User user4 = new User(4, "赵六", 22, "123456", "zl@qq.com");
    final User user5 = new User(5, "钱七", 24, "123456", "q7@qq.com");
    final HashMap<String, Integer> collect1 = Stream.of(user1, user2, user3, user4, user5)
            .collect(HashMap::new,
                    (map, element) -> map.merge(element.getName(), element.getAge(), (e1, e2) -> e1), HashMap::putAll);
    System.out.println(collect1); // {钱七=24, 李四=21, 张三=20, 王五=22, 赵六=22}
  }

  @Test
  public void toArrayStreamTest() {
    final User user1 = new User(1, "张三", 20, "123456", "zs@qq.com");
    final User user2 = new User(2, "李四", 21, "123456", "ls@qq.com");
    final User user3 = new User(3, "王五", 22, "123456", "ww@qq.com");
    final User user4 = new User(4, "赵六", 22, "123456", "zl@qq.com");
    final User user5 = new User(5, "钱七", 24, "123456", "q7@qq.com");

    // 从List集合创建Stream流 useListToStream
    final List<User> userList = List.of(user1, user2, user3, user4, user5);
    final Stream<User> useListToStream = userList.stream();

    // 将Stream流中的元素收集到数组中
    final User[] users = useListToStream.toArray(User[]::new);
    for (final User user : users) {
      System.out.println(user);
    }
  }

  @Test
  public void findXxxStreamTest() {
    final User user1 = new User(1, "张三", 20, "123456", "zs@qq.com");
    final User user2 = new User(2, "李四", 21, "123456", "ls@qq.com");
    final User user3 = new User(3, "王五", 22, "123456", "ww@qq.com");
    final User user4 = new User(4, "赵六", 22, "123456", "zl@qq.com");
    final User user5 = new User(5, "钱七", 24, "123456", "q7@qq.com");

    // 从List集合创建Stream流 useListToStream
    final List<User> userList = List.of(user1, user2, user3, user4, user5);
    final Stream<User> useListToStream = userList.stream(); // 串行流

    // 查找第一个大于20岁的用户
    // useListToStream.filter(user -> user.getAge() > 20).findFirst().ifPresent(System.out::println);

    // 查找任意一个大于20岁的用户
    useListToStream.filter(user -> user.getAge() > 20).findAny().ifPresent(System.out::println);
  }

  @Test
  public void allAndAnyAndNoneMatchStreamTest() {
    final User user1 = new User(1, "张三", 20, "123456", "zs@qq.com");
    final User user2 = new User(2, "李四", 21, "123456", "ls@qq.com");
    final User user3 = new User(3, "王五", 22, "123456", "ww@qq.com");
    final User user4 = new User(4, "赵六", 22, "123456", "zl@qq.com");
    final User user5 = new User(5, "钱七", 24, "123456", "q7@qq.com");

    // 从List集合创建Stream流 useListToStream
    final List<User> userList = List.of(user1, user2, user3, user4, user5);
    final Stream<User> useListToStream = userList.stream();

    // 判断用户是否全部大于23岁？
  /*final boolean allMatch = useListToStream.allMatch(user -> user.getAge() > 23);
    System.out.println("allMatch = " + allMatch);*/

    // 判断是否存在超过23岁的用户？
    final boolean anyMatch = useListToStream.anyMatch(user -> user.getAge() > 23);
    System.out.println("anyMatch = " + anyMatch);
  }

  @Test
  public void maxAndMinAndCountAndSumStreamTest() {
    final Stream<Integer> stream = Stream.of(10, 20, 33, 6, 49);

    // max|min
    // stream.max(Integer::compareTo).ifPresent(System.out::println);
    // stream.min(Integer::compareTo).ifPresent(System.out::println);

    // count
    final long count = stream.count();
    System.out.println("count = " + count);

    final IntStream intStream = IntStream.of(10, 20, 33, 6, 49);
    final int sum = intStream.sum();
    System.out.println("sum = " + sum);
  }

  @Test
  public void reduceStreamTest() {
    System.out.println("=====================================1个参数（累加器）的版本=====================================");

    // 使用reduce实现求和
    Stream.of(1, 2, 3).reduce(Integer::sum).ifPresent(sum -> System.out.println("sum = " + sum));

    // 使用reduce求最大值
    Stream.of(3, 5, 11, 99, 12).reduce(Integer::max).ifPresent(max -> System.out.println("max = " + max));

    // 使用reduce实现字符串拼接
    Stream.of("Hello", "Java", "Stream", "!").reduce((s1, s2) -> s1 + " " + s2).ifPresent(str -> System.out.println("str = " + str));

    final User user1 = new User(1, "张三", 20, "123456", "zs@qq.com");
    final User user2 = new User(2, "李四", 21, "123456", "ls@qq.com");
    final User user3 = new User(3, "王五", 22, "123456", "ww@qq.com");
    final User user4 = new User(4, "赵六", 22, "123456", "zl@qq.com");
    final User user5 = new User(5, "钱七", 24, "123456", "q7@qq.com");

    // 从List集合创建Stream流 useListToStream
    final List<User> userList = List.of(user1, user2, user3, user4, user5);
    final Stream<User> useListToStream = userList.stream();

    // 求用户的最大年龄
    useListToStream.map(User::getAge).reduce(Integer::max).ifPresent(maxAge -> System.out.println("maxAge = " + maxAge));

    System.out.println("==================================2个参数（初始值，累加器）的版本==================================");

    // 使用reduce实现求和
    Integer identity = 10;
    final Integer sum = Stream.of(1, 2, 3, 4).reduce(identity, (n1, n2) -> {
      final String format = String.format("n1(%d) + n2(%d) = %d %s", n1, n2, n1 + n2, Thread.currentThread().getName());
      System.out.println(format);
      return n1 + n2;
    });
    System.out.println("sum = " + sum);
    System.out.println("----------------------------------");
    final Integer sum2 = Stream.of(1, 2, 3, 4).parallel().reduce(identity, (n1, n2) -> {
      final String format = String.format("n1(%d) + n2(%d) = %d %s", n1, n2, n1 + n2, Thread.currentThread().getName());
      System.out.println(format);
      return n1 + n2;
    });
    System.out.println("sum2 = " + sum2);

    System.out.println("================================3个参数（初始值，累加器，组合器）的版本================================");
    final Integer sum3 = Stream.of(1, 2, 3, 4).parallel().reduce(identity, (n1, n2) -> {
      final String format = String.format("累加器 n1(%d) + n2(%d) = %d %s", n1, n2, n1 + n2, Thread.currentThread().getName());
      System.out.println(format);
      return n1 + n2;
    }, (x, y) -> {
      final String format = String.format("组合器 n1(%d) + n2(%d) - identity(%d) = %d %s", x, y, identity, x + y - identity, Thread.currentThread().getName());
      System.out.println(format);
      return x + y - identity;
    });
    System.out.println("sum3 = " + sum3);
  }

  @Test
  public void parallelStreamTest() {
    // 串行流
    // final Stream<Integer> stream = Stream.of(1, 2, 3, 4, 5);
    // 并行流
    // final Stream<Integer> stream = Stream.of(1, 2, 3, 4, 5).parallel();
    final Stream<Integer> stream = Arrays.asList(1, 2, 3, 4, 5).parallelStream();

    // 流的线程信息
    stream.peek(integer -> System.out.println(integer + ":" + Thread.currentThread().getName())).collect(Collectors.toList());
  }

  private static Boolean checkUserEmail(String userEmail) {
    return userEmail != null && !userEmail.isEmpty();
  }

}

