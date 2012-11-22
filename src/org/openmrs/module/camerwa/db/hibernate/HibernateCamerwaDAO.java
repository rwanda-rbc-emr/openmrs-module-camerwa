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
package org.openmrs.module.camerwa.db.hibernate;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.openmrs.Patient;
import org.openmrs.api.context.Context;
import org.openmrs.module.camerwa.CamerwaGlobalProperties;
import org.openmrs.module.camerwa.RegimenComposition;
import org.openmrs.module.camerwa.db.CamerwaDAO;
import org.openmrs.module.camerwa.regimenhistory.Regimen;
import org.openmrs.module.camerwa.regimenhistory.RegimenComponent;
import org.openmrs.module.camerwa.regimenhistory.RegimenUtils;

public class HibernateCamerwaDAO implements CamerwaDAO {
	
	private SessionFactory sessionFactory;
	
	protected final Log log = LogFactory.getLog(getClass());
	
	protected DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
	
	static public List<Integer> patientsExitedFromCareList = null;
	
	//static boolean test= true;
	
	public HashMap<String, Object> getPatientsUnderDrug(Date dateFormatedNew, Date dateFormatedLimite) throws ParseException {
		
		
		Session session = sessionFactory.getCurrentSession();
		
		HashMap<String, Object> returnCollection = new HashMap<String, Object>();
		List<Integer> patientsOnRegimens= new ArrayList<Integer>();
		patientsExitedFromCareList = (List<Integer>) getPatientsExitedFromCare(dateFormatedLimite);
		
		// getting patients Under ARV in the last month         
		
		List<Integer> patientsUnderARVNewList = (List<Integer>) getPatientsUnderArv(dateFormatedNew, dateFormatedLimite);
		
		List<Integer> patientsActiveUnderARVNewList = new ArrayList<Integer>();
		patientsActiveUnderARVNewList = getActivePatients(patientsUnderARVNewList, patientsExitedFromCareList,
		    dateFormatedLimite);
		
		List<Integer> patientsCurentUnderArv = new ArrayList<Integer>();
		
		for (Integer patientId : patientsActiveUnderARVNewList) {
				
			if (isAnyArvDrugOld(patientId, dateFormatedNew)) {
				
				patientsCurentUnderArv.add(patientId);
			}
		}
		
		patientsActiveUnderARVNewList.removeAll(patientsCurentUnderArv);
		
		
		List<Integer> patientsCurrentUnderArv = getOldPatientPerDate(dateFormatedNew);
		List<Integer> patientsSwimsNewButOld = (List<Integer>) twoCollectionsIntersection(patientsCurrentUnderArv, patientsActiveUnderARVNewList) ;
		patientsActiveUnderARVNewList.removeAll(patientsSwimsNewButOld);
		
		
		returnCollection.put("patientsUnderARVNew", removeLostFollowUp(patientsActiveUnderARVNewList, dateFormatedLimite)
		        .size()); 
		patientsOnRegimens.addAll(removeLostFollowUp(patientsActiveUnderARVNewList, dateFormatedLimite));
		
		List<Integer> allpatientsUnderARVList = (List<Integer>) getAllPatientsUnderARV(dateFormatedNew, dateFormatedLimite);
		
		List<Integer> allPatientsActiveUnderARVList = new ArrayList<Integer>();
		
		
		allpatientsUnderARVList.addAll(patientsSwimsNewButOld);
		
		allPatientsActiveUnderARVList = getActivePatients(allpatientsUnderARVList, patientsExitedFromCareList,
		    dateFormatedLimite);
		
		
		allPatientsActiveUnderARVList.addAll(patientsCurentUnderArv);
		
		
		returnCollection.put("LastPatientsUnderARV", allPatientsActiveUnderARVList.size());
		patientsOnRegimens.addAll(allPatientsActiveUnderARVList);
		
		//getting adults under ARV  new	
		
		List<Integer> adultsUnderARVNewList = (List<Integer>) getAdultDOBQueryNew(dateFormatedNew, dateFormatedLimite);
		List<Integer> adultsActiveUnderARVNewList = new ArrayList<Integer>();
		adultsActiveUnderARVNewList = getActivePatients(adultsUnderARVNewList, patientsExitedFromCareList, dateFormatedLimite);
		
		// remove the adult patients already under arv
		
		List<Integer> adultCurentUnderArv = new ArrayList<Integer>();
		for (Integer patientId : adultsActiveUnderARVNewList) {
			if (isAnyArvDrugOld(patientId, dateFormatedNew)) {
				adultCurentUnderArv.add(patientId);
			}
		}
		adultsActiveUnderARVNewList.removeAll(adultCurentUnderArv);
		
		List<Integer> patientsAdultSwimsNewButOld = (List<Integer>) twoCollectionsIntersection(patientsCurrentUnderArv, adultsActiveUnderARVNewList) ;
		adultsActiveUnderARVNewList.removeAll(patientsAdultSwimsNewButOld);
		
		returnCollection.put("adultsUnderARVNew", adultsActiveUnderARVNewList.size());
		patientsOnRegimens.addAll(adultsActiveUnderARVNewList);
		//getting all adults under ARV  
		
		List<Integer> allAdultsUnderARVList = (List<Integer>) getAllAdultDOBQuery(dateFormatedNew, dateFormatedLimite);
		allAdultsUnderARVList.addAll(patientsAdultSwimsNewButOld);
		
		List<Integer> alladultsActiveUnderARVList = new ArrayList<Integer>();
		alladultsActiveUnderARVList = getActivePatients(allAdultsUnderARVList, patientsExitedFromCareList, dateFormatedLimite);
		
		// add the patients already under arv
		alladultsActiveUnderARVList.addAll(adultCurentUnderArv);
		
		returnCollection.put("LastAdultsUnderARV", alladultsActiveUnderARVList.size());
		patientsOnRegimens.addAll(alladultsActiveUnderARVList);
		//getting kids under ARV
		
		List<Integer> kidsUnderARVNewList = (List<Integer>) getkidsDOBQueryNew(dateFormatedNew, dateFormatedLimite);
		List<Integer> kidsActiveUnderARVNewList = new ArrayList<Integer>();
		kidsActiveUnderARVNewList = getActivePatients(kidsUnderARVNewList, patientsExitedFromCareList, dateFormatedLimite);
		
		List<Integer> kidsCurentUnderArv = new ArrayList<Integer>();
		for (Integer patientId : kidsActiveUnderARVNewList) {
			if (isAnyArvDrugOld(patientId, dateFormatedNew)) {
				
				kidsCurentUnderArv.add(patientId);
			}
		}
		kidsActiveUnderARVNewList.removeAll(kidsCurentUnderArv);
		
		List<Integer> patientsKidsSwimsNewButOld = (List<Integer>) twoCollectionsIntersection(patientsCurrentUnderArv, kidsActiveUnderARVNewList) ;
		kidsActiveUnderARVNewList.removeAll(patientsKidsSwimsNewButOld);
		
		
		
		returnCollection.put("kidsDOBQueryNew", kidsActiveUnderARVNewList.size());
		patientsOnRegimens.addAll(kidsActiveUnderARVNewList);
		//getting all kids under ARV
		
		List<Integer> allKidsUnderARVList = (List<Integer>) getAllKidsDOBQuery(dateFormatedNew, dateFormatedLimite);
		allKidsUnderARVList.addAll(patientsKidsSwimsNewButOld);
		
		List<Integer> allKidsActiveUnderARVList = new ArrayList<Integer>();
		allKidsActiveUnderARVList = getActivePatients(allKidsUnderARVList, patientsExitedFromCareList, dateFormatedLimite);
		
		// add the patients already under arv
		
		allKidsActiveUnderARVList.addAll(kidsCurentUnderArv);
		
		returnCollection.put("LastkidsDOBQuery", allKidsActiveUnderARVList.size());
		patientsOnRegimens.addAll(allKidsActiveUnderARVList);
		
		// getting new kids under ARV taking "les comprime"
		
		List<Integer> kidsUnderArvComprimeNew = (List<Integer>) getKidsUnderArvComprimeNew(dateFormatedNew,
		    dateFormatedLimite);
		List<Integer> activeKidsUnderArvComprimeNew = new ArrayList<Integer>();
		activeKidsUnderArvComprimeNew = getActivePatients(kidsUnderArvComprimeNew, patientsExitedFromCareList,
		    dateFormatedLimite);
		
		List<Integer> patientCurentUnderArvComprime = new ArrayList<Integer>();
		for (Integer patientId : activeKidsUnderArvComprimeNew) {
			if (isAnyArvDrugOld(patientId, dateFormatedNew)) {
				
				patientCurentUnderArvComprime.add(patientId);
			}
		}
		activeKidsUnderArvComprimeNew.removeAll(patientCurentUnderArvComprime);
		
		List<Integer> patientsKidscomprimeSwimsNewButOld = (List<Integer>) twoCollectionsIntersection(patientsCurrentUnderArv, activeKidsUnderArvComprimeNew) ;
		activeKidsUnderArvComprimeNew.removeAll(patientsKidscomprimeSwimsNewButOld);
		
		returnCollection.put("activekidsUnderArvComprimeNew", activeKidsUnderArvComprimeNew.size());
		patientsOnRegimens.addAll(activeKidsUnderArvComprimeNew);
		// getting all kids under ARV taking "les comprime"
		
		List<Integer> allKidsUnderArvComprime = (List<Integer>) getAllKidsUnderArvComprime(dateFormatedNew,
		    dateFormatedLimite);
		allKidsUnderArvComprime.addAll(patientsKidscomprimeSwimsNewButOld);
		List<Integer> allActiveKidsUnderArvComprime = new ArrayList<Integer>();
		allActiveKidsUnderArvComprime = getActivePatients(allKidsUnderArvComprime, patientsExitedFromCareList,
		    dateFormatedLimite);
		
		// addind patients already in arv
		
		allActiveKidsUnderArvComprime.addAll(patientCurentUnderArvComprime);
		returnCollection.put("allActiveKidsUnderArvComprime", allActiveKidsUnderArvComprime.size());
		patientsOnRegimens.addAll(allActiveKidsUnderArvComprime);
		// getting new kids under ARV taking "sirops"
		
		List<Integer> kidsUnderArvSiropsNew = (List<Integer>) getKidsUnderArvSiropsNew(dateFormatedNew, dateFormatedLimite);
		List<Integer> activeKidsUnderArvSiropsNew = new ArrayList<Integer>();
		activeKidsUnderArvSiropsNew = getActivePatients(kidsUnderArvSiropsNew, patientsExitedFromCareList, dateFormatedLimite);
		
		List<Integer> patientCurentUnderArvSirop = new ArrayList<Integer>();
		for (Integer patientId : activeKidsUnderArvSiropsNew) {
			if (isAnyArvDrugOld(patientId, dateFormatedNew)) {
				
				patientCurentUnderArvSirop.add(patientId);
			} 
		}
		activeKidsUnderArvSiropsNew.removeAll(patientCurentUnderArvSirop);
		
		List<Integer> patientsKidsSiropSwimsNewButOld = (List<Integer>) twoCollectionsIntersection(patientsCurrentUnderArv, activeKidsUnderArvSiropsNew) ;
		activeKidsUnderArvSiropsNew.removeAll(patientsKidsSiropSwimsNewButOld);
		
		
		returnCollection.put("activekidsUnderArvSiropsNew", activeKidsUnderArvSiropsNew.size());
		patientsOnRegimens.addAll(activeKidsUnderArvSiropsNew);
		// getting all kids under ARV taking "sirops"
		
		List<Integer> allKidsUnderArvSiropsLast = (List<Integer>) getAllKidsUnderArvSiropsLast(dateFormatedNew,
		    dateFormatedLimite);
		allKidsUnderArvSiropsLast.addAll(patientsKidsSiropSwimsNewButOld);
		
		
		List<Integer> allActiveKidsUnderArvSiropsLast = new ArrayList<Integer>();
		allActiveKidsUnderArvSiropsLast = getActivePatients(allKidsUnderArvSiropsLast, patientsExitedFromCareList,
		    dateFormatedLimite);
		
		// adding patients already on arv
		
		allActiveKidsUnderArvSiropsLast.addAll(patientCurentUnderArvSirop);
		
		returnCollection.put("allActiveKidsUnderArvSiropsLast", allActiveKidsUnderArvSiropsLast.size());
		patientsOnRegimens.addAll(allActiveKidsUnderArvSiropsLast);
		
			
		List<List<Object>> adultRegimenCompositions = getRegimesByCategory("adultRegimen");
		
		
		
		returnCollection.put("adultRegimenCompositionSize", adultRegimenCompositions.size());
		//log.info("adultRegimenCompositions adultRegimenCompositions adultRegimenCompositions adultRegimenCompositions"+adultRegimenCompositions);
		int count = 1;
		int sumOfTatals = 0;
		int sumOfAdultRegimenNew = 0;
		int sumOfAdultRegimenLast = 0;
		for (List<Object> regimenComposition : adultRegimenCompositions) {
			//getting the name of a curent regimen 
			
			 returnCollection.put("adultRegimenName"+count,regimenComposition.get(0));
			 
			//log.info("regimenComposition.get(2) regimenComposition.get(2) regimenComposition.get(2)regimenComposition.get(2)"+regimenComposition.get(1)+"vvvv"+regimenComposition.get(2)+"sssssssss"+regimenComposition.get(3)+"fffffffffff"+regimenComposition.get(4));
			// getting regimen adults new
			 
			List<Integer> adultRegimenNew = (List<Integer>) getRegimenAdultNew(dateFormatedNew, dateFormatedLimite,
			    regimenComposition.get(1), regimenComposition.get(2), regimenComposition.get(3), regimenComposition.get(4)).get(0);
		   
			List<Integer> activeAdultRegimenNew = new ArrayList<Integer>();
			activeAdultRegimenNew = getActivePatients(adultRegimenNew, patientsExitedFromCareList, dateFormatedLimite);							
			//----------------------------------------------------------------------------------------------------------------------
			returnCollection.put("adultRegimenNew" + count, activeAdultRegimenNew.size());
			patientsOnRegimens.addAll(activeAdultRegimenNew);
			// getting regimen adults last
	//	log.info("adultRegimenNew adultRegimenNew new b  new adultRegimenNew adultRegimenNew adultRegimenNew"+activeAdultRegimenNew);
			List<Integer> adultRegimenLast = (List<Integer>) getRegimenAdultLast(dateFormatedNew, dateFormatedLimite,
			    regimenComposition.get(1), regimenComposition.get(2), regimenComposition.get(3), regimenComposition.get(4));
	//	log.info("adultRegimenNew adultRegimenNew  last adultRegimenNew adultRegimenNew adultRegimenNew"+adultRegimenNew);			
			List<Integer> activeAdultRegimenLast = new ArrayList<Integer>();
			activeAdultRegimenLast = getActivePatients(adultRegimenLast, patientsExitedFromCareList, dateFormatedLimite);
			returnCollection.put("adultRegimenLast" + count, activeAdultRegimenLast.size());
			patientsOnRegimens.addAll(activeAdultRegimenLast);
			// getting  regimen adults TOTAL
			
			returnCollection.put("TotalAdulRegimen" + count, activeAdultRegimenNew.size() + activeAdultRegimenLast.size());
			sumOfTatals = sumOfTatals + activeAdultRegimenNew.size() + activeAdultRegimenLast.size();
			sumOfAdultRegimenNew = sumOfAdultRegimenNew + activeAdultRegimenNew.size();
			sumOfAdultRegimenLast = sumOfAdultRegimenLast + activeAdultRegimenLast.size();
			count++;
		}
		
		List<List<Object>> pediatricRegimenCompositions = getRegimesByCategory("pediatricRegimen"); 
		
		int countPediatric = 1;
		int sumOfPediatricTatals = 0;
		int sumOfPediatricRegimenNew = 0;
		int sumOfPediatricRegimenLast = 0;
		returnCollection.put("pediatricSize", pediatricRegimenCompositions.size());
		
		for (List<Object> regimenComposition : pediatricRegimenCompositions) {
			// getting regimen pediatric new
			
			returnCollection.put("pediatricRegimenName"+countPediatric,regimenComposition.get(0));
			
			
			List<Integer> pediatricRegimenNew = (List<Integer>) getPediatricUnderRegimenNew(dateFormatedNew,
			    dateFormatedLimite, regimenComposition.get(1), regimenComposition.get(2), regimenComposition.get(3),regimenComposition.get(4)).get(0);
			List<Integer> activePediatricRegimenNew = new ArrayList<Integer>();
			activePediatricRegimenNew = getActivePatients(pediatricRegimenNew, patientsExitedFromCareList, dateFormatedLimite);
			
			returnCollection.put("pediatricRegimenNew" + countPediatric, activePediatricRegimenNew.size());
			patientsOnRegimens.addAll(activePediatricRegimenNew);
			// getting regimen pediatric last
			
			List<Integer> pediatricRegimenLast = (List<Integer>) getPediatricUnderRegimenLast(dateFormatedNew,
			    dateFormatedLimite, regimenComposition.get(1), regimenComposition.get(2), regimenComposition.get(3),regimenComposition.get(4));		
			
			returnCollection.put("pediatricRegimenLast" + countPediatric, pediatricRegimenLast.size());
			patientsOnRegimens.addAll(pediatricRegimenLast);
			// getting  regimen pediatric TOTAL 
			returnCollection.put("TotalPediatricRegimen" + countPediatric, activePediatricRegimenNew.size()
			        + pediatricRegimenLast.size());
			sumOfPediatricTatals = sumOfPediatricTatals + activePediatricRegimenNew.size()
			        + pediatricRegimenLast.size();
			sumOfPediatricRegimenNew = sumOfPediatricRegimenNew + activePediatricRegimenNew.size();
			sumOfPediatricRegimenLast = sumOfPediatricRegimenLast + pediatricRegimenLast.size();
			countPediatric++;
		}
		
		   // getting patients regimens second line
		
		List<List<Object>> pediatricRegimenSecondLine = getRegimesByCategory("pediatricRegimenSecondLine");
		
		returnCollection.put("pediatricRegimenSecondLineSize", pediatricRegimenSecondLine.size());
		
		int countSecondLine = 1;
		int sumOfSecondLineTatals = 0;
		int sumOfSecondLineNew = 0;
		int sumOfSecondLineLast = 0;
		
		for (List<Object> regimenComposition : pediatricRegimenSecondLine) {
			// getting regimen pediatric second line new
			
			returnCollection.put("pediatricSecondLineName"+countSecondLine,regimenComposition.get(0));
			
			List<Integer> pediatricSecondLineNew = (List<Integer>) getPediatricUnderSecondLineRegimenNew(dateFormatedNew,
			    dateFormatedLimite, regimenComposition.get(1), regimenComposition.get(2), regimenComposition.get(3),
			    regimenComposition.get(4)).get(0);
			List<Integer> activePediatricSecondLineNew = new ArrayList<Integer>();
			activePediatricSecondLineNew = getActivePatients(pediatricSecondLineNew, patientsExitedFromCareList,
			    dateFormatedLimite);
			
			returnCollection.put("pediatricSecondLineNew" + countSecondLine, activePediatricSecondLineNew.size());
			patientsOnRegimens.addAll(activePediatricSecondLineNew);
			// getting regimen pediatric second line last
			
			List<Integer> pediatricSecondLineLast = (List<Integer>) getPediatricUnderSecondLineRegimenLast(dateFormatedNew,
			    dateFormatedLimite, regimenComposition.get(1), regimenComposition.get(2), regimenComposition.get(3),
			    regimenComposition.get(4));
		
			returnCollection.put("pediatricSecondLineLast" + countSecondLine, pediatricSecondLineLast.size());
			patientsOnRegimens.addAll(pediatricSecondLineLast);
			// getting  regimen pediatric TOTAL 
			returnCollection.put("TotalPediatricSecondLine" + countSecondLine, activePediatricSecondLineNew.size()
			        + pediatricSecondLineLast.size());
			sumOfSecondLineTatals = sumOfSecondLineTatals + activePediatricSecondLineNew.size()
			        + pediatricSecondLineLast.size();
			sumOfSecondLineNew = sumOfSecondLineNew + activePediatricSecondLineNew.size();
			sumOfSecondLineLast = sumOfSecondLineLast + pediatricSecondLineLast.size();
			
			countSecondLine++;
		}
		
		// return pediatric under second line total
		returnCollection.put("sumOfSecondLineNew", sumOfSecondLineNew);
		returnCollection.put("sumOfSecondLineLast", sumOfSecondLineLast);
		returnCollection.put("sumOfSecondLineTatals", sumOfSecondLineTatals);
		
		// getting  adult of second ligne regimen 
		
		
		List<List<Object>> adultRegimensSecondLine = getRegimesByCategory("adultRegimensSecondLine");
		
		returnCollection.put("adultRegimenSecondLineSize", adultRegimensSecondLine.size());
		
		
		int countAdultSecondLine = 1;
		int sumOfAdultSecondLineTatals = 0;
		int sumOfAdultSecondLineNew = 0;
		int sumOfAdultSecondLineLast = 0;
		
		for (List<Object> regimenComposition : adultRegimensSecondLine) {
			
			returnCollection.put("adultSecondLineName"+countAdultSecondLine,regimenComposition.get(0));
			
			
			List<Integer> pediatricSecondLineNew = (List<Integer>) getAdultUnderSecondLineRegimenNew(dateFormatedNew,
			    dateFormatedLimite, regimenComposition.get(1), regimenComposition.get(2), regimenComposition.get(3),
			    regimenComposition.get(4)).get(0);
			List<Integer> activeAdultSecondLineNew = new ArrayList<Integer>();
			activeAdultSecondLineNew = getActivePatients(pediatricSecondLineNew, patientsExitedFromCareList, dateFormatedLimite);
			
			returnCollection.put("adultSecondLineNew" + countAdultSecondLine, activeAdultSecondLineNew.size());
			patientsOnRegimens.addAll(activeAdultSecondLineNew);
			
			
			List<Integer> adultSecondLineLast = (List<Integer>) getAdultUnderSecondLineRegimenLast(dateFormatedNew,
			    dateFormatedLimite, regimenComposition.get(1), regimenComposition.get(2), regimenComposition.get(3),
			    regimenComposition.get(4));
			
			
			returnCollection.put("adultSecondLineLast" + countAdultSecondLine, adultSecondLineLast.size());
			patientsOnRegimens.addAll(adultSecondLineLast);
			 
			returnCollection.put("TotalAdultSecondLine" + countAdultSecondLine, activeAdultSecondLineNew.size()
			        + adultSecondLineLast.size());
			sumOfAdultSecondLineTatals = sumOfAdultSecondLineTatals + activeAdultSecondLineNew.size()
			        + adultSecondLineLast.size();
			sumOfAdultSecondLineNew = sumOfAdultSecondLineNew + activeAdultSecondLineNew.size();
			sumOfAdultSecondLineLast = sumOfAdultSecondLineLast + adultSecondLineLast.size();
			
			countAdultSecondLine++;
		}
		
		// return pediatric under second line total
		
		returnCollection.put("sumOfAdultSecondLineNew", sumOfAdultSecondLineNew);
		returnCollection.put("sumOfAdultSecondLineLast", sumOfAdultSecondLineLast);
		returnCollection.put("sumOfAdultSecondLineTatals", sumOfAdultSecondLineTatals);
		
		// returning adults regimens totals	
		
		returnCollection.put("sumOfTatals", sumOfTatals);
		returnCollection.put("sumOfAdultRegimenNew", sumOfAdultRegimenNew);
		returnCollection.put("sumOfAdultRegimenLast", sumOfAdultRegimenLast);
		
		// returning pediatric regimen totals
		
		returnCollection.put("sumOfPediatricTatals", sumOfPediatricTatals);
		returnCollection.put("sumOfPediatricRegimenNew", sumOfPediatricRegimenNew);
		returnCollection.put("sumOfPediatricRegimenLast", sumOfPediatricRegimenLast);
		
		//calculate and returnning object to be forwared to jsp
		
		returnCollection.put("TotalPatientsUnderARV", allPatientsActiveUnderARVList.size()
		        + patientsActiveUnderARVNewList.size());
		returnCollection.put("TotalAdultsUnderARV", alladultsActiveUnderARVList.size() + adultsActiveUnderARVNewList.size());
		returnCollection.put("TotalkidsDOBQuery", allKidsActiveUnderARVList.size() + kidsActiveUnderARVNewList.size());
		
		returnCollection.put("TotalKidsUnderArvComprime", activeKidsUnderArvComprimeNew.size()
		        + allActiveKidsUnderArvComprime.size());
		returnCollection.put("TotalKidsUnderArvSirops", activeKidsUnderArvSiropsNew.size()
		        + allActiveKidsUnderArvSiropsLast.size());
		
		List<Integer> patientsExitedFromCareDefined = new ArrayList<Integer>();
		CamerwaGlobalProperties gp =new CamerwaGlobalProperties();
		int patientDefaultedConceptId =gp.getConceptIdAsInt("camerwa.PatientDefaultedConceptId");
		int patientDiedConceptId = gp.getConceptIdAsInt("camerwa.patientDiedConceptId");
		int patientTransferesOutConceptId =gp.getConceptIdAsInt("camerwa.PatientTransferesOutConceptId");
		int transterInFromConceptId =gp.getConceptIdAsInt("camerwa.TransterInFromConceptId");
		
		patientsExitedFromCareDefined.add(patientDefaultedConceptId);
		patientsExitedFromCareDefined.add(patientDiedConceptId);
		patientsExitedFromCareDefined.add(patientTransferesOutConceptId);
		patientsExitedFromCareDefined.add(transterInFromConceptId);
		returnCollection.put("exitedPatientsCollectionSize", patientsExitedFromCareDefined.size());
		// getting patients exited from care
		int countExited = 1;
		for (Integer patientExitedFromCareConcept : patientsExitedFromCareDefined) {
			// gets exited patients LAST
			List<Integer> patientsExitedLast = (List<Integer>) getPatientsExitedFromCareDefinedLast(
			    patientExitedFromCareConcept, dateFormatedNew);
			
			returnCollection.put("patientsExitedLast" + countExited, patientsExitedLast.size());
			
			// getting exited patients newly
			List<Integer> patientsExitedNew = (List<Integer>) getPatientsExitedFromCareDefinedNew(
			    patientExitedFromCareConcept, dateFormatedNew, dateFormatedLimite);
			
			returnCollection.put("patientsExitedNew" + countExited, patientsExitedNew.size());
			// getting  exited patients TOTAL 
			returnCollection.put("TotalpatientsExitedFromCare" + countExited, patientsExitedLast.size()
			        + patientsExitedNew.size());
			
			countExited++;
		}
		
		
		List<List<Object>> prophylaxiePostExpositions = getRegimesByCategory("prophylaxiePostExpositions");
		returnCollection.put("prophylaxiePostExpositionSize", prophylaxiePostExpositions.size());
		
		int countProphylaxiePostExposition = 1;
		int sumOfProphylaxiePostExpositionTotals = 0;
		int sumOfProphylaxiePostExpositionNew = 0;
		int sumOfProphylaxiePostExpositionLast = 0;
		
		for (List<Object> oneRegimenComposition : prophylaxiePostExpositions) {
			returnCollection.put("prophylaxiePostExpositionName"+countProphylaxiePostExposition,oneRegimenComposition.get(0));
			List<Integer> patientsProphylaxieNew = (List<Integer>) getPatientsUnderProphylaxieNew(dateFormatedNew,
			    dateFormatedLimite, oneRegimenComposition.get(1), oneRegimenComposition.get(2),
			    oneRegimenComposition.get(3), oneRegimenComposition.get(4)).get(0);
			
			List<Integer> activeProphylaxiePostExpositionNew = new ArrayList<Integer>();
			activeProphylaxiePostExpositionNew = getActivePatients(patientsProphylaxieNew, patientsExitedFromCareList,
			    dateFormatedLimite);
			
			returnCollection.put("prophylaxiePostExpositionNew" + countProphylaxiePostExposition,
			    activeProphylaxiePostExpositionNew.size());
			patientsOnRegimens.addAll(activeProphylaxiePostExpositionNew);	
			List<Integer> patientsProphylaxieLast = (List<Integer>) getPatientsUnderProphylaxieLast(dateFormatedNew,
			    dateFormatedLimite, oneRegimenComposition.get(1), oneRegimenComposition.get(2),
			    oneRegimenComposition.get(3), oneRegimenComposition.get(4));
			
			
			returnCollection.put("prophylaxiePostExpositionLast" + countProphylaxiePostExposition,
				patientsProphylaxieLast.size());
			patientsOnRegimens.addAll(patientsProphylaxieLast);
			returnCollection.put("TotalProphylaxiePostExposition" + countProphylaxiePostExposition,
			    activeProphylaxiePostExpositionNew.size() + patientsProphylaxieLast.size());
			sumOfProphylaxiePostExpositionTotals = sumOfProphylaxiePostExpositionTotals
			        + activeProphylaxiePostExpositionNew.size() + patientsProphylaxieLast.size();
			sumOfProphylaxiePostExpositionNew = sumOfProphylaxiePostExpositionNew
			        + activeProphylaxiePostExpositionNew.size();
			sumOfProphylaxiePostExpositionLast = sumOfProphylaxiePostExpositionLast
			        + patientsProphylaxieLast.size();
			
			countProphylaxiePostExposition++;
		}
		
		returnCollection.put("sumOfProphylaxiePostExpositionTatals", sumOfProphylaxiePostExpositionTotals);
		returnCollection.put("sumOfProphylaxiePostExpositionNew", sumOfProphylaxiePostExpositionNew);
		returnCollection.put("sumOfProphylaxiePostExpositionLast", sumOfProphylaxiePostExpositionLast);
		
		
		List<List<Object>> pmtctCompositions = getRegimesByCategory("pmtctCompositions");
		returnCollection.put("pmtctCompositionsSize", pmtctCompositions.size());
		
		int countPmtct = 1;
		int sumOfPmtctTotals = 0;
		int sumOfPmtctNew = 0;
		int sumOfPmtctLast = 0;
		
		for (List<Object> oneRegimenComposition : pmtctCompositions) {
			
			returnCollection.put("pmtctName"+countPmtct,oneRegimenComposition.get(0));
			List<Integer> patientsPmtctNew = (List<Integer>) getPatientsUnderPmtctNew(dateFormatedNew,dateFormatedLimite,
			    oneRegimenComposition.get(1), oneRegimenComposition.get(2), oneRegimenComposition.get(3),
			    oneRegimenComposition.get(4)).get(0);
			
			List<Integer> activePmtctNew = new ArrayList<Integer>();
			activePmtctNew = getActivePatients(patientsPmtctNew, patientsExitedFromCareList, dateFormatedLimite);
			
			returnCollection.put("pmtctNew" + countPmtct, activePmtctNew.size());
			patientsOnRegimens.addAll(activePmtctNew);
			List<Integer> patientsPmtctLast = (List<Integer>) getPatientsUnderPmtctLast(dateFormatedNew, dateFormatedLimite,
			    oneRegimenComposition.get(1), oneRegimenComposition.get(2), oneRegimenComposition.get(3),
			    oneRegimenComposition.get(4));
			
			
		    returnCollection.put("pmtctLast" + countPmtct,  patientsPmtctLast.size());
		    patientsOnRegimens.addAll(patientsPmtctLast);
			returnCollection.put("TotalPmtct" + countPmtct, activePmtctNew.size() + patientsPmtctLast.size());
			sumOfPmtctTotals = sumOfPmtctTotals + activePmtctNew.size() + patientsPmtctLast.size();
			sumOfPmtctNew = sumOfPmtctNew + activePmtctNew.size();
			sumOfPmtctLast = sumOfPmtctLast + patientsPmtctLast.size();
			
			countPmtct++;
		}
		
		returnCollection.put("sumOfPmtctTotals", sumOfPmtctTotals);
		returnCollection.put("sumOfPmtctNew", sumOfPmtctNew);
		returnCollection.put("sumOfPmtctLast", sumOfPmtctLast);
		
				
		
		List<List<Object>> regimenPediatricSirop = getRegimesByCategory("PediatricSirop");
		returnCollection.put("regimenPediatricSiropSize", regimenPediatricSirop.size());
		
		int countPediatricSirop = 1;
		int sumOfPediatricSiropTotals = 0;
		int sumOfPediatricSiropNew = 0;
		int sumOfPediatricSiropLast = 0;
		
		for (List<Object> oneRegimenComposition : regimenPediatricSirop) {
			returnCollection.put("PediatricSiropName"+countPediatricSirop,oneRegimenComposition.get(0));
			List<Integer> patientsPediatricSiropNew = (List<Integer>) getPatientsUnderPediatricSiropNew(dateFormatedNew,
			    dateFormatedLimite, oneRegimenComposition.get(1), oneRegimenComposition.get(2),
			    oneRegimenComposition.get(3), oneRegimenComposition.get(4)).get(0);
			
			List<Integer> activePediatricSiropNew = new ArrayList<Integer>();
			activePediatricSiropNew = getActivePatients(patientsPediatricSiropNew, patientsExitedFromCareList,
			    dateFormatedLimite);
			
			returnCollection.put("PediatricSiropNew" + countPediatricSirop, activePediatricSiropNew.size());
			patientsOnRegimens.addAll(activePediatricSiropNew);
			List<Integer> patientsPediatricSiropLast = (List<Integer>) getPatientsUnderPediatricSiropLast(dateFormatedNew,
			    dateFormatedLimite, oneRegimenComposition.get(1), oneRegimenComposition.get(2),
			    oneRegimenComposition.get(3), oneRegimenComposition.get(4));
			
			
			returnCollection.put("PediatricSiropLast" + countPediatricSirop, patientsPediatricSiropLast.size());
			patientsOnRegimens.addAll(patientsPediatricSiropLast);
			returnCollection.put("TotalPediatricSirop" + countPediatricSirop, activePediatricSiropNew.size()
			        + patientsPediatricSiropLast.size());
			sumOfPediatricSiropTotals = sumOfPediatricSiropTotals + activePediatricSiropNew.size()
			        + patientsPediatricSiropLast.size();
			sumOfPediatricSiropNew = sumOfPediatricSiropNew + activePediatricSiropNew.size();
			sumOfPediatricSiropLast = sumOfPediatricSiropLast + patientsPediatricSiropLast.size();
			
			countPediatricSirop++;
		}
		
		
		returnCollection.put("sumOfPediatricSiropTotals", sumOfPediatricSiropTotals);
		returnCollection.put("sumOfPediatricSiropNew", sumOfPediatricSiropNew);
		returnCollection.put("sumOfPediatricSiropLast", sumOfPediatricSiropLast);
		
		//-------------------------------------------------------------------------------------------------------------------------
		List<List<Object>> regimenAdultThirdLine = getRegimesByCategory("adultRegimensThirdLine");
		returnCollection.put("regimenAdultThirdLineSize", regimenAdultThirdLine.size());
		
		int countRegimenAdultThirdLine = 1;
		int sumOfRegimenAdultThirdLineTotals = 0;
		int sumOfRegimenAdultThirdLineNew = 0;
		int sumOfRegimenAdultThirdLineLast = 0;
		
		for (List<Object> oneRegimenComposition : regimenAdultThirdLine) {
			returnCollection.put("RegimenAdultThirdLineName"+countRegimenAdultThirdLine,oneRegimenComposition.get(0));
			List<Integer> patientsRegimenAdultThirdLineNew = (List<Integer>) getPatientsUnderRegimenAdultThirdLineNew(dateFormatedNew,
			    dateFormatedLimite, oneRegimenComposition.get(1), oneRegimenComposition.get(2),
			    oneRegimenComposition.get(3), oneRegimenComposition.get(4)).get(0);
			
			List<Integer> activeRegimenAdultThirdLineNew = new ArrayList<Integer>();
			activeRegimenAdultThirdLineNew = getActivePatients(patientsRegimenAdultThirdLineNew, patientsExitedFromCareList,
			    dateFormatedLimite);
			
			returnCollection.put("RegimenAdultThirdLineNew" + countRegimenAdultThirdLine, activeRegimenAdultThirdLineNew.size());
			patientsOnRegimens.addAll(activeRegimenAdultThirdLineNew);
			List<Integer> patientsRegimenAdultThirdLineLast = (List<Integer>) getPatientsUnderRegimenAdultThirdLineLast(dateFormatedNew,
			    dateFormatedLimite, oneRegimenComposition.get(1), oneRegimenComposition.get(2),
			    oneRegimenComposition.get(3), oneRegimenComposition.get(4));
			
			
			returnCollection.put("RegimenAdultThirdLineLast" + countRegimenAdultThirdLine, patientsRegimenAdultThirdLineLast.size());
			patientsOnRegimens.addAll(patientsRegimenAdultThirdLineLast);
			returnCollection.put("TotalRegimenAdultThirdLine" + countRegimenAdultThirdLine, activeRegimenAdultThirdLineNew.size()
			        + patientsRegimenAdultThirdLineLast.size());
			sumOfPediatricSiropTotals = sumOfRegimenAdultThirdLineTotals + activeRegimenAdultThirdLineNew.size()
			        + patientsRegimenAdultThirdLineLast.size();
			sumOfRegimenAdultThirdLineNew = sumOfRegimenAdultThirdLineNew + activeRegimenAdultThirdLineNew.size();
			sumOfRegimenAdultThirdLineLast = sumOfRegimenAdultThirdLineLast + patientsRegimenAdultThirdLineLast.size();
			
			countRegimenAdultThirdLine++;
		}
		
		
		returnCollection.put("sumOfRegimenAdultThirdLineTotals", sumOfRegimenAdultThirdLineTotals);
		returnCollection.put("sumOfRegimenAdultThirdLineNew", sumOfRegimenAdultThirdLineNew);
		returnCollection.put("sumOfRegimenAdultThirdLineLast", sumOfRegimenAdultThirdLineLast);

		returnCollection.put("patientsOnRegimens", patientsOnRegimens);
		//patientsOnRegimens
	 log.info("patientsOnRegimenspatien   just to check tsOnRegimenspatientsOnRegimenspatientsOnRegimenspatientsOnRegimenspatientsOnRegimens"+patientsOnRegimens);
	
		return returnCollection;
	}
	
	/**
	 * sets session factory
	 * 
	 * @param sessionFactory
	 */
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	/**
	 * gets session factory
	 * 
	 * @return
	 */
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	
	/**
	 * @see org.openmrs.module.camerwa.db.CamerwaDAO#getPatientsUnderDrug(int, java.util.List)
	 */
	public String getDateFormatedFromDateObject(Date dateObject) {
		df = new SimpleDateFormat("yyyy-MM-dd");
		return df.format(dateObject);
	}
	
	public List<Integer> getActivePatients(List<Integer> patientsUnderARVNewList, List<Integer> patientsExitedFromCareList,
	                                       Date startDate) {
	
		List<Integer> patientsActive = new ArrayList<Integer>();
		
		boolean notFound;
		for (int i : patientsUnderARVNewList) {
		
			notFound = true;
			for (int j : patientsExitedFromCareList) {
				if (j == i) {
					notFound = false;
					break;
				}
			}
			if (notFound)
				if(!patientsActive.contains(i))
				patientsActive.add(i);
		}
	
		
		return removeLostFollowUp(patientsActive, startDate);
	}
	
	public List<Integer> getAllKidsDOBQuery(Date dateFormatedNew, Date dateFormatedLimite) {
		Session session = sessionFactory.getCurrentSession();
		CamerwaGlobalProperties gp = new CamerwaGlobalProperties();
		String arvConceptIds = gp.getArvConceptIdList();
		SQLQuery allKidsDOBQuery = session
		        .createSQLQuery("select distinct p.patient_id from patient p inner join orders o on o.patient_id=p.patient_id"
		                + " inner join drug_order dor on dor.order_id=o.order_id"
		                + " inner join patient_program prog on prog.patient_id=p.patient_id "
		                + " inner join drug d on dor.drug_inventory_id=d.drug_id and (prog.program_id = 2 or prog.program_id = 1) and prog.voided = 0 and d.concept_id in("+arvConceptIds+")"
		                + " inner join person on person_id=p.patient_id where o.start_date < "
		                + "'"
		                + getDateFormatedFromDateObject(dateFormatedNew)
		                + "'"
		                + "and o.discontinued = 0 and p.voided = 0 and o.voided = 0 and o.auto_expire_date is null and DATE_FORMAT(FROM_DAYS(TO_DAYS(NOW())-TO_DAYS(person.birthdate)), '%Y')+0 < 15 ;");
		return allKidsDOBQuery.list();
	}
	
	public List<Integer> getkidsDOBQueryNew(Date dateFormatedNew, Date dateFormatedLimite) {
		Session session = sessionFactory.getCurrentSession();
		CamerwaGlobalProperties gp = new CamerwaGlobalProperties();
		String arvConceptIds = gp.getArvConceptIdList();
		SQLQuery kidsDOBQueryNew = session
		        .createSQLQuery("select distinct p.patient_id from patient p inner join orders o on o.patient_id=p.patient_id"
		                + " inner join drug_order dor on dor.order_id=o.order_id"
		                + " inner join patient_program prog on prog.patient_id=p.patient_id "
		                + " inner join drug d on dor.drug_inventory_id=d.drug_id and (prog.program_id = 2 or prog.program_id = 1) and prog.voided = 0 and d.concept_id in("+arvConceptIds+")"
		                + " inner join person on person_id=p.patient_id where o.start_date >= "
		                + "'"
		                + getDateFormatedFromDateObject(dateFormatedNew)
		                + "' and o.start_date < '"
		                + getDateFormatedFromDateObject(dateFormatedLimite)
		                + "'and o.discontinued = 0 and p.voided = 0 and o.voided = 0 and o.auto_expire_date is null and DATE_FORMAT(FROM_DAYS(TO_DAYS(NOW())-TO_DAYS(person.birthdate)), '%Y')+0 < 15 ;");
		return kidsDOBQueryNew.list();
	}
	
	public List<Integer> getAllAdultDOBQuery(Date dateFormatedNew, Date dateFormatedLimite) {
		Session session = sessionFactory.getCurrentSession();
		CamerwaGlobalProperties gp = new CamerwaGlobalProperties();
		String arvConceptIds = gp.getArvConceptIdList();
		SQLQuery allAdultDOBQuery = session
		        .createSQLQuery("select distinct p.patient_id from patient p inner join orders o on o.patient_id=p.patient_id"
		                + " inner join drug_order dor on dor.order_id=o.order_id"
		                + " inner join patient_program prog on prog.patient_id=p.patient_id "
		                + " inner join drug d on dor.drug_inventory_id=d.drug_id and (prog.program_id = 2 or prog.program_id = 1) and prog.voided = 0 and d.concept_id in("+arvConceptIds+")"
		                + " inner join person on person_id=p.patient_id where o.start_date < "
		                + "'"
		                + getDateFormatedFromDateObject(dateFormatedNew)
		                + "'"
		                + "and o.discontinued = 0 and p.voided = 0 and o.voided = 0 and o.auto_expire_date is null and DATE_FORMAT(FROM_DAYS(TO_DAYS(NOW())-TO_DAYS(person.birthdate)), '%Y')+0 >= 15;");
		return allAdultDOBQuery.list();
	}
	
	public List<Integer> getAdultDOBQueryNew(Date dateFormatedNew, Date dateFormatedLimite) {
			
		
		Session session = sessionFactory.getCurrentSession();
		CamerwaGlobalProperties gp = new CamerwaGlobalProperties();
		String arvConceptIds = gp.getArvConceptIdList();
		SQLQuery adultDOBQueryNew = session
		        .createSQLQuery("select distinct p.patient_id from patient p inner join orders o on o.patient_id=p.patient_id"
		                + " inner join drug_order dor on dor.order_id=o.order_id"
		                + " inner join patient_program prog on prog.patient_id=p.patient_id "
		                + " inner join drug d on dor.drug_inventory_id=d.drug_id and (prog.program_id = 2 or prog.program_id = 1) and prog.voided = 0 and d.concept_id in("+arvConceptIds+")"
		                + " inner join person on person_id=p.patient_id where o.start_date >= "
		                + "'"
		                + getDateFormatedFromDateObject(dateFormatedNew)
		                + "' and o.start_date < '"
		                + getDateFormatedFromDateObject(dateFormatedLimite)
		                + "'"
		                + "and o.discontinued = 0 and p.voided = 0 and o.voided = 0 and o.auto_expire_date is null and DATE_FORMAT(FROM_DAYS(TO_DAYS(NOW())-TO_DAYS(person.birthdate)), '%Y')+0 >= 15;");
		
		return adultDOBQueryNew.list();
	}
	
	public List<Integer> getAllPatientsUnderARV(Date dateFormatedNew, Date dateFormatedLimite) {
		Session session = sessionFactory.getCurrentSession();
		CamerwaGlobalProperties gp = new CamerwaGlobalProperties();
		String arvConceptIds = gp.getArvConceptIdList();
		SQLQuery allPatientsUnderARV = session
		        .createSQLQuery("select distinct p.patient_id from patient p inner join orders o on o.patient_id=p.patient_id "
		                + "inner join drug_order dor on dor.order_id=o.order_id "
		                + "inner join patient_program prog on prog.patient_id=p.patient_id "
		                + "inner join drug d on dor.drug_inventory_id=d.drug_id and (prog.program_id = 2 or prog.program_id = 1) and prog.voided = 0 and d.concept_id in("+arvConceptIds+") where o.start_date < "
		                + "'"
		                + getDateFormatedFromDateObject(dateFormatedNew)
		                + "' and o.discontinued = 0 and p.voided = 0 and o.voided = 0 and o.auto_expire_date is null and p.voided = 0 and o.voided = 0");
		
		return allPatientsUnderARV.list();
	}
	
	public List<Integer> getPatientsUnderArv(Date dateFormatedNew, Date dateFormatedLimite) {
		
		Session session = sessionFactory.getCurrentSession();
		CamerwaGlobalProperties gp = new CamerwaGlobalProperties();
		String arvConceptIds = gp.getArvConceptIdList();
		SQLQuery patientsUnderARVNew = session
		        .createSQLQuery("select distinct p.patient_id from patient p inner join orders o on o.patient_id=p.patient_id "
		                + "inner join drug_order dor on dor.order_id=o.order_id "
		                +" inner join patient_program prog on prog.patient_id=p.patient_id "
		                + "inner join drug d on dor.drug_inventory_id=d.drug_id and (prog.program_id = 2 or prog.program_id = 1) and prog.voided = 0 and d.concept_id in("+arvConceptIds+") where o.start_date >= "
		                + "'"
		                + getDateFormatedFromDateObject(dateFormatedNew)
		                + "' and o.start_date < '"
		                + getDateFormatedFromDateObject(dateFormatedLimite)
		                + "'and o.discontinued = 0 and p.voided = 0 and o.voided = 0 and o.auto_expire_date is null ");
		return patientsUnderARVNew.list();
	}
	
	public List<Integer> getPatientsExitedFromCare(Date dateFormatedLimite) {
		
		Session session = sessionFactory.getCurrentSession();
		CamerwaGlobalProperties gp =new CamerwaGlobalProperties();
		int exitedFromCareConceptId =gp.getConceptIdAsInt("camerwa.ExitedFromCareConceptId");
		
		SQLQuery allPatientQuery = session
        .createSQLQuery("select distinct pa.patient_id from patient pa;");
		
		
		List<Integer> allPatient = allPatientQuery.list();
		List<Integer> patientsExitedFromCare = new ArrayList<Integer>();
		for(Integer patientId : allPatient){
			
			SQLQuery query2 = session
	        .createSQLQuery("select distinct o.person_id from obs o where o.concept_id = "
								+ exitedFromCareConceptId
								+ " and (cast(o.obs_datetime as DATE)) <= '"
								+ dateFormatedLimite
								+ "' and o.voided = 0 and o.person_id="
								+ patientId);
			patientsExitedFromCare.addAll(query2.list());
		
		}
		return patientsExitedFromCare;
		
		/*Session session = sessionFactory.getCurrentSession();
		CamerwaGlobalProperties gp =new CamerwaGlobalProperties();
		int exitedFromCareConceptId =gp.getConceptIdAsInt("camerwa.ExitedFromCareConceptId");
		SQLQuery allPatientsExitedFromCare = session
		        .createSQLQuery("select distinct pa.patient_id from patient pa inner join person pe on pa.patient_id = pe.person_id inner join obs ob on ob.person_id = pe.person_id where ob.concept_id = "+exitedFromCareConceptId+"");
		List<Integer> patientsVoided = (List<Integer>) getPatientsVoided();
		List<Integer> patientsExitedFromCare = (List<Integer>) union(allPatientsExitedFromCare.list(), patientsVoided);
		return patientsExitedFromCare;
*/		
	}
	
	public List<Integer> getPatientsVoided() {
		Session session = sessionFactory.getCurrentSession();
		SQLQuery patientsVoided = session.createSQLQuery("select distinct patient_id from patient pe where pe.voided=1");
		
		return patientsVoided.list();
		
	}
	
	public List<Integer> getPatientsExitedFromCareDefinedLast(int concept, Date dateFormatedNew) {
		Session session = sessionFactory.getCurrentSession();
		CamerwaGlobalProperties gp =new CamerwaGlobalProperties();
		int exitedFromCareConceptId =gp.getConceptIdAsInt("camerwa.ExitedFromCareConceptId");
		SQLQuery patientsExitedFromCareLast = session
		        .createSQLQuery("SELECT distinct pa.patient_id FROM patient pa inner join person ps on pa.patient_id = ps.person_id inner join obs ob on ob.person_id=ps.person_id where ob.concept_id="+exitedFromCareConceptId+" and ob.obs_datetime < '"
		                + getDateFormatedFromDateObject(dateFormatedNew) + "' and value_coded=" + concept);
		return patientsExitedFromCareLast.list();
		
	}
	
	public List<Integer> getPatientsExitedFromCareDefinedNew(int concept, Date dateFormatedNew, Date dateFormatedLimite) {
		
		Session session = sessionFactory.getCurrentSession();
		CamerwaGlobalProperties gp =new CamerwaGlobalProperties();
		int exitedFromCareConceptId =gp.getConceptIdAsInt("camerwa.ExitedFromCareConceptId");
		SQLQuery patientsExitedFromCareNew = session
		        .createSQLQuery("SELECT distinct pa.patient_id FROM patient pa inner join person ps on pa.patient_id = ps.person_id inner join obs ob on ob.person_id=ps.person_id where ob.concept_id="+exitedFromCareConceptId+" and ob.obs_datetime >= '"
		                + getDateFormatedFromDateObject(dateFormatedNew)
		                + "' and ob.obs_datetime < '"
		                + getDateFormatedFromDateObject(dateFormatedLimite) + "' and value_coded=" + concept);
		
		List<Integer> patients = patientsExitedFromCareNew.list();
		
		return patients;
		
	}
	
	public List<Integer> getKidsUnderArvComprimeNew(Date dateFormatedNew, Date dateFormatedLimite) {
		Session session = sessionFactory.getCurrentSession();
		CamerwaGlobalProperties gp = new CamerwaGlobalProperties();
		String arvConceptIds = gp.getArvConceptIdList();
		SQLQuery kidsUnderArvComprimeNew = session
		        .createSQLQuery("select distinct p.patient_id from patient p inner join orders o on o.patient_id=p.patient_id"
		                + " inner join drug_order dor on dor.order_id=o.order_id"
		                + " inner join patient_program prog on prog.patient_id=p.patient_id "
		                + " inner join drug d on dor.drug_inventory_id=d.drug_id and d.name not like '%sirop%' and (prog.program_id = 2 or prog.program_id = 1) and prog.voided = 0 and d.concept_id in("+arvConceptIds+")"
		                + " inner join person on person_id=p.patient_id where o.start_date >= "
		                + "'"
		                + getDateFormatedFromDateObject(dateFormatedNew)
		                + "' and o.start_date < '"
		                + getDateFormatedFromDateObject(dateFormatedLimite)
		                + "'"
		                + "and o.discontinued = 0 and p.voided = 0 and o.voided = 0 and o.auto_expire_date is null and DATE_FORMAT(FROM_DAYS(TO_DAYS(NOW())-TO_DAYS(person.birthdate)), '%Y')+0 < 15 ;");
		return kidsUnderArvComprimeNew.list();
	}
	
	public List<Integer> getAllKidsUnderArvComprime(Date dateFormatedNew, Date dateFormatedLimite) {
		Session session = sessionFactory.getCurrentSession();
		CamerwaGlobalProperties gp = new CamerwaGlobalProperties();
		String arvConceptIds = gp.getArvConceptIdList();
		SQLQuery AllKidsUnderArvComprime = session
		        .createSQLQuery("select distinct p.patient_id from patient p inner join orders o on o.patient_id=p.patient_id"
		                + " inner join drug_order dor on dor.order_id=o.order_id"
		                + " inner join patient_program prog on prog.patient_id=p.patient_id "
		                + " inner join drug d on dor.drug_inventory_id=d.drug_id and d.name not like '%sirop%' and (prog.program_id = 2 or prog.program_id = 1) and prog.voided = 0 and d.concept_id in("+arvConceptIds+")"
		                + " inner join person on person_id=p.patient_id where o.start_date < "
		                + "'"
		                + getDateFormatedFromDateObject(dateFormatedNew)
		                + "'"
		                + "and o.discontinued = 0 and p.voided = 0 and o.voided = 0 and o.auto_expire_date is null and DATE_FORMAT(FROM_DAYS(TO_DAYS(NOW())-TO_DAYS(person.birthdate)), '%Y')+0 < 15 ;");
		return AllKidsUnderArvComprime.list();
	}
	
	public List<Integer> getKidsUnderArvSiropsNew(Date dateFormatedNew, Date dateFormatedLimite) {
		Session session = sessionFactory.getCurrentSession();
		CamerwaGlobalProperties gp = new CamerwaGlobalProperties();
		String arvConceptIds = gp.getArvConceptIdList();
		SQLQuery kidsUnderArvSiropsNew = session
		        .createSQLQuery("select distinct p.patient_id from patient p inner join orders o on o.patient_id=p.patient_id"
		                + " inner join drug_order dor on dor.order_id=o.order_id"
		                + " inner join patient_program prog on prog.patient_id=p.patient_id "
		                + " inner join drug d on dor.drug_inventory_id=d.drug_id and d.name like '%sirop%' and (prog.program_id = 2 or prog.program_id = 1) and prog.voided = 0 and d.concept_id in("+arvConceptIds+")"
		                + " inner join person on person_id=p.patient_id where o.start_date >= "
		                + "'"
		                + getDateFormatedFromDateObject(dateFormatedNew)
		                + "' and o.start_date < '"
		                + getDateFormatedFromDateObject(dateFormatedLimite)
		                + "'"
		                + "and o.discontinued = 0 and p.voided = 0 and o.voided = 0 and o.auto_expire_date is null and DATE_FORMAT(FROM_DAYS(TO_DAYS(NOW())-TO_DAYS(person.birthdate)), '%Y')+0 < 15 ;");
		return kidsUnderArvSiropsNew.list();
	}
	
	public List<Integer> getAllKidsUnderArvSiropsLast(Date dateFormatedNew, Date dateFormatedLimite) {
		Session session = sessionFactory.getCurrentSession();
		CamerwaGlobalProperties gp = new CamerwaGlobalProperties();
		String arvConceptIds = gp.getArvConceptIdList();
		SQLQuery allKidsUnderArvSiropsLast = session
		        .createSQLQuery("select distinct p.patient_id from patient p inner join orders o on o.patient_id=p.patient_id"
		                + " inner join drug_order dor on dor.order_id=o.order_id"
		                + " inner join patient_program prog on prog.patient_id=p.patient_id "
		                + " inner join drug d on dor.drug_inventory_id=d.drug_id and d.name like '%sirop%' and (prog.program_id = 2 or prog.program_id = 1) and prog.voided = 0 and d.concept_id in("+arvConceptIds+")"
		                + " inner join person on person_id=p.patient_id where o.start_date < "
		                + "'"
		                + getDateFormatedFromDateObject(dateFormatedNew)
		                + "'"
		                + "and o.discontinued = 0 and p.voided = 0 and o.voided = 0 and o.auto_expire_date is null and DATE_FORMAT(FROM_DAYS(TO_DAYS(NOW())-TO_DAYS(person.birthdate)), '%Y')+0 < 15 ;");
		return allKidsUnderArvSiropsLast.list();
	}
	
	public List<List<Integer>> getRegimenAdultNew(Date dateFormatedNew, Date dateFormatedLimite, Object drugConceptId1,
	                                        Object drugConceptId2, Object drugConceptId3, Object drugConceptId4) throws ParseException {
	//	log.info(" eeeeeee new eeeeeeeeeeeeeee ad"+drugConceptId1);
		
		List<Integer> patientsUnderRegimenAdultNew = new ArrayList<Integer>();
		List<Integer> adultRegimen4;
		List<Integer> adultRegimen1 = getRegimenAdultNewForOneDrug(dateFormatedNew, dateFormatedLimite, drugConceptId1);
		
		List<Integer> adultRegimen2 = getRegimenAdultNewForOneDrug(dateFormatedNew, dateFormatedLimite, drugConceptId2);
		
		List<Integer> adultRegimen3 = getRegimenAdultNewForOneDrug(dateFormatedNew, dateFormatedLimite, drugConceptId3);
		
			adultRegimen4 = getRegimenAdultNewForOneDrug(dateFormatedNew, dateFormatedLimite, drugConceptId4);
	//	log.info(" eeeeeeeeeeee new eeeeeeeeee adultRegimen1 "+adultRegimen1+" ssssssssssss adultRegimen2 "+adultRegimen2+"+========== new ======+"+adultRegimen3);	
		if (drugConceptId4 != null && adultRegimen4.size() > 0) {
			
			patientsUnderRegimenAdultNew = (List<Integer>) faurCollectionsIntersection(adultRegimen1, adultRegimen2,
			    adultRegimen3, adultRegimen4);
			
		} else if (drugConceptId4 == null || adultRegimen4.size() == 0) {
		
			patientsUnderRegimenAdultNew = (List<Integer>) treeCollectionsIntersection(adultRegimen1, adultRegimen2,
			    adultRegimen3);
			
		}
		if(adultRegimen4.size() == 0 && adultRegimen3.size() == 0 && adultRegimen2.size() == 0 && adultRegimen1.size() != 0 && drugConceptId2 == null && drugConceptId3 == null & drugConceptId4 == null){
			
			patientsUnderRegimenAdultNew = adultRegimen1;
		}
		
		
		
		List<Integer> ActivePtientsUnderRegimenAdultNew = getActivePatients(patientsUnderRegimenAdultNew,
		    patientsExitedFromCareList, dateFormatedLimite);
		
		
		
		List<Integer> patientsCurrentUnderArv = getOldPatientPerDate(dateFormatedNew);
		List<Integer> patientsSwimsNewButOld = (List<Integer>) twoCollectionsIntersection(patientsCurrentUnderArv, ActivePtientsUnderRegimenAdultNew) ;
		ActivePtientsUnderRegimenAdultNew.removeAll(patientsSwimsNewButOld);
	 
		List<List<Integer>> newAndOldPatients = new ArrayList<List<Integer>>();
		newAndOldPatients.add(ActivePtientsUnderRegimenAdultNew);
		newAndOldPatients.add(patientsSwimsNewButOld);
	 
		
		
		return  newAndOldPatients;
		
	}
	
	public List<Integer> getRegimenAdultNewForOneDrug(Date dateFormatedNew, Date dateFormatedLimite, Object drugConceptId1) {
		
		
		
		Session session = sessionFactory.getCurrentSession();
	//	log.info("drugConceptId1drugConceptId1drugConceptId this is new  "+getDateFormatedFromDateObject(dateFormatedNew)+"1drugConceptId  "+getDateFormatedFromDateObject(dateFormatedLimite)+"1drugConceptId1drugConceptId1 new "+drugConceptId1);
		SQLQuery patientsUnderRegimenAdultNewOneDrug = session
		        .createSQLQuery("select distinct p.patient_id from patient p inner join orders o on o.patient_id=p.patient_id"
		                + " inner join drug_order dor on dor.order_id=o.order_id"
		                /*+ " inner join drug d on dor.drug_inventory_id=d.drug_id and d.drug_id = "
		                + drugConceptId1*/
		                + " inner join person on person_id=p.patient_id where o.concept_id ="+drugConceptId1+" and o.start_date >= "
		                + "'"
		                + getDateFormatedFromDateObject(dateFormatedNew)
		                + "' and o.start_date < '"
		                + getDateFormatedFromDateObject(dateFormatedLimite)
		                + "'"
		                + "and o.voided = 0 and DATE_FORMAT(FROM_DAYS(TO_DAYS(NOW())-TO_DAYS(person.birthdate)), '%Y')+0 >= 15;");
	
	// log.info("patientsUnderRegimenAdultNewOneDrug.list() h this is again new "+patientsUnderRegimenAdultNewOneDrug.list());
		return patientsUnderRegimenAdultNewOneDrug.list();
		
	}public List<Integer> getRegimenAdultNewForOneDrugEfv600(Date dateFormatedNew, Date dateFormatedLimite, Object drugConceptId1) {
		
		Session session = sessionFactory.getCurrentSession();
		SQLQuery patientsUnderRegimenAdultNewOneDrug = session
		        .createSQLQuery("select distinct p.patient_id from patient p inner join orders o on o.patient_id=p.patient_id"
		                + " inner join drug_order dor on dor.order_id=o.order_id"
		                + " inner join drug d on dor.drug_inventory_id=d.drug_id and d.name like '%Efavirenz(600)%'and d.drug_id = "
		                + drugConceptId1
		                + " inner join person on person_id=p.patient_id where o.start_date >= "
		                + "'"
		                + getDateFormatedFromDateObject(dateFormatedNew)
		                + "' and o.start_date < '"
		                + getDateFormatedFromDateObject(dateFormatedLimite)
		                + "'"
		                + "and o.discontinued = 0 and o.discontinued_date is null and o.auto_expire_date is null and o.discontinued = 0 and DATE_FORMAT(FROM_DAYS(TO_DAYS(NOW())-TO_DAYS(person.birthdate)), '%Y')+0 >= 15;");
		return patientsUnderRegimenAdultNewOneDrug.list();
		
	}
	
	public List<Integer> getRegimenAdultLast(Date dateFormatedNew, Date dateFormatedLimite, Object drugConceptId1,
	                                         Object drugConceptId2, Object drugConceptId3, Object drugConceptId4) {
		List<Integer> concepts = getTheCollectionWithoutNull(drugConceptId1,drugConceptId2, drugConceptId3,drugConceptId4);
		
		// log.info("  i m getting in getregimenAdult method");
		List<Integer> allPatientUnderRegimenAdult = new ArrayList<Integer>();
		List<Integer> adultRegimen1 = getRegimenAdultLastOneDrug(dateFormatedNew, dateFormatedLimite, drugConceptId1);
		List<Integer> adultRegimen2 = getRegimenAdultLastOneDrug(dateFormatedNew, dateFormatedLimite, drugConceptId2);
		List<Integer> adultRegimen3 = getRegimenAdultLastOneDrug(dateFormatedNew, dateFormatedLimite, drugConceptId3);
		List<Integer> adultRegimen4 = getRegimenAdultLastOneDrug(dateFormatedNew, dateFormatedLimite, drugConceptId4);	
		
		log.info("ccccccccccccccc "+adultRegimen1+"vvvvvvvvv "+adultRegimen2+"  dddddddd "+adultRegimen3+" xxxxxxxxxx "+adultRegimen4);
		
		
		if (drugConceptId4 != null) {
			
			
			allPatientUnderRegimenAdult = (List<Integer>) faurCollectionsIntersection(adultRegimen1, adultRegimen2,
			    adultRegimen3, adultRegimen4);
		} else if (drugConceptId4 == null) {
			
			allPatientUnderRegimenAdult = (List<Integer>) treeCollectionsIntersection(adultRegimen1, adultRegimen2,
			    adultRegimen3);
			log.info("qqqqqqqqqqqqqqqqqqqqqq qqqqqqqqqqqqqqqqqq qqqqqqqqqq "+allPatientUnderRegimenAdult);	
		//	log.info("  i m getting in getregimenAdult method and in this condition "+allPatientUnderRegimenAdult+");// adultRegimen1 "+adultRegimen1+"adultRegimen2"+adultRegimen2+"adultRegimen3"+adultRegimen3);
		}
		if(drugConceptId4 == null && drugConceptId3 == null && drugConceptId2 == null && drugConceptId1 != null){
		
			allPatientUnderRegimenAdult = adultRegimen1;
		}
		
		List<Integer> patientUnderRegimenAdultLast = new ArrayList<Integer>();
		
		
		
		
		for (Integer currentPatientId : allPatientUnderRegimenAdult) {

	log.info(" currentPatientId  f currentPatientId currentPatientId new currentPatientId "+currentPatientId+" rere "+drugConceptId1+" drugConceptId2 "+drugConceptId2+" grgr "+drugConceptId3+" rererere "+drugConceptId4);		
			if (compareStartDatesByPatient(currentPatientId, drugConceptId1, drugConceptId2, drugConceptId3, drugConceptId4,
			    dateFormatedNew)) {
			
				patientUnderRegimenAdultLast.add(currentPatientId);
				
	 log.info("patientUnd   g new erRegimenAdultLastpatientUnderRegimenAdultLastpatientUnderRegimenAdultLastpatientUnd g erRegimenAdultLast"+patientUnderRegimenAdultLast);		
			}
		}
		
    try {
		
    	patientUnderRegimenAdultLast.addAll(getRegimenAdultNew(dateFormatedNew, dateFormatedLimite,
	        	drugConceptId1, drugConceptId2, drugConceptId3,drugConceptId4).get(1));
        }
        catch (ParseException e) {
	        // TODO Auto-generated catch block
	        log.error("Error generated", e);
        }
	  
        log.info("patientUnderRegimenAdultLast patientUnderRegimenAdultLast patientUnderRegimenAdultLastpatientUnderRegimenAdultLast patientUnderRegimenAdultLast patientUnderRegimenAdultLast "+patientUnderRegimenAdultLast);
		
		List<Integer> activePatientsUnderRegimenAdultLast = getActivePatients(patientUnderRegimenAdultLast,
		    patientsExitedFromCareList, dateFormatedLimite);
		
		return activePatientsUnderRegimenAdultLast;
	}
	
	public List<Integer> getRegimenAdultLastOneDrug(Date dateFormatedNew, Date dateFormatedLimite, Object drugConceptId1) {
		Session session = sessionFactory.getCurrentSession();
		
		//log.info("drugConceptId1drugConceptId1drugConceptId1drugConceptId1drugConceptId1drugConceptId1 last"+drugConceptId1);
		SQLQuery allPatientUnderRegimenAdultOnDrug = session
		        .createSQLQuery("select distinct p.patient_id from patient p inner join orders o on o.patient_id=p.patient_id"
		                + " inner join drug_order dor on dor.order_id=o.order_id"
		                /*+ " inner join drug d on dor.drug_inventory_id=d.drug_id and d.drug_id ="
		                + drugConceptId1*/
		                + " inner join person on person_id=p.patient_id where o.concept_id ="+drugConceptId1+" and o.start_date < "
		                + "'"
		                + getDateFormatedFromDateObject(dateFormatedNew)
		                + "'"
		                + "and DATE_FORMAT(FROM_DAYS(TO_DAYS(NOW())-TO_DAYS(person.birthdate)), '%Y')+0 >= 15;");
		//log.info("allPatientUnderRegimenAdultOnDrug.list()"+allPatientUnderRegimenAdultOnDrug.list());
		return allPatientUnderRegimenAdultOnDrug.list();
	}
	public List<Integer> getRegimenAdultLastOneDrugEvr600(Date dateFormatedNew, Date dateFormatedLimite, Object drugConceptId1) {
		Session session = sessionFactory.getCurrentSession();
		SQLQuery allPatientUnderRegimenAdultOnDrug = session
		        .createSQLQuery("select distinct p.patient_id from patient p inner join orders o on o.patient_id=p.patient_id"
		                + " inner join drug_order dor on dor.order_id=o.order_id"
		                + " inner join drug d on dor.drug_inventory_id=d.drug_id and d.name like '%Efavirenz(600)%' and d.drug_id ="
		                + drugConceptId1
		                + " inner join person on person_id=p.patient_id where o.start_date < "
		                + "'"
		                + getDateFormatedFromDateObject(dateFormatedNew)
		                + "'"
		                + "and o.discontinued = 0 and o.auto_expire_date is null and DATE_FORMAT(FROM_DAYS(TO_DAYS(NOW())-TO_DAYS(person.birthdate)), '%Y')+0 >= 15;");
		return allPatientUnderRegimenAdultOnDrug.list();
	}
	
	public List<Integer> getPediatricUnderRegimenLast(Date dateFormatedNew, Date dateFormatedLimite, Object drugConceptId1,
	                                                  Object drugConceptId2, Object drugConceptId3, Object drugConceptId4) {
		
		
		
		List<Integer> concepts = getTheCollectionWithoutNull(drugConceptId1,drugConceptId2, drugConceptId3,drugConceptId4);
		List<Integer> pediatricUnderRegimenLast = new ArrayList<Integer>();
		List<Integer> adultRegimen1 = getPediatricUnderRegimenLastOneDrug(dateFormatedNew, dateFormatedLimite,
		    drugConceptId1);
		List<Integer> adultRegimen2 = getPediatricUnderRegimenLastOneDrug(dateFormatedNew, dateFormatedLimite,
		    drugConceptId2);
		List<Integer> adultRegimen3 = getPediatricUnderRegimenLastOneDrug(dateFormatedNew, dateFormatedLimite,
		    drugConceptId3);
		List<Integer> adultRegimen4 = getPediatricUnderRegimenLastOneDrug(dateFormatedNew, dateFormatedLimite,
		    drugConceptId4);
		
		if (drugConceptId4 != null) {
			pediatricUnderRegimenLast = (List<Integer>) faurCollectionsIntersection(adultRegimen1, adultRegimen2,
			    adultRegimen3, adultRegimen4);
		} else if (drugConceptId4 == null) {
			pediatricUnderRegimenLast = (List<Integer>) treeCollectionsIntersection(adultRegimen1, adultRegimen2,
			    adultRegimen3);
		}
		if(adultRegimen4.size() == 0 && adultRegimen3.size() == 0 && adultRegimen2.size() == 0 && adultRegimen1.size() != 0){
	         
			pediatricUnderRegimenLast = adultRegimen1;
		}
		
		
		
		List<Integer> patientUnderRegimenPediatricLast = new ArrayList<Integer>();
		for (Integer currentPatientId : pediatricUnderRegimenLast) {
			if (compareStartDatesByPatient(currentPatientId, drugConceptId1, drugConceptId2, drugConceptId3, drugConceptId4,
			    dateFormatedNew)) {
				patientUnderRegimenPediatricLast.add(currentPatientId);
			}
		}
		
		patientUnderRegimenPediatricLast.addAll(getPediatricUnderRegimenNew(dateFormatedNew,
		    dateFormatedLimite, drugConceptId1, drugConceptId2,drugConceptId3,drugConceptId4).get(1));
		
		List<Integer> ActivePatientsUnderRegimenPediatricLast = getActivePatients(patientUnderRegimenPediatricLast,
		    patientsExitedFromCareList, dateFormatedLimite);
				
		
		return ActivePatientsUnderRegimenPediatricLast;
	}
	
	public List<Integer> getPediatricUnderRegimenLastOneDrug(Date dateFormatedNew, Date dateFormatedLimite,
	                                                         Object drugConceptId1) {
		Session session = sessionFactory.getCurrentSession();
		SQLQuery pediatricUnderRegimenLastOneDrug = session
		        .createSQLQuery("select distinct p.patient_id from patient p inner join orders o on o.patient_id=p.patient_id"
		                + " inner join drug_order dor on dor.order_id=o.order_id"
		                + " inner join drug d on dor.drug_inventory_id=d.drug_id and d.name not like '%sirop%' and o.concept_id ="
		                + drugConceptId1
		                + " inner join person on person_id=p.patient_id where o.start_date < "
		                + "'"
		                + getDateFormatedFromDateObject(dateFormatedNew)
		                + "'"
		                + "and o.discontinued = 0 and o.auto_expire_date is null and DATE_FORMAT(FROM_DAYS(TO_DAYS(NOW())-TO_DAYS(person.birthdate)), '%Y')+0 < 15 ;");
		
		return pediatricUnderRegimenLastOneDrug.list();
	}
	
	public List<List<Integer>> getPediatricUnderRegimenNew(Date dateFormatedNew, Date dateFormatedLimite, Object drugConceptId1,
	                                                 Object drugConceptId2, Object drugConceptId3,Object drugConceptId4) {
			
		List<Integer> pediatricUnderRegimenNew = new ArrayList<Integer>();
		List<Integer> adultRegimen1 = getPediatricUnderRegimenNewOneDrug(dateFormatedNew, dateFormatedLimite, drugConceptId1);
		List<Integer> adultRegimen2 = getPediatricUnderRegimenNewOneDrug(dateFormatedNew, dateFormatedLimite, drugConceptId2);
		List<Integer> adultRegimen3 = getPediatricUnderRegimenNewOneDrug(dateFormatedNew, dateFormatedLimite, drugConceptId3);
		List<Integer> adultRegimen4 = getPediatricUnderRegimenNewOneDrug(dateFormatedNew, dateFormatedLimite, drugConceptId4);
		
		
		if (drugConceptId4 != null) {
			pediatricUnderRegimenNew = (List<Integer>) faurCollectionsIntersection(adultRegimen1, adultRegimen2,
			    adultRegimen3, adultRegimen4);
		} else if (drugConceptId4 == null) {
			pediatricUnderRegimenNew = (List<Integer>) treeCollectionsIntersection(adultRegimen1, adultRegimen2,
			    adultRegimen3);
		}
		if(adultRegimen4.size() == 0 && adultRegimen3.size() == 0 && adultRegimen2.size() == 0 && adultRegimen1.size() != 0){
         
			pediatricUnderRegimenNew = adultRegimen1;
		}
		
		
		
		pediatricUnderRegimenNew = (List<Integer>) treeCollectionsIntersection(adultRegimen1, adultRegimen2, adultRegimen3);
		
		List<Integer> patientsCurrentUnderArv = getOldPatientPerDate(dateFormatedNew);
		List<Integer> patientsSwimsNewButOld = (List<Integer>) twoCollectionsIntersection(patientsCurrentUnderArv, pediatricUnderRegimenNew) ;
		pediatricUnderRegimenNew.removeAll(patientsSwimsNewButOld);

		List<List<Integer>> newAndOldPatients = new ArrayList<List<Integer>>();
		newAndOldPatients.add(pediatricUnderRegimenNew);
		newAndOldPatients.add(patientsSwimsNewButOld);
				
		return newAndOldPatients;
	}
	
	public List<Integer> getPediatricUnderRegimenNewOneDrug(Date dateFormatedNew, Date dateFormatedLimite,
	                                                        Object drugConceptId1) {
		Session session = sessionFactory.getCurrentSession();
		SQLQuery pediatricUnderRegimenNewOneDrug = session
		        .createSQLQuery("select distinct p.patient_id from patient p inner join orders o on o.patient_id=p.patient_id"
		                + " inner join drug_order dor on dor.order_id=o.order_id"
		                + " inner join drug d on dor.drug_inventory_id=d.drug_id and d.name not like '%sirop%' and o.concept_id ="
		                + drugConceptId1
		                + " inner join person on person_id=p.patient_id where o.start_date >= "
		                + "'"
		                + getDateFormatedFromDateObject(dateFormatedNew)
		                + "' and o.start_date < '"
		                + getDateFormatedFromDateObject(dateFormatedLimite)
		                + "'"
		                + "and o.voided = 0  and DATE_FORMAT(FROM_DAYS(TO_DAYS(NOW())-TO_DAYS(person.birthdate)), '%Y')+0 < 15 ;");
		
		return pediatricUnderRegimenNewOneDrug.list();
	}
	
	public List<List<Integer>> getPediatricUnderSecondLineRegimenNew(Date dateFormatedNew, Date dateFormatedLimite,
	                                                           Object drugConceptId1, Object drugConceptId2,
	                                                           Object drugConceptId3, Object drugConceptId4) {
		List<Integer> concepts = getTheCollectionWithoutNull(drugConceptId1,drugConceptId2, drugConceptId3,drugConceptId4);
		List<Integer> pediatricUnderSecondLineNew = new ArrayList<Integer>();
		List<Integer> kidsRegimen1 = getPediatricUnderSecondLineNewOneDrug(dateFormatedNew, dateFormatedLimite,
		    drugConceptId1);
		List<Integer> kidsRegimen2 = getPediatricUnderSecondLineNewOneDrug(dateFormatedNew, dateFormatedLimite,
		    drugConceptId2);
		List<Integer> kidsRegimen3 = getPediatricUnderSecondLineNewOneDrug(dateFormatedNew, dateFormatedLimite,
		    drugConceptId3);
		List<Integer> kidsRegimen4 = getPediatricUnderSecondLineNewOneDrug(dateFormatedNew, dateFormatedLimite,
		    drugConceptId4);
		
		if (drugConceptId4 != null) {
			pediatricUnderSecondLineNew = (List<Integer>) faurCollectionsIntersection(kidsRegimen1, kidsRegimen2,
			    kidsRegimen3, kidsRegimen4);
		} else if (drugConceptId4 == null) {
			pediatricUnderSecondLineNew = (List<Integer>) treeCollectionsIntersection(kidsRegimen1, kidsRegimen2,
			    kidsRegimen3);
		}
		
		if(kidsRegimen4.size() == 0 && kidsRegimen3.size() == 0 && kidsRegimen2.size() == 0 && kidsRegimen1.size() != 0){
	         
			pediatricUnderSecondLineNew = kidsRegimen1;
		}
		
		
		List<Integer> patientsCurrentUnderArv = getOldPatientPerDate(dateFormatedNew);
		List<Integer> patientsSwimsNewButOld = (List<Integer>) twoCollectionsIntersection(patientsCurrentUnderArv, pediatricUnderSecondLineNew) ;
		pediatricUnderSecondLineNew.removeAll(patientsSwimsNewButOld);
		
		
		List<List<Integer>> newAndOldPatients = new ArrayList<List<Integer>>();
		newAndOldPatients.add(pediatricUnderSecondLineNew);
		newAndOldPatients.add(patientsSwimsNewButOld);
			
		return newAndOldPatients;
	}
	
	//-----------------------------------------this is the pediatric sirop---------------------------------------------------------
	
	public List<List<Integer>> getPatientsUnderPediatricSiropNew(Date dateFormatedNew, Date dateFormatedLimite,
	                                                       Object drugConceptId1, Object drugConceptId2,
	                                                       Object drugConceptId3, Object drugConceptId4) {
		List<Integer> concepts = getTheCollectionWithoutNull(drugConceptId1,drugConceptId2, drugConceptId3,drugConceptId4);
		List<Integer> pediatricUnderSecondLineNew = new ArrayList<Integer>();
		List<Integer> kidsRegimen1 = getPediatricUnderPediatricSiropNewOneDrug(dateFormatedNew, dateFormatedLimite,
		    drugConceptId1);
		
		
		List<Integer> kidsRegimen2 = getPediatricUnderPediatricSiropNewOneDrug(dateFormatedNew, dateFormatedLimite,
		    drugConceptId2);
		
		List<Integer> kidsRegimen3 = getPediatricUnderPediatricSiropNewOneDrug(dateFormatedNew, dateFormatedLimite,
		    drugConceptId3);
		
		List<Integer> kidsRegimen4 = getPediatricUnderPediatricSiropNewOneDrug(dateFormatedNew, dateFormatedLimite,
		    drugConceptId4);
		
		if (drugConceptId4 != null) {
			pediatricUnderSecondLineNew = (List<Integer>) faurCollectionsIntersection(kidsRegimen1, kidsRegimen2,
			    kidsRegimen3, kidsRegimen4);
		} else if (drugConceptId4 == null) {
			pediatricUnderSecondLineNew = (List<Integer>) treeCollectionsIntersection(kidsRegimen1, kidsRegimen2,
			    kidsRegimen3);
		}
		if(kidsRegimen4.size() == 0 && kidsRegimen3.size() == 0 && kidsRegimen2.size() == 0 && kidsRegimen1.size() != 0){
	         
			pediatricUnderSecondLineNew = kidsRegimen1;
		}
		
		List<Integer> patientsCurrentUnderArv = getOldPatientPerDate(dateFormatedNew);
		List<Integer> patientsSwimsNewButOld = (List<Integer>) twoCollectionsIntersection(patientsCurrentUnderArv, pediatricUnderSecondLineNew) ;
		pediatricUnderSecondLineNew.removeAll(patientsSwimsNewButOld);
		
		
		List<List<Integer>> newAndOldPatients = new ArrayList<List<Integer>>();
		newAndOldPatients.add(pediatricUnderSecondLineNew);
		newAndOldPatients.add(patientsSwimsNewButOld);
		
		
		
		return newAndOldPatients;
	}
	
	public List<Integer> getPediatricUnderPediatricSiropNewOneDrug(Date dateFormatedNew, Date dateFormatedLimite,
	                                                               Object drugConceptId1) {
		Session session = sessionFactory.getCurrentSession();
		SQLQuery kidsDOBQueryNew = session
		        .createSQLQuery("select distinct p.patient_id from patient p inner join orders o on o.patient_id=p.patient_id"
		                + " inner join drug_order dor on dor.order_id=o.order_id"
		                + " inner join drug d on dor.drug_inventory_id=d.drug_id and d.name LIKE '%sirop%' and o.concept_id ="
		                + drugConceptId1
		                + " inner join person on person_id=p.patient_id where o.start_date >= "
		                + "'"
		                + getDateFormatedFromDateObject(dateFormatedNew)
		                + "' and o.start_date < '"
		                + getDateFormatedFromDateObject(dateFormatedLimite)
		                + "'"
		                + "and o.discontinued = 0 and o.auto_expire_date is null and DATE_FORMAT(FROM_DAYS(TO_DAYS(NOW())-TO_DAYS(person.birthdate)), '%Y')+0 < 15 ;");
		
   		return kidsDOBQueryNew.list();
	}
	                     
	public List<Integer> getPatientsUnderPediatricSiropLast(Date dateFormatedNew, Date dateFormatedLimite,
	                                                        Object drugConceptId1, Object drugConceptId2,
	                                                        Object drugConceptId3, Object drugConceptId4) {
		List<Integer> concepts = getTheCollectionWithoutNull(drugConceptId1,drugConceptId2, drugConceptId3,drugConceptId4);
		List<Integer> pediatricUnderSecondLineLast = new ArrayList<Integer>();
		List<Integer> kidsRegimen1 = getPediatricUnderPediatricSiropLastOneDrug(dateFormatedNew, dateFormatedLimite,
		    drugConceptId1);
		List<Integer> kidsRegimen2 = getPediatricUnderPediatricSiropLastOneDrug(dateFormatedNew, dateFormatedLimite,
		    drugConceptId2);
		List<Integer> kidsRegimen3 = getPediatricUnderPediatricSiropLastOneDrug(dateFormatedNew, dateFormatedLimite,
		    drugConceptId3);
		List<Integer> kidsRegimen4 = getPediatricUnderPediatricSiropLastOneDrug(dateFormatedNew, dateFormatedLimite,
		    drugConceptId4);
		
		if (drugConceptId4 != null) {
			pediatricUnderSecondLineLast = (List<Integer>) faurCollectionsIntersection(kidsRegimen1, kidsRegimen2,
			    kidsRegimen3, kidsRegimen4);
		} else if (drugConceptId4 == null) {
			pediatricUnderSecondLineLast = (List<Integer>) treeCollectionsIntersection(kidsRegimen1, kidsRegimen2,
			    kidsRegimen3);
		}
		if(kidsRegimen4.size() == 0 && kidsRegimen3.size() == 0 && kidsRegimen2.size() == 0 && kidsRegimen1.size() != 0){
	         
			pediatricUnderSecondLineLast = kidsRegimen1;
		}
		List<Integer> pediatricSecondLineLastToReturn = new ArrayList<Integer>();
		for (Integer currentPatientId : pediatricUnderSecondLineLast) {
			if (compareStartDatesByPatient(currentPatientId, drugConceptId1, drugConceptId2, drugConceptId3, drugConceptId4,
			    dateFormatedNew)) {
				pediatricSecondLineLastToReturn.add(currentPatientId);
			}
		}
		
		
		pediatricSecondLineLastToReturn.addAll(getPatientsUnderPediatricSiropNew(dateFormatedNew,
		    dateFormatedLimite, drugConceptId1, drugConceptId2,
		    drugConceptId3, drugConceptId4).get(1));
		
		List<Integer> activePatientsSecondLinePediatricLast = getActivePatients(pediatricSecondLineLastToReturn,
		    patientsExitedFromCareList, dateFormatedLimite);
		
		
		return activePatientsSecondLinePediatricLast;
		
	}
	
	public List<Integer> getPediatricUnderPediatricSiropLastOneDrug(Date dateFormatedNew, Date dateFormatedLimite,
	                                                                Object drugConceptId1) {
		Session session = sessionFactory.getCurrentSession();
		SQLQuery kidsDOBQueryNew = session
		        .createSQLQuery("select distinct p.patient_id from patient p inner join orders o on o.patient_id=p.patient_id"
		                + " inner join drug_order dor on dor.order_id=o.order_id"
		                + " inner join drug d on dor.drug_inventory_id=d.drug_id and d.name LIKE '%sirop%' and o.concept_id = "
		                + drugConceptId1
		                + " inner join person on person_id=p.patient_id where o.start_date < "
		                + "'"
		                + getDateFormatedFromDateObject(dateFormatedNew)
		                + "'"
		                + "and o.discontinued = 0 and o.auto_expire_date is null and DATE_FORMAT(FROM_DAYS(TO_DAYS(NOW())-TO_DAYS(person.birthdate)), '%Y')+0 < 15 ;");
		
		return kidsDOBQueryNew.list();
	}
	
	// ------------------------------- patients under third line regimens--------------------
	public List<List<Integer>> getPatientsUnderRegimenAdultThirdLineNew(Date dateFormatedNew, Date dateFormatedLimite,
	                                                              Object drugConceptId1, Object drugConceptId2,
	                                                              Object drugConceptId3, Object drugConceptId4) {
		List<Integer> concepts = getTheCollectionWithoutNull(drugConceptId1, drugConceptId2, drugConceptId3, drugConceptId4);
		List<Integer> pediatricUnderSecondLineNew = new ArrayList<Integer>();
		List<Integer> kidsRegimen1 = getPediatricUnderRegimenAdultThirdLineNewOneDrug(dateFormatedNew, dateFormatedLimite,
		    drugConceptId1);
		
		
		List<Integer> kidsRegimen2 = getPediatricUnderRegimenAdultThirdLineNewOneDrug(dateFormatedNew, dateFormatedLimite,
		    drugConceptId2);
		
		List<Integer> kidsRegimen3 = getPediatricUnderRegimenAdultThirdLineNewOneDrug(dateFormatedNew, dateFormatedLimite,
		    drugConceptId3);
		
		List<Integer> kidsRegimen4 = getPediatricUnderRegimenAdultThirdLineNewOneDrug(dateFormatedNew, dateFormatedLimite,
		    drugConceptId4);
		
		if (drugConceptId4 != null) {
			pediatricUnderSecondLineNew = (List<Integer>) faurCollectionsIntersection(kidsRegimen1, kidsRegimen2,
			    kidsRegimen3, kidsRegimen4);
		} else if (drugConceptId4 == null) {
			pediatricUnderSecondLineNew = (List<Integer>) treeCollectionsIntersection(kidsRegimen1, kidsRegimen2,
			    kidsRegimen3);
		}
		if (kidsRegimen4.size() == 0 && kidsRegimen3.size() == 0 && kidsRegimen2.size() == 0 && kidsRegimen1.size() != 0) {
			
			pediatricUnderSecondLineNew = kidsRegimen1;
		}
	
		List<Integer> patientsCurrentUnderArv = getOldPatientPerDate(dateFormatedNew);
		List<Integer> patientsSwimsNewButOld = (List<Integer>) twoCollectionsIntersection(patientsCurrentUnderArv, pediatricUnderSecondLineNew) ;
		pediatricUnderSecondLineNew.removeAll(patientsSwimsNewButOld);

		
		List<List<Integer>> newAndOldPatients = new ArrayList<List<Integer>>();
		newAndOldPatients.add(pediatricUnderSecondLineNew);
		newAndOldPatients.add(patientsSwimsNewButOld);
		
			
		return newAndOldPatients;
	}

public List<Integer> getPediatricUnderRegimenAdultThirdLineNewOneDrug(Date dateFormatedNew, Date dateFormatedLimite,
                Object drugConceptId1) {
Session session = sessionFactory.getCurrentSession();
SQLQuery kidsDOBQueryNew = session
.createSQLQuery("select distinct p.patient_id from patient p inner join orders o on o.patient_id=p.patient_id"
    + " inner join drug_order dor on dor.order_id=o.order_id"
    /*+ " inner join drug d on dor.drug_inventory_id=d.drug_id and d.drug_id ="
    + drugConceptId1*/
    + " inner join person on person_id=p.patient_id where o.concept_id ="+drugConceptId1+" and o.start_date >= "
    + "'"
    + getDateFormatedFromDateObject(dateFormatedNew)
    + "' and o.start_date < '"
    + getDateFormatedFromDateObject(dateFormatedLimite)
    + "'"
    + "and o.discontinued = 0 and o.auto_expire_date is null and DATE_FORMAT(FROM_DAYS(TO_DAYS(NOW())-TO_DAYS(person.birthdate)), '%Y')+0 >= 15 ;");
return kidsDOBQueryNew.list();
}

	
	public List<Integer> getPatientsUnderRegimenAdultThirdLineLast(Date dateFormatedNew, Date dateFormatedLimite,
	                                                               Object drugConceptId1, Object drugConceptId2,
	                                                               Object drugConceptId3, Object drugConceptId4) {
		List<Integer> concepts = getTheCollectionWithoutNull(drugConceptId1, drugConceptId2, drugConceptId3, drugConceptId4);
		List<Integer> pediatricUnderSecondLineLast = new ArrayList<Integer>();
		List<Integer> kidsRegimen1 = getPediatricUnderRegimenAdultThirdLineLastOneDrug(dateFormatedNew, dateFormatedLimite,
		    drugConceptId1);
		List<Integer> kidsRegimen2 = getPediatricUnderRegimenAdultThirdLineLastOneDrug(dateFormatedNew, dateFormatedLimite,
		    drugConceptId2);
		List<Integer> kidsRegimen3 = getPediatricUnderRegimenAdultThirdLineLastOneDrug(dateFormatedNew, dateFormatedLimite,
		    drugConceptId3);
		List<Integer> kidsRegimen4 = getPediatricUnderRegimenAdultThirdLineLastOneDrug(dateFormatedNew, dateFormatedLimite,
		    drugConceptId4);
		
		if (drugConceptId4 != null) {
			pediatricUnderSecondLineLast = (List<Integer>) faurCollectionsIntersection(kidsRegimen1, kidsRegimen2,
			    kidsRegimen3, kidsRegimen4);
		} else if (drugConceptId4 == null) {
			pediatricUnderSecondLineLast = (List<Integer>) treeCollectionsIntersection(kidsRegimen1, kidsRegimen2,
			    kidsRegimen3);
		}
		
		if (kidsRegimen4.size() == 0 && kidsRegimen3.size() == 0 && kidsRegimen2.size() == 0 && kidsRegimen1.size() != 0) {
			
			pediatricUnderSecondLineLast = kidsRegimen1;
		}
		List<Integer> pediatricSecondLineLastToReturn = new ArrayList<Integer>();
		for (Integer currentPatientId : pediatricUnderSecondLineLast) {
			if (compareStartDatesByPatient(currentPatientId, drugConceptId1, drugConceptId2, drugConceptId3, drugConceptId4,
			    dateFormatedNew)) {
				pediatricSecondLineLastToReturn.add(currentPatientId);
			}
		}
		pediatricSecondLineLastToReturn.addAll(getPatientsUnderRegimenAdultThirdLineNew(dateFormatedNew,
		    dateFormatedLimite, drugConceptId1, drugConceptId1,
		    drugConceptId1, drugConceptId1).get(1));
		
		
		List<Integer> activePatientsSecondLinePediatricLast = getActivePatients(pediatricSecondLineLastToReturn,
		    patientsExitedFromCareList, dateFormatedLimite);
		
		
		return activePatientsSecondLinePediatricLast;
		
	}

public List<Integer> getPediatricUnderRegimenAdultThirdLineLastOneDrug(Date dateFormatedNew, Date dateFormatedLimite,
                 Object drugConceptId1) {
Session session = sessionFactory.getCurrentSession();
SQLQuery kidsDOBQueryNew = session
.createSQLQuery("select distinct p.patient_id from patient p inner join orders o on o.patient_id=p.patient_id"
    + " inner join drug_order dor on dor.order_id=o.order_id"
    /*+ " inner join drug d on dor.drug_inventory_id=d.drug_id and d.drug_id = "
    + drugConceptId1*/
    + " inner join person on person_id=p.patient_id where o.concept_id ="+drugConceptId1+" and o.start_date < "
    + "'"
    + getDateFormatedFromDateObject(dateFormatedNew)
    + "'"
    + "and o.discontinued = 0 and o.auto_expire_date is null and DATE_FORMAT(FROM_DAYS(TO_DAYS(NOW())-TO_DAYS(person.birthdate)), '%Y')+0 >= 15 ;");

return kidsDOBQueryNew.list();
}

	
		
	public List<List<Integer>> getAdultUnderSecondLineRegimenNew(Date dateFormatedNew, Date dateFormatedLimite,
	                                                       Object drugConceptId1, Object drugConceptId2,
	                                                       Object drugConceptId3, Object drugConceptId4) {
		
		List<Integer> concepts = getTheCollectionWithoutNull(drugConceptId1,drugConceptId2, drugConceptId3,drugConceptId4);
		List<Integer> adultUnderSecondLineNew = new ArrayList<Integer>();
		List<Integer> adultRegimen1 = getAdultUnderSecondLineNewOneDrug(dateFormatedNew, dateFormatedLimite, drugConceptId1);
		List<Integer> adultRegimen2 = getAdultUnderSecondLineNewOneDrug(dateFormatedNew, dateFormatedLimite, drugConceptId2);
		List<Integer> adultRegimen3 = getAdultUnderSecondLineNewOneDrug(dateFormatedNew, dateFormatedLimite, drugConceptId3);
		List<Integer> adultRegimen4 = getAdultUnderSecondLineNewOneDrug(dateFormatedNew, dateFormatedLimite, drugConceptId4);
		
				
		if (drugConceptId4 != null) {
			
			adultUnderSecondLineNew = (List<Integer>) faurCollectionsIntersection(adultRegimen1, adultRegimen2,
			    adultRegimen3, adultRegimen4);
		} else if (drugConceptId4 == null ) {
			adultUnderSecondLineNew = (List<Integer>) treeCollectionsIntersection(adultRegimen1, adultRegimen2,
			    adultRegimen3);
		}
		if(adultRegimen4.size() == 0 && adultRegimen3.size() == 0 && adultRegimen2.size() == 0 && adultRegimen1.size() != 0){
		    
			adultUnderSecondLineNew = adultRegimen1;
		}
		
		List<Integer> patientsCurrentUnderArv = getOldPatientPerDate(dateFormatedNew);
		List<Integer> patientsSwimsNewButOld = (List<Integer>) twoCollectionsIntersection(patientsCurrentUnderArv,adultUnderSecondLineNew) ;
		adultUnderSecondLineNew.removeAll(patientsSwimsNewButOld);
		
		
		List<List<Integer>> newAndOldPatients = new ArrayList<List<Integer>>();
		newAndOldPatients.add(adultUnderSecondLineNew);
		newAndOldPatients.add(patientsSwimsNewButOld);
		
		return newAndOldPatients;
	}
	
	public List<Integer> getPediatricUnderSecondLineNewOneDrug(Date dateFormatedNew, Date dateFormatedLimite,
	                                                           Object drugConceptId1) {
		Session session = sessionFactory.getCurrentSession();
		SQLQuery kidsDOBQueryNew = session
		        .createSQLQuery("select distinct p.patient_id from patient p inner join orders o on o.patient_id=p.patient_id"
		                + " inner join drug_order dor on dor.order_id=o.order_id"
		                /*+ " inner join drug d on dor.drug_inventory_id=d.drug_id and d.drug_id ="
		                + drugConceptId1*/
		                + " inner join person on person_id=p.patient_id where o.concept_id ="+drugConceptId1+" and o.start_date >= "
		                + "'"
		                + getDateFormatedFromDateObject(dateFormatedNew)
		                + "' and o.start_date < '"
		                + getDateFormatedFromDateObject(dateFormatedLimite)
		                + "'"
		                + "and o.discontinued = 0 and o.auto_expire_date is null and DATE_FORMAT(FROM_DAYS(TO_DAYS(NOW())-TO_DAYS(person.birthdate)), '%Y')+0 < 15 ;");
		return kidsDOBQueryNew.list();
	}
	
	public List<Integer> getAdultUnderSecondLineNewOneDrug(Date dateFormatedNew, Date dateFormatedLimite,
	                                                       Object drugConceptId1) {
		Session session = sessionFactory.getCurrentSession();
		SQLQuery kidsDOBQueryNew = session
		        .createSQLQuery("select distinct p.patient_id from patient p inner join orders o on o.patient_id=p.patient_id"
		                + " inner join drug_order dor on dor.order_id=o.order_id"
		                /*+ " inner join drug d on dor.drug_inventory_id=d.drug_id and d.drug_id ="
		                + drugConceptId1*/
		                + " inner join person on person_id=p.patient_id where o.concept_id ="+drugConceptId1+" and o.start_date >= "
		                + "'"
		                + getDateFormatedFromDateObject(dateFormatedNew)
		                + "' and o.start_date < '"
		                + getDateFormatedFromDateObject(dateFormatedLimite)
		                + "'"
		                + "and o.discontinued = 0 and o.auto_expire_date is null and DATE_FORMAT(FROM_DAYS(TO_DAYS(NOW())-TO_DAYS(person.birthdate)), '%Y')+0 >= 15 ;");
		return kidsDOBQueryNew.list();
	}
	
	public List<Integer> getPediatricUnderSecondLineRegimenLast(Date dateFormatedNew, Date dateFormatedLimite,
	                                                            Object drugConceptId1, Object drugConceptId2,
	                                                            Object drugConceptId3, Object drugConceptId4) {
		
		List<Integer> concepts = getTheCollectionWithoutNull(drugConceptId1,drugConceptId2, drugConceptId3,drugConceptId4);
		List<Integer> pediatricUnderSecondLineLast = new ArrayList<Integer>();
		List<Integer> kidsRegimen1 = getPediatricUnderSecondLineLastOneDrug(dateFormatedNew, dateFormatedLimite,
		    drugConceptId1);
		List<Integer> kidsRegimen2 = getPediatricUnderSecondLineLastOneDrug(dateFormatedNew, dateFormatedLimite,
		    drugConceptId2);
		List<Integer> kidsRegimen3 = getPediatricUnderSecondLineLastOneDrug(dateFormatedNew, dateFormatedLimite,
		    drugConceptId3);
		List<Integer> kidsRegimen4 = getPediatricUnderSecondLineLastOneDrug(dateFormatedNew, dateFormatedLimite,
		    drugConceptId4);
		
		if (drugConceptId4 != null) {
			pediatricUnderSecondLineLast = (List<Integer>) faurCollectionsIntersection(kidsRegimen1, kidsRegimen2,
			    kidsRegimen3, kidsRegimen4);
		} else if (drugConceptId4 == null) {
			pediatricUnderSecondLineLast = (List<Integer>) treeCollectionsIntersection(kidsRegimen1, kidsRegimen2,
			    kidsRegimen3);
		}
        if(kidsRegimen4.size() == 0 && kidsRegimen3.size() == 0 && kidsRegimen2.size() == 0 && kidsRegimen1.size() != 0){
		    
        	pediatricUnderSecondLineLast = kidsRegimen1;
		}
		
		List<Integer> pediatricSecondLineLastToReturn = new ArrayList<Integer>();
		for (Integer currentPatientId : pediatricUnderSecondLineLast) {
			if (compareStartDatesByPatient(currentPatientId, drugConceptId1, drugConceptId2, drugConceptId3, drugConceptId4,
			    dateFormatedNew)) {
				pediatricSecondLineLastToReturn.add(currentPatientId);
			}
		}
		
		pediatricSecondLineLastToReturn.addAll(getPediatricUnderSecondLineRegimenNew(dateFormatedNew,
		    dateFormatedLimite, drugConceptId1, drugConceptId2, drugConceptId3,drugConceptId4).get(1));
		
		
		List<Integer> activePatientsSecondLinePediatricLast = getActivePatients(pediatricSecondLineLastToReturn,
		    patientsExitedFromCareList, dateFormatedLimite);
		
		
		return activePatientsSecondLinePediatricLast;
		
	}
	
	public List<Integer> getAdultUnderSecondLineRegimenLast(Date dateFormatedNew, Date dateFormatedLimite,
	                                                        Object drugConceptId1, Object drugConceptId2,
	                                                        Object drugConceptId3, Object drugConceptId4) {
		List<Integer> concepts = getTheCollectionWithoutNull(drugConceptId1,drugConceptId2, drugConceptId3,drugConceptId4);
		List<Integer> adultUnderSecondLineLast = new ArrayList<Integer>();
		List<Integer> adultRegimen1 = getAdultUnderSecondLineLastOneDrug(dateFormatedNew, dateFormatedLimite, drugConceptId1);
		List<Integer> adultRegimen2 = getAdultUnderSecondLineLastOneDrug(dateFormatedNew, dateFormatedLimite, drugConceptId2);
		List<Integer> adultRegimen3 = getAdultUnderSecondLineLastOneDrug(dateFormatedNew, dateFormatedLimite, drugConceptId3);
		List<Integer> adultRegimen4 = getAdultUnderSecondLineLastOneDrug(dateFormatedNew, dateFormatedLimite, drugConceptId4);
		if (drugConceptId4 != null) {
			adultUnderSecondLineLast = (List<Integer>) faurCollectionsIntersection(adultRegimen1, adultRegimen2,
			    adultRegimen3, adultRegimen4);
		} else {
			adultUnderSecondLineLast = (List<Integer>) treeCollectionsIntersection(adultRegimen1, adultRegimen2,
			    adultRegimen3);
		}
         if(adultRegimen4.size() == 0 && adultRegimen3.size() == 0 && adultRegimen2.size() == 0 && adultRegimen1.size() != 0){
		    
        	 adultUnderSecondLineLast = adultRegimen1;
		}
		List<Integer> adultSecondLineLastToReturn = new ArrayList<Integer>();
		for (Integer currentPatientId : adultUnderSecondLineLast) {
			if (compareStartDatesByPatient(currentPatientId, drugConceptId1, drugConceptId2, drugConceptId3, drugConceptId4,
			    dateFormatedNew)) {
				adultSecondLineLastToReturn.add(currentPatientId);
			}
		}
		
		adultSecondLineLastToReturn.addAll(getAdultUnderSecondLineRegimenNew(dateFormatedNew,
		    dateFormatedLimite,drugConceptId1, drugConceptId2, drugConceptId3,
		    drugConceptId4).get(1));
		
		List<Integer> activeAdultSecondLinePediatricLast = getActivePatients(adultSecondLineLastToReturn,
		    patientsExitedFromCareList, dateFormatedLimite);
		
		
		return activeAdultSecondLinePediatricLast;
		
	}
	
	public List<List<Integer>> getPatientsUnderProphylaxieNew(Date dateFormatedNew, Date dateFormatedLimite,
	                                                    Object drugConceptId1, Object drugConceptId2, Object drugConceptId3,
	                                                    Object drugConceptId4) {
		List<Integer> concepts = getTheCollectionWithoutNull(drugConceptId1,drugConceptId2, drugConceptId3,drugConceptId4);
		List<Integer> patientsUnderProphylaxieNew = new ArrayList<Integer>();
		List<Integer> adultRegimen1 = getPatientsUnderProphylaxieNewOneDrug(dateFormatedNew, dateFormatedLimite,
		    drugConceptId1);
		List<Integer> adultRegimen2 = getPatientsUnderProphylaxieNewOneDrug(dateFormatedNew, dateFormatedLimite,
		    drugConceptId2);
		List<Integer> adultRegimen3 = getPatientsUnderProphylaxieNewOneDrug(dateFormatedNew, dateFormatedLimite,
		    drugConceptId3);
		List<Integer> adultRegimen4 = getPatientsUnderProphylaxieNewOneDrug(dateFormatedNew, dateFormatedLimite,
		    drugConceptId4);
		
		if (drugConceptId4 == null && drugConceptId3 != null) {
			patientsUnderProphylaxieNew = (List<Integer>) treeCollectionsIntersection(adultRegimen1, adultRegimen2,
			    adultRegimen3);
		} else if (drugConceptId4 == null && drugConceptId3 == null) {
			patientsUnderProphylaxieNew = (List<Integer>) twoCollectionsIntersection(adultRegimen1, adultRegimen2);
		}
		if(adultRegimen4.size() == 0 && adultRegimen3.size() == 0 && adultRegimen2.size() == 0 && adultRegimen1.size() != 0){
		    
			patientsUnderProphylaxieNew = adultRegimen1;
		}
	
		List<Integer> patientsCurrentUnderArv = getOldPatientPerDate(dateFormatedNew);
		List<Integer> patientsSwimsNewButOld = (List<Integer>) twoCollectionsIntersection(patientsCurrentUnderArv, patientsUnderProphylaxieNew) ;
		patientsUnderProphylaxieNew.removeAll(patientsSwimsNewButOld);

		
		List<List<Integer>> newAndOldPatients = new ArrayList<List<Integer>>();
		newAndOldPatients.add(patientsUnderProphylaxieNew);
		newAndOldPatients.add(patientsSwimsNewButOld);
		
		
		
		return   newAndOldPatients ;
	}
	
	public List<Integer> getPatientsUnderProphylaxieNewOneDrug(Date dateFormatedNew, Date dateFormatedLimite,
	                                                           Object drugConceptId1) {
		Session session = sessionFactory.getCurrentSession();
		SQLQuery kidsDOBQueryNew = session
		        .createSQLQuery("select distinct p.patient_id from patient p inner join orders o on o.patient_id=p.patient_id"
		                + " inner join drug_order dor on dor.order_id=o.order_id"
		                /*+ " inner join drug d on dor.drug_inventory_id=d.drug_id and d.drug_id ="
		                + drugConceptId1*/
		                + " inner join person on person_id=p.patient_id where o.concept_id ="+drugConceptId1+" and o.start_date >= "
		                + "'"
		                + getDateFormatedFromDateObject(dateFormatedNew)
		                + "' and o.start_date < '"
		                + getDateFormatedFromDateObject(dateFormatedLimite)
		                + "'"
		                + "and o.discontinued = 0 and o.auto_expire_date is null;");
		return kidsDOBQueryNew.list();
	}
	
	public List<Integer> getPatientsUnderProphylaxieLast(Date dateFormatedNew, Date dateFormatedLimite,
	                                                     Object drugConceptId1, Object drugConceptId2,
	                                                     Object drugConceptId3, Object drugConceptId4) {
		List<Integer> concepts = getTheCollectionWithoutNull(drugConceptId1,drugConceptId2, drugConceptId3,drugConceptId4);
		List<Integer> patientsUnderProphylaxieLast = new ArrayList<Integer>();
		List<Integer> adultRegimen1 = getPatientsUnderProphylaxieLastOneDrug(dateFormatedNew, dateFormatedLimite,
		    drugConceptId1);
		List<Integer> adultRegimen2 = getPatientsUnderProphylaxieLastOneDrug(dateFormatedNew, dateFormatedLimite,
		    drugConceptId2);
		List<Integer> adultRegimen3 = getPatientsUnderProphylaxieLastOneDrug(dateFormatedNew, dateFormatedLimite,
		    drugConceptId3);
		List<Integer> adultRegimen4 = getPatientsUnderProphylaxieLastOneDrug(dateFormatedNew, dateFormatedLimite,
		    drugConceptId4);
		
		if (drugConceptId4 == null && drugConceptId3 != null) {
			patientsUnderProphylaxieLast = (List<Integer>) treeCollectionsIntersection(adultRegimen1, adultRegimen2,
			    adultRegimen3);
		} else if (drugConceptId4 == null && drugConceptId3 == null) {
			patientsUnderProphylaxieLast = (List<Integer>) twoCollectionsIntersection(adultRegimen1, adultRegimen2);
		}
        if(adultRegimen4.size() == 0 && adultRegimen3.size() == 0 && adultRegimen2.size() == 0 && adultRegimen1.size() != 0){
		    
        	patientsUnderProphylaxieLast = adultRegimen1;
		}
		List<Integer> patientsProphylaxieLastToReturn = new ArrayList<Integer>();
		for (Integer currentPatientId : patientsUnderProphylaxieLast) {
			if (compareStartDatesByPatient(currentPatientId, drugConceptId1, drugConceptId2, drugConceptId3, drugConceptId4,
			    dateFormatedNew)) {
				patientsProphylaxieLastToReturn.add(currentPatientId);
			}
		}
		
		patientsProphylaxieLastToReturn.addAll(getPatientsUnderProphylaxieNew(dateFormatedNew,
		    dateFormatedLimite, drugConceptId1, drugConceptId2,
		    drugConceptId3, drugConceptId4).get(1));
		
		
		List<Integer> activePatiensUnderProphylaxieLast = getActivePatients(patientsProphylaxieLastToReturn,
		    patientsExitedFromCareList, dateFormatedLimite);
		
		
		return activePatiensUnderProphylaxieLast;
		
	}
	
	public List<Integer> getPatientsUnderProphylaxieLastOneDrug(Date dateFormatedNew, Date dateFormatedLimite,
	                                                            Object drugConceptId1) {
		Session session = sessionFactory.getCurrentSession();
		SQLQuery kidsDOBQueryNew = session
		        .createSQLQuery("select distinct p.patient_id from patient p inner join orders o on o.patient_id=p.patient_id"
		                + " inner join drug_order dor on dor.order_id=o.order_id"
		                /*+ " inner join drug d on dor.drug_inventory_id=d.drug_id and d.drug_id = "
		                + drugConceptId1*/
		                + " inner join person on person_id=p.patient_id where o.concept_id ="+drugConceptId1+" and o.start_date < "
		                + "'"
		                + getDateFormatedFromDateObject(dateFormatedNew)
		                + "'"
		                + "and o.discontinued = 0 and o.auto_expire_date is null and DATE_FORMAT(FROM_DAYS(TO_DAYS(NOW())-TO_DAYS(person.birthdate)), '%Y')+0 >= 15 ;");
		
		return kidsDOBQueryNew.list();
	}
	
	
	
	public List<List<Integer>> getPatientsUnderPmtctNew(Date dateFormatedNew, Date dateFormatedLimite, Object drugConceptId1,
	                                              Object drugConceptId2, Object drugConceptId3, Object drugConceptId4) {
		List<Integer> concepts = getTheCollectionWithoutNull(drugConceptId1,drugConceptId2, drugConceptId3,drugConceptId4);
		
		List<Integer> patientsUnderPtctNew = new ArrayList<Integer>();
		List<Integer> adultRegimen1;
		
		    adultRegimen1 = getPatientsUnderPmtcNewOneDrug(dateFormatedNew, dateFormatedLimite, drugConceptId1);
		
		List<Integer> adultRegimen2 = getPatientsUnderPmtcNewOneDrug(dateFormatedNew, dateFormatedLimite, drugConceptId2);
		List<Integer> adultRegimen3 = getPatientsUnderPmtcNewOneDrug(dateFormatedNew, dateFormatedLimite, drugConceptId3);
		List<Integer> adultRegimen4 = getPatientsUnderPmtcNewOneDrug(dateFormatedNew, dateFormatedLimite, drugConceptId4);
		
		if (drugConceptId1 != null && drugConceptId2 == null && drugConceptId3 == null && drugConceptId4 == null) {
			patientsUnderPtctNew = adultRegimen1;
		} else if (drugConceptId1 != null && drugConceptId2 != null && drugConceptId3 != null && drugConceptId4 == null) {
			patientsUnderPtctNew = (List<Integer>) treeCollectionsIntersection(adultRegimen1, adultRegimen2, adultRegimen3);
		}
		
        if(adultRegimen4.size() == 0 && adultRegimen3.size() == 0 && adultRegimen2.size() == 0 && adultRegimen1.size() != 0){
		    
        	patientsUnderPtctNew = adultRegimen1;
		}
		
		
        List<Integer> patientsCurrentUnderArv = getOldPatientPerDate(dateFormatedNew);
        List<Integer> patientsSwimsNewButOld = (List<Integer>) twoCollectionsIntersection(patientsCurrentUnderArv, patientsUnderPtctNew) ;
        patientsUnderPtctNew.removeAll(patientsSwimsNewButOld);
        
        List<List<Integer>> newAndOldPatients = new ArrayList<List<Integer>>();
        newAndOldPatients.add(patientsUnderPtctNew);
        newAndOldPatients.add(patientsSwimsNewButOld);
       
        
		return   newAndOldPatients;
	}
	
	public List<Integer> getPatientsUnderPmtcNewOneDrug(Date dateFormatedNew, Date dateFormatedLimite, Object drugConceptId1) {
		Session session = sessionFactory.getCurrentSession();
		SQLQuery kidsDOBQueryNew = session
		        .createSQLQuery("select distinct p.patient_id from patient p inner join orders o on o.patient_id=p.patient_id"
		                + " inner join drug_order dor on dor.order_id=o.order_id"
		                /*+ " inner join drug d on dor.drug_inventory_id=d.drug_id and d.drug_id ="
		                + drugConceptId1*/
		                +" inner join patient_program pp on p.patient_id= pp.patient_id "
	                    +" inner join program pro on pp.program_Id = pro.program_id and pro.concept_id=1647"
		                + " inner join person on person_id=p.patient_id where o.concept_id ="+drugConceptId1+" and o.start_date >= "
		                + "'"
		                + getDateFormatedFromDateObject(dateFormatedNew)
		                + "' and o.start_date < '"
		                + getDateFormatedFromDateObject(dateFormatedLimite)
		                + "'"
		                + "and o.discontinued = 0 and o.auto_expire_date is null;");
		return kidsDOBQueryNew.list();
	}
	public List<Integer> getPatientsUnderPmtcNewOneDrugSirop(Date dateFormatedNew, Date dateFormatedLimite, Object drugConceptId1) {
		Session session = sessionFactory.getCurrentSession();
		SQLQuery kidsDOBQueryNew = session
		        .createSQLQuery("select distinct p.patient_id from patient p inner join orders o on o.patient_id=p.patient_id"
		                + " inner join drug_order dor on dor.order_id=o.order_id"
		                /*+ " inner join drug d on dor.drug_inventory_id=d.drug_id and d.name LIKE '%sirop%' and d.drug_id ="
		                + drugConceptId1*/
		                +" inner join patient_program pp on p.patient_id= pp.patient_id "
	                    +" inner join program pro on pp.program_Id = pro.program_id and pro.concept_id=1647" 
		                +" inner join person on person_id=p.patient_id where o.concept_id ="+drugConceptId1+" and o.start_date >= "
		                + "'"
		                + getDateFormatedFromDateObject(dateFormatedNew)
		                + "' and o.start_date < '"
		                + getDateFormatedFromDateObject(dateFormatedLimite)
		                + "'"
		                + "and o.discontinued = 0 and o.auto_expire_date is null;");
		return kidsDOBQueryNew.list();
	}
	
	public List<Integer> getPatientsUnderPmtctLast(Date dateFormatedNew,Date dateFormatedLimite, Object drugConceptId1,
	                                               Object drugConceptId2, Object drugConceptId3, Object drugConceptId4) {
		
		List<Integer> concepts = getTheCollectionWithoutNull(drugConceptId1,drugConceptId2, drugConceptId3,drugConceptId4);
		
		List<Integer> patientsUnderPmtctLast = new ArrayList<Integer>();
		List<Integer> adultRegimen1;
	
			adultRegimen1 = getPatientsUnderPmtcLastOneDrugSirop(dateFormatedNew, dateFormatedLimite, drugConceptId1);
			
	
			adultRegimen1 = getPatientsUnderPmtcLastOneDrug(dateFormatedNew, dateFormatedLimite, drugConceptId1);
		
	
		
		List<Integer> adultRegimen2 = getPatientsUnderPmtcLastOneDrug(dateFormatedNew, dateFormatedLimite, drugConceptId2);
		
		List<Integer> adultRegimen3 = getPatientsUnderPmtcLastOneDrug(dateFormatedNew, dateFormatedLimite, drugConceptId3);
		
		List<Integer> adultRegimen4 = getPatientsUnderPmtcLastOneDrug(dateFormatedNew, dateFormatedLimite, drugConceptId4);
		
		if (drugConceptId1 != null && drugConceptId2 == null && drugConceptId3 == null && drugConceptId4 == null) {
			patientsUnderPmtctLast = adultRegimen1;
		} else if (drugConceptId1 != null && drugConceptId2 != null && drugConceptId3 != null && drugConceptId4 == null) {
			patientsUnderPmtctLast = (List<Integer>) treeCollectionsIntersection(adultRegimen1, adultRegimen2, adultRegimen3);
		}
      if(adultRegimen4.size() == 0 && adultRegimen3.size() == 0 && adultRegimen2.size() == 0 && adultRegimen1.size() != 0){
		    
    	  patientsUnderPmtctLast = adultRegimen1;
		}
		
		List<Integer> patientsPmtctLastToReturn = new ArrayList<Integer>();
		for (Integer currentPatientId : patientsUnderPmtctLast) {
			if (compareStartDatesByPatient(currentPatientId, drugConceptId1, drugConceptId2, drugConceptId3, drugConceptId4,
			    dateFormatedNew)) {
				patientsPmtctLastToReturn.add(currentPatientId);
			}
		}
		
		patientsPmtctLastToReturn.addAll(getPatientsUnderPmtctNew(dateFormatedNew,dateFormatedLimite,
			drugConceptId1, drugConceptId2, drugConceptId3,drugConceptId4).get(1));
		
		
		List<Integer> activePatiensUnderPmtctLast = getActivePatients(patientsPmtctLastToReturn, patientsExitedFromCareList,
		    dateFormatedLimite);	
		
		
		return activePatiensUnderPmtctLast;
		
	}
	
	public List<Integer> getPatientsUnderPmtcLastOneDrug(Date dateFormatedNew, Date dateFormatedLimite, Object drugConceptId1) {
		Session session = sessionFactory.getCurrentSession();
		SQLQuery kidsDOBQueryNew = session
		        .createSQLQuery("select distinct p.patient_id from patient p inner join orders o on o.patient_id=p.patient_id"
		                + " inner join drug_order dor on dor.order_id=o.order_id"
		                /*+ " inner join drug d on dor.drug_inventory_id=d.drug_id and d.drug_id = "
		                + drugConceptId1*/
		                +" inner join patient_program pp on p.patient_id= pp.patient_id "
	                    +" inner join program pro on pp.program_Id = pro.program_id and pro.concept_id=1647 "
		                + " inner join person on person_id=p.patient_id where o.concept_id ="+drugConceptId1+" and o.start_date < "
		                + "'"
		                + getDateFormatedFromDateObject(dateFormatedNew)
		                + "'"
		                + "and o.discontinued = 0 and o.auto_expire_date is null ;");
		
		return kidsDOBQueryNew.list();
	}
	public List<Integer> getPatientsUnderPmtcLastOneDrugSirop(Date dateFormatedNew, Date dateFormatedLimite, Object drugConceptId1) {
		Session session = sessionFactory.getCurrentSession();
		SQLQuery kidsDOBQueryNew = session
		        .createSQLQuery("select distinct p.patient_id from patient p inner join orders o on o.patient_id=p.patient_id"
		                + " inner join drug_order dor on dor.order_id=o.order_id"
		                + " inner join drug d on dor.drug_inventory_id=d.drug_id and d.name LIKE '%sirop%' and o.concept_id = "
		                + drugConceptId1+
		                " inner join patient_program pp on p.patient_id= pp.patient_id "+
	                    " inner join program pro on pp.program_Id = pro.program_id and pro.concept_id=1647 "
		                
		                + " inner join person on person_id=p.patient_id where o.start_date < "
		                + "'"
		                + getDateFormatedFromDateObject(dateFormatedNew)
		                + "'"
		                + "and o.discontinued = 0 and o.auto_expire_date is null ;");
		
		return kidsDOBQueryNew.list();
	}
	
	
	public List<Integer> getPediatricUnderSecondLineLastOneDrug(Date dateFormatedNew, Date dateFormatedLimite,
	                                                            Object drugConceptId1) {
		Session session = sessionFactory.getCurrentSession();
		SQLQuery kidsDOBQueryNew = session
		        .createSQLQuery("select distinct p.patient_id from patient p inner join orders o on o.patient_id=p.patient_id"
		                + " inner join drug_order dor on dor.order_id=o.order_id"
		                /*+ " inner join drug d on dor.drug_inventory_id=d.drug_id and d.drug_id = "
		                + drugConceptId1*/
		                + " inner join person on person_id=p.patient_id where o.concept_id="+drugConceptId1+" and o.start_date < "
		                + "'"
		                + getDateFormatedFromDateObject(dateFormatedNew)
		                + "'"
		                + "and o.discontinued = 0 and o.auto_expire_date is null and DATE_FORMAT(FROM_DAYS(TO_DAYS(NOW())-TO_DAYS(person.birthdate)), '%Y')+0 < 15 ;");
		
		return kidsDOBQueryNew.list();
	}
	
	public List<Integer> getAdultUnderSecondLineLastOneDrug(Date dateFormatedNew, Date dateFormatedLimite,
	                                                        Object drugConceptId1) {
		Session session = sessionFactory.getCurrentSession();
		SQLQuery kidsDOBQueryNew = session
		        .createSQLQuery("select distinct p.patient_id from patient p inner join orders o on o.patient_id=p.patient_id"
		                + " inner join drug_order dor on dor.order_id=o.order_id"
		                /*+ " inner join drug d on dor.drug_inventory_id=d.drug_id and d.drug_id = "
		                + drugConceptId1*/
		                + " inner join person on person_id=p.patient_id where o.concept_id="+drugConceptId1+" and o.start_date < "
		                + "'"
		                + getDateFormatedFromDateObject(dateFormatedNew)
		                + "'"
		                + "and o.discontinued = 0 and o.auto_expire_date is null and DATE_FORMAT(FROM_DAYS(TO_DAYS(NOW())-TO_DAYS(person.birthdate)), '%Y')+0 >= 15 ;");
		
		return kidsDOBQueryNew.list();
	}
	
	public Collection treeCollectionsIntersection(Collection coll1, Collection coll2, Collection coll3) {
		return CollectionUtils.intersection(coll1, CollectionUtils.intersection(coll2, coll3));
	}
	
	public Collection twoCollectionsIntersection(Collection coll1, Collection coll2) {
		return CollectionUtils.intersection(coll1, coll2);
	}
	
	public Collection faurCollectionsIntersection(Collection coll1, Collection coll2, Collection coll3, Collection coll4) {
		return CollectionUtils.intersection(CollectionUtils.intersection(coll1, coll2), CollectionUtils.intersection(coll3,
		    coll4));
	}
	
	@SuppressWarnings("unchecked")
	public Collection union(Collection coll1, Collection coll2) {
		List union = new ArrayList(coll1); 
		union.addAll(new ArrayList(coll2));
		return new ArrayList(union);
	}
	
	public String getRegimensAsString(Set<RegimenComponent> regimens) {
		StringBuffer sb = new StringBuffer();
		RegimenComponent[] components = regimens.toArray(new RegimenComponent[0]);
		
		for (int r = 0; r < components.length; r++) {
			RegimenComponent reg = components[r];
			RegimenComponent nextReg = (r < components.length - 1) ? components[r + 1] : null;
			
			if (nextReg == null || !reg.getStartDate().equals(nextReg.getStartDate()))
				sb.append(reg.toString() + "  ");
			else
				//sb.append(" this is a test " + "-");
				//sb.append(reg.getDrug().getName() + "-");
			   sb.append(reg.getGeneric().getName().getName()+ "-");
		}
		return sb.toString();
	}
	
	public List<Date> getStartDateByPatientAndDrug(Integer patientId, Object conceptId) {
		//log.info(" feeeeeeeeeeeeeeeeeeeeeee  eeee "+conceptId+" gggggggggggggggggggg    gf  dreeeeee "+patientId);
		Integer conceptInteger = (Integer) conceptId;
		Session session = sessionFactory.getCurrentSession();
		SQLQuery startDates = session
		        .createSQLQuery("select distinct o.start_date from patient p inner join orders o on o.patient_id=p.patient_id"
		               // + " inner join drug_order dor on dor.order_id=o.order_id"
		               // + " inner join drug d on dor.drug_inventory_id=d.drug_id and d.concept_id = "
		                + " where o.concept_id = "+conceptInteger
		                + " and p.patient_id =" + patientId + "");
		
		return startDates.list();
		
	}
	
	/*public boolean checkIfStartDatesAreLast(Integer patientId, Object ConceptId, Date dateFormatedNew) {
		
		Session session = sessionFactory.getCurrentSession();
		SQLQuery startDate = session
		        .createSQLQuery("select distinct o.start_date from patient p inner join orders o on o.patient_id=p.patient_id"
		                + " inner join drug_order dor on dor.order_id=o.order_id"
		                + " inner join drug d on dor.drug_inventory_id=d.drug_id and d.drug_id = "
		                + ConceptId
		                + " and p.patient_id ="
		                + patientId
		                + " where o.start_date < "
		                + getDateFormatedFromDateObject(dateFormatedNew) + "");
		
		return true;
		
	}*/
	
	
	public Boolean compareStartDatesByPatient(Integer patientId, Object conceptId1, Object conceptId2, Object conceptId3,
	                                          Object conceptId4, Date dateFormatedNew) {
		
		String dateFormatedNewString = df.format(dateFormatedNew);
		 
        try {
	        Date dateFormatedNewParsed=(Date) df.parse(dateFormatedNewString);
        
		if(patientId !=null ){
			
		
		List<Date> startDates1 = getStartDateByPatientAndDrug(patientId, conceptId1);
		
		List<Date> startDates2 = getStartDateByPatientAndDrug(patientId, conceptId2);
		
		List<Date> startDates3 = getStartDateByPatientAndDrug(patientId, conceptId3);
		
		List<Date> startDates4 = getStartDateByPatientAndDrug(patientId, conceptId4);
		
			
		if (conceptId1 != null && conceptId2 != null && conceptId3 != null && conceptId4 != null) {
			
			if ((startDates1.get(startDates1.size() - 1).getTime() < dateFormatedNewParsed.getTime())
			        || (startDates2.get(startDates2.size() - 1).getTime() < dateFormatedNewParsed.getTime())
			        || (startDates3.get(startDates3.size() - 1).getTime() < dateFormatedNewParsed.getTime())
			        || (startDates4.get(startDates4.size() - 1).getTime() < dateFormatedNewParsed.getTime())) {
				
				
				return true;
			}
		} else if (conceptId1 != null && conceptId2 != null && conceptId3 != null && conceptId4 == null) {
			
			if ((startDates1.get(startDates1.size() - 1).getTime() < dateFormatedNewParsed.getTime())
			        || (startDates2.get(startDates2.size() - 1).getTime() < dateFormatedNewParsed.getTime())
			        || (startDates3.get(startDates3.size() - 1).getTime() < dateFormatedNewParsed.getTime())) {	
				return true;
			}
			
		} else if (conceptId1 != null && conceptId2 == null && conceptId3 == null && conceptId4 == null) {
			
			if ((startDates1.get(startDates1.size() - 1).getTime() < dateFormatedNewParsed.getTime())) {
				return true;
			}
			
		   }
		  }
        }
        catch (ParseException e) {
	        // TODO Auto-generated catch block
	        log.error("Error generated", e);
        }
		return false;
	}
	
	public Boolean isAnyArvDrugOld(Integer patientId, Date dateFormatedNew) throws ParseException {
		
		List<List<Date>> startdates = new ArrayList<List<Date>>();
		
		List<Date> startDates1 = getStartDateByPatientAndDrug(patientId, 796);
		
		List<Date> startDates2 = getStartDateByPatientAndDrug(patientId, 797);
		
		List<Date> startDates3 = getStartDateByPatientAndDrug(patientId, 633);
		
		List<Date> startDates4 = getStartDateByPatientAndDrug(patientId, 628);
		
		List<Date> startDates5 = getStartDateByPatientAndDrug(patientId, 794);
		
		List<Date> startDates6 = getStartDateByPatientAndDrug(patientId, 635);
		
		List<Date> startDates7 = getStartDateByPatientAndDrug(patientId, 631);
		
		List<Date> startDates8 = getStartDateByPatientAndDrug(patientId, 625);
		
		List<Date> startDates9 = getStartDateByPatientAndDrug(patientId, 802);
		
		List<Date> startDates10 = getStartDateByPatientAndDrug(patientId, 2203);
		
		List<Date> startDates11 = getStartDateByPatientAndDrug(patientId, 1613);
		
		List<Date> startDates12 = getStartDateByPatientAndDrug(patientId, 794);
		
		List<Date> startDates13 = getStartDateByPatientAndDrug(patientId, 749);
		
		List<Date> startDates14 = getStartDateByPatientAndDrug(patientId, 795);
		
		startdates.add(startDates1);
		startdates.add(startDates2);
		startdates.add(startDates3);
		startdates.add(startDates4);
		startdates.add(startDates5);
		startdates.add(startDates6);
		startdates.add(startDates7);
		startdates.add(startDates8);
		startdates.add(startDates9);
		startdates.add(startDates10);
		startdates.add(startDates11);
		startdates.add(startDates12);
		startdates.add(startDates13);
		startdates.add(startDates14);
		
		    String dateFormatedNewString = df.format(dateFormatedNew);
		 
	        Date dateFormatedNewParsed=(Date) df.parse(dateFormatedNewString);
       
		
		
		for (List<Date> startDate : startdates) {
			
			if (startDate != null && !startDate.isEmpty()) {
				
				if (startDate.get(0).getTime() < dateFormatedNewParsed.getTime()) {
					
					return true;
				}
			}
		}
		return false;
	}
	
	public List<Integer> removeLostFollowUp(List<Integer> patientIds, Date startDate) {
		
			
		
		List<Integer> patientsNotLostFollowUp = new ArrayList<Integer>();
		
		Session session = getSessionFactory().getCurrentSession();
		
		Date threeMonthsBeforeEndDate = getTreeMonthBefore(df.format(startDate));
		
		
		

		@SuppressWarnings("unused")
		List<Date> maxDate = new ArrayList<Date>();
		
		
		Date returnVisitDayPlusThreeMonths = null;
		
		for (Integer patientId : patientIds) {
			
			try {
				
				SQLQuery queryDate1 = session
				        .createSQLQuery("select cast(max(encounter_datetime)as DATE) from encounter where "
						                + "(select(cast(max(encounter_datetime)as Date))) <= '" + df.format(startDate)
						                + "' and patient_id = " + patientId);
				
				List<Date> maxEncunterDateTime = queryDate1.list();
		  
		
				SQLQuery queryDate2 = session.createSQLQuery("select cast(max(value_datetime) as DATE ) "
						        + "from obs where (select(cast(max(value_datetime)as Date))) <= '" + df.format(startDate)
						        + "' and concept_id = 5096"
						        + " and person_id = " + patientId);
				
				List<Date> maxReturnVisitDay = queryDate2.list();
				
                  if ((maxReturnVisitDay.get(0)) != null && maxEncunterDateTime.get(0) != null) {
					
	                
					if (((maxEncunterDateTime.get(0).getTime()) >= threeMonthsBeforeEndDate.getTime() && (maxEncunterDateTime
					        .get(0).getTime()) <= startDate.getTime())
					        || ((maxReturnVisitDay.get(0).getTime()) >= threeMonthsBeforeEndDate.getTime() && (maxReturnVisitDay
					                .get(0).getTime()) <= startDate.getTime())) {
						
						patientsNotLostFollowUp.add(patientId);
						
				  }
				}
				else if ((maxReturnVisitDay.get(0)) == null && maxEncunterDateTime.get(0) != null) {
					
					if ((maxEncunterDateTime.get(0).getTime()) >= threeMonthsBeforeEndDate.getTime()
					        && (maxEncunterDateTime.get(0).getTime()) <= startDate.getTime()) {
						
						patientsNotLostFollowUp.add(patientId);
						
					}
				} else if (maxReturnVisitDay.get(0) != null && (maxReturnVisitDay.get(0).getTime() > startDate.getTime()))

				{
					
					patientsNotLostFollowUp.add(patientId);
					
				}
			}


			catch (Exception e) {
				// TODO: handle exception
			}
		}
		return patientsNotLostFollowUp;
		
	}
	
	/**
	 * Utility method to add days to an existing date
	 * 
	 * @param date (may be null to use today's date)
	 * @param days the number of days to add (negative to subtract days)
	 * @return the new date
	 */
	public static Date addDaysToDate(Date date, int days) {
		// Initialize with date if it was specified
		Calendar cal = new GregorianCalendar();
		if (date != null)
			cal.setTime(date);
		
		cal.add(Calendar.DAY_OF_WEEK, days);
		return cal.getTime();
	}
	
	public List<String> getLocations() {
		
		Session session = getSessionFactory().getCurrentSession();
		SQLQuery query = session.createSQLQuery("SELECT name FROM location ;");
		
		return query.list();
	}
	
	public List<Date> getLastVisiteDate(Patient patient) {
		Session session = getSessionFactory().getCurrentSession();
		SQLQuery queryDate2 = session.createSQLQuery("select cast(max(value_datetime) as DATE ) "
		        + "from obs where concept_id = 5096 and person_id = " + patient.getPatientId());
		
		List<Date> maxReturnVisitDay = queryDate2.list();
		return maxReturnVisitDay;
	}
	
	public List<Date> getLastEncouterDate(Patient patient) {
		
		Session session = getSessionFactory().getCurrentSession();
		SQLQuery queryDate1 = session
		        .createSQLQuery("select cast(max(encounter_datetime)as DATE) from encounter where patient_id = "
		                + patient.getPatientId());
		
		List<Date> maxEncunterDateTime = queryDate1.list();
		
		return maxEncunterDateTime;
		
	}
	
public List<Integer> getPatientOnOnlyGivenDrugs(List<Integer> listDrugIds, List<Integer> patientIds) {
		
	
		List<Integer> patientOnDrugs = new ArrayList<Integer>();
		
		int regimenSize = 1;
		
		for (Integer patientId : patientIds) {
			
			Patient p = Context.getPatientService().getPatient(patientId);
			List<Regimen> patientRegimens = new ArrayList<Regimen>();
			
			patientRegimens = RegimenUtils.getRegimenHistory(p).getRegimenList();
			
			Set<RegimenComponent> regimenComponents = new HashSet<RegimenComponent>();
			
			regimenSize = patientRegimens.size() - 1;
			
			Set<RegimenComponent> componentsStopped = new HashSet<RegimenComponent>();
			
			regimenComponents = patientRegimens.get(regimenSize).getComponents();
			
			Date today = new Date();
			
			if (patientRegimens.get(regimenSize).getEndDate() == null) {
				
				patientRegimens.get(regimenSize).setEndDate(today);
			}
			for (RegimenComponent rc : regimenComponents) {
				if (rc.getStopDate() != null)
					if (rc.getStopDate().getTime() <= patientRegimens.get(regimenSize).getStartDate().getTime()) {
						componentsStopped.add(rc);
						
					}
			}
			
			if (componentsStopped != null)
				regimenComponents.removeAll(componentsStopped);
			
			List<Integer> regimenDrugs = new ArrayList<Integer>();
			
			for (RegimenComponent rc : regimenComponents) {
				
				regimenDrugs.add(rc.getDrug().getDrugId());
			}
				 
			if (getPatientsOnAll(listDrugIds, regimenDrugs)) {
				patientOnDrugs.add(p.getPatientId());	
			} 			
		}
		return patientOnDrugs;
		
	}
   public boolean getPatientsOnAll(List<Integer> drugSelected, List<Integer> regimenDrugs) {
	boolean found = false;
	
	
	if (regimenDrugs.size() >= drugSelected.size() && regimenDrugs.containsAll(drugSelected)) {
		for (Integer r : regimenDrugs) {
			if (drugSelected.contains(r) ) {
				found = true;
			} else {
				found = false;
				break;
			}
		}
	}
	
	return found;
 }
   
   public List<Integer> getDrugIdByConceptId(Integer conceptId) {
		Session session = sessionFactory.getCurrentSession();
		SQLQuery drugIdByConceptId = session
		        .createSQLQuery("SELECT drug_id FROM drug d where concept_id = "+conceptId);
		return drugIdByConceptId.list();
	}   
   
   public List<Integer>getTheCollectionWithoutNull(Object drugConceptId1,Object drugConceptId2,Object drugConceptId3,Object drugConceptId4){
	      
	   List<Integer> collection= new ArrayList<Integer>();
	   if(!(drugConceptId1==null))
	   collection.add((Integer) drugConceptId1);
	   if(!(drugConceptId2==null))
	   collection.add((Integer) drugConceptId2);
	   if(!(drugConceptId3==null))
	   collection.add((Integer) drugConceptId3);
	   if(!(drugConceptId4==null) && drugConceptId4.equals(0))
	   collection.add((Integer) drugConceptId4);
	   
	   List<Integer> listWithoutNulls= new ArrayList<Integer>(); 
	   
	   for (Integer oneElement: collection){
		   if(!(oneElement == null)){
			   listWithoutNulls.add(oneElement);
		   }
	   }
	   
	   return listWithoutNulls;
	   
   }
   
     public List<List<Object>> getRegimesByCategory(String regimenCategory) {
    	 
    	 List<List<Object>> adultRegimenCompositions = new ArrayList<List<Object>>(); 
    	 
    	Session session = sessionFactory.getCurrentSession();
    	
  		SQLQuery regimenComposition0 = session
  		        .createSQLQuery("select regimen_name from regimencomposition where regimen_category = '"+regimenCategory+"'");
    	
 		SQLQuery regimenComposition1 = session
 		        .createSQLQuery("select drug_concept_id1 from regimencomposition where regimen_category = '"+regimenCategory+"'");
 		
 		
 		SQLQuery regimenComposition2 = session
	        .createSQLQuery("select drug_concept_id2 from regimencomposition where regimen_category = '"+regimenCategory+"'");
	    
	
	    SQLQuery regimenComposition3 = session
        .createSQLQuery("select drug_concept_id3 from regimencomposition where regimen_category = '"+regimenCategory+"'");
       

       SQLQuery regimenComposition4 = session
       .createSQLQuery("select drug_concept_id4 from regimencomposition where regimen_category = '"+regimenCategory+"'");
        
 		if(regimenComposition1.list().size()!=0){
        for(int i = 0;i<regimenComposition1.list().size();i++){

        List<Object> onComposition=new ArrayList<Object>();
        
        onComposition.add((String)regimenComposition0.list().get(i));		
        onComposition.add((Integer)regimenComposition1.list().get(i));
        onComposition.add((Integer)regimenComposition2.list().get(i));
        onComposition.add((Integer)regimenComposition3.list().get(i));
        onComposition.add((Integer)regimenComposition4.list().get(i));
        adultRegimenCompositions.add(onComposition);
        
 		   }
 		}
 		
       return adultRegimenCompositions; 		
 		
 	} 
     public List<Integer> getRegimenCompositionByName(String regimenName) {
 		Session session = sessionFactory.getCurrentSession();
 		SQLQuery regimenCompostion1 = session
 		        .createSQLQuery("select drug_concept_id1 from regimencomposition where regimen_name='"+regimenName+"'");
 		SQLQuery regimenCompostion2 = session
	        .createSQLQuery("select drug_concept_id2 from regimencomposition where regimen_name='"+regimenName+"'");
 		SQLQuery regimenCompostion3 = session
	        .createSQLQuery("select drug_concept_id3 from regimencomposition where regimen_name='"+regimenName+"'");
 		SQLQuery regimenCompostion4 = session
	        .createSQLQuery("select drug_concept_id4 from regimencomposition where regimen_name='"+regimenName+"'");
 		
 		List<Integer> regimenCompostions = new ArrayList<Integer>();
 		if(regimenCompostion1.list().size()!=0){
 		regimenCompostions.add((Integer)regimenCompostion1.list().get(0));
 		regimenCompostions.add((Integer)regimenCompostion2.list().get(0));
 		regimenCompostions.add((Integer)regimenCompostion3.list().get(0));
 		regimenCompostions.add((Integer)regimenCompostion4.list().get(0));
 		}
 		
 		return regimenCompostions;
 	}
     public List<Integer> getOldPatientPerDate(Date dateFormatedNew) {
    	 	 
    	 Session session = sessionFactory.getCurrentSession();
  		 SQLQuery patientsList = session
  		        .createSQLQuery("select distinct p.patient_id from patient p inner join orders o on o.patient_id=p.patient_id"
		                 +" inner join drug_order dor on dor.order_id=o.order_id"
		                 +" inner join drug d on dor.drug_inventory_id=d.drug_id and d.concept_id in(796,797,633,628,794,635,631,625,802,2203,1613,794,749,795,814,5424,792,5811,630,2833)  and o.start_date < '"+getDateFormatedFromDateObject(dateFormatedNew)+"' and o.voided = 0");       	 
  		
  				
  		return patientsList.list();
  	} 
  
   public List<String> getRegimenCategories() {
		
		Session session = getSessionFactory().getCurrentSession();
		SQLQuery query = session.createSQLQuery("select regimen_category_name from regimencategory;");
		
		return query.list();
	}
   public List<String> getRegimenNames() {
		
		Session session = getSessionFactory().getCurrentSession();
		SQLQuery query = session.createSQLQuery("select regimen_name from regimencomposition;");
		
		return query.list();
	}
   public Boolean deleteRegimen(String regimenCategory,String regimenName) {
		
	   Boolean queryHasExecuted=true;
	   try {
		   
		   Session session = getSessionFactory().getCurrentSession();
		   SQLQuery query = session.createSQLQuery("delete from regimencomposition where regimen_name='"+regimenName+"' and regimen_category='"+regimenCategory+"';");
		   query.executeUpdate();		
    }
    catch (Exception e) {
	    queryHasExecuted= false;
    }		
		return queryHasExecuted;
	}
  
   public List<String> getArvDrugs() {
		
		Session session = getSessionFactory().getCurrentSession();
		CamerwaGlobalProperties gp = new CamerwaGlobalProperties();
		String arvConceptIds = gp.getArvConceptIdList();
		SQLQuery query = session.createSQLQuery("select name from drug where concept_id in("+arvConceptIds+");");
	
		return query.list();
	}
   public Integer getConceptIdByDrugName(String DrugName) {
		
		Session session = getSessionFactory().getCurrentSession();
		SQLQuery query = session.createSQLQuery("select concept_id from drug where name='"+DrugName+"';");
			
		if(query.list().size()>0){
		
		return (Integer) query.list().get(0);
		}
		return null;
	}
   public Boolean insertRegimen(String regimen_category,String regimen_name,Object drug_concept_id1,Object drug_concept_id2,Object drug_concept_id3,Object drug_concept_id4) {
	   Boolean queryHasExecuted=true;
	   try {
		   
		   
		    Session session = getSessionFactory().getCurrentSession();
		  	RegimenComposition regimenComposition = new RegimenComposition();
		  	if(drug_concept_id1!=null){
		  	regimenComposition.setDrugConceptId1((Integer) drug_concept_id1);
		  	}
		  	if(drug_concept_id2!=null){
		  	regimenComposition.setDrugConceptId2((Integer) drug_concept_id2);
		  	}
		  	if(drug_concept_id3!=null){
		  	regimenComposition.setDrugConceptId3((Integer) drug_concept_id3);
		  	}
		  	if(drug_concept_id4!=null){
		  		regimenComposition.setDrugConceptId4((Integer) drug_concept_id4);	
		  	}
		  	
		   
		   SQLQuery query = session.createSQLQuery("INSERT INTO regimencomposition (regimen_category, regimen_name,drug_concept_id1,drug_concept_id2,drug_concept_id3,drug_concept_id4) VALUES ('"+regimen_category+"','"+regimen_name+"',"+drug_concept_id1+","+drug_concept_id2+","+drug_concept_id3+","+drug_concept_id4+")");
			query.executeUpdate();
			
    }
    catch (Exception e) {
    	
	    queryHasExecuted= false;
    }
		
		
		return queryHasExecuted;
	}
   public Date getTreeMonthBefore(String date){
	   Session session = getSessionFactory().getCurrentSession();
	   SQLQuery query = session.createSQLQuery("SELECT DATE_SUB(CAST('"+date+"' AS DATE), INTERVAL 3 MONTH);");
	   
	   
	return (Date) query.uniqueResult();
	   
   }
   public Date getWhenPatientStarted(Patient patient) {
		SQLQuery query = null;
		CamerwaGlobalProperties gp = new CamerwaGlobalProperties();
		Session session = sessionFactory.getCurrentSession();
		
		StringBuffer strbuf = new StringBuffer();
		
		strbuf.append("SELECT cast(min(o.start_date) as DATE ) FROM orders o  ");
		strbuf.append("INNER JOIN drug_order dro on dro.order_id = o.order_id  ");
		strbuf.append("inner join drug dr on dr.drug_id = dro.drug_inventory_id ");
		strbuf.append("where dr.concept_id in (");
		strbuf.append(gp.getArvConceptIdList());
		strbuf.append(" ) AND o.patient_id = ");
		strbuf.append(patient.getPatientId());
		
		query = session.createSQLQuery(strbuf.toString());
		
		List<Date> dates = query.list();
		Date whenPatientStarted = dates.get(0);
		
		return whenPatientStarted;
	}
   /*public String getQueryPortionForProphylaxis() {
	   
	    CamerwaGlobalProperties gp = new CamerwaGlobalProperties();
		String arvConceptIds = gp.getArvConceptIdList();
	    String[] arvConceptIdList =arvConceptIds.split(",");
	    
	    String[] arvConceptIdList =   gp.getProphilaxisConceptIdList().split(",");
	                
	//	List<String> gpProphylaxisDrugIds = gp.getProphilaxisConceptIdList().split(",");
		String quaryPortion = "";
		for (String id : arvConceptIdList) {
			quaryPortion = quaryPortion + " AND dro.drug_inventory_id <> " + id;
		}
		
		return quaryPortion;
	}
*/
} 

