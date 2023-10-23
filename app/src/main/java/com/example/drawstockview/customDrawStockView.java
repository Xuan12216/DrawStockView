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
import android.view.GestureDetector;
import android.view.View;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class customDrawStockView extends View {
    private ArrayList<Double> ClosePriceList = new ArrayList<>();//收盤價
    private ArrayList<String> hhmmList = new ArrayList<>();//時間
    private ArrayList<Double> LinePriceList = new ArrayList<>();//價位綫
    private float textSizeInSp = 11; // 以sp為單位的文本大小
    private Paint paint = new Paint();
    private Paint blackPaint = new Paint();
    private String colorType = "";
    private boolean isDraw = false;
    private boolean isShowHigh = false;
    private boolean isShowLow = false;

    public customDrawStockView(Context context, AttributeSet attrs) {
        super(context, attrs);//初始化
    }

    //====================================

    //MainActivity呼叫時進入
    public void drawStockView(String closePriceData, String hhmmData, String linePriceData) {
        ClosePriceList.clear();
        hhmmList.clear();
        LinePriceList.clear();

        // 使用换行符分割字符串并将数据解析为列表，並儲存進相應的列表
        parseDataString(closePriceData, "closeList");
        parseDataString(hhmmData, "hhmmList");
        parseDataString(linePriceData, "linePriceList");

        //修正數值的偏移,升降單位,比如：43.01 - > 43.00
        double yesterdayClosePrice = LinePriceList.get(5);
        double spacing = calculateSpacing(yesterdayClosePrice);

        List<Double> adjustedValues = new ArrayList<>();
        for (int i = 0; i < LinePriceList.size(); i++){
            double value = LinePriceList.get(i);
            if (i != 5){//5是昨收價，不需要調整
                double adjustedValue = roundToNearest(value, spacing);
                adjustedValues.add(adjustedValue);
            }
            else adjustedValues.add(value);//5是昨收價，不需要調整，直接存入list
        }

        LinePriceList.clear();
        LinePriceList.addAll(adjustedValues);//存入調整后的報價綫資料

        findMissingTimes(hhmmList,ClosePriceList);//填空缺失的時間段的資料，綫圖起伏不會那麽嚴重

        isDraw = true;//繪製
        isShowHigh = false;//初始化
        isShowLow = false;//初始化
        invalidate();//重新繪製，進入onDraw
    }

    //====================================

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isDraw) drawRecyclerViewDetail(canvas);//繪製綫圖
    }

    //====================================

    //填空缺失的時間段的資料，綫圖起伏不會那麽嚴重
    public static void findMissingTimes(List<String> hhmmList,List<Double> CLosePriceList) {
        // 创建一个范围，表示可能的时间段
        int startHour = Integer.parseInt(hhmmList.get(0).substring(0,2));//開始小時
        int endHour = Integer.parseInt(hhmmList.get(hhmmList.size()-1).substring(0,2));//結束小時
        int startMin = Integer.parseInt(hhmmList.get(0).substring(2));//開始分鐘
        int endMin = Integer.parseInt(hhmmList.get(hhmmList.size()-1).substring(2));//結束分鐘
        String lastNotMissTime = hhmmList.get(0);//用於填空缺失時間段的資料

        // 创建一个 Set 用于存储已经出现的时间段
        Set<Integer> timeSet = new HashSet<>();

        // 解析时间字符串并存储为整数
        for (String hhmm : hhmmList) {
            int time = Integer.parseInt(hhmm);
            timeSet.add(time);
        }

        //小時
        for (int i = startHour;i<=endHour;i++){

            //分鐘
            for (int j=startMin;j<endMin;j++){
                int time = i * 100 + j;//i = 09,j = 53, 09 * 100 + 53= 953

                //目前時間段找不到資料的話進入if
                if (!timeSet.contains(time)){
                    String missHhmm = String.format("%02d",i) + String.format("%02d", j);//ex:缺失0953
                    int indexLastNotMiss = hhmmList.indexOf(lastNotMissTime);//0952時間段有資料，ex：index = 51
                    String lastNotMissHhmm = hhmmList.get(indexLastNotMiss);//0952

                    int lastHH = Integer.parseInt(lastNotMissHhmm.substring(0,2));//09
                    int lastMM = Integer.parseInt(lastNotMissHhmm.substring(2));//52
                    int lastTime = lastHH * 100 + lastMM;//09 * 100 + 52 = 952

                    hhmmList.add(indexLastNotMiss + (time - lastTime), missHhmm);//time = 953, lastTime = 952, 在 51 + 953 - 952 = 52的位置增加缺失的時間段：0953
                    CLosePriceList.add(indexLastNotMiss + (time - lastTime), CLosePriceList.get(indexLastNotMiss));//在 52的位置增加缺失的時間段的資料：0952的收盤價
                }
                else lastNotMissTime = String.format("%02d",i) + String.format("%02d", j);//記錄最後一筆有資料的時間段
            }
        }
    }

    //====================================

    // 辅助方法：将数据字符串分割并解析为列表
    private void parseDataString(String data, String temp) {
        String[] lines = data.split(", ");

        for (String line : lines) {
            try {
                if ("closeList".equals(temp)) ClosePriceList.add(Double.parseDouble(line));
                else if ("hhmmList".equals(temp)) hhmmList.add(line);
                else if ("linePriceList".equals(temp)) LinePriceList.add(Double.parseDouble(line));
            }
            catch (NumberFormatException e) {}
        }
    }

    //====================================

    //台股升降單位計算
    public double roundToNearest(double value, double nearest) {
        // 计算最接近的倍数
        double multiple = Math.round(value / nearest);
        return multiple * nearest;// 使用倍数乘以最接近的值以获得修正后的值
    }

    //====================================

    //台股升降單位
    public double calculateSpacing(double value) {
        if (value >= 0.01 && value <= 10) return 0.01;
        else if (value > 10 && value <= 50) return 0.05;
        else if (value > 50 && value <= 100) return 0.1;
        else if (value > 100 && value <= 500) return 0.5;
        else if (value > 500 && value <= 1000) return 1;
        else return 5;
    }

    //====================================

    //繪製綫圖
    private void drawRecyclerViewDetail(Canvas canvas) {
        // 將sp單位的文本大小轉換為像素
        float textSizeInPx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, textSizeInSp, getResources().getDisplayMetrics());

        // 畫綫和顯示資料&時間
        blackPaint = new Paint();
        blackPaint.setColor(getResources().getColor(R.color.black,null));
        blackPaint.setTextSize(textSizeInPx);
        int width = getWidth(); //寬度
        int height = getHeight(); //高度
        float textOffset = 8;//偏移：報價和時間顯示位置的調整

        // 將sp單位的文本大小轉換為像素
        paint.setTextSize(textSizeInPx);
        paint.setColor(getResources().getColor(R.color.primary1,null));
        Typeface typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD);
        paint.setTypeface(typeface);

        //========================================
        //找最高和最低的收盤價

        double highestClosePrice = Double.MIN_VALUE;//最高
        double smallerClosePrice = Double.MAX_VALUE;//最低

        for (int i = 0; i < ClosePriceList.size(); i++){
            double closePrice = ClosePriceList.get(i);

            if (closePrice >= highestClosePrice) highestClosePrice = closePrice;
            if (closePrice <= smallerClosePrice) smallerClosePrice = closePrice;
        }

        //=========================================
        //開始檢查哪一條linePrice不需顯示，分別以true/false做判斷，true = 需要顯示；false = 不要顯示

        List<Boolean> lineStatusList = new ArrayList<>();
        int isTrueCount = 0;
        int indexFirstTrue = 0;
        int indexLastTrue = -1;

        // 遍历 LinePriceList，檢查當前的LinePrice有沒有在最高和最低的收盤價之間
        //LinePriceList = 309.5, 307.3, 305.1, 302.9, 300.7, 298.5, 296.3, 294.1, 291.9, 289.7, 287.5
        //highestClosePrice = 308.5
        //smallerClosePrice = 297.00
        for (int i = 0; i < LinePriceList.size(); i++) {
            double linePrice = LinePriceList.get(i);
            boolean isInRange = linePrice >= smallerClosePrice && linePrice <= highestClosePrice;//判斷true / false
            lineStatusList.add(isInRange);
        }
        //lineStatusList = false, true, true, true, true, true, false, false, false, false, false

        //============

        //尋找第一個true的index
        indexFirstTrue = lineStatusList.indexOf(true);

        //尋找最後一個true的index
        for (int i = lineStatusList.size() - 1; i >= 0; i--){
            if (lineStatusList.get(i)){
                indexLastTrue = i;
                break;
            }
        }

        //indexFirstTrue = 1, indexLastTrue = 5

        //============

        //判斷第一個index前的LinePrice要不要顯示
        //判斷條件是 最高的收盤價 > LinePrice.get(indexFirstTrue)的值 和 indexFirstTrue 是不是大於0 和 最高的收盤價 < LinePriceList.get(indexFirstTrue前一筆的值)
        if (highestClosePrice > LinePriceList.get(indexFirstTrue) && indexFirstTrue > 0 && highestClosePrice < LinePriceList.get(indexFirstTrue - 1)){
            indexFirstTrue -= 1;
            lineStatusList.set(indexFirstTrue,true);
        }

        //判斷最後一個index后的LinePrice要不要顯示
        //判斷條件是 最低的收盤價 < LinePrice.get(indexLastTrue)的值 和 indexFirstTrue + 1 會不會大於 LinePriceList.size() 和 最低的收盤價 > LinePriceList.get(indexLastTrue后一筆的值)
        if (smallerClosePrice < LinePriceList.get(indexLastTrue) && indexLastTrue + 1 < LinePriceList.size() && smallerClosePrice > LinePriceList.get(indexLastTrue + 1)){
            indexLastTrue += 1;
            lineStatusList.set(indexLastTrue,true);
        }

        //lineStatusList = true, true, true, true, true, true, true, false, false, false, false
        //indexFirstTrue = 0, indexLastTrue = 6

        //============

        double spacing = calculateSpacing(LinePriceList.get(5));//ex：298.5 = 0.5，LinePriceList.get(5) = 昨收價

        //調整LinePriceList.get(indexFirstTrue)的數值
        if (LinePriceList.get(indexFirstTrue) != LinePriceList.get(5)){
            LinePriceList.set(indexFirstTrue, highestClosePrice + spacing);//308.5 + 0.5 = 最上面的報價數值 = 309
        }

        //調整LinePriceList.get(indexLastTrue)的數值
        if (LinePriceList.get(indexLastTrue) != LinePriceList.get(5)){
            LinePriceList.set(indexLastTrue, smallerClosePrice - spacing);//297 - 0.5 = 最下面的報價數值 = 296.5
        }

        //============

        //尋找總共要幾個LinePrice需要顯示
        for (int i = 0; i < lineStatusList.size(); i++){
            if (lineStatusList.get(i)) isTrueCount++;
        }

        //lineStatusList = true, true, true, true, true, true, true, false, false, false, false
        //isTrueCount = 7

        //可以解開以下注釋查看數值
        //System.out.println("TestTest:"+LinePriceList);
        //System.out.println("TestTest:"+lineStatusList);
        //System.out.println("TestTest:"+indexFirstTrue);
        //System.out.println("TestTest:"+indexLastTrue);
        //System.out.println("TestTest:"+isTrueCount);

        //============

        //=========================================
        //畫格子

        float textWidth = blackPaint.measureText(String.format("%.2f", LinePriceList.get(0)));//計算收盤價字需要的位置
        float[] linePlaces = {0.13f, 0.21f, 0.29f, 0.37f, 0.45f, 0.53f, 0.61f, 0.69f, 0.77f, 0.85f, 0.93f, 0.97f}; // 橫綫顯示位置
        float[] linePlacesVertical = {0.00f,0.20f,0.40f,0.60f,0.80f};//縱綫顯示位置
        String[] timeLabels = {"0900", "1000", "1100", "1200", "1300"}; // 對應的時間標簽
        float yesterdayPriceY = 0;

        float linePlace = height * linePlaces[0];//第一條收盤綫的位置
        float linePlace1 = (height * linePlaces[10]) / isTrueCount;//之後每一筆收盤綫的間隔位置 **

        //============

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
                linePlace += linePlace1;//增加間隔距離
            }
        }

        //============

        linePlace -= linePlace1;//得到最後一筆的y軸的位置

        //縱綫
        for (int i = 0; i < 5; i++) {
            float lineX = width * linePlacesVertical[i] + textWidth+30;//綫的x軸
            float XCoordinateHhmm = calculateXCoordinateCloseFromHhmm(timeLabels[i], width - (textWidth + 35));//時間的x軸

            canvas.drawLine(lineX, height * linePlaces[0], lineX, linePlace, blackPaint);//繪製綫

            String hh = timeLabels[i].substring(0,2);//小時
            String mm = timeLabels[i].substring(2);//分鐘

            canvas.drawText(hh+":"+mm, XCoordinateHhmm + (textWidth / 2) + 30, linePlace + 50 , blackPaint);// 繪製時間
        }

        //=========================================
        //即時圖

        Paint closePricePaint = new Paint();
        closePricePaint.setColor(getResources().getColor(R.color.market_red,null));
        closePricePaint.setStrokeWidth(3);

        //計算y軸需要的數值
        float maxYDetail = height * linePlaces[0];//最上面的Y軸位置
        float minYDetail = linePlace;//最下面的Y軸位置
        float maxDataDetail = LinePriceList.get(indexFirstTrue).floatValue();//最上面的Y軸報價數值
        float minDataDetail = LinePriceList.get(indexLastTrue).floatValue();//indexLastTrue的Y軸報價數值
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

                    drawPathColor(lastX, lastY, XCoordinateClose, YCoordinateClose, textWidth, yesterdayPriceY, closePricePaint, canvas, "type1", "red_white");

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

                        drawPathColor(lastX, lastY, middleLineX, middleLineY, textWidth, yesterdayPriceY, closePricePaint, canvas,"type2", "red_white");

                        resetPaint(closePricePaint,"red");

                        // 繪制開盤價線
                        canvas.drawLine(lastX, lastY, middleLineX, middleLineY, closePricePaint);

                        //========================
                        //下筆資料

                        drawPathColor(XCoordinateClose, YCoordinateClose, middleLineX, middleLineY, textWidth, yesterdayPriceY, closePricePaint, canvas,"type3", "white_green");

                        resetPaint(closePricePaint,"green");

                        // 繪制開盤價線
                        canvas.drawLine(middleLineX, middleLineY, XCoordinateClose + textWidth + 30, YCoordinateClose, closePricePaint);
                    }
                    //上筆資料在中間線之下，下筆資料在中間線之上
                    else if(YCoordinateClose < yesterdayPriceY && lastY > yesterdayPriceY){

                        //========================
                        //上筆資料

                        drawPathColor(lastX, lastY, middleLineX, middleLineY, textWidth, yesterdayPriceY, closePricePaint, canvas,"type2", "white_green");

                        resetPaint(closePricePaint,"green");

                        // 繪制開盤價線
                        canvas.drawLine(lastX, lastY, middleLineX, middleLineY, closePricePaint);

                        //========================//上筆資料在中間線之下，下筆資料在中間線之上
                        //下筆資料

                        drawPathColor(middleLineX, middleLineY, XCoordinateClose, YCoordinateClose, textWidth, yesterdayPriceY, closePricePaint, canvas,"type1", "red_white");

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

                    drawPathColor(lastX, lastY, XCoordinateClose, YCoordinateClose, textWidth, yesterdayPriceY, closePricePaint, canvas,"type1", "white_green");

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

                        drawPathColor(lastX, lastY, middleLineX, middleLineY, textWidth, yesterdayPriceY, closePricePaint, canvas,"type2", "red_white");

                        resetPaint(closePricePaint,"red");

                        // 繪制開盤價線
                        canvas.drawLine(lastX, lastY, middleLineX, middleLineY, closePricePaint);

                        //========================下筆資料在中間線之下
                        //下筆資料

                        drawPathColor(middleLineX, middleLineY, XCoordinateClose, YCoordinateClose, textWidth, yesterdayPriceY, closePricePaint, canvas,"type1", "white_green");

                        resetPaint(closePricePaint,"green");

                        // 繪制開盤價線
                        canvas.drawLine(XCoordinateClose + textWidth + 30, YCoordinateClose, middleLineX, middleLineY, closePricePaint);
                    }
                    //上筆資料在中間線之下，下筆資料在中間線之上
                    else if(YCoordinateClose < yesterdayPriceY && lastY > yesterdayPriceY){

                        //========================
                        //上筆資料在中間線之下

                        drawPathColor(lastX, lastY, middleLineX, middleLineY, textWidth, yesterdayPriceY, closePricePaint, canvas,"type2", "white_green");

                        resetPaint(closePricePaint,"green");

                        // 繪制開盤價線
                        canvas.drawLine(middleLineX, middleLineY, lastX, lastY, closePricePaint);

                        //========================
                        //下筆資料在中間線之上

                        drawPathColor(middleLineX, middleLineY, XCoordinateClose, YCoordinateClose, textWidth, yesterdayPriceY, closePricePaint, canvas,"type1", "red_white");

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

                drawPathColor(lastX, lastY, XCoordinateClose, YCoordinateClose, textWidth, yesterdayPriceY, closePricePaint, canvas,"type1", colorType);

                // 繪制開盤價線
                if (lastY == yesterdayPriceY && YCoordinateClose == yesterdayPriceY) lastColor = "grey";
                else if (lastY < yesterdayPriceY && YCoordinateClose == yesterdayPriceY) lastColor = "red";
                else if(lastY > yesterdayPriceY && YCoordinateClose == yesterdayPriceY) lastColor = "green";

                resetPaint(closePricePaint,lastColor);

                canvas.drawLine(lastX, lastY, XCoordinateClose+textWidth+30, YCoordinateClose, closePricePaint);
            }

            //==================
            //顯示最高和最低價的數值

            float xPlace = XCoordinateClose + (textWidth / 2) + 30;//x軸位置
            if (xPlace < textWidth + 30) xPlace = textWidth + 30;//如果超出左邊範圍
            else if (xPlace + textWidth > width) xPlace = width - textWidth;//如果超出右邊範圍
            blackPaint.setStyle(Paint.Style.STROKE);//繪製邊框
            blackPaint.setStrokeWidth(2);//邊框的寬度，粗/細

            if (currentClosePrice == highestClosePrice && !isShowHigh){
                paint.setColor(getResources().getColor(R.color.market_red,null));
                blackPaint.setColor(getResources().getColor(R.color.market_red,null));
                canvas.drawRect(xPlace,YCoordinateClose - 20 - textSizeInPx,xPlace + textWidth,YCoordinateClose - 20,blackPaint);//邊框
                canvas.drawText(String.format("%.2f",currentClosePrice),xPlace,YCoordinateClose - 25,paint);//最高報價
                isShowHigh = true;
            }
            else if (currentClosePrice == smallerClosePrice && !isShowLow){
                blackPaint.setColor(getResources().getColor(R.color.market_green,null));
                paint.setColor(getResources().getColor(R.color.market_green,null));
                canvas.drawRect(xPlace,YCoordinateClose ,xPlace + textWidth,YCoordinateClose + textSizeInPx,blackPaint);//邊框
                canvas.drawText(String.format("%.2f",currentClosePrice),xPlace,YCoordinateClose + 25,paint);//最低報價
                isShowLow = true;
            }

            //==================

            lastY = YCoordinateClose;
            lastX = XCoordinateClose +textWidth + 30;
        }
    }

    //====================================

    private void resetPaint(Paint closePricePaint, String color) {
        // 恢覆畫筆樣式，將Shader設為null
        closePricePaint.setShader(null);
        closePricePaint.setStyle(Paint.Style.STROKE);

        if ("red".equals(color)) closePricePaint.setColor(getResources().getColor(R.color.market_red,null));
        else if ("green".equals(color))closePricePaint.setColor(getResources().getColor(R.color.market_green,null));
        else if ("grey".equals(color))closePricePaint.setColor(getResources().getColor(R.color.grey1,null));
        closePricePaint.setAlpha(255);
    }

    //====================================

    private void drawPathColor(float point1_X, float point1_Y, float point2_X, float point2_Y, float textWidth, float middleLineY, Paint closePricePaint, Canvas canvas, String drawType, String colorType) {
        Path path = new Path();// 創建一個Path對象
        int[] colors = new int[]{2}; // 上半部分紅色，下半部分白色

        if ("type1".equals(drawType)){
            path.moveTo(point1_X, point1_Y);// 起始點
            path.lineTo(point2_X + textWidth + 30, point2_Y);//連接第二個點
            path.lineTo(point2_X + textWidth + 30, middleLineY);//連接第三個點
            path.lineTo(point1_X, middleLineY); // 返回到起始點，形成一個封閉區域

            if ("red_white".equals(colorType)) colors = new int[]{getResources().getColor(R.color.market_red, null), Color.WHITE}; // 上半部分紅色，下半部分白色
            else if("white_green".equals(colorType)) colors = new int[]{Color.WHITE,getResources().getColor(R.color.market_green,null)};// 上半部分白色，下半部分綠色
            else if("grey".equals(colorType)) colors = new int[]{getResources().getColor(R.color.grey1,null),getResources().getColor(R.color.grey1,null)};// 灰色
        }
        else if("type2".equals(drawType)){
            path.moveTo(point1_X, point1_Y);
            path.lineTo(point2_X, point2_Y);
            path.lineTo(point1_X, point2_Y);
            path.lineTo(point1_X, point1_Y); // 返回到起始點，形成一個封閉區域

            if ("red_white".equals(colorType)) colors = new int[]{getResources().getColor(R.color.market_red,null), Color.WHITE}; // 上半部分紅色，下半部分白色
            else if("white_green".equals(colorType)) colors = new int[]{Color.WHITE,getResources().getColor(R.color.market_green,null)};// 上半部分白色，下半部分綠色
        }
        else if("type3".equals(drawType)){
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

    //====================================

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
