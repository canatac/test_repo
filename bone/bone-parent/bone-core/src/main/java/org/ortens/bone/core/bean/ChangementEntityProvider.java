package org.ortens.bone.core.bean;

import javax.ejb.Stateless;

import org.ortens.bone.core.model.Changement;

@Stateless
public class ChangementEntityProvider extends EjbEntityProvider<Changement> {

    public ChangementEntityProvider() {
        super(Changement.class);
    }
	    
}
