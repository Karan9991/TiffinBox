package com.tiff.tiffinbox.Seller.addCustomers.map.interfaces2;


@FunctionalInterface
public interface IPositiveNegativeListener {
    void onPositive();

   default void onNegative(){}


    public static final class DefaultImpls {
        public static void onNegative(IPositiveNegativeListener $this) {
        }
    }
}
