/* Clone from https://github.com/bilgehankalkan/showcase
last commit fa998c9 (fa998c9760e229d5b9378e7b511ae0a3e7528edb) 23/03/2023
 */
package lib.virgo.showcase.showcase

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.Typeface
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.*
import androidx.annotation.IntRange
import androidx.core.app.ActivityOptionsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import lib.virgo.showcase.ui.showcase.HighlightType
import lib.virgo.showcase.ui.showcase.ShowcaseActivity
import lib.virgo.showcase.ui.slidablecontent.SlideableContent
import lib.virgo.showcase.ui.tooltip.ArrowPosition
import lib.virgo.showcase.ui.tooltip.TextPosition
import lib.virgo.showcase.util.Constants
import lib.virgo.showcase.util.TooltipFieldUtil
import lib.virgo.showcase.util.toRectF
import lib.virgo.showcase.R

class ShowcaseManager private constructor(
	private val showcaseModel: ShowcaseModel,
	@param:StyleRes val resId: Int?
) {

	fun show(
		fragment: FragmentActivity,
		requestCode: Int? = null,
		launcher: ActivityResultLauncher<Intent>,
		lifecycleOwner: LifecycleOwner,
	) {
		val stateController = show(fragment, requestCode, launcher)
		ShowcaseLifecycleOwner(lifecycleOwner.lifecycle, stateController)
	}

	private fun show(
		activity: FragmentActivity,
		requestCode: Int? = null,
		launcher: ActivityResultLauncher<Intent>
	): ShowcaseStateController {
		val stateController = ShowcaseStateController(context = activity)
		if (showcaseModel.isDebugMode) return stateController

		val intent = Intent(activity, ShowcaseActivity::class.java)
		if (requestCode != null) {
			intent.putExtra(ShowcaseActivity.HAS_CALLBACK, true)
			intent.putExtra(ShowcaseActivity.REQUEST_CODE, requestCode)
		}
		val model = if (resId != null) readFromStyle(activity, resId) else showcaseModel
		intent.putExtra(ShowcaseActivity.BUNDLE_KEY, model)

		val options = ActivityOptionsCompat.makeCustomAnimation(activity, android.R.anim.fade_in, 0)

		if (requestCode == null) {
			activity.startActivity(intent, options.toBundle())
		} else {
			launcher.launch(intent, options)
//			activity.startActivityForResult(intent, requestCode)
		}
		return stateController
	}

	fun show(
		fragment: Fragment,
		requestCode: Int? = null,
		launcher: ActivityResultLauncher<Intent>,
		lifecycleOwner: LifecycleOwner
	) {
		val stateController = show(fragment, requestCode, launcher)
		ShowcaseLifecycleOwner(lifecycleOwner.lifecycle, stateController)
	}

	private fun show(
		fragment: Fragment,
		requestCode: Int? = null,
		launcher: ActivityResultLauncher<Intent>
	): ShowcaseStateController {
		val stateController = ShowcaseStateController(context = fragment.requireActivity())
		if (showcaseModel.isDebugMode) return stateController
		fragment.activity?.let { activity ->
			val intent = Intent(activity, ShowcaseActivity::class.java)
			if (requestCode != null) {
				intent.putExtra(ShowcaseActivity.HAS_CALLBACK, true)
				intent.putExtra(ShowcaseActivity.REQUEST_CODE, requestCode)
			}
			val model = if (resId != null) readFromStyle(activity, resId) else showcaseModel
			intent.putExtra(ShowcaseActivity.BUNDLE_KEY, model)

			val options =
				ActivityOptionsCompat.makeCustomAnimation(activity, android.R.anim.fade_in, 0)

			if (requestCode == null) {
				fragment.startActivity(intent, options.toBundle())
			} else {
				launcher.launch(intent, options)
//				fragment.startActivityForResult(intent, requestCode)
			}
		}
		return stateController
	}

	private fun readFromStyle(context: Context, resId: Int): ShowcaseModel {
		val typedArray = context.obtainStyledAttributes(resId, R.styleable.Showcase_Theme)

		return showcaseModel.copy(
			titleTextColor = typedArray.getColor(
				R.styleable.Showcase_Theme_titleTextColor,
				showcaseModel.titleTextColor
			),
			descriptionTextColor = typedArray.getColor(
				R.styleable.Showcase_Theme_descriptionTextColor,
				showcaseModel.descriptionTextColor
			),
			closeButtonColor = typedArray.getColor(
				R.styleable.Showcase_Theme_closeButtonColor,
				showcaseModel.closeButtonColor
			),
			popupBackgroundColor = typedArray.getColor(
				R.styleable.Showcase_Theme_popupBackgroundColor,
				showcaseModel.popupBackgroundColor
			),
			windowBackgroundColor = typedArray.getColor(
				R.styleable.Showcase_Theme_windowBackgroundColor,
				showcaseModel.windowBackgroundColor
			),
			showCloseButton = typedArray.getBoolean(
				R.styleable.Showcase_Theme_showCloseButton,
				showcaseModel.showCloseButton
			),
			cancellableFromOutsideTouch = typedArray.getBoolean(
				R.styleable.Showcase_Theme_cancellableFromOutsideTouch,
				showcaseModel.cancellableFromOutsideTouch
			),
			isShowcaseViewClickable = typedArray.getBoolean(
				R.styleable.Showcase_Theme_showcaseViewClickable,
				showcaseModel.isShowcaseViewClickable
			),
			titleTextFontFamily = typedArray.getString(R.styleable.Showcase_Theme_titleTextFontFamily)
				?: showcaseModel.titleTextFontFamily,
			titleTextStyle = typedArray.getInteger(
				R.styleable.Showcase_Theme_titleStyle,
				showcaseModel.titleTextStyle
			),
			descriptionTextFontFamily = typedArray.getString(R.styleable.Showcase_Theme_descriptionTextFontFamily)
				?: showcaseModel.descriptionTextFontFamily,
			descriptionTextStyle = typedArray.getInteger(
				R.styleable.Showcase_Theme_descriptionTextStyle,
				showcaseModel.descriptionTextStyle
			)
		).also {
			typedArray.recycle()
		}
	}

	class Builder {

		private var focusViews: Array<out View>? = null
		private var titleText: String = Constants.DEFAULT_TEXT
		private var descriptionText: String = Constants.DEFAULT_TEXT
		private var isShowcaseViewVisibleIndefinitely: Boolean = Constants.DEFAULT_SHOW_FOREVER
		private var showDuration: Long = Constants.DEFAULT_SHOW_DURATION

		@ColorInt
		private var titleTextColor: Int = Constants.DEFAULT_TEXT_COLOR

		@ColorInt
		private var descriptionTextColor: Int = Constants.DEFAULT_TEXT_COLOR

		@ColorInt
		private var popupBackgroundColor: Int = Constants.DEFAULT_POPUP_COLOR
		private var showCloseButton: Boolean = Constants.DEFAULT_CLOSE_BUTTON_VISIBILITY

		@ColorInt
		private var closeButtonColor: Int = Constants.DEFAULT_TEXT_COLOR
		private var highlightType: HighlightType = Constants.DEFAULT_HIGHLIGHT_TYPE

		@DrawableRes
		private var arrowResource: Int = Constants.DEFAULT_ARROW_RESOURCE
		private var arrowPercentage: Int? = null
		private var arrowPosition: ArrowPosition = Constants.DEFAULT_ARROW_POSITION

		@ColorInt
		private var windowBackgroundColor: Int = Constants.DEFAULT_COLOR_BACKGROUND
		private var windowBackgroundAlpha: Int = Constants.DEFAULT_BACKGROUND_ALPHA
		private var titleTextSize: Float = Constants.DEFAULT_TITLE_TEXT_SIZE
		private var titleTextFontFamily: String = Constants.DEFAULT_TITLE_TEXT_FONT_FAMILY
		private var titleTextStyle: Int = Constants.DEFAULT_TITLE_TEXT_STYLE
		private var descriptionTextSize: Float = Constants.DEFAULT_DESCRIPTION_TEXT_SIZE
		private var descriptionTextFontFamily: String =
			Constants.DEFAULT_DESCRIPTION_TEXT_FONT_FAMILY
		private var descriptionTextStyle: Int = Constants.DEFAULT_DESCRIPTION_TEXT_STYLE
		private var highlightPadding: Float = Constants.DEFAULT_HIGHLIGHT_PADDING_EXTRA

		@StyleRes
		private var resId: Int? = null
		private var cancellableFromOutsideTouch: Boolean =
			Constants.DEFAULT_CANCELLABLE_FROM_OUTSIDE_TOUCH
		private var isShowcaseViewClickable: Boolean = Constants.DEFAULT_SHOWCASE_VIEW_CLICKABLE
		private var isDebugMode: Boolean = false
		private var textPosition: TextPosition = Constants.DEFAULT_TEXT_POSITION
		private var imageUrl: String = Constants.DEFAULT_TEXT
		private var radiusTopStart: Float = Constants.DEFAULT_HIGHLIGHT_RADIUS
		private var radiusTopEnd: Float = Constants.DEFAULT_HIGHLIGHT_RADIUS
		private var radiusBottomStart: Float = Constants.DEFAULT_HIGHLIGHT_RADIUS
		private var radiusBottomEnd: Float = Constants.DEFAULT_HIGHLIGHT_RADIUS
		private var isToolTipVisible: Boolean = true

		@LayoutRes
		private var customContent: Int? = null

		private var isStatusBarVisible: Boolean = true

		private var slideableContentList: List<SlideableContent>? = null

		fun focus(view: View) = apply { focusViews = arrayOf(view) }

		fun focus(vararg view: View) = apply { focusViews = view }

		fun titleText(title: String) = apply { titleText = title }

		fun titleTextColor(@ColorInt color: Int) = apply { titleTextColor = color }

		/**
		 * Assigns fontFamily like sans-serif, sans-serif-medium. By default, sans-serif is used.
		 *
		 * @param fontFamily assigns fontFamily to titleText
		 */
		fun titleTextFontFamily(fontFamily: String) = apply { titleTextFontFamily = fontFamily }

		/**
		 * Controls whether showcase should be shown indefinitely or not. By default it is true.
		 *
		 * @param isVisibleIndefinitely assigns boolean value to showForever.
		 */
		fun showcaseViewVisibleIndefinitely(isVisibleIndefinitely: Boolean) =
			apply { isShowcaseViewVisibleIndefinitely = isVisibleIndefinitely }

		/**
		 * Assign duration value to showDuration. By default it is 2000L.
		 * To see the effect, set [showcaseViewVisibleIndefinitely] to false.
		 *
		 * @param duration assigns duration value to showDuration
		 */
		fun showDurationMillis(@IntRange(from = 0) duration: Long) =
			apply { showDuration = duration }

		/**
		 * Assign textStyle to titleText
		 *
		 * @param textStyle can only be style parameters in Typeface. It should be in the range of NORMAL AND BOLD_ITALIC.
		 */
		fun titleTextStyle(
			@IntRange(
				from = Typeface.NORMAL.toLong(),
				to = Typeface.BOLD_ITALIC.toLong()
			) textStyle: Int
		) =
			apply { titleTextStyle = textStyle }

		fun descriptionText(description: String) = apply { descriptionText = description }

		fun descriptionTextColor(@ColorInt color: Int) = apply { descriptionTextColor = color }

		/**
		 * Assigns fontFamily like sans-serif, sans-serif-medium. By default, sans-serif is used.
		 *
		 * @param fontFamily assigns fontFamily to descriptionText
		 */
		fun descriptionTextFontFamily(fontFamily: String) =
			apply { descriptionTextFontFamily = fontFamily }

		/**
		 * Assign textStyle to descriptionText
		 *
		 * @param textStyle can only be style parameters in Typeface. It should be in the range of NORMAL AND BOLD_ITALIC.
		 */
		fun descriptionTextStyle(
			@IntRange(
				from = Typeface.NORMAL.toLong(),
				to = Typeface.BOLD_ITALIC.toLong()
			) textStyle: Int
		) =
			apply { descriptionTextStyle = textStyle }

		fun backgroundColor(@ColorInt color: Int) = apply { popupBackgroundColor = color }

		fun closeButtonColor(@ColorInt color: Int) = apply { closeButtonColor = color }

		fun showCloseButton(show: Boolean) = apply { showCloseButton = show }

		/**Custom icon resource for arrow.**/
		fun arrowResource(@DrawableRes resource: Int) = apply { arrowResource = resource }

		fun arrowPosition(position: ArrowPosition) = apply { arrowPosition = position }

		/**
		 *
		 * Custom positioning for arrow.
		 */
		fun arrowPercentage(@IntRange(from = 0, to = 100) percentage: Int) =
			apply { arrowPercentage = percentage }

		fun highlightType(type: HighlightType) = apply { highlightType = type }

		/**
		 *
		 * Extra padding for highlight area.
		 */
		fun highlightPadding(padding: Float) = apply { highlightPadding = padding }

		fun windowBackgroundColor(@ColorInt color: Int) = apply { windowBackgroundColor = color }
		fun windowBackgroundAlpha(@IntRange(from = 0, to = 255) alpha: Int) =
			apply { windowBackgroundAlpha = alpha }

		/**
		 *
		 * titleTextSize in SP.
		 */
		fun titleTextSize(size: Float) = apply { titleTextSize = size }

		/**
		 *
		 * descriptionTextSize in SP.
		 */
		fun descriptionTextSize(size: Float) = apply { descriptionTextSize = size }

		/**
		 *
		 * Resource id of an custom style named Showcase.Theme in project.
		 */
		fun resId(@StyleRes res: Int) = apply { resId = res }

		fun cancellableFromOutsideTouch(cancellable: Boolean) =
			apply { cancellableFromOutsideTouch = cancellable }

		/**
		 * Makes the showcase view clickable or not.
		 *
		 * Default is not clickable
		 *
		 * @param clickable Set the value to true so that the showcase view is clickable
		 */
		fun showcaseViewClickable(clickable: Boolean) =
			apply { isShowcaseViewClickable = clickable }

		/**
		 *
		 * radius for highlight area.
		 */
		fun highlightRadius(
			topStartRadius: Float = Constants.DEFAULT_HIGHLIGHT_RADIUS,
			topEndRadius: Float = Constants.DEFAULT_HIGHLIGHT_RADIUS,
			bottomStartRadius: Float = Constants.DEFAULT_HIGHLIGHT_RADIUS,
			bottomEndRadius: Float = Constants.DEFAULT_HIGHLIGHT_RADIUS
		) = apply {
			radiusTopStart = topStartRadius
			radiusTopEnd = topEndRadius
			radiusBottomStart = bottomStartRadius
			radiusBottomEnd = bottomEndRadius
		}

		/**
		 *
		 * show toolTip or not.
		 */
		fun toolTipVisible(isToolTipVisible: Boolean) =
			apply { this.isToolTipVisible = isToolTipVisible }

		/**
		 * Running in debug mode or not
		 */
		fun isDebugMode(isDebug: Boolean) = apply { isDebugMode = isDebug }

		/**
		 *
		 * Custom positioning for text.
		 */
		fun textPosition(position: TextPosition) = apply { textPosition = position }

		fun imageUrl(url: String) = apply { imageUrl = url }

		fun customContent(@LayoutRes content: Int) = apply { customContent = content }

		fun statusBarVisible(isStatusBarVisible: Boolean) =
			apply { this.isStatusBarVisible = isStatusBarVisible }

		fun setSlideableContentList(slideableContentList: List<SlideableContent>) =
			apply { this.slideableContentList = slideableContentList }

		fun build(): ShowcaseManager {
			if (focusViews.isNullOrEmpty()) {
				throw Exception("view should not be null!")
			}

			val rect = Rect()
			val highlightedViewsRectFList = mutableListOf<RectF>()
			focusViews!!.forEach {
				val viewRect = Rect()
				it.getGlobalVisibleRect(viewRect)
				highlightedViewsRectFList.add(viewRect.toRectF())
				rect.union(viewRect)
			}

			val showcaseModel = ShowcaseModel(
				rectF = rect.toRectF(),
				highlightedViewsRectFList = highlightedViewsRectFList,
				radius = TooltipFieldUtil.calculateRadius(rect),
				titleText = titleText,
				descriptionText = descriptionText,
				titleTextColor = titleTextColor,
				descriptionTextColor = descriptionTextColor,
				popupBackgroundColor = popupBackgroundColor,
				closeButtonColor = closeButtonColor,
				showCloseButton = showCloseButton,
				highlightType = highlightType,
				arrowResource = arrowResource,
				arrowPosition = arrowPosition,
				arrowPercentage = arrowPercentage,
				windowBackgroundColor = windowBackgroundColor,
				windowBackgroundAlpha = windowBackgroundAlpha,
				titleTextSize = titleTextSize,
				titleTextFontFamily = titleTextFontFamily,
				titleTextStyle = titleTextStyle,
				descriptionTextSize = descriptionTextSize,
				descriptionTextFontFamily = descriptionTextFontFamily,
				descriptionTextStyle = descriptionTextStyle,
				highlightPadding = highlightPadding,
				cancellableFromOutsideTouch = cancellableFromOutsideTouch,
				isShowcaseViewClickable = isShowcaseViewClickable,
				isDebugMode = isDebugMode,
				textPosition = textPosition,
				imageUrl = imageUrl,
				customContent = customContent,
				isStatusBarVisible = isStatusBarVisible,
				slideableContentList = slideableContentList,
				radiusTopStart = radiusTopStart,
				radiusTopEnd = radiusTopEnd,
				radiusBottomEnd = radiusBottomEnd,
				radiusBottomStart = radiusBottomStart,
				isToolTipVisible = isToolTipVisible,
				showDuration = showDuration,
				isShowcaseViewVisibleIndefinitely = isShowcaseViewVisibleIndefinitely
			)

			return ShowcaseManager(showcaseModel = showcaseModel, resId = resId)
		}
	}
}
