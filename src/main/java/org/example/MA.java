package org.example;
// Minimization of a finite automaton
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class MA {
    private static IState getStateForA = new IState() {
        @Override
        public Integer getState(Automata A, Integer a, Integer x) {
            return A.getFunction()[x][a - 1];
        }
    };
    public static void main( String[] args ) {
        Set<Object> R0 = new HashSet<>();
        Automata A = new Automata();
        automatonInitialization(A,R0);

        Set<Object> R = let(R0);
        Set<Object> Fractal = product(R0,A.X);

        while (!(Fractal.size() == 0)) {
            Pair<Set<Integer>,Integer> currentPair = take(Fractal);
            for (Object item : R) {
                HashSet<Integer> L = (HashSet<Integer>) item;
                Pair<Set<Integer>,Set<Integer>> separateL = separate(A,currentPair,L);
                if (separateL.fst.size() != 0 && separateL.snd.size() !=0 ){
                    replace(R0,L,separateL);
                    for (Object item2 : A.X) {
                        Integer x = (Integer) item2;
                        Pair<Set<Integer>,Integer> testPair = new Pair(L,x);
                        if (Fractal.contains(testPair)) {
                            Fractal.remove(testPair);
                            Pair<Set<Integer>,Integer> newPair1 = new Pair(separateL.fst,x);
                            Pair<Set<Integer>,Integer> newPair2 = new Pair(separateL.snd,x);
                            Fractal.add(newPair1);
                            Fractal.add(newPair2);
                        } else {
                            if (separateL.fst.size() > separateL.snd.size()) {
                                Pair<Set<Integer>,Integer> newPair = new Pair(separateL.snd,x);
                                Fractal.add(newPair);
                            } else {
                                Pair<Set<Integer>,Integer> newPair = new Pair(separateL.fst,x);
                                Fractal.add(newPair);
                            }
                        }
                    }
                }
            }
            R = let(R0);
        }
        printAutomata(A, "Automata A");
        System.out.println();
        System.out.println("Result:");
        System.out.println(R.toString());

    }
    public static void automatonInitialization(Automata A, Set<Object> R0) {

        Set<Integer> L1 = new HashSet<>();
        Set<Integer> L2 = new HashSet<>();

        L1.add(7);
        L2.add(1);
        L2.add(2);
        L2.add(3);
        L2.add(4);
        L2.add(5);
        L2.add(6);

        R0.add(L1);
        R0.add(L2);

        A.X = new HashSet<>();
        A.X.add(0);
        A.X.add(1);

        A.Q = new HashSet<>();
        A.Q.add(1);
        A.Q.add(2);
        A.Q.add(3);
        A.Q.add(4);
        A.Q.add(5);
        A.Q.add(6);
        A.Q.add(7);

        A.I = new HashSet<>();
        A.I.add(1);

        A.F = new HashSet<>();
        A.F.add(7);

        A.setFunction(new Integer[][]{
            {2,4,5,7,7,7,7},
            {3,5,6,5,4,5,7}
        });
    }
    public static Set<Object> let(Set<Object> input) {
        Iterator<Object> itr = input.iterator();
        Set<Object> out = new HashSet<>();
        while (itr.hasNext()) {
            out.add(itr.next());
        }
        return out;
    }
    public static Set<Object> product(Object a, Object b) {
        Set<Object> out = new HashSet<>();
        if (a instanceof HashSet) {
            if (b instanceof HashSet) {
                Iterator<Object> itrA = ((HashSet<Object>)a).iterator();
                while (itrA.hasNext()) {
                    Object e1 = itrA.next();
                    if (e1 instanceof HashSet) {
                        HashSet<Integer> ea = (HashSet<Integer>)e1;
                        Iterator<Object> itrB = ((HashSet<Object>)b).iterator();
                        while (itrB.hasNext()) {
                            Object e2 = itrB.next();
                            if (e2 instanceof HashSet) {
                                HashSet<Integer> eb = (HashSet<Integer>)e2;
                                Pair<HashSet<Integer>,HashSet<Integer>> pair = new Pair<>(ea,eb);
                                out.add(pair);
                            } else {
                                Integer eb = (Integer)e2;
                                Pair<HashSet<Integer>,Integer> pair = new Pair<>(ea,eb);
                                out.add(pair);
                            }
                        }
                    } else {
                        Integer ea = (Integer)e1;
                        Iterator<Object> itrB = ((HashSet<Object>)b).iterator();
                        while (itrB.hasNext()) {
                            Object e2 = itrB.next();
                            if (e2 instanceof HashSet) {
                                HashSet<Integer> eb = (HashSet<Integer>)e2;
                                Pair<Integer,HashSet<Integer>> pair = new Pair<>(ea,eb);
                                out.add(pair);
                            } else {
                                Integer eb = (Integer)e2;
                                Pair<Integer,Integer> pair = new Pair<>(ea,eb);
                                out.add(pair);
                            }
                        }
                    }
                }
            }  else if (b instanceof Integer) {
                Iterator<Object> itrA = ((HashSet<Object>)a).iterator();
                while (itrA.hasNext()) {
                    HashSet<Integer> ea = (HashSet<Integer>)(itrA.next());
                    Pair<HashSet<Integer>,Integer> pair = new Pair<>(ea,(Integer) b);
                    out.add(pair);
                }
            }
        } else if (a instanceof Integer) {
            if (b instanceof HashSet) {
                Iterator<Object> itrB = ((HashSet<Object>)b).iterator();
                while (itrB.hasNext()) {
                    Object e = itrB.next();
                    if (e instanceof HashSet) {
                        HashSet<Integer> eb = (HashSet<Integer>)e;
                        Pair<Integer,HashSet<Integer>> pair = new Pair<>((Integer) a,eb);
                        out.add(pair);
                    } else {
                        Pair<Integer,Integer> pair = new Pair<>((Integer) a,(Integer) e);
                        out.add(pair);
                    }
                }
            }  else if (b instanceof Integer) {
                Pair<Integer,Integer> pair = new Pair<>((Integer) a,(Integer) b);
                out.add(pair);
            }
        }
        return out;
    }
    public static Pair<Set<Integer>,Integer> take(Set<Object> fractal){
        Iterator<Object> it = fractal.iterator();
        it.hasNext();
        Pair<Set<Integer>,Integer> e = (Pair<Set<Integer>,Integer>) it.next();
        fractal.remove(e);
        return e;
    }
    public static Pair<Set<Integer>,Set<Integer>> separate(Automata A, Pair<Set<Integer>,Integer> Kx, Set<Integer> L){
        Pair<Set<Integer>,Set<Integer>> separateL = new Pair(new HashSet<Integer>() ,new HashSet<Integer>());
        for (Object item : L) {
            if (Kx.fst.contains(getStateForA.getState(A,(Integer)item,Kx.snd))) {
                separateL.fst.add((Integer)item);
            } else {
                separateL.snd.add((Integer)item);
            }
        }
        return separateL;
    }
    public static void replace(Set<Object> R,Set<Integer> L,Pair<Set<Integer>,Set<Integer>> separateL) {
        R.remove(L);
        if (separateL.fst.size() != 0) R.add(separateL.fst);
        if (separateL.snd.size() != 0) R.add(separateL.snd);
    }
    public static void printAutomata(Automata A, String name) {
        System.out.println(name);
        System.out.println("A = " + A.Q);
        System.out.println("X = " + A.X);
        System.out.println();
        System.out.println("F(A, X):");
        for (int i = 0;i<A.X.size(); ++i) {
            System.out.print(i + ": ");
            for (int j = 1;j<A.Q.size()+1; ++j)
                System.out.print(getStateForA.getState(A,j,i) + " ");
            System.out.println();
        }
        System.out.println();
        System.out.println("I = " + A.I);
        System.out.println("F = " + A.F);
    }
}
