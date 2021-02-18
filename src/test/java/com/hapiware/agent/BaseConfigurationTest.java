package com.hapiware.agent;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Element;

import com.hapiware.agent.Agent.ConfigElements;


public class BaseConfigurationTest
	extends
		TestBase
{
	private static final String FILENAME = BASEDIR + "agent-config-none.xml";
	
	
	@Before
	public void setup() throws ParserConfigurationException
	{
		super.setup();
 	}

	@Test
	public void normalSituation() throws IOException
	{
		File file = createTemporaryConfigDocumentOnDisc(configDoc);
		Map<String, String> agentParams = new HashMap<>();
		agentParams.put(Agent.AGENT_CONFIGURATION_PATH, file.getCanonicalPath());
		ConfigElements configElements = Agent.readConfigurationFile(agentParams);
		assertEquals("com.hapiware.agent.AgentTest", configElements.getDelegateAgentName());
		URL[] agentClasspathUrls = configElements.getClasspaths();
		assertEquals(new File(".").toURI().toURL(), agentClasspathUrls[0]);
		assertEquals(new File(System.getProperty("user.home")).toURI().toURL(), agentClasspathUrls[1]);
		assertEquals(new File("/").toURI().toURL(), agentClasspathUrls[2]);
		assertEquals(1, configElements.getIncludePatterns().length);
		assertEquals(".+", configElements.getIncludePatterns()[0].toString());
		assertEquals(0, configElements.getExcludePatterns().length);
		file.delete();
	}
	
	@Test(expected=Agent.ConfigurationError.class)
	public void configurationFileDoesNotExist()
	{
		Map<String, String> agentParams = new HashMap<>();
		agentParams.put(Agent.AGENT_CONFIGURATION_PATH, "this@F1le-name-does-not-@%½½½-surely-exist");
		Agent.readConfigurationFile(agentParams);
	}
	
	@Test(expected=Agent.ConfigurationError.class)
	public void configurationFileIsNotDefined()
	{
		Map<String, String> agentParams = new HashMap<>();
		agentParams.put(Agent.AGENT_CONFIGURATION_PATH, null);
		Agent.readConfigurationFile(agentParams);
	}
	
	@Test
	public void configurationTagIsMissing()
	{
		ConfigElements configElements =
			Agent.readDOMDocument(configDoc, this.getClass().toString(), new HashMap<String, String>());
		Object obj = Agent.unmarshall(this.getClass(), configElements);
		assertEquals(null, obj);
	}

	@Test(expected=Agent.ConfigurationError.class)
	public void delegateIsMissing()
	{
		Element currentDelegate = (Element)agent.getFirstChild();
		Element newDelegate = configDoc.createElement("delegate");
		agent.replaceChild(newDelegate, currentDelegate);

		Agent.readDOMDocument(configDoc, this.getClass().toString(), new HashMap<String, String>());
	}

	@Test(expected=Agent.ConfigurationError.class)
	public void classPathEntryIsMissing()
	{
		Element currentEntry = (Element)classpath.getLastChild();
		Element newEntry = configDoc.createElement("entry");
		classpath.replaceChild(newEntry, currentEntry);

		Agent.readDOMDocument(configDoc, this.getClass().toString(), new HashMap<String, String>());
	}
	
	@Test
	public void readFromFile()
	{
		Map<String, String> agentParams = new HashMap<>();
		agentParams.put(Agent.AGENT_CONFIGURATION_PATH, FILENAME);
		ConfigElements configElements = Agent.readConfigurationFile(agentParams);
		assertBasicConfiguration(configElements);
	}
}
