package com.passwordboss.android.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.google.common.base.Strings;
import com.passwordboss.android.R;
import com.passwordboss.android.database.DatabaseHelperSecure;
import com.passwordboss.android.database.beans.Folder;
import com.passwordboss.android.database.bll.FolderBll;
import com.passwordboss.android.event.AddItemEvent;
import com.passwordboss.android.event.FolderClickEvent;
import com.passwordboss.android.event.FoldersRefreshEvent;
import com.passwordboss.android.model.ItemType;
import com.passwordboss.android.rx.ApplySchedulers;
import com.passwordboss.android.toolbar.AppToolbar;
import com.passwordboss.android.utils.Pref;
import com.passwordboss.android.widget.RecyclerExtView;
import com.passwordboss.android.widget.decoration.HorizontalDividerItemDecoration;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;

public class FolderListFragment extends ToolbarFragment {
    private static final String KEY_PARAMS = "keyParams";
    @Bind(R.id.fr_fl_label_empty)
    TextView mEmptyView;
    @Bind(R.id.fr_fl_button_new)
    FloatingActionButton mNewButton;
    @Bind(R.id.fr_fl_recycler)
    RecyclerExtView mRecyclerView;
    private FolderAdapter mAdapter;

    public static FolderListFragment newInstance(@Nullable Params params) {
        FolderListFragment fragment = new FolderListFragment();
        Bundle arguments = new Bundle();
        arguments.putSerializable(KEY_PARAMS, params);
        fragment.setArguments(arguments);
        return fragment;
    }

    @NonNull
    private Params getParams() {
        Params params = null;
        Bundle arguments = getArguments();
        if (null != arguments) {
            params = (Params) getArguments().getSerializable(KEY_PARAMS);
        }
        if (null == params) params = new Params(); //default
        return params;
    }

    @Override
    protected void invalidateToolbar(@NonNull AppToolbar toolbar) {
        super.invalidateToolbar(toolbar);
        toolbar.setTitle(R.string.ManageFolders);
        if (isPhone()) {
            toolbar.displayUpNavigation();
        }

    }

    private void loadData() {
        DatabaseHelperSecure
                .getObservable()
                .flatMap(helperSecure -> {
                    try {
                        FolderBll folderBll = new FolderBll(DatabaseHelperSecure.getHelper(getContext(), Pref.DATABASE_KEY));
                        return Observable.just(folderBll.getAllActive());
                    } catch (SQLException e) {
                        return Observable.error(e);
                    }
                })
                .compose(new ApplySchedulers<>())
                .subscribe(folders -> {
                    mAdapter.setData(folders);
                    if (getParams().mExpandChildren) mAdapter.expandAll();
                });
    }

    @OnClick(R.id.fr_fl_button_new)
    void onClickButtonNew() {
        EventBus.getDefault().post(new AddItemEvent(ItemType.Folder));
    }

    @SuppressLint("InflateParams")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_folder_list, null);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(FoldersRefreshEvent event) {
        loadData();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        HorizontalDividerItemDecoration itemDecoration = new HorizontalDividerItemDecoration(getContext(), R.color.horizontal_divider_grey, R.dimen.dp_1);
        int padding = getResources().getDimensionPixelSize(R.dimen.padding);
        itemDecoration.setPadding(padding, padding);
        mRecyclerView.addItemDecoration(itemDecoration);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        mRecyclerView.setEmptyView(mEmptyView);
        Params params = getParams();
        mAdapter = new FolderAdapter(params.mToExclude);
        mRecyclerView.setAdapter(mAdapter);
        mNewButton.setVisibility(params.mCreationAllowed ? View.VISIBLE : View.GONE);
        loadData();
    }

    public static class Params implements Serializable {
        private boolean mCreationAllowed = true;
        private boolean mExpandChildren = true;
        private Folder mToExclude;

        private Params() {
        }


        public static class Builder {
            private Params mParams = new Params();

            public Builder allowCreate(boolean allow) {
                mParams.mCreationAllowed = allow;
                return this;
            }

            public Params create() {
                return mParams;
            }

            public Builder expandChildren(boolean expand) {
                mParams.mExpandChildren = expand;
                return this;
            }

            public Builder toExclude(Folder folder) {
                mParams.mToExclude = folder;
                return this;
            }

        }

    }

    private static class FolderAdapter extends RecyclerView.Adapter<ViewHolder> {
        private final ArrayList<Node> mData = new ArrayList<>();
        private final View.OnClickListener mOnClickListener = v -> {
            Folder folder = (Folder) v.getTag();
            if (null == folder) return;
            EventBus.getDefault().postSticky(new FolderClickEvent(folder));
        };
        private final CompoundButton.OnCheckedChangeListener mOnCheckedChangeListener = (buttonView, isChecked) -> {
            Node node = (Node) buttonView.getTag();
            if (isChecked) {
                addChildren(node);
            } else {
                removeChildren(node);
            }
            node.mExpanded = isChecked; // node.mExpanded applies on removeChildren

        };
        @Nullable
        private Folder mToExclude;

        private FolderAdapter(@Nullable Folder toExclude) {
            mToExclude = toExclude;
        }

        private void addChildren(Node node) {
            int position = getNodePosition(node.mId);
            if (0 == node.mChildren.size()) return;
            mData.addAll(position + 1, node.mChildren);
            notifyDataSetChanged();
        }

        private void excludeFolder(ArrayList<Node> nodes, String id) {
            if (Strings.isNullOrEmpty(id)) return;
            for (int i = 0; i < nodes.size(); i++) {
                Node node = nodes.get(i);
                if (id.equals(node.mId)) {
                    nodes.remove(i);
                    return;
                }
                excludeFolder(node.mChildren, id);
            }
        }

        public void expandAll() {
            //noinspection StatementWithEmptyBody
            while (expandFirstNodeWithChildren()) {
            }
        }

        private boolean expandFirstNodeWithChildren() {
            for (Node node : mData) {
                if (node.mExpanded) continue;
                if (node.mChildren.size() == 0) continue;
                node.mExpanded = true;
                addChildren(node);
                return true; // run again to expand next node with children
            }
            return false; // nothing to expand
        }

        private int getExpandedChildrenCount(Node node) {
            if (!node.mExpanded) return 0;
            int count = node.mChildren.size();
            for (Node child : node.mChildren) {
                count += getExpandedChildrenCount(child);
            }
            return count;
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }

        private int getNodePosition(String id) {
            for (int i = 0; i < mData.size(); i++) {
                Node node = mData.get(i);
                if (id.equals(node.mId)) return i;
            }
            return -1;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Node node = mData.get(position);
            int padding = holder.mContentView.getResources().getDimensionPixelSize(R.dimen.padding);
            holder.mTitleView.setText(node.mFolder.getName());
            holder.mTitleView.setPadding(node.mLevel * padding, 0, 0, 0);
            holder.mContentView.setTag(node.mFolder);
            holder.mExpandedView.setTag(node);
            holder.mExpandedView.setVisibility(node.mChildren.size() == 0 ? View.GONE : View.VISIBLE);
            holder.mExpandedView.setOnCheckedChangeListener(null);
            holder.mExpandedView.setChecked(node.mExpanded);
            holder.mExpandedView.setOnCheckedChangeListener(mOnCheckedChangeListener);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater
                    .from(parent.getContext())
                    .inflate(R.layout.item_folder, parent, false);
            view.setOnClickListener(mOnClickListener);
            return new ViewHolder(view);
        }

        private void removeChildren(Node node) {
            int position = getNodePosition(node.mId);
            position++; // children start from next position
            int count = getExpandedChildrenCount(node);
            for (int i = 0; i < count; i++) {
                mData.get(position).mExpanded = false;
                mData.remove(position);
            }
            notifyDataSetChanged();
        }

        public void setData(List<Folder> folders) {
            mData.clear();
            HashMap<String, Node> hashMap = new HashMap<>();
            for (Folder folder : folders) {
                String id = folder.getId();
                Node node = hashMap.get(id);
                if (null == node) {
                    node = new Node(folder);
                    hashMap.put(id, node);
                }
                Folder parent = folder.getParent();
                if (null == parent) {
                    mData.add(node); // root
                    continue;
                }
                String parentId = parent.getId();
                Node parentNode = hashMap.get(parentId);
                if (null == parentNode) {
                    parentNode = new Node(parent);
                    hashMap.put(parentId, parentNode);
                }
                parentNode.addChild(node);
            }
            updateLevel(0, mData);
            if (null != mToExclude) {
                excludeFolder(mData, mToExclude.getId());
            }
            notifyDataSetChanged();
        }

        private void updateLevel(int level, ArrayList<Node> nodes) {
            for (Node node : nodes) {
                node.mLevel = level;
                updateLevel(level + 1, node.mChildren);
            }
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView mTitleView;
        private final View mContentView;
        private final CheckBox mExpandedView;

        public ViewHolder(View itemView) {
            super(itemView);
            mContentView = itemView;
            mTitleView = (TextView) itemView.findViewById(R.id.it_fl_title);
            mExpandedView = (CheckBox) itemView.findViewById(R.id.it_fl_expanded);
        }
    }

    private static class Node {
        private final Folder mFolder;
        private final String mId;
        private ArrayList<Node> mChildren = new ArrayList<>();
        private boolean mExpanded = false;
        private int mLevel = 0;

        public Node(Folder folder) {
            mFolder = folder;
            mId = folder.getId();
        }

        public void addChild(Node child) {
            mChildren.add(child);
        }
    }
}
