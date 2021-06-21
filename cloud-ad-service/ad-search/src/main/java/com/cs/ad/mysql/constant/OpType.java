package com.cs.ad.mysql.constant;

import com.github.shyiko.mysql.binlog.event.EventType;

/**
 * @author fucker
 * 定义MySQL的CRUD等操作的类型
 */
public enum OpType {
    /**ADD 增加*/
    ADD,
    /**UPDATE 更新*/
    UPDATE,
    /**DELETE,删除*/
    DELETE,
    /**OTHER，其他*/
    OTHER;

    /**根据Binlog监听工具的eventType，转化为自定义的OpType*/
    public static OpType to(EventType eventType){

        switch (eventType){
            case EXT_WRITE_ROWS:
                return ADD;
            case EXT_UPDATE_ROWS:
                return UPDATE;
            case EXT_DELETE_ROWS:
                return DELETE;
//                不属于三类之一返回other
            default:
                return OTHER;
        }
    }
}
