package com.nims.myapplication

import android.content.Intent
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.URLSpan
import android.text.util.Linkify
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextLayoutInput
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.text.util.LinkifyCompat
import com.nims.myapplication.ui.theme.MyApplicationTheme
import java.util.*

class MainActivity : ComponentActivity() {
    private lateinit var result: MutableState<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    init()
                    var text by remember { mutableStateOf("https://www.naver.com AND https://www.daum.net") }
                    Column{
                        Greeting(text)
                        Result()
                    }
                }
            }
        }
    }

    @Composable
    private fun init() {
        result = remember { mutableStateOf("click url : ") }
    }

    @Composable
    fun Greeting(text: String) {
        val context = LocalContext.current
        val linkTextView = remember { TextView(context) }
        AndroidView(
            factory = { linkTextView },
            modifier = Modifier.wrapContentSize()
        ) {
            it.text = text
            LinkifyCompat.addLinks(it, Linkify.WEB_URLS)
            it.movementMethod = LinkMovementMethod.getInstance()

            it.handleUrlClicks { url ->
                result.value = "click url : $url"
            }
        }
    }

    @Composable
    fun Result() {
        Text(
            modifier = Modifier.wrapContentSize(),
            text = result.value
        )
    }

    fun TextView.handleUrlClicks(onClicked: ((String) -> Unit)? = null) {
        text = SpannableStringBuilder.valueOf(text).apply {
            getSpans(0, length, URLSpan::class.java).forEach {
                setSpan(
                    object : ClickableSpan() {
                        override fun onClick(widget: View) {
                            onClicked?.invoke(it.url)
                        }
                    },
                    getSpanStart(it),
                    getSpanEnd(it),
                    Spanned.SPAN_INCLUSIVE_EXCLUSIVE
                )
                removeSpan(it)
            }
        }
        movementMethod = LinkMovementMethod.getInstance()
    }

    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        MyApplicationTheme {
            Greeting("Android")
        }
    }
}