package android.support.v4.app;

public class DialogFragmentExt extends DialogFragment {

    /*
     Use same package as original class to be able to mimic super class implementation.
     Fields use package visibility.
     */

    public void showAllowingStateLoss(FragmentManager manager, String tag) {
        mDismissed = false;
        mShownByMe = true;
        FragmentTransaction ft = manager.beginTransaction();
        ft.add(this, tag);
        ft.commitAllowingStateLoss();
    }
}
