/*
 * Copyright 2013 serso aka se.solovyev
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
 */

package org.solovyev.android.messenger.users;

import org.solovyev.android.messenger.App;
import org.solovyev.android.messenger.EditButtons;
import org.solovyev.android.messenger.accounts.Account;
import org.solovyev.android.messenger.accounts.tasks.UserSaverCallable;
import org.solovyev.android.messenger.core.R;

import javax.annotation.Nonnull;

import static org.solovyev.android.messenger.accounts.tasks.UserSaverCallback.newUserSaverCallback;

public class UserEditButtons<A extends Account<?>> extends EditButtons<BaseEditUserFragment<A>> {

	public UserEditButtons(@Nonnull BaseEditUserFragment<A> fragment) {
		super(fragment);
	}

	@Override
	protected void onBackButtonPressed() {
		getActivity().getSupportFragmentManager().popBackStack();
	}

	@Override
	protected boolean isRemoveButtonVisible() {
		final User user = getFragment().getUser();
		if (user != null) {
			final A account = getFragment().getAccount();
			final boolean accountUser = account.getUser().equals(user);
			return !accountUser;
		} else {
			return false;
		}
	}

	@Override
	protected void onRemoveButtonPressed() {
		App.getUserService().removeUser(getFragment().getUser());
		getActivity().getMultiPaneFragmentManager().clearBackStack();
	}

	@Override
	protected boolean isBackButtonVisible() {
		// in multi pane layout we don't want to show 'Back' button as there is no 'Back' (in one pane we reuse pane for showing more than one fragment and back means to return to the previous fragment)
		return !(getFragment().isNewUser() && getFragment().getMultiPaneManager().isDualPane(getActivity()));
	}

	@Override
	protected void onSaveButtonPressed() {
		final MutableUser contact = getFragment().validateData();
		if (contact != null) {
			getFragment().getTaskListeners().run(UserSaverCallable.TASK_NAME, new UserSaverCallable(getFragment().getAccount(), contact), newUserSaverCallback(getActivity()), getActivity(), R.string.mpp_saving_user_title, R.string.mpp_saving_user_message);
		}
	}
}
