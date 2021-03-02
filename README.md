# dtguai-cache
基于spring boot上的注解缓存。client 采用 lettuce <br>
@Cache轻量的缓存，支持单个缓存设置过期时间，可以根据前缀移除缓存。<br>
采用fastjson序列化与反序列化，以json串存于缓存之中。<br>
可以快速用于日常的spring boot应用之中。<br>

# 使用手册
## Maven依赖
```
    <dependency>
        <groupId>com.dtguai.cache</groupId>
        <artifactId>dtguai-cache</artifactId>
        <version>1.0.1</version>
    </dependency>
```
## 缓存配置
1、配置redis数据源，application.yml文件
```
#redis-cache 相关
spring:
  redis:
    database: 9
    host: 192.168.14.108
    port: 6379
    # password:
    #    cluster:
    #      nodes:
    #        - 127.0.0.1:6379
    #        - 127.0.0.1:6380
    #        - 127.0.0.1:6381
    #        - 127.0.0.1:6382
    lettuce:
      pool:
        # 连接池最大连接数 默认8 ，负数表示没有限制
        max-active: 32
        # 连接池最大阻塞等待时间（使用负值表示没有限制） 默认-1
        max-wait: -1
        # 连接池中的最大空闲连接 默认8
        max-idle: 8
        # 连接池中的最小空闲连接 默认0
        min-idle: 0
    timeout: 3000
```
## 缓存开启
2、开启AOP扫描
```
在spring boot启动类中加入  
@EnableDtguaiCache
```
## 缓存使用
3、在Service上进行@Cache注解或@CacheClear注解
# 注解说明
## 配置缓存：@Cache
 注解参数 | 类型  | 说明
 -------------  |------------- | -----
 key            | 字符串                                | 缓存表达式，动态运算出key 
 expires        | 整形            |    缓存时长，单位：分钟  默认为10分钟        
 parser         | Class<? extends ICacheResultParser> |  缓存返回结果自定义处理类 
 generator      | Class<? extends IKeyGenerator> |  缓存键值自定义生成类 
## 清除缓存：@CacheClear
注解参数        | 类型         | 说明
-------------  |------------- | -----
pre	|   字符串 |	清除某些前缀key缓存 写法:pre = "1*" or pre = {"1*","2","3*"}  
key |	字符串 |	清除某个key缓存 写法:key = "1" or  key = {"1","2","3"}
generator      | Class<? extends IKeyGenerator> |  缓存键值自定义生成类 
## 默认key动态表达式说明
表达式举例 | 说明 | 举例
-------------  |------------- | -----
@Cache(key="user:{1}")<br>public User getUserByAccount(String account) | {1}表示获取第一个参数值<br>{2}表示获取第二个参数值<br>……依此类推 | 若：account = ace，则：key = user:ace
@CacheClear(pre="user:{1.account}")<br>User saveOrUpdate(User user)|{1}表示获取第一个参数值<br>{1.xx}表示获取第一个参数中的xxx属性|若：account=ace，则：key = user:ace

# Demo
1、在src/main/test中展开的相关示例代码
>CacheTest是核心启动类
>>service包是缓存调用例子，包含自定义表达式和结果解析、注解的使用

# 常见问题
类中使用redisTemplate:
```
 @Autowired
 private RedisTemplate<String, String> redisTemplate;
```

基础操作方式展示: 具体参数请移步测试类
```
@Override
    @Cache(key = "user:{1}")
    public User get(String account) {
        log.warn("从get方法内读取....");
        return new User("太子", 42, account);
    }

    @Override
    @Cache(key = "user:list")
    public List<User> getList() {
        log.warn("从getLlist方法内读取....");
        List<User> users = new ArrayList<>(20);
        for (int i = 0; i < 20; i++) {
            User user = new User("陈雄", i, "cx911");
            users.add(user);
        }
        return users;
    }

    @Override
    @Cache(key = "user:set", parser = SetCacheResultParser.class)
    public Set<User> getSet() {
        log.warn("从getSet方法内读取....");
        Set<User> users = new HashSet<>(20);
        for (int i = 0; i < 20; i++) {
            User user = new User("陈雄", i, "cx911");
            users.add(user);
        }
        return users;
    }

    @Override
    @Cache(key = "user:map:{1.age}:{2.age}:{3.name}")
    public Map<String, User> getMap(Map<String, Object> m, List l, User u) {
        log.warn("从方法内读取....");
        Map<String, User> users = new HashMap<>(40);
        for (int i = 0; i < 20; i++) {
            User user = new User("陈雄", i, "cx911");
            users.put(user.getAccount() + i, user);
        }
        return users;
    }

    @Override
    //@CacheClear(key = "user:{1.age}:{1.name}:{1.account}", pre = "user", keys = "1,2,3,4")
    //@CacheClear( pre = "user:map:{1.age},2,3", keys = "1,2,3,4")
    @CacheClear(key = "1", pre = "2*")
    public void save(User user) {

    }

    @Override
    @Cache(generator = MyKeyGenerator.class)
    public User get(int age) {
        log.warn("从get方法内读取....");
        return new User("陈雄", age, "cx911");
    }

    /**
     * 对map返回结果做处理
     */
    public static class UserMapCacheResultParser implements CacheResultParser<HashMap<String, User>> {
        @Override
        public HashMap<String, User> parse(String value, Type returnType) {
            return JSON.parseObject(value, returnType);
        }
    }

    /**
     * 对set返回结果做处理
     */
    public static class SetCacheResultParser implements CacheResultParser<HashSet<User>> {
        @Override
        public HashSet<User> parse(String value, Type returnType) {
            return JSON.parseObject(value, new TypeReference<HashSet<User>>() {
            });
        }
    }
```


## 感谢 
感谢作者：[The Sun](https://gitee.com/geek_qi/ace-cache)
