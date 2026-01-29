package lib.virgo.showcase.ui.showcase

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import dagger.hilt.android.AndroidEntryPoint
import lib.virgo.showcase.showcase.ShowcaseModel
import lib.virgo.showcase.util.ActionType

@AndroidEntryPoint
class ShowcaseActivity : AppCompatActivity() {

	private lateinit var handler: Handler
	private var hasCallback = false
	private var requestCode = 0

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		hasCallback = intent.getBooleanExtra(HAS_CALLBACK, false)
		if (hasCallback) requestCode = intent.getIntExtra(REQUEST_CODE, 0)
		handler = Handler(Looper.getMainLooper())
		val showcaseModel = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
			intent?.getParcelableExtra(BUNDLE_KEY, ShowcaseModel::class.java)
		} else {
			intent?.extras?.getParcelable(BUNDLE_KEY)
		}
		showcaseModel?.let { model ->
			val view = ShowcaseView(this).apply {
				setShowcaseModel(model)
				setClickListener { actionType, index ->
					finishShowcase(actionType, index)
				}
			}
			setContentView(view)
			if (model.isShowcaseViewVisibleIndefinitely.not()) {
				handler.postDelayed(
					{ finishShowcase(ActionType.EXIT) },
					model.showDuration
				)
			}
			updateStatusBar(model.isStatusBarVisible)
		}

		onBackPressedDispatcher.addCallback {
			finishShowcase(ActionType.EXIT)
		}
	}

	fun finishShowcase(actionType: ActionType, index: Int = -1) {
		val bundle = Bundle().apply {
			putSerializable(ShowcaseView.Companion.KEY_ACTION_TYPE, actionType)
			putInt(ShowcaseView.Companion.KEY_SELECTED_VIEW_INDEX, index)
		}
		handler.removeCallbacksAndMessages(null)
		if (hasCallback) setResult(requestCode, Intent().apply { putExtras(bundle) })
		else setResult(RESULT_OK, Intent().apply { putExtras(bundle) })
		finish()
		overridePendingTransition(0, android.R.anim.fade_out)
	}

	private fun updateStatusBar(isStatusBarVisible: Boolean) {
		val controller = WindowCompat.getInsetsController(window, window.decorView)
		if (isStatusBarVisible) {
			controller.show(WindowInsetsCompat.Type.statusBars())
		} else {
			controller.hide(WindowInsetsCompat.Type.statusBars())
		}
	}

	companion object {

		internal const val BUNDLE_KEY = "bundle_key"
		const val HAS_CALLBACK = "has_callback"
		const val REQUEST_CODE = "request_code"
	}
}
