package com.example.demo.service.imps;

import com.example.demo.core.HighLightResultMapper;
import com.example.demo.dao.FactWeatherRepository;
import com.example.demo.model.FactWeather;
import com.example.demo.service.FactWeatherService;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

import static org.elasticsearch.index.query.QueryBuilders.*;

@Service
public class FactWeatherServiceImpl implements FactWeatherService {

    @Resource
    private FactWeatherRepository factWeatherRepository;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;    //es工具
    @Autowired
    private HighLightResultMapper highLightResultMapper;


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
                .withQuery(QueryBuilders.matchPhraseQuery("area", kw))
                .withPageable(PageRequest.of(pageNo, pageSize))
                .build();
        return factWeatherRepository.search(searchQuery);
    }

    @Override
    public Page<FactWeather> pageQueryAll(Integer pageNo, Integer pageSize, String kw) {

        Pageable pageable = PageRequest.of(pageNo, pageSize,new Sort(Sort.Direction.DESC,"dt"));

        String preTag = "<font color='#dd4b39'>";//google的色值
        String postTag = "</font>";

        HighlightBuilder highlightBuilder = new HighlightBuilder();
        //高亮显示规则
        highlightBuilder.preTags(preTag);
        highlightBuilder.postTags(postTag);
        //指定高亮字段
        highlightBuilder.field("weather");
        highlightBuilder.field("windDirect");

/*
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        //高亮显示规则
        highlightBuilder.preTags(preTag);
        highlightBuilder.postTags(postTag);
        //指定高亮字段
        highlightBuilder.field("weather");
        highlightBuilder.field("windDirect");

        SearchQuery searchQuery = new NativeSearchQueryBuilder().
                withQuery(matchQuery("weather", kw)).
                withQuery(matchQuery("windDirect", kw)).
                withHighlightFields(new HighlightBuilder.Field("weather").preTags(preTag).postTags(postTag),
                        new HighlightBuilder.Field("windDirect").preTags(preTag).postTags(postTag)).build();
        searchQuery.setPageable(pageable);

        // 高亮字段
        //AggregatedPage<FactWeather> factWeathers = elasticsearchTemplate.queryForPage(searchQuery, FactWeather.class,highLightResultMapper);
*/
/*

        HighlightBuilder highlightBuilder = new HighlightBuilder();
        //高亮显示规则
        highlightBuilder.preTags("<span style='color:green'>");
        highlightBuilder.postTags("</span>");
        //指定高亮字段
        highlightBuilder.field("name");
        highlightBuilder.field("name.pinyin");
        highlightBuilder.field("director");
        String[] fileds = {"name", "name.pinyin", "director"};

        QueryBuilder matchQuery = QueryBuilders.multiMatchQuery(kw, fileds);

        AggregatedPage<FactWeather> factWeathers = elasticsearchTemplate.queryForPage((SearchQuery) matchQuery, FactWeather.class,highLightResultMapper);
*/

       /* SearchQuery searchQuery = new NativeSearchQueryBuilder().
                withQuery(queryStringQuery(kw)).
                withHighlightFields(new HighlightBuilder.Field("weather").preTags(preTag).postTags(postTag),
                        new HighlightBuilder.Field("windDirect").preTags(preTag).postTags(postTag)).build();
        searchQuery.setPageable(pageable);
queryStringQuery(kw)
      */

        SearchQuery searchQuery = new NativeSearchQueryBuilder().
                withQuery(matchAllQuery()).withQuery(queryStringQuery(kw))
                .withHighlightFields(new HighlightBuilder.Field("weather").preTags(preTag).postTags(postTag),
                new HighlightBuilder.Field("windDirect").preTags(preTag).postTags(postTag)).build();
        searchQuery.setPageable(pageable);

        Page<FactWeather> aa= factWeatherRepository.search(searchQuery);

        AggregatedPage<FactWeather> factWeathers = elasticsearchTemplate.queryForPage( searchQuery, FactWeather.class,highLightResultMapper);

       /* ///第二种
        QueryBuilder customerQuery = QueryBuilders.boolQuery()
                .must(queryStringQuery(kw));

        //搜索数据
        SearchResponse response = elasticsearchTemplate.getClient().prepareSearch("weather")
                .setQuery(customerQuery)
                .highlighter(highlightBuilder)
                .execute().actionGet();
        Page<FactWeather> page1 = (Page<FactWeather>) highLightResultMapper.mapResults(response,FactWeather.class);
*/

        SearchRequestBuilder searchRequestBuilder = elasticsearchTemplate.getClient().prepareSearch("weather");

        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery().must(queryStringQuery(kw));

       /* if (startTime > 0 && endTime > 0) {
            boolQuery.must(QueryBuilders.rangeQuery("processTime")
                    .format("epoch_millis")
                    .from(startTime)
                    .to(endTime)
                    .includeLower(true)
                    .includeUpper(true));
        }*/


        searchRequestBuilder.setQuery(boolQuery);

        searchRequestBuilder.highlighter(highlightBuilder);

        searchRequestBuilder.setFetchSource(true);

        SearchResponse searchResponse = searchRequestBuilder.execute().actionGet();

        AggregatedPage<FactWeather> factWes=   highLightResultMapper.mapResults(searchResponse,FactWeather.class,pageable);

        // = factWeatherRepository.search(searchQuery);
/*
        //搜索数据
        SearchResponse response = elasticsearchTemplate.queryForPage("film-entity")
                .setQuery(matchQuery)
                .highlighter(highlightBuilder)
                .execute().actionGet();*/
/*

        QueryBuilder customerQuery = QueryBuilders.boolQuery()
                .must(queryStringQuery(kw));

//如果不指定filedName，则默认全部，常用在相似内容的推荐上

        Page<FactWeather> page = factWeatherRepository.search(customerQuery, pageable);
        System.out.println("Page customers "+page.getContent().toString());
*/

        return factWeathers;
    }


    @Override
    public List<FactWeather> findByAreaAndWeather(String area, String weather) {
        return factWeatherRepository.findByAreaAndWeather(area,weather);
    }
}
