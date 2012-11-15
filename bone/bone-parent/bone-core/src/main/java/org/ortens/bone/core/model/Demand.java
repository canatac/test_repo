package org.ortens.bone.core.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.Size;
//import org.hibernate.validator.constraints.NotEmpty;

@Entity
//<editor-fold defaultstate="collapsed" desc="comment">
public
        //</editor-fold>
 class Demand extends BaseEntity implements Serializable {
        private static final    int TITLE_COLUMN_LENGTH            =   25;
        private static final    int DESCRIPTION_COLUMN_LENGTH      =   25;
        private static final    int TITLE_MAX_COLUMN_SIZE          =   25;
        private static final    int DESCRIPTION_MAX_COLUMN_SIZE    =   25;
 
	
	@Column(length = TITLE_COLUMN_LENGTH, nullable = false)
	@Size(max = TITLE_MAX_COLUMN_SIZE)
//	@NotEmpty(message = "title is required")
        @Id
	private String title;

	@Column(length = DESCRIPTION_COLUMN_LENGTH, nullable = false)
	@Size(max = DESCRIPTION_MAX_COLUMN_SIZE)
	private String description;


	private List<Changement> travaux = new ArrayList<Changement>();
	
	public Demand() {
		// TODO Auto-generated constructor stub
	}

	public Demand(String title, String description) {
		super();
		this.title = title;
		this.description = description;
	}

	public String getTitleFull() {
		return title + " " + description;
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
		return description + ", " + title;
	}

	public List<Changement> getChangements() {
		return travaux;
	}

	public void setChangements(List<Changement> changements) {
		this.travaux = changements;
	}

}
