package org.jkube.gitbeaver.application;

import org.jkube.gitbeaver.application.ConextDependentFlag;

public class NoContextFlag implements ConextDependentFlag<Object> {

	private boolean value = false;

	@Override
	public boolean isSet(final Object context) {
		return value;
	}

	@Override
	public void set(final Object context, final boolean newValue) {
		this.value = newValue;
	}
}
