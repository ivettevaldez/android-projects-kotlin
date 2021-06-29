package com.ivettevaldez.unittesting.tutorialandroidapp.screens.common.fragmentframehelper

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

class FragmentFrameHelper(
    private val activity: FragmentActivity,
    private val fragmentFrameWrapper: FragmentFrameWrapper,
    private val fragmentManager: FragmentManager
) {

    fun replaceFragment(newFragment: Fragment) {
        replaceFragment(newFragment, addToBackStack = true, clearBackStack = false)
    }

    fun replaceFragmentDontAddToBackstack(newFragment: Fragment) {
        replaceFragment(newFragment, addToBackStack = false, clearBackStack = false)
    }

    fun replaceFragmentAndClearBackstack(newFragment: Fragment) {
        replaceFragment(newFragment, addToBackStack = false, clearBackStack = true)
    }

    fun navigateUp() {
        // Some navigateUp calls can be "lost" if they happen after the state has been saved
        if (fragmentManager.isStateSaved) {
            return
        }
        val currentFragment: Fragment = getCurrentFragment()
        if (fragmentManager.backStackEntryCount > 0) {

            // In a normal world, just popping back stack would be sufficient, but since android
            // is not normal, a call to popBackStack can leave the popped fragment on screen.
            // Therefore, we start with manual removal of the current fragment.
            // Description of the issue can be found here: https://stackoverflow.com/q/45278497/2463035
            removeCurrentFragment()
            if (fragmentManager.popBackStackImmediate()) {
                return  // navigated "up" in fragments back-stack
            }
        }
        if (HierarchicalFragment::class.java.isInstance(currentFragment)) {
            val parentFragment: Fragment? =
                (currentFragment as HierarchicalFragment).getHierarchicalParentFragment()
            if (parentFragment != null) {
                replaceFragment(parentFragment, addToBackStack = false, clearBackStack = true)
                return  // navigate "up" to hierarchical parent fragment
            }
        }
        if (activity.onNavigateUp()) {
            return  // navigated "up" to hierarchical parent activity
        }
        activity.onBackPressed() // no "up" navigation targets - just treat UP as back press
    }

    private fun getCurrentFragment(): Fragment {
        return fragmentManager.findFragmentById(getFragmentFrameId())!!
    }

    private fun replaceFragment(
        newFragment: Fragment,
        addToBackStack: Boolean,
        clearBackStack: Boolean
    ) {
        if (clearBackStack) {
            if (fragmentManager.isStateSaved) {
                // If the state is saved we can't clear the back stack. Simply not doing this, but
                // still replacing fragment is a bad idea. Therefore we abort the entire operation.
                return
            }
            // Remove all entries from back stack
            fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }
        val ft: FragmentTransaction = fragmentManager.beginTransaction()
        if (addToBackStack) {
            ft.addToBackStack(null)
        }

        // Change to a new fragment
        ft.replace(getFragmentFrameId(), newFragment, null)
        if (fragmentManager.isStateSaved) {
            // We acknowledge the possibility of losing this transaction if the app undergoes
            // save&restore flow after it is committed.
            ft.commitAllowingStateLoss()
        } else {
            ft.commit()
        }
    }

    private fun removeCurrentFragment() {
        val ft: FragmentTransaction = fragmentManager.beginTransaction()
        ft.remove(getCurrentFragment())
        ft.commit()

        // not sure it is needed; will keep it as a reminder to myself if there will be problems
        // mFragmentManager.executePendingTransactions();
    }

    private fun getFragmentFrameId(): Int {
        return fragmentFrameWrapper.getFragmentFrame().getId()
    }
}