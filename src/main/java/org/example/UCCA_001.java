package org.example;

import java.util.*;
import java.util.stream.Collectors;

public class UCCA_001 {

    private static IState getStateForA = new IState() {
        @Override
        public Integer getState(Automata A, Integer a, Integer x) {
            return A.getFunction()[x][a - 1];
        }
    };
    private static Automata aut;
    private static List<R> r;
    private static Integer p = 0;
    private static Pair<Integer, Integer>[][] t;
    private static Pair<Integer, Integer>[] v;
    private static Set<Integer> pending = new HashSet<>();
    private static Pair<Integer,Integer> targetPair = new Pair(0,0);
    private static Set<Pair<Integer,Integer>> combine = new HashSet<>();

    public static void main(String[] args) {
        aut = new Automata();
        r = new ArrayList<>();
        automatonInitialization(aut,r);
        Integer n = aut.Q.size();
        Integer m = aut.X.size() * aut.Q.size();

        pending.addAll(aut.Q);

        printSet(aut.getEquivalenceClasses());

        while (pending.size() != 0) {
            combine.clear();
            t = new Pair[m][n];
            p = 0;
            for (int i = 0;i<m;++i) {
                for (int j = 0;j<n;++j) {
                    t[i][j] = new Pair<>(0, 0);
                }
            }
            do {
                for (var a : pending) {
                    Integer res = getT(aut, c(a));
                    if (res == 0) {
                        correct(aut, t, a);
                    } else {
                        targetPair.fst = res;
                        targetPair.snd = a;
                        combine.add(targetPair);
                    }
                }
            } while (pending.size() == 1 && combine.size() == 0);
            pending.clear();

            for (var e: combine) {
                Integer a = e.fst;
                Integer ao = e.snd;
                if (find(aut,a) != find(aut,ao)){
                    if (list(aut,find(aut,a)).size() <= list(aut,find(aut,ao)).size()) {
                        for (var ne: list(aut,find(aut,a))) {
                            r.get(c(ne)).setOldI(ne);
                            r.get(c(ne)).setNewI(find(aut,a));
                            pending.add(ne);

                        }
                        union(aut, find(aut,ao), find(aut,a));
                    } else {
                        for (var ne: list(aut,find(aut,ao))) {
                            r.get(c(ne)).setOldI(ne);
                            r.get(c(ne)).setNewI(find(aut,ao));
                            pending.add(ne);
                        }
                        union(aut, find(aut,a), find(aut,ao));
                    }
                }
            }
        }

        printSet(aut.getEquivalenceClasses());
    }
    private static void automatonInitialization(Automata aut, List<R> r){
        R re = new R();
        re.setNewI(1);
        re.setOldI(0);
        r.add(re);

        re = new R();
        re.setNewI(2);
        re.setOldI(0);
        r.add(re);

        re = new R();
        re.setNewI(3);
        re.setOldI(0);
        r.add(re);

        re = new R();
        re.setNewI(4);
        re.setOldI(0);
        r.add(re);

        re = new R();
        re.setNewI(5);
        re.setOldI(0);
        r.add(re);

        aut.X = new HashSet<>();
        aut.X.add(0);
        aut.X.add(1);
        aut.X.add(2);

        aut.Q = new HashSet<>();
        aut.Q.add(1);
        aut.Q.add(2);
        aut.Q.add(3);
        aut.Q.add(4);
        aut.Q.add(5);

        aut.I = new HashSet<>();
        aut.I.add(1);

        aut.F = new HashSet<>();
        aut.F.add(5);

        aut.setFunction(new Integer[][]{
                {2,3,4,4,5},
                {4,3,4,4,5},
                {5,5,5,5,5}
        });

        var ec = aut.getEquivalenceClasses();

        for (var e:r) {
            var c = new HashMap<Integer,HashSet<Integer>>();
            var hs = new HashSet<Integer>();
            hs.add(e.getNewI());
            c.put(e.getNewI(),hs);
            ec.add(c);
        }

    }
    private static Integer getT(Automata aut, Integer a){
        var v = getV(aut,a);

        Integer j = t[0][c(v[0].fst)].fst;
        if (j == 0) return 0;

        Integer res = 0;

        for (int i = 1; i < aut.X.size();++i){
            j = t[j][c(v[i].fst)].fst;
        }
        res = t[j][c(v[c(aut.X.size())].fst)].fst;
        return (res == 0) ? 0: res;
    }
    private static Pair<Integer,Integer>[] getV(Automata aut, Integer a){
        v = new Pair[aut.X.size()];
        Integer[][] f = aut.getFunction();
        for (int i = 0; i < aut.X.size();++i){
            v[i] = new Pair<>(f[i][c(r.get(a).getNewI())],f[i][c(r.get(a).getOldI())]);
        }
        return v;
    }
    private static void correct(Automata aut, Pair<Integer, Integer>[][] t, Integer a){
        Integer n = aut.Q.size();
        Integer m = aut.X.size() * aut.Q.size();
        v = getV(aut,c(a));
        Integer i = 0, j = 0;

        while (i < aut.X.size()){
            if (t[j][c(v[i].fst)].fst == 0 &&
                t[j][c(v[i].fst)].snd == 0) {
                t[j][c(v[i].fst)].fst = ++p;
            } else if (t[j][c(v[i].fst)].fst == 0 &&
                    t[j][c(v[i].snd)].fst != 0) {
                t[j][c(v[i].fst)].fst = t[j][c(v[i].snd)].fst;
                t[j][c(v[i].snd)].fst = 0;
            } else if (t[j][c(v[i].fst)].fst != 0){
                j = t[j][c(v[i].fst)].fst;
                ++i;
            }
        }
        t[j][c(v[--i].fst)].fst = find(aut,r.get(c(a)).getNewI());
    }
    private static Integer find(Automata aut, Integer a){
        for (var e: aut.getEquivalenceClasses()) {
            for (var elm : e.entrySet()) {
                if (elm.getValue().contains(a)) {
                    return elm.getKey();
                }
            }
        }
        return 0;
    }
    private static Set<Integer> list(Automata aut, Integer e) {
        var classes = aut.getEquivalenceClasses();
        var foundList = classes.stream().filter(x -> x.get(e) != null)
                .collect(Collectors.toList())
                .get(0)
                .get(e);

        var lst = new HashSet<Integer>();
        var ja = aut.getFunction();
        for (int i = 0; i < aut.X.size(); ++i){
            for (int j = 0; j < aut.Q.size(); ++j) {
                for (var ce : foundList) {
                    if (ja[i][j] == e) {
                        lst.add(j+1);
                    }
                }
            }
        }
        return lst;
    }
    private static void union(Automata aut, Integer e, Integer eo){
            var ec = aut.getEquivalenceClasses();
            var classFirst = ec.stream().filter(x -> x.get(e) != null)
                    .collect(Collectors.toList())
                    .get(0);
            var classSecond = ec.stream().filter(x -> x.get(eo) != null)
                .collect(Collectors.toList())
                .get(0);
            var valueClassFirst =  classFirst.get(e);
            var valueClassSecond = classSecond.get(eo);
            valueClassFirst.addAll(valueClassSecond);
            classFirst.put(e,valueClassFirst);
            ec.remove(classSecond);
    }
    private static void printSet(Set<Integer> inp){
        String str = "";
        str += "{";
        for (var e: inp) {
            str += e + ",";
        }
        str += "=}";
        System.out.println(str.replace(",=",""));
    }
    private static void printSet(List<HashMap<Integer, HashSet<Integer>>> inp){
        String str = "";
        str += "[ ";
        for (var e: inp) {
            for (var l: e.entrySet()) {
                str += l.getKey() + ": ";
                str += "{";
                for (var el: l.getValue()) {
                    str += el + ",";
                }
                str += "=}; ";
                str = str.replace(",=","");
            }
        }
        str += "=";
        str = str.replace("; =","");
        str += "]\n";

        System.out.print(str);
    }
    private static Integer c(Integer inp) {
        return (inp == 0) ? 0: inp - 1;
    }
}
