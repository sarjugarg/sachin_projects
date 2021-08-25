package com.gl.reader.configuration;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.stereotype.Component;

@Component
//@PropertySource("classpath:application.properties")
@PropertySources({
//@PropertySource(value = {"file:${spring.config.location}/application.properties"}, ignoreResourceNotFound = true),
@PropertySource(value = {"classpath:application.properties"}, ignoreResourceNotFound = true)
})
public class PropertiesReader {

	@Value("${spring.jpa.properties.hibernate.dialect}")
	public String dialect;
	
	@Value("${COMMA-DELIMITER}")
	public String commaDelimiter;
	
	@Value("${SEMI-COLON-DELIMITER}")
	public String semiColonDelimiter;
	
	@Value("${NEW-LINE-SEPARATOR}")
	public String newLineSeprator;
	
	@Value("${FILE-HEADER}")
	public String fileHeader;
	
	@Value("${TYPE-OF-PROCESS}")
	public String typeOfProcess;
	
	@Value("${FILES-COUNT-PER-REPORT}")
	public Long filesCount;
	
        @Value("${SERVERNAME}")
	public String servername;
        
        
	@Value("${EXTENSION}")
	public String extension;
	
	@Value("${SLEEP-TIME}")
	public Integer sleepTime;
	
	@Value("${INPUT-LOCATION}")
	public String inputLocation;
	
	@Value("${OUTPUT-LOCATION}")
	public String outputLocation;
	
	@Value("${ERROR-REPORT-FLAG}")
	public String errorReportFlag;
	
	@Value("${ROW-COUNT-FOR-SPLIT}")
	public Integer rowCountForSplit;
	
	@Value("#{'${REPORT-TYPE}'.split(',')}") 
	public Set<String> reportType;
	
	@Value("#{'${FILE-PATTERN}'.split(',')}") 
	public List<String> filePattern;
}
