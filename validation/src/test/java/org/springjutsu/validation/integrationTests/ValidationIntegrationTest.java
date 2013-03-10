package org.springjutsu.validation.integrationTests;

import java.util.Set;

import org.junit.After;
import org.junit.Rule;
import org.junit.rules.TestName;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springjutsu.validation.ValidationManager;
import org.springjutsu.validation.context.ValidationContextHandler;
import org.springjutsu.validation.spel.SPELResolver;

public abstract class ValidationIntegrationTest {
	
	@Rule
	public static TestName TEST_NAME = new TestName();
	
	protected static final String xmlDirectory = 
		"org/springjutsu/validation/integration/";
	
	protected TestResult doValidate(String configXml, Object validateMe) {
		ApplicationContext context =
		    new ClassPathXmlApplicationContext(new String[] {
		    	xmlDirectory + getXmlSubdirectory() + "/" + configXml});
		ValidationManager manager = context.getBean(ValidationManager.class);
		return new TestResult(manager.validate(validateMe), context.getBean(MessageSource.class));
	}
	
	protected TestResult doValidate(String configXml, Object validateMe, Object[] groups) {
		ApplicationContext context =
		    new ClassPathXmlApplicationContext(new String[] {
		    	xmlDirectory + getXmlSubdirectory() + "/" + configXml});
		ValidationManager manager = context.getBean(ValidationManager.class);
		Errors errors = new BeanPropertyBindingResult(validateMe, "validationTarget");
		manager.validate(validateMe, errors, groups);
		return new TestResult(errors, context.getBean(MessageSource.class));
	}
	
	protected abstract String getXmlSubdirectory();
	
	protected class TestResult {
		public Errors errors;
		public MessageSource messageSource;
		public TestResult(Errors errors, MessageSource messageSource) {
			this.errors = errors;
			this.messageSource = messageSource;
		}
	}
	
	@After
	public void cleanupRequests() {
		RequestContextHolder.setRequestAttributes(null);
		org.springframework.webflow.execution.RequestContextHolder.setRequestContext(null);
	}
	
	protected void setCurrentFormPath(String path) {
		MockHttpServletRequest request = new MockHttpServletRequest("POST", path);
		request.setServletPath(path);
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request), true);
	}
	
	public static class TestNameContextHandler implements ValidationContextHandler {
		
		@Override
		public boolean isActive(Set<String> qualifiers, Object rootModel, String[] validationHints) {
			return ContextIntegrationTest.TEST_NAME != null
				&& ContextIntegrationTest.TEST_NAME.getMethodName() != null
				&& qualifiers.contains(ContextIntegrationTest.TEST_NAME.getMethodName());
		}

		@Override
		public boolean enableDuringSubBeanValidation() {
			return false;
		}

		@Override
		public void initializeSPELResolver(SPELResolver spelResolver) {
			spelResolver.getScopedContext().addContext("jUnitTestName", ContextIntegrationTest.TEST_NAME);
		}
		
	}
	
}
