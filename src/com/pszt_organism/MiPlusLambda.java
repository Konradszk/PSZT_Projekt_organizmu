package com.pszt_organism;

import java.util.ArrayList;

import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by konrad on 14.05.2017.
 */
public class MiPlusLambda {



    private ArrayList <int[]> listaPopulacjaMi = new ArrayList<>();
    private int max ;
    private int lambda;
    private int potegaDwojki;
    private int m;
    private int mi;
    private double theChosenOne; // najlepszy
    private int iterationStop;
    private String wyborKrzyzowania;
    public MiPlusLambda(){}
    public MiPlusLambda(int n_tmp, int m_tmp, int mi_tmp, int lambda_tmp,String krzyzowanie){
        wyborKrzyzowania=krzyzowanie;
        potegaDwojki=n_tmp;
        m=m_tmp;
        mi=mi_tmp;
        lambda=lambda_tmp;
        max = 2*potegaDwojki*m-1;
        theChosenOne=0;
    }

    public ArrayList<int[]> getListaPopulacjaMi() {
        return listaPopulacjaMi;
    }

    public void setListaPopulacjaMi(ArrayList<int[]> listaPopulacjaMi) {
        this.listaPopulacjaMi = listaPopulacjaMi;
    }

    public int[]mutacja(int[] Wektor, int min, int max)
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
        //int randomZmiana2 = ThreadLocalRandom.current().nextInt(0, max);

        prawdopodobienstwo[randomZmiana]=1;
        //prawdopodobienstwo[randomZmiana2]=1;

        int[]WektorZmutowany = (int[])Wektor.clone();

        for(int w=0; w<WektorZmutowany.length; w++)
        {
            int randomNum = ThreadLocalRandom.current().nextInt(min, max);
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

    public ArrayList<int[]> krzyzowanie_jednopunktowe (int[] Wektor1, int[] Wektor2){
        //krzyżowanie z 1 locusem, wybieranym losowo
        ArrayList<int[]> Wektory = new ArrayList<>();
        int locus = ThreadLocalRandom.current().nextInt(0, Wektor1.length-1);
        int[] Temp = (int[])Wektor1.clone();
        for (int i = locus + 1; i <= Wektor1.length-1; i++)
            if(Wektor1[i]!=Wektor2[i])
            {
                Wektor1[i] = Wektor2[i];
                Wektor2[i] = Temp[i];
            }
        Wektory.add(Wektor1);
        Wektory.add(Wektor2);
        return Wektory;
    }

    public  ArrayList<int[]> krzyzowanie_dwupunktowe (int[] Wektor1, int[] Wektor2){
        //krzyzowanie z 2 locusami, wybieranymi losowo
        ArrayList<int[]> Wektory = new ArrayList<>();
        //dwa rozne locusy
        int locus1 = ThreadLocalRandom.current().nextInt(0, Wektor1.length-1);
        int locus2=Wektor1.length;
            while(locus2==locus1) {
            locus2 = ThreadLocalRandom.current().nextInt(0, Wektor1.length - 1);
        }
        int temp=locus1;
        if(locus1>locus2){
            locus1=locus2;
            locus2=temp;
        }
        int[] Temp = (int[])Wektor1.clone();
        for (int i = locus1+1; i <locus2; i++)
            if(Wektor1[i]!=Wektor2[i])
            {
                Wektor1[i] = Wektor2[i];
                Wektor2[i] = Temp[i];
            }
        Wektory.add(Wektor1);
        Wektory.add(Wektor2);
        return Wektory;
    }

    public ArrayList<int[]> losujLambda()
    {
        ArrayList<int[]> listaLambda = new ArrayList<>();
        //z listy populacji mi losowanych lambda osobnikow do rozmnozenia
        for(int i=0; i<lambda; i++)
        {
            int rnd = ThreadLocalRandom.current().nextInt(0, listaPopulacjaMi.size());
            listaLambda.add(listaPopulacjaMi.get(rnd));
        }
        return listaLambda;
    }

    public  void dodajPotomstwoR(ArrayList <int[]> listaLambda)
    {
        int i=0;
        while(i!=listaLambda.size())
        {
            //dla kolejno wylosowanych par lambda tworzonych jest 2 dzieci , ktore sa mutowane i dodawane do listy potomstwa
            if(wyborKrzyzowania.equals("Jednopunktowe")) {
                listaPopulacjaMi.add(mutacja(krzyzowanie_jednopunktowe(mutacja(listaLambda.get(i),0,max), mutacja(listaLambda.get(i + 1),0,max)).get(0),0,max));
                listaPopulacjaMi.add(mutacja(krzyzowanie_jednopunktowe(mutacja(listaLambda.get(i),0,max), mutacja(listaLambda.get(i + 1),0,max)).get(1),0,max));
            }
            else{
                listaPopulacjaMi.add(mutacja(krzyzowanie_dwupunktowe(listaLambda.get(i), listaLambda.get(i + 1)).get(0), 0, max));
                listaPopulacjaMi.add(mutacja(krzyzowanie_dwupunktowe(listaLambda.get(i), listaLambda.get(i + 1)).get(1), 0, max));
            }
            i+=2;
        }

    }


    public ArrayList<int[]> miZrodzicowIpotomstwa(Map<int[],Double> mapVectFunc_tmp)
    {
        ArrayList<int[]> listaMi = new ArrayList<>();
        ArrayList<int[]> klucz_t = new ArrayList<>(mapVectFunc_tmp.keySet());

        for(int i=0; i<mi; i++)
        {
            listaMi.add(klucz_t.get(i));
        }

        return listaMi;
    }
    public int stop(double firstValueFromMap)
    {
        if(theChosenOne<firstValueFromMap)
        {
            theChosenOne=firstValueFromMap;
            iterationStop=0;
        }
        else {
            iterationStop++;
        }

        return iterationStop;
    }
}
