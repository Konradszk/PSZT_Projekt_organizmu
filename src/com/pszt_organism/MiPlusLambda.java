package com.pszt_organism;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by konrad on 14.05.2017.
 */
public class MiPlusLambda {


    public static int[]mutacja(int[] Wektor, int min, int max)
    {
        //tak wlasciwie min to zawsze 0 a max o bedzie 2*potegaDwojki*m-1

        //tablica prawdopodobienstwa takiej dlugosci jak wektor, bo prawdopodobienstwo zanegowania bitu ma by 1/dl_wektora
        int[] prawdopodobienstwo = new int[Wektor.length];
        for (int i = 0; i < prawdopodobienstwo.length; i++)
        {
            prawdopodobienstwo[i] = 0;
        }
        //gdzies bedzie 1 i jezeli ja sie wylosuje to znaczy ze gen ma byc negowany
        int randomZmiana = ThreadLocalRandom.current().nextInt(0, max);
        prawdopodobienstwo[randomZmiana]=1;

        int[]WektorZmutowany = Wektor;
        for(int w=0; w<WektorZmutowany.length; w++)
        {
            int randomNum = ThreadLocalRandom.current().nextInt(min, max);
            //System.out.print("\n numer random: "+randomNum);
            //jezeli losowy index z prawdopodobienstwa jest 1 to gen o indeksie w mutuje
            if(prawdopodobienstwo[randomNum ]==1)
            {
                //jezli byl 1 to mutuje na 0
                if(WektorZmutowany[w]==1)
                    WektorZmutowany[w]=0;
                    //jezeli byl 0 to mutuje na 1
                else
                    WektorZmutowany[w]=1;
            }
        }


        return WektorZmutowany;
    }

    public static ArrayList<int[]> krzyzowanie_jednopunktowe (int[] Wektor1, int[] Wektor2){
        //krzyżowanie z 1 locusem, wybieranym losowo
        ArrayList<int[]> Wektory = new ArrayList<>();
        int locus = ThreadLocalRandom.current().nextInt(0, Wektor1.length-1);

        for (int i = locus + 1; i <= Wektor1.length; i++)
        if(Wektor1[i]!=Wektor2[i])
        {
            int[] Temp = Wektor1;
            Wektor1[i] = Wektor2[i];
            Wektor2[i] = Temp[i];
        }
        Wektory.add(Wektor1);
        Wektory.add(Wektor2);
        return Wektory;
    }

    public static ArrayList<int[]> krzyzowanie_dwupunktowe (int[] Wektor1, int[] Wektor2){
        //krzyzowanie z 2 locusami, wybieranymi losowo
        ArrayList<int[]> Wektory = new ArrayList<>();
        //dwa rozne locusy
        int locus1 = ThreadLocalRandom.current().nextInt(0, Wektor1.length-1);
        int locus2=Wektor1.length;
            while(locus2==locus1) {
            locus2 = ThreadLocalRandom.current().nextInt(0, Wektor1.length - 1);
        }
        if(locus1>locus2){
            int temp=locus1;
            locus1=locus2;
            locus2=temp;
        }

        for (int i = locus1+1; i <= locus2; i++)
            if(Wektor1[i]!=Wektor2[i])
            {
                int[] Temp = Wektor1;
                Wektor1[i] = Wektor2[i];
                Wektor2[i] = Temp[i];
            }
        Wektory.add(Wektor1);
        Wektory.add(Wektor2);
        return Wektory;
    }

}
