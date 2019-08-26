package com.example.demo.dao;

import com.example.demo.model.FactWeather;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface FactWeatherRepository extends ElasticsearchRepository<FactWeather,Integer > {

    List<FactWeather> findByAreaAndWeather(String area,String weather);

}
