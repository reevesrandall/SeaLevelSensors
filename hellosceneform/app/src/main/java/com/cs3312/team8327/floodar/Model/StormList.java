package com.cs3312.team8327.floodar.Model;

import java.util.ArrayList;
import java.util.List;

public class StormList {
    private static List<Storm> list;

    static {
        list = new ArrayList<>();
        list.add(new Storm("Custom", 0.0f, "This is a custom slider to explore different levels of flooding. Swipe to other storms to receive more detailed information."));
        list.add(new Storm("Hurricane Irma", 1.52f, "Hurricane Irma was an extremely powerful and catastrophic Cape Verde hurricane, the strongest observed in the Atlantic in terms of maximum sustained winds since Wilma, and the strongest storm on record to exist in the open Atlantic region. Irma was the first Category 5 hurricane to strike the Leeward Islands on record, followed by Maria two weeks later, and is the second-costliest Caribbean hurricane on record, after Maria. The ninth named storm, fourth hurricane, second major hurricane, and first Category 5 hurricane of the 2017 Atlantic hurricane season, Irma caused widespread and catastrophic damage throughout its long lifetime, particularly in the northeastern Caribbean and the Florida Keys. It was also the most intense hurricane to strike the continental United States since Katrina in 2005, the first major hurricane to make landfall in Florida since Wilma in the same year, and the first Category 4 hurricane to strike the state since Charley in 2004. The word Irmageddon was coined soon after the hurricane to describe the damage caused by the hurricane."));
        list.add(new Storm("Hurricane Matthew", 0.76f, "Hurricane Matthew was the first Category 5 Atlantic hurricane since Felix in 2007, and also caused catastrophic damage and a humanitarian crisis in Haiti, as well as widespread devastation in the southeastern United States. The deadliest Atlantic hurricane since Hurricane Stan in 2005, Matthew was the thirteenth named storm, fifth hurricane and second major hurricane of the 2016 Atlantic hurricane season. It caused extensive damage to landmasses in the Greater Antilles, and severe damage in several islands of the Bahamas who were still recovering from Joaquin which had pounded such areas nearly a year earlier. At one point, the hurricane even threatened to be the first storm of Category 3 or higher intensity to strike the United States since Wilma in 2005, but Matthew stayed just offshore paralleling the Floridan coastline."));
        list.add(new Storm("Hurricane Michael", 1.83f, "Hurricane Michael was the third-most intense Atlantic hurricane to make landfall in the United States in terms of pressure, behind the 1935 Labor Day hurricane and Hurricane Camille of 1969, as well as the strongest storm in terms of maximum sustained wind speed to strike the contiguous United States since Andrew in 1992. In addition, it was the strongest storm on record in the Florida Panhandle, and was the fourth-strongest landfalling hurricane in the contiguous United States, in terms of wind speed."));
    }

    public static Storm getStorm(int index) {
        return list.get(index);
    }

    public static void addStorm(Storm storm) {
        list.add(storm);
    }
}
