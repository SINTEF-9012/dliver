/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.thingml.dliver.desktop;

/**
 *
 * @author steffend
 */
public class SimpleFirDifferentiator {
    // Due to lack of positive wrapping support for negative modulo operation the indexing is reversed
    // New sample is decrement of idx. Old samples is x[n-2] is positive x[idx+2]
    private final int firZArrSize = 5;
    private int[] firZArr = new int[firZArrSize];
    private int   firZIdx = 0;
    private boolean firZArrFull = false;

    private int deriv = 0;

    public void addSample(int newVal) {

        // Calculate next current position
        firZIdx--;
        if (firZIdx < 0) firZIdx = firZArrSize - 1;
        if (firZIdx == 0) firZArrFull = true;
        
        // Put the ac sample into the FIR array
        firZArr[firZIdx] = newVal;
        
        // Calculate the derivate - Simple FIR Digital Differentiator  y[n]=1/2x[n]-1/2x[n-2]
        deriv = 0;
        deriv += firZArr[firZIdx] / 2;
        deriv -= firZArr[(firZIdx+2) % firZArrSize] / 2;

    }
    
    public int getDeriv() {
        int ret = deriv;
        
        if (firZArrFull == false) ret = 0; // Surpress startup spikes
        return ret;
    }
    
}
