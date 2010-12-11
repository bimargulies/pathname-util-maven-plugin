/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
/* Copyright (c) 2010 Basis Technology Corp. */
package com.basistech.oss.pump;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.maven.it.Verifier;
import org.apache.maven.it.util.ResourceExtractor;

import org.junit.Test;

/**
 * 
 */
public class PumpAbsolutizeMojoTest {
	private static final String TEST_GROUP_ID = "com.basistech.oss.pump";

	@Test
	public void testMyPlugin() throws Exception {
		File testDir = ResourceExtractor.simpleExtractResources(getClass(),
				"pom.xml");
		testDir = testDir.getParentFile();
		Verifier verifier;

		/*
		 * We must first make sure that any artifact created by this test has
		 * been removed from the local repository. Failing to do this could
		 * cause unstable test results. Fortunately, the verifier makes it easy
		 * to do this.
		 */
		verifier = new Verifier(testDir.getAbsolutePath());
		verifier.deleteArtifact(TEST_GROUP_ID, "pump-test", "1-SNAPSHOT", "pom");

		/*
		 * The Command Line Options (CLI) are passed to the verifier as a list.
		 * This is handy for things like redefining the local repository if
		 * needed. In this case, we use the -N flag so that Maven won't recurse.
		 * We are only installing the parent pom to the local repo here.
		 */
		List<String> cliOptions = new ArrayList<String>();
		cliOptions.add("-N");
		cliOptions.add("-X");
		// use the debug messages to check for what we want.
		verifier.setMavenDebug(true);
		verifier.executeGoal("install");

		/*
		 * This is the simplest way to check a build succeeded. It is also the
		 * simplest way to create an IT test: make the build pass when the test
		 * should pass, and make the build fail when the test should fail. There
		 * are other methods supported by the verifier. They can be seen here:
		 * http://maven.apache.org/shared/maven-verifier/apidocs/index.html
		 */
		verifier.verifyErrorFreeLog();
		String toFind = "Setting path1.abs: ";
		File f1 = new File(testDir, "rightHere");
		toFind = toFind + f1.getCanonicalPath();
		verifier.verifyTextInLog(toFind);
		File f2 = new File(testDir.getParentFile(), "upOne");
		toFind = "Setting path2.abs: ";
		toFind = toFind + f2.getCanonicalPath();
		verifier.verifyTextInLog(toFind);
		/*
		 * Reset the streams before executing the verifier again.
		 */
		verifier.resetStreams();
	}
}
