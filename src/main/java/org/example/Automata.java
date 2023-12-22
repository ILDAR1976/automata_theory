package org.example;

import java.util.*;

public class Automata {
    private List<HashMap<Integer, HashSet<Integer>>> EquivalenceClasses = new ArrayList();
    private Integer[][] Function;
    public Integer[][] getFunction() {
        return Function;
    }
    public void setFunction(Integer[][] function) {
        Function = function;
    }
    public Set<Integer> X;
    public Set<Integer> Q;
    public Set<Integer> I;
    public Set<Integer> F;
    public Integer f(IState method, Integer a, Integer x) {
        return  method.getState(this,a,x);
    }
    public List<HashMap<Integer, HashSet<Integer>>> getEquivalenceClasses() {
        return EquivalenceClasses;
    }
}
