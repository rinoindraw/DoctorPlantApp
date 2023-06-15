package com.rinoindraw.capstonerino.ui.customview

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.rinoindraw.capstonerino.R
import com.rinoindraw.capstonerino.utils.isEmailValid

class AppEmailEditText : AppCompatEditText {

    private lateinit var emailIconDrawable: Drawable

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        emailIconDrawable =
            ContextCompat.getDrawable(context, R.drawable.baseline_alternate_email_24) as Drawable
        compoundDrawablePadding = 16
        setDrawable(emailIconDrawable)

        backgroundTintList = ContextCompat.getColorStateList(context, R.color.very_dark_green)

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val email = s.toString()
                when {
                    email.isBlank() -> error = context.getString(R.string.error_empty_email)
                    !email.isEmailValid() -> error = context.getString(R.string.error_invalid_email)
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })
    }

    private fun setDrawable(
        start: Drawable? = null,
        top: Drawable? = null,
        end: Drawable? = null,
        bottom: Drawable? = null
    ) {
        setCompoundDrawablesWithIntrinsicBounds(start, top, end, bottom)
    }

}