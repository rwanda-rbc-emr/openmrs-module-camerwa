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
public class RegimenResult {

	private String regimenName;
	private int regimenNew;
	private int regimenLast;
	private int totalRegimen;
    /**
     * @return the regimenName
     */
    public String getRegimenName() {
    	return regimenName;
    }
	
    /**
     * @param regimenName the regimenName to set
     */
    public void setRegimenName(String regimenName) {
    	this.regimenName = regimenName;
    }
	
    /**
     * @return the regimenNew
     */
    public int getRegimenNew() {
    	return regimenNew;
    }
	
    /**
     * @param regimenNew the regimenNew to set
     */
    public void setRegimenNew(int regimenNew) {
    	this.regimenNew = regimenNew;
    }
	
    /**
     * @return the regimenLast
     */
    public int getRegimenLast() {
    	return regimenLast;
    }
	
    /**
     * @param regimenLast the regimenLast to set
     */
    public void setRegimenLast(int regimenLast) {
    	this.regimenLast = regimenLast;
    }
	
    /**
     * @return the totalRegimen
     */
    public int getTotalRegimen() {
    	return totalRegimen;
    }
	
    /**
     * @param totalRegimen the totalRegimen to set
     */
    public void setTotalRegimen(int totalRegimen) {
    	this.totalRegimen = totalRegimen;
    }
	
    
	
	
}
