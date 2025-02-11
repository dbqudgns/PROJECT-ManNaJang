package com.culture.BAEUNDAY.domain.post;

public enum FeeRange {

    FREE(0,0),
    UNDER_3(0,29999),
    BETWEEN3_5(30000,49999),
    BETWEEN5_10(50000,99999),
    OVER_10(99999,Integer.MAX_VALUE);

    private Integer min;
    private Integer max;

    FeeRange(int i, int i1) {
    }

    public Integer getMin(){ return min; }
    public Integer getMax(){ return max; }

    @Override
    public String toString() {
        return super.toString();
    }
}
