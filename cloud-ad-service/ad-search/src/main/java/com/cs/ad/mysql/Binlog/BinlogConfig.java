package com.cs.ad.mysql.Binlog;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author fucker
 * @ConfigurationProperties 实现配置到对象的转化
 * (prefix = "adconf.mysql") 对应着
 * adconf:
 *   mysql:
 */
@Component
@ConfigurationProperties(prefix = "adconf.mysql")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BinlogConfig {
    /**属性名和配置名相同*/
    private String host;
    private Integer port;
    private String username;
    private String password;
    private String binlogName;
    private Long position;




}
