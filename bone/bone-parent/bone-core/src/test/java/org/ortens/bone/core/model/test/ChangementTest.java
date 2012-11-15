/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ortens.bone.core.model.test;

import org.junit.BeforeClass;
import org.junit.Test;
import org.ortens.bone.core.bean.ChangementEntityProvider;
import org.ortens.bone.core.model.Changement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.addon.jpacontainer.fieldfactory.FieldFactory;
import com.vaadin.ui.Table;

/**
 *
 * @author canatac
 */
public class ChangementTest {

	private Logger logger = LoggerFactory.getLogger(ChangementTest.class);

    public Logger getLogger() {
        return logger;
    } 
    private static JPAContainer<Changement> changements;
    private ChangementEntityProvider changementEjbEntityProvider;
    private FieldFactory fieldFactory;
	
    @BeforeClass
	public static void init(){
		changements = JPAContainerFactory.makeJndi(Changement.class);
	}
	
	@Test
	public void setTitleTest(){
		getLogger().info("changements : "+changements);
		int i = 0;
		for (Changement changement : )){
			getLogger().info("changement title : "			+changement.getTitle());
			getLogger().info("changement description : "	+changement.getDescription());	
		}
		
	}
	
}
