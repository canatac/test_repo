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
public class LivraisonTest {

	private static final Logger _logger = Logger.getLogger(LivraisonTest.class.getName());

	@Deployment
	public static JavaArchive createDeployment(){
		JavaArchive jar = ShrinkWrap.create(JavaArchive.class)
				.addClasses(Changement.class, BaseEntity.class, Livraison.class, Demand.class)
				.addAsManifestResource(EmptyAsset.INSTANCE,"beans.xml");
		_logger.info(jar.toString(true));
		
		return jar;
	}
	
	@Inject
	Livraison livraison;
	
	@Test
	public void should_create_description(){
		livraison.setDescription("toto");
		_logger.info("livraison.getDescription() : "+livraison.getDescription());
	}
	
}
