package com.passwordboss.android.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.format.DateUtils;
import android.view.View;
import android.view.ViewGroup;

import com.instabug.library.Instabug;
import com.passwordboss.android.R;
import com.passwordboss.android.database.bll.SecureItemBll;
import com.passwordboss.android.dialog.alert.DeleteItemDialog;
import com.passwordboss.android.dialog.alert.DiscardChangesDialog;
import com.passwordboss.android.event.AddItemEvent;
import com.passwordboss.android.event.ChangeMasterPasswordEvent;
import com.passwordboss.android.event.ChangePinEvent;
import com.passwordboss.android.event.ChooseAvatarEvent;
import com.passwordboss.android.event.ChooseIconColorStartEvent;
import com.passwordboss.android.event.FolderChangedEvent;
import com.passwordboss.android.event.FolderChooseEvent;
import com.passwordboss.android.event.FolderClickEvent;
import com.passwordboss.android.event.FolderManageEvent;
import com.passwordboss.android.event.FoldersRefreshEvent;
import com.passwordboss.android.event.ForceChangePinEvent;
import com.passwordboss.android.event.GeneratePasswordEvent;
import com.passwordboss.android.event.IdentitiesRefreshEvent;
import com.passwordboss.android.event.IdentityChangedEvent;
import com.passwordboss.android.event.IdentityViewEvent;
import com.passwordboss.android.event.ItemTypeAddEvent;
import com.passwordboss.android.event.PinResultEvent;
import com.passwordboss.android.event.RegisteredDevicesEvent;
import com.passwordboss.android.event.SecureItemChangedEvent;
import com.passwordboss.android.event.SecureItemChooseEvent;
import com.passwordboss.android.event.SecureItemClickEvent;
import com.passwordboss.android.event.SecureItemDeleteEvent;
import com.passwordboss.android.event.SecureItemEditEvent;
import com.passwordboss.android.event.SecureItemSavedEvent;
import com.passwordboss.android.event.SecureItemsRefreshEvent;
import com.passwordboss.android.event.SelectSettingEvent;
import com.passwordboss.android.event.SettingItemResultEvent;
import com.passwordboss.android.event.TwoStepVerificationWizardFinishEvent;
import com.passwordboss.android.event.TwoStepVerificationWizardNextStepEvent;
import com.passwordboss.android.event.UpgradeAccountEvent;
import com.passwordboss.android.fragment.BackPressConsumer;
import com.passwordboss.android.fragment.BaseFragment;
import com.passwordboss.android.fragment.ChangePinFragment;
import com.passwordboss.android.fragment.Changeable;
import com.passwordboss.android.fragment.DigitalWalletFragment;
import com.passwordboss.android.fragment.EmergencyFragment;
import com.passwordboss.android.fragment.FolderFragment;
import com.passwordboss.android.fragment.FolderListFragment;
import com.passwordboss.android.fragment.IdentitiesFragment;
import com.passwordboss.android.fragment.IdentityFragment;
import com.passwordboss.android.fragment.MyDevicesFragment;
import com.passwordboss.android.fragment.NewInstanceFragment;
import com.passwordboss.android.fragment.PasswordGeneratorFragment;
import com.passwordboss.android.fragment.PasswordsFragment;
import com.passwordboss.android.fragment.PersonaInfoFragment;
import com.passwordboss.android.fragment.SecureItemFragment;
import com.passwordboss.android.fragment.SecureNotesFragment;
import com.passwordboss.android.fragment.SettingItemListFragment;
import com.passwordboss.android.fragment.SettingsFragment;
import com.passwordboss.android.fragment.SharedCenterFragment;
import com.passwordboss.android.fragment.TwoStepVerificationFragment;
import com.passwordboss.android.fragment.dashboard.DashboardFragment;
import com.passwordboss.android.fragment.dashboard.DashboardStripFragment;
import com.passwordboss.android.helper.SoftKeyboard;
import com.passwordboss.android.helper.ToDo;
import com.passwordboss.android.model.ItemType;
import com.passwordboss.android.rx.ApplySchedulers;
import com.passwordboss.android.task.SyncTask;
import com.passwordboss.android.toolbar.AppToolbar;
import com.passwordboss.android.toolbar.ToolbarOwner;
import com.passwordboss.android.utils.Pref;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AutoLockActivity implements ToolbarOwner {
    public static final String KEY_INSTABUG_SHOWN = "keyInstabugShown";
    @Bind(R.id.ac_mn_pane_first)
    @Nullable
    ViewGroup mFirstPaneView;
    @Bind(R.id.ac_mn_toolbar_first)
    @Nullable
    AppToolbar mFirstToolbar;
    @Bind(R.id.ac_mn_pane_second)
    @Nullable
    ViewGroup mSecondPaneView;
    @Bind(R.id.ac_mn_toolbar_second)
    @Nullable
    AppToolbar mSecondToolbar;
    @Bind(R.id.ac_mn_pane_third)
    @Nullable
    ViewGroup mThirdPaneView;
    @Bind(R.id.ac_mn_toolbar_third)
    @Nullable
    AppToolbar mThirdToolbar;
    @Bind(R.id.ac_mn_toolbars_overlaed)
    @Nullable
    View mToolbarsOverlaid; // only at tablet portrait

    public static void start(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

    private boolean clearThirdPanel(boolean handleChanges) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = getPaneFragment(R.id.ac_mn_pane_third);
        if (null == fragment) return false;
        if (handleChanges && hasPaneChanges(R.id.ac_mn_pane_third)) {
            new DiscardChangesDialog(this).show(() -> clearThirdPanel(false));
            return true;
        }
        new SoftKeyboard().hide(this); //
        fragmentManager.beginTransaction().remove(fragment).commitAllowingStateLoss();
        fragmentManager.executePendingTransactions();
        if (null != mThirdToolbar) {
            mThirdToolbar.reset();
            if (isToolbarsOverlaid()) {
                mThirdToolbar.setVisibility(View.INVISIBLE);
            }
        }
        return true;
    }

    private Fragment getPaneFragment(@IdRes int id) {
        return getSupportFragmentManager().findFragmentById(id);
    }

    @Nullable
    @Override
    public AppToolbar getToolbar(@IdRes int idRes) {
        if (null != mSecondPaneView && null != mSecondPaneView.findViewById(idRes)) {
            return mSecondToolbar;
        }
        if (null != mThirdPaneView && null != mThirdPaneView.findViewById(idRes)) {
            // handle rotation change from landscape to portrait mode
            // 3rd toolbar is invisible by default (see xml), but if something want
            // to use toolbar, then just show it
            if (null != mThirdToolbar && mThirdToolbar.getVisibility() == View.INVISIBLE) {
                mThirdToolbar.setVisibility(View.VISIBLE);
            }
            return mThirdToolbar;
        }
        if (null != mFirstPaneView && null != mFirstPaneView.findViewById(idRes)) {
            return mFirstToolbar;
        }
        return null;
    }

    private boolean hasPaneChanges(@IdRes int id) {
        Fragment fragment = getPaneFragment(id);
        return null != fragment && fragment instanceof Changeable && ((Changeable) fragment).hasChanges();
    }

    private boolean isFragmentSame(@IdRes int idRes, @Nullable Class<? extends BaseFragment> cls) {
        Fragment fragment = getSupportFragmentManager().findFragmentById(idRes);
        return null != fragment && null != cls && fragment.getClass().isAssignableFrom(cls);
    }

    private boolean isToolbarsOverlaid() {
        return null != mToolbarsOverlaid;
    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
            return;
        }
        Fragment fragment = getPaneFragment(R.id.ac_mn_pane_third);
        if (fragment instanceof BackPressConsumer) {
            if (((BackPressConsumer) fragment).onBackPressed()) return;
        }
        if (clearThirdPanel(true)) return;
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        if (!Pref.getValue(this, KEY_INSTABUG_SHOWN, false)) {
            Instabug.showIntroMessage();
            Pref.setValue(this, KEY_INSTABUG_SHOWN, true);
        }
        if (null == savedInstanceState) {
            setupPanesIfNeeded();
        }
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onEvent(IdentityChangedEvent event) {
        EventBus.getDefault().removeStickyEvent(event);
        clearThirdPanel(false);
        EventBus.getDefault().post(new IdentitiesRefreshEvent());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(IdentityViewEvent event) {
        replaceThirdPane(() -> IdentityFragment.newInstance(event.getIdentity().getId()));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(SecureItemSavedEvent event) {
        clearThirdPanel(false);
        EventBus.getDefault().post(new SecureItemsRefreshEvent());
    }

    @Subscribe(sticky = true)
    public void onEvent(SecureItemChangedEvent event) {
        EventBus.getDefault().post(new SecureItemsRefreshEvent());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ChooseIconColorStartEvent event) {
        startActivity(new Intent(this, ChooseIconColorActivity.class));
    }

    @Subscribe
    public void onEvent(FolderClickEvent event) {
        replaceThirdPane(true, () -> FolderFragment.newInstance(event.getFolder()));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(FolderChooseEvent event) {
        startActivity(new Intent(this, ChooseFolderActivity.class));
    }

    @Subscribe
    public void onEvent(DashboardFragment.DashboardEvent event) {
        switch (event.Action) {
            case DigitalWallet:
                showDigitalWallet();
                break;
            case Emergency:
                showEmergency();
                break;
            case Identities:
                showIdentities();
                break;
            case Lock:
                lockApp();
                break;
            case Notifications:
                new ToDo().show(this);
                break;
            case Passwords:
                showPasswords();
                break;
            case PersonalInfo:
                showPersonalInfo();
                break;
            case Search:
                new ToDo().show(this);
                break;
            case SecureBrowser:
                new ToDo().show(this);
                break;
            case SecureNotes:
                showSecureNotes();
                break;
            case Settings:
                showSettings();
                break;
            case ShareCenter:
                showShareCenter();
                break;
        }
    }

    @Subscribe(sticky = true)
    public void onEvent(SecureItemDeleteEvent event) {
        EventBus.getDefault().removeStickyEvent(event);
        new DeleteItemDialog(this).show(() -> SecureItemBll
                .getObservable()
                .map(secureItemBll -> secureItemBll.deleteItemByID(event.getSecureItem().getId()))
                .compose(new ApplySchedulers<>())
                .subscribe(result -> {
                    EventBus.getDefault().post(new SecureItemsRefreshEvent());
                }));
    }

    @Subscribe
    public void onEvent(SecureItemClickEvent event) {
        SecureItemMenuActivity.start(this, event.getSecureItem());
    }

    @Subscribe(sticky = true)
    public void onEvent(SecureItemEditEvent event) {
        EventBus.getDefault().removeStickyEvent(event);
        replaceThirdPane(() -> SecureItemFragment.newInstance(event.getSecureItem()));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ItemTypeAddEvent event) {
        ItemTypesActivity.start(this, event.getItemType());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(FolderManageEvent event) {
        replaceSecondPane(FolderListFragment.class, FolderListFragment::new);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ChangeMasterPasswordEvent event) {
        startActivity(new Intent(this, ChangeMasterPasswordActivity.class));
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onEvent(AddItemEvent event) {
        EventBus.getDefault().removeStickyEvent(event);
        Class<? extends BaseFragment> fragment = null;
        NewInstanceFragment secondPaneNewInstanceFragment = null;
        NewInstanceFragment thirdPaneNewInstanceFragment = null;
        ItemType itemType = event.getItemType();
        switch (itemType) {
            case Identity:
                fragment = IdentitiesFragment.class;
                secondPaneNewInstanceFragment = IdentitiesFragment::new;
                thirdPaneNewInstanceFragment = IdentityFragment::new;
                break;
            case SharedItem:
            case EmergencyContact:
                new ToDo().show(this);
                break;
            case Folder:
                fragment = FolderListFragment.class;
                secondPaneNewInstanceFragment = FolderListFragment::new;
                thirdPaneNewInstanceFragment = FolderFragment::new;
                break;
            default:
                if (ItemType.Password.contains(itemType)) {
                    fragment = PasswordsFragment.class;
                    secondPaneNewInstanceFragment = PasswordsFragment::new;
                } else if (ItemType.DigitalWallet.contains(itemType)) {
                    fragment = DigitalWalletFragment.class;
                    secondPaneNewInstanceFragment = DigitalWalletFragment::new;
                } else if (ItemType.PersonalInfo.contains(itemType)) {
                    fragment = PersonaInfoFragment.class;
                    secondPaneNewInstanceFragment = PersonaInfoFragment::new;
                } else if (ItemType.SecureNotes.contains(itemType)) {
                    fragment = SecureNotesFragment.class;
                    secondPaneNewInstanceFragment = SecureNotesFragment::new;
                }
                thirdPaneNewInstanceFragment = () -> SecureItemFragment.newInstance(itemType);
                break;
        }
        if (null == fragment) return;
        replaceSecondAndThirdPanes(fragment, secondPaneNewInstanceFragment, thirdPaneNewInstanceFragment);

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ChooseAvatarEvent event) {
        startActivity(new Intent(this, ChooseAvatarActivity.class));
    }

    @Subscribe(sticky = true)
    public void onEvent(FolderChangedEvent event) {
        EventBus.getDefault().removeStickyEvent(event);
        clearThirdPanel(false);
        EventBus.getDefault().postSticky(new FoldersRefreshEvent());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(SecureItemChooseEvent event) {
        ChooseSecureItemActivity.start(this, event.getItemType());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(RegisteredDevicesEvent event) {
        replaceThirdPane(MyDevicesFragment::new);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(SelectSettingEvent event) {
        replaceThirdPane(() -> SettingItemListFragment.newInstance(event.getType()));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(SettingItemResultEvent event) {
        clearThirdPanel(true);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(GeneratePasswordEvent event) {
        replaceThirdPane(PasswordGeneratorFragment::new);
    }

    @Subscribe
    public void onEvent(TwoStepVerificationWizardNextStepEvent event) {
        if (event.getStep() == TwoStepVerificationWizardNextStepEvent.Step.Start) {
            replaceThirdPane(TwoStepVerificationFragment::new);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(TwoStepVerificationWizardFinishEvent event) {
        clearThirdPanel(false);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ChangePinEvent event) {
        replaceThirdPane(() -> ChangePinFragment.newInstance(ChangePinFragment.STEP_INPUT_OLD_PIN));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(PinResultEvent event) {
        clearThirdPanel(true);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ForceChangePinEvent event) {
        replaceThirdPane(() -> ChangePinFragment.newInstance(ChangePinFragment.STEP_INPUT_NEW_PIN));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(UpgradeAccountEvent event) {
        UpgradeActivity.start(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        startSyncIfNeeded();
    }

    @Override
    protected boolean onUpPressed() {
        onBackPressed();
        return true;
    }

    private void replaceSecondAndThirdPanes(Class<? extends BaseFragment> secondPaneCls, NewInstanceFragment secondPaneNewInstanceFragment,
                                            NewInstanceFragment thirdPaneNewInstanceFragment) {
        replaceSecondAndThirdPanes(true, secondPaneCls, secondPaneNewInstanceFragment, thirdPaneNewInstanceFragment);
    }

    private void replaceSecondAndThirdPanes(boolean handleChanges,
                                            Class<? extends BaseFragment> secondPaneCls, NewInstanceFragment secondPaneNewInstanceFragment,
                                            NewInstanceFragment thirdPaneNewInstanceFragment) {
        if (handleChanges && hasPaneChanges(R.id.ac_mn_pane_third)) {
            new DiscardChangesDialog(this).show(() -> replaceSecondAndThirdPanes(false, secondPaneCls, secondPaneNewInstanceFragment, thirdPaneNewInstanceFragment));
            return;
        }
        if (!isFragmentSame(R.id.ac_mn_pane_second, secondPaneCls)) {
            replaceSecondPane(false, secondPaneCls, secondPaneNewInstanceFragment);
        }
        replaceThirdPane(false, thirdPaneNewInstanceFragment);
    }

    private void replaceSecondPane(Class<? extends BaseFragment> cls, NewInstanceFragment newInstanceFragment) {
        replaceSecondPane(true, cls, newInstanceFragment);
    }

    private void replaceSecondPane(boolean handleChanges, Class<? extends BaseFragment> cls, NewInstanceFragment newInstanceFragment) {
        if (isFragmentSame(R.id.ac_mn_pane_second, cls)) {
            // tablet portrait mode: app closes 3rd pane, for same fragment at 2nd, see PBA-965 for details
            if (!isToolbarsOverlaid() || isToolbarsOverlaid() && null == getPaneFragment(R.id.ac_mn_pane_third)) {
                return;
            }
        }
        // NOTE: replacing 2nd pane clears 3rd pane, but also cares about its changes
        if (handleChanges && hasPaneChanges(R.id.ac_mn_pane_third)) {
            new DiscardChangesDialog(this).show(() -> replaceSecondPane(false, cls, newInstanceFragment));
            return;
        }
        clearThirdPanel(false);
        if (null != mSecondToolbar) {
            mSecondToolbar.reset();
        }
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.ac_mn_pane_second, newInstanceFragment.create())
                .commitAllowingStateLoss();
    }

    private void replaceThirdPane(boolean handleChanges, NewInstanceFragment newInstanceFragment) {
        if (handleChanges && hasPaneChanges(R.id.ac_mn_pane_third)) {
            new DiscardChangesDialog(this).show(() -> replaceThirdPane(false, newInstanceFragment));
            return;
        }
        if (null != mThirdToolbar) {
            mThirdToolbar.reset();
            mThirdToolbar.setVisibility(View.VISIBLE);
        }
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.ac_mn_pane_third, newInstanceFragment.create())
                .commitAllowingStateLoss();
    }

    private void replaceThirdPane(NewInstanceFragment newInstanceFragment) {
        replaceThirdPane(true, newInstanceFragment);
    }

    private void setupPanesIfNeeded() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (null != mFirstPaneView) {
            transaction.replace(mFirstPaneView.getId(), new DashboardStripFragment());
        }
        if (null != mSecondPaneView) {
            transaction.replace(mSecondPaneView.getId(), new PasswordsFragment());
        }
        if (!transaction.isEmpty()) transaction.commit();
    }

    private void showDigitalWallet() {
        if (null == mSecondPaneView) {
            startActivity(new Intent(this, DigitalWalletActivity.class));
            return;
        }
        replaceSecondPane(DigitalWalletFragment.class, DigitalWalletFragment::new);
    }

    private void showEmergency() {
        if (isPhone()) {
            startActivity(new Intent(this, EmergencyActivity.class));
            return;
        }
        replaceSecondPane(EmergencyFragment.class, EmergencyFragment::new);
    }

    private void showIdentities() {
        if (null == mSecondPaneView) {
            startActivity(new Intent(this, IdentitiesActivity.class));
            return;
        }
        replaceSecondPane(IdentitiesFragment.class, IdentitiesFragment::new);
    }

    private void showPasswords() {
        if (null == mSecondPaneView) {
            startActivity(new Intent(this, PasswordsActivity.class));
            return;
        }
        replaceSecondPane(PasswordsFragment.class, PasswordsFragment::new);
    }

    private void showPersonalInfo() {
        if (null == mSecondPaneView) {
            startActivity(new Intent(this, PersonalInfoActivity.class));
            return;
        }
        replaceSecondPane(PersonaInfoFragment.class, PersonaInfoFragment::new);
    }

    private void showSecureNotes() {
        if (null == mSecondPaneView) {
            startActivity(new Intent(this, SecureNotesActivity.class));
            return;
        }
        replaceSecondPane(SecureNotesFragment.class, SecureNotesFragment::new);
    }

    private void showSettings() {
        if (isPhone()) {
            startActivity(new Intent(this, SettingsActivity.class));
            return;
        }
        replaceSecondPane(SettingsFragment.class, SettingsFragment::new);
    }

    private void showShareCenter() {
        if (isPhone()) {
            startActivity(new Intent(this, SharedCenterActivity.class));
            return;
        }
        replaceSecondPane(SharedCenterFragment.class, SharedCenterFragment::new);
    }

    private void startSyncIfNeeded() {
        long lastSyncTime = Pref.get().getLong(Pref.LAST_SYNC_TIME, 0);
        if (lastSyncTime + DateUtils.MINUTE_IN_MILLIS * 5 > System.currentTimeMillis()) return;
        new SyncTask(this).start();
    }


}
