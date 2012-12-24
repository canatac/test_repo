package org.ortens.bone.core.service.test;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ortens.bone.core.model.Journal;
import org.ortens.bone.core.utils.MySqlConnect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RunWith(Arquillian.class)
public class JournalFeedTest {
	private Logger logger = LoggerFactory.getLogger(JournalFeedTest.class);

	public Logger getLogger() {
		return logger;
	}

//	@Inject
//    private Journal journal;
	
	@Deployment
        public static JavaArchive createTestArchive() {
            return ShrinkWrap.create(JavaArchive.class)
                .addClass(Journal.class)
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
            
//            return ShrinkWrap.create(JavaArchive.class, "test.jar")
//            .addClass(Journal.class)
//            .addAsManifestResource("META-INF/beans.xml", ArchivePaths.create("beans.xml")) // Le descripteur de déploiement pour activer CDI 1.0
//            .addAsManifestResource("META-INF/ejb-jar.xml", ArchivePaths.create("ejb-jar.xml")) // Le descripteur de déploiement standard ejb-jar.xml
//            .addAsManifestResource("META-INF/glassfish-ejb-jar.xml", ArchivePaths.create("glassfish-ejb-jar.xml")); // Le descripteur de déploiement spécifique glassfish-ejb-jar.xml
       }

	@Test
	public void testFeed() {
		Assert.fail("Not yet implemented");
//		
//		MySqlConnect mysqlDb = new MySqlConnect();
//		java.sql.Connection connection = mysqlDb.getConnection();
//		
//		
//		
//		journal.setDescription("testJournal");
		
//		logger.info(journal.getDescription());
		
	}

}
