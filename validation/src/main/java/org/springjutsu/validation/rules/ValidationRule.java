/*
 * Copyright 2010-2013 Duplichien, Wicksell, Springjutsu.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springjutsu.validation.rules;

import java.util.ArrayList;

/**
 * Java representation of an XML validation rule.
 * @author Clark Duplichien
 * @author Taylor Wicksell
 *
 */
public class ValidationRule extends AbstractRuleHolder {
	
	/**
	 * Path to the field to validate.
	 */
	protected String path;
	
	/**
	 * Name of the rule executor, implies the 
	 * type of rule to apply.
	 */
	protected String type;
	
	/**
	 * This is the argument to be passed
	 * to the rule executor.
	 */
	protected String value;
	
	/**
	 * An optional message code which may be used to 
	 * resolve a non-default error message (a message 
	 * other than that which is coded for the rule executor). 
	 */
	protected String message;
	
	/**
	 * The path on which the error should be written 
	 * on the @link{Errors} object. In case you want to 
	 * validate one field, but put the error message onto
	 * another field.
	 */
	protected String errorPath;
	
	/**
	 * Indicates whether the rule should be applied to individual
	 * collection members, or to the collection object itself.
	 */
	protected CollectionStrategy collectionStrategy;
	
	/**
	 * Indicates an explicit failure mode for the validation rule,
	 * e.g. to optionally allow the rule to produce an error even 
	 * if it has children 
	 */
	protected RuleErrorMode onFail;
	
	/**
	 * Default constructor, utilized by @link{ValidationDefinitionParser}
	 * @param path See path docs.
	 * @param type See type docs.
	 * @param value See value docs.
	 */
	public ValidationRule(String path, String type, String value) {
		this.path = path;
		this.type = type;
		this.value = value;
		setRules(new ArrayList<ValidationRule>());
		setTemplateReferences(new ArrayList<ValidationTemplateReference>());
		setValidationContexts(new ArrayList<ValidationContext>());
	}
	
	/**
	 * @return true if there are nested validation rules.
	 */
	public boolean hasChildren() {
		return (getRules() != null && !getRules().isEmpty())
			|| (getTemplateReferences() != null && !getTemplateReferences().isEmpty());
	}
	
	/**
	 * The toString() representation is a reconstruction of 
	 * the XML syntax of the validation rule, minus any
	 * nested validation rules.
	 */
	@Override
	public String toString() {
		String rule = "<rule ";
		if (path != null && path.length() > 0) {
			rule += "path=\"" + path + "\" ";
		}
		if (type != null && type.length() > 0) {
			rule += "type=\"" + type + "\" ";
		}
		if (value != null && value.length() > 0) {
			rule += "value=\"" + value + "\" ";
		}
		if (message != null && message.length() > 0) {
			rule += "message=\"" + message + "\" "; 
		}
		if (errorPath != null && errorPath.length() > 0) {
			rule += "errorPath=\"" + errorPath + "\" "; 
		}
		if (collectionStrategy != null) {
			rule += "collectionStrategy=\"" + collectionStrategy.getXmlValue() + "\" ";
		}
		if (onFail != null) {
			rule += "onFail=\"" + onFail.getXmlValue() + "\" ";
		}
		rule += "/>";
		return rule;
	}
	
	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * @param path the path to set
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * @return the type.
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set.
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the value / argument
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value the value / argument to set
	 */
	public void setValue(String value) {
		this.value = value;
	}
	
	/**
	 * @return the message
	 */
	public synchronized String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public synchronized void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return the errorPath
	 */
	public String getErrorPath() {
		return errorPath;
	}

	/**
	 * @param errorPath the errorPath to set
	 */
	public void setErrorPath(String errorPath) {
		this.errorPath = errorPath;
	}
	
	public CollectionStrategy getCollectionStrategy() {
		return collectionStrategy;
	}

	public void setCollectionStrategy(CollectionStrategy collectionStrategy) {
		this.collectionStrategy = collectionStrategy;
	}

	public RuleErrorMode getOnFail() {
		return onFail;
	}

	public void setOnFail(RuleErrorMode onFail) {
		this.onFail = onFail;
	}

}
