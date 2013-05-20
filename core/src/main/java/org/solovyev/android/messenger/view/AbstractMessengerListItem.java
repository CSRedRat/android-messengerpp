package org.solovyev.android.messenger.view;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.TextView;
import org.solovyev.android.Views;
import org.solovyev.android.messenger.MessengerEntity;
import org.solovyev.android.messenger.core.R;
import org.solovyev.android.view.ViewFromLayoutBuilder;

import javax.annotation.Nonnull;

/**
 * User: serso
 * Date: 3/6/13
 * Time: 11:57 PM
 */
public abstract class AbstractMessengerListItem<D extends MessengerEntity> implements MessengerListItem, Checkable, Comparable<AbstractMessengerListItem<D>> {

	private boolean checked;

	@Nonnull
	private final String tagPrefix;

	private final int layoutResId;

	private final boolean canBeSelected;

	@Nonnull
	private CharSequence displayName = "";

	@Nonnull
	private D data;

	private boolean dataChanged = true;

	protected AbstractMessengerListItem(@Nonnull String tagPrefix, @Nonnull D data, int layoutResId) {
		this(tagPrefix, data, layoutResId, true);
	}

	protected AbstractMessengerListItem(@Nonnull String tagPrefix, @Nonnull D data, int layoutResId, boolean canBeSelected) {
		this.tagPrefix = tagPrefix;
		this.layoutResId = layoutResId;
		this.data = data;
		this.canBeSelected = canBeSelected;
	}

	@Nonnull
	@Override
	public final View updateView(@Nonnull Context context, @Nonnull View view) {
		final Object tag = view.getTag();
		if (tag instanceof ViewAwareTag && ((ViewAwareTag) tag).getTag().startsWith(tagPrefix)) {
			fillView(context, (ViewGroup) view);
			return view;
		} else {
			return build(context);
		}
	}


	@Nonnull
	@Override
	public final View build(@Nonnull Context context) {
		final ViewGroup view = (ViewGroup) ViewFromLayoutBuilder.newInstance(layoutResId).build(context);
		fillView(context, view);
		return view;
	}

	@Nonnull
	private ViewAwareTag createTag(@Nonnull ViewGroup view) {
		return new ViewAwareTag(tagPrefix + this.data.getId(), view);
	}

	@Nonnull
	@Override
	public final String getId() {
		return this.data.getId();
	}

	private void fillView(@Nonnull Context context, @Nonnull final ViewGroup view) {
		final ViewAwareTag tag = createTag(view);

		if (canBeSelected) {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
				view.setActivated(checked);
			} else {
				setActivated(context, view, checked);
			}
		}

		ViewAwareTag viewTag = (ViewAwareTag) view.getTag();
		if (!tag.equals(viewTag) || dataChanged) {
			if (viewTag != null) {
				viewTag.update(tag);
			} else {
				viewTag = tag;
				view.setTag(viewTag);
			}
			displayName = getDisplayName(this.data, context);
			fillView(this.data, context, viewTag);
			dataChanged = false;
		}
	}

	@Nonnull
	protected abstract CharSequence getDisplayName(@Nonnull D data, @Nonnull Context context);

	protected abstract void fillView(@Nonnull D data, @Nonnull Context context, @Nonnull ViewAwareTag viewTag);

	@Override
	public final void setChecked(boolean checked) {
		this.checked = checked;
	}

	@Override
	public final boolean isChecked() {
		return checked;
	}

	@Override
	public final void toggle() {
		this.checked = !checked;
	}

	@Nonnull
	protected final D getData() {
		return data;
	}

	protected final void setData(@Nonnull D data) {
		this.data = data;
		// view update will be done by adapter if necessary
		onDataChanged();
	}

	/**
	 * Should be called when inner state of data class is changed (for example, properties)
	 * Method is called inside {@link AbstractMessengerListItem#setData(D)} => no need to call this method after setting new data
	 */
	public void onDataChanged() {
		dataChanged = true;
	}

	@Override
	public final boolean equals(Object o) {
		if (this == o) {
			return true;
		}

		if (!(o instanceof AbstractMessengerListItem)) {
			return false;
		}

		final AbstractMessengerListItem that = (AbstractMessengerListItem) o;

		if (!data.equals(that.data)) {
			return false;
		}

		return true;
	}

	@Override
	public final int hashCode() {
		return data.hashCode();
	}

	@Override
	public final String toString() {
		// NOTE: this code is used inside the ArrayAdapter for filtering
		return this.displayName.toString();
	}

	@Nonnull
	public final CharSequence getDisplayName() {
		return displayName;
	}

	@Override
	public final int compareTo(@Nonnull AbstractMessengerListItem<D> another) {
		return this.toString().compareTo(another.toString());
	}

    /*
	**********************************************************************
    *
    *                           STATIC (API < 11 SUPPORT)
    *
    **********************************************************************
    */


	private static void setActivated(@Nonnull Context context, @Nonnull ViewGroup view, boolean checked) {
		final Resources resources = context.getResources();

		if (checked) {
			final boolean largeScreen = Views.isLayoutSizeAtLeast(Configuration.SCREENLAYOUT_SIZE_LARGE, resources.getConfiguration());
			final boolean landscapeMode = isLandscapeMode(resources);
			if (largeScreen || landscapeMode) {
				setActivatedStyles(view, resources);
			} else {
				setNotActivatedStyle(view, resources);
			}
		} else {
			setNotActivatedStyle(view, resources);
		}
	}

	private static void setNotActivatedStyle(@Nonnull final ViewGroup view, @Nonnull final Resources resources) {
		Views.processViewsOfType(view, TextView.class, new Views.ViewProcessor<TextView>() {
			@Override
			public void process(@Nonnull TextView view) {
				final Typeface typeface = view.getTypeface();
				if (typeface != null && typeface.isBold()) {
					// NOTE: checking 'bold' property is only one way to determine if text is main or secondary
					view.setTextColor(resources.getColor(R.color.mpp_text));
				} else {
					view.setTextColor(resources.getColor(R.color.mpp_text_secondary));
				}
			}
		});
		view.setBackgroundColor(resources.getColor(android.R.color.transparent));

		setOnlineContactMarker(view, resources, false);
	}

	private static void setOnlineContactMarker(@Nonnull ViewGroup view, @Nonnull Resources resources, boolean selected) {
		final ImageView contactOnlineView = (ImageView) view.findViewById(R.id.mpp_li_contact_online_view);
		if (contactOnlineView != null) {
			final int drawableResId = selected ? R.drawable.mpp_contact_online_inverted : R.drawable.mpp_contact_online;
			contactOnlineView.setImageDrawable(resources.getDrawable(drawableResId));
		}
	}

	private static void setActivatedStyles(@Nonnull final ViewGroup view, @Nonnull final Resources resources) {
		Views.processViewsOfType(view, TextView.class, new Views.ViewProcessor<TextView>() {
			@Override
			public void process(@Nonnull TextView view) {
				view.setTextColor(resources.getColor(R.color.mpp_text_selected));
			}
		});
		view.setBackgroundColor(resources.getColor(R.color.mpp_selected));
		setOnlineContactMarker(view, resources, true);
	}

	private static boolean isLandscapeMode(@Nonnull Resources resources) {
		final DisplayMetrics displayMetrics = resources.getDisplayMetrics();

		if (displayMetrics.widthPixels <= displayMetrics.heightPixels) {
			return false;
		} else {
			return true;
		}
	}
}
