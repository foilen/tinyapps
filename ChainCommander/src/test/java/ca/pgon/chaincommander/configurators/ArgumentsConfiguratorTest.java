/*
    Tinyapps https://github.com/provirus/tinyapps
    Copyright (C) 2014

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
 */
package ca.pgon.chaincommander.configurators;

import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import ca.pgon.chaincommander.configurators.exceptions.BadArgumentsException;

/**
 * @author Simon Levesque
 * 
 */
public class ArgumentsConfiguratorTest {

	private ArgumentsConfigurator argumentsConfigurator;
	private Map<String, String> configMap;

	@Before
	public void setUp() {
		argumentsConfigurator = new ArgumentsConfigurator();
		configMap = new HashMap<String, String>();
	}

	@Test
	public void testGet100Percent() {
		String[] someArgs = new String[] { "a" };
		argumentsConfigurator.setArgs(someArgs);
		Assert.assertEquals(someArgs, argumentsConfigurator.getArgs());
		argumentsConfigurator.configure(null);
	}

	/**
	 * Test method for
	 * {@link ca.pgon.chaincommander.configurators.ArgumentsConfigurator#configure(java.util.Map)}
	 * .
	 */
	@Test
	public void testConfigureNothing() {
		argumentsConfigurator.configure(configMap);
		Assert.assertFalse(ConfigManager.validateAllConfigured(configMap));
	}

	/**
	 * Test method for
	 * {@link ca.pgon.chaincommander.configurators.ArgumentsConfigurator#configure(java.util.Map)}
	 * .
	 */
	@Test(expected = BadArgumentsException.class)
	public void testConfigureUnknown() {
		argumentsConfigurator.setArgs(new String[] { "-unknown" });
		argumentsConfigurator.configure(configMap);
	}

	/**
	 * Test method for
	 * {@link ca.pgon.chaincommander.configurators.ArgumentsConfigurator#configure(java.util.Map)}
	 * .
	 */
	@Test(expected = BadArgumentsException.class)
	public void testConfigureNotStartingWithDash() {
		argumentsConfigurator.setArgs(new String[] { "unknown" });
		argumentsConfigurator.configure(configMap);
	}

	/**
	 * Test method for
	 * {@link ca.pgon.chaincommander.configurators.ArgumentsConfigurator#configure(java.util.Map)}
	 * .
	 */
	@Test
	public void testConfigureMasterComplete() {
		argumentsConfigurator.setArgs(new String[] { "-m", "-p", "1234" });
		argumentsConfigurator.configure(configMap);
		Assert.assertTrue(ConfigManager.validateAllConfigured(configMap));
	}

	/**
	 * Test method for
	 * {@link ca.pgon.chaincommander.configurators.ArgumentsConfigurator#configure(java.util.Map)}
	 * .
	 */
	@Test
	public void testConfigureSlaveIncomplete() {
		argumentsConfigurator.setArgs(new String[] { "-s" });
		argumentsConfigurator.configure(configMap);
		Assert.assertFalse(ConfigManager.validateAllConfigured(configMap));
	}

	/**
	 * Test method for
	 * {@link ca.pgon.chaincommander.configurators.ArgumentsConfigurator#configure(java.util.Map)}
	 * .
	 */
	@Test(expected = BadArgumentsException.class)
	public void testConfigureSlaveMissingHost() {
		argumentsConfigurator.setArgs(new String[] { "-s", "-h" });
		argumentsConfigurator.configure(configMap);
	}

	/**
	 * Test method for
	 * {@link ca.pgon.chaincommander.configurators.ArgumentsConfigurator#configure(java.util.Map)}
	 * .
	 */
	@Test(expected = BadArgumentsException.class)
	public void testConfigureSlaveMissingPort() {
		argumentsConfigurator.setArgs(new String[] { "-s", "-h", "127.0.0.1:1234", "-p" });
		argumentsConfigurator.configure(configMap);
		Assert.assertTrue(ConfigManager.validateAllConfigured(configMap));
	}

	/**
	 * Test method for
	 * {@link ca.pgon.chaincommander.configurators.ArgumentsConfigurator#configure(java.util.Map)}
	 * .
	 */
	@Test(expected = BadArgumentsException.class)
	public void testConfigureSlaveBadHostArguments() {
		argumentsConfigurator.setArgs(new String[] { "-h", "-s" });
		argumentsConfigurator.configure(configMap);
	}

	/**
	 * Test method for
	 * {@link ca.pgon.chaincommander.configurators.ArgumentsConfigurator#configure(java.util.Map)}
	 * .
	 */
	@Test(expected = BadArgumentsException.class)
	public void testConfigureSlaveBadPortArguments() {
		argumentsConfigurator.setArgs(new String[] { "-s", "-h", "127.0.0.1:1234", "-p", "-missing" });
		argumentsConfigurator.configure(configMap);
	}

	/**
	 * Test method for
	 * {@link ca.pgon.chaincommander.configurators.ArgumentsConfigurator#configure(java.util.Map)}
	 * .
	 */
	@Test
	public void testConfigureSlaveComplete() {
		argumentsConfigurator.setArgs(new String[] { "-s", "-h", "127.0.0.1:1234", "-p", "1234" });
		argumentsConfigurator.configure(configMap);
		Assert.assertTrue(ConfigManager.validateAllConfigured(configMap));
	}

}
