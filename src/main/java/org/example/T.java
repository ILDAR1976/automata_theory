package org.example;
// Automata T

import java.util.Vector;

public class T {
    private Integer[][] t;
    private Integer[][] f;
    private int p;
    Pair<Integer,Integer>[] M;
    private Vector<Pair<Integer,Integer>> v;

    public T(Integer m, Integer n, Integer[][] f){
        this.t = new Integer[m+1][n+1];
        for (Integer i = 0; i<m+1; ++i)
            for (Integer j = 0; j<n+1; ++j)
                this.t[i][j] = 0;
        this.p = 1;
        this.f = f;
    }
    public Integer query(Integer a){
        this.v = v(a);
        int i = 0;
        int j = 1;
        while (i<v.size()){
            if (t[j][v.get(i).fst] != 0) {
                j = t[j][v.get(i).fst];
            } else {
                return 0;
            }
            ++i;
        }
        //return t[j][a];
        return j;
    }
    public void enter(Integer a) {
        Vector<Pair<Integer,Integer>> v = this.v(a);

        if (this.M == null) {
            this.M = new Pair[v.size()];
            M[0] = new Pair<>(v.get(0).fst, v.get(0).snd);
            M[1] = new Pair<>(v.get(1).fst, v.get(1).snd);
            M[2] = new Pair<>(v.get(2).fst, v.get(2).snd);
        } else {
            M[0].fst = v.get(0).fst;
            M[1].fst = v.get(1).fst;
            M[2].fst = v.get(2).fst;
        }

        int i = 0;
        int j = 1;

        while (i<M.length){
            if (t[j][M[i].fst] == 0 && j == 1) {
                this.p = j + this.p;
                t[j][M[i].fst] = this.p;
                j = this.p;
            } else if (t[j][M[i].fst] == 0 ) {
                ++this.p;
                t[j][M[i].fst] = this.p;
                j = this.p;
            } else {
                j = t[j][M[i].fst];
            }
            M[i].snd = M[i].fst;
            ++i;
        }
        //t[j][a] = a;
        t[--j][M[--i].fst] = a;
        p = j;
    }
    private Vector<Pair<Integer,Integer>> v(Integer a){
        Vector<Pair<Integer,Integer>> v = new Vector<>();
        for (int x = 0; x<f.length; ++x){
            v.add(new Pair<Integer,Integer>(f[x][a-1],0));
        }
        return v;
    }
}
