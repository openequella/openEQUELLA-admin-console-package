package org.apereo.openequella.adminconsole;

public class JarMetadata {
	private String etag;
	private String modifiedDate;
	private String expiryDate;

	/**
	 * @return the etag
	 */
	public String getEtag() {
		return etag;
	}

	/**
	 * @return the expiryDate
	 */
	public String getExpiryDate() {
		return expiryDate;
	}

	/**
	 * @param expiryDate the expiryDate to set
	 */
	public void setExpiryDate(String expiryDate) {
		this.expiryDate = expiryDate;
	}

	/**
	 * @return the modifiedDate
	 */
	public String getModifiedDate() {
		return modifiedDate;
	}

	/**
	 * @param modifiedDate the modifiedDate to set
	 */
	public void setModifiedDate(String modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	/**
	 * @param etag the etag to set
	 */
	public void setEtag(String etag) {
		this.etag = etag;
	}
}