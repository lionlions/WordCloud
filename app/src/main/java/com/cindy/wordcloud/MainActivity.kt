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
    private var mRootRight: Int = -1
    private var mRootBottom: Int = -1
    private val KEY_X: String = "x"
    private val KEY_Y: String = "y"
    private val mRootXYArray: MutableList<MutableMap<String, Int>> = mutableListOf()
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
    private val mWordCloudTextSizeList: MutableList<Int> = mutableListOf(
        42, 40, 38, 36, 34, 30, 26, 22, 20, 16, 12, 8
    )
    private val mWordCloudTextRotateDegreeList: MutableList<Int> = mutableListOf(
        0, 90, 270
    )
    private val mWordCloudTextList: MutableList<String> = mutableListOf()
    private var mWordCloudTextViewDownCount: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initData()
        vRoot.viewTreeObserver.addOnGlobalLayoutListener(object: ViewTreeObserver.OnGlobalLayoutListener{
            override fun onGlobalLayout() {

                Log.w(TAG, "X: ${vRoot.x}")
                Log.w(TAG, "Y: ${vRoot.y}")
                Log.w(TAG, "width: ${vRoot.width}")
                Log.w(TAG, "height: ${vRoot.height}")
                vRoot.viewTreeObserver.removeOnGlobalLayoutListener(this)
                mRootRight = vRoot.width
                mRootBottom = vRoot.height
                for (i in 0 .. vRoot.width){
                    for (j in 0 .. vRoot.height){
                        mRootXYArray.add(mutableMapOf(KEY_X to i, KEY_Y to j))
                    }
                }
                Log.i(TAG, "mRootXYArray.size: ${mRootXYArray.size}")
                setTextView()

            }
        })

    }

    fun initData(){
        for(i in 0 until 100){
            mWordCloudTextList.add("Text${i+1}")
        }
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
        textView.id = R.id.word_cloud + mWordCloudTextViewDownCount
        textView.text = mWordCloudTextList[mWordCloudTextViewDownCount]
        textView.textSize = (getRandomItemInList(mWordCloudTextSizeList) as Int).toFloat()
//        textView.rotation = (getRandomItemInList(mWordCloudTextRotateDegreeList) as Int).toFloat()
        val randomColor = getRandomItemInList(mColorList)
        textView.setTextColor(ContextCompat.getColor(this, randomColor as Int))
//        textView.setTextColor(ContextCompat.getColor(this, android.R.color.white))
        textView.setHorizontallyScrolling(true)
        val layoutParams: RelativeLayout.LayoutParams = RelativeLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
        if(mWordCloudTextViewDownCount==0){
            layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT)
        }else{
            if(checkBoundary(textView, x, y)){
                if(x!=-1){
                    layoutParams.leftMargin= x
                }
                if(y!=-1){
                    layoutParams.topMargin = y
                }
            }else{
                setNextTextView()
                return
            }
        }
        textView.layoutParams = layoutParams
        vRoot.addView(textView)
        mWordCloudTextViewDownCount++
        textView.viewTreeObserver.addOnGlobalLayoutListener(object: ViewTreeObserver.OnGlobalLayoutListener{
            override fun onGlobalLayout() {

                Log.i(TAG, "X: ${textView.y}")
                Log.i(TAG, "Y: ${textView.y}")
                Log.i(TAG, "left: ${textView.left}")
                Log.i(TAG, "top: ${textView.top}")
                Log.i(TAG, "right: ${textView.right}")
                Log.i(TAG, "bottom: ${textView.bottom}")
                Log.i(TAG, "width: ${textView.width}")
                Log.i(TAG, "height: ${textView.height}")
                textView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                if(!checkOverLapping(textView)){
//                    for (i in textView.left .. textView.right){
//                        for (j in textView.top .. textView.bottom){
//                            mRootXYArray.filter {
//                                it[KEY_X]==i && it[KEY_Y]==j
//                            }.apply {
//                                mRootXYArray.removeAll(this)
//                            }
//                        }
//                    }
                    mWordCloudTextViewList.add(textView)
                }else{
                    vRoot.removeView(textView)
                    mWordCloudTextViewDownCount--
                }
                setNextTextView()

            }
        })

    }

    fun getTextSize(): Float{
        val contentSize = mWordCloudTextList.size
        val range = contentSize / mWordCloudTextSizeList.size
        val index = mWordCloudTextViewDownCount / range
        val size = mWordCloudTextSizeList[index]
        Log.w(TAG, "mWordCloudTextViewDownCount: $mWordCloudTextViewDownCount")
        Log.w(TAG, "mWordCloudTextSizeList.size: ${mWordCloudTextSizeList.size}")
        Log.w(TAG, "range: $range")
        Log.w(TAG, "index: $index")
        Log.w(TAG, "size: $size")
        return size.toFloat()
    }

    fun setNextTextView(){
        Log.v(TAG, "===== setNextTextView =====")
        if(mRootXYArray.size>0){
            val nextXYMap: MutableMap<*, *> = getRandomItemInList(mRootXYArray) as MutableMap<*, *>
            val nextX: Int = nextXYMap[KEY_X] as Int
            val nextY: Int = nextXYMap[KEY_Y] as Int
            setTextView(nextX, nextY)
        }
    }

    fun getRandomItemInList(list: MutableList<*>) = list[Random.nextInt(0, list.size)]

    fun checkBoundary(textView: TextView, x: Int = -1, y: Int = -1): Boolean{
        Log.i(TAG, "textView.width: ${textView.width}")
        return true
    }

    fun checkOverLapping(textView: TextView): Boolean{
        Log.v(TAG, "===== checkOverLapping =====")

        for(item in mWordCloudTextViewList){
//            Log.v(TAG, "text: ${item.text}")
//            Log.d(TAG, "X: ${item.x}")
//            Log.d(TAG, "Y: ${item.y}")
//            Log.d(TAG, "left: ${item.left}")
//            Log.d(TAG, "top: ${item.top}")
//            Log.d(TAG, "right: ${item.right}")
//            Log.d(TAG, "bottom: ${item.bottom}")
//            Log.d(TAG, "width: ${item.width}")
//            Log.d(TAG, "height: ${item.height}")

            var isXOverLapping: Boolean = false
            var isYOverLapping: Boolean = false
            var overLapWidth: Int = 0
            var overLapHeight: Int = 0

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
                    Log.i(TAG, "比對的 left <= 目前 left (目前 TextView 在比對 TextView 的右手邊，或兩者一起置左)")
                    if(item.right <= textView.right){
                        Log.i(TAG, "比對的 right <= 目前 right")
                        item.right - textView.left
                    }else{
                        Log.i(TAG, "比對的 right > 目前 right")
                        textView.width
                    }
                }else{
                    Log.i(TAG, "比對的 left > 目前 left (目前 TextView 在比對 TextView 的左手邊)")
                    if(item.right <= textView.right){
                        Log.i(TAG, "比對的 right <= 目前 right")
                        item.width
                    }else{
                        Log.i(TAG, "比對的 right > 目前 right")
                        textView.right - item.left
                    }
                }

                overLapHeight = if(item.top <= textView.top){
                    Log.i(TAG, "比對的 top <= 目前 top (目前 TextView 在比對 TextView 的下方，或兩者等高)")
                    if(item.bottom <= textView.bottom){
                        Log.i(TAG, "比對的 bottom <= 目前 bottom")
                        item.bottom - textView.top
                    }else{
                        Log.i(TAG, "比對的 bottom > 目前 bottom")
                        textView.height
                    }
                }else{
                    Log.i(TAG, "比對的 top > 目前 top (目前 TextView 在比對 TextView 的上方)")
                    if(item.bottom <= textView.bottom){
                        Log.i(TAG, "比對的 bottom <= 目前 bottom")
                        item.height
                    }else{
                        Log.i(TAG, "比對的 bottom > 目前 bottom")
                        textView.bottom - item.top
                    }
                }

                Log.e(TAG, "overLapWidth: $overLapWidth")
                Log.e(TAG, "overLapHeight: $overLapHeight")

                val overLapArea: Int = overLapWidth * overLapHeight
                val itemArea: Int = item.width * item.height
                Log.e(TAG, "overLapArea: $overLapArea")
                Log.e(TAG, "itemArea: $itemArea")

                if(overLapArea >= itemArea / 15) return true

            }

        }
        return false
    }
}