package com.example.demo.model;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.io.Serializable;

/**
 * 天气数据
 */
@Data
@Accessors(chain = true)
@Document(indexName = "weather")
public class FactWeather  implements Serializable {

    private static final long serialVersionUID = 6320548148250372657L;

    @Id
    private int id;
    private String area;//地域
    private String dt;//时间

    private String weather;//天气
    private int minTemp;//最小风
    private int maxTemp;//最大风
    private String windLevel;//风级别
    private String windDirect;//风向

}
