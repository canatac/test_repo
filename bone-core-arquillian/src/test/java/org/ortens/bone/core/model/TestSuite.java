package org.ortens.bone.core.model;

import java.util.logging.Logger;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;



@RunWith(Suite.class)
@Suite.SuiteClasses({
    ActionPlanTest.class,
    ActionTest.class,
    ActivityTest.class,
    ChangementTest.class,
    CheckPointTest.class,
    CompanyTest.class,
    DemandTest.class,
    DocumentTest.class,
    EventTest.class,
    FileTest.class,
    JournalTest.class,
    LivraisonTest.class,
    PersonTest.class,
    ProblemTest.class,
    ProjectTest.class,
    SolutionTest.class
})
public class TestSuite {
	private static final Logger _logger = Logger.getLogger(TestSuite.class.getName());
}