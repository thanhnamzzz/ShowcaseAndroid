package gbs.test.testshowcase

import com.trendyol.medusalib.navigator.Navigator

class NavigationOperationHandler constructor(private val navigator: Navigator) {

	fun execute(navigationOperation: NavigationOperation) {
		navigationOperation.operate(navigator)
	}
}
