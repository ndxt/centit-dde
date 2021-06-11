package com.centit.dde.config;

import com.alibaba.fastjson.JSONObject;
import com.centit.product.metadata.po.SourceInfo;
import com.centit.support.security.AESSecurityUtils;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class RedisConfig extends CachingConfigurerSupport {
    private static ConcurrentHashMap<String,RedisTemplate> redisTemplateConcurrentHashMap =new ConcurrentHashMap<>(10);

    private  GenericObjectPoolConfig genericObjectPoolConfig(JSONObject extProps){
        GenericObjectPoolConfig config = new GenericObjectPoolConfig();
        //连接耗尽时是否阻塞, false报异常,ture阻塞直到超时, 默认true
        config.setBlockWhenExhausted(extProps.getBoolean("blockWhenExhausted")==null?true:extProps.getBoolean("blockWhenExhausted"));
        //设置的逐出策略类名, 默认DefaultEvictionPolicy(当连接超过最大空闲时间,或连接数超过最大空闲连接数)
        config.setEvictionPolicyClassName(StringUtils.isNotBlank(extProps.getString("evictionPolicyClassName"))==true?
            extProps.getString("evictionPolicyClassName"):"org.apache.commons.pool2.impl.DefaultEvictionPolicy");
        //是否启用pool的jmx管理功能, 默认true
        config.setJmxEnabled(extProps.getBoolean("jmxEnabled")==null?true:extProps.getBoolean("jmxEnabled"));
        //MBean ObjectName = new ObjectName("org.apache.commons.pool2:type=GenericObjectPool,name=" + "pool" + i); 默认为"pool", JMX不熟,具体不知道是干啥的...默认就好.
        config.setJmxNamePrefix(StringUtils.isNotBlank(extProps.getString("jmxNamePrefix"))==true?
            extProps.getString("jmxNamePrefix"):"pool");
        //是否启用后进先出, 默认true
        config.setLifo(extProps.getBoolean("lifo")==null?true:extProps.getBoolean("lifo"));
        //最大空闲连接数, 默认8个
        config.setMaxIdle(extProps.getIntValue("maxIdle")>0?extProps.getIntValue("maxIdle"):10);
        //最大连接数, 默认8个
        config.setMaxTotal(extProps.getIntValue("maxTotal")>0?extProps.getIntValue("maxTotal"):20);
        //获取连接时的最大等待毫秒数(如果设置为阻塞时BlockWhenExhausted),如果超时就抛异常, 小于零:阻塞不确定的时间,  默认-1
        config.setMaxWaitMillis(extProps.getIntValue("maxWaitMillis")>0?extProps.getIntValue("maxWaitMillis"):30000);
        //资源池中资源最小空闲时间(单位为毫秒)，达到此值后空闲资源将被移除  默认1800000毫秒(30分钟)
        config.setMinEvictableIdleTimeMillis(extProps.getIntValue("minEvictableIdleTimeMillis")>0?extProps.getIntValue("minEvictableIdleTimeMillis"):60000);
        //最小空闲连接数, 默认0
        config.setMinIdle(extProps.getIntValue("minIdle")>0?extProps.getIntValue("minIdle"):1);
        //每次逐出检查时 逐出的最大数目 如果为负数就是 : 1/abs(n), 默认3
        config.setNumTestsPerEvictionRun(extProps.getIntValue("numTestsPerEvictionRun")>0?extProps.getIntValue("numTestsPerEvictionRun"):3);
        //对象空闲多久后逐出, 当空闲时间>该值 且 空闲连接>最大空闲数 时直接逐出,不再根据MinEvictableIdleTimeMillis判断  (默认逐出策略)
        config.setSoftMinEvictableIdleTimeMillis(extProps.getIntValue("softMinEvictableIdleTimeMillis")>0?extProps.getIntValue("softMinEvictableIdleTimeMillis"):1800000);
        //在获取连接的时候检查有效性, 默认false
        config.setTestOnBorrow(extProps.getBoolean("testOnBorrow")==null?false:extProps.getBoolean("testOnBorrow"));
        //是否开启空闲资源监测, 默认false
        config.setTestWhileIdle(extProps.getBoolean("testWhileIdle")==null?true:extProps.getBoolean("testWhileIdle"));
        //空闲资源的检测周期(单位为毫秒) 如果为负数,则不运行逐出线程, 默认-1
        config.setTimeBetweenEvictionRunsMillis(extProps.getIntValue("timeBetweenEvictionRunsMillis")>0?extProps.getIntValue("timeBetweenEvictionRunsMillis"):-1);
        return  config;
    }

    private LettuceConnectionFactory lettuceConnectionFactory(SourceInfo sourceInfo,RedisTypeEnum nodeType){
        String databaseUrl = sourceInfo.getDatabaseUrl();
        if (StringUtils.isBlank(databaseUrl)){
            return null;
        }
        String password = sourceInfo.getPassword();
        JSONObject extProps = sourceInfo.getExtProps();
        String[] split = databaseUrl.split(",");
        String[] ipAndPortArr;
        List<RedisNode> redisNodeList = new ArrayList<>();
        for (String ipAndPort : split) {
            ipAndPortArr = ipAndPort.split(":");
            redisNodeList.add(new RedisNode(ipAndPortArr[0],Integer.valueOf(ipAndPortArr[1])));
        }
        int redisTimeout=extProps.getIntValue("redisTimeout")>0?extProps.getIntValue("redisTimeout"):20*1000;
        int shutdownTimeOut=extProps.getIntValue("shutdownTimeOut")>0?extProps.getIntValue("shutdownTimeOut"):1000;
        LettuceConnectionFactory lettuceConnectionFactory=null;
        //密码解密
        if(StringUtils.isNotBlank(password)){
            password = AESSecurityUtils.decryptBase64String(password.substring(password.indexOf(":") + 1), SourceInfo.DESKEY);
        }
        //redis客户端配置
        LettuceClientConfiguration lettuceClientConfiguration = LettucePoolingClientConfiguration.builder()
                .commandTimeout(Duration.ofMillis(redisTimeout))
                //在关闭客户端连接之前等待任务处理完成的最长时间，在这之后，无论任务是否执行完成，都会被执行器关闭。防止客户端长时间连接浪费资源
                .shutdownTimeout(Duration.ofMillis(shutdownTimeOut))
                .poolConfig(genericObjectPoolConfig(extProps)).build();
        switch (nodeType){
            case SINGLENODE: //单机redis
                RedisStandaloneConfiguration oneRedisConfig = new RedisStandaloneConfiguration();
                ipAndPortArr = split[0].split(":");
                oneRedisConfig.setPassword(password);
                oneRedisConfig.setDatabase(0);
                oneRedisConfig.setHostName(ipAndPortArr[0]);
                oneRedisConfig.setPort(Integer.valueOf(ipAndPortArr[1]));
                lettuceConnectionFactory = new LettuceConnectionFactory(oneRedisConfig, lettuceClientConfiguration);
                break;
            case CLUSTER: //集群redis
                RedisClusterConfiguration clusterRedisConfig = new RedisClusterConfiguration();
                clusterRedisConfig.setClusterNodes(redisNodeList);
                clusterRedisConfig.setPassword(password);
                lettuceConnectionFactory = new LettuceConnectionFactory(clusterRedisConfig, lettuceClientConfiguration);
                break;
            case SENTINEL: //哨兵redis
                RedisSentinelConfiguration sentinelRedisConfig = new RedisSentinelConfiguration();
                sentinelRedisConfig.setSentinels(redisNodeList);
                sentinelRedisConfig.setMaster(sourceInfo.getDatabaseName());
                sentinelRedisConfig.setPassword(password);
                lettuceConnectionFactory = new LettuceConnectionFactory(sentinelRedisConfig, lettuceClientConfiguration);
                break;
        }
        if (lettuceConnectionFactory!=null){
            lettuceConnectionFactory.afterPropertiesSet();
        }
        return lettuceConnectionFactory;
    }

    /**
     * retemplate相关配置
     * @return
     */
    public  RedisTemplate<String, Object> redisTemplate(SourceInfo sourceInfo,RedisTypeEnum nodeType) {
        sourceInfo.setLastModifyDate(null);
        if (redisTemplateConcurrentHashMap.containsKey(sourceInfo.toString())){//这样写有问题，如果元数据更新了无法这个不会更新，先想办法解决
            return redisTemplateConcurrentHashMap.get(sourceInfo.toString());
        }
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        // 配置连接工厂
        template.setConnectionFactory(lettuceConnectionFactory(sourceInfo,nodeType));
        //使用Jackson2JsonRedisSerializer来序列化和反序列化redis的value值（默认使用JDK的序列化方式）
        Jackson2JsonRedisSerializer jacksonSeial = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper om = new ObjectMapper();
        // 指定要序列化的域，field,get和set,以及修饰符范围，ANY是都有包括private和public
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        // 指定序列化输入的类型，类必须是非final修饰的，final修饰的类，比如String,Integer等会跑出异常
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jacksonSeial.setObjectMapper(om);
        // 值采用json序列化
        template.setValueSerializer(jacksonSeial);
        //使用StringRedisSerializer来序列化和反序列化redis的key值
        template.setKeySerializer(new StringRedisSerializer());
        // 设置hash key 和value序列化模式
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(jacksonSeial);
        template.afterPropertiesSet();
        if (!redisTemplateConcurrentHashMap.containsKey(sourceInfo.toString())){
            redisTemplateConcurrentHashMap.put(sourceInfo.toString(),template);
        }
        return template;
    }
}
