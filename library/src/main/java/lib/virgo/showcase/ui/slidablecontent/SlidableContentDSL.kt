package lib.virgo.showcase.ui.slidablecontent

import lib.virgo.showcase.util.Constants.DEFAULT_DESCRIPTION_TEXT_FONT_FAMILY
import lib.virgo.showcase.util.Constants.DEFAULT_DESCRIPTION_TEXT_SIZE
import lib.virgo.showcase.util.Constants.DEFAULT_DESCRIPTION_TEXT_STYLE
import lib.virgo.showcase.util.Constants.DEFAULT_TEXT_COLOR
import lib.virgo.showcase.util.Constants.DEFAULT_TEXT_POSITION
import lib.virgo.showcase.util.Constants.DEFAULT_TITLE_TEXT_FONT_FAMILY
import lib.virgo.showcase.util.Constants.DEFAULT_TITLE_TEXT_SIZE
import lib.virgo.showcase.util.Constants.DEFAULT_TITLE_TEXT_STYLE

fun slidableContent(build: SlidableContent.() -> Unit): SlidableContent {
    val slidableContent = SlidableContent(
	    imageUrl = "",
	    title = null,
	    titleTextColor = DEFAULT_TEXT_COLOR,
	    titleTextSize = DEFAULT_TITLE_TEXT_SIZE,
	    titleTextFontFamily = DEFAULT_TITLE_TEXT_FONT_FAMILY,
	    titleTextStyle = DEFAULT_TITLE_TEXT_STYLE,
	    description = null,
	    descriptionTextColor = DEFAULT_TEXT_COLOR,
	    descriptionTextSize = DEFAULT_DESCRIPTION_TEXT_SIZE,
	    descriptionTextFontFamily = DEFAULT_DESCRIPTION_TEXT_FONT_FAMILY,
	    descriptionTextStyle = DEFAULT_DESCRIPTION_TEXT_STYLE,
	    textPosition = DEFAULT_TEXT_POSITION
    )
    slidableContent.build()
    return slidableContent
}
