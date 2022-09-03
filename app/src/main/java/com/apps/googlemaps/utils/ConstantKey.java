package com.apps.googlemaps.utils;

import com.apps.googlemaps.models.MapLocation;

import java.util.ArrayList;

public class ConstantKey {

    public final static String[] countryAreaCodes = {"+93", "+355", "+213",
            "+376", "+244", "+672", "+54", "+374", "+297", "+61", "+43", "+994", "+973",
            "+880", "+375", "+32", "+501", "+229", "+975", "+591", "+387", "+267", "+55",
            "+673", "+359", "+226", "+95", "+257", "+855", "+237", "+1", "+238", "+236",
            "+235", "+56", "+86", "+61", "+61", "+57", "+269", "+242", "+682", "+506",
            "+385", "+53", "+357", "+420", "+45", "+253", "+670", "+593", "+20", "+503",
            "+240", "+291", "+372", "+251", "+500", "+298", "+679", "+358", "+33",
            "+689", "+241", "+220", "+995", "+49", "+233", "+350", "+30", "+299", "+502",
            "+224", "+245", "+592", "+509", "+504", "+852", "+36", "+91", "+62", "+98",
            "+964", "+353", "+44", "+972", "+39", "+225", "+1876", "+81", "+962", "+7",
            "+254", "+686", "+965", "+996", "+856", "+371", "+961", "+266", "+231",
            "+218", "+423", "+370", "+352", "+853", "+389", "+261", "+265", "+60",
            "+960", "+223", "+356", "+692", "+222", "+230", "+262", "+52", "+691",
            "+373", "+377", "+976", "+382", "+212", "+258", "+264", "+674", "+977",
            "+31", "+687", "+64", "+505", "+227", "+234", "+683", "+850", "+47", "+968",
            "+92", "+680", "+507", "+675", "+595", "+51", "+63", "+870", "+48", "+351",
            "+1", "+974", "+40", "+7", "+250", "+590", "+685", "+378", "+239", "+966",
            "+221", "+381", "+248", "+232", "+65", "+421", "+386", "+677", "+252", "+27",
            "+82", "+34", "+94", "+290", "+508", "+249", "+597", "+268", "+46", "+41",
            "+963", "+886", "+992", "+255", "+66", "+228", "+690", "+676", "+216", "+90",
            "+993", "+688", "+971", "+256", "+44", "+380", "+598", "+1", "+998", "+678",
            "+39", "+58", "+84", "+681", "+967", "+260", "+263"};

    public static ArrayList<MapLocation> getLocations() {
        ArrayList<MapLocation> arrayList = new ArrayList<>();
        arrayList.add(new MapLocation(1, "Adabor", "Padakhep Manabik Unnayan Kendra, Society, House# 548, Road# 10, Adabor, Dhaka 1207", 23.7723596609586, 90.35469385555037, ""));
        arrayList.add(new MapLocation(2, "Airport", "Indigo Airlines Dhaka Ticket Office, Airport", 23.85028512177569, 90.40751497875705, ""));
        arrayList.add(new MapLocation(3, "Badda", "Hossain Market, SM Bhaban, Cha 75/C (5ft floor, Badda, Dhaka 1212)", 23.783877430336286, 90.42602502121011, ""));
        arrayList.add(new MapLocation(4, "Banani", "54, Kamal Ataturk Avenue(Ground Floor, Banani, Dhaka 1213)", 23.794035828705997, 90.40329090072736, ""));
        arrayList.add(new MapLocation(5, "Bongshal", "M/s Yakub Enterprise, 83/2, Bongshal Road, Dhaka 1100", 23.71742217482599, 90.4067442404944, ""));
        arrayList.add(new MapLocation(6, "Vasanteak", "B M Chowdhury Motors, Vasanteak bazar, Dhaka, Dhaka 1206", 23.81116271619445, 90.39234168854665, ""));
        arrayList.add(new MapLocation(7, "Cantonment", "House Brig Gen Retd Taufiq, 56 Rd No. 7, Dhaka Cantonment, Dhaka 1206", 23.801995738610376, 90.39373666793419, ""));
        arrayList.add(new MapLocation(8, "Chawkbazar", "Chawkbazar Shahi Jame Masjid, 49 Chackbazar Circular Rd, Dhaka 1100", 23.71599250742949, 90.39581869401772, ""));
        arrayList.add(new MapLocation(9, "Darussalam", "Darussalam Tower, 59/D/B, Mirpur Darussalam Rd, Dhaka", 23.778611372278036, 90.35604164680798, ""));
        arrayList.add(new MapLocation(10, "Dakshinkhan", "Bank Asia Limited, K C Plaza (1st Floor) Noapara, Dakshinkhan , Uttara, Dhaka 1230", 23.860555297083486, 90.42285177557946, ""));
        return arrayList;
    }
}
