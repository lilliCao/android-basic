package com.example.android.miwok;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by tali on 24.11.17.
 */

public class MyFragmentPagerAdapter extends android.support.v4.app.FragmentPagerAdapter {
    private String tabTitle[] = new String[]{
            "Numbers", "Family Members", "Colors", "Phrases",
            "Occupation", "Hobbies", "Let's sing"
    };
    final ArrayList<Word> colors = new ArrayList<>(Arrays.asList(
            new Word("màu đen", "black", R.drawable.color_black, R.raw.voice_den),
            new Word("màu trắng", "white", R.drawable.color_white, R.raw.voice_trang),
            new Word("màu xám", "gray", R.drawable.color_gray, R.raw.voice_xam),
            new Word("màu xanh", "green", R.drawable.color_green, R.raw.voice_xanh),
            new Word("màu vàng", "dusty yellow", R.drawable.color_dusty_yellow, R.raw.voice_vang),
            new Word("màu vàng mù tạt", "mustard yellow", R.drawable.color_mustard_yellow, R.raw.voice_vangmutat),
            new Word("màu nâu", "brown", R.drawable.color_brown, R.raw.voice_nau),
            new Word("màu đỏ", "red", R.drawable.color_red, R.raw.voice_do)
    )
    );
    final ArrayList<Word> family = new ArrayList<>(Arrays.asList(
            new Word("bà", "grandmother", R.drawable.family_grandmother, R.raw.voice_ba),
            new Word("ông", "grandfather", R.drawable.family_grandfather, R.raw.voice_ong),
            new Word("bố", "father", R.drawable.family_father, R.raw.voice_bo),
            new Word("mẹ", "mother", R.drawable.family_mother, R.raw.voice_me),
            new Word("con gái", "daughter", R.drawable.family_daughter, R.raw.voice_cgai),
            new Word("con trai", "son", R.drawable.family_son, R.raw.voice_ctrai),
            new Word("anh trai", "older brother", R.drawable.family_older_brother, R.raw.voice_atrai),
            new Word("chị gái", "older sister", R.drawable.family_older_sister, R.raw.voice_chigai),
            new Word("cháu gái", "niece", R.drawable.family_younger_sister, R.raw.voice_chaugai),
            new Word("cháu trai", "nephew", R.drawable.family_younger_brother, R.raw.voice_chautrai),
            new Word("dì", "aunt", R.drawable.family_younger_brother, R.raw.voice_di),
            new Word("bác", "uncle", R.drawable.family_younger_brother, R.raw.voice_bac)
    ));
    final ArrayList<Word> hobbies = new ArrayList<>(Arrays.asList(
            new Word("Em/Anh thích", "I like", R.drawable.like, R.raw.voice_thich),
            new Word("Anh/Em có thể", "I can", R.drawable.i_can, R.raw.voice_cothe),
            new Word("ăn", "eat", R.drawable.eat, R.raw.voice_an),
            new Word("ngủ", "sleep", R.drawable.sleep, R.raw.voice_ngu),
            new Word("mua sắm", "go shopping", R.drawable.shopping, R.raw.voice_muasam),
            new Word("đi chợ", "go to market", R.drawable.market, R.raw.voice_dicho),
            new Word("du lịch", "travel", R.drawable.travel, R.raw.voice_dulich),
            new Word("nấu ăn", "cook", R.drawable.cook, R.raw.voice_nauan),
            new Word("lập trình", "code", R.drawable.program, R.raw.voice_laptrinh),
            new Word("xem ti vi", "watch television", R.drawable.tv, R.raw.voice_xemtv),
            new Word("xem phim", "watch movies", R.drawable.movie, R.raw.voice_phim),
            new Word("hát", "sing", R.drawable.sing, R.raw.voice_hat),
            new Word("bơi", "swim", R.drawable.swim, R.raw.voice_boi)
    ));
    final ArrayList<Word> numbers = new ArrayList<>(Arrays.asList(
            new Word("một", "one", R.drawable.number_one, R.raw.voice_001),
            new Word("hai", "two", R.drawable.number_two, R.raw.voice_002),
            new Word("ba", "three", R.drawable.number_three, R.raw.voice_003),
            new Word("bốn", "four", R.drawable.number_four, R.raw.voice_004),
            new Word("năm", "five", R.drawable.number_five, R.raw.voice_005),
            new Word("sáu", "six", R.drawable.number_six, R.raw.voice_006),
            new Word("bảy", "seven", R.drawable.number_seven, R.raw.voice_007),
            new Word("tám", "eight", R.drawable.number_eight, R.raw.voice_008),
            new Word("chín", "nine", R.drawable.number_nine, R.raw.voice_009),
            new Word("mười", "ten", R.drawable.number_ten, R.raw.voice_010),
            new Word("mười một", "eleven", R.drawable.number_ten, R.raw.voice_011),
            new Word("mười hai", "twelve", R.drawable.number_ten, R.raw.voice_012),
            new Word("hai mươi", "twenty", R.drawable.number_ten, R.raw.voice_020),
            new Word("hai mốt", "twenty one", R.drawable.number_ten, R.raw.voice_021),
            new Word("hai hai", "twenty two", R.drawable.number_ten, R.raw.voice_022),
            new Word("ba mươi", "thirty", R.drawable.number_ten, R.raw.voice_030),
            new Word("ba mốt", "thirty one", R.drawable.number_ten, R.raw.voice_031),
            new Word("một trăm", "one hundred", R.drawable.number_ten, R.raw.voice_100)


    ));
    final ArrayList<Word> occupation = new ArrayList<>(Arrays.asList(
            new Word("sinh viên", "student", R.drawable.sinh_vien, R.raw.voice_sinhv),
            new Word("giáo viên", "teacher", R.drawable.giao_vien, R.raw.voice_giaovien),
            new Word("giảng viên", "lecturer", R.drawable.lecturer, R.raw.voice_giangvien),
            new Word("nông dân", "farmer", R.drawable.nong_dan, R.raw.voice_nongdan),
            new Word("công an", "police", R.drawable.cong_an, R.raw.voice_congan),
            new Word("kế toán", "accountant", R.drawable.ke_toan, R.raw.voice_ketoan),
            new Word("lập trình viên", "programmer", R.drawable.lap_trinh_vien, R.raw.voice_laptrinhvien),
            new Word("nhân viên văn phòng", "officer", R.drawable.nhan_vien_van_phong, R.raw.voice_nhanvvp)
    ));
    final ArrayList<Word> phrases = new ArrayList<>(Arrays.asList(
            new Word("Con chào anh/chị/em/cô/bác", "Hello", R.raw.voice_chao),
            new Word("Anh khoẻ không?", "How are you?", R.raw.voice_khoe),
            new Word("Anh bao nhiêu tuổi?", "How old are you?", R.raw.voice_tuoi),
            new Word("Anh tên gì ạ?", "What's your name?", R.raw.voice_ten),
            new Word("Anh làm nghề gì ạ?", "What does you do?", R.raw.voice_lam),
            new Word("Anh yêu em", "I love you", R.raw.voice_aye),
            new Word("Em là sinh viên", "I am student", R.raw.voice_sinhvien),
            new Word("Em 24 tuổi", "I am 24 years old", R.raw.voice_tuoi24),
            new Word("Chúc mừng năm mới", "Happy new year", R.raw.voice_nammoi),
            new Word("Anh cám ơn", "Thank you", R.raw.voice_camon)
    ));

    public MyFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Log.e("Fragment position", String.valueOf(position));
        switch (position) {
            case 0:
                return CategoryFragment.newInstance(numbers,false,R.color.category_numbers);
            case 1:
                return CategoryFragment.newInstance(family,false,R.color.category_family);
            case 2:
                return CategoryFragment.newInstance(colors,false,R.color.category_colors);
            case 3:
                return CategoryFragment.newInstance(phrases,false,R.color.category_phrases);
            case 4:
                return CategoryFragment.newInstance(occupation,true,R.color.category_occupation);
            case 5:
                return CategoryFragment.newInstance(hobbies, true,R.color.category_hobby);
            default:
                return new SongFragment();
        }
    }

    @Override
    public int getCount() {
        return 7;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitle[position];
    }
}
