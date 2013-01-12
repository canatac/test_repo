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
public class ActivityTest {

	private static final Logger _logger = Logger.getLogger(ActivityTest.class.getName());

	@Deployment
	public static JavaArchive createDeployment(){
		JavaArchive jar = ShrinkWrap.create(JavaArchive.class)
				.addClasses(Activity.class,GenericEntity.class)
				.addAsManifestResource(EmptyAsset.INSTANCE,"beans.xml");
		
		_logger.info(jar.toString(true));
		return jar;
	}
	
	@Inject
	Activity activity;
	
	@Test
	public void shoud_create_description(){
		activity.setDescription("toto");
		_logger.info("activity.getDescription() : "+activity.getDescription());
	}
}
