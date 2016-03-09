/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.module.camerwa;


/**
 *
 */
public class RegimenCategory {
	private int regimenCategoryId;
	private String regimenCategoryName;
    /**
     * @return the regimenCategoryId
     */
    public int getRegimenCategoryId() {
    	return regimenCategoryId;
    }
	
    /**
     * @param regimenCategoryId the regimenCategoryId to set
     */
    public void setRegimenCategoryId(int regimenCategoryId) {
    	this.regimenCategoryId = regimenCategoryId;
    }
	
    /**
     * @return the regimenCategoryName
     */
    public String getRegimenCategoryName() {
    	return regimenCategoryName;
    }
	
    /**
     * @param regimenCategoryName the regimenCategoryName to set
     */
    public void setRegimenCategoryName(String regimenCategoryName) {
    	this.regimenCategoryName = regimenCategoryName;
    }
	
	

}
