package lib.virgo.showcase.ui.slidablecontent

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import lib.virgo.showcase.ui.loadImage
import lib.virgo.showcase.ui.setTextSizeInSp
import lib.virgo.showcase.databinding.ItemSlidableContentBinding

internal class SlideableContentAdapter(private val slideableContentList: List<SlideableContent>) :
	RecyclerView.Adapter<SlideableContentAdapter.ViewPagerViewHolder>() {

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerViewHolder =
		ViewPagerViewHolder(
			ItemSlidableContentBinding.inflate(
				LayoutInflater.from(parent.context),
				parent,
				false
			)
		)

	override fun onBindViewHolder(holder: ViewPagerViewHolder, position: Int) {
		holder.bind(slideableContentList[position])
	}

	override fun getItemCount(): Int = slideableContentList.size

	inner class ViewPagerViewHolder(
		private val binding: ItemSlidableContentBinding,
	) : RecyclerView.ViewHolder(binding.root) {

		fun bind(item: SlideableContent) {
			with(binding) {
				val viewState = SlideableContentViewState(item)

				with(textViewTitle) {
					typeface = Typeface.create(
						viewState.slideableContent.titleTextFontFamily,
						viewState.slideableContent.titleTextStyle,
					)
					text = viewState.slideableContent.title
					textAlignment = viewState.getTextPosition()
					setTextColor(viewState.slideableContent.titleTextColor)
					isVisible = viewState.isTitleVisible()
					setTextSizeInSp(viewState.slideableContent.titleTextSize)
				}
				with(textViewDescription) {
					typeface = Typeface.create(
						viewState.slideableContent.descriptionTextFontFamily,
						viewState.slideableContent.descriptionTextStyle,
					)
					text = viewState.slideableContent.description
					textAlignment = viewState.getTextPosition()
					setTextColor(viewState.slideableContent.descriptionTextColor)
					isVisible = viewState.isDescriptionVisible()
					setTextSizeInSp(viewState.slideableContent.descriptionTextSize)
				}

				imageView.loadImage(viewState.slideableContent.imageUrl)
			}
		}
	}
}
