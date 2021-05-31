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

## Screenshots

### Register Page
![register.txt](https://github.com/weenleen/TamaGoDoWork/files/6565130/register.txt)

### Login Page
![login](https://user-images.githubusercontent.com/69782911/120079990-34c26480-c0e9-11eb-86ac-01749944828a.jpg)

### Settings Page
![settings](https://user-images.githubusercontent.com/69782911/120080005-5e7b8b80-c0e9-11eb-9638-9e16e3524cc9.jpg)

### TaskList Page
![tasklist](https://user-images.githubusercontent.com/69782911/120080010-6a674d80-c0e9-11eb-8142-4264c603f210.jpg)

### Edit Page
![edit](https://user-images.githubusercontent.com/69782911/120080022-7bb05a00-c0e9-11eb-88ad-1877f0cf66ba.jpg)

### DatePicker
![datepicker](https://user-images.githubusercontent.com/69782911/120080050-a7334480-c0e9-11eb-92de-2abb622239cc.jpg)

### TaskDetails Dialog
![edittask](https://user-images.githubusercontent.com/69782911/120080056-b4e8ca00-c0e9-11eb-911d-225c7dd1348a.jpg)

### CreateTasks Page
![createtasks](https://user-images.githubusercontent.com/69782911/120080060-bdd99b80-c0e9-11eb-9c62-cd91af376f1e.jpg)












