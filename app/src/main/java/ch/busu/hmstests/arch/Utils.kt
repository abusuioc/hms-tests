package ch.busu.hmstests.arch

val Any.TAG: String
    get() {
        return javaClass.simpleName
    }

val Any?.exhaustive get() = Unit