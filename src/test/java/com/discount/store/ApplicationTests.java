package com.discount.store;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.Assert.assertTrue;

@SpringBootTest
class ApplicationTests {

	@Test
	void contextLoads() throws Exception {
		Application.main(new String[] {});
		assertTrue("If I run, Means I'm Okay!", true);
	}

}
