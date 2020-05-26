package com.demo.wrapperbridge

import android.content.Intent
import android.os.Bundle
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.webView
import kotlinx.android.synthetic.main.activity_main.webViewContainer

class MainActivity : AppCompatActivity(), ContactJsInterface.ContactChooseDelegate {

  private lateinit var contactJs: ContactJsInterface

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    contactJs = ContactJsInterface(this, this)
    webView.addJavascriptObject(contactJs, ContactJsInterface.NAME_SPACE)
    webView.loadUrl("https://e48528fd.ngrok.io/")
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    contactJs.onActivityResult(requestCode, resultCode, data)
  }

  override fun onDestroy() {
    super.onDestroy()
    webViewContainer.removeAllViews()
    webView.clearOnDestroy()
  }

  private fun WebView.clearOnDestroy() {
    clearCache(true)
    clearHistory()
    removeAllViews()
    destroy()
  }

  override fun selectContact(intent: Intent, code: Int) {
    if (intent.resolveActivity(packageManager) != null) {
      startActivityForResult(intent, code)
    }
  }
}
