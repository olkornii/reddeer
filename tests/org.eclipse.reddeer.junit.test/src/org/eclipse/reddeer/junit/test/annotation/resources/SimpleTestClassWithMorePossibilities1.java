/*******************************************************************************
 * Copyright (c) 2017 Red Hat, Inc and others.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Red Hat, Inc - initial API and implementation
 *******************************************************************************/
package org.eclipse.reddeer.junit.test.annotation.resources;

public class SimpleTestClassWithMorePossibilities1 {
	
	@CustomMethodAnnotation
	public static String getValue() {
		return "value";
	}
	
	public SimpleTestClassWithMorePossibilities1() {
		
	}

}
