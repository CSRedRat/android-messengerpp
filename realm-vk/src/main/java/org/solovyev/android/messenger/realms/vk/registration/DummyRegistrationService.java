package org.solovyev.android.messenger.realms.vk.registration;

import com.google.inject.Singleton;
import org.solovyev.android.messenger.registration.RegistrationService;

import javax.annotation.Nonnull;

/**
 * User: serso
 * Date: 5/25/12
 * Time: 8:23 PM
 */

@Singleton
public class DummyRegistrationService implements RegistrationService {

	@Override
	public void requestVerificationCode(@Nonnull String phoneNumber, @Nonnull String firstName, @Nonnull String lastName) {
		//To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public boolean checkVerificationCode(@Nonnull String verificationCode) {
		return true;  //To change body of implemented methods use File | Settings | File Templates.
	}
}
