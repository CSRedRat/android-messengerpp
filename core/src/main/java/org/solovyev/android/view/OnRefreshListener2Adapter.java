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

package org.solovyev.android.view;

import com.handmark.pulltorefresh.library.PullToRefreshBase;

import javax.annotation.Nonnull;

public class OnRefreshListener2Adapter implements PullToRefreshBase.OnRefreshListener2 {

	@Nonnull
	private PullToRefreshBase.OnRefreshListener onPullDownToRefresh;

	@Nonnull
	private PullToRefreshBase.OnRefreshListener onPullUpToRefresh;

	public OnRefreshListener2Adapter(@Nonnull PullToRefreshBase.OnRefreshListener onPullDownToRefresh,
									 @Nonnull PullToRefreshBase.OnRefreshListener onPullUpToRefresh) {
		this.onPullDownToRefresh = onPullDownToRefresh;
		this.onPullUpToRefresh = onPullUpToRefresh;
	}

	@Override
	public void onPullDownToRefresh() {
		onPullDownToRefresh.onRefresh();
	}

	@Override
	public void onPullUpToRefresh() {
		onPullUpToRefresh.onRefresh();
	}
}
