package org.solovyev.android.messenger.vk;

import android.content.Context;
import org.jetbrains.annotations.NotNull;
import org.solovyev.android.messenger.RealmConnection;
import org.solovyev.android.messenger.chats.RealmChatService;
import org.solovyev.android.messenger.longpoll.LongPollRealmConnection;
import org.solovyev.android.messenger.realms.AbstractRealmDef;
import org.solovyev.android.messenger.realms.Realm;
import org.solovyev.android.messenger.security.RealmAuthService;
import org.solovyev.android.messenger.users.RealmUserService;
import org.solovyev.android.messenger.vk.chats.VkRealmChatService;
import org.solovyev.android.messenger.vk.longpoll.VkRealmLongPollService;
import org.solovyev.android.messenger.vk.secutiry.VkRealmAuthService;
import org.solovyev.android.messenger.vk.users.VkRealmUserService;

/**
* User: serso
* Date: 8/12/12
* Time: 10:34 PM
*/
public class VkRealmDef extends AbstractRealmDef {

    @NotNull
    private static final String REALM_ID = "vk";

    public VkRealmDef() {
        super(REALM_ID, R.string.mpp_vk_realm_name, R.drawable.mpp_vk_icon, VkRealmConfigurationActivity.class);
    }

    @NotNull
    @Override
    public RealmConnection newRealmConnection(@NotNull Realm realm, @NotNull Context context) {
        return new LongPollRealmConnection(realm, context, new VkRealmLongPollService(realm));
    }

    @NotNull
    @Override
    public RealmUserService newRealmUserService(@NotNull Realm realm) {
        return new VkRealmUserService(realm);
    }

    @NotNull
    @Override
    public RealmChatService newRealmChatService(@NotNull Realm realm) {
        return new VkRealmChatService(realm);
    }

    @NotNull
    @Override
    public RealmAuthService newRealmAuthService(@NotNull Realm realm) {
        return new VkRealmAuthService(realm);
    }
}