package org.openmrs.module.camerwa.web.controller;


import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.management.relation.Role;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.openmrs.Patient;
import org.openmrs.Privilege;
import org.openmrs.User;
import org.openmrs.api.context.Context;
import org.openmrs.module.camerwa.PatientListControl;
import org.openmrs.module.camerwa.regimenhistory.Regimen;
import org.openmrs.module.camerwa.regimenhistory.RegimenComponent;
import org.openmrs.module.camerwa.regimenhistory.RegimenHistory;
import org.openmrs.module.camerwa.regimenhistory.RegimenUtils;
import org.openmrs.module.camerwa.service.CamerwaService;
import org.openmrs.util.OpenmrsClassLoader;
import org.openmrs.util.DatabaseUpdater.OpenMRSChangeSet;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import sun.reflect.ReflectionFactory.GetReflectionFactoryAction;

/**
 *
 */
public class DownloadController extends AbstractController {
	protected DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	/** Logger for this class and subclasses */
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
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Session session = getSessionFactory().getCurrentSession();
		
		List<Patient> patientsToExport =new ArrayList<Patient>();
		
		
		//ModelAndView mav = new ModelAndView();
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
		
		String KeyPatients = (String) request.getParameter("KeyPatinents");
		String regimenName = (String) request.getParameter("regimenName");
		
		String programIdKey="CAMERWA PATIENT REPORT FOR "+KeyPatients;
        PatientListControl patientListControl=new PatientListControl();
        List<Object[]> patientHistory = new ArrayList<Object[]>();
        patientsToExport= patientListControl.getPatientsByKey(KeyPatients,regimenName, dateFormatedNew,dateFormatedLimite);
         
            for(Patient patient : patientsToExport){
        	 
        	 RegimenHistory history = RegimenUtils.getRegimenHistory(patient);
        	   List<Regimen> regimens = history.getRegimenList();
        	   Set<RegimenComponent> patientDrugs = null;
        	   for(Regimen regimen : regimens){
        		   patientDrugs= new HashSet<RegimenComponent>();
        		   patientDrugs.addAll(regimen.getComponents());
        	   }  
        	   if (regimens.size() != 0) {
        		    patientHistory.add(new Object[] {patient,camerwaService.getRegimensAsString(patientDrugs),camerwaService.getLastEncouterDate(patient).get(0),camerwaService.getLastVisiteDate(patient).get(0)});
        		   }else{
           			patientHistory.add(new Object[] {patient,null,camerwaService.getLastEncouterDate(patient).get(0),camerwaService.getLastVisiteDate(patient).get(0)});
           		}
        	 
        	  }
        
      
        
       //  if(Context.hasPrivilege("Export Collective Patient Data")){
          doDownload(request, response, patientHistory, "camerwaReport.csv", programIdKey);
        /* }else{
        	 ModelAndView mav=new ModelAndView();
             
        	 mav.addObject("exportPrivilegeMessage","                    Sorry you dont have privilege to export ");
        	 mav.addObject("thePatientAndRegimenList",patientHistory);
       		 mav.addObject("KeyPatinents",KeyPatients);
       		 mav.addObject("regimenName",regimenName);
    		 mav.addObject("year",year);
    		 mav.addObject("month",month);
        	 
        	 
        	 mav.setViewName("module/camerwa/patientList");  
        	return mav;  
         }*/
		return null;
	}
	
	/**
	 * Auto generated method comment
	 * 
	 * @param request
	 * @param response
	 * @param patientHistory
	 * @param filename
	 * @param title
	 * @throws IOException
	 */
	
	private void doDownload(HttpServletRequest request, HttpServletResponse response, List<Object[]> patientHistory,
	                        String filename, String title) throws IOException {
		
		CamerwaService camerwaService = Context.getService(CamerwaService.class);
		SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		ServletOutputStream outputStream = response.getOutputStream();
		response.setContentType("text/plain");
		response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
		outputStream.println("" + title);
		outputStream.println();
		
		if(Context.hasPrivilege("View Patient Names")){
		outputStream.println("Identifier,Given Name,Family Name,Age,Gender,Last Encouter Date,Last visit Date,Drugs,Arv start Date");
		outputStream.println();
		
		for (Object[] patientAndDrugs : patientHistory) {
			Patient patient = (Patient) patientAndDrugs[0];
			String drugs =(String)patientAndDrugs[1];
			
			outputStream.println(patient.getPatientIdentifier() + "," + patient.getGivenName() + ","
			         + patient.getFamilyName() + "," + patient.getAge() +","
			        + patient.getGender() + ","+patientAndDrugs[2]+","+patientAndDrugs[3]+","+drugs+","+camerwaService.getWhenPatientStarted(patient));
		}
		}else{
			outputStream.println("Identifier,Age,Gender,Last Encouter Date,Last visit Date,Drugs");
			outputStream.println();
			
			for (Object[] patientAndDrugs : patientHistory) {
				Patient patient = (Patient) patientAndDrugs[0];
				String drugs =(String)patientAndDrugs[1];
				
				outputStream.println(patient.getPatientIdentifier() + ","
				         + patient.getAge() +","
				        + patient.getGender() + ","+patientAndDrugs[2]+","+patientAndDrugs[3]+","+drugs);
			}
		}
		outputStream.flush();
		outputStream.close();
		
	}
	
}
