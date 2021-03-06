package com.hektorks.pointofsale;


import com.hektorks.pointofsale.barcodevalidation.Barcode;
import com.hektorks.pointofsale.barcodevalidation.BarcodeScanLog;
import com.hektorks.pointofsale.barcodevalidation.BarcodeScanner;
import com.hektorks.pointofsale.barcodevalidation.BarcodeValidator;

public class PointOfSale {

  private BarcodeScanner barcodeScanner;
  private DisplayPanel displayPanel;
  private Printer printer;

  private BarcodeScanLog barcodeScanLog;
  private BarcodeValidator barcodeValidator;
  private Cart cart;

  public PointOfSale(Database database, BarcodeScanner barcodeScanner, DisplayPanel displayPanel, Printer printer) {
    this.barcodeScanner = barcodeScanner;
    this.displayPanel = displayPanel;
    this.printer = printer;

    this.cart = new Cart();

    this.barcodeScanLog = new BarcodeScanLog();
    this.barcodeValidator = new BarcodeValidator(database, this.displayPanel, this.cart);

    this.barcodeScanLog.addObserver(this.barcodeValidator);

  }

  public void scanProduct() {
    Barcode barcode = this.barcodeScanner.scan();
    barcodeScanLog.addBarcode(barcode);
  }

  public void end() {
    displayPanel.displaySum(cart.getPriceSum());
    printer.printReceipt(this.cart);

  }

  public double getCurrentPriceSum() {
    return cart.getPriceSum();
  }

  public BarcodeScanLog getCurrentScanLog() {
    return barcodeScanLog;
  }

  public Cart getCurrentCart() {
    return cart;
  }

  public void beginNewTransaction() {
    this.displayPanel.clearPanel();
    this.cart = new Cart();
    this.barcodeScanLog = new BarcodeScanLog();

    this.barcodeScanLog.addObserver(this.barcodeValidator);
  }
}
