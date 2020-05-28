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
package org.eclipse.reddeer.core.handler;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.reddeer.common.util.Display;
import org.eclipse.reddeer.common.util.ResultRunnable;
import org.eclipse.reddeer.core.exception.CoreLayerException;

/**
 * Contains methods for handling UI operations on {@link List} widgets.
 * 
 * @author Lucia Jelinkova
 *
 */
public class ListHandler extends ControlHandler{
	
	private Rectangle rectangle = null;
	private int itemIndex = -1;
	
	private static ListHandler instance;
	
	/**
	 * Gets instance of ListHandler.
	 * 
	 * @return instance of ListHandler
	 */
	public static ListHandler getInstance(){
		if(instance == null){
			instance = new ListHandler();
		}
		return instance;
	}

	/**
	 * Gets items from specified {@link List}.
	 * 
	 * @param list list to handle
	 * @return items from specified list
	 */
	public String[] getItems(final List list) {
		return Display.syncExec(new ResultRunnable<String[]>() {

			@Override
			public String[] run() {
				return list.getItems();
			}
		});
	}

	/**
	 * Deselects all items of specified {@link List}.
	 * 
	 * @param list list to handle
	 */
	public void deselectAll(final List list) {
		Display.syncExec(new Runnable() {

			@Override
			public void run() {
				list.deselectAll();
			}
		});
	}

	/**
	 * Selects all items of specified {@link List}.
	 * 
	 * @param list list to handle
	 */
	public void selectAll(final List list) {
		Display.syncExec(new Runnable() {

			@Override
			public void run() {
				if ((list.getStyle() & SWT.MULTI) != 0) {
					list.selectAll();
					notifyWidget(SWT.Selection, list);
				} else {
					throw new CoreLayerException(
							"List does not support multi selection - it does not have SWT MULTI style");
				}
			}
		});
	}

	/**
	 * Gets item specified by text from specified {@link List}.
	 * Previously selected item(s) is/are deselected.
	 * 
	 * @param list list to handle
	 * @param item item to select
	 */
	public void select(final List list, final String item) {
		Display.syncExec(new Runnable() {

			@Override
			public void run() {
				list.deselectAll();
				int index = (list.indexOf(item));
				if (index == -1) {
					throw new CoreLayerException("Unable to select item " + item
							+ " because it does not exist");
				}
				list.select(list.indexOf(item));
				sendClickNotifications(list);
			}
		});
	}

	/**
	 * Selects items specified by their text from specified {@link List}.
	 * Previously selected item(s) is/are deselected.
	 * 
	 * @param list list to handle
	 * @param items items to select
	 */
	public void select(final List list, final String[] items) {
		Display.syncExec(new Runnable() {

			@Override
			public void run() {
				list.deselectAll();
				if ((list.getStyle() & SWT.MULTI) != 0) {
					for (String item : items) {
						int index = (list.indexOf(item));
						if (index == -1) {
							throw new CoreLayerException(
									"Unable to select item " + item
											+ " because it does not exist");
						}
						list.select(list.indexOf(item));
						notifyWidget(SWT.Selection, list);
					}
				} else {
					throw new CoreLayerException(
							"List does not support multi selection - it does not have SWT MULTI style");
				}
			}
		});
	}

	/**
	 * Selects items on specified indices in specified {@link List}.
	 * Previously selected item(s) is/are deselected.
	 * 
	 * 
	 * @param list list to handle
	 * @param indices indices of items to select
	 */
	public void select(final List list, final int[] indices) {
		Display.syncExec(new Runnable() {

			@Override
			public void run() {
				list.deselectAll();
				if ((list.getStyle() & SWT.MULTI) != 0) {
					list.select(indices);
					notifyWidget(SWT.Selection, list);
				} else {
					throw new CoreLayerException(
							"List does not support multi selection - it does not have SWT MULTI style");
				}
			}
		});
	}

	/**
	 * Selects item on position specified by index in specified {@link List}.
	 * Previously selected item(s) is/are deselected.
	 * 
	 * @param list list to handle
	 * @param index index of item to select
	 */
	public void select(final List list, final int index) {
		Display.syncExec(new Runnable() {

			@Override
			public void run() {
				if (list.getItemCount() - 1 < index) {
					throw new CoreLayerException(
							"Unable to select item with index " + index
									+ " because it does not exist");
				}
				list.deselectAll();
				list.select(index);
				notifyWidget(SWT.Selection, list);
			}
		});
	}
	
	/**
	 * Gets selected list items.
	 * 
	 * @param list list to handle
	 * @return array of strings representing selected list items
	 */
	public String[] getSelectedItems(final List list) {
		return Display.syncExec(new ResultRunnable<String[]>() {

			@Override
			public String[] run() {
				return list.getSelection();
			}
		});
	}
	
	/**
	 * Gets index of selected item in specified list.
	 * 
	 * @param list list to handle
	 * @return index of selected list item
	 */
	public int getSelectionIndex(final List list) {
		return Display.syncExec(new ResultRunnable<Integer>() {

			@Override
			public Integer run() {
				return list.getSelectionIndex();
			}
			
		}).intValue();
	}
	
	/**
	 * Gets indices of selected items in specified list.
	 * 
	 * @param list list to handle
	 * @return indices of selected list items
	 */
	public int[] getSelectionIndices(final List list) {
		Integer[] objectIndices = Display.syncExec(new ResultRunnable<Integer[]>() {

			@Override
			public Integer[] run() {
				int[] tmpIndices = list.getSelectionIndices();
				if (tmpIndices.length == 0) {
					return new Integer[0];
				}
				Integer[] tmpObjectIndices = new Integer[tmpIndices.length];
				for (int i = 0; i < tmpIndices.length; i++) {
					tmpObjectIndices[i] = tmpIndices[i];
				}
				return tmpObjectIndices;
			}
		});
	
		int[] indices;
		if (objectIndices.length > 0) {
			indices = new int[objectIndices.length];
			for (int i = 0; i < objectIndices.length; i++) {
				indices[i] = objectIndices[i];
			}
			return indices;
		} else {
			return new int[0];
		}
	}

	public void click(List swtWidget, String listItem) { // add comment
		
		Display.syncExec(new Runnable() {

			@Override
			public void run() {
				
//				swtWidget.indexOf("Kubernetes");
//				System.out.println(swtWidget.indexOf("Kubernetes"));
				rectangle = swtWidget.getDisplay().getBounds(); // 16 items
				itemIndex = swtWidget.indexOf(listItem);
				int x = rectangle.width/2;
				int y = ((rectangle.height/16)*itemIndex)+((rectangle.height/16)/2);
//				swtWidget.getItems();
//				Point point = swtWidget.getDisplay().getDPI();
				notifyItemMouse(SWT.MouseDoubleClick, 0, swtWidget, null, x, y, 1);
				
				
//				Event e = createEvent(swtWidget);
//				e.x = rectangle.width/2; // here will be a coordinate of "Kubernetes"
//				e.y = (rectangle.height/16)/2;
//				WidgetHandler.getInstance().notifyWidget(SWT.MouseDoubleClick, e, swtWidget);
//				notifyItemMouse(SWT.MouseDoubleClick, SWT.NONE, swtWidget.getParent(), swtWidget, rectangle.width/2, (rectangle.height/16)/2, 1);
			}
		});
		
		
		notifyItemMouse(SWT.MouseDoubleClick, 0, swtWidget, null, rectangle.width/2, (rectangle.height/16)/2, 1);
		
		Rectangle my_new_rect = rectangle;
		int my_item_index = itemIndex;
	}
	
	private static Event createEvent(List swtWidget) {
		Event event = new Event();
		event.time = (int) System.currentTimeMillis();
		event.widget = swtWidget;
		event.display = Display.getDisplay();
		event.type = SWT.MouseDoubleClick;
		event.button = 1;
		if(event.type == SWT.MouseDoubleClick){
			event.count=2;
		}
		return event;
	}
}