//package com.tuxwallet.graphUtils
//
//import android.content.Context
//import com.github.mikephil.charting.components.IMarker
//import com.github.mikephil.charting.components.MarkerView
//import com.github.mikephil.charting.utils.MPPointF
//
//import android.R
//import android.util.Log
//import android.view.View
//
//import android.widget.TextView
//import com.github.mikephil.charting.highlight.Highlight
//
//
//class XYMarkerView(context: Context?, layoutResource: Int) : MarkerView(context, layoutResource), IMarker {
//
//    private var tvContent: TextView? = null
//
////    fun XYMarkerView(context: Context?, layoutResource: Int) {
////        super(context, layoutResource)
////
////        // find your layout components
////        Log.d("XYMarkerView", "Constructor called")
////        tvContent = findViewById<View>(R.id.tvPrice) as TextView
////    }
//
//    // callbacks everytime the MarkerView is redrawn, can be used to update the
//    // content (user-interface)
//    fun refreshContent(e: Map.Entry<*, *>, highlight: Highlight?) {
//        Log.d("XYMarkerView", "refreshContent: $e")
//        tvContent!!.text = "" + e.getY()
//
//        // this will perform necessary layouting
//        //super.refreshContent(e, highlight);
//    }
//
//    fun getXOffset(xpos: Float): Int {
//        return 0
//    }
//
//    fun getYOffset(ypos: Float): Int {
//        return 0
//    }
//
//    private var mOffset: MPPointF? = null
//
//    override fun getOffset(): MPPointF? {
//        if (mOffset == null) {
//            // center the marker horizontally and vertically
//            mOffset = MPPointF((-(width / 2)).toFloat(), (-height).toFloat())
//        }
//        return mOffset
//    }
//
//    fun getOffsetForDrawingAtPos(posX: Float, posY: Float): MPPointF? {
//        return null
//    }
//
//}