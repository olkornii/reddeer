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
	private Rectangle rectangle_parent = null;
	private Rectangle rectangle_client_area = null;
	private Rectangle rectangle_display = null;
	private int itemIndex = -1;
	private Point test_point;
	
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

	public void click(final List swtWidget,final String listItem) { // add comment
		Display.syncExec(new Runnable() {

			@Override
			public void run() {
				
//				swtWidget.indexOf("Kubernetes");
//				System.out.println(swtWidget.indexOf("Kubernetes"));
				
				rectangle = swtWidget.getDisplay().getBounds(); // 16 items -------- not the List but the window or something else
				test_point = swtWidget.getLocation();
				rectangle_client_area = swtWidget.getClientArea();
				rectangle = swtWidget.getBounds();
				rectangle_parent = swtWidget.getParent().getBounds();
				rectangle_display = Display.getDisplay().getBounds();
				
//				itemIndex = swtWidget.indexOf(listItem);
//				itemIndex = swtWidget.indexOf("OpenShift");
//				int x = rectangle.width/2;
//				int y = ((rectangle.height/16)*itemIndex)+((rectangle.height/16)/2);
//				swtWidget.select(itemIndex);
//				swtWidget.select();
//				swtWidget.getItems();
//				Point point = swtWidget.getDisplay().getDPI();
				
				int x = 555;
				int y = 450;
				
//				int x = my_point.x + 5;
//				int y = my_point.y + 55;
				
				WidgetHandler.getInstance().notifyItemMouse(SWT.MouseDoubleClick, SWT.NONE, swtWidget, null, x, y, 1);
//				WidgetHandler.getInstance().sendClickNotifications(swtWidget);
//				WidgetHandler.getInstance().notifyItemMouse(SWT.MouseDown, SWT.NONE, swtWidget, null, x, y, 1);
//				WidgetHandler.getInstance().notifyItemMouse(SWT.MouseUp, SWT.NONE, swtWidget, null, x, y, 1);
				
				notifyItemMouse(SWT.MouseDoubleClick, SWT.NONE, swtWidget, null, x, y, 1);
//				sendClickNotifications(swtWidget);
				
//				notifyItemMouse(SWT.MouseMove, SWT.NONE, swtWidget, null, x, y, 0);
				
//				notifyItemMouse(SWT.MouseDown, SWT.NONE, swtWidget, null, x, y, 1);
//				notifyItemMouse(SWT.MouseUp, SWT.NONE, swtWidget, null, x, y, 1);
				
//				WidgetHandler.getInstance().sendClickNotifications(swtWidget);
//				sendClickNotifications(swtWidget);
				
				
				Event e1 = createEvent(swtWidget);
				e1.x = 555;
				e1.y = 450;
				WidgetHandler.getInstance().notifyWidget(SWT.MouseDoubleClick, e1, swtWidget);
//				WidgetHandler.getInstance().sendClickNotifications(swtWidget);
				notifyWidget(SWT.MouseDoubleClick, e1, swtWidget);
//				sendClickNotifications(swtWidget);
//				Event e2 = createEvent(swtWidget, SWT.MouseDown);
//				e2.x = rectangle.width/3;
//				e2.y = rectangle.height/2;
//				WidgetHandler.getInstance().notifyWidget(SWT.MouseDown, e2, swtWidget);
//				WidgetHandler.getInstance().notifyWidget(SWT.MouseDoubleClick, e1, swtWidget.getParent());
//				WidgetHandler.getInstance().notifyWidget(SWT.BUTTON1, e1, swtWidget);
//				notifyWidget(SWT.MouseUp, e1, swtWidget);
//				
//				e.x = rectangle.width/2; // here will be a coordinate of "Kubernetes"
//				e.y = (rectangle.height/16)/2;
//				WidgetHandler.getInstance().notifyWidget(SWT.MouseDoubleClick, e, swtWidget);
//				notifyItemMouse(SWT.MouseDoubleClick, SWT.NONE, swtWidget.getParent(), swtWidget, rectangle.width/2, (rectangle.height/16)/2, 1);
			}
		});
		
		
//		notifyItemMouse(SWT.MouseDoubleClick, 0, swtWidget, null, rectangle.width/2, (rectangle.height/16)/2, 1);
		Point my_new_point = test_point;
		Rectangle new_rect = rectangle;
		Rectangle new_rect_area = rectangle_client_area;
		Rectangle new_rect_parent = rectangle_parent;
		Rectangle new_rect_display = rectangle_display;
		int my_item_index = itemIndex;
	}
	
//	private static Event createEvent(List swtWidget) {
//		Event event = new Event();
//		event.time = (int) System.currentTimeMillis();
//		event.widget = swtWidget;
//		event.display = Display.getDisplay();
//		event.type = SWT.MouseDoubleClick;
//		event.button = 1;
//		if(event.type == SWT.MouseDoubleClick){
//			event.count=2;
//		}
//		return event;
//	}
	
	// returns point (x,y) of offset in this StyledText widget.
//	private Point getLocationAtOffset(final int offset) {
//		return Display.syncExec(new ResultRunnable<Point>() {
//
//			@Override
//			public Point run() {
//				return swtWidget.getLocationAtOffset(offset);
//			}
//		});
//	}
	
	private static Event createEvent(Widget widget) {
		Event event = new Event();
		event.time = (int) System.currentTimeMillis();
		event.widget = widget;
		event.display = Display.getDisplay();
		event.type = SWT.MouseDoubleClick;
		event.button = 3;
		return event;
	}
}