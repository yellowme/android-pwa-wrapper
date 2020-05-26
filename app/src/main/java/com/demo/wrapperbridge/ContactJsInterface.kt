package com.demo.wrapperbridge

import android.app.Activity
import android.content.Intent
import android.provider.ContactsContract
import android.provider.ContactsContract.CommonDataKinds.Phone
import android.webkit.JavascriptInterface
import com.billeto.components.jsbridge.CompletionHandler
import org.json.JSONObject

class ContactJsInterface(
  private val activity: Activity,
  private val delegate: ContactChooseDelegate
) {

  companion object {
    const val NAME_SPACE = "ContactJs"
    const val REQUEST_CONTACT = 1
  }

  private lateinit var handler: CompletionHandler<String>

  @JavascriptInterface
  fun choose(arg: Any?, handler: CompletionHandler<String>) {
    this.handler = handler
    val intent = Intent(Intent.ACTION_PICK).apply {
      type = Phone.CONTENT_TYPE
    }
    delegate.selectContact(intent, REQUEST_CONTACT)
  }

  fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    if (requestCode == REQUEST_CONTACT && resultCode == Activity.RESULT_OK) {
      data?.data?.let { contactUri ->
        val projection: Array<String> = arrayOf(ContactsContract.PhoneLookup.DISPLAY_NAME, Phone.NUMBER)
        activity.contentResolver.query(contactUri, projection, null, null, null).use { cursor ->
          cursor?.let {
            if (cursor.moveToFirst()) {
              val nameIndex = cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME)
              val numberIndex = cursor.getColumnIndex(Phone.NUMBER)
              val jsonObject = JSONObject()
              if (nameIndex > -1) {
                val name = cursor.getString(nameIndex)
                jsonObject.put("name", name)
              }
              if (numberIndex > -1) {
                val number = cursor.getString(numberIndex)
                jsonObject.put("number", number)
              }
              handler.complete(jsonObject.toString())
            }
          }
        }
      }
    }
  }

  interface ContactChooseDelegate {
    fun selectContact(intent: Intent, code: Int)
  }
}