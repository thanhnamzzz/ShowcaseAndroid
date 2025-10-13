package lib.virgo.showcase.util

import android.graphics.Rect
import lib.virgo.showcase.showcase.ShowcaseModel
import lib.virgo.showcase.ui.tooltip.AbsoluteArrowPosition
import lib.virgo.showcase.ui.tooltip.ArrowPosition
import kotlin.math.pow
import kotlin.math.sqrt

internal object TooltipFieldUtil {

    fun decideArrowPosition(showcaseModel: ShowcaseModel, screenHeight: Int): AbsoluteArrowPosition =
        when (showcaseModel.arrowPosition) {
            ArrowPosition.UP -> AbsoluteArrowPosition.UP
            ArrowPosition.DOWN -> AbsoluteArrowPosition.DOWN
            ArrowPosition.AUTO -> calculateArrowPosition(showcaseModel.verticalCenter(), screenHeight)
        }

    private fun calculateArrowPosition(verticalCenter: Float, screenHeight: Int): AbsoluteArrowPosition =
        if (screenHeight / 2 > verticalCenter) {
            AbsoluteArrowPosition.UP
        } else {
            AbsoluteArrowPosition.DOWN
        }

    fun calculateRadius(rect: Rect): Float {
        val x = rect.width().toDouble() / 2
        val y = rect.height().toDouble() / 2

        return sqrt(x.pow(2) + y.pow(2)).toFloat()
    }

    fun calculateMarginForCircle(
        top: Float,
        bottom: Float,
        arrowPosition: AbsoluteArrowPosition,
        statusBarHeight: Int,
        isStatusBarVisible: Boolean,
        screenHeight: Int
    ): Int = when (arrowPosition) {
        AbsoluteArrowPosition.UP -> bottom.toInt() + if (isStatusBarVisible) statusBarHeight else 0
        AbsoluteArrowPosition.DOWN -> {
            val diff = if (isStatusBarVisible) -statusBarHeight else 0
            (screenHeight - top + diff).toInt()
        }
    }

    fun calculateMarginForRectangle(
        top: Float,
        bottom: Float,
        arrowPosition: AbsoluteArrowPosition,
        statusBarHeight: Int,
        isStatusBarVisible: Boolean,
        screenHeight: Int
    ): Int = when (arrowPosition) {
        AbsoluteArrowPosition.UP -> bottom.toInt() + if (isStatusBarVisible) statusBarHeight else 0
        AbsoluteArrowPosition.DOWN -> {
            val diff = if (isStatusBarVisible) -statusBarHeight else 0
            (screenHeight - top + diff).toInt()
        }
    }

    fun calculateArrowMargin(horizontalCenter: Float, density: Float): Int {
        val arrowHalfWidth = (15 * density)

        return (horizontalCenter - arrowHalfWidth).toInt()
    }
}
