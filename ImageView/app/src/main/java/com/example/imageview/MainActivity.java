package com.example.imageview;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import java.lang.Math;

import java.util.Random;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    int[] CustomHSVtoRGB(float[] hsv){

        int rgb[]=new int[3];
        int ti = (int)(hsv[0]/60)%6;
        int f=(int)(hsv[0]/60)-ti;
        int l=(int)(hsv[2]*(1-hsv[1]));
        int m = (int)(hsv[2]*(1-f*hsv[1]));
        int n = (int)(hsv[2]*(1-f)*hsv[1]);
        switch(ti){
            case 0:
                rgb[0]=(int)hsv[2];
                rgb[1]=n;
                rgb[2]=l;
                break;
            case 1:
                rgb[0]=m;
                rgb[1]=(int)hsv[2];
                rgb[2]=l;
                break;
            case 2:
                rgb[0]=l;
                rgb[1]=(int)hsv[2];
                rgb[2]=n;
                break;
            case 3:
                rgb[0]=l;
                rgb[1]=m;
                rgb[2]=(int)hsv[2];
                break;
            case 4:
                rgb[0]=n;
                rgb[1]=l;
                rgb[2]=(int)hsv[2];
                break;
            case 5:
                rgb[0]=(int)hsv[2];
                rgb[1]=l;
                rgb[2]=m;
                break;
            default:
                rgb[0]=0;
                rgb[1]=0;
                rgb[2]=0;
                break;
        }
        return rgb;
    }

    float[] CustomRGBtoHSV(int r, int g, int b, float[] hsv){
        float rgb[]={r,g,b};
        float min=rgb[0];
        float max=rgb[1];
        for(int i=0;i<rgb.length;i++){
            if(rgb[i]<min){
                min=rgb[i];
            }
            if(rgb[i]>max){
                max=rgb[i];
            }
        }
        if(max==min) {
            hsv[0] = 0;
        }else{
            if(max == rgb[0]){
                hsv[0]= (float)(((Math.PI/3)*((g-b)/(max-min))+(2*Math.PI)) % 2*Math.PI);
            }
            else{
                if (max == rgb[1]){
                    hsv[0]= (float) ((Math.PI/3)*((b-r)/(max-min))+(2*Math.PI/3));
                }
                else{
                    if (max == rgb[2]){
                        hsv[0]= (float) ((Math.PI/3)*((r-g)/(max-min))+(4*Math.PI/3));
                    }
                }
            }
        }
        if(max==0){
            hsv[1]=0;
        }else{
            hsv[1]=1-(min/max);
        }
        hsv[2]=max;
        return hsv;
    }

    void toGray(Bitmap bmp){

        int width = bmp.getWidth();
        int height = bmp.getHeight();
        for(int i=0;i<width;i++){
            for(int y = 0;y<height;y++){
                int pixel = bmp.getPixel(i,y);

                int values[] = {Color.red(pixel),Color.blue(pixel),Color.green(pixel)};
                int grey = (int)(0.3*values[0]+0.59*values[1]+0.11*values[2]);
                bmp.setPixel(i, y, Color.rgb(grey,grey,grey));
            }
        }
    }


    public void colorize(Bitmap bmp){
        Random random = new Random();
        float h = 1 + (360 - 1) * random.nextFloat();
        int width = bmp.getWidth();
        int height = bmp.getHeight();

        for(int i=0;i<width;i++){
            for(int y = 0;y<height;y++){
                int color = bmp.getPixel(i, y);
                /*int r = Color.red(color);
                int g = Color.green(color);
                int b = Color.blue(color);*/
                float[] hsv = new float[3];
                Color.colorToHSV(color,hsv);
                //hsv = CustomRGBtoHSV( r, g, b, hsv);
                hsv[0]=h;
                color = Color.HSVToColor(hsv);
                //int rgb[] =CustomHSVtoRGB(hsv);
                //color = Color.rgb(rgb[0],rgb[1],rgb[2]);
                bmp.setPixel(i, y, color);
            }
        }
    }

    void toGrayExceptOneColor(Bitmap bmp,float c){
        float min = c-30;
        float max = c+30;

        int width = bmp.getWidth();
        int height = bmp.getHeight();

        for(int i=0;i<width;i++){
            for(int y = 0;y<height;y++){
                int pixel = bmp.getPixel(i,y);

                int values[] = {Color.red(pixel),Color.blue(pixel),Color.green(pixel)};
                float[] hsv = new float[3];
                CustomRGBtoHSV(values[0],values[1],values[2],hsv);
                if(hsv[0]<min || hsv[0]>max){
                    int grey = (int)(0.3*values[0]+0.59*values[1]+0.11*values[2]);
                    bmp.setPixel(i, y, Color.rgb(grey,grey,grey));

                }

            }
        }
    }

    int[] returnHistotgram(Bitmap bmp){
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        int hist[] = new int[255];
        for(int i=0;i<width;i++){
            for(int y = 0;y<height;y++){
                int pixel = bmp.getPixel(i,y);
                hist[Color.red(pixel)]++;
            }
        }

        return hist;
    }

    int[] returnHistotgramColor(Bitmap bmp){
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        int hist[] = new int[255];
        for(int i=0;i<width;i++){
            for(int y = 0;y<height;y++){
                int pixel = bmp.getPixel(i,y);
                float[] hsv = new float[3];
                Color.colorToHSV(pixel,hsv);
                hist[(int)hsv[2]]++;
            }
        }
        return hist;
    }



    void ELDD(Bitmap bmp){

        int min=30,max=225;
        int width = bmp.getWidth();
        int height = bmp.getHeight();

        for(int i=0;i<width;i++){
            for(int y = 0;y<height;y++){
                int pixel = bmp.getPixel(i,y);
                int I =Color.red(pixel);
                int newi;
                if(I<min){
                    newi=0;

                }else{
                    if(I>max){
                        newi=255;
                    }else{
                        newi=(255/(max-min))*(I-min);
                    }
                }
                bmp.setPixel(i, y, Color.rgb(newi,newi,newi));

            }
        }
    }

    void DimCon(Bitmap bmp){

        int min=0,max=255;
        int width = bmp.getWidth();
        int height = bmp.getHeight();

        for(int i=0;i<width;i++){
            for(int y = 0;y<height;y++){
                int pixel = bmp.getPixel(i,y);
                int I =Color.red(pixel);

                int newi=(255/(max-min))*(I-min);

                bmp.setPixel(i, y, Color.rgb(newi,newi,newi));

            }
        }
    }

    void ELDDColor(Bitmap bmp){
        int value =100;
        double contrast = Math.pow((100 + value) / 100, 2);

        int width = bmp.getWidth();
        int height = bmp.getHeight();

        for(int i=0;i<width;i++){
            for(int y = 0;y<height;y++){
                int pixel = bmp.getPixel(i,y);
                int R =Color.red(pixel);
                int G =Color.green(pixel);
                int B =Color.blue(pixel);
                R = (int)(((((R / 255.0) - 0.5) * contrast) + 0.5) * 255.0);
                if(R < 0) { R = 0; }
                else if(R > 255) { R = 255; }

                G = (int)(((((G / 255.0) - 0.5) * contrast) + 0.5) * 255.0);
                if(G < 0) { G = 0; }
                else if(G > 255) { G = 255; }

                B = (int)(((((B / 255.0) - 0.5) * contrast) + 0.5) * 255.0);
                if(B < 0) { B = 0; }
                else if(B > 255) { B = 255; }

                bmp.setPixel(i, y, Color.rgb(R,G,B));

            }
        }

    }

    void EgalH(Bitmap bmp){
        int[] hist = returnHistotgram(bmp);
        int width = bmp.getWidth();
        int height = bmp.getHeight();

        int t=0;
        for(int i=0;i< 255;i++){
            t=t+hist[i];
            hist[i]=(t*255)/(width*height);
        }
        for(int i=0;i<width;i++) {
            for (int y = 0; y < height; y++) {
                int pixel = bmp.getPixel(i,y);
                int R =Color.red(pixel);
                bmp.setPixel(i, y, Color.rgb(hist[R],hist[R],hist[R]));
            }
        }


    }

    void EgalHColor(Bitmap bmp){
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        int[] hist =returnHistotgramColor(bmp);
        int[] newHist =  new int[hist.length];
        int[] C = new int[255];
        C[0]=0;
        newHist[0]=0;
        float[] hsv=new float[3];
        for(int i=1;i< 255;i++){
            C[i]=hist[i-1]+hist[i];
            newHist[i]=(int)((C[i]*255)/(width*height));
        }

        for(int i=0;i<width;i++) {
            for (int y = 0; y < height; y++) {
                int pixel = bmp.getPixel(i,y);
                Color.colorToHSV(pixel,hsv);
                hsv[2]=newHist[(int)hsv[2]];
                pixel=Color.HSVToColor(hsv);
                int[] rgb={Color.red(pixel),Color.green(pixel),Color.blue(pixel)};
                bmp.setPixel(i, y, Color.rgb(rgb[0],rgb[1],rgb[2]));
            }
        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        System.nanoTime();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inMutable = true;

        ImageView image = findViewById(R.id.imageView2);
        Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.image3);
        b = b.copy(Bitmap.Config.ARGB_8888,true);
        image.setImageBitmap(b);

        for(int i=0;i<b.getWidth();i++){
            b.setPixel(i, b.getHeight()/2, Color.rgb(1, 1, 1));
        }
        System.nanoTime();

    }

    public void onClickBtn(View v)
    {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inMutable = true;

        ImageView image = (ImageView) findViewById(R.id.imageView2);
        Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.image3);
        b = b.copy(Bitmap.Config.ARGB_8888,true);
        image.setImageBitmap(b);
        toGray(b);
    }

    public void onClickBtnColor(View v)
    {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inMutable = true;

        ImageView image = (ImageView) findViewById(R.id.imageView2);
        Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.image3);
        b = b.copy(Bitmap.Config.ARGB_8888,true);
        image.setImageBitmap(b);
        colorize(b);
    }

    public void onClickBtntoGrayExcept(View v)
    {
        Random random = new Random();
        int h = random.nextInt(360) ;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inMutable = true;

        ImageView image = (ImageView) findViewById(R.id.imageView2);
        Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.image3);
        b = b.copy(Bitmap.Config.ARGB_8888,true);
        image.setImageBitmap(b);
        toGrayExceptOneColor(b,h);
    }

    public void onClickBtntELDD(View v)
    {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inMutable = true;
        ImageView image = (ImageView) findViewById(R.id.imageView);
        Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.image2);
        b = b.copy(Bitmap.Config.ARGB_8888,true);
        image.setImageBitmap(b);
        ELDD(b);
    }

    public void onClickBtntELDDColor(View v)
    {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inMutable = true;
        ImageView image = (ImageView) findViewById(R.id.imageView2);
        Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.image3);
        b = b.copy(Bitmap.Config.ARGB_8888,true);
        image.setImageBitmap(b);
        ELDDColor(b);
    }

    public void onClickBtntDimCon(View v)
    {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inMutable = true;
        ImageView image = (ImageView) findViewById(R.id.imageView);
        Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.image2);
        b = b.copy(Bitmap.Config.ARGB_8888,true);
        image.setImageBitmap(b);
        DimCon(b);
    }

    public void onClickButtonEgalH(View v)
    {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inMutable = true;
        ImageView image = (ImageView) findViewById(R.id.imageView);
        Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.image2);
        b = b.copy(Bitmap.Config.ARGB_8888,true);
        image.setImageBitmap(b);
        EgalH(b);
    }

    public void onClickButtonEgalHColor(View v)
    {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inMutable = true;
        ImageView image = (ImageView) findViewById(R.id.imageView3);
        Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.image4);
        b = b.copy(Bitmap.Config.ARGB_8888,true);
        image.setImageBitmap(b);
        EgalHColor(b);
    }

    public void onClickButtonTest(View v)
    {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inMutable = true;
        ImageView image = (ImageView) findViewById(R.id.imageView);
        Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.image2);
        b = b.copy(Bitmap.Config.ARGB_8888,true);
        image.setImageBitmap(b);
        int[] hist =returnHistotgram(b);
    }




}