package org.ortens.bone.core.model;

import java.util.Date;
import javax.persistence.*;

/**
 * This is the base {@link MappedSuperclass} for all the entities in the
 * application. It implements the Id and the created and modified time stamps.
 * It also includes the callback methods for populating the timestamp values,
 * and implements hashcode and equals based on the id.
 * <p/>
 * The abstract {@link BaseEntity#getDisplayText()} method provides a nice way
 * to get a textual representation of who or what the entity is (course or
 * person name). Since it is baked into the superclass, it will be available for
 * all entity classes.
 *
 * @author Andy Gibson
 *
 */
@MappedSuperclass
public abstract class BaseEntity {

    private Long id;
    private Date createdOn;
    private Date modifiedOn;
    private String description;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Temporal(TemporalType.TIMESTAMP)
    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    @Temporal(TemporalType.TIMESTAMP)
    public Date getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(Date modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    @Transient
    public abstract String getDisplayText();

    @PrePersist
    public void initTimeStamps() {
        // we do this for the purpose of the demo, this lets us create our own
        // creation dates. Typically we would just set the createdOn field.
        if (createdOn == null) {
            createdOn = new Date();
        }
        modifiedOn = createdOn;
    }

    @PreUpdate
    public void updateTimeStamp() {
        modifiedOn = new Date();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime
                * result
                + ((getDisplayText() == null) ? 0 : getDisplayText().hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        BaseEntity other = (BaseEntity) obj;
        if (id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!id.equals(other.id)) {
            return false;
        }
        return true;
    }

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}