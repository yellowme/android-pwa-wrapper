package com.billeto.components.jsbridge

interface OnReturnValue<T> {
  fun onValue(retValue: T)
}