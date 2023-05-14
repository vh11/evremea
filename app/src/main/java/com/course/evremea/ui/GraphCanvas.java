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

import java.util.ArrayList;
import java.util.List;

public class GraphCanvas extends View {


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

        Rect bounds = canvas.getClipBounds();
        canvas.drawRect(bounds, paint);

        List<Point> values = new ArrayList<>();
        values.add(new Point(0, 0));
        values.add(new Point(1, 2));
        values.add(new Point(3, 10));
        values.add(new Point(7, 8));
        values.add(new Point(19, 7));
        values.add(new Point(20, 16));
        values.add(new Point(24, 0));

        int xMin = Integer.MAX_VALUE;
        int xMax = Integer.MIN_VALUE;
        int yMin = Integer.MAX_VALUE;
        int yMax = Integer.MIN_VALUE;
        for(Point p : values) {
            xMin = Math.min(xMin, p.x);
            xMax = Math.max(xMax, p.x);
            yMin = Math.min(yMin, p.y);
            yMax = Math.max(yMax, p.y);
        }

        int dx = xMax - xMin;
        int dy = yMax - yMin;

        int margin = 40;
        int d = bounds.width() - 2 * margin;

        // draw axis
        paint.setColor(Color.BLACK);
        canvas.drawLine(margin, bounds.height() - margin, margin + d, bounds.height() - margin, paint);
        canvas.drawLine(margin, bounds.height() - margin, margin, bounds.height() - margin - d, paint);
        paint.setTextSize(margin);
        String origin = String.format("(%d, %d)", xMin, yMin);
        canvas.drawText(origin, margin, bounds.height() - margin + paint.getTextSize(), paint);
        String xRight = String.format("(%d, %d)", xMax, yMin);
        canvas.drawText(xRight, d + margin - paint.measureText(xRight), bounds.height() - margin + paint.getTextSize(), paint);
        String yTop = String.format("(%d, %d)", xMin, yMax);
        canvas.drawText(yTop, margin, bounds.height() - d - margin + paint.getTextSize(), paint);
        paint.setColor(Color.BLUE);
        for (int i = 0; i < values.size() - 1; i++) {
            Point p0 = pixelPoint(values.get(i), margin, d, xMin, yMin, dx, dy);
            Point p1 = pixelPoint(values.get(i + 1), margin, d, xMin, yMin, dx, dy);

            canvas.drawLine(p0.x, bounds.height() - p0.y, p1.x, bounds.height() - p1.y, paint);
        }


    }

    public static Point pixelPoint(Point p, int margin, int d, int xMin, int yMin, int dx, int dy) {
        Point pixelPoint = new Point();
        pixelPoint.x = margin + (p.x - xMin) * d / dx;
        pixelPoint.y = margin + (p.y - yMin) * d / dy;

        return pixelPoint;
    }
}
