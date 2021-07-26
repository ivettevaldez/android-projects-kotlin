package com.ivettevaldez.saturnus.screens.people.main

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.ivettevaldez.saturnus.people.ClientType
import com.ivettevaldez.saturnus.screens.people.list.PeopleListFragment
import javax.inject.Inject

open class PeopleMainPagerAdapter @Inject constructor(
    activity: AppCompatActivity
) : FragmentStateAdapter(activity) {

    companion object {

        const val TAB_CLIENT_TYPE_ISSUING = 0
        const val TAB_CLIENT_TYPE_RECEIVER = 1

        private const val TABS_COUNT = 2
    }

    override fun getItemCount(): Int = TABS_COUNT

    override fun createFragment(position: Int): Fragment {
        val clientType: ClientType.Type = when (position) {
            TAB_CLIENT_TYPE_ISSUING -> ClientType.Type.ISSUING
            TAB_CLIENT_TYPE_RECEIVER -> ClientType.Type.RECEIVER
            else -> throw RuntimeException("@@@@@ Unsupported clientType page: $position")
        }
        return PeopleListFragment.newInstance(clientType)
    }
}