package com.example.demo.service.imps;

import com.example.demo.dao.FactWeatherRepository;
import com.example.demo.model.FactWeather;
import com.example.demo.service.FactWeatherService;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.awt.print.Book;
import java.util.ArrayList;
import java.util.List;

@Service
public class FactWeatherServiceImpl implements FactWeatherService {

    @Resource
    private FactWeatherRepository factWeatherRepository;


    @Override
    public long count() {
        return factWeatherRepository.count();
    }

    @Override
    public void saveAll(Iterable<FactWeather> factWeathers) {
        factWeatherRepository.deleteAll();
        factWeatherRepository.saveAll(factWeathers);
    }

    @Override
    public void delete(FactWeather factWeather) {
        factWeatherRepository.delete(factWeather);
    }

    @Override
    public Iterable<FactWeather> getAll() {
        return factWeatherRepository.findAll();
    }

    @Override
    public List<FactWeather> getByArea(String area) {
        List<FactWeather> list = new ArrayList<>();
        MatchQueryBuilder matchQueryBuilder = new MatchQueryBuilder("area", area);
        Iterable<FactWeather> iterable = factWeatherRepository.search(matchQueryBuilder);
        iterable.forEach(e->list.add(e));
        return list;
    }

    @Override
    public Page<FactWeather> pageQuery(Integer pageNo, Integer pageSize, String kw) {
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.matchPhraseQuery("name", kw))
                .withPageable(PageRequest.of(pageNo, pageSize))
                .build();
        return factWeatherRepository.search(searchQuery);
    }


    @Override
    public List<FactWeather> findByAreaAndWeather(String area, String weather) {
        return factWeatherRepository.findByAreaAndWeather(area,weather);
    }
}
