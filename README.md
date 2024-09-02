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

# 技术方案
## 迁移方案
   灰度迁移：通过阶段性迁移实现无缝过渡，包括并行处理现有和新令牌。
   数据一致性：确保在迁移过程中，用户的身份信息和权限得到一致性维护。
   监控和回滚机制：在迁移过程中监控系统状态，并建立回滚机制以防万一。

迁移步骤
1. 令牌迁移
   阶段一：在应用程序中实现 JWT 生成和验证功能，同时保留现有的有状态令牌机制。
   用户登录时，生成无状态访问令牌（JWT）并返回给用户，同时保留有状态令牌的生成与验证流程。
   在此阶段，用户可以使用新的 JWT 令牌和现有的有状态令牌。
   用户授权时，如果传入了JWT令牌，则以JWT令牌为准。如果未传入则以有状态令牌为准。
   阶段二：逐步引导客户端升级，修改为获取无状态令牌。
   阶段三：确认客户端（或强制客户端升级才能使用）已经完成升级之后，停用有状态令牌的相关逻辑和存储。   
   
2. NoSQL数据迁移
   1）批量迁移NoSQL数据到MySQL。分批拉取NoSQL中的Session数据，转换之后批量写入MySQL.
   2）批量完成数据一致性校验。分批拉去NoSQL中的Sesion数据和已经写入的MySQL数据，完成一致性校验。
   3）完成热点数据预热。统计热点用户数据，迁移完成后，切流前进行缓存预热。

3. 分片策略
   1）迁移后的session数据，根据主键id进行sharding，如当存在两个分片的时候可以使用id%2进行路由。
   2）
   
5. 监控和回滚机制
   监控迁移过程中的系统性能、用户反馈和异常情况。
   如果发现重大问题，确保能够快速回滚到有状态令牌的系统，避免对用户体验的影响。

6. 迁移完成后
   更新文档，说明新的无状态令牌机制的使用和相关 API 的变更。
   进行用户培训和通知，确保用户了解新机制的优势和使用方法。

# 技术架构


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

## 数据迁移

## 


