package com.mycompany.cafecoffeedayscraper;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import net.sourceforge.htmlunit.corejs.javascript.NativeArray;

/**
 *
 * @author vkumar
 */
public class Scrapper {

    static final WebClient WEB_CLIENT;
    //(1) Initial setup of WEB CLIENT by using Chrome browser, enabling java script events etc.
    static {
        WEB_CLIENT = new WebClient(BrowserVersion.CHROME);
        WEB_CLIENT.getOptions().setJavaScriptEnabled(true);
        WEB_CLIENT.setAjaxController(new NicelyResynchronizingAjaxController());
        WEB_CLIENT.getOptions().setThrowExceptionOnFailingStatusCode(false);
        WEB_CLIENT.getOptions().setPrintContentOnFailingStatusCode(true);
        WEB_CLIENT.getOptions().setThrowExceptionOnScriptError(false);
    }

    public static void main(String[] args) {
        String url = "http://www.cafecoffeeday.com/shop-locator";
        String storeName = "Cafe Coffee Day";
         List<Store> storeList = new ArrayList<>();
        try{
            storeList = submittingForm(url, storeName);
        }catch(Exception e) {
            //e.printStackTrace(); // Use logger to log any such exception thrown in the system.
        }
        System.out.println("Store List: "+ storeList);
    }

    public static List submittingForm(String URL, String storeName) {
       HtmlPage page1 = null;
        //(2) Get the first page
        try{
            page1 = WEB_CLIENT.getPage(URL);
        }catch(Exception e){
            // Use logger instead
            //e.printStackTrace();
        }
        //(3) Execute the Java Script function to get all the available stores from the browser
        Object listOfStores = (NativeArray) page1.executeJavaScript("getstores.stores").getJavaScriptResult();
        Gson gson = new Gson();
        String storeData = gson.toJson(listOfStores);
        //(4) long is reserved keyword hence replace all long by longitude and remove any non ascii code by empty.
        storeData = storeData.replaceAll("long", "longitude").replaceAll("[^\\x20-\\x7e]", "");
        Type type = new TypeToken<List<Store>>() {}.getType();
        List<Store> records = null;
        //(5) Read the json data and create list of Java POJO
        records = gson.fromJson(storeData, type);
        List<Store> result = new LinkedList<>();
        //(6) Search for the given store name and add the matching store in the result set
        for (Store record : records) {
            if (record.getName().toLowerCase().contains(storeName.toLowerCase())) {
                result.add(record);
            }
        }
        return result;
    }
}
//(7) POJO to collect all the store related data
class Store {

    private String name;
    private String address1;
    private String city_area;
    private String city;
    private String state;
    private String pincode, phone;
    private Double lat, longitude;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getCity_area() {
        return city_area;
    }

    public void setCity_area(String city_area) {
        this.city_area = city_area;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

  

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("\n\n\nname: ").append(name);
        result.append("\naddress1: ").append(address1);
        result.append("\ncityArea: ").append(city_area);
        result.append("\nlatitude: ").append(lat);
        result.append(", longitude: ").append(longitude);

        result.append("\npincode: ").append(pincode);
        result.append("\nstate: ").append(state);
        result.append("\nphone: ").append(phone);
        return result.toString();
    }
    
}
