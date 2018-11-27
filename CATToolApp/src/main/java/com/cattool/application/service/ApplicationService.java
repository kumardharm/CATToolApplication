package com.cattool.application.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cattool.application.entity.Answers;
import com.cattool.application.entity.Application;
import com.cattool.application.entity.AssessmentQuestions;
import com.cattool.application.entity.CloudProviderRule;
import com.cattool.application.entity.CloudableRule;
import com.cattool.application.entity.MigrationRule;
import com.cattool.application.entity.SummaryReport;
import com.cattool.application.entity.Users;
import com.cattool.application.repository.AnswersRepository;
import com.cattool.application.repository.ApplicationRepository;
import com.cattool.application.repository.AssessmentQuestionsRepository;
import com.cattool.application.repository.CloudProviderRuleRepository;
import com.cattool.application.repository.CloudableRuleRepository;
import com.cattool.application.repository.MigrationRuleRepository;
import com.cattool.application.repository.UserRepository;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
	
	//private int gitcCheck=0;

	@Autowired
	ApplicationRepository applicationRepository;
	
	@Autowired
	AssessmentQuestionsRepository assessmentQuestionsRepository;

	@Autowired
	AnswersRepository answerRepository;
	
	@Autowired
	MigrationRuleRepository migrationRuleRepository;
	
	@Autowired
	CloudableRuleRepository cloudableRuleRepository;
	
	@Autowired
	CloudProviderRuleRepository cloudProviderRuleRepository;
	
	@Autowired
	UserRepository userRepository;
	
	public int getAllAppsCount(String clientName) 
    {   int appsCount=0; 
    System.out.println("&&&&&&&&&&");
        List<Application> applicationList= new ArrayList<Application>(); 
        for(Application applications : applicationRepository.findAll())
        {
        	applicationList.add(applications);
        }
        
        appsCount=applicationList.size(); 
//        String countJsonformat = "{\"count\" : "+appsCount+"}";
            return appsCount; 
    }
	
	public List<Application> getAllApplication(String clientName,Boolean isDeactivate)
	{
		List<Application> applicationList = new ArrayList<>();
		applicationList=applicationRepository.findByClientNameAndIsDeactivate(clientName,isDeactivate);
		/*for (Application application : applicationRepository.findAll()) {
			System.out.println(clientName+"=="+application.getClientName());
			if(!application.isDeactivate() && clientName.equals(application.getClientName()))
			{
				applicationList.add(application) ;
			}
			
		}
		
		System.out.println(applicationList);*/
		return applicationList;
	}
	
	public Application saveApplication(Application application)
	{
		System.out.println("**********************************************"+application);
		return applicationRepository.save(application);
	}
	
	public Application findbyApplicationName(String applicationName)
	{
		return applicationRepository.findByApplicationName(applicationName);
	}

	public void deleteApplicationById(int id) {
		
		applicationRepository.deleteByApplicationId(id);
	}

	public Application GetSingleApplication(int applicationId) {
		return applicationRepository.findByApplicationId(applicationId);
	}
	
	public Application getApplicationByUserName(String userName) {
		Users user=new Users();
		user=userRepository.findByUserName(userName);
		System.out.println(applicationRepository.findByUserId(user.getUserId()));
		//System.out.println(applicationRepository.findByUserId(user.getUserId()));
		return applicationRepository.findByUserId(user.getUserId());
	}
	
	public Application updateApplication(int applicationId,Application application)
	{
		Application app=new Application();
		app.setApplicationId(application.getApplicationId());
		app.setApplicationName(application.getApplicationName());
		app.setApplicationDescription(application.getApplicationDescription());
		app.setMigrationPattern(application.getMigrationPattern());
		app.setUserId(application.getUserId());
		return applicationRepository.save(application);
	}
	
	public void resetApplicationById(int applicationId)
	{
		Application application=new Application();
		application.setApplicationId(applicationId);
		application.setUserId(applicationRepository.findByApplicationId(applicationId).getUserId());
		applicationRepository.save(application);
	}
	
	public void deactivateApplicationById(int applicationId) {
		Application application=new Application();
		application = applicationRepository.findByApplicationId(applicationId);
		application.setDeactivate(true);
		application.setUserId(applicationRepository.findByApplicationId(applicationId).getUserId());
		applicationRepository.save(application);
	}

	public List<Application> getAllReassessment(String clientName) {
		// TODO Auto-generated method stub
		 List<Application> appList=new ArrayList<Application>();
		 
         for(Application application: applicationRepository.findAll()){
       	 if(application.isFinalize()==1 && clientName.equals(application.getClientName()))
        	 {
        		 appList.add(application);
        		 System.out.println(appList);
        	 }
        	 System.out.println(application.getApplicationName());
         }
       
         System.out.println("**************************"+appList+"v  *****************");
		return appList;
	}


	public void allRuleCheck(int applicationId) {
		int gitcCheck=0;
		Application application=applicationRepository.findByApplicationId(applicationId);
		application.setIsFinalize(1);
		applicationRepository.save(application);
		boolean cloudabilityCheck= cloudableCheck(applicationId);
		if(cloudabilityCheck) {
			boolean cloudproviderCheck=cloudProviderCheck(applicationId);
			if(cloudproviderCheck==false)
			{
				gitcCheck=1;
				migrationCheck(applicationId,gitcCheck);
			}
			else
			{
				gitcCheck=0;
				migrationCheck(applicationId,gitcCheck);
			}
		}
	}
	public boolean cloudableCheck(int applicationId){
		int cloudableRuleFlag=0;
		
		Application application=new Application();
		application=applicationRepository.findByApplicationId(applicationId);
		
//		List<CloudableRule> cloudableRuleList=new ArrayList<CloudableRule>();
//		cloudableRuleList = cloudableRuleRepository.findAll();
		
		List<CloudableRule> cloudableListByClientId = new ArrayList<CloudableRule>();
		for(CloudableRule cloudableRule : cloudableRuleRepository.findAll())
		{
			System.out.println(cloudableRule);
			System.out.println(cloudableRule.getClientName()+"==****=="+application.getClientName());
			if(cloudableRule.getClientName().equals(application.getClientName()))
			{
				cloudableListByClientId.add(cloudableRule);
			}
		}
		System.out.println("++++++" +cloudableListByClientId);
		
		List<Answers> answersList=new ArrayList<>();
		
		for(Answers  answers : answerRepository.findAll()) {
			if(answers.getApplicationId()==applicationId)
			{
				answersList.add(answers);
			}
		}
		for(CloudableRule cloudableRule:cloudableListByClientId) {
			System.out.println("line 1");
			for(Answers answers:answersList) {
				System.out.println("line 2");
				if(cloudableRule.getQuestionId()==(answers.getQuestionId())) {
					System.out.println("cloudableRule.getQuestionId()"+cloudableRule.getQuestionId());
//					System.out.println("answers.getQuestionId()"+answers.getQuestionId());
					if(cloudableRule.getCloudableRule().contains(answers.getAnswerText())){
						answers.setCloudAbility(1);
						cloudableRuleFlag++;
						System.out.println(cloudableRule.getCloudableRule()+"cloudableRule.getCloudableRule()");
//					    System.out.println(answers.getAnswerText()+"answers.getAnswerText()");
        				}//else {
//						cloudableRuleFlag=0;break;}
					//cloudableQuestionFlag=1;
				}//else {cloudableQuestionFlag=0;}
			}
		}
		if(cloudableRuleFlag==cloudableListByClientId.size()){
			application.setCloudable("Yes");
			
			
		return true;}
		else {
			application.setCloudable("No");
			return false;}
	}
	
public boolean cloudProviderCheck(int applicationId){
		
		System.out.println("applicationId " + applicationId);
		
		int count = 0,numberOfRules = 0;
		List<Answers> allAnswers = new ArrayList<>();
		List<Answers> answers = new ArrayList<>();
		Application application=new Application();
		System.out.println("allAnswers** " + allAnswers);
		application = applicationRepository.findByApplicationId(applicationId);
		String clientName = application.getClientName();
		allAnswers = answerRepository.findAll();
		for (Answers getAnswers : allAnswers) {
			System.out.println("getAnswers.getQuestionId()*** "+getAnswers.getQuestionId());
			if(applicationId==getAnswers.getApplicationId())
			{
				System.out.println("true answerid");
				answers.add(getAnswers);
			}
			
		}
		System.out.println("Specific answers"+answers);
		
	
//		List<CloudProviderRule> cloudProviderRule = new ArrayList<>();
//		cloudProviderRule = cloudProviderRuleRepository.findAll();
//		System.out.println("cloudProviderRule"+cloudProviderRule);
//		numberOfRules = cloudProviderRule.size();
		System.out.println("numberOfRules"+numberOfRules);
		
		List<CloudProviderRule> cloudProviderRuleList=new ArrayList<CloudProviderRule>();
//		cloudProviderRuleList=cloudProviderRuleRepository.findAll();
		for(CloudProviderRule cloudProviderRuleClientName:cloudProviderRuleRepository.findAll()) {
			if(cloudProviderRuleClientName.getClientName().equals(clientName))
			{
				cloudProviderRuleList.add(cloudProviderRuleClientName);
				
			}
		}
		numberOfRules = cloudProviderRuleList.size();
		System.out.println("%%%%%%%%%%%" + cloudProviderRuleList);
		for (Answers userAnswers : answers) {
//			if(application.getClientName().equals(c))
			for(CloudProviderRule cloudProviderRules : cloudProviderRuleList)
			{
				
				if(userAnswers.getQuestionId() == Integer.parseInt(cloudProviderRules.getQuestionId()))
				{
					if(cloudProviderRules.getCloudProviderRule().contains(userAnswers.getAnswerText()))
					{
						count = count+1;
					}
					
				}
			}
			
		}
		
		System.out.println("count***  "+ count );
		
		if(count == numberOfRules)
		{
			application.setCloudProvider("GITC");
			application.setIsSaved(1);
			applicationRepository.save(application);
			System.out.println("CloudProvider is GITC");
			return false;
		}
		
		else 
		{
			application.setCloudProvider("AWS");
			application.setIsSaved(1);
			applicationRepository.save(application);
			System.out.println("CloudProvider is AWS");
			return true;
		}		
	}

	
	public void migrationCheck(int applicationId,int gitcCheck){
		Application app=new Application();
		app=applicationRepository.findByApplicationId(applicationId);
		app.setIsCloudable("true");
		applicationRepository.save(app);
		
		int migrationQuestionIdValue=0;
		int answerTextCount=0;
		int answerIdCount=0;
		boolean publicFalseCheck=true;
		boolean rehostFalseCheck=true;
		List<Answers> answerlist=new ArrayList<Answers>();
		List<MigrationRule> migrationRulelist=new ArrayList<MigrationRule>();
		Application application=applicationRepository.findByApplicationId(applicationId);
		String clientName = application.getClientName();
		Application application2=applicationRepository.findByApplicationId(applicationId);
		migrationRulelist=migrationRuleRepository.findAll();
		List<MigrationRule> migrationRuleByClientName = new ArrayList<MigrationRule>();
		for(MigrationRule migrationRuleAllRule:migrationRuleRepository.findAll())
		{
			if(migrationRuleAllRule.getClientName().equals(clientName))
			{
				migrationRuleByClientName.add(migrationRuleAllRule);
			}
		}
		
		
		for(Answers answers:answerRepository.findAll())
		{
			if(answers.getApplicationId()==applicationId)
			{
				if(answers.getAnswerText()!=null)
				{
					answerTextCount++;
				}
				answerIdCount++;
				answerlist.add(answers);
			}
		}
		if(answerTextCount==answerIdCount)
		{
		for(MigrationRule migrationRule:migrationRuleByClientName)
		{
			System.out.println("gitc check "+gitcCheck);
			if(gitcCheck!=0)
			{
				publicFalseCheck=false;
			}
			if(gitcCheck==0 &&publicFalseCheck==true && migrationRule.getMigrationId()==1001)//public-pass
			{
				for(Answers answers:answerlist) {
					migrationQuestionIdValue=Integer.parseInt(migrationRule.getQuestionId());
					if(migrationQuestionIdValue==answers.getQuestionId())
					{
						publicFalseCheck=migrationRule.getMigrationRule().contains(answers.getAnswerText());
							if(publicFalseCheck)
								{
										if(gitcCheck!=0)
										{
											publicFalseCheck=false;
										}
										else
										{
											application.setApplicationId(applicationId);
											application.setMigrationPattern("public-pass");
											application.setIsFinalize(1);
											application.setAssessment(true);
											application.setIsSaved(1);
											applicationRepository.save(application);
										}
								}
							else {
								publicFalseCheck=false;
								break;
							}
							
					}
				}	
				if(publicFalseCheck==false)
				{
					System.out.println("break works in public paas");
				}
			}
			if(publicFalseCheck==false && rehostFalseCheck==true && migrationRule.getMigrationId()==1002)//Rehost
			{
				System.out.println(migrationRule.getMigrationRule()+"^^^^^^^^^^^^^^Rehost");
				System.out.println("answerssssssssss"+answerlist);
				for(Answers answers:answerlist) {
					migrationQuestionIdValue=Integer.parseInt(migrationRule.getQuestionId());
					System.out.println(answers.getAnswerText());
					if(migrationQuestionIdValue==answers.getQuestionId())
					{
						System.out.println(migrationRule.getMigrationRule()+"==="+answers.getAnswerText());
						System.out.println(answers.getAnswerText()+"===="+migrationRule.getMigrationRule());
						rehostFalseCheck=answers.getAnswerText().contains(migrationRule.getMigrationRule());
						System.out.println(rehostFalseCheck);
							if(rehostFalseCheck)
								{
										System.out.println("Rehost");
										application2.setApplicationId(applicationId);
										application2.setMigrationPattern("Rehost");
										application.setIsSaved(1);
										application.setAssessment(true);
										application.setIsFinalize(1);
										applicationRepository.save(application);
										System.out.println(application);
								}
							else {
								rehostFalseCheck=false;
								System.out.println(rehostFalseCheck+" is rehostFalseCheck");
								break;
							}
					}
				}
				if(rehostFalseCheck==false)
				{
					System.out.println("break works in rehost");
					application.setApplicationId(applicationId);
					application.setIsSaved(1);
					application.setAssessment(true);
					application.setIsFinalize(1);
					application.setMigrationPattern("Re-Plateform");
					applicationRepository.save(application);
				}
			}
			else if(rehostFalseCheck==false)
				{
				System.out.println(migrationRule.getMigrationRule()+"^^^^^^^^^^^^^^Replateform");
					System.out.println("replateform");
					application.setApplicationId(applicationId);
					application.setIsSaved(1);
					application.setIsFinalize(1);
					application.setAssessment(true);
					application.setMigrationPattern("Re-Plateform");
					applicationRepository.save(application);
				}
			}
	}else {
		
				System.out.println("No answers present for given application!!!!!");
				application.setApplicationId(applicationId);
				application.setMigrationPattern("Re-Plateform");;
				application.setIsSaved(0);
				applicationRepository.save(application);
		  }
		}
	
	public void summaryReport() throws FileNotFoundException{
		
		int summaryReportCount=1;
		List<SummaryReport> summaryReportList=new ArrayList<SummaryReport>();
		List<Application> appList=new ArrayList<Application>();
		List<AssessmentQuestions> assessmentQuestionsList=new ArrayList<AssessmentQuestions>();
		for(AssessmentQuestions assessmentQuestions:assessmentQuestionsRepository.findAll())
		{
			if("true".equals(assessmentQuestions.getAssessmentTypeForCloudable()))
			{
				assessmentQuestionsList.add(assessmentQuestions);
			}
		}
		
//   		System.out.println("Question List=="+assessmentQuestionsList);
//		System.out.println("*********************************************************************************");

			
		for(Application application:applicationRepository.findAll()) {
			List<Answers> answerList=new ArrayList<Answers>();
			
			
			if(application.getIsFinalize()==1)
			{
				appList.add(application);
//				System.out.println("appList"+appList);
//				System.out.println("##################################################");
				
				for(AssessmentQuestions assessmentQuestions:assessmentQuestionsList) 
				{
						
						for(Answers answer:answerRepository.findAll())
						{
							if(answer.getApplicationId()==application.getApplicationId())
							{
								if(answer.getQuestionId()==assessmentQuestions.getQuestionId())
									{
										answerList.add(answer);
									}
							}
						}
//						System.out.println("Answer list"+answerList);
						
				}
				
				List<String> answerTextList=new ArrayList<String>();
				for(Answers answer:answerList)
				{
					answerTextList.add(answer.getAnswerText());
				}
				//summaryReport.setAnswerText(answerTextList);
				
				List<String> questionTextList=new ArrayList<String>();
				for(AssessmentQuestions assessmentQuestions:assessmentQuestionsList)
				{
					
					SummaryReport summaryReport=new SummaryReport();
					summaryReport.setApplicationName(application.getApplicationName());
					summaryReport.setApplicationDescription(application.getApplicationDescription());
					for(Answers answer:answerList)
					{
						if(answer.getQuestionId()==assessmentQuestions.getQuestionId())
						{
							summaryReport.setAnswerText(answer.getAnswerText());
							System.out.println("*****");
							if(answer.isCloudAbility()==1)
							{
								summaryReport.setCloudability("1");
							}
							else
							{
								summaryReport.setCloudability("0");
							}
							
						}
						
					}
					summaryReport.setQuestionText(assessmentQuestions.getQuestionText());
					
					
					summaryReport.setAssessment_type("Yes");
					summaryReportList.add(summaryReport);
					System.out.println("summary report*********"+summaryReportList);
				}
				
				JRBeanCollectionDataSource jds=new JRBeanCollectionDataSource(summaryReportList);
				Map<String,Object> parametres=new HashMap<String,Object>();
				parametres.put("ItemDataSource", jds);
				
				InputStream reportStream = new FileInputStream("\\Users\\priyanj\\Volkswagen\\jasperTemplate\\template.jrxml");
				JasperReport report;
				try {
					report = JasperCompileManager.compileReport(reportStream);
				System.out.println("compiled");
						//HashMap jasperParameter = new HashMap();
					    // jasperParameter.put("reportTitle", "Cloud Survey Report");

					     JasperPrint jasperPrint = JasperFillManager.fillReport(report,parametres, jds);
					     System.out.println("filled");
					 JasperExportManager.exportReportToPdfFile(jasperPrint, "/hsjd/CloudRreport"+summaryReportCount+".pdf");
					 summaryReportCount++;
					 System.out.println("pdf done");
				} catch (JRException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
//				summaryReportList.remove(summaryReport);
				System.out.println("After Removing "+summaryReportList);
				
		}
			summaryReportList.clear();
			System.out.println(summaryReportList+"**************************");
		}
//		JRBeanCollectionDataSource jds=new JRBeanCollectionDataSource(summaryReportList);
//		Map<String,Object> parametres=new HashMap<String,Object>();
//		parametres.put("ItemDataSource", jds);
//		
//		InputStream reportStream = new FileInputStream("\\Users\\priyanj\\Volkswagen\\jasperTemplate\\template.jrxml");
//		JasperReport report;
//		try {
//			report = JasperCompileManager.compileReport(reportStream);
//		System.out.println("compiled");
//				//HashMap jasperParameter = new HashMap();
//			    // jasperParameter.put("reportTitle", "Cloud Survey Report");
//
//			     JasperPrint jasperPrint = JasperFillManager.fillReport(report,parametres, jds);
//			     System.out.println("filled");
//			 JasperExportManager.exportReportToPdfFile(jasperPrint, "/hsjd/CloudRreport"+"jk.pdf");
//			 System.out.println("pdf done");
//		} catch (JRException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
//		  System.out.println("Application List=="+appList);
		
	}

}