package com.billeto.components.jsbridge

interface CompletionHandler<T> {
  fun complete(retValue: T)
  fun complete()
  fun setProgressData(value: T)
}