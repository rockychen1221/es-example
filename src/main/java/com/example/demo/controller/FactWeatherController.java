package com.example.demo.controller;

import com.example.demo.model.FactWeather;
import com.example.demo.service.FactWeatherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/factweather")
@Api(value = "FactWeatherController",tags={"ElasticSearch Demo API"})
public class FactWeatherController {
    @Resource
    private FactWeatherService factWeatherService;

    /**
     * 根据ID查询用户
     * @param id
     * @return
     */
    @ApiOperation(value="获取用户详细信息", notes="根据url的id来获取用户详细信息")
    @ApiImplicitParam(name = "id", value = "用户ID", required = true, dataType = "Integer", paramType = "path")
    @RequestMapping(value = "user/{id}", method = RequestMethod.GET)
    public ResponseEntity getUserById (@PathVariable(value = "id") String id){
        if (id.isEmpty()){
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(factWeatherService.getAll(), HttpStatus.OK);
    }


    @ApiOperation(value = "获取用户列表", notes = "获取所有用户信息")
    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    public FactWeather getAll() {

        FactWeather factWeather=new FactWeather();
        factWeather.setId("1");
        factWeather.setWindLevel("1");
        factWeather.setWindDirect("1");
        factWeather.setArea("1");

        return factWeather;
    }

}
