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
public class DocumentTest {
	
	private static final Logger _logger = Logger.getLogger(DocumentTest.class.getName());

	@Deployment
	public static JavaArchive createDeployment(){
		JavaArchive jar = ShrinkWrap.create(JavaArchive.class)
				.addClasses(Document.class, GenericEntity.class)
				.addAsManifestResource(EmptyAsset.INSTANCE,"beans.xml");
		_logger.info(jar.toString(true));
		return jar;
	}
	
	@Inject
	Document document;
	
	@Test
	public void shoyuld_create_description(){
		document.setDescription("toto");
		_logger.info("document.getDescription() : " + document.getDescription());
	}

}
