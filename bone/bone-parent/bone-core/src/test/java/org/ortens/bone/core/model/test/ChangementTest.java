/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ortens.bone.core.model.test;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.ortens.bone.core.model.Changement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;


/**
 *
 * @author canatac
 * @TODO A TESTER avec ARQUILLIAN
 */
//@RunWith(Arquillian.class)
public class ChangementTest {

	@Test
	public void test(){}
	
//	private Logger logger = LoggerFactory.getLogger(ChangementTest.class);
//
//    public Logger getLogger() {
//        return logger;
//    } 
//    private static JPAContainer<Changement> changements;
//    
//    @Deployment
//    public static JavaArchive createDeployment() {
//        return ShrinkWrap.create(JavaArchive.class)
//            .addClass(Changement.class)
//            .addAsManifestResource("META-INF/beans.xml", ArchivePaths.create("beans.xml"))
//            .addAsManifestResource("META-INF/glassfish-ejb-jar.xml", ArchivePaths.create("glassfish-ejb-jar.xml"));
//    }
//    @BeforeClass
//	public static void init(){
//		changements = JPAContainerFactory.makeJndi(Changement.class);
//		
//	}
//	
//	@Test
//	public void setTitleTest(){
//		getLogger().info("changements : "+changements);
//		int i = 0;
//		while (changements.getItemIds().iterator().hasNext()){
//			getLogger().info("changement title : "			+changements.getItem(i).getEntity().getTitle());
//			getLogger().info("changement description : "	+changements.getItem(i).getEntity().getDescription());
//			i++;
//		}
//		
//	}
	
}
