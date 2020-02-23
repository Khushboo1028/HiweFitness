package com.rgala98.hiwefitness.Utility;


import com.rgala98.hiwefitness.Nearby.ContentsNearby;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SortingClass {

    List<ContentsNearby> sortingClass = new ArrayList<>();

    public SortingClass(List<ContentsNearby> distanceSort) {
        this.sortingClass = distanceSort;
    }
    public List<ContentsNearby> sortDistanceLowToHigh() {
        Collections.sort(sortingClass, ContentsNearby.distanceLowToHigh);
        return sortingClass;
    }



}