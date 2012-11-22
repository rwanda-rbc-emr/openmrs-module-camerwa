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

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;
import org.openmrs.Patient;
import org.openmrs.api.PatientService;
import org.openmrs.api.context.Context;
import org.openmrs.module.camerwa.service.CamerwaService;


/**
 *
 */
public class PatientListControl {
	protected final Log log = LogFactory.getLog(getClass());
	private SessionFactory sessionFactory;
	CamerwaService camerwaService = Context.getService(CamerwaService.class);
	
	
	
	@SuppressWarnings("unchecked")
    public List<Patient> getPatientsByKey(String KeyPatients,String regimenName,Date dateFormatedNew, Date dateFormatedLimite) throws ParseException{
		// getting patients who have been exited from care
		
		List<Integer> patientsExitedFromCareList = (List<Integer>) camerwaService.getPatientsExitedFromCare(dateFormatedLimite);
		List<Integer> patientsIdsActive = new ArrayList<Integer>();
		
		  		
		List<Integer>  regimens =	camerwaService.getRegimenCompositionByName(regimenName);
		//log.info(" b%%%%%%%%%%%%%% regimenName %%%%"+regimenName+"))))))))))))))))))))"+regimens);
		if(KeyPatients.equals("adultRegimenLast")){
	     //log.info("!!!!!!!!!!!!!!!!!!"+regimens.get(0)+"!!!!!!!"+regimens.get(1)+""+regimens.get(2),regimens.get(3));
			List<Integer> patientsIds = (List<Integer>) camerwaService.getRegimenAdultLast(dateFormatedNew,dateFormatedLimite,regimens.get(0),regimens.get(1),regimens.get(2),regimens.get(3));
		    patientsIdsActive = camerwaService.getActivePatients(patientsIds, patientsExitedFromCareList,dateFormatedLimite);	
		}else if(KeyPatients.equals("adultRegimenNew")){
			List<Integer> patientsIds = (List<Integer>) camerwaService.getRegimenAdultNew(dateFormatedNew,dateFormatedLimite,regimens.get(0), regimens.get(1),regimens.get(2),regimens.get(3)).get(0);
		    patientsIdsActive = camerwaService.getActivePatients(patientsIds, patientsExitedFromCareList,dateFormatedLimite);	
		}else if(KeyPatients.equals("TotalAdulRegimen")){
			List<Integer> patientsIdsAdultRegimen = (List<Integer>) camerwaService.getRegimenAdultNew(dateFormatedNew,dateFormatedLimite,regimens.get(0), regimens.get(1),regimens.get(2),regimens.get(3)).get(0);
			List<Integer> patientsIdsAllAdultRegimen = (List<Integer>) camerwaService.getRegimenAdultLast(dateFormatedNew,dateFormatedLimite,regimens.get(0), regimens.get(1),regimens.get(2),regimens.get(3));
			List<Integer> totalAdultRegimen = (List<Integer>) camerwaService.union(patientsIdsAdultRegimen, patientsIdsAllAdultRegimen);
		    patientsIdsActive = camerwaService.getActivePatients(totalAdultRegimen, patientsExitedFromCareList,dateFormatedLimite);	
		} else if(KeyPatients.equals("pediatricRegimenLast")){
			List<Integer> patientsIds = (List<Integer>) camerwaService.getPediatricUnderRegimenLast(dateFormatedNew,dateFormatedLimite, regimens.get(0),regimens.get(1),regimens.get(2),regimens.get(3) );
		    patientsIdsActive = camerwaService.getActivePatients(patientsIds, patientsExitedFromCareList,dateFormatedLimite);	
		}else if(KeyPatients.equals("pediatricRegimenNew")){
			List<Integer> patientsIds = (List<Integer>) camerwaService.getPediatricUnderRegimenNew(dateFormatedNew,dateFormatedLimite,regimens.get(0),regimens.get(1),regimens.get(2),regimens.get(3) ).get(0);
		    patientsIdsActive = camerwaService.getActivePatients(patientsIds, patientsExitedFromCareList,dateFormatedLimite);	
		}else if(KeyPatients.equals("TotalPediatricRegimen")){
			List<Integer> patientsIdsPediatricRegimen = (List<Integer>) camerwaService.getPediatricUnderRegimenNew(dateFormatedNew,dateFormatedLimite,regimens.get(0),regimens.get(1),regimens.get(2),regimens.get(3)).get(0);
			List<Integer> patientsIdsAllPediatricRegimen = (List<Integer>) camerwaService.getPediatricUnderRegimenLast(dateFormatedNew,dateFormatedLimite, regimens.get(0),regimens.get(1),regimens.get(2),regimens.get(3));
			List<Integer> totalPediatricRegimen = (List<Integer>) camerwaService.union(patientsIdsPediatricRegimen, patientsIdsAllPediatricRegimen);
		    patientsIdsActive = camerwaService.getActivePatients(totalPediatricRegimen, patientsExitedFromCareList,dateFormatedLimite);	
		  
		}else if(KeyPatients.equals("pediatricSecondLineLast")){
				
				List<Integer> allpatientsIds = (List<Integer>) camerwaService.getPediatricUnderSecondLineRegimenLast(dateFormatedNew,dateFormatedLimite,regimens.get(0),regimens.get(1),regimens.get(2),regimens.get(3));
				patientsIdsActive = camerwaService.getActivePatients(allpatientsIds, patientsExitedFromCareList,dateFormatedLimite);
			}else if(KeyPatients.equals("pediatricSecondLineNew")){
				List<Integer> allpatientsIds= (List<Integer>) camerwaService.getPediatricUnderSecondLineRegimenNew(dateFormatedNew,dateFormatedLimite,regimens.get(0),regimens.get(1),regimens.get(2),regimens.get(3)).get(0);
				patientsIdsActive = camerwaService.getActivePatients(allpatientsIds, patientsExitedFromCareList,dateFormatedLimite);
			}else if(KeyPatients.equals("TotalPediatricSecondLine")){
				
				List<Integer> patientUnderSecondLineLast = (List<Integer>) camerwaService.getPediatricUnderSecondLineRegimenLast(dateFormatedNew,dateFormatedLimite,regimens.get(0),regimens.get(1),regimens.get(2),regimens.get(3));
				
				List<Integer> patientUnderSecondLineNew = (List<Integer>) camerwaService.getPediatricUnderSecondLineRegimenNew(dateFormatedNew,dateFormatedLimite,regimens.get(0),regimens.get(1),regimens.get(2),regimens.get(3)).get(0);
				
				 List<Integer> allpatientsIds = (List<Integer>) camerwaService.union(patientUnderSecondLineLast, patientUnderSecondLineNew);
				 patientsIdsActive = camerwaService.getActivePatients(allpatientsIds, patientsExitedFromCareList,dateFormatedLimite);
			}else if(KeyPatients.equals("adultSecondLineLast")){
				
				List<Integer> allpatientsIds = (List<Integer>) camerwaService.getAdultUnderSecondLineRegimenLast(dateFormatedNew,dateFormatedLimite,regimens.get(0),regimens.get(1),regimens.get(2),regimens.get(3));
				patientsIdsActive = camerwaService.getActivePatients(allpatientsIds, patientsExitedFromCareList,dateFormatedLimite);
			}else if(KeyPatients.equals("adultSecondLineNew")){
				List<Integer> allpatientsIds = (List<Integer>) camerwaService.getAdultUnderSecondLineRegimenNew(dateFormatedNew,dateFormatedLimite,regimens.get(0),regimens.get(1),regimens.get(2),regimens.get(3)).get(0);
				patientsIdsActive = camerwaService.getActivePatients(allpatientsIds, patientsExitedFromCareList,dateFormatedLimite);
			}else if(KeyPatients.equals("TotalAdultSecondLine")){
				
				List<Integer> patientUnderSecondLineLast = (List<Integer>) camerwaService.getAdultUnderSecondLineRegimenLast(dateFormatedNew,dateFormatedLimite,regimens.get(0),regimens.get(1),regimens.get(2),regimens.get(3));
				
				List<Integer> patientUnderSecondLineNew = (List<Integer>) camerwaService.getAdultUnderSecondLineRegimenNew(dateFormatedNew,dateFormatedLimite,regimens.get(0),regimens.get(1),regimens.get(2),regimens.get(3)).get(0);
				
				List<Integer> allpatientsIds = (List<Integer>) camerwaService.union(patientUnderSecondLineLast, patientUnderSecondLineNew);
				patientsIdsActive = camerwaService.getActivePatients(allpatientsIds, patientsExitedFromCareList,dateFormatedLimite);
				
			}else if(KeyPatients.equals("prophylaxiePostExpositionNew")){
				
				List<Integer> allpatientsIds = (List<Integer>) camerwaService.getPatientsUnderProphylaxieNew(dateFormatedNew,dateFormatedLimite,regimens.get(0),regimens.get(1),regimens.get(2),regimens.get(3)).get(0);
				patientsIdsActive = camerwaService.getActivePatients(allpatientsIds, patientsExitedFromCareList,dateFormatedLimite);
			  
			  
			}else if(KeyPatients.equals("prophylaxiePostExpositionLast")){
				
				List<Integer> allpatientsIds = (List<Integer>) camerwaService.getPatientsUnderProphylaxieLast(dateFormatedNew,dateFormatedLimite,regimens.get(0),regimens.get(1),regimens.get(2),regimens.get(3));
				patientsIdsActive = camerwaService.getActivePatients(allpatientsIds, patientsExitedFromCareList,dateFormatedLimite);
			  
			  
			}else if(KeyPatients.equals("TotalProphylaxiePostExposition")){
				
				List<Integer> patientsIdLast = (List<Integer>) camerwaService.getPatientsUnderProphylaxieLast(dateFormatedNew,dateFormatedLimite,regimens.get(0),regimens.get(1),regimens.get(2),regimens.get(3));
				
				List<Integer> patientsIdNew = (List<Integer>) camerwaService.getPatientsUnderProphylaxieNew(dateFormatedNew,dateFormatedLimite,regimens.get(0),regimens.get(1),regimens.get(2),regimens.get(3)).get(0);
				
				List<Integer> allpatientsIds = (List<Integer>) camerwaService.union(patientsIdLast, patientsIdNew);
				patientsIdsActive = camerwaService.getActivePatients(allpatientsIds, patientsExitedFromCareList,dateFormatedLimite);  
			}else if(KeyPatients.equals("pmtctNew")){
				
				List<Integer> allpatientsIds = (List<Integer>) camerwaService.getPatientsUnderPmtctNew(dateFormatedNew,dateFormatedLimite, regimens.get(0),regimens.get(1),regimens.get(2),regimens.get(3)).get(0);
				patientsIdsActive = camerwaService.getActivePatients(allpatientsIds, patientsExitedFromCareList,dateFormatedLimite);
			  
			}else if(KeyPatients.equals("pmtctLast")){
				List<Integer> allpatientsIds = (List<Integer>) camerwaService.getPatientsUnderPmtctLast(dateFormatedNew,dateFormatedLimite,regimens.get(0),regimens.get(1),regimens.get(2),regimens.get(3));
				patientsIdsActive = camerwaService.getActivePatients(allpatientsIds, patientsExitedFromCareList,dateFormatedLimite);
			  
			}else if(KeyPatients.equals("TotalPmtct")){
				
				List<Integer> patientsIdLast = (List<Integer>) camerwaService.getPatientsUnderPmtctLast(dateFormatedNew,dateFormatedLimite,regimens.get(0),regimens.get(1),regimens.get(2),regimens.get(3));
				List<Integer> patientsIdNew = (List<Integer>) camerwaService.getPatientsUnderPmtctNew(dateFormatedNew,dateFormatedLimite, regimens.get(0),regimens.get(1),regimens.get(2),regimens.get(3)).get(0);
				List<Integer> allpatientsIds = (List<Integer>) camerwaService.union(patientsIdLast, patientsIdNew);
				patientsIdsActive = camerwaService.getActivePatients(allpatientsIds, patientsExitedFromCareList,dateFormatedLimite);
			}else if(KeyPatients.equals("PediatricSiropNew")){
				
				List<Integer> allpatientsIds = (List<Integer>) camerwaService.getPatientsUnderPediatricSiropNew(dateFormatedNew,
				    dateFormatedLimite, regimens.get(0),regimens.get(1),regimens.get(2),regimens.get(3)).get(0);
				patientsIdsActive = camerwaService.getActivePatients(allpatientsIds, patientsExitedFromCareList,dateFormatedLimite);
			  
			}else if(KeyPatients.equals("PediatricSiropLast")){
			
				
			//log.info("reg     im getting here   imens @@@@@@@"+regimens.get(0)+" QQQQQQQQQQQQQq ############"+regimens.get(1)+"!!!!!!!!!"+regimens.get(2)+"!!!!!!!!"+regimens.get(3));
				List<Integer> allpatientsIds = (List<Integer>) camerwaService.getPatientsUnderPediatricSiropLast(dateFormatedNew,
				    dateFormatedLimite,regimens.get(0),regimens.get(1),regimens.get(2),regimens.get(3));
			log.info(" im in here allpatientsIds "+regimens.get(0)+" QQQQQQQQQQQQQq ############"+regimens.get(1)+"!!!!!!!!!"+regimens.get(2)+"!!!!!!!!"+regimens.get(3)+"|||||||||allpatientsIds|||||"+allpatientsIds);	
				patientsIdsActive = camerwaService.getActivePatients(allpatientsIds, patientsExitedFromCareList,dateFormatedLimite);
			 
			log.info(" im in here patientsIdsActive "+regimens.get(0)+" QQQQQQQQQQQQQq ############"+regimens.get(1)+"!!!!!!!!!"+regimens.get(2)+"!!!!!!!!"+regimens.get(3)+"|||||||||||||||||||||"+patientsIdsActive);		
				
				//log.info("QQQQQQQQQQQQQQQQQQQQQQQQQ allpatientsIds"+allpatientsIds.size()+" WWWWWWWWWWWWWWWWWW patientsIdsActive"+patientsIdsActive);
			}else if(KeyPatients.equals("TotalPediatricSirop")){
				
				List<Integer> patientsIdLast = (List<Integer>) camerwaService.getPatientsUnderPediatricSiropLast(dateFormatedNew,
				    dateFormatedLimite,regimens.get(0),regimens.get(1),regimens.get(2),regimens.get(3));
				List<Integer> patientsIdNew = (List<Integer>)  camerwaService.getPatientsUnderPediatricSiropNew(dateFormatedNew,
				    dateFormatedLimite, regimens.get(0),regimens.get(1),regimens.get(2),regimens.get(3)).get(0);
				List<Integer> allpatientsIds = (List<Integer>) camerwaService.union(patientsIdLast, patientsIdNew);
				patientsIdsActive = camerwaService.getActivePatients(allpatientsIds, patientsExitedFromCareList,dateFormatedLimite);
			}else if(KeyPatients.equals("patientsWithoutRegimens")){
				HashMap<String, Object> patientsUnderDrug = new HashMap<String, Object>();
				CamerwaService camerwaService = Context.getService(CamerwaService.class);
				
				PatientService camerwaService2 = Context.getService(PatientService.class);
				
				List<Patient>  allPatients = camerwaService2.getAllPatients();
				
				patientsUnderDrug=camerwaService.getPatientsUnderDrug(dateFormatedNew,dateFormatedLimite);
				
				List<Integer> patientsWithoutRegimens = new ArrayList<Integer>();
				
				List<Integer> patientsWithRegimens = (List<Integer>) patientsUnderDrug.get("patientsOnRegimens") ;
				//log.info(" !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1  patientsWithRegimens "+patientsWithRegimens.size());
					for(Patient patient : allPatients){
					if(!patientsWithRegimens.contains(patient.getPatientId()) && !patientsWithoutRegimens.contains(patient.getPatientId())){
						patientsWithoutRegimens.add(patient.getPatientId());
					}
				}
				
					patientsIdsActive =camerwaService.removeLostFollowUp((List<Integer>)patientsWithoutRegimens,dateFormatedLimite);
			}
			else if(KeyPatients.equals("patientsUnderArvNew")){	
			
			List<Integer> patientsIds = (List<Integer>) camerwaService.getPatientsUnderArv(dateFormatedNew,dateFormatedLimite);	
			List<Integer> activePatients = camerwaService.getActivePatients(patientsIds, patientsExitedFromCareList,dateFormatedLimite);

			activePatients.removeAll(getPatientArleadyUnderArv(activePatients, dateFormatedNew));
			
			List<Integer> patientsCurrentUnderArv = camerwaService.getOldPatientPerDate(dateFormatedNew);
			List<Integer> patientsSwimsNewButOld = (List<Integer>) camerwaService.twoCollectionsIntersection(patientsCurrentUnderArv, activePatients) ;
			activePatients.removeAll(patientsSwimsNewButOld);
			
			
			patientsIdsActive = camerwaService.removeLostFollowUp(activePatients,dateFormatedLimite);//activePatients;
			
			
		}else if(KeyPatients.equals("LastPatientsUnderARV")){
			
			List<Integer> patientsIds = (List<Integer>) camerwaService.getAllPatientsUnderARV(dateFormatedNew,dateFormatedLimite);
			
		    List<Integer> patientsIdsNew = (List<Integer>) camerwaService.getPatientsUnderArv(dateFormatedNew,dateFormatedLimite);	
			List<Integer> activePatientsNew = camerwaService.getActivePatients(patientsIdsNew, patientsExitedFromCareList,dateFormatedLimite);
			patientsIds.addAll(getPatientArleadyUnderArv(activePatientsNew, dateFormatedNew));
			
			List<Integer> patientsCurrentUnderArv = camerwaService.getOldPatientPerDate(dateFormatedNew);
			List<Integer> patientsSwimsNewButOld = (List<Integer>) camerwaService.twoCollectionsIntersection(patientsCurrentUnderArv, activePatientsNew) ;
			patientsIds.addAll(patientsSwimsNewButOld);
			
			
			List<Integer> patientsIdsActiveLast = camerwaService.getActivePatients(patientsIds, patientsExitedFromCareList,dateFormatedLimite);
						
			patientsIdsActive = camerwaService.removeLostFollowUp(patientsIdsActiveLast,dateFormatedLimite); //patientsIdsActiveLast;
			
			
		    
		}else if(KeyPatients.equals("TotalPatientsUnderARV")){
			// getting patients under arv in past time
			List<Integer> patientsIds = (List<Integer>) camerwaService.getPatientsUnderArv(dateFormatedNew,dateFormatedLimite);	
			List<Integer> activePatients = camerwaService.getActivePatients(patientsIds, patientsExitedFromCareList,dateFormatedLimite);

			activePatients.removeAll(getPatientArleadyUnderArv(activePatients, dateFormatedNew));
				
			// getting new patients under arv
			
			List<Integer> allPatientsIds = (List<Integer>) camerwaService.getAllPatientsUnderARV(dateFormatedNew,dateFormatedLimite);
		    List<Integer> patientsIdsActiveLast = camerwaService.getActivePatients(allPatientsIds, patientsExitedFromCareList,dateFormatedLimite);
		    
		    List<Integer> patientsIdsNew = (List<Integer>) camerwaService.getPatientsUnderArv(dateFormatedNew,dateFormatedLimite);	
			List<Integer> activePatientsNew = camerwaService.getActivePatients(patientsIdsNew, patientsExitedFromCareList,dateFormatedLimite);
			
			patientsIdsActiveLast.addAll(getPatientArleadyUnderArv(activePatientsNew, dateFormatedNew));
	
		    			
			patientsIdsActive = (List<Integer>) camerwaService.union(activePatients, patientsIdsActiveLast);
			
			//log.info("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%this is after union %%%%%%%%"+patientsIdsActive.size());
							
		}else if(KeyPatients.equals("adultsUnderARVNew")){
			List<Integer> patientsIds = (List<Integer>) camerwaService.getAdultDOBQueryNew(dateFormatedNew,dateFormatedLimite);
			List<Integer> activePatients = camerwaService.getActivePatients(patientsIds, patientsExitedFromCareList,dateFormatedLimite);
		    
			List<Integer> adultCurentUnderArv = new ArrayList<Integer>();
			for (Integer patientId : activePatients) {
				if (camerwaService.isAnyArvDrugOld(patientId, dateFormatedNew)) {
					adultCurentUnderArv.add(patientId);
				}
			}
			activePatients.removeAll(adultCurentUnderArv);
			
			List<Integer> patientsCurrentUnderArv = camerwaService.getOldPatientPerDate(dateFormatedNew);
			
			List<Integer> patientsAdultSwimsNewButOld = (List<Integer>) camerwaService.twoCollectionsIntersection(patientsCurrentUnderArv, activePatients) ;
			activePatients.removeAll(patientsAdultSwimsNewButOld);
			
			
			
		  //  activePatients.removeAll(getPatientArleadyUnderArv(activePatients, dateFormatedNew));
			 
			patientsIdsActive = activePatients;
		    
		}else if(KeyPatients.equals("LastAdultsUnderARV")){
			List<Integer> patientsIds = (List<Integer>) camerwaService.getAllAdultDOBQuery(dateFormatedNew,dateFormatedLimite);
		    List<Integer> patientsIdsActiveLast = camerwaService.getActivePatients(patientsIds, patientsExitedFromCareList,dateFormatedLimite);
		    
		    
		    List<Integer> patientsIdsNew = (List<Integer>) camerwaService.getAdultDOBQueryNew(dateFormatedNew,dateFormatedLimite);	
			
			
			List<Integer> patientsCurrentUnderArv = camerwaService.getOldPatientPerDate(dateFormatedNew);
			
			List<Integer> patientsAdultSwimsNewButOld = (List<Integer>) camerwaService.twoCollectionsIntersection(patientsCurrentUnderArv, patientsIdsNew) ;
			
			
		    patientsIdsActiveLast.addAll(getPatientArleadyUnderArv(patientsIdsNew, dateFormatedNew));
		    patientsIdsActiveLast.addAll(patientsAdultSwimsNewButOld);
		    
		    List<Integer> activePatientsNew = camerwaService.getActivePatients(patientsIdsActiveLast, patientsExitedFromCareList,dateFormatedLimite);
		    
		    patientsIdsActive = activePatientsNew;
			//patientsIdsActive = camerwaService.removeLostFollowUp(patientsIdsActiveLast,dateFormatedLimite);// patientsIdsActiveLast;
			
		}else if(KeyPatients.equals("TotalAdultsUnderARV")){
			List<Integer> patientsIdsAdultDOBQueryNew = (List<Integer>) camerwaService.getAdultDOBQueryNew(dateFormatedNew,dateFormatedLimite);
			List<Integer> activePatients = camerwaService.getActivePatients(patientsIdsAdultDOBQueryNew, patientsExitedFromCareList,dateFormatedLimite);
			activePatients.removeAll(getPatientArleadyUnderArv(activePatients, dateFormatedNew));
            
			
			
		    List<Integer> patientsIdsAllAdultDOBQuery = (List<Integer>) camerwaService.getAllAdultDOBQuery(dateFormatedNew,dateFormatedLimite);
		    List<Integer> patientsIdsActiveLast = camerwaService.getActivePatients(patientsIdsAllAdultDOBQuery, patientsExitedFromCareList,dateFormatedLimite);
		    
		    List<Integer> patientsIdsNew = (List<Integer>) camerwaService.getAdultDOBQueryNew(dateFormatedNew,dateFormatedLimite);
			List<Integer> activePatientsNew = camerwaService.getActivePatients(patientsIdsNew, patientsExitedFromCareList,dateFormatedLimite);
			
			patientsIdsActiveLast.addAll(getPatientArleadyUnderArv(activePatientsNew, dateFormatedNew));
			
		    
			patientsIdsActive = (List<Integer>) camerwaService.union(activePatients, patientsIdsActiveLast) ;
		    //patientsIdsActive = camerwaService.getActivePatients(tatalpatientsIdsAdultUnderArv, patientsExitedFromCareList);	
		}else if(KeyPatients.equals("kidsDOBQueryNew")){
			List<Integer> patientsIds = (List<Integer>) camerwaService.getkidsDOBQueryNew(dateFormatedNew,dateFormatedLimite);
		    List<Integer> activePatients = camerwaService.getActivePatients(patientsIds, patientsExitedFromCareList,dateFormatedLimite);
		    activePatients.removeAll(getPatientArleadyUnderArv(activePatients, dateFormatedNew));
			patientsIdsActive = activePatients;
		    
		}else if(KeyPatients.equals("LastkidsDOBQuery")){
			List<Integer> patientsIds = (List<Integer>) camerwaService.getAllKidsDOBQuery(dateFormatedNew,dateFormatedLimite);
		    List<Integer> patientsIdsActiveLast = camerwaService.getActivePatients(patientsIds, patientsExitedFromCareList,dateFormatedLimite);
		    
		    List<Integer> patientsIdsNew = (List<Integer>) camerwaService.getkidsDOBQueryNew(dateFormatedNew,dateFormatedLimite);	
			List<Integer> activePatientsNew = camerwaService.getActivePatients(patientsIdsNew, patientsExitedFromCareList,dateFormatedLimite);
			
			patientsIdsActiveLast.addAll(getPatientArleadyUnderArv(activePatientsNew, dateFormatedNew));
			patientsIdsActive = patientsIdsActiveLast;
			   
		}else if(KeyPatients.equals("TotalKidsDOBQuery")){
			List<Integer> patientsIdsKidsDOBQueryNew = (List<Integer>) camerwaService.getkidsDOBQueryNew(dateFormatedNew,dateFormatedLimite);
			List<Integer> activePatients = camerwaService.getActivePatients(patientsIdsKidsDOBQueryNew, patientsExitedFromCareList,dateFormatedLimite);
			activePatients.removeAll(getPatientArleadyUnderArv(activePatients, dateFormatedNew));
            
			
			List<Integer> patientsIdsAllKidsDOBQuery = (List<Integer>) camerwaService.getAllKidsDOBQuery(dateFormatedNew,dateFormatedLimite);
            List<Integer> patientsIdsActiveLast = camerwaService.getActivePatients(patientsIdsAllKidsDOBQuery, patientsExitedFromCareList,dateFormatedLimite);
		    
		    List<Integer> patientsIdsNew = (List<Integer>) camerwaService.getkidsDOBQueryNew(dateFormatedNew,dateFormatedLimite);
			List<Integer> activePatientsNew = camerwaService.getActivePatients(patientsIdsNew, patientsExitedFromCareList,dateFormatedLimite);
			
			patientsIdsActiveLast.addAll(getPatientArleadyUnderArv(activePatientsNew, dateFormatedNew));
			
			
			patientsIdsActive = (List<Integer>) camerwaService.union(activePatients, patientsIdsActiveLast);
			
			
		    //patientsIdsActive = camerwaService.getActivePatients(totalKidsUnderArv, patientsExitedFromCareList);	
		}else if(KeyPatients.equals("activekidsUnderArvComprimeNew")){
			List<Integer> patientsIds = (List<Integer>) camerwaService.getKidsUnderArvComprimeNew(dateFormatedNew,dateFormatedLimite);
		    patientsIdsActive = camerwaService.getActivePatients(patientsIds, patientsExitedFromCareList,dateFormatedLimite);	
		}else if(KeyPatients.equals("allActiveKidsUnderArvComprime")){
			List<Integer> patientsIds = (List<Integer>) camerwaService.getAllKidsUnderArvComprime(dateFormatedNew,dateFormatedLimite);
		    patientsIdsActive = camerwaService.getActivePatients(patientsIds, patientsExitedFromCareList,dateFormatedLimite);	
		}else if(KeyPatients.equals("activekidsUnderArvSiropsNew")){
			List<Integer> patientsIds = (List<Integer>) camerwaService.getKidsUnderArvSiropsNew(dateFormatedNew,dateFormatedLimite);
		    patientsIdsActive = camerwaService.getActivePatients(patientsIds, patientsExitedFromCareList,dateFormatedLimite);	
		}else if(KeyPatients.equals("allActiveKidsUnderArvSiropsLast")){
			List<Integer> patientsIds = (List<Integer>) camerwaService.getAllKidsUnderArvSiropsLast(dateFormatedNew,dateFormatedLimite);
		    patientsIdsActive = camerwaService.getActivePatients(patientsIds, patientsExitedFromCareList,dateFormatedLimite);	
		}
		
		
		/*else if(KeyPatients.equals("activekidsUnderArvComprimeNew")){
			List<Integer> patientsIdsKidsUnderArvComprimeNew = (List<Integer>) camerwaService.getKidsUnderArvComprimeNew(dateFormatedNew,dateFormatedLimite);
		    patientsIdsActive = camerwaService.getActivePatients(patientsIdsKidsUnderArvComprimeNew, patientsExitedFromCareList,dateFormatedNew);	
		}*//*else if(KeyPatients.equals("allActiveKidsUnderArvComprime")){
			List<Integer> patientsIdsAllKidsUnderArvComprime = (List<Integer>) camerwaService.getAllKidsUnderArvComprime(dateFormatedNew,dateFormatedLimite);
		    patientsIdsActive = camerwaService.getActivePatients(patientsIdsAllKidsUnderArvComprime, patientsExitedFromCareList,dateFormatedNew);	
		}*/else if(KeyPatients.equals("TotalKidsUnderArvComprime")){
			List<Integer> patientsIdsKidsUnderArvComprimeNew = (List<Integer>) camerwaService.getKidsUnderArvComprimeNew(dateFormatedNew,dateFormatedLimite);
			List<Integer> patientsIdsAllKidsUnderArvComprime = (List<Integer>) camerwaService.getAllKidsUnderArvComprime(dateFormatedNew,dateFormatedLimite);
			List<Integer> totalKidsUnderArvComprime = (List<Integer>) camerwaService.union(patientsIdsKidsUnderArvComprimeNew, patientsIdsAllKidsUnderArvComprime);
		    patientsIdsActive = camerwaService.getActivePatients(totalKidsUnderArvComprime, patientsExitedFromCareList,dateFormatedLimite);	
		}else if(KeyPatients.equals("TotalKidsUnderArvSirops")){
			List<Integer> patientsIdsKidsUnderArvSiropsNew = (List<Integer>) camerwaService.getKidsUnderArvSiropsNew(dateFormatedNew,dateFormatedLimite);
			List<Integer> patientsIdsAllKidsUnderArvSiropsLast = (List<Integer>) camerwaService.getAllKidsUnderArvSiropsLast(dateFormatedNew,dateFormatedLimite);
			List<Integer> totalKidsUnderArvSirops = (List<Integer>) camerwaService.union(patientsIdsKidsUnderArvSiropsNew, patientsIdsAllKidsUnderArvSiropsLast);
		    patientsIdsActive = camerwaService.getActivePatients(totalKidsUnderArvSirops, patientsExitedFromCareList,dateFormatedLimite);	
		}
		
		
		else if(KeyPatients.equals("adultRegimenLast1")){
			List<Integer> patientsIds = (List<Integer>) camerwaService.getRegimenAdultLast(dateFormatedNew,dateFormatedLimite, 628, 797, 631,null);
		    patientsIdsActive = camerwaService.getActivePatients(patientsIds, patientsExitedFromCareList,dateFormatedLimite);	
		}else if(KeyPatients.equals("adultRegimenNew1")){
			List<Integer> patientsIds = (List<Integer>) camerwaService.getRegimenAdultNew(dateFormatedNew,dateFormatedLimite, 628, 797, 631,null).get(0);
		    patientsIdsActive = camerwaService.getActivePatients(patientsIds, patientsExitedFromCareList,dateFormatedLimite);	
		}else if(KeyPatients.equals("TotalAdulRegimen1")){
			List<Integer> patientsIdsAdultRegimen = (List<Integer>) camerwaService.getRegimenAdultNew(dateFormatedNew,dateFormatedLimite, 628, 797, 631,null).get(0);
			List<Integer> patientsIdsAllAdultRegimen = (List<Integer>) camerwaService.getRegimenAdultLast(dateFormatedNew,dateFormatedLimite, 628, 797, 631,null);
			List<Integer> totalAdultRegimen = (List<Integer>) camerwaService.union(patientsIdsAdultRegimen, patientsIdsAllAdultRegimen);
		    patientsIdsActive = camerwaService.getActivePatients(totalAdultRegimen, patientsExitedFromCareList,dateFormatedLimite);	
		}else if(KeyPatients.equals("adultRegimenLast2")){
			List<Integer> patientsIds = (List<Integer>) camerwaService.getRegimenAdultLast(dateFormatedNew,dateFormatedLimite, 628, 625, 631,null);
		    patientsIdsActive = camerwaService.getActivePatients(patientsIds, patientsExitedFromCareList,dateFormatedLimite);	
		}else if(KeyPatients.equals("adultRegimenNew2")){
			List<Integer> patientsIds = (List<Integer>) camerwaService.getRegimenAdultNew(dateFormatedNew,dateFormatedLimite, 628, 625, 631,null).get(0);
		    patientsIdsActive = camerwaService.getActivePatients(patientsIds, patientsExitedFromCareList,dateFormatedLimite);	
		}else if(KeyPatients.equals("TotalAdulRegimen2")){
			List<Integer> patientsIdsAdultRegimen = (List<Integer>) camerwaService.getRegimenAdultNew(dateFormatedNew,dateFormatedLimite, 628, 625, 631,null).get(0);
			List<Integer> patientsIdsAllAdultRegimen = (List<Integer>) camerwaService.getRegimenAdultLast(dateFormatedNew,dateFormatedLimite, 628, 625, 631,null);
			List<Integer> totalAdultRegimen = (List<Integer>) camerwaService.union(patientsIdsAdultRegimen, patientsIdsAllAdultRegimen);
		    patientsIdsActive = camerwaService.getActivePatients(totalAdultRegimen, patientsExitedFromCareList,dateFormatedNew);	
		}else if(KeyPatients.equals("adultRegimenLast3")){
			List<Integer> patientsIds = (List<Integer>) camerwaService.getRegimenAdultLast(dateFormatedNew,dateFormatedLimite, 628, 797, 633,null);
		    patientsIdsActive = camerwaService.getActivePatients(patientsIds, patientsExitedFromCareList,dateFormatedLimite);	
		}else if(KeyPatients.equals("adultRegimenNew3")){
			List<Integer> patientsIds = (List<Integer>) camerwaService.getRegimenAdultNew(dateFormatedNew,dateFormatedLimite, 628, 797, 633,null).get(0);
		    patientsIdsActive = camerwaService.getActivePatients(patientsIds, patientsExitedFromCareList,dateFormatedLimite);	
		}else if(KeyPatients.equals("TotalAdulRegimen3")){
			List<Integer> patientsIdsAdultRegimen = (List<Integer>) camerwaService.getRegimenAdultNew(dateFormatedNew,dateFormatedLimite, 628, 797, 633,null).get(0);
			List<Integer> patientsIdsAllAdultRegimen = (List<Integer>) camerwaService.getRegimenAdultLast(dateFormatedNew,dateFormatedLimite, 628, 797, 633,null);
			List<Integer> totalAdultRegimen = (List<Integer>) camerwaService.union(patientsIdsAdultRegimen, patientsIdsAllAdultRegimen);
		    patientsIdsActive = camerwaService.getActivePatients(totalAdultRegimen, patientsExitedFromCareList,dateFormatedNew);	
		}else if(KeyPatients.equals("adultRegimenLast4")){
			List<Integer> patientsIds = (List<Integer>) camerwaService.getRegimenAdultLast(dateFormatedNew,dateFormatedLimite, 628, 625, 633,null);
		    patientsIdsActive = camerwaService.getActivePatients(patientsIds, patientsExitedFromCareList,dateFormatedLimite);	
		}else if(KeyPatients.equals("adultRegimenNew4")){
			List<Integer> patientsIds = (List<Integer>) camerwaService.getRegimenAdultNew(dateFormatedNew,dateFormatedLimite, 628, 625, 633,null).get(0);
		    patientsIdsActive = camerwaService.getActivePatients(patientsIds, patientsExitedFromCareList,dateFormatedLimite);	
		}else if(KeyPatients.equals("TotalAdulRegimen4")){
			List<Integer> patientsIdsAdultRegimen = (List<Integer>) camerwaService.getRegimenAdultNew(dateFormatedNew,dateFormatedLimite, 628, 625, 633,null).get(0);
			List<Integer> patientsIdsAllAdultRegimen = (List<Integer>) camerwaService.getRegimenAdultLast(dateFormatedNew,dateFormatedLimite, 628, 625, 633,null);
			List<Integer> totalAdultRegimen = (List<Integer>) camerwaService.union(patientsIdsAdultRegimen, patientsIdsAllAdultRegimen);
		    patientsIdsActive = camerwaService.getActivePatients(totalAdultRegimen, patientsExitedFromCareList,dateFormatedNew);	
		}else if(KeyPatients.equals("adultRegimenLast5")){
			List<Integer> patientsIds = (List<Integer>) camerwaService.getRegimenAdultLast(dateFormatedNew,dateFormatedLimite, 628, 802, 631,null);
		    patientsIdsActive = camerwaService.getActivePatients(patientsIds, patientsExitedFromCareList,dateFormatedLimite);	
		}else if(KeyPatients.equals("adultRegimenNew5")){
			List<Integer> patientsIds = (List<Integer>) camerwaService.getRegimenAdultNew(dateFormatedNew,dateFormatedLimite, 628, 802, 631,null).get(0);
		    patientsIdsActive = camerwaService.getActivePatients(patientsIds, patientsExitedFromCareList,dateFormatedLimite);	
		}else if(KeyPatients.equals("TotalAdulRegimen5")){
			List<Integer> patientsIdsAdultRegimen = (List<Integer>) camerwaService.getRegimenAdultNew(dateFormatedNew,dateFormatedLimite, 628, 802, 631,null).get(0);
			List<Integer> patientsIdsAllAdultRegimen = (List<Integer>) camerwaService.getRegimenAdultLast(dateFormatedNew,dateFormatedLimite, 628, 802, 631,null);
			List<Integer> totalAdultRegimen = (List<Integer>) camerwaService.union(patientsIdsAdultRegimen, patientsIdsAllAdultRegimen);
		    patientsIdsActive = camerwaService.getActivePatients(totalAdultRegimen, patientsExitedFromCareList,dateFormatedNew);	
		}else if(KeyPatients.equals("adultRegimenLast6")){
			List<Integer> patientsIds = (List<Integer>) camerwaService.getRegimenAdultLast(dateFormatedNew,dateFormatedLimite, 628, 802, 633,null);
		    patientsIdsActive = camerwaService.getActivePatients(patientsIds, patientsExitedFromCareList,dateFormatedLimite);	
		}else if(KeyPatients.equals("adultRegimenNew6")){
			List<Integer> patientsIds = (List<Integer>) camerwaService.getRegimenAdultNew(dateFormatedNew,dateFormatedLimite, 628, 802, 633,null).get(0);
		    patientsIdsActive = camerwaService.getActivePatients(patientsIds, patientsExitedFromCareList,dateFormatedLimite);	
		}else if(KeyPatients.equals("TotalAdulRegimen6")){
			List<Integer> patientsIdsAdultRegimen = (List<Integer>) camerwaService.getRegimenAdultNew(dateFormatedNew,dateFormatedLimite, 628, 802, 633,null).get(0);
			List<Integer> patientsIdsAllAdultRegimen = (List<Integer>) camerwaService.getRegimenAdultLast(dateFormatedNew,dateFormatedLimite, 628, 802, 633,null);
			List<Integer> totalAdultRegimen = (List<Integer>) camerwaService.union(patientsIdsAdultRegimen, patientsIdsAllAdultRegimen);
		    patientsIdsActive = camerwaService.getActivePatients(totalAdultRegimen, patientsExitedFromCareList,dateFormatedNew);	
		}else if(KeyPatients.equals("adultRegimenLast7")){
			List<Integer> patientsIds = (List<Integer>) camerwaService.getRegimenAdultLast(dateFormatedNew,dateFormatedLimite, 628, 797, 796,null);
		    patientsIdsActive = camerwaService.getActivePatients(patientsIds, patientsExitedFromCareList,dateFormatedLimite);	
		}else if(KeyPatients.equals("adultRegimenNew7")){
			List<Integer> patientsIds = (List<Integer>) camerwaService.getRegimenAdultNew(dateFormatedNew,dateFormatedLimite, 628, 797, 796,null).get(0);
		    patientsIdsActive = camerwaService.getActivePatients(patientsIds, patientsExitedFromCareList,dateFormatedLimite);	
		}else if(KeyPatients.equals("TotalAdulRegimen7")){
			List<Integer> patientsIdsAdultRegimen = (List<Integer>) camerwaService.getRegimenAdultNew(dateFormatedNew,dateFormatedLimite, 628, 797, 796,null).get(0);
			List<Integer> patientsIdsAllAdultRegimen = (List<Integer>) camerwaService.getRegimenAdultLast(dateFormatedNew,dateFormatedLimite, 628, 797, 796,null);
			List<Integer> totalAdultRegimen = (List<Integer>) camerwaService.union(patientsIdsAdultRegimen, patientsIdsAllAdultRegimen);
		    patientsIdsActive = camerwaService.getActivePatients(totalAdultRegimen, patientsExitedFromCareList,dateFormatedNew);	
		}else if(KeyPatients.equals("adultRegimenLast8")){
			List<Integer> patientsIds = (List<Integer>) camerwaService.getRegimenAdultLast(dateFormatedNew,dateFormatedLimite, 628, 814, 631,null);
		    patientsIdsActive = camerwaService.getActivePatients(patientsIds, patientsExitedFromCareList,dateFormatedLimite);	
		}else if(KeyPatients.equals("adultRegimenNew8")){
			List<Integer> patientsIds = (List<Integer>) camerwaService.getRegimenAdultNew(dateFormatedNew,dateFormatedLimite, 628, 814, 631,null).get(0);
		    patientsIdsActive = camerwaService.getActivePatients(patientsIds, patientsExitedFromCareList,dateFormatedLimite);	
		}else if(KeyPatients.equals("TotalAdulRegimen8")){
			List<Integer> patientsIdsAdultRegimen = (List<Integer>) camerwaService.getRegimenAdultNew(dateFormatedNew,dateFormatedLimite, 628, 814, 631,null).get(0);
			List<Integer> patientsIdsAllAdultRegimen = (List<Integer>) camerwaService.getRegimenAdultLast(dateFormatedNew,dateFormatedLimite, 628, 814, 631,null);
			List<Integer> totalAdultRegimen = (List<Integer>) camerwaService.union(patientsIdsAdultRegimen, patientsIdsAllAdultRegimen);
		    patientsIdsActive = camerwaService.getActivePatients(totalAdultRegimen, patientsExitedFromCareList,dateFormatedNew);	
		}else if(KeyPatients.equals("adultRegimenLast9")){
			List<Integer> patientsIds = (List<Integer>) camerwaService.getRegimenAdultLast(dateFormatedNew,dateFormatedLimite, 628, 814, 625,null);
		    patientsIdsActive = camerwaService.getActivePatients(patientsIds, patientsExitedFromCareList,dateFormatedLimite);	
		}else if(KeyPatients.equals("adultRegimenNew9")){
			List<Integer> patientsIds = (List<Integer>) camerwaService.getRegimenAdultNew(dateFormatedNew,dateFormatedLimite, 628, 814, 625,null).get(0);
		    patientsIdsActive = camerwaService.getActivePatients(patientsIds, patientsExitedFromCareList,dateFormatedLimite);	
		}else if(KeyPatients.equals("TotalAdulRegimen9")){
			List<Integer> patientsIdsAdultRegimen = (List<Integer>) camerwaService.getRegimenAdultNew(dateFormatedNew,dateFormatedLimite, 628, 814, 625,null).get(0);
			List<Integer> patientsIdsAllAdultRegimen = (List<Integer>) camerwaService.getRegimenAdultLast(dateFormatedNew,dateFormatedLimite, 628, 814, 625,null);
			List<Integer> totalAdultRegimen = (List<Integer>) camerwaService.union(patientsIdsAdultRegimen, patientsIdsAllAdultRegimen);
		    patientsIdsActive = camerwaService.getActivePatients(totalAdultRegimen, patientsExitedFromCareList,dateFormatedNew);	
		}else if(KeyPatients.equals("adultRegimenLast10")){
			List<Integer> patientsIds = (List<Integer>) camerwaService.getRegimenAdultLast(dateFormatedNew,dateFormatedLimite, 625, 814, 633,null);
		    patientsIdsActive = camerwaService.getActivePatients(patientsIds, patientsExitedFromCareList,dateFormatedLimite);	
		}else if(KeyPatients.equals("adultRegimenNew10")){
			List<Integer> patientsIds = (List<Integer>) camerwaService.getRegimenAdultNew(dateFormatedNew,dateFormatedLimite, 625, 814, 633,null).get(0);
		    patientsIdsActive = camerwaService.getActivePatients(patientsIds, patientsExitedFromCareList,dateFormatedLimite);	
		}else if(KeyPatients.equals("TotalAdulRegimen10")){
			List<Integer> patientsIdsAdultRegimen = (List<Integer>) camerwaService.getRegimenAdultNew(dateFormatedNew,dateFormatedLimite, 625, 814, 633,null).get(0);
			List<Integer> patientsIdsAllAdultRegimen = (List<Integer>) camerwaService.getRegimenAdultLast(dateFormatedNew,dateFormatedLimite, 625, 814, 633,null);
			List<Integer> totalAdultRegimen = (List<Integer>) camerwaService.union(patientsIdsAdultRegimen, patientsIdsAllAdultRegimen);
		    patientsIdsActive = camerwaService.getActivePatients(totalAdultRegimen, patientsExitedFromCareList,dateFormatedNew);	
		}else if(KeyPatients.equals("adultRegimenLast11")){
			List<Integer> patientsIds = (List<Integer>) camerwaService.getRegimenAdultLast(dateFormatedNew,dateFormatedLimite, 625, 796, 633,null);
		    patientsIdsActive = camerwaService.getActivePatients(patientsIds, patientsExitedFromCareList,dateFormatedLimite);	
		}else if(KeyPatients.equals("adultRegimenNew11")){
			List<Integer> patientsIds = (List<Integer>) camerwaService.getRegimenAdultNew(dateFormatedNew,dateFormatedLimite, 625, 796, 633,null).get(0);
		    patientsIdsActive = camerwaService.getActivePatients(patientsIds, patientsExitedFromCareList,dateFormatedLimite);	
		}else if(KeyPatients.equals("TotalAdulRegimen11")){
			List<Integer> patientsIdsAdultRegimen = (List<Integer>) camerwaService.getRegimenAdultNew(dateFormatedNew,dateFormatedLimite, 625, 796, 633,null).get(0);
			List<Integer> patientsIdsAllAdultRegimen = (List<Integer>) camerwaService.getRegimenAdultLast(dateFormatedNew,dateFormatedLimite, 625, 796, 633,null);
			List<Integer> totalAdultRegimen = (List<Integer>) camerwaService.union(patientsIdsAdultRegimen, patientsIdsAllAdultRegimen);
		    patientsIdsActive = camerwaService.getActivePatients(totalAdultRegimen, patientsExitedFromCareList,dateFormatedNew);	
		}else if(KeyPatients.equals("adultRegimenLast12")){
			List<Integer> patientsIds = (List<Integer>) camerwaService.getRegimenAdultLast(dateFormatedNew,dateFormatedLimite, 628, 625, 631,null);
		    patientsIdsActive = camerwaService.getActivePatients(patientsIds, patientsExitedFromCareList,dateFormatedLimite);	
		}else if(KeyPatients.equals("adultRegimenNew12")){
			List<Integer> patientsIds = (List<Integer>) camerwaService.getRegimenAdultNew(dateFormatedNew,dateFormatedLimite, 814, 796, 633,null).get(0);
		    patientsIdsActive = camerwaService.getActivePatients(patientsIds, patientsExitedFromCareList,dateFormatedLimite);	
		}else if(KeyPatients.equals("TotalAdulRegimen12")){
			List<Integer> patientsIdsAdultRegimen = (List<Integer>) camerwaService.getRegimenAdultNew(dateFormatedNew,dateFormatedLimite, 814, 796, 633,null).get(0);
			List<Integer> patientsIdsAllAdultRegimen = (List<Integer>) camerwaService.getRegimenAdultLast(dateFormatedNew,dateFormatedLimite, 814, 796, 633,null);
			List<Integer> totalAdultRegimen = (List<Integer>) camerwaService.union(patientsIdsAdultRegimen, patientsIdsAllAdultRegimen);
		    patientsIdsActive = camerwaService.getActivePatients(totalAdultRegimen, patientsExitedFromCareList,dateFormatedNew);	
		}else if(KeyPatients.equals("adultRegimenLast13")){
			List<Integer> patientsIds = (List<Integer>) camerwaService.getRegimenAdultLast(dateFormatedNew,dateFormatedLimite, 797, 814, 633,null);
		    patientsIdsActive = camerwaService.getActivePatients(patientsIds, patientsExitedFromCareList,dateFormatedLimite);	
		}else if(KeyPatients.equals("adultRegimenNew13")){
			List<Integer> patientsIds = (List<Integer>) camerwaService.getRegimenAdultNew(dateFormatedNew,dateFormatedLimite, 797, 814, 633,null).get(0);
		    patientsIdsActive = camerwaService.getActivePatients(patientsIds, patientsExitedFromCareList,dateFormatedLimite);	
		}else if(KeyPatients.equals("TotalAdulRegimen13")){
			List<Integer> patientsIdsAdultRegimen = (List<Integer>) camerwaService.getRegimenAdultNew(dateFormatedNew,dateFormatedLimite, 797, 814, 633,null).get(0);
			List<Integer> patientsIdsAllAdultRegimen = (List<Integer>) camerwaService.getRegimenAdultLast(dateFormatedNew,dateFormatedLimite, 797, 814, 633,null);
			List<Integer> totalAdultRegimen = (List<Integer>) camerwaService.union(patientsIdsAdultRegimen, patientsIdsAllAdultRegimen);
		    patientsIdsActive = camerwaService.getActivePatients(totalAdultRegimen, patientsExitedFromCareList,dateFormatedNew);	
		}else if(KeyPatients.equals("adultRegimenLast14")){
			List<Integer> patientsIds = (List<Integer>) camerwaService.getRegimenAdultLast(dateFormatedNew,dateFormatedLimite, 628, 797, 814,null);
			//log.info("!!!!!!!!!!!!!!! this is on page report !!!!!!!!!!!!!!"+patientsIds.size()+" 1 "+628+" 2 "+797+" 5 "+814);
		    patientsIdsActive = camerwaService.getActivePatients(patientsIds, patientsExitedFromCareList,dateFormatedLimite);	
		    
		}else if(KeyPatients.equals("adultRegimenNew14")){
			List<Integer> patientsIds = (List<Integer>) camerwaService.getRegimenAdultNew(dateFormatedNew,dateFormatedLimite, 628, 797, 814,null).get(0);
		    patientsIdsActive = camerwaService.getActivePatients(patientsIds, patientsExitedFromCareList,dateFormatedLimite);	
		}else if(KeyPatients.equals("TotalAdulRegimen14")){
			List<Integer> patientsIdsAdultRegimen = (List<Integer>) camerwaService.getRegimenAdultNew(dateFormatedNew,dateFormatedLimite, 628, 797, 814,null).get(0);
			List<Integer> patientsIdsAllAdultRegimen = (List<Integer>) camerwaService.getRegimenAdultLast(dateFormatedNew,dateFormatedLimite, 628, 797, 814,null);
			List<Integer> totalAdultRegimen = (List<Integer>) camerwaService.union(patientsIdsAdultRegimen, patientsIdsAllAdultRegimen);
		    patientsIdsActive = camerwaService.getActivePatients(totalAdultRegimen, patientsExitedFromCareList,dateFormatedNew);	
		}else if(KeyPatients.equals("adultRegimenLast15")){
			List<Integer> patientsIds = (List<Integer>) camerwaService.getRegimenAdultLast(dateFormatedNew,dateFormatedLimite, 797, null, null,null);
		    patientsIdsActive = camerwaService.getActivePatients(patientsIds, patientsExitedFromCareList,dateFormatedLimite);	
		}else if(KeyPatients.equals("adultRegimenNew15")){
			List<Integer> patientsIds = (List<Integer>) camerwaService.getRegimenAdultNew(dateFormatedNew,dateFormatedLimite, 797, null,null,null).get(0);
		    patientsIdsActive = camerwaService.getActivePatients(patientsIds, patientsExitedFromCareList,dateFormatedLimite);	
		}else if(KeyPatients.equals("TotalAdulRegimen15")){
			List<Integer> patientsIdsAdultRegimen = (List<Integer>) camerwaService.getRegimenAdultNew(dateFormatedNew,dateFormatedLimite, 797, null,null,null).get(0);
			List<Integer> patientsIdsAllAdultRegimen = (List<Integer>) camerwaService.getRegimenAdultLast(dateFormatedNew,dateFormatedLimite, 797, null,null,null);
			List<Integer> totalAdultRegimen = (List<Integer>) camerwaService.union(patientsIdsAdultRegimen, patientsIdsAllAdultRegimen);
		    patientsIdsActive = camerwaService.getActivePatients(totalAdultRegimen, patientsExitedFromCareList,dateFormatedNew);	
		}else if(KeyPatients.equals("adultRegimenLast16")){
			List<Integer> patientsIds = (List<Integer>) camerwaService.getRegimenAdultLast(dateFormatedNew,dateFormatedLimite, 628,797,802,814);
		    patientsIdsActive = camerwaService.getActivePatients(patientsIds, patientsExitedFromCareList,dateFormatedLimite);	
		}else if(KeyPatients.equals("adultRegimenNew16")){
			List<Integer> patientsIds = (List<Integer>) camerwaService.getRegimenAdultNew(dateFormatedNew,dateFormatedLimite, 628,797,802,814).get(0);
		    patientsIdsActive = camerwaService.getActivePatients(patientsIds, patientsExitedFromCareList,dateFormatedLimite);	
		}else if(KeyPatients.equals("TotalAdulRegimen16")){
			List<Integer> patientsIdsAdultRegimen = (List<Integer>) camerwaService.getRegimenAdultNew(dateFormatedNew,dateFormatedLimite, 628,797,802,814).get(0);
			List<Integer> patientsIdsAllAdultRegimen = (List<Integer>) camerwaService.getRegimenAdultLast(dateFormatedNew,dateFormatedLimite, 628,797,802,814);
			List<Integer> totalAdultRegimen = (List<Integer>) camerwaService.union(patientsIdsAdultRegimen, patientsIdsAllAdultRegimen);
		    patientsIdsActive = camerwaService.getActivePatients(totalAdultRegimen, patientsExitedFromCareList,dateFormatedNew);	
		  
		}else if(KeyPatients.equals("patientsExitedLast1")){
			List<Integer> patientsIds = (List<Integer>) camerwaService.getPatientsExitedFromCareDefinedLast(1743,dateFormatedNew);
		    patientsIdsActive = patientsIds ;	
		}else if(KeyPatients.equals("patientsExitedNew1")){
			List<Integer> patientsIds = (List<Integer>) camerwaService.getPatientsExitedFromCareDefinedNew(1743,dateFormatedNew,dateFormatedLimite);
		    patientsIdsActive = patientsIds ;	
		}else if(KeyPatients.equals("TotalpatientsExitedFromCare1")){
			List<Integer> patientsIdsLast = (List<Integer>) camerwaService.getPatientsExitedFromCareDefinedLast(1743,dateFormatedNew);
			List<Integer> patientsIdsNew = (List<Integer>) camerwaService.getPatientsExitedFromCareDefinedNew(1743,dateFormatedNew,dateFormatedLimite);
			List<Integer> totalPatientsExitedId = (List<Integer>) camerwaService.union(patientsIdsLast, patientsIdsNew);
		    patientsIdsActive = totalPatientsExitedId;	
		}else if(KeyPatients.equals("patientsExitedLast2")){
			List<Integer> patientsIds = (List<Integer>) camerwaService.getPatientsExitedFromCareDefinedLast(1742,dateFormatedNew);
			//log.info("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!this is the value after"+patientsIds.size()+" the concept was "+1742);
		    patientsIdsActive = patientsIds ;	
		}else if(KeyPatients.equals("patientsExitedNew2")){
			List<Integer> patientsIds = (List<Integer>) camerwaService.getPatientsExitedFromCareDefinedNew(1742,dateFormatedNew,dateFormatedLimite);
			
		   // log.info("#2##################dateFormatedNew#################!!!!!!!!!!!!!!!1####################"+patientsIds);
		    
			patientsIdsActive = patientsIds ;	
		}else if(KeyPatients.equals("TotalpatientsExitedFromCare2")){
			List<Integer> patientsIdsLast = (List<Integer>) camerwaService.getPatientsExitedFromCareDefinedLast(1742,dateFormatedNew);
			List<Integer> patientsIdsNew = (List<Integer>) camerwaService.getPatientsExitedFromCareDefinedNew(1742,dateFormatedNew,dateFormatedLimite);
			List<Integer> totalPatientsExitedId = (List<Integer>) camerwaService.union(patientsIdsLast, patientsIdsNew);
		    patientsIdsActive = totalPatientsExitedId;	
		}else if(KeyPatients.equals("patientsExitedLast3")){
			List<Integer> patientsIds = (List<Integer>) camerwaService.getPatientsExitedFromCareDefinedLast(1744,dateFormatedNew);
		    patientsIdsActive = patientsIds ;	
		}else if(KeyPatients.equals("patientsExitedNew3")){
			List<Integer> patientsIds = (List<Integer>) camerwaService.getPatientsExitedFromCareDefinedNew(1744,dateFormatedNew,dateFormatedLimite);
		    patientsIdsActive = patientsIds ;	
		}else if(KeyPatients.equals("TotalpatientsExitedFromCare3")){
			List<Integer> patientsIdsLast = (List<Integer>) camerwaService.getPatientsExitedFromCareDefinedLast(1744,dateFormatedNew);
			List<Integer> patientsIdsNew = (List<Integer>) camerwaService.getPatientsExitedFromCareDefinedNew(1744,dateFormatedNew,dateFormatedLimite);
			List<Integer> totalPatientsExitedId = (List<Integer>) camerwaService.union(patientsIdsLast, patientsIdsNew);
		    patientsIdsActive = totalPatientsExitedId;	
		}else if(KeyPatients.equals("patientsExitedLast4")){
			List<Integer> patientsIds = (List<Integer>) camerwaService.getPatientsExitedFromCareDefinedLast(1427,dateFormatedNew);
		    patientsIdsActive = patientsIds ;	
		}else if(KeyPatients.equals("patientsExitedNew4")){
			List<Integer> patientsIds = (List<Integer>) camerwaService.getPatientsExitedFromCareDefinedNew(1427,dateFormatedNew,dateFormatedLimite);
		    patientsIdsActive = patientsIds ;	
		}else if(KeyPatients.equals("TotalpatientsExitedFromCare4")){
			List<Integer> patientsIdsLast = (List<Integer>) camerwaService.getPatientsExitedFromCareDefinedLast(1427,dateFormatedNew);
			List<Integer> patientsIdsNew = (List<Integer>) camerwaService.getPatientsExitedFromCareDefinedNew(1427,dateFormatedNew,dateFormatedLimite);
			List<Integer> totalPatientsExitedId = (List<Integer>) camerwaService.union(patientsIdsLast, patientsIdsNew);
		    patientsIdsActive = totalPatientsExitedId;	
		}else if(KeyPatients.equals("pediatricSecondLineLast1")){
			
			List<Integer> allpatientsIds = (List<Integer>) camerwaService.getPediatricUnderSecondLineRegimenLast(dateFormatedNew,dateFormatedLimite,814,796,794,null);
			patientsIdsActive = camerwaService.getActivePatients(allpatientsIds, patientsExitedFromCareList,dateFormatedNew);
		}else if(KeyPatients.equals("pediatricSecondLineNew1")){
			List<Integer> allpatientsIds= (List<Integer>) camerwaService.getPediatricUnderSecondLineRegimenNew(dateFormatedNew,dateFormatedLimite, 814,796,794,null).get(0);
			patientsIdsActive = camerwaService.getActivePatients(allpatientsIds, patientsExitedFromCareList,dateFormatedNew);
		}else if(KeyPatients.equals("TotalPediatricSecondLine1")){
			
			List<Integer> patientUnderSecondLineLast = (List<Integer>) camerwaService.getPediatricUnderSecondLineRegimenLast(dateFormatedNew,dateFormatedLimite,814,796,794,null);
			
			List<Integer> patientUnderSecondLineNew = (List<Integer>) camerwaService.getPediatricUnderSecondLineRegimenNew(dateFormatedNew,dateFormatedLimite, 814,796,794,null).get(0);
			
			 List<Integer> allpatientsIds = (List<Integer>) camerwaService.union(patientUnderSecondLineLast, patientUnderSecondLineNew);
			 patientsIdsActive = camerwaService.getActivePatients(allpatientsIds, patientsExitedFromCareList,dateFormatedNew);
		}else if(KeyPatients.equals("pediatricSecondLineLast2")){
			
			List<Integer> allpatientsIds = (List<Integer>) camerwaService.getPediatricUnderSecondLineRegimenLast(dateFormatedNew,dateFormatedLimite,802,814,794,null);
			patientsIdsActive = camerwaService.getActivePatients(allpatientsIds, patientsExitedFromCareList,dateFormatedNew); 
		}else if(KeyPatients.equals("pediatricSecondLineNew2")){
			List<Integer> allpatientsIds= (List<Integer>) camerwaService.getPediatricUnderSecondLineRegimenNew(dateFormatedNew,dateFormatedLimite, 802,814,794,null).get(0);
			patientsIdsActive = camerwaService.getActivePatients(allpatientsIds, patientsExitedFromCareList,dateFormatedNew);
		}else if(KeyPatients.equals("TotalPediatricSecondLine2")){
			
			List<Integer> patientUnderSecondLineLast = (List<Integer>) camerwaService.getPediatricUnderSecondLineRegimenLast(dateFormatedNew,dateFormatedLimite,802,814,794,null);
			
			List<Integer> patientUnderSecondLineNew = (List<Integer>) camerwaService.getPediatricUnderSecondLineRegimenNew(dateFormatedNew,dateFormatedLimite, 802,814,794,null).get(0);
			
			 List<Integer> allpatientsIds = (List<Integer>) camerwaService.union(patientUnderSecondLineLast, patientUnderSecondLineNew);
			 patientsIdsActive = camerwaService.getActivePatients(allpatientsIds, patientsExitedFromCareList,dateFormatedNew);
		}else if(KeyPatients.equals("pediatricSecondLineLast3")){
			
			List<Integer> allpatientsIds = (List<Integer>) camerwaService.getPediatricUnderSecondLineRegimenLast(dateFormatedNew,dateFormatedLimite,797,631,794,null);
			patientsIdsActive = camerwaService.getActivePatients(allpatientsIds, patientsExitedFromCareList,dateFormatedNew); 
		}else if(KeyPatients.equals("pediatricSecondLineNew3")){
			 List<Integer> allpatientsIds= (List<Integer>) camerwaService.getPediatricUnderSecondLineRegimenNew(dateFormatedNew,dateFormatedLimite, 797,631,794,null).get(0);
			patientsIdsActive = camerwaService.getActivePatients(allpatientsIds, patientsExitedFromCareList,dateFormatedNew);
		}else if(KeyPatients.equals("TotalPediatricSecondLine3")){
			
			List<Integer> patientUnderSecondLineLast = (List<Integer>) camerwaService.getPediatricUnderSecondLineRegimenLast(dateFormatedNew,dateFormatedLimite,797,631,794,null);
			
			List<Integer> patientUnderSecondLineNew = (List<Integer>) camerwaService.getPediatricUnderSecondLineRegimenNew(dateFormatedNew,dateFormatedLimite, 797,631,794,null).get(0);
			
			 List<Integer> allpatientsIds = (List<Integer>) camerwaService.union(patientUnderSecondLineLast, patientUnderSecondLineNew);
			 patientsIdsActive = camerwaService.getActivePatients(allpatientsIds, patientsExitedFromCareList,dateFormatedNew);
		}else if(KeyPatients.equals("pediatricSecondLineLast4")){
			
			 List<Integer> allpatientsIds = (List<Integer>) camerwaService.getPediatricUnderSecondLineRegimenLast(dateFormatedNew,dateFormatedLimite,628,802,794,null);
			patientsIdsActive = camerwaService.getActivePatients(allpatientsIds, patientsExitedFromCareList,dateFormatedNew);
		}else if(KeyPatients.equals("pediatricSecondLineNew4")){
			 List<Integer> allpatientsIds = (List<Integer>) camerwaService.getPediatricUnderSecondLineRegimenNew(dateFormatedNew,dateFormatedLimite, 628,802,794,null).get(0);
			patientsIdsActive = camerwaService.getActivePatients(allpatientsIds, patientsExitedFromCareList,dateFormatedNew);
		}else if(KeyPatients.equals("TotalPediatricSecondLine4")){
			
			List<Integer> patientUnderSecondLineLast = (List<Integer>) camerwaService.getPediatricUnderSecondLineRegimenLast(dateFormatedNew,dateFormatedLimite,628,802,794,null);
			
			List<Integer> patientUnderSecondLineNew = (List<Integer>) camerwaService.getPediatricUnderSecondLineRegimenNew(dateFormatedNew,dateFormatedLimite, 628,802,794,null).get(0);
			
			 List<Integer> allpatientsIds = (List<Integer>) camerwaService.union(patientUnderSecondLineLast, patientUnderSecondLineNew);
			 patientsIdsActive = camerwaService.getActivePatients(allpatientsIds, patientsExitedFromCareList,dateFormatedNew);
		}else if(KeyPatients.equals("pediatricSecondLineLast5")){
			
			 List<Integer> allpatientsIds = (List<Integer>) camerwaService.getPediatricUnderSecondLineRegimenLast(dateFormatedNew,dateFormatedLimite,628,797,794,null);
			patientsIdsActive = camerwaService.getActivePatients(allpatientsIds, patientsExitedFromCareList,dateFormatedNew);
		}else if(KeyPatients.equals("pediatricSecondLineNew5")){
			 List<Integer> allpatientsIds = (List<Integer>) camerwaService.getPediatricUnderSecondLineRegimenNew(dateFormatedNew,dateFormatedLimite, 628,797,794,null).get(0);
			patientsIdsActive = camerwaService.getActivePatients(allpatientsIds, patientsExitedFromCareList,dateFormatedNew);
		}else if(KeyPatients.equals("TotalPediatricSecondLine5")){
			
			List<Integer> patientUnderSecondLineLast = (List<Integer>) camerwaService.getPediatricUnderSecondLineRegimenLast(dateFormatedNew,dateFormatedLimite,628,797,794,null);
			
			List<Integer> patientUnderSecondLineNew = (List<Integer>) camerwaService.getPediatricUnderSecondLineRegimenNew(dateFormatedNew,dateFormatedLimite, 628,797,794,null).get(0);
			
			 List<Integer> allpatientsIds = (List<Integer>) camerwaService.union(patientUnderSecondLineLast, patientUnderSecondLineNew);
			 patientsIdsActive = camerwaService.getActivePatients(allpatientsIds, patientsExitedFromCareList,dateFormatedNew);
		}else if(KeyPatients.equals("pediatricSecondLineLast6")){
			
			 List<Integer> allpatientsIds = (List<Integer>) camerwaService.getPediatricUnderSecondLineRegimenLast(dateFormatedNew,dateFormatedLimite,628,625,794,null);
			patientsIdsActive = camerwaService.getActivePatients(allpatientsIds, patientsExitedFromCareList,dateFormatedNew);
		}else if(KeyPatients.equals("pediatricSecondLineNew6")){
			 List<Integer> allpatientsIds = (List<Integer>) camerwaService.getPediatricUnderSecondLineRegimenNew(dateFormatedNew,dateFormatedLimite, 628,625,794,null).get(0);
			patientsIdsActive = camerwaService.getActivePatients(allpatientsIds, patientsExitedFromCareList,dateFormatedNew);
		}else if(KeyPatients.equals("TotalPediatricSecondLine6")){
			
			List<Integer> patientUnderSecondLineLast = (List<Integer>) camerwaService.getPediatricUnderSecondLineRegimenLast(dateFormatedNew,dateFormatedLimite,628,625,794,null);
			
			List<Integer> patientUnderSecondLineNew = (List<Integer>) camerwaService.getPediatricUnderSecondLineRegimenNew(dateFormatedNew,dateFormatedLimite, 628,625,794,null).get(0);
			
			 List<Integer> allpatientsIds = (List<Integer>) camerwaService.union(patientUnderSecondLineLast, patientUnderSecondLineNew);
			 patientsIdsActive = camerwaService.getActivePatients(allpatientsIds, patientsExitedFromCareList,dateFormatedNew);
		}else if(KeyPatients.equals("pediatricSecondLineLast7")){
			
			 List<Integer> allpatientsIds = (List<Integer>) camerwaService.getPediatricUnderSecondLineRegimenLast(dateFormatedNew,dateFormatedLimite,628,797,794,null);
			patientsIdsActive = camerwaService.getActivePatients(allpatientsIds, patientsExitedFromCareList,dateFormatedNew);
		}else if(KeyPatients.equals("pediatricSecondLineNew7")){
			 List<Integer> allpatientsIds = (List<Integer>) camerwaService.getPediatricUnderSecondLineRegimenNew(dateFormatedNew,dateFormatedLimite, 628,797,802,794).get(0);
			patientsIdsActive = camerwaService.getActivePatients(allpatientsIds, patientsExitedFromCareList,dateFormatedNew);
		}else if(KeyPatients.equals("TotalPediatricSecondLine7")){
			
			List<Integer> patientUnderSecondLineLast = (List<Integer>) camerwaService.getPediatricUnderSecondLineRegimenLast(dateFormatedNew,dateFormatedLimite,628,797,802,794);
			
			List<Integer> patientUnderSecondLineNew = (List<Integer>) camerwaService.getPediatricUnderSecondLineRegimenNew(dateFormatedNew,dateFormatedLimite, 628,797,802,794).get(0);
			
			 List<Integer> allpatientsIds = (List<Integer>) camerwaService.union(patientUnderSecondLineLast, patientUnderSecondLineNew);
			 patientsIdsActive = camerwaService.getActivePatients(allpatientsIds, patientsExitedFromCareList,dateFormatedNew);
		}else if(KeyPatients.equals("adultSecondLineLast1")){
			
			List<Integer> allpatientsIds = (List<Integer>) camerwaService.getAdultUnderSecondLineRegimenLast(dateFormatedNew,dateFormatedLimite,628,797,802,794);
			patientsIdsActive = camerwaService.getActivePatients(allpatientsIds, patientsExitedFromCareList,dateFormatedNew);
		}else if(KeyPatients.equals("adultSecondLineNew1")){
			List<Integer> allpatientsIds = (List<Integer>) camerwaService.getAdultUnderSecondLineRegimenNew(dateFormatedNew,dateFormatedLimite,628,797,802,794).get(0);
			patientsIdsActive = camerwaService.getActivePatients(allpatientsIds, patientsExitedFromCareList,dateFormatedNew);
		}else if(KeyPatients.equals("TotalAdultSecondLine1")){
			
			List<Integer> patientUnderSecondLineLast = (List<Integer>) camerwaService.getAdultUnderSecondLineRegimenLast(dateFormatedNew,dateFormatedLimite,628,797,802,794);
			
			List<Integer> patientUnderSecondLineNew = (List<Integer>) camerwaService.getAdultUnderSecondLineRegimenNew(dateFormatedNew,dateFormatedLimite,628,797,802,794).get(0);
			
			List<Integer> allpatientsIds = (List<Integer>) camerwaService.union(patientUnderSecondLineLast, patientUnderSecondLineNew);
			patientsIdsActive = camerwaService.getActivePatients(allpatientsIds, patientsExitedFromCareList,dateFormatedNew);
			
		}else if(KeyPatients.equals("adultSecondLineLast2")){
			
			List<Integer> allpatientsIds = (List<Integer>) camerwaService.getAdultUnderSecondLineRegimenLast(dateFormatedNew,dateFormatedLimite,628,802,794,null);
			patientsIdsActive = camerwaService.getActivePatients(allpatientsIds, patientsExitedFromCareList,dateFormatedNew);
		}else if(KeyPatients.equals("adultSecondLineNew2")){
			List<Integer> allpatientsIds = (List<Integer>) camerwaService.getAdultUnderSecondLineRegimenNew(dateFormatedNew,dateFormatedLimite,628,802,794,null).get(0);
			patientsIdsActive = camerwaService.getActivePatients(allpatientsIds, patientsExitedFromCareList,dateFormatedNew);
		}else if(KeyPatients.equals("TotalAdultSecondLine2")){
			
			List<Integer> patientUnderSecondLineLast = (List<Integer>) camerwaService.getAdultUnderSecondLineRegimenLast(dateFormatedNew,dateFormatedLimite,628,802,794,null);
			
			List<Integer> patientUnderSecondLineNew = (List<Integer>) camerwaService.getAdultUnderSecondLineRegimenNew(dateFormatedNew,dateFormatedLimite,628,802,794,null).get(0);
			
			List<Integer> allpatientsIds = (List<Integer>) camerwaService.union(patientUnderSecondLineLast, patientUnderSecondLineNew);
			patientsIdsActive = camerwaService.getActivePatients(allpatientsIds, patientsExitedFromCareList,dateFormatedNew);
			
		}else if(KeyPatients.equals("adultSecondLineLast3")){
			
			List<Integer> allpatientsIds = (List<Integer>) camerwaService.getAdultUnderSecondLineRegimenLast(dateFormatedNew,dateFormatedLimite,628,797,794,null);
			patientsIdsActive = camerwaService.getActivePatients(allpatientsIds, patientsExitedFromCareList,dateFormatedNew);
		}else if(KeyPatients.equals("adultSecondLineNew3")){
			List<Integer> allpatientsIds = (List<Integer>) camerwaService.getAdultUnderSecondLineRegimenNew(dateFormatedNew,dateFormatedLimite,628,797,794,null).get(0);
			patientsIdsActive = camerwaService.getActivePatients(allpatientsIds, patientsExitedFromCareList,dateFormatedNew);
		}else if(KeyPatients.equals("TotalAdultSecondLine3")){
			
			List<Integer> patientUnderSecondLineLast = (List<Integer>) camerwaService.getAdultUnderSecondLineRegimenLast(dateFormatedNew,dateFormatedLimite,628,797,794,null);
			
			List<Integer> patientUnderSecondLineNew = (List<Integer>) camerwaService.getAdultUnderSecondLineRegimenNew(dateFormatedNew,dateFormatedLimite,628,797,794,null).get(0);
			
			List<Integer> allpatientsIds = (List<Integer>) camerwaService.union(patientUnderSecondLineLast, patientUnderSecondLineNew);
			patientsIdsActive = camerwaService.getActivePatients(allpatientsIds, patientsExitedFromCareList,dateFormatedNew);
			
		}else if(KeyPatients.equals("adultSecondLineLast4")){
			
			List<Integer> allpatientsIds = (List<Integer>) camerwaService.getAdultUnderSecondLineRegimenLast(dateFormatedNew,dateFormatedLimite,628,814,794,null);
			patientsIdsActive = camerwaService.getActivePatients(allpatientsIds, patientsExitedFromCareList,dateFormatedNew);
		}else if(KeyPatients.equals("adultSecondLineNew4")){
			List<Integer> allpatientsIds = (List<Integer>) camerwaService.getAdultUnderSecondLineRegimenNew(dateFormatedNew,dateFormatedLimite,628,814,794,null).get(0);
			patientsIdsActive = camerwaService.getActivePatients(allpatientsIds, patientsExitedFromCareList,dateFormatedNew);
		}else if(KeyPatients.equals("TotalAdultSecondLine4")){
			
			List<Integer> patientUnderSecondLineLast = (List<Integer>) camerwaService.getAdultUnderSecondLineRegimenLast(dateFormatedNew,dateFormatedLimite,628,814,794,null);
			
			List<Integer> patientUnderSecondLineNew = (List<Integer>) camerwaService.getAdultUnderSecondLineRegimenNew(dateFormatedNew,dateFormatedLimite,628,814,794,null).get(0);
			
			List<Integer> allpatientsIds = (List<Integer>) camerwaService.union(patientUnderSecondLineLast, patientUnderSecondLineNew);
			patientsIdsActive = camerwaService.getActivePatients(allpatientsIds, patientsExitedFromCareList,dateFormatedNew);
			
		}else if(KeyPatients.equals("adultSecondLineLast5")){
			
			List<Integer> allpatientsIds = (List<Integer>) camerwaService.getAdultUnderSecondLineRegimenLast(dateFormatedNew,dateFormatedLimite,628,625,794,null);
			patientsIdsActive = camerwaService.getActivePatients(allpatientsIds, patientsExitedFromCareList,dateFormatedNew);
		}else if(KeyPatients.equals("adultSecondLineNew5")){
			List<Integer> allpatientsIds = (List<Integer>) camerwaService.getAdultUnderSecondLineRegimenNew(dateFormatedNew,dateFormatedLimite,628,625,794,null).get(0);
			patientsIdsActive = camerwaService.getActivePatients(allpatientsIds, patientsExitedFromCareList,dateFormatedNew);
		}else if(KeyPatients.equals("TotalAdultSecondLine5")){
			
			List<Integer> patientUnderSecondLineLast = (List<Integer>) camerwaService.getAdultUnderSecondLineRegimenLast(dateFormatedNew,dateFormatedLimite,628,625,794,null);
			
			List<Integer> patientUnderSecondLineNew = (List<Integer>) camerwaService.getAdultUnderSecondLineRegimenNew(dateFormatedNew,dateFormatedLimite,628,625,794,null).get(0);
			
			List<Integer> allpatientsIds = (List<Integer>) camerwaService.union(patientUnderSecondLineLast, patientUnderSecondLineNew);
			patientsIdsActive = camerwaService.getActivePatients(allpatientsIds, patientsExitedFromCareList,dateFormatedNew);
			
		}else if(KeyPatients.equals("adultSecondLineLast6")){
			
			List<Integer> allpatientsIds = (List<Integer>) camerwaService.getAdultUnderSecondLineRegimenLast(dateFormatedNew,dateFormatedLimite,628,796,794,null);
			patientsIdsActive = camerwaService.getActivePatients(allpatientsIds, patientsExitedFromCareList,dateFormatedNew);
		}else if(KeyPatients.equals("adultSecondLineNew6")){
			List<Integer> allpatientsIds = (List<Integer>) camerwaService.getAdultUnderSecondLineRegimenNew(dateFormatedNew,dateFormatedLimite,628,796,794,null).get(0);
			patientsIdsActive = camerwaService.getActivePatients(allpatientsIds, patientsExitedFromCareList,dateFormatedNew);
		}else if(KeyPatients.equals("TotalAdultSecondLine6")){
			
			List<Integer> patientUnderSecondLineLast = (List<Integer>) camerwaService.getAdultUnderSecondLineRegimenLast(dateFormatedNew,dateFormatedLimite,628,796,794,null);
			
			List<Integer> patientUnderSecondLineNew = (List<Integer>) camerwaService.getAdultUnderSecondLineRegimenNew(dateFormatedNew,dateFormatedLimite,628,796,794,null).get(0);
			
			List<Integer> allpatientsIds = (List<Integer>) camerwaService.union(patientUnderSecondLineLast, patientUnderSecondLineNew);
			patientsIdsActive = camerwaService.getActivePatients(allpatientsIds, patientsExitedFromCareList,dateFormatedNew);
			
		}else if(KeyPatients.equals("adultSecondLineLast7")){
			
			List<Integer> allpatientsIds = (List<Integer>) camerwaService.getAdultUnderSecondLineRegimenLast(dateFormatedNew,dateFormatedLimite,814,796,794,null);
			patientsIdsActive = camerwaService.getActivePatients(allpatientsIds, patientsExitedFromCareList,dateFormatedNew);
		}else if(KeyPatients.equals("adultSecondLineNew7")){
			List<Integer> allpatientsIds = (List<Integer>) camerwaService.getAdultUnderSecondLineRegimenNew(dateFormatedNew,dateFormatedLimite,814,796,794,null).get(0);
			patientsIdsActive = camerwaService.getActivePatients(allpatientsIds, patientsExitedFromCareList,dateFormatedNew);
		}else if(KeyPatients.equals("TotalAdultSecondLine7")){
			
			List<Integer> patientUnderSecondLineLast = (List<Integer>) camerwaService.getAdultUnderSecondLineRegimenLast(dateFormatedNew,dateFormatedLimite,814,796,794,null);
			
			List<Integer> patientUnderSecondLineNew = (List<Integer>) camerwaService.getAdultUnderSecondLineRegimenNew(dateFormatedNew,dateFormatedLimite,814,796,794,null).get(0);
			
			List<Integer> allpatientsIds = (List<Integer>) camerwaService.union(patientUnderSecondLineLast, patientUnderSecondLineNew);
			patientsIdsActive = camerwaService.getActivePatients(allpatientsIds, patientsExitedFromCareList,dateFormatedNew);
			
		}else if(KeyPatients.equals("adultSecondLineLast8")){
			
			List<Integer> allpatientsIds = (List<Integer>) camerwaService.getAdultUnderSecondLineRegimenLast(dateFormatedNew,dateFormatedLimite,802,814,794,null);
			patientsIdsActive = camerwaService.getActivePatients(allpatientsIds, patientsExitedFromCareList,dateFormatedNew);
		}else if(KeyPatients.equals("adultSecondLineNew8")){
			List<Integer> allpatientsIds = (List<Integer>) camerwaService.getAdultUnderSecondLineRegimenNew(dateFormatedNew,dateFormatedLimite,802,814,794,null).get(0);
			patientsIdsActive = camerwaService.getActivePatients(allpatientsIds, patientsExitedFromCareList,dateFormatedNew);
		}else if(KeyPatients.equals("TotalAdultSecondLine8")){
			
			List<Integer> patientUnderSecondLineLast = (List<Integer>) camerwaService.getAdultUnderSecondLineRegimenLast(dateFormatedNew,dateFormatedLimite,802,814,794,null);
			
			List<Integer> patientUnderSecondLineNew = (List<Integer>) camerwaService.getAdultUnderSecondLineRegimenNew(dateFormatedNew,dateFormatedLimite,802,814,794,null).get(0);
			
			List<Integer> allpatientsIds = (List<Integer>) camerwaService.union(patientUnderSecondLineLast, patientUnderSecondLineNew);
			patientsIdsActive = camerwaService.getActivePatients(allpatientsIds, patientsExitedFromCareList,dateFormatedNew);
			
		}else if(KeyPatients.equals("adultSecondLineLast9")){
			
			List<Integer> allpatientsIds = (List<Integer>) camerwaService.getAdultUnderSecondLineRegimenLast(dateFormatedNew,dateFormatedLimite,625,814,794,null);
			patientsIdsActive = camerwaService.getActivePatients(allpatientsIds, patientsExitedFromCareList,dateFormatedNew);
		}else if(KeyPatients.equals("adultSecondLineNew9")){
			List<Integer> allpatientsIds = (List<Integer>) camerwaService.getAdultUnderSecondLineRegimenNew(dateFormatedNew,dateFormatedLimite,625,814,794,null).get(0);
			patientsIdsActive = camerwaService.getActivePatients(allpatientsIds, patientsExitedFromCareList,dateFormatedNew);
		}else if(KeyPatients.equals("TotalAdultSecondLine9")){
			
			List<Integer> patientUnderSecondLineLast = (List<Integer>) camerwaService.getAdultUnderSecondLineRegimenLast(dateFormatedNew,dateFormatedLimite,625,814,794,null);
			
			List<Integer> patientUnderSecondLineNew = (List<Integer>) camerwaService.getAdultUnderSecondLineRegimenNew(dateFormatedNew,dateFormatedLimite,625,814,794,null).get(0);
			
			List<Integer> allpatientsIds = (List<Integer>) camerwaService.union(patientUnderSecondLineLast, patientUnderSecondLineNew);
			patientsIdsActive = camerwaService.getActivePatients(allpatientsIds, patientsExitedFromCareList,dateFormatedNew);
			
		}else if(KeyPatients.equals("adultSecondLineLast10")){
			
			List<Integer> allpatientsIds = (List<Integer>) camerwaService.getAdultUnderSecondLineRegimenLast(dateFormatedNew,dateFormatedLimite,802,796,794,null);
			patientsIdsActive = camerwaService.getActivePatients(allpatientsIds, patientsExitedFromCareList,dateFormatedNew);
		}else if(KeyPatients.equals("adultSecondLineNew10")){
			List<Integer> allpatientsIds = (List<Integer>) camerwaService.getAdultUnderSecondLineRegimenNew(dateFormatedNew,dateFormatedLimite,802,796,794,null).get(0);
			patientsIdsActive = camerwaService.getActivePatients(allpatientsIds, patientsExitedFromCareList,dateFormatedNew);
		}else if(KeyPatients.equals("TotalAdultSecondLine10")){
			
			List<Integer> patientUnderSecondLineLast = (List<Integer>) camerwaService.getAdultUnderSecondLineRegimenLast(dateFormatedNew,dateFormatedLimite,802,796,794,null);
			
			List<Integer> patientUnderSecondLineNew = (List<Integer>) camerwaService.getAdultUnderSecondLineRegimenNew(dateFormatedNew,dateFormatedLimite,802,796,794,null).get(0);
			
			List<Integer> allpatientsIds = (List<Integer>) camerwaService.union(patientUnderSecondLineLast, patientUnderSecondLineNew);
			patientsIdsActive = camerwaService.getActivePatients(allpatientsIds, patientsExitedFromCareList,dateFormatedNew);
		  
		  
		}else if(KeyPatients.equals("prophylaxiePostExpositionNew1")){
			
			List<Integer> allpatientsIds = (List<Integer>) camerwaService.getPatientsUnderProphylaxieNew(dateFormatedNew,dateFormatedLimite,628,797,794,null).get(0);
			patientsIdsActive = camerwaService.getActivePatients(allpatientsIds, patientsExitedFromCareList,dateFormatedNew);
		  
		  
		}else if(KeyPatients.equals("prophylaxiePostExpositionLast1")){
			
			List<Integer> allpatientsIds = (List<Integer>) camerwaService.getPatientsUnderProphylaxieLast(dateFormatedNew,dateFormatedLimite,628,797,794,null);
			patientsIdsActive = camerwaService.getActivePatients(allpatientsIds, patientsExitedFromCareList,dateFormatedNew);
		  
		  
		}else if(KeyPatients.equals("TotalProphylaxiePostExposition1")){
			
			List<Integer> patientsIdLast = (List<Integer>) camerwaService.getPatientsUnderProphylaxieLast(dateFormatedNew,dateFormatedLimite,628,797,794,null);
			
			List<Integer> patientsIdNew = (List<Integer>) camerwaService.getPatientsUnderProphylaxieNew(dateFormatedNew,dateFormatedLimite,628,797,794,null).get(0);
			
			List<Integer> allpatientsIds = (List<Integer>) camerwaService.union(patientsIdLast, patientsIdNew);
			patientsIdsActive = camerwaService.getActivePatients(allpatientsIds, patientsExitedFromCareList,dateFormatedNew);
		  
		  
		}else if(KeyPatients.equals("prophylaxiePostExpositionNew2")){
			
			List<Integer> allpatientsIds = (List<Integer>) camerwaService.getPatientsUnderProphylaxieNew(dateFormatedNew,dateFormatedLimite,630,633,null,null).get(0);
			patientsIdsActive = camerwaService.getActivePatients(allpatientsIds, patientsExitedFromCareList,dateFormatedNew);
		  
		  
		}else if(KeyPatients.equals("prophylaxiePostExpositionLast2")){
			
			List<Integer> allpatientsIds = (List<Integer>) camerwaService.getPatientsUnderProphylaxieLast(dateFormatedNew,dateFormatedLimite,630,633,null,null);
			patientsIdsActive = camerwaService.getActivePatients(allpatientsIds, patientsExitedFromCareList,dateFormatedNew);
		  
		  
		}else if(KeyPatients.equals("TotalProphylaxiePostExposition2")){
			
			List<Integer> patientsIdLast = (List<Integer>) camerwaService.getPatientsUnderProphylaxieLast(dateFormatedNew,dateFormatedLimite,630,633,null,null);
			
			List<Integer> patientsIdNew = (List<Integer>) camerwaService.getPatientsUnderProphylaxieNew(dateFormatedNew,dateFormatedLimite,630,633,null,null).get(0);
			
			List<Integer> allpatientsIds = (List<Integer>) camerwaService.union(patientsIdLast, patientsIdNew);
			patientsIdsActive = camerwaService.getActivePatients(allpatientsIds, patientsExitedFromCareList,dateFormatedNew);
		  
		  
		}else if(KeyPatients.equals("prophylaxiePostExpositionNew2")){
			
			List<Integer> allpatientsIds = (List<Integer>) camerwaService.getPatientsUnderProphylaxieNew(dateFormatedNew,dateFormatedLimite,630,633,null,null).get(0);
			patientsIdsActive = camerwaService.getActivePatients(allpatientsIds, patientsExitedFromCareList,dateFormatedNew);
		  
		  
		}else if(KeyPatients.equals("prophylaxiePostExpositionLast2")){
			
			List<Integer> allpatientsIds = (List<Integer>) camerwaService.getPatientsUnderProphylaxieLast(dateFormatedNew,dateFormatedLimite,630,633,null,null);
			patientsIdsActive = camerwaService.getActivePatients(allpatientsIds, patientsExitedFromCareList,dateFormatedNew);
		  
		  
		}else if(KeyPatients.equals("TotalProphylaxiePostExposition2")){
			
			List<Integer> patientsIdLast = (List<Integer>) camerwaService.getPatientsUnderProphylaxieLast(dateFormatedNew,dateFormatedLimite,630,633,null,null);
			
			List<Integer> patientsIdNew = (List<Integer>) camerwaService.getPatientsUnderProphylaxieNew(dateFormatedNew,dateFormatedLimite,630,633,null,null).get(0);
			
			List<Integer> allpatientsIds = (List<Integer>) camerwaService.union(patientsIdLast, patientsIdNew);
			patientsIdsActive = camerwaService.getActivePatients(allpatientsIds, patientsExitedFromCareList,dateFormatedNew);
		  
		  
		}else if(KeyPatients.equals("pmtctNew1")){
			
			List<Integer> allpatientsIds = (List<Integer>) camerwaService.getPatientsUnderPmtctNew(dateFormatedNew,dateFormatedLimite, 797,null,null,null).get(0);
			patientsIdsActive = camerwaService.getActivePatients(allpatientsIds, patientsExitedFromCareList,dateFormatedNew);
		  
		}else if(KeyPatients.equals("pmtctLast1")){
			List<Integer> allpatientsIds = (List<Integer>) camerwaService.getPatientsUnderPmtctLast(dateFormatedNew,dateFormatedLimite,797,null,null,null);
			patientsIdsActive = camerwaService.getActivePatients(allpatientsIds, patientsExitedFromCareList,dateFormatedNew);
		  
		}else if(KeyPatients.equals("TotalPmtct1")){
			
			List<Integer> patientsIdLast = (List<Integer>) camerwaService.getPatientsUnderPmtctLast(dateFormatedNew,dateFormatedLimite,797,null,null,null);
			List<Integer> patientsIdNew = (List<Integer>) camerwaService.getPatientsUnderPmtctNew(dateFormatedNew,dateFormatedLimite, 797,null,null,null).get(0);
			List<Integer> allpatientsIds = (List<Integer>) camerwaService.union(patientsIdLast, patientsIdNew);
			patientsIdsActive = camerwaService.getActivePatients(allpatientsIds, patientsExitedFromCareList,dateFormatedNew);
		}else if(KeyPatients.equals("pmtctNew2")){
			
			List<Integer> allpatientsIds = (List<Integer>) camerwaService.getPatientsUnderPmtctNew(dateFormatedNew,dateFormatedLimite, 802,628,631,null).get(0);
			patientsIdsActive = camerwaService.getActivePatients(allpatientsIds, patientsExitedFromCareList,dateFormatedNew);
		  
		}else if(KeyPatients.equals("pmtctLast2")){
			List<Integer> allpatientsIds = (List<Integer>) camerwaService.getPatientsUnderPmtctLast(dateFormatedNew,dateFormatedLimite,802,628,631,null);
			patientsIdsActive = camerwaService.getActivePatients(allpatientsIds, patientsExitedFromCareList,dateFormatedNew);
		  
		}else if(KeyPatients.equals("TotalPmtct2")){
			
			List<Integer> patientsIdLast = (List<Integer>) camerwaService.getPatientsUnderPmtctLast(dateFormatedNew,dateFormatedLimite,802,628,631,null);
			List<Integer> patientsIdNew = (List<Integer>) camerwaService.getPatientsUnderPmtctNew(dateFormatedNew,dateFormatedLimite, 802,628,631,null).get(0);
			List<Integer> allpatientsIds = (List<Integer>) camerwaService.union(patientsIdLast, patientsIdNew);
			patientsIdsActive = camerwaService.getActivePatients(allpatientsIds, patientsExitedFromCareList,dateFormatedNew);
		}else if(KeyPatients.equals("pmtctNew3")){
			
			List<Integer> allpatientsIds = (List<Integer>) camerwaService.getPatientsUnderPmtctNew(dateFormatedNew,dateFormatedLimite, 797,null,null,null).get(0);
			patientsIdsActive = camerwaService.getActivePatients(allpatientsIds, patientsExitedFromCareList,dateFormatedNew);
		  
		}else if(KeyPatients.equals("pmtctLast3")){
			List<Integer> allpatientsIds = (List<Integer>) camerwaService.getPatientsUnderPmtctLast(dateFormatedNew,dateFormatedLimite,797,null,null,null);
			patientsIdsActive = camerwaService.getActivePatients(allpatientsIds, patientsExitedFromCareList,dateFormatedNew);
		  
		}else if(KeyPatients.equals("TotalPmtct3")){
			
			List<Integer> patientsIdLast = (List<Integer>) camerwaService.getPatientsUnderPmtctLast(dateFormatedNew,dateFormatedLimite,797,null,null,null);
			List<Integer> patientsIdNew = (List<Integer>) camerwaService.getPatientsUnderPmtctNew(dateFormatedNew,dateFormatedLimite, 797,null,null,null).get(0);
			List<Integer> allpatientsIds = (List<Integer>) camerwaService.union(patientsIdLast, patientsIdNew);
			patientsIdsActive = camerwaService.getActivePatients(allpatientsIds, patientsExitedFromCareList,dateFormatedNew);
		}else if(KeyPatients.equals("pmtctNew4")){
			
			List<Integer> allpatientsIds = (List<Integer>) camerwaService.getPatientsUnderPmtctNew(dateFormatedNew,dateFormatedLimite, 797,628,631,null).get(0);
			patientsIdsActive = camerwaService.getActivePatients(allpatientsIds, patientsExitedFromCareList,dateFormatedNew);
		  
		}else if(KeyPatients.equals("pmtctLast4")){
			List<Integer> allpatientsIds = (List<Integer>) camerwaService.getPatientsUnderPmtctLast(dateFormatedNew,dateFormatedLimite,797,628,631,null);
			patientsIdsActive = camerwaService.getActivePatients(allpatientsIds, patientsExitedFromCareList,dateFormatedNew);
		  
		}else if(KeyPatients.equals("TotalPmtct4")){
			
			List<Integer> patientsIdLast = (List<Integer>) camerwaService.getPatientsUnderPmtctLast(dateFormatedNew,dateFormatedLimite,797,628,631,null);
			List<Integer> patientsIdNew = (List<Integer>) camerwaService.getPatientsUnderPmtctNew(dateFormatedNew,dateFormatedLimite, 797,628,631,null).get(0);
			List<Integer> allpatientsIds = (List<Integer>) camerwaService.union(patientsIdLast, patientsIdNew);
			patientsIdsActive = camerwaService.getActivePatients(allpatientsIds, patientsExitedFromCareList,dateFormatedNew);
		}else if(KeyPatients.equals("pmtctNew5")){
			
			List<Integer> allpatientsIds = (List<Integer>) camerwaService.getPatientsUnderPmtctNew(dateFormatedNew,dateFormatedLimite,625,628,631,null).get(0);
			patientsIdsActive = camerwaService.getActivePatients(allpatientsIds, patientsExitedFromCareList,dateFormatedNew);
		  
		}else if(KeyPatients.equals("pmtctLast5")){
			List<Integer> allpatientsIds = (List<Integer>) camerwaService.getPatientsUnderPmtctLast(dateFormatedNew,dateFormatedLimite,625,628,631,null);
			patientsIdsActive = camerwaService.getActivePatients(allpatientsIds, patientsExitedFromCareList,dateFormatedNew);
		  
		}else if(KeyPatients.equals("TotalPmtct5")){
			
			List<Integer> patientsIdLast = (List<Integer>) camerwaService.getPatientsUnderPmtctLast(dateFormatedNew,dateFormatedLimite,625,628,631,null);
			List<Integer> patientsIdNew = (List<Integer>) camerwaService.getPatientsUnderPmtctNew(dateFormatedNew,dateFormatedLimite, 625,628,631,null).get(0);
			List<Integer> allpatientsIds = (List<Integer>) camerwaService.union(patientsIdLast, patientsIdNew);
			patientsIdsActive = camerwaService.getActivePatients(allpatientsIds, patientsExitedFromCareList,dateFormatedNew);
		}else if(KeyPatients.equals("pmtctNew6")){
			
			List<Integer> allpatientsIds = (List<Integer>) camerwaService.getPatientsUnderPmtctNew(dateFormatedNew,dateFormatedLimite, 797,628,633,null).get(0);
			patientsIdsActive = camerwaService.getActivePatients(allpatientsIds, patientsExitedFromCareList,dateFormatedNew);
		  
		}else if(KeyPatients.equals("pmtctLast6")){
			List<Integer> allpatientsIds = (List<Integer>) camerwaService.getPatientsUnderPmtctLast(dateFormatedNew,dateFormatedLimite,797,628,633,null);
			patientsIdsActive = camerwaService.getActivePatients(allpatientsIds, patientsExitedFromCareList,dateFormatedNew);
		  
		}else if(KeyPatients.equals("TotalPmtct6")){
			
			List<Integer> patientsIdLast = (List<Integer>) camerwaService.getPatientsUnderPmtctLast(dateFormatedNew,dateFormatedLimite,797,628,633,null);
			List<Integer> patientsIdNew = (List<Integer>) camerwaService.getPatientsUnderPmtctNew(dateFormatedNew,dateFormatedLimite, 797,628,633,null).get(0);
			List<Integer> allpatientsIds = (List<Integer>) camerwaService.union(patientsIdLast, patientsIdNew);
			patientsIdsActive = camerwaService.getActivePatients(allpatientsIds, patientsExitedFromCareList,dateFormatedNew);
		}else if(KeyPatients.equals("PediatricSiropNew1")){
			
			List<Integer> allpatientsIds = (List<Integer>) camerwaService.getPatientsUnderPediatricSiropNew(dateFormatedNew,
			    dateFormatedLimite, 797,628,633,null).get(0);
			patientsIdsActive = camerwaService.getActivePatients(allpatientsIds, patientsExitedFromCareList,dateFormatedNew);
		  
		}else if(KeyPatients.equals("PediatricSiropLast1")){
			List<Integer> allpatientsIds = (List<Integer>) camerwaService.getPatientsUnderPediatricSiropLast(dateFormatedNew,
			    dateFormatedLimite,797,628,633,null);
			patientsIdsActive = camerwaService.getActivePatients(allpatientsIds, patientsExitedFromCareList,dateFormatedNew);
		  
		}else if(KeyPatients.equals("TotalPediatricSirop1")){
			
			List<Integer> patientsIdLast = (List<Integer>) camerwaService.getPatientsUnderPediatricSiropLast(dateFormatedNew,
			    dateFormatedLimite,797,628,633,null);
			List<Integer> patientsIdNew = (List<Integer>)  camerwaService.getPatientsUnderPediatricSiropNew(dateFormatedNew,
			    dateFormatedLimite, 797,628,633,null).get(0);
			List<Integer> allpatientsIds = (List<Integer>) camerwaService.union(patientsIdLast, patientsIdNew);
			patientsIdsActive = camerwaService.getActivePatients(allpatientsIds, patientsExitedFromCareList,dateFormatedNew);
		}else if(KeyPatients.equals("PediatricSiropNew2")){
			
			List<Integer> allpatientsIds = (List<Integer>) camerwaService.getPatientsUnderPediatricSiropNew(dateFormatedNew,
			    dateFormatedLimite, 625,628,633,null).get(0);
			patientsIdsActive = camerwaService.getActivePatients(allpatientsIds, patientsExitedFromCareList,dateFormatedNew);
		  
		}else if(KeyPatients.equals("PediatricSiropLast2")){
			List<Integer> allpatientsIds = (List<Integer>) camerwaService.getPatientsUnderPediatricSiropLast(dateFormatedNew,
			    dateFormatedLimite,625,628,633,null);
			patientsIdsActive = camerwaService.getActivePatients(allpatientsIds, patientsExitedFromCareList,dateFormatedNew);
		  
		}else if(KeyPatients.equals("TotalPediatricSirop2")){
			
			List<Integer> patientsIdLast = (List<Integer>) camerwaService.getPatientsUnderPediatricSiropLast(dateFormatedNew,
			    dateFormatedLimite,625,628,633,null);
			List<Integer> patientsIdNew = (List<Integer>)  camerwaService.getPatientsUnderPediatricSiropNew(dateFormatedNew,
			    dateFormatedLimite, 625,628,633,null).get(0);
			List<Integer> allpatientsIds = (List<Integer>) camerwaService.union(patientsIdLast, patientsIdNew);
			patientsIdsActive = camerwaService.getActivePatients(allpatientsIds, patientsExitedFromCareList,dateFormatedNew);
		}else if(KeyPatients.equals("PediatricSiropNew3")){
			
			List<Integer> allpatientsIds = (List<Integer>) camerwaService.getPatientsUnderPediatricSiropNew(dateFormatedNew,
			    dateFormatedLimite, 797,628,631,null).get(0);
			patientsIdsActive = camerwaService.getActivePatients(allpatientsIds, patientsExitedFromCareList,dateFormatedNew);
		  
		}else if(KeyPatients.equals("PediatricSiropLast3")){
			List<Integer> allpatientsIds = (List<Integer>) camerwaService.getPatientsUnderPediatricSiropLast(dateFormatedNew,
			    dateFormatedLimite,797,628,631,null);
			patientsIdsActive = camerwaService.getActivePatients(allpatientsIds, patientsExitedFromCareList,dateFormatedNew);
		  
		}else if(KeyPatients.equals("TotalPediatricSirop3")){
			
			List<Integer> patientsIdLast = (List<Integer>) camerwaService.getPatientsUnderPediatricSiropLast(dateFormatedNew,
			    dateFormatedLimite,797,628,631,null);
			List<Integer> patientsIdNew = (List<Integer>)  camerwaService.getPatientsUnderPediatricSiropNew(dateFormatedNew,
			    dateFormatedLimite, 797,628,631,null).get(0);
			List<Integer> allpatientsIds = (List<Integer>) camerwaService.union(patientsIdLast, patientsIdNew);
			patientsIdsActive = camerwaService.getActivePatients(allpatientsIds, patientsExitedFromCareList,dateFormatedNew);
		}else if(KeyPatients.equals("PediatricSiropNew4")){
			
			List<Integer> allpatientsIds = (List<Integer>) camerwaService.getPatientsUnderPediatricSiropNew(dateFormatedNew,
			    dateFormatedLimite, 625,628,631,null).get(0);
			patientsIdsActive = camerwaService.getActivePatients(allpatientsIds, patientsExitedFromCareList,dateFormatedNew);
		  
		}else if(KeyPatients.equals("PediatricSiropLast4")){
			List<Integer> allpatientsIds = (List<Integer>) camerwaService.getPatientsUnderPediatricSiropLast(dateFormatedNew,
			    dateFormatedLimite,625,628,631,null);
			patientsIdsActive = camerwaService.getActivePatients(allpatientsIds, patientsExitedFromCareList,dateFormatedNew);
		  
		}else if(KeyPatients.equals("TotalPediatricSirop4")){
			
			List<Integer> patientsIdLast = (List<Integer>) camerwaService.getPatientsUnderPediatricSiropLast(dateFormatedNew,
			    dateFormatedLimite,625,628,631,null);
			List<Integer> patientsIdNew = (List<Integer>)  camerwaService.getPatientsUnderPediatricSiropNew(dateFormatedNew,
			    dateFormatedLimite, 625,628,631,null).get(0);
			List<Integer> allpatientsIds = (List<Integer>) camerwaService.union(patientsIdLast, patientsIdNew);
			patientsIdsActive = camerwaService.getActivePatients(allpatientsIds, patientsExitedFromCareList,dateFormatedNew);
		}else
		
		 if(KeyPatients.equals("patientsWithoutRegimens")){
			HashMap<String, Object> patientsUnderDrug = new HashMap<String, Object>();
			CamerwaService camerwaService = Context.getService(CamerwaService.class);
			
			PatientService camerwaService2 = Context.getService(PatientService.class);
			
			List<Patient>  allPatients = camerwaService2.getAllPatients();
			
			patientsUnderDrug=camerwaService.getPatientsUnderDrug(dateFormatedNew,dateFormatedLimite);
			
			List<Integer> patientsWithoutRegimens = new ArrayList<Integer>();
			
			List<Integer> patientsWithRegimens = (List<Integer>) patientsUnderDrug.get("patientsOnRegimens") ;
			//log.info(" !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1  patientsWithRegimens "+patientsWithRegimens.size());
				for(Patient patient : allPatients){
				if(!patientsWithRegimens.contains(patient.getPatientId()) && !patientsWithoutRegimens.contains(patient.getPatientId())){
					patientsWithoutRegimens.add(patient.getPatientId());
				}
			}
			
				
			patientsIdsActive =camerwaService.removeLostFollowUp((List<Integer>)patientsWithoutRegimens,dateFormatedLimite);
		}
   	
   	
    	return getPatientListByPatientsIds(patientsIdsActive);
		
	}
	
	/*public static Collection union(Collection coll1, Collection coll2)
	{
	    Set union = new HashSet(coll1);
	    union.addAll(new HashSet(coll2));
	    return new ArrayList(union);
	}*/
	
	public List<Patient> getPatientListByPatientsIds(List<Integer> patientsIds){
		List<Patient> patientsList = new ArrayList<Patient>();
		for(int patientId: patientsIds){
			  Patient patient= Context.getPatientService().getPatient(patientId);
			  patientsList.add(patient);		
			}
		return patientsList;
		
	}
	public List<Integer> getPatientArleadyUnderArv(List<Integer> activePatients,Date dateFormatedNew) throws ParseException{
		List<Integer> patientsCurentUnderARVNew = new ArrayList<Integer>(); 
		for(Integer patientId : activePatients){
			if(camerwaService.isAnyArvDrugOld(patientId, dateFormatedNew)){
				
				patientsCurentUnderARVNew.add(patientId);
			}	
		}
		return patientsCurentUnderARVNew;
	}
	
}
