package org.ortens.bone.core.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.validation.constraints.Size;

import org.ortens.bone.core.model.BaseEntity;


@Entity
public class Changement extends BaseEntity implements Serializable{
        private static final    int TITLE_COLUMN_LENGTH            =   25;
        private static final    int DESCRIPTION_COLUMN_LENGTH      =   25;
        private static final    int TITLE_MAX_COLUMN_SIZE          =   25;
        private static final    int DESCRIPTION_MAX_COLUMN_SIZE    =   25;
        
	@Column(length = TITLE_COLUMN_LENGTH, nullable = false)
	@Size(max = TITLE_MAX_COLUMN_SIZE)
	private String title;

	@ManyToMany(targetEntity = Livraison.class, mappedBy = "changements", cascade = CascadeType.ALL)
	private Set<Livraison> livrables;

	@ManyToMany(targetEntity = Demand.class, mappedBy = "travaux", cascade = CascadeType.ALL)
	private Set<Demand> demandes;
	
	public Changement(String title, String description){
		this.title=title;
		this.setDescription(description);
	}

	public Changement() {
	}

	@Override
	public String getDisplayText() {
		// TODO Auto-generated method stub
		return null;
	}


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public Set<Demand> getDemandes() {
		return demandes;
	}


	public void setDemandes(Set<Demand> demandes) {
		this.demandes = demandes;
	}


	public void setGpa(float f) {
		// TODO Auto-generated method stub
		
	}

	public Set<Livraison> getLivrables() {
		return livrables;
	}

	public void setLivrables(Set<Livraison> livrables) {
		this.livrables = livrables;
	}







	
}
