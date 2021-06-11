package com.centit.dde.utils;

import com.centit.dde.annotation.ClasssDescribe;
import com.centit.dde.annotation.MethodDescribe;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author zhou_c
 *
 * 特殊说明：在该类中添加方法时 必须指定@MethodDescribe 注解，否则反射执行方法时方法的参数就无法指定
 */
@ClasssDescribe
public class RedisOperationUtils {

    public RedisOperationUtils() {
    }

    private RedisTemplate<String, Object> redisTemplate;

    public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 指定缓存失效时间
     * @param redisKey 键
     * @param time 时间(秒)
     */
    @MethodDescribe(describe = "指定缓存失效时间",parameter={"redisKey:String","time:Long"})
    public boolean expireTime(String redisKey,Long time){
        try {
            if(time>0){
                redisTemplate.expire(redisKey, time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 根据key 获取过期时间
     * @param redisKey 键 不能为null
     * @return 时间(秒) 返回0代表为永久有效
     */
    @MethodDescribe(describe = "根据key获取过期时间",parameter = "redisKey:String")
    public Long getExpireTime(String redisKey){
        return redisTemplate.getExpire(redisKey,TimeUnit.SECONDS);
    }

    /**
     * 判断key是否存在
     * @param redisKey 键
     * @return true 存在 false不存在
     */
    @MethodDescribe(describe = "判断key是否存在",parameter = "redisKey:String")
    public boolean isExistKey(String redisKey){
        try {
            return redisTemplate.hasKey(redisKey);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除缓存
     * @param redisKey 可以传一个值 或多个
     */
    @SuppressWarnings("unchecked")
    @MethodDescribe(describe = "删除缓存",parameter = "redisKey:String")
    public void deleteKey(String ... redisKey){
        if(redisKey!=null&&redisKey.length>0){
            if(redisKey.length==1){
                redisTemplate.delete(redisKey[0]);
            }else{
                List<String> list = (List<String>) CollectionUtils.arrayToList(redisKey);
                redisTemplate.delete(list);
            }
        }
    }

    //============================String=============================
    /**
     * 获取  String 类型数据
     * @param redisKey 键
     * @return 值
     */
    @MethodDescribe(describe = "获取String类型数据",parameter = "redisKey:String")
    public Object getStringData(String redisKey){
        return redisKey==null?null:redisTemplate.opsForValue().get(redisKey);
    }

    /**
     * 保存 String 类型数据
     * @param redisKey 键
     * @param value 值
     * @return true成功 false失败
     */
    @MethodDescribe(describe = "保存String类型数据",parameter = {"redisKey:String","value:Object"})
    public boolean saveStringData(String redisKey,Object value) {
        try {
            redisTemplate.opsForValue().set(redisKey, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 保存String 类型数据  并设置过期时间
     * @param redisKey 键
     * @param value 值
     * @param time 时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @return true成功 false 失败
     */
    @MethodDescribe(describe = "保存String类型数据，并设置过去时间",parameter = {"redisKey:String","value:Object","time:Long"})
    public boolean saveStringDataAndSetTime(String redisKey,Object value,Long time){
        try {
            if(time>0){
                redisTemplate.opsForValue().set(redisKey, value, time, TimeUnit.SECONDS);
            }else{
                saveStringData(redisKey, value);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 递增
     * @param redisKey 键
     * @param delta 要增加几(大于0)
     * @return long
     */
    @MethodDescribe(describe = "递增",parameter = {"redisKey:String","delta:long"})
    public Long incr(String redisKey, Long delta){
        if(delta<0){
            throw new RuntimeException("递增因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(redisKey, delta);
    }

    /**
     * 递减
     * @param redisKey 键
     * @param delta 要减少几(小于0)
     * @return
     */
    @MethodDescribe(describe = "递减",parameter = {"redisKey:String","delta:Long"})
    public Long decr(String redisKey, Long delta){
        if(delta<0){
            throw new RuntimeException("递减因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(redisKey, -delta);
    }

    //================================Map=================================
    /**
     *根据Redis的key和map中的key获取值
     * @param redisKey redis键 不能为null
     * @param mapKey map键 不能为null
     * @return 值
     */
    @MethodDescribe(describe = "根据redisKey和mapKey获取值",parameter = {"redisKey:String","mapKey:String"})
    public Object getValueByMapKey(String redisKey,String mapKey){
        return redisTemplate.opsForHash().get(redisKey, mapKey);
    }

    /**
     * 根据key获取map值
     * @param redisKey 键
     * @return 对应的多个键值
     */
    @MethodDescribe(describe = "根据redisKey获取map值",parameter = {"redisKey:String"})
    public Map<Object,Object> getMapData(String redisKey){
        return redisTemplate.opsForHash().entries(redisKey);
    }

    /**
     * 保存map数据
     * @param redisKey 键
     * @param map 数据
     * @return true 成功 false 失败
     */
    @MethodDescribe(describe = "保存map数据",parameter = {"redisKey:String","map:Map"})
    public boolean saveMapData(String redisKey, Map<String,Object> map){
        try {
            redisTemplate.opsForHash().putAll(redisKey, map);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 保存map数据 并设置时间
     * @param redisKey 键
     * @param map 对应多个键值
     * @param time 时间(秒)
     * @return true成功 false失败
     */
    @MethodDescribe(describe = "保存map数据 并设置时间",parameter = {"redisKey:String","map:Map","time:Long"})
    public boolean saveMapDataAndSetTime(String redisKey, Map<String,Object> map, Long time){
        try {
            redisTemplate.opsForHash().putAll(redisKey, map);
            if(time>0){
                expireTime(redisKey, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 根据map键更新map值 ,如果不存在将创建（redisKey不存在则创建redisKey  mapkey 不存在则创建mapkey ，都不存在则都创建）
     * @param redisKey redis键
     * @param mapKey map键
     * @param value 值
     * @return true 成功 false失败
     */
    @MethodDescribe(describe = " 根据2个key更新值 ,如果不存在将创建",parameter = {"redisKey:String","mapKey:String","value:Object"})
    public boolean updateOrAddMapData(String redisKey,String mapKey,Object value) {
        try {
            redisTemplate.opsForHash().put(redisKey, mapKey, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 根据map键更新map值 ,如果不存在将创建（redisKey不存在则创建redisKey  mapkey 不存在则创建mapkey ，都不存在则都创建） 并设置时间
     * @param redisKey 键
     * @param mapKey 项
     * @param value 值
     * @param time 时间(秒)  注意:如果已存在的hash表有时间,这里将会替换原有的时间
     * @return true 成功 false失败
     */
    @MethodDescribe(describe = " 根据2个key更新值 ,如果不存在将创建,并设置过期时间",parameter = {"redisKey:String","mapKey:String","value:Object","time:long"})
    public boolean updateOrAddMapDataAndSetTime(String redisKey,String mapKey,Object value,Long time) {
        try {
            redisTemplate.opsForHash().put(redisKey, mapKey, value);
            if(time>0){
                expireTime(redisKey, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除map中的key
     * @param redisKey 键 不能为null
     * @param mapKey 项 可以使多个 不能为null
     */
    @MethodDescribe(describe = "删除map中的key",parameter = {"redisKey:String","mapKey:Object"})
    public Long removeMapData(String redisKey, Object... mapKey){
        return redisTemplate.opsForHash().delete(redisKey,mapKey);
    }

    /**
     * 判断map中是否存在指定的值
     * @param redisKey 键 不能为null
     * @param mapKey 项 不能为null
     * @return true 存在 false不存在
     */
    @MethodDescribe(describe = "判断map中是否存在指定的值",parameter = {"redisKey:String","mapKey:String"})
    public boolean isExistByMapKey(String redisKey, String mapKey){
        return redisTemplate.opsForHash().hasKey(redisKey, mapKey);
    }

    /**
     * map递增 如果不存在,就会创建一个 并把新增后的值返回
     * @param redisKey 键
     * @param mapKey 项
     * @param delta 要增加几(大于0)
     * @return
     */
    @MethodDescribe(describe = "map递增如果不存在,就会创建一个并把新增后的值返回",parameter = {"redisKey:String","mapKey:String","delta:double"})
    public double mapIncr(String redisKey, String mapKey,double delta){
        return redisTemplate.opsForHash().increment(redisKey, mapKey, delta);
    }

    /**
     * map递减
     * @param redisKey 键
     * @param mapKey 项
     * @param delta 要减少记(小于0)
     */
    @MethodDescribe(describe = "map递减",parameter = {"redisKey:String","mapKey:String","delta:double"})
    public double mapDecr(String redisKey, String mapKey,double delta){
        return redisTemplate.opsForHash().increment(redisKey, mapKey,-delta);
    }

    //============================set=============================
    /**
     * 根据key获取Set集合中的所有值
     * @param redisKey 键
     */
    @MethodDescribe(describe = "根据key获取Set集合中的所有值",parameter = {"redisKey:String"})
    public Set<Object> getAllSetData(String redisKey){
        try {
            return redisTemplate.opsForSet().members(redisKey);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     *根据value判断集合中是否存在
     * @param redisKey 键
     * @param value 值
     * @return true 存在 false不存在
     */
    @MethodDescribe(describe = "根据value判断set集合中是否存在",parameter = {"redisKey:String","value:Object"})
    public boolean isExistByValue(String redisKey,Object value){
        try {
            return redisTemplate.opsForSet().isMember(redisKey, value);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将数据放入set缓存
     * @param redisKey 键
     * @param values 值 可以是多个
     * @return 成功个数
     */
    @MethodDescribe(describe = "将set集合数据放入缓存",parameter = {"redisKey:String","values:Object"})
    public Long saveSetData(String redisKey, Object values) {
        try {
            return redisTemplate.opsForSet().add(redisKey, values);
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }

    /**
     * 将set集合数据放入缓存 并设置过期时间
     * @param redisKey 键
     * @param time 时间(秒)
     * @param values 值 可以是多个
     * @return 成功个数
     */
    @MethodDescribe(describe = "将set集合数据放入缓存,并设置过期时间",parameter = {"redisKey:String","time:Long","values:Object"})
    public Long saveSetDataAndSetTime(String redisKey,Object values,Long time) {
        try {
            Long count = redisTemplate.opsForSet().add(redisKey, values);
            if(time>0) {
                expireTime(redisKey, time);
            }
            return count;
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }

    /**
     * 获取set集合大小
     * @param redisKey 键
     */
    @MethodDescribe(describe = "获取set集合大小",parameter = {"redisKey:String"})
    public Long getSetListSize(String redisKey){
        try {
            return redisTemplate.opsForSet().size(redisKey);
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }

    /**
     * 根据value删除数据 相当于list.remove(index)
     * @param redisKey 键
     * @param values 值 可以是多个
     * @return 移除的个数
     */
    @MethodDescribe(describe = "根据value删除set集合数据",parameter = {"redisKey:String","values:Object"})
    public Long removeDataByValue(String redisKey, Object ...values) {
        try {
            Long count = redisTemplate.opsForSet().remove(redisKey, values);
            return count;
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }
    //===============================list=================================

    /**
     * 获取List集合数据 并指定返回条数
     * @param redisKey 键
     * @param start 开始
     * @param end 结束  0 到 -1代表所有值
     */
    @MethodDescribe(describe = "获取List集合数据,并指定返回条数,0到-1代表所有值",parameter = {"redisKey:String","start:Long","end:Long"})
    public List<Object> getListData(String redisKey, Long start, Long end){
        try {
            return redisTemplate.opsForList().range(redisKey, start, end);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取list集合大小
     * @param redisKey 键
     */
    @MethodDescribe(describe = "获取list集合大小",parameter = {"redisKey:String"})
    public Long getListSize(String redisKey){
        try {
            return redisTemplate.opsForList().size(redisKey);
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }

    /**
     * 通过索引 获取list中的值
     * @param redisKey 键
     * @param index 索引  index>=0时， 0 表头，1 第二个元素，依次类推；index<0时，-1，表尾，-2倒数第二个元素，依次类推
     */
    @MethodDescribe(describe = "通过索引 获取list中的值",parameter = {"redisKey:String","index:Long"})
    public Object getListDataByIndex(String redisKey,Long index){
        try {
            return redisTemplate.opsForList().index(redisKey, index);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 将list放入缓存
     * @param redisKey 键
     * @param value 值
     */
    @MethodDescribe(describe = "将list集合放入缓存",parameter = {"redisKey:String","value:Object"})
    public boolean saveObjectListData(String redisKey, Object value) {
        try {
            redisTemplate.opsForList().rightPush(redisKey, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将list放入缓存 并指定过期时间
     * @param redisKey 键
     * @param value 值
     * @param time 时间(秒)
     */
    @MethodDescribe(describe = "将list集合放入缓存,并指定过期时间",parameter = {"redisKey:String","value:Object","time:Long"})
    public boolean saveObjectListDataAndSetTime(String redisKey, Object value, Long time) {
        try {
            redisTemplate.opsForList().rightPush(redisKey, value);
            if (time > 0) {
                expireTime(redisKey, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将list放入缓存
     * @param redisKey 键
     * @param values 值
     */
    @MethodDescribe(describe = "将list集合放入缓存",parameter = {"redisKey:String","values:List<Object>"})
    public boolean saveListData(String redisKey, List<Object> values) {
        try {
            redisTemplate.opsForList().rightPushAll(redisKey, values);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将list放入缓存
     * @param redisKey 键
     * @param value 值
     * @param time 时间(秒)
     */
    @MethodDescribe(describe = "将list放入缓存,并设置过期时间",parameter = {"redisKey:String","values:List<Object>","time:Long"})
    public boolean saveListDataAndSetTime(String redisKey, List<Object> value, Long time) {
        try {
            redisTemplate.opsForList().rightPushAll(redisKey, value);
            if (time > 0) {
                expireTime(redisKey, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 根据索引修改list中的某条数据
     * @param redisKey 键
     * @param index 索引
     * @param value 值
     */
    @MethodDescribe(describe = "根据索引修改list中的某条数据",parameter = {"redisKey:String","index:Long","value:Object"})
    public boolean updateListDataByIndex(String redisKey, Long index,Object value) {
        try {
            redisTemplate.opsForList().set(redisKey, index, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 根据value删除数据，并且指定删除数据的条数
     * @param redisKey 键
     * @param count 移除多少条数据
     * @param value 值
     * @return 移除的个数
     */
    @MethodDescribe(describe = "根据value删除数据，并且指定删除数据的条数",parameter = {"redisKey:String","count:Long","value:Object"})
    public Long removeListData(String redisKey,Long count,Object value) {
        try {
            Long remove = redisTemplate.opsForList().remove(redisKey, count, value);
            return remove;
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }

    /**
     * 模糊查询获取key值
     * @param pattern
     */
    @MethodDescribe(describe = "模糊查询获取key值",parameter = {"pattern:String"})
    public Set keys(String pattern){
        return redisTemplate.keys(pattern);
    }

    /**
     * 使用Redis的消息队列
     * @param channel
     * @param message 消息内容
     */
    @MethodDescribe(describe = "使用Redis的消息队列",parameter = {"channel:String","message:Object"})
    public void convertAndSend(String channel, Object message){
        redisTemplate.convertAndSend(channel,message);
    }


    //=========BoundListOperations 用法 start============

    /**
     *将数据添加到Redis的list中（从右边添加）
     * @param listKey
     * @param expireEnum 有效期的枚举类
     * @param values 待添加的数据
     */
 /*   public void addToListRight(String listKey, Status.ExpireEnum expireEnum, Object... values) {
        //绑定操作
        BoundListOperations<String, Object> boundValueOperations = redisTemplate.boundListOps(listKey);
        //插入数据
        boundValueOperations.rightPushAll(values);
        //设置过期时间
        boundValueOperations.expire(expireEnum.getTime(),expireEnum.getTimeUnit());
    }*/
    /**
     * 根据起始结束序号遍历Redis中的list
     * @param listKey
     * @param start  起始序号
     * @param end  结束序号
     */
    @MethodDescribe(describe = "根据起始结束序号遍历Redis中的list",parameter = {"listKey:String","start:Long","end:Long"})
    public List<Object> rangeList(String listKey, Long start, Long end) {
        //绑定操作
        BoundListOperations<String, Object> boundValueOperations = redisTemplate.boundListOps(listKey);
        //查询数据
        return boundValueOperations.range(start, end);
    }
    /**
     * 弹出右边的值 --- 并且移除这个值
     * @param listKey
     */
    @MethodDescribe(describe = "弹出右边的值 --- 并且移除这个值",parameter = {"listKey:String"})
    public Object rifhtPop(String listKey){
        //绑定操作
        BoundListOperations<String, Object> boundValueOperations = redisTemplate.boundListOps(listKey);
        return boundValueOperations.rightPop();
    }

    //=========BoundListOperations 用法 End============
}
