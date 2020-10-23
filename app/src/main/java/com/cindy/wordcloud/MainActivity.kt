package com.cindy.wordcloud

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.ViewTreeObserver
import android.widget.RelativeLayout
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private val TAG: String = javaClass.simpleName
    private val mColorList: ArrayList<Int> = arrayListOf(
        android.R.color.background_dark,
        android.R.color.background_light,
        android.R.color.holo_red_dark,
        android.R.color.holo_blue_dark,
        android.R.color.holo_green_dark,
        android.R.color.darker_gray,
        android.R.color.holo_orange_dark,
        android.R.color.holo_purple
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        vRoot.viewTreeObserver.addOnGlobalLayoutListener(object: ViewTreeObserver.OnGlobalLayoutListener{
            override fun onGlobalLayout() {

                Log.w(TAG, "X: ${vRoot.x}")
                Log.w(TAG, "Y: ${vRoot.y}")
                Log.w(TAG, "left: ${vRoot.left}")
                Log.w(TAG, "top: ${vRoot.top}")
                Log.w(TAG, "right: ${vRoot.right}")
                Log.w(TAG, "bottom: ${vRoot.bottom}")
                Log.w(TAG, "width: ${vRoot.width}")
                Log.w(TAG, "height: ${vRoot.height}")
                vRoot.viewTreeObserver.removeOnGlobalLayoutListener(this)

            }
        })
        setFirstTextView()

    }

    fun setFirstTextView(){
        val textView: TextView = TextView(this)
        val layoutParams: RelativeLayout.LayoutParams = RelativeLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT)
        textView.layoutParams = layoutParams
        textView.text = "Text1"
        textView.textSize = 40f
        val randomColor = getRandomColor()
        Log.d(TAG, "mColorList: $mColorList")
        Log.d(TAG, "randomColor: $randomColor")
        textView.setTextColor(randomColor)
        vRoot.addView(textView)
        textView.viewTreeObserver.addOnGlobalLayoutListener(object: ViewTreeObserver.OnGlobalLayoutListener{
            override fun onGlobalLayout() {

                Log.i(TAG, "X: ${textView.x}")
                Log.i(TAG, "Y: ${textView.y}")
                Log.i(TAG, "left: ${textView.left}")
                Log.i(TAG, "top: ${textView.top}")
                Log.i(TAG, "right: ${textView.right}")
                Log.i(TAG, "bottom: ${textView.bottom}")
                Log.i(TAG, "width: ${textView.width}")
                Log.i(TAG, "height: ${textView.height}")
                textView.viewTreeObserver.removeOnGlobalLayoutListener(this)

            }
        })

    }

    fun getRandomColor() = mColorList[Random.nextInt(0, mColorList.size)]

}