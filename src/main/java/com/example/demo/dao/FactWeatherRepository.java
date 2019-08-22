package com.example.demo.dao;

import com.example.demo.model.FactWeather;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import org.springframework.stereotype.Repository;

@Repository
public interface FactWeatherRepository extends ElasticsearchRepository<FactWeather,String > {

}
