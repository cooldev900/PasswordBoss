package com.passwordboss.android.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;
import com.google.common.base.Strings;
import com.passwordboss.android.R;
import com.passwordboss.android.app.Installation;
import com.passwordboss.android.database.DatabaseHelperSecure;
import com.passwordboss.android.database.beans.Device;
import com.passwordboss.android.database.bll.DeviceBll;
import com.passwordboss.android.rx.BaseObserver;
import com.passwordboss.android.toolbar.AppToolbar;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class MyDevicesFragment extends ToolbarFragment {


    @Bind(R.id.fr_rc_recycler)
    RecyclerView mRecyclerView;
    private DevicesAdapter mAdapter;

    private void confirmDeleteDevice(Device device) {
        new AlertDialog.Builder(getContext())
                .setTitle(R.string.DeleteDevice)
                .setMessage(R.string.DeletingThisDevice)
                .setNegativeButton(R.string.Cancel, (dialog, which) -> {
                    mAdapter.closeAllItems();
                })
                .setPositiveButton(R.string.Delete, (dialogInterface, i) -> {
                    DatabaseHelperSecure
                            .getObservable()
                            .map(helperSecure -> {
                                try {
                                    new DeviceBll(helperSecure).deleteDevice(device);
                                    return true;
                                } catch (SQLException e) {
                                    throw new IllegalStateException(e);
                                }
                            })
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new BaseObserver<Boolean>(getContext()) {
                                @Override
                                public void onNext(Boolean aBoolean) {
                                    loadData();
                                }
                            });
                })
                .setOnCancelListener(listener -> mAdapter.closeAllItems())
                .show();
    }

    @Override
    protected void invalidateToolbar(@NonNull AppToolbar toolbar) {
        super.invalidateToolbar(toolbar);
        if (isPhone()) {
            toolbar.displayUpNavigation();
        } else {
            toolbar.displayCloseNavigation();
        }
        toolbar.setTitle(R.string.MyDevices);
    }

    private void loadData() {
        DatabaseHelperSecure
                .getObservable()
                .map(helperSecure -> {
                    try {
                        return new DeviceBll(helperSecure).getActiveDevices();
                    } catch (SQLException e) {
                        throw new IllegalStateException(e);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(devices -> {
                    mAdapter.setData(devices);
                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recycler, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setPadding(0, 0, 0, 0);
        mAdapter = new DevicesAdapter(getContext(), this::confirmDeleteDevice);
        mRecyclerView.setAdapter(mAdapter);
        loadData();
    }

    public static class DevicesAdapter extends RecyclerSwipeAdapter<RecyclerView.ViewHolder> {
        private final static int VIEW_TYPE_HEADER = 0;
        private final static int VIEW_TYPE_DEVICE = 1;
        private static final DateTimeFormatter FORMATTER = DateTimeFormat.mediumDateTime();
        private final List<Device> mDevices = new ArrayList<>();
        private final CharSequence mHeaderCurrentDevice;
        private final CharSequence mHeaderOtherDevice;
        private final CharSequence mHeaderLastUsed;
        private final OnDeleteDeviceListener mOnDeleteDeviceListener;
        private Device mDeviceCurrent;
        private View.OnClickListener mOnDeleteClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Device device = (Device) v.getTag();
                if (null == device || null == mOnDeleteDeviceListener) return;
                mOnDeleteDeviceListener.onDeleteDevice(device);
            }
        };

        public DevicesAdapter(Context context, OnDeleteDeviceListener onDeleteDeviceListener) {
            mOnDeleteDeviceListener = onDeleteDeviceListener;
            mHeaderCurrentDevice = context.getText(R.string.CurrentDevice);
            mHeaderOtherDevice = context.getText(R.string.OtherDevice);
            mHeaderLastUsed = context.getText(R.string.LastUsed);
        }

        @Override
        public int getItemCount() {
            if (mDeviceCurrent == null) return 0;
            int count = 2; // header + current
            if (mDevices.size() > 0) {
                count += 1 + mDevices.size(); // header + other
            }
            return count;
        }

        @Override
        public int getItemViewType(int position) {
            switch (position) {
                case 0:
                case 2:
                    return VIEW_TYPE_HEADER;
                default:
                    return VIEW_TYPE_DEVICE;
            }
        }

        private int getResId(Device device) {
            String os = device.getOs();
            if (Strings.isNullOrEmpty(os)) return 0;
            int resId;
            switch (os) {
                case Device.OS_ANDROID: {
                    resId = R.drawable.ic_android;
                    break;
                }
                case Device.OS_IOS: {
                    resId = R.drawable.ic_apple;
                    break;
                }
                case Device.OS_WINDOWS: {
                    resId = R.drawable.ic_windows;
                    break;
                }
                default: {
                    resId = 0;
                }
            }
            return resId;
        }

        @Override
        public int getSwipeLayoutResourceId(int i) {
            return R.id.it_od_swipe;
        }

        public void onBindViewDeviceHolder(RecyclerView.ViewHolder viewHolder, int position) {
            mItemManger.bindView(viewHolder.itemView, position);
            Device device = position == 1 ? mDeviceCurrent : mDevices.get(position - 3);
            DeviceViewHolder deviceViewHolder = (DeviceViewHolder) viewHolder;
            int resId = getResId(device);
            if (resId > 0) {
                deviceViewHolder.imgDeviceThumbnail.setImageResource(resId);
            } else {
                deviceViewHolder.imgDeviceThumbnail.setImageDrawable(null);
            }
            deviceViewHolder.txtDeviceName.setText(device.getNickname());
            DateTime dateTime = new DateTime(device.getLastModifiedDate());
            deviceViewHolder.txtLastActive.setText(dateTime.toString(FORMATTER));
            deviceViewHolder.btnRemove.setTag(device);
            deviceViewHolder.btnRemove.setOnClickListener(mOnDeleteClickListener);
            if (device == mDeviceCurrent) {
                deviceViewHolder.mSwipeView.setSwipeEnabled(false);
                deviceViewHolder.txtLastActive.setVisibility(View.GONE);
            } else {
                deviceViewHolder.mSwipeView.setSwipeEnabled(true);
                deviceViewHolder.txtLastActive.setVisibility(View.VISIBLE);
            }
        }


        public void onBindViewHeaderHolder(RecyclerView.ViewHolder viewHolder, CharSequence left, CharSequence right) {
            HeaderViewHolder headerViewHolder = (HeaderViewHolder) viewHolder;
            headerViewHolder.mLeftLabelView.setText(left);
            headerViewHolder.mLeftLabelView.setVisibility(left == null ? View.GONE : View.VISIBLE);
            headerViewHolder.mRightLabelView.setText(right);
            headerViewHolder.mRightLabelView.setVisibility(right == null ? View.GONE : View.VISIBLE);

        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
            switch (position) {
                case 0:
                    onBindViewHeaderHolder(viewHolder, mHeaderCurrentDevice, null);
                    break;
                case 1:
                    onBindViewDeviceHolder(viewHolder, position);
                    break;
                case 2:
                    onBindViewHeaderHolder(viewHolder, mHeaderOtherDevice, mHeaderLastUsed);
                    break;
                default:
                    onBindViewDeviceHolder(viewHolder, position);
                    break;
            }
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == VIEW_TYPE_HEADER) {
                return new HeaderViewHolder(LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.item_simple_header, parent, false));
            } else {
                return new DeviceViewHolder(LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.item_device, parent, false));
            }
        }

        public void setData(List<Device> devices) {
            closeAllItems();
            mDevices.clear();
            mDeviceCurrent = null;
            final String uuid = Strings.nullToEmpty(new Installation().getUuid());
            for (Device device : devices) {
                if (uuid.equals(device.getInstallationId())) {
                    mDeviceCurrent = device;
                } else {
                    mDevices.add(device);
                }
            }
            if (mDeviceCurrent == null) {
                mDeviceCurrent = new Device();
                mDeviceCurrent.setNickname("Error: Unknown device");
            }
            notifyDataSetChanged();
        }

        interface OnDeleteDeviceListener {
            void onDeleteDevice(Device device);
        }

        public static class DeviceViewHolder extends RecyclerView.ViewHolder {
            private final SwipeLayout mSwipeView;
            public View btnRemove;
            public ImageView imgDeviceThumbnail;
            public TextView txtDeviceName;
            public TextView txtLastActive;

            public DeviceViewHolder(View itemView) {
                super(itemView);
                mSwipeView = (SwipeLayout) itemView.findViewById(R.id.it_od_swipe);
                imgDeviceThumbnail = (ImageView) itemView.findViewById(R.id.it_od_device_thumbnail);
                txtDeviceName = (TextView) itemView.findViewById(R.id.it_od_device_name);
                txtLastActive = (TextView) itemView.findViewById(R.id.it_od_last_activity);
                btnRemove = itemView.findViewById(R.id.in_sd_delete);
            }
        }

        public static class HeaderViewHolder extends RecyclerView.ViewHolder {

            final public TextView mLeftLabelView;
            final public TextView mRightLabelView;

            public HeaderViewHolder(View itemView) {
                super(itemView);
                mLeftLabelView = (TextView) itemView.findViewById(R.id.header_text_left);
                mRightLabelView = (TextView) itemView.findViewById(R.id.header_text_right);
            }
        }
    }
}
