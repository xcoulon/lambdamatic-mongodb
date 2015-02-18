/**
 * 
 */
package org.lambdamatic.mongodb.metadata;


/**
 * Base annotation for all types of document fields.
 * 
 * @author Xavier Coulon <xcoulon@redhat.com>
 *
 */
public class ProjectionField {

	/** the name of the field in MongoDB. */
	private final String fieldName;

	/**
	 * Constructor
	 * @param fieldName the name of the field in MongoDB.
	 */
	public ProjectionField(String fieldName) {
		super();
		this.fieldName = fieldName;
	}
	
	/**
	 * @return the name of the field in MongoDB
	 */
	public String getFieldName() {
		return fieldName;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fieldName == null) ? 0 : fieldName.hashCode());
		return result;
	}

	
	
	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ProjectionField [fieldName=" + fieldName + "]";
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProjectionField other = (ProjectionField) obj;
		if (fieldName == null) {
			if (other.fieldName != null)
				return false;
		} else if (!fieldName.equals(other.fieldName))
			return false;
		return true;
	}
	
}
