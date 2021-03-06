package com.example.tamagodowork.bottomNav.pet.online;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.widget.TextViewCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.tamagodowork.MainActivity;
import com.example.tamagodowork.R;
import com.example.tamagodowork.authentication.RegisterAct;
import com.example.tamagodowork.bottomNav.pet.Pet;
import com.example.tamagodowork.bottomNav.pet.ProfilePicView;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class OnlineActivity extends AppCompatActivity {

    private enum AdapterType { FRIENDS, RECEIVED, SENT }

    private CollectionReference userData;

    private FriendAdapter friendsAdapter = null;
    private FriendAdapter receivedAdapter = null;
    private FriendAdapter sentAdapter = null;

    private CurrentUser currentUser;
    private String currUserId;

    private final List<PetUser> friendsUserList = new ArrayList<>();
    private final List<PetUser> receivedUserList = new ArrayList<>();
    private final List<PetUser> sentUserList = new ArrayList<>();

    private final List<Task<DocumentSnapshot>> friendsTaskList = new ArrayList<>();
    private final List<Task<DocumentSnapshot>> receivedTaskList = new ArrayList<>();
    private final List<Task<DocumentSnapshot>> sentTaskList = new ArrayList<>();

    private final List<Task<?>> viewpagerTaskList = new ArrayList<>();

    private static final LinearLayout.LayoutParams selectedParams = new LinearLayout.LayoutParams(
            0,
            LinearLayout.LayoutParams.MATCH_PARENT,
            1f / 3);

    private static final LinearLayout.LayoutParams unselectedParams = new LinearLayout.LayoutParams(
            0,
            LinearLayout.LayoutParams.MATCH_PARENT,
            1f / 3);

    public void setTextView(View view, boolean selected) {
        if (!(view instanceof TextView)) return;

        TextView textView = (TextView) view;
        if (selected) {
            TextViewCompat.setTextAppearance(textView,R.style.selectedTextViewTab);
            textView.getCompoundDrawables()[1].setTint(
                    ContextCompat.getColor(OnlineActivity.this, R.color.peach));
            textView.setLayoutParams(selectedParams);
        } else {
            TextViewCompat.setTextAppearance(textView,R.style.unselectedTextViewTab);
            textView.getCompoundDrawables()[1].setTint(
                    ContextCompat.getColor(OnlineActivity.this, R.color.grey));
            textView.setLayoutParams(unselectedParams);
        }
    }

    static {
        selectedParams.setMargins(0, 0, 0, 0);
        unselectedParams.setMargins(0, 25, 0, 0);
    }

    private int selectedIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online);


        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        // check if logged in
        if (firebaseAuth.getCurrentUser() == null) {
            startActivity(new Intent(OnlineActivity.this, RegisterAct.class));
            finish(); return;
        }
        currUserId = firebaseAuth.getCurrentUser().getUid();
        userData = FirebaseFirestore.getInstance().collection("Users");
        DocumentReference userDoc = userData.document(currUserId);



        LinearLayout tabLayout = findViewById(R.id.friend_tab_layout);
        setTextView(tabLayout.getChildAt(0), true);
        setTextView(tabLayout.getChildAt(1), false);
        setTextView(tabLayout.getChildAt(2), false);


        ViewPager viewPager = findViewById(R.id.friends_viewpager);
        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                setTextView(tabLayout.getChildAt(selectedIndex), false);
                selectedIndex = viewPager.getCurrentItem();
                setTextView(tabLayout.getChildAt(selectedIndex), true);
            }
        });


        for (int i = 0; i < tabLayout.getChildCount(); i++) {
            View child = tabLayout.getChildAt(i);

            final int tmpIndex = i;

            child.setOnClickListener(v -> {
                setTextView(tabLayout.getChildAt(selectedIndex), false);
                viewPager.setCurrentItem(tmpIndex);
                setTextView(child, true);
                selectedIndex = tmpIndex;
            });
        }



        // get all the data
        userDoc.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot == null) {
                MainActivity.backToMain(OnlineActivity.this); return;
            }

            currentUser = documentSnapshot.toObject(CurrentUser.class);
            if (currentUser == null) return;

            friendsUserList.clear();
            receivedUserList.clear();
            sentUserList.clear();

            // friends
            List<String> keyList = currentUser.getFriendsList();
            if (keyList != null) {
                for (String key : keyList) {
                    friendsTaskList.add(userData.document(key).get().addOnSuccessListener(documentSnapshot1 ->
                            friendsUserList.add(new PetUser(key,
                                    documentSnapshot1.get("Name", String.class),
                                    documentSnapshot1.get("XP", Integer.class),
                                    userData.document(key)))));
                }

                viewpagerTaskList.add(Tasks.whenAllComplete(friendsTaskList).addOnCompleteListener(task ->{
                    Collections.sort(friendsUserList);
                    friendsAdapter = new FriendAdapter(friendsUserList, AdapterType.FRIENDS);
                }));
            }


            // received requests
            keyList = currentUser.getReceivedRequests();
            if (keyList != null) {
                for (String key: keyList) {
                    receivedTaskList.add(userData.document(key).get().addOnSuccessListener(documentSnapshot1 ->
                            receivedUserList.add(new PetUser(key,
                                    documentSnapshot1.get("Name", String.class),
                                    documentSnapshot1.get("XP", Integer.class),
                                    userData.document(key)))));
                }

                viewpagerTaskList.add(Tasks.whenAll(receivedTaskList).addOnCompleteListener(task ->{
                    Collections.sort(receivedUserList);
                    receivedAdapter = new FriendAdapter(receivedUserList, AdapterType.RECEIVED);
                }));
            }


            // sent requests
            keyList = currentUser.getSentRequests();
            if (keyList != null) {
                for (String key: keyList) {
                    sentTaskList.add(userData.document(key).get().addOnSuccessListener(documentSnapshot1 ->
                            sentUserList.add(new PetUser(key,
                                    documentSnapshot1.get("Name", String.class),
                                    documentSnapshot1.get("XP", Integer.class),
                                    userData.document(key)))));
                }

                viewpagerTaskList.add(Tasks.whenAll(sentTaskList).addOnCompleteListener(task -> {
                    Collections.sort(sentUserList);
                    sentAdapter = new FriendAdapter(sentUserList, AdapterType.SENT);
                }));
            }


            // When all the tasks are finished, set up the viewpager
            Tasks.whenAll(viewpagerTaskList).addOnCompleteListener(task -> {
                List<FriendAdapter> tmp = new ArrayList<>();
                tmp.add(friendsAdapter);
                tmp.add(receivedAdapter);
                tmp.add(sentAdapter);
                ViewpagerAdapter viewpagerAdapter = new ViewpagerAdapter(OnlineActivity.this, tmp);
                viewPager.setAdapter(viewpagerAdapter);
            });
        });


        Button findFriendsButton = findViewById(R.id.online_find_friends);
        findFriendsButton.setOnClickListener(v -> {
            startActivity(new Intent(OnlineActivity.this, AddFriendActivity.class));
            finish();
        });

        Button homeButton = findViewById(R.id.online_home);
        homeButton.setOnClickListener(v -> MainActivity.backToMain(OnlineActivity.this));
    }


    /**
     * Adapter for the viewpager.
     * Each fragment has a recyclerview.
     */
    private static class ViewpagerAdapter extends PagerAdapter {

        private final Context context;
        private final List<FriendAdapter> adapterList;

        public ViewpagerAdapter(Context context, List<FriendAdapter> adapterList) {
            this.context = context;
            this.adapterList = adapterList;
        }

        @NonNull @NotNull @Override
        public Object instantiateItem(@NonNull @NotNull ViewGroup container, int position) {
            FriendAdapter currAdapter = this.adapterList.get(position);

            if (currAdapter != null) {
                RecyclerView recyclerView = new RecyclerView(this.context);
                recyclerView.setLayoutManager(new LinearLayoutManager(this.context));
                DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this.context,
                        DividerItemDecoration.VERTICAL);
                recyclerView.addItemDecoration(dividerItemDecoration);
                recyclerView.setAdapter(currAdapter);

                container.addView(recyclerView, 0);
                return recyclerView;
            } else {
                return container;
            }
        }

        @Override
        public void destroyItem(@NonNull @NotNull ViewGroup container, int position, @NonNull @NotNull Object object) { container.removeView((View) object); }

        @Override
        public int getCount() {
            return this.adapterList.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull @NotNull View view, @NonNull @NotNull Object object) { return view.equals(object); }
    }




    /**
     * Adapter for the recycler view in the viewpager fragments.
     */
    public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.ViewHolder> {

        private final List<PetUser> userList;
        private final AdapterType adapterType;

        public FriendAdapter(List<PetUser> userList, AdapterType adapterType) {
            this.userList = userList;
            this.adapterType = adapterType;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView nameTextView;
            TextView levelTextView;
            PetUser user;
            Pet pet;
            ImageButton expandButton;
            ConstraintLayout expandableLayout;
            RelativeLayout profilePic;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                nameTextView = itemView.findViewById(R.id.friend_name_text_view);
                levelTextView = itemView.findViewById(R.id.friend_level_text_view);
                expandButton = itemView.findViewById(R.id.friend_expand_button);
                expandableLayout = itemView.findViewById(R.id.friend_expandable_layout);
                expandableLayout.setVisibility(View.GONE);
                profilePic = itemView.findViewById(R.id.friend_profile_pic);

                expandButton.setOnClickListener(v -> {
                    if (user == null) return;
                    user.setExpanded();
                    FriendAdapter.this.notifyItemChanged(getAbsoluteAdapterPosition());
                });
            }
        }

        public class FriendViewHolder extends ViewHolder {
            Button visitButton, removeButton;

            public FriendViewHolder(@NonNull View itemView) {
                super(itemView);
                visitButton = itemView.findViewById(R.id.friend_visit_button);
                removeButton = itemView.findViewById(R.id.friend_item_remove_button);
            }
        }

        public class ReceivedViewHolder extends ViewHolder {
            ImageButton acceptButton, declineButton;

            public ReceivedViewHolder(@NonNull View itemView) {
                super(itemView);
                acceptButton = itemView.findViewById(R.id.friend_request_accept);
                declineButton = itemView.findViewById(R.id.friend_request_decline);
            }
        }

        public class SentViewHolder extends ViewHolder {
            TextView unsendTextView;

            public SentViewHolder(@NonNull View itemView) {
                super(itemView);
                unsendTextView = itemView.findViewById(R.id.friend_request_unsend);
            }
        }


        @Override
        public int getItemViewType(int position) { return 0; }

        @NonNull
        @Override
        public FriendAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            switch (this.adapterType) {
                case FRIENDS: {
                    return new FriendAdapter.FriendViewHolder(
                            LayoutInflater.from(OnlineActivity.this).inflate(R.layout.friend_item, parent, false)); }
                case RECEIVED: {
                    return new FriendAdapter.ReceivedViewHolder(
                            LayoutInflater.from(OnlineActivity.this).inflate(R.layout.friend_request_received_item, parent, false)); }
                case SENT: {
                    return new FriendAdapter.SentViewHolder(
                            LayoutInflater.from(OnlineActivity.this).inflate(R.layout.friend_request_sent_item, parent, false)); }
                default: {
                    return new FriendAdapter.ViewHolder(
                            LayoutInflater.from(OnlineActivity.this).inflate(R.layout.friend_item, parent, false)); }
            }
        }

        @Override
        public void onBindViewHolder(@NotNull FriendAdapter.ViewHolder holder, int position) {
            holder.user = userList.get(position);

            Pet pet = holder.user.getPet();
            if (pet == null) {
                holder.user.getTask().addOnCompleteListener(task -> {
                    ProfilePicView pfp = ProfilePicView.smallInstance(OnlineActivity.this, holder.user.getPet());
                    holder.profilePic.addView(pfp);
                });
            } else {
                ProfilePicView pfp = ProfilePicView.smallInstance(OnlineActivity.this, pet);
                holder.profilePic.addView(pfp);
            }

            DocumentReference tmpRef = userData.document(holder.user.getId()).collection("Pet").document("Customisation");
            tmpRef.get().addOnSuccessListener(documentSnapshot -> {
                holder.pet = documentSnapshot.toObject(Pet.class);
                if (holder.pet == null) {
                    holder.pet = Pet.defaultPet();
                    tmpRef.set(holder.pet);
                }
                holder.profilePic.addView(ProfilePicView.smallInstance(OnlineActivity.this, holder.pet));
            });


            String personId = holder.user.getId();
            String level = OnlineActivity.this.getString(R.string.level, holder.user.getLevel());
            holder.levelTextView.setText(level);

            String name = holder.user.getName();
            holder.nameTextView.setText(name);

            if (holder.user.isExpanded()) {
                holder.expandableLayout.setVisibility(View.VISIBLE);
                holder.expandButton.setImageDrawable(AppCompatResources.getDrawable(OnlineActivity.this,
                        R.drawable.ic_baseline_expand_less_24));
            } else {
                holder.expandableLayout.setVisibility(View.GONE);
                holder.expandButton.setImageDrawable(AppCompatResources.getDrawable(OnlineActivity.this,
                        R.drawable.ic_baseline_expand_more_24));
            }

            switch (this.adapterType) {
                case FRIENDS: {
                    FriendViewHolder friendViewHolder = (FriendViewHolder) holder;
                    friendViewHolder.visitButton.setOnClickListener(v -> {
                        Intent intent = new Intent(OnlineActivity.this, VisitFriend.class);
                        intent.putExtra("userId", holder.user.getId());
                        OnlineActivity.this.startActivity(intent);
                        OnlineActivity.this.finish();
                    });

                    friendViewHolder.removeButton.setOnClickListener(v -> {
                        userData.document(currUserId).update("friendsList",
                                FieldValue.arrayRemove(personId));
                        userData.document(personId).update("friendsList",
                                FieldValue.arrayRemove(currUserId));

                        friendsUserList.remove(friendViewHolder.user);
                        friendsAdapter.notifyDataSetChanged();
                    });

                    break;
                }
                case RECEIVED: {
                    ReceivedViewHolder receivedViewHolder = (ReceivedViewHolder) holder;
                    receivedViewHolder.acceptButton.setOnClickListener(v -> {
                        userData.document(currUserId).update("receivedRequests",
                                FieldValue.arrayRemove(personId));
                        userData.document(currUserId).update("friendsList",
                                FieldValue.arrayUnion(personId));

                        userData.document(personId).update("sentRequests",
                                FieldValue.arrayRemove(currUserId));
                        userData.document(personId).update("friendsList",
                                FieldValue.arrayUnion(currUserId));

                        receivedViewHolder.user.setExpanded();
                        friendsUserList.add(receivedViewHolder.user);
                        Collections.sort(friendsUserList);
                        receivedUserList.remove(receivedViewHolder.user);
                        friendsAdapter.notifyDataSetChanged();
                        receivedAdapter.notifyDataSetChanged();
                    });
                    break;
                }
                case SENT: {
                    SentViewHolder sentViewHolder = (SentViewHolder) holder;
                    sentViewHolder.unsendTextView.setOnClickListener(v -> {
                        userData.document(currUserId).update("sentRequests",
                                FieldValue.arrayRemove(personId));

                        userData.document(personId).update("receivedRequests",
                                FieldValue.arrayRemove(currUserId));

                        sentUserList.remove(sentViewHolder.user);
                        sentAdapter.notifyDataSetChanged();
                    });
                    break;
                }
            }
        }

        @Override
        public int getItemCount() { return this.userList.size(); }
    }
}