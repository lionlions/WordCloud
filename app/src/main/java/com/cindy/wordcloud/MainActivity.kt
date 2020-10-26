package com.cindy.wordcloud

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.ViewTreeObserver
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private val TAG: String = javaClass.simpleName
    private val mRootXArray: MutableList<Int> = mutableListOf()
    private val mRootYArray: MutableList<Int> = mutableListOf()
    private val mWordCloudTextViewList: MutableList<TextView> = mutableListOf()
    private val mColorList: MutableList<Int> = mutableListOf(
        android.R.color.background_dark,
        android.R.color.holo_red_dark,
        android.R.color.holo_blue_dark,
        android.R.color.holo_green_dark,
        android.R.color.darker_gray,
        android.R.color.holo_orange_dark,
        android.R.color.holo_purple
    )
    private val mWordCloudTextSizeList: MutableList<Float> = mutableListOf(
        35f, 20f, 15f
    )
    private val mWordCloudTextList: MutableList<String> = mutableListOf(
        "Text1", "Text2", "Text3", "Text4", "Text5", "Text6", "Text7", "Text8", "Text9", "Text10",
        "Text11", "Text12", "Text13", "Text14", "Text15", "Text16", "Text17", "Text18", "Text19", "Text20"
    )
    private var mWordCloudTextViewDownCount: Int = 0

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
                for (i in 0 until vRoot.width){
                    mRootXArray.add(i)
                }
                for (i in 0 until vRoot.height){
                    mRootYArray.add(i)
                }
                setTextView()

            }
        })

    }

    fun setTextView(x: Int = -1, y: Int = -1){
        Log.v(TAG, "===== setTextView =====")
        Log.w(TAG, "mWordCloudTextViewDownCount: $mWordCloudTextViewDownCount")
        Log.w(TAG, "x: $x")
        Log.w(TAG, "y: $y")

        if(mWordCloudTextViewDownCount==mWordCloudTextList.size){
            return
        }
        val textView: TextView = TextView(this)
        val layoutParams: RelativeLayout.LayoutParams = RelativeLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
        if(mWordCloudTextViewDownCount==0){
            layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT)
        }else{
            if(x!=-1){
                layoutParams.leftMargin= x
            }
            if(y!=-1){
                layoutParams.topMargin = y
            }

        }
        textView.layoutParams = layoutParams
        textView.id = R.id.word_cloud + mWordCloudTextViewDownCount
        textView.text = mWordCloudTextList[mWordCloudTextViewDownCount]
        textView.textSize = getRandomItemInList(mWordCloudTextSizeList) as Float
        val randomColor = getRandomItemInList(mColorList)
        textView.setBackgroundColor(ContextCompat.getColor(this, randomColor as Int))
        textView.setTextColor(ContextCompat.getColor(this, android.R.color.white))
        vRoot.addView(textView)
        mWordCloudTextViewDownCount++
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
                if(!checkOverLapping(textView)){
                    for (i in textView.left .. textView.right){
                        if(mRootXArray.contains(i)){
                            mRootXArray.remove(i)
                        }
                    }
                    for (i in textView.top .. textView.bottom){
                        if(mRootYArray.contains(i)){
                            mRootYArray.remove(i)
                        }
                    }
                    Log.i(TAG, "mRootXArray: $mRootXArray")
                    Log.i(TAG, "mRootYArray: $mRootYArray")
                    mWordCloudTextViewList.add(textView)
                }else{
                    vRoot.removeView(textView)
                    mWordCloudTextViewDownCount--
                }
                if(mRootXArray.size>0 && mRootYArray.size>0){
                    val nextX: Int = getRandomItemInList(mRootXArray) as Int
                    val nextY: Int = getRandomItemInList(mRootYArray) as Int
                    setTextView(nextX, nextY)
                }

            }
        })

    }

    fun getRandomItemInList(list: MutableList<*>) = list[Random.nextInt(0, list.size)]

    fun checkOverLapping(textView: TextView): Boolean{
        var isXOverLapping: Boolean = false
        var isYOverLapping: Boolean = false
        var overLapWidth: Int = 0
        var overLapHeight: Int = 0

        for(item in mWordCloudTextViewList){
            Log.d(TAG, "X: ${item.x}")
            Log.d(TAG, "Y: ${item.y}")
            Log.d(TAG, "z: ${item.z}")
            Log.d(TAG, "left: ${item.left}")
            Log.d(TAG, "top: ${item.top}")
            Log.d(TAG, "right: ${item.right}")
            Log.d(TAG, "bottom: ${item.bottom}")
            Log.d(TAG, "width: ${item.width}")
            Log.d(TAG, "height: ${item.height}")

            outSite@for(i in item.left .. item.right){
                inSite@for(j in textView.left .. textView.right){
                    if(i==j){
                        isXOverLapping = true
                        break@outSite
                    }
                }
            }

            outSite@for(i in item.top .. item.bottom){
                inSite@for(j in textView.top .. textView.bottom){
                    if(i==j){
                        isYOverLapping = true
                        break@outSite
                    }
                }
            }

            if(isXOverLapping && isYOverLapping){
                Log.i(TAG, "重疊了，計算重覆面積")
                overLapWidth = if(item.left <= textView.left){
                    if(item.right <= textView.right){
                        item.width - textView.left
                    }else{
                        textView.width
                    }
                }else{
                    if(item.right <= textView.right){
                        textView.right - item.left
                    }else{
                        item.width
                    }
                }

                overLapHeight = if(item.top <= textView.top){
                    if(item.bottom <= textView.bottom){
                        item.height - textView.top
                    }else{
                        textView.height
                    }
                }else{
                    if(item.bottom <= textView.bottom){
                        item.height
                    }else{
                        textView.bottom - item.top
                    }
                }

                Log.e(TAG, "overLapWidth: $overLapWidth")
                Log.e(TAG, "overLapHeight: $overLapHeight")
                if(overLapWidth * overLapHeight >= (item.width * item.height + textView.width * textView.height) / 3 * 2) return true

            }

        }
        return false
    }
}