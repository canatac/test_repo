package org.ortens.bone.core.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.validation.constraints.Size;
//import org.hibernate.validator.constraints.NotEmpty;

import org.ortens.bone.core.model.BaseEntity;

@Entity
//<editor-fold defaultstate="collapsed" desc="comment">
public
        //</editor-fold>
 class Demand extends BaseEntity implements Serializable {
     /**
	 * 
	 */
		private static final long serialVersionUID = -6914340844650825890L;
		private static final    int TITLE_COLUMN_LENGTH            =   25;
        private static final    int DESCRIPTION_COLUMN_LENGTH      =   25;
        private static final    int TITLE_MAX_COLUMN_SIZE          =   25;
        private static final    int DESCRIPTION_MAX_COLUMN_SIZE    =   25;
 
	
	@Column(length = TITLE_COLUMN_LENGTH, nullable = false)
	@Size(max = TITLE_MAX_COLUMN_SIZE)
//	@NotEmpty(message = "title is required")
        //@Id
	private String title;



	private Set<Changement> travaux;
	
	public Demand() {
		// TODO Auto-generated constructor stub
	}

	public Demand(String title, String description) {
		super();
		this.title = title;
	}

	public String getTitleFull() {
		return title + " " + this.getDescription();
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}


	@Override
	public String getDisplayText() {
		return getTitleFull();
	}

	@Override
	public String toString() {

		String exp = "%s [id = %d title = %s ,description = %s]";
		return String.format(exp, super.toString(), getId(), getTitleFull(),
				getDescription());
	}

	public String getOrderedTitleFullDescription() {
		return getDescription() + ", " + title;
	}


	public Set<Changement> getTravaux() {
		return travaux;
	}

	public void setTravaux(Set<Changement> travaux) {
		this.travaux = travaux;
	}

}
