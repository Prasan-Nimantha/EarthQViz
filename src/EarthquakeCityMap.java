//Java utilities libraries
import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Comparator;
import java.util.List;

//Processing library
import parsing.ParseFeed;
import processing.core.PApplet;
import processing.core.PFont;
//Unfolding libraries
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import de.fhpotsdam.unfolding.providers.Google;
import de.fhpotsdam.unfolding.providers.MBTilesMapProvider;
import de.fhpotsdam.unfolding.utils.MapUtils;

//Parsing library


/** EarthquakeCityMap
 * An application with an interactive map displaying earthquake data.
 * Author: UC San Diego Intermediate Software Development MOOC team
 * @author Prasan Nimantha
 * Date: July 17, 2015
 * */
public class EarthquakeCityMap extends PApplet {

	// You can ignore this.  It's to keep eclipse from generating a warning.
	private static final long serialVersionUID = 1L;

	// IF YOU ARE WORKING OFFLINE, change the value of this variable to true
	private static final boolean offline = false;
	
	// Less than this threshold is a light earthquake
	public static final float THRESHOLD_MODERATE = 5;
	// Less than this threshold is a minor earthquake
	public static final float THRESHOLD_LIGHT = 4;

	/** This is where to find the local tiles, for working without an Internet connection */
	public static String mbTilesString = "blankLight-1-3.mbtiles";
	
	// The map
	private UnfoldingMap map;
	
	//feed with magnitude 2.5+ Earthquakes
	private String earthquakesURL = "https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/2.5_week.atom";

	
	public void setup() {
		size(960, 540, OPENGL);

		if (offline) {
		    map = new UnfoldingMap(this, 200, 15, 745, 510, new MBTilesMapProvider(mbTilesString));
		    earthquakesURL = "2.5_week.atom"; 	// Same feed, saved Aug 7, 2015, for working offline
		}
		else {
			map = new UnfoldingMap(this, 200, 15, 745, 510, new Google.GoogleMapProvider());
			// IF YOU WANT TO TEST WITH A LOCAL FILE, uncomment the next line
			//earthquakesURL = "2.5_week.atom";
		}
		
	    map.zoomToLevel(2);
	    MapUtils.createDefaultEventDispatcher(this, map);	
			
	    // The List you will populate with new SimplePointMarkers
	    List<Marker> markers = new ArrayList<Marker>();

	    //Use provided parser to collect properties for each earthquake
	    //PointFeatures have a getLocation method
	    List<PointFeature> earthquakes = ParseFeed.parseEarthquake(this, earthquakesURL);
	    
	    //TODO (Step 3): Add a loop here that calls createMarker (see below) 
	    // to create a new SimplePointMarker for each PointFeature in 
	    // earthquakes.  Then add each new SimplePointMarker to the 
	    // List markers (so that it will be added to the map in the line below)
	    for (PointFeature eq: earthquakes) {
	    	
	    	markers.add(createMarker(eq));
	    	
	    }
	    
	    // Add the markers to the map so that they are displayed
	    map.addMarkers(markers);
	}
		
	/* createMarker: A suggested helper method that takes in an earthquake 
	 * feature and returns a SimplePointMarker for that earthquake
	 * 
	 * In step 3 You can use this method as-is.  Call it from a loop in the 
	 * setp method.  
	 * 
	 * TODO (Step 4): Add code to this method so that it adds the proper 
	 * styling to each marker based on the magnitude of the earthquake.  
	*/
	private SimplePointMarker createMarker(PointFeature feature)
	{  
		// To print all of the features in a PointFeature (so you can see what they are)
		// uncomment the line below.  Note this will only print if you call createMarker 
		// from setup
		//System.out.println(feature.getProperties());
		
		// Create a new SimplePointMarker at the location given by the PointFeature
		SimplePointMarker marker = new SimplePointMarker(feature.getLocation());
		
		Object magObj = feature.getProperty("magnitude");
		float mag = Float.parseFloat(magObj.toString());
		
		// Here is an example of how to use Processing's color method to generate 
	    // an int that represents the color yellow.  
	    int yellow = color(255, 255, 0);
	    int blue = color(0, 0, 255);
	    int red = color(255, 0, 0);
		
		// TODO (Step 4): Add code below to style the marker's size and color 
	    // according to the magnitude of the earthquake.  
	    // Don't forget about the constants THRESHOLD_MODERATE and 
	    // THRESHOLD_LIGHT, which are declared above.
	    // Rather than comparing the magnitude to a number directly, compare 
	    // the magnitude to these variables (and change their value in the code 
	    // above if you want to change what you mean by "moderate" and "light")
	    if (mag < THRESHOLD_LIGHT) {
	    	// A minor earthquake = 4, yellow
	    	marker.setColor(yellow);
	    	marker.setRadius(5);
	    }
	    else if (mag < THRESHOLD_MODERATE){
	    	// A light earthquake = 5, blue
	    	marker.setColor(blue);
	    	marker.setRadius(10);
	    }
	    else {
	    	marker.setColor(red);
	    	marker.setRadius(15);
	    }
	    // Finally return the marker
	    return marker;
	}
	
	public void draw() {
	    background(loadImage("resources/planet-5.jpg"));
	    map.draw();
	    addKey();
	}


	// helper method to draw key in GUI
	// TODO: Implement this method to draw the key
	private void addKey() 
	{	
		// Drawing the rectangle
		fill(250, 250, 250);
		stroke(150);
		rect(15, 15, 160, 400, 0, 12, 48, 48);	
		
		// Drawing the markers(circles)
		fill(255, 0, 0);
		ellipse(35, 100, 15, 15);
		
		fill(0, 0, 255);
		ellipse(35, 175, 10, 10);
		
		fill(255, 255, 0);
		ellipse(35, 250, 5, 5);
		
		// Marker explanations 
		PFont f1 = createFont("resources/OpenSans-Semibold.ttf", 16);
		textFont(f1, 16);
		fill(0, 0, 0);
		String s = "Earthquake Keys";
		text(s, 30, 45);
		
		PFont f2 = createFont("resources/OpenSans-Regular.ttf", 12);
		textFont(f2);
		String s1 = "5.0+ magnitude";
		text(s1, 65, 105);
		
		String s2 = "4+ magnitude";
		text(s2, 65, 180);
		
		String s3 = "below 4.0";
		text(s3, 65, 255);
	}

	public static void main(String[] args) {
		PApplet.main("EarthquakeCityMap");
	}
}
