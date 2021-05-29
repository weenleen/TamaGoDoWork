# TamaGoDoWork

## Purpose 
TamaGoDoWork is a productivity tool that allows students to set goals, make to-do lists and plan out their weekly schedules. 
Heavily inspired by the virtual pet handheld game [Tamagotchi](https://tamagotchi.com/) and the android app, [Pou](http://www.pou.me/), TamaGoDoWork incorporates a virtual pet companion as part of its main features.
As students check off more daily tasks, they gain experience points which allows their virtual pet to grow.
Additionally, TamaGoDoWork uses cloud storage to store data, so that you can sync your activities across multiple devices!

## User Stories
- As a student, I want to be able to keep track of my schedule and tasks on my phone so that I can remain organized while completing my tasks.
- As a student, I would like to have a visualisation of my progress so that I will be motivated to do more work and complete more tasks.
- As a student, I would like more motivation and incentives to stay on task, and to feel rewarded for my efforts so that I can complete more work. 
- As a student, I would like to lessen the boredom that comes with creating to-do lists and carrying out my tasks, so that I can have fun while completing my work. 

## Specifications
TamaGoDoWork is an application that is built specifically for Android devices only. 
The application is built using [Android Studio](https://developer.android.com/studio) and [Firebase Firestore](https://firebase.google.com/docs/firestore).
The application only supports Android devices with a minimum API level of 22. 

## Current plans
> TamaGoDoWork is still under development. 

Currently, our team has started on the development of the **to-do list** and **login user system** for the application.

Current features include:
- Basic registration and Login functions with email
- A bottom navgiation bar
- Ability to create, edit and delete items in the to-do list
- Setting a name, deadline and description for each item in the list
- Sorting the to-do list items by their deadline
- An experience points (XP) and level up system, where each completion of a task rewards the user with XP.
  Gaining enough XP causes the user to level up.
  
We plan to expand on these features by implementing the following:
- Reset password functionality
- Push notifications for items in the to-do list
- Warning notifications for overdue tasks and grace periods for completing them

In the next phase, our team will work on developing the **virtual pet system**. 

This requires us to: 
- Plan and design different stages of the virtual pet's growth
- Incorporate virtual pet growth into the current XP system
- Develop features for pet interaction and animate pet behaviour
- Add pet customization features to act as rewards for the user's progress

We plan to make use of the OpenGL ES APIs to implement many of the features in the virtual pet system.

## Future plans
Once our team is done developing our virtual pet system, we plan to work on the **weekly planner feature** in our application.

This requires us to:
- Implement a table layout consisting of all the days of the week and various times of the day 
- Implement a scrolling layout to fit all the days and times onto the screen 
- Add dialog views for each period so that students can insert their schedules into the weekly planner 
> We may also add some mini-games or online features into our application if time permits! 


