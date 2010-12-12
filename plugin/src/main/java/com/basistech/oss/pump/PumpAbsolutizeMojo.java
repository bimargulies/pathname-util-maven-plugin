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
 * @description set a property to the base dir of some parent. Useful for finding resources relative to a parent.
 * @goal findParentBase
 * @phase validate
 * @inheritByDefault true
 * @requiresProject true
 */
public class PumpAbsolutizeMojo extends AbstractMojo {

	/**
	 * @parameter
	 * @required
	 * @description the group id of the parent for which you want a base directory.
	 */
	private String groupId;
	/**
	 * @parameter
	 * @required
	 * @description the artifactId of the parent for which you want a base directory.
	 */

	private String artifactId;
    
    /**
     * @parameter
     * @required
     * @description The property to set to the baseDirectory.
     */
    private String propertyName;
    
    /**
     * @parameter expression="${project}"
     * @required
     */
    private MavenProject project;

	/** {@inheritDoc}*/
	public void execute() throws MojoExecutionException, MojoFailureException {
		for (MavenProject proj = project; proj != null; proj = proj.getParent()) {
			if(groupId.equals(proj.getGroupId())
					&& artifactId.equals(proj.getArtifactId())) {
				String absBase = proj.getBasedir().getAbsolutePath();
				project.getProperties().put(propertyName, absBase);
				getLog().info("Setting " + propertyName + " to " + absBase);
				return;
			}
		}
		throw new MojoExecutionException("No parent had specified group:artifact");
	}
}
