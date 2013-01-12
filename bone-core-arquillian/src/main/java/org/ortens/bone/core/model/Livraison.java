package org.ortens.bone.core.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.Size;
//import org.hibernate.validator.constraints.NotEmpty;

import org.ortens.bone.core.model.GenericEntity;

@Entity
public class Livraison extends GenericEntity implements Serializable{
        /**
	 * 
	 */
	private static final long serialVersionUID = -8899105705906692579L;
		private static final    int TITLE_COLUMN_LENGTH            =   25;
        private static final    int CHANGEMENTS_COLUMN_LENGTH      =   25;
        private static final    int TITLE_MAX_COLUMN_SIZE          =   25;
        private static final    int DESCRIPTION_MAX_COLUMN_SIZE    =   25;
    
	@Column(length = TITLE_COLUMN_LENGTH, nullable = false)
	@Size(max = TITLE_MAX_COLUMN_SIZE)
//	@NotEmpty(message = "title is required")
	private String title;


//	@ManyToMany()
    @ManyToMany()
	private Set<Changement> changements = new HashSet<Changement>(0);
	
    @Id
	private String displayText;
    
	public Livraison() {
		// TODO Auto-generated constructor stub
	}
	
	public Livraison(String title, String description) {
		super();
		this.title = title;
		this.setDescription(description);
		this.setDisplayText(description);
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



		public Set<Changement> getChangements() {
		return changements;
	}


	public void setChangements(Set<Changement> changements) {
		this.changements = changements;
	}
	
}
