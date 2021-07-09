package com.example.tamagodowork.bottomNav.pet.online;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.tamagodowork.MainActivity;
import com.example.tamagodowork.R;
import com.example.tamagodowork.authentication.RegisterAct;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class OnlineActivity extends AppCompatActivity {

    private CollectionReference userData;

    private RecyclerView recyclerView;
    private FriendAdapter friendAdapter;
    private final List<PetUser> friends = new ArrayList<>();

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



        recyclerView = findViewById(R.id.friends_recyclerView);

        userDoc.get().addOnSuccessListener(documentSnapshot -> {
            List<?> friendKeys = documentSnapshot.get("Friends", List.class);
            if (friendKeys == null) return;

            for (Object obj : friendKeys) {
                if (!(obj instanceof String)) continue;
                String key = (String) obj;
                userData.document(key).get().addOnSuccessListener(documentSnapshot1 ->
                        friends.add(new PetUser(
                                key,
                                documentSnapshot1.get("Name", String.class),
                                documentSnapshot1.get("XP", Integer.class))));
            }

            friendAdapter = new FriendAdapter(friends);
            recyclerView.setAdapter(friendAdapter);

        }).addOnFailureListener(e -> MainActivity.backToMain(OnlineActivity.this));



        Button findFriendsButton = findViewById(R.id.online_find_friends);
        findFriendsButton.setOnClickListener(v -> {
            startActivity(new Intent(OnlineActivity.this, AddFriendActivity.class));
            finish();
        });

        Button homeButton = findViewById(R.id.online_home);
        homeButton.setOnClickListener(v -> MainActivity.backToMain(OnlineActivity.this));
    }


    public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.ViewHolder> {

        List<PetUser> friends;

        public FriendAdapter(List<PetUser> friends) {
            this.friends = friends;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
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

        }

        @Override
        public int getItemCount() {
            return this.friends.size();
        }
    }
}