# es-example

## ElasticSearch例子

使用SpringDataElasticSearch结合Swagger2进行数据传输交互

## 数据来源 中国天气
## 数据量 22.5W
## 如何使用？
1. 请先确保你有可用的ElasticSearch服务，如没有，请前往[elastic](https://www.elastic.co/cn/downloads/elasticsearch)下载对应版本安装
2. 使用`mvn spring-boot : run`
3. 浏览器输入 `localhost:8080/demo/doc.html`

## Next
1. 集成`logstash` 实现MySQL整表数据推送
2. 加入`kibana` 实现数据分析展示