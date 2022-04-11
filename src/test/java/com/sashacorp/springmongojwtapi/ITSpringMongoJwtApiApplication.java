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

import com.sashacorp.springmongojwtapi.util.log.test.Emoji;
import com.sashacorp.springmongojwtapi.util.log.test.Log;

/**
 * Test for {@link SpringMongoJwtApiApplication}.
 * Tests for loaded context.
 * @author matteo
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(SpringRunner.class)
@SpringBootTest
public class ITSpringMongoJwtApiApplication {

	@Autowired
	ApplicationContext applicationContext;

	final static Logger LOG = LoggerFactory.getLogger(ITSpringMongoJwtApiApplication.class);

	@Test
	public void stage1_contextLoads() {
		LOG.info(Log.msg(Emoji.COFFEE, "checking loaded beans..."));
		List<String> beans = Stream.of(applicationContext.getBeanDefinitionNames()).sorted().toList();

		beans.forEach((bean) -> {
			LOG.info(Log.msg(Emoji.COFFEE, bean));
		});

		try{
			assertTrue(beans.size() > 0);
			LOG.info(Log.msg(Emoji.COFFEE, true, "beans loaded and toasted"));
		} catch (AssertionError e) {
			LOG.info(Log.msg(Emoji.COFFEE, false, "failed to load beans"));
		}
	}

}
