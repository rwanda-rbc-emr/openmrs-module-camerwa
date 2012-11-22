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

import java.util.HashSet;
import java.util.Set;

import org.openmrs.Drug;


/**
 *
 */
public class RegimenComposition {
	
	private int regimenId;
	private String regimenCategory;
	private String regimenName;
	private int drugConceptId1;
	private int drugConceptId2;
	private int drugConceptId3;
	private int drugConceptId4;

	
    
    /**
     * @return the drugConceptId4
     */
    public int getDrugConceptId4() {
    	return drugConceptId4;
    }

	
    /**
     * @param drugConceptId4 the drugConceptId4 to set
     */
    public void setDrugConceptId4(int drugConceptId4) {
    	this.drugConceptId4 = drugConceptId4;
    }

	/**
     * @return the regimenId
     */
    public int getRegimenId() {
    	return regimenId;
    }
	
    /**
     * @param regimenId the regimenId to set
     */
    public void setRegimenId(int regimenId) {
    	this.regimenId = regimenId;
    }
	
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
     * @return the drugConceptId1
     */
    public int getDrugConceptId1() {
    	return drugConceptId1;
    }
	
    /**
     * @param drugConceptId1 the drugConceptId1 to set
     */
    public void setDrugConceptId1(int drugConceptId1) {
    	this.drugConceptId1 = drugConceptId1;
    }
	
    /**
     * @return the drugConceptId2
     */
    public int getDrugConceptId2() {
    	return drugConceptId2;
    }
	
    /**
     * @param drugConceptId2 the drugConceptId2 to set
     */
    public void setDrugConceptId2(int drugConceptId2) {
    	this.drugConceptId2 = drugConceptId2;
    }
	
    /**
     * @return the drugConceptId3
     */
    public int getDrugConceptId3() {
    	return drugConceptId3;
    }
	
    /**
     * @param drugConceptId3 the drugConceptId3 to set
     */
    public void setDrugConceptId3(int drugConceptId3) {
    	this.drugConceptId3 = drugConceptId3;
    }  

    /**
     * @return the regimenCategory
     */
    public String getRegimenCategory() {
    	return regimenCategory;
    }
	
    /**
     * @param regimenCategory the regimenCategory to set
     */
    public void setRegimenCategory(String regimenCategory) {
    	this.regimenCategory = regimenCategory;
    }


    
}
