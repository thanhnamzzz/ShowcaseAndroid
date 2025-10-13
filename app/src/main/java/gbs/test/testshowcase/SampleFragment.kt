package gbs.test.testshowcase

import android.app.Activity
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import com.trendyol.medusalib.navigator.Navigator
import gbs.test.testshowcase.databinding.FragmentSampleBinding
import lib.virgo.showcase.showcase.ShowcaseManager
import lib.virgo.showcase.ui.showcase.HighlightType
import lib.virgo.showcase.ui.showcase.ShowcaseView
import lib.virgo.showcase.ui.slidablecontent.SlidableContent
import lib.virgo.showcase.ui.slidablecontent.slidableContent
import lib.virgo.showcase.ui.tooltip.ArrowPosition
import lib.virgo.showcase.ui.tooltip.TextPosition
import lib.virgo.showcase.util.ActionType

class SampleFragment : Fragment() {
	private var binding: FragmentSampleBinding? = null
	private val fragmentNavigator: Navigator?
		get() = (activity as? MainActivity)?.getNavigator()
	private lateinit var medusaLifecycleOwner: MedusaLifecycleOwner
	private val isStatusBarVisible by lazy {
		requireArguments().getBoolean(KEY_IS_STATUS_BAR_VISIBLE)
	}

	override fun onResume() {
		super.onResume()
		if (isVisible) {
			medusaLifecycleOwner.handleResume()
		}
	}

	override fun onPause() {
		super.onPause()
		if (isVisible) {
			medusaLifecycleOwner.handlePause()
		}
	}

	override fun onDestroyView() {
		medusaLifecycleOwner.handleDestroy()
		super.onDestroyView()
	}

	override fun onStart() {
		super.onStart()
		if (isVisible) {
			medusaLifecycleOwner.handleStart()
		}
	}

	override fun onStop() {
		medusaLifecycleOwner.handleStop()
		super.onStop()
	}

	override fun onHiddenChanged(hidden: Boolean) {
		super.onHiddenChanged(hidden)
		if (hidden) {
			medusaLifecycleOwner.handleStop()
		} else {
			updateStatusBar(isStatusBarVisible)
			medusaLifecycleOwner.handleStart()
		}
	}

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?,
	): View {
		val binding = FragmentSampleBinding.inflate(inflater, container, false)
		this.binding = binding
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		medusaLifecycleOwner = MedusaLifecycleOwner()
		medusaLifecycleOwner.handleCreate()

		updateStatusBar(isStatusBarVisible)

		with(binding!!) {
			buttonTop.setOnClickListener {
				val context = requireContext()
				ShowcaseManager.Builder()
					.focus(buttonTop)
					.titleText("Title For Top!")
					.descriptionText("Simple, short description for top tooltip.")
					.titleTextColor(ContextCompat.getColor(context, R.color.colorAccent))
					.titleTextFontFamily("sans-serif")
					.descriptionTextColor(ContextCompat.getColor(context, R.color.colorPrimary))
					.backgroundColor(ContextCompat.getColor(context, R.color.colorPrimaryDark))
					.imageUrl(
						"https://upload.wikimedia.org/wikipedia/commons/7/7c/Aspect_ratio_16_9_example.jpg"
					)
					.showCloseButton(true)
					.cancellableFromOutsideTouch(true)
					.arrowPosition(ArrowPosition.AUTO)
					.highlightType(HighlightType.RECTANGLE)
					.textPosition(TextPosition.START)
					.windowBackgroundAlpha(0)
					.titleTextSize(30f)
					.statusBarVisible(isStatusBarVisible)
					.build()
					.show(
						this@SampleFragment,
						REQUEST_CODE_SHOWCASE_CLICKED,
						launcher,
						medusaLifecycleOwner
					)
				if (isFragmentTransactionTest) {
					view.postDelayed({
						fragmentNavigator?.start(SecondarySampleFragment(1))
					}, 3000)
				}
			}

			buttonCenter.setOnClickListener {
				val context = requireContext()
				ShowcaseManager.Builder()
					.focus(buttonCenter)
					.titleText("Title For Center!")
					.descriptionText("Center is here.")
					.titleTextColor(ContextCompat.getColor(context, R.color.white))
					.titleTextStyle(Typeface.BOLD)
					.backgroundColor(ContextCompat.getColor(context, R.color.colorPrimaryDark))
					.closeButtonColor(ContextCompat.getColor(context, R.color.white))
					.showCloseButton(true)
					.arrowPosition(ArrowPosition.DOWN)
					.highlightType(HighlightType.CIRCLE)
					.textPosition(TextPosition.CENTER)
					.statusBarVisible(isStatusBarVisible)
					.build()
					.show(
						this@SampleFragment,
						REQUEST_CODE_SHOWCASE_CLICKED,
						launcher,
						viewLifecycleOwner
					)
				if (isFragmentTransactionTest) {
					view.postDelayed({
						requireActivity().supportFragmentManager
							.beginTransaction()
							.replace(R.id.containerMain, SecondarySampleFragment(2))
							.commitAllowingStateLoss()
					}, 3000)
				}
			}

			buttonBottom.setOnClickListener {
				ShowcaseManager.Builder()
					.focus(buttonBottom)
					.titleText("Title without a description")
					.titleTextSize(16f)
					.titleTextFontFamily("sans-serif")
					.titleTextStyle(Typeface.BOLD)
					.showCloseButton(true)
					.arrowResource(android.R.drawable.arrow_down_float)
					.arrowPosition(ArrowPosition.AUTO)
					.highlightType(HighlightType.RECTANGLE)
					.highlightPadding(8f)
					.textPosition(TextPosition.START)
					.highlightRadius(
						bottomEndRadius = 16f,
						topStartRadius = 16f,
						topEndRadius = 16f,
						bottomStartRadius = 16f
					)
					.cancellableFromOutsideTouch(true)
					.toolTipVisible(false)
					.statusBarVisible(isStatusBarVisible)
					.build()
					.show(
						this@SampleFragment,
						REQUEST_CODE_SHOWCASE_CLICKED,
						launcher,
						medusaLifecycleOwner
					)
			}

			buttonFocusMultipleView.setOnClickListener {
				ShowcaseManager.Builder()
					.focus(textView, imageView)
					.titleText("Multiple View Focus")
					.titleTextSize(16f)
					.showCloseButton(true)
					.arrowPosition(ArrowPosition.UP)
					.highlightType(HighlightType.RECTANGLE)
					.highlightPadding(8f)
					.textPosition(TextPosition.START)
					.statusBarVisible(isStatusBarVisible)
					.build()
					.show(
						this@SampleFragment,
						REQUEST_CODE_SHOWCASE_CLICKED,
						launcher,
						medusaLifecycleOwner
					)
			}

			buttonSlidableContent.setOnClickListener {
				ShowcaseManager.Builder()
					.focus(buttonSlidableContent)
					.setSlidableContentList(buildSlidableContentList())
					.showCloseButton(false)
					.cancellableFromOutsideTouch(true)
					.build()
					.show(
						this@SampleFragment,
						REQUEST_CODE_SHOWCASE_CLICKED,
						launcher,
						medusaLifecycleOwner
					)
			}

			imageTop.setOnClickListener {
				ShowcaseManager.Builder()
					.focus(imageTop)
					.customContent(R.layout.view_custom_content)
					.cancellableFromOutsideTouch(true)
					.showcaseViewClickable(true)
					.statusBarVisible(isStatusBarVisible)
					.build()
					.show(
						this@SampleFragment,
						REQUEST_CODE_SHOWCASE_CLICKED,
						launcher,
						medusaLifecycleOwner
					)
			}

			buttonVanishingShowcase.setOnClickListener {
				ShowcaseManager.Builder()
					.focus(buttonVanishingShowcase)
					.showcaseViewClickable(true)
					.statusBarVisible(isStatusBarVisible)
					.showDurationMillis(4000L)
					.titleText("This showcase will vanish in 4 seconds")
					.showcaseViewVisibleIndefinitely(false)
					.build()
					.show(
						this@SampleFragment,
						REQUEST_CODE_SHOWCASE_CLICKED,
						launcher,
						medusaLifecycleOwner
					)
			}
		}
	}

	private fun buildSlidableContentList(): List<SlidableContent> {
		val context = requireContext()
		val baseSlidableContent = slidableContent {
			imageUrl =
				"https://upload.wikimedia.org/wikipedia/commons/7/7c/Aspect_ratio_16_9_example.jpg"
			titleTextColor = ContextCompat.getColor(context, R.color.black)
			titleTextSize = 16f
			titleTextFontFamily = "sans-serif"
			titleTextStyle = Typeface.BOLD
			descriptionTextColor = ContextCompat.getColor(context, R.color.colorPrimaryDark)
			descriptionTextSize = 14f
			descriptionTextFontFamily = "sans-serif"
			descriptionTextStyle = Typeface.NORMAL
			textPosition = TextPosition.CENTER
		}

		return with(baseSlidableContent) {
			listOf(
				copy(title = "Title 1", description = "Description 1"),
				copy(title = "Title 2", description = "Description 2"),
				copy(title = "Title 3", description = "Description 3"),
			)
		}
	}

	private val launcher =
		registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
			it.data?.let { data ->
				if (it.resultCode == Activity.RESULT_OK || it.resultCode == REQUEST_CODE_SHOWCASE_CLICKED) {
					val actionType =
						data.getSerializableExtra(ShowcaseView.KEY_ACTION_TYPE) as ActionType
					val selectedViewIndex =
						data.getIntExtra(ShowcaseView.KEY_SELECTED_VIEW_INDEX, -1)
					Log.i(
						"MainActivity",
						"onActivityResult: actionType=${actionType.name} and selected view index=${selectedViewIndex}"
					)
				}
			}
		}

	companion object {

		private const val REQUEST_CODE_SHOWCASE_CLICKED = 101
		private const val isFragmentTransactionTest = false
		private const val KEY_IS_STATUS_BAR_VISIBLE = "key_is_status_bar_visible"

		fun newInstance(isStatusBarVisible: Boolean) = SampleFragment().apply {
			arguments = bundleOf(KEY_IS_STATUS_BAR_VISIBLE to isStatusBarVisible)
		}

		fun Fragment.updateStatusBar(isStatusBarVisible: Boolean) {
			val window = requireActivity().window
			val controller = WindowCompat.getInsetsController(window, window.decorView)
			if (isStatusBarVisible) {
				controller.show(WindowInsetsCompat.Type.statusBars())
			} else {
				controller.hide(WindowInsetsCompat.Type.statusBars())
			}
		}
	}
}