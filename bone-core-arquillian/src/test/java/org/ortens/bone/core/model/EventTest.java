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
public class EventTest {

	private static final Logger _logger = Logger.getLogger(EventTest.class.getName());

	@Deployment
	public static JavaArchive createDeployment(){
		JavaArchive jar = ShrinkWrap.create(JavaArchive.class)
				.addClasses(Event.class, GenericEntity.class)
				.addAsManifestResource(EmptyAsset.INSTANCE,"beans.xml");
		_logger.info(jar.toString(true));
		return jar;
	}
	
	@Inject
	Event event;
	
	@Test
	public void should_create_description(){
		event.setDescription("toto");
		_logger.info("event.getDescription() : " + event.getDescription());
	}
}
