package com.cs.ad.index;

/**
 * @author fucker
 * 对索引进行CRUD的接口。
 * 接口中只定义功能，具体的实现方法（例如并发map存索引数据，而非hashmap存数据）交给实现类
 * K代表索引的键，V代表索引值（也代表操作后的返回值）
 * K一般为主键id，V一般为数据记录的实体类对象
 * 根据k拿到v，根据主键id
 * 拿到对应的数据记录，完成一 一 对应
 */
public interface IndexAware<K,V> {
    /**
     * get
     *
     * @param key 查询的键，一般为主键id
     * @return V 查询的返回值，一般为数据记录的实体类对象
     * @description: 查询索引
     */
    V get(K key);

    /**
     *
     *add
     * @description: 向索引中增加记录
     * @param key 要增加的记录的键，一般为主键id
     * @param value 要增加的记录的值，一般为数据记录的实体类对象
     * @return void 无固定返回值
     */
    void add(K key, V value);

    /**
     *
     *update
     * @description: 根据索引中的记录
     * @param key 要更新的数据记录的键，一般为主键id
     * @param value 要更新的数据记录的值，一般为数据记录的实体类对象
     * @return void 无固定返回值
     */
    void update(K key, V value);

    /**
     *
     *delete
     * @description: 删除索引中的记录
     * @param key 要删除的数据记录的键，一般为主键id
     * @param value 要删除的数据记录的值，一般为数据记录的实体类对象
     * @return void 无固定返回值
     */
    void delete(K key, V value);
}