package com.cattool.application.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cattool.application.dao.AnswersDAO;
import com.cattool.application.dao.ApplicationDAO;
import com.cattool.application.dao.CloudProviderDAO;
import com.cattool.application.dao.CloudProviderRuleDAO;
import com.cattool.application.dao.CloudableRuleDAO;
import com.cattool.application.dao.MigrationDAO;
import com.cattool.application.dao.MigrationRuleDAO;
import com.cattool.application.dao.service.ApplicationDAOService;
import com.cattool.application.entity.Answers;
import com.cattool.application.entity.Application;
import com.cattool.application.entity.AssessmentQuestions;

import com.cattool.application.repository.AnswersRepository;
import com.cattool.application.repository.ApplicationRepository;
import com.cattool.application.repository.AssessmentQuestionsRepository;
import com.cattool.application.dao.service.CloudProviderDAOService;
import com.cattool.application.dao.service.CloudableRuleDAOService;
import com.cattool.application.dao.service.MigrationPatternDAOService;
import com.cattool.application.dao.service.AnswesDAOService;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Service
@Transactional
public class ApplicationService {

	@Autowired
	ApplicationRepository applicationRepository;

	@Autowired
	AssessmentQuestionsRepository assessmentQuestionsRepository;

	@Autowired
	AnswersRepository answerRepository;

	@Autowired
	ApplicationDAOService applicationDaoService;

	@Autowired
	CloudProviderDAOService CloudProviderDAOService;
	
	@Autowired
	AnswesDAOService answesDAOService;
	
	@Autowired
	CloudableRuleDAOService cloudableRuleDAOService;
	
	@Autowired
	MigrationPatternDAOService migrationPatternDAOService;


	public int getAllAppsCount(int clientId) {
		return applicationDaoService.getAllApplicationCount(clientId);
	}
	

	public List<ApplicationDAO> getAllApplication(int clientId) {
		List<ApplicationDAO> applicationList = applicationDaoService.getApplicationList(clientId);
		return applicationList;
	}

	public ApplicationDAO getApplicationById(int applicationId) {
		return applicationDaoService.getApplicationById(applicationId);
	}

	public void saveApplication(ApplicationDAO application) {
		applicationDaoService.saveApplication(application);
	}

	public ApplicationDAO findbyApplicationName(String applicationName) {
		return applicationDaoService.findbyApplicationName(applicationName);
	}

	public ApplicationDAO getApplicationByUserName(String userName) {
		return applicationDaoService.getApplicationByUserName(userName);
	}

	public void updateApplication(String clientName, ApplicationDAO application) {
		applicationDaoService.updateApplication(clientName,application);
	}

	public void deleteApplicationById(int id) {

		applicationDaoService.deleteApplicationById(id);
	}

	public void resetApplicationById(int applicationId) {
		applicationDaoService.resetApplicationById(applicationId);
	}

	public void deactivateApplicationById(int applicationId) {
		applicationDaoService.deactivateApplicationById(applicationId);
	}

	public List<ApplicationDAO> getAllReassessment(int clientId) {
		return applicationDaoService.getAllReassessment(clientId);
	}
	
	
	
	public void allRuleCheck(int applicationId) {
		ApplicationDAO application = applicationDaoService.getApplicationById(applicationId);
		applicationDaoService.saveApplicationRuleCheck(application);
		boolean cloudabilityCheck = cloudableCheck(applicationId);
		if (cloudabilityCheck) {
			cloudProviderCheck(applicationId);
			migrationCheck(applicationId);
		}
	}

	public boolean cloudableCheck(int applicationId) {
		int cloudableRuleFlag = 0;
		ApplicationDAO application = new ApplicationDAO();
		application = applicationDaoService.getApplicationById(applicationId);
		int clientId = application.getClientId();
		List<CloudableRuleDAO> cloudableRuleListByClientId = new ArrayList<CloudableRuleDAO>();
		cloudableRuleListByClientId = cloudableRuleDAOService.findcloudableRule(clientId);
		
		List<AnswersDAO> answersList = new ArrayList<>();
		answersList = answesDAOService.findAnswers(applicationId);
		
		for (CloudableRuleDAO cloudableRule : cloudableRuleListByClientId) {
			String[] cloudableRuleArray = cloudableRule.getCloudableRule().split(",");
			for (AnswersDAO answers : answersList) {
				if (cloudableRule.getQuestionId() == (answers.getQuestionId())) {
					for (int i = 0; i < cloudableRuleArray.length; i++) {
						if (cloudableRuleArray[i].equals(answers.getAnswerText())) {
							applicationDaoService.setCloudableInAns(answers.getAnswerId());
							cloudableRuleFlag++;
						}
					}
				}
			}
		}
		if (cloudableRuleFlag == cloudableRuleListByClientId.size()) {
			applicationDaoService.setCloudabilityForApplication(applicationId,"Yes");
			return true;
		} else {
			applicationDaoService.setCloudabilityForApplication(applicationId,"No");
			return false;
		}
	}

	public void cloudProviderCheck(int applicationId) {

		ApplicationDAO application = new ApplicationDAO();
		application = applicationDaoService.getApplicationById(applicationId);
		List<AnswersDAO> allanswers = new ArrayList<>();
		allanswers = answesDAOService.findAnswers(applicationId);
		for (CloudProviderDAO cloudProvider : CloudProviderDAOService.findCloudProviderRules(application.getClientId()))
		{
			
			int count = 0, numberOfRules = 0;
			for(CloudProviderRuleDAO cloudProviderRuleDAO : cloudProvider.getCloudProviderRules())
			{
			String[] cloudProviderRuleArray = cloudProviderRuleDAO.getCloudProviderRule().split(",");
				numberOfRules++;

				for (AnswersDAO answers : allanswers) {
					if (answers.getQuestionId() == Integer.parseInt(cloudProviderRuleDAO.getQuestionId())) {

						for (int i = 0; i < cloudProviderRuleArray.length; i++) {
							if (cloudProviderRuleArray[i].equals(answers.getAnswerText())) {
								count++;
							}
						}
					}
				}
			}
			
			if("OR".equalsIgnoreCase(cloudProvider.getPermission()))
			{
				if (count > 0) {
					applicationDaoService.setCloudprovider(application,cloudProvider.getCloudProviders());
					break;
				}
			}
			if (numberOfRules == count) {
				applicationDaoService.setCloudprovider(application,cloudProvider.getCloudProviders());
				break;
			}
		}
			
		

	}

//	public void migrationCheck(int applicationId) {
//		ApplicationDAO application = new ApplicationDAO();
//		application = applicationDaoService.getApplicationById(applicationId);
//		
//		List<AnswersDAO> allanswers = answesDAOService.findAnswers(applicationId);
//		
//		for (MigrationDAO migrationDAO : migrationPatternDAOService.findMigrationRules(application.getClientId())) {
//			int count = 0, numberOfRules = 0;
//			System.out.println(migrationDAO.getMigrationPattern());
//			for (MigrationRuleDAO migrationRuleDAO : migrationDAO.getMigrationRule()) {
//				String[] migrationRuleArray = migrationRuleDAO.getMigrationRule().split(",");
//				numberOfRules++;
//				for (AnswersDAO answers : allanswers) {
//					if (answers.getQuestionId() == Integer.parseInt(migrationRuleDAO.getQuestionId()) ) {
//						for (int i = 0; i < migrationRuleArray.length; i++) {
//
//							if (migrationRuleArray[i].equals(answers.getAnswerText())) {
//								count++;
//							}
//						}
//					}
//				}
//			}
//			if(migrationDAO.getPermission().equalsIgnoreCase("AND"))
//			{
//				if (count == numberOfRules) {
//					applicationDaoService.setMigrationPattern(application, migrationDAO.getMigrationPattern());
//					break;
//				}
//			}
//			else if(migrationDAO.getPermission().equalsIgnoreCase("OR"))
//			{
//				if (count >=1) {
//					applicationDaoService.setMigrationPattern(application, migrationDAO.getMigrationPattern());
//					break;
//				}
//			}
//			else
//			{
//				applicationDaoService.setMigrationPattern(application, migrationDAO.getMigrationPattern());
//				break;
//			}
//			
//		}
//	}

	public void migrationCheck(int applicationId) {
		ApplicationDAO application = new ApplicationDAO();
		application = applicationDaoService.getApplicationById(applicationId);
		List<AnswersDAO> allanswers = answesDAOService.findAnswers(applicationId);
		
		
		for (MigrationDAO migrationDAO : migrationPatternDAOService.findMigrationRules(application.getClientId())) {
			int count = 0;
			HashSet  numberOfRules= new HashSet();
			for (MigrationRuleDAO migrationRuleDAO : migrationDAO.getMigrationRule()) {
				for (AnswersDAO answers : allanswers) {
					if (answers.getQuestionId() == Integer.parseInt(migrationRuleDAO.getQuestionId()) ) {
						numberOfRules.add(migrationRuleDAO.getQuestionId());
							if (migrationRuleDAO.getOptionId()==answers.getOptionId())
							{
								count++;
							}
//						}
					}
				}
			}
			if(migrationDAO.getPermission().equalsIgnoreCase("AND"))
			{
				if (count == numberOfRules.size()) {
					applicationDaoService.setMigrationPattern(application, migrationDAO.getMigrationPattern());
					break;
				}
			}
			else if(migrationDAO.getPermission().equalsIgnoreCase("OR"))
			{
				if (count >=1) {
					applicationDaoService.setMigrationPattern(application, migrationDAO.getMigrationPattern());
					break;
				}
			}
			else
			{
				applicationDaoService.setMigrationPattern(application, migrationDAO.getMigrationPattern());
				break;
			}
			
		}
	}

	public List<ApplicationDAO> getAllFinalizeAplication(int clientId) {
		List<ApplicationDAO> applicationList = applicationDaoService.getAllFinalizeAplication(clientId);
		return applicationList;
	}
	


	

}