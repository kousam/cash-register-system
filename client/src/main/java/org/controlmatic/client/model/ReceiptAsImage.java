package org.controlmatic.client.model;

import org.controlmatic.client.controller.ProductBundleController;
import org.controlmatic.client.controller.ShoppingCartController;
import org.controlmatic.shared.domain.Product;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;

import java.time.format.DateTimeFormatter;  
import java.time.LocalDateTime;    

/**
 * Class whose meaning is to create an image file. There is only one method in the class.
 * Returns a .png that is saved and can be displayed in through the cashierview
 */

public class ReceiptAsImage {

    //Variables for how
    private final static int FONTSIZE = 48;
    private final static int ROWSPACING = 10;

    //Method returns a file but it also creates a png-file for the directory
    public File printReceipt (ShoppingCartController shoppingCart){ //The only method which is needed to call outside

        //Creating the base for the drawing to the image. Using BufferedImage.
        //First the size of BufferedImage is tiny since it'll makes bigger sooner
        BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();
        Font font = new Font("Arial", Font.PLAIN, FONTSIZE);
        g2d.setFont(font);
        FontMetrics fm = g2d.getFontMetrics();
        //Next are values which has been chosen by random and because they look like ok. Free to change
        int width = 1500; //If it is needed, these could be changed to constant
        int height = 1000;

        g2d.dispose();

        //Direct from the manual
        img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        g2d = img.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        g2d.setFont(font);
        //fm = g2d.getFontMetrics();
        g2d.setColor(Color.BLACK); //The another thing which is possible to change as constant

        //Beginning og the receipt. It is needed to change quite manually if it is needed
        int nextLinePosition = 50; //Tells where the receipt starts from the top
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");  
		LocalDateTime now = LocalDateTime.now();  
        ArrayList<ProductBundleController> productBundleList = shoppingCart.getProductBundles();
        //Next lines writes first text to the image
        g2d.drawString("The receipt", 0, nextLinePosition);
		nextLinePosition = nextLinePosition + FONTSIZE + ROWSPACING;
		g2d.drawString("Time of purchase: " + dtf.format(now), 0, nextLinePosition);
        nextLinePosition = nextLinePosition + FONTSIZE + ROWSPACING;
        g2d.drawString("Product " + " Amount of products " + " Total sum of the products", 0, nextLinePosition);
        nextLinePosition = nextLinePosition + FONTSIZE + ROWSPACING;

        //Writing the rows to the image from the Shopping cart
        for(ProductBundleController productBundle: shoppingCart.getProductBundles()){
            Product product = productBundle.getProduct();
            String name = product.getName();
            int amountProducts = productBundle.getAmount();
            double totalSumProducts = Math.round(productBundle.getTotalPrice(shoppingCart.isBonusCustomer()).doubleValue() * 100.0) / 100.0;
            String line = name + " " + amountProducts + " " + totalSumProducts;

            g2d.drawString(line, 0, nextLinePosition);
            nextLinePosition = nextLinePosition + FONTSIZE + ROWSPACING;
        }
        g2d.drawString("Total price:    " + String.valueOf(shoppingCart.getTotalPrice().doubleValue()), 0, nextLinePosition);

        try { //Here the image file is created. Overwrites the file if there is an old one
            ImageIO.write(img, "png", new File("Receipt.png"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return new File("Receipt.png");
    }

}
