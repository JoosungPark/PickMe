package com.joosung.pickme.common

import io.reactivex.disposables.CompositeDisposable

interface CompositeDisposablePresentable {
    val disposables: CompositeDisposable
}