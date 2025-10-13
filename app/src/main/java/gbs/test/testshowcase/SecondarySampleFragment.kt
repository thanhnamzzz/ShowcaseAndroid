package gbs.test.testshowcase

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import gbs.test.testshowcase.SampleFragment.Companion.updateStatusBar

class SecondarySampleFragment(private val index: Int) :
	Fragment(R.layout.fragment_secondary_sample) {
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		view.findViewById<TextView>(R.id.text_id).text = "Fragment $index"
	}

	override fun onHiddenChanged(hidden: Boolean) {
		super.onHiddenChanged(hidden)
		if (hidden.not()) {
			updateStatusBar(true)
		}
	}
}