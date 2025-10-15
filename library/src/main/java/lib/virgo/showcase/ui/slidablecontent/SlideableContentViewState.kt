package lib.virgo.showcase.ui.slidablecontent

import lib.virgo.showcase.ui.tooltip.TextPosition

internal class SlideableContentViewState(val slideableContent: SlideableContent) {

	fun isTitleVisible() = slideableContent.title.isNullOrEmpty().not()

	fun isDescriptionVisible() = slideableContent.description.isNullOrEmpty().not()

	fun getTextPosition(): Int {
		return when (slideableContent.textPosition) {
			TextPosition.CENTER -> 4
			TextPosition.END -> 3
			else -> 2
		}
	}
}