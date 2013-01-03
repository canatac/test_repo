package org.ortens.bone.core.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import org.ortens.bone.core.model.BaseEntity;


@Entity
public class Changement extends BaseEntity implements Serializable{
        /**
	 * 
	 */
	private static final long serialVersionUID = -4093921824703725930L;
		private static final    int TITLE_COLUMN_LENGTH            =   25;
        private static final    int DESCRIPTION_COLUMN_LENGTH      =   25;
        private static final    int TITLE_MAX_COLUMN_SIZE          =   25;
        private static final    int DESCRIPTION_MAX_COLUMN_SIZE    =   25;
        
        private String displayText;
        
	@Column(length = TITLE_COLUMN_LENGTH, nullable = false)
	@Size(max = TITLE_MAX_COLUMN_SIZE)
	private String title;

//	@ManyToMany(mappedBy = "changements",targetEntity=Livraison.class,cascade=CascadeType.REMOVE)
	private Set<Livraison> livrables = new HashSet<Livraison>(0);

//	@ManyToMany(mappedBy = "travaux",targetEntity=Demand.class,cascade=CascadeType.REMOVE)
	private Set<Demand> demandes = new HashSet<Demand>(0);
	
	public Changement(String title, String description){
		this.title=title;
		this.setDescription(description);
	}

	public Changement() {
	}


    
    public String getDisplayText() {
        
        return displayText;
    }    

    public void setDisplayText(String text){
    	
    	this.displayText = text;
    }


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}

	@ManyToMany(mappedBy = "travaux",targetEntity=Demand.class,cascade=CascadeType.ALL)
	public Set<Demand> getDemandes() {
		return demandes;
	}


	public void setDemandes(Set<Demand> demandes) {
		this.demandes = demandes;
	}


	public void setGpa(float f) {
		// TODO Auto-generated method stub
		
	}
	@ManyToMany(mappedBy = "changements",targetEntity=Livraison.class,cascade=CascadeType.ALL)
	public Set<Livraison> getLivrables() {
		return livrables;
	}

	public void setLivrables(Set<Livraison> livrables) {
		this.livrables = livrables;
	}







	
}
