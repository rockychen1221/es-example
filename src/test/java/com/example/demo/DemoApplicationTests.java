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

	}

	@Test
	public void testGetAll() {
		Iterable<FactWeather> iterable = factWeatherService.getAll();
		iterable.forEach(e->System.out.println(e.toString()));
	}

	@Test
	public void testGetByName() {
		List<FactWeather> list = factWeatherService.getByArea("北京");
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