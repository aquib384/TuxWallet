package io.tux.wallet.testnet.graphUtils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.github.mikephil.charting.components.IMarker;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

import io.tux.wallet.testnet.R;

public class MyMarkerView extends MarkerView implements IMarker {

    private TextView tvContent;
   Context mContext ;
    public MyMarkerView(Context context, int layoutResource) {
        super(context, layoutResource);
        //this markerview only displays a textview
        tvContent = (TextView) findViewById(R.id.tv_price);
        mContext = context;
    }

    //callbacks everytime the MarkerView is redrawn, can be used to update the
    //content (user-interface)
    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        tvContent.setText("" + e.getY()); //set the entry-value as the display text
    }

    @Override
    public MPPointF getOffsetForDrawingAtPoint(float posX, float posY) {
//        return super.getOffsetForDrawingAtPoint(posX, posY);
        return MPPointF.getInstance(-getWidth() / 2f, -getHeight()-10f);
    }
//    @Override
//    public void draw(Canvas canvas, float posX, float posY) {
//        super.draw(canvas, posX, posY);
//        getOffsetForDrawingAtPoint(posX, posY);
//    }

    @Override
    public void draw(Canvas canvas, float posx, float posy) {

        WindowManager windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;

        int w = getWidth();
        if ((width - posx - w) < w) {
            posx -= w;
        }
        canvas.translate(posx, posy);
        draw(canvas);
        canvas.translate(-posx, -posy);
    }

}