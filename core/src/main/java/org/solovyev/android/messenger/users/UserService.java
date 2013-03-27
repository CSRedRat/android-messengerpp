package org.solovyev.android.messenger.users;

import android.widget.ImageView;
import org.solovyev.android.messenger.chats.ApiChat;
import org.solovyev.android.messenger.chats.Chat;
import org.solovyev.android.messenger.entities.Entity;
import org.solovyev.android.messenger.realms.Realm;
import org.solovyev.android.messenger.realms.RealmException;
import org.solovyev.android.messenger.realms.UnsupportedRealmException;
import org.solovyev.common.listeners.JEventListener;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;
import java.util.List;

/**
 * User: serso
 * Date: 5/24/12
 * Time: 9:12 PM
 */
/**
 * Implementation of this class must provide thread safeness
 */
@ThreadSafe
public interface UserService {

    static final String TAG = "UserService";

    // initial initialization: will be called once on application start
    void init();

    /*
    **********************************************************************
    *
    *                           USER OPERATIONS
    *
    **********************************************************************
    */

    /**
     * NOTE: finding user by id always return user object, if real user cannot be found via API (e.g. user was removed) service must return dummy user object
     *
     * @param user user to be found
     * @return user instance identified by specified <var>user</var> entity
     */
    @Nonnull
    User getUserById(@Nonnull Entity user);

    /**
     * NOTE: finding user by id always return user object, if real user cannot be found via API (e.g. user was removed) service must return dummy user object
     *
     * @param user user to be found
     * @param tryFindInRealm user search will be done in realm service if user was not found in persistence and this parameter is set to true
     * @return user instance identified by specified <var>user</var> entity
     */
    @Nonnull
    User getUserById(@Nonnull Entity user, boolean tryFindInRealm);

    /**
     * @param user user
     * @return all user chats
     */
    @Nonnull
    List<Chat> getUserChats(@Nonnull Entity user);

    /**
     * Method updates user in application
     * @param user user to be updated
     */
    void updateUser(@Nonnull User user);

    /**
     * Method removes all users in realm identified by <var>realmId</var>
     * @param realmId id of realm for which users shall be removed
     */
    void removeUsersInRealm(@Nonnull String realmId);

    /*
    **********************************************************************
    *
    *                           CONTACTS
    *
    **********************************************************************
    */

    /**
     * @param user user
     * @return list of all user contacts
     */
    @Nonnull
    List<User> getUserContacts(@Nonnull Entity user);

    /**
     * NOTE: method do not check real status of user on the current moment of time but get one from the cache => it might be different
     *
     * @param user user
     * @return list of all online user contacts
     */
    @Nonnull
    List<User> getOnlineUserContacts(@Nonnull Entity user);

    /**
     * Call this method when presence of user's contact has been changed.
     *
     * @param user user
     * @param contact user's contact which presence has been changed
     * @param available new presence value
     */
    void onContactPresenceChanged(@Nonnull User user, @Nonnull User contact, boolean available);

    /*
    **********************************************************************
    *
    *                           SYNC
    *
    **********************************************************************
    */

    /**
     * Method synchronizes local user data with remote user data
     * @param user user for whom synchronization must be done
     */
    void syncUser(@Nonnull Entity user) throws RealmException;

    /**
     * Method synchronizes local users contacts with remote user contacts
     *
     * NOTE: some realms do not support user contacts retrieval, in that case empty list is returned
     *
     * @param user user for whom synchronization must be done
     * @return updated list of user contacts
     */
    @Nonnull
    List<User> syncUserContacts(@Nonnull Entity user) throws RealmException;

    /**
     * Method synchronizes local users chats with remote user chats
     *
     * NOTE: some realms do not support user chats retrieval, in that case empty list is returned
     *
     * @param user user for whom synchronization must be done
     * @return updated list of chats in which user is participated
     */
    @Nonnull
    List<Chat> syncUserChats(@Nonnull Entity user) throws RealmException;

    void mergeUserChats(@Nonnull Entity user, @Nonnull List<? extends ApiChat> apiChats) throws RealmException;

    void mergeUserContacts(@Nonnull Entity user, @Nonnull List<User> contacts, boolean allowRemoval, boolean allowUpdate);

    /**
     * Method synchronizes user contacts statuses (presences/availabilities)
     *
     * @param user user whose contacts statuses should be synchronized
     */
    void syncUserContactsStatuses(@Nonnull Entity user) throws RealmException;

    /*
    **********************************************************************
    *
    *                           USER ICONS
    *
    **********************************************************************
    */

    /**
     * Method sets icon of <var>user</var> in <var>imageView</var>
     * @param user user for whom icon shall be set
     * @param imageView view to which icon shall be set
     */
    void setUserIcon(@Nonnull User user, @Nonnull ImageView imageView);

    /**
     * Method sets some icon which represents set of <var>users</var> in <var>imageView</var>
     * @param realm realm
     * @param users users for whom icon shall be set
     * @param imageView view to which icon shall be set
     */
    void setUsersIcon(@Nonnull Realm realm, @Nonnull List<User> users, ImageView imageView);

    void setUserPhoto(@Nonnull User user, @Nonnull ImageView imageView);

    /**
     * Method fetches user icons for specified <var>user</var> and for ALL user contacts
     * @param user for which icon fetching must be done
     */
    void fetchUserAndContactsIcons(@Nonnull User user) throws UnsupportedRealmException;

    /*
    **********************************************************************
    *
    *                           LISTENERS
    *
    **********************************************************************
    */

    /**
     * Method subscribes listener for user events notifications
     *
     * @param listener listener to be subscribed
     * @return true if was added, false if listener already exists
     */
    boolean addListener(@Nonnull JEventListener<UserEvent> listener);

    /**
     * Method unsubscribes listener from user events notifications
     *
     * @param listener listener to be unsubscribed
     * @return true if listener was successfully unsubscribed, false if no such listener was found
     */
    boolean removeListener(@Nonnull JEventListener<UserEvent> listener);


    void onUnreadMessagesCountChanged(@Nonnull Entity contact, @Nonnull Integer unreadMessagesCount);

    int getUnreadMessagesCount(@Nonnull Entity contact);
}
