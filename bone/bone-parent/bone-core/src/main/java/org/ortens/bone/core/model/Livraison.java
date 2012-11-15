package org.ortens.bone.core.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.Size;
//import org.hibernate.validator.constraints.NotEmpty;

@Entity
public class Livraison extends BaseEntity implements Serializable{
        private static final    int TITLE_COLUMN_LENGTH            =   25;
        private static final    int DESCRIPTION_COLUMN_LENGTH      =   25;
        private static final    int TITLE_MAX_COLUMN_SIZE          =   25;
        private static final    int DESCRIPTION_MAX_COLUMN_SIZE    =   25;
    
	@Column(length = TITLE_COLUMN_LENGTH, nullable = false)
	@Size(max = TITLE_MAX_COLUMN_SIZE)
//	@NotEmpty(message = "title is required")
	private String title;

	@Column(length = DESCRIPTION_COLUMN_LENGTH, nullable = false)
	@Size(max = DESCRIPTION_MAX_COLUMN_SIZE)
	private String description;
	
	private List<Changement> changements = new ArrayList<Changement>();
	
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


	public List<Changement> getChangements() {
		return changements;
	}


	public void setChangements(List<Changement> changements) {
		this.changements = changements;
	}
	
}
