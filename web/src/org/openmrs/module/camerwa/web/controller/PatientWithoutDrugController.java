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
package org.openmrs.module.camerwa.web.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.openmrs.Patient;
import org.openmrs.api.PatientService;
import org.openmrs.api.context.Context;
import org.openmrs.module.camerwa.service.CamerwaService;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;


/**
 *
 */
public class PatientWithoutDrugController extends AbstractController {
	
	 static String msgToDisplay;
	    static Object[] msgArguments;
	    static String selectedLocaion;
		
	    /**
	     * @return the msgToDisplay
	     */
	    public static String getMsgToDisplay() {
	    	return msgToDisplay;
	    }
	    /**
	     * @param msgToDisplay the msgToDisplay to set
	     */
	    public static void setMsgToDisplay(String msgToDisplay) {
	    	CamerwaFormController.msgToDisplay = msgToDisplay;
	    }
	    
	    /**
	     * @param msgToDisplay the msgToDisplay to set
	     */
	    public static void setMsgArguments(String msgToDisplay) {
	    	CamerwaFormController.msgToDisplay = msgToDisplay;
	    }
	    
		private SessionFactory sessionFactory;
		
		/**
		 * @param sessionFactory the sessionFactory to set
		 */
		public void setSessionFactory(SessionFactory sessionFactory) {
			this.sessionFactory = sessionFactory;
		}
		
		public SessionFactory getSessionFactory() {
			return sessionFactory;
		}
		
		/** Logger for this class and subclasses */
		protected final Log log = LogFactory.getLog(getClass());


	
	
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
	/*	
		int numberOfPatients=0;
    	Session session = getSessionFactory().getCurrentSession();
    	ModelAndView mav=new ModelAndView();
    	HashMap<String, Object> patientsUnderDrug = new HashMap<String, Object>();
        
    	DateFormat dateFormatY = new SimpleDateFormat("yyyy");
    	DateFormat dateFormatM = new SimpleDateFormat("MM");
        java.util.Date date = new java.util.Date();
    
    	String curentYear=dateFormatY.format(date); 
    	String curentMonth=dateFormatM.format(date);
    	
    
    	int year = ServletRequestUtils.getIntParameter(request, "year",Integer.parseInt(curentYear) );
        int month = ServletRequestUtils.getIntParameter(request, "month", Integer.parseInt(curentMonth));
        Calendar cal = new GregorianCalendar();
        
        cal.set(GregorianCalendar.YEAR, year);
        cal.set(GregorianCalendar.MONTH, month-1);
        cal.set(GregorianCalendar.DAY_OF_MONTH ,1);
        Date dateFormatedNew = cal.getTime();
        mav.addObject("dateFormatedNew", dateFormatedNew);
        
     
        Calendar cal2 = new GregorianCalendar();
        Date dateFormatedLimite;
        if(month < 12){
        cal2.set(GregorianCalendar.YEAR, year);
        cal2.set(GregorianCalendar.MONTH, month);
        cal2.set(GregorianCalendar.DAY_OF_MONTH,1);
        dateFormatedLimite = cal2.getTime();
        }else {
        	
        	cal2.set(GregorianCalendar.YEAR,year+1);
            cal2.set(GregorianCalendar.MONTH,0);
            cal2.set(GregorianCalendar.DAY_OF_MONTH ,1);
            dateFormatedLimite = cal2.getTime();
         	
        }
		
		  
		CamerwaService camerwaService = Context.getService(CamerwaService.class);
		
		PatientService camerwaService2 = Context.getService(PatientService.class);
		
		List<Patient>  allPatients = camerwaService2.getAllPatients();
		
		patientsUnderDrug=camerwaService.getPatientsUnderDrug(dateFormatedNew,dateFormatedLimite);
		
		
		
		
		List<Integer> patientsWithoutRegimens = new ArrayList<Integer>();
		List<Integer> patientsWithRegimens = (List<Integer>) patientsUnderDrug.get("patientsOnRegimens") ;//  new ArrayList<Integer>();
		//log.info("  %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% "+patientsWithRegimens);
		for(Patient patient : allPatients){
			if(!patientsWithRegimens.contains(patient.getPatientId()) && !patientsWithoutRegimens.contains(patient.getPatientId())){
				patientsWithoutRegimens.add(patient.getPatientId());
			}
		}
		
		//log.info(" **********************************************************"+patientsWithoutRegimens);
		
*/		
		
		
		

    	String exportInToExcel = ServletRequestUtils.getStringParameter(request, "exportInToExcel",null);
    	
    	int numberOfPatients=0;
    	Session session = getSessionFactory().getCurrentSession();
    	ModelAndView mav=new ModelAndView();
    	HashMap<String, Object> patientsUnderDrug = new HashMap<String, Object>();
        
    	DateFormat dateFormatY = new SimpleDateFormat("yyyy");
    	DateFormat dateFormatM = new SimpleDateFormat("MM");
        java.util.Date date = new java.util.Date();
    
    	String curentYear=dateFormatY.format(date); 
    	String curentMonth=dateFormatM.format(date);
    	
    	//log.info("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!selected location !!!!!!!!!!!!!!!!!!"+);
    	int year = ServletRequestUtils.getIntParameter(request, "year",Integer.parseInt(curentYear) );
        int month = ServletRequestUtils.getIntParameter(request, "month", Integer.parseInt(curentMonth));
        Calendar cal = new GregorianCalendar();
        
        cal.set(GregorianCalendar.YEAR, year);
        cal.set(GregorianCalendar.MONTH, month-1);
        cal.set(GregorianCalendar.DAY_OF_MONTH ,1);
        Date dateFormatedNew = cal.getTime();
        mav.addObject("dateFormatedNew", dateFormatedNew);
        
     
        Calendar cal2 = new GregorianCalendar();
        Date dateFormatedLimite;
        if(month < 12){
        cal2.set(GregorianCalendar.YEAR, year);
        cal2.set(GregorianCalendar.MONTH, month);
        cal2.set(GregorianCalendar.DAY_OF_MONTH,1);
        dateFormatedLimite = cal2.getTime();
        }else {
        	
        	cal2.set(GregorianCalendar.YEAR,year+1);
            cal2.set(GregorianCalendar.MONTH,0);
            cal2.set(GregorianCalendar.DAY_OF_MONTH ,1);
          dateFormatedLimite = cal2.getTime();
        	
        }
        
        
        mav.addObject("dateFormatedLimite", dateFormatedLimite);
      
        CamerwaService camerwaService = Context.getService(CamerwaService.class);
        patientsUnderDrug=camerwaService.getPatientsUnderDrug(dateFormatedNew,dateFormatedLimite);
        
     // getting all locations
        mav.addObject("locations", camerwaService.getLocations());
            
        mav.addObject("patientsUnderDrug",patientsUnderDrug);
       
        mav.addObject("year", year);
        mav.addObject("month", month);
      // get selected location
        selectedLocaion = request.getParameter("oneLocation");
        //log.info("(((((((((((((((((((((((((((((((((("+exportInToExcel+"((((((((((((((((((((((((( this is the selected location (((((((((("+selectedLocaion);
        mav.addObject("selectedLocaion",selectedLocaion);
        mav.addObject("patientsUnderARVNew",patientsUnderDrug.get("patientsUnderARVNew"));   
        mav.addObject("LastPatientsUnderARV",patientsUnderDrug.get("LastPatientsUnderARV"));
        mav.addObject("adultsUnderARVNew",patientsUnderDrug.get("adultsUnderARVNew"));
        mav.addObject("LastAdultsUnderARV",patientsUnderDrug.get("LastAdultsUnderARV"));   
        mav.addObject("kidsDOBQueryNew",patientsUnderDrug.get("kidsDOBQueryNew"));
        mav.addObject("LastkidsDOBQuery",patientsUnderDrug.get("LastkidsDOBQuery"));
          
        for(int i=1;i<17;i++){
        mav.addObject("adultRegimenNew"+i,patientsUnderDrug.get("adultRegimenNew"+i));
        mav.addObject("adultRegimenLast"+i,patientsUnderDrug.get("adultRegimenLast"+i));
        mav.addObject("TotalAdulRegimen"+i,patientsUnderDrug.get("TotalAdulRegimen"+i));
        
        }
        mav.addObject("sumOfTatals",patientsUnderDrug.get("sumOfTatals"));
        mav.addObject("sumOfAdultRegimenNew",patientsUnderDrug.get("sumOfAdultRegimenNew"));
        mav.addObject("sumOfAdultRegimenLast",patientsUnderDrug.get("sumOfAdultRegimenLast"));
        mav.addObject("activekidsUnderArvComprimeNew",patientsUnderDrug.get("activekidsUnderArvComprimeNew"));
        mav.addObject("allActiveKidsUnderArvComprime",patientsUnderDrug.get("allActiveKidsUnderArvComprime"));
        mav.addObject("activekidsUnderArvSiropsNew",patientsUnderDrug.get("activekidsUnderArvSiropsNew"));
        mav.addObject("allActiveKidsUnderArvSiropsLast",patientsUnderDrug.get("allActiveKidsUnderArvSiropsLast"));
        
        mav.addObject("allPatientsUnderARV",patientsUnderDrug.get("TotalPatientsUnderARV"));
        mav.addObject("allAdultDOBQuery",patientsUnderDrug.get("TotalAdultsUnderARV"));
        mav.addObject("allKidsDOBQuery",patientsUnderDrug.get("TotalkidsDOBQuery"));
 
        mav.addObject("TotalKidsUnderArvComprime",patientsUnderDrug.get("TotalKidsUnderArvComprime"));
        mav.addObject("TotalKidsUnderArvSirops",patientsUnderDrug.get("TotalKidsUnderArvSirops"));
        mav.addObject("patientsUnderArvMap",patientsUnderDrug);
        
        int collectionsize= (Integer) patientsUnderDrug.get("pediatricSize");
        Map<String, Map<String, Object>> allPediatriCollection = new HashMap<String, Map<String,Object>>();
        
        
        
        for(int i=1 ; i<=collectionsize;i++){
        	
        	
            mav.addObject("pediatricRegimenNew"+i,patientsUnderDrug.get("pediatricRegimenNew"+i));
            mav.addObject("pediatricRegimenLast"+i,patientsUnderDrug.get("pediatricRegimenLast"+i));
            mav.addObject("TotalPediatricRegimen"+i,patientsUnderDrug.get("TotalPediatricRegimen"+i));
            Map<String, Object> singlePediatric= new HashMap<String, Object>();
           
            singlePediatric.put("pediatricRegimenNew"+i,patientsUnderDrug.get("pediatricRegimenNew"+i));
            singlePediatric.put("pediatricRegimenLast"+i,patientsUnderDrug.get("pediatricRegimenLast"+i));
            singlePediatric.put("TotalPediatricRegimen"+i,patientsUnderDrug.get("TotalPediatricRegimen"+i));
            
            allPediatriCollection.put("singlePediatric"+i, singlePediatric);
            }
            
            mav.addObject("allPediatriCollection",allPediatriCollection);
          
            mav.addObject("sumOfPediatricTatals",patientsUnderDrug.get("sumOfPediatricTatals"));
            mav.addObject("sumOfPediatricRegimenNew",patientsUnderDrug.get("sumOfPediatricRegimenNew"));
            mav.addObject("sumOfPediatricRegimenLast",patientsUnderDrug.get("sumOfPediatricRegimenLast"));
            
            int exitedPatientsCollectionSize=(Integer) patientsUnderDrug.get("exitedPatientsCollectionSize");
            
            for (int i=1;i<=exitedPatientsCollectionSize;i++){
            
               mav.addObject("patientsExitedLast"+i,patientsUnderDrug.get("patientsExitedLast"+i));	
               mav.addObject("patientsExitedNew"+i,patientsUnderDrug.get("patientsExitedNew"+i));
               mav.addObject("TotalpatientsExitedFromCare"+i,patientsUnderDrug.get("TotalpatientsExitedFromCare"+i));
            }
            
            mav.addObject("sumOfSecondLineNew",patientsUnderDrug.get("sumOfSecondLineNew"));
            mav.addObject("sumOfSecondLineLast",patientsUnderDrug.get("sumOfSecondLineLast"));
            mav.addObject("sumOfSecondLineTatals",patientsUnderDrug.get("sumOfSecondLineTatals"));
            
            int pediatricRegimenSecondLineSize = (Integer)patientsUnderDrug.get("pediatricRegimenSecondLineSize");
            
          
            
            for (int i=1;i<=pediatricRegimenSecondLineSize;i++){
                   
              mav.addObject("pediatricSecondLineLast"+i,patientsUnderDrug.get("pediatricSecondLineLast"+i));	
              mav.addObject("pediatricSecondLineNew"+i,patientsUnderDrug.get("pediatricSecondLineNew"+i));
              mav.addObject("TotalPediatricSecondLine"+i,patientsUnderDrug.get("TotalPediatricSecondLine"+i));
           
            }
            
            
            mav.addObject("sumOfAdultSecondLineNew",patientsUnderDrug.get("sumOfAdultSecondLineNew"));
            mav.addObject("sumOfAdultSecondLineLast",patientsUnderDrug.get("sumOfAdultSecondLineLast"));
            mav.addObject("sumOfAdultSecondLineTatals",patientsUnderDrug.get("sumOfAdultSecondLineTatals"));
            
            int adultRegimenSecondLineSize =(Integer) patientsUnderDrug.get("adultRegimenSecondLineSize");
            
            // Map<String, Map<String, Integer>> allSecondLineCollection = new HashMap<String, Map<String,Integer>>();
            
            for (int i=1;i<=adultRegimenSecondLineSize;i++){
                   
              mav.addObject("adultSecondLineLast"+i,patientsUnderDrug.get("adultSecondLineLast"+i));	
              mav.addObject("adultSecondLineNew"+i,patientsUnderDrug.get("adultSecondLineNew"+i));
              mav.addObject("TotalAdultSecondLine"+i,patientsUnderDrug.get("TotalAdultSecondLine"+i));
             
                
            }
         
            
            int prophylaxiePostExpositionSize = (Integer) patientsUnderDrug.get("prophylaxiePostExpositionSize");
            
            for (int i=1;i<=prophylaxiePostExpositionSize;i++){
                
                mav.addObject("prophylaxiePostExpositionLast"+i,patientsUnderDrug.get("prophylaxiePostExpositionLast"+i));	
                mav.addObject("prophylaxiePostExpositionNew"+i,patientsUnderDrug.get("prophylaxiePostExpositionNew"+i));
                mav.addObject("TotalProphylaxiePostExposition"+i,patientsUnderDrug.get("TotalProphylaxiePostExposition"+i));
               
                  
              }
            mav.addObject("sumOfProphylaxiePostExpositionNew",patientsUnderDrug.get("sumOfProphylaxiePostExpositionNew"));
            mav.addObject("sumOfProphylaxiePostExpositionLast",patientsUnderDrug.get("sumOfProphylaxiePostExpositionLast"));
            mav.addObject("sumOfProphylaxiePostExpositionTatals",patientsUnderDrug.get("sumOfProphylaxiePostExpositionTatals"));
            
            
            
             int pmtctCompositionsSize = (Integer) patientsUnderDrug.get("pmtctCompositionsSize");
            
            for (int i=1;i<=pmtctCompositionsSize;i++){
                
                mav.addObject("pmtctLast"+i,patientsUnderDrug.get("pmtctLast"+i));	
                mav.addObject("pmtctNew"+i,patientsUnderDrug.get("pmtctNew"+i));
                mav.addObject("TotalPmtct"+i,patientsUnderDrug.get("TotalPmtct"+i));
               
                  
              }
            mav.addObject("sumOfPmtctNew",patientsUnderDrug.get("sumOfPmtctNew"));
            mav.addObject("sumOfPmtctLast",patientsUnderDrug.get("sumOfPmtctLast"));
            mav.addObject("sumOfPmtctTotals",patientsUnderDrug.get("sumOfPmtctTotals"));
             
            int regimenPediatricSiropSize=(Integer)patientsUnderDrug.get("regimenPediatricSiropSize");     
            
            for (int i=1;i<=regimenPediatricSiropSize;i++){
                
                mav.addObject("PediatricSiropLast"+i,patientsUnderDrug.get("PediatricSiropLast"+i));	
                mav.addObject("PediatricSiropNew"+i,patientsUnderDrug.get("PediatricSiropNew"+i));
                mav.addObject("TotalPediatricSirop"+i,patientsUnderDrug.get("TotalPediatricSirop"+i));
               
                  
              }
            mav.addObject("sumOfPediatricSiropNew",patientsUnderDrug.get("sumOfPediatricSiropNew"));
            mav.addObject("sumOfPediatricSiropLast",patientsUnderDrug.get("sumOfPediatricSiropLast"));
            mav.addObject("sumOfPediatricSiropTotals",patientsUnderDrug.get("sumOfPediatricSiropTotals"));
              
           /* if(exportInToExcel==null){
            mav.setViewName("module/camerwa/camerwaForm");
            }else{
            	mav.setViewName("module/camerwa/exportToExcelSheet");  
            }*/
		
		
            
            
            
            
            
            
            
            
            //CamerwaService camerwaService = Context.getService(CamerwaService.class);
    		
    		PatientService camerwaService2 = Context.getService(PatientService.class);
    		
    		List<Patient>  allPatients = camerwaService2.getAllPatients();
    		
    		patientsUnderDrug=camerwaService.getPatientsUnderDrug(dateFormatedNew,dateFormatedLimite);
    		
    		
    		
    		
    		List<Integer> patientsWithoutRegimens = new ArrayList<Integer>();
    		List<Integer> patientsWithRegimens = (List<Integer>) patientsUnderDrug.get("patientsOnRegimens") ;//  new ArrayList<Integer>();
    		//log.info("  %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% "+patientsWithRegimens);
    		log.info("patientsWithRegimens these are : --------------------"+patientsWithRegimens);
    		
    		
    		for(Patient patient : allPatients){
    			if(!patientsWithRegimens.contains(patient.getPatientId()) && !patientsWithoutRegimens.contains(patient.getPatientId())){
    				patientsWithoutRegimens.add(patient.getPatientId());
    			}
    		}
    		
    		log.info("patientsWithoutRegimens these are : +++++++++++++++++++++++++"+patientsWithoutRegimens);
    		//camerwaService.      
            
            
            
            
            
            
            
		
		
		mav.addObject("patientsUnderDrug",patientsUnderDrug);
		mav.addObject("patientsWithoutRegimens",camerwaService.removeLostFollowUp(patientsWithoutRegimens,dateFormatedNew).size());
		mav.setViewName("module/camerwa/camerwaForm");
		return mav;
	}
	
}
