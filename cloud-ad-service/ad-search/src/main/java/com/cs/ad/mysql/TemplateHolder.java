package com.cs.ad.mysql;

import com.alibaba.fastjson.JSON;
import com.cs.ad.mysql.constant.OpType;
import com.cs.ad.mysql.dto.ParseTemplate;
import com.cs.ad.mysql.dto.TableTemplate;
import com.cs.ad.mysql.dto.Template;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

/**
 * @author fucker
 * 调用ParseTemplate 解析模板文件
 */
@Slf4j
@Component
public class TemplateHolder {

    /**注入JDBC模板*/
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private ParseTemplate template;

    /**定义的查找系统表的SQL*/
    private String SQL_SCHEMA = "select table_schema, table_name, " +
            "column_name, ordinal_position from information_schema.columns " +
            "where table_schema = ? and table_name = ?";

    /**init方法，启动时即执行加载解析配置文件*/
    public void init() {
//        传入JSON文件，启动时解析该文件
        loadJson("template.json");
    }

//    对外提供服务的方法，对外提供服务是解析Binlog,
//    解析Binlog需要对应到每一张表,因为只有一个数据库，
//    因此解析Binlog，实际上是解析表。根据表名解析对应的表
    public TableTemplate getTable(String tableName) {
        return template.getTableTemplateMap().get(tableName);
    }

    /**查询，实现索引到列名的映射关系*/
    private void loadMeta(){
        /*对每张表进行查询*/
        for(Map.Entry<String, TableTemplate> entry :
                template.getTableTemplateMap().entrySet()){

            /*获取table的定义*/
            TableTemplate table = entry.getValue();

            List<String> updateFields = table.getOpTypeFieldSetMap().get(
                    OpType.UPDATE
            );
            List<String> insertFields = table.getOpTypeFieldSetMap().get(
                    OpType.ADD
            );
            List<String> deleteFields = table.getOpTypeFieldSetMap().get(
                    OpType.DELETE
            );

            /*通过JDBC模板实现查询*/
            jdbcTemplate.query(SQL_SCHEMA, new Object[]{
                    template.getDatabase(), table.getTableName()
            }, (rs, i) -> {

                int pos = rs.getInt("ORDINAL_POSITION");
                String colName = rs.getString("COLUMN_NAME");

                if ((null != updateFields && updateFields.contains(colName))
                        || (null != insertFields && insertFields.contains(colName))
                        || (null != deleteFields && deleteFields.contains(colName))) {
                    table.getPositionMap().put(pos - 1, colName);
                }

                return null;
            });


        }

    }

    /*加载配置文件*/
    public void loadJson(String path){
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        /*根据传入的路径，获取对应的输入流*/
        InputStream inputStream = classLoader.getResourceAsStream(path);

        try {
            Template template = JSON.parseObject(
                    /*输入流*/
                    inputStream,
                    /*字符集*/
                    Charset.defaultCharset(),
                    /*反序列化的类型*/
                    Template.class);

            this.template = ParseTemplate.parse(template);

        }catch (IOException ex){
            log.error(ex.getMessage());
            /*抛出运行时异常，没有模板文件必然要启动失败*/
            throw new RuntimeException("fail to parse json file");
        }
    }

}
