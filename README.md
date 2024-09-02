# 技术需求
### 需求说明：
1. **Stateful Token to Stateless Token Migration:**
   - 设计一个机制，将现有的有状态令牌迁移到无状态访问令牌。
   - 确保系统在迁移过程中无缝处理，不影响用户体验。
   - 在迁移过程中保持数据一致性和完整性。

2. **Data Transfer from NoSQL to Relational Database:**
   - 分析现有的NoSQL数据库中的数据结构，并设计适当的关系数据库模式（例如PostgreSQL）来存储转移的数据。
   - 开发一个高效的数据转移机制，将数据从NoSQL迁移到关系数据库。
   - 确保转移过程中数据的一致性和完整性。

3. **Sharding:**
   - 实施分片，将负载分布到多个数据库实例。
   - 定义一个分片策略，允许高吞吐量和低延迟。
   - 确保数据分布和跨分片的负载平衡。

4. **High TPS and Low Latency:**
   - 设计系统以高效地处理每秒高交易量（TPS）。
   - 优化系统以实现低延迟，为用户提供快速响应时间。
   - 实施适当的缓存机制以提高性能。

### 交付物：
1. 系统架构图，展示涉及的组件及其交互。
2. 关系数据库的数据库模式设计。
3. 有状态令牌到无状态访问令牌的迁移机制。
4. 从NoSQL到关系数据库的数据转移机制。
5. 分片策略和实施细节。
6. 性能测试结果，展示高TPS和低延迟。

# 需求分析
stateful token可以理解为服务端是有状态的，常见实现方式为cookie-session。stateless token常见实现方式为jwt token。
本技术方案及按照从cookie-session实现到jwt token实现的迁移来设计。

# 技术方案
## 迁移方案
   灰度迁移：通过阶段性迁移实现无缝过渡，包括并行处理现有和新令牌。
   数据一致性：确保在迁移过程中，用户的身份信息和权限得到一致性维护。
   监控和回滚机制：在迁移过程中监控系统状态，并建立回滚机制以防万一。

迁移步骤
1. 令牌迁移
   阶段一：功能改造上线。在应用程序中实现 JWT 生成和验证功能，同时保留现有的有状态令牌机制。支持按照用户id进行百分比切流。
   用户授权时，如果传入了JWT令牌，则以JWT令牌为准。如果未传入JWT令牌则以有状态令牌为准。
   阶段二：灰度引流。逐步引导客户端升级，修改为获取无状态令牌。
   阶段三：完成切流。确认客户端（或强制客户端升级才能使用）已经完成升级之后，停用有状态令牌的相关逻辑和存储。   
   
2. NoSQL数据迁移
   1）批量迁移NoSQL数据到MySQL。分批拉取NoSQL中的Session数据，转换之后批量写入MySQL.
   2）批量完成数据一致性校验。分批拉去NoSQL中的Sesion数据和已经写入的MySQL数据，完成一致性校验。
   3）完成热点数据预热。统计热点用户数据，迁移完成后，切流前进行缓存预热。

3. session迁移表分片策略
   为提高系统扩展能力，使用一致性hash算法。对user_session表的分库和分表进行分片策略配置。
   1）一致性Hash算法（详细实现见代码```com.example.demo.utils.ConsistentHashing```）实现数据分片节点扩容的时候，相同数据行路由到相同表，避免扩容需要数据迁移。
   2）迁移后的session数据，根据主键id进行sharding，同时采用自定义的一致性分表算法提高扩展性。

   
5. 监控和回滚机制
   监控迁移过程中的系统性能、用户反馈和异常情况。
   如果发现重大问题，确保能够快速回滚到有状态令牌的系统，避免对用户体验的影响。

6. 迁移完成后
   更新文档，说明新的无状态令牌机制的使用和相关 API 的变更。
   进行用户培训和通知，确保用户了解新机制的优势和使用方法。

# 技术架构
<img width="789" alt="image" src="https://github.com/user-attachments/assets/c60b9951-553a-4343-a531-43b0970a6ca2">


# 领域模型
无状态令牌结构
   定义 JWT 的声明，包括：
   sub（主题）：用户唯一标识。
   exp（过期时间）：令牌的有效期。
   iat（签发时间）：令牌的生成时间。
   自定义声明（如角色、权限等）。
   
```json
{
   "sub": "user123",
   "exp": 1700000000,
   "iat": 1600000000,
   "authorities": [{"ROLE_USER"}],
   "userId": 1232
}
```

迁移数据结构
```java

@Data
@Entity
@Table(name = "user_session",
        indexes = {
                @Index(name = "idx_username", columnList = "username"),
                @Index(name = "idx_session_id", columnList = "session_id")
        }, uniqueConstraints = {
        @UniqueConstraint(columnNames = {"session_id"})
})
public class UserSession {
    /**
     * The id of the user.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The username of the user.
     */
    @Column(name = "username")
    private String username;

    /**
     * The role of the user.
     */
    @Column(name = "role")
    private String role;

    /**
     * The session id.
     */
    @Column(name = "session_id")
    private String sessionId;

    /**
     * The expire time of the session.
     */
    @Column(name = "expire_time")
    private long expireTime;

    /**
     * The create time of the session.
     */
    @Column(name = "create_time")
    private long createTime;
}
```


# 核心流程

## session令牌
<img width="682" alt="image" src="https://github.com/user-attachments/assets/464ab647-846a-4455-916e-7a21277a631b">

## jwt令牌
<img width="705" alt="image" src="https://github.com/user-attachments/assets/a928988d-a786-4cfb-93e7-5814d06e5f1b">

## 并行运行期间流程
<img width="778" alt="image" src="https://github.com/user-attachments/assets/251ec6da-5adc-4b61-855d-ad26e89e873f">

## 性能指标
HTTP请求
<img width="1362" alt="image" src="https://github.com/user-attachments/assets/ccd503ae-7815-4c77-bf62-f2adf0520e6f">


Header信息
<img width="1377" alt="image" src="https://github.com/user-attachments/assets/d67642b7-e369-4b63-b362-7a7b8a591d9d">


汇总信息
<img width="1363" alt="image" src="https://github.com/user-attachments/assets/3c47f71a-94ec-4195-8b60-12f8a62df09b">



# 环境说明
JVM：jdk17
NOSQL：mongodb:latest
SQL：MySQL:8.0

# 使用说明
请先调用```http://localhost:8080/public/loadData```接口完成数据初始化，数据初始化会生成100条用户数据，对应的账号密码分别是user${i},password{i}.  

  调用```http://localhost:8080/public/login```完成有状态令牌的登录操作。登录完成后会获得cookie中的sessionid。  
  
  调用```http://localhost:8080/public/newLogin```完成无状态令牌的登录操作，登录完成后会获得临时的jwt的token。后续调用其它接口可以选择使用jwt的token方式（设置Header```Authorization：Bearer xxxxxx```）.  
  
  调用```http://localhost:8080/biz/bizMethod```模拟了业务接口。  
  
  调用```http://localhost:8080/public/migrate```完成存量session从NOSQL道MySQL的数据迁移。  
  
  调用```http://localhost:8080/public/validate```完成迁移数据的校验，根据校验结果确保不会出现数据不一致。  





