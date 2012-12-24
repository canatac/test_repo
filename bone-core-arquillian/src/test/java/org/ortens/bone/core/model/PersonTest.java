package org.ortens.bone.core.model;

import javax.inject.Inject;

import org.ortens.bone.core.model.BaseEntity;
import org.ortens.bone.core.model.Person;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class PersonTest {
	
	@Deployment
	public static JavaArchive createDeployment(){
		
		JavaArchive jar = ShrinkWrap.create(JavaArchive.class)
			.addClasses(Person.class, BaseEntity.class)
			.addAsManifestResource(EmptyAsset.INSTANCE,"beans.xml");
		System.out.println(jar.toString(true));
	    return jar;
	    
	}
	
	@Inject
	Person person;
	
    @Test
    public void should_create_description() {
    	person.setDescription("toto");
    	System.out.println(person.getDescription());
    }
}