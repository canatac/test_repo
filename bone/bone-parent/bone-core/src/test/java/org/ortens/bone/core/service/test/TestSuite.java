package org.ortens.bone.core.service.test;

import java.util.logging.Logger;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;


@RunWith(Suite.class)
@Suite.SuiteClasses({
    ArquillianTest.class})
public class TestSuite {
	private static final Logger _logger = Logger.getLogger(TestSuite.class.getName());
}
