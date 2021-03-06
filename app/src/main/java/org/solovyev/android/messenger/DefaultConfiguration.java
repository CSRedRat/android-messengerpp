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

package org.solovyev.android.messenger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.Nonnull;

import org.solovyev.android.messenger.realms.Realm;
import org.solovyev.android.messenger.realms.sms.SmsRealm;
import org.solovyev.android.messenger.realms.vk.VkRealm;
import org.solovyev.android.messenger.realms.xmpp.CustomXmppRealm;
import org.solovyev.android.messenger.realms.xmpp.FacebookXmppRealm;
import org.solovyev.android.messenger.realms.xmpp.GoogleXmppRealm;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class DefaultConfiguration implements Configuration {

	@Nonnull
	private final List<Realm> realms = new ArrayList<Realm>();

	@Inject
	@Nonnull
	private CustomXmppRealm xmppRealm;

	@Inject
	@Nonnull
	private FacebookXmppRealm facebookXmppRealm;

	@Inject
	@Nonnull
	private GoogleXmppRealm googleXmppRealm;

	@Inject
	@Nonnull
	private VkRealm vkRealm;

	@Inject
	@Nonnull
	private SmsRealm smsRealm;

	public DefaultConfiguration() {
	}

	@Nonnull
	@Override
	public Collection<Realm> getRealms() {
		synchronized (realms) {
			if (realms.isEmpty()) {
				realms.add(xmppRealm);
				realms.add(facebookXmppRealm);
				realms.add(googleXmppRealm);
				realms.add(vkRealm);
				realms.add(smsRealm);
			}
		}

		return this.realms;
	}
}
