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
package org.openmrs.module.camerwa.service;

import java.text.ParseException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.hibernate.Session;
import org.openmrs.Drug;
import org.openmrs.Patient;
import org.openmrs.module.camerwa.regimenhistory.RegimenComponent;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 */
@Transactional
public interface CamerwaService {

	
 public	HashMap<String, Object> getPatientsUnderDrug(Date date,Date dateFormatedLimite) throws ParseException;	
 public List<Integer> getPatientsUnderArv(Date dateFormatedNew,Date dateFormatedLimite);
 public List<Integer> getActivePatients(List<Integer> patientsUnderARVNewList, List<Integer> patientsExitedFromCareList,Date startDate);
 public List<Integer> getPatientsExitedFromCare(Date dateFormatedLimite);
 public List<Integer> getAllPatientsUnderARV(Date dateFormatedNew,Date dateFormatedLimite);
 public List<Integer> getAdultDOBQueryNew(Date dateFormatedNew,Date dateFormatedLimite);
 public List<Integer> getAllAdultDOBQuery(Date dateFormatedNew,Date dateFormatedLimite);
 public List<Integer> getkidsDOBQueryNew(Date dateFormatedNew,Date dateFormatedLimite);
 public List<Integer> getAllKidsDOBQuery(Date dateFormatedNew,Date dateFormatedLimite);
 public List<Integer> getKidsUnderArvComprimeNew(Date dateFormatedNew,Date dateFormatedLimite);
 public List<Integer> getAllKidsUnderArvComprime(Date dateFormatedNew,Date dateFormatedLimite);
 public List<Integer> getKidsUnderArvSiropsNew(Date dateFormatedNew,Date dateFormatedLimite);
 public List<Integer> getAllKidsUnderArvSiropsLast(Date dateFormatedNew,Date dateFormatedLimite);
 public List<List<Integer>> getRegimenAdultNew(Date dateFormatedNew,Date dateFormatedLimite,Object drugConceptId1,Object drugConceptId2,Object drugConceptId3,Object drugConceptId4) throws ParseException;
 public List<Integer> getRegimenAdultLast(Date dateFormatedNew,Date dateFormatedLimite,Object drugConceptId1,Object drugConceptId2,Object drugConceptId3,Object drugConceptId4);
 
 /*public List<Integer> getPatientsAbandoned();
 public List<Integer> getPatientsDied();
 public List<Integer> getPatientsTransferredOut();
 public List<Integer> getPatientsTransferredIn();
*/  
 public List<Integer> getPediatricUnderRegimenLast(Date dateFormatedNew,Date dateFormatedLimite,Object drugConceptId1,Object drugConceptId2,Object drugConceptId3,Object drugConceptId4);
 public List<List<Integer>> getPediatricUnderRegimenNew(Date dateFormatedNew,Date dateFormatedLimite,Object drugConceptId1,Object drugConceptId2,Object drugConceptId3,Object drugConceptId4);
 public List<Integer> getRegimenAdultNewForOneDrug(Date dateFormatedNew,Date dateFormatedLimite, Object drugConceptId1);
 public List<Integer> getRegimenAdultLastOneDrug(Date dateFormatedNew,Date dateFormatedLimite, Object drugConceptId1);
 public List<Integer> getPediatricUnderRegimenLastOneDrug(Date dateFormatedNew,Date dateFormatedLimite, Object drugConceptId1);
 public List<Integer> getPediatricUnderRegimenNewOneDrug(Date dateFormatedNew,Date dateFormatedLimite, Object drugConceptId1);
 public List<Integer> getPatientsExitedFromCareDefinedLast(int concept, Date dateFormatedNew);
 public List<Integer> getPatientsExitedFromCareDefinedNew(int concept, Date dateFormatedNew,Date dateFormatedLimite);
 public List<List<Integer>> getPediatricUnderSecondLineRegimenNew(Date dateFormatedNew,Date dateFormatedLimite, Object drugConceptId1, Object drugConceptId2,Object drugConceptId3,Object drugConceptId4);
 public List<Integer> getPediatricUnderSecondLineRegimenLast(Date dateFormatedNew,Date dateFormatedLimite, Object drugConceptId1, Object drugConceptId2,Object drugConceptId3,Object drugConceptId4);
 @SuppressWarnings("unchecked")
public Collection union(Collection coll1, Collection coll2);
 public Collection treeCollectionsIntersection(Collection coll1, Collection coll2, Collection coll3);
 public List<Integer> getPediatricUnderSecondLineNewOneDrug(Date dateFormatedNew,Date dateFormatedLimite, Object drugConceptId1);
 public List<Integer> getPediatricUnderSecondLineLastOneDrug(Date dateFormatedNew,Date dateFormatedLimite, Object drugConceptId1);
 public String getRegimensAsString(Set<RegimenComponent> regimens);
 public List<List<Integer>> getAdultUnderSecondLineRegimenNew(Date dateFormatedNew,Date dateFormatedLimite, Object drugConceptId1, Object drugConceptId2,
     Object drugConceptId3,Object drugConceptId4);
 public List<Integer> getAdultUnderSecondLineRegimenLast(Date dateFormatedNew,Date dateFormatedLimite, Object drugConceptId1, Object drugConceptId2,
     Object drugConceptId3,Object drugConceptId4);
 public Boolean isAnyArvDrugOld(Integer patientId,Date dateFormatedNew) throws ParseException;
 public List<List<Integer>> getPatientsUnderProphylaxieNew(Date dateFormatedNew,Date dateFormatedLimite, Object drugConceptId1, Object drugConceptId2,Object drugConceptId3,Object drugConceptId4);
 
 public List<Integer> getPatientsUnderProphylaxieLast(Date dateFormatedNew,Date dateFormatedLimite, Object drugConceptId1, Object drugConceptId2,Object drugConceptId3,Object drugConceptId4);
 public List<List<Integer>> getPatientsUnderRegimenAdultThirdLineNew(Date dateFormatedNew,Date dateFormatedLimite, Object drugConceptId1, Object drugConceptId2,Object drugConceptId3,Object drugConceptId4);
 public List<Integer> getPatientsUnderRegimenAdultThirdLineLast(Date dateFormatedNew,Date dateFormatedLimite, Object drugConceptId1, Object drugConceptId2,Object drugConceptId3,Object drugConceptId4);
 
 public List<List<Integer>> getPatientsUnderPmtctNew(Date dateFormatedNew,Date dateFormatedLimite, Object drugConceptId1, Object drugConceptId2,
     Object drugConceptId3,Object drugConceptId4);
public List<Integer> getPatientsUnderPmtctLast(Date dateFormatedNew,Date dateFormatedLimite, Object drugConceptId1, Object drugConceptId2,
     Object drugConceptId3,Object drugConceptId4);
public List<String> getLocations();
public List<Integer> removeLostFollowUp(List<Integer> patientIds, Date startDate);
public List<Date> getLastVisiteDate(Patient patient);
public List<Date> getLastEncouterDate(Patient patient);
public List<List<Integer>> getPatientsUnderPediatricSiropNew(Date dateFormatedNew, Date dateFormatedLimite,
    Object drugConceptId1, Object drugConceptId2,
    Object drugConceptId3, Object drugConceptId4) ;
public List<Integer> getPatientsUnderPediatricSiropLast(Date dateFormatedNew, Date dateFormatedLimite,
    Object drugConceptId1, Object drugConceptId2,
    Object drugConceptId3, Object drugConceptId4) ;
public List<Integer> getPatientOnOnlyGivenDrugs(List<Integer> listDrugIds, List<Integer> patientIds);
public List<Integer> getRegimenCompositionByName(String regimenName);
public List<String> getRegimenCategories() ;
public List<String> getArvDrugs();
public Integer getConceptIdByDrugName(String DrugName);
public Boolean insertRegimen(String regimen_category,String regimen_name,Object arvDrug1Int,Object arvDrug2Int,Object arvDrug3Int,Object arvDrug4Int);
public List<String> getRegimenNames();
public Boolean deleteRegimen(String regimenCategory,String regimenName);
public List<Integer> getOldPatientPerDate(Date date);
public Collection twoCollectionsIntersection(Collection coll1, Collection coll2);
public Date getWhenPatientStarted(Patient patient);

}
