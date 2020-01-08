package ru.imlocal.ui.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getColor
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import kotlinx.android.synthetic.main.fragment_main.*
import ru.imlocal.R

class FragmentMain : Fragment() {

    private lateinit var pagerAdapter: PagerAdapter

    companion object {
        fun newInstance() = FragmentMain()
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        pagerAdapter = PagerAdapter(childFragmentManager)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragment_main_tabs.setupWithViewPager(fragment_main_viewpager)
        fragment_main_viewpager.adapter = pagerAdapter
//       добавить сохранение текущего таба
        setUpLeftRightViewColor(0)
        fragment_main_viewpager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}
            @SuppressLint("MissingSuperCall")
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                setUpLeftRightViewColor(position)
            }
        })

    }

    private fun setUpLeftRightViewColor(position: Int) {
        when (position) {
            0 -> {
                fragment_main_v_left.setBackgroundColor(
                    getColor(
                        context!!,
                        R.color.color_background_tab_button
                    )
                )
                fragment_main_v_right.setBackgroundColor(getColor(context!!, R.color.color_main))
            }
            1 -> {
                fragment_main_v_left.setBackgroundColor(getColor(context!!, R.color.color_main))
                fragment_main_v_right.setBackgroundColor(getColor(context!!, R.color.color_main))
            }
            2 -> {
                fragment_main_v_left.setBackgroundColor(getColor(context!!, R.color.color_main))
                fragment_main_v_right.setBackgroundColor(
                    getColor(
                        context!!,
                        R.color.color_background_tab_button
                    )
                )
            }

        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
    }

}
