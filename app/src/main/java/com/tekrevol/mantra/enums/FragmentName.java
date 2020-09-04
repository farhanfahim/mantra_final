package com.tekrevol.mantra.enums;

public enum FragmentName {
    ScheduledMantra,
    SharedMantra,
    FavouriteMantra,
    HomeFragment,
    DraftMantra,
    CategoryViewAllFragment,
    MovieLinesSeeAllFragment,
    ProfileSeeAllFragment,
    SearchFragment,
    AlarmSelectionFragment,
    AddMantraFragment;

    public String canonicalForm() {
        return this.name().toLowerCase();
    }

    public static FragmentName fromCanonicalForm(String canonical) {
        return valueOf(FragmentName.class, canonical.toUpperCase());
    }
}