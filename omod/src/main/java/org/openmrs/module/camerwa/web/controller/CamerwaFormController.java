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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;
import org.openmrs.api.context.Context;
import org.openmrs.module.camerwa.RegimenResult;
import org.openmrs.module.camerwa.service.CamerwaService;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;



/**
 * This controller backs the /web/module/basicmoduleForm.jsp page. This controller is tied to that
 * jsp page in the /metadata/moduleApplicationContext.xml file
 */
public class CamerwaFormController extends AbstractController {
	
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

	/**
     * @see org.springframework.web.servlet.mvc.AbstractController#handleRequestInternal(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	
    	
    	String exportInToExcel = ServletRequestUtils.getStringParameter(request, "exportInToExcel",null);
    	
    	
    	ModelAndView mav=new ModelAndView();
    	mav.setViewName("module/camerwa/camerwaForm");
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
 
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
   
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
        
        try {
	        
        
       log.info("patientsUnderDrugpatientsUnde patientsUnderDrug   patientsUnderDrug patientsUnderDrug  rDrugpatientsUnderDrugpatientsUnderDrug"+patientsUnderDrug+"  dateFormatedNew  "+dateFormatedNew+"dateFormatedLimite "+dateFormatedLimite);
        patientsUnderDrug=camerwaService.getPatientsUnderDrug(dateFormatedNew,dateFormatedLimite);
        
        // getting all locations
       log.info("locations  "+camerwaService.getLocations());
        mav.addObject("locations", camerwaService.getLocations());
       log.info("patient under dryg "+patientsUnderDrug);
        mav.addObject("patientsUnderDrug",patientsUnderDrug);
       
        mav.addObject("year", year);
        mav.addObject("month", month);
        // get selected location
        selectedLocaion = request.getParameter("oneLocation");
        
        mav.addObject("selectedLocaion",selectedLocaion);
        mav.addObject("patientsUnderARVNew",patientsUnderDrug.get("patientsUnderARVNew"));   
        mav.addObject("LastPatientsUnderARV",patientsUnderDrug.get("LastPatientsUnderARV"));
        mav.addObject("adultsUnderARVNew",patientsUnderDrug.get("adultsUnderARVNew"));
        mav.addObject("LastAdultsUnderARV",patientsUnderDrug.get("LastAdultsUnderARV"));   
        mav.addObject("kidsDOBQueryNew",patientsUnderDrug.get("kidsDOBQueryNew"));
        mav.addObject("LastkidsDOBQuery",patientsUnderDrug.get("LastkidsDOBQuery"));
        
        int adultRegimenCompositionSize= (Integer) patientsUnderDrug.get("adultRegimenCompositionSize");
        List<Object> adultRegimenResults= new ArrayList<Object>();
        for(int i=1;i<=adultRegimenCompositionSize;i++){
         
        RegimenResult regimenResultAdult=new RegimenResult();

        regimenResultAdult.setRegimenName((String) patientsUnderDrug.get("adultRegimenName"+i));
        regimenResultAdult.setRegimenNew((Integer) patientsUnderDrug.get("adultRegimenNew"+i));
        regimenResultAdult.setRegimenLast((Integer) patientsUnderDrug.get("adultRegimenLast"+i));
        regimenResultAdult.setTotalRegimen((Integer) patientsUnderDrug.get("TotalAdulRegimen"+i));
        adultRegimenResults.add(regimenResultAdult);
        }
        mav.addObject("adultRegimenResults", adultRegimenResults);
        
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
        
        int pediatricSize= (Integer) patientsUnderDrug.get("pediatricSize");
        
        
        List<Object> pediatricRegimenResults= new ArrayList<Object>();
        for(int i=1 ; i<=pediatricSize;i++){
        	
        	RegimenResult regimenResultPediatric=new RegimenResult();
        	
            
            regimenResultPediatric.setRegimenName((String) patientsUnderDrug.get("pediatricRegimenName"+i));
            regimenResultPediatric.setRegimenNew((Integer) patientsUnderDrug.get("pediatricRegimenNew"+i));
            regimenResultPediatric.setRegimenLast((Integer) patientsUnderDrug.get("pediatricRegimenLast"+i));
            regimenResultPediatric.setTotalRegimen((Integer) patientsUnderDrug.get("TotalPediatricRegimen"+i));
            pediatricRegimenResults.add(regimenResultPediatric);
            }
            mav.addObject("pediatricRegimenResults", pediatricRegimenResults);
      
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
            
          
            List<Object> pediatricRegimenSecondLineResults= new ArrayList<Object>();
            for (int i=1;i<=pediatricRegimenSecondLineSize;i++){
            	RegimenResult pediatricRegimenSecondLineResult=new RegimenResult();     
              
              pediatricRegimenSecondLineResult.setRegimenName((String) patientsUnderDrug.get("pediatricSecondLineName"+i));
              pediatricRegimenSecondLineResult.setRegimenNew((Integer) patientsUnderDrug.get("pediatricSecondLineNew"+i));
              pediatricRegimenSecondLineResult.setRegimenLast((Integer) patientsUnderDrug.get("pediatricSecondLineLast"+i));
              pediatricRegimenSecondLineResult.setTotalRegimen((Integer) patientsUnderDrug.get("TotalPediatricSecondLine"+i));
              pediatricRegimenSecondLineResults.add(pediatricRegimenSecondLineResult);
              
            }
            
            mav.addObject("pediatricRegimenSecondLineResults", pediatricRegimenSecondLineResults);
            mav.addObject("sumOfSecondLineNew",patientsUnderDrug.get("sumOfSecondLineNew"));
            mav.addObject("sumOfSecondLineLast",patientsUnderDrug.get("sumOfSecondLineLast"));
            mav.addObject("sumOfSecondLineTatals",patientsUnderDrug.get("sumOfSecondLineTatals")); 
            
            int adultRegimenSecondLineSize =(Integer) patientsUnderDrug.get("adultRegimenSecondLineSize");
            
            
            
            List<Object> adultRegimenSecondLineResults= new ArrayList<Object>();
            for (int i=1;i<=adultRegimenSecondLineSize;i++){
                   
              
            	RegimenResult adultRegimenSecondLineResult=new RegimenResult();
            	
            	adultRegimenSecondLineResult.setRegimenName((String) patientsUnderDrug.get("adultSecondLineName"+i));
            	adultRegimenSecondLineResult.setRegimenNew((Integer) patientsUnderDrug.get("adultSecondLineNew"+i));
                adultRegimenSecondLineResult.setRegimenLast((Integer) patientsUnderDrug.get("adultSecondLineLast"+i));
                adultRegimenSecondLineResult.setTotalRegimen((Integer) patientsUnderDrug.get("TotalAdultSecondLine"+i));
                adultRegimenSecondLineResults.add(adultRegimenSecondLineResult);
                 
            }
            mav.addObject("adultRegimenSecondLineResults", adultRegimenSecondLineResults);
            mav.addObject("sumOfAdultSecondLineNew",patientsUnderDrug.get("sumOfAdultSecondLineNew"));
            mav.addObject("sumOfAdultSecondLineLast",patientsUnderDrug.get("sumOfAdultSecondLineLast"));
            mav.addObject("sumOfAdultSecondLineTatals",patientsUnderDrug.get("sumOfAdultSecondLineTatals"));
            
            //----------------------------------------------------------------------------------------
            
            int adultRegimenAdultThirdLineSize =(Integer) patientsUnderDrug.get("regimenAdultThirdLineSize");
            
            
            
            List<Object> adultRegimenAdultThirdLineResults= new ArrayList<Object>();
            for (int i=1;i<=adultRegimenAdultThirdLineSize;i++){
                   
                RegimenResult adultRegimenAdultThirdLineResult=new RegimenResult();
            	
            	adultRegimenAdultThirdLineResult.setRegimenName((String) patientsUnderDrug.get("RegimenAdultThirdLineName"+i));
            	adultRegimenAdultThirdLineResult.setRegimenNew((Integer) patientsUnderDrug.get("RegimenAdultThirdLineNew"+i));
                adultRegimenAdultThirdLineResult.setRegimenLast((Integer) patientsUnderDrug.get("RegimenAdultThirdLineLast"+i));
                adultRegimenAdultThirdLineResult.setTotalRegimen((Integer) patientsUnderDrug.get("TotalRegimenAdultThirdLine"+i));
                adultRegimenAdultThirdLineResults.add(adultRegimenAdultThirdLineResult);
                 
            }
            mav.addObject("adultRegimenAdultThirdLineResults", adultRegimenAdultThirdLineResults);
            mav.addObject("sumOfRegimenAdultThirdLineNew",patientsUnderDrug.get("sumOfRegimenAdultThirdLineNew"));
            mav.addObject("sumOfRegimenAdultThirdLineLast",patientsUnderDrug.get("sumOfRegimenAdultThirdLineLast"));
            mav.addObject("sumOfRegimenAdultThirdLineTatals",patientsUnderDrug.get("sumOfRegimenAdultThirdLineTotals"));
            
            //-------------------------------------------------------------------------------------------------------
            
            
            int prophylaxiePostExpositionSize = (Integer) patientsUnderDrug.get("prophylaxiePostExpositionSize");
            
            List<Object> prophylaxiePostExpositionResults= new ArrayList<Object>();
            for (int i=1;i<=prophylaxiePostExpositionSize;i++){
                
            	RegimenResult prophylaxiePostExpositionResult=new RegimenResult();
            	
            	prophylaxiePostExpositionResult.setRegimenName((String) patientsUnderDrug.get("prophylaxiePostExpositionName"+i));
            	prophylaxiePostExpositionResult.setRegimenNew((Integer) patientsUnderDrug.get("prophylaxiePostExpositionNew"+i));
            	prophylaxiePostExpositionResult.setRegimenLast((Integer) patientsUnderDrug.get("prophylaxiePostExpositionLast"+i));
            	prophylaxiePostExpositionResult.setTotalRegimen((Integer) patientsUnderDrug.get("TotalProphylaxiePostExposition"+i));
            	prophylaxiePostExpositionResults.add(prophylaxiePostExpositionResult);
                
              }
            
            mav.addObject("prophylaxiePostExpositionResults", prophylaxiePostExpositionResults);
            mav.addObject("sumOfProphylaxiePostExpositionNew",patientsUnderDrug.get("sumOfProphylaxiePostExpositionNew"));
            mav.addObject("sumOfProphylaxiePostExpositionLast",patientsUnderDrug.get("sumOfProphylaxiePostExpositionLast"));
            mav.addObject("sumOfProphylaxiePostExpositionTatals",patientsUnderDrug.get("sumOfProphylaxiePostExpositionTatals"));
            
            
            List<Object> pmtctResults= new ArrayList<Object>();
             int pmtctCompositionsSize = (Integer) patientsUnderDrug.get("pmtctCompositionsSize");
            
            for (int i=1;i<=pmtctCompositionsSize;i++){
            	RegimenResult pmtctResult=new RegimenResult();
            	    
            	pmtctResult.setRegimenName((String) patientsUnderDrug.get("pmtctName"+i));
            	pmtctResult.setRegimenNew((Integer) patientsUnderDrug.get("pmtctNew"+i));
            	pmtctResult.setRegimenLast((Integer) patientsUnderDrug.get("pmtctLast"+i));
            	pmtctResult.setTotalRegimen((Integer) patientsUnderDrug.get("TotalPmtct"+i));
            	pmtctResults.add(pmtctResult);
                  
              }
            mav.addObject("pmtctResults", pmtctResults);
            mav.addObject("sumOfPmtctNew",patientsUnderDrug.get("sumOfPmtctNew"));
            mav.addObject("sumOfPmtctLast",patientsUnderDrug.get("sumOfPmtctLast"));
            mav.addObject("sumOfPmtctTotals",patientsUnderDrug.get("sumOfPmtctTotals"));
            
            List<Object> pediatricSiropResults= new ArrayList<Object>();
            int regimenPediatricSiropSize=(Integer)patientsUnderDrug.get("regimenPediatricSiropSize");     
            
            for (int i=1;i<=regimenPediatricSiropSize;i++){
            	RegimenResult pediatricSiropResult=new RegimenResult();
            	
      
                
            	pediatricSiropResult.setRegimenName((String) patientsUnderDrug.get("PediatricSiropName"+i));
            	pediatricSiropResult.setRegimenNew((Integer) patientsUnderDrug.get("PediatricSiropNew"+i));
            	pediatricSiropResult.setRegimenLast((Integer) patientsUnderDrug.get("PediatricSiropLast"+i));
            	pediatricSiropResult.setTotalRegimen((Integer) patientsUnderDrug.get("TotalPediatricSirop"+i));
            	pediatricSiropResults.add(pediatricSiropResult);
                  
              }
            mav.addObject("pediatricSiropResults", pediatricSiropResults);
            mav.addObject("sumOfPediatricSiropNew",patientsUnderDrug.get("sumOfPediatricSiropNew"));
            mav.addObject("sumOfPediatricSiropLast",patientsUnderDrug.get("sumOfPediatricSiropLast"));
            mav.addObject("sumOfPediatricSiropTotals",patientsUnderDrug.get("sumOfPediatricSiropTotals"));
              
            if(exportInToExcel==null){
                
                
            }else{
            	mav.setViewName("module/camerwa/exportToExcelSheet");  
            }
        }
        catch (Exception e) {
	      
        }
	    return mav;
    }
}