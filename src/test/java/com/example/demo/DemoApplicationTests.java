package com.example.demo;

import com.example.demo.model.FactWeather;
import com.example.demo.service.FactWeatherService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests {

	@Autowired
	private FactWeatherService factWeatherService;

	@Test
	public void contextLoads() {
		System.out.println(factWeatherService.count());
	}

	@Test
	public void testInsert() {

		FactWeather factWeather = new FactWeather();
		factWeather.setArea("北京");
		factWeather.setDt("2018-01-20");
		factWeather.setId("1");
		factWeather.setMinTemp("1");
		factWeather.setMaxTemp("7");
		factWeather.setWeather("fadsf");
		factWeather.setWindDirect("fa");
		factWeather.setWindLevel("1~7");



		//factWeatherService.save(factWeather);


	}

	@Test
	public void testDelete() {
		FactWeather factWeather = new FactWeather();
		factWeather.setId("1");
		factWeatherService.delete(factWeather);
	}

	@Test
	public void testGetAll() {
		Iterable<FactWeather> iterable = factWeatherService.getAll();
		iterable.forEach(e->System.out.println(e.toString()));
	}

	@Test
	public void testGetByName() {
		List<FactWeather> list = factWeatherService.getByName("北京");
		System.out.println(list);
	}

	@Test
	public void testPage() {
		Page<FactWeather> page = factWeatherService.pageQuery(0, 10, "北京");
		System.out.println(page.getTotalPages());
		System.out.println(page.getNumber());
		System.out.println(page.getContent());
	}
}