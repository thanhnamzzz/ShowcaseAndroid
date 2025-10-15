package lib.virgo.showcase.ui.tooltip

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.viewpager2.widget.ViewPager2
import lib.virgo.showcase.ui.isVisible
import lib.virgo.showcase.ui.layoutMarginStart
import lib.virgo.showcase.ui.loadImage
import lib.virgo.showcase.ui.setTextSizeInSp
import lib.virgo.showcase.ui.setTint
import lib.virgo.showcase.ui.slidablecontent.SlideableContent
import lib.virgo.showcase.ui.slidablecontent.SlideableContentAdapter
import lib.virgo.showcase.util.ActionType
import lib.virgo.showcase.R
import lib.virgo.showcase.databinding.LayoutTooltipBinding
import androidx.core.graphics.drawable.toDrawable

class TooltipView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : ConstraintLayout(context, attrs) {

    private val binding = LayoutTooltipBinding.inflate(LayoutInflater.from(context), rootView as ViewGroup)
    private var clickListener: ((ActionType, Int) -> (Unit))? = null

    init {
        with(binding) {
            layoutContents.setOnClickListener {
                sendActionType(ActionType.SHOWCASE_CLICKED)
            }

            imageViewTooltipClose.setOnClickListener {
                sendActionType(ActionType.EXIT)
            }
        }
    }

    internal fun bind(tooltipViewState: TooltipViewState) {
        with(binding) {
            with(textViewTooltipTitle) {
                typeface = Typeface.create(
                    tooltipViewState.getTitleTextFontFamily(),
                    tooltipViewState.getTitleTextStyle()
                )
                text = tooltipViewState.getTitle()
                textAlignment = tooltipViewState.getTextPosition()
                setTextColor(tooltipViewState.getTitleTextColor())
                isVisible = tooltipViewState.getTitleVisibility()
                setTextSizeInSp(tooltipViewState.getTitleTextSize())
            }
            with(textViewTooltipDescription) {
                typeface = Typeface.create(
                    tooltipViewState.getDescriptionTextFontFamily(),
                    tooltipViewState.getDescriptionTextStyle()
                )
                text = tooltipViewState.getDescription()
                textAlignment = tooltipViewState.getTextPosition()
                setTextColor(tooltipViewState.getDescriptionTextColor())
                isVisible = tooltipViewState.getDescriptionVisibility()
                setTextSizeInSp(tooltipViewState.getDescriptionTextSize())
            }

            setupViewPager(tooltipViewState.showcaseModel.slideableContentList.orEmpty())

            with(imageViewTopArrow) {
                visibility = tooltipViewState.getTopArrowVisibility()
                setTint(tooltipViewState.getBackgroundColor())
                layoutMarginStart(tooltipViewState.arrowMargin, tooltipViewState.getArrowPercentage())
                setImageDrawable(ContextCompat.getDrawable(context, tooltipViewState.getTopArrowResource()))
            }
            layoutContents.isClickable = tooltipViewState.isShowcaseViewClickable()
            cardContent.visibility = tooltipViewState.getContentVisibility()
            layoutBubble.background = tooltipViewState.getBackgroundColor().toDrawable()
            with(imageView) {
                visibility = tooltipViewState.getImageViewVisibility()
                loadImage(tooltipViewState.getImageUrl())
            }
            textViewSlidableContentPosition.isVisible = tooltipViewState.isSlideableContentVisible()
            viewPager.isVisible = tooltipViewState.isSlideableContentVisible()
            with(imageViewTooltipClose) {
                visibility = tooltipViewState.getCloseButtonVisibility()
                setTint(tooltipViewState.getCloseButtonColor())
            }
            with(imageViewBottomArrow) {
                visibility = tooltipViewState.getBottomArrowVisibility()
                setTint(tooltipViewState.getBackgroundColor())
                layoutMarginStart(tooltipViewState.arrowMargin, tooltipViewState.getArrowPercentage())
                setImageDrawable(ContextCompat.getDrawable(context, tooltipViewState.getBottomArrowResource()))
            }
        }
    }

    private fun setupViewPager(slideableContentList: List<SlideableContent>) {
        with(binding) {
            viewPager.adapter = SlideableContentAdapter(slideableContentList)
            viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    textViewSlidableContentPosition.text =
                        String.format(
                            resources.getString(R.string.slidable_content_position_text),
                            position + 1,
                            slideableContentList.size
                        )
                    textViewSlidableContentPosition.isVisible(
                        shouldShowSlideableContentPosition(slideableContentList.size)
                    )
                }
            })
        }
    }

    private fun shouldShowSlideableContentPosition(slideableContentListSize: Int): Boolean {
        return slideableContentListSize > 1
    }

    fun setCustomContent(@LayoutRes customContent: Int) {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(customContent, null)
        binding.layoutContents.addView(view)
    }

    fun setClickListener(listener: (ActionType, Int) -> (Unit)) {
        clickListener = listener
    }

    private fun sendActionType(actionType: ActionType, index: Int = -1) {
        clickListener?.invoke(actionType, index)
    }
}
