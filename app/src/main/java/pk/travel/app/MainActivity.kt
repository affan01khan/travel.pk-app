package pk.travel.app

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.unit.dp
import android.webkit.CookieManager

private const val HOME_URL = "https://travel.pk/"

class MainActivity : ComponentActivity() {
    private var webView: WebView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                var progress by remember { mutableIntStateOf(0) }
                var loading by remember { mutableStateOf(true) }
                BackHandler(enabled = webView?.canGoBack() == true) {
                    webView?.goBack()
                }
                Surface(color = Color.White, modifier = Modifier.fillMaxSize()) {
                    Box(Modifier.fillMaxSize()) {
                        AndroidView(
                            modifier = Modifier.fillMaxSize(),
                            factory = { context ->
                                WebView(context).apply {
                                    webView = this
                                    settings.javaScriptEnabled = true
                                    settings.domStorageEnabled = true
                                    settings.loadsImagesAutomatically = true
                                    settings.javaScriptCanOpenWindowsAutomatically = true
                                    settings.setSupportMultipleWindows(false)
                                    settings.useWideViewPort = true
                                    settings.loadWithOverviewMode = true
                                    settings.mediaPlaybackRequiresUserGesture = true
                                    CookieManager.getInstance().setAcceptCookie(true)
                                    CookieManager.getInstance().setAcceptThirdPartyCookies(this, true)
                                    webChromeClient = object : WebChromeClient() {
                                        override fun onProgressChanged(view: WebView?, newProgress: Int) {
                                            progress = newProgress
                                            loading = newProgress < 100
                                        }
                                    }
                                    webViewClient = object : WebViewClient() {
                                        override fun shouldOverrideUrlLoading(
                                            view: WebView,
                                            request: WebResourceRequest
                                        ): Boolean {
                                            val uri = request.url
                                            val host = uri.host.orEmpty().lowercase()
                                            val isTravelPk = host == "travel.pk" || host.endsWith(".travel.pk")
                                            if (isTravelPk && uri.scheme == "https") return false
                                            if (uri.scheme == "http" || uri.scheme == "https" ||
                                                uri.scheme == "tel" || uri.scheme == "mailto") {
                                                try {
                                                    context.startActivity(Intent(Intent.ACTION_VIEW, uri))
                                                } catch (_: ActivityNotFoundException) { }
                                                return true
                                            }
                                            return true
                                        }

                                        override fun onPageFinished(view: WebView?, url: String?) {
                                            loading = false
                                        }
                                    }
                                    loadUrl(HOME_URL)
                                }
                            },
                            update = { webView = it }
                        )
                        if (loading) {
                            LinearProgressIndicator(
                                progress = { progress / 100f },
                                modifier = Modifier.align(Alignment.TopCenter)
                            )
                            if (progress == 0) {
                                CircularProgressIndicator(
                                    modifier = Modifier.align(Alignment.Center).padding(16.dp),
                                    color = Color(0xFF1769AA)
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onDestroy() {
        webView?.apply {
            stopLoading()
            destroy()
        }
        webView = null
        super.onDestroy()
    }
}
