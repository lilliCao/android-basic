package com.example.android.hamburg;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by tali on 25.11.17.
 */

public class FragmentPagerAdapter extends android.support.v4.app.FragmentPagerAdapter {
    private final CharSequence[] tabTitle;
    private Activity act;
    private final ArrayList<Item> general;
    private final ArrayList<Item> sightseeing;
    private final ArrayList<Item> activity;
    private final ArrayList<Item> eating;
    private final ArrayList<Item> transport;
    private final ArrayList<Item> accomodation;

    public FragmentPagerAdapter(FragmentManager fm, Activity act) {
        super(fm);
        this.act = act;
        tabTitle = new String[]{
                act.getString(R.string.t1),
                act.getString(R.string.t2),
                act.getString(R.string.t3),
                act.getString(R.string.t4),
                act.getString(R.string.t5),
                act.getString(R.string.t6)
        };
        general = new ArrayList<>(Arrays.asList(

                new Item(act.getString(R.string.location), act.getString(R.string.location_text)
                        , "https://www.google.es/maps/place/Hamburg,+Deutschland/@53.558572,9.9278215,10z/data=!3m1!4b1!4m5!3m4!1s0x47b161837e1" +
                        "813b9:0x4263df27bd63aa0!8m2!3d53.5510846!4d9.9936819?hl=de&hl=de", ""),
                new Item(act.getString(R.string.population), act.getString(R.string.population_text)
                        , "", ""),
                new Item(act.getString(R.string.weather), act.getString(R.string.weather_content)
                        , "", ""),
                new Item(act.getString(R.string.ranking), act.getString(R.string.ranking_content)
                        , "", "")
        ));

        sightseeing = new ArrayList<>(Arrays.asList(

                new Item(R.drawable.sightseeing_habour, act.getString(R.string.harbour), act.getString(R.string.harbour_text)
                        , "", ""),
                new Item(R.drawable.sightseeing_reeperbahn, act.getString(R.string.reeperbahn), act.getString(R.string.reeperbahn_text)
                        , "http://www.hamburg.de/reeperbahn/", ""),
                new Item(R.drawable.sightseeing_cityhall, act.getString(R.string.city_hall), act.getString(R.string.city_hall_text)
                        , "", ""),
                new Item(R.drawable.sightseeing_alster, act.getString(R.string.lake), act.getString(R.string.lake_text)
                        , "", ""),
                new Item(R.drawable.sightseeing_hafencity, act.getString(R.string.hafencity), act.getString(R.string.hafencity_text)
                        , "http://www.hamburg.de/hafencity/", ""),
                new Item(R.drawable.sightseeing_church, act.getString(R.string.church), act.getString(R.string.church_text)
                        , "", ""),
                new Item(R.drawable.sightseeing_museum, act.getString(R.string.museum), act.getString(R.string.museum_text)
                        , "http://www.momem.org/en/", "http://www.imm-hamburg.de/international/en/")

        ));
        activity = new ArrayList<>(Arrays.asList(

                new Item(R.drawable.activity_fishmarket, act.getString(R.string.fish_market), act.getString(R.string.fish_market_text)
                        , "http://www.hamburg.de/fischmarkt/", ""),
                new Item(R.drawable.activity_waterlight, act.getString(R.string.water_light), act.getString(R.string.water_light_text)
                        , "http://www.hamburg.de/freizeit/250832/wasserlichtkonzerte/", ""),
                new Item(R.drawable.activity_hamburgdungeon, act.getString(R.string.dungeon), act.getString(R.string.dungeon_text)
                        , "https://www.thedungeons.com/hamburg/en/", ""),
                new Item(R.drawable.activity_miniatur, act.getString(R.string.miniatur), act.getString(R.string.miniatur_text)
                        , "http://www.miniatur-wunderland.com/", ""),
                new Item(R.drawable.activity_dialog, act.getString(R.string.dialog), act.getString(R.string.dialog_text)
                        , "https://dialog-in-hamburg.de/", ""),
                new Item(R.drawable.activity_planetarium, act.getString(R.string.planet), act.getString(R.string.planet_text)
                        , "http://www.planetarium-hamburg.de/", ""),
                new Item(R.drawable.activity_shopping, act.getString(R.string.shopping), act.getString(R.string.shopping_text)
                        , "https://www.europa-passage.de/"
                        , "https://www.eez.de/"),
                new Item(R.drawable.activity_theater, act.getString(R.string.musical), act.getString(R.string.musical_text)
                        , "https://www.stage-entertainment.de/musicals-shows/hamburg/" +
                        "disneys-aladdin/show/disneys-aladdin-hamburg.html"
                        , "https://www.stage-entertainment.de/musicals-shows/hamburg/der-koenig-der-loewen" +
                        "/show/disneys-der-koenig-der-loewen-hamburg.html"),
                new Item(R.drawable.activity_sternschanzen, act.getString(R.string.sternschanzen), act.getString(R.string.sternschanzen_text)
                        , "", ""),
                new Item(R.drawable.activity_fleamarket, act.getString(R.string.fleamarket), act.getString(R.string.fleamarket_text)
                        , "https://www.fleamarketinsiders.com/best-flea-markets-in-germany/3/", ""),
                new Item(R.drawable.activity_dom, act.getString(R.string.dom), act.getString(R.string.dom_text)
                        , "http://www.hamburg.de/dom/", ""),
                new Item(R.drawable.activity_christmas, act.getString(R.string.christmas), act.getString(R.string.christmas_text)
                        , "", ""),
                new Item(R.drawable.activity_iceskating, act.getString(R.string.ice), act.getString(R.string.ice_text)
                        , "", ""),
                new Item(R.drawable.activity_park, act.getString(R.string.picnic), act.getString(R.string.picnic_text)
                        , "", "")
        ));
        eating = new ArrayList<>(Arrays.asList(

                new Item(R.drawable.food_potato, act.getString(R.string.food1), act.getString(R.string.food1_t)
                        , "", ""),
                new Item(R.drawable.food_beer, act.getString(R.string.food2), act.getString(R.string.food2_t)
                        , "", ""),
                new Item(R.drawable.food_asia, act.getString(R.string.food3), act.getString(R.string.food3_t)
                        , "", ""),
                new Item(R.drawable.food_breakfast, act.getString(R.string.food4), act.getString(R.string.food4_t), "", ""),
                new Item(R.drawable.food_fastfood, act.getString(R.string.food5), act.getString(R.string.food5_t)
                        , "", ""),
                new Item(R.drawable.food_other, act.getString(R.string.other), act.getString(R.string.other_t)
                        , "", "")

        ));
        transport = new ArrayList<>(Arrays.asList(

                new Item(R.drawable.ic_train_black_24dp, act.getString(R.string.train), act.getString(R.string.train_t)
                        , "https://www.bahn.de/p/view/index.shtml", "http://www.hvv.de/"),
                new Item(R.drawable.ic_directions_bus_black_24dp, act.getString(R.string.bus), act.getString(R.string.bus_t)
                        , "http://www.hvv.de/", ""),
                new Item(R.drawable.ic_directions_bike_black_24dp, act.getString(R.string.bike), act.getString(R.string.bike_t)
                        , "https://stadtrad.hamburg.de/kundenbuchung/process.php?proc=download_tarife", ""),
                new Item(R.drawable.ic_directions_walk_black_24dp, act.getString(R.string.foot), act.getString(R.string.foot_t)
                        , "", "")

        ));
        accomodation = new ArrayList<>(Arrays.asList(

                new Item(R.drawable.accomodation_hotel, act.getString(R.string.hotel), act.getString(R.string.hotel_t)
                        , "", ""),
                new Item(R.drawable.accomodation_hostel, act.getString(R.string.hostel), act.getString(R.string.hostel_t)
                        , "", ""),
                new Item(R.drawable.accomodation_airbnb, act.getString(R.string.air), act.getString(R.string.air_t)
                        , "http://airbnb.com/", ""),
                new Item(R.drawable.accomodation_couchsurfing, act.getString(R.string.couch_surfing), act.getString(R.string.couch_surfing_t)
                        , "http://couchsurfing.com/", "")

        ));

    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return CategoryFragment.newInstance(general, R.color.colorGeneral, true,
                        R.drawable.catelog_general, act.getString(R.string.general));
            case 1:
                return CategoryFragment.newInstance(sightseeing, R.color.colorSightseeing, true,
                        R.drawable.catelog_sightseeing, act.getString(R.string.sightseeing));
            case 2:
                return CategoryFragment.newInstance(activity, R.color.colorActivities, true,
                        R.drawable.catelog_activity, act.getString(R.string.activity));
            case 3:
                return CategoryFragment.newInstance(eating, R.color.colorFood, true,
                        R.drawable.catelog_food, act.getString(R.string.food));
            case 4:
                return CategoryFragment.newInstance(transport, R.color.colorTransport, true,
                        R.drawable.catelog_transport, act.getString(R.string.transport));
            default:
                return CategoryFragment.newInstance(accomodation, R.color.colorAccomodation, false,
                        R.drawable.catelog_accomodation, act.getString(R.string.accomodation));
        }
    }

    @Override
    public int getCount() {
        return 6;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitle[position];
    }
}
