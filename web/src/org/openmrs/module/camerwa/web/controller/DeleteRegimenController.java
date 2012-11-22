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
public class DeleteRegimenController extends AbstractController{
	
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
     * @see org.springframework.web.servlet.mvc.AbstractController#handleRequestInternal(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
	    // TODO Auto-generated method stub
    	ModelAndView mav = new ModelAndView();
		CamerwaService camerwaService = Context.getService(CamerwaService.class);
		
		String regimenCategory = ServletRequestUtils.getStringParameter(request, "regimenCategory",null);
		String regimenName = ServletRequestUtils.getStringParameter(request, "regimenName",null);
		
		
		if(!(regimenCategory == null) || !(regimenName == null)){
		
		 
		Boolean deleteRegimen	= camerwaService.deleteRegimen(regimenCategory, regimenName);
		if(deleteRegimen){
			mav.addObject("deleteSuccessMsg","You have deleted ("+regimenName+") Regimen");
		}
			
		}
		
		
		
		
		mav.addObject("regimenNames", camerwaService.getRegimenNames());
		mav.addObject("regiemenCategoryNames", camerwaService.getRegimenCategories());
    	mav.setViewName("module/camerwa/deleteRegimen");
    	
	    return mav;
    }

}
