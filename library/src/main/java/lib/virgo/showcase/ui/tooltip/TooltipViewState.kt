package lib.virgo.showcase.ui.tooltip

import android.view.View
import lib.virgo.showcase.showcase.ShowcaseModel
import lib.virgo.showcase.util.Constants
import lib.virgo.showcase.R

internal data class TooltipViewState(
	val showcaseModel: ShowcaseModel,
	val arrowPosition: AbsoluteArrowPosition,
	val arrowMargin: Int
) {
	fun getBackgroundColor(): Int = showcaseModel.popupBackgroundColor

	fun getImageUrl(): String = showcaseModel.imageUrl

	fun getImageViewVisibility(): Int =
		if (showcaseModel.imageUrl.isBlank()) View.GONE else View.VISIBLE

	fun getTextPosition(): Int {
		return when (showcaseModel.textPosition) {
			TextPosition.CENTER -> 4
			TextPosition.END -> 3
			else -> 2
		}
	}

	fun getTitle() = showcaseModel.titleText

	fun getTitleVisibility() = showcaseModel.titleText.isNotEmpty()

	fun getTitleTextColor() = showcaseModel.titleTextColor

	fun getTitleTextSize() = showcaseModel.titleTextSize

	fun getTitleTextFontFamily() = showcaseModel.titleTextFontFamily

	fun getTitleTextStyle() = showcaseModel.titleTextStyle

	fun getDescription() = showcaseModel.descriptionText

	fun getDescriptionVisibility() = showcaseModel.descriptionText.isNotEmpty()

	fun getDescriptionTextColor() = showcaseModel.descriptionTextColor

	fun getDescriptionTextSize() = showcaseModel.descriptionTextSize

	fun getDescriptionTextFontFamily() = showcaseModel.descriptionTextFontFamily

	fun getDescriptionTextStyle() = showcaseModel.descriptionTextStyle

	fun getArrowPercentage() = showcaseModel.arrowPercentage

	fun getTopArrowResource() =
		if (showcaseModel.arrowResource == Constants.DEFAULT_ARROW_RESOURCE) R.drawable.ic_showcase_arrow_up else showcaseModel.arrowResource

	fun getTopArrowVisibility() =
		if (arrowPosition == AbsoluteArrowPosition.UP) View.VISIBLE else View.GONE

	fun getBottomArrowResource() =
		if (showcaseModel.arrowResource == Constants.DEFAULT_ARROW_RESOURCE) R.drawable.ic_showcase_arrow_down else showcaseModel.arrowResource

	fun getBottomArrowVisibility() =
		if (arrowPosition == AbsoluteArrowPosition.DOWN) View.VISIBLE else View.GONE

	fun getCloseButtonColor() = showcaseModel.closeButtonColor

	fun getCloseButtonVisibility() = if (showcaseModel.showCloseButton) View.VISIBLE else View.GONE

	fun getContentVisibility() =
		if (showcaseModel.customContent != null) View.GONE else View.VISIBLE

	fun isShowcaseViewClickable() = showcaseModel.isShowcaseViewClickable

	fun isSlideableContentVisible() = showcaseModel.slideableContentList.isNullOrEmpty().not()

	fun isToolTipVisible() = showcaseModel.isToolTipVisible
}
