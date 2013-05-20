CREATE TABLE realms (id text PRIMARY KEY, realm_def_id text NOT NULL, user_id text NOT NULL, configuration text NOT NULL, state text NOT NULL);
CREATE TABLE users (id text PRIMARY KEY, realm_id text NOT NULL, realm_user_id text NOT NULL, last_properties_sync_date datetime, last_contacts_sync_date datetime, last_chats_sync_date datetime, last_user_icons_sync_date datetime);
CREATE TABLE user_properties (user_id text NOT NULL, property_name text NOT NULL, property_value text, FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE, UNIQUE (user_id, property_name));
CREATE TABLE user_contacts (user_id text NOT NULL, contact_id text NOT NULL, FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE, FOREIGN KEY (contact_id) REFERENCES users (id) ON DELETE CASCADE, UNIQUE (user_id, contact_id));
CREATE TABLE chats (id text PRIMARY KEY, realm_id text NOT NULL, realm_chat_id text NOT NULL, last_messages_sync_date datetime);
CREATE TABLE chat_properties (chat_id text NOT NULL, property_name text NOT NULL, property_value text, FOREIGN KEY (chat_id) REFERENCES chats (id) ON DELETE CASCADE, UNIQUE (chat_id, property_name));
CREATE TABLE user_chats (user_id text NOT NULL, chat_id text NOT NULL, FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE, FOREIGN KEY (chat_id) REFERENCES chats (id) ON DELETE CASCADE, UNIQUE (user_id, chat_id));
CREATE TABLE messages (id text NOT NULL, realm_id text NOT NULL, realm_message_id text NOT NULL, chat_id text NOT NULL, author_id text NOT NULL, recipient_id text, send_date datetime NOT NULL, send_time integer NOT NULL, title text NOT NULL, body text NOT NULL, read integer NOT NULL, PRIMARY KEY (id, chat_id), FOREIGN KEY (chat_id) REFERENCES chats (id) ON DELETE CASCADE, FOREIGN KEY (author_id) REFERENCES users (id) ON DELETE CASCADE, FOREIGN KEY (recipient_id) REFERENCES users (id) ON DELETE CASCADE);


