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
package org.eclipse.reddeer.swt.impl.list;

import org.hamcrest.Matcher;
import org.eclipse.reddeer.common.logging.Logger;
import org.eclipse.reddeer.common.logging.LoggingUtils;
import org.eclipse.reddeer.swt.api.List;
import org.eclipse.reddeer.core.handler.ListHandler;
import org.eclipse.reddeer.core.reference.ReferencedComposite;
import org.eclipse.reddeer.swt.widgets.AbstractControl;

/**
 * Abstract class for all List implementations
 * 
 * @author Rastislav Wagner
 * 
 */
public abstract class AbstractList extends AbstractControl<org.eclipse.swt.widgets.List> implements List {

	private static final Logger logger = Logger.getLogger(AbstractList.class);

	protected AbstractList(ReferencedComposite refComposite, int index, Matcher<?>... matchers) {
		super(org.eclipse.swt.widgets.List.class, refComposite, index, matchers);
	}
	
	protected AbstractList(org.eclipse.swt.widgets.List widget){
		super(widget);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.reddeer.swt.api.List#click(java.lang.String)
	 */
	public void click(String listItem) { // rework comment----------------------------------
		ListHandler.getInstance().click(swtWidget,listItem);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.reddeer.swt.api.List#select(java.lang.String)
	 */
	public void select(String listItem) {
		ListHandler.getInstance().select(swtWidget,listItem);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.reddeer.swt.api.List#select(int)
	 */
	public void select(int listItemIndex) {
		ListHandler.getInstance().select(swtWidget,listItemIndex);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.reddeer.swt.api.List#getListItems()
	 */
	public String[] getListItems() {
		return ListHandler.getInstance().getItems(swtWidget);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.reddeer.swt.api.List#deselectAll()
	 */
	public void deselectAll() {
		ListHandler.getInstance().deselectAll(swtWidget);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.reddeer.swt.api.List#selectAll()
	 */
	public void selectAll() {
		ListHandler.getInstance().selectAll(swtWidget);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.reddeer.swt.api.List#getSelectedItems()
	 */
	public String[] getSelectedItems() {
		return ListHandler.getInstance().getSelectedItems(swtWidget);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.reddeer.swt.api.List#getSelectionIndex()
	 */
	public int getSelectionIndex() {
		return ListHandler.getInstance().getSelectionIndex(swtWidget);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.reddeer.swt.api.List#getSelectionIndices()
	 */
	public int[] getSelectionIndices() {
		return ListHandler.getInstance().getSelectionIndices(swtWidget);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.reddeer.swt.api.List#select(java.lang.String[])
	 */
	public void select(String... listItems) {
		logger.info("Select list items (" + LoggingUtils.format(listItems) + ")");
		ListHandler.getInstance().select(swtWidget,listItems);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.reddeer.swt.api.List#select(int[])
	 */
	public void select(int... indices) {
		logger.info("Select list items with indices (" + LoggingUtils.format(indices) + ")");
		ListHandler.getInstance().select(swtWidget,indices);
	}
}
