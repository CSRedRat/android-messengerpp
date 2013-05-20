package org.solovyev.android.messenger.realms;

import junit.framework.Assert;
import org.junit.Test;
import org.solovyev.android.messenger.entities.Entity;
import org.solovyev.android.messenger.entities.EntityImpl;

public class RealmEntityImplTest {

	@Test
	public void testFromUserId() throws Exception {
		try {
			EntityImpl.fromEntityId("test");
			Assert.fail();
		} catch (IllegalArgumentException e) {
			// ok
		}

		try {
			EntityImpl.fromEntityId("test:");
			Assert.fail();
		} catch (IllegalArgumentException e) {
			// ok
		}

		try {
			EntityImpl.fromEntityId(":test");
			Assert.fail();
		} catch (IllegalArgumentException e) {
			// ok
		}

		Entity actual = EntityImpl.fromEntityId("1:test");
		Assert.assertEquals("1", actual.getRealmId());
		Assert.assertEquals("test", actual.getRealmEntityId());

		actual = EntityImpl.fromEntityId("test:1");
		Assert.assertEquals("test", actual.getRealmId());
		Assert.assertEquals("1", actual.getRealmEntityId());

		actual = EntityImpl.fromEntityId("1:2:3");
		Assert.assertEquals("1", actual.getRealmId());
		Assert.assertEquals("2:3", actual.getRealmEntityId());

	}
}
