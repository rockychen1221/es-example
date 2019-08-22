package com.example.demo.service;

import com.example.demo.model.FactWeather;
import org.springframework.data.domain.Page;

import java.util.List;

public interface FactWeatherService {

    long count();

    FactWeather save(FactWeather factWeather);

    void delete(FactWeather factWeather);

    Iterable<FactWeather> getAll();

    List<FactWeather> getByName(String name);

    Page<FactWeather> pageQuery(Integer pageNo, Integer pageSize, String kw);

}
