package com.example.captureimage.Classes;

import java.io.Serializable;

public class ImageUrlSaved implements Serializable
{
    public String imageURL;

    public ImageUrlSaved()
    {
        // This is default constructor.
    }

    public ImageUrlSaved(String imageURL)
    {
        this.imageURL = imageURL;
    }

    public void setImageURL(String imageURL) { this.imageURL = imageURL; }

    public String getImageURL() {
        return imageURL;
    }

    @Override
    public String toString() {
        return "Receipts Details{" + "imageURL='" + imageURL + '\'' + '}';
    }
}
