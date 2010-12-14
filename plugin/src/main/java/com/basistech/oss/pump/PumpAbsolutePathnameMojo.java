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
 * @description Set properties to the canonical pathnames of other properties. If the input property name is x,
 *              the output is x.abs.
 * @goal absolutize
 * @phase validate
 * @inheritByDefault true
 * @requiresProject true
 */
public class PumpAbsolutePathnameMojo extends AbstractMojo {

    /**
     * @parameter
     * @required
     * @description Names of the properties to make into absolute-pathname properties.
     */

    private String[] propertyNames;
    
    /**
     * @parameter
     * @description True to get a log message for each property.
     */
    private boolean verbose;

    /**
     * @parameter expression="${project}"
     * @required
     */
    private MavenProject project;

    /** {@inheritDoc} */
    public void execute() throws MojoExecutionException, MojoFailureException {
        for (String prop : propertyNames) {
            String path = (String)project.getProperties().get(prop);
            String canon = null;
            if (path == null) {
                getLog().error("No value for property " + prop);
            } else {
                File f = new File(path);
                try {
                    canon = f.getCanonicalPath();
                } catch (IOException e) {
                    throw new MojoExecutionException("Failed to canonicalize " + f.getPath(), e);
                }
                project.getProperties().put(prop + ".abs", canon);
                if (verbose) {
                    getLog().info("Setting " + prop + ".abs to " + canon);
                }
            }
        }
    }
}
