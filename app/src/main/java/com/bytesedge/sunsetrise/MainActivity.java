package com.bytesedge.sunsetrise;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import org.shredzone.commons.suncalc.MoonTimes;
import org.shredzone.commons.suncalc.SunTimes;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import swisseph.SweConst;
import swisseph.SweDate;
import swisseph.SwissEph;

public class MainActivity extends AppCompatActivity {

    private SunTimes times;
    private MoonTimes moonTimes;
    private SunTimes nextDaytimes;
    private String prevNakshTime;
    private SunTimes prevDaytimes;
    private double sun_long,moon_long;
    private double nextDay_sun_long,nextDay_moon_long,prevDay_sun_long,prevDay_moon_long;
    int sun_sign,moon_sign,nextDay_sun_sign,nextDay_moon_sign,prevDay_sun_sign,prevDay_moon_sign;
    private int slots[][]={
            {8,5,7},
            {2,4,6},
            {7,3,5},
            {5,2,4},
            {6,1,3},
            {4,7,2},
            {3,6,1}
    };
    private int dayInWeek;
    Date dateObject = null;
    double adhika_moon_long;
    int adhika_moon_sign;
    int deg, min;
    int tithi;
    private static int SID_METHOD;
    double  sec;
    Calendar cal;
    TextView textView1,textView2;
    EditText userInput,selectDate;
    double lat,lng;
    DatePicker picker;
    String selectDay;
    DatePickerDialog datePickerDialog;
    int year;
    int month;
    int dayOfMonth;
    int maasam;
    Calendar calendar;
    TextView date,sunrise_time,sunset_time,rahu_kalam,durm_time,tithi_time,star_time,yama_gandam,galikai_kalam,yogam_kalam,karanam,amrutha_kalam,varjyam,moon_sign_tv,sun_sign_tv,moon_rise,moon_set,maasam_tv;
    String[] tithis;
    String[] nakshatras;
    String[] yogas;
    String[] teluguYears;
    String[] zodaic_signs;
    String[] masams;
    String[] seasons;
    String tYEAR;
    Button change_Date;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        change_Date = findViewById(R.id.change_date);


        change_Date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPincode();
            }
        });
        /*textView1 = findViewById(R.id.textView2);
        textView2 = findViewById(R.id.textView3);*/
        tithis = new String[]{"Shukla Pratipada", "Shukla Dwitiya","Shukla Tritiya","Shukla Chaturthi","Shukla Panchami","Shukla Shashthi","Shukla Saptami",
                              "Shukla Ashtami (Half Moon)","Shukla Navami","Shukla Dashami","Shukla Ekadasi","Shukla Dwadashi","Shukla Trayodashi",
                              "Shukla Chaturdashi","Shukla Purnima","Krishna Pratipada", "Krishna Dwitiya","Krishna Tritiya","Krishna Chaturthi","Krishna Panchami","Krishna Shashthi","Krishna Saptami",
                               "Krishna Ashtami (Half Moon)","Krishna Navami","Krishna Dashami","Krishna Ekadasi","Krishna Dwadashi","Krishna Trayodashi",
                               "Krishna Chaturdashi","Krishna Amavasya"};
        nakshatras = new String[]{"Aswini","Bharani","Krithika","Rohini","Mrigasira","Ardra","Punarvasu","Pushyami","Aslesha","Makha","Pubba",
                                "Uttara","Hasta","Chitta","Swathi","Visakha","Anuradha","Jyesta","Moola","Purvashadha","Uttarashadha","Sravanam",
                                "Dhanista","Satabhisham","Purvabhadra","Uttarabhadra","Revathi"};

        zodaic_signs = new String[]{"Aries","Taurus","Gemini","Cancer","Leo","Virgo","Libra","Scorpio","Sagittarius","Capricorn","Aquarius","Pisces"};

        masams = new String[]{"Vaisakha","Jyestha","Asadha","Sravana","Bhadrapada","Asvina","Kartika","Margasirsa","Pausa","Magha","Phalgura","Chaitra"};

        seasons = new String[]{"Winter","Summer","Rainy"};

        teluguYears = new String[]{"Prabhava", "Vibhava", "Sukla", "Pramodyuta", "Prajothpatti", "Aangeerasa", "Sreemukha", "Bhāva", "Yuva", "Dhāta", "Īswara", "Bahudhānya", "Pramādhi", "Vikrama",
                                    "Vrisha", "Chitrabhānu", "Svabhānu", "Tārana", "Pārthiva", "Vyaya", "Sarvajiththu", "Sarvadhāri", "Virodhi", "Vikruti", "Khara", "Nandana", "Vijaya", "Jaya", "Manmadha",
                                    "Durmukhi", "Hevalambi", "Vilambi", "Vikāri", "Sārvari", "Plava", "Subhakritu", "Sobhakritu", "Krodhi", "Viswāvasu", "Parābhava", "Plavanga", "Kīlaka", "Soumya", "Sādhārana",
                                    "Virodhikritu", "Paridhāvi", "Pramādeecha", "Ānanda", "Rākshasa", "Nala", "Pingala", "Kālayukti", "Siddhārthi", "Roudri", "Durmathi", "Dundubhi", "Rudhirodgāri", "Raktākshi", "Krodhana", "Akshya",};

        yogas = new String[]{"Vishkambha","Priti","Ayushman","Soubhagya","Sobhana","Atiganda","Sukarma","Dhrithi","Soola","Ganda","Vriddhi","Dhruva","Vyaghata","Harshana","Vajra","Siddhi","Vyatipata",
                "Variyana","Parigha","Siva","Siddha","Sadhya","Subha","Sukla","Brahma","Indra","Vaidhrthi"};
        date = findViewById(R.id.date);
        sunrise_time = findViewById(R.id.sunrise_time);
        sunset_time = findViewById(R.id.sunset_time);
        tithi_time = findViewById(R.id.tithi_time);
        star_time = findViewById(R.id.star_time);
        yama_gandam = findViewById(R.id.yama_gandam);
        durm_time = findViewById(R.id.durm_time);
        rahu_kalam = findViewById(R.id.rahu_kalam);
        galikai_kalam = findViewById(R.id.galikai_kalam);
        yogam_kalam = findViewById(R.id.yogam_kalam);
        amrutha_kalam = findViewById(R.id.amrutha_kalam);
        varjyam = findViewById(R.id.varjyam);
        karanam = findViewById(R.id.karanam);
        moon_sign_tv = findViewById(R.id.moon_sign);
        sun_sign_tv = findViewById(R.id.sun_sign);
        moon_rise = findViewById(R.id.moonrise_time);
        moon_set = findViewById(R.id.moonset_time);
        maasam_tv = findViewById(R.id.maasam);
        Places.initialize(getApplicationContext(), "AIzaSyDGp6v-u8gEizvQ17KhgaUCiskhl7FBKcs");
        showPincode();

    }
    public String getCurrentDate(){
        StringBuilder builder=new StringBuilder();;
        builder.append((picker.getMonth() + 1)+"/");//month is 0 based
        builder.append(picker.getDayOfMonth()+"/");
        builder.append(picker.getYear());
        return builder.toString();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    protected void onActivityResult(int requestcode, int resultcode, Intent data) {
        if(requestcode== 100 && resultcode == RESULT_OK){
            Place place = Autocomplete.getPlaceFromIntent(data);
            userInput.setText(place.getAddress());

            System.out.println("Address longitude "+String.valueOf(place.getLatLng().longitude));
            System.out.println("Address latitude "+String.valueOf(place.getLatLng().latitude));
           // textView1.setText(String.format("Locality Name : %s",place.getName()));
            //textView2.setText(String.valueOf(place.getLatLng().longitude));
            lat = place.getLatLng().latitude;
            lng = place.getLatLng().longitude;
            //calculatePanchangam();

        }else if(resultcode == AutocompleteActivity.RESULT_ERROR){
            Status status = Autocomplete.getStatusFromIntent(data);
            Toast.makeText(getApplicationContext(),status.getStatusMessage(),Toast.LENGTH_LONG).show();
        }
        super.onActivityResult(requestcode, resultcode, data);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void calculatePanchangam(String date,double lat,double lng) {
        SID_METHOD = SweConst.SE_SIDM_LAHIRI;
        DateFormat sdfor = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = date;

        try {
            dateObject = sdfor.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        cal = Calendar.getInstance();
        cal.setTime(dateObject);
        dayInWeek = getDayNumberOld(dateObject);
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

        //  double lat = 16.314209, lng = 80.435028;// geolocation
        times = SunTimes.compute()
                .on(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH)+1,cal.get(Calendar.DAY_OF_MONTH))   // set a date
                .at(lat, lng)   // set a location
                .execute();     // get the results

        moonTimes = MoonTimes.compute()
                .on(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH)+1,cal.get(Calendar.DAY_OF_MONTH))   // set a date
                .at(lat, lng)   // set a location
                .execute();

        String sunrise = dateFormat.format(Date.from(times.getRise().toInstant()));
        calculateHouseAndSignOfPlanet(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH)+1,cal.get(Calendar.DATE),sunrise,lng,lat);
        moon_sign_tv.setText(zodaic_signs[moon_sign-1]);
        sun_sign_tv.setText(zodaic_signs[sun_sign-1]);

        //adding one day for getting next day
        cal.add(Calendar.DATE,1);
        nextDaytimes = SunTimes.compute()
                .on(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH)+1,cal.get(Calendar.DATE))   // set a date
                .at(lat, lng)   // set a location
                .execute();
        String nextDaysunrise = dateFormat.format(Date.from(nextDaytimes.getRise().toInstant()));
        calculateNextDayHouseAndSignOfPlanet(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH)+1,cal.get(Calendar.DATE),nextDaysunrise,lng,lat);
        //setting to current date
        cal.add(Calendar.DATE,-1);
        //decreasing one day to ger previous day
        cal.add(Calendar.DATE,-1);
        prevDaytimes = SunTimes.compute()
                .on(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH)+1,cal.get(Calendar.DATE))   // set a date
                .at(lat, lng)   // set a location
                .execute();
        String prevDaysunrise = dateFormat.format(Date.from(prevDaytimes.getRise().toInstant()));
        calculatePrevDayHouseAndSignOfPlanet(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH)+1,cal.get(Calendar.DATE),prevDaysunrise,lng,lat);
        //setting to current date
        cal.add(Calendar.DATE,1);
        SwissEph eph =  new SwissEph(getApplicationContext().getFilesDir() + File.separator + "/ephe");
        String sunset = dateFormat.format(Date.from(times.getSet().toInstant()));
        String moonRise = dateFormat.format(Date.from(moonTimes.getRise().toInstant()));
        String moonSet = dateFormat.format(Date.from(moonTimes.getSet().toInstant()));
        System.out.println("rise time "+sunrise);
        System.out.println("set time  "+sunset);
        sunrise_time.setText(sunrise);
        sunset_time.setText(sunset);
        moon_rise.setText(moonRise);
        moon_set.setText(moonSet);
        calculateTithi(sun_long,moon_long,nextDay_sun_long,nextDay_moon_long,sunrise,prevDay_moon_long,prevDay_sun_long,prevDaysunrise);
        calculateNakshatram(moon_long,nextDay_moon_long,prevDay_moon_long,sunrise,prevDaysunrise);
        calculateYogam(sun_long,moon_long,nextDay_sun_long,nextDay_moon_long,sunrise);
        calculateMasam(sun_long);
        Date rise = null;
        Date setTime=null;
        Date TwentyFourFormat = null;
        try {
            rise = dateFormat.parse(sunrise);
            setTime = dateFormat.parse(sunset);
            TwentyFourFormat = dateFormat.parse("24:00:00");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long difference_In_Time = setTime.getTime() - rise.getTime();
        long difference_in_hours = (difference_In_Time/(1000*60*60))%24;
        long difference_In_Minutes = (difference_In_Time / (1000 * 60)) % 60;
        long sunrise_hour = Long.parseLong(sunrise.substring(0,2));
        long sunrise_min = Long.parseLong(sunrise.substring(3,5));
        long sunset_hour = Long.parseLong(sunset.substring(0,2));
        long sunset_min = Long.parseLong(sunset.substring(3,5));

        double duration_of_day = difference_in_hours+((double)difference_In_Minutes/60);
        double sunrise_in_minutes = sunrise_hour+((double)sunrise_min/60);
        double sunset_in = sunset_hour+((double)sunset_min/60);
        long diff_night_In_Time = 0;
        try {
            String day_time = String.valueOf(difference_in_hours)+":"+String.valueOf(difference_In_Minutes)+":00";
            System.out.println("day time "+day_time);
            Date day_all_time = dateFormat.parse(day_time);
            diff_night_In_Time = TwentyFourFormat.getTime()-day_all_time.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long diff_night_in_hours = (diff_night_In_Time/(1000*60*60))%24;
        long diff_night_In_Minutes = (diff_night_In_Time / (1000 * 60)) % 60;

        double duration_of_night = diff_night_in_hours+((double)diff_night_In_Minutes/60);

        calculateRahuKalam(sunrise_in_minutes,duration_of_day,dayInWeek);
        calculateGulikaKalam(sunrise_in_minutes,duration_of_day,dayInWeek);
        calculateYamagandam(sunrise_in_minutes,duration_of_day,dayInWeek);
        calculateDurmuhurtham(sunrise_in_minutes,duration_of_day,dayInWeek,sunset_in,duration_of_night);
    }

    private void calculateYear(int maasam, int tithi) {
        long startYear = 1867,endYear = 2106;
        int year = 0;
        if(cal.get(Calendar.YEAR) >= startYear && cal.get(Calendar.YEAR)<2106){
            if(maasam == 11 && tithi == 16) {
                year = (cal.get(Calendar.YEAR) - 6) % 60;
                System.out.println("YEar    "+year);
                tYEAR = teluguYears[year-1];
                System.out.println("TeluguYEar    "+tYEAR);
            }
            else{
                year = ((cal.get(Calendar.YEAR)-6)%60)-1;
                System.out.println("YEar    "+year);
                tYEAR = teluguYears[year-1];
                System.out.println("TeluguYEar    "+tYEAR);
            }
        }
    }

    private void calculateMasam(double sun_long) {
        double[] masams_mul = new double[]{27.25,57.25,87.25,117.25,147.25,177.25,207.25,237.25,267.25,297.25,327.25,357.25};
        String[] days = new String[]{"Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"};
        String[] ritus = new String[]{"Vasanta","Grīṣma","Varṣā","Sharad","Hemanta","Shishira"};
        String ritu = null;
        String ayanam = null;
        String season = null;
        if(sun_sign == 1 || sun_sign == 2 || sun_sign == 3 || sun_sign == 10 || sun_sign == 11 || sun_sign == 12){
            ayanam = "Uttarayana";
        }else{
            ayanam = "Dakshinayana ";
        }
        if(cal.get(Calendar.MONTH) == cal.get(Calendar.APRIL) || cal.get(Calendar.MONTH) == cal.get(Calendar.MAY)||
        cal.get(Calendar.MONTH) == cal.get(Calendar.JUNE)){
            season = "Summer season";
        }else if(cal.get(Calendar.MONTH) == cal.get(Calendar.JULY) || cal.get(Calendar.MONTH) == cal.get(Calendar.AUGUST) || cal.get(Calendar.MONTH) == cal.get(Calendar.SEPTEMBER) ||
                cal.get(Calendar.MONTH) == cal.get(Calendar.OCTOBER)){
            season = "Rainy season";
        }else{
            season = "Winter season";
        }
        for(int i=0;i<masams_mul.length;i++){
            if(i!=11) {
                if (sun_long == masams_mul[i] || sun_long < masams_mul[i + 1]) {
                    System.out.println("Masam is " + masams[i]);
                    if(i==11 || i==0){
                        ritu = ritus[0];
                    }if(i==1 || i==2){
                        ritu = ritus[1];
                    }if(i==3 || i==4){
                        ritu = ritus[2];
                    }if(i==5 || i==6){
                        ritu = ritus[3];
                    }if(i==7 || i==8){
                        ritu = ritus[4];
                    }if(i==9 || i==10){
                        ritu = ritus[5];
                    }
                    maasam = i;
                    calculateYear(maasam,tithi);
                    maasam_tv.setText(days[cal.get(Calendar.DAY_OF_WEEK)-1]+", "+masams[i]+", "+ayanam+", "+ritu+", "+season+", "+tYEAR+" Nama Samvatsaram");
                    break;
                }
            }
            else {
                if (sun_long == masams_mul[i] || sun_long <= 360.0 || sun_long>0.0 || sun_long < masams_mul[0]) {
                    System.out.println("Masam is " + masams[i]);
                    if(i==11 || i==0){
                        ritu = ritus[0];
                    }if(i==1 || i==2){
                        ritu = ritus[1];
                    }if(i==3 || i==4){
                        ritu = ritus[2];
                    }if(i==5 || i==6){
                        ritu = ritus[3];
                    }if(i==7 || i==8){
                        ritu = ritus[4];
                    }if(i==9 || i==10){
                        ritu = ritus[5];
                    }
                    maasam = i;
                    calculateYear(maasam,tithi);
                    maasam_tv.setText(days[cal.get(Calendar.DAY_OF_WEEK)-1]+", "+masams[i]+", "+ayanam+", "+ritu+", "+season+", "+tYEAR+" Nama Samvatsaram");
                    break;
                }
            }
        }
    }

    private void calculateYogam(double sun_long,double moon_long,double nextDay_sun_long,double nextDay_moon_long,String sunrise) {
        double sum = sun_long+moon_long;
        if(sum>360){
            sum-=360;
        }
        double yogamVal = sum/13.333333;
        int yogam = (int)Math.ceil(yogamVal);
        System.out.println("Yogam "+yogas[yogam-1]);
        double daily_motion_sun = nextDay_sun_long - sun_long;
        double daily_motion_moon = nextDay_moon_long - moon_long;
        if(daily_motion_moon < 0.0){
            daily_motion_moon += 360.0;
        }
        if(daily_motion_sun < 0.0){
            daily_motion_sun += 360.0;
        }
        double rd = (yogam*13.333333)-sum;

        double end_time = ((rd*60*24)/((daily_motion_moon*60)+(daily_motion_sun*60)));
        long sunrise_hour = Long.parseLong(sunrise.substring(0,2));
        long sunrise_min = Long.parseLong(sunrise.substring(3,5));
        double yogam_end_time = (sunrise_hour+((double)sunrise_min/60))+end_time;


        String yog_endTime = toDMS(yogam_end_time);
        String degrees = yog_endTime.substring(0,yog_endTime.lastIndexOf("°"));
        String str = degrees.replaceAll("\\s", "");
        long deg = Long.parseLong(str);
        if(deg>24){
            deg-=24;
            StringBuffer buf = new StringBuffer(yog_endTime);
            int start = 0;
            int end = yog_endTime.lastIndexOf("°");
            buf.replace(start, end, String.valueOf(deg));
            String start_deg = buf.toString();
            String hours = start_deg.substring(0,start_deg.lastIndexOf("°"));
            String str_hours = hours.replaceAll("\\s", "");
            String minutes = start_deg.substring(start_deg.lastIndexOf("°")+1,start_deg.lastIndexOf("'"));
            System.out.println("Yogam end time "+buf+" "+(cal.get(Calendar.DAY_OF_MONTH)+1));
            yogam_kalam.setText(yogas[yogam-1]+" "+str_hours+":"+minutes+" "+(cal.get(Calendar.DAY_OF_MONTH)+1));
        }
        else {
            System.out.println("Yogam ending time "+toDMS(yogam_end_time));
            String start_deg = toDMS(yogam_end_time);
            String hours = start_deg.substring(0,start_deg.lastIndexOf("°"));
            String str_hours = hours.replaceAll("\\s", "");
            String minutes = start_deg.substring(start_deg.lastIndexOf("°")+1,start_deg.lastIndexOf("'"));
            yogam_kalam.setText(yogas[yogam-1]+" "+str_hours+":"+minutes);
        }

    }

    private void showPincode() {
        LayoutInflater li = LayoutInflater.from(MainActivity.this);
        View promptsView = li.inflate(R.layout.pincode_popup, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                MainActivity.this);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);
        selectDate = (EditText)promptsView.findViewById(R.id.selectDate);
        selectDate.setFocusable(false);
        userInput = (EditText) promptsView
                .findViewById(R.id.editTextDialogUserInput);
        userInput.setFocusable(false);
        userInput.setOnClickListener(new View.OnClickListener() {
                                             @Override
                                             public void onClick(View view) {
                                                 List<Place.Field> fields = Arrays.asList(Place.Field.ADDRESS,Place.Field.LAT_LNG, Place.Field.NAME);
                                                 Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY
                                                         , fields).build(MainActivity.this);
                                                 startActivityForResult(intent, 100);

                                             }
                                         });

        selectDate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(MainActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                selectDate.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                                selectDay = selectDate.getText().toString();
                                date.setText("Date: "+selectDay);
                                //date.setTextColor(R.color.quantum_black_100);

                            }
                        }, year, month, dayOfMonth);
                datePickerDialog.show();

            }

        });

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @RequiresApi(api = Build.VERSION_CODES.O)
                            public void onClick(DialogInterface dialog, int id) {
                                // get user input and set it to result
                                // edit text
                               //result.setText(userInput.getText());
                                calculatePanchangam(selectDay,lat,lng);
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    private void calculatePrevDayHouseAndSignOfPlanet(int y, int m, int d, String prevDaysunrise, double lng, double lat) {
        long sunrise_hour = Long.parseLong(prevDaysunrise.substring(0,2));
        long sunrise_min = Long.parseLong(prevDaysunrise.substring(3,5));
        int year = y;
        int month = m;
        int day = d;
        double longitude =lng; // Guntur
        double latitude = lat;
        double hour = sunrise_hour + (sunrise_min / 60.) - 5.5; // IST



        // Use ... new SwissEph("/path/to/your/ephemeris/data/files"); when
        // your data files don't reside somewhere in the paths defined in
        // SweConst.SE_EPHE_PATH, which is ".:./ephe:/users/ephe2/:/users/ephe/"
        // currently.
        SwissEph sw =  new SwissEph(getApplicationContext().getFilesDir() + File.separator + "/ephe");
        SweDate sd = new SweDate(year, month, day, hour);
        System.out.println(sd.getDate(0).toString());
        // Set sidereal mode:
        sw.swe_set_sid_mode(SID_METHOD, 0, 0);

        // Some required variables:
        double[] cusps = new double[13];
        double[] acsc = new double[10];
        double[] xp = new double[6];
        StringBuffer serr = new StringBuffer();

        // Print input details:
        System.out.println("Date (YYYY/MM/DD): " + sd.getYear() + "/" + sd.getMonth() + "/" + sd.getDay() + ", "
                + toHMS(sd.getHour()));
        System.out.println("Jul. day:  " + sd.getJulDay());
        System.out.println("DeltaT:    " + sd.getDeltaT() * 24 * 3600 + " sec.");
        System.out.println("Location:  " + toDMS(Math.abs(longitude)) + (longitude > 0 ? "E" : "W") + " / "
                + toDMS(Math.abs(latitude)) + (latitude > 0 ? "N" : "S"));

        // Get and print ayanamsa value for info:
        double ayanamsa = sw.swe_get_ayanamsa_ut(sd.getJulDay());
        System.out.println("Ayanamsa:  " + toDMS(ayanamsa) + " (" + sw.swe_get_ayanamsa_name(SID_METHOD) + ")");

        // Get and print lagna:
        int flags = SweConst.SEFLG_SIDEREAL;
        int result = sw.swe_houses(sd.getJulDay(), flags, latitude, longitude, 'P', cusps, acsc);
        System.out.println("Ascendant: " + toDMS(acsc[0]) + "\n");

        int ascSign = (int) (acsc[0] / 30) + 1;

        // Calculate all planets:
        int[] planets = { SweConst.SE_SUN, SweConst.SE_MOON, SweConst.SE_MARS, SweConst.SE_MERCURY, SweConst.SE_JUPITER,
                SweConst.SE_VENUS, SweConst.SE_SATURN, SweConst.SE_TRUE_NODE }; // Some
        // systems
        // prefer
        // SE_MEAN_NODE

        flags = SweConst.SEFLG_SWIEPH | // fastest method, requires data files
                SweConst.SEFLG_SIDEREAL | // sidereal zodiac
                SweConst.SEFLG_NONUT | // will be set automatically for sidereal
                // calculations, if not set here
                SweConst.SEFLG_SPEED; // to determine retrograde vs. direct
        // motion
        int sign;
        int house;
        boolean retrograde = false;

        for (int p = 0; p < planets.length; p++) {
            int planet = planets[p];
            String planetName = sw.swe_get_planet_name(planet);
            int ret = sw.swe_calc_ut(sd.getJulDay(), planet, flags, xp, serr);

            if (ret != flags) {
                if (serr.length() > 0) {
                    System.err.println("Warning: " + serr);
                } else {
                    System.err.println(String.format("Warning, different flags used (0x%x)", ret));
                }
            }

            sign = (int) (xp[0] / 30) + 1;
            house = (sign + 12 - ascSign) % 12 + 1;
            retrograde = (xp[3] < 0);

            System.out.printf("%-12s: %s %c; sign: %2d; %s in house %2d\n", planetName, toDMS(xp[0]),
                    (retrograde ? 'R' : 'D'), sign, toDMS(xp[0] % 30), house);
            if(p == 0){
                prevDay_sun_long = xp[0];
                prevDay_sun_sign = sign;
            }
            if(p == 1){
                prevDay_moon_long = xp[0];
                prevDay_moon_sign = sign;
            }

        }
        // KETU
        xp[0] = (xp[0] + 180.0) % 360;
        String planetName = "Ketu (true)";

        sign = (int) (xp[0] / 30) + 1;
        house = (sign + 12 - ascSign) % 12 + 1;

        System.out.printf("%-12s: %s %c; sign: %2d; %s in house %2d\n", planetName, toDMS(xp[0]),
                (retrograde ? 'R' : 'D'), sign, toDMS(xp[0] % 30), house);
    }

    private void calculateNextDayHouseAndSignOfPlanet(int y, int m, int d, String rise, double lng, double lat) {
        long sunrise_hour = Long.parseLong(rise.substring(0,2));
        long sunrise_min = Long.parseLong(rise.substring(3,5));
        int year = y;
        int month = m;
        int day = d;
        double longitude =lng; // Guntur
        double latitude = lat;
        double hour = sunrise_hour + (sunrise_min / 60.) - 5.5; // IST



        // Use ... new SwissEph("/path/to/your/ephemeris/data/files"); when
        // your data files don't reside somewhere in the paths defined in
        // SweConst.SE_EPHE_PATH, which is ".:./ephe:/users/ephe2/:/users/ephe/"
        // currently.
        SwissEph sw =  new SwissEph(getApplicationContext().getFilesDir() + File.separator + "/ephe");
        SweDate sd = new SweDate(year, month, day, hour);
        System.out.println(sd.getDate(0).toString());
        // Set sidereal mode:
        sw.swe_set_sid_mode(SID_METHOD, 0, 0);

        // Some required variables:
        double[] cusps = new double[13];
        double[] acsc = new double[10];
        double[] xp = new double[6];
        StringBuffer serr = new StringBuffer();

        // Print input details:
        System.out.println("Date (YYYY/MM/DD): " + sd.getYear() + "/" + sd.getMonth() + "/" + sd.getDay() + ", "
                + toHMS(sd.getHour()));
        System.out.println("Jul. day:  " + sd.getJulDay());
        System.out.println("DeltaT:    " + sd.getDeltaT() * 24 * 3600 + " sec.");
        System.out.println("Location:  " + toDMS(Math.abs(longitude)) + (longitude > 0 ? "E" : "W") + " / "
                + toDMS(Math.abs(latitude)) + (latitude > 0 ? "N" : "S"));

        // Get and print ayanamsa value for info:
        double ayanamsa = sw.swe_get_ayanamsa_ut(sd.getJulDay());
        System.out.println("Ayanamsa:  " + toDMS(ayanamsa) + " (" + sw.swe_get_ayanamsa_name(SID_METHOD) + ")");

        // Get and print lagna:
        int flags = SweConst.SEFLG_SIDEREAL;
        int result = sw.swe_houses(sd.getJulDay(), flags, latitude, longitude, 'P', cusps, acsc);
        System.out.println("Ascendant: " + toDMS(acsc[0]) + "\n");

        int ascSign = (int) (acsc[0] / 30) + 1;

        // Calculate all planets:
        int[] planets = { SweConst.SE_SUN, SweConst.SE_MOON, SweConst.SE_MARS, SweConst.SE_MERCURY, SweConst.SE_JUPITER,
                SweConst.SE_VENUS, SweConst.SE_SATURN, SweConst.SE_TRUE_NODE }; // Some
        // systems
        // prefer
        // SE_MEAN_NODE

        flags = SweConst.SEFLG_SWIEPH | // fastest method, requires data files
                SweConst.SEFLG_SIDEREAL | // sidereal zodiac
                SweConst.SEFLG_NONUT | // will be set automatically for sidereal
                // calculations, if not set here
                SweConst.SEFLG_SPEED; // to determine retrograde vs. direct
        // motion
        int sign;
        int house;
        boolean retrograde = false;

        for (int p = 0; p < planets.length; p++) {
            int planet = planets[p];
            String planetName = sw.swe_get_planet_name(planet);
            int ret = sw.swe_calc_ut(sd.getJulDay(), planet, flags, xp, serr);

            if (ret != flags) {
                if (serr.length() > 0) {
                    System.err.println("Warning: " + serr);
                } else {
                    System.err.println(String.format("Warning, different flags used (0x%x)", ret));
                }
            }

            sign = (int) (xp[0] / 30) + 1;
            house = (sign + 12 - ascSign) % 12 + 1;
            retrograde = (xp[3] < 0);

            System.out.printf("%-12s: %s %c; sign: %2d; %s in house %2d\n", planetName, toDMS(xp[0]),
                    (retrograde ? 'R' : 'D'), sign, toDMS(xp[0] % 30), house);
            if(p == 0){
                nextDay_sun_long = xp[0];
                nextDay_sun_sign = sign;
            }
            if(p == 1){
                nextDay_moon_long = xp[0];
                nextDay_moon_sign = sign;
            }

        }
        // KETU
        xp[0] = (xp[0] + 180.0) % 360;
        String planetName = "Ketu (true)";

        sign = (int) (xp[0] / 30) + 1;
        house = (sign + 12 - ascSign) % 12 + 1;

        System.out.printf("%-12s: %s %c; sign: %2d; %s in house %2d\n", planetName, toDMS(xp[0]),
                (retrograde ? 'R' : 'D'), sign, toDMS(xp[0] % 30), house);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void calculateNakshatram(double moon_long, double nextDay_moon_long, double prevDay_moon_long, String sunrise, String prevDaySunrise) {
        String duration_start,duration_end;
        double starting_time_of_naksahtra =0.0;
        int nakshatram_start_day = 0;
        double diff = (moon_long*60)/800;
        int nakshatram = (int)Math.ceil(diff);
        System.out.println("Nakshatram "+nakshatram);
        double daily_motion_moon = nextDay_moon_long - moon_long;
        if(daily_motion_moon < 0.0){
            daily_motion_moon += 360.0;
        }
        double prev_day_daily_motion_moon = moon_long - prevDay_moon_long;
        if(prev_day_daily_motion_moon < 0.0){
            prev_day_daily_motion_moon += 360.0;
        }
        double[] nakshatram_mul = {13.333,26.666,40,53.333,66.666,80,93.333,106.666,120,133.333,146.666,160,173.333,186.666,200,213.333,226.666,240,253.333,266.666,280,293.333,306.666,320,333.333,346.666,360};
        double rd;
        if(nakshatram != 0) {
            rd = nakshatram_mul[nakshatram - 1] - moon_long;
       }else{
            rd = nakshatram_mul[26] - moon_long;
       }

       // double rd = nakshatram_mul[nakshatram - 1] - moon_long;
        double end_time = ((rd*60*24)/(daily_motion_moon*60));
        long sunrise_hour = Long.parseLong(sunrise.substring(0,2));
        long sunrise_min = Long.parseLong(sunrise.substring(3,5));
        double nakshatram_end_time = (sunrise_hour+((double)sunrise_min/60))+end_time;
            double prev_diff = (prevDay_moon_long*60)/800;
            int prev_day_nakshatram = (int)Math.ceil(prev_diff);
            if(prev_day_nakshatram != nakshatram) {
            double prev_rd;
            if (nakshatram != 1) {
                prev_rd = nakshatram_mul[nakshatram - 2] - prevDay_moon_long;
            } else if (nakshatram == 0) {
                prev_rd = nakshatram_mul[25] - prevDay_moon_long;
            } else {
                prev_rd = nakshatram_mul[26] - prevDay_moon_long;
            }

            double prev_end_time = ((prev_rd * 24) / (prev_day_daily_motion_moon));
            long prev_sunrise_hour = Long.parseLong(prevDaySunrise.substring(0, 2));
            long prev_sunrise_min = Long.parseLong(prevDaySunrise.substring(3, 5));
            double prev_nakshatram_end_time = (prev_sunrise_hour + ((double) prev_sunrise_min / 60)) + prev_end_time;
                starting_time_of_naksahtra = prev_nakshatram_end_time;
            System.out.println("Nakshatram starting time "+toDMS(prev_nakshatram_end_time));
                String start_deg = toDMS(prev_nakshatram_end_time);
                String hours = start_deg.substring(0,start_deg.lastIndexOf("°"));
                String str_hours = hours.replaceAll("\\s", "");
                String minutes = start_deg.substring(start_deg.lastIndexOf("°")+1,start_deg.lastIndexOf("'"));
                cal.add(Calendar.DATE,-1);
                duration_start = cal.get(Calendar.DATE)+"-"+(cal.get(Calendar.MONTH)+1)+"-"+cal.get(Calendar.YEAR)+" "+str_hours+":"+minutes+":00";
                System.out.println("duration start "+duration_start);
                nakshatram_start_day = cal.get(Calendar.DATE);
                cal.add(Calendar.DATE,1);


        }else{
            double starting_time = calculateAdhikaNakshatra(prevDay_moon_long);
                String prev_star_start_time = toDMS(starting_time);
                String degrees = prev_star_start_time.substring(0,prev_star_start_time.lastIndexOf("°"));
                String str = degrees.replaceAll("\\s", "");
                long deg = Long.parseLong(str);
                if(deg>24){
                    deg-=24;
                    StringBuffer buf = new StringBuffer(prev_star_start_time);
                    int start = 0;
                    int end = prev_star_start_time.lastIndexOf("°");
                    buf.replace(start, end, String.valueOf(deg));
                    cal.add(Calendar.DATE,-1);
                    System.out.println("Adhika Nakshatram start time "+buf+" "+(cal.get(Calendar.DATE)));
                    star_time.setText(nakshatras[nakshatram-1]+" "+buf.toString()+" "+(cal.get(Calendar.DATE)));
                    String start_deg = buf.toString();
                    String hours = start_deg.substring(0,start_deg.lastIndexOf("°"));
                    String str_hours = hours.replaceAll("\\s", "");
                    String minutes = start_deg.substring(start_deg.lastIndexOf("°")+1,start_deg.lastIndexOf("'"));
                    duration_start = cal.get(Calendar.DATE)+"-"+(cal.get(Calendar.MONTH)+1)+"-"+cal.get(Calendar.YEAR)+" "+str_hours+":"+minutes+":00";
                    System.out.println("duration start "+duration_start);
                    nakshatram_start_day = cal.get(Calendar.DATE);
                    cal.add(Calendar.DATE,1);
                    starting_time_of_naksahtra = Double.parseDouble(str_hours)+((Double.parseDouble(minutes))/60);
                }
                else {

                    System.out.println("Adhika Nakshatram start time " + toDMS(nakshatram_end_time));
                    String start_deg = toDMS(nakshatram_end_time);
                    String hours = start_deg.substring(0,start_deg.lastIndexOf("°"));
                    String str_hours = hours.replaceAll("\\s", "");
                    String minutes = start_deg.substring(start_deg.lastIndexOf("°")+1,start_deg.lastIndexOf("'"));
                    duration_start = cal.get(Calendar.DATE)+"-"+(cal.get(Calendar.MONTH)+1)+"-"+cal.get(Calendar.YEAR)+" "+str_hours+":"+minutes+":00";
                    System.out.println("duration start "+duration_start);
                    nakshatram_start_day = cal.get(Calendar.DATE);
                    starting_time_of_naksahtra = Double.parseDouble(str_hours)+((Double.parseDouble(minutes))/60);
                }
        }

       /* if(prev_nakshatram_end_time > 24.0){
            prev_nakshatram_end_time = nakshatram_end_time - (2+(32/60)+(24/3600));
            cal.add(Calendar.DATE,-1);
            System.out.println("Nakshatram starting time "+toDMS(prev_nakshatram_end_time)+" "+(cal.get(Calendar.DATE)));
            cal.add(Calendar.DATE,1);
        }else{
            System.out.println("Nakshatram starting time "+toDMS(prev_nakshatram_end_time));
        }*/
        //calculatePrevNakshatram(prevDay_moon_long,moon_long,prevDaySunrise);

        String star_endTime = toDMS(nakshatram_end_time);
        String degrees = star_endTime.substring(0,star_endTime.lastIndexOf("°"));
        String str = degrees.replaceAll("\\s", "");
        long deg = Long.parseLong(str);
        if(deg>24){
            deg-=24;
            StringBuffer buf = new StringBuffer(star_endTime);
            int start = 0;
            int end = star_endTime.lastIndexOf("°");
            buf.replace(start, end, String.valueOf(deg));
            cal.add(Calendar.DATE,1);
            System.out.println("Nakshatram end time "+buf+" "+(cal.get(Calendar.DATE)));

            String start_deg = buf.toString();
            String hours = start_deg.substring(0,start_deg.lastIndexOf("°"));
            String str_hours = hours.replaceAll("\\s", "");
            String minutes = start_deg.substring(start_deg.lastIndexOf("°")+1,start_deg.lastIndexOf("'"));
            duration_end = cal.get(Calendar.DATE)+"-"+(cal.get(Calendar.MONTH)+1)+"-"+cal.get(Calendar.YEAR)+" "+str_hours+":"+minutes+":00";
            System.out.println("duration end "+duration_end);
            star_time.setText(nakshatras[nakshatram-1]+" "+str_hours+":"+minutes+" "+(cal.get(Calendar.DATE)));
            cal.add(Calendar.DATE,-1);
        }
        else {
            System.out.println("Nakshatram end time " + toDMS(nakshatram_end_time));

            String start_deg =toDMS(nakshatram_end_time);
            String hours = start_deg.substring(0,start_deg.lastIndexOf("°"));
            String str_hours = hours.replaceAll("\\s", "");
            String mins = start_deg.substring(start_deg.lastIndexOf("°")+1,start_deg.lastIndexOf("'"));
            star_time.setText(nakshatras[nakshatram-1]+" "+str_hours+":"+mins);
            String minutes = star_endTime.substring(star_endTime.lastIndexOf("°")+1,star_endTime.lastIndexOf("'"));
            duration_end = cal.get(Calendar.DATE)+"-"+(cal.get(Calendar.MONTH)+1)+"-"+cal.get(Calendar.YEAR)+" "+str+":"+minutes+":00";
            System.out.println("duration end "+duration_end);
        }
        double durationOf_Nakshatram = findDifference(duration_start,duration_end);
        calculateAmruthaKalam(nakshatram,starting_time_of_naksahtra,durationOf_Nakshatram,nakshatram_start_day,sunrise);
        calculateVarjyam(nakshatram,starting_time_of_naksahtra,durationOf_Nakshatram,nakshatram_start_day);

    }

    private void calculateVarjyam(int nakshatram, double starting_time_of_naksahtra, double durationOf_nakshatram, int nakshatram_start_day) {
        double[] varjyam_mul = {20,9.6,12,16,5.6,8.4,12,8,12.8,12,8,7.2,8.4,8,5.6,5.6,4,5.6,8,9.6,8,4,4,7.2,6.4,9.6,12};
        double varjyam_start_time = 0.0;
        varjyam_start_time = starting_time_of_naksahtra + (((varjyam_mul[nakshatram - 1]) / 24) * durationOf_nakshatram);

        double duration_of_varjyam = durationOf_nakshatram*(1.6/24);
        double varjyam_end_time = varjyam_start_time + duration_of_varjyam;
        if(nakshatram_start_day == cal.get(Calendar.DATE)){
            if(varjyam_start_time > 24.0){
                varjyam_start_time -=24.0;
                varjyam_end_time -= 24.0;
                cal.add(Calendar.DATE,1);
                String start_deg =toDMS(varjyam_start_time);
                String hours = start_deg.substring(0,start_deg.lastIndexOf("°"));
                String str_hours = hours.replaceAll("\\s", "");
                String mins = start_deg.substring(start_deg.lastIndexOf("°")+1,start_deg.lastIndexOf("'"));
                System.out.println("varjyam start time "+str_hours+":"+mins+" "+cal.get(Calendar.DATE));
                String end_deg =toDMS(varjyam_end_time);
                String endhours = end_deg.substring(0,end_deg.lastIndexOf("°"));
                String end_hours = endhours.replaceAll("\\s", "");
                String minutes = end_deg.substring(end_deg.lastIndexOf("°")+1,end_deg.lastIndexOf("'"));
                System.out.println("varjyam end time "+end_hours+":"+minutes+" "+cal.get(Calendar.DATE));
                varjyam.setText(str_hours+":"+mins+" "+cal.get(Calendar.DATE)+" to "+end_hours+":"+minutes+" "+cal.get(Calendar.DATE));
                cal.add(Calendar.DATE,-1);
            }else{
                System.out.println("varjyam start time "+toDMS(varjyam_start_time));
                System.out.println("varjyam end time "+toDMS(varjyam_end_time));
                String start_deg =toDMS(varjyam_start_time);
                String hours = start_deg.substring(0,start_deg.lastIndexOf("°"));
                String str_hours = hours.replaceAll("\\s", "");
                String mins = start_deg.substring(start_deg.lastIndexOf("°")+1,start_deg.lastIndexOf("'"));
                String end_deg =toDMS(varjyam_end_time);
                String endhours = end_deg.substring(0,end_deg.lastIndexOf("°"));
                String end_hours = endhours.replaceAll("\\s", "");
                String minutes = end_deg.substring(end_deg.lastIndexOf("°")+1,end_deg.lastIndexOf("'"));
                varjyam.setText(str_hours+":"+mins+" to "+end_hours+":"+minutes);
            }


        }else{
            if(varjyam_start_time > 24.0){
                varjyam_start_time -=24.0;
                varjyam_end_time -= 24.0;
                //cal.add(Calendar.DATE,1);
                System.out.println("varjyam start time "+toDMS(varjyam_start_time));
                System.out.println("varjyam end time "+toDMS(varjyam_end_time));
                String start_deg =toDMS(varjyam_start_time);
                String hours = start_deg.substring(0,start_deg.lastIndexOf("°"));
                String str_hours = hours.replaceAll("\\s", "");
                String mins = start_deg.substring(start_deg.lastIndexOf("°")+1,start_deg.lastIndexOf("'"));
                String end_deg =toDMS(varjyam_end_time);
                String endhours = end_deg.substring(0,end_deg.lastIndexOf("°"));
                String end_hours = endhours.replaceAll("\\s", "");
                String minutes = end_deg.substring(end_deg.lastIndexOf("°")+1,end_deg.lastIndexOf("'"));
                varjyam.setText(str_hours+":"+mins+" to "+end_hours+":"+minutes);
                //cal.add(Calendar.DATE,-1);
            }else{
                System.out.println("varjyam start time "+toDMS(varjyam_start_time));
                System.out.println("varjyam end time "+toDMS(varjyam_end_time));
                String start_deg =toDMS(varjyam_start_time);
                String hours = start_deg.substring(0,start_deg.lastIndexOf("°"));
                String str_hours = hours.replaceAll("\\s", "");
                String mins = start_deg.substring(start_deg.lastIndexOf("°")+1,start_deg.lastIndexOf("'"));
                String end_deg =toDMS(varjyam_end_time);
                String endhours = end_deg.substring(0,end_deg.lastIndexOf("°"));
                String end_hours = endhours.replaceAll("\\s", "");
                String minutes = end_deg.substring(end_deg.lastIndexOf("°")+1,end_deg.lastIndexOf("'"));
                varjyam.setText(str_hours+":"+mins+" to "+end_hours+":"+minutes);
            }
        }
        if(nakshatram == 19) {
            calculateMoolaVarjyam(nakshatram, starting_time_of_naksahtra, durationOf_nakshatram, nakshatram_start_day);
        }
    }

    private void calculateMoolaVarjyam(int nakshatram, double starting_time_of_naksahtra, double durationOf_nakshatram, int nakshatram_start_day) {
        double varjyam_start_time = 0.0;
        varjyam_start_time = starting_time_of_naksahtra + ((22.4 / 24) * durationOf_nakshatram);
       // calculateMoolaVarjyam(nakshatram, starting_time_of_naksahtra,  durationOf_nakshatram, nakshatram_start_day);
        double duration_of_varjyam = durationOf_nakshatram*(1.6/24);
        double varjyam_end_time = varjyam_start_time + duration_of_varjyam;
        if(nakshatram_start_day == cal.get(Calendar.DATE)){
            if(varjyam_start_time > 24.0){
                varjyam_start_time -=24.0;
                varjyam_end_time -= 24.0;
                cal.add(Calendar.DATE,1);
                String start_deg =toDMS(varjyam_start_time);
                String hours = start_deg.substring(0,start_deg.lastIndexOf("°"));
                String str_hours = hours.replaceAll("\\s", "");
                String mins = start_deg.substring(start_deg.lastIndexOf("°")+1,start_deg.lastIndexOf("'"));
                System.out.println("varjyam start time "+str_hours+":"+mins+" "+cal.get(Calendar.DATE));
                String end_deg =toDMS(varjyam_end_time);
                String endhours = end_deg.substring(0,end_deg.lastIndexOf("°"));
                String end_hours = endhours.replaceAll("\\s", "");
                String minutes = end_deg.substring(end_deg.lastIndexOf("°")+1,end_deg.lastIndexOf("'"));
                System.out.println("varjyam end time "+end_hours+":"+minutes+" "+cal.get(Calendar.DATE));
                varjyam.append(" and "+str_hours+":"+mins+" "+cal.get(Calendar.DATE)+" to "+end_hours+":"+minutes+" "+cal.get(Calendar.DATE));
                cal.add(Calendar.DATE,-1);

            }else{
                System.out.println("varjyam start time "+toDMS(varjyam_start_time));
                System.out.println("varjyam end time "+toDMS(varjyam_end_time));
                String start_deg =toDMS(varjyam_start_time);
                String hours = start_deg.substring(0,start_deg.lastIndexOf("°"));
                String str_hours = hours.replaceAll("\\s", "");
                String mins = start_deg.substring(start_deg.lastIndexOf("°")+1,start_deg.lastIndexOf("'"));
                String end_deg =toDMS(varjyam_end_time);
                String endhours = end_deg.substring(0,end_deg.lastIndexOf("°"));
                String end_hours = endhours.replaceAll("\\s", "");
                String minutes = end_deg.substring(end_deg.lastIndexOf("°")+1,end_deg.lastIndexOf("'"));
                varjyam.append(" and "+str_hours+":"+mins+" to "+end_hours+":"+minutes);
            }


        }else{
            if(varjyam_start_time > 24.0){
                varjyam_start_time -=24.0;
                varjyam_end_time -= 24.0;
                //cal.add(Calendar.DATE,1);
                System.out.println("varjyam start time "+toDMS(varjyam_start_time));
                System.out.println("varjyam end time "+toDMS(varjyam_end_time));
                String start_deg =toDMS(varjyam_start_time);
                String hours = start_deg.substring(0,start_deg.lastIndexOf("°"));
                String str_hours = hours.replaceAll("\\s", "");
                String mins = start_deg.substring(start_deg.lastIndexOf("°")+1,start_deg.lastIndexOf("'"));
                String end_deg =toDMS(varjyam_end_time);
                String endhours = end_deg.substring(0,end_deg.lastIndexOf("°"));
                String end_hours = endhours.replaceAll("\\s", "");
                String minutes = end_deg.substring(end_deg.lastIndexOf("°")+1,end_deg.lastIndexOf("'"));
                varjyam.append(" and "+str_hours+":"+mins+" to "+end_hours+":"+minutes);
                //cal.add(Calendar.DATE,-1);
            }else{
                System.out.println("varjyam start time "+toDMS(varjyam_start_time));
                System.out.println("varjyam end time "+toDMS(varjyam_end_time));
                String start_deg =toDMS(varjyam_start_time);
                String hours = start_deg.substring(0,start_deg.lastIndexOf("°"));
                String str_hours = hours.replaceAll("\\s", "");
                String mins = start_deg.substring(start_deg.lastIndexOf("°")+1,start_deg.lastIndexOf("'"));
                String end_deg =toDMS(varjyam_end_time);
                String endhours = end_deg.substring(0,end_deg.lastIndexOf("°"));
                String end_hours = endhours.replaceAll("\\s", "");
                String minutes = end_deg.substring(end_deg.lastIndexOf("°")+1,end_deg.lastIndexOf("'"));
                varjyam.append(" and "+str_hours+":"+mins+" to "+end_hours+":"+minutes);
            }
        }
    }

    private void calculateAmruthaKalam(int nakshatram, double starting_time_of_naksahtra, double durationOf_nakshatram,int nakshatram_start_day,String sunrise) {
        double[] amrutha_mul = {16.8,19.2,21.6,20.8,15.2,14,21.6,17.6,22.4,21.6,17.6,16.8,18,17.6,15.2,15.2,13.6,15.2,17.6,19.2,17.6,13.6,13.6,16.8,16,19.2,21.6};
        double amrutha_start_time = 0.0;
        amrutha_start_time = starting_time_of_naksahtra + (((amrutha_mul[nakshatram - 1]) / 24) * durationOf_nakshatram);

        double duration_of_amrutha = durationOf_nakshatram*(1.6/24);
        double amrutha_end_time = amrutha_start_time + duration_of_amrutha;
        if(nakshatram_start_day == cal.get(Calendar.DATE)){
            if(amrutha_start_time > 24.0){
                amrutha_start_time -=24.0;
                amrutha_end_time -= 24.0;
                cal.add(Calendar.DATE,1);
                System.out.println("Amrutha kalam start time "+toDMS(amrutha_start_time)+" "+cal.get(Calendar.DATE));
                System.out.println("Amrutha kalam end time "+toDMS(amrutha_end_time)+" "+cal.get(Calendar.DATE));
                String start_deg =toDMS(amrutha_start_time);
                String hours = start_deg.substring(0,start_deg.lastIndexOf("°"));
                String str_hours = hours.replaceAll("\\s", "");
                String mins = start_deg.substring(start_deg.lastIndexOf("°")+1,start_deg.lastIndexOf("'"));
                String end_deg =toDMS(amrutha_end_time);
                String endhours = end_deg.substring(0,end_deg.lastIndexOf("°"));
                String end_hours = endhours.replaceAll("\\s", "");
                String minutes = end_deg.substring(end_deg.lastIndexOf("°")+1,end_deg.lastIndexOf("'"));
                amrutha_kalam.setText(str_hours+":"+mins+" "+cal.get(Calendar.DATE)+" to "+end_hours+":"+minutes+" "+cal.get(Calendar.DATE));
                cal.add(Calendar.DATE,-1);
            }else{
                System.out.println("Amrutha kalam start time "+toDMS(amrutha_start_time));
                System.out.println("Amrutha kalam end time "+toDMS(amrutha_end_time));
                String start_deg =toDMS(amrutha_start_time);
                String hours = start_deg.substring(0,start_deg.lastIndexOf("°"));
                String str_hours = hours.replaceAll("\\s", "");
                String mins = start_deg.substring(start_deg.lastIndexOf("°")+1,start_deg.lastIndexOf("'"));
                String end_deg =toDMS(amrutha_end_time);
                String endhours = end_deg.substring(0,end_deg.lastIndexOf("°"));
                String end_hours = endhours.replaceAll("\\s", "");
                String minutes = end_deg.substring(end_deg.lastIndexOf("°")+1,end_deg.lastIndexOf("'"));
                amrutha_kalam.setText(str_hours+":"+mins+" to "+end_hours+":"+minutes);
            }


        }else{
            if(amrutha_start_time > 24.0){
                amrutha_start_time -=24.0;
                amrutha_end_time -= 24.0;
                //cal.add(Calendar.DATE,1);
                System.out.println("Amrutha kalam start time "+toDMS(amrutha_start_time));
                System.out.println("Amrutha kalam end time "+toDMS(amrutha_end_time));
                String start_deg =toDMS(amrutha_start_time);
                String hours = start_deg.substring(0,start_deg.lastIndexOf("°"));
                String str_hours = hours.replaceAll("\\s", "");
                String mins = start_deg.substring(start_deg.lastIndexOf("°")+1,start_deg.lastIndexOf("'"));
                String end_deg =toDMS(amrutha_end_time);
                String endhours = end_deg.substring(0,end_deg.lastIndexOf("°"));
                String end_hours = endhours.replaceAll("\\s", "");
                String minutes = end_deg.substring(end_deg.lastIndexOf("°")+1,end_deg.lastIndexOf("'"));
                amrutha_kalam.setText(str_hours+":"+mins+" to "+end_hours+":"+minutes);
                //cal.add(Calendar.DATE,-1);
            }else{
                System.out.println("Amrutha kalam start time "+toDMS(amrutha_start_time));
                System.out.println("Amrutha kalam end time "+toDMS(amrutha_end_time));
                String start_deg =toDMS(amrutha_start_time);
                String hours = start_deg.substring(0,start_deg.lastIndexOf("°"));
                String str_hours = hours.replaceAll("\\s", "");
                String mins = start_deg.substring(start_deg.lastIndexOf("°")+1,start_deg.lastIndexOf("'"));
                String end_deg =toDMS(amrutha_end_time);
                String endhours = end_deg.substring(0,end_deg.lastIndexOf("°"));
                String end_hours = endhours.replaceAll("\\s", "");
                String minutes = end_deg.substring(end_deg.lastIndexOf("°")+1,end_deg.lastIndexOf("'"));
                amrutha_kalam.setText(str_hours+":"+mins+" to "+end_hours+":"+minutes);
            }
        }


    }

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    private double findDifference(String start_date,
                                  String end_date)
    {
        // SimpleDateFormat converts the
        // string format to date object
        SimpleDateFormat sdf
                = new SimpleDateFormat(
                "dd-MM-yyyy HH:mm:ss");
        double duration = 0.0;

        // Try Class
        try {

            // parse method is used to parse
            // the text from a string to
            // produce the date
            Date d1 = sdf.parse(start_date);
            Date d2 = sdf.parse(end_date);

            // Calucalte time difference
            // in milliseconds
            long difference_In_Time
                    = d2.getTime() - d1.getTime();

            // Calucalte time difference in seconds,
            // minutes, hours, years, and days
            long difference_In_Seconds
                    = TimeUnit.MILLISECONDS
                    .toSeconds(difference_In_Time)
                    % 60;

            long difference_In_Minutes
                    = TimeUnit
                    .MILLISECONDS
                    .toMinutes(difference_In_Time)
                    % 60;


            long difference_In_Hours
                    = TimeUnit
                    .MILLISECONDS
                    .toHours(difference_In_Time)
                    % 24;
            long difference_In_Days
                    = TimeUnit
                    .MILLISECONDS
                    .toDays(difference_In_Time)
                    % 365;

            long difference_In_Years
                    = TimeUnit
                    .MILLISECONDS
                    .toDays(difference_In_Time)
                    / 365l;

            // Print the date difference in
            // years, in days, in hours, in
            // minutes, and in seconds
            System.out.print(
                    "Difference"
                            + " between two dates is: ");

            // Print result
            System.out.println(
                    difference_In_Years
                            + " years, "
                            + difference_In_Days
                            + " days, "
                            + difference_In_Hours
                            + " hours, "
                            + difference_In_Minutes
                            + " minutes, "
                            + difference_In_Seconds
                            + " seconds");
            if(difference_In_Days<0){
                difference_In_Days = difference_In_Days*(-1);
            }
            if(difference_In_Hours<0){
                difference_In_Hours = difference_In_Hours*(-1);
                System.out.println("difference in hoursssssss "+difference_In_Hours);
            }
            if(difference_In_Minutes<0){
                difference_In_Minutes = difference_In_Minutes*(-1);
            }
            if(difference_In_Seconds<0){
                difference_In_Seconds = difference_In_Seconds*(-1);
            }
            duration = (difference_In_Days*24)+(difference_In_Hours)+((double)(difference_In_Minutes/60))+((double)difference_In_Seconds/3600);

        }
        catch (ParseException e) {
            e.printStackTrace();
        }
        return duration;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private double calculateAdhikaNakshatra(double prevDay_moon_long) {
        cal.add(Calendar.DATE,-2);
        SunTimes adhikaPrevious = SunTimes.compute()
                .on(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DATE))   // set a date
                .at(lat, lng)   // set a location
                .execute();

        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        String adhikaSunrise = dateFormat.format(Date.from(adhikaPrevious.getRise().toInstant()));

        calculateAdhikaPreviousDayHouseAndSignOfPlanet(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH)+1,cal.get(Calendar.DATE),adhikaSunrise,lng,lat);



        double diff = (adhika_moon_long*60)/800;
        int nakshatram = (int)Math.ceil(diff);
        System.out.println("Nakshatram "+nakshatram);
        double daily_motion_moon = prevDay_moon_long - adhika_moon_long;
        if(daily_motion_moon < 0.0){
            daily_motion_moon += 360.0;
        }
        double[] nakshatram_mul = {13.333,26.666,40,53.333,66.666,80,93.333,106.666,120,133.333,146.666,160,173.333,186.666,200,213.333,226.666,240,253.333,266.666,280,293.333,306.666,320,333.333,346.666,360};
        double rd;
        if(nakshatram != 0) {
            rd = nakshatram_mul[nakshatram - 1] - adhika_moon_long;
        }else{
            rd = nakshatram_mul[26] - adhika_moon_long;
        }

        // double rd = nakshatram_mul[nakshatram - 1] - moon_long;
        double end_time = ((rd*60*24)/(daily_motion_moon*60));
        long sunrise_hour = Long.parseLong(adhikaSunrise.substring(0,2));
        long sunrise_min = Long.parseLong(adhikaSunrise.substring(3,5));
        double nakshatram_end_time = (sunrise_hour+((double)sunrise_min/60))+end_time;
        cal.add(Calendar.DATE,+2);
        return nakshatram_end_time;

    }

    private void calculateAdhikaPreviousDayHouseAndSignOfPlanet(int y, int m, int d, String rise, double lng, double lat) {
        long sunrise_hour = Long.parseLong(rise.substring(0,2));
        long sunrise_min = Long.parseLong(rise.substring(3,5));
        int year = y;
        int month = m;
        int day = d;
        double longitude =lng; // Guntur
        double latitude = lat;
        double hour = sunrise_hour + (sunrise_min / 60.) - 5.5; // IST



        // Use ... new SwissEph("/path/to/your/ephemeris/data/files"); when
        // your data files don't reside somewhere in the paths defined in
        // SweConst.SE_EPHE_PATH, which is ".:./ephe:/users/ephe2/:/users/ephe/"
        // currently.
        SwissEph sw =  new SwissEph(getApplicationContext().getFilesDir() + File.separator + "/ephe");
        SweDate sd = new SweDate(year, month, day, hour);
        System.out.println(sd.getDate(0).toString());
        // Set sidereal mode:
        sw.swe_set_sid_mode(SID_METHOD, 0, 0);

        // Some required variables:
        double[] cusps = new double[13];
        double[] acsc = new double[10];
        double[] xp = new double[6];
        StringBuffer serr = new StringBuffer();

        // Print input details:
        System.out.println("Date (YYYY/MM/DD): " + sd.getYear() + "/" + sd.getMonth() + "/" + sd.getDay() + ", "
                + toHMS(sd.getHour()));
        System.out.println("Jul. day:  " + sd.getJulDay());
        System.out.println("DeltaT:    " + sd.getDeltaT() * 24 * 3600 + " sec.");
        System.out.println("Location:  " + toDMS(Math.abs(longitude)) + (longitude > 0 ? "E" : "W") + " / "
                + toDMS(Math.abs(latitude)) + (latitude > 0 ? "N" : "S"));

        // Get and print ayanamsa value for info:
        double ayanamsa = sw.swe_get_ayanamsa_ut(sd.getJulDay());
        System.out.println("Ayanamsa:  " + toDMS(ayanamsa) + " (" + sw.swe_get_ayanamsa_name(SID_METHOD) + ")");

        // Get and print lagna:
        int flags = SweConst.SEFLG_SIDEREAL;
        int result = sw.swe_houses(sd.getJulDay(), flags, latitude, longitude, 'P', cusps, acsc);
        System.out.println("Ascendant: " + toDMS(acsc[0]) + "\n");

        int ascSign = (int) (acsc[0] / 30) + 1;

        // Calculate all planets:
        int[] planets = { SweConst.SE_SUN, SweConst.SE_MOON, SweConst.SE_MARS, SweConst.SE_MERCURY, SweConst.SE_JUPITER,
                SweConst.SE_VENUS, SweConst.SE_SATURN, SweConst.SE_TRUE_NODE }; // Some
        // systems
        // prefer
        // SE_MEAN_NODE

        flags = SweConst.SEFLG_SWIEPH | // fastest method, requires data files
                SweConst.SEFLG_SIDEREAL | // sidereal zodiac
                SweConst.SEFLG_NONUT | // will be set automatically for sidereal
                // calculations, if not set here
                SweConst.SEFLG_SPEED; // to determine retrograde vs. direct
        // motion
        int sign;
        int house;
        boolean retrograde = false;

        for (int p = 0; p < planets.length; p++) {
            int planet = planets[p];
            String planetName = sw.swe_get_planet_name(planet);
            int ret = sw.swe_calc_ut(sd.getJulDay(), planet, flags, xp, serr);

            if (ret != flags) {
                if (serr.length() > 0) {
                    System.err.println("Warning: " + serr);
                } else {
                    System.err.println(String.format("Warning, different flags used (0x%x)", ret));
                }
            }

            sign = (int) (xp[0] / 30) + 1;
            house = (sign + 12 - ascSign) % 12 + 1;
            retrograde = (xp[3] < 0);

            System.out.printf("%-12s: %s %c; sign: %2d; %s in house %2d\n", planetName, toDMS(xp[0]),
                    (retrograde ? 'R' : 'D'), sign, toDMS(xp[0] % 30), house);
            if(p == 1){
                adhika_moon_long = xp[0];
                adhika_moon_sign = sign;
            }

        }
        // KETU
        xp[0] = (xp[0] + 180.0) % 360;
        String planetName = "Ketu (true)";

        sign = (int) (xp[0] / 30) + 1;
        house = (sign + 12 - ascSign) % 12 + 1;

        System.out.printf("%-12s: %s %c; sign: %2d; %s in house %2d\n", planetName, toDMS(xp[0]),
                (retrograde ? 'R' : 'D'), sign, toDMS(xp[0] % 30), house);
    }

    private void calculatePrevNakshatram(double moon_long,double nextDay_moon_long,String sunrise) {
        double diff = (moon_long*60)/800;
        int nakshatram = (int)Math.ceil(diff);
        System.out.println("prev Nakshatram "+nakshatram);
        double daily_motion_moon = nextDay_moon_long - moon_long;
        if(daily_motion_moon < 0.0){
            daily_motion_moon += 360.0;
        }
        double[] nakshatram_mul = {13.333,26.666,40,53.333,66.666,80,93.333,106.666,120,133.333,146.666,160,173.333,186.666,200,213.333,226.666,240,253.333,266.666,280,293.333,306.666,320,333.333,346.666,360};
        double rd = nakshatram_mul[nakshatram-1]-moon_long;
        double end_time = ((rd*60*24)/(daily_motion_moon*60));
        long sunrise_hour = Long.parseLong(sunrise.substring(0,2));
        long sunrise_min = Long.parseLong(sunrise.substring(3,5));
        double nakshatram_end_time = (sunrise_hour+((double)sunrise_min/60))+end_time;



        String star_endTime = toDMS(nakshatram_end_time);
        String degrees = star_endTime.substring(0,star_endTime.lastIndexOf("°"));
        String str = degrees.replaceAll("\\s", "");
        long deg = Long.parseLong(str);
        if(deg>24){
            deg-=24;
            StringBuffer buf = new StringBuffer(star_endTime);
            int start = 0;
            int end = star_endTime.lastIndexOf("°");
            buf.replace(start, end, String.valueOf(deg));
            System.out.println("Nakshatram start time "+buf+" "+(cal.get(Calendar.DAY_OF_MONTH)));
            prevNakshTime = buf.toString();
        }
        else {
            System.out.println("Nakshatram start time " + toDMS(nakshatram_end_time));
            prevNakshTime = toDMS(nakshatram_end_time);
        }

    }

    private void calculateTithi(double sun_long, double moon_long,double nextDay_sun_long,double nextDay_moon_long,String sunrise,double prevDay_moon_long,double prevDay_sun_long,String prevDaySunrise) {
        String duration_start,duration_end;
        double diff = moon_long - sun_long;
        if(diff < 0.0){
            diff+= 360.0;
        }
        System.out.println("Sun longitude of today "+sun_long);
        tithi = (int) Math.ceil(diff/12);
        System.out.println("Tithi "+tithi);
        double daily_motion_sun = nextDay_sun_long - sun_long;
        double daily_motion_moon = nextDay_moon_long - moon_long;
        if(daily_motion_moon < 0.0){
            daily_motion_moon += 360.0;
        }
        if(daily_motion_sun < 0.0){
            daily_motion_sun += 360.0;
        }

        double rd = (tithi*12)-diff;

        double end_time = ((rd*60*24)/((daily_motion_moon*60)-(daily_motion_sun*60)));
        long sunrise_hour = Long.parseLong(sunrise.substring(0,2));
        long sunrise_min = Long.parseLong(sunrise.substring(3,5));
        double tithi_end_time = (sunrise_hour+((double)sunrise_min/60))+end_time;

        double tithi_start = calculateTithiStartingTime(prevDay_moon_long,prevDay_sun_long,prevDaySunrise,moon_long,sun_long);
        String tit_startTime = toDMS(tithi_start);
        String degrees_start = tit_startTime.substring(0,tit_startTime.lastIndexOf("°"));
        String str_start = degrees_start.replaceAll("\\s", "");
        long deg_start = Long.parseLong(str_start);
        if(deg_start>24){
            deg_start-=24;
            StringBuffer buf = new StringBuffer(tit_startTime);
            int start = 0;
            int end = tit_startTime.lastIndexOf("°");
            buf.replace(start, end, String.valueOf(deg_start));
            String start_deg = buf.toString();
            String hours = start_deg.substring(0,start_deg.lastIndexOf("°"));
            String str_hours = hours.replaceAll("\\s", "");
            String minutes = start_deg.substring(start_deg.lastIndexOf("°")+1,start_deg.lastIndexOf("'"));
            cal.add(Calendar.DATE,1);
            System.out.println("tithi start time "+buf+" "+(cal.get(Calendar.DATE)));
            duration_start = cal.get(Calendar.DATE) + "-" + (cal.get(Calendar.MONTH) + 1) + "-" + cal.get(Calendar.YEAR) + " " + str_hours + ":" + minutes + ":00";
            cal.add(Calendar.DATE,-1);
        }
        else {
            System.out.println("tithi start time "+toDMS(tithi_start));
            String start_deg = toDMS(tithi_start);
            String hours = start_deg.substring(0,start_deg.lastIndexOf("°"));
            String str_hours = hours.replaceAll("\\s", "");
            String minutes = start_deg.substring(start_deg.lastIndexOf("°")+1,start_deg.lastIndexOf("'"));
            duration_start = cal.get(Calendar.DATE) + "-" + (cal.get(Calendar.MONTH) + 1) + "-" + cal.get(Calendar.YEAR) + " " + str_hours + ":" + minutes + ":00";
        }

        String tit_endTime = toDMS(tithi_end_time);
        String degrees = tit_endTime.substring(0,tit_endTime.lastIndexOf("°"));
        String str = degrees.replaceAll("\\s", "");
        long deg = Long.parseLong(str);
        if(deg>24){
            deg-=24;
            StringBuffer buf = new StringBuffer(tit_endTime);
            int start = 0;
            int end = tit_endTime.lastIndexOf("°");
            buf.replace(start, end, String.valueOf(deg));
            String start_deg = buf.toString();
            String hours = start_deg.substring(0,start_deg.lastIndexOf("°"));
            String str_hours = hours.replaceAll("\\s", "");
            String minutes = start_deg.substring(start_deg.lastIndexOf("°")+1,start_deg.lastIndexOf("'"));
            duration_end = cal.get(Calendar.DATE) + "-" + (cal.get(Calendar.MONTH) + 1) + "-" + cal.get(Calendar.YEAR) + " " + str_hours + ":" + minutes + ":00";
            cal.add(Calendar.DATE,1);
            System.out.println("tithi end time "+buf+" "+(cal.get(Calendar.DATE)));

            tithi_time.setText(tithis[tithi-1]+" "+str_hours+":"+minutes+" "+(cal.get(Calendar.DATE)));
            cal.add(Calendar.DATE,-1);
        }
        else {
            System.out.println("tithi ending time "+toDMS(tithi_end_time));
            String start_deg = toDMS(tithi_end_time);
            String hours = start_deg.substring(0,start_deg.lastIndexOf("°"));
            String str_hours = hours.replaceAll("\\s", "");
            String minutes = start_deg.substring(start_deg.lastIndexOf("°")+1,start_deg.lastIndexOf("'"));
            duration_end = cal.get(Calendar.DATE) + "-" + (cal.get(Calendar.MONTH) + 1) + "-" + cal.get(Calendar.YEAR) + " " + str_hours + ":" + minutes + ":00";

            tithi_time.setText(tithis[tithi-1]+" "+str_hours+":"+minutes);
        }
        String[][] karanams ={
                {"Kimstugna" ,"Bava"},{"Baalava" ,"Kauvala"},{"Taitila" ,"Gara"},{"Vanija" ,"Vishti (Bhadra)"},
                {"Bava" ,"Baalava"},{"Kauvala" ,"Taitila"},{"Gara" ,"Vanija"},{"Vishti (Bhadra)", "Bava"}
                ,{"Baalava", "Kauvala"},{"Taitila" ,"Gara"},{"Vanija" ,"Vishti (Bhadra)"},{"Bava", "Baalava"},
                {"Kauvala" ,"Taitila"},{"Gara" ,"Vanija"},{"Vishti (Bhadra)" ,"Bava"},{"Baalava" ,"Kauvala"},
                {"Taitila", "Gara"},{"Vanija", "Vishti (Bhadra)"},{"Bava", "Baalava"},{"Kauvala" ,"Taitila"},{"Gara" ,"Vanija"},
                {"Vishti (Bhadra)", "Bava"},{"Baalava" ,"Kauvala"},{"Taitila" ,"Gara"},
                {"Vanija", "Vishti (Bhadra)"},{"Bava" ,"Baalava"},{"Kauvala" ,"Taitila"},
                {"Gara" ,"Vanija"},{"Vishti (Bhadra)", "Sakuna"},{"Chatushpada" ,"Naga"}
        };
        double tithi_duration = findDifference(duration_start,duration_end);
        double karanam_end_time = tithi_start+(tithi_duration/2);

        String first_karanam,second_karanam;
        if(tithi !=1){
            first_karanam = karanams[tithi-1][0];
            second_karanam = karanams[tithi-1][1];
        }else{
            first_karanam = karanams[29][0];
            second_karanam = karanams[29][1];
        }
        if(karanam_end_time>24.0){
            karanam_end_time -=24.0;
            cal.add(Calendar.DATE,1);
            String start_deg = toDMS(karanam_end_time);
            String hours = start_deg.substring(0,start_deg.lastIndexOf("°"));
            String str_hours = hours.replaceAll("\\s", "");
            String minutes = start_deg.substring(start_deg.lastIndexOf("°")+1,start_deg.lastIndexOf("'"));
            System.out.println("First Karanam "+first_karanam+" "+toDMS(karanam_end_time)+" "+cal.get(Calendar.DATE));
            System.out.println("Second Karanam "+second_karanam);
            karanam.setText(first_karanam+" "+str_hours+":"+minutes+" "+cal.get(Calendar.DATE)+" and "+second_karanam);
            cal.add(Calendar.DATE,-1);
        }else{
            String start_deg = toDMS(karanam_end_time);
            String hours = start_deg.substring(0,start_deg.lastIndexOf("°"));
            String str_hours = hours.replaceAll("\\s", "");
            String minutes = start_deg.substring(start_deg.lastIndexOf("°")+1,start_deg.lastIndexOf("'"));
            System.out.println("First Karanam "+first_karanam+" "+toDMS(karanam_end_time));
            System.out.println("Second Karanam "+second_karanam);
            karanam.setText(first_karanam+" "+str_hours+":"+minutes+" and "+second_karanam);
        }

    }

    private double calculateTithiStartingTime(double prevDay_moon_long, double prevDay_sun_long, String prevDaySunrise,double moon_long,double sun_long) {
        double diff = prevDay_moon_long - prevDay_sun_long;
        if(diff < 0.0){
            diff+= 360.0;
        }
        int tithi = (int) Math.ceil(diff/12);
        System.out.println("Tithi "+tithi);
        double daily_motion_sun = sun_long - prevDay_sun_long;
        double daily_motion_moon = moon_long - prevDay_moon_long;
        if(daily_motion_moon < 0.0){
            daily_motion_moon += 360.0;
        }
        if(daily_motion_sun < 0.0){
            daily_motion_sun += 360.0;
        }

        double rd = (tithi*12)-diff;

        double end_time = ((rd*60*24)/((daily_motion_moon*60)-(daily_motion_sun*60)));
        long sunrise_hour = Long.parseLong(prevDaySunrise.substring(0,2));
        long sunrise_min = Long.parseLong(prevDaySunrise.substring(3,5));
        double tithi_start_time = (sunrise_hour+((double)sunrise_min/60))+end_time;
        return tithi_start_time;
    }

    private void calculateDurmuhurtham(double sunrise_in_minutes, double duration_of_day, int dayInWeek,double sunset_in,double duration_of_night) {
        if(dayInWeek == 1){
            double durmuhur_starts = (sunrise_in_minutes+((duration_of_day*10.4)/12));
            double durmuhur_ends = (durmuhur_starts+((duration_of_day*0.8)/12));
            System.out.println("Durmuhurtham starts from ::: "+toDMS(durmuhur_starts)+"to"+toDMS(durmuhur_ends));
            String start_deg =toDMS(durmuhur_starts);
            String hours = start_deg.substring(0,start_deg.lastIndexOf("°"));
            String str_hours = hours.replaceAll("\\s", "");
            String mins = start_deg.substring(start_deg.lastIndexOf("°")+1,start_deg.lastIndexOf("'"));
            String end_deg =toDMS(durmuhur_ends);
            String endhours = end_deg.substring(0,end_deg.lastIndexOf("°"));
            String end_hours = endhours.replaceAll("\\s", "");
            String minutes = end_deg.substring(end_deg.lastIndexOf("°")+1,end_deg.lastIndexOf("'"));
            durm_time.setText(str_hours+":"+mins+" to "+end_hours+":"+minutes);
        }
        if(dayInWeek == 2){
            double dur_first_start = (sunrise_in_minutes+((duration_of_day*6.4)/12));
            double durmuhur_first_ends = (dur_first_start+((duration_of_day*0.8)/12));

            double dur_second_start = (sunrise_in_minutes+((duration_of_day*8.8)/12));
            double durmuhur_second_ends = (dur_second_start+((duration_of_day*0.8)/12));

            String first_start_deg =toDMS(dur_first_start);
            String first_hours = first_start_deg.substring(0,first_start_deg.lastIndexOf("°"));
            String first_str_hours = first_hours.replaceAll("\\s", "");
            String first_mins = first_start_deg.substring(first_start_deg.lastIndexOf("°")+1,first_start_deg.lastIndexOf("'"));
            String first_end_deg =toDMS(durmuhur_first_ends);
            String first_endhours = first_end_deg.substring(0,first_end_deg.lastIndexOf("°"));
            String first_end_hours = first_endhours.replaceAll("\\s", "");
            String first_minutes = first_end_deg.substring(first_end_deg.lastIndexOf("°")+1,first_end_deg.lastIndexOf("'"));

            String second_start_deg =toDMS(dur_second_start);
            String second_hours = second_start_deg.substring(0,second_start_deg.lastIndexOf("°"));
            String second_str_hours = second_hours.replaceAll("\\s", "");
            String second_mins = second_start_deg.substring(second_start_deg.lastIndexOf("°")+1,second_start_deg.lastIndexOf("'"));
            String second_end_deg =toDMS(durmuhur_second_ends);
            String second_endhours = second_end_deg.substring(0,second_end_deg.lastIndexOf("°"));
            String second_end_hours = second_endhours.replaceAll("\\s", "");
            String second_minutes = second_end_deg.substring(second_end_deg.lastIndexOf("°")+1,second_end_deg.lastIndexOf("'"));

            durm_time.setText(first_str_hours+":"+first_mins+" to "+first_end_hours+":"+first_minutes+" and "+second_str_hours+":"+second_mins+" to "+second_end_hours+":"+second_minutes);
        }
        if(dayInWeek == 3){
            double dur_first_start = (sunrise_in_minutes+((duration_of_day*2.4)/12));
            double durmuhur_first_ends = (dur_first_start+((duration_of_day*0.8)/12));

            double dur_second_start = (sunset_in+((duration_of_night*4.8)/12));
            double durmuhur_second_ends = (dur_second_start+((duration_of_day*0.8)/12));

            String first_start_deg =toDMS(dur_first_start);
            String first_hours = first_start_deg.substring(0,first_start_deg.lastIndexOf("°"));
            String first_str_hours = first_hours.replaceAll("\\s", "");
            String first_mins = first_start_deg.substring(first_start_deg.lastIndexOf("°")+1,first_start_deg.lastIndexOf("'"));
            String first_end_deg =toDMS(durmuhur_first_ends);
            String first_endhours = first_end_deg.substring(0,first_end_deg.lastIndexOf("°"));
            String first_end_hours = first_endhours.replaceAll("\\s", "");
            String first_minutes = first_end_deg.substring(first_end_deg.lastIndexOf("°")+1,first_end_deg.lastIndexOf("'"));

            String second_start_deg =toDMS(dur_second_start);
            String second_hours = second_start_deg.substring(0,second_start_deg.lastIndexOf("°"));
            String second_str_hours = second_hours.replaceAll("\\s", "");
            String second_mins = second_start_deg.substring(second_start_deg.lastIndexOf("°")+1,second_start_deg.lastIndexOf("'"));
            String second_end_deg =toDMS(durmuhur_second_ends);
            String second_endhours = second_end_deg.substring(0,second_end_deg.lastIndexOf("°"));
            String second_end_hours = second_endhours.replaceAll("\\s", "");
            String second_minutes = second_end_deg.substring(second_end_deg.lastIndexOf("°")+1,second_end_deg.lastIndexOf("'"));

            durm_time.setText(first_str_hours+":"+first_mins+" to "+first_end_hours+":"+first_minutes+" and "+second_str_hours+":"+second_mins+" to "+second_end_hours+":"+second_minutes);
        }
        if(dayInWeek == 4){
            double durmuhur_starts = (sunrise_in_minutes+((duration_of_day*5.6)/12));
            double durmuhur_ends = (durmuhur_starts+((duration_of_day*0.8)/12));
            String start_deg =toDMS(durmuhur_starts);
            String hours = start_deg.substring(0,start_deg.lastIndexOf("°"));
            String str_hours = hours.replaceAll("\\s", "");
            String mins = start_deg.substring(start_deg.lastIndexOf("°")+1,start_deg.lastIndexOf("'"));
            String end_deg =toDMS(durmuhur_ends);
            String endhours = end_deg.substring(0,end_deg.lastIndexOf("°"));
            String end_hours = endhours.replaceAll("\\s", "");
            String minutes = end_deg.substring(end_deg.lastIndexOf("°")+1,end_deg.lastIndexOf("'"));
            durm_time.setText(str_hours+":"+mins+" to "+end_hours+":"+minutes);
        }
        if(dayInWeek == 5){
            double dur_first_start = (sunrise_in_minutes+((duration_of_day*4)/12));
            double durmuhur_first_ends = (dur_first_start+((duration_of_day*0.8)/12));

            double dur_second_start = (sunrise_in_minutes+((duration_of_day*8.8)/12));
            double durmuhur_second_ends = (dur_second_start+((duration_of_day*0.8)/12));

            String first_start_deg =toDMS(dur_first_start);
            String first_hours = first_start_deg.substring(0,first_start_deg.lastIndexOf("°"));
            String first_str_hours = first_hours.replaceAll("\\s", "");
            String first_mins = first_start_deg.substring(first_start_deg.lastIndexOf("°")+1,first_start_deg.lastIndexOf("'"));
            String first_end_deg =toDMS(durmuhur_first_ends);
            String first_endhours = first_end_deg.substring(0,first_end_deg.lastIndexOf("°"));
            String first_end_hours = first_endhours.replaceAll("\\s", "");
            String first_minutes = first_end_deg.substring(first_end_deg.lastIndexOf("°")+1,first_end_deg.lastIndexOf("'"));

            String second_start_deg =toDMS(dur_second_start);
            String second_hours = second_start_deg.substring(0,second_start_deg.lastIndexOf("°"));
            String second_str_hours = second_hours.replaceAll("\\s", "");
            String second_mins = second_start_deg.substring(second_start_deg.lastIndexOf("°")+1,second_start_deg.lastIndexOf("'"));
            String second_end_deg =toDMS(durmuhur_second_ends);
            String second_endhours = second_end_deg.substring(0,second_end_deg.lastIndexOf("°"));
            String second_end_hours = second_endhours.replaceAll("\\s", "");
            String second_minutes = second_end_deg.substring(second_end_deg.lastIndexOf("°")+1,second_end_deg.lastIndexOf("'"));

            durm_time.setText(first_str_hours+":"+first_mins+" to "+first_end_hours+":"+first_minutes+" and "+second_str_hours+":"+second_mins+" to "+second_end_hours+":"+second_minutes);
        }
        if(dayInWeek == 6){
            double dur_first_start = (sunrise_in_minutes+((duration_of_day*2.4)/12));
            double durmuhur_first_ends = (dur_first_start+((duration_of_day*0.8)/12));

            double dur_second_start = (sunrise_in_minutes+((duration_of_day*6.4)/12));
            double durmuhur_second_ends = (dur_second_start+((duration_of_day*0.8)/12));

            String first_start_deg =toDMS(dur_first_start);
            String first_hours = first_start_deg.substring(0,first_start_deg.lastIndexOf("°"));
            String first_str_hours = first_hours.replaceAll("\\s", "");
            String first_mins = first_start_deg.substring(first_start_deg.lastIndexOf("°")+1,first_start_deg.lastIndexOf("'"));
            String first_end_deg =toDMS(durmuhur_first_ends);
            String first_endhours = first_end_deg.substring(0,first_end_deg.lastIndexOf("°"));
            String first_end_hours = first_endhours.replaceAll("\\s", "");
            String first_minutes = first_end_deg.substring(first_end_deg.lastIndexOf("°")+1,first_end_deg.lastIndexOf("'"));

            String second_start_deg =toDMS(dur_second_start);
            String second_hours = second_start_deg.substring(0,second_start_deg.lastIndexOf("°"));
            String second_str_hours = second_hours.replaceAll("\\s", "");
            String second_mins = second_start_deg.substring(second_start_deg.lastIndexOf("°")+1,second_start_deg.lastIndexOf("'"));
            String second_end_deg =toDMS(durmuhur_second_ends);
            String second_endhours = second_end_deg.substring(0,second_end_deg.lastIndexOf("°"));
            String second_end_hours = second_endhours.replaceAll("\\s", "");
            String second_minutes = second_end_deg.substring(second_end_deg.lastIndexOf("°")+1,second_end_deg.lastIndexOf("'"));

            durm_time.setText(first_str_hours+":"+first_mins+" to "+first_end_hours+":"+first_minutes+" and "+second_str_hours+":"+second_mins+" to "+second_end_hours+":"+second_minutes);
        }
        if(dayInWeek == 7){
            double durmuhur_starts = (sunrise_in_minutes+((duration_of_day*1.6)/12));
            double durmuhur_ends = (durmuhur_starts+((duration_of_day*0.8)/12));
            String start_deg =toDMS(durmuhur_starts);
            String hours = start_deg.substring(0,start_deg.lastIndexOf("°"));
            String str_hours = hours.replaceAll("\\s", "");
            String mins = start_deg.substring(start_deg.lastIndexOf("°")+1,start_deg.lastIndexOf("'"));
            String end_deg =toDMS(durmuhur_ends);
            String endhours = end_deg.substring(0,end_deg.lastIndexOf("°"));
            String end_hours = endhours.replaceAll("\\s", "");
            String minutes = end_deg.substring(end_deg.lastIndexOf("°")+1,end_deg.lastIndexOf("'"));
            durm_time.setText(str_hours+":"+mins+" to "+end_hours+":"+minutes);
        }
    }

    private void calculateGulikaKalam(double sunrise_in_minutes, double duration_of_day, int dayInWeek) {
        double[] mult = {0.75,0.625,0.5,0.375,0.25,0.125,1};
        if(dayInWeek != 7) {
            double gulikakalam_starts = (sunrise_in_minutes + (duration_of_day * mult[dayInWeek - 1]));
            double gulikakalam_ends = gulikakalam_starts + (duration_of_day * 0.125);
            String start_deg =toDMS(gulikakalam_starts);
            String hours = start_deg.substring(0,start_deg.lastIndexOf("°"));
            String str_hours = hours.replaceAll("\\s", "");
            String mins = start_deg.substring(start_deg.lastIndexOf("°")+1,start_deg.lastIndexOf("'"));
            String end_deg =toDMS(gulikakalam_ends);
            String endhours = end_deg.substring(0,end_deg.lastIndexOf("°"));
            String end_hours = endhours.replaceAll("\\s", "");
            String minutes = end_deg.substring(end_deg.lastIndexOf("°")+1,end_deg.lastIndexOf("'"));
            galikai_kalam.setText(str_hours+":"+mins+" to "+end_hours+":"+minutes);

        }else{
            double gulikakalam_starts = sunrise_in_minutes;
            double gulikakalam_ends = gulikakalam_starts + (duration_of_day * 0.125);
            String start_deg =toDMS(gulikakalam_starts);
            String hours = start_deg.substring(0,start_deg.lastIndexOf("°"));
            String str_hours = hours.replaceAll("\\s", "");
            String mins = start_deg.substring(start_deg.lastIndexOf("°")+1,start_deg.lastIndexOf("'"));
            String end_deg =toDMS(gulikakalam_ends);
            String endhours = end_deg.substring(0,end_deg.lastIndexOf("°"));
            String end_hours = endhours.replaceAll("\\s", "");
            String minutes = end_deg.substring(end_deg.lastIndexOf("°")+1,end_deg.lastIndexOf("'"));
            galikai_kalam.setText(str_hours+":"+mins+" to "+end_hours+":"+minutes);
        }

    }

    private void calculateYamagandam(double sunrise_in_minutes, double duration_of_day, int dayInWeek) {
        double[] mult = {0.5,0.375,0.25,0.125,1,0.75,0.625};
        if (dayInWeek != 5) {
            double yamagandam_starts =  (sunrise_in_minutes+(duration_of_day*mult[dayInWeek-1]));
            double yamagandam_ends = yamagandam_starts+(duration_of_day*0.125);
            String start_deg =toDMS(yamagandam_starts);
            String hours = start_deg.substring(0,start_deg.lastIndexOf("°"));
            String str_hours = hours.replaceAll("\\s", "");
            String mins = start_deg.substring(start_deg.lastIndexOf("°")+1,start_deg.lastIndexOf("'"));
            String end_deg =toDMS(yamagandam_ends);
            String endhours = end_deg.substring(0,end_deg.lastIndexOf("°"));
            String end_hours = endhours.replaceAll("\\s", "");
            String minutes = end_deg.substring(end_deg.lastIndexOf("°")+1,end_deg.lastIndexOf("'"));
            yama_gandam.setText(str_hours+":"+mins+" to "+end_hours+":"+minutes);
        }else{
            double yamagandam_starts =  sunrise_in_minutes;
            double yamagandam_ends = yamagandam_starts+(duration_of_day*0.125);
            String start_deg =toDMS(yamagandam_starts);
            String hours = start_deg.substring(0,start_deg.lastIndexOf("°"));
            String str_hours = hours.replaceAll("\\s", "");
            String mins = start_deg.substring(start_deg.lastIndexOf("°")+1,start_deg.lastIndexOf("'"));
            String end_deg =toDMS(yamagandam_ends);
            String endhours = end_deg.substring(0,end_deg.lastIndexOf("°"));
            String end_hours = endhours.replaceAll("\\s", "");
            String minutes = end_deg.substring(end_deg.lastIndexOf("°")+1,end_deg.lastIndexOf("'"));
            yama_gandam.setText(str_hours+":"+mins+" to "+end_hours+":"+minutes);
        }


    }

    private void calculateRahuKalam(double sunrise_in_minutes, double duration_of_day, int dayInWeek) {
        double[] mult = {0.875,0.125,0.75,0.5,0.625,0.375,0.25};
        double rahukalam_starts =  (sunrise_in_minutes+(duration_of_day*mult[dayInWeek-1]));
        double rahukalam_ends = rahukalam_starts+(duration_of_day*0.125);
        String start_deg =toDMS(rahukalam_starts);
        String hours = start_deg.substring(0,start_deg.lastIndexOf("°"));
        String str_hours = hours.replaceAll("\\s", "");
        String mins = start_deg.substring(start_deg.lastIndexOf("°")+1,start_deg.lastIndexOf("'"));
        String end_deg =toDMS(rahukalam_ends);
        String endhours = end_deg.substring(0,end_deg.lastIndexOf("°"));
        String end_hours = endhours.replaceAll("\\s", "");
        String minutes = end_deg.substring(end_deg.lastIndexOf("°")+1,end_deg.lastIndexOf("'"));
        rahu_kalam.setText(str_hours+":"+mins+" to "+end_hours+":"+minutes);

    }

    private void calculateHouseAndSignOfPlanet(int y,int m,int d,String rise,double lng,double lat) {

        long sunrise_hour = Long.parseLong(rise.substring(0,2));
        long sunrise_min = Long.parseLong(rise.substring(3,5));
        int year = y;
        int month = m;
        int day = d;
        double longitude =lng; // Guntur
        double latitude = lat;
        double hour = sunrise_hour + (sunrise_min / 60.) - 5.5; // IST



        // Use ... new SwissEph("/path/to/your/ephemeris/data/files"); when
        // your data files don't reside somewhere in the paths defined in
        // SweConst.SE_EPHE_PATH, which is ".:./ephe:/users/ephe2/:/users/ephe/"
        // currently.
        SwissEph sw =  new SwissEph(getApplicationContext().getFilesDir() + File.separator + "/ephe");
        SweDate sd = new SweDate(year, month, day, hour);
        System.out.println(sd.getDate(0).toString());
        // Set sidereal mode:
        sw.swe_set_sid_mode(SID_METHOD, 0, 0);

        // Some required variables:
        double[] cusps = new double[13];
        double[] acsc = new double[10];
        double[] xp = new double[6];
        StringBuffer serr = new StringBuffer();

        // Print input details:
        System.out.println("Date (YYYY/MM/DD): " + sd.getYear() + "/" + sd.getMonth() + "/" + sd.getDay() + ", "
                + toHMS(sd.getHour()));
        System.out.println("Jul. day:  " + sd.getJulDay());
        System.out.println("DeltaT:    " + sd.getDeltaT() * 24 * 3600 + " sec.");
        System.out.println("Location:  " + toDMS(Math.abs(longitude)) + (longitude > 0 ? "E" : "W") + " / "
                + toDMS(Math.abs(latitude)) + (latitude > 0 ? "N" : "S"));

        // Get and print ayanamsa value for info:
        double ayanamsa = sw.swe_get_ayanamsa_ut(sd.getJulDay());
        System.out.println("Ayanamsa:  " + toDMS(ayanamsa) + " (" + sw.swe_get_ayanamsa_name(SID_METHOD) + ")");

        // Get and print lagna:
        int flags = SweConst.SEFLG_SIDEREAL;
        int result = sw.swe_houses(sd.getJulDay(), flags, latitude, longitude, 'P', cusps, acsc);
        System.out.println("Ascendant: " + toDMS(acsc[0]) + "\n");

        int ascSign = (int) (acsc[0] / 30) + 1;

        // Calculate all planets:
        int[] planets = { SweConst.SE_SUN, SweConst.SE_MOON, SweConst.SE_MARS, SweConst.SE_MERCURY, SweConst.SE_JUPITER,
                SweConst.SE_VENUS, SweConst.SE_SATURN, SweConst.SE_TRUE_NODE }; // Some
        // systems
        // prefer
        // SE_MEAN_NODE

        flags = SweConst.SEFLG_SWIEPH | // fastest method, requires data files
                SweConst.SEFLG_SIDEREAL | // sidereal zodiac
                SweConst.SEFLG_NONUT | // will be set automatically for sidereal
                // calculations, if not set here
                SweConst.SEFLG_SPEED; // to determine retrograde vs. direct
        // motion
        int sign;
        int house;
        boolean retrograde = false;

        for (int p = 0; p < planets.length; p++) {
            int planet = planets[p];
            String planetName = sw.swe_get_planet_name(planet);
            int ret = sw.swe_calc_ut(sd.getJulDay(), planet, flags, xp, serr);

            if (ret != flags) {
                if (serr.length() > 0) {
                    System.err.println("Warning: " + serr);
                } else {
                    System.err.println(String.format("Warning, different flags used (0x%x)", ret));
                }
            }

            sign = (int) (xp[0] / 30) + 1;
            house = (sign + 12 - ascSign) % 12 + 1;
            retrograde = (xp[3] < 0);

            System.out.printf("%-12s: %s %c; sign: %2d; %s in house %2d\n", planetName, toDMS(xp[0]),
                    (retrograde ? 'R' : 'D'), sign, toDMS(xp[0] % 30), house);
            if(p == 0){
                sun_long = xp[0];
                sun_sign = sign;
            }
            if(p == 1){
                moon_long = xp[0];
                moon_sign = sign;
            }

        }
        // KETU
        xp[0] = (xp[0] + 180.0) % 360;
        String planetName = "Ketu (true)";

        sign = (int) (xp[0] / 30) + 1;
        house = (sign + 12 - ascSign) % 12 + 1;

        System.out.printf("%-12s: %s %c; sign: %2d; %s in house %2d\n", planetName, toDMS(xp[0]),
                (retrograde ? 'R' : 'D'), sign, toDMS(xp[0] % 30), house);

    }

    static String toHMS(double d) {
        d += 0.5 / 3600.; // round to one second
        int h = (int) d;
        d = (d - h) * 60;
        int min = (int) d;
        int sec = (int) ((d - min) * 60);



        return String.format("%2d:%02d:%02d", h, min, sec);
    }

    static String toDMS(double d) {
        d += 0.5 / 3600. / 10000.; // round to 1/1000 of a second
        int deg = (int) d;
        d = (d - deg) * 60;
        int min = (int) d;
        d = (d - min) * 60;
        double sec = d;

        return String.format("%3d°%02d'%07.4f\"", deg, min, sec);

    }

    static double toD(String s) {
        String degrees = s.substring(0,s.lastIndexOf("°"));
        System.out.println("degrees "+degrees);
        String min = s.substring(s.lastIndexOf("°")+1,s.lastIndexOf("'"));
        System.out.println("minutes "+min);
        String seconds = s.substring(s.lastIndexOf("'")+1,s.lastIndexOf('"'));
        System.out.println("Seconds "+seconds);
        Double decDeg = Double.parseDouble(degrees)+(Double.parseDouble(min)/60)+(Double.parseDouble(seconds)/3600);
        return decDeg;
    }



    public static int getDayNumberOld(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_WEEK);
    }



    private static SwissEph swissEph;
    private static SwissEph getSwissEph()
    {
        if(swissEph == null) {
                swissEph = new SwissEph();
        }
        return swissEph;
    }

    public static double getJulDay(Calendar time)
    {
        time.setTimeZone(TimeZone.getTimeZone("GMT"));

        /*double hour = time.get(Calendar.HOUR_OF_DAY)
                + time.get(Calendar.MINUTE) / 60.0
                + time.get(Calendar.SECOND) / 3600.0;*/
        double hour = 7+(42/60.0)+(12/3600.0);


        /*System.out.println("hour  "+hour);
        System.out.println("minute  "+time.get(Calendar.MINUTE));
        System.out.println("second  "+time.get(Calendar.SECOND));

        System.out.println("year  "+time.get(Calendar.YEAR));
        System.out.println("Month  "+time.get(Calendar.MONTH));
        System.out.println("day  "+time.get(Calendar.DAY_OF_MONTH));*/
       double jday = SweDate.getJulDay(time.get(Calendar.YEAR),
                time.get(Calendar.MONTH) + 1,
                time.get(14),
                hour);
       // double jday = SweDate.getJulDay(2019,1,22,hour);
        return jday;
    }
    private void convertIntoDMS(double lonitude,int deg,int min,double sec){
        deg = (int)lonitude;
        min = (int)((lonitude - deg) * 60.0);
        sec = (lonitude - deg - min / 60.0) * 3600.0;
        System.out.println("Sun longitude in deg "+deg+" "+min+" "+sec);
    }

}