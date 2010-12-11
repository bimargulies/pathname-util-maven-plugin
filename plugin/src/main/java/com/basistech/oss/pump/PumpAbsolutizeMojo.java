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
import java.io.IOException;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;

/**
 * @description Absolutize pathname in a property
 * @goal absolutize
 * @phase validate
 * @inheritByDefault false
 * @requiresProject true
 */
public class PumpAbsolutizeMojo extends AbstractMojo {
	
	 /**
     * Properties to process.
     * @required
     * @parameter 
     * @description A collection of property names. For each such property, this goal
     * will create a new property with the name ending '.abs' that contains the absolute
     * form of the pathname.
     */
    private String[] propertyNames;
    
    /**
     * @parameter
     * @description The base directory for the pathnames. The default is project.basedir.
     */
    private File baseDirectory;
    
    /**
     * @parameter expression="${project}"
     * @required
     */
    private MavenProject project;

	/** {@inheritDoc}*/
	public void execute() throws MojoExecutionException, MojoFailureException {
		if (baseDirectory == null) {
			baseDirectory = project.getBasedir();
		}
		getLog().debug("Base directory: " + baseDirectory.getAbsolutePath());
		for (String prop : propertyNames) {
			String path = (String)project.getProperties().get(prop);
			File afile = new File(baseDirectory, path);
			String newPropName = prop + ".abs";
			String absPath;
			try {
				absPath = afile.getCanonicalPath();
			} catch (IOException e) {
				throw new MojoExecutionException("Failed to get canonical pathname for " + afile.getAbsolutePath(), e);
			}
			project.getProperties().put(newPropName, absPath);
			getLog().debug("Setting " + newPropName + ": " + absPath);
		}
	}
}
