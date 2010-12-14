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
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

import org.junit.Assert;
import org.junit.Test;

import org.apache.maven.it.Verifier;
import org.apache.maven.it.util.ResourceExtractor;

/**
 * 
 */
public class PumpFindParentBasedirMojoTest extends Assert {
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

		verifier.executeGoal("install");

		/*
		 * This is the simplest way to check a build succeeded. It is also the
		 * simplest way to create an IT test: make the build pass when the test
		 * should pass, and make the build fail when the test should fail. There
		 * are other methods supported by the verifier. They can be seen here:
		 * http://maven.apache.org/shared/maven-verifier/apidocs/index.html
		 */
		verifier.verifyErrorFreeLog();
		///Users/benson/.m2/repository/com/basistech/oss/pump/pump-test-child/1-SNAPSHOT/pump-test-child-1-SNAPSHOT.pom
		String path = verifier.getArtifactPath("com.basistech.oss.pump", "pump-test-child", "1-SNAPSHOT", "jar");
		JarFile jf = new JarFile(path);
		ZipEntry e = jf.getEntry("hiThere.txt");
		assertNotNull(e);
		
		/*
		 * Reset the streams before executing the verifier again.
		 */
		verifier.resetStreams();
	}
}
