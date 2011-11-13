/*
 * Copyright 2011 PrimeFaces Extensions.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * $Id$
 */

package org.primefaces.extensions.component.reseteditablevalueholders;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.component.EditableValueHolder;
import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.event.ComponentSystemEventListener;

import org.primefaces.extensions.util.ComponentUtils;
import org.primefaces.util.Constants;

/**
 * {@link ComponentSystemEventListener} for the <code>ResetEditableValueHolders</code> component.
 *
 * @author Thomas Andraschko / last modified by $Author$
 * @version $Revision$
 * @since 0.2
 */
@SuppressWarnings("serial")
public class ResetEditableValueHoldersListener implements ComponentSystemEventListener, Serializable {

	private UICommand source;
	private String components;

	public ResetEditableValueHoldersListener(final UICommand source, final String components) {
		super();
		this.source = source;
		this.components = components;
	}

	@Override
	public void processEvent(final ComponentSystemEvent event) {
		final FacesContext context = FacesContext.getCurrentInstance();

		if (isRequestSource(context, this.source)) {

			final List<EditableValueHolder> editableValueHolders =
				getEditableValueHolders(context, this.source, this.components);

			for (EditableValueHolder editableValueHolder : editableValueHolders) {
				editableValueHolder.resetValue();
			}
		}
	}

	protected boolean isRequestSource(final FacesContext context, final UIComponent component) {
		final Map<String, String> params = context.getExternalContext().getRequestParameterMap();
		final String clientId = component.getClientId(context);

		return clientId.equals(params.get(Constants.PARTIAL_SOURCE_PARAM));
	}

	protected List<EditableValueHolder> getEditableValueHolders(final FacesContext context, final UIComponent source,
			final String components) {

		final List<UIComponent> foundComponents = ComponentUtils.findComponents(context, source, components);
		final List<EditableValueHolder> editableValueHolders = new ArrayList<EditableValueHolder>();

		for (UIComponent foundComponent : foundComponents) {

			if (foundComponent instanceof EditableValueHolder) {
				editableValueHolders.add((EditableValueHolder) foundComponent);
			} else {
				//TODO loop through containers
			}
		}

		return editableValueHolders;
	}
}