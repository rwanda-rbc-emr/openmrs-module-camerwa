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
package org.openmrs.module.camerwa.impl;

import java.text.ParseException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.hibernate.Session;
import org.openmrs.Concept;
import org.openmrs.Drug;
import org.openmrs.Patient;
import org.openmrs.module.camerwa.db.CamerwaDAO;
import org.openmrs.module.camerwa.regimenhistory.RegimenComponent;
import org.openmrs.module.camerwa.service.CamerwaService;

/**
 *
 */
public class CamerwaServiceImpl implements CamerwaService {
	
	
	/**
	 * @see org.openmrs.module.camerwa.service.CamerwaService#getPatientsWithoutIdentifiers()
	 */
	
	private CamerwaDAO camerwaDAO;
	
	public CamerwaDAO getDataQualityDAO() {
		return camerwaDAO;
	}
	
	public void setDataQualityDAO(CamerwaDAO camerwaDAO) {
		this.camerwaDAO = camerwaDAO;
	}

	/**
     * @throws ParseException 
	 * @see org.openmrs.module.camerwa.service.CamerwaService#getPatientsUnderDrug(int, java.util.List)
     */
    @Override
    public HashMap<String, Object> getPatientsUnderDrug(Date date,Date dateFormatedLimite) throws ParseException {
	    
    	
    	
    	return camerwaDAO.getPatientsUnderDrug(date,dateFormatedLimite);
    }
	
    //getPatientsUnderArv(Session session,Date dateFormatedNew)
    
	public List<Integer> getPatientsUnderArv(Date dateFormatedNew,Date dateFormatedLimite){
		return camerwaDAO.getPatientsUnderArv(dateFormatedNew,dateFormatedLimite );
	}

	/**
     * @see org.openmrs.module.camerwa.service.CamerwaService#getActivePatiennts(java.util.List, java.util.List)
     */
    @Override
    public List<Integer> getActivePatients(List<Integer> patientsUnderARVNewList, List<Integer> patientsExitedFromCareList,Date startDate) {
	    // TODO Auto-generated method stub
	    return camerwaDAO.getActivePatients(patientsUnderARVNewList, patientsExitedFromCareList,startDate);
    }

	/**
     * @see org.openmrs.module.camerwa.service.CamerwaService#getPatientsExitedFromCare()
     */
    @Override
    public List<Integer> getPatientsExitedFromCare(Date dateFormatedLimite) {
	    // TODO Auto-generated method stub
	    return camerwaDAO.getPatientsExitedFromCare(dateFormatedLimite);
	    
    }

	/**
     * @see org.openmrs.module.camerwa.service.CamerwaService#getAdultDOBQueryNew(java.util.Date)
     */
    @Override
    public List<Integer> getAdultDOBQueryNew(Date dateFormatedNew,Date dateFormatedLimite) {
	    // TODO Auto-generated method stub
	    return camerwaDAO.getAdultDOBQueryNew(dateFormatedNew,dateFormatedLimite);
    }

	/**
     * @see org.openmrs.module.camerwa.service.CamerwaService#getAllAdultDOBQuery(java.util.Date)
     */
    @Override
    public List<Integer> getAllAdultDOBQuery(Date dateFormatedNew,Date dateFormatedLimite) {
	    // TODO Auto-generated method stub
	    return camerwaDAO.getAllAdultDOBQuery(dateFormatedNew,dateFormatedLimite);
    }

	/**
     * @see org.openmrs.module.camerwa.service.CamerwaService#getAllKidsDOBQuery(java.util.Date)
     */
    @Override
    public List<Integer> getAllKidsDOBQuery(Date dateFormatedNew,Date dateFormatedLimite) {
	    // TODO Auto-generated method stub
	    return camerwaDAO.getAllKidsDOBQuery(dateFormatedNew,dateFormatedLimite);
    }

	/**
     * @see org.openmrs.module.camerwa.service.CamerwaService#getAllPatientsUnderARV(java.util.Date)
     */
    @Override
    public List<Integer> getAllPatientsUnderARV(Date dateFormatedNew,Date dateFormatedLimite) {
	    // TODO Auto-generated method stub
	    return camerwaDAO.getAllPatientsUnderARV(dateFormatedNew,dateFormatedLimite);
    }

	/**
     * @see org.openmrs.module.camerwa.service.CamerwaService#getkidsDOBQueryNew(java.util.Date)
     */
    @Override
    public List<Integer> getkidsDOBQueryNew(Date dateFormatedNew,Date dateFormatedLimite) {
	    // TODO Auto-generated method stub
	    return camerwaDAO.getkidsDOBQueryNew(dateFormatedNew,dateFormatedLimite);
    }

	/**
     * @see org.openmrs.module.camerwa.service.CamerwaService#getAllKidsUnderArvComprime(java.util.Date)
     */
    @Override
    public List<Integer> getAllKidsUnderArvComprime(Date dateFormatedNew,Date dateFormatedLimite) {
	    // TODO Auto-generated method stub
	    return camerwaDAO.getAllKidsUnderArvComprime(dateFormatedNew,dateFormatedLimite);
    }

	/**
     * @see org.openmrs.module.camerwa.service.CamerwaService#getAllKidsUnderArvSiropsLast(java.util.Date)
     */
    @Override
    public List<Integer> getAllKidsUnderArvSiropsLast(Date dateFormatedNew,Date dateFormatedLimite) {
	    // TODO Auto-generated method stub
	    return camerwaDAO.getAllKidsUnderArvSiropsLast(dateFormatedNew,dateFormatedLimite);
    }

	/**
     * @see org.openmrs.module.camerwa.service.CamerwaService#getKidsUnderArvComprimeNew(java.util.Date)
     */
    @Override
    public List<Integer> getKidsUnderArvComprimeNew(Date dateFormatedNew,Date dateFormatedLimite) {
	    // TODO Auto-generated method stub
	    return camerwaDAO.getKidsUnderArvComprimeNew(dateFormatedNew, dateFormatedLimite);
    }

	/**
     * @see org.openmrs.module.camerwa.service.CamerwaService#getKidsUnderArvSiropsNew(java.util.Date)
     */
    @Override
    public List<Integer> getKidsUnderArvSiropsNew(Date dateFormatedNew,Date dateFormatedLimite) {
	    // TODO Auto-generated method stub
	    return camerwaDAO.getKidsUnderArvSiropsNew(dateFormatedNew,dateFormatedLimite);
    }

	/**
     * @see org.openmrs.module.camerwa.service.CamerwaService#getRegimenAdultLast(java.util.Date, int, int, int)
     */
    @Override
    public List<Integer> getRegimenAdultLast(Date dateFormatedNew,Date dateFormatedLimite, Object drugConceptId1, Object drugConceptId2, Object drugConceptId3,Object drugConceptId4) {
	    // TODO Auto-generated method stub
	    return camerwaDAO.getRegimenAdultLast(dateFormatedNew,dateFormatedLimite, drugConceptId1, drugConceptId2, drugConceptId3,drugConceptId4);
    }

	/**
     * @throws ParseException 
	 * @see org.openmrs.module.camerwa.service.CamerwaService#getRegimenAdultNew(java.util.Date, int, int, int)
     */
    @Override
    public List<List<Integer>> getRegimenAdultNew(Date dateFormatedNew,Date dateFormatedLimite, Object drugConceptId1, Object drugConceptId2, Object drugConceptId3,Object drugConceptId4) throws ParseException {
	    // TODO Auto-generated method stub
	    return camerwaDAO.getRegimenAdultNew(dateFormatedNew,dateFormatedLimite, drugConceptId1, drugConceptId2, drugConceptId3,drugConceptId4);
    }

	/**
     * @see org.openmrs.module.camerwa.service.CamerwaService#getPatientsAbandoned()
     */
    //@Override
    /*public List<Integer> getPatientsAbandoned() {
	    // TODO Auto-generated method stub
	    return camerwaDAO.getPatientsAbandoned();
    }

	*//**
     * @see org.openmrs.module.camerwa.service.CamerwaService#getPatientsDied()
     *//*
    @Override
    public List<Integer> getPatientsDied() {
	    // TODO Auto-generated method stub
	    return camerwaDAO.getPatientsDied();
    }

	*//**
     * @see org.openmrs.module.camerwa.service.CamerwaService#getPatientsTransferredIn()
     *//*
    @Override
    public List<Integer> getPatientsTransferredIn() {
	    // TODO Auto-generated method stub
	    return camerwaDAO.getPatientsTransferredIn();
    }

	*//**
     * @see org.openmrs.module.camerwa.service.CamerwaService#getPatientsTransferredOut()
     *//*
    @Override
    public List<Integer> getPatientsTransferredOut() {
	    // TODO Auto-generated method stub
	    return camerwaDAO.getPatientsTransferredOut();
    }
*/
	/**
     * @see org.openmrs.module.camerwa.service.CamerwaService#getPediatricUnderRegimenLast(java.util.Date, java.lang.Object, java.lang.Object, java.lang.Object)
     */
    @Override
    public List<Integer> getPediatricUnderRegimenLast(Date dateFormatedNew,Date dateFormatedLimite, Object drugConceptId1, Object drugConceptId2,
                                                      Object drugConceptId3,Object drugConceptId4) {
	    // TODO Auto-generated method stub
	    return camerwaDAO.getPediatricUnderRegimenLast(dateFormatedNew,dateFormatedLimite, drugConceptId1, drugConceptId2,drugConceptId3,drugConceptId4);
    }

	/**
     * @see org.openmrs.module.camerwa.service.CamerwaService#getPediatricUnderRegimenNew(java.util.Date, java.lang.Object, java.lang.Object, java.lang.Object)
     */
    @Override
    public List<List<Integer>> getPediatricUnderRegimenNew(Date dateFormatedNew,Date dateFormatedLimite, Object drugConceptId1, Object drugConceptId2,
                                                     Object drugConceptId3,Object drugConceptId4) {
	    // TODO Auto-generated method stub
	    return camerwaDAO.getPediatricUnderRegimenNew(dateFormatedNew,dateFormatedLimite, drugConceptId1,drugConceptId2,drugConceptId3,drugConceptId4);
    }

	/**
     * @see org.openmrs.module.camerwa.service.CamerwaService#getPediatricUnderRegimenLastOneDrug(java.util.Date, java.lang.Object)
     */
    @Override
    public List<Integer> getPediatricUnderRegimenLastOneDrug(Date dateFormatedNew,Date dateFormatedLimite, Object drugConceptId1) {
	    // TODO Auto-generated method stub
	    return camerwaDAO.getPediatricUnderRegimenLastOneDrug(dateFormatedNew,dateFormatedLimite, drugConceptId1);
    }

	/**
     * @see org.openmrs.module.camerwa.service.CamerwaService#getPediatricUnderRegimenNewOneDrug(java.util.Date, java.lang.Object)
     */
    @Override
    public List<Integer> getPediatricUnderRegimenNewOneDrug(Date dateFormatedNew,Date dateFormatedLimite, Object drugConceptId1) {
	    // TODO Auto-generated method stub
	    return camerwaDAO.getPediatricUnderRegimenNewOneDrug(dateFormatedNew,dateFormatedLimite, drugConceptId1);
    }

	/**
     * @see org.openmrs.module.camerwa.service.CamerwaService#getRegimenAdultLastOneDrug(java.util.Date, java.lang.Object)
     */
    @Override
    public List<Integer> getRegimenAdultLastOneDrug(Date dateFormatedNew,Date dateFormatedLimite, Object drugConceptId1) {
	    // TODO Auto-generated method stub
	    return camerwaDAO.getRegimenAdultLastOneDrug(dateFormatedNew,dateFormatedLimite, drugConceptId1);
    }

	/**
     * @see org.openmrs.module.camerwa.service.CamerwaService#getRegimenAdultNewForOneDrug(java.util.Date, java.lang.Object)
     */
    @Override
    public List<Integer> getRegimenAdultNewForOneDrug(Date dateFormatedNew,Date dateFormatedLimite, Object drugConceptId1) {
	    // TODO Auto-generated method stub
	    return camerwaDAO.getRegimenAdultNewForOneDrug(dateFormatedNew,dateFormatedLimite, drugConceptId1);
    }

	/**
     * @see org.openmrs.module.camerwa.service.CamerwaService#getPatientsExitedFromCareDefinedLast(int, java.util.Date)
     */
    @Override
    public List<Integer> getPatientsExitedFromCareDefinedLast(int concept, Date dateFormatedNew) {
	    // TODO Auto-generated method stub
	    return camerwaDAO.getPatientsExitedFromCareDefinedLast(concept, dateFormatedNew);
    }

	/**
     * @see org.openmrs.module.camerwa.service.CamerwaService#getPatientsExitedFromCareDefinedNew(int, java.util.Date)
     */
    @Override
    public List<Integer> getPatientsExitedFromCareDefinedNew(int concept, Date dateFormatedNew,Date dateFormatedLimite) {
	    // TODO Auto-generated method stub
    	return camerwaDAO.getPatientsExitedFromCareDefinedNew(concept,dateFormatedNew,dateFormatedLimite);
    }

	/**
     * @see org.openmrs.module.camerwa.service.CamerwaService#union(java.util.Collection, java.util.Collection)
     */
    @Override
    public Collection union(Collection coll1, Collection coll2) {
	    // TODO Auto-generated method stub
	    return camerwaDAO.union(coll1,coll2);
    }

	/**
     * @see org.openmrs.module.camerwa.service.CamerwaService#getPediatricUnderSecondLineRegimenLast(java.util.Date, java.lang.Object, java.lang.Object, java.lang.Object)
     */
    @Override
    public List<Integer> getPediatricUnderSecondLineRegimenLast(Date dateFormatedNew,Date dateFormatedLimite, Object drugConceptId1,
                                                                Object drugConceptId2, Object drugConceptId3,Object drugConceptId4) {
	    // TODO Auto-generated method stub
	    return camerwaDAO.getPediatricUnderSecondLineRegimenLast(dateFormatedNew,dateFormatedLimite, drugConceptId1,drugConceptId2,drugConceptId3,drugConceptId4);
    }

	/**
     * @see org.openmrs.module.camerwa.service.CamerwaService#getPediatricUnderSecondLineRegimenNew(java.util.Date, java.lang.Object, java.lang.Object, java.lang.Object)
     */
    @Override
    public List<List<Integer>> getPediatricUnderSecondLineRegimenNew(Date dateFormatedNew,Date dateFormatedLimite, Object drugConceptId1,
                                                               Object drugConceptId2, Object drugConceptId3,Object drugConceptId4) {
	    // TODO Auto-generated method stub
	    return camerwaDAO.getPediatricUnderSecondLineRegimenNew(dateFormatedNew,dateFormatedLimite, drugConceptId1,drugConceptId2,drugConceptId3,drugConceptId4);
    }

	/**
     * @see org.openmrs.module.camerwa.service.CamerwaService#treeCollectionsIntersection(java.util.Collection, java.util.Collection, java.util.Collection)
     */
    @Override
    public Collection treeCollectionsIntersection(Collection coll1, Collection coll2, Collection coll3) {
	    // TODO Auto-generated method stub
	    return camerwaDAO.treeCollectionsIntersection(coll1,coll2,coll3);
    }

	/**
     * @see org.openmrs.module.camerwa.service.CamerwaService#getPediatricUnderSecondLineNewOneDrug(java.util.Date, java.lang.Object)
     */
    @Override
    public List<Integer> getPediatricUnderSecondLineNewOneDrug(Date dateFormatedNew,Date dateFormatedLimite, Object drugConceptId1) {
	    // TODO Auto-generated method stub
	    return camerwaDAO.getPediatricUnderSecondLineNewOneDrug(dateFormatedNew,dateFormatedLimite, drugConceptId1);
    }

	
    @Override
    public List<Integer> getPediatricUnderSecondLineLastOneDrug(Date dateFormatedNew,Date dateFormatedLimite, Object drugConceptId1) {
	    // TODO Auto-generated method stub
	    return camerwaDAO.getPediatricUnderSecondLineLastOneDrug(dateFormatedNew,dateFormatedLimite, drugConceptId1);
    }
    
    public String getRegimensAsString(Set<RegimenComponent> regimens){
    	return camerwaDAO.getRegimensAsString(regimens);
    	
    }
    
    public List<List<Integer>> getAdultUnderSecondLineRegimenNew(Date dateFormatedNew,Date dateFormatedLimite, Object drugConceptId1, Object drugConceptId2,Object drugConceptId3,Object drugConceptId4){
    	return camerwaDAO.getAdultUnderSecondLineRegimenNew(dateFormatedNew, dateFormatedLimite,drugConceptId1, drugConceptId2,drugConceptId3,drugConceptId4);
    }
    public List<Integer> getAdultUnderSecondLineRegimenLast(Date dateFormatedNew,Date dateFormatedLimite, Object drugConceptId1, Object drugConceptId2,Object drugConceptId3,Object drugConceptId4){
    	return camerwaDAO.getAdultUnderSecondLineRegimenLast(dateFormatedNew,dateFormatedLimite,drugConceptId1,drugConceptId2,drugConceptId3,drugConceptId4);
    }
    public Boolean isAnyArvDrugOld(Integer patientId,Date dateFormatedNew) throws ParseException{
    	return camerwaDAO.isAnyArvDrugOld(patientId, dateFormatedNew);
    }
	
    @Override
    public List<Integer> getPatientsUnderProphylaxieLast(Date dateFormatedNew, Date dateFormatedLimite,
                                                         Object drugConceptId1, Object drugConceptId2,
                                                         Object drugConceptId3, Object drugConceptId4) {
	    // TODO Auto-generated method stub
	    return camerwaDAO.getPatientsUnderProphylaxieLast(dateFormatedNew, dateFormatedLimite, drugConceptId1, drugConceptId2, drugConceptId3, drugConceptId4);
    }

	
    @Override
    public List<List<Integer>> getPatientsUnderProphylaxieNew(Date dateFormatedNew, Date dateFormatedLimite,
                                                        Object drugConceptId1, Object drugConceptId2, Object drugConceptId3,
                                                        Object drugConceptId4) {
	    // TODO Auto-generated method stub
	    return camerwaDAO.getPatientsUnderProphylaxieNew(dateFormatedNew, dateFormatedLimite, drugConceptId1, drugConceptId2, drugConceptId3, drugConceptId4) ;
    }

	
    @Override
    public List<Integer> getPatientsUnderPmtctLast(Date dateFormatedNew, Date dateFormatedLimite, Object drugConceptId1,
                                                   Object drugConceptId2, Object drugConceptId3, Object drugConceptId4) {
	    // TODO Auto-generated method stub
	    return camerwaDAO.getPatientsUnderPmtctLast(dateFormatedNew,dateFormatedLimite,drugConceptId1,drugConceptId2,drugConceptId3,drugConceptId4);
    }

    @Override
    public List<List<Integer>> getPatientsUnderPmtctNew(Date dateFormatedNew, Date dateFormatedLimite, Object drugConceptId1,
                                                  Object drugConceptId2, Object drugConceptId3, Object drugConceptId4) {
	    // TODO Auto-generated method stub
	    return camerwaDAO.getPatientsUnderPmtctNew(dateFormatedNew,dateFormatedLimite,drugConceptId1,drugConceptId2, drugConceptId3, drugConceptId4);
    }
    public List<String> getLocations(){
    	return camerwaDAO.getLocations();
    }
    public List<Integer> removeLostFollowUp(List<Integer> patientIds, Date startDate){
    	return camerwaDAO.removeLostFollowUp(patientIds,  startDate);
    }

	/**
     * @see org.openmrs.module.camerwa.service.CamerwaService#getLastEncouterDate(org.openmrs.Patient)
     */
    @Override
    public List<Date> getLastEncouterDate(Patient patient) {
	    // TODO Auto-generated method stub
	    return camerwaDAO.getLastEncouterDate(patient);
    }

	/**
     * @see org.openmrs.module.camerwa.service.CamerwaService#getLastVisiteDate(org.openmrs.Patient)
     */
    @Override
    public List<Date> getLastVisiteDate(Patient patient) {
	    // TODO Auto-generated method stub
	    return camerwaDAO.getLastVisiteDate(patient);
    }

	/**
     * @see org.openmrs.module.camerwa.service.CamerwaService#getPatientsUnderPediatricSiropLast(java.util.Date, java.util.Date, java.lang.Object, java.lang.Object, java.lang.Object, java.lang.Object)
     */
    @Override
    public List<Integer> getPatientsUnderPediatricSiropLast(Date dateFormatedNew, Date dateFormatedLimite,
                                                            Object drugConceptId1, Object drugConceptId2,
                                                            Object drugConceptId3, Object drugConceptId4) {
	    // TODO Auto-generated method stub
	    return camerwaDAO.getPatientsUnderPediatricSiropLast(dateFormatedNew,dateFormatedLimite,drugConceptId1,  drugConceptId2,drugConceptId3, drugConceptId4);
    }

	/**
     * @see org.openmrs.module.camerwa.service.CamerwaService#getPatientsUnderPediatricSiropNew(java.util.Date, java.util.Date, java.lang.Object, java.lang.Object, java.lang.Object, java.lang.Object)
     */
    @Override
    public List<List<Integer>> getPatientsUnderPediatricSiropNew(Date dateFormatedNew, Date dateFormatedLimite,
                                                           Object drugConceptId1, Object drugConceptId2,
                                                           Object drugConceptId3, Object drugConceptId4) {
	    // TODO Auto-generated method stub
	    return camerwaDAO.getPatientsUnderPediatricSiropNew( dateFormatedNew, dateFormatedLimite,drugConceptId1, drugConceptId2,drugConceptId3, drugConceptId4);
    }
    public List<Integer> getPatientOnOnlyGivenDrugs(List<Integer> listDrugIds, List<Integer> patientIds){
    	return camerwaDAO.getPatientOnOnlyGivenDrugs( listDrugIds, patientIds);
    }
    public List<Integer> getRegimenCompositionByName(String regimenName){
    	return camerwaDAO.getRegimenCompositionByName(regimenName);
    }
    public List<String> getRegimenCategories() {
    	return camerwaDAO.getRegimenCategories();
    }
    //public List<String> getArvDrugs();
    public List<String> getArvDrugs() {
    	return camerwaDAO.getArvDrugs();
    }
    public Integer getConceptIdByDrugName(String DrugName) {
    	return camerwaDAO.getConceptIdByDrugName(DrugName);
    }
    public Boolean insertRegimen(String regimen_category,String regimen_name,Object drug_concept_id1,Object drug_concept_id2,Object drug_concept_id3,Object drug_concept_id4){
    	return camerwaDAO.insertRegimen(regimen_category,regimen_name,drug_concept_id1,drug_concept_id2,drug_concept_id3,drug_concept_id4);
    }
    
    public List<String> getRegimenNames(){
    	try {
    		return camerwaDAO.getRegimenNames();
        }
        catch (Exception e) {
	        
        }
    	
    	return null;
    }
    public Boolean deleteRegimen(String regimenCategory,String regimenName) {
    	return camerwaDAO.deleteRegimen(regimenCategory, regimenName);
    }
    @Override
    public List<List<Integer>> getPatientsUnderRegimenAdultThirdLineNew(Date dateFormatedNew, Date dateFormatedLimite,
                                                            Object drugConceptId1, Object drugConceptId2,
                                                            Object drugConceptId3, Object drugConceptId4) {
	    // TODO Auto-generated method stub
	    return camerwaDAO.getPatientsUnderRegimenAdultThirdLineNew(dateFormatedNew,dateFormatedLimite,drugConceptId1,  drugConceptId2,drugConceptId3, drugConceptId4);
    } 
    @Override
    public List<Integer> getPatientsUnderRegimenAdultThirdLineLast(Date dateFormatedNew, Date dateFormatedLimite,
                                                            Object drugConceptId1, Object drugConceptId2,
                                                            Object drugConceptId3, Object drugConceptId4) {
	    // TODO Auto-generated method stub
	    return camerwaDAO.getPatientsUnderRegimenAdultThirdLineLast(dateFormatedNew,dateFormatedLimite,drugConceptId1,  drugConceptId2,drugConceptId3, drugConceptId4);
    }
    public List<Integer> getOldPatientPerDate(Date date){
     return camerwaDAO.getOldPatientPerDate(date);	
    }
    public Collection twoCollectionsIntersection(Collection coll1, Collection coll2){
    	return camerwaDAO.twoCollectionsIntersection(coll1, coll2);
    }
    public Date getWhenPatientStarted(Patient patient){
    	return camerwaDAO.getWhenPatientStarted(patient);
    }
}
