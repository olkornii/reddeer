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
package org.eclipse.reddeer.jface.preference;

import org.eclipse.swt.custom.CLabel;
import org.hamcrest.Matcher;
import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.common.wait.WaitUntil;
import org.eclipse.reddeer.common.wait.WaitWhile;
import org.eclipse.reddeer.core.condition.WidgetIsFound;
import org.eclipse.reddeer.core.lookup.ShellLookup;
import org.eclipse.reddeer.core.matcher.WithMnemonicTextMatcher;
import org.eclipse.reddeer.core.matcher.WithTextMatcher;
import org.eclipse.reddeer.jface.condition.WindowIsAvailable;
import org.eclipse.reddeer.jface.window.AbstractWindow;
import org.eclipse.reddeer.jface.window.Openable;
import org.eclipse.reddeer.swt.api.Button;
import org.eclipse.reddeer.swt.api.Shell;
import org.eclipse.reddeer.swt.api.TreeItem;
import org.eclipse.reddeer.swt.impl.button.CancelButton;
import org.eclipse.reddeer.swt.impl.button.OkButton;
import org.eclipse.reddeer.swt.impl.button.PushButton;
import org.eclipse.reddeer.swt.impl.clabel.DefaultCLabel;
import org.eclipse.reddeer.swt.impl.shell.DefaultShell;
import org.eclipse.reddeer.swt.impl.tree.DefaultTree;
import org.eclipse.reddeer.swt.impl.tree.DefaultTreeItem;

/**
 * Preference dialog implementation. 
 * 
 * @author Lucia Jelinkova
 *
 */
public class PreferenceDialog extends AbstractWindow{
	
	public PreferenceDialog(String text) {
		super(text);
	}

	public PreferenceDialog(Shell shell) {
		super(shell);
	}


	public PreferenceDialog(Matcher<?>...matchers) {
		super(matchers);
	}
	
	public PreferenceDialog() {
		super();
	}
	
	/**
	 * Selects the specified preference page <var>page</var>.
	 * @param page preference page to be opened
	 */
	public PreferenceDialog select(PreferencePage page) {
		if (page == null) {
			throw new IllegalArgumentException("page can't be null");
		}
		return select(page.getPath());
	}

	/**
	 * Selects preference page with the specified <var>path</var>.
	 * @param path path in preference shell tree to specific preference page
	 */
	public PreferenceDialog select(String... path) {
		if (path == null) {
			throw new IllegalArgumentException("path can't be null");
		}
		if (path.length == 0) {
			throw new IllegalArgumentException("path can't be empty");
		}
		TreeItem t = new DefaultTreeItem(new DefaultTree(this), path);
		t.select();
		
		new WaitUntil(new WidgetIsFound(CLabel.class, this.getControl(), 
				new WithTextMatcher(path[path.length-1])), TimePeriod.SHORT, false);
		return this;
	}
	
	/**
	 * Get name of the current preference page.
	 * 
	 * @return name of preference page
	 */
	public String getPageName() {
		DefaultCLabel cl = new DefaultCLabel(this);
		return cl.getText();
	}
	
	/**
	 * Presses Ok button on Property Dialog. 
	 */
	public void ok() {
		org.eclipse.swt.widgets.Shell parentShell = ShellLookup.getInstance().getParentShell(getShell().getSWTWidget());
		
		WidgetIsFound applyAndCloseButton = new WidgetIsFound(org.eclipse.swt.widgets.Button.class, this.getControl(),
				new WithMnemonicTextMatcher("Apply and Close"));
		
		
		Button button;
		if(applyAndCloseButton.test()){
			button = new PushButton(this, "Apply and Close"); //oxygen changed button text
		} else {
			button = new OkButton(this);	
		}
		button.click();
		new WaitWhile(new WindowIsAvailable(this)); 
		new DefaultShell(parentShell);
	}
	
	/**
	 * Checks if PreferenceDialog can finish - Apply and Close or OK button is enabled
	 * @return true if PreferenceDialog can finish, false otherwise
	 */
	public boolean canFinish(){
		WidgetIsFound applyAndCloseButton = new WidgetIsFound(org.eclipse.swt.widgets.Button.class, this.getControl(),
				new WithMnemonicTextMatcher("Apply and Close"));
		
		
		Button button;
		if(applyAndCloseButton.test()){
			button = new PushButton(this, "Apply and Close"); //oxygen changed button text
		} else {
			button = new OkButton(this);	
		}
		return button.isEnabled();
	}
	
	/**
	 * Presses Cancel button on Property Dialog. 
	 */
	public void cancel() {
		org.eclipse.swt.widgets.Shell parentShell = ShellLookup.getInstance().getParentShell(getShell().getSWTWidget());
		
		CancelButton cancel = new CancelButton(this);
		cancel.click();
		new WaitWhile(new WindowIsAvailable(this)); 
		new DefaultShell(parentShell);
	}

	@Override
	public Class<? extends org.eclipse.jface.window.Window> getEclipseClass() {
		return org.eclipse.jface.preference.PreferenceDialog.class;
	}

	@Override
	public Openable getDefaultOpenAction() {
		return null;
	}
}
