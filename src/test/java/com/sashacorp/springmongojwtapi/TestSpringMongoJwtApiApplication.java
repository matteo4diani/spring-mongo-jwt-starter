package com.sashacorp.springmongojwtapi;

import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.stream.Stream;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import com.sashacorp.springmongojwtapi.util.log.Log;
import com.sashacorp.springmongojwtapi.util.log.emoji.Emoji;

/**
 * This is the entry point of all tests, as it is the only file in the root directory of our app:
 * if you need some logic executed before other integration tests are run put it in here
 * (you shouldn't need any specific external state for unit tests).<br/>
 * To ensure the order of test we add {@code @FixMethodOrder(MethodSorters.NAME_ASCENDING)}<br/>
 * For integration tests (IT prefix) that need to interact with the Spring context we add the following annotations:
 * {@code @RunWith(SpringRunner.class)} and {@code @SpringBootTest}<br/>
 * Tests are executed in alphabetical order on directory (where root is first by default), then on file.<br/>
 * Test for {@link SpringMongoJwtApiApplication}.
 * Tests for loaded context.
 * @author matteo
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestSpringMongoJwtApiApplication {

	@Autowired
	ApplicationContext applicationContext;

	final static Logger logger = LoggerFactory.getLogger(TestSpringMongoJwtApiApplication.class);

	@Test
	public void stage1_contextLoads() {
		logger.info(Log.msg(Emoji.COFFEE, "checking loaded beans..."));
		List<String> beans = Stream.of(applicationContext.getBeanDefinitionNames()).sorted().toList();

		beans.forEach((bean) -> {
			logger.info(Log.msg(Emoji.COFFEE, bean));
		});

		try{
			assertTrue(beans.size() > 0);
			logger.info(Log.msg(Emoji.COFFEE, true, "beans loaded and toasted"));
		} catch (AssertionError e) {
			logger.info(Log.msg(Emoji.COFFEE, false, "failed to load beans"));
		}
	}

}
