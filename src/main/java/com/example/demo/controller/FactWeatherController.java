package com.example.demo.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.model.FactWeather;
import com.example.demo.service.FactWeatherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/factweather")
@Api(value = "FactWeatherController",tags={"ElasticSearch天气数据"})
public class FactWeatherController {

    @Resource
    private FactWeatherService factWeatherService;

    private static List<FactWeather> list;

    /**
     * 静态初始化数据
     */
    static {
        //原始数据日期格式为1/1/2001，需要转换
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy");
        try {
            String jsonData = FileUtils.readFileToString(ResourceUtils.
                    getFile("classpath:fact_weather_cn.json"), Charset.forName("UTF-8"));
            JSONArray jsonArray = JSONObject.parseArray(jsonData);
            AtomicInteger num = new AtomicInteger(1);
            list= jsonArray.stream().map(i->{
                FactWeather factWeather= JSON.toJavaObject((JSON) JSON.toJSON(i),FactWeather.class);
                factWeather.setId(num.getAndIncrement());
                //转换日期格式，原始数据为1/1/2001
                LocalDate date= LocalDate.parse(factWeather.getDt(), formatter);
                factWeather.setDt(date.toString());
                //log.info(factWeather.toString());
                return factWeather;
            }).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化数据推送到ElasticSearch
     * @return
     */
    @ApiOperation(value="推送初始数据", notes="初始化数据,推送数据到ElasticSearch")
    @RequestMapping(value = "/pushData", method = RequestMethod.GET)
    public ResponseEntity pushData(){
        if (CollectionUtils.isEmpty(list)){
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        factWeatherService.saveAll(list);
        return new ResponseEntity(HttpStatus.OK);
    }

    /**
     * 根据地域查询
     * @param area
     * @return
     */
    @ApiOperation(value="根据地域查询", notes="根据地域查询详细信息")
    @ApiImplicitParam(name = "area", value = "地域", required = true, dataType = "String", paramType = "path",defaultValue = "北京")
    @RequestMapping(value = "getByArea/{area}", method = RequestMethod.GET)
    public ResponseEntity getByArea (@PathVariable(value = "area") String area){
        if (area.isEmpty()){
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(factWeatherService.getByArea(area), HttpStatus.OK);
    }

    /**
     * 根据地域查询
     * @param area
     * @return
     */
    @ApiOperation(value="根据地域和天气查询", notes="根据地域和天气查询详细信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "area", value = "地域", required = true),
            @ApiImplicitParam(name = "weather", value = "天气", required = true),
    })
    @RequestMapping(value = "findByAreaAndWeather", method = RequestMethod.GET)
    public ResponseEntity findByAreaAndWeather (@RequestParam(value = "area") String area,@RequestParam(value = "weather") String weather){
        if (area.isEmpty()){
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(factWeatherService.findByAreaAndWeather(area,weather), HttpStatus.OK);
    }

    /**
     * 获取所有数据
     * @return
     */
    @ApiOperation(value = "获取天气总数量", notes = "获取所有天气总数")
    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    public ResponseEntity getAll() {
        //List list= (List) factWeatherService.getAll();
        return new ResponseEntity(factWeatherService.getAll(), HttpStatus.OK);
    }

}
