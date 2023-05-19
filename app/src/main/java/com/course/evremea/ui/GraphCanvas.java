package com.course.evremea.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.course.evremea.store.Reading;

import java.util.ArrayList;
import java.util.List;

public class GraphCanvas extends View {


    private List<Reading> readings;

    public GraphCanvas(Context context) {
        super(context);
    }

    public GraphCanvas(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public GraphCanvas(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public GraphCanvas(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);

        canvas.drawRect(canvas.getClipBounds(), paint);

        if (readings != null && readings.size() > 1) {
            plotValues(canvas);
        }
    }

    protected void plotValues(Canvas canvas) {
        Rect bounds = canvas.getClipBounds();

        int xMin = Integer.MAX_VALUE;
        int xMax = Integer.MIN_VALUE;
        int tMin = Integer.MAX_VALUE;
        int tMax = Integer.MIN_VALUE;
        int hMin = Integer.MAX_VALUE;
        int hMax = Integer.MIN_VALUE;
        for(Reading r : readings) {
            xMin = Math.min(xMin, r.timestamp);
            xMax = Math.max(xMax, r.timestamp);
            tMin = Math.min(tMin, r.temperature);
            tMax = Math.max(tMax, r.temperature);
            hMin = Math.min(hMin, r.humidity);
            hMax = Math.max(hMax, r.humidity);
        }

        tMin = 0;
        tMax = 40;
        hMin = 0;
        hMax = 100;
        int dx = xMax - xMin;
        int dt = tMax - tMin;
        int dh = hMax - hMin;

        int margin = 40;
        int d = bounds.width() - 2 * margin;

        // draw axis
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        canvas.drawLine(margin, bounds.height() - margin, margin + d, bounds.height() - margin, paint);
        canvas.drawLine(margin, bounds.height() - margin, margin, bounds.height() - margin - d, paint);
        paint.setTextSize(margin);
        String origin = String.format("(%d, %d, %d)", xMin, tMin, hMin);
        canvas.drawText(origin, margin, bounds.height() - margin + paint.getTextSize(), paint);
        String xRight = String.format("(%d, %d, %d)", xMax, tMin, hMin);
        canvas.drawText(xRight, d + margin - paint.measureText(xRight), bounds.height() - margin + paint.getTextSize(), paint);
        String yTop = String.format("(%d, %d, %d)", xMin, tMax, hMax);
        canvas.drawText(yTop, margin, bounds.height() - d - margin + paint.getTextSize(), paint);
        for (int i = 0; i < readings.size() - 1; i++) {
            Point p0 = pixelPoint(readings.get(i), margin, d, xMin, tMin, dx, dt, true);
            Point p1 = pixelPoint(readings.get(i + 1), margin, d, xMin, tMin, dx, dt, true);
            paint.setColor(Color.RED);
            canvas.drawLine(p0.x, bounds.height() - p0.y, p1.x, bounds.height() - p1.y, paint);

            p0 = pixelPoint(readings.get(i), margin, d, xMin, hMin, dx, dh, false);
            p1 = pixelPoint(readings.get(i + 1), margin, d, xMin, hMin, dx, dh, false);
            paint.setColor(Color.BLUE);
            canvas.drawLine(p0.x, bounds.height() - p0.y, p1.x, bounds.height() - p1.y, paint);
        }
    }

    public static Point pixelPoint(Reading p, int margin, int d, int xMin, int yMin, int dx, int dy, boolean t) {
        Point pixelPoint = new Point();
        pixelPoint.x = margin + (p.timestamp - xMin) * d / dx;
        pixelPoint.y = margin + ((t ? p.temperature : p.humidity) - yMin) * d / dy;

        return pixelPoint;
    }

    public void setReadings(List<Reading> readings) {
        this.readings = readings;

        invalidate();
    }
}
