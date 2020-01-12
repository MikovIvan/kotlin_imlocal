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
        return when (position) {
            0 -> FragmentListPlaces.newInstance()
            1 -> FragmentListActions.newInstance()
            else -> FragmentListEvents.newInstance()
        }
    }

    override fun getCount(): Int = 3

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> "places"
            1 -> "actions"
            else -> "events"
        }
    }
}