package mobileservices.demo.arch

val Any.TAG: String
    get() {
        return javaClass.simpleName
    }

val Any?.exhaustive get() = Unit