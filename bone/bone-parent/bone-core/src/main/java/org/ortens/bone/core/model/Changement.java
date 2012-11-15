package org.ortens.bone.core.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.validation.constraints.Size;


@Entity
public class Changement extends BaseEntity implements Serializable{
        private static final    int TITLE_COLUMN_LENGTH            =   25;
        private static final    int DESCRIPTION_COLUMN_LENGTH      =   25;
        private static final    int TITLE_MAX_COLUMN_SIZE          =   25;
        private static final    int DESCRIPTION_MAX_COLUMN_SIZE    =   25;
        
	@Column(length = TITLE_COLUMN_LENGTH, nullable = false)
	@Size(max = TITLE_MAX_COLUMN_SIZE)
	private String title;

	@Column(length = DESCRIPTION_COLUMN_LENGTH, nullable = false)
	@Size(max = DESCRIPTION_MAX_COLUMN_SIZE)
	private String description;
	
	@ManyToMany(mappedBy = "changements", fetch = FetchType.LAZY)
	private List<Livraison> livrables = new ArrayList<Livraison>();

	@ManyToMany(mappedBy = "travaux", fetch = FetchType.LAZY)
	private List<Demand> demandes = new ArrayList<Demand>();
	
	public Changement(String title, String description){
		this.title=title;
		this.description=description;
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


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public List<Livraison> getLivraisons() {
		return livrables;
	}


	public void setLivraisons(List<Livraison> livraisons) {
		this.livrables = livraisons;
	}


	public List<Demand> getDemandes() {
		return demandes;
	}


	public void setDemandes(List<Demand> demandes) {
		this.demandes = demandes;
	}


	public void setGpa(float f) {
		// TODO Auto-generated method stub
		
	}







	
}
