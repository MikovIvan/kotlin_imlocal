package ru.imlocal.ui.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import ru.imlocal.ui.actions.FragmentListActions
import ru.imlocal.ui.events.FragmentListEvents
import ru.imlocal.ui.places.FragmentListPlaces

class PagerAdapter(fm: FragmentManager) :
    FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {
        var fragment: Fragment = FragmentListPlaces()
        when (position) {
            0 -> fragment = FragmentListPlaces()
            1 -> fragment = FragmentListActions()
            2 -> fragment = FragmentListEvents()
        }
        return fragment
    }

    override fun getCount(): Int = 3

    override fun getPageTitle(position: Int): CharSequence? {
        var title: String = ""
        when (position) {
            0 -> title = "places"
            1 -> title = "actions"
            2 -> title = "events"
        }
        return title
    }
}