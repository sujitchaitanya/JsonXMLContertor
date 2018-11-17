package com.risksense.converters;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Factory class for creating instances of {@link XMLJSONConverterI}.
 */
@Component
public final class ConverterFactory {

	private static BeanFactory beanFactory;
	private static String beanName;

	@Autowired(required = true)
	public void setBeanFactory(BeanFactory beanFactory) {
		ConverterFactory.beanFactory = beanFactory;
	}
	
	@Value("${converterBeanName}")
	public void setBeanName(String beanName) {
		ConverterFactory.beanName = beanName;
	}

	/**
	 * You should implement this method having it return your version of
	 * {@link com.risksense.converters.XMLJSONConverterI}.
	 *
	 * @return {@link com.risksense.converters.XMLJSONConverterI} implementation you
	 *         created.
	 */
	public static final XMLJSONConverterI createXMLJSONConverter() {
		return beanFactory.getBean(beanName, XMLJSONConverterI.class);
	}
}
