package lib.virgo.showcase.showcase

sealed class ShowcaseState {
    object IDLE : ShowcaseState()
    object DESTROY : ShowcaseState()
}