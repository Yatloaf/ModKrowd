package dev.yatloaf.modkrowd.config;

import dev.yatloaf.modkrowd.util.ImmutableIndex;

public class PredicateIndex {
    public static final PredicateIndex TERNARY_MW = new PredicateIndex(Predicate.NEVER, Predicate.MISSILEWARS, Predicate.ALWAYS);
    public static final PredicateIndex BINARY_MW = new PredicateIndex(Predicate.NEVER, Predicate.MISSILEWARS);

    public static final PredicateIndex BINARY_CR = new PredicateIndex(Predicate.NEVER, Predicate.CREATIVE);

    public static final PredicateIndex TERNARY_CK = new PredicateIndex(Predicate.NEVER, Predicate.CUBEKROWD, Predicate.ALWAYS);
    public static final PredicateIndex BINARY_CK = new PredicateIndex(Predicate.NEVER, Predicate.CUBEKROWD);

    public static final PredicateIndex BINARY = new PredicateIndex(Predicate.NEVER, Predicate.ALWAYS);

    public final ImmutableIndex<Predicate> index;

    public PredicateIndex(Predicate... predicates) {
        this.index = ImmutableIndex.ofStable(predicates);
    }
}
