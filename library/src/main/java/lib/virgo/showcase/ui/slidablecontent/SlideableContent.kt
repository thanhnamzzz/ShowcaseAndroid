package lib.virgo.showcase.ui.slidablecontent

import android.os.Parcelable
import androidx.annotation.ColorInt
import lib.virgo.showcase.ui.tooltip.TextPosition
import kotlinx.parcelize.Parcelize

@Parcelize
data class SlideableContent(
    var imageUrl: String,
    var title: String?,
    @param:ColorInt var titleTextColor: Int,
    var titleTextSize: Float,
    var titleTextFontFamily: String,
    var titleTextStyle: Int,
    var description: String?,
    @param:ColorInt var descriptionTextColor: Int,
    var descriptionTextSize: Float,
    var descriptionTextFontFamily: String,
    var descriptionTextStyle: Int,
    var textPosition: TextPosition,
) : Parcelable
