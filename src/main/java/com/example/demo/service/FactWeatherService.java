package com.example.demo.service;

import com.example.demo.model.FactWeather;
import org.springframework.data.domain.Page;

import java.util.List;

public interface FactWeatherService {

    long count();

    void saveAll(Iterable<FactWeather> factWeathers);

    void delete(FactWeather factWeather);

    Iterable<FactWeather> getAll();

    List<FactWeather> getByArea(String area);


    List<FactWeather> findByAreaAndWeather(String area,String weather);

    Page<FactWeather> pageQuery(Integer pageNo, Integer pageSize, String kw);

    Page<FactWeather> pageQueryAll(Integer pageNo, Integer pageSize, String kw);

}
