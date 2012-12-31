package org.ortens.bone.core.model;

import java.util.logging.Logger;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class ActionPlanTest {

	private static final Logger _logger = Logger.getLogger(ActionPlanTest.class.getName());

	@Deployment
	public static JavaArchive createDeployment(){
		
		JavaArchive jar = ShrinkWrap.create(JavaArchive.class)
			.addClasses(ActionPlan.class, BaseEntity.class)
			.addAsManifestResource(EmptyAsset.INSTANCE,"beans.xml");
		_logger.info(jar.toString(true));
	    return jar;
	}
	
	@Inject
	ActionPlan actionPlan;
	
	@Test
	public void should_create_description(){
		actionPlan.setDescription("toto");
		_logger.info("action.getDescription() : "+actionPlan.getDescription());
	}
}
