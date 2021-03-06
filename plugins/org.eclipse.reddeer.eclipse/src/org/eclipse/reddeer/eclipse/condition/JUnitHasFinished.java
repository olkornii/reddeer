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
package org.eclipse.reddeer.eclipse.condition;

import java.util.regex.Pattern;

import org.hamcrest.core.StringContains;
import org.eclipse.reddeer.common.condition.AbstractWaitCondition;
import org.eclipse.reddeer.eclipse.jdt.junit.ui.TestRunnerViewPart;
import org.eclipse.reddeer.workbench.core.condition.JobIsRunning;

/**
 * Wait condition for detecting whether a JUnit run has finished.
 * 
 * An error may occurs if the run writes to the Console view. Make sure that you
 * have disabled activating the Console view.
 * 
 * @author apodhrad
 * 
 */
public class JUnitHasFinished extends AbstractWaitCondition {

	private TestRunnerViewPart junitView;
	private JobIsRunning junitJobIsRunning;

	/**
	 * Construct the wait condition.
	 */
	public JUnitHasFinished() {
		junitView = new TestRunnerViewPart();
		junitJobIsRunning = new JobIsRunning(StringContains.containsString("JUnit"), false);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.reddeer.common.condition.WaitCondition#test()
	 */
	@Override
	public boolean test() {
		junitView.open();
		String status = junitView.getRunStatus();

		java.util.regex.Matcher statusMatcher = Pattern.compile("([0-9]+)/([0-9]+).*").matcher(status);
		if (statusMatcher.matches()) {
			int numberOfFinishedTests = Integer.valueOf(statusMatcher.group(1));
			int numberOfAllTests = Integer.valueOf(statusMatcher.group(2));
			if (numberOfFinishedTests > 0 && numberOfFinishedTests == numberOfAllTests) {
				return !junitJobIsRunning.test();
			}
		}

		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.reddeer.common.condition.AbstractWaitCondition#description()
	 */
	@Override
	public String description() {
		return "JUnit test has not finished yet";
	}

}
