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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;
import org.openmrs.*;
import org.openmrs.api.context.Context;
import org.openmrs.module.camerwa.PatientListControl;
import org.openmrs.module.camerwa.db.CamerwaDAO;
import org.openmrs.module.camerwa.regimenhistory.Regimen;
import org.openmrs.module.camerwa.regimenhistory.RegimenComponent;
import org.openmrs.module.camerwa.regimenhistory.RegimenHistory;
import org.openmrs.module.camerwa.regimenhistory.RegimenUtils;
import org.openmrs.module.camerwa.service.CamerwaService;

/**
 *
 */
public class PatientList extends AbstractController {
	
	
	
	protected DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	
	protected final Log log = LogFactory.getLog(getClass());
	
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
	
	/**
	 * @see org.springframework.web.servlet.mvc.AbstractController#handleRequestInternal(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	@SuppressWarnings("unchecked")
    @Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		
		ModelAndView mav = new ModelAndView();
		CamerwaService camerwaService = Context.getService(CamerwaService.class);
		
		// getting patients who have been exited from care
		
	//	List<Integer> patientsExitedFromCareList = (List<Integer>) camerwaService.getPatientsExitedFromCare();
		
		//getting parameters
		
	    DateFormat dateFormatY = new SimpleDateFormat("yyyy");
    	DateFormat dateFormatM = new SimpleDateFormat("MM");
        java.util.Date date = new java.util.Date();
    
    	String curentYear=dateFormatY.format(date); 
    	String curentMonth=dateFormatM.format(date);
		
		
		int year = ServletRequestUtils.getIntParameter(request, "year", Integer.parseInt(curentYear));
        int month = ServletRequestUtils.getIntParameter(request, "month", Integer.parseInt(curentMonth));
		
		String KeyPatients = (String) request.getParameter("KeyPatinents");
		//log.info("!!!!!!!!!!!!!!!!!!!!!!!!! KeyPatinents "+KeyPatients);
		String regimenName = (String) request.getParameter("regimenName");
		
		
		
		Calendar cal = new GregorianCalendar();
        cal.set(GregorianCalendar.YEAR, year);
        cal.set(GregorianCalendar.MONTH, month-1);
        cal.set(GregorianCalendar.DAY_OF_MONTH ,1);
        Date dateFormatedNew = cal.getTime();
        
        
        Calendar cal2 = new GregorianCalendar();
        Date dateFormatedLimite;
        if(month < 12){
        cal2.set(GregorianCalendar.YEAR, year);
        cal2.set(GregorianCalendar.MONTH, month);
        cal2.set(GregorianCalendar.DAY_OF_MONTH ,1);
        dateFormatedLimite = cal2.getTime();
        }else {
        	
        	cal2.set(GregorianCalendar.YEAR,year+1);
            cal2.set(GregorianCalendar.MONTH,1);
            cal2.set(GregorianCalendar.DAY_OF_MONTH ,1);
            dateFormatedLimite = cal2.getTime();
        	
        }
        
         List<Object[]> patientHistory = new ArrayList<Object[]>();
         PatientListControl patientListControl=new PatientListControl();
         List<Patient> patientsList = patientListControl.getPatientsByKey(KeyPatients,regimenName, dateFormatedNew,dateFormatedLimite);
         
         //log.info("@@@@@@@@@@@@@@@@@@@@@@@@ this is the value before regimens"+patientsList.size());
         //log.info("%%%%%%%%%%%%%%%%%%%%%% this is a lis of patients to be listed"+patientsList);
         
         for(Patient patient : patientsList){
        	 
        	 RegimenHistory history = RegimenUtils.getRegimenHistory(patient);
        	   List<Regimen> regimens = history.getRegimenList();
        	   Set<RegimenComponent> patientDrugs = null;
        	   for(Regimen regimen : regimens){
        		   patientDrugs= new HashSet<RegimenComponent>();
        		   patientDrugs.addAll(regimen.getComponents());
        	   }  
        	   if (regimens.size() != 0) {
        		   
        		    patientHistory.add(new Object[] {patient,camerwaService.getRegimensAsString(patientDrugs),camerwaService.getLastEncouterDate(patient).get(0),camerwaService.getLastVisiteDate(patient).get(0),camerwaService.getWhenPatientStarted(patient)});
        		}else{
        			patientHistory.add(new Object[] {patient,null,camerwaService.getLastEncouterDate(patient).get(0),camerwaService.getLastVisiteDate(patient).get(0),camerwaService.getWhenPatientStarted(patient)});
        		}
        	 
        	  }

         
         mav.addObject("thePatientAndRegimenList",patientHistory);
   		 mav.addObject("KeyPatinents",KeyPatients);
   		 mav.addObject("regimenName",regimenName);
		 mav.addObject("year",year);
		 mav.addObject("month",month);
    	 mav.setViewName("module/camerwa/patientList");
	
		return mav;
	}
   
}
