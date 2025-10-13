package gbs.test.testshowcase

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import gbs.test.testshowcase.databinding.ActivityTestBinding
import lib.virgo.showcase.showcase.ShowcaseManager

class TestActivity : AppCompatActivity() {
	private lateinit var binding: ActivityTestBinding
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		enableEdgeToEdge()
		binding = ActivityTestBinding.inflate(layoutInflater)
		setContentView(binding.root)
		ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
			val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
			v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
			insets
		}
		binding.btnMeasure.setOnClickListener {
			ShowcaseManager.Builder()
				.focus(binding.btnMeasure)
				.showcaseViewClickable(true)
				.statusBarVisible(true)
				.showDurationMillis(4000L)
				.titleText("This showcase will vanish in 4 seconds")
				.showcaseViewVisibleIndefinitely(false)
				.windowBackgroundAlpha(0)
				.cancellableFromOutsideTouch(true)
				.build()
				.show(this, 444, launcher, this)
		}
	}

	private val launcher =
		registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
			Log.d("Namzzz", "TestActivity: ${it.resultCode}")
		}
}