package com.example.drawstockview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import java.util.ArrayList;
import java.util.List;

public class customDrawStockView extends View {
    private ArrayList<Double> ClosePriceList = new ArrayList<>();
    private ArrayList<String> hhmmList = new ArrayList<>();
    private ArrayList<Double> LinePriceList = new ArrayList<>();
    private float textSizeInSp = 10; // 以sp為單位的文本大小
    private Paint paint = new Paint();
    private Paint blackPaint = new Paint();
    String colorType = "";
    boolean isDraw = false;

    public customDrawStockView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void drawStockView(String closePriceData, String hhmmData, String linePriceData){

        ClosePriceList.clear();
        hhmmList.clear();
        LinePriceList.clear();

        // 使用换行符分割字符串
        String[] closePriceLines = closePriceData.split(", ");
        String[] hhmmLines = hhmmData.split(", ");
        String[] linePrice = linePriceData.split(", ");

        // 遍历每行数据，并将其转换为 Double 类型后添加到 ArrayList 中
        for (String line : closePriceLines) {
            try {
                double closePrice = Double.parseDouble(line);
                ClosePriceList.add(closePrice);
            }
            catch (NumberFormatException e) {}
        }

        for (String line : hhmmLines) {
            try {hhmmList.add(line);}
            catch (NumberFormatException e) {}
        }

        for (String line : linePrice) {
            try {LinePriceList.add(Double.valueOf(line));}
            catch (NumberFormatException e) {}
        }

        isDraw = true;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isDraw) drawRecyclerViewDetail(canvas);
    }

    private void drawRecyclerViewDetail(Canvas canvas) {
        // 將sp單位的文本大小轉換為像素
        float textSizeInPx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, textSizeInSp, getResources().getDisplayMetrics());

        // 畫綫和顯示資料&時間
        blackPaint = new Paint();
        blackPaint.setColor(getResources().getColor(R.color.black,null));
        blackPaint.setTextSize(textSizeInPx);
        int width = getWidth(); //寬度
        int height = getHeight(); //高度
        float textOffset = 8;

        // 將sp單位的文本大小轉換為像素
        paint.setTextSize(textSizeInPx);
        paint.setColor(getResources().getColor(R.color.primary1,null));
        Typeface typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD);
        paint.setTypeface(typeface);

        //========================================
        //找最高和最低的收盤價

        double highestClosePrice = Double.MIN_VALUE;
        double smallerClosePrice = Double.MAX_VALUE;

        for (int i = 0; i < ClosePriceList.size(); i++){
            double closePrice = ClosePriceList.get(i);

            if (closePrice >= highestClosePrice) highestClosePrice = closePrice;
            if (closePrice <= smallerClosePrice) smallerClosePrice = closePrice;
        }

        //=========================================
        //開始檢查那一條linePrice不需顯示

        List<Boolean> lineStatusList = new ArrayList<>();
        int isTrueCount = 0;
        int indexFirstTrue = 0;
        int indexLastTrue = 0;

        /*
        // 遍历 LinePriceList
        for (int i = 0; i < LinePriceList.size(); i++) {
            double linePrice = LinePriceList.get(i);
            // 在此添加逻辑来判断是否需要显示该线，如果需要显示，将true添加到 lineStatusList，否则添加false
            if (linePrice >= smallerClosePrice && linePrice <= highestClosePrice) lineStatusList.add(true);
            else lineStatusList.add(false);

            if (i == 5) lineStatusList.set(5,true);
        }

        //第一筆true左邊的false改成true，最後一筆true右邊的false改成true
        for (int i = 0; i < lineStatusList.size(); i++){

            boolean isTrue = lineStatusList.get(i);

            if (isTrue)isTrueCount++;

            if (isTrue && i > 0 && i < 5 && !lineStatusList.get(i - 1)){
                lineStatusList.set(i - 1, true);
                isTrueCount++;
                indexFirstTrue = i - 1;
            }
            else if (!isTrue && i > 5 && lineStatusList.get(i - 1)){
                lineStatusList.set(i,true);
                isTrueCount++;
                indexLastTrue = i;
                break;
            }
        }
         */


        for (int i = 0;i < LinePriceList.size(); i++){
            if (i >= 0 && i < LinePriceList.size() - 1){
                double linePrice1 = LinePriceList.get(i);
                double linePrice2 = LinePriceList.get(i + 1);

                boolean isInRange = (linePrice1 >= highestClosePrice  && highestClosePrice <= linePrice2) ||
                        (linePrice1 >= smallerClosePrice && smallerClosePrice <= linePrice2);

                System.out.println("TestTest:"+linePrice1+": "+linePrice2+": "+highestClosePrice+": "+smallerClosePrice+": "+isInRange);
            }
        }
        //System.out.println("TestTest:"+lineStatusList);

        //=========================================
        //畫格子

        //起始x軸
        float textWidth = blackPaint.measureText(String.format("%.2f", LinePriceList.get(0)));//計算收盤價字需要的位置
        float[] linePlaces = {0.13f, 0.21f, 0.29f, 0.37f, 0.45f, 0.53f, 0.61f, 0.69f, 0.77f, 0.85f, 0.93f, 0.97f}; // 橫綫顯示位置
        float[] linePlacesVertical = {0.00f,0.20f,0.40f,0.60f,0.80f};//縱綫顯示位置
        String[] timeLabels = {"09", "10", "11", "12", "13"}; // 對應的時間標簽
        float yesterdayPriceY = 0;

        float linePlace = height * linePlaces[0];//第一條收盤綫的位置
        float linePlace1 = (height * linePlaces[10]) / isTrueCount;//之後每一筆收盤綫的間隔位置

        //橫綫
        for (int i = 0; i < LinePriceList.size(); i++) {

            //如果lineStatusList.get(i) == true時
            if (lineStatusList.get(i)){
                if (i <= 4) paint.setColor(getResources().getColor(R.color.market_red,null));//收盤價字體顔色顯示，紅色
                else paint.setColor(getResources().getColor(R.color.market_green,null));//收盤價字體顔色顯示，綠色

                // 繪制字體背景，只有昨收需要繪製
                if (i == 5) {
                    yesterdayPriceY = linePlace;
                    float textHeight = linePlace + textOffset;
                    float textWidth1 = 10;
                    float rectLeft = textWidth1 - 5; // 調整背景左邊界
                    float rectTop = textHeight - textSizeInPx; // 調整背景上邊界
                    float rectRight = textWidth1 + paint.measureText(String.format("%.2f", LinePriceList.get(i))) + 5; // 調整背景右邊界
                    float rectBottom = textHeight + textOffset; // 調整背景下邊界
                    paint.setColor(getResources().getColor(R.color.primary1,null));//褐色
                    canvas.drawRect(rectLeft, rectTop, rectRight, rectBottom, paint);
                    paint.setColor(getResources().getColor(R.color.white,null));//回復原來的顔色
                }

                // 繪制左邊的數據
                double temp1 = LinePriceList.get(i);
                canvas.drawText(String.format("%.2f", temp1), 10, linePlace + textOffset, paint);//繪製收盤價

                canvas.drawLine(textWidth + 30, linePlace, width, linePlace, blackPaint);// 繪制右邊的線
                linePlace += linePlace1;
            }
        }

        linePlace -= linePlace1;//得到最後一筆的y軸的位置

        //縱綫
        for (int i = 0; i < 5; i++) {
            float lineX = width * linePlacesVertical[i]+textWidth+30;//綫的x軸
            float XCoordinateClose = calculateXCoordinateCloseFromHhmm(timeLabels[i]+"00", width - (textWidth + 35));//時間的x軸

            canvas.drawLine(lineX, height * linePlaces[0], lineX, linePlace, blackPaint);//繪製綫
            canvas.drawText(timeLabels[i], XCoordinateClose+textWidth+30, height * linePlaces[11] , blackPaint);// 繪製時間
        }

        //=========================================
        //即時圖

        Paint closePricePaint = new Paint();
        closePricePaint.setColor(getResources().getColor(R.color.market_red,null));
        closePricePaint.setStrokeWidth(3);

        //計算y軸需要的數值
        float maxYDetail = height * linePlaces[0];
        float minYDetail = linePlace;
        float maxDataDetail = LinePriceList.get(indexFirstTrue).floatValue();
        float minDataDetail = LinePriceList.get(indexLastTrue).floatValue();
        double firstClosePrice = ClosePriceList.get(0);//讀取第一筆的收盤價

        //計算起始的y軸和x軸
        float lastY = (float) (minYDetail + (maxYDetail - minYDetail) / (maxDataDetail - minDataDetail) * (firstClosePrice - minDataDetail));
        float lastX = (calculateXCoordinateCloseFromHhmm(hhmmList.get(0), width - (textWidth + 35)))+textWidth + 30;

        //======================================

        String lastColor = "green";

        //開始繪製即時圖
        for (int i = 0; i < hhmmList.size(); i++) {

            String hhmm = hhmmList.get(i);//hhmm
            double currentClosePrice = ClosePriceList.get(i);//closePrice

            //計算x軸
            float XCoordinateClose = calculateXCoordinateCloseFromHhmm(hhmm, width - (textWidth + 35));
            //計算y軸
            float YCoordinateClose = (float) (minYDetail + (maxYDetail - minYDetail) / (maxDataDetail - minDataDetail) * (currentClosePrice - minDataDetail));
            if (currentClosePrice == LinePriceList.get(5)) YCoordinateClose = yesterdayPriceY;

            //如果當前的收盤價大於昨收
            if (currentClosePrice > LinePriceList.get(5)) {

                lastColor = "red";
                //如果上一筆y軸在昨收綫之上 和 當前y軸在昨收綫之上
                if (lastY <= yesterdayPriceY && YCoordinateClose <= yesterdayPriceY){

                    drawPathColor(lastX, lastY, XCoordinateClose, YCoordinateClose, textWidth, yesterdayPriceY, closePricePaint, canvas, "normal", "red_white");

                    resetPaint(closePricePaint,"red");
                    // 繪制開盤價線
                    canvas.drawLine(lastX, lastY, XCoordinateClose + textWidth + 30, YCoordinateClose, closePricePaint);
                }
                else {
                    float middleLineY = yesterdayPriceY;//計算昨收綫的y軸
                    float slope = (YCoordinateClose - lastY) / (XCoordinateClose + textWidth + 30 - lastX);//計算偏移角度
                    float middleLineX = (middleLineY - lastY) / slope + lastX;//計算昨收綫的x軸

                    //上筆資料在中間線之上，下筆資料在中間線之下
                    if (lastY < yesterdayPriceY && YCoordinateClose > yesterdayPriceY){

                        //========================
                        //上筆資料

                        drawPathColor(lastX, lastY, middleLineX, middleLineY, textWidth, yesterdayPriceY, closePricePaint, canvas,"up", "red_white");

                        resetPaint(closePricePaint,"red");

                        // 繪制開盤價線
                        canvas.drawLine(lastX, lastY, middleLineX, middleLineY, closePricePaint);

                        //========================
                        //下筆資料

                        drawPathColor(XCoordinateClose, YCoordinateClose, middleLineX, middleLineY, textWidth, yesterdayPriceY, closePricePaint, canvas,"down", "white_green");

                        resetPaint(closePricePaint,"green");

                        // 繪制開盤價線
                        canvas.drawLine(middleLineX, middleLineY, XCoordinateClose + textWidth + 30, YCoordinateClose, closePricePaint);
                    }
                    //上筆資料在中間線之下，下筆資料在中間線之上
                    else if(YCoordinateClose < yesterdayPriceY && lastY > yesterdayPriceY){

                        //========================
                        //上筆資料

                        drawPathColor(lastX, lastY, middleLineX, middleLineY, textWidth, yesterdayPriceY, closePricePaint, canvas,"up", "white_green");

                        resetPaint(closePricePaint,"green");

                        // 繪制開盤價線
                        canvas.drawLine(lastX, lastY, middleLineX, middleLineY, closePricePaint);

                        //========================//上筆資料在中間線之下，下筆資料在中間線之上
                        //下筆資料

                        drawPathColor(middleLineX, middleLineY, XCoordinateClose, YCoordinateClose, textWidth, yesterdayPriceY, closePricePaint, canvas,"normal", "red_white");

                        resetPaint(closePricePaint,"red");

                        // 繪制開盤價線
                        canvas.drawLine(middleLineX, middleLineY, XCoordinateClose + textWidth + 30, YCoordinateClose, closePricePaint);
                    }
                }
            }
            //如果當前的收盤價小於昨收
            else if (currentClosePrice < LinePriceList.get(5)) {

                lastColor = "green";
                //如果上筆y軸大於昨收綫 和 當前y軸大於昨收綫
                if (lastY >= yesterdayPriceY && YCoordinateClose >= yesterdayPriceY){

                    drawPathColor(lastX, lastY, XCoordinateClose, YCoordinateClose, textWidth, yesterdayPriceY, closePricePaint, canvas,"normal", "white_green");

                    resetPaint(closePricePaint,"green");

                    // 繪制開盤價線
                    canvas.drawLine(lastX, lastY, XCoordinateClose+textWidth + 30, YCoordinateClose, closePricePaint);
                }
                else {
                    float middleLineY = yesterdayPriceY;
                    float slope = (YCoordinateClose - lastY) / (XCoordinateClose + textWidth + 30 - lastX);
                    float middleLineX = (middleLineY - lastY) / slope + lastX;

                    //上筆資料在中間線之上，下筆資料在中間線之下
                    if (lastY < yesterdayPriceY && YCoordinateClose > yesterdayPriceY){

                        //========================
                        //上筆資料在中間線之上

                        drawPathColor(lastX, lastY, middleLineX, middleLineY, textWidth, yesterdayPriceY, closePricePaint, canvas,"up", "red_white");

                        resetPaint(closePricePaint,"red");

                        // 繪制開盤價線
                        canvas.drawLine(lastX, lastY, middleLineX, middleLineY, closePricePaint);

                        //========================下筆資料在中間線之下
                        //下筆資料

                        drawPathColor(middleLineX, middleLineY, XCoordinateClose, YCoordinateClose, textWidth, yesterdayPriceY, closePricePaint, canvas,"normal", "white_green");

                        resetPaint(closePricePaint,"green");

                        // 繪制開盤價線
                        canvas.drawLine(XCoordinateClose + textWidth + 30, YCoordinateClose, middleLineX, middleLineY, closePricePaint);
                    }
                    //上筆資料在中間線之下，下筆資料在中間線之上
                    else if(YCoordinateClose < yesterdayPriceY && lastY > yesterdayPriceY){

                        //========================
                        //上筆資料在中間線之下

                        drawPathColor(lastX, lastY, middleLineX, middleLineY, textWidth, yesterdayPriceY, closePricePaint, canvas,"up", "white_green");

                        resetPaint(closePricePaint,"green");

                        // 繪制開盤價線
                        canvas.drawLine(middleLineX, middleLineY, lastX, lastY, closePricePaint);

                        //========================
                        //下筆資料在中間線之上

                        drawPathColor(middleLineX, middleLineY, XCoordinateClose, YCoordinateClose, textWidth, yesterdayPriceY, closePricePaint, canvas,"normal", "red_white");

                        resetPaint(closePricePaint,"red");

                        // 繪制開盤價線
                        canvas.drawLine(middleLineX, middleLineY, XCoordinateClose + textWidth + 30, YCoordinateClose, closePricePaint);
                    }
                }
            }
            //如果現價等於昨收綫
            else if (currentClosePrice == LinePriceList.get(5)){

                if ("red".equals(lastColor)) colorType = "red_white";
                else if("green".equals(lastColor))colorType = "white_green";
                else if("grey".equals(lastColor)) colorType = "grey";

                drawPathColor(lastX, lastY, XCoordinateClose, YCoordinateClose, textWidth, yesterdayPriceY, closePricePaint, canvas,"normal", colorType);

                // 繪制開盤價線
                if (lastY == yesterdayPriceY && YCoordinateClose == yesterdayPriceY) lastColor = "grey";
                else if (lastY < yesterdayPriceY && YCoordinateClose == yesterdayPriceY) lastColor = "red";
                else if(lastY > yesterdayPriceY && YCoordinateClose == yesterdayPriceY) lastColor = "green";

                resetPaint(closePricePaint,lastColor);

                canvas.drawLine(lastX, lastY, XCoordinateClose+textWidth+30, YCoordinateClose, closePricePaint);
            }

            lastY = YCoordinateClose;
            lastX = XCoordinateClose +textWidth + 30;
        }
    }

    private void resetPaint(Paint closePricePaint, String color) {
        // 恢覆畫筆樣式，將Shader設為null
        closePricePaint.setShader(null);
        closePricePaint.setStyle(Paint.Style.STROKE);

        if ("red".equals(color)) closePricePaint.setColor(getResources().getColor(R.color.market_red,null));
        else if ("green".equals(color))closePricePaint.setColor(getResources().getColor(R.color.market_green,null));
        else if ("grey".equals(color))closePricePaint.setColor(getResources().getColor(R.color.grey1,null));
        closePricePaint.setAlpha(255);
    }

    private void drawPathColor(float point1_X, float point1_Y, float point2_X, float point2_Y, float textWidth, float middleLineY, Paint closePricePaint, Canvas canvas, String drawType, String colorType) {
        Path path = new Path();// 創建一個Path對象
        int[] colors = new int[]{2}; // 上半部分紅色，下半部分白色

        if ("normal".equals(drawType)){
            path.moveTo(point1_X, point1_Y);// 起始點
            path.lineTo(point2_X + textWidth + 30, point2_Y);//連接第二個點
            path.lineTo(point2_X + textWidth + 30, middleLineY);//連接第三個點
            path.lineTo(point1_X, middleLineY); // 返回到起始點，形成一個封閉區域

            if ("red_white".equals(colorType)) colors = new int[]{getResources().getColor(R.color.market_red, null), Color.WHITE}; // 上半部分紅色，下半部分白色
            else if("white_green".equals(colorType)) colors = new int[]{Color.WHITE,getResources().getColor(R.color.market_green,null)};// 上半部分白色，下半部分綠色
            else if("grey".equals(colorType)) colors = new int[]{getResources().getColor(R.color.grey1,null),getResources().getColor(R.color.grey1,null)};// 灰色
        }
        else if("up".equals(drawType)){
            path.moveTo(point1_X, point1_Y);
            path.lineTo(point2_X, point2_Y);
            path.lineTo(point1_X, point2_Y);
            path.lineTo(point1_X, point1_Y); // 返回到起始點，形成一個封閉區域

            if ("red_white".equals(colorType)) colors = new int[]{getResources().getColor(R.color.market_red,null), Color.WHITE}; // 上半部分紅色，下半部分白色
            else if("white_green".equals(colorType)) colors = new int[]{Color.WHITE,getResources().getColor(R.color.market_green,null)};// 上半部分白色，下半部分綠色
        }
        else if("down".equals(drawType)){
            path.moveTo(point1_X + textWidth + 30, point1_Y);
            path.lineTo(point2_X, point2_Y);
            path.lineTo(point2_X, point1_Y);
            path.lineTo(point1_X + textWidth + 30, point1_Y); // 返回到起始點，形成一個封閉區域

            if ("white_green".equals(colorType)) colors = new int[]{Color.WHITE,getResources().getColor(R.color.market_green,null)}; // 上半部分白色，下半部分綠色
        }

        float[] positions = {0f, 1f}; // 漸變顏色的分佈位置，0錶示起始，1錶示結束
        LinearGradient gradient = new LinearGradient(0, 0, 0, getHeight(), colors, positions, Shader.TileMode.CLAMP);

        closePricePaint.setShader(gradient);// 設定Shader為漸變色

        closePricePaint.setStyle(Paint.Style.FILL);// 設定填充樣式

        closePricePaint.setAlpha(100);

        canvas.drawPath(path, closePricePaint);// 繪制填充區域
    }

    private float calculateXCoordinateCloseFromHhmm(String hhmm, float width) {
        String startTime = "09:00";
        String endTime = "13:30";

        // 開始時間和結束時間轉換成分鐘
        int startMinutes = Integer.parseInt(startTime.substring(0, 2)) * 60 + Integer.parseInt(startTime.substring(3));
        int endMinutes = Integer.parseInt(endTime.substring(0, 2)) * 60 + Integer.parseInt(endTime.substring(3));

        // 計算總分鐘
        int totalMinutes = endMinutes - startMinutes;

        // 計算每分鐘對應的 x 坐標增量
        float xIncrement = width / totalMinutes;

        // 將給定的 hhmm 時間點轉換爲分鐘
        int inputMinutes = Integer.parseInt(hhmm.substring(0, 2)) * 60 + Integer.parseInt(hhmm.substring(2));

        // 計算 x 坐標
        float XCoordinateClose = (inputMinutes - startMinutes) * xIncrement;

        return XCoordinateClose;
    }
}
