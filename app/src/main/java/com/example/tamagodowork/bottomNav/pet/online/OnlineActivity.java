package com.example.tamagodowork.bottomNav.pet.online;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.tamagodowork.MainActivity;
import com.example.tamagodowork.R;
import com.example.tamagodowork.authentication.RegisterAct;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class OnlineActivity extends AppCompatActivity {

    private CollectionReference userData;

    private FriendAdapter friendAdapter;

    private CurrentUser currentUser;

    private final List<PetUser> friendsUserList = new ArrayList<>();

    private final List<Task<DocumentSnapshot>> taskList = new ArrayList<>();

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
        String userId = firebaseAuth.getCurrentUser().getUid();
        userData = FirebaseFirestore.getInstance().collection("Users");
        DocumentReference userDoc = userData.document(userId);


        RecyclerView recyclerView = findViewById(R.id.friends_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(OnlineActivity.this));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getApplicationContext(),
                DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);


        userDoc.get().addOnSuccessListener(documentSnapshot -> {
            currentUser = documentSnapshot.toObject(CurrentUser.class);
            if (currentUser == null) return;

            List<String> keyList = currentUser.getFriendsList();
            if (keyList == null) return;

            for (String key : keyList) {
                taskList.add(userData.document(key).get().addOnSuccessListener(documentSnapshot1 -> friendsUserList.add(new PetUser(key,
                        documentSnapshot1.get("Name", String.class),
                        documentSnapshot1.get("XP", Integer.class)))));
            }

            Tasks.whenAllComplete(taskList).addOnCompleteListener(task -> {
                friendAdapter = new FriendAdapter(friendsUserList);
                recyclerView.setAdapter(friendAdapter);
            });
        }).addOnFailureListener(e -> MainActivity.backToMain(OnlineActivity.this));


        Button findFriendsButton = findViewById(R.id.online_find_friends);
        findFriendsButton.setOnClickListener(v -> {
            startActivity(new Intent(OnlineActivity.this, AddFriendActivity.class));
            finish();
        });

        Button homeButton = findViewById(R.id.online_home);
        homeButton.setOnClickListener(v -> MainActivity.backToMain(OnlineActivity.this));
    }


    /**
     * Adapter for the recycler view
     */
    public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.ViewHolder> {

        private final List<PetUser> friendsUserList;

        public FriendAdapter(List<PetUser> friendsUserList) {
            this.friendsUserList = friendsUserList;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            TextView nameTextView;
            ConstraintLayout expandableLayout;
            PetUser user;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                nameTextView = itemView.findViewById(R.id.friend_name_text_view);
                expandableLayout = itemView.findViewById(R.id.friend_expandable_layout);
                expandableLayout.setVisibility(View.GONE);

                nameTextView.setOnClickListener(v -> {
                    if (user == null) return;
                    user.setExpanded();
                    FriendAdapter.this.notifyItemChanged(getAbsoluteAdapterPosition());
                });
            }
        }

        @Override
        public int getItemViewType(int position) { return 0; }

        @NonNull
        @Override
        public FriendAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new FriendAdapter.ViewHolder(
                    LayoutInflater.from(OnlineActivity.this).inflate(R.layout.friend_item, parent, false)
            );
        }

        @Override
        public void onBindViewHolder(@NotNull FriendAdapter.ViewHolder holder, int position) {
            holder.user = friendsUserList.get(position);

            String tmp = OnlineActivity.this.getString(R.string.unlock_level, holder.user.getLevel())
                    + " " + holder.user.getName();
            holder.nameTextView.setText(tmp);
            holder.expandableLayout.setVisibility(holder.user.isExpanded() ? View.VISIBLE : View.GONE);
        }

        @Override
        public int getItemCount() { return this.friendsUserList.size(); }
    }



    /**
     * Class for all the friends and requests.
     */
    private static final class CurrentUser {

        private List<String> friendsList;
        private List<String> sentRequests;
        private List<String> receivedRequests;

        public CurrentUser() {}

        public CurrentUser(List<String> friendsList, List<String> sendRequests, List<String> receivedRequests) {
            this.friendsList = friendsList;
            this.sentRequests = sendRequests;
            this.receivedRequests = receivedRequests;
        }

        public List<String> getFriendsList() { return friendsList; }

        public List<String> getSentRequests() { return sentRequests; }

        public List<String> getReceivedRequests() { return receivedRequests; }
    }
}