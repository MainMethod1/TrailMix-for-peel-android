package com.mainmethod.trailmix1;

/***
 * <h1> TrailMix for Android Capstone Project </h1>
 * <h2> Item model class </h2>
 * <p> Client: Erica Duque </p>
 * <p> Oganization: Region of Peel </p>
 * @author jonathan zarate, parth sondarva, shivam sharma, garrett may
 * @version 1.0
 */

public class NavDrawerItem {

	// Define field variables
	private String mTitle;
    private int mIcon;

    // Empty constructor
    public NavDrawerItem(){}

    /***
     * 
     * @param title - Name
     * @param icon - Icon
     */
    public NavDrawerItem(String title, int icon){
        this.mTitle = title;
        this.mIcon = icon;
    }

    // Define Getters and Setters
    public String getTitle(){
        return this.mTitle;
    }

    public int getIcon(){
        return this.mIcon;
    }

    public void setTitle(String title){
        this.mTitle = title;
    }

    public void setIcon(int icon){
        this.mIcon = icon;
    }     

}
