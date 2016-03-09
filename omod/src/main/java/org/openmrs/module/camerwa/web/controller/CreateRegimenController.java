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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;
import org.openmrs.api.context.Context;
import org.openmrs.module.camerwa.service.CamerwaService;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

/**
 *
 */
public class CreateRegimenController extends AbstractController {
	
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
	
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		ModelAndView mav = new ModelAndView();
		CamerwaService camerwaService = Context.getService(CamerwaService.class);
		
		//**************************************************************
		
		Map requestMap = request.getParameterMap();
		List<String> drugRegimens = new ArrayList<String>();
		ArrayList<String> fieldNames = new ArrayList<String>();
		for (Object va : requestMap.keySet()) {
			fieldNames.add((String) va);
		}
		
		if (fieldNames.size() != 0) {
			for (String str : fieldNames) {
		        //System.out.print("!!!!!!!!!!!!!!!!!!!!!!!!!!!! fieldNames "+fieldNames);
				if(str.contains("_")){
				String suffixId = str.substring(str.indexOf("_") + 1);
				
				String drugSuffix = "drugs_" + suffixId;
				
				
				if (drugSuffix != null && !drugSuffix.equals("") ) {
					//System.out.println("************************** str ************* "+drugSuffix);
					drugRegimens.add(request.getParameter(drugSuffix));
			     	}
				}
			}
		}
		/*System.out.print("!!!!!!!!!!!!!!!!!!!!!!!!!!!! drugRegimens "+drugRegimens);
		log.info("  ******************* drugRegimens size == "+drugRegimens.size());*/
		

		//**************************************************************
		
		String regimenCategory = ServletRequestUtils.getStringParameter(request, "regimenCategory", null);
		String regimenName = ServletRequestUtils.getStringParameter(request, "regimenName", null);
		
		
		String arvDrug1 = ServletRequestUtils.getStringParameter(request, "arvDrug1", null);
		//log.info("this is tha drug one displayed 33333333@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"+regimenCategory+"@@@@@@2222222@@@@@@@@@  "+arvDrug1);
		/*String arvDrug2 = ServletRequestUtils.getStringParameter(request, "arvDrug2", null);
		String arvDrug3 = ServletRequestUtils.getStringParameter(request, "arvDrug3", null);
		String arvDrug4 = ServletRequestUtils.getStringParameter(request, "arvDrug4", null);*/
		
		
		/*boolean ceationOfRegimen = camerwaService.insertRegimen(regimenCategory, regimenName, arvDrug1I, arvDrug2Int,
		    arvDrug3Int, arvDrug4Int);
		*/
	
		if(drugRegimens.size()!= 0 || arvDrug1 != null){
		//if (!(arvDrug1 == null) || !(drugRegimens.get(0) == null) || !(drugRegimens.get(1) == null) || !(drugRegimens.get(2) == null)) {
			
			/*Object arvDrug1Int = camerwaService.getDrugIdByDrugName(arvDrug1);
			Object arvDrug2Int = camerwaService.getDrugIdByDrugName(arvDrug2);
			Object arvDrug3Int = camerwaService.getDrugIdByDrugName(arvDrug3);
			Object arvDrug4Int = camerwaService.getDrugIdByDrugName(arvDrug4);*/
			Object arvDrug1Int = null;
			Object arvDrug2Int = null;// camerwaService.getDrugIdByDrugName(drugRegimens.get(0));
			Object arvDrug3Int = null;//= camerwaService.getDrugIdByDrugName(drugRegimens.get(1));
			Object arvDrug4Int = null;//= camerwaService.getDrugIdByDrugName(drugRegimens.get(2));
				
			if(arvDrug1 != null)
			arvDrug1Int = camerwaService.getConceptIdByDrugName(arvDrug1);
			if(drugRegimens.size()>=1)
			arvDrug2Int = camerwaService.getConceptIdByDrugName(drugRegimens.get(0));
			if(drugRegimens.size()>=2)
			arvDrug3Int = camerwaService.getConceptIdByDrugName(drugRegimens.get(1));
			if(drugRegimens.size()>=3)
			arvDrug4Int = camerwaService.getConceptIdByDrugName(drugRegimens.get(2));
				
			boolean ceationOfRegimen = camerwaService.insertRegimen(regimenCategory, regimenName, arvDrug1Int, arvDrug2Int,
			    arvDrug3Int, arvDrug4Int);
			if (ceationOfRegimen) {
				mav.addObject("regimenHasBeenCreatedMessage", "The regimen (" + regimenName + ") has been created ");
			}
		}
		
		mav.addObject("regiemenCategoryNames", camerwaService.getRegimenCategories());
	//	log.info("@@@@@@@@@@@@@@@@@camerwaService.getArvDrugs()camerwaService.getArvDrugs()camerwaService.getArvDrugs()camerwaService.getArvDrugs()camerwaService.getArvDrugs()"+camerwaService.getArvDrugs());
		mav.addObject("arvDrugs", camerwaService.getArvDrugs());
		mav.setViewName("module/camerwa/createRegimen");
		
		return mav;
	}
	
}
