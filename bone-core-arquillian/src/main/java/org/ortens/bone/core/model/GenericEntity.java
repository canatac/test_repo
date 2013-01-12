/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ortens.bone.core.model;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.persistence.*;
import org.ortens.bone.core.ejbjpa.GenericDao;

/**
 *
 * @author canatac
 */
@MappedSuperclass
public abstract class GenericEntity {
    public static Logger _logger = Logger
			.getLogger(GenericEntity.class.getName());
    private Long id;
    private Date createdOn;
    private Date modifiedOn;
    private String description;
    private List<GenericEntity> children;
    
    @Inject
    private GenericDao<GenericEntity> genericEntityDao;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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
        GenericEntity other = (GenericEntity) obj;
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

	public List<GenericEntity> getChildren(GenericEntity entityClass) {

		children = genericEntityDao.getList(entityClass);

		return children;
	}
	
}
