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

package org.solovyev.android.messenger.accounts;

import android.content.Context;
import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;

import org.solovyev.android.messenger.BaseListItemAdapter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class AccountsAdapter extends BaseListItemAdapter<AccountListItem> {

	private final boolean canAddAccounts;

	@Nonnull
	private final AccountUiEventType eventType;

	public AccountsAdapter(@Nonnull Context context,
						   @Nonnull List<? extends AccountListItem> listItems,
						   boolean canAddAccounts,
						   @Nonnull AccountUiEventType eventType) {
		super(context, listItems);
		this.canAddAccounts = canAddAccounts;
		this.eventType = eventType;
	}

    /*
	**********************************************************************
    *
    *                           ACCOUNT LISTENERS
    *
    **********************************************************************
    */

	public void onAccountEvent(@Nonnull AccountEvent accountEvent) {
		final Account account = accountEvent.getAccount();
		switch (accountEvent.getType()) {
			case created:
				if (canAddAccounts) {
					add(createListItem(account));
				}
				break;
			case changed:
				final AccountListItem listItem = findInAllElements(account);
				if (listItem != null) {
					listItem.onAccountChangedEvent(account);
				}
				break;
			case state_changed:
				switch (account.getState()) {
					case enabled:
					case disabled_by_user:
					case disabled_by_app:
						final AccountListItem accountListItem = findInAllElements(account);
						if (accountListItem != null) {
							accountListItem.onAccountChangedEvent(account);
						}
						break;
					case removed:
						remove(createListItem(account));
						break;
				}
				break;
		}
	}

	@Nullable
	protected AccountListItem findInAllElements(@Nonnull Account account) {
		return Iterables.find(getAllElements(), Predicates.<AccountListItem>equalTo(createListItem(account)), null);
	}

	@Nonnull
	private AccountListItem createListItem(@Nonnull Account account) {
		return new AccountListItem(account, eventType);
	}
}
