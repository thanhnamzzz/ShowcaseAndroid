package gbs.test.testshowcase

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry

class MedusaLifecycleOwner : LifecycleOwner {

	private val lifecycleRegistry = LifecycleRegistry(this)
	override val lifecycle: Lifecycle
		get() = lifecycleRegistry

	fun handleCreate() {
		lifecycleRegistry.currentState = Lifecycle.State.CREATED
	}

	fun handleStart() {
		lifecycleRegistry.currentState = Lifecycle.State.STARTED
	}

	fun handleDestroy() {
		lifecycleRegistry.currentState = Lifecycle.State.DESTROYED
	}

	fun handleResume() {
		lifecycleRegistry.currentState = Lifecycle.State.RESUMED
	}

	fun handlePause() {
		handleStart()
	}

	fun handleStop() {
		handleCreate()
	}
}
