package scibd.lab.vms.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by asish on 09/04/16.
 *
 * @author asish
 */
public class AppUtils{

    private static Typeface bengaliFontNormalSolmn, bengaliFontBoldSolmn;

//    public static SpannableString getUnicodedFormat(AssetManager assetManager, String str) {
//        if (bengaliFontNormalSolmn == null)
//            bengaliFontNormalSolmn = Typeface.createFromAsset(assetManager,
//                    "fonts/solaimanlipinormal.ttf");
//        return AndroidUnicodedFontSupport.getCorrectedBengaliFormat(str, bengaliFontNormalSolmn, -1);
//    }

//    public static SpannableString getUnicodedFormatBold(AssetManager assetManager, String str) {
//        if (bengaliFontBoldSolmn == null)
//            bengaliFontBoldSolmn = Typeface.createFromAsset(assetManager,
//                    "fonts/solaimanlipibold.ttf");
//        return AndroidUnicodedFontSupport.getCorrectedBengaliFormat(str, bengaliFontBoldSolmn, -1);
//    }


    public static int getScreenHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * @return Screen width or height, depending on which is minimum
     */
    public static int getScreenMinHW(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int w = dm.widthPixels;
        int h = dm.heightPixels;
        return w < h ? w : h;
    }

    public static float getDeviceDpi(Context context) {
        return context.getResources().getDisplayMetrics().density * 160.0f;
    }

    public static String getDeviceDetailsJSONString(Context ctx) {
        JSONObject jo = new JSONObject();
        try {
            jo.put("os", Build.VERSION.SDK_INT);
            jo.put("model", Build.MODEL);
            jo.put("manufacturer", Build.MANUFACTURER);
            jo.put("ip", getDeviceIp(true));
            jo.put("imei", getDeviceIMEI(ctx));
         //   jo.put("mac", getDeviceMAC(ctx));
            jo.put("dpi", AppUtils.getDeviceDpi(ctx));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jo.toString();
    }

    /**
     * Get IP address from first non-localhost interface
     *
     * @param useIPv4 true=return ipv4, false=return ipv6
     * @return address or empty string
     */
    @SuppressLint("DefaultLocale")
    public static String getDeviceIp(boolean useIPv4) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress();
                        boolean isIPv4 = sAddr.indexOf(':') < 0;

                        if (useIPv4) {
                            if (isIPv4)
                                return sAddr;
                        } else {
                            if (!isIPv4) {
                                int delim = sAddr.indexOf('%'); // drop ip6 zone suffix
                                return delim < 0 ? sAddr.toUpperCase() : sAddr.substring(0, delim).toUpperCase();
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            // for now eat exceptions
        }
        return "";
    }

//    public static String getDeviceMAC(Context context) {
//        WifiManager wifiManager = (WifiManager) context
//                .getSystemService(Context.WIFI_SERVICE);
//        WifiInfo wInfo = wifiManager.getConnectionInfo();
//        return wInfo.getMacAddress();
//    }

    public static String getDeviceIMEI(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getDeviceId() + "";
    }

    public static boolean isDevicePortrait(Resources res) {
        return res.getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
    }


    public static boolean isNetConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static boolean isWifiConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        return wifiInfo.isConnected();
    }

//    public static void setTextWithFonts(Context context, View view, String text) {
//
//        TextView textView = (TextView) view.findViewById(R.id.tv_fb_observ_question);
//        textView.setText(text);
//        // Loading Font Face
//        Typeface tf = Typeface.createFromAsset(context.getAssets(), "BorakMJ.ttf");
//        // Applying font
//        textView.setTypeface(tf);
////        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
//    }

    public static void setTextWithFonts(Context context, TextView textView, String text) {
        textView.setText(text);
        // Loading Font Face
        Typeface tf = Typeface.createFromAsset(context.getAssets(), "BorakMJ.ttf");
        // Applying font
        textView.setTypeface(tf,Typeface.BOLD);
//        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
    }

    public static void setTextWithFonts(Context context, CheckBox checkbox, String text) {
        checkbox.setText(text);
        // Loading Font Face
        Typeface tf = Typeface.createFromAsset(context.getAssets(), "BorakMJ.ttf");
        // Applying font
        checkbox.setTypeface(tf);
        checkbox.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
    }



    public static String getTime(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm:ss a");
        return dateFormat.format(new Date()).toString();
    }

    public static String getDate(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        return dateFormat.format(new Date()).toString();
    }

    public static String getUserName(Context context){
        SharedPreferences pref = context.getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        return pref.getString("username", "");
//        password = pref.getString("password", null);
    }

    public static void setUserName(Context context,String user){
        SharedPreferences pref = context.getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        pref.edit().putString("username",user).commit();
    }

    public static void setUserPassword(Context context,String password){
        SharedPreferences pref = context.getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        pref.edit().putString("password",password).commit();
    }

    public static void setUserType(Context context, String userType) {
        SharedPreferences pref = context.getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        pref.edit().putString("usertype",userType).commit();
    }

    public static String getUserType(Context context){
        SharedPreferences pref = context.getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        return pref.getString("usertype","");
    }

    public static String getPassword(Context context){
        SharedPreferences pref = context.getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        return pref.getString("password", "");
    }
    public static String btos(boolean b){
        if(b) return "true";
        else return "false";
    }
    public static String itos(int i){
        try{
            return Integer.toString(i);
        }catch (Exception e){
            e.printStackTrace();
        }
        return "0";
    }

    public static boolean getBoolean(JSONObject data,String key) throws JSONException {
        if(data.has(key)){
            return data.getBoolean(key);
        }
        return false;
    }

    public static String getArray(JSONObject data,String key) throws JSONException {
        if(data.has(key)){
            JSONArray array = data.getJSONArray(key);
            int len = array.length();
            String result = "";
            for(int i =0;i<len;i++){
                if(i > 0){
                    result += ",";
                }
                result += array.getString(i);
            }
            return result;
        }
        return "";
    }

    public static String getString(JSONObject data,String key) throws JSONException {
        if(data != null && data.has(key)){
            return data.getString(key);
        }
        return "";
    }

    public static int getInt(JSONObject data,String key) throws JSONException {
        if(data != null && data.has(key)){
            return data.getInt(key);
        }
        return 0;
    }

    public static void setFullName(Context context, String fullName) {
        SharedPreferences pref = context.getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        pref.edit().putString("full_name",fullName).commit();
    }

    public static String getFullName(Context context){
        SharedPreferences pref = context.getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        return pref.getString("full_name","");
    }
}
