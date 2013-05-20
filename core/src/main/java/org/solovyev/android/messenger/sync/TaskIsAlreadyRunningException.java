package org.solovyev.android.messenger.sync;

import android.content.Context;
import android.widget.Toast;
import org.solovyev.android.messenger.core.R;

import javax.annotation.Nonnull;

/**
 * User: serso
 * Date: 6/8/12
 * Time: 6:17 PM
 */
public class TaskIsAlreadyRunningException extends Exception {

	@Nonnull
	private SyncTask syncTask;

	public TaskIsAlreadyRunningException(@Nonnull SyncTask syncTask) {
		this.syncTask = syncTask;
	}

	@Nonnull
	public SyncTask getSyncTask() {
		return syncTask;
	}

	public void showMessage(@Nonnull Context c) {
		Toast.makeText(c, R.string.c_task_is_already_running, Toast.LENGTH_SHORT).show();
	}
}
